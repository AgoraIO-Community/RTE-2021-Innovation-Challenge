//
//  UIView+flashAnimal.h
//  SmartPiano
//
//  Created by JM(jieson) on 16/4/19.
//  Copyright © 2016年 XiYun. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIView (flashAnimal)
- (void)flashInDuarition:(NSTimeInterval) aTime;
- (void)flashInDuarition:(NSTimeInterval)aTime withOnceDuarion:(NSTimeInterval) aOnceDuarion;
- (void)removeFlashError;
//for 萤火虫
- (void)yingHuoChongflashInDuarition:(NSTimeInterval)aTime withOnceDuarion:(NSTimeInterval) aOnceDuarion;
@end
