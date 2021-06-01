import React from 'react'
import { Form, Radio } from 'antd'
import { RoomType } from 'models/room'

const RoomTypeForm: React.FC = () => {
  return (
    <Form.Item label="房间类型" name="type">
      <Radio.Group defaultValue={RoomType.Public}>
        <Radio.Button value={RoomType.Public}>公开房间</Radio.Button>
        <Radio.Button value={RoomType.Private}>私有房间</Radio.Button>
      </Radio.Group>
    </Form.Item>
  )
}

export default RoomTypeForm
