//
//  MoreHotRecommendVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/8.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "MoreHotRecommendVC.h"
#import "zuyu.h"
#import "MoreHotRecommendCell.h"
#import "MainThirdMP3ViewController.h"
#import "MainMP4ViewController.h"
#import "SingleResourcesVC.h"
#import "MainWebViewController.h"
#import "NetworkErrorView.h"

@interface MoreHotRecommendVC ()<NavgationViewDelegate,UITableViewDelegate,UITableViewDataSource,MoreHotRecommendCellDelegate,NetworkErrorViewDeleagete>
{
    UITableView        *_tableView;
    NSMutableArray     *_titleArray;
    NSMutableArray     *_dataArray;
    NSMutableArray     *_typeIdArray;
}
@property(nonatomic,strong) NetworkErrorView *errorView;

@end

@implementation MoreHotRecommendVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self initArray];
    [self createNavgation];
    [self createTableView];
    [self requestData];
        // Do any additional setup after loading the view.
}
#pragma mark - navgation
-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.titleStr = @"热门推荐";
    [self.view addSubview:view];
}
-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}
#pragma mark - initArray
-(void)initArray
{
    _titleArray = [NSMutableArray arrayWithObjects:@"电子图书",
                   @"有声图书",
                   @"国学经典",
                   @"书法分馆",
                   @"中华戏曲",
                   @"名师绘画",
                   nil];
    
    _dataArray  = [NSMutableArray array];

    _typeIdArray = [NSMutableArray arrayWithObjects:@"22",
                    @"55",
                    @"88",
                    @"33",
                    @"44",
                    @"11",
                     nil];
    
    for (int i = 0; i<_typeIdArray.count; i++) {
        NSMutableArray *arr = [NSMutableArray array];
        [_dataArray addObject:arr];
    }
}

#pragma mark - 网络加载失败视图
-(NetworkErrorView *)errorView
{
    if (_errorView == nil) {
        _errorView = [[NetworkErrorView alloc] initWithFrame:CGRectMake(0, 64, WIDTH, HEIGHT - 64)];
        _errorView.delegate = self;
        [self.view addSubview:_errorView];
    }
    return _errorView;
}
//重新加载
- (void)refreshLoadResouce
{
    self.errorView.hidden = YES;
    [self initArray];
    
    for (int i = 0; i<_typeIdArray.count; i++) {
        [self classRequestDate:i];
    }
}

-(void)requestData
{
    for (int i = 0; i<_typeIdArray.count; i++) {
        [self classRequestDate:i];
    }
}

#pragma mark - createTableView
-(void)createTableView
{
    _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74) style:UITableViewStylePlain];
    
    _tableView.delegate = self;
    
    _tableView.dataSource = self;
    
    _tableView.rowHeight = WIDTH/3 * 1.3 * 2 + 80;
    
    [self.view addSubview:_tableView];
    
    _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
}
#pragma mark - TableViewDelagateAndDatasouce

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _titleArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{

    MoreHotRecommendCell*  cell = [tableView dequeueReusableCellWithIdentifier:@"MoreHotRecommendCell"];
    
    if (cell == nil) {
        cell = [[MoreHotRecommendCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"MoreHotRecommendCell"];
    }
    cell.dataArray = _dataArray[indexPath.row];
    cell.index  = indexPath;
    cell.delegate  = self;
    cell.title = _titleArray[indexPath.row];
    return cell;
}


-(void)collectionViewDidSelectItemAtIndexPath:(MainCellListModel *)model withType:(NSInteger )type
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
        
        //            }
        
    }
    
}

#pragma mark - requestData
-(void)classRequestDate:(NSInteger )row
{
    
    
    MBProgressHUD  *_hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
    NSString *hotCellTitlePorts = [NSString stringWithFormat:@"http://resource.cncgroup.net:8020/api/books/subtypes/%@/books",_typeIdArray[row]];
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    [manager GET:hotCellTitlePorts parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        NSMutableArray *data = [NSMutableArray array];
        
        for (NSDictionary *itemDict in responseObject) {
            
            MainCellListModel *model = [[MainCellListModel alloc] init];
            model.ID                = [itemDict objectForKey:@"ID"];
            model.name              = [itemDict objectForKey:@"Name"];
            model.imageName         = [itemDict objectForKey:@"CoverImageUrl"];
            model.bookTypeName      = [itemDict objectForKey:@"BookTypeName"];
            model.author            = [itemDict objectForKey:@"Author"];
            model.viewCount         = [itemDict objectForKey:@"ViewCount"];
            model.parameterID       = [itemDict objectForKey:@"ID"];
            model.Summary           = [itemDict objectForKey:@"Summary"];
            model.ResourceUrl       = [itemDict objectForKey:@"ResourceUrl"];
            model.VolumeCount       = [ZuyuJsonRead jsonRead:itemDict WithKey:@"VolumeCount"];
            model.ResourceType      = [itemDict objectForKey:@"ResourceType"];
            model.MenuID            = [itemDict objectForKey:@"MenuID"];
            model.CreatedOn         = [itemDict objectForKey:@"CreatedOn"];
            
            [data addObject:model];
        }
        
        [_dataArray replaceObjectAtIndex:row withObject:data];
        
        [_tableView reloadData];
        
        [_hud hideAnimated:YES];
        
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        self.errorView.hidden = NO;
        
        [_hud hideAnimated:YES];
        
    }];
    
}




- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark - nav 处理.
-(void)viewWillAppear:(BOOL)animated
{
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
