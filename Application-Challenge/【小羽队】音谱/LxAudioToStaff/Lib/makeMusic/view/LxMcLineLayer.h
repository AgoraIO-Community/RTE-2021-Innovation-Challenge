//
//  LxMcLineLayer.h
//  SmartPiano
//
//  Created by DavinLee on 2019/4/30.
//  Copyright © 2019 Ydtec. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>
@class LxMcNoteView;
NS_ASSUME_NONNULL_BEGIN

@interface LxMcLineLayer : CAShapeLayer
/**   持有当前连线中的所有音符    **/
@property (weak, nonatomic) NSMutableArray <LxMcNoteView *>*lineNotes;

/**   获取默认layer    **/
+ (LxMcLineLayer *)lx_defaultLineLayer;

@end

NS_ASSUME_NONNULL_END
