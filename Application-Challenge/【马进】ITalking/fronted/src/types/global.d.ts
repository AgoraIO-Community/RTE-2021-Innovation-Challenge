import { IAgoraRTCClient } from 'agora-rtc-sdk-ng'
import { RtmClient } from 'agora-rtm-sdk'
import { RoomRTMApi } from 'hooks/useRoomRTM'
import { RTCApi } from 'hooks/useAgoraRTC'
import { GlobalRTMApi } from 'hooks/useGlobalRTM'

declare global {
  interface Window {
    rtcClient: IAgoraRTCClient
    rtmClient: RtmClient
    globalRTMApi?: GlobalRTMApi
    roomRTMApi?: RoomRTMApi
    rtcApi?: RTCApi
  }
}
