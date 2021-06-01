//
//  QHTableSubViewController.m
//  QHTableViewDemo
//
//  Created by chen on 17/3/21.
//  Copyright © 2017年 chen. All rights reserved.
//

#import "QHGameRoomViewController.h"

#import "QHAppContext.h"

#import <HyphenateChat/HyphenateChat.h>
#import <iflyMSC/iflyMSC.h>

#import "QHTKChatRoomView.h"
#import "QHRTCKitManager.h"
#import "QHAudioConverter.h"
#import "QHSpeakerManager.h"
#import "QHHyphenateChatManager.h"
#import "QHHyphenateChatManagerDefine.h"
#import "NSTimer+EOCBlocksSupport.h"
#import "MBProgressHUD+Add.h"
#import "QHLinkMicAuthorizationView.h"
#import "QHHelpView.h"

#define K_IFLY_TIME 2
#define K_GAME_FLOW @"游戏流程：\r\n(1)双方进入连麦\r\n(2)房主创建游戏\r\n(3)房客加入游戏\r\n(4)房主开始游戏\r\n(5)双方轮流答题，由房主先\r\n(6)点击答题后，3秒内说出答案"

@interface QHGameRoomViewController () <EMChatManagerDelegate, QHChatBaseViewDelegate, QHRTCKitManagerDelegate, IFlySpeechRecognizerDelegate, QHAudioConverterDelegate>

@property (weak, nonatomic) IBOutlet UIView *chatSuperV;
@property (weak, nonatomic) IBOutlet UIButton *readyBtn;
@property (weak, nonatomic) IBOutlet UIButton *answerBtn;
@property (weak, nonatomic) IBOutlet UIButton *goGameBtn;
@property (weak, nonatomic) IBOutlet UIView *preview;
@property (weak, nonatomic) IBOutlet UILabel *questionTipL;
@property (weak, nonatomic) IBOutlet UISwitch *lianmaiS;

@property (nonatomic, strong) QHTKChatRoomView *chatView;
@property (nonatomic, strong) NSTimer *answerTimer;
@property (nonatomic) NSUInteger answerT;

@property (nonatomic, strong) QHRTCKitManager *kitManager;
@property (nonatomic) BOOL bJoined;

@property (nonatomic, strong) IFlySpeechRecognizer *iFlySpeechRecognizer;
@property (nonatomic, strong) QHAudioConverter *audioConverter;
@property (nonatomic, strong) QHSpeakerManager *speakerManager;
@property (nonatomic, strong) QHHyphenateChatManager *hyphenateChatManager;

@property (nonatomic, strong) QHHelpView *helpV;

@end

@implementation QHGameRoomViewController

- (void)dealloc {
#if DEBUG
    NSLog(@"%s", __FUNCTION__);
#endif
}

- (void)viewDidDisappear:(BOOL)animated {
    if (self.navigationController.topViewController != self) {
        [self p_leave];
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    [self p_setup];
    [self p_initEMC];
    [self p_initChatView];
    [self p_initAGR];
    [self p_initIFly];
    [self p_initAudioConverter];
    [self p_initSpeakerManager];
    [self p_initHyphenateChatManager];
    [self p_addNotifition];
    [self p_authorizationAction];
    
    NSDictionary *msg = @{@"op": @(QHHCActionSystem), @"c": K_GAME_FLOW};
    [self.chatView insertChatData:@[msg]];
    NSDictionary *msg1 = [_hyphenateChatManager welcome];
    [self.chatView insertChatData:@[msg1]];
}

#pragma mark - Private

- (void)p_setup {
    self.view.backgroundColor = [UIColor whiteColor];
    self.questionTipL.text = @"";
    
    UIBarButtonItem *helpBtn = [[UIBarButtonItem alloc] initWithTitle:@"帮助" style:UIBarButtonItemStylePlain target:self action:@selector(helpAction)];
    self.navigationItem.rightBarButtonItems = @[helpBtn];
    if ([QHAppContext context].isHost) {
        [self.readyBtn setTitle:@"创建" forState:UIControlStateNormal];
    }
}

- (void)p_authorizationAction {
    QHLinkMicAuthorizationView *authorizationV = [QHLinkMicAuthorizationView creatIn:self.view authorization:^(BOOL bAuthorization) {
        NSLog(@"Authorization == %d", bAuthorization);
    }];
    [authorizationV hiddenClose:NO];
}

- (void)p_addNotifition {
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(toRTCKitJoinNotificationAction:) name:kQHRTCKitJoinNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(toRTCKitOccurErrorNotificationAction:) name:kQHRTCKitOccurErrorNotification object:nil];
}

- (void)p_removeNotification {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)p_initEMC {
    EMError *error;
    [[EMClient sharedClient].roomManager joinChatroom:[QHAppContext context].EMRoomId error:&error];
    [[EMClient sharedClient].chatManager addDelegate:self delegateQueue:nil];
}

- (void)p_initChatView {
    QHChatBaseConfig *config = [QHChatBaseConfig new];
    config.bLongPress = YES;
    config.bOpenScorllFromBottom = NO;
    config.chatCountMax = 100;
    config.chatCountDelete = 30;
    QHChatCellConfig cellConfig = config.cellConfig;
    cellConfig.cellLineSpacing = 1;
    cellConfig.fontSize = 14;
    cellConfig.cellWidth = 240;
    config.cellConfig = cellConfig;
    // 自下到上，需要自计算高度
    config.bAutoCellHeight = YES;
    QHTKChatRoomView *v = [QHTKChatRoomView createChatViewToSuperView:self.chatSuperV withConfig:config];
    v.delegate = self;
    v.backgroundColor = [UIColor clearColor];
    _chatView = v;
}

- (void)p_initAGR {
    _kitManager = [QHRTCKitManager create:QHRTCKitTypeAG superV:_preview config:nil delegate:self];
    _kitManager.role = QHRTCRoleHost;
    QHAppContext *c = [QHAppContext context];
    [_kitManager checkAndSetConfig:@{@"token": c.AGToken, @"channel": c.AGChannelId, @"hostId": @(c.hostId), @"userId": @(c.curId), @"nickName": c.curUser}];
}

- (void)p_initIFly {
    _iFlySpeechRecognizer = [IFlySpeechRecognizer sharedInstance];
    self.iFlySpeechRecognizer.delegate = self;
    [self.iFlySpeechRecognizer setParameter:@"-1" forKey:@"audio_source"];
}

- (void)p_initAudioConverter {
    _audioConverter = [QHAudioConverter new];
    _audioConverter.delegate = self;
}

- (void)p_initSpeakerManager {
    _speakerManager = [QHSpeakerManager new];
}

- (void)p_initHyphenateChatManager {
    _hyphenateChatManager = [QHHyphenateChatManager new];
}

- (void)p_leave {
    EMError *error;
    [[EMClient sharedClient].roomManager leaveChatroom:[QHAppContext context].EMRoomId error:&error];
    [_kitManager leave];
    [self.iFlySpeechRecognizer cancel];
}

- (void)p_back {
    [self p_leave];
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)p_end {
    if ([QHAppContext context].isHost) {
        NSString *win = [self.speakerManager win];
        NSDictionary *msg = [self.hyphenateChatManager end4Win:win];
        [self p_show2Win:msg[@"n"]];
    }
    self.questionTipL.text = @"";
    self.questionTipL.hidden = YES;
    
    self.goGameBtn.hidden = YES;
    self.goGameBtn.enabled = YES;
    self.answerBtn.hidden = YES;
    
    [self p_initSpeakerManager];
    [self.readyBtn setTitle:[QHAppContext context].isHost ? @"创建" : @"准备" forState:UIControlStateNormal];
    self.readyBtn.enabled = YES;
}

- (void)p_answer {
    if (self.answerT == 0) {
        [self.speakerManager start];
        [self.iFlySpeechRecognizer startListening];
        [self.kitManager open4AudioCallback:YES];
    }
    else if (self.answerT > K_IFLY_TIME) {
        [self p_log:@"答题结束"];
        [self.answerBtn setTitle:@"0" forState:UIControlStateNormal];
        [self.answerTimer invalidate];
        self.answerTimer = nil;
        self.answerT = 0;
        [self.kitManager open4AudioCallback:NO];
        [self.iFlySpeechRecognizer stopListening];
        
        // 自行测试
//        IFlySpeechError *e = [IFlySpeechError initWithError:-9999];
//        e.errorDesc = @"自行测试";
//        [self onCompleted:e];
        return;
    }
    
    self.answerT += 1;
    [self.answerBtn setTitle:[NSString stringWithFormat:@"%ld", K_IFLY_TIME - self.answerT + 1] forState:UIControlStateNormal];
}

- (void)p_log:(NSString *)content {
    NSLog(@"%@", content);
}

- (void)p_go2answer:(NSInteger)aIdx {
    [MBProgressHUD showSuccess:@"该你答题，后点击答题，3秒内说出答案即可" toView:nil];
    self.questionTipL.text = [self.speakerManager getQuestionSubTitle:aIdx];
    [self.answerBtn setTitle:@"答题" forState:UIControlStateNormal];
    self.answerBtn.enabled = YES;
    self.answerBtn.hidden = NO;
}

- (void)p_show2Win:(NSString *)userName {
    if (userName == nil || userName.length <= 0) {
        [MBProgressHUD show:@"平局，您俩棋逢对手" icon:@"qh_icon_pk_pingju" view:nil afterDelay:3];
    }
    else if ([[QHAppContext context].curUser isEqualToString:userName]) {
        [MBProgressHUD show:@"恭喜，您赢了" icon:@"qh_icon_pk_shengli" view:nil afterDelay:3];
    }
    else {
        [MBProgressHUD show:@"遗憾，您输了" icon:@"qh_icon_pk_shibai" view:nil afterDelay:3];
    }
}

#pragma mark - EMChatManagerDelegate

- (void)messagesDidReceive:(NSArray *)aMessages {
}

- (void)cmdMessagesDidReceive:(NSArray *)aCmdMessages {
    for (EMMessage *msg in aCmdMessages) {
        [self.chatView insertChatData:@[msg.ext]];
        [self.speakerManager dealMsg:msg.ext];
        NSInteger type = [msg.ext[@"op"] integerValue];
        switch (type) {
            case QHHCActionBegan: {
                self.questionTipL.hidden = NO;
                [MBProgressHUD showSuccess:@"游戏正式开始，请看题目" toView:nil];
                self.questionTipL.text = [self.speakerManager getQuestionSubTitle:0];
            }
                break;
            case QHHCActionResult: {
                if ([QHAppContext context].isHost && [msg.ext[@"res"] boolValue] == YES) {
                    NSString *n = msg.ext[@"n"];
                    [self.speakerManager addSorce:n];
                }
            }
                break;
            case QHHCActionNext: {
                NSString *n = msg.ext[@"nextP"];
                if ([[QHAppContext context].curUser isEqualToString:n]) {
                    NSInteger aIdx = [msg.ext[@"nextQ"] integerValue];
                    [self p_go2answer:aIdx];
                }
            }
                break;
            case QHHCActionEnd: {
                [self p_end];
            }
                break;
            case QHHCActionEnd4Win: {
                [self p_show2Win:msg.ext[@"n"]];
            }
                break;
            default:
                break;
        }
        
    }
}

#pragma mark - IFlySpeechRecognizerDelegate

- (void)onResults:(NSArray *)results isLast:(BOOL)isLast {
    [self.speakerManager add:results];
}

- (void)onCompleted:(IFlySpeechError *)error {
    [self p_log:[NSString stringWithFormat:@"语音：%@", error.errorDesc]];
    if (error.errorCode != 0 && error.errorCode != -9999) {
        return;
    }
    
    NSString *a = nil;
    if (error.errorCode == -9999) {
        a = [self.speakerManager testCheck:[self.speakerManager getAnswer]];
    }
    else {
        a = [self.speakerManager check];
    }
    
    NSDictionary *msg = [self.hyphenateChatManager result:a];
    [self.chatView insertChatData:@[msg]];
    
    if ([msg[@"res"] boolValue]) {
        [self.speakerManager addSorce:[QHAppContext context].curUser];
        [MBProgressHUD showError:@"答题正确" toView:nil];
    }
    else {
        [MBProgressHUD showError:@"答题错误" toView:nil];
    }
    self.answerBtn.hidden = YES;
    if ([self.speakerManager hasAnswerIsOver]) {
        NSDictionary *msg = [self.hyphenateChatManager end];
        [self.chatView insertChatData:@[msg]];
        [self p_end];
    }
    else {
        NSString *np = [self.speakerManager nextPlayer];
        NSInteger aIdx = [self.speakerManager nextQuestion];
        NSDictionary *msg = [self.hyphenateChatManager nextplay:np questionIdx:aIdx];
        [self.chatView insertChatData:@[msg]];
        if ([np isEqualToString:[QHAppContext context].curUser]) {
            [self p_go2answer:aIdx];
        }
        else {
            self.questionTipL.text = [self.speakerManager getQuestionSubTitle:aIdx];
        }
    }
}

#pragma mark - QHRTCKitManagerDelegate

- (BOOL)logRTCKitManager:(id)manager content:(NSString *)content {
    [self p_log:[NSString stringWithFormat:@"连麦：%@", content]];
    return NO;
}

- (void)getAudioRTCKitManager:(id)manager buffer:(CMSampleBufferRef)buffer {
    [self.audioConverter decodeAudioSamepleBuffer:buffer];
}

#pragma mark - QHAudioConverterDelegate

- (void)decodeResult:(QHAudioConverter *)ac bufferList:(AudioBufferList)bl {
    
    NSData *data = [NSData dataWithBytes:bl.mBuffers[0].mData length:bl.mBuffers[0].mDataByteSize];
    BOOL r = [self.iFlySpeechRecognizer writeAudio:data];
    if (!r) {
        [self p_log:@"语音：writeAudio fail"];
    }
}

#pragma mark - Action

- (IBAction)helloAction:(id)sender {
    NSDictionary *msg = [_hyphenateChatManager sayHello];
    [self.chatView insertChatData:@[msg]];
}

- (void)toRTCKitJoinNotificationAction:(NSNotification *)notif {
    // 加入连麦房间使用这个判断成功与否
    NSError *error = notif.object;
    if (error == nil) {
        _bJoined = YES;
        [MBProgressHUD showError:@"加入连麦成功" toView:nil];
    }
    else {
        _bJoined = NO;
        [MBProgressHUD showError:@"加入连麦失败" toView:nil];
    }
}

- (void)toRTCKitOccurErrorNotificationAction:(NSNotification *)notif {
    NSError *error = notif.object;
    if (error.code == 109 || error.code == 110) {
        [MBProgressHUD showError:@"AgoraToken 无效，需项目进行配置更新" toView:nil];
    }
}

- (IBAction)answerAction:(id)sender {
    self.answerBtn.enabled = NO;
    [self.answerTimer invalidate];
    self.answerTimer = nil;
    self.answerT = 0;
    __weak typeof(self) weakSelf = self;
    self.answerTimer = [NSTimer eoc_scheduledTimerWithTimeInterval:1 block:^{
        [weakSelf p_answer];
    } repeats:YES];
    [[NSRunLoop currentRunLoop] addTimer:self.answerTimer forMode:NSRunLoopCommonModes];
    [self.answerTimer fire];
}

- (IBAction)readyGameAction:(id)sender {
    if (![QHAppContext context].isHost && !_speakerManager.canReady) {
        [MBProgressHUD showError:@"需房主创建之后才可加入游戏" toView:nil];
        return;
    }
    if (self.bJoined == NO) {
        [MBProgressHUD showError:@"请先加入连麦之后才可加入游戏" toView:nil];
        return;
    }
    if ([QHAppContext context].isHost) {
        [_speakerManager joinlocal];
        self.goGameBtn.hidden = NO;
    }
    [self.readyBtn setTitle:@"等待" forState:UIControlStateNormal];
    self.readyBtn.enabled = NO;
    NSDictionary *msg = [_hyphenateChatManager join];
    [self.chatView insertChatData:@[msg]];
    self.questionTipL.text = [self.speakerManager getQuestionTitle];
}

- (IBAction)goGameAction:(id)sender {
    if (_speakerManager.players.count < 2) return;
    [self.speakerManager initSorce];
    NSDictionary *msg = [_hyphenateChatManager began:[QHAppContext context].questionId players:_speakerManager.players];
    [self.chatView insertChatData:@[msg]];
    NSInteger aIdx = [self.speakerManager nextQuestion];
    NSDictionary *msg1 = [self.hyphenateChatManager nextplay:[QHAppContext context].curUser questionIdx:aIdx];
    [self.chatView insertChatData:@[msg1]];
    self.goGameBtn.enabled = NO;
    
    // host 先获取题目回答
    [MBProgressHUD showSuccess:@"游戏正式开始，请看题目" toView:nil];
    self.questionTipL.hidden = NO;
    [self p_go2answer:aIdx];
}

- (IBAction)lianmaiAction:(UISwitch *)sender {
    if (sender.on) {
        BOOL b = [_kitManager join];
        if (b == NO) {
            _bJoined = NO;
            [MBProgressHUD showError:@"加入连麦失败" toView:nil];
        }
    }
    else {
        [_kitManager leave];
        _bJoined = NO;
    }
}

- (void)helpAction {
    if (_helpV == nil) {
        _helpV = [QHHelpView createWith:self.view];
    }
    else {
        [_helpV removeFromSuperview];
        _helpV = nil;
    }
}

@end
