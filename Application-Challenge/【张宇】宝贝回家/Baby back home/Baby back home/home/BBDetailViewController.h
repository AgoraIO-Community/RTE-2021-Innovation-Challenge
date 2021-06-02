//
//  BBDetailViewController.h
//  Baby back home
//
//  Created by zhangyu on 2021/5/17.
//

#import <UIKit/UIKit.h>
#import "BBMessageModel.h"
NS_ASSUME_NONNULL_BEGIN

@interface BBDetailViewController : UIViewController
@property(assign,nonatomic)NSUInteger numOfPic;
@property(strong,nonatomic)BBMessageModel * model;

@end

NS_ASSUME_NONNULL_END
