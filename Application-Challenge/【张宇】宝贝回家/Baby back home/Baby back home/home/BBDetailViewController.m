//
//  BBDetailViewController.m
//  Baby back home
//
//  Created by zhangyu on 2021/5/17.
//

#import "BBDetailViewController.h"
#import "MessageDBManager.h"
#import "EMChatViewController.h"
#import "EMPersonalDataViewController.h"
#import <AVKit/AVKit.h>

@interface BBDetailViewController ()
@property (nonatomic, strong) NSString *videoUrl;
@property (nonatomic, strong)AVPlayerViewController *playerVC;
@end

@implementation BBDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = UIColor.whiteColor;
    
//    UIBarButtonItem * barBtn = [[UIBarButtonItem alloc] initWithTitle:@"转发" style:(UIBarButtonItemStyleDone) target:self action:@selector(share)];
    UIBarButtonItem * barBtn = [[UIBarButtonItem alloc] initWithImage:[[UIImage imageNamed:@"share"] imageWithRenderingMode:(UIImageRenderingModeAlwaysOriginal)] style:(UIBarButtonItemStyleDone) target:self action:@selector(share)];
    
    UIBarButtonItem * leftBtn = [[UIBarButtonItem alloc] initWithImage:[[UIImage imageNamed:@"back_left"] imageWithRenderingMode:(UIImageRenderingModeAlwaysOriginal)] style:(UIBarButtonItemStyleDone) target:self action:@selector(popVC)];
    self.navigationItem.rightBarButtonItem = barBtn;
    self.navigationItem.leftBarButtonItem = leftBtn;
    self.navigationItem.title = @"详情";
    [self initSubviews];
    
    
    
    // Do any additional setup after loading the view.
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
}
- (void)popVC{
    [self.navigationController popViewControllerAnimated:YES];
}


- (void)initSubviews{
    if (self.numOfPic == 0 && [self.model.imgName containsString:@"mp4"]) {
        self.numOfPic = 1;
    }
    CGFloat picY = KScreenWidth * 4/3 * self.numOfPic;
    CGFloat picH = KScreenWidth * 4/3;
    CGFloat lbx = 40 * KScreenRatio;
    CGFloat tfx = 220 * KScreenRatio;
    UIScrollView * scrollview = [[UIScrollView alloc] initWithFrame:(CGRectMake(0, 0, KScreenWidth, KScreenHeight))];
    [self.view addSubview:scrollview];
    scrollview.contentSize = CGSizeMake(0, picY + 360 + [self textHeight:self.model.des]);

    UILabel * titleLB = [[UILabel alloc] init];
    titleLB.font = [UIFont systemFontOfSize:26 weight:(UIFontWeightHeavy)];
    titleLB.text = self.model.title;
    titleLB.x = lbx;
    titleLB.y = 20;
    titleLB.width = KScreenWidth - 50;
    titleLB.height = 30;
    [scrollview addSubview:titleLB];
    
    UILabel * desLB = [[UILabel alloc] init];
    desLB.text = self.model.des;
    desLB.numberOfLines = 0;
    desLB.textColor = KFontColor;
    desLB.x = lbx;
    desLB.y = 60;
    desLB.width = KScreenWidth - 50;
    desLB.height = [self textHeight:self.model.des];
    [scrollview addSubview:desLB];
    
    if ([self.model.imgName containsString:@"mp4"]) {
        self.videoUrl = self.model.imgName;
            /*
             因为是 http 的链接，所以要去 info.plist里面设置
             App Transport Security Settings
             Allow Arbitrary Loads  = YES
             */
            self.playerVC = [[AVPlayerViewController alloc] init];
            self.playerVC.player = [AVPlayer playerWithURL:[self.videoUrl hasPrefix:@"http"] ? [NSURL URLWithString:self.videoUrl]:[NSURL fileURLWithPath:self.videoUrl]];
            self.playerVC.view.frame = CGRectMake(0, 160 + [self textHeight:self.model.des], KScreenWidth, picH);
            self.playerVC.showsPlaybackControls = YES;
        //self.playerVC.entersFullScreenWhenPlaybackBegins = YES;//开启这个播放的时候支持（全屏）横竖屏哦
        //self.playerVC.exitsFullScreenWhenPlaybackEnds = YES;//开启这个所有 item 播放完毕可以退出全屏
            [self.view addSubview:self.playerVC.view];
            
            if (self.playerVC.readyForDisplay) {
                [self.playerVC.player play];
            }
    }else{
        NSArray * picArr = [self.model.imgName componentsSeparatedByString:@","];
        for (int i = 0; i < self.numOfPic; i++) {
            UIImageView * img = [[UIImageView alloc] initWithFrame:(CGRectMake(21, (i * picH) + 60 + [self textHeight:self.model.des], KScreenWidth - 42, picH))];
            img.contentMode = UIViewContentModeScaleAspectFit;
            img.image = [[MessageDBManager sharedInstance] getCacheImageUseImagePath:picArr[i]];
            if ([[MessageDBManager sharedInstance] getCacheImageUseImagePath:picArr[i]] == nil) {
                img.image = [UIImage imageNamed:self.model.imgName];
            }
            
            [scrollview addSubview:img];
        }
    }
    
    
    
    

    
    
    
    UIView * backGreenView = [[UIView alloc] initWithFrame:(CGRectMake(21, picY + 80 + [self textHeight:self.model.des], KScreenWidth - 42, 257))];
    backGreenView.backgroundColor = [UIColor colorWithRed:227/255.0 green:245/255.0 blue:242/255.0 alpha:1];
    backGreenView.layer.cornerRadius = 6;
    backGreenView.layer.masksToBounds = YES;
    [scrollview addSubview:backGreenView];
    
    
    UILabel * nameTitleLB = [[UILabel alloc] init];
    nameTitleLB.width = KScreenWidth - 90;
    nameTitleLB.x = lbx;
    nameTitleLB.y = 20;
    nameTitleLB.height = 25;
    NSString * name = [NSString stringWithFormat:@"姓名:  %@",self.model.pName];
    nameTitleLB.text = name;
    nameTitleLB.textColor = UIColor.blackColor;
    [backGreenView addSubview:nameTitleLB];
//    [self textHeight:self.model.pName];
    UILabel * genderLB = [[UILabel alloc] init];
    genderLB.width = KScreenWidth - 90;
    genderLB.x = lbx;
    genderLB.y = 60;
    genderLB.height = 25;
    NSString * gender = [NSString stringWithFormat:@"性别:  %@",self.model.pGender];
    genderLB.text = gender;
    genderLB.numberOfLines = 0;
    genderLB.textColor = UIColor.blackColor;
    [backGreenView addSubview:genderLB];
    
    UILabel * cityTitleLB = [[UILabel alloc] init];
    cityTitleLB.width = KScreenWidth - 90;
    cityTitleLB.x = lbx;
    cityTitleLB.y = 100;
    cityTitleLB.height = 25;
    NSString * city = [NSString stringWithFormat:@"丢失城市:  %@",self.model.address];
    cityTitleLB.text = city;
    cityTitleLB.textColor = UIColor.blackColor;
    [backGreenView addSubview:cityTitleLB];
    
    
    
    UILabel * phoneTitleLB = [[UILabel alloc] init];
    phoneTitleLB.width = KScreenWidth - 90;
    phoneTitleLB.x = lbx;
    phoneTitleLB.y = 140;
    phoneTitleLB.height = 25;
    NSString * phoneNum = [NSString stringWithFormat:@"联系电话:  %@",self.model.phoneNum];
    phoneTitleLB.text = phoneNum;
    phoneTitleLB.textColor = UIColor.blackColor;
    [backGreenView addSubview:phoneTitleLB];
    
    UIButton *contactBtn = [[UIButton alloc]init];
    contactBtn.frame = CGRectMake(60, 180, backGreenView.width - 120, 40);
    [contactBtn setTitle:@"私信 TA" forState:(UIControlStateNormal)];
    contactBtn.backgroundColor = KBGreenColor2;
    contactBtn.layer.cornerRadius = 20;
    contactBtn.layer.masksToBounds = YES;
    [contactBtn setTitleColor:UIColor.whiteColor forState:(UIControlStateNormal)];
    [contactBtn addTarget:self action:@selector(contactAction) forControlEvents:UIControlEventTouchUpInside];
    [backGreenView addSubview:contactBtn];
    
    
    
}

- (void)contactAction{
//    [EMClient sharedClient].currentUsername;
    if (self.model.userid == nil) {
        self.model.userid = @"user1";
    }
    EMChatViewController * vc = [[EMChatViewController alloc]initWithConversationId:self.model.userid conversationType:EMConversationTypeChat];

    [self.navigationController pushViewController:vc animated:YES];
    
//    EMPersonalDataViewController *controller = [[EMPersonalDataViewController alloc]initWithNickName:self.model.userid];
//    [self.navigationController pushViewController:controller animated:YES];
}
- (void)backBackion{
    [self.navigationController popViewControllerAnimated:YES];
}

-(CGFloat)textHeight:(NSString *)str{
    NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    paragraphStyle.lineBreakMode = NSLineBreakByCharWrapping;
//Attribute传和label设定的一样
    NSDictionary * attributes = @{
                                  NSFontAttributeName:[UIFont systemFontOfSize:17.f],
                                  NSParagraphStyleAttributeName: paragraphStyle
                                  };
//这里的size，width传label的宽，高默认都传MAXFLOAT
    CGSize textRect = CGSizeMake(KScreenWidth - (KScreenRatio * 240), MAXFLOAT);
    CGFloat textHeight = [str boundingRectWithSize: textRect
                                           options:NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading
                                        attributes:attributes
                                           context:nil].size.height;
    return textHeight;
}



- (void)share{
    
    NSArray * picArr = [self.model.imgName componentsSeparatedByString:@","];
    NSMutableArray * imgArr = [NSMutableArray array];
    for (int i = 0; i < self.numOfPic; i++) {
        UIImage * img = [[MessageDBManager sharedInstance] getCacheImageUseImagePath:picArr[i]];
        if (img == nil) {
            img = [UIImage imageNamed:self.model.imgName];
        }
        [imgArr addObject:img];
    }
    [imgArr insertObject:@"宝贝回家" atIndex:0];
    
    NSArray * shareArr = [NSArray arrayWithArray:imgArr];
    
    UIActivityViewController * shareVC = [[UIActivityViewController alloc] initWithActivityItems:shareArr applicationActivities:nil];
    shareVC.view.backgroundColor = UIColor.whiteColor;
    [self presentViewController:shareVC animated:YES completion:NULL];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
