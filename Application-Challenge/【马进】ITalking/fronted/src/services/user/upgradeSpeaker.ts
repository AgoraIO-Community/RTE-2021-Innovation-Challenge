import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

type UpgradeSpeaker = (roomId: string) => Promise<AxiosResponse<HTTPResponse>>

const upgradeSpeaker: UpgradeSpeaker = async roomId => {
  return await request.post<HTTPResponse>(
    Api.User.Upgrade + '/' + roomId
  )
}

export default upgradeSpeaker
