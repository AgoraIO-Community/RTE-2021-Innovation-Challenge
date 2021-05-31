//
//  LxAlertController.m
//  svgtest2
//
//  Created by 李翔 on 2017/6/20.
//  Copyright © 2017年 ydkj. All rights reserved.
//

#import "LxAlertController.h"
#import "LxAlertAction.h"

@interface LxAlertController ()

@end

@implementation LxAlertController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Function
+ (UIAlertActionStyle)actionStyleWithArray:(NSArray *)array index:(NSInteger)index
{
    if (array.count > index) {
        return [array[index] integerValue];
    }else
    {
        return UIAlertActionStyleDefault;
    }
}

#pragma mark - CallFunction
+ (LxAlertController *)lx_alertShowWithTitle:(NSString *)title
                                     message:(NSString *)message
                                actionTitles:(NSArray <NSString *>*)actionTitles
                                actionStyles:(NSArray *)actionStyles
                             clickIndexBlock:(void(^)(NSInteger clickIndex))block
{
    LxAlertController *alertController = [LxAlertController alertControllerWithTitle:title
                                                                             message:message
                                                                      preferredStyle:UIAlertControllerStyleAlert];
    for (int i = 0; i < actionTitles.count; i ++) {
        NSString *title = actionTitles[i];
        
        UIAlertActionStyle style = [LxAlertController actionStyleWithArray:actionStyles index:i];
        LxAlertAction *action = [LxAlertAction lx_actionWithTitle:title
                                                            style:style
                                                      actionIndex:i
                                                            block:^(LxAlertAction *clickAction) {
                                                                if (block) {
                                                                    block(clickAction.click_index);
                                                                }
                                                                [alertController dismissViewControllerAnimated:YES completion:nil];
                                                            }];
        [alertController addAction:action];
    }

    return alertController;
}

+ (LxAlertController *)lx_alertShowWithTitle:(NSString *)title
                                     message:(NSString *)message
                          textfiledHolderStr:(NSString *)textFiledHolder
                                actionTitles:(NSArray <NSString *>*)actionTitles
                                actionStyles:(NSArray *)actionStyles
                             clickIndexBlock:(void(^)(NSInteger clickIndex,NSString *text))block
{
    LxAlertController *alertController = [LxAlertController alertControllerWithTitle:title
                                                                             message:message
                                                                      preferredStyle:UIAlertControllerStyleAlert];
    for (int i = 0; i < actionTitles.count; i ++) {
        NSString *title = actionTitles[i];
        
        UIAlertActionStyle style = [LxAlertController actionStyleWithArray:actionStyles index:i];
        LxAlertAction *action = [LxAlertAction lx_actionWithTitle:title
                                                            style:style
                                                      actionIndex:i
                                                            block:^(LxAlertAction *clickAction) {
                                                                if (block) {
                                                                    UITextField *filed = [alertController.textFields firstObject];
                                                                    block(clickAction.click_index,filed.text);
                                                                }
                                                                [alertController dismissViewControllerAnimated:YES completion:nil];
                                                            }];
        
        [alertController addAction:action];
    }
    [alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
        textField.placeholder = textFiledHolder;
    }];
    return alertController;

}

- (void)dealloc
{
    debugLog(@"%s",__func__);
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
