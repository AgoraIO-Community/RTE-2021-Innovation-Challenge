//
//  BJDatePicker.h
//  BJDatePicker
//
//  Created by zbj-mac on 16/4/8.
//  Copyright © 2016年 zbj. All rights reserved.
//
//weakSelf
#define WS(weakSelf)  __weak __typeof(&*self)weakSelf = self;
#define KDeviceWidth [UIScreen mainScreen].bounds.size.width
#define KDeviceHeight [UIScreen mainScreen].bounds.size.height

#import <UIKit/UIKit.h>
//height=226 无遮盖(可替代键盘使用)
typedef void(^dateSelected)(NSString*date);
@interface BJDatePicker : UIView
/**
 *  单例创建
 */
+(BJDatePicker*)shareDatePicker;
/**
 *  实例创建
 */
+(instancetype)datePicker;

/**
 *  选中日期回调
 */
-(void)datePickerDidSelected:(dateSelected)dateSelected;
@end