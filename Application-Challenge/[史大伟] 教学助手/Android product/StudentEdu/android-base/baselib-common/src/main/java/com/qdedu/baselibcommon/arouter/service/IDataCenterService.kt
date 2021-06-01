package com.qdedu.baselibcommon.arouter.service

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * Time:
 * 2020-06-18
 * Creator:
 * GuFanFan.
 * Description:
 * -.
 */
interface IDataCenterService : IProvider {

    /**
     * 用户id.
     */
    fun userId(): String

    /**
     * 用户年级.
     */
    fun userGrade(): String

    /**
     * 用户性别.
     */
    fun userGender(): String

    /**
     * APP token.
     */
    fun appToken(): String

    /**
     * 用户信息.
     */
    fun userInfo(): String

    /**
     * 用户年级信息.
     */
    fun userGradeInfo(): String

    /**
     * 主路由.
     */
    fun hostMain(): String

    fun schoolId():Long

    fun roleId():Int

    /**
     * 用户所在班级列表
     */
    fun userClassIdList():List<Long>
}