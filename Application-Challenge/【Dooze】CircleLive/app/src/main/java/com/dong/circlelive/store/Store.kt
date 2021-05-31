package com.dong.circlelive.store

import com.tencent.mmkv.MMKV


/**
 * Create by dooze on 2021/5/25  1:51 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */

val store by lazy { MMKV.defaultMMKV()!! }


const val KEY_LIVE_FILTER_PATH = "key_live_filter_path"

const val KEY_LIVE_FILTER_INTENSITY = "key_live_filter_intensity"

const val KEY_SHOWED_FILTER_TIPS = "key_showed_filter_tips"