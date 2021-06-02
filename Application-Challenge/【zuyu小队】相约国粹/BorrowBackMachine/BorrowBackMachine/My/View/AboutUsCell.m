//
//  AboutUsCell.m
//  SiyecaoTercher
//
//  Created by zuyu on 2018/5/22.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "AboutUsCell.h"

@interface AboutUsCell()
@property(nonatomic,strong)UILabel *textL;
@property(nonatomic,strong)UILabel *valueL;

@end
@implementation AboutUsCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier indexPathRow:(NSInteger )row
{
    self = [super initWithStyle:UITableViewCellStyleDefault reuseIdentifier:reuseIdentifier];
    
    if (self) {
        
        _textL = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, WIDTH  /2 - 10, 50)];
        
        _textL.textAlignment = NSTextAlignmentLeft;
        
        _textL.backgroundColor = [UIColor whiteColor];
        
        [self.contentView addSubview:_textL];
        if (row < 3) {
            
            _valueL = [[UILabel alloc] initWithFrame:CGRectMake(WIDTH/2, 10, WIDTH/2-10, 50)];
            
            _valueL.textAlignment = NSTextAlignmentRight;
            
            _valueL.backgroundColor = [UIColor whiteColor];
            
            _valueL.textColor = [UIColor lightGrayColor];
            
            [self.contentView addSubview:_valueL];
       
        }else{
            
            _textL.frame = CGRectMake(10, 10, WIDTH - 10, 50);
       
        }
        
        
    }
    
    return self;
}


-(void)setText:(NSString *)text
{
    _textL.text = text;
}

-(void)setValue:(NSString *)value
{
    _valueL.text = value;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
