import React from 'react'
import { message, Modal } from 'antd'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import UserCard from 'common/UserCard'
import FollowBtn from 'common/FollowBtn'

interface Props {
  visible: boolean
  closeModal: () => void
}

const UserInfoModal: React.FC<Props> = props => {
  const { visible, closeModal } = props
  const global = useSelector((state: RootState) => state.GlobalModel)
  const user = useSelector((state: RootState) => state.UserModel)
  const $ = useDispatch<Dispatch>()

  const toggleFollowStatus = async () => {
    if (!global.viewedUser?.id) {
      return
    }
    const toggleSuccess = await $.UserModel.toggleFollow({
      uid: global.viewedUser.id,
      isFollowing
    })
    await $.GlobalModel.setViewedUserAsync(global.viewedUser.id)
    if (!toggleSuccess) {
      message.warn('因不可抗拒因素，操作失败')
    }
  }
  const isFollowing = user.followings.some(f => f.id === global.viewedUser?.id)

  return (
    <Modal className="custom-modal user-info" visible={visible} title="用户信息" footer={null} onCancel={closeModal}
           closable centered>
      {global.viewedUser && <UserCard user={global.viewedUser}/>}
      {global.viewedUser?.id !== user.id && (
        <FollowBtn onClick={toggleFollowStatus} isFollowing={isFollowing} />
      )}
    </Modal>
  )
}

export default UserInfoModal
