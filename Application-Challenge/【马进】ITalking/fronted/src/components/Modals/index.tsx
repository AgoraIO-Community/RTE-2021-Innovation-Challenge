import React from 'react'
import CreateRoomModal from './CreateRoomModal'
import UserInfoModal from './UserInfoModal'
import ProfileModal from './ProfileModal'
import CreateReservationModal from './CreateReservationModal'
import UpdateReservationModal from './UpdateReservationModal'
import PasswordModal from './PasswordModal'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import UpdateRoomModal from 'components/Modals/UpdateRoomModal'

const Modal: React.FC = () => {
  const modal = useSelector((state: RootState) => state.ModalModel)
  const $ = useDispatch<Dispatch>()

  return (
    <>
      <CreateRoomModal visible={modal.createRoomModalVisible} closeModal={$.ModalModel.closeCreateRoomModal} />
      <UserInfoModal visible={modal.userInfoModalVisible} closeModal={$.ModalModel.closeUserInfoModal} />
      <ProfileModal visible={modal.profileModalVisible} closeModal={$.ModalModel.closeProfileModal} />
      <CreateReservationModal visible={modal.createReservationModalVisible} closeModal={$.ModalModel.closeCreateReservationModal} />
      <UpdateReservationModal visible={modal.updateReservationModalVisible} closeModal={$.ModalModel.closeUpdateReservationModal} />
      <PasswordModal visible={modal.passwordModalVisible} closeModal={$.ModalModel.closePasswordModal} />
      <UpdateRoomModal visible={modal.updateRoomModalVisible} closeModal={$.ModalModel.closeUpdateRoomModal} />
    </>
  )
}

export default Modal
