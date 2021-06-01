import { Models } from '@rematch/core'
import GlobalModel from 'models/global'
import ChatModel from 'models/chat'
import ModalModel from 'models/modal'
import RoomModel from 'models/room'
import UserModel from 'models/user'
import FeedModel from 'models/feed'
import ReservationModel from 'models/reservation'
import NotificationModel from './notification'

export interface RootModel extends Models<RootModel> {
  ChatModel: typeof ChatModel
  GlobalModel: typeof GlobalModel
  ModalModel: typeof ModalModel
  RoomModel: typeof RoomModel
  UserModel: typeof UserModel
  FeedModel: typeof FeedModel
  ReservationModel: typeof ReservationModel
  NotificationModel: typeof NotificationModel
}

export const models: RootModel = { GlobalModel, ChatModel, ModalModel, RoomModel, UserModel, FeedModel, ReservationModel, NotificationModel }
