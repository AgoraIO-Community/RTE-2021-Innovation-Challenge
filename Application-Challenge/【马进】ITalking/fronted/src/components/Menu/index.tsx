import React from 'react'
import {
  CalendarOutlined,
  CalendarFilled,
  HomeOutlined,
  HomeFilled,
  SettingOutlined,
  EyeFilled,
  EyeOutlined,
  BellFilled,
  BellOutlined,
  SettingFilled,
  EllipsisOutlined
} from '@ant-design/icons'
import * as S from './styles'
import SvgIcon from 'common/SvgIcon'
import RouteMap from 'types/route'
import { useHistory, useLocation } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import { Badge } from 'antd'

const Menu: React.FC = () => {
  const user = useSelector((state: RootState) => state.UserModel)
  const $ = useDispatch<Dispatch>()
  const history = useHistory()
  const location = useLocation()

  const goHome = () => {
    history.push(RouteMap.Feed)
  }

  const goReservation = () => {
    history.push(RouteMap.Reservation)
  }

  const goNotification = () => {
    history.push(RouteMap.Notification)
  }

  const goProfile = () => {
    history.push(RouteMap.Friend)
  }

  const goSetting = () => {
    history.push(RouteMap.Setting)
  }

  const goMorePage = () => {
    // window.open('https://tomotoes.com', '_blank')
  }

  const createRoom = async () => {
    $.ModalModel.openCreateRoomModal()
  }

  return (
    <S.Menu>
      <S.Banner>
        <SvgIcon src="logo.svg" width={50} height={50}/>
        <S.Title>ITalking</S.Title>
      </S.Banner>
      <S.MenuList>
        <S.MenuItem active={location.pathname === RouteMap.Feed} onClick={goHome}>
          <S.MenuIcon>{location.pathname === RouteMap.Feed ? <HomeFilled/> : <HomeOutlined/>}</S.MenuIcon>
          <S.MenuTitle>主页</S.MenuTitle>
        </S.MenuItem>
        <S.MenuItem active={location.pathname === RouteMap.Reservation} onClick={goReservation}>
          <S.MenuIcon>{location.pathname === RouteMap.Reservation ? <CalendarFilled/> : <CalendarOutlined/>}</S.MenuIcon>
          <S.MenuTitle>预订</S.MenuTitle>
        </S.MenuItem>
        <S.MenuItem active={location.pathname === RouteMap.Friend} onClick={goProfile}>
          <S.MenuIcon>{location.pathname === RouteMap.Friend ? <EyeFilled/> : <EyeOutlined/>}</S.MenuIcon>
          <S.MenuTitle>朋友</S.MenuTitle>
        </S.MenuItem>
        <S.MenuItem active={location.pathname === RouteMap.Notification} onClick={goNotification}>
          {user.unread && <S.UnreadDot><Badge status="processing"/></S.UnreadDot>}
          <S.MenuIcon>{location.pathname === RouteMap.Notification ? <BellFilled/> : <BellOutlined/>}</S.MenuIcon>
          <S.MenuTitle>通知</S.MenuTitle>
        </S.MenuItem>
        <S.MenuItem active={location.pathname === RouteMap.Setting} onClick={goSetting}>
          <S.MenuIcon>{location.pathname === RouteMap.Setting ? <SettingFilled/> : <SettingOutlined/>}</S.MenuIcon>
          <S.MenuTitle>设置</S.MenuTitle>
        </S.MenuItem>
        <S.MenuItem onClick={goMorePage} active={false} >
          <S.MenuIcon><EllipsisOutlined /></S.MenuIcon>
          <S.MenuTitle>更多</S.MenuTitle>
        </S.MenuItem>
      </S.MenuList>
      <S.CreateRoomButton onClick={createRoom}>
        创建房间
      </S.CreateRoomButton>
    </S.Menu>
  )
}

export default Menu
