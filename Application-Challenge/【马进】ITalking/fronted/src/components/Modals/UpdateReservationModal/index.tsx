import React from 'react'
import { DatePicker, Form, message, Modal } from 'antd'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import NProgress from 'nprogress'
import moment, { Moment } from 'moment'
import locale from 'antd/es/date-picker/locale/zh_CN'
import RoomNameForm from 'common/FormItem/RoomNameForm'
import RoomDescriptionForm from 'common/FormItem/RoomDescriptionForm'

interface Props {
  visible: boolean
  closeModal: () => void
}

const UpdateReservationModal: React.FC<Props> = props => {
  const { visible, closeModal } = props
  const [form] = Form.useForm()
  const $ = useDispatch<Dispatch>()
  const { updatedReservation: reservation } = useSelector((state: RootState) => state.ReservationModel)
  const updateReservationInfo = async () => {
    if (!reservation) {
      return
    }
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
    const updateSuccess = await $.ReservationModel.updateAsync({
      id: reservation.id,
      name,
      description,
      time: (time as Moment).unix()
    })
    if (!updateSuccess) {
      message.warn('因不可抗拒因素，房间信息更新失败了')
    } else {
      message.success('房间信息已更新')
    }
    NProgress.done()
  }
  const disabledDate = (current: Moment) => {
    return current && current < moment().subtract(1, 'day').endOf('day')
  }
  const initialValues = {
    name: reservation?.name ?? '',
    description: reservation?.description ?? '',
    time: !reservation?.time ? moment() : moment(reservation?.time * 1000)
  }
  form.setFieldsValue(initialValues)
  return (
    <Modal className="custom-modal" visible={visible} onOk={updateReservationInfo} onCancel={closeModal}
           title="更新房间" okText="更新信息" cancelText="取消" okButtonProps={{ shape: 'round' }}
           cancelButtonProps={{ shape: 'round' }} closable maskClosable={false}>
      <Form layout="vertical" form={form} initialValues={initialValues}>
        <RoomNameForm/>
        <RoomDescriptionForm/>
        <Form.Item label="预订时间" name="time" required>
          <DatePicker showTime locale={locale} placeholder="请选择..." disabledDate={disabledDate} showToday={false} showNow={false} />
        </Form.Item>
      </Form>
    </Modal>
  )
}

export default UpdateReservationModal
