//
//  ConfirmUserCardView.m
//  EaseIM
//
//  Created by lixiaoming on 2021/3/25.
//  Copyright © 2021 lixiaoming. All rights reserved.
//

#import "ConfirmUserCardView.h"
#import <SDWebImage/UIImageView+WebCache.h>

@interface ConfirmUserCardView ()
@property (nonatomic,strong) NSString* remoteName;
@property (nonatomic,strong) NSString* avatarUrl;
@property (nonatomic,strong) NSString* showName;
@property (nonatomic,strong) NSString* uid;
@end

@implementation ConfirmUserCardView
- (instancetype)initWithRemoteName:(NSString*)aRemoteName avatarUrl:(NSString*)aUrl showName:(NSString*)aShowName uid:(NSString*)aUid delegate:(id<ConfirmUserCardViewDelegate>)aDelegate
{
    self = [super init];
    if(self) {
        self.avatarUrl = aUrl;
        self.remoteName = aRemoteName;
        self.showName = aShowName;
        self.delegate = aDelegate;
        self.uid = aUid;
        [self setupSubViews];
    }
    return self;
}

- (void)setupSubViews
{
    self.alpha = 1.0;
    self.backgroundColor = [UIColor whiteColor];
    self.layer.cornerRadius = 4;
    self.layer.borderColor = [UIColor grayColor].CGColor;
    UILabel* titleLable = [[UILabel alloc] initWithFrame:CGRectMake(20, 16, 150, 28)];
    titleLable.text = @"发送给:";
    titleLable.textColor = [UIColor colorWithRed:66/255.0 green:66/255.0 blue:66/255.0 alpha:1.0];
    titleLable.font = [UIFont systemFontOfSize:28];
    [self addSubview:titleLable];
    UIImageView* avatarView = [[UIImageView alloc] initWithFrame:CGRectMake(17, 60, 60, 60)];
    avatarView.image = [UIImage imageNamed:@"defaultAvatar"];
    if(self.avatarUrl.length > 0) {
        NSURL*url = [NSURL URLWithString:self.avatarUrl];
        if(url){
            [avatarView sd_setImageWithURL:url completed:nil];
        }
    }
    
    [self addSubview:avatarView];
    UILabel* remoteTitle = [[UILabel alloc] initWithFrame:CGRectMake(89, 77, 150, 24)];
    remoteTitle.font = [UIFont systemFontOfSize:24];
    remoteTitle.text = self.remoteName;
    remoteTitle.textColor = [UIColor colorWithRed:51/255.0 green:51/255.0 blue:51/255.0 alpha:1.0];
    [self addSubview:remoteTitle];
    
    UILabel* userCardLable = [[UILabel alloc] initWithFrame:CGRectMake(16, 140, 310, 36)];
    userCardLable.text = [NSString stringWithFormat:@"   [个人名片] %@",self.showName];
    userCardLable.font = [UIFont systemFontOfSize:12];
    userCardLable.layer.cornerRadius = 4;
    userCardLable.textColor = [UIColor colorWithRed:153/255.0 green:153/255.0 blue:153/255.0 alpha:1.0];
    userCardLable.backgroundColor = [UIColor colorWithRed:242/255.0 green:242/255.0 blue:242/255.0 alpha:1.0];
    [self addSubview:userCardLable];
    
    UIButton* okButton = [UIButton buttonWithType:UIButtonTypeSystem];
    [okButton setTitle:@"确定" forState:UIControlStateNormal];
    okButton.frame = CGRectMake(170, 193, 171, 57);
    okButton.layer.borderWidth = 1;
    okButton.layer.borderColor = [UIColor colorWithRed:242/255.0 green:242/255.0 blue:242/255.0 alpha:1.0].CGColor;
    [okButton setTitleColor:[UIColor colorWithRed:4/255.0 green:174/255.0 blue:240/255.0 alpha:1.0] forState:UIControlStateNormal];
    [self addSubview:okButton];
    [okButton addTarget:self action:@selector(okAction) forControlEvents:UIControlEventTouchUpInside];
    UIButton* cancelButton = [UIButton buttonWithType:UIButtonTypeSystem];
    [cancelButton setTitle:@"取消" forState:UIControlStateNormal];
    [self addSubview:cancelButton];
    [cancelButton setTitleColor:[UIColor colorWithRed:51/255.0 green:51/255.0 blue:51/255.0 alpha:1.0] forState:UIControlStateNormal];
    cancelButton.frame = CGRectMake(0, 193, 171, 57);
    cancelButton.layer.borderWidth = 1;
    cancelButton.layer.borderColor = [UIColor colorWithRed:242/255.0 green:242/255.0 blue:242/255.0 alpha:1.0].CGColor;
    [cancelButton addTarget:self action:@selector(cancelAction) forControlEvents:UIControlEventTouchUpInside];
}

- (void)cancelAction
{
    if(self.delegate && [self.delegate respondsToSelector:@selector(clickCancel)]) {
        [self.delegate clickCancel];
    }
}

- (void)okAction
{
    if(self.delegate && [self.delegate respondsToSelector:@selector(clickOK:nickName:avatarUrl:)]) {
        [self.delegate clickOK:self.uid nickName:self.showName avatarUrl:self.avatarUrl];
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
