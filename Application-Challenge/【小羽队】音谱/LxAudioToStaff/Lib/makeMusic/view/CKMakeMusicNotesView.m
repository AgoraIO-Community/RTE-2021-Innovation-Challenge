//
//  CKMakeMusicNotesView.m
//  SmartPiano
//
//  Created by xy on 2018/5/19.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import "CKMakeMusicNotesView.h"
#import "LxMcStaffHeader.h"
#import "UIImage+Default.h"
#import "CKNoteModel.h"

@interface CKMakeMusicNotesView ()

@property (nonatomic, strong) NSMutableArray<LxMcNoteView *> *notesViewArray;

@end
@implementation CKMakeMusicNotesView

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
//        [self initNotes];
    }
    return self;
}


- (void)initABCNotes {
    CGFloat centerX = self.width / self.dataSource.count;
    for (int i = 0 ; i < self.dataSource.count ; i ++) {
        //设置音符泡泡
        CKNoteModel *noteModel = self.dataSource[i];
        LxMcNoteView *noteView = [LxMcNoteView lx_defaultABCNoteViewWithNoteType:noteModel.noteType isRest:noteModel.isRest isDot:noteModel.isDot isTouchUI:YES isABC:YES abcName:noteModel.abcName];
//        noteView.backgroundColor = [UIColor blueColor];
        noteView.x = i * centerX;
        noteView.y = 21 - i*2;
        noteView.userInteractionEnabled = YES;
        [self addSubview:noteView];

        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(bubbleTap:)];
        tap.numberOfTapsRequired = 1;
        [noteView addGestureRecognizer:tap];
        UIPanGestureRecognizer *pan = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(bubblePan:)];
        [noteView addGestureRecognizer:pan];
        [self.notesViewArray addObject:noteView];
    }
}


- (void)initNotes {
    CGFloat centerY = self.height / self.dataSource.count;
    for (int i = 0 ; i < self.dataSource.count ; i ++) {
        //创建下面冰块
//        UIImageView *iceLandView = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"write_stage"]];
//        iceLandView.centerX = self.width * 0.5;
//        iceLandView.tag = i + 100;
//        [self addSubview:iceLandView];
        //设置音符泡泡
        CKNoteModel *noteModel = self.dataSource[i];
        LxMcNoteView *noteView = [LxMcNoteView lx_bubbleNoteViewWithNoteType:noteModel.noteType isRest:noteModel.isRest isDot:noteModel.isDot];
        noteView.centerX = self.width * 0.5;
        noteView.y = i * centerY;
        noteView.userInteractionEnabled = YES;
        [self addSubview:noteView];
//        iceLandView.y = CGRectGetMaxY(noteView.frame) - iceLandView.height;
//        if (WeLibraryAPI.sharedInstance.bookState > kBookStateTwo) {
//            iceLandView.y += 6;
//        }
        
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(bubbleTap:)];
        tap.numberOfTapsRequired = 1;
        [noteView addGestureRecognizer:tap];
        UIPanGestureRecognizer *pan = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(bubblePan:)];
        [noteView addGestureRecognizer:pan];
        [self.notesViewArray addObject:noteView];
    }
}

- (void)noteViewSeletedAnimation:(LxMcNoteView *)noteView {
    [noteView lx_resetSelecteState:YES];
//    POPBasicAnimation *centerYAnimation = [POPBasicAnimation easeInEaseOutAnimation];
//    centerYAnimation.property = [POPAnimatableProperty propertyWithName:kPOPViewCenter];
//    centerYAnimation.duration = 5.f;
//    centerYAnimation.toValue = [NSValue valueWithCGPoint:CGPointMake(noteView.centerX, noteView.centerY - [noteView.zoom_scale floatValue] * 40)];
//    centerYAnimation.autoreverses = YES;
//    centerYAnimation.repeatCount = NSNotFound;
//    [noteView pop_addAnimation:centerYAnimation forKey:@"centerY"];
}

- (void)resetNoteViewAnimation {
    NSInteger index = 0;
    CGFloat centerY = self.height / self.notesViewArray.count / self.transform.d;
//    debugLog(@"a=%f b=%f c=%f d=%f x=%f y=%f",self.transform.a,self.transform.b,self.transform.c,self.transform.d,self.transform.tx,self.transform.ty);
    for (LxMcNoteView *tempView in self.notesViewArray) {
//        if ([tempView pop_animationKeys].count > 0) {
//            [tempView pop_removeAllAnimations];
//            
//            tempView.centerX = self.width * 0.5 / self.transform.a;
//            tempView.y = index * centerY;
//        }
        [tempView lx_resetSelecteState:NO];
        index ++;
    }
}

- (void)iceViewAlpha:(CGFloat)alpha {
    for (NSInteger index = 0; index < self.dataSource.count; index ++) {
        UIView *iceView = [self viewWithTag:index + 100];
        iceView.alpha = alpha;
    }
    
}

#pragma mark  --  events reposne
- (void)bubbleTap:(UITapGestureRecognizer *)tap{
    LxMcNoteView *noteView = (LxMcNoteView *)tap.view;
    if ([noteView isKindOfClass:[LxMcNoteView class]]) {
//        if (self.block) {
//            self.block(noteView);
//        }
//        [self resetNoteViewAnimation];
//        [self noteViewSeletedAnimation:noteView];
        self.seletedIndex = [self.notesViewArray indexOfObject:noteView];
    }
}

- (void)bubblePan:(UIPanGestureRecognizer *)pan {
    if (pan.state == UIGestureRecognizerStateBegan) {
        LxMcNoteView *noteView = (LxMcNoteView *)pan.view;
        if ([noteView isKindOfClass:[LxMcNoteView class]]) {
            self.seletedIndex = [self.notesViewArray indexOfObject:noteView];
        }
    }
    if (self.panMoveBlock) {
        self.panMoveBlock(pan);
    }
}

- (void)initABCNameArray:(NSArray *)nameArray {
    _dataSource = nameArray;
    [self initABCNotes];
}

#pragma mark  --  setter
- (void)setDataSource:(NSArray *)dataSource {
    _dataSource = dataSource;
    [self initNotes];
}

- (void)setSeletedIndex:(NSInteger)seletedIndex {
    _seletedIndex = seletedIndex;
    LxMcNoteView *noteView = self.notesViewArray[seletedIndex];
    if (self.block) {
        self.block(noteView);
    }
    if (noteView.noteType == MusicNodeShowABC || noteView.isABC) {
        return;
    }
    [self resetNoteViewAnimation];
    [self noteViewSeletedAnimation:noteView];
}

#pragma mark  --  getter
- (NSMutableArray <LxMcNoteView *>*)notesViewArray {
    if (!_notesViewArray) {
//        _notesViewArray = [NSMutableArray arrayWithArray:@[[LxMcNoteView lx_bubbleNoteViewWithNoteType:MusicNodeWhole
//                                                                                                   isRest:YES
//                                                                                                    isDot:NO],
//                                                              [LxMcNoteView lx_bubbleNoteViewWithNoteType:MusicNodeHalf
//                                                                                                   isRest:YES
//                                                                                                    isDot:NO],/*
//                                                              [LxMcNoteView lx_bubbleNoteViewWithNoteType:MusicNodeQuarter
//                                                                                                   isRest:YES
//                                                                                                    isDot:NO],
//                                                              [LxMcNoteView lx_bubbleNoteViewWithNoteType:MusicNodeEighth
//                                                                                                   isRest:YES
//                                                                                                    isDot:NO],*/
//                                                              [LxMcNoteView lx_bubbleNoteViewWithNoteType:MusicNodeEighth
//                                                                                                   isRest:NO
//                                                                                                    isDot:NO]]];
        _notesViewArray = [[NSMutableArray alloc] init];
    }
    return _notesViewArray;
}

@end
