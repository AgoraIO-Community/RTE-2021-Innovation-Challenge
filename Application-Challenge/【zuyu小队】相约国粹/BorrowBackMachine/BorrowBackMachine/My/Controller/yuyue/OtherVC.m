//
//  OtherVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/9/30.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "OtherVC.h"
#import "zuyu.h"
#import "BespeakOtherCell.h"
#import "BespeakModel.h"


@interface OtherVC ()<NavgationViewDelegate,UITableViewDataSource,UITableViewDelegate>
{
    UITableView *_tableView;
    NSMutableArray *_dataArray;
    NSInteger       _page;
}
@end

@implementation OtherVC

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
    view.scanHidden = YES;
    view.searchBtnHidden = YES;
    view.createCodeHidden = YES;
    view.titleStr = _NavTitle;
    [self.view addSubview:view];
    self.view.backgroundColor = RGBA(246, 245, 242, 1);
    
}


-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}


-(void)initArray
{
    _dataArray = [NSMutableArray array];
    _page = 1;
}
#pragma mark - createTabView
-(void)createTableView
{
    _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74) style:UITableViewStylePlain];
    
    _tableView.delegate = self;
    
    _tableView.dataSource = self;
    
    _tableView.rowHeight = 180;
    
    _tableView.estimatedRowHeight = 0;
    _tableView.estimatedSectionFooterHeight = 0;
    _tableView.estimatedSectionHeaderHeight = 0;
    _tableView.mj_footer = [MJRefreshBackNormalFooter footerWithRefreshingBlock:^{
        _page++;
        [self requestData];
        
    }];
    
    
    _tableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        _page = 1;
        [_dataArray removeAllObjects];
        [self requestData];
    }];
    
    [self.view addSubview:_tableView];
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _dataArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    BespeakOtherCell *cell = [tableView dequeueReusableCellWithIdentifier:@"BespeakOtherCell"];
    
    if (cell == nil) {
        cell = [[BespeakOtherCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"BespeakOtherCell"];
    }
    
    BespeakModel *model = _dataArray[indexPath.row];
    
    cell.imageUrl = model.BookLogoUrl;
    cell.title = model.BookName;
    NSString *title = @"借书时间";
    cell.time  = [NSString stringWithFormat:@"%@%@",title,model.RetentionTime];
    cell.deviceName = model.EquipmentName;
    cell.deviceForm = model.EquipmentAddress;
    cell.userInteractionEnabled = NO;
    return cell;
}


-(void)requestData
{
    MBProgressHUD*_hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    NSString *HeaderField = [NSString stringWithFormat:@"Bearer %@",TOKEN];
    
    [manager.requestSerializer setValue:HeaderField  forHTTPHeaderField:@"Authorization"];
    
    NSString *page = [NSString stringWithFormat:@"%ld",_page];

    NSDictionary *parameter = [NSDictionary dictionaryWithObjectsAndKeys:
                               page,@"P",
                               @"10",@"S",
                               [NSString stringWithFormat:@"%ld",self.type],@"QueryType",
                               nil];
    
    NSLog(@"%@ - 参数%@", self.NavTitle, parameter);
    
    [manager POST:PORT(@"BorrowRecord/mypage") parameters:parameter progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
            
            NSArray *dataArray = [[responseObject objectForKey:@"Data"] objectForKey:@"PagedRows"];
            
            for (NSDictionary *itme in dataArray) {
                BespeakModel *model = [[BespeakModel alloc] init];
                
                model.BespeakTime = [ZuyuJsonRead jsonRead:itme WithKey:@"BespeakTime"];
                model.BookName = [ZuyuJsonRead jsonRead:itme WithKey:@"BookName"];
                model.BookStockId = [ZuyuJsonRead jsonRead:itme WithKey:@"BookStockId"];
                
                model.BorrowTime = [ZuyuJsonRead jsonRead:itme WithKey:@"BorrowTime"];
                
                model.CardNo = [ZuyuJsonRead jsonRead:itme WithKey:@"CardNo"];
                model.EquipmentAddress = [ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentAddress"];
                model.EquipmentCode = [ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentCode"];
                model.EquipmentId = [ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentId"];
                model.EquipmentName = [ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentName"];
                model.Id = [ZuyuJsonRead jsonRead:itme WithKey:@"Id"];
                model.IsBorrow = [ZuyuJsonRead jsonRead:itme WithKey:@"IsBorrow"];
                model.ReaderId = [ZuyuJsonRead jsonRead:itme WithKey:@"ReaderId"];
                model.ReaderName = [ZuyuJsonRead jsonRead:itme WithKey:@"ReaderName"];
                model.RetentionTime = [ZuyuJsonRead jsonRead:itme WithKey:@"CreateTime"];
                model.BookLogoUrl = [ZuyuJsonRead jsonRead:itme WithKey:@"BookLogoUrl"];
                
                
                [_dataArray addObject:model];
            }
            

            if (!_dataArray.count) {
                _hud.mode = MBProgressHUDModeText;
                
                _hud.label.text = NSLocalizedString(@"暂无数据", @"HUD message title");
                
                [_hud hideAnimated:YES afterDelay:1];
            }else{
                [_hud hideAnimated:YES];

            }
            
            [_tableView reloadData];
            
            
        }else if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Code"]].integerValue == 2001){
            
            [ZuyuAlertShow alertShow:@"账号不存在" viewController: self];
          
            [[NSUserDefaults standardUserDefaults] setObject:@"0" forKey:@"isLogin"];
            [[NSUserDefaults standardUserDefaults] synchronize];
            [self.navigationController popViewControllerAnimated:YES];
       
        }else{
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString([ZuyuJsonRead jsonRead:responseObject WithKey:@"Message"], @"HUD message title");
            [_hud hideAnimated:YES afterDelay:1];
        }
        
        [_tableView.mj_header endRefreshing];
        [_tableView.mj_footer endRefreshing];
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        [_tableView.mj_header endRefreshing];
        [_tableView.mj_footer endRefreshing];
        NSHTTPURLResponse * responses = (NSHTTPURLResponse *)task.response;
        
        if (responses.statusCode == 401) {
            
            [_hud hideAnimated:YES ];
            
            [ZuyuTokenRefresh tokenRefreshSuccess:^(NSURLSessionDataTask * _Nonnull dataTask, id  _Nullable responseObject) {
                
                [self requestData];
                
            } failure:^(NSURLSessionDataTask * _Nullable dataTask, NSError * _Nonnull error) {
                
                
            }];
            
            
        }else{
            
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"请求超时，请刷新", @"HUD message title");
            _hud.label.numberOfLines = 0;
            [_hud hideAnimated:YES afterDelay:1];
            NSLog(@"%@",error);
        }
        
    }];
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
