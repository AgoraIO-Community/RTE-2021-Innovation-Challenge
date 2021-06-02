import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

type DeleteReservation = (id: string) => Promise<AxiosResponse<HTTPResponse>>

const deleteReservation: DeleteReservation = async id => {
  return await request.delete<HTTPResponse>(
    Api.Reservation.List + '/' + id
  )
}

export default deleteReservation
