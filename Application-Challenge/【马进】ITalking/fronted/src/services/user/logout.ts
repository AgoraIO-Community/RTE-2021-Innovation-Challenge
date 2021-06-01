import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'
type UserLogout = () => Promise<AxiosResponse<HTTPResponse>>

const logout: UserLogout = async () => {
  return await request.delete<HTTPResponse>(
    Api.User.Logout
  )
}

export default logout
