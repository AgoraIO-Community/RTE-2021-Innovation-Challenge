import { useEffect } from 'react'
import { UserStatus } from 'models/user'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'

const useUserStatus = () => {
  const user = useSelector((state: RootState) => state.UserModel)
  const room = useSelector((state: RootState) => state.RoomModel)
  const $ = useDispatch<Dispatch>()
  useEffect(() => {
    if (!user.id) {
      return
    }
    if (room.speakers.some(f => f.id === user.id)) {
      $.UserModel.setStatus(UserStatus.Speaker)
      return
    }
    if (room.applicants.includes(user.id)) {
      $.UserModel.setStatus(UserStatus.Applicant)
      return
    }
    if (room.spectators.some(f => f.id === user.id)) {
      $.UserModel.setStatus(UserStatus.Spectator)
    }
  }, [room.speakers, room.spectators, room.applicants, user.id])
}

export default useUserStatus
