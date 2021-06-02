//
//  EntitySearchVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/29.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "EntitySearchVC.h"
#import "zuyu.h"
#import "EntityResourcesModel.h"
#import "OnlineHotCell.h"
#import "EntitydetailVC.h"

@interface EntitySearchVC ()<UISearchBarDelegate,UICollectionViewDelegate,UICollectionViewDataSource>
{
    NSMutableArray *_dataArray;
    UICollectionView *_collectionView;

}
@property (strong, nonatomic) UISearchBar *searchBar;
@end

@implementation EntitySearchVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self initArray];
    [self createButtonViewAndButton];
    [self createCollectionView];
    // Do any additional setup after loading the view.
}


-(void)initArray
{
    _dataArray = [NSMutableArray array];
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



#pragma mark - 懒加载导航栏
- (UISearchBar *)searchBar {
    if (!_searchBar) {
        _searchBar = [[UISearchBar alloc]initWithFrame:CGRectMake(40, 20, WIDTH - 90, 44)]; 
        _searchBar.delegate = self;
        _searchBar.placeholder = @"搜索";
        _searchBar.showsCancelButton = NO;
        _searchBar.barTintColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"header-bg"]];;
    }
    return _searchBar;
}


- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText{
    
    
}

#pragma mark - 创建本地和网络搜索的按钮
-(void)createButtonViewAndButton
{
    
    UIView *navView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 74)];
    
    UIImageView *image = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 74)];
    
    image.image = [UIImage imageNamed:@"header-bg"];
    
    [navView addSubview:image];
    
    ZuyuBackButton *backButton = [[ZuyuBackButton alloc] initWithFrame:CGRectMake(0, 20, 80, 44)];
    
    [backButton addTarget:self action:@selector(popClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [navView addSubview:backButton];
    
    [self.view addSubview:navView];
    
    [navView addSubview:self.searchBar];
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    
    button.frame = CGRectMake(WIDTH - 47, 17,  50, 50);
    
    [button setImage:[UIImage imageNamed:@"searchForNav"] forState:UIControlStateNormal];
    
    [button addTarget:self action:@selector(searchClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [navView addSubview:button];
    
} 

-(void)searchClick:(UIButton *)button
{
    [_searchBar resignFirstResponder];
    [self serchRerultForSverse:_searchBar.text WithType:@""];
}

-(void)popClick:(ZuyuBackButton *)button
{
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - 网络搜索文件
-(void)serchRerultForSverse:(NSString *)str WithType:(NSString *)type
{
    if (_dataArray.count) {
        [_dataArray removeAllObjects];
    }
   
    MBProgressHUD*_hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"搜索中...", @"HUD loading title");
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    NSDictionary *parameters = [NSDictionary dictionaryWithObjectsAndKeys:str,@"K", nil];
    
    [manager POST:PORT(@"InteBook/GetPagedData") parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
            
            for (NSDictionary *itme in [[responseObject objectForKey:@"Data"] objectForKey:@"PagedRows"]) {
                
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
                [_dataArray addObject:model];
                
            }
            
            if (!_dataArray.count) {
                
                _hud.mode = MBProgressHUDModeText;
                
                _hud.label.text = NSLocalizedString(@"暂无对应数据", @"HUD message title");
                
                [_hud hideAnimated:YES afterDelay:1];
            }else{
                [_hud hideAnimated:YES];

            }
            
            
        }else{
            [_hud hideAnimated:YES];

        }
            [_collectionView reloadData];
        
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        [_hud hideAnimated:YES];

    }];
    
    
}

#pragma mark - 键盘按钮事件
- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar
{
    [_searchBar resignFirstResponder];
    [self serchRerultForSverse:self.searchBar.text WithType:@"0"];
}

#pragma mark - 导航栏创建及操作原nav
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:YES animated:NO];
    
}
- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:NO];
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
