//
//  ReleaseViewController.m
//  Baby back home
//
//  Created by zhangyu on 2021/5/12.
//

#import "ReleaseViewController.h"
#import "XXCityPickerView.h"
#import "MessageDBManager.h"
#import "BBMessageModel.h"
#import "ChoosePhotoAndVideoController.h"
#import "HXPhotoView.h"
#import <MBProgressHUD.h>
#import "BBAddShadowTool.h"
#import "ACFloatingTextField.h"
static const CGFloat kPhotoViewMargin = 12.0;


@interface ReleaseViewController ()<XXCityPickerViewDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate,HXPhotoViewDelegate>
@property (strong, nonatomic) HXPhotoManager *manager;

@property (strong, nonatomic) UIScrollView *scrollView;

@property (strong, nonatomic) HXPhotoView *photoView;

@property(nonatomic,strong)UILabel *cityAddressLB;

@property(nonatomic,strong)UIButton *btn;

@property(nonatomic,weak)UIButton *manBtn;

@property(nonatomic,weak)UIButton *womenBtn;


@property(nonatomic,weak)UIImageView *img;

@property(nonatomic,strong)XXCityPickerView *cityPicker;

@property(nonatomic,weak)UITextField  *titleTF;
@property(nonatomic,weak)UITextField  *nameTF;
@property(nonatomic,weak)UITextField  *genderTF;
@property(nonatomic,weak)UITextView  *desTF;
@property(nonatomic,weak)UITextField  *phoneTF;
@property(copy, nonatomic) NSArray *selectList;
@property(copy, nonatomic) NSMutableArray *choosePic;
@property(weak, nonatomic) UIView * citylabelBottomView;


@property(strong,nonatomic)MBProgressHUD * insertHUD;

@end

@implementation ReleaseViewController

- (NSMutableArray *)choosePic{
    if (!_choosePic) {
        _choosePic = [NSMutableArray array];
    }
    return _choosePic;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = UIColor.whiteColor;
    [self initSubviews];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(insertDBsuccess) name:@"insertDBsuccess" object:nil];
    // Do any additional setup after loading the view.
}
- (void)insertDBsuccess{
    dispatch_sync(dispatch_get_main_queue(), ^{
        [self.insertHUD hideAnimated:YES];
    });
   
}
- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self.navigationController.navigationBar setHidden:YES];
}


- (void)initSubviews{
    
    CGFloat lbx = 50 * KScreenRatio;
    CGFloat tfx = 220 * KScreenRatio;
    
    UIView * topShadowView = [[UIView alloc] initWithFrame:(CGRectMake(0, 0, KScreenWidth, 134 * KScreenRatio))];
    topShadowView.backgroundColor = UIColor.whiteColor;
    [self.view addSubview:topShadowView];
    [BBAddShadowTool addShadowToViewOnlyBottom:topShadowView];
    
    UILabel * lb = [[UILabel alloc] init];
    lb.width = 200;
    lb.centerX = self.view.centerX;
    lb.y = 30;
    lb.height = 25;
    lb.text = @"发布信息";
    lb.textAlignment = NSTextAlignmentCenter;
    lb.textColor = UIColor.blackColor;
    lb.font = [UIFont systemFontOfSize:20];
    [topShadowView addSubview:lb];
    
    UILabel * titleLB = [[UILabel alloc] init];
    titleLB.x = lbx;
    titleLB.y = 100;
    titleLB.width = KScreenRatio * 120;
    titleLB.height = 25;
    titleLB.text = @"标题";
    titleLB.textColor = KFontColor;
    [self.view addSubview:titleLB];
    
    ACFloatingTextField * titleTF = [[ACFloatingTextField alloc] init];
    titleTF.x = lbx;
    titleTF.y = 120;
    titleTF.width = KScreenWidth - (2 * lbx);
    titleTF.height = 25;
    titleTF.placeholder = @"请输入标题";
    titleTF.disableFloatingLabel = YES;
    self.titleTF = titleTF;
    [self.view addSubview:titleTF];
    
    UILabel * desLB = [[UILabel alloc] init];
    desLB.x = lbx;
    desLB.y = 160;
    desLB.width = KScreenRatio * 120;
    desLB.height = 25;
    desLB.text = @"描述";
    desLB.textColor = KFontColor;
    [self.view addSubview:desLB];
    
    UITextView * desTV = [[UITextView alloc] init];
    desTV.x = lbx;
    desTV.y = 190;
    desTV.width =  KScreenWidth - (2 * lbx);
    desTV.height = 105;
    desTV.layer.cornerRadius = 5;
    desTV.layer.borderColor = KFontColor.CGColor;
    desTV.layer.borderWidth = 0.3;
    self.desTF = desTV;
    [self.view addSubview:desTV];
    
    UILabel * nameLB = [[UILabel alloc] init];
    nameLB.x = lbx;
    nameLB.y = 300;
    nameLB.width = KScreenRatio * 220;
    nameLB.height = 25;
    nameLB.text = @"被寻人姓名";
    nameLB.textColor = KFontColor;
    [self.view addSubview:nameLB];
    
    ACFloatingTextField * nameTF = [[ACFloatingTextField alloc] init];
    nameTF.x = lbx;
    nameTF.y = 320;
    nameTF.width = KScreenWidth - (2 * lbx);
    nameTF.height = 25;
    nameTF.placeholder = @"请输入被寻人姓名";
    self.nameTF = nameTF;
    [self.view addSubview:nameTF];
    
    
    
    
    
    
    UILabel * cityLB = [[UILabel alloc] init];
    cityLB.x = lbx;
    cityLB.y = 360;
    cityLB.width = KScreenRatio * 200;
    cityLB.height = 25;
    cityLB.text = @"丢失城市";
    cityLB.textColor = KFontColor;
    [self.view addSubview:cityLB];
    
    UILabel * cityTF = [[UILabel alloc] init];
    cityTF.x = lbx;
    cityTF.y = 380;
    cityTF.width = KScreenWidth - (2 * lbx);
    cityTF.height = 25;
    
    [self.view addSubview:cityTF];
    self.cityAddressLB = cityTF;
    UITapGestureRecognizer * chooseTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(cityPicker)];
    [cityTF addGestureRecognizer:chooseTap];
    cityTF.userInteractionEnabled = YES;
    
    UIView * citylabelBottomView = [[UIView alloc] init];
    citylabelBottomView.x = lbx;
    citylabelBottomView.y = 403;
    citylabelBottomView.width = KScreenWidth - (2 * lbx);
    citylabelBottomView.height = 1;
    citylabelBottomView.backgroundColor = [UIColor colorWithWhite:230/255.0 alpha:1];;
    self.citylabelBottomView = citylabelBottomView;
    citylabelBottomView.userInteractionEnabled = YES;
    [self.view addSubview:citylabelBottomView];
    
    
    UILabel * contactLB = [[UILabel alloc] init];
    contactLB.x = lbx;
    contactLB.y = 420;
    contactLB.width = KScreenRatio * 180;
    contactLB.height = 25;
    contactLB.text = @"联系方式";
    contactLB.textColor = KFontColor;
    [self.view addSubview:contactLB];
    
    ACFloatingTextField * contactTF = [[ACFloatingTextField alloc] init];
    contactTF.x = lbx;
    contactTF.y = 440;
    contactTF.width = KScreenWidth - (2 * lbx);
    contactTF.height = 25;
    contactTF.keyboardType = UIKeyboardTypeNumberPad;
    contactTF.placeholder = @"请输入您的联系方式";
    self.phoneTF = contactTF;
    [self.view addSubview:contactTF];
    
    UILabel * genderLB = [[UILabel alloc] init];
    genderLB.x = lbx;
    genderLB.y = 475;
    genderLB.width = KScreenRatio * 120;
    genderLB.height = 25;
    genderLB.text = @"性别";
    genderLB.textColor = KFontColor;
    [self.view addSubview:genderLB];
    
    UILabel * manLB = [[UILabel alloc] init];
    manLB.text = @"男";
    manLB.textColor = KFontColor;
    manLB.x = tfx;
    manLB.y = 475;
    manLB.width = 17;
    manLB.height = 25;
    [self.view addSubview:manLB];
    
    UIButton * manBtn = [UIButton buttonWithType:(UIButtonTypeCustom)];
    manBtn.x = tfx + 20;
    manBtn.y = 475;
    manBtn.width = 25;
    manBtn.height = 25;
    [manBtn setImage:[UIImage imageNamed:@"genderNormal"] forState:(UIControlStateNormal)];
    [manBtn setImage:[UIImage imageNamed:@"genderSelected"] forState:(UIControlStateSelected)];
    [manBtn addTarget:self action:@selector(chooseGender:) forControlEvents:(UIControlEventTouchUpInside)];
    self.manBtn = manBtn;
    [self.view addSubview:manBtn];
    self.manBtn.selected = true;
   
    UILabel * womenLB = [[UILabel alloc] init];
    womenLB.text = @"女";
    womenLB.textColor = KFontColor;
    womenLB.x = tfx + 70;
    womenLB.y = 475;
    womenLB.width = 17;
    womenLB.height = 25;
    [self.view addSubview:womenLB];
    
    UIButton * womenBtn = [UIButton buttonWithType:(UIButtonTypeCustom)];
    womenBtn.x = tfx + 90;
    womenBtn.y = 475;
    womenBtn.width = 25;
    womenBtn.height = 25;
    [womenBtn setImage:[UIImage imageNamed:@"genderNormal"] forState:(UIControlStateNormal)];
    [womenBtn setImage:[UIImage imageNamed:@"genderSelected"] forState:(UIControlStateSelected)];
    [womenBtn addTarget:self action:@selector(chooseGender:) forControlEvents:(UIControlEventTouchUpInside)];
    self.womenBtn = womenBtn;
    [self.view addSubview:womenBtn];
    
    
    UILabel * picLB = [[UILabel alloc] init];
    picLB.x = lbx;
    picLB.y = 510;
    picLB.width = KScreenRatio * 120;
    picLB.height = 25;
    picLB.text = @"照片";
    picLB.textColor = KFontColor;
    [self.view addSubview:picLB];
    
//    UIImageView * img = [[UIImageView alloc] initWithFrame:(CGRectMake(KScreenRatio * 50, 540, KScreenRatio * 200, KScreenRatio * 200))];
//    self.img = img;
//    [self.view addSubview:img];
    
    UIScrollView *scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(lbx, 540, KScreenWidth - (KScreenRatio * 2 * lbx), 300 * KScreenRatio)];
    scrollView.alwaysBounceVertical = YES;
    self.scrollView = scrollView;
    [self.view addSubview:scrollView];
   
    
    CGFloat width = scrollView.frame.size.width;
    HXPhotoView *photoView = [[HXPhotoView alloc] initWithFrame:CGRectMake(0, 0, width, 0) manager:self.manager];
    photoView.delegate = self;
    photoView.spacing = 7;
    photoView.lineCount = 3;
    photoView.collectionView.contentInset = UIEdgeInsetsMake(16, 16, 16, 16);
    self.photoView = photoView;
    [scrollView addSubview:photoView];
    
    
    UIButton * saveBtn = [UIButton buttonWithType:(UIButtonTypeCustom)];
    saveBtn.frame = CGRectMake(KScreenRatio * 100, KScreenHeight - 180, KScreenWidth - KScreenRatio * 200, 50);
//    saveBtn.centerX = self.view.centerX;
    saveBtn.layer.cornerRadius = 25;
    saveBtn.layer.masksToBounds = YES;
    [saveBtn setTitle:@"发布信息" forState:(UIControlStateNormal)];
    saveBtn.backgroundColor = KBGreenColor2;
    [saveBtn setTitleColor:UIColor.whiteColor forState:(UIControlStateNormal)];
    [self.view addSubview:saveBtn];
    [saveBtn addTarget:self action:@selector(didSaveImg) forControlEvents:(UIControlEventTouchUpInside)];
    
}

- (HXPhotoManager *)manager {
    if (!_manager) {
        _manager = [[HXPhotoManager alloc] initWithType:HXPhotoManagerSelectedTypePhotoAndVideo];
        _manager.configuration.openCamera = YES;
        _manager.configuration.photoMaxNum = 9;
        _manager.configuration.videoMaxNum = 9;
        _manager.configuration.maxNum = 18;

    }
    return _manager;
}

- (void)chooseGender:(UIButton *)sender{
    if (sender == self.manBtn) {
        sender.selected = true;
        self.womenBtn.selected = false;
    }else{
        sender.selected = true;
        self.manBtn.selected = false;
    }
}

- (void)saveLocalMessage:(NSString *)mp4Address{
    
    if (self.titleTF.text.length == 0) {
        UIAlertController * alert = [UIAlertController alertControllerWithTitle:@"提示" message:@"标题不能为空" preferredStyle:(UIAlertControllerStyleAlert)];
        UIAlertAction * action = [UIAlertAction actionWithTitle:@"确认" style:(UIAlertActionStyleDestructive) handler:^(UIAlertAction * _Nonnull action) {

        }];
        [alert addAction:action];
        [self presentViewController:alert animated:YES completion:nil];
        return;
    }

    if (self.desTF.text.length == 0) {
        UIAlertController * alert = [UIAlertController alertControllerWithTitle:@"提示" message:@"描述不能为空" preferredStyle:(UIAlertControllerStyleAlert)];
        UIAlertAction * action = [UIAlertAction actionWithTitle:@"确认" style:(UIAlertActionStyleDestructive) handler:^(UIAlertAction * _Nonnull action) {

        }];

        [alert addAction:action];
        [self presentViewController:alert animated:YES completion:nil];
        return;
    }

    if (self.nameTF.text.length == 0) {
        UIAlertController * alert = [UIAlertController alertControllerWithTitle:@"提示" message:@"被寻人姓名不能为空" preferredStyle:(UIAlertControllerStyleAlert)];
        UIAlertAction * action = [UIAlertAction actionWithTitle:@"确认" style:(UIAlertActionStyleDestructive) handler:^(UIAlertAction * _Nonnull action) {

        }];

        [alert addAction:action];
        [self presentViewController:alert animated:YES completion:nil];
        return;
    }

    

    if (self.phoneTF.text.length == 0) {
        UIAlertController * alert = [UIAlertController alertControllerWithTitle:@"提示" message:@"联系方式不能为空" preferredStyle:(UIAlertControllerStyleAlert)];
        UIAlertAction * action = [UIAlertAction actionWithTitle:@"确认" style:(UIAlertActionStyleDestructive) handler:^(UIAlertAction * _Nonnull action) {

        }];
        [alert addAction:action];
        [self presentViewController:alert animated:YES completion:nil];
        return;
    }

    if (self.cityAddressLB.text.length == 0) {
        UIAlertController * alert = [UIAlertController alertControllerWithTitle:@"提示" message:@"地址不能为空" preferredStyle:(UIAlertControllerStyleAlert)];
        UIAlertAction * action = [UIAlertAction actionWithTitle:@"确认" style:(UIAlertActionStyleDestructive) handler:^(UIAlertAction * _Nonnull action) {

        }];
        [alert addAction:action];
        [self presentViewController:alert animated:YES completion:nil];
        return;
    }
    
   
    
    BBMessageModel * model = [[BBMessageModel alloc] init];
    model.address = self.cityAddressLB.text;
    model.title = self.titleTF.text;
    model.des = self.desTF.text;
    model.phoneNum = self.phoneTF.text;
    model.imgName = [NSString stringWithFormat:@"%@.%@",model.title,model.phoneNum];
    
    model.pName = self.nameTF.text;
    if (self.manBtn.selected == true) {
        model.pGender = @"男";
    }else{
        model.pGender = @"女";
    }
    model.userid = [EMClient sharedClient].currentUsername;
    NSMutableArray * nameArr = [NSMutableArray array];
    for (int i = 0; i < self.choosePic.count; i++) {
        NSString * append;
        if (i > 0) {
             append = [NSString stringWithFormat:@",%@-%d",model.imgName,i];
        }else{
            append = [NSString stringWithFormat:@"%@-%d",model.imgName,i];
        }
        
        [nameArr addObject:append];
        NSString * resultStr =  [append stringByReplacingOccurrencesOfString:@"," withString:@""];
        [self saveFile:self.choosePic[i] andImageName:resultStr];
    }
    NSString * modelName = @"";
    for (int i = 0; i < nameArr.count; i++) {
        modelName = [modelName stringByAppendingString:nameArr[i]];
        NSLog(@"%@",nameArr[i]);
    }
    
    if (mp4Address.length > 0) {
        model.imgName = mp4Address;
    }else{
        model.imgName = modelName;
    }
    [[MessageDBManager sharedInstance] addMessage:@[model]];
    [self.choosePic removeAllObjects];
    [self dismissViewControllerAnimated:YES completion:^{
        self.titleTF.text = @"";
        self.cityAddressLB.text = @"";
        self.desTF.text = @"";
        self.phoneTF.text = @"";
        self.nameTF.text = @"";
        self.genderTF.text = @"";
        [[NSNotificationCenter defaultCenter] postNotificationName:@"jumpToHomeVC" object:nil];
    }];
    
    
    
}
//验证电话号码格式
- (BOOL)validateEmail:(NSString *)mobileNum
{
    NSString *MOBILE = @"^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|7[0678])\\d{8}$";
    NSPredicate *regextestmobile = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", MOBILE];
    return [regextestmobile evaluateWithObject:mobileNum];
}


- (void)didSaveImg {
    // 如果将_manager.configuration.requestImageAfterFinishingSelection 设为YES，
    // 那么在选择完成的时候就会获取图片和视频地址
    // 如果选中了原图那么获取图片时就是原图
    // 获取视频时如果设置 exportVideoURLForHighestQuality 为YES，则会去获取高等质量的视频。其他情况为中等质量的视频
    // 个人建议不在选择完成的时候去获取，因为每次选择完都会去获取。获取过程中可能会耗时过长
    // 可以在要上传的时候再去获取
    self.insertHUD = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
   __block int indicatot = 0;
    for (HXPhotoModel *model in self.selectList) {
        // 数组里装的是所有类型的资源，需要判断
        // 先判断资源类型
        if (model.subType == HXPhotoModelMediaSubTypePhoto) {
            // 当前为图片
            if (model.photoEdit) {
                // 如果有编辑数据，则说明这张图篇被编辑过了
                // 需要这样才能获取到编辑之后的图片
//                model.photoEdit.editPreviewImage;
                return;
            }
            // 再判断具体类型
            if (model.type == HXPhotoModelMediaTypeCameraPhoto) {
                // 到这里就说明这张图片不是手机相册里的图片，可能是本地的也可能是网络图片
                // 关于相机拍照的的问题，当系统 < ios9.0的时候拍的照片虽然保存到了相册但是在列表里存的是本地的，没有PHAsset
                // 当系统 >= ios9.0 的时候拍的照片就不是本地照片了，而是手机相册里带有PHAsset对象的照片
                // 这里的 model.asset PHAsset是空的
                // 判断具体类型
                if (model.cameraPhotoType == HXPhotoModelMediaTypeCameraPhotoTypeLocal) {
                    // 本地图片
                
                }else if (model.cameraPhotoType == HXPhotoModelMediaTypeCameraPhotoTypeLocalGif) {
                    // 本地gif图片
                    
                }else if (model.cameraPhotoType == HXPhotoModelMediaTypeCameraPhotoTypeNetWork) {
                    // 网络图片
                
                }else if (model.cameraPhotoType == HXPhotoModelMediaTypeCameraPhotoTypeNetWorkGif) {
                    // 网络gif图片
                    
                }else if (model.cameraPhotoType == HXPhotoModelMediaTypeCameraPhotoTypeLocalLivePhoto) {
                    // 本地livePhoto
                    
                }else if (model.cameraPhotoType == HXPhotoModelMediaTypeCameraPhotoTypeNetWorkLivePhoto) {
                    // 网络livePhoto
                    
                }
                // 上传图片的话可以不用判断具体类型，按下面操作取出图片
                if (model.networkPhotoUrl) {
                    // 如果网络图片地址有值就说明是网络图片，可直接拿此地址直接使用。避免重复上传
                    // 这里需要注意一下，先要判断是否为图片。因为如果是网络视频的话此属性代表视频封面地址
                    
                }else {
                    // 网络图片地址为空了，那就肯定是本地图片了
                    // 直接取 model.previewPhoto 或者 model.thumbPhoto，这两个是同一个image
                    [self.choosePic addObject:model.previewPhoto];
                    indicatot ++;
                }
            }else {
                // 到这里就是手机相册里的图片了 model.asset PHAsset对象是有值的
                // 如果需要上传 Gif 或者 LivePhoto 需要具体判断
                if (model.type == HXPhotoModelMediaTypePhoto) {
                    // 普通的照片，如果不可以查看和livePhoto的时候，这就也可能是GIF或者LivePhoto了，
                    // 如果你的项目不支持动图那就不要取NSData或URL，因为如果本质是动图的话还是会变成动图传上去
                    // 这样判断是不是GIF model.photoFormat == HXPhotoModelFormatGIF
                    
                    // 如果 requestImageAfterFinishingSelection = YES 的话，直接取 model.previewPhoto 或者 model.thumbPhoto 在选择完成时候已经获取并且赋值了
                    // 获取image
                    // size 就是获取图片的质量大小，原图的话就是 PHImageManagerMaximumSize，其他质量可设置size来获取
                    CGSize size = CGSizeMake(model.imageSize.width * 0.5, model.imageSize.height * 0.5);
                    
                    [model requestPreviewImageWithSize:size startRequestICloud:^(PHImageRequestID iCloudRequestId, HXPhotoModel * _Nullable model) {
                        // 如果图片是在iCloud上的话会先走这个方法再去下载
                    } progressHandler:^(double progress, HXPhotoModel * _Nullable model) {
                        // iCloud的下载进度
                    } success:^(UIImage * _Nullable image, HXPhotoModel * _Nullable model, NSDictionary * _Nullable info) {
                        // image
                        [self.choosePic addObject:image];
                        NSLog(@"%@",image);
                        indicatot ++;
                        if (indicatot == self->_selectList.count) {
                            [self saveLocalMessage:nil];
                        }
                    } failed:^(NSDictionary * _Nullable info, HXPhotoModel * _Nullable model) {
                        NSLog(@"%@",info);
                        // 获取失败
                    }];
                }else if (model.type == HXPhotoModelMediaTypePhotoGif) {
                    // 动图，如果 requestImageAfterFinishingSelection = YES 的话，直接取 model.imageURL。因为在选择完成的时候已经获取了不用再去获取
//                    model.imageURL;
                    // 上传动图时，不要直接拿image上传哦。可以获取url或者data上传
                    // 获取url
                    [model requestImageURLStartRequestICloud:nil progressHandler:nil success:^(NSURL * _Nullable imageURL, HXPhotoModel * _Nullable model, NSDictionary * _Nullable info) {
                        // 下载完成，imageURL 本地地址
                    } failed:nil];
                    
                    // 获取data
                    [model requestImageDataStartRequestICloud:nil progressHandler:nil success:^(NSData * _Nullable imageData, UIImageOrientation orientation, HXPhotoModel * _Nullable model, NSDictionary * _Nullable info) {
                        // imageData
                    } failed:nil];
                }else if (model.type == HXPhotoModelMediaTypeLivePhoto) {
                    // LivePhoto，requestImageAfterFinishingSelection = YES 时没有处理livephoto，需要自己处理
                    // 如果需要上传livephoto的话，需要上传livephoto里的图片和视频
                    // 展示的时候需要根据图片和视频生成livephoto
                    [model requestLivePhotoAssetsWithSuccess:^(NSURL * _Nullable imageURL, NSURL * _Nullable videoURL, BOOL isNetwork, HXPhotoModel * _Nullable model) {
                        // imageURL - LivePhoto里的照片封面地址
                        // videoURL - LivePhoto里的视频地址
                        
                    } failed:^(NSDictionary * _Nullable info, HXPhotoModel * _Nullable model) {
                        // 获取失败
                    }];
                }
                // 也可以不用上面的判断和方法获取，自己根据 model.asset 这个PHAsset对象来获取想要的东西
//                PHAsset *asset = model.asset;
                
                // 自由发挥
            }
            
        }else if (model.subType == HXPhotoModelMediaSubTypeVideo) {
            // 当前为视频
            if (model.type == HXPhotoModelMediaTypeVideo) {
                // 为手机相册里的视频
                // requestImageAfterFinishingSelection = YES 时，直接去 model.videoURL，在选择完成时已经获取了
//                model.videoURL;
                // 获取视频时可以获取 AVAsset，也可以获取 AVAssetExportSession，获取之后再导出视频
                // 获取 AVAsset
                [model requestAVAssetStartRequestICloud:nil progressHandler:nil success:^(AVAsset * _Nullable avAsset, AVAudioMix * _Nullable audioMix, HXPhotoModel * _Nullable model, NSDictionary * _Nullable info) {
                    // avAsset
                    // 自己根据avAsset去导出视频
                } failed:nil];
                
                // 获取 AVAssetExportSession
                [model requestAVAssetExportSessionStartRequestICloud:nil progressHandler:nil success:^(AVAssetExportSession * _Nullable assetExportSession, HXPhotoModel * _Nullable model, NSDictionary * _Nullable info) {
                    
                } failed:nil];
                
                // HXPhotoModel也提供直接导出视频地址的方法
                // presetName 导出视频的质量，自己根据需求设置
                [model exportVideoWithPresetName:AVAssetExportPresetMediumQuality startRequestICloud:nil iCloudProgressHandler:nil exportProgressHandler:^(float progress, HXPhotoModel * _Nullable model) {
                    // 导出视频时的进度，在iCloud下载完成之后
                } success:^(NSURL * _Nullable videoURL, HXPhotoModel * _Nullable model) {
                    // 导出完成, videoURL
                    NSLog(@"%@",videoURL);
                    [self saveLocalMessage:[NSString stringWithFormat:@"%@",videoURL]];
                } failed:nil];
                
                // 也可以不用上面的方法获取，自己根据 model.asset 这个PHAsset对象来获取想要的东西
//                PHAsset *asset = model.asset;
                // 自由发挥
            }else {
                // 本地视频或者网络视频
                if (model.cameraVideoType == HXPhotoModelMediaTypeCameraVideoTypeLocal) {
                    // 本地视频
                    // model.videoURL 视频的本地地址
                }else if (model.cameraVideoType == HXPhotoModelMediaTypeCameraVideoTypeNetWork) {
                    // 网络视频
                    // model.videoURL 视频的网络地址
                    // model.networkPhotoUrl 视频封面网络地址
                }
            }
        }
    }
}


#pragma mark - HXPhotoView
- (void)photoView:(HXPhotoView *)photoView changeComplete:(NSArray<HXPhotoModel *> *)allList photos:(NSArray<HXPhotoModel *> *)photos videos:(NSArray<HXPhotoModel *> *)videos original:(BOOL)isOriginal {
    NSSLog(@"%@",allList);
    self.selectList = allList;
   
}

- (void)photoView:(HXPhotoView *)photoView updateFrame:(CGRect)frame {
    NSSLog(@"%@",NSStringFromCGRect(frame));
    self.scrollView.contentSize = CGSizeMake(self.scrollView.frame.size.width, CGRectGetMaxY(frame) + kPhotoViewMargin);
    
}



- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event{
    [self.view endEditing:YES];
}

#pragma mark - XXCityPickerView

- (XXCityPickerView *)cityPicker{
    if (_cityPicker == nil){
        // 初始化  设置类别   1省  2省-市  3省-市-区
        _cityPicker = [[XXCityPickerView alloc]initWithComponents:3];
        // 设置代理
        _cityPicker.delegate = self;
        [self.view addSubview:_cityPicker];
        self.citylabelBottomView.backgroundColor = KBGreenColor2;
    }
    return  _cityPicker;
}

// ---- 【XXCityPickerViewDelegate】
// 取消
- (void)XXCityPickerViewLeftAction{
    [self removeCityPickerView];
    self.citylabelBottomView.backgroundColor = [UIColor colorWithWhite:230/255.0 alpha:1];;
}
// 完成
- (void)XXCityPickerViewRightAction:(XXCityPickerView *)picker andCity:(NSString *)city andData:(NSMutableDictionary *)dic{
    self.cityAddressLB.text = city;
    [self removeCityPickerView];
    NSLog(@"🐼🐼🐼XXCityPickerView:\n city= %@   dic= %@",city,dic);
    self.citylabelBottomView.backgroundColor = [UIColor colorWithWhite:230/255.0 alpha:1];;
}

-(void)removeCityPickerView{
    if (_cityPicker) {
        [_cityPicker removeFromSuperview];
        _cityPicker = nil;
    }
}

- (NSArray *) getAllFileNames:(NSString *)dirName
{
// 获得此程序的沙盒路径
    NSArray *patchs = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    
    // 获取Documents路径
    // [patchs objectAtIndex:0]
    NSString *documentsDirectory = [patchs objectAtIndex:0];
    NSString *fileDirectory = [documentsDirectory stringByAppendingPathComponent:[NSString stringWithFormat:@"%@", dirName]];
    NSLog(@"%@",fileDirectory);
    NSArray *files = [[NSFileManager defaultManager] subpathsOfDirectoryAtPath:fileDirectory error:nil];
    return files;
}


- (void)saveFile:(UIImage *)image andImageName:(NSString *)name{
    
    NSString *savePath= [NSString stringWithFormat:@"Documents/%@.png",name];
    NSString *imageAllPath = [NSHomeDirectory() stringByAppendingPathComponent:savePath];
    BOOL result = [UIImagePNGRepresentation(image) writeToFile:imageAllPath atomically:YES];

    NSLog(@"文件%@%@存到本地文件夹内", imageAllPath, result ? @"已经":@"没有");
    
}

- (UIImage *)getCacheImageUseImagePath:(NSString *)imagePath
{
    //防止每次启动程序沙盒前缀地址改变,只存储后边文件路径,调用时再次拼接
    NSString *imageAllPath = [NSHomeDirectory() stringByAppendingPathComponent:imagePath];
    return [UIImage imageWithContentsOfFile:imageAllPath];
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
