//
//  JJSNavViewController.m
//  JJSApp
//
//  Created by silencestorm on 2017/9/28.
//  Copyright © 2017年 iOS1. All rights reserved.
//

#import "JJSNavViewController.h"

@interface JJSNavViewController ()<UINavigationControllerDelegate>
@property(nonatomic,assign)BOOL isPush;

@end

@implementation JJSNavViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.delegate = self;
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void)pushViewController:(UIViewController *)viewController animated:(BOOL)animated{
    if (self.isPush) {
        //如果已经isPush,但尚未didShow,则返回不再二次push
        return;
    }else{
        self.isPush = YES;
    }
    [super pushViewController:viewController animated:animated];
}
- (void)navigationController:(UINavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated{
    //didShow之后,重置isPush状态为NO,以达到下次可以正常push的目的;
    self.isPush = NO;
}

//状态栏白色
//- (UIStatusBarStyle)preferredStatusBarStyle {
//
//
//    return UIStatusBarStyleLightContent;
//}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
