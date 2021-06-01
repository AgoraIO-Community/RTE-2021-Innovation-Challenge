import React from 'react'
import { Form, Input } from 'antd'
import App from 'constants/app'

const RoomNameForm: React.FC = () => {
  return (
    <Form.Item label="房间名称" name="name" required rules={[{
      validator: async (_, name) => {
        if (!name?.trim()) {
          throw new Error('请填写房间名称')
        }
        if (!/^[\u4e00-\u9fa5_a-zA-Z0-9\s]+$/.test(name)) {
          throw new Error('暂不支持包含特殊字符的名称')
        }
        if (name.length < App.RoomNameMinLength) {
          throw new Error(`暂只支持${App.RoomNameMinLength}个字符以上的名称`)
        }
        if (name.length > App.RoomNameMaxLength) {
          throw new Error(`暂只支持${App.RoomNameMinLength}个字符以内的名称`)
        }
      }
    }]}>
      <Input placeholder="请输入..." maxLength={App.RoomNameMaxLength}/>
    </Form.Item>
  )
}

export default RoomNameForm
