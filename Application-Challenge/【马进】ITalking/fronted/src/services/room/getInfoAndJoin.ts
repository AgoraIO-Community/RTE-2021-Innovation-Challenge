import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'
import { Room } from 'models/room'

export interface RoomInfoResponse extends HTTPResponse {
  data: Room
}

type GetRoomInfoAndJoin = (id: string) => Promise<AxiosResponse<RoomInfoResponse>>

const getRoomInfoAndJoin: GetRoomInfoAndJoin = async id => {
  return await request.put<RoomInfoResponse>(
    Api.Room.Info + '/' + id
  )
}

export default getRoomInfoAndJoin
