import React, { useEffect } from 'react'
import Menu from 'components/Menu'
import * as S from './styles'
import Stage from 'components/Room/Stage'
import Chat from 'components/Room/Chat'
import { useHistory, useParams } from 'react-router-dom'
import { message } from 'antd'
import RouteMap from 'types/route'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import SoundEffects from 'components/Room/SoundEffects'
import { Helmet } from 'react-helmet'

const Room: React.FC = () => {
  const { id } = useParams<{ id: string }>()
  const room = useSelector((state: RootState) => state.RoomModel)
  const $ = useDispatch<Dispatch>()
  const history = useHistory()

  useEffect(() => {
    if (room.id) {
      return
    }
    (async () => {
      const setRoomSuccess = await $.RoomModel.setAsync(id)
      if (!setRoomSuccess) {
        await $.FeedModel.setAsync()
        message.warn('该房间已被关闭，请换一个吧')
        history.push(RouteMap.Feed)
      }
    })()
  }, [])

  return (
    <S.Room>
      <Helmet>
        <title>{room?.name ?? '房间'} / ITalking</title>
      </Helmet>
      <Menu/>
      <Stage/>
      <Chat/>
      <SoundEffects/>
    </S.Room>
  )
}

export default Room
