import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

type LeaveRoom = (id: string) => Promise<AxiosResponse<HTTPResponse>>

const leaveRoom: LeaveRoom = async id => {
  return await request.delete<HTTPResponse>(
    Api.Room.Leave + '/' + id
  )
}

export default leaveRoom
