//
//  BJDatePickerView.m
//  BJDatePicker
//
//  Created by zbj-mac on 16/4/8.
//  Copyright © 2016年 zbj. All rights reserved.
//

#import "BJDatePickerView.h"
@interface BJDatePickerView()
@property(nonatomic,strong)BJDatePicker*datePicker;

/**
 *  选中回调
 */
@property(nonatomic,copy)dateSelected dateSelected;
@end
@implementation BJDatePickerView

-(BJDatePicker *)datePicker{
    if (!_datePicker) {
        _datePicker=[BJDatePicker datePicker];
        WS(ws);
        [_datePicker datePickerDidSelected:^(NSString *date) {
         
            !ws.dateSelected?: ws.dateSelected(date);
              [ws hidden];
        }];
    }
    return _datePicker;
}

#pragma mark----创建-----
 static BJDatePickerView* instance;
+(BJDatePickerView*)shareDatePickerView{
    if (!instance) {
        static dispatch_once_t onceToken;
        dispatch_once(&onceToken, ^{
            instance = [[BJDatePickerView alloc] init];
        });
    }
    return instance;
}

+(instancetype)datePickerView{
    return [[self alloc] init];
}
-(instancetype)init{
    if (self=[super init]) {
        [self addSubview:self.datePicker];
  
    }
    return self;
}
-(void)datePickerViewDidSelected:(dateSelected)dateSelected{
    self.dateSelected=dateSelected;
}
-(void)show{
    [[[UIApplication sharedApplication] keyWindow] addSubview:self];
    self.frame=self.superview.bounds;
    [UIView animateWithDuration:0.25f animations:^{
        self.datePicker.frame=CGRectMake(0, KDeviceHeight-226, KDeviceWidth, 226);
    }];
}


-(void)hidden{
    [UIView animateWithDuration:0.25f animations:^{
        self.datePicker.frame=CGRectMake(0, KDeviceHeight, KDeviceWidth, 226);
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
}
-(void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    [self hidden];
}


@end
