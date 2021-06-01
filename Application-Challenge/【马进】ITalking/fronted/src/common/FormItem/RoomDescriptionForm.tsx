import React from 'react'
import { Form, Input } from 'antd'
import App from 'constants/app'

const RoomDescriptionForm: React.FC = () => {
  return (
    <Form.Item label="房间简介" name="description">
      <Input.TextArea placeholder="请输入..." showCount maxLength={App.RoomDescriptionMaxLength}/>
    </Form.Item>
  )
}

export default RoomDescriptionForm
