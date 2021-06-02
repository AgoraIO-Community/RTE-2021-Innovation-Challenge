//
//  DJListenVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/11/22.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "DJListenVC.h"
#import "zuyu.h"
#import "SDCycleScrollView.h"
#import "MJRefresh.h"
#import "MainCellListModel.h" 
#import "MainMiddenModel.h"
#import "MainSocendTableViewCell.h"
#import "UIImageView+WebCache.h"
#import "MBProgressHUD.h"
#import "MainThirdMP3ViewController.h"
#import "MainMP4ViewController.h"
#import "MainWebViewController.h"
#import "MainSecondViewClassCellModel.h"
#import "SingleResourcesVC.h"
#import "DJMP3VC.h"
#import "DJListenCell.h"
@interface DJListenVC ()<NavgationViewDelegate,UIScrollViewDelegate,UITableViewDelegate,UITableViewDataSource,NetworkErrorViewDeleagete>
{
    //最上边的二级分栏
    UIScrollView   *_butListScrollView;
    //二级分栏按钮 title
    NSMutableArray *_buttonListArray;
    //二级分栏按钮对应的 id
    NSMutableArray *_typeIdArray;
    //记录点击 button 的 array(点击状态)
    NSMutableArray *_buttonArray;
    //tableview cell list model Array
    NSMutableArray *_classListArray;
    //记录隐藏三级分栏
    int hiddenAllListView;
    //三级分栏的 View
    UIView *_buttonView;
    //暂时未知
    NSInteger scrollHiddens;
    //TableView
    UITableView *_tableView;
    //确认是否二级分栏的第一个按钮
    NSInteger _buttonTagvalue;
    //二级分栏按钮 array
    NSMutableArray *_erjifenlanArray;
    //二级分栏 View
    UIView *_erjifenlanView;
    NSMutableArray *_erjifenlanTpyeIDArray;
    //二级分栏下边的小横杠
    UILabel *_lable;
    //分栏数据 type
    NSString *_typeIDString;
    //8个按钮的 cell model array
    NSMutableArray *_hotListArray;
    //8个按钮的 cell 对应的 id array
    NSMutableArray *_buttonListArrayID;
    
    SDCycleScrollView *cycleScrollView;
    
    NSArray *_bannerImageArray;
}

@property(nonatomic,strong )NetworkErrorView *errorView;

@end

@implementation DJListenVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [super viewDidLoad];
    [super viewDidLoad];
    [self createNavgation];
    [self initArray];
    [self createTableView];
    if (!_isSonClass) {
        [self twoViewRequestDate];
    }else{
        [self bannerImageGet];
    }
    // Do any additional setup after loading the view.
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
    if (!_isSonClass) {
        [self twoViewRequestDate];
    }else{
        [self bannerImageGet];
    }
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
    view.titleStr = self.navTitle;
    [self.view addSubview:view];
    self.view.backgroundColor = RGBA(246, 245, 242, 1);
    
}

-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - initArray
-(void)initArray
{
    hiddenAllListView = 0;
    scrollHiddens = 0;
    _buttonArray = [NSMutableArray array];
    _buttonListArray = [NSMutableArray array];
    _typeIdArray = [NSMutableArray array];
    _classListArray = [NSMutableArray array];
    _erjifenlanArray = [NSMutableArray array];
    _erjifenlanTpyeIDArray = [NSMutableArray array];
    _hotListArray = [NSMutableArray array];
    _buttonListArrayID = [NSMutableArray array];
}
#pragma mark - 二级分栏
-(void)createMenuButton
{
    _butListScrollView = [[UIScrollView alloc ] initWithFrame:CGRectMake(0, 74,WIDTH - 40,40)];
    
    _butListScrollView.scrollEnabled = YES;
    
    _butListScrollView.delegate = self;
    
    _butListScrollView.bounces = NO;
    
    _butListScrollView.backgroundColor = [UIColor whiteColor];
    
    _butListScrollView.showsHorizontalScrollIndicator = NO;
    
    _butListScrollView.contentSize = CGSizeMake(110*_buttonListArray.count, 0);
    
    _butListScrollView.backgroundColor = [UIColor whiteColor];
    
    for (int i = 0; i<_buttonListArray.count; i++) {
        
        UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
        
        button.frame = CGRectMake(i*110, 0, 110 , 40);
        
        [button setTitle:[_buttonListArray objectAtIndex:i] forState:UIControlStateNormal];
        
        button.titleLabel.font = [UIFont systemFontOfSize: 16];
        
        button.tag = 1000+i;
        
        [button setTitleColor:[UIColor blackColor]forState:UIControlStateNormal];
        
        UILabel *lable = [[UILabel alloc] initWithFrame:CGRectMake(0, 39, 110, 1)];
        
        lable.backgroundColor = [UIColor whiteColor];
        
        [button addSubview:lable];
        
        [button addTarget:self action:@selector(clickListButton:) forControlEvents:UIControlEventTouchUpInside];
        
        if (i == 0) {
            
            lable.backgroundColor = COLOR;
            
            [button setTitleColor:COLOR forState:UIControlStateNormal];
        }
        
        [_buttonArray addObject:button];
        
        [_butListScrollView addSubview:button];
        
    }
    
    [self.view addSubview:_butListScrollView];
    
    UIButton *AllButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    AllButton.frame = CGRectMake(WIDTH - 40, 74, 40 , 40);
    
    
    AllButton.backgroundColor = [UIColor whiteColor];
    
    [AllButton setImage:[UIImage imageNamed:@"V"] forState:UIControlStateNormal];
    
    [AllButton addTarget:self action:@selector(clickAllButton:) forControlEvents:UIControlEventTouchUpInside];
    
    UILabel *lable = [[UILabel alloc] initWithFrame:CGRectMake(0, 39, 40, 1)];
    
    lable.backgroundColor = [UIColor whiteColor];
    
    [AllButton addSubview:lable];
    
    [self.view addSubview:AllButton];
    
}

#pragma mark - 二级分栏点击事件
-(void)clickAllButton:(UIButton *)button
{
    _erjifenlanView.hidden = YES;
    
    if (hiddenAllListView % 2) {
        
        _buttonView.hidden = YES;
        
        [button setTitleColor:COLOR forState:UIControlStateNormal];
        
    }else{
        
        _buttonView.hidden = NO;
        
        [button setTitleColor:[UIColor blackColor]forState:UIControlStateNormal];
    }
    
    hiddenAllListView++;
    
}


#pragma mark - 二级分栏点击出现的 View 创建
-(void)createButtonView
{
    NSInteger viewHeight;
    
    if (_buttonListArray.count %3 + 1 == 0) {
        
        viewHeight = _buttonListArray.count/3  * 50 + 10;
        
    }else{
        viewHeight = (_buttonListArray.count/3 + 1) * 50 + 10;
    }
    _buttonView = [[UIView alloc] initWithFrame:CGRectMake(0, 104, WIDTH, viewHeight)];
    
    _buttonView.backgroundColor = [UIColor whiteColor];
    
    _buttonView.hidden = YES;
    
    [self createListButton];
    
    [ [ [ UIApplication  sharedApplication ]  keyWindow ] addSubview : _buttonView] ;
}

#pragma mark - 创建三级分栏上的按钮
-(void)createListButton
{
    NSInteger arr = _buttonListArray.count + 1;
    
    NSInteger arrIn;
    
    float witdh = (WIDTH - 50) / 3;
    float height = 40;
    
    int j = 1;
    
    if (arr%3 == 0) {
        arrIn = arr/3;
    }else{
        arrIn = arr/3 + 1;
    }
    
    for (int i = 0; i<arrIn; i++) {
        
        for (int k = 0; k<3; k++) {
            
            
            
            if (j <= arr ) {
                
                UIButton*  _listButton = [UIButton buttonWithType:UIButtonTypeCustom];
                
                _listButton.frame = CGRectMake(10 + k * (witdh+10), (height +10) *i +10, witdh, height);
                
                
                if (j - 1<_buttonListArray.count) {
                    
                    [_listButton addTarget:self action:@selector(clickListButton:) forControlEvents:UIControlEventTouchUpInside];
                    
                    [_listButton setTitle:[_buttonListArray objectAtIndex:j-1] forState:UIControlStateNormal];
                    
                    UIImageView *btnImg = [[UIImageView alloc] initWithFrame:CGRectMake(10, 2.5, witdh - 20, height - 5)];
                    
                    btnImg.image = [UIImage imageNamed:@"type_back_title"];
                    
                    [_listButton addSubview:btnImg];
                    
                    _listButton.titleLabel.font   = [UIFont systemFontOfSize: 12];
                    
                    _listButton.tag = j + 999;
                    
                    [_listButton setTitleColor:[UIColor blackColor]forState:UIControlStateNormal];
                    
                    _listButton.titleLabel.font = [UIFont systemFontOfSize: 16];
                    
                }else{
                    UIImageView *btnImg = [[UIImageView alloc] initWithFrame:CGRectMake(10, 2.5, witdh - 20, height - 5)];
                    
                    btnImg.image = [UIImage imageNamed:@"close_type_book"];
                    
                    [_listButton addTarget:self action:@selector(hiddenBtnView) forControlEvents:UIControlEventTouchUpInside];
                    
                    [_listButton addSubview:btnImg];
                }
                
                [_buttonView addSubview:_listButton];
                
            }
            
            j++;
        }
        
    }
    
}


#pragma mark - -
-(void)clickTwoFenlan:(UIButton*)button
{
    //    self.page = 0;
    
    _erjifenlanView.hidden = YES;
    
    _typeIDString = _erjifenlanTpyeIDArray[button.tag - 10000];
    
    [_classListArray removeAllObjects];
    
    [self refreshHotRequestDate];
    //
    [self refreshClassRequestDate];
}

//_buttonView 隐藏
-(void)hiddenBtnView
{
    _buttonView.hidden = YES;
}

#pragma mark - 二级列表按钮点击事件
-(void)clickListButton:(UIButton *)button
{
    
    
    NSLog(@"xxxxxxx");
    
    _buttonTagvalue = button.tag-1000;
    
    if (button.tag == 1000) {
        scrollHiddens = 0;
    }else{
        scrollHiddens = 1;
        
    }
    
    
    
    if ([self.type integerValue]) {
        if(_buttonTagvalue >0){
            
            _typeIDString = [NSString stringWithFormat:@"%@",_typeIdArray[button.tag - 1000]];
            
        }
    }else{
        _typeIDString = [NSString stringWithFormat:@"%@",_typeIdArray[button.tag - 1000]];
    }
    
    
    
    [_erjifenlanArray removeAllObjects];
    
    //    _erjifenlanView
    
    [_erjifenlanView removeFromSuperview];
    
    if ([self.type integerValue]) {
        if (button.tag != 1000) {
            [self requestTwoFenlan:_typeIDString];
            
        }
    }else{
        [self requestTwoFenlan:_typeIDString];
        
    }
    
    
    
    //    self.hotCellTitlePortsPreserve = [NSString stringWithFormat:@"http://resource.cncgroup.net:8009/api/books/subtypes/%@/books/recommend?pageindex=0&pagesize=8",_typeIDString];
    
    [_tableView reloadData];
    
    _buttonTagvalue++;
    
    [_classListArray removeAllObjects];
    
    
    if (_buttonTagvalue == 1) {
        
        //        [self hotRequestDate];
        
        [self classRequestDate];
        
    }else{
        
        [_classListArray removeAllObjects];
        
        [self refreshHotRequestDate];
        //
        [self refreshClassRequestDate];
    }
    
    _buttonView.hidden = YES;
    
    hiddenAllListView = 0;
    
    CGPoint point;
    
    point = CGPointMake(110*(button.tag - 1001), 0);
    
    if (_buttonListArray.count < 5) {
        
    }else{
        
        if (button.tag == 1000) {
            
            point = CGPointMake(110*(button.tag - 1000), 0);
            
        }
        if (button.tag - 1000 == _buttonListArray.count - 1 ) {
            
            point = CGPointMake(110*_buttonListArray.count - WIDTH + 40
                                , 0);
            
        }
        if (button.tag - 1000 == _buttonListArray.count - 2 ) {
            
            point = CGPointMake(110*_buttonListArray.count - WIDTH + 40
                                , 0);
        }
        if (button.tag - 1000 == _buttonListArray.count - 3 ) {
            
            point = CGPointMake(110*_buttonListArray.count - WIDTH + 40
                                , 0);
        }
        [_butListScrollView setContentOffset:point animated:YES];
    }
    
    for (UIButton *btn in _buttonArray){
        
        if (btn.tag ==button.tag) {
            
            _lable = [[UILabel alloc] initWithFrame:CGRectMake(0, 39, 110, 1)];
            
            _lable.backgroundColor = COLOR;
            
            [btn addSubview:_lable];
            
            [btn setTitleColor:COLOR forState:UIControlStateNormal];
            
            
            
        } else {
            
            btn.backgroundColor =[UIColor clearColor];
            
            [btn setTitleColor:[UIColor blackColor]forState:UIControlStateNormal];
            
            _lable = [[UILabel alloc] initWithFrame:CGRectMake(0, 39, 110, 1)];
            
            _lable.backgroundColor = [UIColor whiteColor];
            
            [btn addSubview:_lable];
        }
        
    }
    
}

#pragma mark - 三级分栏内容获取

#pragma mark - 二级分栏
-(void)requestTwoFenlan:(NSString * )buttonTag
{
    
    [_erjifenlanTpyeIDArray removeAllObjects];
    
    NSString *butTitlePorts = [NSString stringWithFormat:@"http://resource.cncgroup.net:8020/api/books/subtypes/%@",buttonTag];
    
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    [manager GET:butTitlePorts parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        for (NSDictionary *itemDict in responseObject) {
            
            [_erjifenlanArray addObject:[NSString stringWithFormat:@"%@",[itemDict objectForKey:@"TypeName"]]];
            
            [_erjifenlanTpyeIDArray addObject:[NSString stringWithFormat:@"%@",[itemDict objectForKey:@"TypeId"]]];
            
        }
        //
        
        [self createTwoFenlanView];
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
    }];
    
    
}

#pragma mark - 分栏
-(void)createTwoFenlanView
{
    
    if (_erjifenlanArray.count) {
        
        NSInteger viewHeight;
        
        if ((_erjifenlanArray.count + 1) %3 == 0) {
            
            viewHeight = _erjifenlanArray.count/3  * 50  + 50 + 10 ;
            
        }else{
            
            viewHeight = (_erjifenlanArray.count/3 + 1) * 50 + 10;
            
        }
        
        _erjifenlanView = [[UIView alloc] initWithFrame:CGRectMake(0, 104, WIDTH, viewHeight)];
        
        _erjifenlanView.backgroundColor = [UIColor whiteColor];
        
        NSInteger arr = _erjifenlanArray.count +1;
        
        NSInteger arrIn;
        
        float witdh = (WIDTH - 50) / 3;
        float height = 40;
        
        int j = 1;
        
        if (arr%3 == 0) {
            arrIn = arr/3;
        }else{
            arrIn = arr/3 + 1;
        }
        
        for (int i = 0; i<arrIn; i++) {
            
            for (int k = 0; k<3; k++) {
                
                
                
                if (j <= arr) {
                    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
                    
                    button.frame = CGRectMake(10 + k * (witdh+10), (height +10) *i +10, witdh, height);
                    
                    
                    
                    if (j<=_erjifenlanTpyeIDArray.count) {
                        
                        
                        UIImageView *btnImg = [[UIImageView alloc] initWithFrame:CGRectMake(10, 2.5, witdh - 20, height - 5)];
                        
                        btnImg.image = [UIImage imageNamed:@"type_back_title"];
                        
                        [button addSubview:btnImg];
                        [button setTitle:[_erjifenlanArray objectAtIndex:j-1] forState:UIControlStateNormal];
                        [button addTarget:self action:@selector(clickTwoFenlan:) forControlEvents:UIControlEventTouchUpInside];
                        
                        //                    search_bar_edit_normal.9
                        
                    }else{
                        //                    [button setTitle:@"X" forState:UIControlStateNormal];
                        
                        UIImageView *btnImg = [[UIImageView alloc] initWithFrame:CGRectMake(10, 2.5, witdh - 20, height - 5)];
                        
                        btnImg.image = [UIImage imageNamed:@"close_type_book"];
                        
                        [button addSubview:btnImg];
                        
                        //                    [button setImage:[UIImage imageNamed:@"close_type_book"] forState:UIControlStateNormal];
                        
                        
                        
                        [button addTarget:self action:@selector(hiddenTwoView:) forControlEvents:UIControlEventTouchUpInside];
                    }
                    
                    
                    button.titleLabel.font   = [UIFont systemFontOfSize: 12];
                    
                    button.tag = j + 9999;
                    
                    [button setTitleColor:[UIColor blackColor]forState:UIControlStateNormal];
                    
                    button.titleLabel.font = [UIFont systemFontOfSize: 16];
                    
                    [_erjifenlanView addSubview:button];
                    
                    
                }
                j++;
                
            }
            
        }
        
        [self.view addSubview:_erjifenlanView];
        
        //    [ [ [ UIApplication  sharedApplication ]  keyWindow ] addSubview : _erjifenlanView] ;
    }
}


-(void)hiddenTwoView:(UIButton *)button
{
    _erjifenlanView.hidden = YES;
    
}


#pragma mark - ------------TABLEVIEW-----------

#pragma mark - TableView
-(void)createTableView
{
    
    if (_isSonClass) {
        _tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT-40) style:UITableViewStylePlain];
    }else{
        _tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 104, WIDTH, HEIGHT-40) style:UITableViewStylePlain];
    }
    
    
    
    _tableView.delegate = self;
    _tableView.dataSource = self;
    _tableView.autoresizingMask =  UIViewAutoresizingFlexibleWidth;
    [self.view addSubview:_tableView];
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return 1;
    }else{
        return _classListArray.count;
    }
    
}

//cell内容编辑
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    static NSString *mCellIdentifier = @"MainSocendTableViewCell";
    
    if(indexPath.section == 0 && indexPath.row == 0){
        
        
        UITableViewCell *cell = [[UITableViewCell alloc] init];
        
        [cell addSubview:[self craeteScrollView]];
        
        cell.userInteractionEnabled = NO;
        
        return cell;
        
    }else{
        
        
        DJListenCell *mCell =(DJListenCell*)[tableView dequeueReusableCellWithIdentifier:mCellIdentifier];
        
        if (mCell==nil) {
            
            mCell = [[DJListenCell alloc] initWithStyle:UITableViewCellStyleDefault
                                      reuseIdentifier:mCellIdentifier];
            mCell.selectionStyle = UITableViewCellSelectionStyleNone;
            
        }
        
        if (_classListArray.count) {
            
            MainCellListModel *model = _classListArray[indexPath.row];
            
            mCell.title = model.name;
        }
        
        
        
        return mCell;
        
        
    }
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.section == 0 && indexPath.row == 0) {
        
        if (scrollHiddens) {
            return 40;
        }else{
            return 180;
        }
    }
    
    return 100;
    
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (section == 1) {
        return 40;
    }else
    {
        return 0;
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    _erjifenlanView.hidden = YES;
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    MainCellListModel *model = _classListArray[indexPath.row];
    
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
        
        NSString *summary =  [model.Summary stringByReplacingOccurrencesOfString:@"\r" withString:@""];
        summary = [summary stringByReplacingOccurrencesOfString:@"\n" withString:@""];
        summary = [NSString stringWithFormat:@"      %@",summary];
        vc.summary = summary;
        vc.classType = self.type;
        [vc setHidesBottomBarWhenPushed:YES];
        [self.navigationController pushViewController:vc animated:YES];
        
    }else if([dataType isEqualToString:@"3"]){
        MainWebViewController *vc = [[MainWebViewController alloc] init];
        
        vc.webUrl = [NSString  stringWithFormat:@"%@",model.ResourceUrl];
        
        [vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController:vc animated:YES];
    }else{
        
        MainWebViewController *vc = [[MainWebViewController alloc] init];
        vc.name = model.name;
        //        vc.webUrl = @"http://211.145.50.11:82/zhdjh5/zhdj/News_info.aspx?Bookid=16705&Typeid=12-6-0";
        
        vc.webUrl = [NSString  stringWithFormat:@"%@",model.Tag];
        
        [vc setHidesBottomBarWhenPushed:YES];
        
        [self.navigationController pushViewController:vc animated:YES];
        
        //            }
        
    }
    
}

#pragma mark - cell列表的数据
-(void)classRequestDate
{
    
    
    
    MBProgressHUD  *_hud = [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
    
    NSString *hotCellTitlePorts;
    
    if (self.isSonClass) {
        
        hotCellTitlePorts =[NSString stringWithFormat:@"http://resource.cncgroup.net:8020/api/books/subtypes/%@/books",self.type];
    }else{
        hotCellTitlePorts =[NSString stringWithFormat:@"http://resource.cncgroup.net:8020/api/books/subtypes/%@/books",_typeIdArray[0]];
    }
    
    if (![self.type integerValue]) {
        hotCellTitlePorts =[NSString stringWithFormat:@"http://resource.cncgroup.net:8020/api/books/subtypes/%@/books",@"22"];
    }
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    [manager GET:hotCellTitlePorts parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
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
            model.ResourceType = [itemDict objectForKey:@"ResourceType"];
            model.MenuID =[itemDict objectForKey:@"MenuID"];
            model.CreatedOn =[itemDict objectForKey:@"CreatedOn"];
            model.Tag = [itemDict objectForKey:@"Tag"];
            [_classListArray addObject:model];
        }
        
        [_tableView reloadData];
        
        [_hud hideAnimated:YES];
        
        [self endRefresh];
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        _hud.mode = MBProgressHUDModeText;
        _hud.label.text = NSLocalizedString(@"请求失败", @"HUD message title");
        [_hud hideAnimated:YES afterDelay: 1];
        [self endRefresh];
        
    }];
    
}

-(void)refreshHotRequestDate
{
    
    [_hotListArray removeAllObjects];
    
    [_buttonListArrayID removeAllObjects];
    
    NSString *hotCellTitlePorts = @"";
    
    hotCellTitlePorts = [NSString stringWithFormat:@"http://resource.cncgroup.net:8020/api/books/subtypes/%@/books/recommend?pageindex=0&pagesize=8",_typeIDString];
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    [manager GET:hotCellTitlePorts parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        for (NSDictionary *itemDict in responseObject) {
            
            MainMiddenModel *model = [[MainMiddenModel alloc] init];
            model.ID = [itemDict objectForKey:@"ID"];
            model.lableTitle =[itemDict objectForKey:@"Name"];
            model.imageName =[itemDict objectForKey:@"CoverImageUrl"];
            model.resourceType = [itemDict objectForKey:@"ResourceType"];
            model.ResourceUrl = [itemDict objectForKey:@"ResourceUrl"];
            
            model.MenuID =[itemDict objectForKey:@"MenuID"];
            
            
            [_hotListArray addObject:model];
            
            [_buttonListArrayID addObject:[itemDict objectForKey:@"ID"]];
        }
        
        [_tableView reloadData];
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        self.errorView.hidden = NO;

    }];
    
}

#pragma mark - 二级分栏数据请求
-(void)twoViewRequestDate
{
    if (![self.type integerValue]) {
        _buttonListArray = [NSMutableArray arrayWithObjects:@"电子图书",
                            @"有声图书",
                            @"少儿分馆",
                            @"绘本动画",
                            @"英语分馆",
                            @"音乐分馆",
                            @"美术分馆",
                            @"书法分馆",
                            nil];
        _typeIdArray = [NSMutableArray arrayWithObjects:@"22",
                        @"55",
                        @"33",
                        @"11",
                        @"88",
                        @"66",
                        @"44",
                        @"99", nil];
        
        [self createMenuButton];
        [self createButtonView];
        
        
    }else{
        
        NSString *port = @"";
        
        port = [NSString stringWithFormat:@"http://resource.cncgroup.net:8020/api/books/subtypes/%@",self.type];
        
        AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
        
        [manager GET:port parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
            
        } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            
            for (NSDictionary *dict in responseObject) {
                [_buttonListArray addObject:[dict objectForKey:@"TypeName"]];
                [_typeIdArray addObject:[dict objectForKey:@"TypeId"]];
            }
            if (!_isdj) {
                [_buttonListArray insertObject:@"全部分类" atIndex:0];
            }
            [self createMenuButton];
            [self createButtonView];
            if (_typeIdArray.count) {
                [self bannerImageGet];
            }
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            
        }];
    }
    
    
}

#pragma mark - 二级分栏下级数据
-(void)refreshClassRequestDate
{
    
    
    
    NSString *hotCellTitlePorts = @"";
    
    
    hotCellTitlePorts = [NSString stringWithFormat:@"http://resource.cncgroup.net:8020/api/books/subtypes/%@/books",_typeIDString];
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    [manager GET:hotCellTitlePorts parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        for (NSDictionary *itemDict in responseObject) {
            
            MainCellListModel *model = [[MainCellListModel alloc] init];
            //
            model.ID = [itemDict objectForKey:@"ID"];
            
            model.name = [itemDict objectForKey:@"Name"];
            model.imageName = [itemDict objectForKey:@"CoverImageUrl"];
            model.bookTypeName = [itemDict objectForKey:@"BookTypeName"];
            model.author = [itemDict objectForKey:@"Author"];
            model.viewCount = [itemDict objectForKey:@"ViewCount"];
            model.parameterID = [itemDict objectForKey:@"ID"];
            model.Summary = [itemDict objectForKey:@"Summary"];
            model.ResourceUrl = [itemDict objectForKey:@"ResourceUrl"];
            model.ResourceType = [itemDict objectForKey:@"ResourceType"];
            model.CreatedOn =[itemDict objectForKey:@"CreatedOn"];
            model.Tag = [itemDict objectForKey:@"Tag"];
            
            model.MenuID =[itemDict objectForKey:@"MenuID"];
            
            [_classListArray addObject:model];
            
        }
        
        
        [_tableView reloadData];
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
    }];
    
}

#pragma mark - 高度

- (CGSize )getAttributeSizeWithText:(NSString *)text fontSize:(int)fontSize
{
    CGSize size=[text sizeWithAttributes:@{NSFontAttributeName: [UIFont systemFontOfSize:fontSize]}];
    
    NSAttributedString *attributeSting = [[NSAttributedString alloc] initWithString:text attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:fontSize]}];
    size = [attributeSting size];
    return size;
}

#pragma mark - table 刷新结束
-(void)endRefresh{
    [_tableView.mj_header endRefreshing];
    [_tableView.mj_footer endRefreshing];
}


-(void)bannerImageGet
{
    
    
    NSMutableArray *imageArray = [NSMutableArray array];
    
    
    
    NSDictionary* parameters = [NSDictionary dictionaryWithObjectsAndKeys:@"6",@"Top",
                                self.type,@"Type",
                                nil];
    
    [[AFHTTPSessionManager manager] POST:PORT(@"AppBanner/GetTopList") parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
            
            for (NSDictionary *itme in [responseObject objectForKey:@"Data"]) {
                
                
                NSString *iamge = [ZuyuJsonRead jsonRead:itme WithKey:@"BannerUrl"];
                
                iamge = [iamge stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
                
                iamge = [NSString stringWithFormat:@"%@%@",FILE,iamge];
                
                [imageArray addObject:iamge];
                
            }
            
            _bannerImageArray = [NSArray arrayWithArray:imageArray];
            [self classRequestDate];
            
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        self.errorView.hidden = NO;
    }];
    
}

#pragma mark - 滚动视图
-(UIScrollView *)craeteScrollView{
    
    UIScrollView *demoContainerView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 180)];
    
    [self.view addSubview:demoContainerView];
    
    if (scrollHiddens) {
        demoContainerView.hidden = YES;
    }else{
        demoContainerView.hidden = NO;
    }
    
    cycleScrollView = [SDCycleScrollView cycleScrollViewWithFrame:CGRectMake(0, 0, WIDTH, 180) delegate:self placeholderImage:[UIImage imageNamed:@"placeholder"]];
    cycleScrollView.pageControlAliment = SDCycleScrollViewPageContolAlimentRight;
    cycleScrollView.currentPageDotColor = [UIColor whiteColor];
    cycleScrollView.imageURLStringsGroup = _bannerImageArray;
    
    [demoContainerView addSubview:cycleScrollView];
    
    
    return demoContainerView;
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
    _buttonView.hidden = YES;
    
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
