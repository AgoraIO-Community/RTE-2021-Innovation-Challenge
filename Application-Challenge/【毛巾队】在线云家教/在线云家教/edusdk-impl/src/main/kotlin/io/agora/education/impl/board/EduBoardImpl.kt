package io.agora.education.impl.board

import io.agora.Constants
import io.agora.base.callback.ThrowableCallback
import io.agora.base.network.RetrofitManager
import io.agora.education.api.BuildConfig.API_BASE_URL
import io.agora.education.api.EduCallback
import io.agora.education.api.board.EduBoard
import io.agora.education.api.board.data.EduBoardInfo
import io.agora.education.api.user.data.EduUserInfo
import io.agora.education.impl.ResponseBody
import io.agora.education.impl.board.data.request.BoardRoomStateReq
import io.agora.education.impl.board.data.request.BoardUserStateReq
import io.agora.education.impl.board.data.response.BoardRoomRes
import io.agora.education.impl.board.network.BoardService

class EduBoardImpl : EduBoard() {
    override fun followMode(enable: Boolean, callback: EduCallback<Unit>) {
        RetrofitManager.instance().getService(API_BASE_URL, BoardService::class.java)
                .updateBoardRoomState("", Constants.APPID, "", BoardRoomStateReq(if (enable) 1 else 0))
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<Nothing>> {
                    override fun onSuccess(res: ResponseBody<Nothing>?) {

                    }

                    override fun onFailure(throwable: Throwable?) {

                    }
                }))
    }

    override fun grantPermission(user: EduUserInfo, callback: EduCallback<Unit>) {
        RetrofitManager.instance().getService(API_BASE_URL, BoardService::class.java)
                .updateBoardUserState("", Constants.APPID, "", user.userUuid, BoardUserStateReq(1))
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<Nothing>> {
                    override fun onSuccess(res: ResponseBody<Nothing>?) {

                    }

                    override fun onFailure(throwable: Throwable?) {

                    }
                }))
    }

    override fun revokePermission(user: EduUserInfo, callback: EduCallback<Unit>) {
        RetrofitManager.instance().getService(API_BASE_URL, BoardService::class.java)
                .updateBoardUserState("", Constants.APPID, "", user.userUuid, BoardUserStateReq(0))
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<Nothing>> {
                    override fun onSuccess(res: ResponseBody<Nothing>?) {

                    }

                    override fun onFailure(throwable: Throwable?) {

                    }
                }))
    }

    override fun getBoardInfo(callback: EduCallback<EduBoardInfo>) {
        RetrofitManager.instance().getService(API_BASE_URL, BoardService::class.java)
                .getBoardRoom("", Constants.APPID, "")
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<BoardRoomRes>> {
                    override fun onSuccess(res: ResponseBody<BoardRoomRes>?) {

                    }

                    override fun onFailure(throwable: Throwable?) {

                    }
                }))
    }
}
