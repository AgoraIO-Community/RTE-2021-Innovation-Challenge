//
//  TYSMFeatureView.m
//  TYSMRTE
//
//  Created by Cookies L on 2021/5/26.
//

#import "TYSMFeatureView.h"

@interface TYSMFeatureView ()
@property (weak, nonatomic) IBOutlet UIStackView *stackView;

@property (weak, nonatomic) IBOutlet UIButton *singleFaceButton;
@property (weak, nonatomic) IBOutlet UIButton *smileButton;
@property (weak, nonatomic) IBOutlet UIButton *yawnButton;

@property (strong, nonatomic) IBOutletCollection(UIButton) NSArray <UIButton *> *buttons;
@property (weak, nonatomic) IBOutlet UILabel *resultLabel;
@end

@implementation TYSMFeatureView

- (void)awakeFromNib {
    [super awakeFromNib];
}
/*
 // Only override drawRect: if you perform custom drawing.
 // An empty implementation adversely affects performance during animation.
 - (void)drawRect:(CGRect)rect {
 // Drawing code
 }
 */


- (IBAction)tapButton:(UIButton *)sender {
    sender.selected = !sender.selected;
    
    [self handleBlockFromSender:sender];
}

- (void)handleBlockFromSender:(UIButton *)sender {
    
    if (sender.selected) {
        for (UIButton *button in _buttons) {
            if ([button isEqual:sender]) continue;
            button.selected = NO;
        }
    }
    
    NSDictionary *dic = @{
        @"brf.single_face.enable" : @(self.singleFaceButton.selected),
        @"brf.smile.enable" : @(self.smileButton.selected),
        @"brf.yawn.enable" : @(self.yawnButton.selected)
    };
    
    
    
    !_tapBlock ? : _tapBlock(dic);
}

- (void)setResultWith:(NSString *)result {
//
//    if (self.singleFaceButton.selected) [self.singleFaceButton setTitle:result forState:UIControlStateSelected];
//
//    if (self.smileButton.selected) [self.smileButton setTitle:result forState:UIControlStateSelected];
//
//    if (self.yawnButton.selected) [self.yawnButton setTitle:result forState:UIControlStateSelected];
//
    
    if (self.singleFaceButton.selected ==
        self.smileButton.selected ==
        self.yawnButton.selected == NO) {
        self.resultLabel.text = @"选择以下功能激活↓↓↓";
        
        return;
    }
    
    if (self.singleFaceButton.selected) return;;
    
    self.resultLabel.text = [NSString stringWithFormat:@"结果：%@",result];
}

@end
