import React from 'react'
import { DatePicker, Form, message, Modal } from 'antd'
import { PlusCircleOutlined } from '@ant-design/icons'
import NProgress from 'nprogress'
import { useDispatch } from 'react-redux'
import { Dispatch } from 'store'
import moment, { Moment } from 'moment'
import locale from 'antd/es/date-picker/locale/zh_CN'
import RoomNameForm from 'common/FormItem/RoomNameForm'
import RoomDescriptionForm from 'common/FormItem/RoomDescriptionForm'

interface Props {
  visible: boolean
  closeModal: () => void
}

const CreateReservationModal: React.FC<Props> = props => {
  const { visible, closeModal } = props
  const [form] = Form.useForm()
  const $ = useDispatch<Dispatch>()
  const resetState = () => {
    form.resetFields()
  }
  const createReservation = async () => {
    const { name, description, time } = await form.validateFields()
    if (!time) {
      message.warn('请填写预订时间')
      return
    }
    if ((time as Moment).isBefore(Date.now())) {
      message.warn('预订时间已经是过去式了')
      return
    }
    NProgress.start()
    closeModal()
    await $.ReservationModel.createAsync({ name, description, time: (time as Moment).unix() })
    NProgress.done()
  }

  const disabledDate = (current: Moment) => {
    return current && current < moment().subtract(1, 'day').endOf('day')
  }
  return (
    <Modal className="custom-modal" visible={visible} onOk={createReservation} onCancel={closeModal}
           afterClose={resetState}
           title="预订房间" okText="快速预订"
           maskClosable={false}
           cancelText="取消" okButtonProps={{ shape: 'round', icon: <PlusCircleOutlined/> }}
           cancelButtonProps={{ shape: 'round' }} closable>
      <Form layout="vertical" form={form}>
       <RoomNameForm/>
        <RoomDescriptionForm/>
        <Form.Item label="预订时间" name="time" required>
          <DatePicker showTime locale={locale} placeholder="请选择..." disabledDate={disabledDate} showToday={false} showNow={false} />
        </Form.Item>
      </Form>
    </Modal>
  )
}

export default CreateReservationModal
