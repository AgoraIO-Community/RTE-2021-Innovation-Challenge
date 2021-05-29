//
//  TYSMExtensionManger.h
//  TYSMExtension
//
//  Created by Jele on 20/5/2021.
//

#import <Foundation/Foundation.h>
#import <TYSMExtension/TYSMVendor.h>

// 需要公开一下文件额 Include of non-modular header inside framework module
//#import <AgoraRtcKit2/AgoraMediaFilterExtensionDelegate.h>

NS_ASSUME_NONNULL_BEGIN

@interface TYSMExtensionManger : NSObject
+ (instancetype)sharedInstance;

/// 组件名
- (NSArray <NSString *> * __nonnull)vendorNames;

/// 加载插件
/// @param vendorName 插件名，参考 TYSMVendor
- (BOOL)loadPluginWithVendor:(TYSM_VENDOR_NAME)vendorName;

/// 移出插件
/// @param vendorName 插件名，参考 TYSMVendor
//- (BOOL)unloadPluginWithVendor:(TYSM_VENDOR_NAME)vendorName;

/// 获取插件
- (NSArray *)getPlugins;
@end

NS_ASSUME_NONNULL_END
