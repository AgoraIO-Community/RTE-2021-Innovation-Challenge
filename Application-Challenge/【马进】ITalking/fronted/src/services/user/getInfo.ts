import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'
import { User } from 'models/user'

export interface UserInfoResponse extends HTTPResponse {
  data: User
}

type GetUserInfo = (id: string) => Promise<AxiosResponse<UserInfoResponse>>

const getUserInfo: GetUserInfo = async id => {
  return await request.get<UserInfoResponse>(
    (Api.User.Info + '/' + id)
  )
}

export default getUserInfo
