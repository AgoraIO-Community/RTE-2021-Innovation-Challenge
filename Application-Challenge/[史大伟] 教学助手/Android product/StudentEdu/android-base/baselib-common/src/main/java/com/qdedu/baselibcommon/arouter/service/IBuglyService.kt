package com.qdedu.baselibcommon.arouter.service

import com.alibaba.android.arouter.facade.template.IProvider


interface IBuglyService : IProvider {

    fun putUserData(user:String)

    fun putData(key:String,value:String)

}