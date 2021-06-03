package com.game.tingshuo.pop

import android.content.Context
import com.lxj.xpopup.impl.PartShadowPopupView
import com.game.tingshuo.R

/**
 * Description:  - 自定义局部阴影弹窗
 *
 * 调用方式:
    XPopup.Builder(mActivity)
        .atView(toolbar)
        .setPopupCallback(object : SimpleCallback() {
            override fun onShow(popupView: BasePopupView) {

            }

            override fun onDismiss(popupView: BasePopupView) {
            }
    }).asCustom(FileSortPopupView(mActivity!!)).show()

 */
class MyPartShadowPopup(context: Context) : PartShadowPopupView(context) {

    override fun getImplLayoutId(): Int {
        return R.layout.pop_my_partshdow_demo
    }

    override fun onCreate() {
        super.onCreate()


    }

}