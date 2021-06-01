//
//  QHHelpView.m
//  QHSpeakerGame
//
//  Created by Anakin chen on 2021/5/24.
//

/*
 [在Storyboard中使用由xib定义的view](https://blog.csdn.net/wty21cn/article/details/52675545)
 */

#import "QHHelpView.h"

@implementation QHHelpView

+ (instancetype)createWith:(UIView *)superV {
    QHHelpView *helpV = [[[NSBundle mainBundle] loadNibNamed:@"QHHelpView" owner:nil options:nil] firstObject];
    [superV addSubview:helpV];
    helpV.translatesAutoresizingMaskIntoConstraints = NO;
    NSDictionary *viewsDict = NSDictionaryOfVariableBindings(helpV);
    [superV addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"|-0-[helpV]-0-|" options:NSLayoutFormatAlignAllBaseline metrics:0 views:viewsDict]];
    [superV addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|-0-[helpV]-0-|" options:NSLayoutFormatAlignAllBaseline metrics:0 views:viewsDict]];
    return helpV;
}

- (instancetype)initWithCoder:(NSCoder *)aDecoder {
    self = [super initWithCoder:aDecoder];
    if (self) {
        [self setupXibBridgeWithPlaceholderViewNibName:NSStringFromClass([self class])];
    }
    return self;
}

- (void)setupXibBridgeWithPlaceholderViewNibName:(NSString *)placeholderViewNibName {
    UIView *contentView = [[[NSBundle mainBundle] loadNibNamed:placeholderViewNibName
                                                        owner:self
                                                      options:nil] objectAtIndex:0];

    [self setBackgroundColor:[UIColor clearColor]];
    [self addSubview:contentView];
    [self setXibBridgeConstraintsToContentView:contentView];
}

- (void)setXibBridgeConstraintsToContentView:(UIView *)contentView {
    [self addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|[contentView]|" options:0 metrics:nil views:NSDictionaryOfVariableBindings(contentView)]];
    [self addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|[contentView]|" options:0 metrics:nil views:NSDictionaryOfVariableBindings(contentView)]];
    //为保证AutoLayout生效，必须加上下面这句话
    contentView.translatesAutoresizingMaskIntoConstraints = NO;
}

@end
