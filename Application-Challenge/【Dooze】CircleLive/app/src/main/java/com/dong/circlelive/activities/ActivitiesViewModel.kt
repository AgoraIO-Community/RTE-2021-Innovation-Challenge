package com.dong.circlelive.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.launch
import androidx.lifecycle.viewModelScope
import com.dong.circlelive.db.UserDatabase
import com.dong.circlelive.emClient
import com.dong.circlelive.toast
import com.hyphenate.chat.EMMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Create by dooze on 2021/5/25  10:27 上午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class ActivitiesViewModel : ViewModel() {

    fun makeActivityAction(activity: Activity) {
        activity.status = Activity.Status.DONE.value
        launch {
            kotlin.runCatching {
                when (activity.type) {
                    Activity.Type.INVITATION.value -> {
                        val emUsername = activity.fromEmUsername ?: return@launch
                        emClient.contactManager().acceptInvitation(emUsername)
                        withContext(Dispatchers.IO) {
                            UserDatabase.db.activityDao().update(activity)
                            emClient.chatManager().sendMessage(EMMessage.createTxtSendMessage("我们成为了朋友啦", emUsername))
                        }
                    }
                }
            }.exceptionOrNull()?.toast()
        }
    }

    fun deleteActivity(activity: Activity) {
        launch(Dispatchers.IO) {
            UserDatabase.db.activityDao().deleteActivity(activity)
        }
    }

    fun clearAll() {
        launch(Dispatchers.IO) {
            UserDatabase.db.activityDao().deleteAll()
        }
    }

    val activities = UserDatabase.db.activityDao().queryAll()

}