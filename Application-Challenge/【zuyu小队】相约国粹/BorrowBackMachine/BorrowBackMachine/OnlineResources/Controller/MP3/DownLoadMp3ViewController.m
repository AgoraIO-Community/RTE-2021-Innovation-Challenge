//
//  DownLoadMp3ViewController.m
//  SkyFarmingBookshop
//
//  Created by zuyu on 16/6/1.
//  Copyright © 2016年 zuyu. All rights reserved.
//



#import "DownLoadMp3ViewController.h"
#import "KrVideoPlayerController.h"
#import "ZFDownloadViewController.h"
#import "ALLDownloadListCeViewController.h"
#import "ZFPlayer.h"
#import "Masonry.h"

@interface DownLoadMp3ViewController ()<UITableViewDelegate,UITableViewDataSource,ZFPlayerDelegate>
{
    UITableView *_tableView;
    
}
@property (nonatomic, strong) KrVideoPlayerController  *videoController;


@property (strong, nonatomic)  UIView *playerFatherView;
@property (strong, nonatomic) ZFPlayerView *playerView;
/** 离开页面时候是否在播放 */
@property (nonatomic, assign) BOOL isPlaying;
@property (nonatomic, strong) ZFPlayerModel *playerModel;

@end

@implementation DownLoadMp3ViewController



- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self.navigationController setNavigationBarHidden:YES animated:NO];
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    [self playVideo];
    
    [self createListTableView];
}
- (void)playVideo{
    
    self.playerFatherView = [[UIView alloc] init];
    
    [self.view addSubview:self.playerFatherView];
    
    self.playerFatherView.frame = CGRectMake(0, 0, WIDTH, HEIGHT);
    
    [self.playerFatherView addSubview:self.playerView];
    
    [self.playerView autoPlayTheVideo];
    
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
        
        NSURL *url = [NSURL fileURLWithPath:self.dataArray[_chooseID]];

        _playerModel                  = [[ZFPlayerModel alloc] init];
        _playerModel.title            = @"";
        _playerModel.videoURL         = url;
        _playerModel.placeholderImage = [UIImage imageNamed:@"loading_bgView1"];
        _playerModel.fatherView       = self.playerFatherView;
        
    }
    return _playerModel;
}


#pragma mark - 获取当前播放时间
-(void)backSecond:(NSInteger)sccond
{
    
}

- (void)zf_playerBackAction {
    [self.navigationController popViewControllerAnimated:YES];
}



//- (void)addVideoPlayerWithURL:(NSURL *)url{
//
//
//    if (!self.videoController) {
//        CGFloat height = [[UIScreen mainScreen] bounds].size.width;
//        CGFloat width = [[UIScreen mainScreen] bounds].size.height;
//        CGRect frame = CGRectMake((height - width) / 2, (width - height) / 2, width, height);
//
//        self.videoController = [[KrVideoPlayerController alloc] initWithFrame:frame];
//
//        [self.videoController.view setTransform:CGAffineTransformMakeRotation(M_PI_2)];
//
////
//        self.videoController.videoControl.shrinkScreenButton.hidden  = YES;
//
//        self.videoController.videoControl.fullScreenButton.hidden  = YES;
//
//        UIButton *listButton = [UIButton buttonWithType:UIButtonTypeCustom];
//
//        listButton.frame = CGRectMake(HEIGHT - 40 , WIDTH - 40, 40, 40);
//
//        [listButton setTitle:@"选集" forState:UIControlStateNormal];
//
//        [listButton addTarget:self action:@selector(xuanjiClick:) forControlEvents:UIControlEventTouchUpInside];
//
//        [self.videoController.videoControl addSubview:listButton];
//
//        [self.view addSubview:self.videoController.view];
// 
//    }
//    self.videoController.contentURL = url;
//
//}


-(void)xuanjiClick:(UIButton *)button
{
    if (_tableView.hidden) {
        _tableView.hidden = NO;

    }else{
        _tableView.hidden = YES;
    }
    

}

-(void)createListTableView
{
    _tableView = [[UITableView alloc] initWithFrame:CGRectMake(HEIGHT/4 * 3, 40, HEIGHT/4, WIDTH - 80) style:UITableViewStylePlain];
    
    _tableView.backgroundColor = [UIColor blackColor];
    
    _tableView.alpha = 0.6;
    
    _tableView.hidden = YES;
    
    _tableView.delegate = self;
    
    _tableView.dataSource = self;
    
    [ self.videoController.videoControl addSubview : _tableView] ;
    
    UIView *topView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, HEIGHT, 40)];
    
    topView.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.2];
    
    
    UIButton *backButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    backButton.frame = CGRectMake(20 ,0, 40, 40);
    
    [backButton setTitle:@"←" forState:UIControlStateNormal];
    
    [backButton addTarget:self action:@selector(doBack:) forControlEvents:UIControlEventTouchUpInside];
    
    [topView addSubview:backButton];
    
    [ self.videoController.videoControl addSubview : topView] ;

    
}

 
-(void)doBack:(UIButton *)button
{
    
    [self.navigationController popViewControllerAnimated:YES];
    

}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _dataArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identifier = @"cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
    }
    
    cell.textLabel.text =  [self decodeString:_nameArray[indexPath.row]];
    
    cell.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.1];
    
    cell.textLabel.highlightedTextColor = [UIColor blueColor];
    
    cell.textLabel.textColor = [UIColor whiteColor];
    
    return cell;

}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    NSLog(@"1111111---------");
    
    _chooseID = indexPath.row;
    
    _tableView.hidden = YES;
    
    [self playVideo];
    
}



#pragma mark - nav 处理.
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:NO];
}


- (void) viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    [_videoController dismiss];

    [self.navigationController setNavigationBarHidden:NO animated:NO];
}




- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(NSString *)decodeString:(NSString*)encodedString

{
    //NSString *decodedString = [encodedString stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding ];
    
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
