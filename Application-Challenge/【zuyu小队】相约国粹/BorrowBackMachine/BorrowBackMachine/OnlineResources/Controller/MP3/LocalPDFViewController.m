//
//  LocalPDFViewController.m
//  CNCLibraryScan
//
//  Created by zuyu on 2017/9/15.
//  Copyright © 2017年 zuyu. All rights reserved.
//

#import "LocalPDFViewController.h"

@interface LocalPDFViewController ()<UIWebViewDelegate>
{
    UIWebView *webView;
   
    UIActivityIndicatorView *activityIndicatorView;
    
    UIView *opaqueView;
}
@end
  
@implementation LocalPDFViewController


-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    self.tabBarController.tabBar.hidden = YES;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    [self simpleUIWebViewTest];
    // Do any additional setup after loading the view.
}

- (void)simpleUIWebViewTest {
  
    
    webView = [[UIWebView alloc]initWithFrame:[[UIScreen mainScreen] bounds]];
    [webView setUserInteractionEnabled:YES];//是否支持交互
    //[webView setDelegate:self];
    webView.delegate=self;
    //    [webView setOpaque:NO];//opaque是不透明的意思
        [webView setScalesPageToFit:YES];//自动缩放以适应屏幕
    
    [webView stringByEvaluatingJavaScriptFromString:@"document.getElementsByTagName('body')[0].style.webkitTextSizeAdjust= '80%'"];
    [self.view addSubview:webView];
    
//    NSURL *url = [NSURL fileURLWithPath:self.urlString];
    
    NSURL *url = [NSURL URLWithString:self.urlString];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    [webView loadRequest:request];
    
    opaqueView = [[UIView alloc]initWithFrame:[[UIScreen mainScreen] bounds]];
    activityIndicatorView = [[UIActivityIndicatorView alloc]initWithFrame:[[UIScreen mainScreen] bounds]];
    [activityIndicatorView setCenter:opaqueView.center];
    [activityIndicatorView setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleWhite];
    [opaqueView setBackgroundColor:[UIColor blackColor]];
    [opaqueView setAlpha:1];
    [self.view addSubview:opaqueView];
    [opaqueView addSubview:activityIndicatorView];
    
}


-(void)webViewDidStartLoad:(UIWebView *)webView{
    [activityIndicatorView startAnimating];
    opaqueView.hidden = NO;
}

-(void)webViewDidFinishLoad:(UIWebView *)webView{
    [activityIndicatorView startAnimating];
    opaqueView.hidden = YES;
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
