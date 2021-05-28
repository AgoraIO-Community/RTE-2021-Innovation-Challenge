package com.qdedu.baselibcommon.data

import android.os.Parcelable
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.data.model.SelectModel
import com.kangraoo.basektlib.tools.SSysStore
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * 注意环境变量每次更改后需要重新清楚数据
 * Created by hyy on 2018/09/05.
 */
object AppHuanJingFactory {
    /**
     * 注意环境变量每次更改后需要重新清楚数据
     */
    val dev = AppDataModel(
        "开发",
        "",
        ""
    )
    val test = AppDataModel(
        "测试",
        "",
        ""
    )
    val online = AppDataModel(
        "线上",
        "",
        ""
    )
    val uat = AppDataModel(
        "预发布",
        "",
        ""
    )
    val huanJingSelectList: List<SelectModel<*>>?
        get() {
            if (SApplication.instance().sConfiger.debugStatic) {
                var selectModels = ArrayList<SelectModel<*>>()
                selectModels.add(
                    SelectModel(
                        online.name,
                        online, false
                    )
                )
                selectModels.add(
                    SelectModel(
                        dev.name,
                        dev, false
                    )
                )
                selectModels.add(
                    SelectModel(
                        test.name,
                        test, false
                    )
                )
                selectModels.add(
                    SelectModel(
                        uat.name,
                        uat, false
                    )
                )
                return selectModels
            }
            return null
        }

    val appModel: AppDataModel
        get() {
            val selectModel = SSysStore.instance.sysDebugDataSelect
            if (selectModel != null) {
                val o: Any? = selectModel.value
                if (o != null && o is AppDataModel) {
                    return o
                }
            }
            return online
        }

}
/**
 * 注意环境变量每次更改后需要重新清楚数据
 */
@Parcelize
data class AppDataModel(
    val name: String,
    var apiDomains: String,
    var h5Domains: String,
    var gameDomains: String = "",
    var umappid: String? = null,
    var qiyuappid: String? = null,
    var huanxinappid: String? = null,
    var shareEntity:ShareEntity?=null,
    val updateDomains:String = "",
    var buglyappid:String? = null,
    var youdaoappid:String? = null
) : Parcelable
/**
 * 注意环境变量每次更改后需要重新清楚数据
 */
@Parcelize
data class ShareEntity(val wxKey: String? = null,
                       val wxValue: String? = null,
                       val qqKey: String? = null,
                       val qqValue: String? = null,
                       val wbKey: String? = null,
                       val wbValue: String? = null,
                       val dingKey: String? = null,
                       val logoId:Int? = null): Parcelable

