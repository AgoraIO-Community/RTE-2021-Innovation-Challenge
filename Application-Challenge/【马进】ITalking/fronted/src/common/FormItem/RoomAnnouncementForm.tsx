import React from 'react'
import { Form, Input } from 'antd'

const RoomAnnouncementForm: React.FC = () => {
  return (
    <Form.Item label="房间公告" name="announcement" tooltip="用户进入房间后将发送公告到聊天栏">
      <Input.TextArea placeholder="请输入..." showCount/>
    </Form.Item>
  )
}

export default RoomAnnouncementForm
