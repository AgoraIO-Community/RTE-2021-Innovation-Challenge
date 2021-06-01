import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

type UpdatePassword = (password: string) => Promise<AxiosResponse<HTTPResponse>>

const updatePassword: UpdatePassword = async password => {
  return await request.post<HTTPResponse>(
    Api.User.Password,
    { password }
  )
}

export default updatePassword
