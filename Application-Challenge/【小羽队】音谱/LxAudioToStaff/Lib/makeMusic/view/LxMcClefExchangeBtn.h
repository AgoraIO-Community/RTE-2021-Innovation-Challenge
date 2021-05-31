//
//  LxMcClefExchangeBtn.h
//  SmartPiano
//
//  Created by DavinLee on 2018/4/6.
//  Copyright © 2018年 Xytec. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LxMcStaffHeader.h"

@interface LxMcClefExchangeBtn : UIButton
/** 当前按钮状态 **/
@property (assign, nonatomic,readonly) LxMcState btnState;
/** 更新状态（默认状态+1） **/
- (void)lx_updateBtnState;
@end
