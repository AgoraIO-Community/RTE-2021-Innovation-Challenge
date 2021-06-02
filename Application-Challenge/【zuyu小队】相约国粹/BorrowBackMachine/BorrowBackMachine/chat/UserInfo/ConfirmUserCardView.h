//
//  ConfirmUserCardView.h
//  EaseIM
//
//  Created by lixiaoming on 2021/3/25.
//  Copyright © 2021 lixiaoming. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@protocol ConfirmUserCardViewDelegate <NSObject>

- (void)clickOK:(NSString*)aUid nickName:(NSString*)aNickName avatarUrl:(NSString*)aUrl;
- (void)clickCancel;

@end

@interface ConfirmUserCardView : UIView
- (instancetype)initWithRemoteName:(NSString*)aRemoteName avatarUrl:(NSString*)aUrl showName:(NSString*)aShowName uid:(NSString*)aUid delegate:(id<ConfirmUserCardViewDelegate>)aDelegate;

@property (weak,nonatomic) id<ConfirmUserCardViewDelegate> delegate;

@end

NS_ASSUME_NONNULL_END
