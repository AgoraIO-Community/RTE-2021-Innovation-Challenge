import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'
import { ReservationType } from 'models/reservation'

type CreateReservation = (data: Partial<ReservationType>) => Promise<AxiosResponse<HTTPResponse>>

const createReservation: CreateReservation = async data => {
  return await request.put<HTTPResponse>(
    Api.Reservation.List,
    data
  )
}

export default createReservation
