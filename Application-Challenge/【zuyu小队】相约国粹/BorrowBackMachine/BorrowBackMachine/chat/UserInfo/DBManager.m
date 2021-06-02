//
//  DBManager.m
//  EaseIM
//
//  Created by lixiaoming on 2021/3/29.
//  Copyright © 2021 lixiaoming. All rights reserved.
//

#import "DBManager.h"
#import <FMDB/FMDB.h>

@interface DBManager ()
@property (nonatomic,strong) NSMutableArray* usersInfoArray;
@property (nonatomic,strong) dispatch_queue_t workQueue;
@property (atomic,strong) NSLock* lock;
@property (nonatomic) NSTimeInterval timeOutInterval;
@property (nonatomic,strong) FMDatabase* database;
@end

static DBManager *databaseManager = nil;
@implementation DBManager

+(instancetype) sharedInstance
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        databaseManager = [[DBManager alloc] init];
        databaseManager.lock = [[NSLock alloc] init];
        databaseManager.workQueue = dispatch_queue_create("demo.DBManager", DISPATCH_QUEUE_SERIAL);
        databaseManager.timeOutInterval = 7*24*3600;
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

-(void) addUserInfos:(NSArray<EMUserInfo*>*)aUserInfos
{
    [self.lock lock];
    [self.usersInfoArray addObjectsFromArray:aUserInfos];
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
    if([EMClient sharedClient].options.appkey.length > 0) {
        NSString*appkeyPath = [path stringByAppendingPathComponent:[EMClient sharedClient].options.appkey];
        [self createPath:appkeyPath];
        if([EMClient sharedClient].currentUsername.length > 0) {
            NSString* userPath = [appkeyPath stringByAppendingPathComponent:[EMClient sharedClient].currentUsername];
            [self createPath:userPath];
            
            NSString *dbfile = [userPath stringByAppendingFormat:@"/userinfo.db"];
            self.database = [[FMDatabase alloc] initWithPath:dbfile];
            if([self.database open]) {
                [self createUserInfoTable];
                [self deleteOutDateData];
            }
            else {
                NSLog(@"Failed to open/create database");
            }
        }
    }
}

-(void) createUserInfoTable
{
    NSString *sql_stmt =
    @"create table if not exists userinfotable (userid text PRIMARY KEY     NOT NULL, nickname text, avarturl text, sign text,gender integer,mail text,phone text,ext text,birth text,timestamp integer)";
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
        NSString* stmt = @"replace into userinfotable (userid,nickname,avarturl,sign,gender,mail,phone,ext,birth,timestamp) values ";
        BOOL bFirst = YES;
        [self.lock lock];
        NSString*(^format)(NSString*) = ^(NSString* str) {
            return str.length>0?str:@"";
        };
        NSDate* now = [NSDate date];
        NSInteger ts = [now timeIntervalSince1970];
        for(EMUserInfo* userInfo in self.usersInfoArray) {
            if(bFirst) {
                bFirst = NO;
                stmt = [stmt stringByAppendingFormat:@"('%@','%@','%@','%@',%ld,'%@','%@','%@','%@',%ld)",format(userInfo.userId),format(userInfo.nickName),format(userInfo.avatarUrl),format(userInfo.sign),userInfo.gender,format(userInfo.mail),format(userInfo.phone),format(userInfo.ext),format(userInfo.birth),ts];
            }else{
                stmt = [stmt stringByAppendingFormat:@",('%@','%@','%@','%@',%ld,'%@','%@','%@','%@',%ld)",userInfo.userId,userInfo.nickName,userInfo.avatarUrl,userInfo.sign,userInfo.gender,userInfo.mail,userInfo.phone,userInfo.ext,userInfo.birth,ts];
            }
        }
        [self.usersInfoArray removeAllObjects];
        [self.lock unlock];
        if ([self.database executeUpdate:stmt])
        {
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
        NSString* stmt = [NSString stringWithFormat:@"delete from userinfotable where timestamp < %ld",ts];
        if ([self.database executeUpdate:stmt]) {
            NSLog(@"delete timeout data success");
        }else{
            NSLog(@"delete timeout data fail");
        }
    }
}
-(NSArray<EMUserInfo*>*) loadUserInfos
{
    NSMutableArray* array = [NSMutableArray array];
    if([self.database isOpen]) {
        NSString* stmt = @"select userid,nickname,avarturl,sign,gender,mail,phone,ext,birth from userinfotable ";
        FMResultSet *result = [self.database executeQuery:stmt];
        while ([result next])
        {
            EMUserInfo* userInfo = [[EMUserInfo alloc] init];
            userInfo.userId = [result stringForColumnIndex:0];
            userInfo.nickName = [result stringForColumnIndex:1];
            userInfo.avatarUrl = [result stringForColumnIndex:2];
            userInfo.sign = [result stringForColumnIndex:3];
            userInfo.gender = [result intForColumnIndex:4];
            userInfo.mail = [result stringForColumnIndex:5];
            userInfo.phone = [result stringForColumnIndex:6];
            userInfo.ext = [result stringForColumnIndex:7];
            userInfo.birth = [result stringForColumnIndex:8];
            [array addObject:userInfo];
        }
    }
    return array;
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
