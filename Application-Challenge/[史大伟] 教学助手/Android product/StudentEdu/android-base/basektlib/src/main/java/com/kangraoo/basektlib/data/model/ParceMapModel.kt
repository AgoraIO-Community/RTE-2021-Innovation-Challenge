package com.kangraoo.basektlib.data.model

import android.content.Intent
import android.os.Parcelable
import android.util.ArrayMap
import com.google.android.material.internal.ParcelableSparseArray
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.exception.LibSaveDataException
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/15
 * desc : 安全模型
 */
const val BTYPE = 1
const val BYTYPE = 2
const val CTYPE = 3
const val STYPE = 4
const val ITYPE = 5
const val LTYPE = 6
const val FTYPE = 7
const val DTYPE = 8
const val STTYPE = 9
const val CHARTYPE = 10
const val PARTYPE = 11
const val PARSTYPE = 12
const val NULLTYPE = -1

@Parcelize
class ParceMapModel(var key:String?=null,var vb:Boolean? = null,var vby:Byte? = null,
                                    var vc:Char? = null,var vs :Short? = null,var vi:Int? = null,var vl:Long? = null,var vf:Float? = null,var vd:Double? = null,
                                    var vst:String? = null,var vchar :CharSequence? = null,var vpar: Parcelable? = null,
                                    var vpars:Array<Parcelable>? = null,var type:Int? = null,var next : ParceMapModel? = null) : Parcelable

object UParce{

    fun put(parceMapModel: ParceMapModel,key: String,value: Any?):ParceMapModel {
        parceMapModel.key = key
        var p = ParceMapModel()
        when (value) {
            is Boolean -> {
                parceMapModel.apply {
                    vb = value
                    type = BTYPE
                    next = p
                }
            }
            is Byte -> {
                parceMapModel.apply {
                    vby = value
                    type = BYTYPE
                    next = p
                }
            }
            is Char -> {
                parceMapModel.apply {
                    vc = value
                    type = CTYPE
                    next = p
                }
            }
            is Short -> {
                parceMapModel.apply {
                    vs = value
                    type = STYPE
                    next = p
                }
            }
            is Int ->  {
                parceMapModel.apply {
                    vi = value
                    type = ITYPE
                    next = p
                }
            }
            is Long ->  {
                parceMapModel.apply {
                    vl = value
                    type = LTYPE
                    next = p
                }
            }
            is Float -> {
                parceMapModel.apply {
                    vf = value
                    type = FTYPE
                    next = p
                }
            }
            is Double -> {
                parceMapModel.apply {
                    vd = value
                    type = DTYPE
                    next = p
                }
            }
            is String -> {
                parceMapModel.apply {
                    vst = value
                    type = STTYPE
                    next = p
                }
            }
            is CharSequence -> {
                parceMapModel.apply {
                    vchar = value
                    type = CHARTYPE
                    next = p
                }
            }
            else -> {
                parceMapModel.apply {
                    vchar = null
                    type = NULLTYPE
                    next = p
                }
            }
        }
        return p
    }

    fun <T : Parcelable> put(parceMapModel: ParceMapModel,key: String,value : T?) :ParceMapModel {
        var p = ParceMapModel()
        parceMapModel.key = key
        parceMapModel.apply {
            vpar = value
            type = PARTYPE
            next = p
        }
        return p
    }

    fun <T : Parcelable> put(parceMapModel: ParceMapModel,key: String, value: Array<T>?):ParceMapModel{
        var p = ParceMapModel()
        parceMapModel.key = key
        parceMapModel.apply {
            vpars = value as Array<Parcelable>?
            type = PARSTYPE
            next = p
        }
        return p
    }

    fun intentData(intent: Intent, node: ParceMapModel) {
        when (node.type) {
            BTYPE ->{
                intent.putExtra(node.key,node.vb)
            }
            BYTYPE ->{
                intent.putExtra(node.key,node.vby)

            }
            CTYPE ->{
                intent.putExtra(node.key,node.vc)

            }
            STYPE ->{
                intent.putExtra(node.key,node.vs)

            }
            ITYPE ->{
                intent.putExtra(node.key,node.vi)

            }
            LTYPE ->{
                intent.putExtra(node.key,node.vl)

            }
            FTYPE ->{
                intent.putExtra(node.key,node.vf)

            }
            DTYPE ->{
                intent.putExtra(node.key,node.vd)

            }
            STTYPE ->{
                intent.putExtra(node.key,node.vst)

            }
            CHARTYPE  ->{
                intent.putExtra(node.key,node.vchar)

            }
            PARTYPE  ->{
                intent.putExtra(node.key,node.vpar)

            }
            PARSTYPE  ->{
                intent.putExtra(node.key,node.vpars)

            }
            NULLTYPE  ->{
//                intent.putExtra(node.key,null)

            }

        }
    }
}