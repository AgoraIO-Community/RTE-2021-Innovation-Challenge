//
//  MainThirdMP3ViewController.m
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/5/5.
//  Copyright © 2016年 zuyu. All rights reserved.
//



#import "MainThirdMP3ViewController.h"
#import "MainThirdTableViewCell.h"
#import "MainThirdUpTableViewCell.h"
#import "AFNetworking.h"
#import "MainThirdMP4Modle.h"
#import "ThirdMp3TableViewCell.h"
#import "ThirdMp3ButtonTableViewCell.h"
#import "MBProgressHUD.h"
#import "AppDelegate.h"
#import "UIImageView+WebCache.h"
#import "ZFListViewController.h"
#import "ZFDownloadManager.h"
#import "ScanDownloadViewController.h"
#import "DownloadData.h"
#import "zuyu.h"
#import "EMChatViewController.h"
@interface MainThirdMP3ViewController ()<UITableViewDelegate,UITableViewDataSource,mp3CellDownloadDelegate,NavgationViewDelegate,NetworkErrorViewDeleagete>
{
    NSMutableArray *_playerArray;
    
    UITableView *_tableView;
    
    NSInteger chooseID;
    
    NSString *_result;
    
    UILabel *_statusLabel;
    
    UIView *_videoView;
    
    UILabel *_timeLable;
    
    UILabel *_nameLable;
    
    NSString *_returnStr;
    
    int _stopOrStar;
    
    float returnCell0Height;
    
    NSString *_hotCellTitlePorts;
    
    //暂停播放按钮
    UIButton * _stopButton;
    
}
@property (nonatomic, strong) NSTimer *durationTimer;
@property (nonatomic,strong) NetworkErrorView *errorView;
@end

@implementation MainThirdMP3ViewController

#pragma mark - navgation
-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.titleStr = @"音频列表";
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(WIDTH - 120, 0, 120, 74);
    button.backgroundColor = [UIColor clearColor];
    [button setTitle:@"加入群聊" forState:UIControlStateNormal];
    [button addTarget:self action:@selector(joinGroup:) forControlEvents:UIControlEventTouchUpInside];
    [button setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
    [view addSubview:button];
    [self.view addSubview:view];
    
}


-(void)joinGroup:(id)arg
{
    
    NSString *title = @"";
    if (_dataArray.count) {
        MainThirdMP4Modle *model = _dataArray[0];
        title = model.bookName;
    }
    
    
    
    
    
    [PushToGroupTool groupBookID:self.tableListID withTitle:title withVC:self];
    
    
    
//}
//    EMGroupOptions *setting = [[EMGroupOptions alloc] init]; // 群组属性选项
//        setting.maxUsersCount = 500; // 群组的最大成员数(含群主、管理员，默认是200，最大3000)
//        setting.IsInviteNeedConfirm = NO; //邀请群成员时，是否需要发送邀请通知.若NO，被邀请的人自动加入群组
//        setting.style = EMGroupStylePublicOpenJoin;// 创建不同类型的群组，这里需要才传入不同的类型
//        setting.ext = @"群组扩展信息"; // 扩展信息
//    // 调用:
//
////    _tableListID
//
//    [[EMClient sharedClient].groupManager createGroupWithSubject:title description:@"" invitees:@[] message:@"" setting:setting completion:^(EMGroup *aGroup, EMError *aError) {
//        if(!aError){
//            NSLog(@"创建群组成功 -- %@",aGroup.groupId);
//
//            [self pushToChatVC:aGroup.groupId];
//
//        } else {
//            NSLog(@"创建群组失败的原因 --- %@", aError.errorDescription);
//        }
//    }];

}


-(void)pushToChatVC:(NSString *)gid
{
    EMChatViewController *chatController = [[EMChatViewController alloc]initWithConversationId:gid conversationType:EMConversationTypeGroupChat];
    
    [self.navigationController pushViewController:chatController animated:YES];
}


-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self createNavgation];
    
    [[UINavigationBar appearance] setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor whiteColor]}];
    
    self.navigationController.interactivePopGestureRecognizer.enabled = NO;
    
    _stopOrStar = 0;
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    chooseID = 0;
    
    _playerArray = [NSMutableArray array];
    
    _dataArray = [NSMutableArray array];
    
    _downLoadListArray = [NSMutableArray array];
    
    _statusLabel  = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, 200)];
    
    _statusLabel.backgroundColor = [UIColor greenColor];
    
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
    [self hotRequestDate];

}
-(NSMutableArray *)parametersArray
{
    
    if (_parametersArray == nil) {
        _parametersArray = [[NSMutableArray alloc] init];
    }
    
    return _parametersArray;
}


#pragma mark - CreateTableView
-(void)createTable
{
    CGSize sizes = [[UIScreen mainScreen] bounds].size;
    
    _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 74, sizes.width, sizes.height - 140 ) ];
    
    _tableView.delegate = self;
    
    _tableView.dataSource = self;
    
    _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
    
    NSIndexPath *firstPath = [NSIndexPath indexPathForRow:0 inSection:1];
    [_tableView selectRowAtIndexPath:firstPath animated:NO scrollPosition:UITableViewScrollPositionNone];
    //     [ [ [ UIApplication  sharedApplication ]  keyWindow ] addSubview : _tableView] ;
    [self.view addSubview:_tableView];
    
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return 1;
    }
    
    return  _dataArray.count;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *oneCellIdentifier = @"ThirdMp3TableViewCell";
    
    static NSString *twoCellIdentifier = @"ThirdMp3ButtonTableViewCell";
    
    if (indexPath.section == 0 && indexPath.row == 0) {
        
        ThirdMp3TableViewCell *sCell =(ThirdMp3TableViewCell*)[tableView dequeueReusableCellWithIdentifier:oneCellIdentifier];
        
        if (sCell==nil) {
            
            sCell = [[ThirdMp3TableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
                                                 reuseIdentifier:oneCellIdentifier];
        }
        
        if (_dataArray.count) {
            
            MainThirdMP4Modle *model = _dataArray[indexPath.row];
            
            sCell.titleLable.text = model.bookName;
            
            if (WIDTH == 320) {
                sCell.tepyLable.font = [UIFont systemFontOfSize:14];
                sCell.writerLable.font = [UIFont systemFontOfSize:14];
                sCell.countLable.font = [UIFont systemFontOfSize:14];
                sCell.popularLable.font = [UIFont systemFontOfSize:14];
            }
            
            sCell.tepyLable.text = [NSString stringWithFormat:@"类别:%@",_BookTypeName];
            
            sCell.writerLable.text = [NSString stringWithFormat:@"作者:%@",self.writer];
            
            sCell.countLable.text = [NSString stringWithFormat:@"章节:%ld",_dataArray.count];
            
            sCell.popularLable.text =  [NSString stringWithFormat:@"人气:%@",self.viewCount];
            
            [sCell.image sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@",self.imageName]] placeholderImage:[ZuyuPlaceholderImage returnPlaceholder:1] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
                
            }];
            
            sCell.introduceLable.text = [NSString stringWithFormat:@"简介:\n\n%@",self.Summary]  ;
            
            sCell.selectionStyle = UITableViewCellSelectionStyleNone;
            
        }
        
        return sCell;
        
    }else{
        
        ThirdMp3ButtonTableViewCell *tCell =(ThirdMp3ButtonTableViewCell*)[tableView dequeueReusableCellWithIdentifier:twoCellIdentifier];
        
        
        if (tCell==nil) {
            
            tCell = [[ThirdMp3ButtonTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
                                                       reuseIdentifier:twoCellIdentifier];
            
        }
        tCell.delegate = self;
        
        if (_dataArray.count) {
            
            MainThirdMP4Modle *model = _dataArray[indexPath.row];
            
            if (indexPath.row == 0) {
                _nameLable.text = model.name;
            }
            
            tCell.nameLable.text = model.name;
            
            tCell.nameLable.highlightedTextColor = COLOR;
            
            tCell.image.highlightedImage = [UIImage imageNamed:@"play_btn_hover"];
            
            //            tCell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            tCell.selectionStyle=UITableViewCellSelectionStyleGray;
            
            tCell.selectedBackgroundView=[[UIView alloc]initWithFrame:tCell.frame];
            tCell.selectedBackgroundView.backgroundColor=[UIColor clearColor];
            
            tCell.downImage.tag = 500 + indexPath.row;
            
//            float size = [[NSString stringWithFormat:@"%@",model.ResourceSize] floatValue];
//
//            NSString *sizeStr = [NSString stringWithFormat:@"%.2fM",size/1024/1024];
//
//            tCell.readLable.text = sizeStr;
            tCell.readLable.hidden = YES;
            
        }
        
        return tCell;
    }
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    CGSize sizes = [[UIScreen mainScreen] bounds].size;
    
    if (indexPath.section == 0 && indexPath.row == 0) {
        
        //        return returnCell0Height + sizes.height / 5 + 50;
        
        return sizes.height/2;
        
    }else if(indexPath.section == 0 && indexPath.row == 1){
        
        return 10;
        
    }else{
        
        return sizes.height/11;
        
    }
    
}
//
- (nullable UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    CGSize sizes = [[UIScreen mainScreen] bounds].size;

    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, sizes.width, 40)];

    view.backgroundColor = [UIColor whiteColor];

    UILabel *lable = [[UILabel alloc] initWithFrame:CGRectMake(15, 10, sizes.width, 20)];

    lable.text = [NSString stringWithFormat:@"共有%ld章节",_dataArray.count];

    [view addSubview:lable];
    
    view.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"hot_recommended_list_bg"]];
    
    return view;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (section) {
        return 40;
    }
    return 0;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.section == 0) {
        
    }else{
        
        chooseID = indexPath.row;
        
        MainThirdMP4Modle *model = _dataArray[indexPath.row];
        
        _nameLable.text = model.name;
        
        [self playVideo];
        
    }
    if (_stopOrStar%2) {
        _stopOrStar++;
    }

    [_stopButton setImage:[UIImage imageNamed:@"movie_stop_bt1"] forState:UIControlStateNormal];
    //    NSLog(@"%ld",indexPath.row);
}

#pragma mark - 数据请求
-(void)hotRequestDate
{
    
    
    if (self.tableListID) {
        
        _hotCellTitlePorts = [NSString stringWithFormat:@"http://resource.cncgroup.net:8015/api/books/%@/volumes",_tableListID];
        
        AFHTTPSessionManager *operationManager = [AFHTTPSessionManager manager];
        //自动帮我们进行数据解析,关闭自动解析
//        operationManager.responseSerializer = [AFHTTPResponseSerializer serializer];
       
        [operationManager GET:_hotCellTitlePorts parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
            
        } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
          
            NSLog(@"%@",responseObject);
//            for (NSArray *array in responseObject) {
//
//

            for (NSDictionary *itemDict in responseObject) {

                MainThirdMP4Modle *model = [[MainThirdMP4Modle alloc ] init];

                model.name = [itemDict objectForKey:@"Name"];

                model.resourceUrl = [itemDict objectForKey:@"ResourceUrl"];

                model.summary = [itemDict objectForKey:@"Summary"];

                model.bookName = [itemDict objectForKey:@"BookName"];

                model.viewCount = [itemDict objectForKey:@"ViewCount"];

                model.volumeId = [itemDict objectForKey:@"ID"];

                model.bookId = [itemDict objectForKey:@"BookID"];

                model.MenuID =[itemDict objectForKey:@"MenuID"];

                model.ResourceSize = [itemDict objectForKey:@"ResourceSize"];

                [self.dataArray addObject:model];

                [_playerArray addObject:[itemDict objectForKey:@"ResourceUrl"]];

            }
            [_tableView reloadData];
            
            [self playVideo];
            
            [self createTable];
            
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            self.errorView.hidden = NO;
        }];
        
      
        
    }
    
    else if (self.parametersArray.count) {
        
        _hotCellTitlePorts = [NSString stringWithFormat:@"http://resource.cncgroup.net:8015/api/books/%@/volumes",[self.parametersArray objectAtIndex:(self.butTag - 100)]];
        
        AFHTTPSessionManager *operationManager = [AFHTTPSessionManager manager];
       
        [operationManager GET:_hotCellTitlePorts parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
            
        } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            
            int i = 0;
            
            for (NSDictionary *itemDict in responseObject) {
                
                [_playerArray addObject:[itemDict objectForKey:@"ResourceUrl"]];
                
                MainThirdMP4Modle *model = [[MainThirdMP4Modle alloc ] init];
                
                model.name = [itemDict objectForKey:@"Name"];
                
                model.resourceUrl = [itemDict objectForKey:@"ResourceUrl"];
                
                model.summary = [itemDict objectForKey:@"Summary"];
                
                model.bookName = [itemDict objectForKey:@"BookName"];
                
                model.viewCount = [itemDict objectForKey:@"ViewCount"];
                
                model.volumeId = [itemDict objectForKey:@"ID"];
                
                model.bookId = [itemDict objectForKey:@"BookID"];
                
                model.ResourceSize = [itemDict objectForKey:@"ResourceSize"];
                
                [self.dataArray addObject:model];
                
                i++;
                
            }
            
            [_tableView reloadData];
            
            [self playVideo];
            
            [self createTable];
            
            
            
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            self.errorView.hidden = NO;
        }];
        
        
        [_tableView reloadData];
        
    }
    
    
    
}

#pragma mark - 播放器
- (void)playVideo{
    
    if (_playerArray.count) {
        
        NSURL *url = [NSURL URLWithString:[_playerArray objectAtIndex:chooseID]];
        
        [self addVideoPlayerWithURL:url];

    }
    
}

- (void)addVideoPlayerWithURL:(NSURL *)url{
    
    if (!self.videoController) {
        
        CGSize sizes = [[UIScreen mainScreen] bounds].size;
        
        CGFloat width = [UIScreen mainScreen].bounds.size.width;
        
        CGFloat viewFeigthFrame = sizes.height - 104 + 28;
        
        if (sizes.width == 320) {
            self.videoController = [[KrVideoPlayerController alloc] initWithFrame:CGRectMake(-15, 35, width + sizes.width/10 - 5 , 40)];
        }else{
            
            self.videoController = [[KrVideoPlayerController alloc] initWithFrame:CGRectMake(-15, 35, width + sizes.width/10 - 5 , 40)];
            
        }
        
        self.videoController.view.hidden = YES;
        __weak typeof(self)weakSelf = self;
        
        [self.videoController setDimissCompleteBlock:^{
            
            weakSelf.videoController = nil;
            
        }];
        [self.videoController setWillBackOrientationPortrait:^{
            
            [weakSelf toolbarHidden:NO];
        }];
        [self.videoController setWillChangeToFullscreenMode:^{
            
            [weakSelf toolbarHidden:YES];
        }];
        
        self.videoController.backgroundView.backgroundColor = [UIColor colorWithRed:157.f/255.f green:202.f/255.f blue:103.f/255.f alpha:1];
        
        
        _videoView = [[UIView alloc] initWithFrame:CGRectMake(0, viewFeigthFrame + 10, width , 70)];
        
        _videoView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"hot_recommended_list_bg"]];
        
        UILabel *lion = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 0.5)];
        
        lion.backgroundColor = [UIColor lightGrayColor];
        
        
        [_videoView addSubview:lion];
        
        
        _stopButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
        _stopButton.frame = CGRectMake(10 ,15, 40, 40);
        
        [_stopButton setImage:[UIImage imageNamed:@"movie_stop_bt1"] forState:UIControlStateNormal];
        
        [_stopButton addTarget:self action:@selector(stop:) forControlEvents:UIControlEventTouchUpInside];
        
        [_videoView addSubview:_stopButton];
        
        
        
        _timeLable  = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH * 0.7,15,WIDTH * 0.3,40)];
        
        _timeLable.textAlignment = NSTextAlignmentCenter;
        
        _timeLable.text = @"00:00/00:00";
        if (WIDTH == 320) {
            _timeLable.font = [UIFont systemFontOfSize:12];
        }
        
        [self startDurationTimer];
        
        [self monitorVideoPlayback];
        
        [_videoView addSubview:_timeLable];
        
        _nameLable = [[UILabel alloc] initWithFrame:CGRectMake(70, 15, WIDTH * 0.5, 40)];
        
        [_videoView addSubview:_nameLable];
        
        [self.view addSubview:_videoView];
        
        [_videoView addSubview:self.videoController.view];
        
        
        self.videoController.videoControl.timeLabel.hidden = YES;
        
        self.videoController.videoControl.pauseButton.alpha = 0.0;
        
        self.videoController.videoControl.playButton.alpha = 0.0;
        
        self.videoController.videoControl.fullScreenButton.alpha = 0.0;
        
    }
    
    
    
    self.videoController.contentURL = url;
    
    
    
}



#pragma mrak - 播放条控制
//暂停播放
-(void)stop:(UIButton *)button
{
    if(_stopOrStar%2){
        
        [self.videoController play];
        
        [button setImage:[UIImage imageNamed:@"movie_stop_bt1"] forState:UIControlStateNormal];
        
    }else{
        
        [button setImage:[UIImage imageNamed:@"movie_play_bt1"] forState:UIControlStateNormal];
        
        [self.videoController pause];
        
    }
    if(_stopOrStar%2){
        
    }
    _stopOrStar++;
}


- (void)monitorVideoPlayback
{
    double currentTime = floor(self.videoController.currentPlaybackTime);
    double totalTime = floor(self.videoController.duration);
    [self setTimeLabelValues:currentTime totalTime:totalTime];
    //    self.videoControl.progressSlider.value = ceil(currentTime);
}

- (void)setTimeLabelValues:(double)currentTime totalTime:(double)totalTime {
    
    if (currentTime && totalTime) {
        double minutesElapsed = floor(currentTime / 60.0);
        double secondsElapsed = fmod(currentTime, 60.0);
        NSString *timeElapsedString = [NSString stringWithFormat:@"%02.0f:%02.0f", minutesElapsed, secondsElapsed];
        
        double minutesRemaining = floor(totalTime / 60.0);;
        double secondsRemaining = floor(fmod(totalTime, 60.0));;
        NSString *timeRmainingString = [NSString stringWithFormat:@"%02.0f:%02.0f", minutesRemaining, secondsRemaining];
        
        
        _timeLable.text = [NSString stringWithFormat:@"%@/%@",timeElapsedString,timeRmainingString];
    }else{
        _timeLable.text = @"加载中...";
    }
}


- (void)stopDurationTimer
{
    [self.durationTimer invalidate];
}

- (void)startDurationTimer
{
    self.durationTimer = [NSTimer scheduledTimerWithTimeInterval:0.2 target:self selector:@selector(monitorVideoPlayback) userInfo:nil repeats:YES];
    [[NSRunLoop currentRunLoop] addTimer:self.durationTimer forMode:NSDefaultRunLoopMode];
}





//隐藏navigation tabbar 电池栏
- (void)toolbarHidden:(BOOL)Bool{
        [[UIApplication sharedApplication] setStatusBarHidden:Bool withAnimation:UIStatusBarAnimationFade];
}



#pragma mark - system

-(void)viewWillDisappear:(BOOL)animated
{
    [super  viewWillDisappear:animated];
    
    [_videoController dismiss];
    
    _videoView.hidden = YES;
    
    [self.navigationController setNavigationBarHidden:NO animated:NO];

    [_dataArray removeAllObjects];
    
}



-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self hotRequestDate];
    [self.navigationController setNavigationBarHidden:YES animated:NO];
    
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma  mark - downLoad

-(void)downloadMP3
{
    
   
    [self requestScanDate:[NSString stringWithFormat:@"http://resource.cncgroup.net:8011/api/Books/favorite/book/%@/1/0",self.BookID]];
    
    NSLog(@"download");
}
-(void)requestScanDate:(NSString *)port
{
    
   MBProgressHUD *_hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
    
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
                 
                 [_videoController pause];
                 
                 [self.navigationController pushViewController:vc animated:YES];
                 
             }else if ([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]] isEqualToString:@"55"] && [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"ResourceType"]] isEqualToString:@"10"]){
                 
                 NSArray *arr = [NSArray arrayWithObject:responseObject];
                 
                 ScanDownloadViewController *vc = [[ScanDownloadViewController alloc] init];
                 
                 vc.dataArray = arr;
                 
                 vc.classID = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]];
                 
                 vc.reType = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"ResourceType"]];
                 
                 [vc setHidesBottomBarWhenPushed:YES];
                 
                 [_videoController pause];
                 
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
                 
                 if (_isHot) {
                     vc.classID = @"55";
                 }else{
                     vc.classID = model.Class1;
                 }
                 
                 [vc setHidesBottomBarWhenPushed:YES];
                 
                 [_videoController pause];
                 
                 [self.navigationController pushViewController:vc animated:YES];
                 
             }
             
         }
     
         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull   error) {
             
             NSLog(@"%@",error);  //这里打印错误信息
             
             [_hud hideAnimated:YES];
             
         }];
    
}

#pragma mark - 如果音乐播放器未被释放

//内存处理
-(void)didMoveToParentViewController:(UIViewController *)parent
{
    if (parent == nil) {
        _videoController = nil;
    }
}

#pragma mark - 删除收藏记录!! 留着下次用!
//-(void)tableView:(UITableView*)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath*)indexPath
//
//{
//
//    if (editingStyle == UITableViewCellEditingStyleDelete)
//    {
//
//        MainThirdMP4Modle *modle = _dataArray[indexPath.row];
//
//        NSString *jiekoustr = [NSString stringWithFormat:@"http://resource.cncgroup.net:80/api/books/History/Delete/%@",modle.deletaID];
//
//        AFHTTPRequestOperationManager *operationManager = [AFHTTPRequestOperationManager manager];
//        //自动帮我们进行数据解析,关闭自动解析
//        operationManager.responseSerializer = [AFHTTPResponseSerializer serializer];
//        [operationManager GET:jiekoustr parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
//
//            NSLog(@"delete");
//
//
//        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
//
//            NSLog(@"loser");
//        }];
//
//        [_dataArray removeObjectAtIndex:indexPath.row];
//
//        [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
//
//    }
//
//
//}
//
////修改删除按钮为中文的删除
//
//-(NSString*)tableView:(UITableView*)tableView titleForDeleteConfirmationButtonForRowAtIndexPath:(NSIndexPath*)indexPath
//
//{
//
//    return@"  删  除  ";
//
//}
//
////是否允许编辑行，默认是YES
//
//-(BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
//
//{
//
//    return YES;
//
//}
//

//
//

/*
 #pragma mark - Navigatio n
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
