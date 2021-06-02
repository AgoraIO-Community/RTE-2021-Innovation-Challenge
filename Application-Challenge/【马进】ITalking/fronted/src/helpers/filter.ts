import { Room } from 'models/room'
import { DefaultUser, User } from 'models/user'
import { Notification } from 'models/notification'
import { ReservationType } from 'models/reservation'
import moment from 'moment'
import 'moment/locale/zh-cn'
moment.locale('zh_CN')

export const filterRoomList = (roomList: Room[]) => {
  return roomList
    .map(filterRoom)
    .filter(room => (room.speakers.length + room.spectators.length) > 0)
}

type FilterRoom = (room: Room) => Room
export const filterRoom: FilterRoom = (room: Room) => {
  return ({
    ...room,
    creator: room.edges?.creator,
    spectators: room.edges?.spectators ?? [],
    speakers: room.edges?.speakers ?? []
  })
}

type FilterUser = (user: User) => User
export const filterUser: FilterUser = user => {
  user.followings = user.edges?.followings ?? []
  user.followers = user.edges?.followers ?? []
  user.unread = !!user.unread
  user.speaking = !!user.speaking
  return user
}

type FilterNotification = (notification: Notification) => Notification
export const filterNotification: FilterNotification = notification => {
  notification.sender = notification.edges?.sender ?? DefaultUser
  return notification
}

type FilterReservation = (reservation: ReservationType) => ReservationType
export const filterReservation: FilterReservation = reservation => {
  reservation.timeString = moment(reservation.time * 1000).calendar()
  reservation.creator = reservation.edges?.creator ?? DefaultUser
  return reservation
}
