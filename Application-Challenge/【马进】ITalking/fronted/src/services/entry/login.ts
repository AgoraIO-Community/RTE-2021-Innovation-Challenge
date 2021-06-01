import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

type Login = (
  name: string, password: string
) => Promise<AxiosResponse<HTTPResponse>>

const login: Login = async (name, password) => {
  return await request.post<HTTPResponse>(
    Api.Entry.Login,
    { name, password }
  )
}

export default login
