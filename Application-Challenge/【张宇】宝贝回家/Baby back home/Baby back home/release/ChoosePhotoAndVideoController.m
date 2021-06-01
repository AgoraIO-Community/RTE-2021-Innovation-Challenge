//
//  ChoosePhotoAndVideoController.m
//  Baby back home
//
//  Created by zhangyu on 2021/5/17.
//

#import "ChoosePhotoAndVideoController.h"
#import "HXPhotoView.h"


static const CGFloat kPhotoViewMargin = 12.0;
@interface ChoosePhotoAndVideoController ()<HXPhotoViewDelegate>
@property (strong, nonatomic) HXPhotoManager *manager;
@property (strong, nonatomic) HXPhotoView *photoView;
@property (strong, nonatomic) UIScrollView *scrollView;
@property (strong, nonatomic) NSArray * choosePicArr;

@end

@implementation ChoosePhotoAndVideoController


- (void)traitCollectionDidChange:(UITraitCollection *)previousTraitCollection {
    [super traitCollectionDidChange:previousTraitCollection];
#ifdef __IPHONE_13_0
    if (@available(iOS 13.0, *)) {
        [self preferredStatusBarUpdateAnimation];
        [self changeStatus];
    }
#endif
}
- (UIStatusBarStyle)preferredStatusBarStyle {
#ifdef __IPHONE_13_0
    if (@available(iOS 13.0, *)) {
        if (UITraitCollection.currentTraitCollection.userInterfaceStyle == UIUserInterfaceStyleDark) {
            return UIStatusBarStyleLightContent;
        }
    }
#endif
    return UIStatusBarStyleDefault;
}
- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.navigationController.navigationBar setHidden:NO];
    [self changeStatus];
}
#pragma clang diagnostic push
#pragma clang diagnostic ignored"-Wdeprecated-declarations"
- (void)changeStatus {
#ifdef __IPHONE_13_0
    if (@available(iOS 13.0, *)) {
        if (UITraitCollection.currentTraitCollection.userInterfaceStyle == UIUserInterfaceStyleDark) {
            [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent animated:YES];
            return;
        }
    }
#endif
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleDefault animated:YES];
}
#pragma clang diagnostic pop
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

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    // Fallback on earlier versions
    self.view.backgroundColor = [UIColor whiteColor];
#ifdef __IPHONE_13_0
    if (@available(iOS 13.0, *)) {
        self.view.backgroundColor = [UIColor colorWithDynamicProvider:^UIColor * _Nonnull(UITraitCollection * _Nonnull traitCollection) {
            if (traitCollection.userInterfaceStyle == UIUserInterfaceStyleDark) {
                return UIColor.blackColor;
            }
            return UIColor.whiteColor;
        }];
    }
#endif
    //    self.navigationController.navigationBar.translucent = NO;
    self.automaticallyAdjustsScrollViewInsets = YES;
    
    // 加载本地图片
    NSMutableArray *images = [NSMutableArray array];
    
//    for (int i = 0 ; i < 4; i++) {
//        UIImage *image = [UIImage imageNamed:[NSString stringWithFormat:@"%d",i]];
//
//        HXCustomAssetModel *model = [HXCustomAssetModel assetWithLocalImage:image selected:YES];
//        [images addObject:model];
//    }
    UIScrollView *scrollView = [[UIScrollView alloc] initWithFrame:self.view.bounds];
    scrollView.alwaysBounceVertical = YES;
    [self.view addSubview:scrollView];
    self.scrollView = scrollView;
    
    CGFloat width = scrollView.frame.size.width;
    HXPhotoView *photoView = [[HXPhotoView alloc] initWithFrame:CGRectMake(0, 0, width, 0) manager:self.manager];
    photoView.delegate = self;
    photoView.spacing = 7;
    photoView.lineCount = 3;
    photoView.collectionView.contentInset = UIEdgeInsetsMake(16, 16, 16, 16);
    [self.manager addCustomAssetModel:images];
    
    /**  添加本地视频  **/
//    NSURL *url = [[NSBundle mainBundle] URLForResource:@"LocalSampleVideo" withExtension:@"mp4"];
//    HXCustomAssetModel *videoAsset = [HXCustomAssetModel assetWithLocalVideoURL:url selected:YES];
//    [self.manager addCustomAssetModel:@[videoAsset]];
    
    [photoView refreshView];
    [scrollView addSubview:photoView];
    self.photoView = photoView;
    
    UIBarButtonItem *item1 = [[UIBarButtonItem alloc] initWithTitle:@"相机" style:UIBarButtonItemStylePlain target:self action:@selector(didNavOneBtnClick)];
    
    UIBarButtonItem *item2 = [[UIBarButtonItem alloc] initWithTitle:@"保存" style:UIBarButtonItemStylePlain target:self action:@selector(didNavTwoBtnClick)];
    
    self.navigationItem.rightBarButtonItems = @[item2,item1];
}

- (void)didNavOneBtnClick {
    [self.photoView goCameraViewController];
}

- (void)didNavTwoBtnClick {
//    if (self.manager.afterSelectPhotoCountIsMaximum) {
//        [self.view hx_showImageHUDText:@"图片已达到最大数"];
//        return;
//    }
//    int x = arc4random() % 4;
//    HXCustomAssetModel *asset = [HXCustomAssetModel assetWithLocaImageName:@(x).stringValue selected:YES];
//    [self.manager addCustomAssetModel:@[asset]];
//    [self.photoView refreshView];
    self.completeChoosePic(self.choosePicArr);
    [self.navigationController popViewControllerAnimated:YES];
    
    
}
 
- (void)photoView:(HXPhotoView *)photoView changeComplete:(NSArray<HXPhotoModel *> *)allList photos:(NSArray<HXPhotoModel *> *)photos videos:(NSArray<HXPhotoModel *> *)videos original:(BOOL)isOriginal {
    NSSLog(@"%@",allList);
    self.choosePicArr = allList;
   
}

- (void)photoView:(HXPhotoView *)photoView updateFrame:(CGRect)frame {
    NSSLog(@"%@",NSStringFromCGRect(frame));
    self.scrollView.contentSize = CGSizeMake(self.scrollView.frame.size.width, CGRectGetMaxY(frame) + kPhotoViewMargin);
    
}

- (void)dealloc {
    NSSLog(@"%@",self);
}

@end
