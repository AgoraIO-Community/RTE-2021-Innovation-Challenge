import { createModel } from '@rematch/core'
import { RootModel } from './index'
import { Room } from './room'
import RoomApi from 'services/room'
import { isFailedResponse } from 'helpers/http'
import { filterRoomList } from 'helpers/filter'

export enum FeedOrder {
  CreatedTimeAsc, CreatedTimeDesc, PeopleNumberAsc, PeopleNumberDesc
}

interface FeedState {
  list: Room[]
  isEmpty: boolean
  isLoading: boolean
  order: FeedOrder
}

const state: FeedState = {
  list: [],
  isLoading: true,
  isEmpty: false,
  order: FeedOrder.CreatedTimeAsc
}

const FeedModel = createModel<RootModel>()({
  state,
  reducers: {
    setList (state, payload: Room[]) {
      state.isEmpty = false
      state.list = payload
      return state
    },
    setEmpty (state) {
      state.isEmpty = true
      state.list = []
      return state
    },
    setOrder (state, order: FeedOrder) {
      state.order = order
      return state
    },
    setLoading (state, isLoading: boolean) {
      state.isLoading = isLoading
      return state
    }
  },
  effects: dispatch => ({
    async getAsync () {
      dispatch.FeedModel.setLoading(true)
      const response = await RoomApi.GetList()
      dispatch.FeedModel.setLoading(false)
      if (!isFailedResponse) {
        return []
      }
      const { data } = response.data
      if (!data?.length) {
        return []
      }
      const filtererRoomList = filterRoomList(data)
      if (!filtererRoomList?.length) {
        return []
      }
      return filtererRoomList
    },
    async setAsync () {
      const roomList = await dispatch.FeedModel.getAsync()
      if (!roomList.length) {
        dispatch.FeedModel.setEmpty()
        return false
      }
      dispatch.FeedModel.setList(roomList)
      return true
    },
    async setOrderAsync (order: FeedOrder) {
      dispatch.FeedModel.setOrder(order)
      const roomList = await dispatch.FeedModel.getAsync()
      if (!roomList.length) {
        return
      }
      if (order === FeedOrder.CreatedTimeAsc) {
        dispatch.FeedModel.setList(roomList)
      } else if (order === FeedOrder.CreatedTimeDesc) {
        dispatch.FeedModel.setList(roomList.reverse())
      } else {
        const roomListOrderByPeopleNum = roomList.sort((a, b) => {
          const numOfA = (a.speakers?.length ?? 0) + (a.spectators?.length ?? 0)
          const numOfB = (b.speakers?.length ?? 0) + (b.spectators?.length ?? 0)
          return numOfA - numOfB
        })
        if (order === FeedOrder.PeopleNumberDesc) {
          dispatch.FeedModel.setList(roomListOrderByPeopleNum)
        } else if (order === FeedOrder.PeopleNumberAsc) {
          dispatch.FeedModel.setList(roomListOrderByPeopleNum.reverse())
        }
      }
    }

  })
})

export default FeedModel
