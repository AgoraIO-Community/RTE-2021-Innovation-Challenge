//
//  FindViewController.m
//  Baby back home
//
//  Created by zhangyu on 2021/5/12.
//

#import "FindViewController.h"
#import "EMChatViewController.h"
#import "MessageDBManager.h"
#import "BBHomeMessageTableViewCell.h"
#import "BJDatePicker.h"
#import "XXCityPickerView.h"
#import "BBDetailViewController.h"
#import "BBMessageModel.h"
#import "LMJDropdownMenu.h"
#import <MJRefresh.h>
#import "BBDataFromJson.h"
#import "BBAddShadowTool.h"

@interface FindViewController ()<UITableViewDelegate,UITableViewDataSource,XXCityPickerViewDelegate,LMJDropdownMenuDataSource,LMJDropdownMenuDelegate,UITextFieldDelegate>

@property(strong,nonatomic)NSArray * dataArr;
@property(strong,nonatomic)NSMutableArray * totalArr;

@property(weak,nonatomic)UITableView * tableView;
@property(nonatomic,strong) BJDatePicker*datePicker;
@property(weak,nonatomic)UITextField * timeChooseTF;
@property(weak,nonatomic)UITextField * searchTF;

@property(weak,nonatomic)UILabel * addressLB;
@property (nonatomic, strong) NSString * searchCondition;
@property(strong,nonatomic)NSArray * jsonArr;

@property(nonatomic,strong)XXCityPickerView *cityPicker;
@property (nonatomic,copy) NSString * chooseAddress;

@end

@implementation FindViewController
{
    NSArray * _menu1OptionTitles;
    NSArray * _searchKeys;

    LMJDropdownMenu * menu1;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = UIColor.whiteColor;
    [self initSubViews];
    _menu1OptionTitles = @[@"男",@"女"];
    self.jsonArr = [BBDataFromJson getdatafromJson];

    self.dataArr = [[MessageDBManager sharedInstance] loadMessages];
    self.totalArr = [NSMutableArray arrayWithArray:self.jsonArr];
    [self.totalArr addObjectsFromArray:self.dataArr];
    __weak typeof(self) weakSelf = self;
    self.tableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        [weakSelf.totalArr removeAllObjects];
        weakSelf.dataArr = [[MessageDBManager sharedInstance] loadMessages];
        weakSelf.totalArr = [NSMutableArray arrayWithArray:self.jsonArr];
        [weakSelf.totalArr addObjectsFromArray:self.dataArr];
        [weakSelf.tableView reloadData];
        [weakSelf.tableView.mj_header endRefreshing];
        weakSelf.addressLB.text = @" 地址";
        weakSelf.timeChooseTF.text = @"";
        self->menu1.title = @"性别";
    }];
    
    // Do any additional setup after loading the view.
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
}

- (void)initSubViews{
    self.view.backgroundColor = UIColor.whiteColor;
    
    UIView * topShadowView = [[UIView alloc] initWithFrame:(CGRectMake(0, 0, KScreenWidth, 174 * KScreenRatio))];
    topShadowView.backgroundColor = UIColor.whiteColor;
    [self.view addSubview:topShadowView];
    [BBAddShadowTool addShadowToViewOnlyBottom:topShadowView];
    
    UIView * backTFView = [[UIView alloc] initWithFrame:(CGRectMake(20, 70 * KScreenRatio, KScreenWidth - 40, 40))];
    backTFView.backgroundColor = [UIColor colorWithWhite:239/255.0 alpha:1];
    backTFView.layer.cornerRadius = 20;
    backTFView.layer.masksToBounds = YES;
    [topShadowView addSubview:backTFView];
    UITextField * searchTf = [[UITextField alloc] initWithFrame:(CGRectMake(20, 0, KScreenWidth - 40, 40))];
    searchTf.backgroundColor = [UIColor colorWithWhite:239/255.0 alpha:1];
    searchTf.returnKeyType = UIReturnKeyDone;
    searchTf.delegate = self;
    self.searchTF = searchTf;
    searchTf.placeholder = @"搜索";
    [backTFView addSubview:searchTf];
    
    CGFloat wid = (KScreenWidth - 40)/3.0;
    CGFloat hei = 60 * KScreenRatio;
    UIView * chooseBackView = [[UIView alloc] initWithFrame:(CGRectMake(0, 204 * KScreenRatio, KScreenWidth, hei))];
    [self.view addSubview:chooseBackView];
    
    UITextField * timeTF = [[UITextField alloc] initWithFrame:(CGRectMake(20 + wid, 0, wid - 10, hei))];
    timeTF.textColor = [UIColor colorWithWhite:68/255.0 alpha:1];
    timeTF.backgroundColor = [UIColor colorWithWhite:239/255.0 alpha:1];
    timeTF.inputView = self.datePicker;
    NSString * placeholder = @" 日期";
    timeTF.layer.cornerRadius = 15;
    timeTF.layer.masksToBounds = YES;
//    timeTF.attributedPlaceholder = [placeholder getAttributeStringWithColor:[UIColor colorWithWhite:222/255.0 alpha:1] fontSize:[UIFont systemFontSize]];
    NSMutableAttributedString *attriString = [[NSMutableAttributedString alloc] initWithString:placeholder];
    [attriString addAttribute:NSForegroundColorAttributeName
                        value:[UIColor colorWithWhite:68/255.0 alpha:1]
                        range:NSMakeRange(0,placeholder.length)];
    timeTF.attributedPlaceholder = attriString;
    self.timeChooseTF = timeTF;
    [chooseBackView addSubview:timeTF];
    
    
    UILabel * addressLB = [[UILabel alloc] initWithFrame:(CGRectMake(20, 0, wid - 10, hei))];
    addressLB.text = @" 地址";
    self.addressLB = addressLB;
    addressLB.textColor = [UIColor colorWithWhite:68/255.0 alpha:1];
    addressLB.backgroundColor = [UIColor colorWithWhite:239/255.0 alpha:1];
    [chooseBackView addSubview:addressLB];
    UITapGestureRecognizer * chooseTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(cityPicker)];
    [addressLB addGestureRecognizer:chooseTap];
    addressLB.userInteractionEnabled = YES;
    addressLB.layer.cornerRadius = 15;
    addressLB.layer.masksToBounds = YES;
    UIView *bgview = [[UIView alloc] initWithFrame:CGRectMake(20 + 2 * wid, 0, wid, hei)];
    [chooseBackView addSubview:bgview];
    bgview.layer.cornerRadius = 15;
    bgview.layer.masksToBounds = YES;
    menu1 = [[LMJDropdownMenu alloc] init];
    [menu1 setFrame:CGRectMake(0, 0, wid, hei)];
    menu1.dataSource = self;
    menu1.delegate   = self;
    
    
    
    menu1.title           = @"性别";
    //[UIColor colorWithRed:64/255.f green:151/255.f blue:255/255.f alpha:1];
    menu1.titleBgColor    = [UIColor colorWithWhite:239/255.0 alpha:1];
    menu1.titleColor      = [UIColor colorWithWhite:68/255.0 alpha:1];
    menu1.titleAlignment  = NSTextAlignmentLeft;
//    menu1.titleEdgeInsets = UIEdgeInsetsMake(0, 15, 0, 0);
    
//    menu1.rotateIcon      = [UIImage imageNamed:@"arrowIcon3"];
    menu1.rotateIconSize  = CGSizeMake(15, 15);
    
    menu1.optionBgColor         = [UIColor colorWithWhite:239/255.0 alpha:1];
    menu1.optionFont            = [UIFont systemFontOfSize:13];
    menu1.optionTextColor       = [UIColor blackColor];
    menu1.optionTextAlignment   = NSTextAlignmentLeft;
    menu1.optionNumberOfLines   = 0;
    menu1.optionLineColor       = [UIColor whiteColor];
    menu1.optionIconSize        = CGSizeMake(15, 15);
    menu1.optionIconMarginRight = 30;
    
    [bgview addSubview:menu1];
    
    
    
    
    
    
    UITableView * tableView = [[UITableView alloc] initWithFrame:(CGRectMake(0, 274 * KScreenRatio, KScreenWidth, KScreenHeight - (274 * KScreenRatio) - 64)) style:(UITableViewStylePlain)];
    tableView.delegate = self;
    tableView.dataSource = self;
    tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [tableView registerClass:[BBHomeMessageTableViewCell class] forCellReuseIdentifier:@"homeCell"];
    [self.view addSubview:tableView];
    self.tableView = tableView;
    
//    UITapGestureRecognizer * tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(resignTF)];
//    [tableView addGestureRecognizer:tap];
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    [self.view endEditing:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    
    [textField resignFirstResponder];//取消第一响应者
    if (textField.text.length == 0) {
        [self.totalArr removeAllObjects];
        self.dataArr = [[MessageDBManager sharedInstance] loadMessages];
        self.totalArr = [NSMutableArray arrayWithArray:self.jsonArr];
        [self.totalArr addObjectsFromArray:self.dataArr];
        [self searchPeople];
//        [self.tableView reloadData];
    }else{
        [self searchPeople];
    }

    return YES;
}

- (void)searchPeople{
    
    
    NSMutableArray * dateArr = [NSMutableArray array];
    for (int i = 0; i < self.totalArr.count; i++) {
        BBMessageModel * model = self.totalArr[i];
        NSString * time = [self timestampSwitchTime:model.timestamp andFormatter:@"YYYY-MM-dd"];
        if ([self.timeChooseTF.text isEqualToString:time] || self.timeChooseTF.text.length == 0) {
            [dateArr addObject:model];
        }
    }
    
    NSMutableArray * secondArr = [NSMutableArray array];
    for (int i = 0; i < dateArr.count; i++) {
        BBMessageModel * model = dateArr[i];
        NSString * address = model.address;
        if ([self.chooseAddress isEqualToString:address] || self.chooseAddress.length == 0 || self.chooseAddress == nil) {
            [secondArr addObject:model];
        }
    }
    
    NSMutableArray * genderArr = [NSMutableArray array];
    for (int i = 0; i < secondArr.count; i++) {
        BBMessageModel * model = secondArr[i];
        NSString * gender = model.pGender;
        if ([self.searchCondition isEqualToString:gender] || self.searchCondition.length == 0 || [self.searchCondition containsString:@"性别"]) {
            [genderArr addObject:model];
        }
    }
    
    NSMutableArray * searchArr = [NSMutableArray array];
    for (int i = 0; i < genderArr.count; i++) {
        BBMessageModel * model = secondArr[i];
        NSString * title = model.title;
        if ([title containsString:self.searchTF.text] || self.searchTF.text.length == 0) {
            [searchArr addObject:model];
        }
    }
    
    
    self.totalArr = searchArr;
    [self.tableView reloadData];
    
}

- (NSString *)timestampSwitchTime:(NSInteger)timestamp andFormatter:(NSString *)format{
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateStyle:NSDateFormatterMediumStyle];
    [formatter setTimeStyle:NSDateFormatterShortStyle];
    [formatter setDateFormat:format];
    NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"Asia/Beijing"];
    [formatter setTimeZone:timeZone];
    
    NSDate *confromTimesp = [NSDate dateWithTimeIntervalSince1970:timestamp];
    
    NSString *confromTimespStr = [formatter stringFromDate:confromTimesp];
    if ([format isEqual:@"HH:mm:ss"] && confromTimespStr.length<8) {
        confromTimespStr = @"00:00:00";
    }
    if ([format isEqual:@"YYYY-MM-dd HH:mm:ss"] && confromTimespStr.length<18) {
        confromTimespStr = @"2000-01-01 00:00:00"; //默认返回
    }
    return confromTimespStr;
}



#pragma mark - LMJDropdownMenu DataSource
- (NSUInteger)numberOfOptionsInDropdownMenu:(LMJDropdownMenu *)menu{
    if (menu == menu1) {
        return _menu1OptionTitles.count;
    } else {
        return 0;
    }
}
- (CGFloat)dropdownMenu:(LMJDropdownMenu *)menu heightForOptionAtIndex:(NSUInteger)index{
    if (menu == menu1) {
        return 30;
    } else {
        return 0;
    }
}
- (NSString *)dropdownMenu:(LMJDropdownMenu *)menu titleForOptionAtIndex:(NSUInteger)index{
    if ( menu == menu1) {
        return _menu1OptionTitles[index];
    } else {
        return @"";
    }
}

#pragma mark - LMJDropdownMenu Delegate
- (void)dropdownMenu:(LMJDropdownMenu *)menu didSelectOptionAtIndex:(NSUInteger)index optionTitle:(NSString *)title{
    if (menu == menu1) {
        NSLog(@"你选择了(you selected)：menu1，index: %ld - title: %@", index, title);
        self.searchCondition = _menu1OptionTitles[index];
        [self searchPeople];
    }
}



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.totalArr.count;
    
}



- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    BBHomeMessageTableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:@"homeCell"];
    BBMessageModel * model = self.totalArr[indexPath.row];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.model = model;
    
    return cell;
}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    BBDetailViewController * detailVC = [[BBDetailViewController alloc] init];
    detailVC.model = self.totalArr[indexPath.row];
    NSArray * picArr = [detailVC.model.imgName componentsSeparatedByString:@","];
    detailVC.numOfPic = picArr.count;
    [self.view endEditing:YES];
    [self.navigationController pushViewController:detailVC animated:YES];
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 100;
}


-(BJDatePicker *)datePicker{
    if (!_datePicker) {
        _datePicker=[BJDatePicker shareDatePicker];
    }
    WS(ws);
    
    [_datePicker datePickerDidSelected:^(NSString *date) {
        //赋值
        ws.timeChooseTF.text=date;
        //收键盘
        [ws.timeChooseTF resignFirstResponder];
        
        [self searchPeople];
    }];

    return _datePicker;
}

#pragma mark - XXCityPickerView

- (XXCityPickerView *)cityPicker{
    if (_cityPicker == nil){
        // 初始化  设置类别   1省  2省-市  3省-市-区
        _cityPicker = [[XXCityPickerView alloc]initWithComponents:3];
        // 设置代理
        _cityPicker.delegate = self;
        [self.view addSubview:_cityPicker];
    }
    return  _cityPicker;
}

// ---- 【XXCityPickerViewDelegate】
// 取消
- (void)XXCityPickerViewLeftAction{
    [self removeCityPickerView];
}
// 完成
- (void)XXCityPickerViewRightAction:(XXCityPickerView *)picker andCity:(NSString *)city andData:(NSMutableDictionary *)dic{
    self.chooseAddress = city;
    self.addressLB.text = [city componentsSeparatedByString:@"-"].lastObject;
    [self removeCityPickerView];
    NSLog(@"🐼🐼🐼XXCityPickerView:\n city= %@   dic= %@",city,dic);
    [self searchPeople];
}

-(void)removeCityPickerView{
    if (_cityPicker) {
        [_cityPicker removeFromSuperview];
        _cityPicker = nil;
    }
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
