# libyuv-ios

> libyuv是Google开源库，可用作图像数据格式的转换，比如视频流编解码时格式的转换，YUV数据转化RGB等
为了方便使用，已经将源代码打包成了iOS静态库，其中Code中的中的内容为静态库和头文件(静态库仅支持arm64)

下面以nv12(yuv420sp)转化为I420(yuv420p)为例：
```
void nv12_to_yuv420p(unsigned char* nv12, unsigned char* yuv420p, int width, int height) {
    
    int y_size = width * height;
    int u_size = y_size / 4;
    int v_size = u_size;
    int uv_size = u_size + v_size;

    unsigned char *src_y = malloc(y_size);
    unsigned char *src_uv = malloc(uv_size);
    memcpy(src_y, nv12, y_size);
    memcpy(src_uv, nv12 + y_size, uv_size);
    
    unsigned char *dst_y = malloc(y_size);
    unsigned char *dst_u = malloc(u_size);
    unsigned char *dst_v = malloc(v_size);
    
    int src_stride_y = width;
    int src_stride_uv = width;
    int dst_stride_y = width;
    int dst_stride_u = width >> 1;
    int dst_stride_v = dst_stride_u;
    
    NV12ToI420(src_y, src_stride_y, src_uv, src_stride_uv, dst_y, dst_stride_y, dst_u, dst_stride_u, dst_v, dst_stride_v, width, height);
    memcpy(yuv420p, dst_y, y_size);
    memcpy(yuv420p + y_size, dst_u, u_size);
    memcpy(yuv420p + y_size + u_size, dst_v, v_size);
    
    free(src_y);
    free(src_uv);
    free(dst_y);
    free(dst_u);
    free(dst_v);
}
```

