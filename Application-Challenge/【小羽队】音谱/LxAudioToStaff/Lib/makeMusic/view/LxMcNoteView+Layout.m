//
//  LxMcNoteView+Layout.m
//  SmartPiano
//
//  Created by DavinLee on 2019/5/7.
//  Copyright © 2019 Ydtec. All rights reserved.
//

#import "LxMcNoteView+Layout.h"
@implementation LxMcNoteView (Layout)
#pragma mark - ************************CallFunction************************
/**
 *@description 获取音符联系顶点位置(相对于曲谱位置)
 *@param lineUpdirection 连线弧度是否朝上
 **/
- (CGPoint)lx_getLinePointWithLineUpDirection:(BOOL)lineUpdirection{
    CGPoint point = CGPointZero;
    if (self.noteType == LxMcNoteWhole) {
        point.x = CGRectGetMinX(self.frame) + CGRectGetMidX(self.layerNoteHead.frame);
        if (lineUpdirection) {
            point.y = CGRectGetMinX(self.frame) + CGRectGetMaxY(self.layerNoteHead.frame);
        }else{
            point.y = CGRectGetMinX(self.frame) + CGRectGetMinY(self.layerNoteHead.frame);
        }
    }else{
        switch (self.headDirection) {
            case LxMcNoteHead_right_down:
            case LxMcNoteHead_left_down:
            LxMcNoteHead_left_up:{
                if (lineUpdirection) {
                    point.x = CGRectGetMinX(self.frame) + CGRectGetMidX(self.layerNoteHead.frame);
                    point.y = CGRectGetMinY(self.frame) + CGRectGetMaxY(self.layerNoteHead.frame);
                }else{
                    point.x = CGRectGetMinX(self.frame) + CGRectGetMidX(self.layerNoteBody.frame);
                    point.y = CGRectGetMinY(self.frame) + CGRectGetMinY(self.layerNoteBody.frame);
                }
            }
                break;
                case LxMcNoteHead_right_up:
                case  LxMcNoteHead_left_up:
            {
                if (lineUpdirection) {
                    point.x = CGRectGetMinX(self.frame) + CGRectGetMidX(self.layerNoteBody.frame);
                    point.y = CGRectGetMinY(self.frame) + CGRectGetMaxY(self.layerNoteBody.frame);
                }else{
                     point.x = CGRectGetMinX(self.frame) + CGRectGetMidX(self.layerNoteHead.frame);
                    point.y = CGRectGetMinY(self.frame) + CGRectGetMinY(self.layerNoteHead.frame);
                }
                
            }
                break;
            default:
                break;
        }
    }
    
    return point;
    
}



@end
