//
//  XXCityPickerView.m
//  CityPicker
//
//  Created by rattanchen on 17/9/9.
//  Copyright © 2017年 rattanchen. All rights reserved.
//

#import "XXCityPickerView.h"
#import <sqlite3.h>

// 屏幕宽
#define kScreenPicker_W [UIScreen mainScreen].bounds.size.width
// 屏幕高
#define kScreenPicker_H [UIScreen mainScreen].bounds.size.height

@interface XXCityPickerView()<UIPickerViewDelegate,UIPickerViewDataSource>
{
    // 操作数据库的对象
    sqlite3 *db;
}

@property (assign,nonatomic) NSInteger components;
@property (copy,nonatomic) NSString *address;
@property (strong, nonatomic) NSMutableDictionary *dicData ;// 省市区


// 地址=============================
// 地址 picker
@property (nonatomic,strong) UIPickerView *addressPicker;

// 地址数组
@property (nonatomic,strong) NSMutableArray *arr_address;
// 省
@property (nonatomic, strong) NSMutableArray *mArrProvince;
// 市
@property (nonatomic, strong) NSMutableArray *mArrCity;
// 区
@property (nonatomic, strong) NSMutableArray *mArrArea;

@end

@implementation XXCityPickerView

- (instancetype)initWithComponents:(NSInteger)components
{
    if (self = [super init])
    {
        self.frame = CGRectMake(-1, kScreenPicker_H-263, kScreenPicker_W+2, 200);
        self.backgroundColor = KBGreenColor2;
        self.layer.borderWidth = 1;
        self.layer.borderColor = [UIColor colorWithWhite:0.847 alpha:1.000].CGColor;
        
        self.components = components;
        [self creacteBtn];// 创建按钮
        [self getAddressData];// 获取地址信息
        
    }
    return self;
}
// 创建按钮
- (void)creacteBtn
{
    [self addSubview: [self getButton:CGRectMake(kScreenPicker_W-60, 0, 50, 40) title:@"完成" imageArr:nil backgroundColor:nil Target:@selector(rightBtnAction)]];
    [self addSubview: [self getButton:CGRectMake(10, 0, 50, 40) title:@"取消" imageArr:nil backgroundColor:nil Target:@selector(leftBtnAction)]];
}
- (void)leftBtnAction
{
    [self.delegate XXCityPickerViewLeftAction];
}
- (void)rightBtnAction
{
    [self.delegate XXCityPickerViewRightAction:self andCity:self.address andData:self.dicData];
}
#pragma mark  获取地址数据库数据的方法
- (void)getAddressData
{
    NSString *path = [[NSBundle mainBundle] pathForResource:@"region" ofType:@"sqlite"];
    if (path != nil)
    {
        sqlite3_open([path UTF8String], &db);
        NSString *sql = @"select *from 'region_conf';";
        sqlite3_stmt *statu_stmt;
        sqlite3_prepare_v2(db, [sql UTF8String], -1, &statu_stmt, nil);
        while (sqlite3_step(statu_stmt) == SQLITE_ROW)
        {
            int id_int = sqlite3_column_int(statu_stmt, 0);
            char *name_text = (char *)sqlite3_column_text(statu_stmt, 1);
            int pid_int = sqlite3_column_int(statu_stmt, 2);
            
            NSString *add_id = [NSString stringWithFormat:@"%d", id_int];
            NSString *add_name = [NSString stringWithUTF8String:name_text];
            NSString *add_pid = [NSString stringWithFormat:@"%d", pid_int];
            
            NSDictionary *dic = @{@"id":add_id, @"name":add_name, @"pid":add_pid};
            [self.arr_address addObject:dic];
        }
    }
    
    // 初始化数据为北京
    // 省
    for (int i = 0; i < self.arr_address.count; i++)
    {
        if ([self.arr_address[i][@"pid"] intValue] == 1)
        {
            [self.mArrProvince addObject:self.arr_address[i]];
        }
    }
    // 北京市
    for (int i = 0; i < self.arr_address.count; i++)
    {
        if ([self.arr_address[i][@"pid"] intValue] == 2)
        {
            [self.mArrCity addObject:self.arr_address[i]];
        }
    }
    // 北京  区
    for (int i = 0; i < self.arr_address.count; i++)
    {
        if ([self.arr_address[i][@"pid"] intValue] == 52)
        {
            [self.mArrArea addObject:self.arr_address[i]];
        }
    }
    
    // 初始化城市
    NSString *strProvince = self.mArrProvince[0][@"name"];
    NSString *strCity = self.mArrCity[0][@"name"];
    NSString *strArea = self.mArrArea[0][@"name"];
    
    if (self.components == 1)
    {
        self.address = strProvince;
        [self.dicData setObject:strProvince forKey:@"province"];
    }
    else if (self.components == 2)
    {
        self.address = [NSString stringWithFormat:@"%@-%@",strProvince,strCity];
        self.city = strCity;
        [self.dicData setObject:strProvince forKey:@"province"];
        [self.dicData setObject:strCity forKey:@"city"];
    }
    else
    {
        self.address = [NSString stringWithFormat:@"%@-%@-%@",strProvince,strCity,strArea];
        self.city = strCity;
        [self.dicData setObject:strProvince forKey:@"province"];
        [self.dicData setObject:strCity forKey:@"city"];
        [self.dicData setObject:strArea forKey:@"area"];
    }
    [self addressPicker];
}
#pragma mark -

#pragma mark picker代理
// 列数
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return self.components;
}
// 行数
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    if (component == 0)
    {
        return self.mArrProvince.count;
    }
    else if (component == 1)
    {
        return self.mArrCity.count;
    }
    else
    {
        return self.mArrArea.count;
    }
    
}

// 内容
- (NSString*)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    if (component == 0)
    {
        if (self.mArrProvince.count > 0)
        {
            return [self.mArrProvince objectAtIndex:row][@"name"];
        }
    }
    else if (component == 1)
    {
        if (self.mArrCity.count > 0)
        {
            return [self.mArrCity objectAtIndex:row][@"name"];
        }
    }
    else
    {
        if (self.mArrArea.count > 0)
        {
            return [self.mArrArea objectAtIndex:row][@"name"];
        }
    }
    return nil;
}

// picker 选中时调用的方法
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    // 更改省份
    if (component == 0)
    {
        // 清空城市数组
        self.mArrCity = nil;
        // 清空区数组
        self.mArrArea = nil;
        
        if (self.components == 2)// 两级
        {
            // 刷新城市
            for (NSDictionary *dic in self.arr_address )
            {
                if ([dic[@"pid"] isEqualToString:self.mArrProvince[row][@"id"]])
                {
                    [self.mArrCity addObject:dic];
                }
            }
            // 刷新
            [self.addressPicker reloadAllComponents];
        }
        if (self.components == 3)// 三级
        {
            // 刷新城市数组
            for (NSDictionary *dic in self.arr_address )
            {
                if ([dic[@"pid"] isEqualToString:self.mArrProvince[row][@"id"]])
                {
                    [self.mArrCity addObject:dic];
                }
            }
            // 刷新区数组
            for (int i = 0; i <self.arr_address.count; i++)
            {
                if ([self.arr_address[i][@"pid"] isEqualToString:self.mArrCity[0][@"id"]])
                {
                    [self.mArrArea addObject:self.arr_address[i]];
                }
            }
            // 刷新
            [self.addressPicker reloadAllComponents];
        }
        [self.addressPicker selectRow:0 inComponent:1 animated:YES];
        
    }
    else if (component == 1)
    {
        // 清空区数组
        self.mArrArea = nil;
        if (self.components == 3)
        {
            for (NSDictionary *dic in self.arr_address )
            {
                if ([dic[@"pid"] isEqualToString:self.mArrCity[row][@"id"]])
                {
                    [self.mArrArea addObject:dic];
                }
            }
            // 刷新
            [self.addressPicker reloadAllComponents];
        }
    }
    
    if (self.components == 1)
    {
        // 获得省
        NSInteger provinceRow = [self.addressPicker selectedRowInComponent:0];
        NSString *strProvince = self.mArrProvince[provinceRow][@"name"];
        self.city = strProvince;
        
        // 显示
        self.address = [NSString stringWithFormat:@"%@",strProvince];
        [self.dicData setObject:strProvince forKey:@"province"];
    }
    else if (self.components == 2)
    {
        // 获得省
        NSInteger provinceRow = [self.addressPicker selectedRowInComponent:0];
        NSString *strProvince = self.mArrProvince[provinceRow][@"name"];
        
        // 获得市
        NSInteger cityRow = [self.addressPicker selectedRowInComponent:1];
        NSString *strCity = self.mArrCity[cityRow][@"name"];
        
        // 显示
        self.address = [NSString stringWithFormat:@"%@-%@",strProvince,strCity];
        [self.dicData setObject:strProvince forKey:@"province"];
        [self.dicData setObject:strCity forKey:@"city"];
        self.city = strCity;
    }
    else
    {
        // 获得省
        NSInteger provinceRow = [self.addressPicker selectedRowInComponent:0];
        NSString *strProvince = self.mArrProvince[provinceRow][@"name"];
        
        // 获得市
        NSInteger cityRow = [self.addressPicker selectedRowInComponent:1];
        NSString *strCity = self.mArrCity[cityRow][@"name"];
        
        // 获得区
        NSInteger areaRow = [self.addressPicker selectedRowInComponent:2];
        NSString *strArea = self.mArrArea[areaRow][@"name"];
        
        // 显示
        self.address = [NSString stringWithFormat:@"%@-%@-%@",strProvince,strCity,strArea];
        [self.dicData setObject:strProvince forKey:@"province"];
        [self.dicData setObject:strCity forKey:@"city"];
        [self.dicData setObject:strArea forKey:@"area"];
        self.city = strCity;
    }
    
}

#pragma mark - get

- (UIPickerView*)addressPicker
{
    if (_addressPicker == nil)
    {
        _addressPicker  = [[UIPickerView alloc] initWithFrame:CGRectMake(0, 40, kScreenPicker_W, 160)];
        _addressPicker.delegate = self;
        _addressPicker.dataSource = self;
        _addressPicker.showsSelectionIndicator = YES;
        _addressPicker.backgroundColor = [UIColor whiteColor];
        [self addSubview:_addressPicker];
    }
    return _addressPicker;
}
- (NSMutableArray*)mArrArea
{
    if (_mArrArea == nil)
    {
        _mArrArea = [NSMutableArray new];
    }
    return _mArrArea;
}
- (NSMutableArray*)mArrCity
{
    if (_mArrCity == nil)
    {
        _mArrCity = [NSMutableArray new];
    }
    return _mArrCity;
}
- (NSMutableArray*)mArrProvince
{
    if (_mArrProvince == nil)
    {
        _mArrProvince = [NSMutableArray new];
    }
    return _mArrProvince;
}
- (NSMutableArray*)arr_address
{
    if (_arr_address == nil)
    {
        _arr_address = [NSMutableArray new];
    }
    return _arr_address;
}
- (NSMutableDictionary*)dicData
{
    if (_dicData == nil)
    {
        _dicData = [NSMutableDictionary new];
        [_dicData setObject:@"" forKey:@"province"];
        [_dicData setObject:@"" forKey:@"city"];
        [_dicData setObject:@"" forKey:@"area"];
    }
    return _dicData;
}
#pragma mark - 创建按钮
/** 创建按钮 */
- (UIButton *)getButton:(CGRect)frame title:(NSString *)title imageArr:(NSArray *)imgarr backgroundColor:(UIColor *)color Target:(SEL)target
{
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeSystem];
    btn.frame = frame;
    btn.titleLabel.font = [UIFont systemFontOfSize:14];
    [btn setTitle:title forState:UIControlStateNormal];
    [btn setTitleColor:UIColor.whiteColor forState:(UIControlStateNormal)];
    [btn setBackgroundColor:color];
    [btn addTarget:self action:target forControlEvents:UIControlEventTouchUpInside];
    if (imgarr)
    {
        if (imgarr[0])
        {
            [btn setBackgroundImage:imgarr[0] forState:UIControlStateNormal];
        }
        if (imgarr[1])
        {
            [btn setBackgroundImage:imgarr[1] forState:UIControlStateHighlighted];
        }
        if (imgarr[2])
        {
            [btn setBackgroundImage:imgarr[2] forState:UIControlStateDisabled];
        }
    }
    return btn;
}


@end
