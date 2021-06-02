import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'
import { Notification } from 'models/notification'

export interface GetNotificationResponse extends HTTPResponse {
  data: Notification[]
}

type GetNotification = () => Promise<AxiosResponse<GetNotificationResponse>>

const getNotification: GetNotification = async () => {
  return await request.get<GetNotificationResponse>(
    Api.Notification.Info
  )
}

export default getNotification
