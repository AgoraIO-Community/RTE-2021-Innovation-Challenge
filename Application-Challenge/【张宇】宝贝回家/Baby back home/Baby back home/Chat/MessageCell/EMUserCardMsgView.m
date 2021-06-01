//
//  EMUserCardMsgView.m
//  EaseIM
//
//  Created by lixiaoming on 2021/3/19.
//  Copyright © 2021 lixiaoming. All rights reserved.
//

#import "EMUserCardMsgView.h"
#import <SDWebImage/UIImageView+WebCache.h>

@interface EMUserCardMsgView ()
@end

@implementation EMUserCardMsgView

- (instancetype)init
{
    self = [super init];
    if(self){
        [self setupSubViews];
    }
    return self;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (void)setupSubViews
{
    self.layer.cornerRadius = 2;
    UIView* splitLine = [[UIView alloc] init];
    splitLine.backgroundColor = [UIColor colorWithRed:153/255.0 green:153/255.0 blue:153/255.0 alpha:1.0];
    [self addSubview:splitLine];
    [splitLine mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self).offset(20);
        make.right.equalTo(self).offset(-20);
        make.height.equalTo(@1);
        make.bottom.equalTo(self).offset(-30);
    }];
    UILabel* lable = [[UILabel alloc] init];
    lable.text = @"[个人名片]";
    lable.font = [UIFont systemFontOfSize:12];
    lable.textColor = [UIColor colorWithRed:153/255.0 green:153/255.0 blue:153/255.0 alpha:1.0];
    [self addSubview:lable];
    [lable mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(@20);
        make.right.equalTo(self);
        make.bottom.equalTo(self);
        make.top.equalTo(splitLine.mas_bottom);
    }];
}
- (void)setModel:(EaseMessageModel *)model
{
    EMMessage* msg = model.message;
    if(msg.body.type == EMMessageBodyTypeCustom) {
        EMCustomMessageBody* body = (EMCustomMessageBody* )msg.body;
        NSString* uid = [body.customExt objectForKey:@"uid"];
        UILabel* uidLable = [[UILabel alloc] initWithFrame:CGRectMake(80, 50, 200, 25)];
        uidLable.text = uid;
        uidLable.textColor = [UIColor grayColor];
        uidLable.font = [UIFont systemFontOfSize:12];
        [self addSubview:uidLable];
        NSString* nickName = [body.customExt objectForKey:@"nickname"];
        UILabel* nickNameLable = [[UILabel alloc] initWithFrame:CGRectMake(80, 20, 200, 25)];
        nickNameLable.text = nickName;
        nickNameLable.font = [UIFont systemFontOfSize:25];
        [self addSubview:nickNameLable];
        NSString* strUrl = [body.customExt objectForKey:@"avatar"];
        NSURL* url = [NSURL URLWithString:strUrl];
        UIImageView* avartView = [[UIImageView alloc] initWithFrame:CGRectMake(20, 20, 50, 50)];
        if(strUrl.length > 0 && url)
        {
            [avartView sd_setImageWithURL:url completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
                    
                }];
        }else{
            avartView.image = [UIImage imageNamed:@"defaultAvatar"];
        }
        [self addSubview:avartView];
            
    }
}
@end
