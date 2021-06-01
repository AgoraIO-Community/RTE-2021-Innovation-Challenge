//
//  LxSegmentedControl.m
//  PianoBridgeHD
//
//  Created by 李翔 on 2017/8/2.
//  Copyright © 2017年 Mason. All rights reserved.
//

#import "LxSegmentedControl.h"
#import "UIImage+Default.h"
@interface LxSegmentedControl()

/**
 *@description btn容器
 **/
@property (strong, nonatomic) NSMutableArray <UIButton *> *segBtnArray;

@end


@implementation LxSegmentedControl

#pragma mark - CallFunction
/**
 *@description 设置默认图片风格segMentEdControl
 **/
- (void)lx_segmentedControledWithNormalItems:(NSArray <UIImage *>*)normalItems
                            SelectedItems:(NSArray <UIImage *>*)selectedItems
                               clickBlock:(LxSegmentedControlClickBlock)block
{
    if (_segBtnArray) {
        for (UIButton *tempBtn in _segBtnArray) {
            [tempBtn removeFromSuperview];
        }
        [_segBtnArray removeAllObjects];
    }else
    {
        _segBtnArray = [NSMutableArray array];
    }
    _clickBlock= nil;
    _clickBlock = [block copy];
    
    self.backgroundColor = [UIColor clearColor];
    
    NSInteger tag = 1;
    CGFloat offsetX = 0;
    for (UIImage *norMalImage in normalItems) {
        UIButton *btn = [[UIButton alloc] initWithFrame:CGRectMake(offsetX, 0, norMalImage.size.width, norMalImage.size.height)];
        [btn setImage:norMalImage forState:UIControlStateNormal];
        btn.tag = tag;
        [btn addTarget:self action:@selector(clickBtn:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:btn];
        [_segBtnArray addObject:btn];
        
        
        tag ++;
        offsetX += CGRectGetWidth(btn.frame);
    }

    tag = 1;
    for (UIImage *selectedImage in selectedItems) {
        UIButton *btn = [self viewWithTag:tag];
        if (btn) {
            [btn setImage:selectedImage forState:UIControlStateSelected];
        }
        tag ++;
    }
    
    if (_segBtnArray.count > 0) {
        self.frame = CGRectMake(0, 0, CGRectGetMaxX([_segBtnArray lastObject].frame), CGRectGetHeight([_segBtnArray lastObject].frame));
    }
}

- (void)clickBtnIndex:(NSInteger)index forceCallBack:(BOOL)callBack
{
    UIButton *btn = [self viewWithTag:index + 1];
    if (btn) {
        btn.selected = YES;
        for (UIButton *tempBtn in _segBtnArray) {
            if (tempBtn.selected && tempBtn != btn) {
                tempBtn.selected = NO;
                break;
            }
        }
        debugLog(@"%s   index =%ld",__func__,(long)index);
        if (_clickBlock && callBack) {
            _clickBlock(index);
        }
    }
}

- (void)clickBtn:(UIButton *)btn
{
    if (!btn.selected) {
        [self clickBtnIndex:btn.tag - 1 forceCallBack:YES];
    }
}


/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
