import React from 'react'
import * as S from './styles'
import {
  FileSearchOutlined,
  FullscreenOutlined,
  HistoryOutlined,
  PauseCircleOutlined,
  PlayCircleOutlined,
  PoweroffOutlined,
  UserAddOutlined
} from '@ant-design/icons'
import { Fade } from 'react-awesome-reveal'
import Avatar from 'common/Avatar'
import SearchBar from 'common/SearchBar'
import { useHistory } from 'react-router-dom'
import RouteMap from 'types/route'
import MemberList from 'common/MemberList'
import { Tooltip } from 'antd'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import useRoom from 'hooks/useRoom'
import UserCard from 'common/UserCard'
import { UserStatus } from 'models/user'

const Sidebar: React.FC = () => {
  const user = useSelector((state: RootState) => state.UserModel)
  const room = useSelector((state: RootState) => state.RoomModel)
  const reservation = useSelector((state: RootState) => state.ReservationModel)
  const $ = useDispatch<Dispatch>()
  const history = useHistory()
  const { leaveRoom, inviteFriend } = useRoom()
  const goReservation = () => {
    history.push(RouteMap.Reservation)
  }
  const goRoom = () => {
    history.push('/room/' + room.id)
  }
  const goFollowingTab = () => {
    $.GlobalModel.setViewFollowing(true)
    history.push(RouteMap.Friend)
  }
  const goFollowedTab = () => {
    $.GlobalModel.setViewFollowing(false)
    history.push(RouteMap.Friend)
  }
  return (
    <S.Sidebar>
      <SearchBar/>
      <UserCard user={user} onInfoClick={$.ModalModel.openProfileModal} onFollowingClick={goFollowingTab} onFollowedClick={goFollowedTab} />
      {user.status !== UserStatus.Visitor && (
        <S.RoomCard>
          <S.RoomCardHeader>
            <S.RoomCardName>{room.name}</S.RoomCardName>
            <S.RoomCardCount>
              <S.RoomCardDot/>{room.speakers.length + room.spectators.length}
            </S.RoomCardCount>
          </S.RoomCardHeader>
          <S.RoomCardDescription>{room.description}</S.RoomCardDescription>
          <MemberList room={room}/>
          <S.RoomCardFooter>
            <Tooltip placement="top" title={room.playing ? '静音' : '播放'}>
              <S.RoomCardBtn onClick={$.RoomModel.togglePlaying} primary={!room.playing}>
                {room.playing ? <PauseCircleOutlined/> : <PlayCircleOutlined/>}
              </S.RoomCardBtn>
            </Tooltip>
            <Tooltip placement="top" title="进入房间">
              <S.RoomCardBtn onClick={goRoom}>
                <FullscreenOutlined/>
              </S.RoomCardBtn>
            </Tooltip>
            <Tooltip placement="top" title="邀请">
              <S.RoomCardBtn onClick={inviteFriend}>
                <UserAddOutlined/>
              </S.RoomCardBtn>
            </Tooltip>
            <Tooltip placement="top" title="退出房间">
              <S.RoomCardBtn primary onClick={async () => await leaveRoom()}>
                <PoweroffOutlined/>
              </S.RoomCardBtn>
            </Tooltip>
          </S.RoomCardFooter>
        </S.RoomCard>
      )}
      <Fade duration={800} triggerOnce>
        <S.UpcomingRoom>
          <S.RoomHeader>
            即将开始的房间<HistoryOutlined/>
          </S.RoomHeader>
          {reservation.list.length !== 0
            ? <S.RoomList>
              {reservation.list.slice(0, 3).map(room => (
                <S.RoomItem key={room.id} onClick={goReservation}>
                  <S.RoomTime>{room.timeString}</S.RoomTime>
                  <S.RoomName>{room.name}</S.RoomName>
                  {room.description && <S.RoomDescription>{room.description}</S.RoomDescription>}
                  <S.RoomCreator>
                    <S.RoomCreatorAvatar> <Avatar name={room.creator.name}/></S.RoomCreatorAvatar>
                    <S.RoomCreatorName>{room.creator.name}</S.RoomCreatorName>
                  </S.RoomCreator>
                </S.RoomItem>
              ))}
            </S.RoomList>
            : <S.EmptyBlock description="当前还没有人预订房间，快来试一下吧~"/>}
          <S.RoomExplore onClick={goReservation}>
            探寻更多房间<FileSearchOutlined/>
          </S.RoomExplore>
        </S.UpcomingRoom>
      </Fade>
      <S.Footer>
        <a href="https://tomotoes.com" target="_blank">© 2021 TOMOTOES.COM</a>
      </S.Footer>
    </S.Sidebar>
  )
}

export default Sidebar
