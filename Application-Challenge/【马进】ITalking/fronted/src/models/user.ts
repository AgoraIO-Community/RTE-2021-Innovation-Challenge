import { createModel } from '@rematch/core'
import { RootModel } from 'models/index'
import UserApi from 'services/user'
import { isFailedResponse } from 'helpers/http'
import { filterUser } from 'helpers/filter'

export enum UserStatus {
  Visitor = 'visitor', Speaker = 'speaker', Spectator = 'spectator', Applicant = 'applicant'
}

export interface User {
  id: string
  name: string
  description: string
  unread?: boolean
  status: UserStatus
  speaking?: boolean
  edges?: {
    followers?: User[]
    followings?: User[]
  }
  followers: User[]
  followings: User[]
}

export const DefaultUser: User = {
  id: '',
  name: '',
  description: '',
  unread: false,
  status: UserStatus.Visitor,
  speaking: true,
  followers: [],
  followings: []
}

const state: User = DefaultUser

const UserModel = createModel<RootModel>()({
  state,
  reducers: {
    stopSpeaking (state) {
      state.speaking = false
      return state
    },
    resumeSpeaking (state) {
      state.speaking = true
      return state
    },
    exitRoom (state) {
      state.status = UserStatus.Visitor
      state.speaking = true
      return state
    },
    setUser (state, user: User) {
      return { ...state, ...filterUser(user) }
    },
    updateUser (state, payload: {name: string, description: string}) {
      state.name = payload.name
      state.description = payload.description
      return state
    },
    setHadReadStatus (state) {
      state.unread = false
      return state
    },
    setStatus (state, status: UserStatus) {
      state.status = status
      return state
    }
  },
  effects: dispatch => ({
    async setAsync () {
      const response = await UserApi.GetOwnInfo()
      if (isFailedResponse(response)) {
        return false
      }
      dispatch.UserModel.setUser(response.data.data)
      return true
    },
    async updateAsync (payload: {name: string, description: string}) {
      const response = await UserApi.UpdateInfo(payload)
      if (isFailedResponse(response)) {
        return false
      }
      dispatch.UserModel.updateUser(payload)
      return true
    },
    async toggleFollow (payload: {isFollowing: boolean, uid: string}) {
      const { isFollowing, uid } = payload
      if (!uid) {
        return false
      }
      if (isFollowing) {
        const response = await UserApi.CancelFollow(uid)
        if (isFailedResponse(response)) {
          return false
        }
      } else {
        const response = await UserApi.AddFollow(uid)
        if (isFailedResponse(response)) {
          return false
        }
      }
      await dispatch.UserModel.setAsync()
      return true
    },
    async stopSpeakingAsync () {
      dispatch.UserModel.stopSpeaking()
      await UserApi.Mute()
    },
    async resumeSpeakingAsync () {
      dispatch.UserModel.resumeSpeaking()
      await UserApi.Speak()
    }
  })
})

export default UserModel
