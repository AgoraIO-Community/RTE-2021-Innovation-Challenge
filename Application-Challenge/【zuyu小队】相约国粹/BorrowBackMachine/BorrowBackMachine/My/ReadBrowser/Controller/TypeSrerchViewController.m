//
//  TypeSrerchViewController.m
//  CNCLibraryScan
//
//  Created by zuyu on 2017/9/20.
//  Copyright © 2017年 zuyu. All rights reserved.
//

#import "TypeSrerchViewController.h"
#import "SeabarOTableViewCell.h"
#import "TMyDownloadViewController.h"
#import "ZFDownloadManager.h"
#import "zuyu.h"

@interface TypeSrerchViewController ()<UISearchBarDelegate,UITableViewDelegate,UITableViewDataSource,BookSelfNavViewDelegate>
{
    NSMutableArray *_dataArray;
    NSMutableArray *_AllDataArray;
    NSMutableArray *_classIDArray;
    UITableView *_tableView;
    NSMutableArray *_nameArray;
}
@property (strong, nonatomic) UISearchBar *searchBar;
@property (strong, nonatomic) UIView *statusBar;

@end

@implementation TypeSrerchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
   
    [self createTableView];
    
    [self createNavgation];
    
    // Do any additional setup after loading the view.
}

#pragma mark - 导航栏创建及操作原nav
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self initArray];
    
    [self initData];
    
    [self.navigationController setNavigationBarHidden:YES animated:NO];
    
}
 
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
    [self serchRerult:text];

}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}


-(void)initArray
{
    _dataArray = [NSMutableArray array];
    _AllDataArray = [NSMutableArray array];
    _classIDArray = [NSMutableArray array];
    _nameArray = [NSMutableArray array];
}


-(void)initData
{
    
    NSMutableArray *finishArr = [[NSMutableArray alloc] initWithContentsOfFile:PLIST_PATH];
    
    NSMutableArray *bookNameArr = [NSMutableArray array];
    
    for (NSDictionary *dict in finishArr) {
        
        
        NSDictionary *dic = [NSDictionary dictionaryWithObject:[dict objectForKey:@"ClassID"] forKey:[dict objectForKey:@"bookName"]] ;
        
        [bookNameArr addObject:dic];
        
        
    }
    
    
    for (int i = 0 ; i<bookNameArr.count; i++) {
        
        NSString *key;
        
        for (NSString *keys in [bookNameArr[i] allKeys]) {
            key = keys;
        }
        
        if (![_AllDataArray containsObject:key]) {
            
            [_AllDataArray addObject:bookNameArr[i]];
            
            [_nameArray addObject:key];
        }
        
    }
    
    [_tableView reloadData];
    
}



#pragma mark - tableView
-(void)createTableView
{
    _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74) style:UITableViewStylePlain];
    
    _tableView.delegate = self;
    
    _tableView.dataSource = self;
    
    [self.view addSubview:_tableView];
}


#pragma mark - UITableviewDatasouce
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _dataArray.count;
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identifier = @"cell";
    
    SeabarOTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    
    if (cell == nil) {
        cell = [[SeabarOTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        
    }
    
    if (_nameArray.count) {
        
        cell.nameLable.text = _dataArray[indexPath.row];
        
        cell.typeLable.text = @"本地文件";
    }
    
    
    return cell;
    
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 80;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    TMyDownloadViewController *vc = [[TMyDownloadViewController alloc] init];
    
    vc.bookName = _dataArray[indexPath.row];
    
    [self.navigationController pushViewController:vc animated:YES];
    
}




#pragma mark - 懒加载导航栏
- (UISearchBar *)searchBar {
    if (!_searchBar) {
        _searchBar = [[UISearchBar alloc]initWithFrame:CGRectMake(40, 20, WIDTH - 90, 44)];
        _searchBar.delegate = self;
        _searchBar.placeholder = @"搜索";
        _searchBar.showsCancelButton = NO;
        _searchBar.barTintColor=[UIColor colorWithRed:20.f/255.f green:175.f/255.f blue:255.f/255.f alpha:1];
        
    }
    return _searchBar;
}



#pragma mark - 创建本地和网络搜索的按钮
-(void)createButtonViewAndButton
{
    [self.view addSubview:self.searchBar];
    
    self.statusBar = [[UIView alloc]initWithFrame:CGRectMake(0, 0, WIDTH, 21)];
    
    self.statusBar.backgroundColor = RGBA(20, 175, 255, 1);
    
    [self.view addSubview:self.statusBar];
    
    //
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(WIDTH - 50, 20,  100, 44)];
    
    view.backgroundColor = [UIColor colorWithRed:20.f/255.f green:175.f/255.f blue:255.f/255.f alpha:1];
    
    [self.view addSubview:view];
    
    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    UIButton *classButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    classButton.frame = CGRectMake(0, 7, 50, 30);
    
    [classButton setTitle: @"搜索" forState:UIControlStateNormal];
    
    [classButton addTarget:self action:@selector(searchClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [view addSubview:classButton];
    
    UILabel *lionLable = [[UILabel alloc] initWithFrame:CGRectMake(50, 12, 1, 20)];
    
    lionLable.backgroundColor = [UIColor whiteColor];
    
    [view addSubview:lionLable];
    
    
    UIView *view1 = [[UIView alloc] initWithFrame:CGRectMake(0, 20,  40, 44)];
    
    view1.backgroundColor = [UIColor colorWithRed:20.f/255.f green:175.f/255.f blue:255.f/255.f alpha:1];
    
    [self.view addSubview:view1];
    
    
    UIButton *popButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    popButton.frame = CGRectMake(0, 9, 36, 26);
    
    [popButton setImage:[UIImage imageNamed:@"pop<<"] forState:UIControlStateNormal];
    
    [popButton addTarget:self action:@selector(popClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [view1 addSubview:popButton];
    
}

#pragma mark - pop
-(void)popClick:(UIButton *)button
{
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - 搜索按钮事件

-(void)searchClick:(UIButton *)button
{
    [_searchBar resignFirstResponder];
    
    [self serchRerult:_searchBar.text];
}

#pragma mark - 键盘按钮事件
- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar
{
    [_searchBar resignFirstResponder];
    
    [self serchRerult:searchBar.text];
}


#pragma mark - 搜索
-(void)serchRerult:(NSString *)text
{
    if (_dataArray.count) {
        [_dataArray removeAllObjects];
    }
    
    
    for (NSString *str in _nameArray)
    {
        if ([str rangeOfString:text options:NSCaseInsensitiveSearch].length > 0)
        {
            if (![_dataArray containsObject:str]) {
                [_dataArray addObject:str];

            }
            
        }
    }
    
    if (!_dataArray.count) {
        MBProgressHUD *hub = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        hub.mode = MBProgressHUDModeText;
        
        hub.label.text = NSLocalizedString(@"暂无对应数据", @"HUD message title");
        
        [hub hideAnimated:YES afterDelay:1];
    }
    
    [_tableView reloadData];
    
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
