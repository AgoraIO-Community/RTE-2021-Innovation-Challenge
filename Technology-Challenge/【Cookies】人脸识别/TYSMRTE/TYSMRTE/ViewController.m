//
//  ViewController.m
//  TYSMRTE
//
//  Created by jele lam on 16/5/2021.
//

#import "ViewController.h"
#import <AgoraRtcKit2/AgoraRtcEngineKit.h>
#import <TYSMExtension/TYSMExtension.h>
#import "zhPopupController.h"
#import "TYSMFeatureView.h"

@interface ViewController () <AgoraMediaFilterEventDelegate,AgoraRtcEngineDelegate>



/// 这个不解释
@property (strong, nonatomic) AgoraRtcEngineKit *agoraKit;
/// 本地 video view
@property (weak, nonatomic) IBOutlet UIView *localVideo;
/// 远程 video view
@property (weak, nonatomic) IBOutlet UIView *remoteVideo;
/// 加入直播间按钮
@property (weak, nonatomic) IBOutlet UIButton *joinButton;
/// 插件功能按钮
@property (weak, nonatomic) IBOutlet UIButton *featureButton;
/// 插件开关按钮
@property (weak, nonatomic) IBOutlet UIButton *extensionButton;

/// 插件功能窗口
@property (nonatomic, strong) TYSMFeatureView *featureView;
@property (nonatomic, strong) zhPopupController *popupController4;
@end

@implementation ViewController


- (void)dealloc {
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self initializeAgoraEngine];
    
    [self setupVideo];
    [self setupLocalVideo];
    
    [self.agoraKit startPreview];

    [self enableEffect];

}

- (void)initializeAgoraEngine {
    
    AgoraRtcEngineConfig *cfg = [AgoraRtcEngineConfig new];
    
    cfg.appId = appId;

    TYSMExtensionManger *manager = [TYSMExtensionManger sharedInstance];
    [manager loadPluginWithVendor:TYSM_VENDOR_NAME_BRF];
    NSArray *plugs = [manager getPlugins];
    cfg.mediaFilterExtensions = plugs;
    
    cfg.eventDelegate = self;
    
    self.agoraKit = [AgoraRtcEngineKit sharedEngineWithConfig:cfg delegate:self];

}

- (void)enableEffect {
    
    for (TYSM_VENDOR_NAME vendorName in TYSMExtensionManger.sharedInstance.vendorNames) {
        if ([vendorName isEqualToString:TYSM_VENDOR_NAME_BRF]) {
            
            NSDictionary *data = @{
                @"brf.init" : @{
                        @"width" : @(AgoraVideoDimension640x480.width),
                        @"height" : @(AgoraVideoDimension640x480.height)
                }
            };
            
            NSError *error;
            NSData *jsonData = [NSJSONSerialization dataWithJSONObject:data options:NSJSONWritingPrettyPrinted error:&error];
            NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
            
            
            [self.agoraKit setExtensionPropertyWithVendor:AgoraMediaSourceTypeVideoCamera vendor:vendorName key:@"key" value:jsonString];
            [self.agoraKit enableExtensionWithVendor:AgoraMediaSourceTypeVideoCamera vendor:vendorName enabled:NO];
        }
    }
    
}

- (void)setupVideo {
    [self.agoraKit setChannelProfile:AgoraChannelProfileLiveBroadcasting];
    [self.agoraKit setClientRole:AgoraClientRoleBroadcaster];
    [self.agoraKit enableVideo];
    //[self.agoraKit startPreview];
    // Set up the configuration such as dimension, frame rate, bit rate and orientation
    AgoraVideoEncoderConfiguration *encoderConfiguration =
    [[AgoraVideoEncoderConfiguration alloc] initWithSize:AgoraVideoDimension640x480
                                               frameRate:AgoraVideoFrameRateFps15
                                                 bitrate:AgoraVideoBitrateStandard
                                         orientationMode:AgoraVideoOutputOrientationModeAdaptative mirrorMode:AgoraVideoMirrorModeAuto];
    [self.agoraKit setVideoEncoderConfiguration:encoderConfiguration];
}

- (IBAction)joinChannel {
    self.joinButton.selected = !self.joinButton.selected;
    
    [self.joinButton setTitle:@"Feature" forState:UIControlStateNormal];
    
    [self.joinButton removeTarget:self action:@selector(joinChannel) forControlEvents:UIControlEventTouchUpInside];
    [self.joinButton addTarget:self action:@selector(tapMenu:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.agoraKit joinChannelByToken:token channelId:channelName info:nil uid:0 joinSuccess:^(NSString *channel, NSUInteger uid, NSInteger elapsed) {
        
        NSLog(@"%@|%lu|%lu",channel, uid, elapsed);
    }];
    
    // The UID database is maintained by your app to track which users joined which channels. If not assigned (or set to 0), the SDK will allocate one and returns it in joinSuccessBlock callback. The App needs to record and maintain the returned value as the SDK does not maintain it.
    
    [self.agoraKit setEnableSpeakerphone:YES];
    [UIApplication sharedApplication].idleTimerDisabled = YES;
    
    
    if (self.extensionButton.selected) return;
    [self tapExtension:self.extensionButton];
}

// ViewController.m
- (void)setupLocalVideo {
    // 启用视频模块
    [self.agoraKit enableVideo];
    AgoraRtcVideoCanvas *videoCanvas = [[AgoraRtcVideoCanvas alloc] init];
    videoCanvas.uid = 0;
    videoCanvas.renderMode = AgoraVideoRenderModeHidden;
    videoCanvas.view = self.localVideo;
    // 设置本地视图
    [self.agoraKit setupLocalVideo:videoCanvas];
}

- (IBAction)switchCamera:(id)sender {
    [self.agoraKit switchCamera];
}

- (IBAction)tapMenu:(id)sender {
 
    if (_popupController4 == nil) {
        self.featureView = [[UINib nibWithNibName:@"TYSMFeatureView" bundle:nil] instantiateWithOwner:nil options:nil].firstObject;
        self.featureView.frame = CGRectMake(0, 0, self.view.bounds.size.width, self.view.bounds.size.width * 0.8);
        __weak typeof(self)weak_self = self;
        self.featureView.tapBlock = ^(id  _Nonnull data) {
            
            for (TYSM_VENDOR_NAME vendorName in TYSMExtensionManger.sharedInstance.vendorNames) {
                if ([vendorName isEqualToString:TYSM_VENDOR_NAME_BRF]) {
                    NSError *error;
                    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:data options:NSJSONWritingPrettyPrinted error:&error];
                    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
                    
                     [weak_self.agoraKit setExtensionPropertyWithVendor:AgoraMediaSourceTypeVideoCamera vendor:vendorName key:@"key" value:jsonString];
                }
            }
        };
        
        
        _popupController4 = [[zhPopupController alloc] initWithView:self.featureView size:self.featureView.bounds.size];
        _popupController4.layoutType = zhPopupLayoutTypeTop;
        _popupController4.presentationStyle = zhPopupSlideStyleFromTop;
        _popupController4.offsetSpacing = -30;
        
        _popupController4.willPresentBlock = ^(zhPopupController * _Nonnull popupController) {
        };
        
        _popupController4.willDismissBlock = ^(zhPopupController * _Nonnull popupController) {
        };
    }
    
    [_popupController4 showInView:self.view.window duration:0.75 bounced:YES completion:nil];
}

- (IBAction)tapExtension:(UIButton *)sender {
    sender.selected = !sender.selected;
    
    [sender setTitle:sender.selected ? @"unload extension" : @"load extension" forState:UIControlStateNormal];
    
    self.featureButton.enabled = sender.selected;
    
    for (TYSM_VENDOR_NAME vendorName in TYSMExtensionManger.sharedInstance.vendorNames) {
            [self.agoraKit enableExtensionWithVendor:AgoraMediaSourceTypeVideoCamera vendor:vendorName enabled:sender.selected];
    }
    
}


#pragma mark - delegate
/// Callback to handle the event such when the first frame of a remote video stream is decoded on the device.
/// @param engine - RTC engine instance
/// @param uid - user id
/// @param size - the height and width of the video frame
/// @param elapsed - lapsed Time elapsed (ms) from the local user calling JoinChannel method until the SDK triggers this callback.
- (void)rtcEngine:(AgoraRtcEngineKit *)engine firstRemoteVideoDecodedOfUid:(NSUInteger)uid size: (CGSize)size elapsed:(NSInteger)elapsed {
    //  if (self.remoteVideo.hidden) {
    //    self.remoteVideo.hidden = NO;
    //  }
    
    AgoraRtcVideoCanvas *videoCanvas = [[AgoraRtcVideoCanvas alloc] init];
    videoCanvas.uid = uid;
    // Since we are making a simple 1:1 video chat app, for simplicity sake, we are not storing the UIDs. You could use a mechanism such as an array to store the UIDs in a channel.
    
    videoCanvas.view = self.remoteVideo;
    videoCanvas.renderMode = AgoraVideoRenderModeHidden;
    [self.agoraKit setupRemoteVideo:videoCanvas];
    // Bind remote - (void)onEvent:(NSString * _Nullable)vendor key:(NSString * _Nullable)key json_value:(NSString * _Nullable)json_value {
    
}

- (void)rtcEngine:(AgoraRtcEngineKit *)engine tokenPrivilegeWillExpire:(NSString *)token {
    NSLog(@"%s",__func__);
}

- (void)rtcEngineRequestToken:(AgoraRtcEngineKit *)engine {
    NSLog(@"%s",__func__);
    if ([engine renewToken:@"006b11196b76c2548c294fd08cd9f51d0bdIACjyx4P9bIYpmQbSx2jK/LnQkin6EqcW0Oxao5q/TkbCo+p48AAAAAAEAB33sfL/fijYAEAAQD8+KNg"]) {
        NSLog(@"YES");
    };
}

- (void)rtcEngine:(AgoraRtcEngineKit *)engine didJoinedOfUid:(NSUInteger)uid elapsed:(NSInteger)elapsed {
    NSLog(@"远端主播加入");
}

#pragma mark - AgoraMediaFilterEventDelegate

- (void)onEvent:(NSString *)vendor key:(NSString *)key json_value:(NSString *)json_value {
    NSLog(@"%@ %@ %@",vendor,key,json_value);
    if (self.extensionButton.enabled && [key containsString:@"brf"] ) {
        [self.featureView setResultWith:json_value];
    }
}

@end
