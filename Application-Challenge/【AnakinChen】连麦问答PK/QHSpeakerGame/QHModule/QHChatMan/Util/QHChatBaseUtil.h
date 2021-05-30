//
//  QHChatBaseUtil.h
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/27.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void (^AddContentBlock)(UIImageView *imageV);

@interface QHChatBaseUtil : NSObject

+ (void)addCellDefualAttributes:(NSMutableAttributedString *)attr lineSpacing:(CGFloat)lineSpacing fontSize:(CGFloat)fontSize;

+ (NSAttributedString *)toHTML:(NSString *)content;

+ (NSAttributedString *)toContent:(NSString *)content color:(UIColor *)color;

+ (NSAttributedString *)toImage:(UIImage *)image size:(CGSize)size;

+ (NSAttributedString *)toImage:(UIImage *)image size:(CGSize)size addContentBlock:(nullable AddContentBlock)block;

+ (CGSize)calculateString:(NSString *)string size:(CGSize)size font:(UIFont *)font;

@end

NS_ASSUME_NONNULL_END
