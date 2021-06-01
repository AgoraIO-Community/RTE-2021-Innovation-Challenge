//
//  EMContactModel.h
//  EaseIM
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface EMContactModel : NSObject <EaseUserDelegate>
@property (nonatomic, strong) NSString *easeId;
@property (nonatomic, copy) NSString *showName;         // 显示昵称
@property (nonatomic, copy) NSString *avatarURL;        // 显示头像的url
@property (nonatomic, copy) UIImage *defaultAvatar;     //默认头像
@end

NS_ASSUME_NONNULL_END
