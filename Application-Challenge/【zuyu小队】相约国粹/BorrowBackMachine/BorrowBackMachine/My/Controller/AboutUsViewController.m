//
//  AboutUsViewController.m
//  SiyecaoTercher
//
//  Created by zuyu on 2018/5/22.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "AboutUsViewController.h"
#import "AboutUsCell.h"
#import "zuyu.h"
@interface AboutUsViewController ()<UITableViewDelegate,UITableViewDataSource,NavgationViewDelegate>
{
    NSMutableArray *_dataArray;
    NSMutableArray *_valueArray;

    UITableView *_tableView;
}
@end

@implementation AboutUsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createNavgation];
    [self initArray];
    [self createTableView];
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
    view.titleStr = @"关于我们";
    [self.view addSubview:view];
    self.view.backgroundColor = RGBA(246, 245, 242, 1);
    
}

-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - --- - -- - -

-(void)initArray
{
    _dataArray = [NSMutableArray arrayWithObjects:@"官方网站",
                  @"商务合作",
                  @"客服邮箱",
                  @"中创文教(北京)数字科技有限公司",nil];
    
    _valueArray = [NSMutableArray arrayWithObjects:@"www.cncgroup.net",
                   @"400-8860-170",
                   @"service@cncgroup.net",
                   @"",nil];
}
-(void)createTableView
{
    _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, 230 + 280) style:UITableViewStyleGrouped];
    
    _tableView.delegate = self;
    
    _tableView.dataSource = self;
    
    _tableView.rowHeight = 70;
    
    _tableView.scrollEnabled = YES;
    
    [self.view addSubview:_tableView];
    
}

-(UIView *)headView
{
    
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, WIDTH, 230)];
    
    UIImageView *image = [[UIImageView alloc] initWithFrame:CGRectMake(WIDTH*0.4, 40, WIDTH*0.2, WIDTH*0.2)];
    
    image.image = [UIImage imageNamed:@"1024x1024"];
    
    [view addSubview:image];
    
    UILabel *lable = [[UILabel alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(image.frame) + 10, WIDTH, 40)];
    
    lable.text = @"纸电同步智慧图书馆";
    
    lable.textAlignment = NSTextAlignmentCenter;
    
    [view addSubview:lable];
    
    UILabel *lion = [[UILabel alloc] initWithFrame:CGRectMake(0, 220, WIDTH, 10)];
    
    lion.backgroundColor = [UIColor colorWithRed:142.f/255.f green:140.f/255.f blue:146.f/255.f alpha:0.1];
    
    [view addSubview:lion];
    
    view.backgroundColor = [UIColor whiteColor];
    
    return view;
}



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    return _dataArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    AboutUsCell *cell = [tableView dequeueReusableCellWithIdentifier:@"AboutUsCell"];
    if (cell == nil) {
        
        cell = [[AboutUsCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"AboutUsCell" indexPathRow:indexPath.row];
        
        cell.text = _dataArray[indexPath.row];
        
        cell.value = _valueArray[indexPath.row];
        
    }
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    
    return 230;
}
- (nullable UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    
    return  [self headView];
    
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
