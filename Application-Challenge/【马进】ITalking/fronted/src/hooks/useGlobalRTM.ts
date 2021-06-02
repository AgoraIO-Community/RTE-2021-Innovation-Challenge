import AgoraRTM, { RtmChannel, RtmMessage } from 'agora-rtm-sdk'
import { Dispatch, RootState } from 'store'
import Message, { GlobalMessageType } from 'types/message'
import { useDispatch, useSelector } from 'react-redux'
import { useEffect, useState } from 'react'
import App from 'constants/app'
import RouteMap from 'types/route'
import { useLocation } from 'react-router-dom'

export type ChannelMessageEvent = (message: RtmMessage, memberId: string) => void

type UserLogin = () => Promise<void>
type UserLogout = () => Promise<void>
type SendMessage = (message: Message) => Promise<void>

export interface GlobalRTMApi {
  login: UserLogin
  logout: UserLogout
  sendMessage: SendMessage
}

const useGlobalRTM: () => GlobalRTMApi = () => {
  const [globalChannel, setGlobalChannel] = useState<RtmChannel | undefined>()
  const user = useSelector((state: RootState) => state.UserModel)
  const global = useSelector((state: RootState) => state.GlobalModel)
  const feed = useSelector((state: RootState) => state.FeedModel)
  const $ = useDispatch<Dispatch>()
  const location = useLocation()

  useEffect(() => {
    const config = App.IsProduction
      ? {
          enableLogUpload: false,
          logFilter: AgoraRTM.LOG_FILTER_OFF
        }
      : {}
    window.rtmClient = AgoraRTM.createInstance(App.AgoraAppId, config)
  }, [])

  useEffect(() => {
    if (!user.id || global.globalRTMJoined) {
      return
    }
    (async () => {
      try {
        $.GlobalModel.setGlobalRTMJoinStatus(true)
        let rtmToken = global.rtmToken
        if (!rtmToken) {
          rtmToken = await $.GlobalModel.setRTMTokenAsync()
        }
        await window.rtmClient?.login({ uid: user.id, token: rtmToken })
        await login()
      } catch (error) {
        console.error(error)
      }
    })()
  }, [user.id])

  useEffect(() => {
    if (!globalChannel) {
      return
    }
    globalChannel?.on('ChannelMessage', onReceiveMessage)
    return () => {
      globalChannel.off('ChannelMessage', onReceiveMessage)
    }
  }, [globalChannel, user, global, feed])

  const onReceiveMessage: ChannelMessageEvent = async (messageData, memberId) => {
    const _message: Message = JSON.parse((messageData as any)?.text)
    console.log({
      message: _message,
      memberId
    })
    const { type } = _message
    if (type === GlobalMessageType.RefreshFeed && location.pathname === RouteMap.Feed) {
      setTimeout(async () => {
        await $.FeedModel.setAsync()
      }, 1000)
    }
  }

  const login: UserLogin = async () => {
    const channel = window.rtmClient?.createChannel(App.GlobalChannelName)
    await channel?.join()
    setGlobalChannel(channel)
  }

  const logout: UserLogout = async () => {
    if (!globalChannel) {
      return
    }

    await globalChannel.leave()
    setGlobalChannel(undefined)
  }

  const sendMessage: SendMessage = async (message) => {
    if (!globalChannel) {
      return
    }
    await globalChannel.sendMessage({ text: JSON.stringify(message) })
  }

  return {
    login,
    logout,
    sendMessage
  }
}

export default useGlobalRTM
