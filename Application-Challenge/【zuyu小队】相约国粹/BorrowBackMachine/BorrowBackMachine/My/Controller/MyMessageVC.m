//
//  MyMessageVC.m
//  BorrowBackMachine
//
//  Created by zuyu on 2018/10/9.
//  Copyright © 2018年 zuyu. All rights reserved.
//

#import "MyMessageVC.h"
#import "zuyu.h"
#import "MyMessageCell.h"
#import "ZLPhotoActionSheet.h"
#import "RevisePhoneNumController.h"


@interface MyMessageVC ()<NavgationViewDelegate,UITableViewDelegate,UITableViewDataSource>
{
    UITableView *_tableView;
    ZLPhotoActionSheet *_imageActionSheet;
    MBProgressHUD *_hud;


}
@property (nonatomic, strong) NSMutableArray<UIImage *> *lastSelectPhotos;
@property (nonatomic, strong) NSMutableArray<PHAsset *> *lastSelectAssets;
@property (nonatomic, strong) NSArray *arrDataSources;
@property (nonatomic, assign) BOOL isOriginal;
@end

@implementation MyMessageVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self createNavgation];
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
    view.titleStr = @"修改资料";
    [self.view addSubview:view];
    self.view.backgroundColor = RGBA(246, 245, 242, 1);
    
}

-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}


#pragma mark - UITableView

-(void)createTableView
{
    _tableView = [[UITableView alloc ] initWithFrame:CGRectMake(0, 74, WIDTH, 240) style:UITableViewStylePlain];
    
    _tableView.delegate = self;
    
    _tableView.dataSource = self;
    
    _tableView.rowHeight = 80;
    
    [self.view addSubview:_tableView];
    
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 3;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    MyMessageCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MyMessageCell"];
    
    if (cell == nil) {
        cell = [[MyMessageCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"MyMessageCell" withIndexRow:indexPath.row];
        
        
    }
    
    cell.image = HEADIMAGE;
    if (indexPath.row == 2) {
        
        NSString *phoneString = PhoneNum;
        
        if (phoneString.length == 11) {
            
            phoneString = [NSString stringWithFormat:@" 手机号 : %@****%@",[phoneString substringToIndex:3],[phoneString substringFromIndex:7] ];
            
            cell.title = phoneString;
        }
    }else{
        cell.title = [NSString stringWithFormat:@"昵称 : %@",USERNAME];
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if (indexPath.row == 1) {
        
        UIAlertController *alertVc = [UIAlertController alertControllerWithTitle:@"提示" message:nil preferredStyle:
                                      UIAlertControllerStyleAlert];
        // 添加输入框 (注意:在UIAlertControllerStyleActionSheet样式下是不能添加下面这行代码的)
        [alertVc addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
            textField.placeholder = @"请输入新昵称";
        }];
        UIAlertAction *action1 = [UIAlertAction actionWithTitle:@"确认" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
            // 通过数组拿到textTF的值
            NSString * newName = [[alertVc textFields] objectAtIndex:0].text;
            
            if (newName.length > 12) {
                MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                hud.mode = MBProgressHUDModeText;
                hud.label.text = NSLocalizedString(@"昵称长度不能超过12位", @"HUD message title");
                [hud hideAnimated:YES afterDelay:1];
            }else{
                [self reviseName:newName];

            }
        }];
        UIAlertAction *action2 = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
        // 添加行为
        [alertVc addAction:action2];
        [alertVc addAction:action1];
        [self presentViewController:alertVc animated:YES completion:nil];
        
    }else if(indexPath.row == 2){
        
        RevisePhoneNumController *vc = [[RevisePhoneNumController alloc] init];
        
        vc.hidesBottomBarWhenPushed = YES;
        
        [self.navigationController pushViewController:vc animated:YES];
//        NSLog(@"%@\n%@",PhoneNum,CardID);
        
    }else{
        [self selectImage];
    }
    
}

#pragma mark - 昵称
-(void)reviseName:(NSString *)newName
{
    
    MBProgressHUD * hud;

    
    hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    
    hud.label.text = NSLocalizedString(@"上传中...", @"HUD loading title");
    
    NSString *HeaderField = [NSString stringWithFormat:@"Bearer %@",TOKEN];
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    [manager.requestSerializer setValue:HeaderField  forHTTPHeaderField:@"Authorization"];
    
    NSDictionary *parameters = [[NSDictionary alloc] initWithObjectsAndKeys:HEADIMAGE,@"HeadImgUrl",newName,@"Name", nil];
    
    [manager POST:PORT(@"Account/ModifyMyBasicInfo") parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        if ([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]] integerValue]){
            
            [[NSUserDefaults standardUserDefaults] setObject:newName forKey:@"userName"];
            
            [[NSUserDefaults standardUserDefaults] synchronize];
    
            
            
            hud.mode = MBProgressHUDModeText;
            hud.label.text = NSLocalizedString(@"名字修改成功", @"HUD message title");
            
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [hud hideAnimated:YES ];
                [self.navigationController popViewControllerAnimated:YES];
            });
            
        }else if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Code"]].integerValue == 2001){
            
            [ZuyuAlertShow alertShow:@"账号不存在" viewController: self];
            
            [[NSUserDefaults standardUserDefaults] setObject:@"0" forKey:@"isLogin"];
            [[NSUserDefaults standardUserDefaults] synchronize];
            [self.navigationController popViewControllerAnimated:YES];
            
        }else{
            
            hud.mode = MBProgressHUDModeText;
            hud.label.text = NSLocalizedString(@"上传失败", @"HUD message title");
            
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [hud hideAnimated:YES ];
            });
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        
        NSHTTPURLResponse * responses = (NSHTTPURLResponse *)task.response;
        
        if (responses.statusCode == 401) {
            
            [hud hideAnimated:YES ];
            
            [ZuyuTokenRefresh tokenRefreshSuccess:^(NSURLSessionDataTask * _Nonnull dataTask, id  _Nullable responseObject) {
                
                [self reviseName:newName];
                
            } failure:^(NSURLSessionDataTask * _Nullable dataTask, NSError * _Nonnull error) {
                
            }];
            
        }else{
            hud.mode = MBProgressHUDModeText;
            hud.label.text = NSLocalizedString(@"上传失败", @"HUD message title");
            [hud hideAnimated:YES afterDelay:1];
        }
        
    }];
}


#pragma mark - 头像
-(void)selectImage
{
    _imageActionSheet = [[ZLPhotoActionSheet alloc] init];
    //设置照片最大预览数
    _imageActionSheet.configuration.maxPreviewCount = 0;
    //设置照片最大选择数
    _imageActionSheet.configuration.maxSelectCount = 1;
    //是否在选择图片后直接进入编辑界面
    _imageActionSheet.configuration.editAfterSelectThumbnailImage = NO;
    //系统语言
    _imageActionSheet.configuration.languageType = 0;
    //记录
    _imageActionSheet.arrSelectedAssets = self.lastSelectAssets;
    
    //是否允许框架解析图片
    _imageActionSheet.configuration.shouldAnialysisAsset = YES;
    
    _imageActionSheet.configuration.allowEditImage = YES;
    
    _imageActionSheet.configuration.navBarColor = COLOR;
    
    _imageActionSheet.configuration.allowSelectVideo = NO;
    
    _imageActionSheet.sender = self;
    
    //选择回调
    
    __weak typeof(self) weakSelf = self;
    
    [_imageActionSheet setSelectImageBlock:^(NSArray<UIImage *> * _Nonnull images, NSArray<PHAsset *> * _Nonnull assets, BOOL isOriginal) {
        //your codes
        
        //        _lastSelectPhotos = images.mutableCopy;
        //        _lastSelectAssets = assets.mutableCopy;
        _isOriginal = isOriginal;
        
        NSLog(@"^^^^^^^^^^^^________image\n - %@",images);
        
        dispatch_async(dispatch_get_main_queue(), ^{
                //直接上传
            [weakSelf postPhoto:images[0]];
        });
        
    }];
    
    [_imageActionSheet showPreviewAnimated:YES];
    
    _imageActionSheet.cancleBlock = ^{
        NSLog(@"取消选择图片");
    };
}

-(void)postPhoto:(UIImage *)postImage
{
    
    _hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    _hud.label.text = NSLocalizedString(@"上传中...", @"HUD loading title");
    UIImage *img = postImage;
    NSData *data = UIImageJPEGRepresentation(img, 0.5);
    NSDate *date=[NSDate date];
    NSDateFormatter *format=[[NSDateFormatter alloc] init];
    [format setDateFormat:@"MM-dd-HH-mm-ss"];
    NSString *dateStr = [format stringFromDate:date];
    NSString *fileName = [NSString stringWithFormat:@"%@.png",dateStr];
    
    [ZuyuPostManger uploadWithURL:PostFileSevers baseURL:@"" params:nil fileData:data name:@"file" fileName:fileName mimeType:@"image/png" progress:^(NSProgress *progress) {
        NSLog(@"%lli,%lli",progress.completedUnitCount,progress.totalUnitCount);
    } success:^(NSURLSessionDataTask *task, id responseObject) {
        NSLog(@"%@responseObject   ======== ",responseObject);
        
        NSString *url = [[responseObject objectForKey:@"Data"] objectForKey:@"Url"];
        
        NSDictionary *dic = [[NSDictionary alloc] initWithObjectsAndKeys:[NSString stringWithFormat:@"%@",url],@"HeadImgUrl",USERNAME,@"Name", nil];
      
        [self postUrlToServers:dic];
        
    } fail:^(NSURLSessionDataTask *task, NSError *error) {
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.38 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"网络错误", @"HUD message title");
            [_hud hideAnimated:YES afterDelay: 1];
            
        });
        NSLog(@"%@ -========error ",error);
    }];
    
}


-(void)postUrlToServers:(NSDictionary *)parameter
{
    
    NSString *HeaderField = [NSString stringWithFormat:@"Bearer %@",TOKEN];
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    [manager.requestSerializer setValue:HeaderField  forHTTPHeaderField:@"Authorization"];
    
    [manager POST:PORT(@"Account/ModifyMyBasicInfo") parameters:parameter progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        if ([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"IsDone"]] integerValue]){
            
            NSString *url = [parameter objectForKey:@"HeadImgUrl"];
            url = [NSString stringWithFormat:@"%@%@",FILE,url];
            
            [[NSUserDefaults standardUserDefaults] setObject:url forKey:@"userHeadImage"];
            
            [[NSUserDefaults standardUserDefaults] synchronize];
            
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"头像上传成功", @"HUD message title");
            
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [_hud hideAnimated:YES ];
                [self.navigationController popViewControllerAnimated:YES];
            });
        
        }else if ([NSString stringWithFormat:@"%@",[responseObject objectForKey:@"Code"]].integerValue == 2001){
            
            [ZuyuAlertShow alertShow:@"账号不存在" viewController: self];
            
            [[NSUserDefaults standardUserDefaults] setObject:@"0" forKey:@"isLogin"];
            [[NSUserDefaults standardUserDefaults] synchronize];
            [self.navigationController popViewControllerAnimated:YES];
            
        }else{
            
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"请求超时，请重新上传", @"HUD message title");
            _hud.label.numberOfLines = 0;
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [_hud hideAnimated:YES ];
            });
        }
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        
        NSHTTPURLResponse * responses = (NSHTTPURLResponse *)task.response;
        
        if (responses.statusCode == 401) {
            
            [_hud hideAnimated:YES ];
            
            [ZuyuTokenRefresh tokenRefreshSuccess:^(NSURLSessionDataTask * _Nonnull dataTask, id  _Nullable responseObject) {
                
                [self postUrlToServers:parameter];
                
            } failure:^(NSURLSessionDataTask * _Nullable dataTask, NSError * _Nonnull error) {
                
                
            }];
            
            
        }else{
            
            _hud.mode = MBProgressHUDModeText;
            _hud.label.text = NSLocalizedString(@"请求超时，请重新上传", @"HUD message title");
            _hud.label.numberOfLines = 0;
            [_hud hideAnimated:YES afterDelay:1];

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
