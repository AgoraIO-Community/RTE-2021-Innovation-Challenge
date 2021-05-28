package com.kangraoo.basektlib.tools

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.kangraoo.basektlib.tools.log.ULog

object URouter {

    fun navigationFragment(
        context: Context? = null,
        path: String,
        bundle: Bundle? = null
    ): Fragment {
        val arouter = ARouter.getInstance().build(path)
        if (bundle != null) {
            arouter.with(bundle)
        }
        return if (context != null) {
            arouter.navigation(context) as Fragment
        } else {
            arouter.navigation() as Fragment
        }
    }

    fun navigation(
        context: Context? = null,
        path: String,
        bundle: Bundle? = null,
        flags: Int? = null
    ) {
        val arouter = ARouter.getInstance().build(path)
        if (bundle != null) {
            arouter.with(bundle)
        }
        if (flags != null) {
            arouter.withFlags(flags)
        }
        if (context != null) {
            arouter.navigation(context)
        } else {
            arouter.navigation()
        }
    }

//    /**
//     * 使用 [ARouter] 根据 `path` 跳转到对应的页面, 这个方法因为没有使用 [Activity]跳转
//     * 所以 [ARouter] 会自动给 [android.content.Intent] 加上 Intent.FLAG_ACTIVITY_NEW_TASK
//     * 如果不想自动加上这个 Flag 请使用 [ARouter.getInstance] 并传入 [Activity]
//     *
//     * @param path `path`
//     */
//    @Deprecated("废弃")
//    fun navigation(path: String) {
//        ARouter.getInstance().build(path).navigation()
//    }
//    @Deprecated("废弃")
//    fun navigation(path: String, bundle: Bundle?) {
//        ARouter.getInstance().build(path).with(bundle).navigation()
//    }
//
//    /**
//     * 使用 [ARouter] 根据 `path` 跳转到对应的页面, 如果参数 `context` 传入的不是 [Activity]
//     * [ARouter] 就会自动给 [android.content.Intent] 加上 Intent.FLAG_ACTIVITY_NEW_TASK
//     * 如果不想自动加上这个 Flag 请使用 [Activity] 作为 `context` 传入
//     *
//     * @param context
//     * @param path
//     */
//    @Deprecated("废弃")
//    fun navigation(context: Context, path: String) {
//        ARouter.getInstance().build(path).navigation(context)
//    }
//
//    @Deprecated("废弃")
//    fun navigation(
//        context: Context,
//        path: String,
//        bundle: Bundle?
//    ) {
//        ARouter.getInstance().build(path).with(bundle).navigation(context)
//    }

    fun <T : IProvider> provide(clz: Class<T>, path: String): T? {
        return ARouter.getInstance()
            .build(path)
            .navigation() as T?
    }

    fun <T : IProvider> provide(clz: Class<T>): T? {
        return ARouter.getInstance().navigation(clz) as T?
    }

    fun <T : IProvider> provideCallBack(clz: Class<T>, path: String): T? {
        return ARouter.getInstance()
            .build(path)
            .navigation(null, object : NavCallback() {
                override fun onArrival(postcard: Postcard?) {
                    ULog.d("ARouter", "${postcard?.path}模块跳转成功")
                }

                override fun onLost(postcard: Postcard?) {
                    super.onLost(postcard)
                    ULog.d("ARouter", "${postcard?.path}没有找到该模块")
                }
            }) as T?
    }
}
