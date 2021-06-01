import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

type Ping = (
) => Promise<AxiosResponse<HTTPResponse>>

const ping: Ping = async () => {
  return await request.get<HTTPResponse>(
    Api.Entry.Ping
  )
}

export default ping
