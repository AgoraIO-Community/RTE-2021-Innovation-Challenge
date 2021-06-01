import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

type Speak = () => Promise<AxiosResponse<HTTPResponse>>

const speak: Speak = async () => {
  return await request.post<HTTPResponse>(
    Api.User.Speak
  )
}

export default speak
