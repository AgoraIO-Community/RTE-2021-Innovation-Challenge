//
//  QHAboutViewController.m
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/24.
//

#import "QHAboutViewController.h"

@interface QHAboutViewController ()

@property (weak, nonatomic) IBOutlet UILabel *contenL;

@end

@implementation QHAboutViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.contenL.text = @"开发者：AnakinChen\r\nGitHub：https://github.com/chenqihui\r\n邮箱：chen_qihui@qq.com";
}

@end
