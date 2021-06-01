import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'
import { User } from 'models/user'

export interface OwnInfoResponse extends HTTPResponse {
  data: User
}

type GetOwnInfo = () => Promise<AxiosResponse<OwnInfoResponse>>

const getOwnInfo: GetOwnInfo = async () => {
  return await request.get<OwnInfoResponse>(
    Api.User.OwnInfo
  )
}

export default getOwnInfo
