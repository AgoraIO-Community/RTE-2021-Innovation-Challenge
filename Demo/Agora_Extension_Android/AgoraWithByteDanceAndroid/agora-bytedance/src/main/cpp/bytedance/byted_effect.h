// Copyright (C) 2018 Beijing Bytedance Network Technology Co., Ltd.

#ifndef DEMO_BYTED_AFFECT_H
#define DEMO_BYTED_AFFECT_H

#include <android/log.h>

#define NELEMS(x) ((int) (sizeof(x) / sizeof((x)[0])))

#define LOG_TAG "bef_effect_ai"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#endif //DEMO_BYTED_AFFECT_H
