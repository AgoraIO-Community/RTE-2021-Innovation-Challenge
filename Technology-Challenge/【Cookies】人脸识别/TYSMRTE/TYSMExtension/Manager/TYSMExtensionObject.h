//
//  TYSMExtensionObject.h
//  TYSMExtension
//
//  Created by jele lam on 22/5/2021.
//

#import <Foundation/Foundation.h>
#import <AgoraRtcKit2/AgoraMediaFilterExtensionDelegate.h>
#import <TYSMExtension/TYSMVendor.h>

NS_ASSUME_NONNULL_BEGIN

@interface TYSMExtensionObject : NSObject <AgoraMediaFilterExtensionDelegate>

/// 插件名
@property (nonatomic, copy) NSString * vendorName;
/// C++ 插件 provider
@property (nonatomic, assign) void * __nullable mediaFilterRawProvider;
/// OC 插件 provider
@property (nonatomic, weak) id<AgoraExtProviderDelegate> __nullable mediaFilterProvider;

/// 生成一个 Cpp 的 插件
/// @param name 插件名
/// @param provider C++ 插件 provider
+ (TYSMExtensionObject *)generateExtensionObjectWith:(TYSM_VENDOR_NAME)name cppProvider:(void *)provider;

/// 生成一个 OC 的 插件
/// @param name 插件名
/// @param provider OC 插件 provider
+ (TYSMExtensionObject *)generateExtensionObjectWith:(TYSM_VENDOR_NAME)name OCProvider:(id<AgoraExtProviderDelegate>)provider;

@end

NS_ASSUME_NONNULL_END
