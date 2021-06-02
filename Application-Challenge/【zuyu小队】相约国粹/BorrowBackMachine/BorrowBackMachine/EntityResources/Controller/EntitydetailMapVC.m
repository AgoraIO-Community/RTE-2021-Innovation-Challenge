//
//  EntitydetailMapVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/8.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "EntitydetailMapVC.h"
#import <MAMapKit/MAMapKit.h>
#import <AMapFoundationKit/AMapFoundationKit.h>
#import "MapModel.h"
#import "BookselfContentVC.h"
#import "zuyu.h"
#import "EntityEasyMachineModel.h"

 
@interface EntitydetailMapVC ()<MAMapViewDelegate,NavgationViewDelegate>
{
    //大屏大头针
    NSMutableArray *_annotationViewArray;
    //藏馆大头针
    NSMutableArray *_easyannotationViewArray;
    //大屏 data
    NSMutableArray *_dataArray;
    //藏馆 data
    NSMutableArray *_easyMachineDataArray;
    MAMapView *_mapView;
    
    UIButton       *_bigMachineButton;
    UIButton       *_easyMachineButton;
    //1代表大屏 . 2代表场馆
    NSInteger      _page;
}


@end

@implementation EntitydetailMapVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [AMapServices sharedServices].enableHTTPS = YES;
    ///初始化地图
    _mapView = [[MAMapView alloc] initWithFrame:CGRectMake(0, 74 + 60, WIDTH, HEIGHT - 74 - 60)];
    
    ///把地图添加至view
    [self.view addSubview:_mapView];
    
    //显示自己定位的小点点
    _mapView.showsUserLocation = YES;
    _mapView.userTrackingMode = MAUserTrackingModeFollow;
    _mapView.delegate = self;
    [self createNavgation];
    [self navCreateButton];
    [self initArray];
    [self requestData];
    [self requestEasyData];
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
    view.titleLableHidden = NO;
    view.rightItmeImage = @"searchForNav";
    view.titleStr = @"附近";
    [self.view addSubview:view];
}

-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)navCreateButton
{
    _bigMachineButton = [UIButton buttonWithType:UIButtonTypeCustom];
    _bigMachineButton.frame = CGRectMake(0, 74, WIDTH/2, 60);
    [_bigMachineButton setImage:[UIImage imageNamed:@"bigMachineButtonSelected"] forState:UIControlStateNormal];
    [_bigMachineButton addTarget:self action:@selector(bigMachineClick:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:_bigMachineButton];
    
    
    _easyMachineButton = [UIButton buttonWithType:UIButtonTypeCustom];
    _easyMachineButton.frame = CGRectMake(WIDTH/2, 74, WIDTH/2, 60);
    [_easyMachineButton setImage:[UIImage imageNamed:@"easyMachineButton"] forState:UIControlStateNormal];
    [_easyMachineButton addTarget:self action:@selector(easyMachineClick:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:_easyMachineButton];
    
}
-(void)bigMachineClick:(UIButton *)button
{
    [_bigMachineButton setImage:[UIImage imageNamed:@"bigMachineButtonSelected"] forState:UIControlStateNormal];
    
    [_easyMachineButton setImage:[UIImage imageNamed:@"easyMachineButton"] forState:UIControlStateNormal];
    
    [_mapView removeAnnotations:_easyannotationViewArray];
    [_mapView addAnnotations:_annotationViewArray];
    
    if (!_annotationViewArray.count) {
        [self hub];
    }
    _page = 1;
}




-(void)easyMachineClick:(UIButton *)button
{
    [_bigMachineButton setImage:[UIImage imageNamed:@"bigMachineButton"] forState:UIControlStateNormal];
    
    [_easyMachineButton setImage:[UIImage imageNamed:@"easyMachineButtonSelected"] forState:UIControlStateNormal];
    
    [_mapView removeAnnotations:_annotationViewArray];
    [_mapView addAnnotations:_easyannotationViewArray];
    
    if (!_easyMachineDataArray.count) {
        [self hub];
    }
    
    _page = 2;
}


-(void)hub
{
    MBProgressHUD*_hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    _hud.mode = MBProgressHUDModeText;
    
    _hud.label.text = NSLocalizedString(@"暂无可预约设备", @"HUD message title");
    
    [_hud hideAnimated:YES afterDelay:2];
    
}
-(void)initArray
{
    _page                    = 1;
    _dataArray               = [NSMutableArray array];
    _annotationViewArray     = [NSMutableArray array];
    _easyMachineDataArray    = [NSMutableArray array];
    _easyannotationViewArray = [NSMutableArray array];
}


#pragma mark - network

-(void)requestData
{
    MBProgressHUD*_hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    _hud.label.text = NSLocalizedString(@"寻找设备中...", @"HUD loading title");
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
     
    NSDictionary *parameters = [NSDictionary dictionaryWithObjectsAndKeys:self.bookID,@"InteBookId", nil];
    
    [manager POST:PORT(@"Equipment/GetPagedDataByInteBookId") parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
            
            
            NSDictionary *dataDic = [[responseObject objectForKey:@"Data"] objectForKey:@"PagedRows"];
            
            
            for (NSDictionary *itme  in dataDic) {
                MapModel *model = [[MapModel alloc] init];
                model.Id                = [ZuyuJsonRead jsonRead:itme WithKey:@"Id"];
                model.EquipmentTypeId   = [ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentTypeId"];
                model.EquipmentTypeName = [ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentTypeName"];
                model.EquipmentName     = [ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentName"];
                model.EquipmentCode     = [ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentCode"];
                model.jingdu            = [[ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentLatitude"] floatValue];;
                model.weidu             = [[ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentLongitude"] floatValue];;
                model.EquipmentAddress  = [ZuyuJsonRead jsonRead:itme WithKey:@"EquipmentAddress"];
                model.Remark            = [ZuyuJsonRead jsonRead:itme WithKey:@"Remark"];
                model.Status            = [ZuyuJsonRead jsonRead:itme WithKey:@"Status"];
                model.StatusName        = [ZuyuJsonRead jsonRead:itme WithKey:@"StatusName"];
                model.ControlBoardComPort = [ZuyuJsonRead jsonRead:itme WithKey:@"ControlBoardComPort"];
                model.RfidComPort       = [ZuyuJsonRead jsonRead:itme WithKey:@"RfidComPort"];
                model.CreateTime        = [ZuyuJsonRead jsonRead:itme WithKey:@"CreateTime"];
                model.CreateId          = [ZuyuJsonRead jsonRead:itme WithKey:@"CreateId"];
                
                MAPointAnnotation *pointAnnotation = [[MAPointAnnotation alloc] init];
                pointAnnotation.coordinate = CLLocationCoordinate2DMake(model.jingdu, model.weidu);
                pointAnnotation.title = model.EquipmentAddress;
                
                [_annotationViewArray addObject: pointAnnotation];

                [_dataArray addObject:model];
            }
            
            
            if (!_dataArray.count) {
                _hud.mode = MBProgressHUDModeText;
                
                _hud.label.text = NSLocalizedString(@"暂无可预约设备", @"HUD message title");
                
                [_hud hideAnimated:YES afterDelay:1];
            }else{
                [_hud hideAnimated:YES];
                
            }
            
            [_mapView addAnnotations:_annotationViewArray];
            
            
            
        }else{
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString([ZuyuJsonRead jsonRead:responseObject WithKey:@"Message"], @"HUD message title");
            [_hud hideAnimated:YES afterDelay:1];
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        _hud.mode = MBProgressHUDModeText;
        _hud.label.text = NSLocalizedString(@"网络错误,请稍后再试", @"HUD message title");
        _hud.label.numberOfLines = 0;
        [_hud hideAnimated:YES afterDelay:1];
        NSLog(@"%@",error);
    }];
}

-(void)requestEasyData
{
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    NSDictionary *parameters = [NSDictionary dictionaryWithObjectsAndKeys:self.bookID,@"InteBookId", nil];

    NSString *HeaderField = [NSString stringWithFormat:@"Bearer %@",TOKEN];
    
    [manager.requestSerializer setValue:HeaderField  forHTTPHeaderField:@"Authorization"];
    
    [manager POST:PORT(@"Venue/GetPagedDataByInteBookId") parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
            
            NSArray *data = [[responseObject objectForKey:@"Data"] objectForKey:@"PagedRows"];
            
            for (NSDictionary *itme in data) {
                
                EntityEasyMachineModel *model = [[EntityEasyMachineModel alloc] init];
                
                model.CreateId = [ZuyuJsonRead jsonRead:itme WithKey:@"CreateId"];
                model.CreateTime = [ZuyuJsonRead jsonRead:itme WithKey:@"CreateTime"];
                model.Id = [ZuyuJsonRead jsonRead:itme WithKey:@"Id"];
                model.SchoolId = [ZuyuJsonRead jsonRead:itme WithKey:@"SchoolId"];
                model.VenueAddress = [ZuyuJsonRead jsonRead:itme WithKey:@"VenueAddress"];
                model.VenueName = [ZuyuJsonRead jsonRead:itme WithKey:@"VenueName"];
                model.CreateId = [ZuyuJsonRead jsonRead:itme WithKey:@"CreateId"];
                model.jingdu    =  [[ZuyuJsonRead jsonRead:itme WithKey:@"VenueLatitude"] floatValue ];
                model.weidu    =  [[ZuyuJsonRead jsonRead:itme WithKey:@"VenueLongitude"] floatValue ];
                
                
                MAPointAnnotation *pointAnnotation = [[MAPointAnnotation alloc] init];
                
                pointAnnotation.coordinate = CLLocationCoordinate2DMake(model.jingdu, model.weidu);
                pointAnnotation.title = model.VenueName;
                pointAnnotation.subtitle = model.VenueAddress;
                [_easyannotationViewArray addObject: pointAnnotation];
                
                [_easyMachineDataArray addObject:model];
                
            }
            
        }else if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Code"]].integerValue == 2001){
            
            [ZuyuAlertShow alertShow:@"账号不存在" viewController: self];
            
            [[NSUserDefaults standardUserDefaults] setObject:@"0" forKey:@"isLogin"];
            [[NSUserDefaults standardUserDefaults] synchronize];
            [self.navigationController popViewControllerAnimated:YES];
            
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        NSHTTPURLResponse * responses = (NSHTTPURLResponse *)task.response;
        
        if (responses.statusCode == 401) {
            
            [ZuyuTokenRefresh tokenRefreshSuccess:^(NSURLSessionDataTask * _Nonnull dataTask, id  _Nullable responseObject) {
                
                [self requestEasyData];
                
            } failure:^(NSURLSessionDataTask * _Nullable dataTask, NSError * _Nonnull error) {
                
                
            }];
        }
    }];
}





- (MAAnnotationView *)mapView:(MAMapView *)mapView viewForAnnotation:(id <MAAnnotation>)annotation
{
    if ([annotation isKindOfClass:[MAPointAnnotation class]])
    {
        static NSString *pointReuseIndentifier = @"pointReuseIndentifier";
        MAPinAnnotationView*annotationView = (MAPinAnnotationView*)[mapView dequeueReusableAnnotationViewWithIdentifier:pointReuseIndentifier];
        if (annotationView == nil)
        {
            annotationView = [[MAPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:pointReuseIndentifier];
            
            NSLog(@"xxxx");
        }
        annotationView.canShowCallout= YES;       //设置气泡可以弹出，默认为NO
        annotationView.animatesDrop = YES;        //设置标注动画显示，默认为NO
        annotationView.draggable = NO;        //设置标注可以拖动，默认为NO
        annotationView.pinColor = MAPinAnnotationColorPurple;
        return annotationView;
    }
    return nil;
}



/*
 点击气泡
 */
- (void)mapView:(MAMapView *)mapView didAnnotationViewCalloutTapped:(MAAnnotationView *)view
{
    
    if (_page == 1) {
        int con = -1;
        
        for (int i = 0; i< _annotationViewArray.count; i++) {
            
            MAPointAnnotation *antion = _annotationViewArray[i];
            
            if (antion == view.annotation) {
                con = i;
                break;
            }
            
        }
        
        
        if (con+1) {
            MapModel *model = _dataArray[con];
            
            
            NSDictionary *parameter = [NSDictionary dictionaryWithObjectsAndKeys:_bookID,@"InteBookId",
                                       model.Id,@"EquipmentId",
                                       nil];
            
            [self orderNetwork:parameter];

        }
    }else{
        int con = -1;
        
        for (int i = 0; i< _easyannotationViewArray.count; i++) {
            
            MAPointAnnotation *antion = _easyannotationViewArray[i];
            
            if (antion == view.annotation) {
                con = i;
                break;
            }
            
        }
        
        
        if (con+1) {
            EntityEasyMachineModel *model = _easyMachineDataArray[con];
            
            NSDictionary *parameter = [NSDictionary dictionaryWithObjectsAndKeys:_bookID,@"InteBookId",
                                       model.Id,@"VenueId",
                                       nil];
            
            [self orderNetwork:parameter];
            
        }
        
    }
    
    
    
}

#pragma mark - 预约书本的请求
-(void)orderNetwork:(NSDictionary *)parameter
{
    if ([ISLOGIN integerValue]) {
        MBProgressHUD *_hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        
        _hud.label.text = NSLocalizedString(@"请求中...", @"HUD loading title");
        
        AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
        NSString *HeaderField = [NSString stringWithFormat:@"Bearer %@",TOKEN];
        
        [manager.requestSerializer setValue:HeaderField  forHTTPHeaderField:@"Authorization"];
        
        
        [manager POST:PORT(@"BespeakRecord/Bespeak") parameters:parameter progress:^(NSProgress * _Nonnull uploadProgress) {
            
        } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            
            if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]].integerValue) {
                _hud.mode = MBProgressHUDModeText;
                _hud.label.text = NSLocalizedString(@"预约成功", @"HUD message title");
                [_hud hideAnimated:YES afterDelay:1];
                
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
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            
            NSHTTPURLResponse * responses = (NSHTTPURLResponse *)task.response;
            
            if (responses.statusCode == 401) {
                
                [_hud hideAnimated:YES ];
                
                [ZuyuTokenRefresh tokenRefreshSuccess:^(NSURLSessionDataTask * _Nonnull dataTask, id  _Nullable responseObject) {
                    
                    [self orderNetwork:parameter];
                    
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
    }else{
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nil message:@"请先登录" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
    }
    
   
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
