//
//  ViewController.m
//  LxAudioToStaff
//
//  Created by DavinLee on 2021/5/27.
//

#import "ViewController.h"
#import "CKMakeMusic.h"
#import "LxAlertController.h"
#import <YYKit/YYKit.h>
#import "UIImage+Default.h"
#import "UIView+Default.h"
#import "LxMcStaffLineView+MeasureLayout.h"
#import <POP/POP.h>
#import "UIImageView+Default.h"
#import "CKMixAudioTool.h"
#import "LxNoteToXmlHelp.h"
#import "WeBasicAnimation.h"
#import "LxAlertController.h"
#import "LxStaffNoteModel.h"
#import "NSString+Helper.h"
#import "LxRecordListVc.h"
#import "YPNodeTypeModel.h"
#import "LxMcMeasureModel.h"
#import "CKMakeMusic_AllFuntion.h"
@interface ViewController ()<LxMcStaffLineViewDelegate,
UIGestureRecognizerDelegate>
@property (nonatomic, strong) CKMakeMusic *makeMusic;

@property (strong, nonatomic) UIImageView *zoomScaleView;
@property (weak, nonatomic) IBOutlet UIImageView *backImageView;
@property (weak, nonatomic) IBOutlet UIButton *refreshBtn;
@property (weak, nonatomic) IBOutlet UIButton *cancelBtn;
@property (weak, nonatomic) IBOutlet UIButton *recordBtn;

/**
 作曲游戏所有视图
 */
@property (weak, nonatomic) IBOutlet UIButton *editModeBtn;
@property (nonatomic, weak) CKMakeMusicView *makeMusicView;
/** 当前音符View **/
@property (weak, nonatomic) LxMcNoteView *currentNoteView;

@property (assign, nonatomic) NSInteger index;
@property (strong,nonatomic) NSMutableArray *musicNodeArray;
//音符类型数组
@property (strong,nonatomic) NSMutableArray *nodeTypeModelArray;

@property (strong, nonatomic) IBOutletCollection(UIButton) NSArray *octaveBtns;
@property (weak, nonatomic) IBOutlet UIButton *saveBtn;

/** Lx description   当前选择音区  **/
@property (assign, nonatomic) NSInteger octaveIndex;
@end

@implementation ViewController

#pragma mark - ************************conFig************************
- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupDefault];
    // Do any additional setup after loading the view.
}

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    
}

- (void)setupDefault{
    self.makeMusic = [[CKMakeMusic_AllFuntion alloc] init];
    CKMakeMusicView *makeMusicView = [self.makeMusic makeMusicView];
    [self.view addSubview:makeMusicView];
    self.makeMusicView = makeMusicView;
    
    WF;
    makeMusicView.playCompleted = ^{
        weakSelf.recordBtn.alpha = 1;
        for (UIButton *btn in weakSelf.octaveBtns) {
            btn.alpha = 1;
        }
        weakSelf.zoomScaleView.userInteractionEnabled = YES;
        weakSelf.editModeBtn.alpha = 1;
        [weakSelf zoomScaleInAni];
        weakSelf.refreshBtn.alpha = weakSelf.cancelBtn.alpha = self.saveBtn.alpha = 1;
        
    };
    
    /** 设置缩放按钮 **/
    self.zoomScaleView = [UIImageView new];
    self.zoomScaleView.backgroundColor = [UIColor clearColor];
    self.zoomScaleView.size = CGSizeMake(175, 147);
    self.zoomScaleView.center = CGPointMake(175.f/2.f+4, mScreenHeight - 20);
    [self.view addSubview:self.zoomScaleView];
    [self creatNodeTypeModelArray];
    [self zoomScaleInAni];
    self.zoomScaleView.userInteractionEnabled = YES;
    UITapGestureRecognizer *zoomTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(zoomViewTapGesture:)];
    [self.zoomScaleView addGestureRecognizer:zoomTap];
    
    [self.view insertSubview:self.makeMusicView atIndex:1];
    
    [LxSpeechManager.sharedInstance lx_checkSpeechAuthBlock:^(SFSpeechRecognizerAuthorizationStatus status) {
        switch (status) {
            case SFSpeechRecognizerAuthorizationStatusAuthorized:
            {
                debugLog(@"授权成功");
            }
                break;
            case SFSpeechRecognizerAuthorizationStatusRestricted:{
                [MBProgressHUD lx_showHudWithTitle:@"没有录音设备，你玩个啥" hideCompletion:nil];
            }
                break;
            case SFSpeechRecognizerAuthorizationStatusDenied:{
                [MBProgressHUD lx_showHudWithTitle:@"不给授权翻译，你玩个啥？考虑清楚!" hideCompletion:^{
                    
                }];
            }
                break;
                
            default:
            {
                [MBProgressHUD lx_showHudWithTitle:@"发生未知错误，我们重新来一次！" hideCompletion:^{
                    
                }];
            }
                break;
        }
    }];
    
    [LxSpeechManager.sharedInstance lx_checkAudioAuthBlock:^(BOOL authed) {
        if (!authed) {
            [MBProgressHUD lx_showHudWithTitle:@"不给授权录音，你玩个啥？考虑清楚!" hideCompletion:^{
                
            }];
        }
    }];
    [self.octaveBtns[1] sendActionsForControlEvents:UIControlEventTouchUpInside];
    [mNotificationCenter addObserver:self selector:@selector(enterForeGroundNotification:) name:UIApplicationWillEnterForegroundNotification object:nil];
}

- (void)enterForeGroundNotification:(NSNotification *)not{
    [self zoomScaleInAni];
}

- (void)creatNodeTypeModelArray{
    self.nodeTypeModelArray = [NSMutableArray array];
    NSArray *noteModelArray = [self.makeMusic getLeftNotesArray];
    LxMcStaffLineView *tabsView = self.makeMusicView.tabsView;
    CGFloat maxDuration = [tabsView noteBeatsForStaffBeatsType:tabsView.staffBeatsType];
    
    NSMutableArray *musicNodeTimeArray = [[NSMutableArray alloc]init];
    
    NSMutableArray *musicNodeTimeArrayT = [[NSMutableArray alloc]init];
    
    CGFloat diff = maxDuration;
    for (int i = 0; i<noteModelArray.count; i++) {
        CKNoteModel *model = noteModelArray[i];
        model.nodeTime = [tabsView noteTypeBeatsForNoteType:model.noteType isDot:model.isDot];
        if (model.nodeTime <= diff) {
            [musicNodeTimeArrayT addObject:model];
        }
    }
    
    if (musicNodeTimeArrayT.count == 0) return;
    
    // 排序key, 某个对象的属性名称，是否升序, YES-升序, NO-降序
    NSSortDescriptor *sortDescriptor = [NSSortDescriptor sortDescriptorWithKey:@"nodeTime" ascending:YES];
    // 排序结果
    NSArray *resultArray = [musicNodeTimeArrayT sortedArrayUsingDescriptors:[NSArray arrayWithObject:sortDescriptor]];
    
    for (int i = 0; i<resultArray.count - 1; i++) {
        CKNoteModel *model = resultArray[i];
        CKNoteModel *nextmodel = resultArray[i+1];
        CGFloat duartion = model.nodeTime + (model.nodeTime - nextmodel.nodeTime)/2;
        CGFloat time = i == 0? 0.f:[musicNodeTimeArray[i-1] floatValue];
        [musicNodeTimeArray addObject:@(time + duartion)];
    }
    
    for (int i = 0; i<resultArray.count; i++) {
        CGFloat intervalMin;
        CGFloat intervalMax;
        
        intervalMin = i==0? 0:[musicNodeTimeArray[i-1] floatValue];
        intervalMax = i==(resultArray.count-1)? MAXFLOAT:[musicNodeTimeArray[i] floatValue];
        
        CKNoteModel *ckModel = resultArray[i];
        YPNodeTypeModel *nodeModel = [[YPNodeTypeModel alloc]init];
        nodeModel.intervalMin = intervalMin;
        nodeModel.intervalMax = intervalMax;
        nodeModel.nodeType = ckModel.noteType;
        nodeModel.isRest = ckModel.isRest;
        nodeModel.isDot = ckModel.isDot;
        
        [_nodeTypeModelArray addObject:nodeModel];
    }
    
    
}

- (void)zoomScaleOutAni
{
    
    
    NSMutableArray *aniArray = [WeBasicAnimation setGifArr:37
                                                   strpath:@"seal%d"
                                                   strpath:@"seal%d"];
    CAKeyframeAnimation *gifAni  = [WeBasicAnimation getGifAnimationWithDuration:2.5
                                                                      statusSave:NO
                                                                     autoReverse:NO];
    gifAni.values = aniArray;
    gifAni.repeatCount = NSNotFound;
    [self.zoomScaleView.layer addAnimation:gifAni forKey:@"gif"];
}

- (void)zoomScaleInAni
{
    
    
    NSMutableArray *aniArray = [WeBasicAnimation setGifArr:37
                                                   strpath:@"seal_stand%d"
                                                   strpath:@"seal_stand%d"];
    CAKeyframeAnimation *gifAni  = [WeBasicAnimation getGifAnimationWithDuration:2.5
                                                                      statusSave:NO
                                                                     autoReverse:NO];
    gifAni.values = aniArray;
    gifAni.repeatCount = NSNotFound;
    [self.zoomScaleView.layer addAnimation:gifAni forKey:@"gif"];
}


- (void)showNotesWithRecordName:(NSString *)name{
    NSArray *noteInfos = [mUserDefaults objectForKey:name];
    if (noteInfos) {
        [self clickRefreshBtn:nil];
        for (NSString *infoStr in noteInfos) {
            NSDictionary *info = [infoStr lx_getDictionary];
            LxMcNoteView *note = [LxMcNoteView lx_noteViewWithDic:info];
            
            [self.makeMusicView.tabsView addNodeWithNoeView:note superOffsetPoint:CGPointZero miditag:note.miditag];
        }
    }
}

#pragma mark - ************************ClickAction************************



- (IBAction)clickEditModeBtn:(UIButton *)sender {
    sender.selected = !sender.selected;
    if (sender.selected) {
        [sender setTitle:@"滚动模式" forState:UIControlStateNormal];
        self.makeMusicView.tabsView.userInteractionEnabled = NO;
        self.recordBtn.alpha = 0;
        for (UIButton *btn in self.octaveBtns) {
            btn.alpha = 0;
        }
    }else{
        [sender setTitle:@"编辑模式" forState:UIControlStateNormal];
        self.makeMusicView.tabsView.userInteractionEnabled = YES;
        self.recordBtn.alpha = 1;
        for (UIButton *btn in self.octaveBtns) {
            btn.alpha = 1;
        }
    }
}

- (IBAction)clickOctaveBtn:(UIButton *)sender {
    
    for (UIButton *btn in self.octaveBtns) {
        if (sender == btn) {
            btn.selected = YES;
            btn.layer.borderColor = [UIColor lx_colorWithHexString:@"#EDFFEA"].CGColor;
            btn.layer.borderWidth = 2;
            btn.layer.cornerRadius = 10;
            btn.tintColor = [UIColor clearColor];
            self.octaveIndex = sender.tag;
        }else{
            btn.selected = NO;
            btn.layer.borderWidth = 0;
            
        }
    }
}


- (void)senderrecordTapGesture:(UITapGestureRecognizer *)gesture{
    
    
    
}

- (void)zoomViewTapGesture:(UITapGestureRecognizer *)gesture{
    switch (gesture.state) {
        case UIGestureRecognizerStateBegan:
        {
            
        }
            break;
        case UIGestureRecognizerStateChanged:
        case UIGestureRecognizerStatePossible:
        {
            break;
        }
        default:
        {
            self.zoomScaleView.userInteractionEnabled = NO;
            self.recordBtn.alpha = 0;
            for (UIButton *btn in self.octaveBtns) {
                btn.alpha = 0;
            }
            self.editModeBtn.alpha = 0;
            [self.makeMusicView.tabsView playNoteViewsWithQueueNoteS];
            [self zoomScaleOutAni];
            self.refreshBtn.alpha = self.cancelBtn.alpha = self.saveBtn.alpha = 0;
            self.makeMusicView.tabsView.userInteractionEnabled = NO;
        }
            break;
    }
}

- (IBAction)clickRefreshBtn:(id)sender {
    
    [self.makeMusicView.tabsView resetDefaultStaffLineView];
    [self.makeMusicView.tabsView measureAdd];
}

- (IBAction)clickRecordListBtn:(id)sender {
    LxRecordListVc *vc = [[LxRecordListVc alloc] init];
    self.modalPresentationStyle = UIModalPresentationFullScreen;
    WF;
    [vc setBlock:^(NSString * _Nonnull recordName) {
        [weakSelf showNotesWithRecordName:recordName];
    }];
    [self presentViewController:vc animated:YES completion:nil];
}
- (IBAction)clickSaveBTN:(id)sender {
    
    NSArray <LxMcNoteView *>* tempNotes = [self.makeMusicView.tabsView.ClefMeasureModelArray.firstObject allNoteViewsArray];
    if (tempNotes.count < 1) {
        [MBProgressHUD lx_showHudWithTitle:@"没有音符你保存个啥？赶紧点去" hideCompletion:nil];
        return;
    }
    NSString *fileName = [NSString lx_currentDateStr];
    LxAlertController *vc = [LxAlertController lx_alertShowWithTitle:@"保存"
                                                             message:@"是否进行记录保存？"
                                                  textfiledHolderStr:fileName
                                                        actionTitles:@[@"取消",@"确定"]
                                                        actionStyles:@[@(UIAlertActionStyleCancel),@(UIAlertActionStyleDefault)]
                                                     clickIndexBlock:^(NSInteger clickIndex, NSString *text) {
        
        if (clickIndex == 1) {
            NSArray <LxMcNoteView *>* notes = [self.makeMusicView.tabsView.ClefMeasureModelArray.firstObject allNoteViewsArray];

            NSMutableArray *infos = [[NSMutableArray alloc] init];
            for (LxMcNoteView *note in notes) {
                NSString *noteStr = [note lx_Json];
                [infos addObject:noteStr];
            }
            [mUserDefaults setObject:infos forKey:text];
            [mUserDefaults synchronize];
            NSMutableArray *namesArray = [NSMutableArray arrayWithArray:[mUserDefaults objectForKey:@"recordNames"]];
            if (!namesArray) {
                namesArray = [[NSMutableArray alloc] init];
            }
            [namesArray insertObject:text atIndex:0];
            [mUserDefaults setObject:namesArray forKey:@"recordNames"];
        }
        
    }];
    [self presentViewController:vc animated:YES completion:nil];
}

- (IBAction)clickBeginRecord:(UIButton *)sender {
    self.recordBtn.selected = YES;
    self.recordBtn.tintColor = [UIColor lx_colorWithHexString:@"#EDFFEA"];
    [LxSpeechManager.sharedInstance lx_startRecord];
    
}

- (IBAction)clickEndRecord:(UIButton *)sender {
    [self endRecord];
}

- (IBAction)clickEndOutRecord:(UIButton *)sender {
    [self endRecord];
    
}

- (void)endRecord{
    WF;
    [LxSpeechManager.sharedInstance lx_stopRecordWithTransBlock:^(NSMutableArray<NSString *> * _Nonnull strs) {
        for (NSString *letter in strs) {
            LxMcNoteView *noteView = [LxMcNoteView lx_defaultNoteViewWithNoteType:MusicNodeQuarter isRest:NO isDot:NO];
            noteView.userInteractionEnabled = YES;
            NSInteger midiTag = 0;
            if ([letter isEqualToString:@"D"]) {
                midiTag = 60 + (weakSelf.octaveIndex - 4) * 12;
            }else if ([letter isEqualToString:@"R"]){
                midiTag = 62 + (weakSelf.octaveIndex - 4) * 12;
            }
            else if ([letter isEqualToString:@"M"]){
                midiTag = 64 + (weakSelf.octaveIndex - 4) * 12;
            }
            else if ([letter isEqualToString:@"F"]){
                midiTag = 65 + (weakSelf.octaveIndex - 4) * 12;
            }
            else if ([letter isEqualToString:@"S"]){
                midiTag = 67 + (weakSelf.octaveIndex - 4) * 12;
            }
            else if ([letter isEqualToString:@"L"]){
                midiTag = 69 + (weakSelf.octaveIndex - 4) * 12;
            }
            else if ([letter isEqualToString:@"X"]){
                midiTag = 71 + (weakSelf.octaveIndex - 4) * 12;
            }
            if (midiTag >= 60) {
                noteView.isUpClef = YES;
            }else{
                noteView.isUpClef = NO;
            }
            [self.makeMusicView.tabsView resetMidiPlay];
            [self.makeMusicView.tabsView addNodeWithNoeView:noteView superOffsetPoint:CGPointZero miditag:midiTag];
        }
        
        
        
    }];
    self.recordBtn.selected = NO;
    self.recordBtn.tintColor = [UIColor lx_colorWithHexString:@"#EB6C74"];
}



@end
