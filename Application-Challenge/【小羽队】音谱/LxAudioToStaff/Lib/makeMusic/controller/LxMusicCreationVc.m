//
//  LxMusicCreationVc.m
//  SmartPiano
//
//  Created by DavinLee on 2018/1/30.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "LxMusicCreationVc.h"
#import <YYKit/YYKit.h>
#import "UIImage+Default.h"
#import "UIView+Default.h"
#import "YYSpriteSheetImage+Default.h"
#import "LxMcStaffLineView+MeasureLayout.h"
#import <POP/POP.h>
#import "UIImageView+Default.h"
#import "CKMixAudioTool.h"
#import "LxNoteToXmlHelp.h"
#import "WeBasicAnimation.h"
#import "LxAlertController.h"
#import "LxStaffNoteModel.h"
#import "YPNodeTypeModel.h"
#import "LxMcMeasureModel.h"
#import "LxCanInteraSubImageView.h"

typedef NS_ENUM(NSInteger, LxMcUploadState){
    LxMcUploadMididata = 0,
    LxMcUploadXml = 1,
    LxMcUploadComMp3 = 2,
};

@interface LxMusicCreationVc () <LxMcStaffLineViewDelegate,
UIGestureRecognizerDelegate>

/** UI缩放按钮 **/
@property (strong, nonatomic) UIImageView *zoomScaleView;
@property (strong, nonatomic) UIImageView *measureErrorView;
@property (weak, nonatomic) IBOutlet UIButton *playBtn;
@property (weak, nonatomic) IBOutlet UIButton *metronomeButton;
@property (weak, nonatomic) IBOutlet UIButton *saveMidiBtn;
@property (weak, nonatomic) IBOutlet UIImageView *backImageView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *metronomeRight;

/******************************************************上传相关***************************************************************/
/** 上传文件 **/
@property(strong, nonatomic) NSData *midiData;
/** 上传xml数据 **/
@property (strong, nonatomic) NSData *xmlData;
/**
 作曲游戏所有视图
 */
@property (nonatomic, weak) CKMakeMusicView *makeMusicView;
@property (nonatomic, strong) CKMixAudioTool *audioTool;


@property (strong, nonatomic) NSTimer *mp3ExistTimer;
@property (assign, nonatomic) NSInteger checkCount;
@property (strong, nonatomic) AVAssetExportSession *audioExport;
/** 当前上传内容类型 **/
@property (assign, nonatomic) LxMcUploadState uploadState;
/****************************************************** 保存后弹奏相关***************************************************************/
/** 持有保存曲谱在oss路径 **/
@property (copy, nonatomic) NSString *savePath;
/** 持有b保存曲谱显示名称 **/
@property (nonatomic, copy) NSString *saveName;
/** 弱持有当前已弹出的alertvc **/
@property (weak, nonatomic) LxAlertController *alertVc;
/** 计数判断oss文件存在错误次数 **/
@property (assign, nonatomic) NSInteger reCheckExistCount;
/** 当前音符View **/
@property (weak, nonatomic) LxMcNoteView *currentNoteView;

//模式显示
@property (strong, nonatomic) UIButton *testButton;
//开始
@property (strong, nonatomic) UIButton *beginButton;
//结束
@property (strong, nonatomic) UIButton *endButton;

@property (assign, nonatomic) NSInteger index;
/** 存放琴键按下是时长的字典(暂存) **/
@property (strong,nonatomic) NSMutableDictionary *pressPinaoDrutionDict;
@property (strong,nonatomic) NSMutableArray *musicNodeArray;
//音符类型数组
@property (strong,nonatomic) NSMutableArray *nodeTypeModelArray;
//休止音符类型数组
@property (strong,nonatomic) NSMutableArray *restNodeTypeModelArray;
@property (assign,nonatomic) BOOL firstLoad;


@end

@implementation LxMusicCreationVc
- (NSString *)gameintroIdenClassName
{
    return [[self.makeMusic class] description];
}
- (void)viewDidLoad {
    [super viewDidLoad];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(someMethod:)
                                                 name:UIApplicationDidBecomeActiveNotification object:nil];
//    [self setTestEvent];
    

    CKMakeMusicView *makeMusicView = [self.makeMusic makeMusicView];
    [self.view addSubview:makeMusicView];
    self.makeMusicView = makeMusicView;
    self.makeMusicView.tabsView.restNodeViewArray = self.restNodeTypeModelArray;
  

    ckWeakSelf
    makeMusicView.playCompleted = ^{
        [weakSelf btnEnabled:YES];
    };
    
    //更换拍号
    makeMusicView.beatsBtnClick = ^{
        [weakSelf creatNodeTypeModelArray];
    };
    
    
    /** 设置缩放按钮 **/
    self.zoomScaleView = [UIImageView new];
    self.zoomScaleView.backgroundColor = [UIColor clearColor];
    self.zoomScaleView.size = CGSizeMake(175, 147);
    self.zoomScaleView.center = CGPointMake(mScreenWidth/2.f, mScreenHeight - 35);
    [self dogPlay];
//    [self.zoomScaleView lx_zoomScale:1.6
//              scalePriScaleDirection:LxViewScalePriCenter];
    self.zoomScaleView.userInteractionEnabled = YES;
    self.zoomScaleView.userInteractionEnabled = NO;
    [self.view addSubview:self.zoomScaleView];
    
    //默认隐藏
//    self.makeMusicView.leftNotesView.hidden = YES;
//    self.makeMusicView.rightNotesView.hidden = YES;
//    self.playBtn.hidden = YES;
//    self.saveMidiBtn.hidden = YES;
    
    [self setDiffImage];
    
    [self creatNodeTypeModelArray];
}

- (void)setDiffImage {



            UIImage *playImage = [UIImage imageNamed:@"play_rudin2"];
            [self.playBtn setImage:playImage forState:UIControlStateNormal];
            self.playBtn.size = playImage.size;
        self.backImageView.image = mImageByName(@"write_background");
        
        if (self.isFullFuntion) {
            self.backImageView.image = [UIImage imageNamed:@"FullFunction_makeMusic_bg"];
        }
}

#pragma mark - Function
- (void)btnEnabled:(BOOL)enable
{
    self.playBtn.enabled = enable;
    self.makeMusicView.opreation = enable;
    self.saveMidiBtn.enabled = enable;
    self.zoomScaleView.userInteractionEnabled = enable;
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self.makeMusicView.tabsView stopPlay];
    [self uploadViewRemove];
    [_mp3ExistTimer invalidate];
    _mp3ExistTimer = nil;
  
}

- (void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    if (self.alertVc) {
        [self.alertVc dismissViewControllerAnimated:NO completion:nil];
    }
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self someMethod:nil];


}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
   
}

- (void)someMethod:(NSNotification *)notification {
    //动物动画会消失
//    if (_firstLoad && self.makeMusicView.tabsView.editMode == LxMcEditModeKeyborad) {
//        [self zoomScaleOutAni];
//    }else {
//        [self zoomScaleInAni];
//    }
    _firstLoad = YES;
}

- (void)zoomScaleOutAni
{
    
    
    
   
    NSMutableArray *aniArray = [WeBasicAnimation setGifArr:36
                                                   strpath: @"an_seal_stanby_@2x_0000%d.png"
                                                   strpath: @"an_seal_stanby_@2x_000%d.png" ];
    CAKeyframeAnimation *gifAni  = [WeBasicAnimation getGifAnimationWithDuration:2.5
                                                                      statusSave:NO
                                                                     autoReverse:NO];
    gifAni.values = aniArray;
    gifAni.repeatCount = NSNotFound;
    [self.zoomScaleView.layer removeAllAnimations];
    [self.zoomScaleView.layer addAnimation:gifAni forKey:@"gif"];
}


- (void)dogPlay {
    NSMutableArray *aniArray = [WeBasicAnimation setGifArr:32
                                                   strpath:@"daiziDog_0000%d.png"
                                                   strpath:@"daiziDog_000%d.png"];
    CAKeyframeAnimation *gifAni  = [WeBasicAnimation getGifAnimationWithDuration:2.5
                                                                      statusSave:NO
                                                                     autoReverse:NO];
    gifAni.values = aniArray;
    gifAni.repeatCount = NSNotFound;
    [self.zoomScaleView.layer removeAllAnimations];
    [self.zoomScaleView.layer addAnimation:gifAni forKey:@"gif"];
}

- (void)zoomScaleInAni
{
    NSMutableArray *aniArray = [NSMutableArray array];
  
        for ( int i = 0; i < 37; i ++) {
            UIImage *image;
            if (i < 10) {
                image = [UIImage lx_imageFromBundleWithName:[NSString stringWithFormat:@"an_seal_click_@2x_0000%d",i]];
            }else
            {
                image = [UIImage lx_imageFromBundleWithName:[NSString stringWithFormat:@"an_seal_click_@2x_000%d",i]];
            }
            [aniArray addObject:(id)[image CGImage]];
        }
   
    CAKeyframeAnimation *gifAni  = [WeBasicAnimation getGifAnimationWithDuration:2.5
                                                                      statusSave:NO
                                                                     autoReverse:NO];
    gifAni.values = aniArray;
    gifAni.repeatCount = NSNotFound;
    [self.zoomScaleView.layer removeAllAnimations];
    [self.zoomScaleView.layer addAnimation:gifAni forKey:@"gif"];
}

- (void)uploadViewRemove
{

}

/** 展示小节线不满足情况 **/
- (void)showMeasureError
{
    [_measureErrorView pop_removeAllAnimations];
    [_measureErrorView removeFromSuperview];
    _measureErrorView = nil;
    POPBasicAnimation *basicAnimation1 = [POPBasicAnimation linearAnimation];
    basicAnimation1.property =[POPAnimatableProperty propertyWithName:kPOPViewAlpha];
    basicAnimation1.duration = 0.3;
    basicAnimation1.toValue = @(1);
    basicAnimation1.autoreverses = YES;
    basicAnimation1.repeatCount = 3;
    [self.measureErrorView pop_addAnimation:basicAnimation1 forKey:@"alpha"];
    [basicAnimation1 setCompletionBlock:^(POPAnimation *anim, BOOL finished) {
        if (finished) {
            [self.measureErrorView removeFromSuperview];
            self.measureErrorView = nil;
        }
    }];
}
/** 显示个人作品页面，删除多余曲子 **/
- (void)showPrivateMusicDeleteVc{
  
}

#pragma mark - ********************  touch  ********************
- (void)touchesEnded:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
{
    for (UITouch *touch in touches) {
        CGPoint touchPoint = [touch locationInView:self.view];
        if (CGRectContainsPoint(self.zoomScaleView.frame, touchPoint)) {
            if ((self.makeMusicView.isZoomOut && touchPoint.y > CGRectGetMidY(self.zoomScaleView.frame) - 30) || !self.makeMusicView.isZoomOut) {
                [self zoomTap];
            }
        }
    }
}
#pragma mark - GetMethod

- (UIImageView *)measureErrorView
{
    if (!_measureErrorView) {
        _measureErrorView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"wenhao"]];
        _measureErrorView.center = self.makeMusicView .center;
        _measureErrorView.alpha = 0;
        [self.view addSubview:_measureErrorView];
    }
    return _measureErrorView;
}

#pragma mark - ClickActin

- (IBAction)clickFinishButton:(id)sender {
    //    if (self.makeMusicView.cannotBtnAction) {
    //        return;
    //    }
    [self.makeMusicView.bellView stop];
  
}

- (IBAction)clickBackButton:(id)sender {
    //    if (self.makeMusicView.cannotBtnAction) {
    //        return;
    //    }
    [self.makeMusicView.bellView stop];
   
}

- (IBAction)clickShuaXin:(id)sender {
    //    if (self.makeMusicView cannotBtnAction) {
    //        return;
    //    }
    [self.makeMusicView reloadUI];
    
}
- (IBAction)clickPlayButton:(id)sender {
    //    if (self.makeMusicView.cannotBtnAction) {
    //        return;
    //    }
    
    if ([self.makeMusicView.tabsView checkMeasuresRight]) {
        
        [self.makeMusicView.tabsView playNoteViewsWithQueueNoteS];
        if (self.makeMusicView.bellView) {
            [self.makeMusicView.bellView play:nil];
        }
        [self btnEnabled:NO];
    }else
    {
        if (self.makeMusicView.bellView) {
            [self btnEnabled:NO];

        }
        [self showMeasureError];
    }
}

- (IBAction)clickSaveMidiButton:(id)sender {
    
    if ([self.makeMusicView.tabsView checkMeasuresRight]) {
        self.uploadState = LxMcUploadMididata;
        NSMutableArray <LxMcNoteView *>*queuePlayNoteViews = [self.makeMusicView.tabsView lx_getPlayQueueNoteArray];
        NSTimeInterval currentTime = [[NSDate date] timeIntervalSince1970];
        /** mid制作 **/
       
        for (LxMcNoteView *noteView in queuePlayNoteViews) {
            if (noteView.isRest) {
                continue;
            }
           
        }
      
    }else
    {
        [self showMeasureError];
    }
}

- (void)zoomTap
{
    return;
    //播放音符状态 无法缩放
    if (self.makeMusicView.tabsView.isPlay) {
        return ;
    }
    
    BOOL zoomed = [self.makeMusicView zoomScale:^{
        
    }];
    
//    if (!zoomed) {
//        [self.testButton setTitle:@"琴键模式" forState:UIControlStateNormal];
//        self.playBtn.hidden = YES;
//        self.saveMidiBtn.hidden = YES;
//    }else {
//        [self.testButton setTitle:@"拖拽模式" forState:UIControlStateNormal];
//        self.playBtn.hidden = NO;
//        self.saveMidiBtn.hidden = NO;
//    }
    
    if (zoomed) {
        self.zoomScaleView.image = nil;
        
        [UIView transitionWithView:self.zoomScaleView
                          duration:kMcZoomScaleDuration
                           options:UIViewAnimationOptionTransitionNone
                        animations:^{
                            self.zoomScaleView.centerY = mScreenHeight - 46;
                           
                                self.zoomScaleView.center = CGPointMake(mScreenWidth/2.f + 20 , mScreenHeight - 46);
                          
                            
                            [self.zoomScaleView lx_zoomScale:0.7
                                      scalePriScaleDirection:LxViewScalePriCenter];
                        } completion:^(BOOL finished) {
                            [self zoomScaleInAni];
                        }];
    }else
    {
        self.zoomScaleView.image = nil;
        [UIView transitionWithView:self.zoomScaleView
                          duration:kMcZoomScaleDuration
                           options:UIViewAnimationOptionTransitionNone
                        animations:^{
                          
                                self.zoomScaleView.center = CGPointMake(mScreenWidth/2.f + 40, mScreenHeight - 20);
                          
                            [self.zoomScaleView lx_zoomScale:1.6
                                      scalePriScaleDirection:LxViewScalePriCenter];
                        } completion:^(BOOL finished) {
                            [self zoomScaleOutAni];
                        }];
    }
}

#pragma mark - uploadView_Delegate
- (void)cancelClicked
{
    [self uploadViewRemove];
}










#pragma mark - ********************  after save to playH  ********************
- (void)alertEnsureToplayH{
    WF;
    LxAlertController *alertVc = [LxAlertController lx_alertShowWithTitle:@"来弹奏自己的曲子吧^_^"
                                                                  message:@"小朋友们，可以马上弹奏自己刚做完的曲子哦!!!平时也可以去个人中心->个人作品中找到自己的曲子选择弹奏"
                                                             actionTitles:@[@"取消",@"确定"]
                                                             actionStyles:@[@(UIAlertActionStyleCancel),@(UIAlertActionStyleDefault)]
                                                          clickIndexBlock:^(NSInteger clickIndex) {
                                                              
                                                          }];
    [self presentViewController:alertVc
                       animated:YES
                     completion:^{
                         weakSelf.alertVc = alertVc;
                     }];
}


#pragma mark - ********************  获取钢琴按键  ********************

- (void)pianoKeyTouchEvent {
    NSArray *lowMiditagArray = @[@(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                 @(72),
                                ];
    
    NSArray *noteTypeArray = @[@(2),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(2),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(8),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(4),
                                 @(4),
                                 ];
    for (int i = 0; i<5; i++) {
        LxMcNoteView *noteView = [LxMcNoteView lx_defaultNoteViewWithNoteType:2 isRest:NO isDot:YES];
        noteView.userInteractionEnabled = YES;
        noteView.isKeyboardInput = YES;
        noteView.miditag = 72;
        [self.makeMusicView.tabsView addNodeWithNoeView:noteView superOffsetPoint:CGPointZero miditag:72];
    }
}

- (void)pianoKeyTouchEvent1 {
//    NSArray *lowMiditagArray = @[@(58),
//                                 @(58),
//                                 @(58),
//                                 @(58),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 @(60),
//                                 ];
//    for (int i = 0; i<18; i++) {
//        LxMcNoteView *noteView = [LxMcNoteView lx_defaultNoteViewWithNoteType:4 isRest:NO isDot:YES];
//        noteView.userInteractionEnabled = YES;
//        noteView.isKeyboardInput = YES;
//        noteView.miditag = 48;
//        [self.makeMusicView.tabsView addNodeWithNoeView:noteView superOffsetPoint:CGPointZero miditag:48];
//    }
    
//    pianoTouchStartWithTag
    [self pianoTouchStartWithTag:72];
}

- (void)pianoKeyTouchEvent2 {
    [self pianoTouchEndWithTag:72];
//    LxMcNoteView *noteView = [LxMcNoteView lx_defaultNoteViewWithNoteType:4 isRest:NO isDot:YES];
//    noteView.userInteractionEnabled = YES;
//    noteView.isKeyboardInput = YES;
//    noteView.miditag = 48;
//    [self.makeMusicView.tabsView addNodeWithNoeView:noteView superOffsetPoint:CGPointZero miditag:48];
}

- (void)pianoKeyTouchEvent3 {
    LxMcNoteView *noteView = [LxMcNoteView lx_defaultNoteViewWithNoteType:[@(4) integerValue] isRest:NO isDot:NO];
    noteView.userInteractionEnabled = YES;
    noteView.isKeyboardInput = YES;
    noteView.miditag = [@(62) integerValue];
    [self.makeMusicView.tabsView addNodeWithNoeView:noteView superOffsetPoint:CGPointZero miditag:[@(62) integerValue]];
}

- (void)setTestEvent {
    self.testButton = [[UIButton alloc]init];
    self.testButton.frame = CGRectMake(mScreenWidth/2 - 75, 0, 140, 50);
    self.testButton.backgroundColor = [UIColor redColor];
    [self.testButton setTitle:@"琴键模式" forState:UIControlStateNormal];
    [self.testButton addTarget:self action:@selector(pianoKeyTouchEvent) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.testButton];
    
    self.beginButton = [[UIButton alloc]init];
    self.beginButton.frame = CGRectMake(mScreenWidth/2 - 60, self.testButton.bottom, 50, 50);
    self.beginButton.backgroundColor = [UIColor  greenColor];
    [self.beginButton setTitle:@"start" forState:UIControlStateNormal];
    [self.beginButton addTarget:self action:@selector(pianoKeyTouchEvent1) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.beginButton];
    
    self.endButton = [[UIButton alloc]init];
    self.endButton.frame = CGRectMake(mScreenWidth/2 , self.beginButton.top, 50, 50);
    self.endButton.backgroundColor = [UIColor blueColor];
    [self.endButton setTitle:@"输出" forState:UIControlStateNormal];
    [self.endButton addTarget:self action:@selector(pianoKeyTouchEvent2) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.endButton];
}

- (void)pianoTouchStartWithTag:(int)tag {
    //记录midi按下时间
    [self startPressPinaoWithMidtag:tag];
    //默认展示符头占位 MusicNodeQuarter
    LxMcNoteView *noteView = [LxMcNoteView lx_defaultNoteViewWithNoteType:MusicNodeLoading isRest:NO isDot:NO];
    noteView.userInteractionEnabled = YES;
    noteView.isKeyboardInput = YES;
    self.currentNoteView = noteView;
    [self.makeMusicView.tabsView addNodeWithNoeView:noteView superOffsetPoint:CGPointZero miditag:tag];
}

- (void)testPianoTouchStartWithTag:(int)tag {
    //记录midi按下时间
    [self startPressPinaoWithMidtag:tag];
    
    //默认展示符头占位 MusicNodeQuarter
    LxMcNoteView *noteView = [LxMcNoteView lx_defaultNoteViewWithNoteType:MusicNodeHalf isRest:NO isDot:YES];
    noteView.userInteractionEnabled = YES;
    noteView.isKeyboardInput = YES;
    
    self.currentNoteView = noteView;
    [self.makeMusicView.tabsView addNodeWithNoeView:noteView superOffsetPoint:CGPointZero miditag:tag];
}

- (void)pianoTouchEndWithTag:(int)tag {
    [self creatNodeTypeModelArray];
    double interval = [self endPressPinaoWithMidtag:tag];
    if (interval && interval > 0.f) {
        for (YPNodeTypeModel *model in self.nodeTypeModelArray) {
            if (model.intervalMin < interval && interval < model.intervalMax) {
                debugLog(@"添加的音符类型%ld__附点音符%ld_____休止符%ld",(long)model.nodeType,(long)model.isDot,(long)model.isRest);
                if (self.currentNoteView.noteType == MusicNodeLoading && self.currentNoteView) {
                    [self.currentNoteView setIsRest:model.isRest];
                    [self.currentNoteView setIsDot:model.isDot];
                    [self.currentNoteView setNodeType:model.nodeType];
                }
            }
        }
    }
}

#pragma mark - MidiDelegate






//计算
- (void)startPressPinaoWithMidtag:(NSInteger)midtag {
    double start = [[NSDate date] timeIntervalSince1970];
    NSLog(@"按下琴键midi值%ld,按下时间%f",(long)midtag,start);
    [self.pressPinaoDrutionDict setObject:[NSString stringWithFormat:@"%f",start] forKey:[NSString stringWithFormat:@"%ld",(long)midtag]];
    
}

- (double)endPressPinaoWithMidtag:(NSInteger)midtag {
    double interval = 0.0f;
    double end = [[NSDate date] timeIntervalSince1970];
    if ([self.pressPinaoDrutionDict.allKeys containsObject:[NSString stringWithFormat:@"%ld",(long)midtag]]) {
        NSString *start = [self.pressPinaoDrutionDict valueForKey:[NSString stringWithFormat:@"%ld",(long)midtag]];
        interval = end - [start doubleValue];
        NSLog(@"按下琴键midi值%ld,按下时间%f,差值%f",(long)midtag,end,interval);
    }
    return interval;
}

- (NSMutableDictionary *)pressPinaoDrutionDict {
    if (!_pressPinaoDrutionDict) {
        _pressPinaoDrutionDict = [[NSMutableDictionary alloc]init];
    }
    return _pressPinaoDrutionDict;
}

- (void)creatNodeTypeModelArray {
//    if (!_nodeTypeModelArray) {
        self.nodeTypeModelArray = [NSMutableArray array];
        
        NSArray *noteModelArray = [self.makeMusic getLeftNotesArray];
    
        LxMcStaffLineView *tabsView = self.makeMusicView.tabsView;
        //曲谱小节最大时长
        CGFloat maxDuration = [tabsView noteBeatsForStaffBeatsType:tabsView.staffBeatsType];
  
        CGFloat diff = maxDuration;
        if (tabsView.editMode == LxMcEditModeKeyborad) {
            LxMcMeasureModel *measureModel = tabsView.ClefMeasureModelArray.firstObject;
            NSMutableArray <LxMcNoteView *>* clefNoteViews = tabsView.isEditClefUp? measureModel.clefUpNoteViewArray : measureModel.clefDoNoteViewArray;
            CGFloat totalDuration = [measureModel clefMeasureTotalDurationWithNoteViews:clefNoteViews];//已添加音符时长
            CGFloat maxDuration1 = tabsView.maxSection * [LxMcStaffLineView durationUnitWithBeatasType:tabsView.staffBeatsType];//小节最大时长
            diff = maxDuration1 - totalDuration;
        }
    
        //60的速率 4分音符为一拍  = 1s
        CGFloat speed = 60.f;
        //分子
        NSInteger beat = 1;
        //分母
        CGFloat beat_type = 4.f;
        //速度时长
        CGFloat beat_time = 60/speed;
        
        NSMutableArray *musicNodeTimeArray = [[NSMutableArray alloc]init];
    
        NSMutableArray *musicNodeTimeArrayT = [[NSMutableArray alloc]init];
    
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

- (NSMutableArray *)restNodeTypeModelArray {
    
    if (!_restNodeTypeModelArray) {
        _restNodeTypeModelArray = [[NSMutableArray alloc]init];
        NSArray *rightModelArray = [self.makeMusic getRightNotesArray];
        for (int i=0; i<rightModelArray.count; i++) {
            CKNoteModel *model = rightModelArray[i];
            YPNodeTypeModel *nodeModel = [[YPNodeTypeModel alloc]init];
            if (i==0) {
                nodeModel.intervalMin = 0;
            }else {
                YPNodeTypeModel *oldmodel = _restNodeTypeModelArray[i-1];
                nodeModel.intervalMin = oldmodel.intervalMax;
            }
            
            if (i==rightModelArray.count - 1) {
                nodeModel.intervalMax = MAXFLOAT;
            }else {
                nodeModel.intervalMax = 1.f/model.noteType;
            }
            
            nodeModel.nodeType = model.noteType;
            nodeModel.isRest = model.isRest;
            nodeModel.isDot = model.isRest;
            [_restNodeTypeModelArray addObject:nodeModel];
        }
    }
    return _restNodeTypeModelArray;
}


#pragma mark - ********************  dealloc  ********************
- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    debugLog(@"%s",__func__);
}
@end
