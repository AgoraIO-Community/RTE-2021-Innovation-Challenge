import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'
import { ReservationType } from 'models/reservation'

type UpdateReservation = (data: Partial<ReservationType>) => Promise<AxiosResponse<HTTPResponse>>

const updateReservation: UpdateReservation = async data => {
  return await request.post<HTTPResponse>(
    Api.Reservation.List + '/' + data.id,
    data
  )
}

export default updateReservation
