//
//  MyMessageCell.h
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/9.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MyMessageCell : UITableViewCell

@property(nonatomic,strong) NSString *image;
@property(nonatomic,strong) NSString *title;

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier withIndexRow:(NSInteger )row;
@end
