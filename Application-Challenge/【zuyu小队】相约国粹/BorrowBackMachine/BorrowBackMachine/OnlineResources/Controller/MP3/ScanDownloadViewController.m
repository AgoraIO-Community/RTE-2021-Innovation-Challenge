//
//  ScanDownloadViewController.m
//  CNCLibraryScan
//
//  Created by zuyu on 2017/9/12.
//  Copyright © 2017年 zuyu. All rights reserved.
//

#import "ScanDownloadViewController.h"
#import "DownloadCeModel.h"
#import "DownListTableViewCell.h"
#import "ZFDownloadManager.h"
#import "zuyu.h"
#import "ZFDownloadViewController.h"
@interface ScanDownloadViewController ()<UITableViewDataSource,UITableViewDelegate>
{
    NSMutableArray *_dataSouceArray;
    
    UITableView *_tableView;
    
    NSString *_isAllSelected;
    
    NSMutableArray *_indexArray;
    
    
}
@end

@implementation ScanDownloadViewController

- (void)viewDidLoad {
    
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    [self initClass];
    
    [self initData];
    
    [self createNavItme];
    
    [self createTable];
    
    [self createButton];
    
    [self UIBarButtonBackItemSet];
    //    [self createTable];
    // Do any additional setup after loading the view.
}


-(void)initClass
{
    
    self.title = @"下载";
    
    _dataSouceArray = [NSMutableArray array];
    
    _indexArray     = [NSMutableArray array];
    
    _isAllSelected  = @"0";
}



-(void)initData
{
    
    
    for (NSDictionary *dict in _dataArray) {
        
        DownloadCeModel *model  = [[DownloadCeModel alloc] init];
        
        model.BookID        = [NSString stringWithFormat:@"%@",[dict objectForKey:@"BookID"]];
        
        model.BookName      = [NSString stringWithFormat:@"%@",[dict objectForKey:@"BookName"]];
        
        model.BookType      = [NSString stringWithFormat:@"%@",[dict objectForKey:@"BookType"]];
        model.CoverImageUrl = [NSString stringWithFormat:@"%@",[dict objectForKey:@"CoverImageUrl"]];
        
        model.HasContent    = [NSString stringWithFormat:@"%@",[dict objectForKey:@"HasContent"]];
        
        model.ID            = [NSString stringWithFormat:@"%@",[dict objectForKey:@"ID"]];
        
        model.Name          = [NSString stringWithFormat:@"%@",[dict objectForKey:@"Name"]];
        
        model.ResourceDuration = [NSString stringWithFormat:@"%@",[dict objectForKey:@"ResourceDuration"]];
        
        model.ResourceSize  = [NSString stringWithFormat:@"%@",[dict objectForKey:@"ResourceSize"]];
        
        model.ResourceType  = [NSString stringWithFormat:@"%@",[dict objectForKey:@"ResourceType"]];
        
        model.ResourceUrl   = [NSString stringWithFormat:@"%@",[dict objectForKey:@"ResourceUrl"]];
        
        model.ViewCount     = [NSString stringWithFormat:@"%@",[dict objectForKey:@"ViewCount"]];
        
        model.VolumnNo      = [NSString stringWithFormat:@"%@",[dict objectForKey:@"VolumnNo"]];
        
        [_dataSouceArray addObject:model];
        
    }
    
    
}


#pragma mark - createNavItme

-(void)createNavItme
{
    UIBarButtonItem *barItem1 = [[UIBarButtonItem alloc]
                                 
                                 initWithImage:[[UIImage imageNamed:@"down_list_into_img"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]
                                 
                                 style:UIBarButtonItemStylePlain
                                 
                                 target:self
                                 
                                 action:@selector(downLoadAllList:)];
    
    NSArray *arr1=[[NSArray alloc]initWithObjects:barItem1, nil];
    
    
    self.navigationItem.rightBarButtonItems = arr1;
    
    
    
}

-(void)downLoadAllList:(UIBarButtonItem *)item
{
    ZFDownloadViewController *vc = [[ZFDownloadViewController alloc] init];
    
    [vc setHidesBottomBarWhenPushed:YES];
    
    [self.navigationController pushViewController:vc animated:YES];
}



#pragma mark - createTableview

-(void)createTable
{
    _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, HEIGHT - 50) style:UITableViewStylePlain];
    
    _tableView.dataSource = self;
    
    _tableView.delegate  = self;
    
    [self.view addSubview:_tableView];
    
}


#pragma mark - 创建全选.下载按钮

-(void)createButton
{
    CGSize sizes = [UIScreen mainScreen].bounds.size;
    
    UIButton *but1  = [UIButton  buttonWithType:UIButtonTypeCustom];
    //
    but1.frame = CGRectMake(0, sizes.height - 50, sizes.width/2, 50);
    
    //    [but1 setTitle:@"全选" forState:UIControlStateNormal];
    
    UIImageView *imageV = [[UIImageView alloc] initWithFrame:CGRectMake(15,  5 , WIDTH/2 - 30, 40)];
    
    imageV.image = [UIImage imageNamed:@"quanxuan"];
    
    [but1 addSubview:imageV];
    
    [but1 setTitleColor:[UIColor orangeColor] forState:UIControlStateNormal];
    
    [but1 addTarget:self action:@selector(allXuan:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:but1];
    
    UIButton *but2  = [UIButton  buttonWithType:UIButtonTypeCustom];
    //
    but2.frame = CGRectMake(sizes.width/2, sizes.height - 50, sizes.width/2, 50);
    
    UIImageView *imageVv = [[UIImageView alloc] initWithFrame:CGRectMake(15,  5 , WIDTH/2 - 30, 40)];
    
    imageVv.image = [UIImage imageNamed:@"xiazai"];
    
    [but2 addSubview:imageVv];
    
    [but2 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    //
    [but2 addTarget:self action:@selector(downLoad:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:but2];
    
    
}



#pragma mark - 全选
-(void)allXuan:(UIButton *)button
{
    
    if ([_isAllSelected isEqualToString:@"0"]) {
        
        _isAllSelected = @"1";
        
        //        [button setTitle:@"取消全选" forState:UIControlStateNormal];
        
        for (int i = 0; i < _dataSouceArray.count; i++) {
            
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:i inSection:0];
            [_tableView selectRowAtIndexPath:indexPath animated:YES scrollPosition:UITableViewScrollPositionTop];
            
            
            if ([_indexArray containsObject:indexPath] == NO) {
                
                [_indexArray addObject:indexPath];
                
            }
            
        }
    } else {
        
        _isAllSelected = @"0";
        
        [_indexArray removeAllObjects];
        
        for (int i = 0; i < _dataSouceArray.count; i++) {
            
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:i inSection:0];
            
            [_tableView deselectRowAtIndexPath:indexPath animated:YES];
        }
    }
    
    [_tableView reloadData];
}


#pragma mark - 下载
-(void)downLoad :(UIButton *)button
{
    
    
    if ([ISLOGIN integerValue]) {
        
        NSMutableArray *arrayA = _indexArray;
        
        
        ZFDownloadManager *downManger = [ZFDownloadManager sharedDownloadManager];
        
        for (int i = 0; i<arrayA.count; i++) {
            
            NSIndexPath *indexPath = arrayA[i];
            
            DownloadCeModel *model = _dataSouceArray[indexPath.row];
            
            
            NSString *bookCeName  =  [[[NSString stringWithFormat:@"%@",model.ResourceUrl] componentsSeparatedByString:@"/"] lastObject];
            
            NSString *typeString = [bookCeName substringFromIndex:bookCeName.length-3];
            
            if ( [typeString isEqualToString:@"drm"]) {
                
                bookCeName = [bookCeName substringToIndex:bookCeName.length-3];
                
                bookCeName = [NSString stringWithFormat:@"%@mp3",bookCeName];
            }
            
            if ( [typeString isEqualToString:@"swf"]) {
                
                bookCeName = [bookCeName substringToIndex:bookCeName.length-3];
                
                bookCeName = [NSString stringWithFormat:@"%@mp4",bookCeName];
            }
            
            
            if ([self.classID isEqualToString:@"22"]||
                [self.classID isEqualToString:@"77"]||
                [self.classID isEqualToString:@"99"]||
                [self.classID isEqualToString:@"79"]) {
                
                [downManger downFileUrl:model.ResourceUrl filename:bookCeName fileimage:nil bookName:model.Name classID:self.classID];
                
            }else if ([self.classID isEqualToString:@"55"] && [self.reType isEqualToString:@"10"]) {
                
                NSString *url = [model.ResourceUrl substringToIndex:model.ResourceUrl.length-3];
                
                url = [NSString stringWithFormat:@"%@mp4",url];
                
                [downManger downFileUrl:url filename:bookCeName fileimage:nil bookName:model.Name classID:self.classID];
            }else{
                
                [downManger downFileUrl:model.ResourceUrl filename:bookCeName fileimage:nil bookName:model.BookName classID:self.classID];
            }
            
        }
        
        downManger.maxCount = 1;
        
        MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
        
        hud.mode = MBProgressHUDModeText;
        
        hud.label.text = NSLocalizedString(@"已进入后台下载(已下载的内容不会重复下载)", @"HUD message title");
        
        hud.label.numberOfLines = 0;
        
        hud.offset = CGPointMake(0.f, MBProgressMaxOffset);
        
        [hud hideAnimated:YES afterDelay:1.f];
        
        [_tableView reloadData];
        
    }else{
        [ZuyuAlertShow alertShow:@"请先登录" viewController:self];
    }
    
}


#pragma mark - tableviewDalegateAndDatasouce

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _dataSouceArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    DownListTableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:@"listCell"];
    
    if (cell==nil) {
        
        cell = [[DownListTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
                                            reuseIdentifier:@"listCell"];
    }
    
    DownloadCeModel *model = _dataSouceArray[indexPath.row];
    
    cell.titleLabel.text = model.Name;
    
    float size = [[NSString stringWithFormat:@"%@",model.ResourceSize] floatValue];
    
    if (size) {
        NSString *sizeStr = [NSString stringWithFormat:@"%.2fM",size/1024/1024];
        
        cell.sizeLable.text = sizeStr;
    }else{
        cell.sizeLable.text = @"";
    }
    
    if ([_indexArray containsObject:indexPath]) {
        
        cell.image.image = [UIImage imageNamed:@"downlistYes.jpg"];
        
    }else{
        
        cell.image.image = [UIImage imageNamed:@"downlistNo.jpg"];
        
    }
    
    cell.tag = indexPath.row + 100;
    
    NSString *bookCeName  =  [[[NSString stringWithFormat:@"%@",model.ResourceUrl] componentsSeparatedByString:@"/"] lastObject];
    
    NSString *typeString = [bookCeName substringFromIndex:bookCeName.length-3];
    
    if ( [typeString isEqualToString:@"drm"]) {
        
        bookCeName = [bookCeName substringToIndex:bookCeName.length-3];
        
        bookCeName = [NSString stringWithFormat:@"%@mp3",bookCeName];
    }
    
    if ( [typeString isEqualToString:@"swf"]) {
        
        bookCeName = [bookCeName substringToIndex:bookCeName.length-3];
        
        bookCeName = [NSString stringWithFormat:@"%@mp4",bookCeName];
    }
    
    NSString *tempfilePath = [TEMP_PATH(bookCeName) stringByAppendingString:@".plist"];
    
    if ([ZFCommonHelper isExistFile:FILE_PATH(bookCeName)]) { // 已经下载过一次
        cell.isDownloadLable.text = @"已下载";
    } else if ([ZFCommonHelper isExistFile:tempfilePath]) {  // 存在于临时文件夹里
        cell.isDownloadLable.text = @"下载中";
    }else{
        cell.isDownloadLable.text = @"未下载";
    }
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 80;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    NSLog(@"click");
    
    if ([_indexArray containsObject:indexPath]) {
        
        [_indexArray removeObject:indexPath];
        
    }else{
        
        [_indexArray addObject:indexPath];
        
    }
    
    [_tableView reloadData];
    
}

#pragma mark - URLDEcode
//URLDEcode
-(NSString *)decodeString:(NSString*)encodedString

{
    NSString *decodedString  = (__bridge_transfer NSString *)CFURLCreateStringByReplacingPercentEscapesUsingEncoding(NULL,
                                                                                                                     (__bridge CFStringRef)encodedString,
                                                                                                                     CFSTR(""),
                                                                                                                     CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding));
    return decodedString;
}

#pragma mark - 导航栏返回按钮颜色设置 及 文字变成<
//  导航栏返回按钮颜色设置 及 文字变成<
-(void)UIBarButtonBackItemSet
{
    UIBarButtonItem *btn_back = [[UIBarButtonItem alloc]init];
    btn_back.title = @"";
    self.navigationItem.backBarButtonItem = btn_back;
    self.navigationController.navigationBar.barStyle = UIStatusBarStyleDefault;
    [self.navigationController.navigationBar setTintColor:[UIColor whiteColor]];
}


#pragma mark - nav 处理.
//-(void)viewWillAppear:(BOOL)animated
//{
//    [super viewWillAppear:animated];
//    [self.navigationController setNavigationBarHidden:YES animated:NO];
//}
//
//
//- (void) viewWillDisappear:(BOOL)animated
//{
//    [super viewWillDisappear:animated];
//    [self.navigationController setNavigationBarHidden:NO animated:NO];
//}
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

