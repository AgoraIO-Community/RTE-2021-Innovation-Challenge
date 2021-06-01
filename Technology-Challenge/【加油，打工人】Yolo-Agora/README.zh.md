## 项目目标
1. 提供对Yolo v3算法配置和封装；
2. 提供Unity3D与开源机器学习算法Yolo v3的结合使用方法；
3. 提供对Agora以及其他视频源在Unity 3D层面的AI识别Demo；
## 项目过程
#### Yolo V3 封装
这一阶段使用[AlexeyAB](https://github.com/AlexeyAB/darknet)在Windows上配置Yolo V3并实现运行和视频识别  
目前系统封装了如下主要函数
```c
//定义C#的Debug函数，方便调试
typedef void(*FuncPtr)(const char *);
//传入Debug托管函数
extern "C" YOLODLL_API void  set_debug(FuncPtr fp);
//传入是否展示Opencv渲染输出
extern "C" YOLODLL_API void  set_show(bool s);
//测试callback
extern "C" YOLODLL_API void  test(char* s);
//仅能使用NEt视频流，Web视频流的识别
extern "C" YOLODLL_API bool detect_net( char* filename, char* type, float thresh , bool use_mean);
//配合上一函数使用
extern "C" YOLODLL_API int  update_tracking(uchar* data, bbox_t_container &container,int &w,int &h);
//辅助色彩转换函数
extern "C" YOLODLL_API int  bgr_to_rgb(const uint8_t* src, const size_t data_length, uchar* des);
//辅助大小转换（图片太大时候使用）
extern "C" YOLODLL_API int resize(const uint8_t* src, const size_t data_length, int w, int h, uchar* des);
//初始化，传入Yolo v3cfg、weights等
extern "C" YOLODLL_API int init(const char *configurationFilename, const char *weightsFilename, const char* names, int gpu);
//识别单张图片文件
extern "C" YOLODLL_API int detect_image(const char *filename, bbox_t_container &container);
//识别单张图片文件bytes
extern "C" YOLODLL_API int detect_mat( uint8_t* data, const size_t data_length,  bbox_t_container &container, float thresh, bool use_mean);
//关闭系统
extern "C" YOLODLL_API int dispose();
```
#### C++与C#通信
这一阶段完成C++数据和C#的交互通信，视频的传入传输和识别结果传递
```c#
  [DllImport(YoloLibraryName, EntryPoint = "init")]
private static extern int InitializeYolo(string configurationFilename, string weightsFilename, string vocNaame,int gpu);

[DllImport(YoloLibraryName, EntryPoint = "detect_image")]
private static extern int DetectImage(string filename, ref BboxContainer container);

[DllImport(YoloLibraryName, EntryPoint = "detect_mat")]
private static extern int DetectImage(IntPtr pArray, int nSize,ref BboxContainer container, float thresh = 0.200000003F, bool use_mean = false);

[DllImport(YoloLibraryName, EntryPoint = "dispose")]
private static extern int DisposeYolo();

[DllImport(YoloLibraryName, EntryPoint = "set_debug")]
private static extern int SetDebug(IntPtr fp);

[DllImport(YoloLibraryName, EntryPoint = "test")]
private static extern int Test(string s);

[DllImport(YoloLibraryName, EntryPoint = "detect_net")]
private static extern bool DetectNet(string filename, string type, float thresh = 0.200000003F, bool use_mean = false);

[DllImport(YoloLibraryName, EntryPoint = "update_tracking")]
private static extern int UpdateVideoRawData(IntPtr data, ref BboxContainer container, ref int w,ref int h);

[DllImport(YoloLibraryName, EntryPoint = "resize")]
private static extern int ResizeImage(IntPtr src,int nSize , int w,  int h, IntPtr des);

[DllImport(YoloLibraryName, EntryPoint = "set_show")]
private static extern int SetShow(bool show);
```
#### Unity 3D前端
1. 使用AVProVideo 进行视频的解析
2. 使用Agora SDK提供RTM和RTC音视频数据
3. 使用Chart And Graph插件做表格展示
4. 以及其他逻辑设计
#### 目前效果
1. 实现对三种视频流的展示和实时识别
2. 支持多种识别数据库，只需替换对应文件即可实现多种识别
3. 实时性强，超过60帧

