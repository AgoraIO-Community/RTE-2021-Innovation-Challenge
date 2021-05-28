package com.kangraoo.basektlib.data.model

import com.kangraoo.basektlib.tools.net.NetMap

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/15
 * desc : 网络请求，参数基础继承
 */
open class BParam

fun BParam.toNetMap(): Map<String, String> {
    return NetMap.httpBuildQueryMap(NetMap.paramDataToMapNoThrows(this))
}
