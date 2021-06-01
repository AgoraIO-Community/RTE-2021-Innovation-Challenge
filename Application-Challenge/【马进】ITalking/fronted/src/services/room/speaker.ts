import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

type AddSpeaker = (uid: string) => Promise<AxiosResponse<HTTPResponse>>

const addSpeaker: AddSpeaker = async (uid) => {
  return await request.put<HTTPResponse>(
    Api.Room.Speaker + '/' + uid
  )
}

export default addSpeaker
