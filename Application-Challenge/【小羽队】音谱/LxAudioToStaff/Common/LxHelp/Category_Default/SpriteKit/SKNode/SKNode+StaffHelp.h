//
//  SKNode+StaffHelp.h
//  SmartPiano
//
//  Created by DavinLee on 2020/6/22.
//  Copyright © 2020 Ydtec. All rights reserved.
//

#import <SpriteKit/SpriteKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface SKNode (StaffHelp)

/** LxInterface <#description#>
 @param space 五线谱间隔
 @param nodeHeight 设置midi块高度
 @param nodeWidth 设置midi块宽度
  @param startPoint 起始布局位置，统一自下而上，横向位置为父视图中间点，即宽度一半
 @param fromMidi 只能传入白键midi值，高音谱传入最小midi，低音谱传入最大midi
 @param toMidi 与fromMidi相反
 */
- (void)lx_setupWithStaffSpace:(CGFloat)space
                    nodeHeight:(CGFloat)nodeHeight
                     nodeWidth:(CGFloat)nodeWidth
              originStartPoint:(CGPoint)startPoint
                      fromMidi:(NSInteger)fromMidi
                        toMidi:(NSInteger)toMidi;




@end

NS_ASSUME_NONNULL_END
