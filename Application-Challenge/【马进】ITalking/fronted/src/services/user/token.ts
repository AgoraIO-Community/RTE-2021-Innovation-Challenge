import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

export interface GetTokenResponse extends HTTPResponse {
  data: string
}

type GetGlobalToken = () => Promise<AxiosResponse<GetTokenResponse>>

const getGlobalToken: GetGlobalToken = async () => {
  return await request.get<GetTokenResponse>(
    Api.User.Token
  )
}

export default getGlobalToken
