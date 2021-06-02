//
//  XXCityPickerView.h
//  CityPicker
//
//  Created by rattanchen on 17/9/9.
//  Copyright © 2017年 rattanchen. All rights reserved.
//

#import <UIKit/UIKit.h>

@class XXCityPickerView;

@protocol XXCityPickerViewDelegate <NSObject>

- (void)XXCityPickerViewLeftAction;
- (void)XXCityPickerViewRightAction:(XXCityPickerView *)picker andCity:(NSString *)city andData:(NSMutableDictionary *)dic;

@end

@interface XXCityPickerView : UIView

@property (weak,nonatomic) id<XXCityPickerViewDelegate>  delegate ;
@property (copy,nonatomic) NSString *city;

- (instancetype)initWithComponents:(NSInteger)components;

@end
