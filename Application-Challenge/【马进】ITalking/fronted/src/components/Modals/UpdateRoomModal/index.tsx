import React from 'react'
import { Form, message, Modal } from 'antd'
import NProgress from 'nprogress'
import { RoomMessageType } from 'types/message'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import { getChatId } from 'helpers/id'
import RoomNameForm from 'common/FormItem/RoomNameForm'
import RoomDescriptionForm from 'common/FormItem/RoomDescriptionForm'
import RoomAnnouncementForm from 'common/FormItem/RoomAnnouncementForm'
import RoomTypeForm from 'common/FormItem/RoomType'

interface Props {
  visible: boolean
  closeModal: () => void
}

const UpdateRoomModal: React.FC<Props> = props => {
  const { visible, closeModal } = props
  const [form] = Form.useForm()
  const room = useSelector((state: RootState) => state.RoomModel)
  const user = useSelector((state: RootState) => state.UserModel)
  const $ = useDispatch<Dispatch>()

  const resetState = () => {
    form.setFieldsValue({
      name: room.name,
      description: room.description,
      announcement: room.announcement,
      type: room.type
    })
  }
  const updateRoomInfo = async () => {
    const { name, description, announcement, type } = await form.validateFields()
    NProgress.start()
    closeModal()

    const sendNewAnnouncement = async () => {
      if (!announcement || announcement === room.announcement) {
        return
      }
      await $.ChatModel.addSync({
        id: 'room-announcement' + getChatId(),
        name: user.name,
        time: '房间公告',
        content: announcement
      })
    }

    const updateSuccess = await $.RoomModel.updateAsync({
      id: room.id,
      name,
      description,
      announcement,
      type
    })
    if (!updateSuccess) {
      message.warn('因不可抗拒因素，房间信息更新失败了')
    } else {
      message.success('房间信息已更新')
    }
    await Promise.all([
      $.FeedModel.setAsync(),
      sendNewAnnouncement(),
      window.roomRTMApi?.sendMessage({
        type: RoomMessageType.UpdateRoom
      })
    ])
    NProgress.done()
  }
  return (
    <Modal className="custom-modal" visible={visible} onOk={updateRoomInfo} onCancel={closeModal}
           afterClose={resetState} title="更新房间" okText="更新信息"
           cancelText="取消" okButtonProps={{ shape: 'round' }}
           cancelButtonProps={{ shape: 'round' }} closable maskClosable={false}
    >
      <Form layout="vertical" form={form} initialValues={{
        name: room.name,
        description: room.description,
        announcement: room.announcement,
        type: room.type
      }}>
        <RoomNameForm/>
        <RoomDescriptionForm />
        <RoomAnnouncementForm/>
        <RoomTypeForm/>
      </Form>
    </Modal>
  )
}

export default UpdateRoomModal
