//
//  OnlineFristCollectionCell.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/7.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OnlineFristCollectionCell : UICollectionViewCell
@property(nonatomic,strong) NSString *bookImage;
@property(nonatomic,strong) NSString *bookNameStr;
@property(nonatomic,strong) NSString *writerNameStr;
@property(nonatomic,strong) NSString *summerStr;
@property(nonatomic,strong) UIImageView *typeImage;


@end
