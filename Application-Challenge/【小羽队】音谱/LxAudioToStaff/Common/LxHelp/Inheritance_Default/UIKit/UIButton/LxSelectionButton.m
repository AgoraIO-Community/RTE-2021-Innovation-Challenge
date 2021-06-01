//
//  LxSelectionButton.m
//  SmartPiano
//
//  Created by DavinLee on 2018/1/29.
//  Copyright © 2018年 XiYun. All rights reserved.
//

#import "LxSelectionButton.h"
#import "UIImage+Default.h"

@implementation LxSelectionButton

/**
 *@description 获取基础按钮
 *@param imageName 默认图片名称（暂只使用@2x图)
 *@return btn
 **/
+ (LxSelectionButton *)lx_defaultBtnWithImageName:(NSString *)imageName
{
    LxSelectionButton *btn = [LxSelectionButton buttonWithType:UIButtonTypeCustom];
    UIImage *image = [UIImage lx_imageFromBundleWithName:imageName];
    btn.defaultImageName = imageName;
    CGRect rect = CGRectMake(0, 0, image.size.width, image.size.height);
    [btn setImage:image forState:UIControlStateNormal];
    btn.frame = rect;
    return btn;
}

#pragma mark - OverWrite
- (void)setSelected:(BOOL)selected
{
    [super setSelected:selected];
    if (_defaultImageName) {
        NSString *imageName = nil;
        if (selected) {
            imageName = [@"selected_" stringByAppendingString:_defaultImageName];
        }else
        {
            imageName = _defaultImageName;
        }
        UIImage *image = [UIImage lx_imageFromBundleWithName:imageName];
        if (image)
            [self setImage:image forState:UIControlStateNormal];
    }
}




/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
