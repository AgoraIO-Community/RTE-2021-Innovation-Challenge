import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'
import { RoomType } from 'models/room'

export interface CreateRoomResponse extends HTTPResponse {
  data: string
}

type CreateRoom = (data: {name: string, description?: string, announcement?: string, type: RoomType }) => Promise<AxiosResponse<CreateRoomResponse>>

const createRoom: CreateRoom = async data => {
  return await request.put<CreateRoomResponse>(
    Api.Room.Create,
    data
  )
}

export default createRoom
