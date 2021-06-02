//
//  ZLEditViewController.m
//  ZLPhotoBrowser
//
//  Created by long on 2017/6/23.
//  Copyright © 2017年 long. All rights reserved.
//

#import "ZLEditViewController.h"
#import "ZLPhotoModel.h"
#import "ZLDefine.h"
#import "ZLPhotoManager.h"
#import "ToastUtils.h"
#import "ZLProgressHUD.h"
#import "ZLPhotoBrowser.h"
#import "UIImage+ZLPhotoBrowser.h"

//裁剪代码借鉴与CLImageEditor github:https://github.com/yackle/CLImageEditor

#pragma mark- UI components
@interface ZLClippingCircle : UIView

@property (nonatomic, strong) UIColor *bgColor;

@end

@implementation ZLClippingCircle

- (void)drawRect:(CGRect)rect
{
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGRect rct = self.bounds;
    rct.origin.x = rct.size.width/2-rct.size.width/6;
    rct.origin.y = rct.size.height/2-rct.size.height/6;
    rct.size.width /= 3;
    rct.size.height /= 3;
    
    CGContextSetFillColorWithColor(context, self.bgColor.CGColor);
    CGContextFillEllipseInRect(context, rct);
}

@end

//!!!!: ZLRatio
@interface ZLRatio : NSObject
@property (nonatomic, assign) BOOL isLandscape;
@property (nonatomic, readonly) CGFloat ratio;
@property (nonatomic, strong) NSString *titleFormat;

- (id)initWithValue1:(CGFloat)value1 value2:(CGFloat)value2;

@end

@implementation ZLRatio
{
    CGFloat _longSide;
    CGFloat _shortSide;
}

- (id)initWithValue1:(CGFloat)value1 value2:(CGFloat)value2
{
    self = [super init];
    if(self){
        _longSide  = MAX(fabs(value1), fabs(value2));
        _shortSide = MIN(fabs(value1), fabs(value2));
    }
    return self;
}

- (NSString*)description
{
    NSString *format = (self.titleFormat) ? self.titleFormat : @"%g : %g";
    
    if(self.isLandscape){
        return [NSString stringWithFormat:format, _longSide, _shortSide];
    }
    return [NSString stringWithFormat:format, _shortSide, _longSide];
}

- (CGFloat)ratio
{
    if(_longSide==0 || _shortSide==0){
        return 0;
    }
    
    if(self.isLandscape){
        return _shortSide / (CGFloat)_longSide;
    }
    return _longSide / (CGFloat)_shortSide;
}

@end

//!!!!: ZLRatioMenuItem
@interface ZLRatioMenuItem : UIView
{
    UIImageView *_iconView;
    UILabel *_titleLabel;
}
@property (nonatomic, strong) UIImageView *iconView;
@property (nonatomic, strong) ZLRatio *ratio;
- (void)changeOrientation;
@end

@implementation ZLRatioMenuItem

- (id)initWithFrame:(CGRect)frame target:(id)target action:(SEL)action
{
    self = [self initWithFrame:frame];
    if(self){
        UITapGestureRecognizer *gesture = [[UITapGestureRecognizer alloc] initWithTarget:target action:action];
        [self addGestureRecognizer:gesture];
        
        CGFloat W = frame.size.width;
        _iconView = [[UIImageView alloc] initWithFrame:CGRectMake(10, 5, W-20, W-20)];
        _iconView.clipsToBounds = YES;
        _iconView.layer.cornerRadius = 5;
        _iconView.contentMode = UIViewContentModeScaleAspectFill;
        [self addSubview:_iconView];
        
        _titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(_iconView.frame) + 5, W, 15)];
        _titleLabel.backgroundColor = [UIColor clearColor];
        _titleLabel.textColor = kRGB(18, 18, 18);
        _titleLabel.font = [UIFont systemFontOfSize:10];
        _titleLabel.textColor = [UIColor whiteColor];
        _titleLabel.textAlignment = NSTextAlignmentCenter;
        [self addSubview:_titleLabel];
    }
    return self;
}

- (void)setRatio:(ZLRatio *)ratio
{
    if(ratio != _ratio){
        _ratio = ratio;
    }
}

- (void)refreshViews
{
    _titleLabel.text = [_ratio description];
    
    CGPoint center = _iconView.center;
    CGFloat W, H;
    if (_ratio.ratio != 0) {
        if(_ratio.isLandscape) {
            W = 50;
            H = 50*_ratio.ratio;
        } else {
            W = 50/_ratio.ratio;
            H = 50;
        }
    } else {
        CGFloat maxW  = MAX(_iconView.image.size.width, _iconView.image.size.height);
        W = 50 * _iconView.image.size.width / maxW;
        H = 50 * _iconView.image.size.height / maxW;
    }
    _iconView.frame = CGRectMake(center.x-W/2, center.y-H/2, W, H);
}

- (void)changeOrientation
{
    self.ratio.isLandscape = !self.ratio.isLandscape;
    
    [UIView animateWithDuration:0.2 animations:^{
        [self refreshViews];
    }];
}

@end

@interface ZLGridLayar : CALayer
@property (nonatomic, assign) CGRect clippingRect;
@property (nonatomic, strong) UIColor *bgColor;
@property (nonatomic, strong) UIColor *gridColor;

@end

@implementation ZLGridLayar

+ (BOOL)needsDisplayForKey:(NSString*)key
{
    if ([key isEqualToString:@"clippingRect"]) {
        return YES;
    }
    return [super needsDisplayForKey:key];
}

- (id)initWithLayer:(id)layer
{
    self = [super initWithLayer:layer];
    if(self && [layer isKindOfClass:[ZLGridLayar class]]){
        self.bgColor   = ((ZLGridLayar *)layer).bgColor;
        self.gridColor = ((ZLGridLayar *)layer).gridColor;
        self.clippingRect = ((ZLGridLayar *)layer).clippingRect;
    }
    return self;
}

- (void)drawInContext:(CGContextRef)context
{
    CGRect rct = self.bounds;
    CGContextSetFillColorWithColor(context, self.bgColor.CGColor);
    CGContextFillRect(context, rct);
    
    CGContextClearRect(context, _clippingRect);
    
    CGContextSetStrokeColorWithColor(context, self.gridColor.CGColor);
    CGContextSetLineWidth(context, 1);
    
    rct = self.clippingRect;
    
    CGContextBeginPath(context);
    CGFloat dW = 0;
    for(int i=0;i<4;++i){
        CGContextMoveToPoint(context, rct.origin.x+dW, rct.origin.y);
        CGContextAddLineToPoint(context, rct.origin.x+dW, rct.origin.y+rct.size.height);
        dW += _clippingRect.size.width/3;
    }
    
    dW = 0;
    for(int i=0;i<4;++i){
        CGContextMoveToPoint(context, rct.origin.x, rct.origin.y+dW);
        CGContextAddLineToPoint(context, rct.origin.x+rct.size.width, rct.origin.y+dW);
        dW += rct.size.height/3;
    }
    CGContextStrokePath(context);
}

@end

//!!!!: edit vc
@interface ZLEditViewController ()
{
    UIImageView *_imageView;
    UIActivityIndicatorView *_indicator;
    
    ZLGridLayar *_gridLayer;
    ZLClippingCircle *_ltView;
    ZLClippingCircle *_lbView;
    ZLClippingCircle *_rtView;
    ZLClippingCircle *_rbView;
    
    UIView *_bottomView;
    UIButton *_cancelBtn;
    UIButton *_rotateImageBtn;
    UIButton *_doneBtn;
    //计算imageView尺寸时是否交换宽高（旋转图片90°及270°时候值为YES）
    BOOL _exchangeImageWH;
    //是否正在旋转图片
    BOOL _isRotatingImage;
    
    //旋转比例按钮
    UIButton *_rotateRatioBtn;
    //比例底滚动视图
    UIScrollView *_menuScroll;
}

@property (nonatomic, strong) ZLRatioMenuItem *selectedMenu;
@property (nonatomic, assign) CGRect clippingRect;
@property (nonatomic, strong) ZLRatio *clippingRatio;

@end

@implementation ZLEditViewController

- (void)dealloc
{
//    NSLog(@"---- %s", __FUNCTION__);
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self initUI];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [UIApplication sharedApplication].statusBarHidden = YES;
    self.navigationController.navigationBar.hidden = YES;
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    self.navigationController.navigationBar.hidden = NO;
}

- (void)viewDidLayoutSubviews
{
    [super viewDidLayoutSubviews];
    
    UIEdgeInsets inset = UIEdgeInsetsZero;
    if (@available(iOS 11, *)) {
        inset = self.view.safeAreaInsets;
    }
    
    ZLPhotoConfiguration *configuration = [(ZLImageNavigationController *)self.navigationController configuration];
    
    BOOL hideClipRatioView = configuration.hideClipRatiosToolBar ?: [self shouldHideClipRatioView];
    
    _imageView.frame = [self getImageViewFrame];
    _gridLayer.frame = _imageView.bounds;
    [self clippingRatioDidChange];
    
    CGFloat bottomViewH = 44;
    CGFloat bottomBtnH = 30;
    
    _bottomView.frame = CGRectMake(0, kViewHeight-bottomViewH-inset.bottom, kViewWidth, bottomViewH);
    _cancelBtn.frame = CGRectMake(10+inset.left, 7, GetMatchValue(GetLocalLanguageTextValue(ZLPhotoBrowserCancelText), 15, YES, bottomBtnH), bottomBtnH);
    _rotateImageBtn.frame = CGRectMake(kViewWidth/2-20, 2, 40, 40);
    _doneBtn.frame = CGRectMake(kViewWidth-70-inset.right, 7, 60, bottomBtnH);
    
    _indicator.center = _imageView.center;
    
    
    if (hideClipRatioView) {
        _rotateRatioBtn.hidden = YES;
        _menuScroll.hidden = YES;
    } else {
        _rotateRatioBtn.superview.frame = CGRectMake(kViewWidth-70-inset.right, kViewHeight-128-inset.bottom, 70, 80);
        _menuScroll.frame = CGRectMake(inset.left, kViewHeight-128-inset.bottom, kViewWidth-70-inset.left-inset.right, 80);
    }
}

//当裁剪比例只有 custom 或者 1:1 的时候隐藏比例视图
- (BOOL)shouldHideClipRatioView
{
    ZLPhotoConfiguration *configuration = [(ZLImageNavigationController *)self.navigationController configuration];
    if (configuration.clipRatios.count <= 1) {
        NSInteger value1 = [configuration.clipRatios.firstObject[ClippingRatioValue1] integerValue];
        NSInteger value2 = [configuration.clipRatios.firstObject[ClippingRatioValue2] integerValue];
        if ((value1==0 && value2==0) || (value1==1 && value2==1)) {
            return YES;
        }
    }
    return NO;
}

- (CGRect)getImageViewFrame
{
    UIEdgeInsets inset = UIEdgeInsetsZero;
    if (@available(iOS 11, *)) {
        inset = self.view.safeAreaInsets;
    }
    
    BOOL hideClipRatioView = [self shouldHideClipRatioView];
    //隐藏时 底部工具条高44，间距设置4即可，不隐藏时，比例view高度80，则为128
    CGFloat flag = hideClipRatioView ? 48 : 128;
    
    CGFloat w = kViewWidth-20-inset.left-inset.right;
    CGFloat maxH = kViewHeight-flag-inset.bottom-inset.top-50;
    
    CGFloat imgW = _exchangeImageWH ? self.model.asset.pixelHeight : self.model.asset.pixelWidth;
    CGFloat imgH = _exchangeImageWH ? self.model.asset.pixelWidth : self.model.asset.pixelHeight;
    
    CGFloat h = w * imgH / imgW;
    if (h > maxH) {
        h = maxH;
        w = h * imgW / imgH;
    }
    return CGRectMake((kViewWidth-w)/2, (kViewHeight-flag-h)/2, w, h);
}

- (void)initUI
{
    self.view.backgroundColor = [UIColor blackColor];
    //禁用返回手势
    if ([self.navigationController respondsToSelector:@selector(interactivePopGestureRecognizer)]) {
        self.navigationController.interactivePopGestureRecognizer.delegate = nil;
        self.navigationController.interactivePopGestureRecognizer.enabled = NO;
    }
    
    [self creatBottomView];
    [self setCropMenu];
    [self loadImage];
    
    _gridLayer = [[ZLGridLayar alloc] init];
    _gridLayer.bgColor   = [[UIColor blackColor] colorWithAlphaComponent:.5];
    _gridLayer.gridColor = [UIColor whiteColor];
    [_imageView.layer addSublayer:_gridLayer];
    
    _ltView = [self clippingCircleWithTag:0];
    _lbView = [self clippingCircleWithTag:1];
    _rtView = [self clippingCircleWithTag:2];
    _rbView = [self clippingCircleWithTag:3];
    
    UIPanGestureRecognizer *panGesture = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(panGridView:)];
    _imageView.userInteractionEnabled = YES;
    [_imageView addGestureRecognizer:panGesture];
}

- (void)creatBottomView
{
    ZLPhotoConfiguration *configuration = [(ZLImageNavigationController *)self.navigationController configuration];
    //下方视图
    _bottomView = [[UIView alloc] init];
    _bottomView.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:.7];
    [self.view addSubview:_bottomView];
    
    _cancelBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    _cancelBtn.titleLabel.font = [UIFont systemFontOfSize:15];
    [_cancelBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [_cancelBtn setTitle:GetLocalLanguageTextValue(ZLPhotoBrowserCancelText) forState:UIControlStateNormal];
    [_cancelBtn addTarget:self action:@selector(cancelBtn_click) forControlEvents:UIControlEventTouchUpInside];
    [_bottomView addSubview:_cancelBtn];
    
    _rotateImageBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [_rotateImageBtn setImage:GetImageWithName(@"zl_rotateimage") forState:UIControlStateNormal];
    [_rotateImageBtn addTarget:self action:@selector(rotateImageBtn_click) forControlEvents:UIControlEventTouchUpInside];
    [_bottomView addSubview:_rotateImageBtn];
    
    _doneBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [_doneBtn setTitle:GetLocalLanguageTextValue(ZLPhotoBrowserDoneText) forState:UIControlStateNormal];
    [_doneBtn setBackgroundColor:configuration.bottomBtnsNormalTitleColor];
    [_doneBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    _doneBtn.titleLabel.font = [UIFont systemFontOfSize:15];
    _doneBtn.layer.masksToBounds = YES;
    _doneBtn.layer.cornerRadius = 3.0f;
    [_doneBtn addTarget:self action:@selector(btnDone_click) forControlEvents:UIControlEventTouchUpInside];
    [_bottomView addSubview:_doneBtn];
}

- (void)loadImage
{
    //imageview
    _imageView = [[UIImageView alloc] init];
    _imageView.image = self.oriImage;
    _imageView.contentMode = UIViewContentModeScaleAspectFit;
    [self.view addSubview:_imageView];
    
    _indicator = [[UIActivityIndicatorView alloc] init];
    _indicator.center = _imageView.center;
    _indicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyleWhiteLarge;
    _indicator.hidesWhenStopped = YES;
    [self.view addSubview:_indicator];
    
    CGFloat scale = 3;
    CGFloat width = MIN(kViewWidth, kMaxImageWidth);
    CGSize size = CGSizeMake(width*scale, width*scale*self.model.asset.pixelHeight/self.model.asset.pixelWidth);
    
    [_indicator startAnimating];
    zl_weakify(self);
    [ZLPhotoManager requestImageForAsset:self.model.asset size:size completion:^(UIImage *image, NSDictionary *info) {
        if (![[info objectForKey:PHImageResultIsDegradedKey] boolValue]) {
            zl_strongify(weakSelf);
            [strongSelf->_indicator stopAnimating];
            strongSelf->_imageView.image = image;
            
            CGFloat W = 70;
            CGSize  imgSize = image.size;
            CGFloat maxW = MIN(imgSize.width, imgSize.height);
            UIImage *iconImage = [strongSelf scaleImage:image toSize:CGSizeMake(W * imgSize.width/maxW, W * imgSize.height/maxW)];
            for (UIView *v in strongSelf->_menuScroll.subviews) {
                if ([v isKindOfClass:[ZLRatioMenuItem class]]) {
                    ((ZLRatioMenuItem *)v).iconView.image = iconImage;
                    [((ZLRatioMenuItem *)v) refreshViews];
                }
            }
        }
    }];
}

- (UIImage *)scaleImage:(UIImage *)image toSize:(CGSize)size
{
    NSData *data = UIImageJPEGRepresentation(image, 1.0);
    return [self scaledlmageWithData:data withSize:size scale:1.0 orientation:image.imageOrientation];
}

- (UIImage *)scaledlmageWithData:(NSData *)data withSize:(CGSize)size scale:(CGFloat)scale orientation:(UIImageOrientation)orientation {
    CGFloat maxPixelSize = MAX(size.width, size.height);
    CGImageSourceRef sourceRef = CGImageSourceCreateWithData((__bridge CFDataRef)data, nil);
    NSDictionary *options = @{(__bridge id)kCGImageSourceCreateThumbnailFromImageAlways:(__bridge id)kCFBooleanTrue,
                              (__bridge id)kCGImageSourceThumbnailMaxPixelSize:[NSNumber numberWithFloat:maxPixelSize]};
    CGImageRef imageRef = CGImageSourceCreateThumbnailAtIndex(sourceRef, 0, (__bridge CFDictionaryRef)options);
    UIImage *resultlmage = [UIImage imageWithCGImage:imageRef scale:scale orientation:orientation];
    CGImageRelease(imageRef);
    CFRelease(sourceRef);
    return resultlmage;
}


- (void)setCropMenu
{
    //这只是初始坐标，实际坐标在viewdidlayoutsubviews里面布局
    _menuScroll = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, kViewWidth-70, 80)];
    _menuScroll.backgroundColor = [UIColor clearColor];
    _menuScroll.showsHorizontalScrollIndicator = NO;
    _menuScroll.clipsToBounds = NO;
    [self.view addSubview:_menuScroll];
    //旋转按钮
    UIView *view = [[UIView alloc] init];
    view.backgroundColor = [[UIColor whiteColor] colorWithAlphaComponent:.8];
    [self.view addSubview:view];
    _rotateRatioBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    _rotateRatioBtn.frame = CGRectMake(15, 20, 40, 40);
    _rotateRatioBtn.imageView.contentMode = UIViewContentModeScaleAspectFit;
    [_rotateRatioBtn setBackgroundImage:GetImageWithName(@"btn_rotate") forState:UIControlStateNormal];
    [_rotateRatioBtn addTarget:self action:@selector(pushedRotateBtn:) forControlEvents:UIControlEventTouchUpInside];
    [view addSubview:_rotateRatioBtn];
    
    CGFloat W = 70;
    CGFloat x = 0;
    
    ZLPhotoConfiguration *configuration = [(ZLImageNavigationController *)self.navigationController configuration];
    //如需要其他比例，请按照格式自行设置
    
    for(NSDictionary *info in configuration.clipRatios){
        CGFloat val1 = [info[@"value1"] floatValue];
        CGFloat val2 = [info[@"value2"] floatValue];
        
        ZLRatio *ratio = [[ZLRatio alloc] initWithValue1:val1 value2:val2];
        ratio.titleFormat = info[@"titleFormat"];
        
        ratio.isLandscape = val1 > val2;
        
        ZLRatioMenuItem *view = [[ZLRatioMenuItem alloc] initWithFrame:CGRectMake(x, 0, W, _menuScroll.frame.size.height) target:self action:@selector(tappedMenu:)];
        view.ratio = ratio;
        
        [_menuScroll addSubview:view];
        x += W;
        
        if(self.selectedMenu==nil){
            self.selectedMenu = view;
        }
    }
    _menuScroll.contentSize = CGSizeMake(MAX(x, _menuScroll.frame.size.width+1), 0);
}

- (void)tappedMenu:(UITapGestureRecognizer*)sender
{
    ZLRatioMenuItem *view = (ZLRatioMenuItem*)sender.view;
    
    view.alpha = 0.2;
    [UIView animateWithDuration:0.2 animations:^{
         view.alpha = 1;
    }];
    
    self.selectedMenu = view;
}

- (void)setSelectedMenu:(ZLRatioMenuItem *)selectedMenu
{
    if(selectedMenu != _selectedMenu){
        _selectedMenu.backgroundColor = [UIColor clearColor];
        _selectedMenu = selectedMenu;
        _selectedMenu.backgroundColor = kRGB(30, 30, 30);
        
        if(selectedMenu.ratio.ratio==0){
            self.clippingRatio = nil;
        } else {
            self.clippingRatio = selectedMenu.ratio;
        }
    }
}

- (void)setClippingRatio:(ZLRatio *)clippingRatio
{
    if(clippingRatio != _clippingRatio){
        _clippingRatio = clippingRatio;
        [self clippingRatioDidChange];
    }
}

- (void)clippingRatioDidChange
{
    CGRect rect = _imageView.bounds;
    if (self.clippingRatio) {
        CGFloat H = rect.size.width * self.clippingRatio.ratio;
        if (H<=rect.size.height) {
            rect.size.height = H;
        } else {
            rect.size.width *= rect.size.height / H;
        }
        
        rect.origin.x = (_imageView.bounds.size.width - rect.size.width) / 2;
        rect.origin.y = (_imageView.bounds.size.height - rect.size.height) / 2;
    }
    [self setClippingRect:rect animated:YES];
}

- (void)setClippingRect:(CGRect)clippingRect animated:(BOOL)animated
{
    if (animated) {
        [UIView animateWithDuration:0.2 animations:^{
                             _ltView.center = [self.view convertPoint:CGPointMake(clippingRect.origin.x, clippingRect.origin.y) fromView:_imageView];
                             _lbView.center = [self.view convertPoint:CGPointMake(clippingRect.origin.x, clippingRect.origin.y+clippingRect.size.height) fromView:_imageView];
                             _rtView.center = [self.view convertPoint:CGPointMake(clippingRect.origin.x+clippingRect.size.width, clippingRect.origin.y) fromView:_imageView];
                             _rbView.center = [self.view convertPoint:CGPointMake(clippingRect.origin.x+clippingRect.size.width, clippingRect.origin.y+clippingRect.size.height) fromView:_imageView];
                         }
         ];
        
        CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"clippingRect"];
        animation.duration = 0.2;
        animation.fromValue = [NSValue valueWithCGRect:_clippingRect];
        animation.toValue = [NSValue valueWithCGRect:clippingRect];
        [_gridLayer addAnimation:animation forKey:nil];
        
        _gridLayer.clippingRect = clippingRect;
        _clippingRect = clippingRect;
        [_gridLayer setNeedsDisplay];
    } else {
        self.clippingRect = clippingRect;
    }
}

- (void)pushedRotateBtn:(UIButton*)sender
{
    for(ZLRatioMenuItem *item in _menuScroll.subviews){
        if([item isKindOfClass:[ZLRatioMenuItem class]]){
            [item changeOrientation];
        }
    }
    
    if (self.clippingRatio.ratio!=0 &&
        self.clippingRatio.ratio!=1){
        [self clippingRatioDidChange];
    }
}

- (ZLClippingCircle*)clippingCircleWithTag:(NSInteger)tag
{
    ZLClippingCircle *view = [[ZLClippingCircle alloc] initWithFrame:CGRectMake(0, 0, 50, 50)];
    view.backgroundColor = [UIColor clearColor];
    view.bgColor = [UIColor whiteColor];
    view.tag = tag;
    
    UIPanGestureRecognizer *panGesture = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(panCircleView:)];
    [view addGestureRecognizer:panGesture];
    
    [self.view addSubview:view];
    
    return view;
}

- (void)setClippingRect:(CGRect)clippingRect
{
    _clippingRect = clippingRect;
    
    _ltView.center = [self.view convertPoint:CGPointMake(_clippingRect.origin.x, _clippingRect.origin.y) fromView:_imageView];
    _lbView.center = [self.view convertPoint:CGPointMake(_clippingRect.origin.x, _clippingRect.origin.y+_clippingRect.size.height) fromView:_imageView];
    _rtView.center = [self.view convertPoint:CGPointMake(_clippingRect.origin.x+_clippingRect.size.width, _clippingRect.origin.y) fromView:_imageView];
    _rbView.center = [self.view convertPoint:CGPointMake(_clippingRect.origin.x+_clippingRect.size.width, _clippingRect.origin.y+_clippingRect.size.height) fromView:_imageView];
    
    _gridLayer.clippingRect = clippingRect;
    [_gridLayer setNeedsDisplay];
}

#pragma mark - 拖动
- (void)panCircleView:(UIPanGestureRecognizer*)sender
{
    CGPoint point = [sender locationInView:_imageView];
    CGPoint dp = [sender translationInView:_imageView];
    
    CGRect rct = self.clippingRect;
    
    const CGFloat W = _imageView.frame.size.width;
    const CGFloat H = _imageView.frame.size.height;
    CGFloat minX = 0;
    CGFloat minY = 0;
    CGFloat maxX = W;
    CGFloat maxY = H;
    
    CGFloat ratio = (sender.view.tag == 1 || sender.view.tag==2) ? -self.clippingRatio.ratio : self.clippingRatio.ratio;
    
    switch (sender.view.tag) {
        case 0: // upper left
        {
            maxX = MAX((rct.origin.x + rct.size.width)  - 0.1 * W, 0.1 * W);
            maxY = MAX((rct.origin.y + rct.size.height) - 0.1 * H, 0.1 * H);
            
            if (ratio!=0) {
                CGFloat y0 = rct.origin.y - ratio * rct.origin.x;
                CGFloat x0 = -y0 / ratio;
                minX = MAX(x0, 0);
                minY = MAX(y0, 0);
                
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
                
                if(-dp.x*ratio + dp.y > 0){ point.x = (point.y - y0) / ratio; }
                else{ point.y = point.x * ratio + y0; }
            } else {
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
            }
        
            rct.size.width  = rct.size.width  - (point.x - rct.origin.x);
            rct.size.height = rct.size.height - (point.y - rct.origin.y);
            rct.origin.x = point.x;
            rct.origin.y = point.y;
            break;
        }
        case 1: // lower left
        {
            maxX = MAX((rct.origin.x + rct.size.width)  - 0.1 * W, 0.1 * W);
            minY = MAX(rct.origin.y + 0.1 * H, 0.1 * H);
            
            if (ratio!=0) {
                CGFloat y0 = (rct.origin.y + rct.size.height) - ratio* rct.origin.x ;
                CGFloat xh = (H - y0) / ratio;
                minX = MAX(xh, 0);
                maxY = MIN(y0, H);
                
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
                
                if(-dp.x*ratio + dp.y < 0){ point.x = (point.y - y0) / ratio; }
                else{ point.y = point.x * ratio + y0; }
            } else {
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
            }
            
            rct.size.width  = rct.size.width  - (point.x - rct.origin.x);
            rct.size.height = point.y - rct.origin.y;
            rct.origin.x = point.x;
            break;
        }
        case 2: // upper right
        {
            minX = MAX(rct.origin.x + 0.1 * W, 0.1 * W);
            maxY = MAX((rct.origin.y + rct.size.height) - 0.1 * H, 0.1 * H);
            
            if (ratio!=0) {
                CGFloat y0 = rct.origin.y - ratio * (rct.origin.x + rct.size.width);
                CGFloat yw = ratio * W + y0;
                CGFloat x0 = -y0 / ratio;
                maxX = MIN(x0, W);
                minY = MAX(yw, 0);
                
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
                
                if(-dp.x*ratio + dp.y > 0){ point.x = (point.y - y0) / ratio; }
                else{ point.y = point.x * ratio + y0; }
            } else {
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
            }
            
            rct.size.width  = point.x - rct.origin.x;
            rct.size.height = rct.size.height - (point.y - rct.origin.y);
            rct.origin.y = point.y;
            break;
        }
        case 3: // lower right
        {
            minX = MAX(rct.origin.x + 0.1 * W, 0.1 * W);
            minY = MAX(rct.origin.y + 0.1 * H, 0.1 * H);
            
            if (ratio!=0) {
                CGFloat y0 = (rct.origin.y + rct.size.height) - ratio * (rct.origin.x + rct.size.width);
                CGFloat yw = ratio * W + y0;
                CGFloat xh = (H - y0) / ratio;
                maxX = MIN(xh, W);
                maxY = MIN(yw, H);
                
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
                
                if(-dp.x*ratio + dp.y < 0){ point.x = (point.y - y0) / ratio; }
                else{ point.y = point.x * ratio + y0; }
            } else {
                point.x = MAX(minX, MIN(point.x, maxX));
                point.y = MAX(minY, MIN(point.y, maxY));
            }
            
            rct.size.width  = point.x - rct.origin.x;
            rct.size.height = point.y - rct.origin.y;
            break;
        }
        default:
            break;
    }
    self.clippingRect = rct;
}

- (void)panGridView:(UIPanGestureRecognizer*)sender
{
    static BOOL dragging = NO;
    static CGRect initialRect;
    
    if (sender.state==UIGestureRecognizerStateBegan) {
        CGPoint point = [sender locationInView:_imageView];
        dragging = CGRectContainsPoint(_clippingRect, point);
        initialRect = self.clippingRect;
    } else if(dragging) {
        CGPoint point = [sender translationInView:_imageView];
        CGFloat left  = MIN(MAX(initialRect.origin.x + point.x, 0), _imageView.frame.size.width-initialRect.size.width);
        CGFloat top   = MIN(MAX(initialRect.origin.y + point.y, 0), _imageView.frame.size.height-initialRect.size.height);
        
        CGRect rct = self.clippingRect;
        rct.origin.x = left;
        rct.origin.y = top;
        self.clippingRect = rct;
    }
}

#pragma mark - action
- (void)cancelBtn_click
{
    ZLImageNavigationController *nav = (ZLImageNavigationController *)self.navigationController;
    ZLPhotoConfiguration *configuration = nav.configuration;
    
    if (configuration.editAfterSelectThumbnailImage &&
        configuration.maxSelectCount == 1) {
        [nav.arrSelectedModels removeAllObjects];
    }
    UIViewController *vc = [self.navigationController popViewControllerAnimated:NO];
    if (!vc) {
        [self dismissViewControllerAnimated:YES completion:nil];
    }
}

- (void)rotateImageBtn_click
{
    if (_isRotatingImage) {
        //旋转过程中不接受再次旋转
        return;
    }
    _isRotatingImage = YES;
    
    UIImage *newImage = [_imageView.image rotate:UIImageOrientationLeft];

    _exchangeImageWH = !_exchangeImageWH;

    [_gridLayer removeFromSuperlayer];
    _ltView.hidden = YES;
    _lbView.hidden = YES;
    _rtView.hidden = YES;
    _rbView.hidden = YES;
    
    [UIView animateWithDuration:0.25 animations:^{
        _imageView.transform = CGAffineTransformMakeRotation(-M_PI_2);
    } completion:^(BOOL finished) {
        _imageView.image = newImage;
        _imageView.frame = [self getImageViewFrame];
        _imageView.transform = CGAffineTransformIdentity;
        _isRotatingImage = NO;
    }];
    
    [self.class cancelPreviousPerformRequestsWithTarget:self selector:@selector(showGridLayer) object:nil];
    [self performSelector:@selector(showGridLayer) withObject:nil afterDelay:0.5];
}

- (void)showGridLayer
{
    [_imageView.layer addSublayer:_gridLayer];
    _ltView.hidden = NO;
    _lbView.hidden = NO;
    _rtView.hidden = NO;
    _rbView.hidden = NO;
}

- (void)btnDone_click
{
    //确定裁剪，返回
    ZLProgressHUD *hud = [[ZLProgressHUD alloc] init];
    [hud show];
    
    ZLImageNavigationController *nav = (ZLImageNavigationController *)self.navigationController;
    
    UIImage *image = [self clipImage];
    
    if (nav.configuration.saveNewImageAfterEdit) {
        __weak typeof(nav) weakNav = nav;
        [ZLPhotoManager saveImageToAblum:image completion:^(BOOL suc, PHAsset *asset) {
            dispatch_async(dispatch_get_main_queue(), ^{
                [hud hide];
                if (suc) {
                    __strong typeof(weakNav) strongNav = weakNav;
                    if (strongNav.callSelectClipImageBlock) {
                        strongNav.callSelectClipImageBlock(image, asset);
                    }
                } else {
                    ShowToastLong(@"%@", GetLocalLanguageTextValue(ZLPhotoBrowserSaveImageErrorText));
                }
            });
        }];
    } else {
        [hud hide];
        if (image) {
            if (nav.callSelectClipImageBlock) {
                nav.callSelectClipImageBlock(image, self.model.asset);
            }
        } else {
            ShowToastLong(@"%@", GetLocalLanguageTextValue(ZLPhotoBrowserSaveImageErrorText));
        }
    }
}

- (UIImage *)clipImage
{
    CGFloat zoomScale = _imageView.bounds.size.width / _imageView.image.size.width;
    CGRect rct = self.clippingRect;
    rct.size.width  /= zoomScale;
    rct.size.height /= zoomScale;
    rct.origin.x    /= zoomScale;
    rct.origin.y    /= zoomScale;
    
    CGPoint origin = CGPointMake(-rct.origin.x, -rct.origin.y);
    UIImage *img = nil;
    
    UIGraphicsBeginImageContextWithOptions(rct.size, NO, _imageView.image.scale);
    [_imageView.image drawAtPoint:origin];
    img = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return img;
}

@end
