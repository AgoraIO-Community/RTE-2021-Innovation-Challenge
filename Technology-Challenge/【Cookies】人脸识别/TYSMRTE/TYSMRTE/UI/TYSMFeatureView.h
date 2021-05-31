//
//  TYSMFeatureView.h
//  TYSMRTE
//
//  Created by Cookies L on 2021/5/26.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void(^TYSMFeatureTapBlock)(id data);
@interface TYSMFeatureView : UIView
@property (nonatomic, copy) TYSMFeatureTapBlock tapBlock;

- (void)setResultWith:(NSString *)result;
@end

NS_ASSUME_NONNULL_END
