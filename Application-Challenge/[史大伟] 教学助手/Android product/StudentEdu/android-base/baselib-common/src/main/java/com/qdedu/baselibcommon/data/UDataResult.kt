package com.qdedu.baselibcommon.data

import com.google.gson.JsonSyntaxException
import com.kangraoo.basektlib.data.DataResult
import com.kangraoo.basektlib.data.netLibError
import com.kangraoo.basektlib.exception.LibNetWorkException
import com.kangraoo.basektlib.tools.HString
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.okhttp.HttpPersistentManager
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.tools.tip.TipToast
import com.qdedu.baselibcommon.app.HTTP_SUCCESS_CODE
import com.qdedu.baselibcommon.app.TOKEN_EXCEED_CODE
import com.qdedu.baselibcommon.app.USER_ACCOUNT_302_CODE
import com.qdedu.baselibcommon.data.model.responses.BasicApiResult
import com.qdedu.baselibcommon.exception.TokenExceedException
import com.qdedu.baselibcommon.exception.UserAccountException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun <T> DataResult.Success<T>.netSuccess():DataResult.Success<T>{
    var data = this.data
    if(data is BasicApiResult<*>){
        if(data.code.toInt()!= HTTP_SUCCESS_CODE){
            when(data.code.toInt()){
                in TOKEN_EXCEED_CODE -> {
                    //HttpPersistentManager.instance.removeAllPersistent(HString.host(AppHuanJingFactory.appModel.apiDomains))
                    ULog.d("清空应用，跳转登录")
                    throw TokenExceedException(data.code.toInt(),data.message)

//                    TipToast.tip(Tip.Error,"清空应用，跳转登录")
                }
                USER_ACCOUNT_302_CODE->{
                    ULog.d("登录失败")
                    throw UserAccountException(data.message)
                }
                else -> throw LibNetWorkException(data.code.toInt(),data.message)
            }
        }
    }
    return this
}


val DataResult<*>.netAppError
    get() = this.netLibError || this is DataResult.Error && (this.exception is TokenExceedException)
