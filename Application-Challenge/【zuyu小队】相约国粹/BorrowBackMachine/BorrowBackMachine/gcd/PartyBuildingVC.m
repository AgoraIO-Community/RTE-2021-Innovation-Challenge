//
//  PartyBuildingVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/9.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "PartyBuildingVC.h"
#import <WebKit/WebKit.h>
#import "zuyu.h"
#import "SDCycleScrollView.h"
#import "PartyBuildingMainCell.h"
#import "ResourcesListVC.h"
#import "DJListVC.h"
#import "QRCodeReaderViewController.h"
#import "ScanDownloadViewController.h"
#import "DownloadData.h"
#import "DJSearchVC.h"
#import "DJNewsVC.h"
#import "DJListenVC.h"

@interface PartyBuildingVC ()<UIWebViewDelegate,WKNavigationDelegate,UICollectionViewDelegate,UICollectionViewDataSource,QRCodeReaderDelegate>
{
    UICollectionView *_collectionView;
    SDCycleScrollView *cycleScrollView;
}

@end

@implementation PartyBuildingVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createNav];
    [self createScrollBanner];
    [self createCollection];
}


-(void)createNav
{
    UIView *navView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 74)];
    
    [self.view addSubview:navView];
    
    UIImageView *image = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 74)];
    
    image.image = [UIImage imageNamed:@"gcdNav"];
    
    [navView addSubview:image];
    
    UIImageView *titleImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, 24, WIDTH/2.5, 40)];
    
    titleImage.image = [UIImage imageNamed:@"gcdTitle"];
    
    [navView addSubview:titleImage];
    
    UIButton *scanButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    scanButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    scanButton.frame = CGRectMake(WIDTH * 0.8, 30, 26, 26);
    
    [scanButton setImage:[UIImage imageNamed:@"scanButton"] forState:UIControlStateNormal];
    
    [scanButton addTarget:self action:@selector(scanClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [navView addSubview:scanButton];
    
    
    UIButton *searchButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    searchButton.frame = CGRectMake(WIDTH * 0.9, 30, 26, 26);
    
    [searchButton setImage:[UIImage imageNamed:@"searchForFrist"] forState:UIControlStateNormal];
    
    [searchButton addTarget:self action:@selector(searchClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [navView addSubview:searchButton];
    
    
}
#pragma mark - 扫一扫
-(void)scanClick:(UIButton *)button
{
    
    if ([ISLOGIN integerValue]) {
        QRCodeReaderViewController *reader = [QRCodeReaderViewController new];
        
        reader.modalPresentationStyle = UIModalPresentationFormSheet;
        
        reader.delegate = self;
        
        __weak typeof (self) wSelf = self;
        
        [reader setCompletionWithBlock:^(NSString *resultAsString) {
            
            [wSelf.navigationController popViewControllerAnimated:YES];
            
            NSLog(@"%@",resultAsString);
            
            [wSelf requestScanDate:resultAsString];
            
        }];
        
        [self.navigationController pushViewController:reader animated:YES];
    }else{
        [ZuyuAlertShow alertShow:@"请先登录" viewController: self];

    }
   
}

#pragma mark - 扫描结果的数据请求
-(void)requestScanDate:(NSString *)port
{
    
    NSString *string  =  [[[NSString stringWithFormat:@"%@",port] componentsSeparatedByString:@"/"] lastObject];
    
    NSString *butTitlePorts = [NSString stringWithFormat:@"http://resource.cncgroup.net:8011/api/Books/favorite/book/%@/1/0",string];
    
    
    MBProgressHUD * _hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    
    [manager GET:butTitlePorts parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
    }
         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
             
             [_hud hideAnimated:YES];
             
             if ([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]] isEqualToString:@"22"]||
                 [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]] isEqualToString:@"77"]||
                 [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]] isEqualToString:@"99"]) {
                 
                 NSArray *arr = [NSArray arrayWithObject:responseObject];
                 
                 ScanDownloadViewController *vc = [[ScanDownloadViewController alloc] init];
                 
                 vc.dataArray = arr;
                 
                 vc.classID = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]];
                 
                 [vc setHidesBottomBarWhenPushed:YES];
                 
                 [self.navigationController pushViewController:vc animated:YES];
                 
             }else if ([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]] isEqualToString:@"55"] && [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"ResourceType"]] isEqualToString:@"10"]){
                 
                 NSArray *arr = [NSArray arrayWithObject:responseObject];
                 
                 ScanDownloadViewController *vc = [[ScanDownloadViewController alloc] init];
                 
                 vc.dataArray = arr;
                 
                 vc.classID = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]];
                 
                 vc.reType = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"ResourceType"]];
                 
                 [vc setHidesBottomBarWhenPushed:YES];
                 
                 [self.navigationController pushViewController:vc animated:YES];
                 
             }else{
                 
                 DownloadData *model = [[DownloadData alloc] init];
                 
                 model.Author = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Author"]];
                 
                 model.BookType = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"BookType"]];
                 
                 model.BookTypeName = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"BookTypeName"]];
                 
                 model.Class1 = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]];
                 
                 model.Class2 = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class2"]];
                 
                 model.CollectCount = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"CollectCount"]];
                 
                 model.CoverImageUrl = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"CoverImageUrl"]];
                 
                 model.CreatedOn = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"CreatedOn"]];
                 
                 model.HasContent = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"HasContent"]];
                 
                 model.ID = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"ID"]];
                 
                 model.MenuID = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"MenuID"]];
                 
                 model.Name = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Name"]];
                 
                 model.RecommendType = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"RecommendType"]];
                 
                 model.ResourceType = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"ResourceType"]];
                 
                 model.ResourceUrl = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"ResourceUrl"]];
                 
                 model.Summary = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Summary"]];
                 
                 model.Tag = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Tag"]];
                 
                 model.ViewCount = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"ViewCount"]];
                 
                 model.VolumeCount = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"VolumeCount"]];
                 
                 model.Volumes = [responseObject objectForKey:@"Volumes"];
                 
                 ScanDownloadViewController *vc = [[ScanDownloadViewController alloc] init];
                 
                 vc.dataArray = model.Volumes;
                 
                 vc.classID = model.Class1;
                 
                 [vc setHidesBottomBarWhenPushed:YES];
                 
                 [self.navigationController pushViewController:vc animated:YES];
                 
             }
             
         }
     
         failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull   error) {
             
             NSLog(@"%@",error);  //这里打印错误信息
             
             [_hud hideAnimated:YES];
             
         }];
    
}

#pragma mark - 搜索
-(void)searchClick:(UIButton *)button
{
    DJSearchVC *vc = [[DJSearchVC alloc] init];
    [vc setHidesBottomBarWhenPushed:YES];
    [self.navigationController pushViewController:vc animated:YES];
}


#pragma mark - 滚动视图
-(void)createScrollBanner
{
    UIScrollView *demoContainerView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, 180)];
    demoContainerView.contentSize = CGSizeMake(self.view.frame.size.width, 180);
    [self.view addSubview:demoContainerView];
   
    cycleScrollView = [SDCycleScrollView cycleScrollViewWithFrame:CGRectMake(0, 0, WIDTH, 180) delegate:nil placeholderImage:[UIImage imageNamed:@""]];
    
    cycleScrollView.infiniteLoop = YES;
    
    cycleScrollView.pageControlStyle = SDCycleScrollViewPageContolStyleClassic;
    
    [demoContainerView addSubview:cycleScrollView];
    
    [ZuyuBanner getBannerwithType:@"104" count:@"5" imageArray:^(NSArray *array) {
        cycleScrollView.imageURLStringsGroup = array;
    }];
}


#pragma mark - UICollection

-(void)createCollection
{
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    float cellWidth = WIDTH /3 - 0.1;
    float cellHeight = (HEIGHT - 74 - 180 - 44) / 3 - 0.1;
    layout.itemSize = CGSizeMake(cellWidth,cellHeight);
    layout.minimumInteritemSpacing = 0;
    layout.minimumLineSpacing = 0;
    layout.sectionInset = UIEdgeInsetsMake(0, 0, 0, 0);
    
    _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 74 + 180, WIDTH, HEIGHT - 74 - 180 - 44) collectionViewLayout:layout];
    _collectionView.backgroundColor = [UIColor whiteColor];
    [_collectionView registerClass:[PartyBuildingMainCell class] forCellWithReuseIdentifier:@"PartyBuildingMainCell"];
    _collectionView.delegate = self;
    _collectionView.dataSource = self;
    _collectionView.scrollEnabled = NO;
    [self.view addSubview:_collectionView];
    
}


- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return 9;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    PartyBuildingMainCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"PartyBuildingMainCell" forIndexPath:indexPath];
    
    cell.bgImage = [NSString stringWithFormat:@"gcdBGI%ld",indexPath.item + 1];
    cell.centerImage = [NSString stringWithFormat:@"gcdCenterImage%ld",indexPath.item + 1];
    cell.title  = @[@"习主席专栏",@"党建课程",@"党课收听",@"党政建设",@"党务工作",@"党建文库",@"党建活动",@"专家辅导",@"党建动态"][indexPath.item];
    return cell;
}


- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    if (indexPath.item == 2) {
        DJListenVC *vc = [[DJListenVC alloc ] init];
        vc.navTitle = @[@"习主席专栏",@"党建课程",@"党课收听",@"党政建设",@"党务工作",@"党建文库",@"党建活动",@"专家辅导",@"党建动态"][indexPath.item];
        
        vc.type = @[@"87",@"86",@"80",@"85",@"84",@"220101",@"83",@"82",@"81"][indexPath.item];
        
        NSInteger isSon = [@[@"87",@"86",@"80",@"85",@"84",@"220101",@"83",@"82",@"81"][indexPath.item] integerValue];
        
        if (isSon == 81 ||
            isSon == 80 ||
            isSon == 82 ||
            isSon == 83 ||
            isSon == 220101) {
            vc.isSonClass = YES;
        }
        vc.isdj = YES;
        [vc setHidesBottomBarWhenPushed:YES];
        [self.navigationController pushViewController:vc animated:YES];
    }else if (indexPath.item == 1 ||indexPath.item == 5 ) {
        
        DJListVC *vc = [[DJListVC alloc] init];
        vc.navTitle = @[@"习主席专栏",@"党建课程",@"党课收听",@"党政建设",@"党务工作",@"党建文库",@"党建活动",@"专家辅导",@"党建动态"][indexPath.item];
        
        vc.type = @[@"87",@"86",@"80",@"85",@"84",@"220101",@"83",@"82",@"81"][indexPath.item];
        
        NSInteger isSon = [@[@"87",@"86",@"80",@"85",@"84",@"220101",@"83",@"82",@"81"][indexPath.item] integerValue];
        
        if (isSon == 81 ||
            isSon == 80 ||
            isSon == 82 ||
            isSon == 83 ||
            isSon == 220101) {
            vc.isSonClass = YES;
        }
        vc.isdj = YES;
        [vc setHidesBottomBarWhenPushed:YES];
        [self.navigationController pushViewController:vc animated:YES];
        
       
    }else{
        DJNewsVC *vc = [[DJNewsVC alloc] init];
        vc.navTitle = @[@"习主席专栏",@"党建课程",@"党课收听",@"党政建设",@"党务工作",@"党建文库",@"党建活动",@"专家辅导",@"党建动态"][indexPath.item];
        
        vc.type = @[@"87",@"86",@"80",@"85",@"84",@"220101",@"83",@"82",@"81"][indexPath.item];
        
        NSInteger isSon = [@[@"87",@"86",@"80",@"85",@"84",@"220101",@"83",@"82",@"81"][indexPath.item] integerValue];
        
        if (isSon == 81 ||
            isSon == 80 ||
            isSon == 82 ||
            isSon == 83 ||
            isSon == 220101) {
            vc.isSonClass = YES;
        }
        vc.isdj = YES;
        [vc setHidesBottomBarWhenPushed:YES];
        [self.navigationController pushViewController:vc animated:YES];
    }
    
    
  
}

/*
 

 
 */
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark - nav 处理.
-(void)viewWillAppear:(BOOL)animated
{
    
    [super viewWillAppear:animated];
    for (UIView *subView in self.view.subviews) {
        if ([subView isKindOfClass:[MBProgressHUD class]]) {
            subView.hidden = YES;
        }
    }
    [self.navigationController setNavigationBarHidden:YES animated:NO];
    if (cycleScrollView) {
        [ZuyuBanner getBannerwithType:@"104" count:@"5" imageArray:^(NSArray *array) {
            cycleScrollView.imageURLStringsGroup = array;
        }];
    }
    
}


- (void) viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:NO];
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
