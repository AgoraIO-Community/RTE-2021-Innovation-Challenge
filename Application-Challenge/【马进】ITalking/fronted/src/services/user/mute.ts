import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

type Mute = () => Promise<AxiosResponse<HTTPResponse>>

const mute: Mute = async () => {
  return await request.post<HTTPResponse>(
    Api.User.Mute
  )
}

export default mute
