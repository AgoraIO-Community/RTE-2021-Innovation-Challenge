//
//  AllDownloadingViewController.m
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/9/21.
//  Copyright © 2016年 zuyu. All rights reserved.
//
#import "ZFListViewController.h"
#import "ZFDownloadManager.h"
#import "ZFListCell.h"
#import "ZFDownloadViewController.h"
#import "ZFDownloadingCell.h"
#import "DetDownloadingCell.h"
#import "ALLDownloadListCeViewController.h"

#define WIDTH ([UIScreen mainScreen].bounds.size.width)
#define HEIGHT ([UIScreen mainScreen].bounds.size.height)

#import "AllDownloadingViewController.h"

@interface AllDownloadingViewController ()<UITableViewDelegate,UITableViewDataSource,ZFDownloadDelegate>
{
    UIButton *_stopButton;
    UIButton *_goOnButton;
    UITableView *_deleteTableView;

    NSMutableArray *_indexArray;
    
    NSString *_isAllSelected;
    
    NSMutableArray *_indexDataArray;
    
    UIButton *_allXuanButton;
    
    UIButton *_deleteButton;
    
     
}
@property (strong, nonatomic  ) UITableView *tableView;
@property (nonatomic, strong) NSArray       *dataSource;
@property (atomic, strong) NSMutableArray *downloadObjectArr;
@property (nonatomic, strong) ZFDownloadManager *downloadManage;

@end

@implementation AllDownloadingViewController
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    self.view.backgroundColor = [UIColor whiteColor];
    //    self.navigationController.navigationBarHidden = NO;
    //    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleDefault animated:YES];
    // 更新数据源
    
    _indexArray = [NSMutableArray
                   array];
    
    
    _indexDataArray = [NSMutableArray
                       array];
    
    _isAllSelected = @"0";
    [self initData];
   
    [self createButton];
    
    [self createDeleteTableViewAndAllXuanDeteleButton];
    
    [self createTableView];
  
    self.tabBarController.tabBar.hidden = YES;
    
}



#pragma mark - createTableView

-(void)createTableView
{
    
    self.tableView = [[UITableView alloc ] initWithFrame:CGRectMake(0, 121 + 64, WIDTH, HEIGHT - 100 -64) style:UITableViewStylePlain];
    
    _tableView.delegate = self;
    
    _tableView.dataSource = self;
    
    _tableView.rowHeight = 90;
    
    [self.view addSubview:_tableView];
}




#pragma mark - createDeleteTableView And Create allXuan丶 deleteButton
-(void)createDeleteTableViewAndAllXuanDeteleButton
{
    
    
    _deleteTableView = [[UITableView alloc ] initWithFrame:CGRectMake(0, 121 + 64, WIDTH, HEIGHT - 100 -64 - 50) style:UITableViewStylePlain];
    
    _deleteTableView.delegate = self;
    
    _deleteTableView.dataSource = self;
    
    _deleteTableView.hidden = YES;
    
    _deleteTableView.rowHeight = 90;
    
    [self.view addSubview:_deleteTableView];
    
    
    
    _allXuanButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    _allXuanButton.frame = CGRectMake(0, HEIGHT - 60 ,WIDTH/2,60);
    
    [_allXuanButton addTarget:self action:@selector(Allxuan:) forControlEvents:UIControlEventTouchUpInside];
    
    _allXuanButton.hidden = YES;
    
    UIImageView *allImage = [[UIImageView alloc] initWithFrame:CGRectMake(15, 10, WIDTH/2 - 30, 40)];
    
    allImage.image  = [UIImage imageNamed:@"my_down_check_all"];
    
    [_allXuanButton addSubview:allImage];
    
    _allXuanButton.backgroundColor = [UIColor whiteColor];
    
    [self.view addSubview:_allXuanButton];
    
    _deleteButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    _deleteButton.frame = CGRectMake(WIDTH/2, HEIGHT - 60 ,WIDTH/2,60);
    
    [_deleteButton addTarget:self action:@selector(deleteData:) forControlEvents:UIControlEventTouchUpInside];
    
    _deleteButton.hidden = YES;
    
    UIImageView *deleteImage = [[UIImageView alloc] initWithFrame:CGRectMake(15, 10, WIDTH/2 - 30, 40)];
    
    deleteImage.image  = [UIImage imageNamed:@"my_down_delete_all"];
    
    [_deleteButton addSubview:deleteImage];
    
    _deleteButton.backgroundColor = [UIColor whiteColor];
    
    [self.view addSubview:_deleteButton];
    
}
 





#pragma mark - AllXuanAndDelete - selector



-(void)Allxuan:(UIButton *)button
{
    
    
    NSMutableArray * downloadingArray = self.downloadObjectArr[1];
    
    if ([_isAllSelected isEqualToString:@"0"]) {
        
        _isAllSelected = @"1";
        
        [button setTitle:@"取消全选" forState:UIControlStateNormal];
        
        for (int i = 0; i < downloadingArray.count; i++) {
            
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:i inSection:0];
            [_deleteTableView selectRowAtIndexPath:indexPath animated:YES scrollPosition:UITableViewScrollPositionTop];
            
            if ([_indexArray containsObject:indexPath] == NO) {
                
                [_indexArray addObject:indexPath];
                
            }
            if ([_indexDataArray containsObject:downloadingArray[indexPath.row]] == NO) {
                
                [_indexDataArray addObject:downloadingArray[indexPath.row]];
                
            }
            //            _indexDataArray
            
        }
    } else {
        
        _isAllSelected = @"0";
        
        
        [button setTitle:@"全选" forState:UIControlStateNormal];
        
        [_indexArray removeAllObjects];
        
        [_indexDataArray removeAllObjects];
        
        for (int i = 0; i < downloadingArray.count; i++) {
            
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:i inSection:0];
            
            [_deleteTableView deselectRowAtIndexPath:indexPath animated:YES];
        }
    }
     
//    NSLog(@"%@",_indexDataArray);
    
    [_deleteTableView reloadData];
    
}


-(void)deleteData:(UIButton *)button
{
    NSMutableArray * downloadingArray = self.downloadObjectArr[1];
    
    for (ZFHttpRequest *request in  _indexDataArray) {
        
        [self.downloadManage deleteRequest:request];
        
        ZFFileModel *fileInfo = [request.userInfo objectForKey:@"File"];
        
        [self deletePlistWithName:[self decodeString: fileInfo.fileName ]];
        
        [downloadingArray removeObject:request];
        
    }
    
    [_indexDataArray removeAllObjects];
    
    [_indexArray removeAllObjects];
    
    [_deleteTableView reloadData];
    
    [_tableView reloadData];
    
}














#pragma mark - 未下载已下载按钮创建.

-(void)createButton
{
    CGSize sizes = [UIScreen mainScreen].bounds.size;
    
    UIButton *but1  = [UIButton  buttonWithType:UIButtonTypeCustom];
    
    but1.frame = CGRectMake(0, 64, sizes.width/2, 50);
    
    [but1 setImage:[UIImage imageNamed:@"select_down_isok"] forState:UIControlStateNormal];
    
    [but1 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    
    [but1 addTarget:self action:@selector(pushViewController:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:but1];
    
    
    UIButton *but2  = [UIButton  buttonWithType:UIButtonTypeCustom];
    
    but2.frame = CGRectMake(sizes.width/2, 64, sizes.width/2, 50);
    
    [but2 setImage:[UIImage imageNamed:@"downloadingDed"] forState:UIControlStateNormal];
        //
    [but2 setTitleColor:[UIColor orangeColor] forState:UIControlStateNormal];
    
    
    [self.view addSubview:but2];
    
    
    
    _stopButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    _stopButton.frame = CGRectMake(15, 64+54, sizes.width/2 - 30, 50);
    
    [_stopButton setImage:[UIImage imageNamed:@"down_all_stop"] forState:UIControlStateNormal];
    
    _stopButton.hidden = YES;
    
    [_stopButton addTarget:self action:@selector(AllStop) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:_stopButton];
    
    
    
    _goOnButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    _goOnButton.frame = CGRectMake(15, 64+54, sizes.width/2 - 30, 50);
    
    [_goOnButton setImage:[UIImage imageNamed:@"down_all_goon"] forState:UIControlStateNormal];
    
    [_goOnButton addTarget:self action:@selector(AllGoOnDownLoad) forControlEvents:UIControlEventTouchUpInside];

    
    [self.view addSubview:_goOnButton];
    
   
    
    
    UIButton *deleteButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    
    deleteButton.frame = CGRectMake(sizes.width/2 + 15, 64+54, sizes.width/2 - 30, 50);
    
    [deleteButton setImage:[UIImage imageNamed:@"btn_down_delete"] forState:UIControlStateNormal];
    
    [deleteButton addTarget:self action:@selector(deleteDataYa:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:deleteButton];
    
    
    
    UILabel *lionLable = [[UILabel alloc] initWithFrame:CGRectMake(0, 64+110, WIDTH, 1)];
    
    lionLable.backgroundColor =[UIColor lightGrayColor];
    
    [self.view addSubview:lionLable];
    
    
}


-(void)pushViewController:(UIButton *)button
{
    
    
    NSArray *temArray = self.navigationController.viewControllers;
    //
    for(UIViewController *temVC in temArray)
    {
        if ([temVC isKindOfClass:[ALLDownloadListCeViewController class]])
        {
            [self.navigationController popToViewController:temVC animated:NO];
        }
    }
}





#pragma mark - deleteData

-(void)deleteDataYa:(UIButton *) button
{
    
    
    if (_allXuanButton.hidden) {
        
        _tableView.hidden = YES;
        _deleteTableView.hidden = NO;
        _allXuanButton.hidden = NO;
        _deleteButton.hidden = NO;
        
    }else{
        
        
        _tableView.hidden = NO;
        _deleteTableView.hidden = YES;
        _allXuanButton.hidden = YES;
        _deleteButton.hidden = YES;
        
    }
    NSLog(@"get into deleteDate state");
}


#pragma mark - system

- (void)viewDidLoad {
    [super viewDidLoad];
    self.tableView.tableFooterView = [UIView new];
    self.tableView.contentInset = UIEdgeInsetsMake(0, 0, -49, 0);
    self.tableView.rowHeight = 70;
    self.downloadManage.downloadDelegate = self;
    //NSLog(@"%@", NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES));
}


#pragma mark - requestData
- (void)initData
{
    [self.downloadManage startLoad];
    NSMutableArray *downladed = self.downloadManage.finishedlist;
    NSMutableArray *downloading = self.downloadManage.downinglist;

    self.downloadObjectArr = @[].mutableCopy;
    [self.downloadObjectArr addObject:downladed];
    [self.downloadObjectArr addObject:downloading];
    
    [self.tableView reloadData];
}

- (ZFDownloadManager *)downloadManage
{
    if (!_downloadManage) {
        _downloadManage = [ZFDownloadManager sharedDownloadManager];
    }
    return _downloadManage;
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
        NSArray *sectionArray = self.downloadObjectArr[1];
        return sectionArray.count;
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (tableView == _tableView) {
        ZFDownloadingCell *cell = [tableView dequeueReusableCellWithIdentifier:@"downloadingCell"];
        
        if (cell == nil) {
            cell = [[ZFDownloadingCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"downloadingCell"];
        }
        
        ZFHttpRequest *request = self.downloadObjectArr[1][indexPath.row];
        if (request == nil) { return nil; }
        
        ZFFileModel *fileInfo = [request.userInfo objectForKey:@"File"];
        
        __weak typeof(self) weakSelf = self;
        
        // 下载按钮点击时候的要刷新列表
        
        cell.btnClickBlock = ^{
            [weakSelf initData];
            
        };
        // 下载模型赋值
        cell.fileInfo = fileInfo;
        // 下载的request
        cell.request = request;
        return cell;

    }else{
    
        DetDownloadingCell *cell = [tableView dequeueReusableCellWithIdentifier:@"detdownloadingCell"];
        
        if (cell == nil) {
            cell = [[DetDownloadingCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"detdownloadingCell"];
        }
        
        ZFHttpRequest *request = self.downloadObjectArr[1][indexPath.row];
        if (request == nil) { return nil; }
        
        ZFFileModel *fileInfo = [request.userInfo objectForKey:@"File"];
        
        __weak typeof(self) weakSelf = self;
        // 下载按钮点击时候的要刷新列表
        cell.btnClickBlock = ^{
            [weakSelf initData];
            
        };
        // 下载模型赋值
        cell.fileInfo = fileInfo;
        // 下载的request
        cell.request = request;
        
        
        if ([_indexArray containsObject:indexPath]) {
            
            cell.isXuanImage.image = [UIImage imageNamed:@"downlistYes.jpg"];
            
        }else{
            
            cell.isXuanImage.image = [UIImage imageNamed:@"downlistNo.jpg"];
            
        }
        return cell;
    
    }
    
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (tableView == _tableView) {
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
        
    }else{
        
        if ([_indexArray containsObject:indexPath]) {
            
            [_indexArray removeObject:indexPath];
            
        }else{
            
            [_indexArray addObject:indexPath];
            
        }
        
        if ([_indexDataArray containsObject:self.downloadObjectArr[1][indexPath.row]]) {
            
            [_indexDataArray removeObject:self.downloadObjectArr[1][indexPath.row]];
            
        }else{
            
            [_indexDataArray addObject:self.downloadObjectArr[1][indexPath.row]];
            
        }
        
        [_deleteTableView reloadData];
    }
    
}

- (NSString *)tableView:(UITableView *)tableView titleForDeleteConfirmationButtonForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return @"删除";
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}

-(void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    NSMutableArray *downLoadingArr = self.downloadObjectArr[1];
    
    ZFHttpRequest *request = downLoadingArr[indexPath.row];
    
    [downLoadingArr removeObject:request];
    
    [self.downloadManage deleteRequest:request];
    
    
    ZFFileModel *fileInfo = [request.userInfo objectForKey:@"File"];
    
    
    [self deletePlistWithName:[self decodeString: fileInfo.fileName ]];
    
    [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationBottom];
    
    [_tableView reloadData];
    
    
}






#pragma mark - deletePlist


-(void)deletePlistWithName:(NSString *)name
{
    NSArray *patharray = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    
    NSString *path =  [patharray objectAtIndex:0];
    
    NSString *filepath=[path stringByAppendingPathComponent:@"downtest4.plist"];
    
    NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithContentsOfFile:filepath];
    
    [dic removeObjectForKey:name];
    
    [dic writeToFile:filepath atomically:YES];
    
    NSLog(@"%@",dic);
    
}

#pragma mark - ZFDownloadDelegate

// 开始下载
- (void)startDownload:(ZFHttpRequest *)request
{
    NSLog(@"开始下载!");
}

// 下载中
- (void)updateCellProgress:(ZFHttpRequest *)request
{
    ZFFileModel *fileInfo = [request.userInfo objectForKey:@"File"];
    [self performSelectorOnMainThread:@selector(updateCellOnMainThread:) withObject:fileInfo waitUntilDone:YES];
}

// 下载完成
- (void)finishedDownload:(ZFHttpRequest *)request
{
    [self initData];
}

// 更新下载进度
- (void)updateCellOnMainThread:(ZFFileModel *)fileInfo
{
    NSArray *cellArr = [self.tableView visibleCells];
    for (id obj in cellArr) {
        if([obj isKindOfClass:[ZFDownloadingCell class]]) {
            ZFDownloadingCell *cell = (ZFDownloadingCell *)obj;
            if([cell.fileInfo.fileURL isEqualToString:fileInfo.fileURL]) {
                cell.fileInfo = fileInfo;
            }
        }
    }
}



#pragma mark - decodeURI
-(NSString *)decodeString:(NSString*)encodedString

{
    //NSString *decodedString = [encodedString stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding ];
    
    NSString *decodedString  = (__bridge_transfer NSString *)CFURLCreateStringByReplacingPercentEscapesUsingEncoding(NULL,
                                                                                                                     (__bridge CFStringRef)encodedString,
                                                                                                                     CFSTR(""),
                                                                                                                     CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding));
    return decodedString;
}/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
