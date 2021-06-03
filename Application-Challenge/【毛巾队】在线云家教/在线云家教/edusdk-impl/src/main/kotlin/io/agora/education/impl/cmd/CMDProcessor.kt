package io.agora.education.impl.cmd

import io.agora.education.impl.util.Convert
import io.agora.education.api.room.EduRoom
import io.agora.education.api.room.data.RoomType
import io.agora.education.api.stream.data.EduStreamEvent
import io.agora.education.api.stream.data.EduStreamInfo
import io.agora.education.api.user.data.EduBaseUserInfo
import io.agora.education.api.user.data.EduUserEvent
import io.agora.education.api.user.data.EduUserInfo
import io.agora.education.impl.room.data.response.EduFromUserRes
import io.agora.education.impl.room.data.response.EduUserRes
import io.agora.education.impl.user.data.EduUserInfoImpl
import io.agora.rte.RteEngineImpl

internal open class CMDProcessor {
    companion object {
        const val TAG = "CMDProcessor"

        /**调用此函数之前须确保first和second代表的是同一个用户
         * 比较first的数据是否比second的更为接近当前时间(即找出一个最新数据)
         * @return > 0（user > old）
         *         !(> 0) user <= old*/
        internal fun compareUserInfoTime(first: EduUserInfo, second: EduUserInfo): Long {
            /**判断更新时间是否为空(为空的有可能是原始数据)*/
            if ((first as EduUserInfoImpl).updateTime == null) {
                return -1
            }
            if ((second as EduUserInfoImpl).updateTime == null) {
                return first.updateTime!!
            }
            /**最终判断出最新数据*/
            return first.updateTime!!.minus(second.updateTime!!)
        }

        /**operator有可能为空(说明用户自己就是操作者)，我们需要把当前用户设置为操作者*/
        internal fun getOperator(operator: Any?, userInfo: EduBaseUserInfo, roomType: RoomType)
                : EduBaseUserInfo {
            /**operator为空说明操作者是自己*/
            var operatorUser: EduBaseUserInfo? = null
            operator?.let {
                if (operator is EduUserRes) {
                    operatorUser = Convert.convertUserInfo(operator, roomType)
                } else if (operator is EduFromUserRes) {
                    operatorUser = Convert.convertUserInfo(operator, roomType)
                }
            }
            if (operatorUser == null) {
                operatorUser = userInfo
            }
            return operatorUser!!
        }

        /**从集合中过滤掉本地用户的信息并返回*/
        internal fun filterLocalUserInfo(userInfo: EduBaseUserInfo, userInfos: MutableList<EduUserInfo>)
                : EduUserInfo? {
            userInfos?.let {
                val iterable = userInfos.iterator()
                while (iterable.hasNext()) {
                    val it = iterable.next()
                    if (it == userInfo) {
                        iterable.remove()
                        return it
                    }
                }
            }
            return null
        }

        internal fun filterLocalUserEvent(userInfo: EduBaseUserInfo, userEvents: MutableList<EduUserEvent>)
                : EduUserEvent? {
            userEvents?.let {
                val iterable = userEvents.iterator()
                while (iterable.hasNext()) {
                    val it = iterable.next()
                    if (it.modifiedUser == userInfo) {
                        iterable.remove()
                        return it
                    }
                }
            }
            return null
        }

        /**从集合中过滤掉本地流的信息并返回*/
        internal fun filterLocalStreamInfo(userInfo: EduBaseUserInfo, streamEvents: MutableList<EduStreamEvent>)
                : EduStreamEvent? {
            val iterable = streamEvents.iterator()
            while (iterable.hasNext()) {
                val it = iterable.next()
                if (it.modifiedStream.publisher == userInfo) {
                    iterable.remove()
                    return it
                }
            }
            return null
        }
    }
}