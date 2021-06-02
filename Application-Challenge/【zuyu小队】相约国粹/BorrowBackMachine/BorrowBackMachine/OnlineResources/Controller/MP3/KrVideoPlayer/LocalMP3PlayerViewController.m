//
//  LocalMP3PlayerViewController.m
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/9/8.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#define WIDTH ([UIScreen mainScreen].bounds.size.width)
#define HEIGHT ([UIScreen mainScreen].bounds.size.height)


#import "LocalMP3PlayerViewController.h"
#import "ThirdMp3ButtonTableViewCell.h"
#import "LocalMP3forthViewController.h"
#import "zuyu.h"

@interface LocalMP3PlayerViewController ()<UITableViewDelegate,UITableViewDataSource,NavgationViewDelegate>
{
    UITableView *_tableView;
   
    UIView *_videoView;
    
    UILabel *_timeLable;
    
    UILabel *_nameLable;
    
    NSInteger _stopOrStar;
}
@property (nonatomic, strong) NSTimer *durationTimer;

@end

@implementation LocalMP3PlayerViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    [self UIBarButtonBackItemSet];
    
    [self createNavgation];
    
    [self createTableView];
    
    [self playVideo];
    
    // Do any additional setup after loading the view.
}

#pragma mark - navgation
-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.titleStr = @"本地音频";
    [self.view addSubview:view];
}


-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}
-(void)createTableView
{
    _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 74, WIDTH,HEIGHT - 140 + 74 ) ];

    _tableView.delegate = self;
    
    _tableView.dataSource = self;
    
    _tableView.rowHeight = 70;
    
    _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
    NSIndexPath *firstPath = [NSIndexPath indexPathForRow:_indexRow inSection:0];
    
    [_tableView selectRowAtIndexPath:firstPath animated:NO scrollPosition:UITableViewScrollPositionNone];
    
    [self.view addSubview:_tableView];
    
}



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _URLArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    static NSString *CellIdentifier = @"cell";
    
    ThirdMp3ButtonTableViewCell *cell =(ThirdMp3ButtonTableViewCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    
    if (cell==nil) {
        
        cell = [[ThirdMp3ButtonTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
                                                   reuseIdentifier:CellIdentifier];
    }
    if (indexPath.row == 0) {
        _nameLable.text = [self decodeString:_nameArray[indexPath.row]];
        
    }
    
    
    cell.nameLable.text = [self decodeString:_nameArray[indexPath.row]];
    
    cell.nameLable.highlightedTextColor = [UIColor orangeColor];
    
    cell.image.highlightedImage = [UIImage imageNamed:@"play_btn_hover"];
    
    cell.sizeLable.hidden = YES;
    
    cell.downImage.hidden = YES;
    
    cell.readLable.hidden = YES;
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    _indexRow = indexPath.row;
    
    _nameLable.text = [self decodeString:_nameArray[_indexRow]];
    
    [self playVideo];
}





#pragma mark - 播放器
- (void)playVideo{
    
    
    NSURL *url = [NSURL fileURLWithPath:_URLArray[_indexRow]];
    
    [self addVideoPlayerWithURL:url];
   
}


- (void)addVideoPlayerWithURL:(NSURL *)url{
    
    
    if (self.videoController == nil) {
        
        CGSize sizes = [[UIScreen mainScreen] bounds].size;
        
        CGFloat width = [UIScreen mainScreen].bounds.size.width;
        
        CGFloat viewFeigthFrame = sizes.height - 104 + 28;
        
        
        if (sizes.width == 320) {
            //
            
            self.videoController = [[KrVideoPlayerController alloc] initWithFrame:CGRectMake(-15, 35, width + sizes.width/10 - 5 , 40)];
        }else{
            
            self.videoController = [[KrVideoPlayerController alloc] initWithFrame:CGRectMake(-15, 35, width + sizes.width/10 - 5 , 40)];
            //
            //            self.videoController.backgroundView.alpha = 1.0;
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
        
        
        
        _timeLable  = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH * 0.7,15,WIDTH * 0.3,40)];
        
        _timeLable.textAlignment = NSTextAlignmentCenter;
        
        if (WIDTH == 320) {
            _timeLable.font = [UIFont systemFontOfSize:12];
        }
        
        [self startDurationTimer];
        
        [self monitorVideoPlayback];
        
        [_videoView addSubview:_timeLable];
        

        _nameLable = [[UILabel alloc] initWithFrame:CGRectMake(70, 15, WIDTH * 0.5, 40)];
        
        _nameLable.font = [UIFont systemFontOfSize:14];
        
        [_videoView addSubview:_nameLable];
        
        //        [ [ [ UIApplication  sharedApplication ]  keyWindow ] addSubview : _videoView];
        [self.view addSubview:_videoView];
        
        [_videoView addSubview:self.videoController.view];
        
        
        self.videoController.videoControl.timeLabel.hidden = YES;
        
        self.videoController.videoControl.pauseButton.alpha = 0.0;
        
        self.videoController.videoControl.playButton.alpha = 0.0;
        
        self.videoController.videoControl.fullScreenButton.alpha = 0.0;
        
        //        [ [ [ UIApplication  sharedApplication ]  keyWindow ] addSubview : self.videoController.view];
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
    double minutesElapsed = floor(currentTime / 60.0);
    double secondsElapsed = fmod(currentTime, 60.0);
    NSString *timeElapsedString = [NSString stringWithFormat:@"%02.0f:%02.0f", minutesElapsed, secondsElapsed];
    
    double minutesRemaining = floor(totalTime / 60.0);;
    double secondsRemaining = floor(fmod(totalTime, 60.0));;
    NSString *timeRmainingString = [NSString stringWithFormat:@"%02.0f:%02.0f", minutesRemaining, secondsRemaining];
    
    
    _timeLable.text =   [NSString stringWithFormat:@"%@/%@",timeElapsedString,timeRmainingString];
}


- (void)startDurationTimer
{
    self.durationTimer = [NSTimer scheduledTimerWithTimeInterval:0.2 target:self selector:@selector(monitorVideoPlayback) userInfo:nil repeats:YES];
    [[NSRunLoop currentRunLoop] addTimer:self.durationTimer forMode:NSDefaultRunLoopMode];
}

//隐藏navigation tabbar 电池栏
- (void)toolbarHidden:(BOOL)Bool{
    self.navigationController.navigationBar.hidden = Bool;
    self.tabBarController.tabBar.hidden = Bool;
    [[UIApplication sharedApplication] setStatusBarHidden:Bool withAnimation:UIStatusBarAnimationFade];
}



#pragma mark - decodeURI
-(NSString *)decodeString:(NSString*)encodedString
{
    //NSString *decodedString = [encodedString stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding ];
    
    NSString *decodedString = (__bridge_transfer NSString *)CFURLCreateStringByReplacingPercentEscapesUsingEncoding(NULL,
                                                                                                                    (__bridge CFStringRef)encodedString,
                                                                                                                    CFSTR(""),
                                                                                                                    CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding));
    return decodedString;
}

#pragma mark -  pushLocalMP3forthViewController

-(void)pushForthMp3VC:(UIButton *)button
{
    LocalMP3forthViewController *vc = [[LocalMP3forthViewController alloc] init];
    
    vc.dataArray = self.URLArray;
    vc.nameArray = self.nameArray;
    vc.chooseID = _indexRow;
    
    [self.navigationController pushViewController:vc animated:YES];
    
}

#pragma mark - system

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:NO];

    _stopOrStar = 0;

}

-(void)viewWillDisappear:(BOOL)animated
{
    [super  viewWillDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:NO];

    [_videoController dismiss];
    
}


#pragma mark - 导航栏返回按钮颜色设置 及 文字变成<
//  导航栏返回按钮颜色设置 及 文字变成<
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
