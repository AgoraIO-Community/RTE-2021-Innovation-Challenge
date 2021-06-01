import React from 'react'
import { Form, Input } from 'antd'
import App from 'constants/app'
import { EyeInvisibleOutlined, EyeTwoTone } from '@ant-design/icons'

interface Props {
  label: string
  name: string
}

const PasswordForm: React.FC<Props> = props => {
  const { label, name } = props
  return (
    <Form.Item label={label} name={name} required rules={[{
      validator: async (_, password) => {
        if (!password?.trim()) {
          throw new Error('请填写密码')
        }
        if (!/^[a-zA-Z0-9._!@#]+$/.test(password)) {
          throw new Error('暂不支持包含特殊字符的密码')
        }
        if (password.length > App.PasswordMaxLength) {
          throw new Error(`暂只支持${App.PasswordMaxLength}个字符以内的密码`)
        }
        if (password.length < App.PasswordMinLength) {
          throw new Error(`暂只支持${App.PasswordMinLength}个字符以上的密码`)
        }
      }
    }]}>
      <Input.Password iconRender={visible => (visible ? <EyeTwoTone/> : <EyeInvisibleOutlined/>)}
                      placeholder="请输入..." maxLength={App.PasswordMaxLength}/>
    </Form.Item>
  )
}

export default PasswordForm
