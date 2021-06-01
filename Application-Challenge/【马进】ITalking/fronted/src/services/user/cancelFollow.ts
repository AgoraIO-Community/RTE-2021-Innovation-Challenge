import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

type CancelFollow = (id: string) => Promise<AxiosResponse<HTTPResponse>>

const cancelFollow: CancelFollow = async id => {
  return await request.delete<HTTPResponse>(
    (Api.User.Follow + '/' + id)
  )
}

export default cancelFollow
