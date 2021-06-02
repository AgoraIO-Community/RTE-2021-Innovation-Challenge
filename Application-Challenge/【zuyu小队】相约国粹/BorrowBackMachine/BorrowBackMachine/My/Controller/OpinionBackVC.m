//
//  OpinionBackVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/28.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "OpinionBackVC.h"
#import "zuyu.h"
#import "UITextView+ZWPlaceHolder.h"
#import "UITextView+ZWLimitCounter.h"
@interface OpinionBackVC ()<NavgationViewDelegate>
{
    UITextView *_textView;
}
@end

@implementation OpinionBackVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self createNavgation];
    [self createImageView];
    [self createTextView];
    // Do any additional setup after loading the view.
}

#pragma mark - navgation
-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.scanHidden = YES;
    view.searchBtnHidden = YES;
    view.createCodeHidden = YES;
    view.titleStr = @"意见反馈";
    [self.view addSubview:view];
    self.view.backgroundColor = RGBA(246, 245, 242, 1);
    
}

-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}


#pragma mark - BGView

-(void)createImageView
{
    UIImageView *bgImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74)];
    bgImage.image = [UIImage imageNamed:@"OpinionBackBGI"];
    
    [self.view addSubview:bgImage];
    
    UILabel *lable = [[UILabel alloc] initWithFrame:CGRectMake(50, 50, WIDTH - 20, 40)];
    
    lable.text = @"您的意见对我们非常重要";
    
    lable.textColor = [UIColor lightGrayColor];
    
    [bgImage addSubview:lable];
}

#pragma mark - createTextView

-(void)createTextView
{
    _textView = [[UITextView alloc] initWithFrame:CGRectMake(50, 180, WIDTH - 100, WIDTH * 0.42)];
    
    _textView.font = [UIFont systemFontOfSize:16];
    
    _textView.zw_placeHolder = @"请输入您想要反馈的内容(300字内)";
    _textView.zw_limitCount = 300;
    [self.view addSubview:_textView];
    
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    
    button.frame = CGRectMake(50, CGRectGetMaxY(_textView.frame) + 25, WIDTH - 100 , 50);
    
    [button setTitle:@"完成" forState: UIControlStateNormal];
    
    [button setTitleColor:COLOR forState:UIControlStateNormal];
    
    [button.layer setBorderColor:COLOR.CGColor];
    
    [button.layer setBorderWidth:1.0];
    
    [button addTarget:self action:@selector(feedBack:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:button];
    
}

-(void)feedBack:(UIButton *)button
{
    if (_textView.text.length) {
        MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        
        hud.label.text = NSLocalizedString(@"反馈中...", @"HUD loading title");
        
        dispatch_async(dispatch_get_global_queue(QOS_CLASS_USER_INITIATED, 0), ^{
            
            float i = arc4random()%2 + 0.5;
            
            sleep(i);
            dispatch_async(dispatch_get_main_queue(), ^{
                hud.mode = MBProgressHUDModeText;
                hud.label.text = NSLocalizedString(@"反馈成功", @"HUD message title");
                
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    [hud hideAnimated:YES ];
                    [self.navigationController popViewControllerAnimated:YES];
                    
                });
            });
        });
    } else{
        
        MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        hud.mode = MBProgressHUDModeText;
        hud.label.text = NSLocalizedString(@"请输入反馈内容", @"HUD message title");
        [hud hideAnimated:YES afterDelay:1];
    }
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark - nav 处理.
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:NO];
}


- (void) viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:NO];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
