import { createModel, Reducer } from '@rematch/core'
import { RootModel } from './index'
import { capitalize } from 'helpers/fn'

const modalSet = ['createRoom', 'userInfo', 'profile', 'createReservation', 'updateReservation', 'password', 'updateRoom'] as const

type ModalState = {
  [K in typeof modalSet[number] as `${K}ModalVisible`]: boolean;
}

const state: ModalState = modalSet.reduce(
  (o: any, k) => ((o[`${k}ModalVisible`] = false), o),
  {}
)

type OpenModalActions = {
  [K in typeof modalSet[number] as `open${Capitalize<
  string & K
  >}Modal`]: Reducer<ModalState>;
}

type CloseModalActions = {
  [K in typeof modalSet[number] as `close${Capitalize<
  string & K
  >}Modal`]: Reducer<ModalState>;
}

type ModalActions = OpenModalActions & CloseModalActions

const reducers: ModalActions = modalSet.reduce((o: any, k) => {
  const action = `${capitalize(k)}Modal`
  const key = `${k}ModalVisible`
  o[`open${action}`] = (s: ModalState) => ({ ...s, [key]: true })
  o[`close${action}`] = (s: ModalState) => ({ ...s, [key]: false })
  return o
}, {})

const ModalModel = createModel<RootModel>()({
  state,
  reducers
})

export default ModalModel
