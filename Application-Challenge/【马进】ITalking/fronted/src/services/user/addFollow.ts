import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

type AddFollow = (id: string) => Promise<AxiosResponse<HTTPResponse>>

const addFollow: AddFollow = async id => {
  return await request.post<HTTPResponse>(
    (Api.User.Follow + '/' + id)
  )
}

export default addFollow
