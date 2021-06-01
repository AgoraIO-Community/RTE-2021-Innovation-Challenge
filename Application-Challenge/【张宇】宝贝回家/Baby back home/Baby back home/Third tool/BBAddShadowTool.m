//
//  BBAddShadowTool.m
//  Baby back home
//
//  Created by zhangyu on 2021/5/28.
//

#import "BBAddShadowTool.h"

@implementation BBAddShadowTool

+ (void)addShadowToView:(UIView *)theView {
    // 阴影颜色
    theView.layer.shadowColor = UIColor.blackColor.CGColor;
    // 阴影偏移，默认(0, -3)
    theView.layer.shadowOffset = CGSizeMake(0,0);
    // 阴影透明度，默认0
    theView.layer.shadowOpacity = 0.3;
    // 阴影半径，默认3
    theView.layer.shadowRadius = 3;
    
}

+ (void)addShadowToViewOnlyBottom:(UIView *)theView {
    
    theView.layer.shadowColor = UIColor.blackColor.CGColor;
    theView.layer.shadowOffset = CGSizeMake(0,0);
    theView.layer.shadowOpacity = 0.3;
    theView.layer.shadowRadius = 4;
    // 单边阴影 顶边
    float shadowPathWidth = theView.layer.shadowRadius;
    CGRect shadowRect = CGRectMake(0, theView.height, theView.bounds.size.width, shadowPathWidth);
    UIBezierPath *path = [UIBezierPath bezierPathWithRect:shadowRect];
    theView.layer.shadowPath = path.CGPath;
    
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
