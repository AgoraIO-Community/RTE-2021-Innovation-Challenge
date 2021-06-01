import getReservationList from 'services/reservation/getList'
import createReservation from 'services/reservation/create'
import updateReservation from 'services/reservation/update'
import deleteReservation from 'services/reservation/delete'

const ReservationApi = {
  GetList: getReservationList,
  Create: createReservation,
  Update: updateReservation,
  Delete: deleteReservation
}

export default ReservationApi
