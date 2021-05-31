//
//  CKMakeMusicView.m
//  SmartPiano
//
//  Created by xy on 2018/5/19.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import "CKMakeMusicView.h"
#import "UIButton+Default.h"
#import "LxMcStaffLineView+MeasureLayout.h"
#import "LxSelectionButton.h"
#import "LxCanInteraSubImageView.h"
#import "UIImage+Default.h"

typedef struct {
    CGFloat backScale;
    CGFloat bubbleScale;
    BOOL isScale;
    BOOL zoomed;
}ZoomRelated;

@interface CKMakeMusicView ()<LxMcStaffLineViewDelegate,MakeMusicScrollBarDelegate> {
    ZoomRelated zoomRelated;
}

@property (nonatomic, strong) UIButton *seletedBeatButton;
/**
 曲谱滚动
 */
@property (nonatomic, weak) UIScrollView *scrollView;
///**
// *@description 连音线按钮
// **/
//@property (strong, nonatomic) UIButton *lineStateBtn;

@property (nonatomic, weak) UIView *backView;


@end

@implementation CKMakeMusicView

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
//        UIImage *image = [UIImage imageNamed:@"write_back"];
//        LxCanInteraSubImageView *imageView = [[LxCanInteraSubImageView alloc] initWithImage:image];
//        imageView.frame = CGRectMake((mScreenWidth - image.size.width)/2.f, (mScreenHeight - image.size.height)/2.f, image.size.width, image.size.height);
//        if (WeLibraryAPI.sharedInstance.bookState == kBookStateThree) {
//            imageView.centerY -= 30;
//        }
//        [self.backView addSubview:imageView];
        self.opreation = YES;
        [self addAuxiliaryUI];
        [self setMetronome];
        
    }
    return self;
}

- (void)setMetronome {

    
//    //节拍器
//    self.metronomeManage = [[YDmetronomeManage alloc]init];
//    self.metronomeManage.delegate = self;
//    self.metronomeManage.metronomeOn = NO;
//    [self.metronomeManage startMetronomeInFollowModeWithPer_Minite:60/(kMcStaffWholeDuration/4) playRate:1];
}

- (void)setMetronomeOpen:(BOOL)isOpen {
   
}

- (void)addAuxiliaryUI{

//    /**   设置连音btn    **/
//    self.lineStateBtn = [UIButton buttonWithType:UIButtonTypeCustom];
//    self.lineStateBtn.frame = CGRectMake(300, 0, 75, 25);
//    [self.lineStateBtn setTitle:@"连音线" forState:UIControlStateNormal];
//    [self.lineStateBtn setTitle:@"连音中" forState:UIControlStateSelected];
//    [self.lineStateBtn addTarget:self action:@selector(clickLineBtn:) forControlEvents:UIControlEventTouchUpInside];
//    [self addSubview:self.lineStateBtn];
    
}

#pragma mark  --  public func
- (BOOL)zoomScale:(void (^)(void))completed {
  
    zoomRelated.zoomed = NO;
    
    [UIView animateWithDuration:kMcZoomScaleDuration animations:^{
        [self zoomBackView];
        [self zoomNotesView];
        
    } completion:^(BOOL finished) {
        self->zoomRelated.zoomed = YES;
        if (completed) {
            completed();
        }
    }];
    zoomRelated.isScale = !zoomRelated.isScale;
    return zoomRelated.isScale;
}


- (void)addSections:(NSInteger)count {

    self.tabsView.sectionIndex = count - 1;
//    for (NSInteger index = 0; index < count; index++) {
        [self.tabsView measureAdd];
//    }
    [self.tabsView resetOctave];
    [self.scrollBar updateProgressViewxWithCurrentPageOriginX:0];
    if ([self.scrollBar.delegate respondsToSelector:@selector(resetOriginX:)]) {
        [self.scrollBar.delegate resetOriginX:0];
    }
}

- (void)reloadUI {
    [self.tabsView resetDefaultStaffLineView];
    [self addSections:8];
//    [self.tabsView setUpStaticNotes];
//    self.fourButton.selected = YES;
//    self.eightButton.selected = NO;
}

#pragma mark  --  events response
- (void)eightBeatBtn:(UIButton *)button {
    if (!button.selected) {
        [self.tabsView resetDefaultStaffLineView];
        [self addSections:8];
        button.selected = YES;
        self.fourButton.selected = NO;
        [self.tabsView setUpStaticNotes];
    }
}

- (void)fourBeatBtn:(UIButton *)button {
    if (!button.selected) {
        [self.tabsView resetDefaultStaffLineView];
        [self addSections:4];
        button.selected = YES;
        self.eightButton.selected = NO;
        [self.tabsView setUpStaticNotes];
    }
}

- (void)beatsBtnClicked:(UIButton *)button {
    if (self.seletedBeatButton != button) {
        self.seletedBeatButton.selected = NO;
        button.selected = YES;
        self.seletedBeatButton = (LxSelectionButton *)button;
        [self.tabsView resetDefaultStaffLineView];
        [self.tabsView resetBeatsType:button.tag - 50];
        [self addSections:self.tabsView.sectionIndex + 1];
        [self.tabsView setUpStaticNotes];
        
        if (_beatsBtnClick) {
            self.beatsBtnClick();
        }
    }
}

- (void)changeCandyButton:(UIButton *)button {
    //如果在播放 不执行
    if (self.tabsView.isPlay) {
        return;
    }

    button.selected = !button.selected;
    self.tabsView.candy = button.selected;
    
//    [self.tabsView resetDefaultStaffLineView];
//    [self addSections:self.tabsView.sectionIndex + 1];
//    [self.tabsView setUpStaticNotes];
    
}


- (void)clickLineBtn:(UIButton *)btn
{
    btn.selected = !btn.selected;
    self.tabsView.lineTapState = btn.selected;
}

#pragma mark  --  LxMcStaffLineViewDelegate
- (void)reSetScrollViewContent {
    CGPoint offset = self.scrollView.contentOffset;
    self.scrollView.contentSize = CGSizeMake(MAX(mScreenWidth, CGRectGetWidth(self.tabsView.frame)), CGRectGetHeight(self.scrollView.frame));
    self.scrollView.contentOffset = offset;
//    [self.scrollBar resetScrollBarWithMaxOffset:CGRectGetWidth(self.tabsView.frame) - CGRectGetWidth(self.scrollView.frame)
//                                        initOffsetX:0
//                                     currentOriginX:-self.scrollView.contentOffset.x];
}

- (void)playEnd {
    if (self.playCompleted) {
        self.playCompleted();
    }
    [self.bellView stop];
}

- (void)reScrollToTop {
    self.scrollView.contentOffset = CGPointMake(0, 0);
    [self reSetScrollViewContent];
}

- (void)reScrollToDown {
    [self.scrollView setContentOffset:CGPointMake(self.scrollView.contentSize.width + 20, 0) animated:YES];
}

- (void)scrollTox:(CGFloat)scrollX {
    debugLog(@"滚动距离%f",scrollX);
    [UIView animateWithDuration:0.25 animations:^{
      [self.scrollView setContentOffset:CGPointMake(MAX(scrollX, 0), 0)];
    }];
}

#pragma mark  -- MakeMusicScrollBarDelegate
- (void)resetOriginX:(CGFloat)offset_x {
    debugLog(@"滑动距离%f",offset_x);
    self.scrollView.contentOffset = CGPointMake(-offset_x, 0);
}

#pragma mark  --  setter
- (void)setTabsView:(LxMcStaffLineView *)tabsView {
    _tabsView = tabsView;
    tabsView.delegate = self;
    tabsView.frame = self.scrollView.bounds;
    
    [self.scrollView addSubview:tabsView];
    [self reloadUI];
//    [self scrollBar];
}

- (void)setRightNotesView:(CKMakeMusicNotesView *)rightNotesView {
    _rightNotesView = rightNotesView;
    ckWeakSelf
    rightNotesView.block = ^(LxMcNoteView *seletedNote) {
        [weakSelf.leftNotesView resetNoteViewAnimation];
        [weakSelf noteTap:seletedNote];
    };
    rightNotesView.panMoveBlock = ^(UIPanGestureRecognizer *pan) {
        [weakSelf.tabsView handlepanGesture:pan];
    };
    [self addSubview:rightNotesView];
}

- (void)setLeftNotesView:(CKMakeMusicNotesView *)leftNotesView {
    _leftNotesView = leftNotesView;
    ckWeakSelf
    leftNotesView.block = ^(LxMcNoteView *seletedNote) {
        [weakSelf.rightNotesView resetNoteViewAnimation];
        [weakSelf noteTap:seletedNote];
    };
    leftNotesView.panMoveBlock = ^(UIPanGestureRecognizer *pan) {
        [weakSelf.tabsView handlepanGesture:pan];
    };
    leftNotesView.seletedIndex = 0;
    [self addSubview:leftNotesView];
}

- (void)noteTap:(LxMcNoteView *)noteView {
    self.tabsView.modelNodeType = noteView.noteType;
    self.tabsView.modelDot = noteView.isDot;
    self.tabsView.modelRest = noteView.isRest;
}

- (void)setBellView:(CKBellView *)bellView {
    _bellView =  bellView;
    [self.backView addSubview:bellView];
}

- (void)setFourButton:(UIButton *)fourButton {
    _fourButton = fourButton;
    fourButton.center = CGPointMake(810, CGRectGetMidY(self.scrollBar.frame));
    [fourButton addTarget:self action:@selector(fourBeatBtn:) forControlEvents:UIControlEventTouchUpInside];
    [self.backView addSubview:fourButton];
    [self fourBeatBtn:fourButton];
}

- (void)setEightButton:(UIButton *)eightButton {
    _eightButton = eightButton;
    eightButton.center = CGPointMake(860, CGRectGetMidY(self.scrollBar.frame));
    [eightButton addTarget:self action:@selector(eightBeatBtn:) forControlEvents:UIControlEventTouchUpInside];
    [self.backView addSubview:eightButton];
    [self eightBeatBtn:eightButton];
}

- (void)setCutButton:(UIButton *)cutButton {
    _cutButton = cutButton;
    cutButton.center = CGPointMake(840, CGRectGetMidY(self.scrollBar.frame));
    [cutButton addTarget:self action:@selector(tapCutButton:) forControlEvents:UIControlEventTouchUpInside];
    [self.backView addSubview:cutButton];
    [self fourBeatBtn:[UIButton new]] ;
}

- (void)setAddButton:(UIButton *)addButton {
    _eightButton = addButton;
    addButton.center = CGPointMake(890, CGRectGetMidY(self.scrollBar.frame));
    [addButton addTarget:self action:@selector(tapAddButton:) forControlEvents:UIControlEventTouchUpInside];
    [self.backView addSubview:addButton];
//    [self eightBeatBtn:eightButton];
}

- (void)tapCutButton:(UIButton *)button {
    [self.tabsView measureDelete];
//    [self addSections:4];
}

- (void)tapAddButton:(UIButton *)button {
    [self.tabsView measureAdd];
//    [self addSections:4];
}


- (void)setBeatView:(UIView *)beatView {
//    _beatView = beatView;
//    for (UIButton *button in beatView.subviews) {
//        [button addTarget:self action:@selector(beatsBtnClicked:) forControlEvents:UIControlEventTouchUpInside];
//        if (button.selected) {
//            self.seletedBeatButton = button;
//        }
//    }
//    CGRect rect = _beatView.frame;
//    rect.origin.x = 190.5;
//    rect.origin.y = rect.origin.y+10;
//    _beatView.frame = rect;
//
//    [self.backView addSubview:beatView];
}

- (void)setChangeCandyButton:(UIButton *)changeCandyButton {
    _changeCandyButton = changeCandyButton;
    [changeCandyButton addTarget:self action:@selector(changeCandyButton:) forControlEvents:UIControlEventTouchUpInside];
    [self.backView addSubview:changeCandyButton];
}

#pragma mark  --  private func
- (void)zoomBackView {
 
    //设置拖拽音符显示隐藏
//    self.leftNotesView.hidden = !self.leftNotesView.hidden;
//    self.rightNotesView.hidden = !self.rightNotesView.hidden;

    //设置乐谱编辑模式
//    self.tabsView.editMode = LxMcEditModeMove;
//    [self.tabsView measureAddForNote:self.tabsView.editMode==LxMcEditModeMove];
//    
    zoomRelated.backScale = kMcZoomScale;
    if (zoomRelated.isScale) {
        zoomRelated.backScale = kMcBubbleZoomOutScale;
    }
    self.backView.transform = CGAffineTransformMakeScale(zoomRelated.backScale, zoomRelated.backScale);
}

- (void)zoomNotesView {
    zoomRelated.bubbleScale = kMcBubbleZoomOutScale;
    CGFloat alpha = 0.0;
    if (zoomRelated.isScale) {
        zoomRelated.bubbleScale = kMcBubbleZoomScale;
        alpha = 1.0;
    }
    self.leftNotesView.transform = CGAffineTransformMakeScale(zoomRelated.bubbleScale, zoomRelated.bubbleScale);
    self.leftNotesView.x = 0;
    [self.leftNotesView iceViewAlpha:alpha];
    self.rightNotesView.transform = CGAffineTransformMakeScale(zoomRelated.bubbleScale, zoomRelated.bubbleScale);
    self.rightNotesView.x = self.width - self.rightNotesView.width;
    [self.rightNotesView iceViewAlpha:alpha];
}

- (void)setOpreation:(BOOL)opreation {
    _opreation = opreation;
    self.fourButton.userInteractionEnabled =
    self.eightButton.userInteractionEnabled =
    self.leftNotesView.userInteractionEnabled =
    self.rightNotesView.userInteractionEnabled =
    self.tabsView.userInteractionEnabled =
    self.scrollBar.userInteractionEnabled =
    self.beatView.userInteractionEnabled = self.opreation;
}

#pragma mark  --  getter
- (UIScrollView *)scrollView {
    if (!_scrollView) {
        /** 五线谱滚动区域设置 **/
        CGRect frame = CGRectZero;

                frame = CGRectMake(0, 30, mScreenWidth , mScreenHeight - 60);

        UIScrollView *scrollView = [[UIScrollView alloc] initWithFrame:frame];
        scrollView.showsHorizontalScrollIndicator = YES;
        scrollView.alwaysBounceHorizontal = NO;
        scrollView.clipsToBounds = YES;
        scrollView.userInteractionEnabled = YES;
        scrollView.scrollEnabled = YES;
        scrollView.showsHorizontalScrollIndicator = NO;
        scrollView.alwaysBounceVertical = NO;
//        [self.backView addSubview:scrollView];
        [self addSubview:scrollView];
        self.userInteractionEnabled = YES;
        _scrollView = scrollView;
        scrollView.contentInset = UIEdgeInsetsMake(0, -40, 0,60);
       
       
        [_scrollView addObserver:self forKeyPath:@"contentOffset" options:NSKeyValueObservingOptionNew context:nil];
        [_scrollView addObserver:self forKeyPath:@"contentSize" options:NSKeyValueObservingOptionNew context:nil];
       
    }
    return _scrollView;
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary<NSKeyValueChangeKey,id> *)change context:(void *)context{
    debugLog(@"scroll.x = %f contentSize = %f",_scrollView.contentOffset.x,_scrollView.contentSize.width);
}

- (BOOL)isZoomOut
{
    return zoomRelated.isScale;
}

- (MackMusicVcScrollBar *)scrollBar {
    if (!_scrollBar) {
//        /** 滚动拖条设置 **/
//        MackMusicVcScrollBar *scrollRollBar = [[MackMusicVcScrollBar alloc] initWithFrame:CGRectMake(155, 650, 640, 17.5)];
//
//        [scrollRollBar setupDefault];
//        [scrollRollBar resetScrollBarWithMaxOffset:self.scrollView.contentSize.width initOffsetX:0 currentOriginX:0];
//        scrollRollBar.userInteractionEnabled = YES;
//        scrollRollBar.delegate = self;
//        [self.backView addSubview:scrollRollBar];
//        _scrollBar = scrollRollBar;
    }
    return _scrollBar;
}

- (UIView *)backView {
    if (!_backView) {
//        UIView *view = [[UIView alloc] initWithFrame:self.bounds];
//        view.center = self.center;
//        [self addSubview:view];
//        _backView = view;
    }
    return _backView;
}




@end
