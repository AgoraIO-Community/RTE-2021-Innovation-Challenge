// Copyright (C) 2019 Beijing Bytedance Network Technology Co., Ltd.
#ifndef EFFECT_SDK_BEF_EFFECT_AI_PUBLIC_DEFINE_H
#define EFFECT_SDK_BEF_EFFECT_AI_PUBLIC_DEFINE_H


#define BEF_INTENSITY_TYPE_NONE                 0
#define BEF_INTENSITY_TYPE_BEAUTY_WHITEN        1   // 美白
#define BEF_INTENSITY_TYPE_BEAUTY_SMOOTH        2   // 磨平
#define BEF_INTENSITY_TYPE_FACE_SHAPE           3   // 大眼/瘦脸
#define BEF_INTENSITY_TYPE_BEAUTY_SHARP         9   // 锐化
#define BEF_INTENSITY_TYPE_GLOBAL_FILTER_V2     12  // 滤镜
#define BEF_INTENSITY_TYPE_BUILDIN_LIP          17  // 唇色
#define BEF_INTENSITY_TYPE_BUILDIN_BLUSHER      18  // 腮红


//bef_framework_public_base_define
#    ifdef __cplusplus
#        ifdef _EFFECT_SDK_EXPORTS_
#            define BEF_SDK_API extern "C" __attribute__((visibility ("default")))
#        else
#            define BEF_SDK_API extern "C"
#        endif
#    else
#        ifdef _EFFECT_SDK_EXPORTS_
#            define BEF_SDK_API __attribute__((visibility ("default")))
#        else
#            define BEF_SDK_API
#        endif
#    endif


typedef    short           int16_t;
typedef    int             int32_t;

typedef unsigned long long UINT64;
// def byted effect handle
typedef void *bef_effect_handle_t;

// def byted effect result
typedef int bef_effect_result_t;

typedef int effect_result;

// define bef_intensity_type
typedef int bef_intensity_type;


//bef_framework_public_constant_define
#define BEF_RESULT_SUC                       0  // 成功返回
#define BEF_RESULT_FAIL                     -1  // 内部错误
#define BEF_RESULT_FILE_NOT_FIND            -2  // 文件没找到

#define BEF_RESULT_INVALID_INTERFACE        -3  // 接口未实现
#define BEF_RESULT_FILE_OPEN_FAILED         -4  // 文件打开失败

#define BEF_RESULT_INVALID_EFFECT_HANDLE    -5  // 无效的Effect句柄
#define BEF_RESULT_INVALID_EFFECT_MANAGER   -6  // 无效的EffectManager
#define BEF_RESULT_INVALID_FEATURE_HANDLE   -7  // 无效的Feature句柄
#define BEF_RESULT_INVALID_FEATURE          -8  // 无效的Feature
#define BEF_RESULT_INVALID_RENDER_MANAGER   -9  // 无效的RenderManager

#define BEF_RESULT_ALG_FACE_106_CREATE_FAIL -22 // 人脸检测106算法创建失败
#define BEF_RESULT_ALG_FACE_280_CREATE_FAIL -23 // 人脸检测280算法创建失败
#define BEF_RESULT_ALG_FACE_PREDICT_FAIL    -24 // 人脸检测预测失败

#define BEF_RESULT_ALG_HAND_CREATE_FAIL     -26 // 创建手势算法失败
#define BEF_RESULT_ALG_HAND_PREDICT_FAIL    -27 // 手势算法预测失败


#define BEF_RESULT_INVALID_TEXTURE          -36 // 无效的texture
#define BEF_RESULT_INVALID_IMAGE_DATA       -37 // 无效的图像数据
#define BEF_RESULT_INVALID_IMAGE_FORMAT     -38 // 无效的图片格式
#define BEF_RESULT_INVALID_PARAM_TYPE       -39 // 无效的参数类型
#define BEF_RESULT_INVALID_RESOURCE_VERSION -40 // 资源文件指定sdk版本过高
#define BEF_RESULT_INVALID_PARAM_VALUE      -47 // 无效的参数值

#define BEF_RESULT_SMASH_E_INTERNAL         -101 // 未定义内部错误
#define BEF_RESULT_SMASH_E_NOT_INITED       -102 // 未初始化相关资源
#define BEF_RESULT_SMASH_E_MALLOC           -103 // 申请内存失败
#define BEF_RESULT_SMASH_E_INVALID_PARAM    -104 // 无效的参数
#define BEF_RESULT_SMASH_E_ESPRESSO         -105 // ESPRESSO错误
#define BEF_RESULT_SMASH_E_MOBILECV         -106 // MOBILECV错误
#define BEF_RESULT_SMASH_E_INVALID_CONFIG   -107 // 无效的配置
#define BEF_RESULT_SMASH_E_INVALID_HANDLE   -108 // 无效的句柄
#define BEF_RESULT_SMASH_E_INVALID_MODEL    -109 // 无效的模型
#define BEF_RESULT_SMASH_E_INVALID_PIXEL_FORMAT        -110 // 无效的图像格式
#define BEF_RESULT_SMASH_E_INVALID_POINT               -111 // 无效的点
#define BEF_RESULT_SMASH_E_REQUIRE_FEATURE_NOT_INIT    -112 // 依赖模块没有初始化
#define BEF_RESULT_SMASH_E_NOT_IMPL                    -113 // 未实现的接口

#define BEF_RESULT_INVALID_LICENSE                     -114 // 无效的license
#define BEF_RESULT_NULL_BUNDLEID                       -115 // Application/Bundle ID 为空
#define BEF_RESULT_LICENSE_STATUS_INVALID              -116 // 非法授权文件
#define BEF_RESULT_LICENSE_STATUS_EXPIRED              -117 // 授权文件过期
#define BEF_RESULT_LICENSE_STATUS_NO_FUNC              -118 // 请求功能不匹配
#define BEF_RESULT_LICENSE_STATUS_ID_NOT_MATCH         -119 // Application/Bundle ID 不匹配

#define BEF_RESULT_LICENSE_BAG_NULL_PATH               -120 // 授权包路径为空
#define BEF_RESULT_LICENSE_BAG_INVALID_PATH            -121 // 错误的授权包路径
#define BEF_RESULT_LICENSE_BAG_TYPE_NOT_MATCH          -122 // 授权包类型不匹配
#define BEF_RESULT_LICENSE_BAG_INVALID_VERSION         -123 // 无效的版本
#define BEF_RESULT_LICENSE_BAG_INVALID_BLOCK_COUNT     -124 // 无效的数据块
#define BEF_RESULT_LICENSE_BAG_INVALID_BLOCK_LEN       -125 // 无效的数据块长度
#define BEF_RESULT_LICENSE_BAG_INCOMPLETE_BLOCK        -126 // 数据块不完整
#define BEF_RESULT_LICENSE_BAG_UNAUTHORIZED_FUNC       -127 // license未授权的功能

#define BEF_RESULT_SDK_FUNC_NOT_INCLUDE                -128 // SDK 未包含功能
#define BEF_RESULT_GL_ERROR_OCCUR                      -150 // opengl发生错误
// 注意 EffectSDK 内部使用了-114 -115， 我们使用TOB的宏进行隔离 并转换到 -151 -152，后续错误码需要避开
//#define BEF_RESULT_GL_CONTECT               -151 //无效的glcontext
//#define BEF_RESULT_GL_TEXTURE               -152 //无效的gltexture


#define BEF_EFFECT_FEATURE_LEN                          128 //feature name 默认长度

// bef_framework_public_geometry_define
// @brief image rotate type definition
typedef enum {
    BEF_AI_CLOCKWISE_ROTATE_0 = 0, // 图像不需要旋转，图像中的人脸为正脸
    BEF_AI_CLOCKWISE_ROTATE_90 = 1, // 图像需要顺时针旋转90度，使图像中的人脸为正
    BEF_AI_CLOCKWISE_ROTATE_180 = 2, // 图像需要顺时针旋转180度，使图像中的人脸为正
    BEF_AI_CLOCKWISE_ROTATE_270 = 3  // 图像需要顺时针旋转270度，使图像中的人脸为正
} bef_ai_rotate_type;

// ORDER!!!
typedef enum {
    BEF_AI_PIX_FMT_RGBA8888, // RGBA 8:8:8:8 32bpp ( 4通道32bit RGBA 像素 )
    BEF_AI_PIX_FMT_BGRA8888, // BGRA 8:8:8:8 32bpp ( 4通道32bit RGBA 像素 )
    BEF_AI_PIX_FMT_BGR888,   // BGR 8:8:8 24bpp ( 3通道32bit RGB 像素 )
    BEF_AI_PIX_FMT_RGB888,   // RGB 8:8:8 24bpp ( 3通道32bit RGB 像素 )
    BEF_AI_PIX_FMT_GRAY8,    // GRAY 8bpp ( 1通道8bit 灰度像素 ). 目前还不支持
    BEF_AI_PIX_FMT_YUV420P,  // YUV  4:2:0   12bpp ( 3通道, 一个亮度通道, 另两个为U分量和V分量通道, 所有通道都是连续的 ). 目前还不支持
    BEF_AI_PIX_FMT_NV12,     // YUV  4:2:0   12bpp ( 3通道, 一个亮度通道, 另一道为UV分量交错 ). 目前还不支持
    BEF_AI_PIX_FMT_NV21      // YUV  4:2:0   12bpp ( 3通道, 一个亮度通道, 另一道为VU分量交错 ). 目前还不支持
} bef_ai_pixel_format;


typedef struct bef_ai_fpoint_t {
    float x;
    float y;
} bef_ai_fpoint;

typedef struct bef_ai_fpoint3d_t {
    float x;
    float y;
    float z;
} bef_ai_fpoint3d;

typedef struct bef_ai_rect_t {
    int left;   // Left most coordinate in rectangle. 矩形最左边的坐标
    int top;    // Top coordinate in rectangle.  矩形最上边的坐标
    int right;  // Right most coordinate in rectangle.  矩形最右边的坐标
    int bottom; // Bottom coordinate in rectangle. 矩形最下边的坐标
} bef_ai_rect;

// Same definiation as bef_rect, but in float type
// 和bef_rect一样的定义，类型为单精度浮点
typedef struct bef_ai_rectf_t {
    float left;
    float top;
    float right;
    float bottom;
} bef_ai_rectf;


typedef enum bef_ai_camera_position_t {
    bef_ai_camera_position_front,
    bef_ai_camera_position_back
} bef_ai_camera_position;



typedef struct bef_ai_frect_st {
    float left;   ///< 矩形最左边的坐标
    float top;    ///< 矩形最上边的坐标
    float right;  ///< 矩形最右边的坐标
    float bottom; ///< 矩形最下边的坐标
} bef_ai_frect;


typedef struct bef_ai_image_t {
    const unsigned char *data;
    int width;
    int height;
    int stride;
    int format;
} bef_ai_image;

typedef struct bef_ai_tt_key_point_st {
    float x; // 对应 cols, 范围在 [0, width] 之间
    float y; // 对应 rows, 范围在 [0, height] 之间
    bool is_detect; // 如果该值为 false, 则 x,y 无意义
} bef_ai_tt_key_point;


#define EFFECT_HAND_DETECT_DELAY_FRAME_COUNT 4


#endif
