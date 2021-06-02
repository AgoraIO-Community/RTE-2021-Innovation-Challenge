//
//  DJMP3VC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/19.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "DJMP3VC.h"
#import "KrVideoPlayerController.h"
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
#import "KrVideoPlayerController.h"
@interface DJMP3VC ()<UITableViewDelegate,UITableViewDataSource,mp3CellDownloadDelegate,NavgationViewDelegate,NetworkErrorViewDeleagete>
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
    
    
}
@property (nonatomic, strong) KrVideoPlayerController  *videoController;
@property (nonatomic, strong) NSTimer *durationTimer;

@end

@implementation DJMP3VC

#pragma mark - navgation
-(void)createNavgation
{
//    _model.ResourceUrl = @"http://211.145.50.11:82/jyj_mx//resource/05%E5%9B%BD%E5%AD%A6%E7%BB%8F%E5%85%B8/01%E8%AE%BA%E8%AF%AD/%E8%AE%BA%E8%AF%AD%5B%E5%A5%B3%E5%A3%B0%5D/20%E5%B0%A7%E6%9B%B0%E7%AC%AC%E4%BA%8C%E5%8D%81.drm";
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.titleStr = @"音频列表";
    [self.view addSubview:view];
}


-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self createNavgation];
    
    [self playVideo];
    
    [self createTable];
    [[UINavigationBar appearance] setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor whiteColor]}];
    
    self.navigationController.interactivePopGestureRecognizer.enabled = NO;
    
    _stopOrStar = 0;
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    chooseID = 0;
    
    _playerArray = [NSMutableArray array];

    _statusLabel  = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, 200)];
    
    _statusLabel.backgroundColor = [UIColor greenColor];
    
}


#pragma mark - CreateTableView
-(void)createTable
{
    CGSize sizes = [[UIScreen mainScreen] bounds].size;
    
    _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 74, sizes.width, sizes.height - 140 ) ];
    
    _tableView.delegate = self;
    
    _tableView.dataSource = self;
    
    _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
    _tableView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"hot_recommended_list_bg"]];
    
    NSIndexPath *firstPath = [NSIndexPath indexPathForRow:0 inSection:1];
    [_tableView selectRowAtIndexPath:firstPath animated:NO scrollPosition:UITableViewScrollPositionNone];
    //     [ [ [ UIApplication  sharedApplication ]  keyWindow ] addSubview : _tableView] ;
    [self.view addSubview:_tableView];
    
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    return 1;
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
        
        if (self.model) {
            
            
            sCell.titleLable.text = self.model.name;
            
            if (WIDTH == 320) {
                sCell.tepyLable.font = [UIFont systemFontOfSize:14];
                sCell.writerLable.font = [UIFont systemFontOfSize:14];
                sCell.countLable.font = [UIFont systemFontOfSize:14];
                sCell.popularLable.font = [UIFont systemFontOfSize:14];
            }
            
            sCell.tepyLable.text = [NSString stringWithFormat:@"类别:%@",_model.bookTypeName];
            
            sCell.writerLable.text = [NSString stringWithFormat:@"作者:%@",_model.author];
            
            sCell.countLable.text = [NSString stringWithFormat:@"章节:%@",_model.VolumeCount];
            
            sCell.popularLable.text =  [NSString stringWithFormat:@"人气:%@",_model.viewCount];
            
            [sCell.image sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@",_model.imageName]] placeholderImage:[ZuyuPlaceholderImage returnPlaceholder:2] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
                
            }];
            
            sCell.introduceLable.text = [NSString stringWithFormat:@"简介:\n\n%@",_model.Summary]  ;
            
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


#pragma mark - 播放器
- (void)playVideo{
    
        NSURL *url = [NSURL URLWithString:_model.ResourceUrl];
        
        [self addVideoPlayerWithURL:url];
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
        
        
        UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
        
        button.frame = CGRectMake(10 ,15, 40, 40);
        
        [button setImage:[UIImage imageNamed:@"movie_stop_bt1"] forState:UIControlStateNormal];
        
        [button addTarget:self action:@selector(stop:) forControlEvents:UIControlEventTouchUpInside];
        
        [_videoView addSubview:button];
        
        
        
        _timeLable  = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH * 0.5,15,WIDTH * 0.3,40)];
        
        _timeLable.textAlignment = NSTextAlignmentCenter;
        
        if (WIDTH == 320) {
            _timeLable.font = [UIFont systemFontOfSize:12];
        }
        
        UIButton *downLoadButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
        downLoadButton.frame = CGRectMake(WIDTH * 0.85, 15, WIDTH * 0.1, 40);
        
        [downLoadButton setTitle:@"下载" forState: UIControlStateNormal];
        
        [downLoadButton setTitleColor:COLOR forState:UIControlStateNormal];
        
        [downLoadButton addTarget:self action:@selector(selectDownload:) forControlEvents:UIControlEventTouchUpInside];
        
        [_videoView addSubview:downLoadButton];

        
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



#pragma mark - 下载
-(void)selectDownload:(UIButton *)button
{
    
    ZFDownloadManager *downManger = [ZFDownloadManager sharedDownloadManager];
    //80
//    NSString *bookCeName  =  [[[NSString stringWithFormat:@"%@",_model.ResourceUrl] componentsSeparatedByString:@"/"] lastObject];
//
//    NSString *typeString = [bookCeName substringFromIndex:bookCeName.length-3];
//
//    if ( [typeString isEqualToString:@"drm"]) {
//
//        bookCeName = [bookCeName substringToIndex:bookCeName.length-3];
//
//        bookCeName = [NSString stringWithFormat:@"%@mp3",_model.name];
//    }
//
   NSString * bookCeName = [NSString stringWithFormat:@"%@.mp3",_model.name];

    //编码
    bookCeName = [bookCeName stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    
    [downManger downFileUrl:_model.ResourceUrl filename:bookCeName fileimage:nil bookName:_model.name classID:@"80"];
    
    downManger.maxCount = 1;
    
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
    
    hud.mode = MBProgressHUDModeText;
    
    hud.label.text = NSLocalizedString(@"已进入后台下载(已下载的内容不会重复下载)", @"HUD message title");
    
    hud.label.numberOfLines = 0;
    
    hud.offset = CGPointMake(0.f, MBProgressMaxOffset);
    
    [hud hideAnimated:YES afterDelay:1.f];
    NSLog(@"下载");
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
    
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:YES animated:NO];
    
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
