//
//  DJSearchVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/22.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "DJSearchVC.h"
#import "UISearchBar+FMAdd.h"
#import "TMyDownloadViewController.h"
#import "SeabarOTableViewCell.h"
#import "MainCellListModel.h"
#import "MainSocendTableViewCell.h"
#import "MainMP4ViewController.h"
#import "MainThirdMP3ViewController.h"
#import "MainWebViewController.h"
#import "zuyu.h"
#import "ZuyuBackButton.h"
#import "SingleResourcesVC.h"
#import "DJMP3VC.h"
@interface DJSearchVC ()<UITableViewDelegate,UITableViewDataSource>
{
    NSMutableArray *_dataArray;
    NSMutableArray *_AllDataArray;
    NSMutableArray *_classIDArray;
    UITableView *_tableView;
    NSMutableArray *_nameArray;
    UIView *_loacAndOnlionView;
    UIView *_typeView;
    
    UIButton *classButton;
    
    /*
     记录是哪一种搜索.1为网络.2为本地
     */
    NSInteger _locOrOnli;
    
}
@property (strong, nonatomic) UISearchBar *searchBar;

@end
 
@implementation DJSearchVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    
    [self initArray];
    
    [self createTableView];
    [self createButtonViewAndButton];
    // Do any additional setup after loading the view.
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


-(void)initArray
{
    _dataArray = [NSMutableArray array];
    _AllDataArray = [NSMutableArray array];
    _classIDArray = [NSMutableArray array];
    _nameArray = [NSMutableArray array];
    _locOrOnli = 1;
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
    if (_locOrOnli == 2) {
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
        
    }else{
        
        static NSString *mCellIdentifier = @"MainSocendTableViewCell";
        
        MainSocendTableViewCell *mCell =(MainSocendTableViewCell*)[tableView dequeueReusableCellWithIdentifier:mCellIdentifier];
        
        if (mCell==nil) {
            
            mCell = [[MainSocendTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
                                                   reuseIdentifier:mCellIdentifier];
            mCell.selectionStyle = UITableViewCellSelectionStyleNone;
            
        }
        
        MainCellListModel *model = _dataArray[indexPath.row];
        
        mCell.nameLable.text = model.name;
        
        mCell.countryLable.hidden = YES;
        
        mCell.collectLable.hidden = YES;
        
        mCell.summaryLable.text = model.author;
        
        [mCell.image sd_setImageWithURL:[NSURL URLWithString:model.imageName] placeholderImage:[ZuyuPlaceholderImage returnPlaceholder:2] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
            
        }];
        
        NSString *dataType = [NSString stringWithFormat:@"%@",model.ResourceType];
        
        if ([dataType isEqualToString:@"0"] || [dataType isEqualToString:@"8"]) {
            //音
            mCell.classTypeSign.image = [UIImage imageNamed:@"mp3Type"];
        }else if ([dataType isEqualToString:@"1"]||[dataType isEqualToString:@"6"] || [dataType isEqualToString:@"10"]){
            //视
            mCell.classTypeSign.image = [UIImage imageNamed:@"mp4Type"];
            
        }else{
            //web
            mCell.classTypeSign.image = [UIImage imageNamed:@"webType"];
            
        }
        
        
        return mCell;
    }
    
    
    
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (_locOrOnli == 1) {
        return HEIGHT / 6;
    }
    return 80;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (_locOrOnli == 1) {
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
        MainCellListModel *model = _dataArray[indexPath.row];
        
        NSString *dataType = [NSString stringWithFormat:@"%@",model.ResourceType];
        
        if ([dataType isEqualToString:@"8"]) {
            //音
            DJMP3VC *vc = [[DJMP3VC alloc] init];
            
            vc.model = model;
            
            [vc setHidesBottomBarWhenPushed:YES];
            
            [self.navigationController pushViewController:vc animated:YES];
            
        }else if ([dataType isEqualToString:@"1"]||[dataType isEqualToString:@"6"]){
            //视
            MainMP4ViewController *vc = [[MainMP4ViewController alloc] init];
            
            vc.BookID = model.parameterID;
            
            [vc setHidesBottomBarWhenPushed:YES];
            
            [self.navigationController pushViewController:vc animated:YES];
            
        }else if([dataType isEqualToString:@"10"]){
            SingleResourcesVC *vc = [[SingleResourcesVC alloc] init];
            vc.titleStr = model.name;
            vc.rscourceUrl = model.ResourceUrl;
            vc.summary = model.Summary;
            vc.classType = @"86";
            [vc setHidesBottomBarWhenPushed:YES];
            [self.navigationController pushViewController:vc animated:YES];
        }else{
            
            MainWebViewController *vc = [[MainWebViewController alloc] init];
            vc.name = model.name;
            //        vc.webUrl = @"http://211.145.50.11:82/zhdjh5/zhdj/News_info.aspx?Bookid=16705&Typeid=12-6-0";
            
            if ([dataType isEqualToString:@"100"]) {
                vc.webUrl = [NSString  stringWithFormat:@"%@",model.Tag];
            }else{
                vc.webUrl = [NSString  stringWithFormat:@"%@",model.ResourceUrl];
            }
            
            [vc setHidesBottomBarWhenPushed:YES];
            
            [self.navigationController pushViewController:vc animated:YES];
            
            //            }
            
        }
        
    }else{
        
        TMyDownloadViewController *vc = [[TMyDownloadViewController alloc] init];
        
        vc.bookName = _dataArray[indexPath.row];
        
        [self.navigationController pushViewController:vc animated:YES];
    }
    
    
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
    [self serchRerultForSverse:self.searchBar.text WithType:@"0"];
}

-(void)popClick:(ZuyuBackButton *)button
{
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - 搜索分类
-(void)typeClick:(UIButton *)button
{
    _loacAndOnlionView.hidden = YES;
    
    if (_typeView == nil) {
        
        
        NSArray *typeArray = [NSArray arrayWithObjects:@"电子图书",
                              @"有声图书",
                              @"书法分馆",
                              @"绘画分馆",
                              @"国学分馆",
                              @"传世名曲",
                              @"中华戏曲",
                              @"数字报纸",nil];
        
        _typeView = [[UIView alloc] initWithFrame:CGRectMake(0, 64, WIDTH, 100)];
        
        _typeView.backgroundColor = [UIColor lightGrayColor];
        
        float btnWid = (WIDTH - 50)/4;
        
        for (int i = 0; i<typeArray.count; i++) {
            
            UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
            
            button.tag = 200 + i;
            
            int y = i;
            if (i > 3) {
                y = i - 4;
            }
            
            int z = 0;
            if (i > 3) {
                z = 1;
            }
            
            button.frame = CGRectMake(10 + y * (10 + btnWid), z * 50 + 5, btnWid, 40);
            
            [button setTitle:typeArray[i] forState:UIControlStateNormal];
            
            [button.layer setCornerRadius:20];
            
            button.layer.masksToBounds = YES;
            
            [button.layer setBorderColor:[UIColor whiteColor].CGColor];
            
            [button.layer setBorderWidth:1.0];
            
            [button addTarget:self action:@selector(searchTypeClick:) forControlEvents:UIControlEventTouchUpInside];
            
            [_typeView addSubview:button];
        }
        
        [self.view addSubview:_typeView];
        
    }else{
        _typeView.hidden = !_typeView.hidden;
    }
    
}

#pragma mark - 分类按钮事件
-(void)searchTypeClick:(UIButton *)button
{
    NSArray *classArray = [NSArray arrayWithObjects:@"22",
                           @"55",
                           @"33",
                           @"11",
                           @"88",
                           @"66",
                           @"44",
                           @"99",
                           nil];
    if (_locOrOnli == 1) {
        [self serchRerultForSverse:self.searchBar.text WithType:classArray[button.tag - 200]];
        
    }
    
    _typeView.hidden = YES;
}


#pragma mark - 网络搜索
-(void)onliClick:(UIButton *)button
{
    [classButton setTitle:@"远程搜索" forState: UIControlStateNormal];
    _locOrOnli = 1;
    _loacAndOnlionView.hidden = YES;
    [self serchRerultForSverse:self.searchBar.text WithType:@"0"];
}
#pragma mark - 本地搜索(总)
-(void)loaClick:(UIButton *)button
{
    [classButton setTitle:@"本地搜索" forState: UIControlStateNormal];
    
    _locOrOnli = 2;
    _loacAndOnlionView.hidden = YES;
    [self serchRerult:self.searchBar.text];
    
}

#pragma mark - 网络搜索文件
-(void)serchRerultForSverse:(NSString *)str WithType:(NSString *)type
{
    if (_dataArray.count) {
        [_dataArray removeAllObjects];
    }
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    NSString *port = [NSString stringWithFormat:@"http://resource.cncgroup.net:8020/api/books/Search/%@/%@?pageIndex=0&pageSize=100",type,str];
    
    port =  [port stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    
    [manager GET:port parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        
        if (_dataArray.count) {
            [_dataArray removeAllObjects];
        }
        
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
            model.Tag = [itemDict objectForKey:@"Tag"];

            [_dataArray addObject:model];
        }
        
        if (!_dataArray.count) {
            MBProgressHUD *hub = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
            hub.mode = MBProgressHUDModeText;
            
            hub.label.text = NSLocalizedString(@"暂无对应数据", @"HUD message title");
            
            [hub hideAnimated:YES afterDelay:1];
            
        }
        
        [_tableView reloadData];
        NSLog(@"%@",responseObject);
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
    }];
    
    [_tableView reloadData];
    
}

#pragma mark - 键盘按钮事件
- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar
{
    
    [_searchBar resignFirstResponder];
    if (_locOrOnli == 1) {
        [self serchRerultForSverse:self.searchBar.text WithType:@"0"];
        
    }else{
        [self serchRerult:searchBar.text];
        
    }
}


#pragma mark - 本地搜索
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
    
    [_tableView reloadData];
    
}

#pragma mark - 高度

- (CGSize )getAttributeSizeWithText:(NSString *)text fontSize:(int)fontSize
{
    CGSize size=[text sizeWithAttributes:@{NSFontAttributeName: [UIFont systemFontOfSize:fontSize]}];
    
    NSAttributedString *attributeSting = [[NSAttributedString alloc] initWithString:text attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:fontSize]}];
    size = [attributeSting size];
    return size;
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
