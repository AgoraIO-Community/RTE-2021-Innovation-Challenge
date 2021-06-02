//
//  EasyBookselfVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/23.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "EasyBookselfVC.h"
#import "zuyu.h"
#import "BookselfCell.h"
#import "OnlineHotCell.h"
#import "BookselfEntityVC.h"
#import "MJRefresh.h"
#import "EasyBookselfModel.h"
#import "EasyBookselfEntityVC.h"
@interface EasyBookselfVC ()<BookSelfNavViewDelegate,UICollectionViewDelegate,UICollectionViewDataSource>
{
    UICollectionView *_collectionView;
    NSMutableArray   *_dataArray;
    int         _page;
}

@end

@implementation EasyBookselfVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _page = 0;
    [self initArray];
    [self createNavgation];
    [self request];
    [self createCollection];
    // Do any additional setup after loading the view.
}

-(void)initArray
{
    _dataArray = [NSMutableArray array];
}
#pragma mark - navgation
-(void)createNavgation
{
    BookSelfNavView *view = [[BookSelfNavView alloc] init];
    view.delegate = self;
    [self.view addSubview:view];
}

-(void)BookSelfNavViewPop
{
    [self.navigationController popViewControllerAnimated:YES];
}
-(void)BookSelfNavSearch:(NSString *)text
{
    
    [_dataArray removeAllObjects];
    
    if (text.length) {
        [self requestData:text];
    }else{
        
        _page = 0;
        [self request];
    }
}



#pragma mark - collection
-(void)createCollection
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
    
    _collectionView.mj_footer = [MJRefreshBackStateFooter footerWithRefreshingBlock:^{
        
        [self request];
        
    }];
    
    //    _collectionView m
}


- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return _dataArray.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    OnlineHotCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"OnlineHotCell" forIndexPath:indexPath];
    
    EasyBookselfModel *model = _dataArray[indexPath.item];
    
    cell.bookImage = model.LogoUrl;
    cell.bookNameStr = model.BookName;
    cell.writerNameStr = model.Author;
    
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    EasyBookselfModel *model = _dataArray[indexPath.item];
    EasyBookselfEntityVC *vc = [[EasyBookselfEntityVC alloc] init];
    vc.model             = model;
    vc.bookselfID        = self.bookSelfID;
    [self.navigationController pushViewController:vc animated:YES];
}




#pragma mark - network
-(void)request
{
    
    _page++;
    MBProgressHUD*_hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    NSDictionary *parameters = [NSDictionary dictionaryWithObjectsAndKeys:self.bookSelfID,@"VenueId",
                                [NSString stringWithFormat:@"%d",_page],@"P",
                                @"15",@"S",
                                nil];
    
    [manager POST:PORT(@"InteBookStock/GetPagedByVenueId") parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
            
            for (NSDictionary *itme in [[responseObject objectForKey:@"Data"] objectForKey:@"PagedRows"]) {
                
                EasyBookselfModel *model = [[EasyBookselfModel alloc] init];
                
                model.Id             = [ZuyuJsonRead jsonRead:itme WithKey:@"Id"];
                model.BookCode       = [ZuyuJsonRead jsonRead:itme WithKey:@"BookCode"];
                model.BookName       = [ZuyuJsonRead jsonRead:itme WithKey:@"BookName"];
                model.InteBookId     = [ZuyuJsonRead jsonRead:itme WithKey:@"InteBookId"];
                model.EquipmentId    = [ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentId"];
                model.PositionCode   = [ZuyuJsonRead jsonRead:itme WithKey:@"PositionCode"];
                model.BookStatus     = [ZuyuJsonRead jsonRead:itme WithKey:@"BookStatus"];
                model.EquipmentName = [ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentName"];
                model.IsBespeak      = [ZuyuJsonRead jsonRead:itme WithKey:@"IsBespeak"];
                model.BorrowNum      = [ZuyuJsonRead jsonRead:itme WithKey:@"BorrowNum"];
                model.BespeakNum     = [ZuyuJsonRead jsonRead:itme WithKey:@"BespeakNum"];
                model.EquipmentId     = [ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentId"];
                model.InteBookId       = [ZuyuJsonRead jsonRead:itme WithKey:@"InteBookId"];
                model.LogoUrl       = [ZuyuJsonRead jsonRead:itme WithKey:@"LogoUrl"];
                model.Author        = [ZuyuJsonRead jsonRead:itme WithKey:@"Author"];
                
                [_dataArray addObject:model];
                
            }
            [_collectionView reloadData];
        }
        [_hud hideAnimated:YES];
        
        [_collectionView.mj_footer endRefreshing];
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        [_hud hideAnimated:YES];
        [_collectionView.mj_footer endRefreshing];
        
    }];
    
}
#pragma mark - 搜索 network
-(void)requestData:(NSString *)text
{
    
    if (text.length) {
        _page = 1;
        MBProgressHUD*_hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        
        _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
        
        AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
        
        NSDictionary *parameters = [NSDictionary dictionaryWithObjectsAndKeys:self.bookSelfID,@"EquipmentId",
                                    text,@"K",
                                    nil];
        
        [manager POST:PORT(@"Equipment/GetBookStockPagedData") parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
            
        } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            
            if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
                
                for (NSDictionary *itme in [[responseObject objectForKey:@"Data"] objectForKey:@"PagedRows"]) {
                    
                    BookselfModel *model = [[BookselfModel alloc] init];
                    
                    model.Id             = [ZuyuJsonRead jsonRead:itme WithKey:@"Id"];
                    model.BookCode       = [ZuyuJsonRead jsonRead:itme WithKey:@"BookCode"];
                    model.BookName       = [ZuyuJsonRead jsonRead:itme WithKey:@"BookName"];
                    model.InteBookId     = [ZuyuJsonRead jsonRead:itme WithKey:@"InteBookId"];
                    model.EquipmentId    = [ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentId"];
                    model.PositionCode   = [ZuyuJsonRead jsonRead:itme WithKey:@"PositionCode"];
                    model.BookStatus     = [ZuyuJsonRead jsonRead:itme WithKey:@"BookStatus"];
                    model.BookStatusName = [ZuyuJsonRead jsonRead:itme WithKey:@"BookStatusName"];
                    model.IsBespeak      = [ZuyuJsonRead jsonRead:itme WithKey:@"IsBespeak"];
                    model.BorrowNum      = [ZuyuJsonRead jsonRead:itme WithKey:@"BorrowNum"];
                    model.BespeakNum     = [ZuyuJsonRead jsonRead:itme WithKey:@"BespeakNum"];
                    model.CreateTime     = [ZuyuJsonRead jsonRead:itme WithKey:@"CreateTime"];
                    model.CreateId       = [ZuyuJsonRead jsonRead:itme WithKey:@"CreateId"];
                    model.LogoUrl       = [ZuyuJsonRead jsonRead:itme WithKey:@"LogoUrl"];
                    model.Author        = [ZuyuJsonRead jsonRead:itme WithKey:@"Author"];
                    
                    [_dataArray addObject:model];
                    
                }
                [_collectionView reloadData];
                
                if (!_dataArray.count) {
                    _hud.mode = MBProgressHUDModeText;
                    
                    _hud.label.text = NSLocalizedString(@"暂无相关书籍", @"HUD message title");
                    
                    [_hud hideAnimated:YES afterDelay:1];
                }else{
                    [_hud hideAnimated:YES];
                }
                
            }else{
                [_hud hideAnimated:YES];
            }
            
            
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            [_hud hideAnimated:YES];
        }];
    }else{
        _page = 0;
        [_dataArray removeAllObjects];
        [self request];
    }
    
    
}




#pragma mark - nav 处理.
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:NO];
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
