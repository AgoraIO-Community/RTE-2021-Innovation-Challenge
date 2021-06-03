package com.game.tingshuo.widgets

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import java.io.Serializable
import java.util.*

/**
 * Created by twan on 2021/01/02.
 */
class Router private constructor() {
    private val intent: Intent?
    private var from: Activity? = null
    private var to: Class<*>? = null
    private var data: Bundle? = null
    private var options: ActivityOptionsCompat? = null
    private var requestCode = -1
    private var enterAnim = RES_NONE
    private var exitAnim = RES_NONE
    fun to(to: Class<*>?): Router {
        this.to = to
        return this
    }

    fun addFlags(flags: Int): Router {
        intent?.addFlags(flags)
        return this
    }

    fun data(data: Bundle?): Router {
        this.data = data
        return this
    }

    fun putByte(key: String?, value: Byte): Router {
        bundleData.putByte(key, value)
        return this
    }

    fun putChar(key: String?, value: Char): Router {
        bundleData.putChar(key, value)
        return this
    }

    fun putInt(key: String?, value: Int): Router {
        bundleData.putInt(key, value)
        return this
    }

    fun putString(key: String?, value: String?): Router {
        bundleData.putString(key, value)
        return this
    }

    fun putShort(key: String?, value: Short): Router {
        bundleData.putShort(key, value)
        return this
    }

    fun putFloat(key: String?, value: Float): Router {
        bundleData.putFloat(key, value)
        return this
    }

    fun putCharSequence(key: String?, value: CharSequence?): Router {
        bundleData.putCharSequence(key, value)
        return this
    }

    fun putParcelable(key: String?, value: Parcelable?): Router {
        bundleData.putParcelable(key, value)
        return this
    }

    fun putParcelableArray(key: String?, value: Array<Parcelable?>?): Router {
        bundleData.putParcelableArray(key, value)
        return this
    }

    fun putParcelableArrayList(key: String?, value: ArrayList<out Parcelable?>?): Router {
        bundleData.putParcelableArrayList(key, value)
        return this
    }

    fun putIntegerArrayList(key: String?, value: ArrayList<Int?>?): Router {
        bundleData.putIntegerArrayList(key, value)
        return this
    }

    fun putStringArrayList(key: String?, value: ArrayList<String?>?): Router {
        bundleData.putStringArrayList(key, value)
        return this
    }

    fun putCharSequenceArrayList(key: String?, value: ArrayList<CharSequence?>?): Router {
        bundleData.putCharSequenceArrayList(key, value)
        return this
    }

    fun putSerializable(key: String?, value: Serializable?): Router {
        bundleData.putSerializable(key, value)
        return this
    }

    fun options(options: ActivityOptionsCompat?): Router {
        this.options = options
        return this
    }

    fun requestCode(requestCode: Int): Router {
        this.requestCode = requestCode
        return this
    }

    fun anim(enterAnim: Int, exitAnim: Int): Router {
        this.enterAnim = enterAnim
        this.exitAnim = exitAnim
        return this
    }

    fun launch() {
        try {
            if (intent != null && from != null && to != null) {
                callback?.onBefore(from!!, to!!)
                intent.setClass(from!!, to!!)
                intent.putExtras(bundleData)
                if (options == null) {
                    if (requestCode < 0) {
                        from!!.startActivity(intent)
                    } else {
                        from!!.startActivityForResult(intent, requestCode)
                    }
                    if (enterAnim > 0 && exitAnim > 0) {
                        from!!.overridePendingTransition(enterAnim, exitAnim)
                    }
                } else {
                    if (requestCode < 0) {
                        ActivityCompat.startActivity(from!!, intent, options!!.toBundle())
                    } else {
                        ActivityCompat.startActivityForResult(
                            from!!,
                            intent,
                            requestCode,
                            options!!.toBundle()
                        )
                    }
                }
                callback?.onNext(from!!, to!!)
            }
        } catch (throwable: Throwable) {
            callback?.onError(from!!, to!!, throwable)
        }
    }

    private val bundleData: Bundle
        private get() {
            if (data == null) {
                data = Bundle()
            }
            return data!!
        }

    companion object {
        const val RES_NONE = -1
        private var callback: RouterCallback? = null
        fun newIntent(context: Activity?): Router {
            val router = Router()
            router.from = context
            return router
        }

        fun pop(activity: Activity) {
            activity.finish()
        }

        fun setCallback(callback: RouterCallback?) {
            Companion.callback = callback
        }
    }

    init {
        intent = Intent()
    }


    //回调
    interface RouterCallback {
        fun onBefore(from: Activity, to: Class<*>)
        fun onNext(from: Activity, to: Class<*>)
        fun onError(from: Activity, to: Class<*>, throwable: Throwable)
    }

}