//
//  SKNode+StaffHelp.m
//  SmartPiano
//
//  Created by DavinLee on 2020/6/22.
//  Copyright © 2020 Ydtec. All rights reserved.
//

#import "SKNode+StaffHelp.h"
#import "UIColor+Default.h"

@implementation SKNode (StaffHelp)

/** Lx description   高音谱布局添加midi对应点击块 ，自下而上  **/
- (void)lx_setupWithStaffSpace:(CGFloat)space
      nodeHeight:(CGFloat)nodeHeight
       nodeWidth:(CGFloat)nodeWidth
originStartPoint:(CGPoint)startPoint
        fromMidi:(NSInteger)fromMidi
          toMidi:(NSInteger)toMidi{
    
    NSMutableArray *allWhiteMids = [[NSMutableArray alloc] init];
    [allWhiteMids addObject:@(21)];
    [allWhiteMids addObject:@(23)];
    for (int i  = 0; i < 7; i ++) {
        for (int j = 0; j < 7; j ++) {
            NSInteger midi = 24 + (i * 12);
            if (j < 3) {
                midi += j * 2;
            }else if ( j == 3) {
                midi += 5;
            }else{
                midi += j * 2 - 1;
            }
            
            [allWhiteMids addObject:@(midi)];
        }
    }
    [allWhiteMids addObject:@(108)];
    
    NSInteger midiIndex = [allWhiteMids indexOfObject:@(fromMidi)];
    NSInteger toMidiIndex = [allWhiteMids indexOfObject:@(toMidi)];
    if (toMidiIndex < 1) {
        toMidiIndex = allWhiteMids.count - 1;
    }
    for (int i = (int)midiIndex; i <= toMidiIndex; i ++) {
        NSInteger midi = [allWhiteMids[i] integerValue];
        SKSpriteNode *midiNode = [SKSpriteNode node];
        midiNode.size = CGSizeMake(nodeWidth, nodeHeight);
        midiNode.position = CGPointMake(startPoint.x, startPoint.y + space * (i - midiIndex) / 2.f);
        midiNode.color = [UIColor clearColor];
        [self addChild:midiNode];
        midiNode.name = [NSString stringWithFormat:@"%ld",(long)midi];
        midiNode.alpha = 0.4;
    }
    
}



@end
