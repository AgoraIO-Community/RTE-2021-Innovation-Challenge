//
//  EntitydetailVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/3.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "EntitydetailVC.h"
#import "zuyu.h"
#import "EntityDetailView.h"
#import "EntitydetailMapVC.h"

@interface EntitydetailVC ()<NavgationViewDelegate>
{
    EntityDetailView *_resouceView;
}
@end

@implementation EntitydetailVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self createNavgation];
    [self createUI];
    [self requestData:_model.Id];
    // Do any additional setup after loading the view.
}

#pragma mark - navgation
-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.titleStr = @"书籍详情";
    [self.view addSubview:view];
}


-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}


#pragma  mark - createUI
-(void)createUI
{
    UIImageView *bgImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, 74, WIDTH, HEIGHT - 74)];
    
    bgImage.image = [UIImage imageNamed:@"zhizhiBGI"];
    
    [self.view addSubview:bgImage];
    
    _resouceView = [[EntityDetailView alloc] init];
    
    _resouceView.frame = CGRectMake(0, 74, WIDTH, HEIGHT - 200);
    
    [self.view addSubview:_resouceView];
    
    
    UIButton *orderButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    orderButton.frame = CGRectMake(0, HEIGHT - 60, WIDTH, 60);
    
    [orderButton setTitle:@"预约" forState:UIControlStateNormal];
    
    orderButton.backgroundColor = COLOR;
    
    [orderButton addTarget:self action:@selector(orderClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:orderButton];
}


#pragma mark 预约

-(void)orderClick:(UIButton *)button
{
    EntitydetailMapVC *vc = [[EntitydetailMapVC alloc] init];
    [vc setHidesBottomBarWhenPushed:YES];
    vc.bookID = self.model.Id;
    [self.navigationController pushViewController:vc animated:YES];
}



#pragma mark - network
-(void)requestData:(NSString *)bookID
{
    MBProgressHUD *_hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];

    NSDictionary *parameter = [NSDictionary dictionaryWithObjectsAndKeys:bookID,@"Id",nil];
    
    [manager POST:PORT(@"InteBook/GetInteBookInfo") parameters:parameter progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        NSDictionary *item = [responseObject objectForKey:@"Data"];
        
        _resouceView.name = [ZuyuJsonRead jsonRead:item WithKey:@"BookName"];
        _resouceView.iamge = _model.LogoUrl;
        _resouceView.author = [ZuyuJsonRead jsonRead:item WithKey:@"Author"];
        _resouceView.form = [ZuyuJsonRead jsonRead:item WithKey:@"PublishCompany"];
        _resouceView.descr = [ZuyuJsonRead jsonRead:item WithKey:@"Describe"];

        
        
        [_hud hideAnimated:YES];
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        NSHTTPURLResponse * responses = (NSHTTPURLResponse *)task.response;
        
        if (responses.statusCode == 401) {
            
            [_hud hideAnimated:YES ];
            
            [ZuyuTokenRefresh tokenRefreshSuccess:^(NSURLSessionDataTask * _Nonnull dataTask, id  _Nullable responseObject) {
                
                [self requestData:bookID];
                
            } failure:^(NSURLSessionDataTask * _Nullable dataTask, NSError * _Nonnull error) {
                
                
            }];
            
            
        }else{
            
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"网络错误,请稍后再试", @"HUD message title");
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
