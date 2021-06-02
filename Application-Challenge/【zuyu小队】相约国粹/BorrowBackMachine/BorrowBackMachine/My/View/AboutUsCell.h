//
//  AboutUsCell.h
//  SiyecaoTercher
//
//  Created by zuyu on 2018/5/22.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AboutUsCell : UITableViewCell

@property(nonatomic,strong) NSString *text;
@property(nonatomic,strong) NSString *value;

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier indexPathRow:(NSInteger )row;
@end
