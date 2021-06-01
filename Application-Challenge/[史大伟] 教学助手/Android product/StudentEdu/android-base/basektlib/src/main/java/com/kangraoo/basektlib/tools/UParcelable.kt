package com.kangraoo.basektlib.tools

import android.os.Parcel
import android.os.Parcelable
import com.kangraoo.basektlib.tools.log.ULog
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel


/**
 * @author shidawei
 * 创建日期：2021/2/20
 * 描述：
 */
object UParcelable {
    fun marshall(parceable: Parcelable): ByteArray {
        val source = Parcel.obtain()
        parceable.writeToParcel(source, parceable.describeContents())
        val bytes = source.marshall()
        source.recycle()
        return bytes
    }

    private val mCreators: HashMap<String, Parcelable.Creator<*>?> = HashMap()

    fun <T : Parcelable?> decodeParcelable(bytes: ByteArray, tClass: Class<T>): T? {
        val source = Parcel.obtain()
        source.unmarshall(bytes, 0, bytes.size)
        source.setDataPosition(0)
        try {
            val name = tClass.toString()
            var creator: Parcelable.Creator<*>?
            synchronized(mCreators) {
                creator = mCreators[name]
                if (creator == null) {
                    val f = tClass.getField("CREATOR")
                    creator = f[null as Any?] as Parcelable.Creator<*>
                    if (creator != null) {
                        mCreators[name] = creator
                    }
                }
            }
            if (creator == null) {
                throw Exception("Parcelable protocol requires a non-null static Parcelable.Creator object called CREATOR on class $name")
            }
            return creator!!.createFromParcel(source) as T
        } catch (var16: Exception) {
            ULog.e(var16)
        } finally {
            source.recycle()
        }
        return null
    }
}