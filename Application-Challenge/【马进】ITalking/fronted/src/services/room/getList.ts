import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'
import { Room } from 'models/room'

export interface RoomListResponse extends HTTPResponse {
  data: Room[]
}

type GetRoomList = () => Promise<AxiosResponse<RoomListResponse>>

const getRoomList: GetRoomList = async () => {
  return await request.get<RoomListResponse>(
    Api.Room.List
  )
}

export default getRoomList
