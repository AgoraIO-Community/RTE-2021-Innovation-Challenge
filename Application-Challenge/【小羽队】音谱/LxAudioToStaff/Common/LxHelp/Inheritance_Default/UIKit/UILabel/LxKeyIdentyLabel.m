//
//  LxKeyIdentyLabel.m
//  SmartPiano
//
//  Created by DavinLee on 2018/5/28.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import "LxKeyIdentyLabel.h"
#import <CoreText/CoreText.h>
#import "CATextLayer+Default.h"
@interface LxKeyIdentyLabel ()
/** 音阶layer **/
@property (strong, nonatomic) CATextLayer *stepLayer;
@end
@implementation LxKeyIdentyLabel
/** init **/
- (instancetype)initWithFrame:(CGRect)frame
{
    if (self == [super initWithFrame:frame]) {
        debugLog(@"进入键位note的init 了 ");
    }
    return self;
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    if ([self.keyTag integerValue] >= 21) {
        [self lx_setupWithKeyTag:[self.keyTag integerValue]];
    }
    
}
#pragma mark - ********************  Call_Function  ********************
/**
 *@description 获取琴键的音阶
 *@param keyTag 琴键tag
 *@return NSInteger 音阶
 **/
+ (NSInteger)lx_stepWithKeyTag:(NSInteger)keyTag
{
    return (keyTag - 12) / 12;
}
/**
 *@description 获取琴键音名
 *@param keytag 琴键tag
 *@praram step 音阶
 **/
+ (NSString *)lx_octaveWithKeyTag:(NSInteger)keytag step:(NSInteger)step
{
    NSInteger octaveInt = keytag - (step - 1) * 12 - 23;
    switch (octaveInt) {
        case 1:
        {
            if (step >= 3) {
                return @"c";
            }else
            {
                return @"C";
            }
        }
            break;
        case 3:
        {
            if (step >= 3) {
                return @"d";
            }else
            {
                return @"D";
            }
        }
            break;
        case 5:
        {
            if (step >= 3) {
                return @"e";
            }else
            {
                return @"E";
            }
        }
            break;
        case 6:
        {
            if (step >= 3) {
                return @"f";
            }else
            {
                return @"F";
            }
        }
            break;
        case 8:
        {
            if (step >= 3) {
                return @"g";
            }else
            {
                return @"G";
            }
        }
            break;
        case 10:
        {
            if (step >= 3) {
                return @"a";
            }else
            {
                return @"A";
            }
        }
            break;
        case 12:
        {
            if (step >= 3) {
                return @"b";
            }else
            {
                return @"B";
            }
        }
            break;
            
        default:
            return @"无";
            break;
    }
}
- (void)lx_setupWithKeyTag:(NSInteger)keyTag
{
    self.textAlignment = NSTextAlignmentCenter;/** 统一居中处理 **/
    NSInteger step = [LxKeyIdentyLabel lx_stepWithKeyTag:keyTag];
    NSString *octave = [LxKeyIdentyLabel lx_octaveWithKeyTag:keyTag step:step];
    self.text = octave;
    CGSize size = CGSizeMake(self.font.pointSize + 1, self.font.pointSize + 1);
    /** 添加音阶layer **/
    BOOL smallFont = keyTag >= 48;
    if (self.stepLayer) {
        [self.stepLayer removeFromSuperlayer];
    }
    UIFont *stepFont = [UIFont systemFontOfSize:smallFont ? self.font.pointSize * 0.4 : self.font.pointSize * 0.7];
    self.stepLayer = [CATextLayer lx_getDefaultTextLayerWithFontSize:stepFont.pointSize];
    self.stepLayer.string = [self stepStrWithStepInt:step];
    
    CGRect octaveFrame = CGRectMake(CGRectGetWidth(self.frame)/2.f - size.width/2.f,
                                    CGRectGetHeight(self.frame)/2.f - size.height/2.f,
                                    size.width,
                                    size.height);
    CGFloat stepPositionY = smallFont ? CGRectGetMinY(octaveFrame) + self.font.pointSize * 0.3 : CGRectGetMaxY(octaveFrame) - self.font.pointSize * 0.3;
    
    self.stepLayer.frame = CGRectMake(0,
                                      0,
                                      stepFont.pointSize + 1,
                                      stepFont.pointSize + 1);
    self.stepLayer.position = CGPointMake(CGRectGetMaxX(octaveFrame), stepPositionY);
    self.stepLayer.foregroundColor = self.textColor.CGColor;
    [self.layer addSublayer:self.stepLayer];

}

/**
 *@description 获取琴键keyTag
 **/
+ (NSInteger)lx_keyTagWithOctave:(NSString *)octave step:(NSInteger)step
{
    NSDictionary *dic = @{@"C":@(1),
                          @"D":@(3),
                          @"E":@(5),
                          @"F":@(6),
                          @"G":@(8),
                          @"A":@(10),
                          @"B":@(12)
                          };
    NSNumber *number = dic[octave];
    NSInteger ret = (step - 1)*12+23+number.integerValue;
    return ret;
}
#pragma mark - ********************  Function  ********************
- (NSString *)stepStrWithStepInt:(NSInteger)stepInt
{
    switch (stepInt) {
        case 0:
            return @"2";
            break;
        case 1:
            return @"1";
            break;
        case 2:
            return @"";
            break;
        case 3:
            return @"";
            break;
        case 4:
            return @"1";
            break;
        case 5:
            return @"2";
            break;
        case 6:
            return @"3";
            break;
        case 7:
            return @"4";
            break;
        case 8:
            return @"5";
            break;
            
        default:
            return @"";
            break;
    }
    return @"";
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
