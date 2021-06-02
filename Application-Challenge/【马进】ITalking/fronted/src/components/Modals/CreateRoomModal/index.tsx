import React from 'react'
import { Form, message, Modal } from 'antd'
import { RoomType } from 'models/room'
import RoomApi from 'services/room'
import { isFailedResponse } from 'helpers/http'
import { PlusCircleOutlined } from '@ant-design/icons'
import { useHistory } from 'react-router-dom'
import NProgress from 'nprogress'
import { GlobalMessageType } from 'types/message'
import useRoom from 'hooks/useRoom'
import { useSelector } from 'react-redux'
import { RootState } from 'store'
import { UserStatus } from 'models/user'
import RoomNameForm from 'common/FormItem/RoomNameForm'
import RoomDescriptionForm from 'common/FormItem/RoomDescriptionForm'
import RoomAnnouncementForm from 'common/FormItem/RoomAnnouncementForm'
import RoomTypeForm from 'common/FormItem/RoomType'

interface Props {
  visible: boolean
  closeModal: () => void
}

const CreateRoomModal: React.FC<Props> = props => {
  const { visible, closeModal } = props
  const [form] = Form.useForm()
  const history = useHistory()
  const userState = useSelector((state: RootState) => state.UserModel)
  const { leaveRoom } = useRoom()
  const resetState = () => {
    form.resetFields()
  }
  const createRoom = async () => {
    const { name, description, announcement, type } = await form.validateFields()
    NProgress.start()
    closeModal()
    const response = await RoomApi.Create({
      name,
      description,
      announcement,
      type: type ? RoomType.Private : RoomType.Public
    })

    if (isFailedResponse(response)) {
      message.warn('房间创建失败')
      NProgress.done()
      return
    }
    const { data } = response.data
    history.push('/room/' + data)
    await window.globalRTMApi?.sendMessage({
      type: GlobalMessageType.RefreshFeed
    })
    NProgress.done()
  }
  const checkStatus = async () => {
    if (userState.status !== UserStatus.Visitor) {
      await leaveRoom(() => {
        (async () => {
          await createRoom()
        })()
      }, '创建房间后原有的房间将被解散，确认要创建吗？')
      return
    }
    await createRoom()
  }
  return (
    <Modal className="custom-modal" visible={visible} onOk={checkStatus} onCancel={closeModal} afterClose={resetState}
           title="创建房间" okText="快速创建"
           cancelText="取消" okButtonProps={{ shape: 'round', icon: <PlusCircleOutlined/> }}
           cancelButtonProps={{ shape: 'round' }} maskClosable={false}
           closable>
      <Form layout="vertical" form={form}>
        <RoomNameForm/>
        <RoomDescriptionForm />
        <RoomAnnouncementForm/>
        <RoomTypeForm/>
      </Form>
    </Modal>
  )
}

export default CreateRoomModal
