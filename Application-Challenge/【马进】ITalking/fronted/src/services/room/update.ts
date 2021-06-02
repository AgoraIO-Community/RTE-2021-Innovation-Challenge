import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'
import { RoomType } from 'models/room'

export interface CreateRoomResponse extends HTTPResponse {
  data: string
}

type UpdateRoom = (data: { name: string, description?: string, announcement?: string, type: RoomType }) => Promise<AxiosResponse<CreateRoomResponse>>

const updateRoom: UpdateRoom = async data => {
  return await request.post<CreateRoomResponse>(
    Api.Room.Info,
    data
  )
}

export default updateRoom
