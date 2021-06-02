//
//  ZuyuPlaceholderImage.h
//  ZuyuWebImage
//
//  Created by zuyu on 2018/10/29.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface ZuyuPlaceholderImage : NSObject


+(void)loadZuyuPlaceholder;

+(UIImage *)returnPlaceholder:(NSInteger )type;
@end
