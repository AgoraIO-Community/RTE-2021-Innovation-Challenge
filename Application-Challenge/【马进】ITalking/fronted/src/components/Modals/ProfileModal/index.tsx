import React, { useEffect, useState } from 'react'
import { Form, Input, message, Modal, Popover } from 'antd'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import App from 'constants/app'
import Avatar from 'common/Avatar'
import NProgress from 'nprogress'
import * as S from './styles'
import { UserStatus } from 'models/user'
import { RoomMessageType } from 'types/message'

interface Props {
  visible: boolean
  closeModal: () => void
}

const ProfileModal: React.FC<Props> = props => {
  const { visible, closeModal } = props
  const [form] = Form.useForm()
  const user = useSelector((state: RootState) => state.UserModel)
  const room = useSelector((state: RootState) => state.RoomModel)
  const $ = useDispatch<Dispatch>()
  const [name, setName] = useState(user.name)

  useEffect(() => {
    setName(user.name)
  }, [user.name])

  const changeName = (e: React.FormEvent<HTMLInputElement>) => {
    const newName = e.currentTarget.value ?? user.name
    setName(newName)
  }

  const resetState = () => {
    setName(user.name)
    form.setFieldsValue({ name: user.name, description: user.description })
  }
  const updateProfile = async () => {
    const { name, description } = await form.validateFields()
    NProgress.start()
    closeModal()
    const updateSuccess = await $.UserModel.updateAsync({ name, description })
    if (!updateSuccess) {
      message.warn('因不可抗拒因素，您的信息更新失败了')
    } else {
      message.success('您的信息已更新')
    }
    if (user.status !== UserStatus.Visitor) {
      await Promise.all([
        $.RoomModel.setAsync(room.id),
        $.FeedModel.setAsync(),
        window.roomRTMApi?.sendMessage({
          type: RoomMessageType.UpdateRoom
        })
      ])
    }
    NProgress.done()
  }
  return (
    <Modal className="custom-modal" visible={visible} onOk={updateProfile} onCancel={closeModal} afterClose={resetState}
           title="用户信息" okText="更新信息" maskClosable={false}
           cancelText="取消" okButtonProps={{ shape: 'round' }} cancelButtonProps={{ shape: 'round' }} closable>
      <Popover content="您的头像"><S.Avatar><Avatar name={name} size={80}/></S.Avatar></Popover>
      <Form layout="vertical" form={form} initialValues={{ name: user.name, description: user.description }}>
        <Form.Item label="您的昵称" name="name" required rules={[{
          validator: async (_, name) => {
            if (!name?.trim()) {
              throw new Error('请填写昵称')
            }
            if (!/^[\u4e00-\u9fa5_a-zA-Z0-9\s]+$/.test(name)) {
              throw new Error('暂不支持包含特殊字符的昵称')
            }
            if (name.length > App.NameMaxLength) {
              throw new Error(`暂只支持${App.NameMaxLength}个字符以内的昵称`)
            }
            if (name.length < App.NameMinLength) {
              throw new Error(`暂只支持${App.NameMinLength}个字符以上的昵称`)
            }
          }
        }]}>
          <Input placeholder="请输入..." maxLength={App.NameMaxLength} value={name} onChange={changeName}/>
        </Form.Item>
        <Form.Item label="您的简介" name="description">
          <Input.TextArea placeholder="请输入..." showCount maxLength={App.DescriptionMaxLength}/>
        </Form.Item>
      </Form>
    </Modal>
  )
}

export default ProfileModal
