//
//  BJDatePickerView.h
//  BJDatePicker
//
//  Created by zbj-mac on 16/4/8.
//  Copyright © 2016年 zbj. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BJDatePicker.h"
//有遮盖，添加在window
@interface BJDatePickerView : UIView
/**
 *  单例创建
 */
+(BJDatePickerView*)shareDatePickerView;
/**
 *  实例创建
 */
+(instancetype)datePickerView;
/**
 *  选中日期回调
 *
 *  @param dateSelected 回调
 */
-(void)datePickerViewDidSelected:(dateSelected)dateSelected;
/**
 *  展示
 */
-(void)show;
/**
 *  移除
 */
-(void)hidden;



@end
