//
//  EntityResourcesVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/18.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "EntityResourcesVC.h"
#import "OnlineNavgationView.h"
#import "OnlineSearchVC.h"
#import "QRCodeReaderViewController.h"
#import "SDCycleScrollView.h"
#import "OnlineCollctionHeaderButtonsView.h"
#import "OnlineHotCell.h"
#import "CreateCodeVC.h"
#import "EntityResourcesModel.h"
#import "zuyu.h"
#import "EntitydetailVC.h"
#import "ScanDownloadViewController.h"
#import "DownloadData.h"
#import "EntitySearchVC.h"
#import "ScanLoginVC.h"

@interface EntityResourcesVC ()<UINavigationControllerDelegate,NavgationViewDelegate,QRCodeReaderDelegate,UICollectionViewDelegate,UICollectionViewDataSource,CollctionHeaderButtonsViewDelegate,FirstNavViewDelegate>
{
    UICollectionView *_collectionView;
    //热门按钮和横线
    UIButton *_hotButton;
    UILabel  *_hotLabel;
    ///推荐按钮和横线
    UIButton *_RecomButton;
    UILabel  *_RecomLabel;
    //展示的数据
    NSMutableArray *_dataArray;
    //热门数据
    NSMutableArray *_hotDataArray;
    //推荐数据
    NSMutableArray *_recomDataArray;
    //用来记录当前是热门还是推荐 1是热门  2是推荐
    NSInteger       _hotOrRecom;

}

@end

@implementation EntityResourcesVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createNavgation];
    [self createCollectionView];
    [self initArray];
    [self requesHotData];
    [self requesRecommendData];
    // Do any additional setup after loading the view.
}


-(void)initArray
{
    _dataArray = [NSMutableArray array];
    _hotDataArray = [NSMutableArray array];
    _recomDataArray = [NSMutableArray array];
    _hotOrRecom = 1;
}


#pragma mark - navgation
-(void)createNavgation
{
    FirstNavView *view = [[FirstNavView alloc] init];
    view.delegate = self;
    [self.view addSubview:view];
}
#pragma mark - 扫一扫
-(void)firstForScan
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

#pragma mark - 搜索
-(void)firstForSearch
{
    
    EntitySearchVC *vc = [[EntitySearchVC alloc] init];
    
    vc.hidesBottomBarWhenPushed = YES;
    
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma marl - 生成二维码
-(void)firstForCode
{
    CreateCodeVC *vc  = [[CreateCodeVC alloc] init];
    
    vc.hidesBottomBarWhenPushed = YES;
    
    [self.navigationController pushViewController:vc animated:YES];
    NSLog(@"------>>>>>>>..生成二维码");
}


#pragma mark - createCollectionView
-(void)createCollectionView
{
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    float cellWidth = WIDTH /4;
    layout.itemSize = CGSizeMake(cellWidth,cellWidth * 1.3 + 50);
    layout.minimumInteritemSpacing = 0;
    layout.minimumLineSpacing = 0;
    layout.sectionInset = UIEdgeInsetsMake(0, 0, 0, 0);
    
    _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74) collectionViewLayout:layout];
    _collectionView.backgroundColor = [UIColor whiteColor];
    [_collectionView registerClass:[OnlineHotCell class] forCellWithReuseIdentifier:@"OnlineHotCell"];
    _collectionView.delegate = self;
    _collectionView.dataSource = self;
    [self.view addSubview:_collectionView];
    
    [_collectionView registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header"];
    
     _collectionView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"hot_recommended_list_bg"]];
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return _dataArray.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    OnlineHotCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"OnlineHotCell" forIndexPath:indexPath];
    
    EntityResourcesModel *model = _dataArray[indexPath.item];
    cell.bookImage = model.LogoUrl;
    NSLog(@"%@-->>>>>",model.LogoUrl);
    cell.bookNameStr = model.BookName;
    cell.writerNameStr = model.BookWriter;
    
    return cell;
}



-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    EntityResourcesModel *model = _dataArray[indexPath.row];
    NSLog(@"%@ -- %@",model.BookName,model.Id);
    
    EntitydetailVC *vc = [[EntitydetailVC alloc] init];
    
    vc.model = model;
    
    [vc setHidesBottomBarWhenPushed:YES];
        
    [self.navigationController pushViewController:vc animated:YES];
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForHeaderInSection:(NSInteger)section {
    return CGSizeMake(WIDTH, 210);
}

- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
{
    UICollectionReusableView *header = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header" forIndexPath:indexPath];
    
    UIScrollView *demoContainerView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 160)];
    demoContainerView.contentSize = CGSizeMake(self.view.frame.size.width, 180);
    [header addSubview:demoContainerView];
    
    SDCycleScrollView *cycleScrollView = [SDCycleScrollView cycleScrollViewWithFrame:CGRectMake(0, 0, WIDTH, 160) delegate:nil placeholderImage:[UIImage imageNamed:@""]];
    
    cycleScrollView.infiniteLoop = YES;
    
    cycleScrollView.pageControlStyle = SDCycleScrollViewPageContolStyleClassic;
    
    [demoContainerView addSubview:cycleScrollView];
    
    
    [ZuyuBanner getBannerwithType:@"102" count:@"5" imageArray:^(NSArray *array) {
        cycleScrollView.imageURLStringsGroup = array;
    }];
    
    _hotButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    _hotButton.frame = CGRectMake(20, 160, WIDTH/2 - 40, 50);
    
    [_hotButton setTitle:@"热门" forState: UIControlStateNormal];
    
    [_hotButton setTitleColor:[UIColor orangeColor] forState:UIControlStateNormal];
    
    [_hotButton addTarget:self action:@selector(hotClick:) forControlEvents:UIControlEventTouchUpInside];
    
    _hotButton.backgroundColor = [UIColor whiteColor];

    [header addSubview:_hotButton];
    
    _RecomButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    _RecomButton.frame = CGRectMake(WIDTH/2 + 20, 160, WIDTH/2 - 40, 50);
    
    [_RecomButton setTitle:@"推荐" forState: UIControlStateNormal];
    
    [_RecomButton addTarget:self action:@selector(recomClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [_RecomButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];

    _RecomButton.backgroundColor = [UIColor whiteColor];
    
    [header addSubview:_RecomButton];
    
    header.backgroundColor = [UIColor whiteColor];
    
    if (_hotOrRecom == 1) {
        
        [_hotButton setImage:[UIImage imageNamed:@"paper_hot_nav_active"] forState:UIControlStateNormal];
        [_RecomButton setImage:[UIImage imageNamed:@"paper_recoment_nav"] forState:UIControlStateNormal];
    }else{
        [_hotButton setImage:[UIImage imageNamed:@"paper_hot_nav"] forState:UIControlStateNormal];
        [_RecomButton setImage:[UIImage imageNamed:@"paper_recoment_nav_active"] forState:UIControlStateNormal];
    }
    
    return header;
}

#pragma mark - 热门按钮点击
-(void)hotClick:(UIButton *)button
{
    _hotOrRecom = 1;
    
    _dataArray = _hotDataArray;
    
    [_collectionView reloadData];
}

#pragma mark - 推荐按钮点击
-(void)recomClick:(UIButton *)button
{
    _hotOrRecom = 2;
    
    _dataArray = _recomDataArray;
    
    [_collectionView reloadData];
}


#pragma mark - network


-(void)requesHotData
{
//    MBProgressHUD*_hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
//
//    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
//
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    NSString *HeaderField = [NSString stringWithFormat:@"Bearer %@",TOKEN];
    
    [manager.requestSerializer setValue:HeaderField  forHTTPHeaderField:@"Authorization"];
    NSDictionary *parameter = [NSDictionary dictionaryWithObjectsAndKeys:@"0",@"Top",nil];
    
    [manager POST:PORT(@"InteBook/GetHotList") parameters:parameter progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
            
            for (NSDictionary *itme in [responseObject objectForKey:@"Data"]) {
                
                EntityResourcesModel *model = [[EntityResourcesModel alloc] init];
                
                model.BookName = [ZuyuJsonRead jsonRead:itme WithKey:@"BookName"];
                model.Id = [ZuyuJsonRead jsonRead:itme WithKey:@"Id"];
                model.BookWriter = [ZuyuJsonRead jsonRead:itme WithKey:@"Author"];
                model.PublishCompany = [ZuyuJsonRead jsonRead:itme WithKey:@"PublishCompany"];
                model.LogoUrl = [ZuyuJsonRead jsonRead:itme WithKey:@"LogoUrl"];
                model.Describe = [ZuyuJsonRead jsonRead:itme WithKey:@"Describe"];
                model.BespeakCount = [ZuyuJsonRead jsonRead:itme WithKey:@"BespeakCount"];
                model.BorrowCount = [ZuyuJsonRead jsonRead:itme WithKey:@"BorrowCount"];
                model.ResourceUrl = [ZuyuJsonRead jsonRead:itme WithKey:@"ResourceUrl"];
                model.CreateTime = [ZuyuJsonRead jsonRead:itme WithKey:@"CreateTime"];
                
                model.CreateId = [ZuyuJsonRead jsonRead:itme WithKey:@"CreateId"];
                
                [_hotDataArray addObject:model];
                
            }
            _dataArray = _hotDataArray;
            [_collectionView reloadData];
//            [_hud hideAnimated:YES];
            
        }else if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Code"]].integerValue == 2001){
            
            [ZuyuAlertShow alertShow:@"账号不存在" viewController: self];
            
            [[NSUserDefaults standardUserDefaults] setObject:@"0" forKey:@"isLogin"];
            [[NSUserDefaults standardUserDefaults] synchronize];
//            [self.navigationController popViewControllerAnimated:YES];
            
        }else{
//            _hud.mode = MBProgressHUDModeText;
//            _hud.label.text = NSLocalizedString([ZuyuJsonRead jsonRead:responseObject WithKey:@"Message"], @"HUD message title");
//            [_hud hideAnimated:YES afterDelay:1];
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        NSHTTPURLResponse * responses = (NSHTTPURLResponse *)task.response;

        if (responses.statusCode == 401) {
            
//            [_hud hideAnimated:YES ];
            
            [ZuyuTokenRefresh tokenRefreshSuccess:^(NSURLSessionDataTask * _Nonnull dataTask, id  _Nullable responseObject) {
                
                [self requesHotData];
                
            } failure:^(NSURLSessionDataTask * _Nullable dataTask, NSError * _Nonnull error) {
                
                
            }];
            
            
        }else{
            
//            _hud.mode = MBProgressHUDModeText;
//            _hud.label.text = NSLocalizedString(@"网络错误,请稍后再试", @"HUD message title");
//            _hud.label.numberOfLines = 0;
//            [_hud hideAnimated:YES afterDelay:1];
//            NSLog(@"%@",error);

        }
    }];
}


-(void)requesRecommendData
{
    MBProgressHUD*_hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    NSString *HeaderField = [NSString stringWithFormat:@"Bearer %@",TOKEN];
    
    [manager.requestSerializer setValue:HeaderField  forHTTPHeaderField:@"Authorization"];
    NSDictionary *parameter = [NSDictionary dictionaryWithObjectsAndKeys:@"0",@"Top",nil];
    
    [manager POST:PORT(@"InteBook/GetRecommendList") parameters:parameter progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
            
            for (NSDictionary *itme in [responseObject objectForKey:@"Data"]) {
                
                EntityResourcesModel *model = [[EntityResourcesModel alloc] init];
                
                model.BookName = [ZuyuJsonRead jsonRead:itme WithKey:@"BookName"];
                model.Id = [ZuyuJsonRead jsonRead:itme WithKey:@"Id"];
                model.BookWriter = [ZuyuJsonRead jsonRead:itme WithKey:@"Author"];
                model.PublishCompany = [ZuyuJsonRead jsonRead:itme WithKey:@"PublishCompany"];
                model.LogoUrl = [ZuyuJsonRead jsonRead:itme WithKey:@"LogoUrl"];
                model.Describe = [ZuyuJsonRead jsonRead:itme WithKey:@"Describe"];
                model.BespeakCount = [ZuyuJsonRead jsonRead:itme WithKey:@"BespeakCount"];
                model.BorrowCount = [ZuyuJsonRead jsonRead:itme WithKey:@"BorrowCount"];
                model.ResourceUrl = [ZuyuJsonRead jsonRead:itme WithKey:@"ResourceUrl"];
                model.CreateTime = [ZuyuJsonRead jsonRead:itme WithKey:@"CreateTime"];
                
                model.CreateId = [ZuyuJsonRead jsonRead:itme WithKey:@"CreateId"];
                
                [_recomDataArray addObject:model];
                
            }
            [_collectionView reloadData];
            [_hud hideAnimated:YES];
            
        }else if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Code"]].integerValue == 2001){
            
            [ZuyuAlertShow alertShow:@"账号不存在" viewController: self];
            
            [[NSUserDefaults standardUserDefaults] setObject:@"0" forKey:@"isLogin"];
            [[NSUserDefaults standardUserDefaults] synchronize];
//            [self.navigationController popViewControllerAnimated:YES];
            
        }else{
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString([ZuyuJsonRead jsonRead:responseObject WithKey:@"Message"], @"HUD message title");
            [_hud hideAnimated:YES afterDelay:1];
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        NSHTTPURLResponse * responses = (NSHTTPURLResponse *)task.response;
        
        if (responses.statusCode == 401) {
            
            [_hud hideAnimated:YES ];
            
            [ZuyuTokenRefresh tokenRefreshSuccess:^(NSURLSessionDataTask * _Nonnull dataTask, id  _Nullable responseObject) {
                
                [self requesHotData];
                
            } failure:^(NSURLSessionDataTask * _Nullable dataTask, NSError * _Nonnull error) {
                
                
            }];
            
            
        }else{
            
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"网络错误,请稍后再试", @"HUD message title");
            _hud.label.numberOfLines = 0;
            [_hud hideAnimated:YES afterDelay:1];
            NSLog(@"%@",error);
            
        }
    }];
}




#pragma mark - 扫一扫
-(void)navScan
{
    
    if ([ISLOGIN integerValue]) {
        QRCodeReaderViewController *reader = [QRCodeReaderViewController new];
        
        reader.modalPresentationStyle = UIModalPresentationFormSheet;
        
        reader.delegate = self;
        
        __weak typeof (self) wSelf = self;
        
        [reader setCompletionWithBlock:^(NSString *resultAsString) {
            
            [wSelf.navigationController popViewControllerAnimated:YES];
            
            NSString *type = [resultAsString substringToIndex:4];
            NSLog(@"%@,type",type)
            
            if ([type isEqualToString:@"http"]) {
                [wSelf requestScanDate:resultAsString];
            }else if ([type containsString:@"Ty"]){
                [self scanForLogin:[self dictionaryWithJsonString:resultAsString]];
                
            }else{
                [ZuyuAlertShow alertShow:resultAsString viewController: self];
            }
            
        }];
        
        [self.navigationController pushViewController:reader animated:YES];
    }else{
        [ZuyuAlertShow alertShow:@"请先登录" viewController: self];
    }
    
}

#pragma mark - 扫码登录
-(void)scanForLogin:(NSDictionary *)parameters
{
    ScanLoginVC *vc = [[ScanLoginVC alloc] init];
    vc.parameters = parameters;
    [self presentViewController:vc  animated:YES completion:nil];
}


#pragma mark - json 转 字典
- (NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString
{
    if (jsonString == nil) {
        return nil;
    }
    
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *err;
    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData
                                                        options:NSJSONReadingMutableContainers error:&err];
    
    if(err) {
        NSLog(@"json解析失败：%@",err);
        return nil;
    }
    return dic;
}

#pragma mark - 搜索
-(void)navSearch
{
    EntitySearchVC *vc = [[EntitySearchVC alloc] init];
    
    vc.hidesBottomBarWhenPushed = YES;
    
    [self.navigationController pushViewController:vc animated:NO];
}

#pragma marl - 生成二维码
-(void)navCreateCode
{
    CreateCodeVC *vc  = [[CreateCodeVC alloc] init];
    
    vc.hidesBottomBarWhenPushed = YES;
    
    [self.navigationController pushViewController:vc animated:YES];
    NSLog(@"------>>>>>>>..生成二维码");
}
#pragma mark - system void 

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
}


- (void) viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:NO];
}

#pragma mark - 扫描结果的数据请求
-(void)requestScanDate:(NSString *)port
{
    
    if([port rangeOfString:@":8011"].location !=NSNotFound){
        port = [NSString stringWithFormat:@"%@/1/0",port];
    }
    else
    {
        NSMutableString *ports = [NSMutableString stringWithFormat:@"%@",port];
        
        [ports insertString:@":8011" atIndex:21];
        
        port = [NSString stringWithFormat:@"%@/1/0",ports];
        
    }
    
    MBProgressHUD * _hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    NSLog(@"%@",port);
    
    [manager GET:port parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
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
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
