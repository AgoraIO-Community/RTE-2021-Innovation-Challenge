//
//  MainWebViewController.m
//  CNCLibraryScan
//
//  Created by zuyu on 2017/11/21.
//  Copyright © 2017年 zuyu. All rights reserved.
//

#import "MainWebViewController.h"
#import "zuyu.h"
@interface MainWebViewController ()<UIWebViewDelegate,NavgationViewDelegate,NetworkErrorViewDeleagete>
{
    UIWebView *webView;
    
    UIActivityIndicatorView *activityIndicatorView;
    
    UIView *opaqueView;
    
    NSString *_urlStr;
    
    
}

@property(nonatomic,strong) NetworkErrorView *errorView;

@end

@implementation MainWebViewController
#pragma mark - navgation
-(void)createNavgation
{
    OnlineNavgationView *view = [[OnlineNavgationView alloc] init];
    view.delegate = self;
    view.backBtnHidden = NO;
    view.scanHidden = YES;
    view.searchBtnHidden = YES;
    view.createCodeHidden = YES;
    view.titleStr = @"";
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(WIDTH - 120, 0, 120, 74);
    button.backgroundColor = [UIColor clearColor];
    [button setTitle:@"加入群聊" forState:UIControlStateNormal];
    [button addTarget:self action:@selector(joinGroup:) forControlEvents:UIControlEventTouchUpInside];
    [button setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
    [view addSubview:button];
    [self.view addSubview:view];
}


-(void)joinGroup:(id)arg
{
    [PushToGroupTool groupBookID:self.webUrl withTitle:self.name withVC:self];
}
 

-(void)navPop
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)viewDidLoad {
    
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    [self createNavgation];
    
//    self.navigationController.interactivePopGestureRecognizer.enabled = NO;
    
    webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, 64, WIDTH, HEIGHT - 64)];
    [webView setUserInteractionEnabled:YES];//是否支持交互
    //[webView setDelegate:self];
    webView.delegate = self;
    //    [webView setOpaque:NO];//opaque是不透明的意思
    
    [webView setScalesPageToFit:YES];
    //自动缩放以适应屏幕
    
    [webView stringByEvaluatingJavaScriptFromString:@"document.getElementsByTagName('body')[0].style.webkitTextSizeAdjust= '80%'"];
    [self.view addSubview:webView];
    
    //加载网页的方式
    //1.创建并加载远程网页
    
    //    if (self.ResourceUrl != nil) {
    
    _urlStr  = [NSString stringWithFormat:@"%@",self.webUrl];
    
    NSURL *url = [NSURL URLWithString:_urlStr];
    
    NSURLRequest *request = [[NSURLRequest alloc] initWithURL:url
                             
                                                  cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
                             
                                              timeoutInterval:30];
    
    [webView loadRequest:request];
    opaqueView = [[UIView alloc]initWithFrame:[[UIScreen mainScreen] bounds]];
    activityIndicatorView = [[UIActivityIndicatorView alloc]initWithFrame:[[UIScreen mainScreen] bounds]];
    [activityIndicatorView setCenter:opaqueView.center];
    [activityIndicatorView setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleWhite];
    [opaqueView setBackgroundColor:[UIColor lightGrayColor]];
    [opaqueView setAlpha:1];
    [self.view addSubview:opaqueView];
    [opaqueView addSubview:activityIndicatorView];
    
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
    NSURL *url = [NSURL URLWithString:_urlStr];
    
    NSURLRequest *request = [[NSURLRequest alloc] initWithURL:url
                             
                                                  cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
                             
                                              timeoutInterval:30];
    
    [webView loadRequest:request];
}


-(void)webViewDidStartLoad:(UIWebView *)webView{
    [activityIndicatorView startAnimating];
    opaqueView.hidden = NO;
}

-(void)webViewDidFinishLoad:(UIWebView *)webView{
    [activityIndicatorView startAnimating];
    opaqueView.hidden = YES;
    
    [webView stringByEvaluatingJavaScriptFromString:@"document.getElementsByClassName('return_link')[0].style.display='none'"];
    
}

//UIWebView如何判断 HTTP 404 等错误
-(void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response{
    opaqueView.hidden = YES;

}

-(void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error

{
    opaqueView.hidden = YES;
self.errorView.hidden = NO;
    NSLog(@"测试！～～～%@,",webView);
    
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
