//
//  TMyDownloadViewController.m
//  CNCLibraryScan
//
//  Created by zuyu on 17/9/10.
//  Copyright © 2017年 zuyu. All rights reserved.
//

#import "TMyDownloadViewController.h"
#import "ZFDownloadingCell.h"
#import "ZFDownloadedCell.h"
#import "ZFListViewController.h"
#import "LocalMP3PlayerViewController.h"
#import "DownLoadMp3ViewController.h"
#import "LocalPDFViewController.h"
#import "zuyu.h"


@interface TMyDownloadViewController ()<NavgationViewDelegate,UITableViewDelegate,UITableViewDataSource>
@property (strong, nonatomic) UITableView *tableView;

@property (atomic, strong) NSMutableArray *downloadObjectArr;

@property (nonatomic, strong) ZFDownloadManager *downloadManage;
@end

@implementation TMyDownloadViewController


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:YES animated:NO];
    self.view.backgroundColor = [UIColor whiteColor];
    //    self.navigationController.navigationBarHidden = NO;
    //    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleDefault animated:YES];
    // 更新数据源
    
    
    [self initData];
    
    [self createNavgation];
    
    [self createButton];
    
    [self createTableView];
    
    
    self.tabBarController.tabBar.hidden = YES;
    
    
    NSLog(@"%@",self.bookName);
    
}

#pragma mark - navgation
-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.titleStr = @"我的下载";
    [self.view addSubview:view];
}


-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}


- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:NO];

    
}

-(void)createTableView
{
    self.tableView = [[UITableView alloc ] initWithFrame:CGRectMake(0, 130, WIDTH, HEIGHT - 130 -74) style:UITableViewStylePlain];
    
    _tableView.delegate = self;
    
    _tableView.dataSource = self;
    
    _tableView.rowHeight = 90;
    
    
    [self.view addSubview:_tableView];
}

#pragma mark - 未下载已下载按钮创建.

-(void)createButton
{
    CGSize sizes = [UIScreen mainScreen].bounds.size;
    
    UIButton *but1  = [UIButton  buttonWithType:UIButtonTypeCustom];
    
    but1.frame = CGRectMake(0, 74, sizes.width/2, 50);
    
    [but1 setImage:[UIImage imageNamed:@"select_down_ok"] forState:UIControlStateNormal];
    
    [but1 setTitleColor:[UIColor orangeColor] forState:UIControlStateNormal];
    
    [self.view addSubview:but1];
    
    UIButton *but2  = [UIButton  buttonWithType:UIButtonTypeCustom];
    
    but2.frame = CGRectMake(sizes.width/2, 74, sizes.width/2, 50);
    //    
    [but2 setImage:[UIImage imageNamed:@"downloading"] forState:UIControlStateNormal];
    
    [but2 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    
    [but2 addTarget:self action:@selector(pushViewController:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:but2];
    
}

-(void)pushViewController:(UIButton *)button
{
    
    
    ZFListViewController *tw = [[ZFListViewController alloc] init];
    
    [self.navigationController pushViewController:tw animated:NO];
    
    
}






- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = @"我的下载";
    self.tableView.tableFooterView = [UIView new];
    self.tableView.contentInset = UIEdgeInsetsMake(0, 0, -49, 0);
    self.tableView.rowHeight = 70;
    self.downloadManage.downloadDelegate = self;
    
}

- (void)initData
{
    
    
    
    [self.downloadManage startLoad];
    
    
    NSMutableArray *downladed = self.downloadManage.finishedlist;
    NSMutableArray *downloading = self.downloadManage.downinglist;
    
    NSMutableArray *downladedClass = @[].mutableCopy;
    
    for ( ZFFileModel *fileInfo in downladed) {
        
        if ([fileInfo.bookName isEqualToString:self.bookName]) {
            [downladedClass addObject:fileInfo];
        }
        
        //
    }
    
    self.downloadObjectArr = @[].mutableCopy;
    
    [self.downloadObjectArr addObject:downladedClass];
    
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
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    if (section) {
        return 0;
    }
    NSArray *sectionArray = self.downloadObjectArr[section];
    return sectionArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        ZFDownloadedCell *cell = [tableView dequeueReusableCellWithIdentifier:@"downloadedCell"];
        
        if (cell == nil) {
            cell = [[ZFDownloadedCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"downloadedCell"];
        }
        
        ZFFileModel *fileInfo = self.downloadObjectArr[indexPath.section][indexPath.row];
        cell.fileInfo = fileInfo;
        
        if ([self.type isEqualToString:@"22"]||
            [self.type isEqualToString:@"77"]||
            [self.type isEqualToString:@"99"]||
            [self.type isEqualToString:@"79"]){
            
            cell.fileNameLabel.text = fileInfo.bookName;
            
        }
        
        return cell;
    } else if (indexPath.section == 1) {
        ZFDownloadingCell *cell = [tableView dequeueReusableCellWithIdentifier:@"downloadingCell"];
        
        if (cell == nil) {
            cell = [[ZFDownloadingCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"downloadingCell"];
        }
        
        ZFHttpRequest *request = self.downloadObjectArr[indexPath.section][indexPath.row];
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
    }
    
    return nil;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    NSMutableArray *urlArr = @[].mutableCopy;
    NSMutableArray *nameArr = @[].mutableCopy;
    
    
    for (ZFFileModel *fileInfo in self.downloadObjectArr[indexPath.section]) {
        
        [urlArr addObject:FILE_PATH(fileInfo.fileName)];
        [nameArr addObject:fileInfo.fileName];
    }
    
    ZFFileModel *fileInfo = self.downloadObjectArr[indexPath.section][indexPath.row];
    
    NSString *string =  FILE_PATH(fileInfo.fileName);
    
    NSString *typeString = [string substringFromIndex:string.length-3];
    
     
    if ([typeString isEqualToString:@"mp3"] || [typeString isEqualToString:@"drm"]) {
        
        LocalMP3PlayerViewController *vc = [[LocalMP3PlayerViewController alloc] init];
        
        [vc setHidesBottomBarWhenPushed:YES];
        
        vc.URLArray = urlArr;
        
        vc.indexRow = indexPath.row;
        
        vc.nameArray = nameArr;
        
        [self.navigationController pushViewController:vc animated:YES];
        
    }else if ([typeString isEqualToString:@"pdf"]) {
        
        LocalPDFViewController *vc = [[LocalPDFViewController alloc] init];
        
        [vc setHidesBottomBarWhenPushed:YES];
        
        vc.urlString = urlArr[0];
        vc.name = nameArr[0];

        [self.navigationController pushViewController:vc animated:YES];
        
    }else{
        
        DownLoadMp3ViewController *vc = [[DownLoadMp3ViewController alloc] init];
        
        vc.dataArray = urlArr;
        
        vc.nameArray = nameArr;
        
        vc.chooseID = indexPath.row;
        
        [vc setHidesBottomBarWhenPushed:YES];

        [self.navigationController pushViewController:vc animated:YES];
        
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
    
    UIAlertView *alert = [UIAlertView showWithTitle:@"是否确定删除该资源" message:@"" cancelButtonTitle:@"取消" otherButtonTitles:@[@"删除"] tapBlock:^(UIAlertView * _Nonnull alertView, NSInteger buttonIndex) {
        if (buttonIndex) {
            
            NSMutableArray *downLoadArray = self.downloadObjectArr[indexPath.section];
            
            ZFFileModel *fileInfo = downLoadArray[indexPath.row];
            
            [self.downloadManage deleteFinishFile:fileInfo];
            
            [downLoadArray removeObject:fileInfo];
            
            [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationBottom];
        }
    }];
    
}

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


-(void)UIBarButtonBackItemSet
{
    UIBarButtonItem*btn_back = [[UIBarButtonItem alloc]init];
    
    btn_back.title = @"";
    
    self.navigationItem.backBarButtonItem= btn_back;
    
    self.navigationController.navigationBar.barStyle = UIStatusBarStyleDefault;
    
    [self.navigationController.navigationBar setTintColor:[UIColor whiteColor]];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
