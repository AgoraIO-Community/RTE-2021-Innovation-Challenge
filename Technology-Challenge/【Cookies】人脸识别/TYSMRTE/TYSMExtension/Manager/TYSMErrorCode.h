//
//  TYSMErrorCode.h
//  TYSMExtension
//
//  Created by jele lam on 23/5/2021.
//

#ifndef BDErrorCode_h
#define BDErrorCode_h

#import <Foundation/Foundation.h>

typedef NS_ENUM(int, TYSMErrorCode) {
    TYSMErrorCodeOK = 0,                    // 成功
    TYSMErrorCodeNotInitRTCEngine = 1,      // 初始化 RCT 引擎失败
    TYSMErrorCodeNotInitVideoFilter = 2,    // 初始化 Fliter 失败
    TYSMErrorCodeNotInitPluginManager = 3,  // 初始化插件 manager 失败
    TYSMErrorCodeErrorParameter = 10,       // 参数错误
    TYSMErrorCodeInvalidJSON = 100,         // 非 JSON 结构
    TYSMErrorCodeInvalidJSONType = 101,     // json 内部类型解析错误
    TYSMErrorCodeInvalidYUV = 102,          // yuv 解析失败
    TYSMErrorCodeInvalidEAGLContext = 103,  // 切换 context 失败
};

#endif /* BDErrorCode_h */
