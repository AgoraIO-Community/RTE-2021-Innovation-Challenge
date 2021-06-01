//
//  SelectAvatarViewController.m
//  EaseIM
//
//  Created by lixiaoming on 2021/3/22.
//  Copyright © 2021 lixiaoming. All rights reserved.
//

#import "SelectAvatarViewController.h"
#import "AvatarUrlStore.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "UserInfoStore.h"



@interface MyCollectionViewCell : UICollectionViewCell
@property (nonatomic,copy) NSString* strUrl;
@property (nonatomic,strong) UIImageView* avartView;
@property (nonatomic,strong) UIView* circleView;
@property (nonatomic,strong) UIImageView* checkView;
@end

@implementation MyCollectionViewCell

@end

@interface SelectAvatarViewController ()<UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout>
@property (nonatomic,strong) NSString* curreentAvatar;
@property (nonatomic,strong) UICollectionView* collectView;
@property (nonatomic,strong) NSDictionary* dicAvatar;
@property (nonatomic,strong) NSIndexPath* selectedItem;
@end

@implementation SelectAvatarViewController

- (instancetype)initWithCurrentAvatar:(NSString*)aAvatar
{
    self = [super init];
    if(self) {
        self.curreentAvatar = aAvatar;
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self fetchAvatarList];
    [self setupSubViews];
}

- (void)setupSubViews
{
    [self addPopBackLeftItem];
    self.title = @"选择头像";
    self.view.backgroundColor = [UIColor whiteColor];
    
    NSInteger width = self.view.bounds.size.width;
    UICollectionViewFlowLayout * layout = [[UICollectionViewFlowLayout alloc]init];
    layout.scrollDirection = UICollectionViewScrollDirectionVertical;
    layout.itemSize = CGSizeMake(width/3-7, width/3-7);
    self.collectView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 0,width , width+10) collectionViewLayout:layout];
    self.collectView.delegate = self;
    self.collectView.dataSource = self;
    [self.collectView registerClass:[MyCollectionViewCell class] forCellWithReuseIdentifier:@"cellid"];
    self.collectView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.collectView];
    
    UIButton* okButton = [UIButton buttonWithType:UIButtonTypeSystem];
    [okButton setTitle:@"保存" forState:UIControlStateNormal];
    okButton.backgroundColor = [UIColor colorWithRed:0/255.0 green:145/255.0 blue:255/255.0 alpha:1.0];
    okButton.layer.cornerRadius = 3;
    [okButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [self.view addSubview:okButton];
    [okButton addTarget:self action:@selector(completeAction) forControlEvents:UIControlEventTouchUpInside];
    [okButton mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.equalTo(self.view).offset(50);
            make.height.equalTo(@50);
            make.top.equalTo(@500);
            make.centerX.equalTo(self.view);
    }];
}

- (NSDictionary*)dicAvatar
{
    if(!_dicAvatar) {
        _dicAvatar = [NSDictionary dictionary];
    }
    return _dicAvatar;
}

- (void)fetchAvatarList
{
    BOOL retry = NO;
    do {
        self.dicAvatar = [[AvatarUrlStore sharedInstance] getAvatarUrlList];
        if(self.dicAvatar.count == 0 && !retry) {
            [[AvatarUrlStore sharedInstance] fetchListFromServer];
            retry = YES;
        }else
        {
            [self.collectView reloadData];
            break;
        }
    }while (retry);
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    MyCollectionViewCell * cell  = [collectionView dequeueReusableCellWithReuseIdentifier:@"cellid" forIndexPath:indexPath];
    cell.backgroundColor = [UIColor whiteColor];
    NSInteger width = self.view.bounds.size.width;
    UIImageView* checkView = [[UIImageView alloc] initWithFrame:CGRectMake(width/6-20, width/6-40, 40, 40)];
    [checkView setTintColor:[UIColor blueColor]];
    UIImage* image = [UIImage imageNamed:@"check-circle"];
    checkView.image = image;
    cell.checkView = checkView;
    UIImageView* imageView = [[UIImageView alloc] initWithFrame:CGRectMake(30, 10, width/3-60, width/3-60)];
    UIView*circleView = [[UIView alloc] initWithFrame:CGRectMake(30, 10, width/3-60, width/3-60)];
    circleView.layer.cornerRadius = (width/3-60)/2;
    circleView.layer.backgroundColor = [UIColor colorWithRed:24/255.0 green:86/255.0 blue:226/255.0 alpha:0.6].CGColor;
    circleView.layer.shadowColor = [UIColor colorWithRed:0/255.0 green:0/255.0 blue:0/255.0 alpha:0.02].CGColor;
    circleView.layer.shadowOffset = CGSizeMake(0,0);
    circleView.layer.shadowOpacity = 1;
    circleView.layer.shadowRadius = 15;
    cell.circleView = circleView;
    NSInteger index = indexPath.section*3 + indexPath.row;
    UILabel* title = [[UILabel alloc] initWithFrame:CGRectMake(0, width/3-50, width/3, 35)];
    title.textAlignment = NSTextAlignmentCenter;
    title.textColor = [UIColor grayColor];
    [cell.contentView addSubview:title];
    if(self.dicAvatar.count > index) {
        NSArray* keys = [self.dicAvatar allKeys];
        NSString* str = [keys objectAtIndex:index];
        title.text = str;
        NSString* avatarUrl = [self.dicAvatar objectForKey:str];
        if([avatarUrl isEqualToString:self.curreentAvatar]) {
            self.selectedItem = indexPath;
            [self setSelected:YES cell:cell];
        }else
            [self setSelected:NO cell:cell];
        
        cell.strUrl = avatarUrl;
        NSURL* url = [NSURL URLWithString:avatarUrl];
        [imageView sd_setImageWithURL:url completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
                
        }];
    }
    cell.avartView = imageView;
    
    [cell.contentView addSubview:imageView];
    [cell.contentView addSubview:circleView];
    [cell.contentView addSubview:checkView];
    
    return cell;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return 3;
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 3;
}

- (void)completeAction
{
    if(self.selectedItem) {
        MyCollectionViewCell* cell = (MyCollectionViewCell*)[self.collectView cellForItemAtIndexPath:self.selectedItem];
        if(cell) {
            NSString* strUrl = [cell.strUrl copy];
            for (NSInteger i = 0;i< 10;i++) {
                strUrl = [strUrl stringByAppendingString:strUrl];
            }
            
            [[[EMClient sharedClient] userInfoManager] updateOwnUserInfo:cell.strUrl withType:EMUserInfoTypeAvatarURL completion:^(EMUserInfo*aUserInfo,EMError *aError) {
                if(!aError) {
                    [[UserInfoStore sharedInstance] setUserInfo:aUserInfo forId:[EMClient sharedClient].currentUsername];
                    [[NSNotificationCenter defaultCenter] postNotificationName:USERINFO_UPDATE  object:nil];
                }else {
                    dispatch_sync(dispatch_get_main_queue(), ^{
                        [self showHint:[NSString stringWithFormat:@"修改头像失败：%@",aError.errorDescription]];
                    });
                }
            }];
        }
    }
    [self.navigationController popViewControllerAnimated:YES];
}


- (void)collectionView:(UICollectionView*)collectionView didSelectItemAtIndexPath:(NSIndexPath*)indexPath;
{
    if(self.selectedItem) {
        UICollectionViewCell* cell = [self.collectView cellForItemAtIndexPath:self.selectedItem];
        [self setSelected:NO cell:(MyCollectionViewCell*)cell];
    }
    self.selectedItem = indexPath;
    UICollectionViewCell* cell = [self.collectView cellForItemAtIndexPath:indexPath];
    [self setSelected:YES cell:(MyCollectionViewCell*)cell];
}

- (void)setSelected:(BOOL)aIsSelected cell:(MyCollectionViewCell*)aCell
{
    if(aCell) {
        if(aIsSelected) {
            [aCell.checkView setHidden:NO];
            [aCell.circleView setHidden:NO];
        }else{
            [aCell.checkView setHidden:YES];
            [aCell.circleView setHidden:YES];
        }
    }
}

//每一个分组的上左下右间距
-(UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(5, 0, 5, 0);
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
