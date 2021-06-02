//
//  SingleResourcesVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/22.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "SingleResourcesVC.h"
#import "ZFPlayer.h"
#import "AFNetworking.h"
#import "MainMP4ListCell.h"
#import "MBProgressHUD.h"
#import "ScanDownloadViewController.h"
#import "DownloadData.h"
#import "ZFDownloadManager.h"
#import "zuyu.h"


@interface SingleResourcesVC ()<ZFPlayerDelegate,MainMP4ListCellDelegate,UITextViewDelegate>
{
    NSMutableArray *_dataArray;
    MBProgressHUD *_hud;
    
}

@property (strong, nonatomic)  UIView *playerFatherView;
@property (strong, nonatomic) ZFPlayerView *playerView;
/** 离开页面时候是否在播放 */
@property (nonatomic, assign) BOOL isPlaying;
@property (nonatomic, strong) ZFPlayerModel *playerModel;

@end

@implementation SingleResourcesVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [[NSNotificationCenter defaultCenter]addObserver:self
                                            selector:@selector(NetWorkStatesChange:) name:@"netWorkChange"
                                              object:nil];
    
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
    
    // Do any additional setup after loading the view.
}

-(void)selfInit
{
    
    _dataArray = [NSMutableArray array];
    
    [self UIBarButtonBackItemSet];
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    [self playVideo];
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    
    button.frame = CGRectMake(0, HEIGHT - 50, WIDTH, 50);
    
    [button setTitle:@"下载" forState: UIControlStateNormal];
    
    [button addTarget:self action:@selector(downloadClick: ) forControlEvents:UIControlEventTouchUpInside];
    
    button.backgroundColor = COLOR;
    
    [self.view addSubview:button];
    
    UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(12, WIDTH/16 * 9 + 20, WIDTH, 30)];
    
    titleLabel.text = self.titleStr;
    
    titleLabel.font = [UIFont systemFontOfSize:20];
    
    [self.view addSubview:titleLabel];
    if (self.summary.length) {
        UITextView *textView = [[UITextView alloc] initWithFrame:CGRectMake(0, WIDTH/16 * 9 + 60, WIDTH, 200)];
        
        textView.text = self.summary;
        
        [textView setEditable:NO];
        
        textView.font = [UIFont systemFontOfSize:15];
        
        textView.delegate = self;
        
        [self.view addSubview:textView];
        
    }
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

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
    
    return NO;
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    self.tabBarController.tabBar.hidden = YES;
    [self.navigationController setNavigationBarHidden:YES animated:NO];
    
    if (_playerView) {
        [_playerView play];
        
    }
    
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    
    // Dispose of any resources that can be recreated.
}


-(void)playVideoUrl:(NSString *)url WithTitle:(NSString *)title
{
    _playerModel.title            = title;
    _playerModel.videoURL         = [NSURL URLWithString:url];
    [_playerView resetToPlayNewVideo:_playerModel];
    
}
#pragma mark - ZFPlayer

-(void)playVideo{
    
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
        _playerModel                  = [[ZFPlayerModel alloc] init];
        _playerModel.title            = self.titleStr;
        _playerModel.videoURL         = [NSURL URLWithString:self.rscourceUrl] ;
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


-(void)downloadClick:(UIButton *)button
{
//    self.rscourceUrl = @"http://211.145.50.11:82/jyj_mx//resource/04%E7%BB%98%E7%94%BB%E5%88%86%E9%A6%86/%E8%B7%9F%E5%BE%90%E6%B9%9B%E5%AD%A6%E5%9B%BD%E7%94%BB/%E8%B7%9F%E5%BE%90%E6%B9%9B%E8%80%81%E5%B8%88%E5%AD%A6%E5%9B%BD%E7%94%BB(%E7%AC%AC1%E9%9B%86%20%E5%86%99%E6%84%8F%E8%8A%B1%E9%B8%9F%E7%94%BB%E7%9A%84%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86).mp4";
    
    if ([ISLOGIN integerValue]) {
        
        ZFDownloadManager *downManger = [ZFDownloadManager sharedDownloadManager];
        
        //    NSString *bookCeName  =  [[[NSString stringWithFormat:@"%@",self.rscourceUrl] componentsSeparatedByString:@"/"] lastObject];
        
        
        NSString * bookCeName = [NSString stringWithFormat:@"%@.mp4",self.titleStr];
        
        //编码
        bookCeName = [bookCeName stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        
        
        [downManger downFileUrl:self.rscourceUrl filename:bookCeName fileimage:nil bookName:self.titleStr classID:self.classType];
        
        downManger.maxCount = 1;
        
        MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
        
        hud.mode = MBProgressHUDModeText;
        
        hud.label.text = NSLocalizedString(@"已进入后台下载(已下载的内容不会重复下载)", @"HUD message title");
        
        hud.label.numberOfLines = 0;
        
        hud.offset = CGPointMake(0.f, MBProgressMaxOffset);
        
        [hud hideAnimated:YES afterDelay:1.f];
    }else{
        [ZuyuAlertShow alertShow:@"请先登录" viewController: self];
    }
    
}
#pragma mark - 获取当前播放时间
-(void)backSecond:(NSInteger)sccond
{
    
    
}



#pragma mark - pop/ 记录播放时间.
- (void)zf_playerBackAction {
    //    NSLog(@"%ld" ,_nowSecond);
    //    [self setLookedWithSecond:_nowSecond];
    [self.navigationController popViewControllerAnimated:YES];
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
