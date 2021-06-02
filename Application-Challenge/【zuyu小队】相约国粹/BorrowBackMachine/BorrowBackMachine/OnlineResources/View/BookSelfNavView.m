//
//  BookSelfNavView.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/1.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "BookSelfNavView.h"
#import "ZuyuBackButton.h"
#import "UISearchBar+FMAdd.h"


@interface BookSelfNavView()<UISearchBarDelegate>
{
    ZuyuBackButton *backButton;

}
@property (strong, nonatomic) UISearchBar *customSearchBar;

@end
@implementation BookSelfNavView


-(instancetype)init
{
    self = [super init];
    
    if (self) {
        self.frame = CGRectMake(0, 0, WIDTH, 74);
        
        UIImageView *image = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 74)];
        
        image.image = [UIImage imageNamed:@"header-bg"];
        
        [self addSubview:image];
        
        backButton = [[ZuyuBackButton alloc] initWithFrame:CGRectMake(0, 20, 80, 44)];
        
        [backButton addTarget:self action:@selector(popClick:) forControlEvents:UIControlEventTouchUpInside];
        
        [self addSubview:backButton];
        
        
        self.customSearchBar = [[UISearchBar alloc] initWithFrame:CGRectMake(35, 24, WIDTH - 70, 36)];
//        self.customSearchBar.delegate = self;
        self.customSearchBar.placeholder = @"请输入查询信息";
        
        self.customSearchBar.delegate = self;
        //1. 设置背景颜色
        //设置背景图是为了去掉上下黑线
        self.customSearchBar.backgroundImage = [[UIImage alloc] init];
        // 设置SearchBar的颜色主题为白色
//        self.customSearchBar.barTintColor = [UIColor whiteColor];
        
        //2. 设置圆角和边框颜色
        UITextField *searchField = [self.customSearchBar valueForKey:@"searchField"];
        if (searchField) {
            [searchField setBackgroundColor:[UIColor whiteColor]];
            searchField.layer.cornerRadius = 10.0f;
            searchField.layer.borderColor = [UIColor lightGrayColor].CGColor;
            searchField.layer.borderWidth = 1;
            searchField.layer.masksToBounds = YES;
        }
        
        //3. 设置按钮文字和颜色
        [self.customSearchBar fm_setCancelButtonTitle:@"取消"];
        self.customSearchBar.tintColor = [UIColor colorWithRed:86/255.0 green:179/255.0 blue:11/255.0 alpha:1];
        //设置取消按钮字体
        [self.customSearchBar fm_setCancelButtonFont:[UIFont systemFontOfSize:22]];
        //修正光标颜色
        [searchField setTintColor:[UIColor blackColor]];
        
        //4. 设置输入框文字颜色和字体
        [self.customSearchBar fm_setTextColor:[UIColor blackColor]];
        [self.customSearchBar fm_setTextFont:[UIFont systemFontOfSize:14]];
        
        //5. 设置搜索Icon
        [self.customSearchBar setImage:[UIImage imageNamed:@"nopicture"]
                      forSearchBarIcon:UISearchBarIconSearch
                                 state:UIControlStateNormal];
        
        [self addSubview:self.customSearchBar];
        
        UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
        
        button.frame = CGRectMake(WIDTH - 47, 17,  50, 50);
        
        [button setImage:[UIImage imageNamed:@"searchForNav"] forState:UIControlStateNormal];
        
        [button addTarget:self action:@selector(searchClick:) forControlEvents:UIControlEventTouchUpInside];
        
        [self addSubview:button];
    }
    
    return self;
}

-(void)searchClick:(UIButton *)button
{
    [_customSearchBar resignFirstResponder];
    [self.delegate BookSelfNavSearch:self.customSearchBar.text];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar{

    [searchBar resignFirstResponder];
    [self.delegate BookSelfNavSearch:searchBar.text];
}

-(void)popClick:(ZuyuBackButton *)button
{
    [self.delegate BookSelfNavViewPop];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
