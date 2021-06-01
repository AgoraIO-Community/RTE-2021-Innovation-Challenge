package com.kangraoo.basektlib.exception

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2019/07/27
 * desc : 框架网络异常
 */
class LibNetWorkException(val code: Int = 0, val msg: String) : Exception("code:$code $msg")
