//
//  MainMP4ViewController.m
//  CNCLibraryScan
//
//  Created by zuyu on 2017/11/21.
//  Copyright © 2017年 zuyu. All rights reserved.
//

#import "MainMP4ViewController.h"
#import "KrVideoPlayerController.h"
#import "AFNetworking.h"
#import "MainMp4Model.h"
#import "MainMP4ListCell.h"
#import "MBProgressHUD.h"
#import "ScanDownloadViewController.h"
#import "DownloadData.h"
#import "ZFDownloadManager.h" 
#import "UIAlertView+Blocks.h"
#import "zuyu.h"


@interface MainMP4ViewController ()<ZFPlayerDelegate,UITableViewDelegate,UITableViewDataSource,MainMP4ListCellDelegate,NetworkErrorViewDeleagete>
{
    NSMutableArray *_dataArray;
    UITableView *_tableView;
    MBProgressHUD *_hud;

    //判断是否刚进入页面
    NSInteger _flag;
}
@property (strong, nonatomic)  UIView *playerFatherView;
@property (strong, nonatomic) ZFPlayerView *playerView;
/** 离开页面时候是否在播放 */
@property (nonatomic, assign) BOOL isPlaying;
@property (nonatomic, strong) ZFPlayerModel *playerModel;

@property (nonatomic, strong) NetworkErrorView *errorView;
@end

@implementation MainMP4ViewController
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    self.tabBarController.tabBar.hidden = YES;
    [self.navigationController setNavigationBarHidden:YES animated:NO];
    
    if (_playerView) {
        [_playerView play];
    }

}
- (void)viewDidLoad {
    
    [[NSNotificationCenter defaultCenter]addObserver:self
                                            selector:@selector(NetWorkStatesChange:) name:@"netWorkChange"
                                              object:nil];
    
    self.view.backgroundColor = [UIColor whiteColor];
    if ([ZuyuNetState zuyuGetNetState] == 1) {
        UIAlertView *alert = [UIAlertView showWithTitle:@"当前为非 Wifi 环境" message:@"是否播放视频" cancelButtonTitle:@"取消" otherButtonTitles:@[@"播放"] tapBlock:^(UIAlertView * _Nonnull alertView, NSInteger buttonIndex) {
            if (buttonIndex) {
                [self selfInit];
            }else{
                [self.navigationController popViewControllerAnimated:YES];
            }
            
        }];
        
    }else{
        [self selfInit];
    }
    
    [super viewDidLoad];
    
    
}

-(void)selfInit
{
    _dataArray = [NSMutableArray array];
    
    [self UIBarButtonBackItemSet];
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    [self RuqestData];
    
    [self createTableView];
    
    _flag = 1;
}
#pragma mark - 网络加载失败视图
-(NetworkErrorView *)errorView
{
    if (_errorView == nil) {
        _errorView = [[NetworkErrorView alloc] initWithFrame:CGRectMake(0, 64, WIDTH, HEIGHT - 64)];
        _errorView.delegate = self;
        [self.view addSubview:_errorView];
        
    }
    return _errorView;
}
//重新加载
- (void)refreshLoadResouce
{
    self.errorView.hidden = YES;
    [self RuqestData];

}
#pragma mark - 网络状态发生变化通知方法
-(void)NetWorkStatesChange:(NSNotification *)sender{
    int networkState = [[sender object] intValue];
    switch (networkState) {
        case -1:
            //未知网络状态
            
            break;
            
        case 0:
            //没有网络
            break;
            
        case 1:
            //3G或者4G，反正用的是流量
            [self netStateChange4G];
            break;
            
        case 2:
            //WIFI网络
            
            break;
            
        default:
            break;
    }
}
-(void)netStateChange4G
{
    
    [_playerView pause];
    
    UIAlertView *alert = [UIAlertView showWithTitle:@"当前为非 Wifi 环境" message:@"已为您暂停播放" cancelButtonTitle:@"暂停播放" otherButtonTitles:@[@"继续播放"] tapBlock:^(UIAlertView * _Nonnull alertView, NSInteger buttonIndex) {
        if (buttonIndex) {
            [_playerView play];
        }
    }];
}

#pragma mark - UITableView

-(void)createTableView
{
    float height =  HEIGHT - WIDTH*(9.0/16.0);

    _tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, WIDTH*(9.0/16.0) ,WIDTH, height ) style:UITableViewStylePlain];
    
    _tableView.delegate = self;
    
    _tableView.dataSource = self;
    
    _tableView.rowHeight = 60;
    
    _tableView.separatorStyle = UITableViewCellEditingStyleNone; 
    
    [self.view addSubview:_tableView];
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _dataArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    MainMP4ListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MainMP4ListCell"];
    
    if (cell == nil) {
        cell = [[MainMP4ListCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"MainMP4ListCell" WithTag:indexPath.row];
        
    }
    
    if (_dataArray.count) {
        
        MainMp4Model *model = _dataArray[indexPath.row];
        
        cell.text.text = model.Name;
        
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
            cell.downLoadText.text = @"已下载";
        } else if ([ZFCommonHelper isExistFile:tempfilePath]) {  // 存在于临时文件夹里
            cell.downLoadText.text = @"下载中";
        }else{
            cell.downLoadText.text = @"未下载";
        }
        
    }
    
    cell.delegate = self;
    
    return cell;

}

- (nullable UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    CGSize sizes            = [[UIScreen mainScreen] bounds].size;
  
    UIView *view            = [[UIView alloc] initWithFrame:CGRectMake(0, 0, sizes.width, 60)];
    view.backgroundColor    = [UIColor whiteColor];
   
    UILabel *lable          = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, WIDTH * 0.67, 40)];
    lable.text              = [NSString stringWithFormat:@"播放列表  共有%ld章节",_dataArray.count];
    
    lable.font = [UIFont systemFontOfSize:22];
    
    [view addSubview:lable];
    
    UIButton *downLoadAll = [UIButton buttonWithType:UIButtonTypeCustom];
    
    downLoadAll.frame = CGRectMake(WIDTH - 110, 10, 100, 40);
    
    [downLoadAll setTitleColor: [UIColor blackColor] forState:UIControlStateNormal];
    
    [downLoadAll setTitle:@"加入群聊" forState:UIControlStateNormal];
    
    [downLoadAll addTarget:self action:@selector(downloadAll:) forControlEvents:UIControlEventTouchUpInside];
    
    [view addSubview:downLoadAll];
    
    return view;
  
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 60;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    MainMp4Model *model = _dataArray[indexPath.row];
    [self playVideoUrl:model.ResourceUrl WithTitle:model.Name];
    
}
-(void)playVideoUrl:(NSString *)url WithTitle:(NSString *)title
{
    _playerModel.title            = title;
    _playerModel.videoURL         = [NSURL URLWithString:url];
    [_playerView resetToPlayNewVideo:_playerModel];
    
}
#pragma mark - ZFPlayer

-(void)playVideo{
    
    if (_dataArray.count) {
        
        self.playerFatherView = [[UIView alloc] init];
        
        [self.view addSubview:self.playerFatherView];
        
        [self.playerFatherView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(0);
            make.leading.trailing.mas_equalTo(0);
            make.height.mas_equalTo(self.playerFatherView.mas_width).multipliedBy(9.0f/16.0f);
        }];
        
        [self.playerFatherView addSubview:self.playerView];
        
        [self.playerView autoPlayTheVideo];
   
    }
    
}




- (ZFPlayerView *)playerView {
    if (!_playerView) {
        _playerView = [[ZFPlayerView alloc] init];
        [_playerView playerControlView:nil playerModel:self.playerModel];
        _playerView.delegate = self;
        _playerView.hasPreviewView = YES;
    }
    return _playerView;
    
}

- (ZFPlayerModel *)playerModel {
    if (!_playerModel) {
        NSURL *url;
                
        MainMp4Model *model = _dataArray[0];
        
        url =  [NSURL URLWithString:model.ResourceUrl];
        _playerModel                  = [[ZFPlayerModel alloc] init];
        _playerModel.title            = model.Name;
        _playerModel.videoURL         = url;
        _playerModel.placeholderImage = [UIImage imageNamed:@"loading_bgView1"];
        _playerModel.fatherView       = self.playerFatherView;
        
    }
    return _playerModel;
}



-(void)viewWillDisappear:(BOOL)animated
{
    [super  viewWillDisappear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:NO];
}



#pragma mark - netWork
-(void)RuqestData
{
    
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    NSString *port = Mp4Port(self.BookID);
    
    if (self.isDH) {
        port = Mp4DHPort(self.BookID);
    }
    
    [manager GET:port parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        for (NSDictionary *dict in responseObject) {
            
            MainMp4Model *model = [[MainMp4Model alloc] init];
            
            model.CID = [NSString stringWithFormat:@"%@",[dict objectForKey:@"ID"]];
            
            model.Name = [NSString stringWithFormat:@"%@",[dict objectForKey:@"Name"]];
            
            model.VolumnNo = [NSString stringWithFormat:@"%@",[dict objectForKey:@"VolumnNo"]];
            
            model.BookID = [NSString stringWithFormat:@"%@",[dict objectForKey:@"BookID"]];
            
            model.ResourceType = [NSString stringWithFormat:@"%@",[dict objectForKey:@"ResourceType"]];
           
            model.CoverImageUrl = [NSString stringWithFormat:@"%@",[dict objectForKey:@"CoverImageUrl"]];
            
            model.ViewCount = [NSString stringWithFormat:@"%@",[dict objectForKey:@"ViewCount"]];
            
            model.ResourceSize = [NSString stringWithFormat:@"%@",[dict objectForKey:@"ResourceSize"]];
            
            model.ResourceDuration = [NSString stringWithFormat:@"%@",[dict objectForKey:@"ResourceDuration"]];
            
            model.BookName = [NSString stringWithFormat:@"%@",[dict objectForKey:@"BookName"]];
            
            model.BookType = [NSString stringWithFormat:@"%@",[dict objectForKey:@"BookType"]];
            
            model.ResourceUrl = [NSString stringWithFormat:@"%@",[dict objectForKey:@"ResourceUrl"]];
            
            model.Classp = [NSString stringWithFormat:@"%@",[dict objectForKey:@"Class1"]];
            
            [_dataArray addObject:model];
            
        }
        
        [self playVideo];
        [_tableView reloadData];
        if (_dataArray.count) {
            [self.navigationController setNavigationBarHidden:YES animated:NO];
        }else{
            [self.navigationController setNavigationBarHidden:NO animated:NO];
        }

        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
    
        [self.navigationController setNavigationBarHidden:NO animated:NO];
        self.errorView.hidden = NO;

        NSLog(@" --->>>>>> error %@", error );
        
    }];
    
    
    
}

#pragma mark - 下载
-(void)download:(NSInteger)indexRow
{

    [self requestScanDate:[NSString stringWithFormat:@"http://resource.cncgroup.net/api/Books/favorite/book/%@",self.BookID]];
    
}
#pragma mark - 加入群聊
-(void)downloadAll:(UIButton *)button
{
    
    NSString *title = @"";
    if (_dataArray.count) {
        MainMp4Model *model = _dataArray[0];
        title = model.Name;
    }
    
    [PushToGroupTool groupBookID:self.BookID withTitle:title withVC:self];

    
    NSLog(@"加入群聊");
//    [self requestScanDate:[NSString stringWithFormat:@"http://resource.cncgroup.net/api/Books/favorite/book/%@",self.BookID]];
    
    
} 

#pragma mark - 获取当前播放时间
-(void)backSecond:(NSInteger)sccond
{

    
}


#pragma mark - 扫描结果的数据请求
-(void)requestScanDate:(NSString *)port
{
    
    if([port rangeOfString:@":8011"].location !=NSNotFound){
        port = [NSString stringWithFormat:@"%@/1/0",port];
    }
    else
    {
        NSMutableString *ports = [NSMutableString stringWithFormat:@"%@",port];
        
        [ports insertString:@":8011" atIndex:21];
        
        port = [NSString stringWithFormat:@"%@/1/0",ports];
        
    }
    
    _hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"获取中...", @"HUD loading title");
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    NSLog(@"%@",port);
    
    [manager GET:port parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
    }
         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
             
             [_hud hideAnimated:YES];
             
             if ([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]] isEqualToString:@"22"]||
                 [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]] isEqualToString:@"77"]||
                 [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]] isEqualToString:@"99"]) {
                 
                 NSArray *arr = [NSArray arrayWithObject:responseObject];
                 
                 ScanDownloadViewController *vc = [[ScanDownloadViewController alloc] init];
                 
                 vc.dataArray = arr;
                 
                 vc.classID = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]];
                 
                 [vc setHidesBottomBarWhenPushed:YES];
                 
                 [_playerView pause];
                 
                 [self.navigationController pushViewController:vc animated:YES];
                 
             }else if ([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]] isEqualToString:@"55"] && [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"ResourceType"]] isEqualToString:@"10"]){
                 
                 NSArray *arr = [NSArray arrayWithObject:responseObject];
                 
                 ScanDownloadViewController *vc = [[ScanDownloadViewController alloc] init];
                 
                 vc.dataArray = arr;
                 
                 vc.classID = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]];
                 
                 vc.reType = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"ResourceType"]];
        
                 [vc setHidesBottomBarWhenPushed:YES];
                 
                 [_playerView pause];

                 [self.navigationController pushViewController:vc animated:YES];
                 
             }else{
                 
                 DownloadData *model = [[DownloadData alloc] init];
                 
                 model.Author = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Author"]];
                 
                 model.BookType = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"BookType"]];
                 
                 model.BookTypeName = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"BookTypeName"]];
                 
                 model.Class1 = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]];
                 
                 model.Class2 = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class2"]];
                 
                 model.CollectCount = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"CollectCount"]];
                 
                 model.CoverImageUrl = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"CoverImageUrl"]];
                 
                 model.CreatedOn = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"CreatedOn"]];
                 
                 model.HasContent = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"HasContent"]];
                 
                 model.ID = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"ID"]];
                 
                 model.MenuID = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"MenuID"]];
                 
                 model.Name = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Name"]];
                 
                 model.RecommendType = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"RecommendType"]];
                 
                 model.ResourceType = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"ResourceType"]];
                 
                 model.ResourceUrl = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"ResourceUrl"]];
                 
                 model.Summary = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Summary"]];
                 
                 model.Tag = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Tag"]];
                 
                 model.ViewCount = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"ViewCount"]];
                 
                 model.VolumeCount = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"VolumeCount"]];
                 
                 model.Volumes = [responseObject objectForKey:@"Volumes"];
                 
                 ScanDownloadViewController *vc = [[ScanDownloadViewController alloc] init];
                 
                 vc.dataArray = model.Volumes;
                 
                 vc.classID = model.Class1;
                 
                 [vc setHidesBottomBarWhenPushed:YES];
                 
                 [_playerView pause];

                 [self.navigationController pushViewController:vc animated:YES];
                 
             }
             
         }
     
         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull   error) {
             
             NSLog(@"%@",error);  //这里打印错误信息
             
             [_hud hideAnimated:YES];
             
         }];
    
}



#pragma mark - pop/ 记录播放时间.
- (void)zf_playerBackAction {
//    NSLog(@"%ld" ,_nowSecond);
//    [self setLookedWithSecond:_nowSecond];
    [self.navigationController popViewControllerAnimated:YES];
}


#pragma mark - -------------------
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // -------Dispose of any resources that can be recreated.
}


-(void)UIBarButtonBackItemSet
{
    UIBarButtonItem*btn_back = [[UIBarButtonItem alloc]init];
    
    btn_back.title = @"";
    
    self.navigationItem.backBarButtonItem = btn_back;
    
    self.navigationController.navigationBar.barStyle = UIStatusBarStyleDefault;
    
    [self.navigationController.navigationBar setTintColor:[UIColor whiteColor]];
    
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

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
