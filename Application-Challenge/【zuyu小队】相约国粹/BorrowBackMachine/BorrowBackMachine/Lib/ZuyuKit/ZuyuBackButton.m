//
//  ZuyuBackButton.m
//  SiyecaoTercher
//
//  Created by zuyu on 2018/4/26.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "ZuyuBackButton.h"

@implementation ZuyuBackButton

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    
    if (self) {
        
        UIImageView *image = [[UIImageView alloc] initWithFrame:CGRectMake(8, 9, 24, 24)];
        
        image.image = [UIImage imageNamed:@"jiehuanBack"];
        
        [self addSubview:image];
        
    }
    
    return self;
    
}


@end
