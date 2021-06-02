# 作品简介

> 本作品在 iOS 平台上使用 AgoraEngineKit2 加载一个基于 C++ 语言封装的的人脸识别插件，

## 功能
* 脸部追踪
* 微笑识别
* 打哈欠识别

![tutieshi_320x691_4s](https://user-images.githubusercontent.com/16119393/120501599-4f9c1e00-c3f4-11eb-8243-ddb6ef7a5006.gif)
![tutieshi_320x691_6s](https://user-images.githubusercontent.com/16119393/120500616-93425800-c3f3-11eb-9cb4-ac79351c9e48.gif)                        ![tutieshi_320x691_10s](https://user-images.githubusercontent.com/16119393/120500666-9c332980-c3f3-11eb-8876-65aa21d36ae7.gif)

## 使用方法：

> 插件的初始化和配置全依赖 AgoraEngineKit2 提供的方法进行传递
```Objecitve-C
- (int)setExtensionPropertyWithVendor:(AgoraMediaSourceType)sourceType vendor:(NSString * __nonnull)vendor key:(NSString * __nonnull)key value:(NSString * __nonnull)value;
```
1. 初始化 宽高 ，插件仅支持 480 * 640
2. 按功能需要传入 相应参数

### Example：

```Objective-C
///  初始化
    NSDictionary *data = @{
                            @"brf.init" : @{
                                              @"width" : @(AgoraVideoDimension640x480.width),
                                              @"height" : @(AgoraVideoDimension640x480.height)
                                            }
                         };

    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:data options:NSJSONWritingPrettyPrinted error:&error];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
            
    [self.agoraKit setExtensionPropertyWithVendor:AgoraMediaSourceTypeVideoCamera vendor:vendorName key:@"key" value:jsonString];
    ...
    ...
  
///  传入参数
   NSDictionary *data = @{
                            @"brf.single_face.enable" : @(YES),
                            @"brf.smile.enable" : @(NO),
                            @"brf.yawn.enable" : @(NO)
    };

    ...
    ...

    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:data options:NSJSONWritingPrettyPrinted error:&error];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
            
    [self.agoraKit setExtensionPropertyWithVendor:AgoraMediaSourceTypeVideoCamera vendor:vendorName key:@"key" value:jsonString];


```


## 开启 / 关闭特效

> 这里有两个方式，一种是通过传入参数，针对该插件的某个特效进行控制。一种是通过调用 AgoraEngineKit 方法，针对整个插件。后者级别最高。

接口如下
```
/// 传入参数
- (int)setExtensionPropertyWithVendor:(AgoraMediaSourceType)sourceType vendor:(NSString * __nonnull)vendor key:(NSString * __nonnull)key value:(NSString * __nonnull)value;
/// 整个插件
- (int)enableExtensionWithVendor:(AgoraMediaSourceType)sourceType vendor:(NSString * __nonnull)vendor enabled:(BOOL)enabled;

```

### Example
1. 使用参数传递
```Objective-C

@implementation ViewController {
    ...
    ...
    
    NSDictionary *data = @{
                            @"brf.single_face.enable" : @(YES),
                            @"brf.smile.enable" : @(NO),
                            @"brf.yawn.enable" : @(NO)
    };

    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:data options:NSJSONWritingPrettyPrinted error:&error];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
            
    [self.agoraKit setExtensionPropertyWithVendor:AgoraMediaSourceTypeVideoCamera vendor:vendorName key:@"key" value:jsonString];
    ...
    ...
    
@end
```

2. 使用 AgoraEngineKit 方法控制

``` Objectice-C
- (IBAction)tapExtension:(UIButton *)sender {
    sender.selected = !sender.selected;
    
    self.featureButton.enabled = sender.selected;
    
    for (TYSM_VENDOR_NAME vendorName in TYSMExtensionManger.sharedInstance.vendorNames) {
            [self.agoraKit enableExtensionWithVendor:AgoraMediaSourceTypeVideoCamera vendor:vendorName enabled:sender.selected];
    }
    
}
```
如图所示：

![tutieshi_320x691_15s](https://user-images.githubusercontent.com/16119393/120506057-27162300-c3f8-11eb-9d85-875dc75b19be.gif)


## 文件结构

![image](https://user-images.githubusercontent.com/16119393/120113548-8e8c6280-c1ad-11eb-97e1-8e5782774415.png)


### 插件管理
通过 ***TYSMExtensionManger*** 类与对外交互，对内则处理插件实现的相关逻辑 
#### 插件实现
主要是将 ***IExtensionProvider***、***IVideoFilter*** 和自己的封装的 ***processer*** 放在这里
### 插件库
插件库的官方源码
### 第三方库
* ***rapidjson***: 官方推荐的  json 工具，好东西。
* ***libyuv***: Google开源的实现各种YUV与RGB之间相互转换、旋转、缩放的库


## TYSMExtension 实现介绍

用 framework 方式对外公开两个文件

```Objective-C
#import <TYSMExtension/TYSMVendor.h>
#import <TYSMExtension/TYSMExtensionManger.h>
```
***TYSMVendor*** 插件名，本 framework 所集成的插件都罗列在这里。其作用一是方便查阅、二是作为参数传递，增强代码可阅读。
***TYSMExtensionManger*** 一个是单例，客户端只需要调用 ***loadPluginWithVendor*** 方法，配套 ***TYSMVendor.h*** 提供的插件名即可加载完毕，开放的接口如下：

```Objective-C
/// 组件名
- (NSArray <NSString *> * __nonnull)vendorNames;

/// 加载插件
/// @param vendorName 插件名，参考 TYSMVendor
- (BOOL)loadPluginWithVendor:(TYSM_VENDOR_NAME)vendorName;

/// 获取插件
- (NSArray *)getPlugins;
```

#### Example ： 
```Objective-C
    AgoraRtcEngineConfig *cfg = [AgoraRtcEngineConfig new];

    ·····

    TYSMExtensionManger *manager = [TYSMExtensionManger sharedInstance];
    [manager loadPluginWithVendor:TYSM_VENDOR_NAME_BRF];
    NSArray *plugs = [manager getPlugins];
    cfg.mediaFilterExtensions = plugs;

    ·······
    
```

### framework 内部构造

***TYSMExtensionManger*** 内部所做的事情主要是声明一个插件对象***TYSMExtensionObject*** 把该对象放进数组里面
  * 外部可以通过调用 vendorNames 方法获取已经加载的插件名
  * 也可以通过调用 getPlugins 方法获取已加载的插件对象

***TYSMExtensionObject*** 封装一个 Extension 类，实现 AgoraMediaFilterExtensionDelegate 协议，填入相应的插件名，注册自己的插件.
  * 为了方便，头文件也简单封了创建
  * 插件可以是 C++ ，也可以是 OC .
  * 目前仅实现 C++ 
 
```Objective-C
/// 生成一个 Cpp 的 插件
/// @param name 插件名
/// @param provider C++ 插件 provider
+ (TYSMExtensionObject *)generateExtensionObjectWith:(TYSM_VENDOR_NAME)name cppProvider:(void *)provider;

/// 生成一个 OC 的 插件
/// @param name 插件名
/// @param provider OC 插件 provider
+ (TYSMExtensionObject *)generateExtensionObjectWith:(TYSM_VENDOR_NAME)name OCProvider:(id<AgoraExtProviderDelegate>)provider;

```

有了以上封装之后，接下来是参考官方 Demo 实现 IExtensionProvider、IVideoFilter，以及插件的 processer
  * 这部分主要是需要插件开发者能完全了解本插件的方法使用，提供相应的参数供客户端调用。
  * 与官方 IVideoFilter 不同的是，回调 onEvent 事件时，不放在 IVideoFilter ，而是直接让 processor 发起。以减少代码耦合情况发生，明确职责

***SO,经过以上简单粗暴的介绍，内部一个基本的创建流程基本是这样：***
```
TYSMExtensionManger | -> TYSMExtensionObject -> IExtensionProvider -> IVideoFilter -> processer
                    | -> TYSMExtensionObject -> IExtensionProvider -> IVideoFilter -> processer
                    ........
                    ........
                    ........
                    ........
```

引入相关的插件库，在 TYSMVendor 声明一个vender name，然后按照上述的流程，代码撸一把，提供相应的参数参考。完事。

#### Example：
```Objective-C
@implementation TYSMExtensionManger {
    ........
    // new 一个你的插件 provider 
      auto brf_provider = new agora::RefCountedObject<agora::extension::VideoProvider>();

      if (brf_provider == nullptr) {
          return NO;
      }
  
      brf_provider->setExtensionVendor(TYSM_VENDOR_NAME_BRF.UTF8String);

    // 构造一个 插件对象
      TYSMExtensionObject *obj = [TYSMExtensionObject generateExtensionObjectWith:vendorName cppProvider:brf_provider];
    // 加入到 数组里面去
      [self.vendorObjs addObject:obj];
  
      return YES;
    ........
}
```
   
# 插件思考：

1. 作品封装的目的能极少的代码给开发者提供丰富的功能效果。对于以往设计 SDK 的经验而言，相信的一个想法思路是：对外开放的类、接口越少，对外使用就更简单，对内的设计以及稳定性可维护性就越高。
2. TYSMVendor.h 声明初衷是为了统一内部代码、增强开发者体验。本着这个思路，客户端在配置插件参数，即传入 property 的字典所需要的 key value 集，其实也可以组织声明一套模版，做一个强绑定，但考虑到插件方的参数以及时间问题，最终决定分摊给开发者。
3. 对于 ``TYSMExtensionObject -> IExtensionProvider -> IVideoFilter -> processer`` 该流程来说，插件其实还可以再往深封一层，如 IExtensionProvider 可以按 PROVIDER_TYPE 区分等，但考虑到一个插件的内存占用（80-120M）左右，似乎有过度设计的味道

# 其他：

## Demo : 
***APPID.h***类文件用来配置 Agora token 





