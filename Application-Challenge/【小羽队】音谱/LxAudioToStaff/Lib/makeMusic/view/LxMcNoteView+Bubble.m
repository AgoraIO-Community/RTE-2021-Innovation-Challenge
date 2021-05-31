//
//  LxMcNoteView+Bubble.m
//  SmartPiano
//
//  Created by DavinLee on 2018/2/5.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "LxMcNoteView+Bubble.h"
#import <objc/runtime.h>
#import "CALayer+Default.h"
#import "UIImage+Default.h"
#import "WeBasicAnimation.h"

@implementation LxMcNoteView (Bubble)
#pragma mark - CallFunction
/**
 *@description 获取默认泡泡音符
 *@param noteType 音符时值类型
 *@param isRest 是否休止符
 *@param isDot 是否附点音符
 **/
+ (LxMcNoteView *)lx_bubbleNoteViewWithNoteType:(MusicNodeType)noteType
                                         isRest:(BOOL)isRest
                                          isDot:(BOOL)isDot {
    LxMcNoteView *noteView = [LxMcNoteView new];
    noteView.noteType = noteType;
    noteView.isRest = isRest;
    noteView.isDot = isDot;
    [noteView createBubbleUI];
    
    return noteView;
}


+ (LxMcNoteView *)lx_defaultNoteViewWithNoteType:(MusicNodeType)noteType
                                          isRest:(BOOL)isRest
                                           isDot:(BOOL)isDot {
    LxMcNoteView *noteView = [LxMcNoteView new];
    noteView.noteType = noteType;
    noteView.isRest = isRest;
    noteView.isDot = isDot;
    [noteView createStaffUI];
    
    return noteView;
}

+ (LxMcNoteView *)lx_defaultABCNoteViewWithNoteType:(MusicNodeType)noteType
                                          isRest:(BOOL)isRest
                                           isDot:(BOOL)isDot
                                           isTouchUI:(BOOL)isTouchUI
                                          isABC:(BOOL)isABC
                                        abcName:(NSString *)name{
    
    LxMcNoteView *noteView = [LxMcNoteView new];
    noteView.noteType = noteType;
    noteView.isRest = isRest;
    noteView.isDot = isDot;
    noteView.isTouchABC = isTouchUI;
    noteView.isABC = isABC;
    noteView.ABCName = name;
    [noteView createStaffUI];
    return noteView;
}


/**
 *@description 设置音符是否选中状态（作曲游戏)
 **/
- (void)lx_resetSelecteState:(BOOL)selected
{
    self.selected = selected;
    if (self.selected) {
        CABasicAnimation *rotationAni = [WeBasicAnimation getRotationAnimationWithDuration:4
                                                                                 fromValue:0
                                                                                   toValue:M_PI * 2
                                                                                statusSave:NO autoReverse:NO];
        rotationAni.repeatCount = NSNotFound;
        [self.selectedBoarderView.layer addAnimation:rotationAni forKey:@"ani"];
    }else{
        [self.selectedBoarderView.layer removeAllAnimations];
        [self.selectedBoarderView removeFromSuperview];
        self.selectedBoarderView = nil;
    }
}

- (void)lx_zoomNoteViewScale:(CGFloat)scale scalePriScaleDirection:(LxViewScalePriDirection)direction
{
    [self lx_zoomScale:scale scalePriScaleDirection:direction];
    if (self.selected) {
         [self.selectedBoarderView lx_zoomScale:scale scalePriScaleDirection:LxViewScalePriCenter];
    }
   
}

#pragma mark - Function
- (void)createBubbleUI
{
    [self clearAllStaffUI];
    NSMutableString *bubbleImageName = [NSMutableString stringWithString:@"writeBubbleNote"];
    [bubbleImageName appendFormat:@"_%ld",(long)self.noteType];
    if (self.isRest) {
        [bubbleImageName appendString:@"_rest"];
    }else if(self.isDot)
    {
        [bubbleImageName appendString:@"_dot"];
    }

    [bubbleImageName appendString:@"@2x"];
    UIImage *bubbleImage = [UIImage lx_imageFromBundleWithName:[self reSetImageName:bubbleImageName]];
    CGRect frame = self.frame;
    frame = CGRectMake(CGRectGetMidX(frame) - bubbleImage.size.width /2.f,
                       CGRectGetMidY(frame) - bubbleImage.size.height/2.f,
                       bubbleImage.size.width,
                       bubbleImage.size.height);
    self.image = bubbleImage;
    self.frame = frame;
    if (self.noteType > MusicNodeWhole) {
        self.headDirection = LxMcNoteHead_left_down;
    }
}


@end
