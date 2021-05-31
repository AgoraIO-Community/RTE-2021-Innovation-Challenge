//
//  MackMusicVcScrollBar.m
//  SmartPiano
//
//  Created by 李翔 on 17/2/20.
//  Copyright © 2017年 XiYun. All rights reserved.
//

#import "MackMusicVcScrollBar.h"
#import "UIImage+Default.h"
#define kOffsetX 0
#define kRangeOffsetHeight 20
#define kScreenPageWidth 848.f

@interface MackMusicVcScrollBar ()


/**
 *拖动条中间纹理
 */
@property (strong, nonatomic) CALayer *textureLayer;

/**
 *记录手势起始点
 */
@property (assign, nonatomic) CGPoint startTouchPoint;
/**
 *記錄開始手勢位置x
 */
@property (assign, nonatomic) CGFloat startTouchX;

/**
 *記錄原頁面最大偏移x
 */
@property (assign, nonatomic) CGFloat maxOffsetx;
/**
 *記錄原頁面初始偏移x
 */
@property (assign, nonatomic) CGFloat initOffsetx;
/******************************************************UI相关持有（为缩放所准备）***************************************************************/



@end

@implementation MackMusicVcScrollBar

- (void)setupDefault
{
    
    //背景设置
    UIImage *bgImage = [UIImage imageNamed:@"write_rollBack"];
    CGRect frame = self.frame;
    frame.size = self.size;
    self.frame = frame;
    self.image = bgImage;
    self.contentMode = UIViewContentModeScaleToFill;
    
    //滚动条设置
    UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(kOffsetX, 0, 200, CGRectGetHeight(self.frame))];
    
    imageView.clipsToBounds = YES;
    imageView.contentMode = UIViewContentModeScaleToFill;
    imageView.image = [UIImage imageNamed:@"write_roll_progress"];
    

    [self addSubview:imageView];
    self.progressView = imageView;
    self.canDrag = YES;
    
    CALayer *textureLayer = [CALayer layer];
    textureLayer.frame = CGRectMake(85, 1.5, 11.5, 10.5);
    textureLayer.contents = (id)[[UIImage imageNamed:@"write_roll_texture"] CGImage];
    [imageView.layer addSublayer:textureLayer];
    self.textureLayer = textureLayer;
    
    UIPanGestureRecognizer *pan = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(pangesture:)];
    [self.progressView addGestureRecognizer:pan];
    
}

- (void)resetScrollBarWithMaxOffset:(CGFloat)maxOffsetX initOffsetX:(CGFloat)initOffsetX currentOriginX:(CGFloat)originX;
{
    self.initOffsetx = initOffsetX;
    self.maxOffsetx = - maxOffsetX;
    CGFloat progressWidthScale = MIN(1, kScreenPageWidth / (maxOffsetX + kScreenPageWidth));
  
    self.progressView.width = self.width * progressWidthScale - 2;
    [CATransaction begin];
    [CATransaction setAnimationDuration:0.2];
    self.textureLayer.position = CGPointMake(self.progressView.width/2.f , self.progressView.height/2.f);
    [CATransaction commit];
    [self updateProgressViewxWithCurrentPageOriginX:originX];
}

- (void)updateProgressViewxWithCurrentPageOriginX:(CGFloat)originX
{
    self.progressView.x = MIN(self.width - self.progressView.width, MAX(0, (originX - self.initOffsetx) / (self.maxOffsetx ) * (self.width - self.progressView.width)));
}

- (void)pangesture:(UIPanGestureRecognizer *)ges
{
    if (!self.canDrag) {
        return;
    }
    CGPoint point = [ges locationInView:self];
    if (ges.state == UIGestureRecognizerStateBegan) {
        self.startTouchPoint = point;
        self.startTouchX = self.progressView.x;
    }else if (ges.state == UIGestureRecognizerStateChanged && self.progressView.width < self.width)
    {
        CGFloat touchOffsetX =  point.x - self.startTouchPoint.x + self.startTouchX;
        touchOffsetX = MIN((self.width - self.progressView.width - kOffsetX - 1), MAX(1, touchOffsetX));
        self.progressView.x = touchOffsetX;
        CGFloat offsetxScale = self.progressView.x / (self.width - self.progressView.width);
        if ([_delegate respondsToSelector:@selector(resetOriginX:)]) {
            [_delegate resetOriginX:(self.maxOffsetx * offsetxScale + self.initOffsetx)];
        }
        
    }else if (ges.state == UIGestureRecognizerStateEnded  && self.progressView.width < self.width)
    {
//        CGFloat touchOffsetX =  point.x - self.startTouchPoint.x + self.startTouchX;
//        touchOffsetX = MIN((self.width - self.progressView.width - kOffsetX - 1), MAX(1, touchOffsetX));
//        self.progressView.x = touchOffsetX ;
        CGFloat offsetxScale = self.progressView.x / (self.width - self.progressView.width);
        if ([_delegate respondsToSelector:@selector(resetOriginX:)]) {
            [_delegate resetOriginX:(self.maxOffsetx * offsetxScale + self.initOffsetx)];
        }
    }
}


- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event
{
    CGRect rect = self.bounds;
    rect.size.height += kRangeOffsetHeight;
    rect.origin.y -= kRangeOffsetHeight/2.f;
    if (CGRectContainsPoint(rect, point)) {
        return self.progressView;
    }
    return nil;
}

@end
