import { useEffect, useState } from 'react'
import AgoraRTC, {
  ConnectionState,
  IAgoraRTCRemoteUser,
  ILocalAudioTrack,
  IMicrophoneAudioTrack,
  MicrophoneAudioTrackInitConfig
} from 'agora-rtc-sdk-ng'
import App from 'constants/app'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import { message } from 'antd'
import playSound, { SoundEffect } from 'helpers/sound'
import { UserStatus } from 'models/user'

export type JoinRoomEvent = () => Promise<void>
type JoinRoom = (roomId: string, token: string) => Promise<void>
type LeaveRoom = () => Promise<void>
type Speak = () => Promise<void>
type StopSpeaking = () => Promise<void>
type Mute = () => void
type Play = () => void

export interface RTCApi {
  joinRoom: JoinRoom
  leaveRoom: LeaveRoom
  speak: Speak
  stopSpeaking: Speak
  mute: Mute
  play: Play
}

const useRTC: () => RTCApi = () => {
  const [localAudioTrack, setLocalAudioTrack] = useState<ILocalAudioTrack>()
  const [memberList, setMemberList] = useState<IAgoraRTCRemoteUser[]>([])
  const room = useSelector((state: RootState) => state.RoomModel)
  const user = useSelector((state: RootState) => state.UserModel)
  const global = useSelector((state: RootState) => state.GlobalModel)
  const $ = useDispatch<Dispatch>()

  useEffect(() => {
    if (App.IsProduction) {
      AgoraRTC.setLogLevel(4)
      AgoraRTC.disableLogUpload()
    }
    const supportAgora = AgoraRTC.checkSystemRequirements()
    if (!supportAgora) {
      message.warn('您的浏览器版本过低，暂不支持语音交流功能')
      return
    }
    window.rtcClient = AgoraRTC.createClient({ mode: 'rtc', codec: 'vp8' })
  }, [])

  useEffect(() => {
    if (!room.id || !user.id || global.rtcJoined) {
      return
    }
    (async () => {
      try {
        $.GlobalModel.setRTCJoinStatus(true)
        let rtcToken = global.rtcToken
        if (!rtcToken) {
          rtcToken = await $.GlobalModel.setRTCTokenAsync(room.id)
        }
        await joinRoom(room.id, rtcToken)
      } catch (error) {
        console.error(error)
        message.warn('加入房间失败，请稍后再试试吧')
      }
    })()
  }, [room.id, user.id])

  useEffect(() => {
    if (!memberList.length || room.volume === 100) {
      return
    }
    memberList.forEach(member => member.audioTrack?.setVolume(room.volume))
  }, [room.volume])

  useEffect(() => {
    if (!window.rtcClient || !user.id || !room.id) return
    setMemberList(window.rtcClient.remoteUsers)

    const handleUserPublished = async (user: IAgoraRTCRemoteUser, mediaType: 'audio' | 'video') => {
      await window.rtcClient.subscribe(user, mediaType)
      room.playing && user.audioTrack?.play()
    }
    const handleUserUnpublished = async (user: IAgoraRTCRemoteUser, mediaType: 'audio' | 'video') => {
      user.audioTrack?.stop()
      await window.rtcClient.unsubscribe(user, mediaType)
    }
    const handleUserJoined = () => {
      setMemberList(() => Array.from(window.rtcClient.remoteUsers))
    }
    const handleUserLeft = () => {
      setMemberList(() => Array.from(window.rtcClient.remoteUsers))
    }
    window.rtcClient.on('user-published', handleUserPublished)
    window.rtcClient.on('user-unpublished', handleUserUnpublished)
    window.rtcClient.on('user-joined', handleUserJoined)
    window.rtcClient.on('user-left', handleUserLeft)

    const handleConnectStateChange = async (currentState: ConnectionState) => {
      if (currentState === 'CONNECTED') {
        await onJoinRoom?.()
      }
    }

    window.rtcClient.on('connection-state-change', handleConnectStateChange)

    return () => {
      if (!window.rtcClient) return
      window.rtcClient.off('user-published', handleUserPublished)
      window.rtcClient.off('user-unpublished', handleUserUnpublished)
      window.rtcClient.off('user-joined', handleUserJoined)
      window.rtcClient.off('user-left', handleUserLeft)
      window.rtcClient.off('connection-state-change', handleConnectStateChange)
    }
  }, [window.rtcClient, user, room])

  const onJoinRoom: JoinRoomEvent = async () => {
    if (user.status === UserStatus.Speaker && user.speaking) {
      try {
        await speak()
      } catch (error) {
        console.error(error)
        message.warn('上麦失败，请稍后再试试吧')
      }
    }
  }

  const createLocalTracks = async (audioConfig?: MicrophoneAudioTrackInitConfig): Promise<IMicrophoneAudioTrack> => {
    const microphoneTrack = await AgoraRTC.createMicrophoneAudioTrack(audioConfig)
    setLocalAudioTrack(microphoneTrack)
    return microphoneTrack
  }

  const joinRoom: JoinRoom = async (roomId, token) => {
    if (!window.rtcClient) return
    await window.rtcClient.join(App.AgoraAppId, roomId, token)
  }

  const speak: Speak = async () => {
    if (!window.rtcClient) return
    const microphoneTrack = await createLocalTracks()
    await window.rtcClient.publish([microphoneTrack])
    playSound(SoundEffect.Unmute)
  }

  const stopSpeaking: StopSpeaking = async () => {
    if (!window.rtcClient) return
    await window.rtcClient.unpublish([localAudioTrack!])
    playSound(SoundEffect.Mute)
  }

  const mute: Mute = () => {
    memberList.forEach(member => member.audioTrack?.stop())
    playSound(SoundEffect.Deafen)
  }

  const play: Play = () => {
    memberList.forEach(member => member.audioTrack?.play())
    playSound(SoundEffect.Undeafen)
  }

  const leaveRoom: LeaveRoom = async () => {
    if (localAudioTrack) {
      localAudioTrack.stop()
      localAudioTrack.close()
    }
    setMemberList([])
    await window.rtcClient?.leave()
  }

  return {
    leaveRoom,
    joinRoom,
    speak,
    stopSpeaking,
    mute,
    play
  }
}

export default useRTC
