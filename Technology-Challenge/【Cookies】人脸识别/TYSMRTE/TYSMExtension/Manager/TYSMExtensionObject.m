//
//  TYSMExtensionObject.m
//  TYSMExtension
//
//  Created by jele lam on 22/5/2021.
//

#import "TYSMExtensionObject.h"

@implementation TYSMExtensionObject

+ (TYSMExtensionObject *)generateExtensionObjectWith:(TYSM_VENDOR_NAME)name cppProvider:(void *)provider {
    NSParameterAssert(name);
    NSParameterAssert(provider);
    
    TYSMExtensionObject *obj = [[TYSMExtensionObject alloc] init];
    obj.vendorName = name;
    obj.mediaFilterRawProvider = provider;
    obj.mediaFilterProvider = nil;
    return obj;
}

+(TYSMExtensionObject *)generateExtensionObjectWith:(TYSM_VENDOR_NAME)name OCProvider:(id<AgoraExtProviderDelegate>)provider {
    NSParameterAssert(name);
    NSParameterAssert(provider);
    
    TYSMExtensionObject *obj = [[TYSMExtensionObject alloc] init];
    obj.vendorName = name;
    obj.mediaFilterRawProvider = nil;
    obj.mediaFilterProvider = provider;
    return obj;
}

- (NSString * _Nonnull)vendor {
    NSParameterAssert(_vendorName);
    return _vendorName;
}

- (void *)mediaFilterRawProvider {
    NSParameterAssert(_vendorName);
    return _mediaFilterRawProvider;
}

- (id<AgoraExtProviderDelegate>)mediaFilterProvider {
    NSParameterAssert(_vendorName);
    return _mediaFilterProvider;
}

- (void)dealloc {
    NSLog(@"%s",__func__);
}

@end
