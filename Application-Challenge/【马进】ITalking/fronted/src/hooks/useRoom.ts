import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import { useHistory } from 'react-router-dom'
import { GlobalMessageType, RoomMessageType } from 'types/message'
import { message, Modal } from 'antd'
import { UserStatus } from 'models/user'
import RoomApi from 'services/room'
import RouteMap from 'types/route'
import { useCallback, useEffect } from 'react'
import { copyTextToClipboard } from 'helpers/browser'
import App from 'constants/app'

type UseRoom = () => {
  consentSpeaking: (uid: string) => Promise<void>
  stopSpeaking: () => Promise<void>
  resumeSpeaking: () => Promise<void>
  requestSpeaking: () => Promise<void>
  leaveRoom: (callback?: () => void, prompt?: string) => Promise<void>
  enterRoom: (roomId: string) => Promise<void>
  inviteFriend: () => void
}

const useRoom: UseRoom = () => {
  const user = useSelector((state: RootState) => state.UserModel)
  const room = useSelector((state: RootState) => state.RoomModel)
  const global = useSelector((state: RootState) => state.GlobalModel)
  const $ = useDispatch<Dispatch>()
  const history = useHistory()

  const handlePageClose = useCallback(async () => {
    if (user.status === UserStatus.Visitor) {
      return
    }
    if (room.speakers.length === 1 && user.status === UserStatus.Speaker) {
      await RoomApi.Dissolve()
    }
    await RoomApi.Leave(room.id)
  }, [room.speakers, user.status])

  useEffect(() => {
    window.addEventListener('beforeunload', handlePageClose, false)
    return () => {
      window.removeEventListener('beforeunload', handlePageClose)
    }
  }, [handlePageClose])

  const consentSpeaking = async (uid: string) => {
    if (!room.applicants.includes(uid) || room.speakers.some(f => f.id === uid)) {
      return
    }
    await RoomApi.AddSpeaker(uid)
    await Promise.all([
      window.roomRTMApi?.sendMessage({
        type: RoomMessageType.ConsentSpeaking,
        data: uid
      }), $.RoomModel.setAsync(room.id)])
  }

  const stopSpeaking = async () => {
    try {
      $.RoomModel.stopSpeaking(user.id)
      await Promise.all([
        $.UserModel.stopSpeakingAsync(),
        window.rtcApi?.stopSpeaking(),
        window.roomRTMApi?.sendMessage({ type: RoomMessageType.StopSpeaking })
      ])
    } catch (error) {
      console.error('闭麦失败')
    }
  }

  const resumeSpeaking = async () => {
    try {
      $.RoomModel.resumeSpeaking(user.id)
      await Promise.all([
        $.UserModel.resumeSpeakingAsync(),
        window.rtcApi?.speak(),
        window.roomRTMApi?.sendMessage({ type: RoomMessageType.ResumeSpeaking })
      ])
    } catch (error) {
      console.error('开麦失败')
    }
  }

  const requestSpeaking = async () => {
    message.loading('正在申请上麦中...')
    $.RoomModel.addApplicant(user.id)
    await window.roomRTMApi?.sendMessage({ type: RoomMessageType.RequestSpeaking })
    setTimeout(() => {
      $.RoomModel.removeApplicant(user.id)
    }, App.RequestSpeakingDisplayTime)
  }

  const clearRoomState = () => {
    $.RoomModel.resetRoom()
    $.UserModel.exitRoom()
    $.GlobalModel.exitRoom()
    $.ChatModel.clear()
  }
  const enterRoom = async (roomId: string) => {
    if (user.status !== UserStatus.Visitor && room.id !== roomId) {
      await leaveRoom(() => {
        history.push('/room/' + roomId)
      })
      return
    }
    history.push('/room/' + roomId)
  }
  const leaveRoom = async (callback = () => {
  }, prompt = '您离开后房间将会解散，确认要离开吗？') => {
    if (!global.rtcJoined || !global.roomRTMJoined) {
      return
    }
    if (room.speakers.length === 1 && user.status === UserStatus.Speaker) {
      Modal.confirm({
        title: '离开房间',
        content: prompt,
        className: 'custom-modal mini',
        okType: 'danger',
        okText: '确认',
        cancelText: '取消',
        okButtonProps: { shape: 'round' },
        cancelButtonProps: { shape: 'round' },
        centered: true,
        closable: true,
        async onOk () {
          clearRoomState()
          await Promise.all([
            RoomApi.Dissolve(),
            window.roomRTMApi?.sendMessage({
              type: RoomMessageType.DissolveRoom
            }),
            window.globalRTMApi?.sendMessage({
              type: GlobalMessageType.RefreshFeed
            })
          ])
          history.push(RouteMap.Feed)
          await Promise.all([
            window.rtcApi?.leaveRoom(),
            window.roomRTMApi?.leaveRoom(),
            $.FeedModel.setAsync()
          ])
          callback()
        }
      })
      return
    }
    clearRoomState()
    history.push(RouteMap.Feed)
    await Promise.all([
      RoomApi.Leave(room.id),
      window.rtcApi?.leaveRoom(),
      window.roomRTMApi?.leaveRoom()
    ])
    await $.FeedModel.setAsync()
    callback()
  }
  const inviteFriend = () => {
    copyTextToClipboard(`点击链接，快来 ITalking 和我一起交流吧：${location.href}`)
    message.success('邀请链接已复制到剪切板，快发送给你的朋友吧~')
  }

  return {
    stopSpeaking,
    consentSpeaking,
    resumeSpeaking,
    requestSpeaking,
    leaveRoom,
    enterRoom,
    inviteFriend
  }
}

export default useRoom
