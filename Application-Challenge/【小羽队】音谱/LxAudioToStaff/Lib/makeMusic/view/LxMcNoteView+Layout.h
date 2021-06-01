//
//  LxMcNoteView+Layout.h
//  SmartPiano
//
//  Created by DavinLee on 2019/5/7.
//  Copyright © 2019 Ydtec. All rights reserved.
//

#import "LxMcNoteView.h"

NS_ASSUME_NONNULL_BEGIN

@interface LxMcNoteView (Layout)
/**
 *@description 获取音符联系顶点位置(相对于曲谱位置)
 *@param lineUpdirection 连线弧度是否朝上
 **/
- (CGPoint)lx_getLinePointWithLineUpDirection:(BOOL)lineUpdirection;

@end

NS_ASSUME_NONNULL_END
