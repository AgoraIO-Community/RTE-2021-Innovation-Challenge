#ifndef VLIVE_COMMON_H
#define VLIVE_COMMON_H

#include <android/log.h>

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "vlive_audio", __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, "vlive_audio", __VA_ARGS__))

#endif //VLIVE_COMMON_H
