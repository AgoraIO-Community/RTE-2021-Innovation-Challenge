//
//  BBTabBar.m
//  Baby back home
//
//  Created by zhangyu on 2021/5/14.
//

#import "BBTabBar.h"

@implementation BBTabBar

- (instancetype)init
{
    self = [super init];
    if (self) {
        [self setUI];
    }
    return self;
}
#pragma 设置UI布局
- (void)setUI{
    self.centerBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    //设置button大小为图片尺寸
    UIImage *normalImage = [UIImage imageNamed:@"centerAdd"];
    self.centerBtn.frame = CGRectMake(0,0,normalImage.size.width,normalImage.size.height);
    [self.centerBtn setImage:normalImage forState:UIControlStateNormal];
    //去除选择时高亮
    self.centerBtn.adjustsImageWhenHighlighted = NO;
   //设置图片位置居中显示
    self.centerBtn.frame = CGRectMake(([UIScreen mainScreen].bounds.size.width - normalImage.size.width)/2, - normalImage.size.height/2,normalImage.size.width, normalImage.size.height);
    [self addSubview:self.centerBtn];
}
//解决超出superView点击无效问题
- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event{
    UIView *view = [super hitTest:point withEvent:event];
    if (!view){
        //转换坐标
        CGPoint tempPoint = [self.centerBtn convertPoint:point fromView:self];
        //判断点击的点是否在按钮区域内
        if (CGRectContainsPoint(self.centerBtn.bounds, tempPoint)){
            //返回按钮
            return self.centerBtn;
        }
    }
    return view;
}
@end



