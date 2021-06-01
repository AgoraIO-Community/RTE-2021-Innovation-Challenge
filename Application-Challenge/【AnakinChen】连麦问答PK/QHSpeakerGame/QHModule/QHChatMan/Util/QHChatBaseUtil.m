//
//  QHChatBaseUtil.m
//  QHChatDemo
//
//  Created by Anakin chen on 2018/12/27.
//  Copyright © 2018 Chen Network Technology. All rights reserved.
//

#import "QHChatBaseUtil.h"

@implementation QHChatBaseUtil

+ (void)addCellDefualAttributes:(NSMutableAttributedString *)attr lineSpacing:(CGFloat)lineSpacing fontSize:(CGFloat)fontSize {
    if (attr == nil) {
        return;
    }
    // [NSAttributedString 的使用 - 简书](https://www.jianshu.com/p/3f85f91d1208)
    NSMutableParagraphStyle *paragraphStyle = [NSMutableParagraphStyle new];
    paragraphStyle.lineSpacing = lineSpacing;
    paragraphStyle.lineBreakMode = NSLineBreakByCharWrapping;
    [attr addAttributes:@{NSFontAttributeName: [UIFont systemFontOfSize:fontSize], NSParagraphStyleAttributeName: paragraphStyle} range:NSMakeRange(0, attr.length)];
}

+ (NSAttributedString *)toHTML:(NSString *)content {
    if (content == nil || content.length <= 0) {
        return nil;
    }
    NSError *error = nil;
    NSAttributedString *attributedString = [[NSAttributedString alloc] initWithData:[content dataUsingEncoding:NSUnicodeStringEncoding] options:@{NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType} documentAttributes:nil error:&error];
    if (error != nil) {
        return nil;
    }
    return attributedString;
}

+ (NSAttributedString *)toContent:(NSString *)content color:(UIColor *)color {
    if (content == nil || content.length <= 0) {
        return nil;
    }
    NSAttributedString *contentAttr = [[NSAttributedString alloc] initWithString:content attributes:@{NSForegroundColorAttributeName:color}];
    return contentAttr;
}

+ (NSAttributedString *)toImage:(UIImage *)image size:(CGSize)size {
    return [QHChatBaseUtil toImage:image size:size addContentBlock:nil];
}

// [Null passed to a callee that requires a non-nul... - 简书](https://www.jianshu.com/p/3d030d367a34)
+ (NSAttributedString *)toImage:(UIImage *)image size:(CGSize)size addContentBlock:(nullable AddContentBlock)block {
    UIImageView *imageV = [[UIImageView alloc] initWithImage:image];
    imageV.frame = (CGRect){CGPointZero, size};
    if (block != nil) {
        block(imageV);
    }
    NSAttributedString *imageAttr = nil;
    @try {
        NSTextAttachment *textAttachment = [[NSTextAttachment alloc] init];
        textAttachment.image = [self convertViewToImage:imageV];;
        textAttachment.bounds = CGRectMake(0, -2, textAttachment.image.size.width, textAttachment.image.size.height);
        imageAttr = [NSAttributedString attributedStringWithAttachment:textAttachment];
    } @catch (NSException *exception) {
        imageAttr = [[NSAttributedString alloc] initWithString:@""];
    } @finally {
        
    }
    return imageAttr;
}

+ (UIImage *)convertViewToImage:(UIView *)view {
    UIGraphicsBeginImageContextWithOptions(view.bounds.size, NO, [UIScreen mainScreen].scale);
    //    [view drawRect:CGRectMake(0, 50, view.frame.size.width, view.frame.size.height)];
    [view.layer renderInContext:UIGraphicsGetCurrentContext()];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    view.layer.contents = nil;
    
    return image;
}

+ (CGSize)calculateString:(NSString *)string size:(CGSize)size font:(UIFont *)font {
    CGSize expectedLabelSize = CGSizeZero;
    
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7) {
        NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
        paragraphStyle.lineBreakMode = NSLineBreakByWordWrapping;
        NSDictionary *attributes = @{NSFontAttributeName:font, NSParagraphStyleAttributeName:paragraphStyle.copy};
        
        expectedLabelSize = [string boundingRectWithSize:size options:NSStringDrawingUsesLineFragmentOrigin attributes:attributes context:nil].size;
    }
    else {
        //        expectedLabelSize = [string sizeWithFont:font
        //                             constrainedToSize:size
        //                                 lineBreakMode:NSLineBreakByWordWrapping];
    }
    
    return CGSizeMake(ceil(expectedLabelSize.width), ceil(expectedLabelSize.height));
}

@end
