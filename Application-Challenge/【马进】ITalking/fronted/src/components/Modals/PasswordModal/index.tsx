import React from 'react'
import { Form, message, Modal } from 'antd'
import NProgress from 'nprogress'
import UserApi from 'services/user'
import { isFailedResponse } from 'helpers/http'
import PasswordForm from 'common/FormItem/Password'

interface Props {
  visible: boolean
  closeModal: () => void
}

const PasswordModal: React.FC<Props> = props => {
  const { visible, closeModal } = props
  const [form] = Form.useForm()

  const resetState = () => {
    form.resetFields()
  }
  const updatePassword = async () => {
    const { password, confirmedPassword } = await form.validateFields()
    if (password !== confirmedPassword) {
      message.warn('您输入的两次密码不一致')
      return
    }
    NProgress.start()
    closeModal()
    const response = await UserApi.UpdatePassword(password)
    if (isFailedResponse(response)) {
      message.warn('密码更新失败')
    } else {
      message.success('密码更新成功')
    }
    NProgress.done()
  }

  return (
    <Modal className="custom-modal" visible={visible} onOk={updatePassword} onCancel={closeModal}
           afterClose={resetState} title="更新密码"
           okText="保存修改"
           cancelText="取消" okButtonProps={{ shape: 'round' }} cancelButtonProps={{ shape: 'round' }} closable>
      <Form layout="vertical" form={form}>
        <PasswordForm label="您的新密码" name="password"/>
        <PasswordForm label="再次确定密码" name="confirmedPassword"/>
      </Form>
    </Modal>
  )
}

export default PasswordModal
