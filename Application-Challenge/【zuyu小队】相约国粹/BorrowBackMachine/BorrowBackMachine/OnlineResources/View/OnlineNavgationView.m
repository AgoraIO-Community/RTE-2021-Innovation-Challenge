//
//  OnlineNavgationView.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/20.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "OnlineNavgationView.h"
#import "ZuyuBackButton.h"
@interface OnlineNavgationView()
{
    UIButton *scanButton;
    UIButton *searchButton;
    UIButton *createCodeButton;
    ZuyuBackButton *backButton;
    UILabel  *titleLabel;

}
@end
@implementation OnlineNavgationView

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
        
        [self addSubview:image];
        
        scanButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
        scanButton.frame = CGRectMake(11, 28, 28, 28);
        
        [scanButton setImage:[UIImage imageNamed:@"scanButton"] forState:UIControlStateNormal];
        
        [scanButton addTarget:self action:@selector(scanClick:) forControlEvents:UIControlEventTouchUpInside];
        
        [self addSubview:scanButton];
        
        searchButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
        searchButton.frame = CGRectMake(50, 26, WIDTH - 100, 32);
        
        [searchButton setImage:[UIImage imageNamed:@"searchButton"] forState:UIControlStateNormal];
        
        [searchButton addTarget:self action:@selector(searchClick:) forControlEvents:UIControlEventTouchUpInside];
        
        [self addSubview:searchButton];
        
        
        createCodeButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
        createCodeButton.frame = CGRectMake(WIDTH - 39, 27, 28, 28);
        
        [createCodeButton setImage:[UIImage imageNamed:@"createCode"] forState:UIControlStateNormal];
        
        [createCodeButton addTarget:self action:@selector(createCode:) forControlEvents:UIControlEventTouchUpInside];
        
        [self addSubview:createCodeButton];
        
        
    }
    
    return self;
}




-(void)scanClick:(UIButton *)button
{
    [self.delegate navScan];
}


-(void)searchClick:(UIButton *)button
{
    [self.delegate navSearch];
}

-(void)createCode:(UIButton *)button
{
    [self.delegate navCreateCode];
}

-(void)popClick:(ZuyuBackButton *)button
{
    [self.delegate navPop];
}


-(void)setScanHidden:(BOOL)scanHidden
{
    scanButton.hidden = scanHidden;
}




-(void)setSearchBtnHidden:(BOOL)searchBtnHidden
{
    searchButton.hidden = searchBtnHidden;
}

-(void)setCreateCodeHidden:(BOOL)createCodeHidden
{
    createCodeButton.hidden = createCodeHidden;
}

-(void)setTitleStr:(NSString *)titleStr
{
    titleLabel.text = titleStr;
}

-(void)setBackBtnHidden:(BOOL)backBtnHidden
{
    if (!backBtnHidden) {
        //有返回按钮
        scanButton.hidden = YES;
        createCodeButton.hidden = YES;
        searchButton.hidden = YES;
        backButton = [[ZuyuBackButton alloc] initWithFrame:CGRectMake(0, 20, 80, 44)];
        
        [backButton addTarget:self action:@selector(popClick:) forControlEvents:UIControlEventTouchUpInside];
        
        [self addSubview:backButton];
        
        titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH*0.3, 20, WIDTH*0.4, 44)];
        
        titleLabel.textAlignment = NSTextAlignmentCenter;
        
        titleLabel.font = [UIFont systemFontOfSize:19 weight:1];
        
        [self addSubview:titleLabel];
  
    }else{
        backButton.hidden = backBtnHidden;
//        titleLabel.hidden = backBtnHidden;
    }
}

-(void)setTitleLableHidden:(BOOL)titleLableHidden
{
    if (titleLableHidden) {
        titleLabel.hidden = titleLableHidden;
    }else{
        if (titleLabel == nil) {
            titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH*0.3, 20, WIDTH*0.4, 44)];
            titleLabel.textAlignment = NSTextAlignmentCenter;
            titleLabel.font = [UIFont systemFontOfSize:19 weight:1];
            
            [self addSubview:titleLabel];
        }else{
            titleLabel.hidden = titleLableHidden;
        }
    }
}

-(void)setRightItmeImage:(NSString *)rightItmeImage
{
    [createCodeButton setImage:[UIImage imageNamed:rightItmeImage] forState:UIControlStateNormal];
}

@end
