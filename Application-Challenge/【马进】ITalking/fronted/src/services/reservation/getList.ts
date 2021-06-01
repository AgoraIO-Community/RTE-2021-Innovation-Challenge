import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'
import { ReservationType } from 'models/reservation'

export interface ReservationListResponse extends HTTPResponse {
  data: ReservationType[]
}

type GetReservationList = () => Promise<AxiosResponse<ReservationListResponse>>

const getReservationList: GetReservationList = async () => {
  return await request.get<ReservationListResponse>(
    Api.Reservation.List
  )
}

export default getReservationList
