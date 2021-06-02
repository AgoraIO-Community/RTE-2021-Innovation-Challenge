//
//  ViewController.m
//  Baby back home
//
//  Created by zhangyu on 2021/5/12.
//

#import "ViewController.h"
// 导入 AgoraRtcKit 类
// 自 3.0.0 版本起，AgoraRtcEngineKit 类名更换为 AgoraRtcKit
// 如果获取的是 3.0.0 以下版本的 SDK，请改用 #import <AgoraRtcEngineKit/AgoraRtcEngineKit.h>
#import <AgoraRtcKit/AgoraRtcEngineKit.h>
#import "EMChatViewController.h"
@interface ViewController ()<AgoraRtcEngineDelegate>

// 定义 localView 变量
@property (nonatomic, strong) UIView *localView;
// 定义 remoteView 变量
@property (nonatomic, strong) UIView *remoteView;

// 定义 agoraKit 变量
@property (strong, nonatomic) AgoraRtcEngineKit *agoraKit;


@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = UIColor.whiteColor;
    
    // 调用初始化视频窗口函数
        [self initViews];
        // 后续步骤调用 Agora API 使用的函数
        [self initializeAgoraEngine];
        [self setupLocalVideo];
        [self joinChannel];
        [self closeVideoChat];
    // Do any additional setup after loading the view.
}

- (void)closeVideoChat{
    UIBarButtonItem * leftBtn = [[UIBarButtonItem alloc] initWithTitle:@"返回" style:(UIBarButtonItemStyleDone) target:self action:@selector(leaveChannel)];
    self.navigationItem.leftBarButtonItem = leftBtn;

}



// 设置视频窗口布局
- (void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    self.remoteView.frame = self.view.bounds;
    self.localView.frame = CGRectMake(self.view.bounds.size.width - 90, 100, 90, 160);
}
- (void)initViews {
    // 初始化远端视频窗口
    self.remoteView = [[UIView alloc] init];
    [self.view addSubview:self.remoteView];
    // 初始化本地视频窗口
    self.localView = [[UIView alloc] init];
    self.localView.backgroundColor = UIColor.blackColor;
    [self.view addSubview:self.localView];
}

- (void)initializeAgoraEngine {
    self.agoraKit = [AgoraRtcEngineKit sharedEngineWithAppId:@"ffb21f338d0b4443a7bf785efc867bc4" delegate:self];
}

- (void)setupLocalVideo {
    // 启用视频模块
    [self.agoraKit enableVideo];
    AgoraRtcVideoCanvas *videoCanvas = [[AgoraRtcVideoCanvas alloc] init];
    videoCanvas.uid = 0;
    videoCanvas.renderMode = AgoraVideoRenderModeHidden;
    videoCanvas.view = self.localView;
    // 设置本地视图
    [self.agoraKit setupLocalVideo:videoCanvas];
}

- (void)joinChannel {
    // 频道内每个用户的 uid 必须是唯一的
    [self.agoraKit joinChannelByToken:@"006ffb21f338d0b4443a7bf785efc867bc4IABB4Vv/QEowJpd+MBP/R1jui/xKHAoKfwGeEJDrmJxXZAx+f9gAAAAAEAB33sfL/CSmYAEAAQAUJaZg" channelId:@"test" info:nil uid:0 joinSuccess:^(NSString * _Nonnull channel, NSUInteger uid, NSInteger elapsed) {
}];
}

// 监听 didJoinedOfUid 回调
// 远端用户加入频道时，会触发该回调
- (void)rtcEngine:(AgoraRtcEngineKit *)engine didJoinedOfUid:(NSUInteger)uid elapsed:(NSInteger)elapsed {
    AgoraRtcVideoCanvas *videoCanvas = [[AgoraRtcVideoCanvas alloc] init];
    videoCanvas.uid = uid;
    videoCanvas.renderMode = AgoraVideoRenderModeHidden;
    videoCanvas.view = self.remoteView;
    // 设置远端视图
    [self.agoraKit setupRemoteVideo:videoCanvas];
}

//离开频道
- (void)leaveChannel {
    [self.agoraKit leaveChannel:nil];
    [self.navigationController popViewControllerAnimated:YES];
}



@end
