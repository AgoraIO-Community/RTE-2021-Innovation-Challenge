package com.kangraoo.basektlib.data.source

import com.kangraoo.basektlib.data.source.local.BaseLocalDataSource
import com.kangraoo.basektlib.data.source.remote.BaseRemoteDataSource

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/15
 * desc : 数据仓库基础类
 */
open class BaseRepository<T1 : BaseLocalDataSource, T2 : BaseRemoteDataSource>(val localDataSource: T1, val remoteDataSource: T2)
