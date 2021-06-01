import React, { useEffect } from 'react'
import { CalendarTwoTone, PlusCircleOutlined, ScheduleOutlined, FormOutlined, DeleteOutlined } from '@ant-design/icons'
import * as S from './styles'
import FixHeader from 'common/FixHeader'
import { Button, Popconfirm } from 'antd'
import EmptyStatus from 'common/EmptyStatus'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import useUserCard from 'hooks/useUserCard'
import useDocumentVisibility from 'hooks/useDocumentVisibility'
import Avatar from 'common/Avatar'
import { makeGoogleCalendarUrl } from 'helpers/time'
import { ReservationType } from 'models/reservation'
import { Helmet } from 'react-helmet'

const Reservation: React.FC = () => {
  const reservation = useSelector((state: RootState) => state.ReservationModel)
  const user = useSelector((state: RootState) => state.UserModel)
  const $ = useDispatch<Dispatch>()
  const userCard = useUserCard()
  const documentVisibility = useDocumentVisibility()

  useEffect(() => {
    if (documentVisibility !== 'visible') {
      return
    }
    (async () => {
      await $.ReservationModel.setAsync()
    })()
  }, [documentVisibility])
  const scheduleDate = (reservation: ReservationType) => {
    const googleCalendarUrl = makeGoogleCalendarUrl(reservation)
    window.open(googleCalendarUrl, '_blank')
  }
  const updateReservation = (reservation: ReservationType) => {
    $.ReservationModel.updateReservation(reservation)
    $.ModalModel.openUpdateReservationModal()
  }
  return (
    <S.Reservation>
      <Helmet>
        <title>预订房间 / ITalking</title>
      </Helmet>
      <FixHeader title="预订的房间" icon={<CalendarTwoTone/>}/>
      <S.ReservationHeader>
        <S.ReservationTitle>
          即将开始的房间
        </S.ReservationTitle>
        <S.CreateReservationBtn>
          <Button type="primary" size="large" onClick={$.ModalModel.openCreateReservationModal} shape="round"
                  icon={<PlusCircleOutlined/>}>
            预订我的房间
          </Button>
        </S.CreateReservationBtn>
      </S.ReservationHeader>
      {!reservation.list?.length && !reservation.loading
        ? (<EmptyStatus description="当前还没有人预订房间，快来试一下吧~"/>)
        : (
          <S.RoomList>
            {reservation.list.map(reservation => (
              <S.RoomItem key={reservation.id}>
                <S.RoomDetail>
                  <S.RoomName>
                    {reservation.name}
                  </S.RoomName>
                  {reservation.description && (
                    <S.RoomDescription>
                      {reservation.description}
                    </S.RoomDescription>
                  )}
                </S.RoomDetail>
                <S.RoomContent>
                  <S.RoomCreator onClick={async () => await userCard.view(reservation.creator.id)}>
                    <S.CreatorAvatar><Avatar size={32} name={reservation.creator.name}/></S.CreatorAvatar>
                    <S.CreatorName>{reservation.creator.name}</S.CreatorName>
                  </S.RoomCreator>
                  <S.RoomBtnGroup>
                    <S.RoomBtn>
                      <Button icon={<ScheduleOutlined/>} onClick={() => scheduleDate(reservation)}/>
                    </S.RoomBtn>
                    {user.id === reservation.creator.id && (
                      <>
                        <S.RoomBtn onClick={() => updateReservation(reservation)}>
                          <Button icon={<FormOutlined/>}/>
                        </S.RoomBtn>
                        <S.RoomBtn>
                          <Popconfirm title="删除要该预订房间吗？" okText="删除" cancelText="取消"
                                      onConfirm={async () => await $.ReservationModel.deleteAsync(reservation.id!)}
                                      cancelButtonProps={{ type: 'text' }}
                                      okButtonProps={{ type: 'link' }}
                          >
                            <Button danger icon={<DeleteOutlined/>}/>
                          </Popconfirm>
                        </S.RoomBtn>
                      </>
                    )}
                  </S.RoomBtnGroup>
                </S.RoomContent>
                <S.RoomTime>
                  {reservation.timeString}
                </S.RoomTime>
              </S.RoomItem>
            ))}
          </S.RoomList>
          )
      }
    </S.Reservation>

  )
}

export default Reservation
