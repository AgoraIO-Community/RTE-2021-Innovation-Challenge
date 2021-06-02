//
//  MainMP4ListCell.h
//  CNCLibraryScan
//
//  Created by zuyu on 2018/1/3.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol MainMP4ListCellDelegate <NSObject>
- (void)download:(NSInteger) indexRow;
@end



@interface MainMP4ListCell : UITableViewCell

@property (nonatomic,strong) UILabel *text;
@property (nonatomic,strong) UILabel *downLoadText;

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier WithTag:(NSInteger )tag;
@property (nonatomic, assign) id <MainMP4ListCellDelegate> delegate;

@end
