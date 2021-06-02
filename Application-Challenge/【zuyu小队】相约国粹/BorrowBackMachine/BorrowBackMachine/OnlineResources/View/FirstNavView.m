//
//  FirstNavView.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/2.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "FirstNavView.h"
@interface FirstNavView()
{
    UIButton *_scanButton;
    UIButton *searchButton;
    UIButton *createCodeButton;
}
@end
@implementation FirstNavView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
-(instancetype)init
{
    self = [super init];
    
    if (self) {
        
        self.frame = CGRectMake(0, 0, WIDTH, 74);
    
        UIImageView *image = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 74)];
        
        image.image = [UIImage imageNamed:@"header-bg"];
        
        image.userInteractionEnabled = YES;
        
        [self addSubview:image];
        
        UILabel *lable = [[UILabel alloc] initWithFrame:CGRectMake(10, 20, WIDTH - 33, 54)];
        lable.text = @"经 典 国 学 书 院";
        lable.font = [UIFont systemFontOfSize:16 weight:1.1];
        [self addSubview:lable];
        
//        UIImageView *logoImage = [[UIImageView alloc] initWithFrame:CGRectMake(10, 24, WIDTH * 0.6 - 10, 40)];
//
//        logoImage.image = [UIImage imageNamed:@"img_home_index_page_title"];
//
//        [self addSubview:logoImage];
        
//        _scanButton = [UIButton buttonWithType:UIButtonTypeCustom];
//
//        _scanButton.frame = CGRectMake(WIDTH * 0.8, 28, 28, 28);
//
//        _scanButton.userInteractionEnabled = YES;
//
//        [_scanButton setImage:[UIImage imageNamed:@"scanButton"] forState:UIControlStateNormal];
//
//        [_scanButton addTarget:self action:@selector(scanClick:) forControlEvents:UIControlEventTouchUpInside];
//
//        [self addSubview:_scanButton];
        
        searchButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
        searchButton.frame = CGRectMake(WIDTH * 0.9, 28, 28, 28);
        
        [searchButton setImage:[UIImage imageNamed:@"searchForFrist"] forState:UIControlStateNormal];
        
        [searchButton addTarget:self action:@selector(searchClick:) forControlEvents:UIControlEventTouchUpInside];
        
        [self addSubview:searchButton];
        
        
//        createCodeButton = [UIButton buttonWithType:UIButtonTypeCustom];
//
//        createCodeButton.frame = CGRectMake(WIDTH * 0.9, 28, 28, 28);
//
//        [createCodeButton setImage:[UIImage imageNamed:@"createCode"] forState:UIControlStateNormal];
//
//        [createCodeButton addTarget:self action:@selector(createCode:) forControlEvents:UIControlEventTouchUpInside];
//
//        [self addSubview:createCodeButton];
    }
    return self;
}

-(void)scanClick:(UIButton *)button
{
    [self.delegate firstForScan];
}

-(void)searchClick:(UIButton *)button
{
    [self.delegate firstForSearch];
}

-(void)createCode:(UIButton *)button
{
    
    if ([ISLOGIN integerValue]) {
        [self.delegate firstForCode];
    }else{
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nil message:@"请先登录" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
    }
    
}
@end
