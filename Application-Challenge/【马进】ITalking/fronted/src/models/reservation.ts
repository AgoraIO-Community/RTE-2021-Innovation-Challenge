import { createModel } from '@rematch/core'
import { RootModel } from 'models/index'
import { isFailedResponse } from 'helpers/http'
import { message } from 'antd'
import ReservationApi from 'services/reservation'
import { User } from './user'
import { filterReservation } from 'helpers/filter'

export interface ReservationType {
  name: string
  id?: string
  description?: string
  time: number
  timeString: string
  edges?: {
    creator?: User
  }
  creator: User
}

interface ReservationState {
  list: ReservationType[]
  loading: boolean
  updatedReservation?: ReservationType
}

const state: ReservationState = {
  list: [],
  loading: true
}

const ReservationModel = createModel<RootModel>()({
  state,
  reducers: {
    setLoading (state, loading: boolean) {
      state.loading = loading
      return state
    },
    set (state, payload: ReservationType[]) {
      state.list = payload.map(filterReservation)
      state.loading = false
      return state
    },
    updateReservation (state, reservation: ReservationType) {
      state.updatedReservation = reservation
      return state
    }
  },
  effects: dispatch => ({
    async setAsync () {
      dispatch.ReservationModel.setLoading(true)
      const response = await ReservationApi.GetList()
      if (isFailedResponse(response)) {
        message.warn('获取预订房间信息失败')
        dispatch.ReservationModel.set([])
        return
      }
      dispatch.ReservationModel.set(response.data.data)
    },
    async createAsync (payload: Partial<ReservationType>) {
      dispatch.ReservationModel.setLoading(true)
      const response = await ReservationApi.Create(payload)
      dispatch.ReservationModel.setLoading(false)
      if (isFailedResponse(response)) {
        message.warn('预订房间失败')
        return false
      }
      await dispatch.ReservationModel.setAsync()
      return true
    },
    async updateAsync (payload: Partial<ReservationType>) {
      dispatch.ReservationModel.setLoading(true)
      const response = await ReservationApi.Update(payload)
      dispatch.ReservationModel.setLoading(false)
      if (isFailedResponse(response)) {
        message.warn('更新房间信息失败')
        return false
      }
      await dispatch.ReservationModel.setAsync()
      return true
    },
    async deleteAsync (id: string) {
      dispatch.ReservationModel.setLoading(true)
      const response = await ReservationApi.Delete(id)
      dispatch.ReservationModel.setLoading(false)
      if (isFailedResponse(response)) {
        message.warn('删除预订房间失败')
        return false
      }
      await dispatch.ReservationModel.setAsync()
      return true
    }
  })
})

export default ReservationModel
