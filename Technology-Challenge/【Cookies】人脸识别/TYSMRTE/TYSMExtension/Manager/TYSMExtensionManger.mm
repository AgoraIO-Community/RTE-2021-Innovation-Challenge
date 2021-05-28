//
//  TYSMExtensionManger.m
//  TYSMExtension
//
//  Created by Jele on 20/5/2021.
//

#import "TYSMExtensionManger.h"
#import <objc/runtime.h>

#include "TYSMVideoFilterProvider.h"
#import <OpenGLES/EAGL.h>
#import <TYSMExtensionObject.h>
#import <AgoraRtcKit2/AgoraMediaFilterExtensionDelegate.h>
#import <AgoraRtcKit2/AgoraRtcEngineKit.h>

static std::mutex mtx;

@interface TYSMExtensionManger ()
/// OpenGLES
@property (nonatomic, strong) EAGLContext* context;
/// 组件列表
@property (nonatomic, strong) NSMutableArray <id<AgoraMediaFilterExtensionDelegate>> *vendorObjs;
@end

@implementation TYSMExtensionManger

static TYSMExtensionManger* instance = nil;

+ (instancetype)sharedInstance {
    static dispatch_once_t onceToken ;
    dispatch_once(&onceToken, ^{
        std::cout << "========= RTE 2021 =========" << std::endl;
        std::cout << NSStringFromClass(self.class).UTF8String << " By Cookies" << std::endl ;
        std::cout << "============================" << std::endl;

        instance = [[super allocWithZone:NULL] init];
    }) ;
    
    return instance ;
}

+ (id)allocWithZone:(struct _NSZone *)zone {
    return [TYSMExtensionManger sharedInstance] ;
}

- (id)copyWithZone:(struct _NSZone *)zone {
    return [TYSMExtensionManger sharedInstance] ;
}

- (NSArray<NSString *> *)vendorNames {
    std::lock_guard<std::mutex> lock(mtx);
    return [self.vendorObjs valueForKey:@"vendorName"];
}

- (NSArray <id<AgoraMediaFilterExtensionDelegate>> *)getPlugins {
    std::lock_guard<std::mutex> lock(mtx);
    return self.vendorObjs.copy;
}

- (BOOL)loadPluginWithVendor:(TYSM_VENDOR_NAME)vendorName {
    std::lock_guard<std::mutex> lock(mtx);

    if ([vendorName isEqualToString:TYSM_VENDOR_NAME_BRF]) {
        NSLog(@"增加插件 %@",vendorName);
        
        auto brf_provider = new agora::RefCountedObject<agora::extension::VideoProvider>();
        
        if (brf_provider == nullptr) {
            return NO;
        }
        brf_provider->setExtensionVendor(TYSM_VENDOR_NAME_BRF.UTF8String);
        
        TYSMExtensionObject *obj = [TYSMExtensionObject generateExtensionObjectWith:vendorName cppProvider:brf_provider];
        [self.vendorObjs addObject:obj];
        return YES;
    }
    
    
    return NO;
}

- (BOOL)unloadPluginWithVendor:(TYSM_VENDOR_NAME)vendorName {
    std::lock_guard<std::mutex> lock(mtx);
    
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"vendorName == %@", vendorName];

    NSArray *willUnloadVendors = [self.vendorObjs filteredArrayUsingPredicate:predicate].copy;

    for (id<AgoraMediaFilterExtensionDelegate> vendorObj in self.vendorObjs) {
        NSLog(@"移除插件 ： %@",vendorObj.vendor);
        if ([vendorObj.vendor isEqualToString:vendorName]) {

            if (vendorObj.mediaFilterProvider) {
                
            }
            
            if (vendorObj.mediaFilterRawProvider) {

            }
        }
    }
    
    [self.vendorObjs removeObjectsInArray:willUnloadVendors];
    
    return YES;
}

#pragma mark - loadlazy

- (NSMutableArray *)vendorObjs {
    if (_vendorObjs == nil) {
        _vendorObjs = [NSMutableArray array];
    }
    return _vendorObjs;
}

#pragma mark - private
- (BOOL)initGL {
    if (_context == nil) {
        _context = [[EAGLContext alloc] initWithAPI:kEAGLRenderingAPIOpenGLES2];
    }
    return [EAGLContext setCurrentContext:_context];
}

- (BOOL)releaseGL {
    if ([EAGLContext setCurrentContext:nil]) {
        _context = nil;
        return YES;
    }
    return NO;
}

- (BOOL)makeCurrent {
    EAGLContext *prev = [EAGLContext currentContext];
    if (prev != _context) {
        return [EAGLContext setCurrentContext:_context];
    }
    return YES;
}


@end

extern "C" bool initGL() {
    return [[TYSMExtensionManger sharedInstance] initGL];
}

extern "C" bool releaseGL() {
    return [[TYSMExtensionManger sharedInstance] releaseGL];
}

extern "C" bool makeCurrent() {
    return [[TYSMExtensionManger sharedInstance] makeCurrent];
}
