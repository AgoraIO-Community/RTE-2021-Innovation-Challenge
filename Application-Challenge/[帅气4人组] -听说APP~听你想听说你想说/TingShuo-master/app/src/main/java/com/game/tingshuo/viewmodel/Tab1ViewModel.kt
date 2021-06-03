package com.game.tingshuo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.game.tingshuo.R

//class Tab1ViewModel: ViewModel() {//不需要context使用ViewModel即可
class Tab1ViewModel(application: Application) : AndroidViewModel(application) {
    var number = 1

    fun getData():String{
        return getApplication<Application>().resources.getString(R.string.app_name)
    }

}