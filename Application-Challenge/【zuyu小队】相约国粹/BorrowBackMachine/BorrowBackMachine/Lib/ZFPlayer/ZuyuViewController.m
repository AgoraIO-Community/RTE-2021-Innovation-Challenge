//
//  ZuyuViewController.m
//  Player
//
//  Created by zuyu on 17/3/14.
//  Copyright © 2017年 任子丰. All rights reserved.
//

#import "ZuyuViewController.h"

@interface ZuyuViewController ()

@end

@implementation ZuyuViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor  redColor];
    
    UILabel *lable  = [[UILabel alloc] initWithFrame:self.view.frame];
    
    lable.text = @"three";
    
    lable.font = [UIFont systemFontOfSize:32];
    
    [self.view addSubview:lable];
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    
    button.frame = CGRectMake(100, 100, 200, 100);
    
    button.backgroundColor = [UIColor blackColor];
    
    [button addTarget:self action:@selector(click:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:button];
    
    // Do any additional setup after loading the view.
}

-(void)click:(UIButton *)button
{
//    Zuyu2ViewController *vc = [[Zuyu2ViewController alloc] init];
//    
//    [self.navigationController pushViewController:vc animated:YES];

}




- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
