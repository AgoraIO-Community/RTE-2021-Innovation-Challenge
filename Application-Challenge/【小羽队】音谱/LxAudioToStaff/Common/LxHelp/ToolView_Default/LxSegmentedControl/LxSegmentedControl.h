//
//  LxSegmentedControl.h
//  PianoBridgeHD
//
//  Created by 李翔 on 2017/8/2.
//  Copyright © 2017年 Mason. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef void(^LxSegmentedControlClickBlock)(NSInteger clickIndex);
@interface LxSegmentedControl : UIView

/**
 *@description 点击回调block
 **/
@property (copy, nonatomic) LxSegmentedControlClickBlock clickBlock;



/**
 *@description 设置默认图片风格segMentEdControl
 *@param normalItems 正常显示图片
 *@param selectedItems 选择图片
 *@param block 点击回调
 **/
- (void)lx_segmentedControledWithNormalItems:(NSArray <UIImage *>*)normalItems
                            SelectedItems:(NSArray <UIImage *>*)selectedItems
                               clickBlock:(LxSegmentedControlClickBlock)block;
/**
 *@description 设置当前点击状态
 *@param index 点击序列
 *@param callBack 是否强制回调
 **/
- (void)clickBtnIndex:(NSInteger)index forceCallBack:(BOOL)callBack;


@end
