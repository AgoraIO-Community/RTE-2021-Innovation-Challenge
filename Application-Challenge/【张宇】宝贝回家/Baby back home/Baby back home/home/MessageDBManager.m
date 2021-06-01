//
//  MessageDBManager.m
//  Baby back home
//
//  Created by zhangyu on 2021/5/14.
//

#import "MessageDBManager.h"
#import <FMDB/FMDB.h>

@interface MessageDBManager ()
@property (nonatomic,strong) NSMutableArray* usersInfoArray;
@property (nonatomic,strong) dispatch_queue_t workQueue;
@property (atomic,strong) NSLock* lock;
@property (nonatomic) NSTimeInterval timeOutInterval;
@property (nonatomic,strong) FMDatabase* database;


@end

static MessageDBManager *databaseManager = nil;


@implementation MessageDBManager

+(instancetype) sharedInstance
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        databaseManager = [[MessageDBManager alloc] init];
        databaseManager.lock = [[NSLock alloc] init];
        databaseManager.workQueue = dispatch_queue_create("RTC.MessageDBManager", DISPATCH_QUEUE_SERIAL);
        databaseManager.timeOutInterval = 30*24*3600;
        [databaseManager initDB];
    });
    return databaseManager;
}

- (void)initDB
{
    [self openDB];
}

- (NSMutableArray*)usersInfoArray
{
    if(!_usersInfoArray) {
        _usersInfoArray = [NSMutableArray array];
    }
    return _usersInfoArray;
}

-(void) addMessage:(NSArray<BBMessageModel*>*)aMessages;
{
    [self.lock lock];
    [self.usersInfoArray addObjectsFromArray:aMessages];
    [self.lock unlock];
    __weak typeof(self) weakself = self;
    dispatch_async(self.workQueue, ^{
        [weakself saveUserInfos];
    });
}

#pragma mark - fmdb

-(void) openDB
{
    NSString* path = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES).firstObject;
    [self createPath:path];
    NSString *dbfile = [path stringByAppendingFormat:@"/messagelist.db"];
    self.database = [[FMDatabase alloc] initWithPath:dbfile];
    if([self.database open]) {
        [self createUserInfoTable];
//        [self deleteOutDateData];
    }
    else {
        NSLog(@"Failed to open/create database");
    }
//    if([EMClient sharedClient].options.appkey.length > 0) {
//        NSString*appkeyPath = [path stringByAppendingPathComponent:[EMClient sharedClient].options.appkey];
//        [self createPath:appkeyPath];
//        if([EMClient sharedClient].currentUsername.length > 0) {
//            NSString* userPath = [appkeyPath stringByAppendingPathComponent:[EMClient sharedClient].currentUsername];
//            [self createPath:userPath];
//            
//            NSString *dbfile = [userPath stringByAppendingFormat:@"/messagelist.db"];
//            self.database = [[FMDatabase alloc] initWithPath:dbfile];
//            if([self.database open]) {
//                [self createUserInfoTable];
//                [self deleteOutDateData];
//            }
//            else {
//                NSLog(@"Failed to open/create database");
//            }
//        }
//    }
}

-(void) createUserInfoTable
{
    NSString *sql_stmt =
    @"create table if not exists messagelisttable (messageid text PRIMARY KEY     NOT NULL, title text, des text, address text,phoneNum text,imgName text,timestamp integer,name text,gender text,userid text)";
    if([self.database executeUpdate:sql_stmt])
    {
        NSLog(@"Create table success");
    }else{
        NSLog(@"Failed to create table");
    }
}

-(void) closeDB
{
    if(![self.database isOpen]) {
        [self.database close];
    }
}
-(void) saveUserInfos
{
    if([self.database isOpen] && self.usersInfoArray.count > 0) {
        NSString* stmt = @"INSERT into messagelisttable (messageid,title,des,address,phoneNum,imgName,timestamp,name,gender,userid) values ";
//        BOOL bFirst = YES;
        [self.lock lock];
        
        
        NSNumber *maxID = @(0);
        
        FMResultSet *res = [self.database executeQuery:@"SELECT * FROM messagelisttable"];
        //获取数据库中最大的ID
        while ([res next]) {
            if ([maxID integerValue] < [[res stringForColumn:@"messageid"] integerValue]) {
                maxID = @([[res stringForColumn:@"messageid"] integerValue] ) ;
            }
            
        }
        maxID = @([maxID integerValue] + 1);
        
        NSString*(^format)(NSString*) = ^(NSString* str) {
            return str.length>0?str:@"";
        };
        NSDate* now = [NSDate date];
        NSInteger ts = [now timeIntervalSince1970];
        BBMessageModel* userInfo = self.usersInfoArray.firstObject;
        stmt = [stmt stringByAppendingFormat:@"('%@','%@','%@','%@','%@','%@',%ld,'%@','%@',%@)",format([NSString  stringWithFormat:@"%@",maxID]),format(userInfo.title),format(userInfo.des),format(userInfo.address),userInfo.phoneNum,format(userInfo.imgName),ts,userInfo.pName,userInfo.pGender,userInfo.userid];
//        for(BBMessageModel* userInfo in self.usersInfoArray) {
//            if(bFirst) {
//                bFirst = NO;
//                stmt = [stmt stringByAppendingFormat:@"('%@','%@','%@','%@','%@','%@',%ld)",format(userInfo.messageid),format(userInfo.title),format(userInfo.des),format(userInfo.address),userInfo.phoneNum,format(userInfo.imgName),ts];
//            }else{
//                stmt = [stmt stringByAppendingFormat:@"('%@','%@','%@','%@',%@,'%@',%ld)",userInfo.messageid,userInfo.title,userInfo.des,userInfo.address,userInfo.phoneNum,userInfo.imgName,ts];
//            }
//        }
        [self.usersInfoArray removeAllObjects];
        [self.lock unlock];
        if ([self.database executeUpdate:stmt])
        {

            [[NSNotificationCenter defaultCenter] postNotificationName:@"insertDBsuccess" object:nil];
            NSLog(@"insert success");
        }
        else {
            NSLog(@"insert fail");
        }
    }
}

-(void)deleteOutDateData
{
    if([self.database isOpen]) {
        NSDate* now = [NSDate date];
        NSInteger ts = [now timeIntervalSince1970] - self.timeOutInterval;
        NSString* stmt = [NSString stringWithFormat:@"delete from messagelisttable where timestamp < %ld",ts];
        if ([self.database executeUpdate:stmt]) {
            NSLog(@"delete timeout data success");
        }else{
            NSLog(@"delete timeout data fail");
        }
    }
}
-(NSArray<BBMessageModel*>*) loadMessages
{
    NSMutableArray* array = [NSMutableArray array];
    if([self.database isOpen]) {
        NSString* stmt = @"select messageid,title,des,address,phoneNum,imgName,timestamp,name,gender,userid from messagelisttable ";
        FMResultSet *result = [self.database executeQuery:stmt];
        while ([result next])
        {
            BBMessageModel* userInfo = [[BBMessageModel alloc] init];
            userInfo.messageid = [result stringForColumnIndex:0];
            userInfo.title = [result stringForColumnIndex:1];
            userInfo.des = [result stringForColumnIndex:2];
            userInfo.address = [result stringForColumnIndex:3];
            userInfo.phoneNum = [result stringForColumnIndex:4];
            userInfo.imgName = [result stringForColumnIndex:5];
            userInfo.timestamp = [result intForColumnIndex:6];
            userInfo.pName = [result stringForColumnIndex:7];
            userInfo.pGender = [result stringForColumnIndex:8];
            userInfo.userid = [result stringForColumnIndex:9];
            [array addObject:userInfo];
        }
    }
    return array;
}
//获取沙盒图片
- (UIImage *)getCacheImageUseImagePath:(NSString *)imagePath
{
    //防止每次启动程序沙盒前缀地址改变,只存储后边文件路径,调用时再次拼接
    NSString *savePath= [NSString stringWithFormat:@"Documents/%@.png",imagePath];
    NSString *imageAllPath = [NSHomeDirectory() stringByAppendingPathComponent:savePath];
    return [UIImage imageWithContentsOfFile:imageAllPath];
}

#pragma mark - common

-(BOOL) createPath:(NSString*)pFold
{
    BOOL isDir = NO;
    NSFileManager *fileManager = [NSFileManager defaultManager];
    BOOL existed = [fileManager fileExistsAtPath:pFold isDirectory:&isDir];
    if ( !(isDir == YES && existed == YES) )
    {
        return [fileManager createDirectoryAtPath:pFold withIntermediateDirectories:YES attributes:nil error:nil];
    }
    return YES;
}
@end
