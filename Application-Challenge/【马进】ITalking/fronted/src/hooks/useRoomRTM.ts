import { RtmChannel, RtmMessage } from 'agora-rtm-sdk'
import { Dispatch, RootState } from 'store'
import Message, { RoomMessageType } from 'types/message'
import { useDispatch, useSelector } from 'react-redux'
import { useEffect, useState } from 'react'
import RoomApi from 'services/room'
import { UserStatus } from 'models/user'
import { message } from 'antd'
import { ChatState } from 'models/chat'
import RouteMap from 'types/route'
import { useHistory, useLocation } from 'react-router-dom'
import App from 'constants/app'
import UserApi from 'services/user'

export type ChannelMessageEvent = (message: RtmMessage, memberId: string) => void
export type MemberJoinedEvent = (memberId: string) => void
export type MemberLeftEvent = (memberId: string) => void

type JoinRoom = (channelName: string) => Promise<void>
type LeaveRoom = () => Promise<void>
type SendMessage = (message: Message) => Promise<void>

export interface RoomRTMApi {
  joinRoom: JoinRoom
  leaveRoom: LeaveRoom
  sendMessage: SendMessage
}

const useRoomRTM: () => RoomRTMApi = () => {
  const [channel, setChannel] = useState<RtmChannel | undefined>()
  const user = useSelector((state: RootState) => state.UserModel)
  const room = useSelector((state: RootState) => state.RoomModel)
  const global = useSelector((state: RootState) => state.GlobalModel)
  const $ = useDispatch<Dispatch>()
  const history = useHistory()
  const location = useLocation()

  useEffect(() => {
    if (!global.globalRTMJoined || global.roomRTMJoined || !room.id) {
      return
    }
    (async () => {
      try {
        $.GlobalModel.setRoomRTMJoinStatus(true)
        await joinRoom(room.id)
      } catch (error) {
        console.error(error)
        message.warn('加入房间失败，请稍后再试试吧')
      }
    })()
  }, [global.globalRTMJoined, room.id])

  useEffect(() => {
    if (!channel) {
      return
    }
    channel?.on('ChannelMessage', onReceiveMessage)
    channel?.on('MemberJoined', onMemberJoin)
    channel?.on('MemberLeft', onMemberLeave)
    return () => {
      channel.off('ChannelMessage', onReceiveMessage)
      channel.off('MemberJoined', onMemberJoin)
      channel.off('MemberLeft', onMemberLeave)
    }
  }, [channel, user, room, global])

  const dissolveRoom = async () => {
    message.info('由于最后一名主播退出了房间，房间被解散')
    $.RoomModel.resetRoom()
    $.UserModel.exitRoom()
    $.GlobalModel.exitRoom()
    $.ChatModel.clear()
    history.push(RouteMap.Feed)
    await Promise.all([
      window.rtcApi?.leaveRoom(),
      leaveRoom(),
      $.FeedModel.setAsync()
    ])
  }

  const onMemberJoin: MemberJoinedEvent = async () => {
    await $.RoomModel.setAsync(room.id)
    if (location.pathname === RouteMap.Feed) {
      await $.FeedModel.setAsync()
    }
  }

  const onMemberLeave: MemberLeftEvent = async memberId => {
    if (room.speakers.length === 1 && room.speakers[0].id === memberId) {
      await Promise.all([
        dissolveRoom(),
        RoomApi.Dissolve()]
      )
      return
    }
    $.RoomModel.removeMember(memberId)
    if (location.pathname === RouteMap.Feed) {
      await $.FeedModel.setAsync()
    }
  }

  const onReceiveMessage: ChannelMessageEvent = async (messageData, memberId) => {
    const _message: Message = JSON.parse((messageData as any)?.text)
    console.log({
      message: _message,
      memberId
    })
    const { type, data } = _message
    if (type === RoomMessageType.RequestSpeaking && !room.applicants.includes(memberId)) {
      $.RoomModel.addApplicant(memberId)
      if (user.status !== UserStatus.Speaker) {
        setTimeout(() => {
          $.RoomModel.removeApplicant(memberId)
        }, App.RequestSpeakingDisplayTime)
      }
      if (user.status === UserStatus.Speaker && !location.pathname.startsWith('/room')) {
        message.info('有听众在请求上麦，回房间看看吧~')
      }
    }

    if (type === RoomMessageType.ConsentSpeaking) {
      const uid = (data as string)
      try {
        if (uid === user.id && user.status !== UserStatus.Speaker) {
          await Promise.all([
            window.rtcApi?.speak(),
            UserApi.UpgradeSpeaker(room.id)
          ])
        }
        await $.RoomModel.setAsync(room.id)
      } catch (error) {
        console.error(error)
        message.warn('用户上麦失败')
      }
    }

    if (type === RoomMessageType.StopSpeaking) {
      $.RoomModel.stopSpeaking(memberId)
    }

    if (type === RoomMessageType.ResumeSpeaking) {
      $.RoomModel.resumeSpeaking(memberId)
    }

    if (type === RoomMessageType.Chat) {
      $.ChatModel.add(data as ChatState)
    }

    if (type === RoomMessageType.DissolveRoom) {
      await dissolveRoom()
    }

    if (type === RoomMessageType.UpdateRoom) {
      await Promise.all([$.RoomModel.setAsync(room.id), $.FeedModel.setAsync()])
    }
  }

  const joinRoom: JoinRoom = async channelName => {
    const roomChannel = window.rtmClient?.createChannel(channelName)
    await roomChannel?.join()
    setChannel(roomChannel)
    if (room.announcement) {
      $.ChatModel.add({
        id: 'room-announcement',
        name: room?.creator?.name ?? room.speakers[0].name,
        time: '房间公告',
        content: room.announcement
      })
    }
  }

  const leaveRoom: LeaveRoom = async () => {
    if (!channel) {
      return
    }

    await channel.leave()
    setChannel(undefined)
  }

  const sendMessage: SendMessage = async (message) => {
    if (!channel) {
      return
    }
    await channel.sendMessage({ text: JSON.stringify(message) })
  }

  return {
    joinRoom,
    leaveRoom,
    sendMessage
  }
}

export default useRoomRTM
