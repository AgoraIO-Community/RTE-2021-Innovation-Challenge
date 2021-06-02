import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

type DissolveRoom = () => Promise<AxiosResponse<HTTPResponse>>

const dissolveRoom: DissolveRoom = async () => {
  return await request.delete<HTTPResponse>(
    Api.Room.Dissolve
  )
}

export default dissolveRoom
