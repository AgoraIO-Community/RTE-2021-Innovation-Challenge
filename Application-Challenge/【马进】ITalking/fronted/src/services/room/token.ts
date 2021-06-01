import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

export interface GetTokenResponse extends HTTPResponse {
  data: string
}

type GetRomeToken = (id: string) => Promise<AxiosResponse<GetTokenResponse>>

const getRoomToken: GetRomeToken = async id => {
  return await request.get<GetTokenResponse>(
    Api.Room.Token + '/' + id
  )
}

export default getRoomToken
