//
//  OnlineResouceVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/18.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "OnlineResouceVC.h"
#import "zuyu.h"
#import "OnlineSearchVC.h"
#import "QRCodeReaderViewController.h"
#import "SDCycleScrollView.h"
#import "OnlineCollctionHeaderButtonsView.h"
#import "OnlineHotCell.h"
#import "CreateCodeVC.h"
#import "MainCellListModel.h"
#import "MainThirdMP3ViewController.h"
#import "ResourcesListVC.h"
#import "ScanDownloadViewController.h"
#import "DownloadData.h"
#import "ResouceClassModel.h"
#import "EntityResourcesVC.h"
#import "SonEntityResouceVC.h"
#import "DHGuidePageHUD.h"
#import "OnlineResouceCell.h"
#import "MainMP4ViewController.h"
#import "SingleResourcesVC.h"
#import "MainWebViewController.h"
#import "OnlineFristResouceCell.h"
#import "MoreHotRecommendVC.h"
#import "ScanLoginVC.h"
#import "ListonScrollCell.h"
#import "MainSocendTableViewCell.h"
#import "LoadErrorCell.h"
#import "LoadErrorCellView.h"
#import "ZuyuCoustomRefreshFooter.h"

@interface OnlineResouceVC ()<UINavigationControllerDelegate,NavgationViewDelegate,QRCodeReaderDelegate,UICollectionViewDelegate,CollctionHeaderButtonsViewDelegate,FirstNavViewDelegate,DHGuidePageHUDDelegate,UITableViewDelegate,UITableViewDataSource,OnlineResouceCellDelegate,OnlineFristResouceCellDelegate,ListonScrollCellDelegate,SDCycleScrollViewDelegate,LoadErrorCellDelegate>
{
    UICollectionView *_collectionView;
    NSMutableArray *_dataArray;
    NSMutableArray *_classModelArray;
    OnlineCollctionHeaderButtonsView *_headerView;
    UITableView *_tableView;
    UIView *_header;
    NSMutableArray *_pageArray;
    /* 暂时放在这里 */
    UILabel *_scrollerLabel;
    
    //资源列表的数组
    NSMutableArray *_resouceListArray;
    
    LoadErrorCellView *_loadErrorView;
    
    SDCycleScrollView *cycleScrollView;
}


@end

@implementation OnlineResouceVC

#pragma mark - 引导页
#pragma mark - 设置APP静态图片引导页
- (void)setStaticGuidePage {
    self.tabBarController.tabBar.hidden = YES;
    
    NSArray *imageNameArray = @[@"ZCYD1",@"ZCYD2",@"ZCYD3"];
    DHGuidePageHUD *guidePage = [[DHGuidePageHUD alloc] dh_initWithFrame:self.view.frame imageNameArray:imageNameArray buttonIsHidden:NO];
    guidePage.slideInto = YES;
    guidePage.delegate = self;
    
    [self.navigationController.view addSubview:guidePage];
    
}

//转场动画结束回调
-(void)DoEndWelcomePage
{
    [self setNetWorkRequest];
    [self createNavgation];
    [self createTableView];
    [self requesData:1];
    [self requestDataOfTypeClass];
    self.tabBarController.tabBar.hidden = NO;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    
    _dataArray = [NSMutableArray array];
    _pageArray = [NSMutableArray array];
    _classModelArray = [NSMutableArray array];
    _resouceListArray = [NSMutableArray array];
    if(![[NSUserDefaults standardUserDefaults] boolForKey:@"firstLaunch"]){
        [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"firstLaunch"];
        [self setStaticGuidePage];
    }else{
        //不是第一次启动
        [self setNetWorkRequest];
        [self createNavgation];
        [self createTableView];
        [self requesData:1];
        [self requestDataOfTypeClass];
    }
    // Do any additional setup after loading the view.
}


#pragma setRequest
-(void)setNetWorkRequest
{
    
    [_dataArray removeAllObjects];
    [_pageArray removeAllObjects];

    for (int i = 0; i<6; i++) {
        NSArray *arr = [NSArray array];
        [_dataArray addObject:arr];
        [_pageArray addObject:@"0"];
        if (i) {
            [self classRequestDate:i withPage:@"0"];
        }
    }
    
}

#pragma mark - navgation
-(void)createNavgation
{
    FirstNavView *view = [[FirstNavView alloc] init];
    view.delegate = self;
    [self.view addSubview:view];
    
}

-(void)createTableView
{
    
    _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74 - 48) style:UITableViewStyleGrouped];
    _tableView.delegate = self;
    _tableView.dataSource = self;
    _tableView.estimatedRowHeight = 0;
    _tableView.estimatedSectionFooterHeight = 0;
    _tableView.estimatedSectionHeaderHeight = 0;
    _tableView.rowHeight = WIDTH/3 * 1.3 * 2 + 110;
    [self.view addSubview:_tableView];
    
    _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    _tableView.mj_header =  [MJRefreshNormalHeader headerWithRefreshingBlock:^{
//        [self setNetWorkRequest];
        [self requesData:1];
    }];
    
    _tableView.mj_footer  =  [ZuyuCoustomRefreshFooter footerWithRefreshingBlock:^{
        [_tableView.mj_footer endRefreshingWithNoMoreData];
    }];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.section) {
        if (_resouceListArray.count) {
            return 120;
        }
        return 300;
    }else{
        if (indexPath.row == 3) {
            return 230;
        }else if (indexPath.row) {
            return WIDTH/3 * 1.3  + 110;
        }else{
            return 220;
        }
    }
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    if (section) {
        if (_resouceListArray.count) {
            return _resouceListArray.count;
        }
        
        return 1;
    }
    return 6;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.section) {
        
        
        if (_resouceListArray.count) {
            MainSocendTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MainSocendTableViewCell"];
            
            if (cell == nil) {
                cell = [[MainSocendTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"MainSocendTableViewCell"];
            }
            
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            MainCellListModel *model = _resouceListArray[indexPath.row];
            
            cell.nameLable.text = model.name;
            
            cell.countryLable.text = model.author;
            
            cell.collectLable.text = [NSString stringWithFormat:@"%@人气",model.viewCount];
            
            cell.summaryLable.text = model.Summary;
            
            [cell.image sd_setImageWithURL:[NSURL URLWithString:model.imageName] placeholderImage:[ZuyuPlaceholderImage returnPlaceholder:1] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
                
            }];
            
            if (WIDTH == 320 ) {
                
                cell.nameLable.font = [UIFont systemFontOfSize:14.0];
                
                cell.collectLable.font = [UIFont systemFontOfSize:14.0];
                
                cell.summaryLable.font = [UIFont systemFontOfSize:14.0];
                
            }
            
            
            
            NSString *dataType = [NSString stringWithFormat:@"%@",model.ResourceType];
            
            if ([dataType isEqualToString:@"0"]) {
                //音
                cell.classTypeSign.image = [UIImage imageNamed:@"mp3Type"];
            }else if ([dataType isEqualToString:@"1"]||[dataType isEqualToString:@"6"] || [dataType isEqualToString:@"10"]){
                //视
                cell.classTypeSign.image = [UIImage imageNamed:@"mp4Type"];
                
            }else{
                //web
                cell.classTypeSign.image = [UIImage imageNamed:@"webType"];
                
            }
            
            return cell;
        }else{
            
            LoadErrorCell *cell = [tableView dequeueReusableCellWithIdentifier:@"LoadErrorCell"];
            
            if (cell == nil) {
                cell = [[LoadErrorCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"LoadErrorCell"];
            }
            
            cell.delegate = self;
            return cell;
        }
        
        
    }else{
        if (!indexPath.row) {
            OnlineFristResouceCell *cell = [tableView dequeueReusableCellWithIdentifier:@"OnlineFristResouceCell"];
            
            if (cell == nil) {
                cell = [[OnlineFristResouceCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"OnlineFristResouceCell"];
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.delegate = self;
            cell.dataArray = _dataArray[indexPath.row];
            cell.pageControl.frame = CGRectMake(0, 202, WIDTH, 20);
            return cell;
        }else if (indexPath.row == 3){
            
          
            
            ListonScrollCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ListonScrollCell"];
            if (cell == nil) {
                cell = [[ListonScrollCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"ListonScrollCell"];
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.index = indexPath;
            cell.dataArray = _dataArray[indexPath.row];
            cell.delegate = self;
            return cell;
        }else{
                OnlineResouceCell*  cell = [tableView dequeueReusableCellWithIdentifier:@"OnlineResouceCell"];
                
                if (cell == nil) {
                    cell = [[OnlineResouceCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"OnlineResouceCell"];
                }
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                cell.dataArray = _dataArray[indexPath.row];
                cell.index  = indexPath;
                cell.delegate  = self;
                return cell;
           
        }
    }
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section) {
        [self pushVC:_resouceListArray[indexPath.row]];
    }
}

- (nullable UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    if (section) {
        
        UIView *headView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 40)];
        
        headView.backgroundColor = [UIColor whiteColor];
        
        UIImageView *imageV = [[UIImageView alloc] initWithFrame:CGRectMake(15, 6, 10, 28)];
        
        imageV.image = [UIImage imageNamed:@"shu"];
        
        [headView addSubview:imageV];
        
        UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(33, 5, WIDTH - 200, 30)];
        
        titleLabel.text = @"资源列表";
        
        [headView addSubview:titleLabel];
        
        
        UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
        
        button.frame = CGRectMake(WIDTH - 120, 0, 120, 40);
        
        [button setTitle:@"查看更多" forState: UIControlStateNormal];
        
        [button setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
        
        [button addTarget:self action:@selector(resouceMoreClick:) forControlEvents:UIControlEventTouchUpInside];
        [headView addSubview:button];
        
        return  headView;
    }
    if (_header == nil) {
        _header = [[UIView alloc ] initWithFrame:CGRectMake(0, 0, WIDTH, 380)];
        
        UIScrollView *demoContainerView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 160)];
        demoContainerView.contentSize = CGSizeMake(self.view.frame.size.width, 180);
        [_header addSubview:demoContainerView];
        
        
        cycleScrollView = [SDCycleScrollView cycleScrollViewWithFrame:CGRectMake(0, 0, WIDTH, 160) delegate:self placeholderImage:[UIImage imageNamed:@""]];;
        
        cycleScrollView.infiniteLoop = YES;
        
        cycleScrollView.pageControlStyle = SDCycleScrollViewPageContolStyleClassic;
        
        [demoContainerView addSubview:cycleScrollView];
        
        _headerView = [[OnlineCollctionHeaderButtonsView alloc] init];
        
        _headerView.delegate = self;
        
        [_header addSubview:_headerView];
        
        _header.backgroundColor = LIONCOLOR;
        
        UIImageView *hotImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, 330, WIDTH, 40)];
        
        hotImage.image = [UIImage imageNamed:@"hot_recommended_title_bg"];
        [ZuyuBanner getBannerwithType:@"103" count:@"5" imageArray:^(NSArray *array) {
                cycleScrollView.imageURLStringsGroup = array;
                [_tableView reloadData];
        }];
        
        _loadErrorView = [[LoadErrorCellView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 380)];
        _loadErrorView.hidden = YES;
        
        UITapGestureRecognizer *tapGesturRecognizer=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(errorViewTouchUpInsind:)];
        
        [_loadErrorView addGestureRecognizer:tapGesturRecognizer];
        
        [_header addSubview:_loadErrorView];
        
        
        
    }
        return _header;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (section) {
        return 40;
    }
    return 340;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.1;
}

#pragma mark - 资源列表查看更多
-(void)resouceMoreClick:(UIButton *)button
{
    ResourcesListVC *vc = [[ResourcesListVC alloc] init];
    vc.navTitle = @"全部分类";
    vc.type = @"0";
    [vc setHidesBottomBarWhenPushed:YES];
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark - 跳转界面

-(void)pushVC:(MainCellListModel *)model
{
    NSString *dataType = [NSString stringWithFormat:@"%@",model.ResourceType];
    
    if ([dataType isEqualToString:@"0"]) {
        //音
        
        MainThirdMP3ViewController *thirdMp3Vc = [[MainThirdMP3ViewController alloc] init];
        
        thirdMp3Vc.Summary = model.Summary;
        
        thirdMp3Vc.tableListID = model.parameterID;
        
        thirdMp3Vc.MenuID = model.MenuID;
        
        thirdMp3Vc.thirdTitle = model.name;
        
        thirdMp3Vc.writer = model.author;
        
        thirdMp3Vc.viewCount = model.viewCount;
        
        thirdMp3Vc.imageName = model.imageName;
        
        thirdMp3Vc.BookTypeName = model.bookTypeName;
        
        thirdMp3Vc.VolumeCount = model.VolumeCount;
        
        thirdMp3Vc.BookID = model.ID;
        
        [thirdMp3Vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController:thirdMp3Vc animated:YES];
        
    }else if ([dataType isEqualToString:@"1"]||[dataType isEqualToString:@"6"]){
        //视
        MainMP4ViewController *vc = [[MainMP4ViewController alloc] init];
        
        vc.BookID = model.parameterID;
        
        [vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController:vc animated:YES];
        
    }else if ([dataType isEqualToString:@"10"]){
        //无册视频
        SingleResourcesVC *vc = [[SingleResourcesVC alloc] init];
        
        vc.titleStr = model.name;
        
        vc.rscourceUrl = model.ResourceUrl;
        
        //        vc.classType = self.type;
        
        [vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController:vc animated:YES];
        
    }else{
        
        MainWebViewController *vc = [[MainWebViewController alloc] init];
        
        vc.webUrl = [NSString  stringWithFormat:@"%@",model.ResourceUrl];
        vc.name = model.name;
        [vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController:vc animated:YES];
        
    }

    
}

#pragma mark - cell delegate
#pragma mark - 第一个 cell 查看更多
-(void)hotRecommendMoreClick
{
    
    MoreHotRecommendVC *vc = [[MoreHotRecommendVC alloc] init];
    [vc setHidesBottomBarWhenPushed:YES];
    [self.navigationController pushViewController:vc animated:YES];
    
}
#pragma mark - 查看更多
-(void)moreClickThing:(NSInteger )type
{
    
        NSArray *typeArray = [NSArray arrayWithObjects:@"0",
                         @"22",
                         @"55",
                         @"44",
                         @"88",
                         @"66",
                         nil];
        
        NSArray *titleArray = [NSArray arrayWithObjects:@"全部分类",
                              @"电子图书",
                              @"有声图书",
                              @"中华戏曲",
                              @"国学分馆",
                              @"传世名曲",
                              nil];
    
            ResourcesListVC *vc = [[ResourcesListVC alloc] init];
            vc.navTitle = titleArray[type];
            vc.type = typeArray[type];
        
            [vc setHidesBottomBarWhenPushed:YES];
            [self.navigationController pushViewController:vc animated:YES];
   
    
}
#pragma mark - 精彩收听轮播图点击事件
-(void)listonScrollCellViewDidSelectItemAtIndexPath:(MainCellListModel *)model
{
    [self pushVC:model];
}

#pragma mark - 加载失败,重新加载
- (void)resouceLoadErrorTouchUpInsind:(NSInteger)type
{
    [_pageArray replaceObjectAtIndex:type withObject:@"0"];
    [self classRequestDate:type withPage:_pageArray[type]];

}

- (void)listonScrollCellResouceLoadError:(NSInteger)type
{
    [_pageArray replaceObjectAtIndex:type withObject:@"0"];
    [self classRequestDate:type withPage:_pageArray[type]];
}

- (void)fristCellLoadResouceError:(NSInteger)type
{
    [self requesData:0];
}

-(void)errorViewTouchUpInsind:(id)tap
{
    _loadErrorView.hidden = YES;
    [self requestDataOfTypeClass];
    [ZuyuBanner getBannerwithType:@"103" count:@"5" imageArray:^(NSArray *array) {
        cycleScrollView.imageURLStringsGroup = array;
        [_tableView reloadData];
    }];
}

-(void)loadErrorCellRefresh
{
    [self requesData:2];
}
#pragma mark - 换一批
-(void)pageClickThing:(NSInteger )type
{
    
    NSInteger page = [_pageArray[type] integerValue] + 1 ;
    
    if (page == 5) {
        page = 0;
    }
    
    [_pageArray replaceObjectAtIndex:type withObject:[NSString stringWithFormat:@"%ld",page]];
    
    [self classRequestDate:type withPage:_pageArray[type]];

    NSLog(@"---->>>>%ld",type);
}
#pragma mark - 热门推荐数据点击
-(void)hotRecommendCollectionDidSelect:(MainCellListModel *)model
{
    [self pushVC:model];
}

#pragma mark - cell 上的 collection 的 click
-(void)collectionViewDidSelectItemAtIndexPath:(MainCellListModel *)model withType:(NSInteger )type
{
    [self pushVC:model];
    NSLog(@"---->>>>%ld",type);
}


-(void)classRequestDate:(NSInteger )type withPage:(NSString *)page
{
    
    NSArray *typeArray = [NSArray arrayWithObjects:@"88",
                          @"22",
                          @"55",
                          @"44",
                          @"88",
                          @"66",nil];
    
    
    MBProgressHUD  *_hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
    
   NSString *hotCellTitlePorts =[NSString stringWithFormat:@"http://resource.cncgroup.net:8020/api/books/subtypes/%@/books?pageIndex=%@&pageSize=3",typeArray[type],page];
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    [manager GET:hotCellTitlePorts parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        NSMutableArray *arr= [NSMutableArray array];
        
        for (NSDictionary *itemDict in responseObject) {
            
            MainCellListModel *model = [[MainCellListModel alloc] init];
            model.ID = [itemDict objectForKey:@"ID"];
            model.name = [itemDict objectForKey:@"Name"];
            model.imageName = [itemDict objectForKey:@"CoverImageUrl"];
            model.bookTypeName = [itemDict objectForKey:@"BookTypeName"];
            model.author = [itemDict objectForKey:@"Author"];
            model.viewCount = [itemDict objectForKey:@"ViewCount"];
            model.parameterID = [itemDict objectForKey:@"ID"];
            model.Summary = [itemDict objectForKey:@"Summary"];
            model.ResourceUrl =[itemDict objectForKey:@"ResourceUrl"];
            model.VolumeCount = [ZuyuJsonRead jsonRead:itemDict WithKey:@"VolumeCount"];
//            [itemDict objectForKey:@"VolumeCount"];
            model.ResourceType = [itemDict objectForKey:@"ResourceType"];
            model.MenuID =[itemDict objectForKey:@"MenuID"];
            model.CreatedOn =[itemDict objectForKey:@"CreatedOn"];
            
            [arr addObject:model];
        }
        
        [_dataArray replaceObjectAtIndex:type withObject:arr];
        
        NSIndexPath *indexPath = [NSIndexPath indexPathForRow:type inSection:0];
       
        [_tableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];
        
        [_hud hideAnimated:YES];
        
        if (type == 3) {
            [_tableView.mj_header endRefreshing];
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        _hud.mode = MBProgressHUDModeText;
        _hud.label.text = NSLocalizedString(@"网络异常，请稍后重试", @"HUD message title");
        [_hud hideAnimated:YES afterDelay: 1];
        
        if (type == 3) {
            [_tableView.mj_header endRefreshing];
        }
    }];
}

#pragma mark - createCollectionView
//-(void)createCollectionView
//{
//    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
//    float cellWidth = WIDTH /4;
//    layout.itemSize = CGSizeMake(cellWidth,cellWidth * 1.3 + 50);
//    layout.minimumInteritemSpacing = 0;
//    layout.minimumLineSpacing = 0;
//    layout.sectionInset = UIEdgeInsetsMake(0, 0, 0, 0);
//
//    _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74) collectionViewLayout:layout];
//    _collectionView.backgroundColor = [UIColor whiteColor];
//    [_collectionView registerClass:[OnlineHotCell class] forCellWithReuseIdentifier:@"OnlineHotCell"];
//    _collectionView.delegate = self;
//    _collectionView.dataSource = self;
//    [self.view addSubview:_collectionView];
//
//    [_collectionView registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header"];
//
//    _collectionView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"hot_recommended_list_bg"]];
    
//}
//
//- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
//{
//    return _dataArray.count;
//}
//
//- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
//{
//    OnlineHotCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"OnlineHotCell" forIndexPath:indexPath];
//
//    MainCellListModel *model = _dataArray[indexPath.item];
//
//    cell.bookImage = model.imageName;
//    cell.bookNameStr = model.name;
//    cell.writerNameStr = model.author;
//
//    return cell;
//}



//-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
//{
//    MainCellListModel *model = _dataArray[indexPath.item];
//
//    MainThirdMP3ViewController *thirdMp3Vc = [[MainThirdMP3ViewController alloc] init];
//    
//    thirdMp3Vc.Summary = model.Summary;
//    
//    thirdMp3Vc.tableListID = model.parameterID;
//    
//    thirdMp3Vc.MenuID = model.MenuID;
//    
//    thirdMp3Vc.butTag = indexPath.row % 8 + 100;
//    
//    thirdMp3Vc.thirdTitle = model.name;
//    
//    thirdMp3Vc.writer = model.author;
//    
//    thirdMp3Vc.viewCount = model.viewCount;
//    
//    thirdMp3Vc.imageName = model.imageName;
//    
//    thirdMp3Vc.BookTypeName = model.bookTypeName;
//    
//    thirdMp3Vc.VolumeCount = model.VolumeCount;
//    
//    thirdMp3Vc.BookID = model.ID;
//    
//    thirdMp3Vc.isHot = YES;
//    
//    [thirdMp3Vc setHidesBottomBarWhenPushed:YES];
//    
//    [self.navigationController pushViewController:thirdMp3Vc animated:YES];
//}



//
//- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForHeaderInSection:(NSInteger)section {
//    return CGSizeMake(WIDTH, 370);
//}
//
//- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
//{
//    UICollectionReusableView *header = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header" forIndexPath:indexPath];
//
//    UIScrollView *demoContainerView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 160)];
//    demoContainerView.contentSize = CGSizeMake(self.view.frame.size.width, 180);
//    [header addSubview:demoContainerView];
//
//    NSArray *imageNames = @[@"MainSecondVC1.jpg",
//                            @"MainSecondVC2.jpg",
//                            @"MainSecondVC3.png",
//                            @"MainSecondVC4.png",
//                            @"MainSecondVC5.png",
//                            ];
//
//    SDCycleScrollView *cycleScrollView = [SDCycleScrollView cycleScrollViewWithFrame:CGRectMake(0, 0, WIDTH, 160) imageNamesGroup:imageNames];
//
//    cycleScrollView.infiniteLoop = YES;
//
//    cycleScrollView.pageControlStyle = SDCycleScrollViewPageContolStyleClassic;
//
//    [demoContainerView addSubview:cycleScrollView];
//
//    if (_headerView == nil) {
//
//        _headerView = [[OnlineCollctionHeaderButtonsView alloc] init];
//
//        _headerView.delegate = self;
//
//        [header addSubview:_headerView];
//    }
//
//    header.backgroundColor = LIONCOLOR;
//
//    UIImageView *hotImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, 330, WIDTH, 40)];
//
//    hotImage.image = [UIImage imageNamed:@"hot_recommended_title_bg"];
//
//    UIImageView *hotTitleImage = [[UIImageView alloc] initWithFrame:CGRectMake(10, 6, 85, 28)];
//
//    hotTitleImage.image = [UIImage imageNamed:@"hot_recommended"];
//
//    [hotImage addSubview:hotTitleImage];
//
//    [header addSubview:hotImage];
//
//    return header;
//}

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
            
            NSString *type = [resultAsString substringToIndex:4];
            
            if ([type isEqualToString:@"http"]) {
                [wSelf requestScanDate:resultAsString];
            }else if ([resultAsString containsString:@"EquipmentCode"]||[resultAsString containsString:@"Type"]){
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
-(void)firstForSearch
{
    OnlineSearchVC *vc = [[OnlineSearchVC alloc] init];
    
    vc.hidesBottomBarWhenPushed = YES;
    
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma marl - 生成二维码
-(void)firstForCode
{
    CreateCodeVC *vc  = [[CreateCodeVC alloc] init];
    
    vc.hidesBottomBarWhenPushed = YES;
    
    [self.navigationController pushViewController:vc animated:YES];
    NSLog(@"------>>>>>>>生成二维码");
}

#pragma mark - buttonsClick
-(void)HeaderButtonsClick:(ResouceClassModel *)model
{
    
    if ([model.typeID isEqualToString:@"101"]) {
        
        SonEntityResouceVC *vc = [[SonEntityResouceVC alloc] init];
        [vc setHidesBottomBarWhenPushed:YES];
        [self.navigationController pushViewController:vc animated:YES];
    
    }else{
        
        ResourcesListVC *vc = [[ResourcesListVC alloc] init];
        vc.navTitle = model.title;
        vc.type = model.typeID;
        if ([model.typeID isEqualToString:@"100"]) {
            vc.type = @"0";
        }
        [vc setHidesBottomBarWhenPushed:YES];
        [self.navigationController pushViewController:vc animated:YES];
    }
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - network
//stuck 为1时.请求 list 和 fristdata. 为0时只请求 fristdata  为2时只请求list
-(void)requesData:(NSInteger )stuck
{
    [_resouceListArray removeAllObjects];
    
    MBProgressHUD * _hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    [manager GET:@"http://resource.cncgroup.net:8015/api/books/subtypes/0/books/recommend?pageIndex=0&pageSize=6" parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
      
        NSMutableArray *data = [NSMutableArray array];
        
        int i = 0;
        
            for (NSDictionary *itemDict in responseObject) {
            
            MainCellListModel *model = [[MainCellListModel alloc] init];
            model.ID = [itemDict objectForKey:@"ID"];
            model.name = [itemDict objectForKey:@"Name"];
            model.imageName = [itemDict objectForKey:@"CoverImageUrl"];
            model.bookTypeName = [itemDict objectForKey:@"BookTypeName"];
            model.author = [itemDict objectForKey:@"Author"];
            model.viewCount = [itemDict objectForKey:@"ViewCount"];
            model.parameterID = [itemDict objectForKey:@"ID"];
            model.Summary = [itemDict objectForKey:@"Summary"];
            model.ResourceUrl =[itemDict objectForKey:@"ResourceUrl"];
            model.VolumeCount =[ZuyuJsonRead jsonRead:itemDict WithKey:@"VolumeCount"];
            model.ResourceType = [itemDict objectForKey:@"ResourceType"];
            model.MenuID =[itemDict objectForKey:@"MenuID"];
            model.CreatedOn =[itemDict objectForKey:@"CreatedOn"];
                
               
                if (stuck == 1) {
                    [_resouceListArray addObject:model];
                    if (i<4) {
                        [data addObject:model];
                    }
                }else if(stuck == 0){
                    if (i<4) {
                        [data addObject:model];
                    }
                }else{
                    [_resouceListArray addObject:model];
                }
                i++;
        }
        [_tableView.mj_header endRefreshing];
        [_hud hideAnimated:YES];
        [_dataArray replaceObjectAtIndex:0 withObject:data];
        [_tableView reloadData];
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        NSLog(@"%@",error);
        
        _hud.mode = MBProgressHUDModeText;
        _hud.label.text = NSLocalizedString(@"网络差,稍后再试", @"HUD message title");
        [_hud hideAnimated:YES afterDelay: 1];
        [_tableView.mj_header endRefreshing];

//        [_hud hideAnimated:YES];

    }];
}


#pragma mark - 扫描结果的数据请求
-(void)requestScanDate:(NSString *)port
{
    
    NSString *string  =  [[[NSString stringWithFormat:@"%@",port] componentsSeparatedByString:@"/"] lastObject];
    
    NSString *butTitlePorts = [NSString stringWithFormat:@"http://resource.cncgroup.net:8011/api/Books/favorite/book/%@/1/0",string];
    
   MBProgressHUD * _hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];

    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    [manager GET:butTitlePorts parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
    }
         success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
             
             [_hud hideAnimated:YES];
             
             if ([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"BookType"]] isEqualToString:@"220101"]) {
                 NSArray *arr = [NSArray arrayWithObject:responseObject];

                 ScanDownloadViewController *vc = [[ScanDownloadViewController alloc] init];

                 vc.dataArray = arr;

                 vc.classID = @"79";

                 [vc setHidesBottomBarWhenPushed:YES];

                 [self.navigationController pushViewController:vc animated:YES];
             }else
                 if ([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]] isEqualToString:@"22"]||
                 [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]] isEqualToString:@"77"]||
                 [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Class1"]] isEqualToString:@"99"]){
                 
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

#pragma mark - 电子资源分类
-(void)requestDataOfTypeClass
{
    
    [_classModelArray removeAllObjects];
    
    MBProgressHUD * _hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    NSDictionary *parameters = [NSDictionary dictionaryWithObjectsAndKeys:@"true",@"IsHome" ,nil];
    
    [manager POST:@"http://cnc242.cncgroup.net/appapi/Category/GetActiveList" parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        NSArray *dataArr = [responseObject objectForKey:@"Data"];
        
        for (NSDictionary *dic in dataArr) {
            ResouceClassModel *model = [[ResouceClassModel alloc] init];
            model.image = [ZuyuJsonRead jsonRead:dic WithKey:@"IconUrl"];
            model.title = [ZuyuJsonRead jsonRead:dic WithKey:@"CategoryName"];
            model.typeID = [ZuyuJsonRead jsonRead:dic WithKey:@"CategoryId"];
            [_classModelArray addObject:model];
        }
        _headerView.dataArray = _classModelArray;
        
        [_hud hideAnimated:YES];
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"%@",error);
        _loadErrorView.hidden = NO;
        [_hud hideAnimated:YES];

    }];
}


#pragma mark - nav 处理.
-(void)viewWillAppear:(BOOL)animated
{
    
    for (UIView *subView in self.view.subviews) {
        if ([subView isKindOfClass:[MBProgressHUD class]]) {
            subView.hidden = YES;
        }
    }
    
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:NO];
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
