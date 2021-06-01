import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

type SignIn = (
  name: string, password: string
) => Promise<AxiosResponse<HTTPResponse>>

const signIn: SignIn = async (name, password) => {
  return await request.post<HTTPResponse>(
    Api.Entry.SignIn,
    { name, password }
  )
}

export default signIn
