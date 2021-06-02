//
//  EntityDetailView.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/8.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "EntityDetailView.h"
#import "UIImageView+WebCache.h"
#import "zuyu.h"
@interface EntityDetailView()
{
    UIImageView *_imageView;
    UILabel     *_nameLabel;
    UILabel     *_writerLabel;
    UILabel     *_formLabel;
    UILabel     *_sizeLabel;
}

@end
@implementation EntityDetailView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

-(instancetype)init
{
    self = [super init];
    
    if (self) {
        
        _imageView = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 110, 160)];
        
        [self addSubview:_imageView];
        
        _nameLabel = [[UILabel alloc ] initWithFrame:CGRectMake(130, 10, WIDTH - 130, 53)];
        
        _nameLabel.font = [UIFont systemFontOfSize:16];
        
        [self addSubview:_nameLabel];
        
        _writerLabel = [[UILabel alloc] initWithFrame:CGRectMake(130, 63, WIDTH - 130, 53)];
        
        _writerLabel.textColor = [UIColor lightGrayColor];

        [self addSubview:_writerLabel];
        
        _formLabel = [[UILabel alloc] initWithFrame:CGRectMake(130, 116, WIDTH - 130, 53)];
        _formLabel.textColor = [UIColor lightGrayColor];

        [self addSubview:_formLabel];
        
        _sizeLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 180, WIDTH - 20, HEIGHT - 300)];
        
        _sizeLabel.numberOfLines = 0;
        
        [self addSubview:_sizeLabel];
        
    }
    
    return self;
}

-(void)setIamge:(NSString *)iamge
{
    [_imageView sd_setImageWithURL:[NSURL URLWithString:iamge] placeholderImage:[ZuyuPlaceholderImage returnPlaceholder:3]];
}

-(void)setName:(NSString *)name
{
    _nameLabel.text = name;
}

-(void)setAuthor:(NSString *)author
{
    _writerLabel.text = [NSString stringWithFormat:@"作者:%@",author];
}

-(void)setForm:(NSString *)form
{
    _formLabel.text = [NSString stringWithFormat:@"出版社:%@",form];
}


-(void)setDescr:(NSString *)descr
{
    _sizeLabel.text = [NSString stringWithFormat:@"简介:%@",descr];
    
    float h = [self getHeightLineWithString:_sizeLabel.text withWidth:WIDTH - 20 withFont:[UIFont systemFontOfSize:15]];
    
    _sizeLabel.frame = CGRectMake(10, 170, WIDTH - 20, h);
}

- (CGFloat)getHeightLineWithString:(NSString *)string withWidth:(CGFloat)width withFont:(UIFont *)font {        //1.1最大允许绘制的文本范围
    CGSize size = CGSizeMake(width, 2000);    //1.2配置计算时的行截取方法,和contentLabel对应
    NSMutableParagraphStyle *style = [[NSMutableParagraphStyle alloc] init];    [style setLineSpacing:10];
    //1.3配置计算时的字体的大小    //1.4配置属性字典
    NSDictionary *dic = @{NSFontAttributeName:font, NSParagraphStyleAttributeName:style};
    //2.计算    //如果想保留多个枚举值,则枚举值中间加按位或|即可,并不是所有的枚举类型都可以按位或,只有枚举值的赋值中有左移运算符时才可以
    CGFloat height = [string boundingRectWithSize:size options:NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading attributes:dic context:nil].size.height;
    
    return height;
    
    
}

@end
