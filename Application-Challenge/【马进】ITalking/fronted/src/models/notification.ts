import { createModel } from '@rematch/core'
import { RootModel } from 'models/index'
import NotificationApi from 'services/notification'
import { isFailedResponse } from 'helpers/http'
import { message } from 'antd'
import { User } from 'models/user'
import { filterNotification } from 'helpers/filter'

export enum NotificationType {
  Official = 'official', Follow = 'follow', Invite = 'invite'
}

export interface Notification {
  id: string
  type: NotificationType
  content: string
  created_at: string
  edges?: {
    sender?: User
  }
  sender: User
}

export interface NotificationState {
  list: Notification[]
  loading: boolean
  type: NotificationType
}

const state: NotificationState = {
  list: [],
  loading: true,
  type: NotificationType.Official
}

const NotificationModel = createModel<RootModel>()({
  state,
  reducers: {
    setLoading (state) {
      state.loading = true
      return state
    },
    set (state, payload: Notification[]) {
      state.list = payload
        .sort((a, b) => (new Date(b.created_at)).valueOf() - (new Date(a.created_at)).valueOf())
        .map(filterNotification)
      state.loading = false
      return state
    },
    setType (state, type: NotificationType) {
      state.type = type
      return state
    }
  },
  effects: dispatch => ({
    async setAsync () {
      dispatch.NotificationModel.setLoading()
      const response = await NotificationApi.GetInfo()
      if (isFailedResponse(response)) {
        message.warn('获取通知信息失败')
        dispatch.NotificationModel.set([])
        return
      }
      dispatch.UserModel.setHadReadStatus()
      dispatch.NotificationModel.set(response.data.data)
    }
  })
})

export default NotificationModel
