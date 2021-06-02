import React, { useEffect } from 'react'
import { SoundTwoTone } from '@ant-design/icons'
import * as S from './styles'
import FixHeader from 'common/FixHeader'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import EmptyStatus from 'common/EmptyStatus'
import Avatar from 'common/Avatar'
import useUserCard from 'hooks/useUserCard'
import { NotificationType } from 'models/notification'
import { formatRelativeTime } from 'helpers/time'
import useDocumentVisibility from 'hooks/useDocumentVisibility'
import { Helmet } from 'react-helmet'

const Notification: React.FC = () => {
  const notification = useSelector((state: RootState) => state.NotificationModel)
  const $ = useDispatch<Dispatch>()
  const userCard = useUserCard()
  const documentVisibility = useDocumentVisibility()

  useEffect(() => {
    if (documentVisibility !== 'visible') {
      return
    }
    (async () => {
      await $.NotificationModel.setAsync()
    })()
  }, [documentVisibility])
  const notificationList = !notification?.list?.length ? [] : (notification.list?.filter(n => n.type === notification.type))
  return (
    <S.Notification>
      <Helmet>
        <title>通知 / ITalking</title>
      </Helmet>
      <FixHeader title="最新通知" icon={<SoundTwoTone />}/>
      <S.Tabs>
        <S.Tab active={notification.type === NotificationType.Official}
               onClick={() => $.NotificationModel.setType(NotificationType.Official)}>
          官方消息
        </S.Tab>
        <S.Tab active={notification.type === NotificationType.Follow}
               onClick={() => $.NotificationModel.setType(NotificationType.Follow)}>
          好友关注
        </S.Tab>
      </S.Tabs>
      {!notificationList?.length && !notification.loading
        ? (<EmptyStatus description="获取通知失败，请稍后再试"/>)
        : (
            <S.NotificationList>
              {notificationList.map(notification => (
                <S.NotificationItem key={notification.id}>
                  <S.NotificationSender onClick={async () => await userCard.view(notification.sender.id)}>
                    <S.NotificationAvatar>
                      <Avatar name={notification.sender.name} size={48}/>
                    </S.NotificationAvatar>
                    <S.NotificationName>
                      {notification.sender.name}
                    </S.NotificationName>
                  </S.NotificationSender>
                  <S.NotificationContent>
                    {notification.content}
                  </S.NotificationContent>
                  <S.NotificationTime>
                    {formatRelativeTime(notification.created_at)}
                  </S.NotificationTime>
                </S.NotificationItem>
              ))}
            </S.NotificationList>
          )
      }

    </S.Notification>

  )
}

export default Notification
