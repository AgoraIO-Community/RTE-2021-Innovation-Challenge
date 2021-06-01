import { createModel } from '@rematch/core'
import { RootModel } from './index'
import RoomApi from 'services/room'
import { isFailedResponse } from 'helpers/http'
import { User } from 'models/user'
import UserApi from 'services/user'
import { filterUser } from 'helpers/filter'

interface GlobalState {
  rtcToken: string
  rtmToken: string
  roomRTMJoined: boolean
  globalRTMJoined: boolean
  rtcJoined: boolean
  viewedUser?: User
  viewFollowing: boolean
}

const state: GlobalState = {
  rtmToken: '',
  rtcToken: '',
  roomRTMJoined: false,
  globalRTMJoined: false,
  rtcJoined: false,
  viewFollowing: false
}

const GlobalModel = createModel<RootModel>()({
  state,
  reducers: {
    setRTCToken (state, payload: string) {
      state.rtcToken = payload
      return state
    },
    setRTCJoinStatus (state, payload: boolean) {
      state.rtcJoined = payload
      return state
    },
    setRoomRTMJoinStatus (state, payload: boolean) {
      state.roomRTMJoined = payload
      return state
    },
    setGlobalRTMJoinStatus (state, payload: boolean) {
      state.globalRTMJoined = payload
      return state
    },
    exitRoom (state) {
      state.rtcToken = ''
      state.rtcJoined = false
      state.roomRTMJoined = false
      return state
    },
    setRTMToken (state, payload: string) {
      state.rtmToken = payload
      return state
    },
    setViewedUser (state, viewedUser: User) {
      state.viewedUser = filterUser(viewedUser)
      return state
    },
    setViewFollowing (state, viewFollowing: boolean) {
      state.viewFollowing = viewFollowing
      return state
    }
  },
  effects: dispatch => ({
    async setRTCTokenAsync (channelId: string) {
      const response = await RoomApi.GetRTCToken(channelId)
      if (isFailedResponse(response)) {
        return ''
      }
      dispatch.GlobalModel.setRTCToken(response.data.data)
      return response.data.data
    },
    async setRTMTokenAsync () {
      const response = await UserApi.GetToken()
      if (isFailedResponse(response)) {
        return ''
      }
      dispatch.GlobalModel.setRTMToken(response.data.data)
      return response.data.data
    },
    async setViewedUserAsync (uid: string) {
      const response = await UserApi.GetInfo(uid)
      if (isFailedResponse(response)) {
        return false
      }
      dispatch.GlobalModel.setViewedUser(response.data.data)
      return true
    }
  })
})

export default GlobalModel
