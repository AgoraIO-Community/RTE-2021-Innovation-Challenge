//
//  LxMcClefExchangeBtn.m
//  SmartPiano
//
//  Created by DavinLee on 2018/4/6.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import "LxMcClefExchangeBtn.h"
#import "UIImage+Default.h"
@implementation LxMcClefExchangeBtn

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
#pragma mark - Call_Function
- (void)lx_updateBtnState
{
    _btnState = (self.btnState + 1) % 3;
    switch (self.btnState) {
        case LxMcMcBothClef:
        {
            [self setImage:[UIImage lx_imageFromBundleWithName:@"write_gclef the bass clef_switching_ruding@2x"] forState:UIControlStateNormal];
        }
            break;
            case LxMcMcHighClef:
        {
            [self setImage:[UIImage lx_imageFromBundleWithName:@"write_G clef_switching_ruding@2x"] forState:UIControlStateNormal];
        }
            break;
            case LxMcMcLowClef:
        {
            [self setImage:[UIImage lx_imageFromBundleWithName:@"write_the bass clef_switching_ruding@2x"] forState:UIControlStateNormal];
        }
            break;
        default:
            break;
    }
}


@end
