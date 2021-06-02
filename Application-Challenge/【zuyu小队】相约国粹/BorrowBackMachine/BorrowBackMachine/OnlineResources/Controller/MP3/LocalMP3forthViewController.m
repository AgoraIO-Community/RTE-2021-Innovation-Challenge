//
//  LocalMP3forthViewController.m
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/9/9.
//  Copyright © 2016年 zuyu. All rights reserved.
//

#define WIDTH ([UIScreen mainScreen].bounds.size.width)
#define HEIGHT ([UIScreen mainScreen].bounds.size.height)

#import "LocalMP3forthViewController.h"
#import "LocalMP3PlayerViewController.h"


@interface LocalMP3forthViewController ()
{
    UIView *_videoView;
    
    UILabel *_nameLable;
    
    UILabel *_leftTimeLable;
    
    UILabel *_rightTimeLable;
    
    UILabel *_nameCeLable;
    
    UIButton *_button;
    
    int _stopOrStar;
    
}

@property (nonatomic, strong) NSTimer *durationTimer;

@end

@implementation LocalMP3forthViewController

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    self.tabBarController.tabBar.hidden = YES;
    
    self.navigationController.navigationBar.barTintColor = [UIColor colorWithRed:107.f/255.f green:91.f/255.f blue:75.f/255.f alpha:1];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor colorWithRed:107.f/255.f green:91.f/255.f blue:75.f/255.f alpha:1];
    
    
    
    
    self.title = @"音频播放";
    
    [[UINavigationBar appearance] setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor whiteColor]}];

    
    _stopOrStar = 0;
    
    [self createImageView];
    
    
    // Do any additional setup after loading the view.
}


-(void)createImageView
{
    
    _nameCeLable =[[UILabel alloc] initWithFrame:CGRectMake(WIDTH/5, 44 + HEIGHT/4, WIDTH/5*3, HEIGHT/4)];
    
    _nameCeLable.text = [self decodeString: self.nameArray[_chooseID]];
    
    _nameCeLable.textAlignment = NSTextAlignmentCenter;
    
    [self.view addSubview:_nameCeLable];
    
    [self playVideo];
    
}


#pragma mark - 播放器
- (void)playVideo{
    
    
    NSURL *url = [NSURL fileURLWithPath:_dataArray[_chooseID]];
    
    [self addVideoPlayerWithURL:url];
    
}

- (void)addVideoPlayerWithURL:(NSURL *)url{
    
    
    if (!self.videoController) {
        
        CGSize sizes = [[UIScreen mainScreen] bounds].size;
        
        CGFloat width = [UIScreen mainScreen].bounds.size.width;
        
        CGFloat viewFeigthFrame = HEIGHT - HEIGHT/3.5;
        
        
        if (sizes.width == 320) {
            self.videoController = [[KrVideoPlayerController alloc] initWithFrame:CGRectMake(0, 35, width, 200)];
        }else{
            
            self.videoController = [[KrVideoPlayerController alloc] initWithFrame:CGRectMake(0, 1, width, 40)];
            
        }
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
        
        
        
        self.videoController.view.backgroundColor = [UIColor clearColor];
        
        _videoView = [[UIView alloc] initWithFrame:CGRectMake(0, viewFeigthFrame, width , HEIGHT/3.5)];
        
        //        _videoView.backgroundColor = [UIColor colorWithRed:157.f/255.f green:202.f/255.f blue:103.f/255.f alpha:1];
        
        _videoView.backgroundColor = [UIColor colorWithRed:70.f/255.f green:60.f/255.f blue:50.f/255.f alpha:0.9];;
        
        
        _button = [UIButton buttonWithType:UIButtonTypeCustom];
        
        _button.frame = CGRectMake(10 + WIDTH/5 * 2, HEIGHT/3.5/2, WIDTH/5 - 20, WIDTH/5 - 20);
        
        [_button setImage:[UIImage imageNamed:@"movie_stop_bt_item"] forState:UIControlStateNormal];
        
        [_button addTarget:self action:@selector(stop:) forControlEvents:UIControlEventTouchUpInside];
        
        [_videoView addSubview:_button];
        
        if (WIDTH == 320) {
            _leftTimeLable.font = [UIFont systemFontOfSize:12];
            
            _rightTimeLable.font = [UIFont systemFontOfSize:12];
            
        }
        
        [self startDurationTimer];
        
        [self monitorVideoPlayback];
        
        _nameLable = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH/5/2, 40, WIDTH - (WIDTH/5*3 + WIDTH/5/2), 20)];
        
        [_videoView addSubview:_nameLable];
        
        self.videoController.videoControl.progressSlider.frame = CGRectMake(WIDTH/5, 35, WIDTH/5*3, 10);
        
        _leftTimeLable = [[UILabel alloc] initWithFrame:CGRectMake( WIDTH/15, 33, WIDTH/5, 15)];
        
        [_videoView addSubview:_leftTimeLable];
        
        _rightTimeLable = [[UILabel alloc] initWithFrame:CGRectMake(   WIDTH/5*4 +8, 33, WIDTH/5, 15)];
        
        [_videoView addSubview:_rightTimeLable];
        
        [_videoView addSubview: self.videoController.videoControl.progressSlider];
        
        //        [ [ [ UIApplication  sharedApplication ]  keyWindow ] addSubview : _videoView];
        
        [self createPlayerViewSubButton];
        
        [self.view addSubview:_videoView];
        
        //        [_videoView addSubview:self.videoController.view];
        
        self.videoController.videoControl.timeLabel.hidden = YES;
        
        self.videoController.videoControl.pauseButton.alpha = 0.0;
        
        self.videoController.videoControl.playButton.alpha = 0.0;
        
        self.videoController.videoControl.fullScreenButton.alpha = 0.0;
        
        //        [_videoView addSubview:self.videoController.view];
        
        // [ [ [ UIApplication  sharedApplication ]  keyWindow ] addSubview : self.videoController.view];
    }
    
    self.videoController.contentURL = url;
    
}

#pragma mark - playerViewSubViews Button


-(void)createPlayerViewSubButton
{
    float buttonWidth = WIDTH/5 - 30;
    
    UIButton *backListButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    backListButton.frame = CGRectMake(15, HEIGHT/7, buttonWidth, buttonWidth);
    
    [backListButton setImage:[UIImage imageNamed:@"list_item_books"] forState:UIControlStateNormal];
    
    [backListButton addTarget:self action:@selector(backListClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [_videoView addSubview:backListButton];
    
    UIButton *lastButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    lastButton.frame = CGRectMake(15 + WIDTH/5, HEIGHT/7, buttonWidth, buttonWidth);
    
    [lastButton setImage:[UIImage imageNamed:@"movie_next_bt_item"] forState:UIControlStateNormal];
    
    [lastButton addTarget:self action:@selector(lastClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [_videoView addSubview:lastButton];
    
    UIButton *nextButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    nextButton.frame = CGRectMake(15 + WIDTH/5 * 3, HEIGHT/7, buttonWidth, buttonWidth);
    
    [nextButton setImage:[UIImage imageNamed:@"movie_last_bt_item"] forState:UIControlStateNormal];
    
    [nextButton addTarget:self action:@selector(nextClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [_videoView addSubview:nextButton];
    
    
    
    
    UIButton *downloadButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    downloadButton.frame = CGRectMake(15 + WIDTH/5 * 4, HEIGHT/7, buttonWidth, buttonWidth);
    
    [downloadButton setImage:[UIImage imageNamed:@"list_item_downs"] forState:UIControlStateNormal];
    
    [downloadButton addTarget:self action:@selector(downLoadClick:) forControlEvents:UIControlEventTouchUpInside];
    
//    [_videoView addSubview:downloadButton];
    
}

//downLoadClick

-(void)downLoadClick:(UIButton *)button
{
   
    
    
}



//backListClick

-(void)backListClick:(UIButton *)button
{
    NSArray *temArray = self.navigationController.viewControllers;
    
    for(UIViewController *temVC in temArray)
        
    {
        
        if ([temVC isKindOfClass:[LocalMP3PlayerViewController class]])
            
        {
            
            [self.navigationController popToViewController:temVC animated:YES];
            
        }
        
        //
    }
}


//lastClick

-(void)lastClick:(UIButton *)button
{
    if (_chooseID) {
        _chooseID--;
    }else{
        _chooseID = self.dataArray.count - 1;
    }
    
    _nameCeLable.text =  [self decodeString: self.nameArray[_chooseID]];
    
    [_button setImage:[UIImage imageNamed:@"movie_stop_bt_item"] forState:UIControlStateNormal];
    
    _stopOrStar = 0;
    
    
    [self playVideo];
    
}

//nextClick

-(void)nextClick:(UIButton *)button
{
    if (_chooseID == self.dataArray.count-1) {
        _chooseID = 0;
    }else{
        _chooseID++;
    }
    
    
    _nameCeLable.text =  [self decodeString: self.nameArray[_chooseID]];
    
    [_button setImage:[UIImage imageNamed:@"movie_stop_bt_item"] forState:UIControlStateNormal];
    
    _stopOrStar = 0;
    
    [self playVideo];
}

#pragma mark - ------------------


//隐藏navigation tabbar 电池栏
- (void)toolbarHidden:(BOOL)Bool{
    self.navigationController.navigationBar.hidden = Bool;
    self.tabBarController.tabBar.hidden = Bool;
    //    [[UIApplication sharedApplication] setStatusBarHidden:Bool withAnimation:UIStatusBarAnimationFade];
}
//暂停播放
-(void)stop:(UIButton *)button
{
    if(_stopOrStar%2){
        
        [self.videoController play];
        
        [button setImage:[UIImage imageNamed:@"movie_stop_bt_item"] forState:UIControlStateNormal];
        //        movie_play_bt_item
    }else{
        
        [button setImage:[UIImage imageNamed:@"movie_play_bt_item"] forState:UIControlStateNormal];
        
        [self.videoController pause];
        
    }
    _stopOrStar++;
    
}

- (void)monitorVideoPlayback
{
    double currentTime = floor(self.videoController.currentPlaybackTime);
 
    double totalTime = floor(self.videoController.duration);
  
    [self setTimeLabelValues:currentTime totalTime:totalTime];
    //        self.videoController.videoControl.progressSlider.value = ceil(currentTime);
}

- (void)setTimeLabelValues:(double)currentTime totalTime:(double)totalTime {
    double minutesElapsed = floor(currentTime / 60.0);
    double secondsElapsed = fmod(currentTime, 60.0);
    NSString *timeElapsedString = [NSString stringWithFormat:@"%02.0f:%02.0f", minutesElapsed, secondsElapsed];
    
    double minutesRemaining = floor(totalTime / 60.0);;
    double secondsRemaining = floor(fmod(totalTime, 60.0));;
    NSString *timeRmainingString = [NSString stringWithFormat:@"%02.0f:%02.0f", minutesRemaining, secondsRemaining];
    
    _leftTimeLable.text = [NSString stringWithFormat:@"%@",timeElapsedString];
    
    _rightTimeLable.text = [NSString stringWithFormat:@"%@",timeRmainingString];
    
}

- (void)startDurationTimer
{
    self.durationTimer = [NSTimer scheduledTimerWithTimeInterval:0.2 target:self selector:@selector(monitorVideoPlayback) userInfo:nil repeats:YES];
    [[NSRunLoop currentRunLoop] addTimer:self.durationTimer forMode:NSDefaultRunLoopMode];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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


-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    self.navigationController.navigationBar.barTintColor = [UIColor colorWithRed:20.f/255.f green:175.f/255.f blue:255.f/255.f alpha:1];
    
    [_videoController dismiss];
    
    _videoView.hidden = YES;
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
