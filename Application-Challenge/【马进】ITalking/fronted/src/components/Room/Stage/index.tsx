import React from 'react'
import * as S from './styles'
import {
  AudioMutedOutlined,
  AudioOutlined,
  LoadingOutlined,
  PauseCircleOutlined,
  PlayCircleOutlined,
  PoweroffOutlined,
  SmileOutlined,
  UserAddOutlined
} from '@ant-design/icons'
import FixHeader from 'common/FixHeader'
import Avatar from 'common/Avatar'
import { UserStatus } from 'models/user'
import { message, Popconfirm, Popover, Skeleton } from 'antd'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import useRoom from 'hooks/useRoom'
import useUserCard from 'hooks/useUserCard'
import { AttentionSeeker } from 'react-awesome-reveal'

const Stage: React.FC = () => {
  const user = useSelector((state: RootState) => state.UserModel)
  const global = useSelector((state: RootState) => state.GlobalModel)
  const room = useSelector((state: RootState) => state.RoomModel)
  const userCard = useUserCard()
  const $ = useDispatch<Dispatch>()
  const { stopSpeaking, consentSpeaking, resumeSpeaking, requestSpeaking, leaveRoom, inviteFriend } = useRoom()

  const changeUserStatus = async () => {
    if (user.status === UserStatus.Speaker) {
      if (user.speaking) {
        await stopSpeaking()
      } else {
        await resumeSpeaking()
      }
    } else if (user.status !== UserStatus.Applicant) {
      await requestSpeaking()
    }
  }

  const updateRoomInfo = () => {
    if (room.speakers.every(f => f.id !== user.id)) {
      message.info('只有主播才能修改信息哦')
      return
    }
    $.ModalModel.openUpdateRoomModal()
  }

  const isLoading = !global.roomRTMJoined || !global.rtcJoined

  return (
    <S.Stage>
      <S.StageInner>
        <FixHeader title={'结识那些陌生而有趣的人'} icon={<SmileOutlined/>}/>
        <S.StageHeader onClick={updateRoomInfo}>
          <Skeleton loading={isLoading} active paragraph={{ rows: 3 }}>
            <S.StageTitle>
              <S.StageName> {room.name}</S.StageName>
              <S.StageTimer>{room.time}</S.StageTimer>
            </S.StageTitle>
            {room?.creator?.name && <S.StageDetail>with<S.StageCreator>{room?.creator?.name}</S.StageCreator></S.StageDetail>}
            {room.description && <S.StageDescription>{room.description}</S.StageDescription>}
          </Skeleton>
        </S.StageHeader>
        <S.StageBody>
          <S.StageSpeaker>
            <S.StageLabel>主播<S.StageCount>{room.speakers.length}</S.StageCount></S.StageLabel>
            <S.StageUserList>
              <Skeleton loading={isLoading} active avatar paragraph={{ rows: 3 }}>
                {room.speakers.map(speaker => (
                  <S.StageUser key={speaker.id}>
                    <S.StageUserAvatar>
                      <Avatar onClick={async () => await userCard.view(speaker.id)} name={speaker.name}
                              size={60}/>
                    </S.StageUserAvatar>
                    <S.StageSpeakerStatus>
                      {!speaker.speaking
                        ? <AudioMutedOutlined/>
                        : <AudioOutlined/>}
                    </S.StageSpeakerStatus>
                    <S.StageUserName primary={speaker.id === user.id}
                                     onClick={async () => await userCard.view(speaker.id)}>
                      {speaker.name}
                    </S.StageUserName>
                  </S.StageUser>
                ))}
              </Skeleton>
            </S.StageUserList>
          </S.StageSpeaker>

          <S.StageSpectator>
            <S.StageLabel>听众<S.StageCount>{room.spectators.length}</S.StageCount></S.StageLabel>
            <S.StageUserList>
              <Skeleton loading={isLoading} active avatar paragraph={{ rows: 4 }}>
                {room.spectators.map(spectator => {
                  let avatar = (
                    <S.StageUserAvatar>
                      <Avatar onClick={async () => await userCard.view(spectator.id)} name={spectator.name}
                              size={60}/>
                    </S.StageUserAvatar>)
                  if (room.applicants.includes(spectator.id)) {
                    if (user.status === UserStatus.Speaker) {
                      avatar = (
                        <AttentionSeeker effect="shake">
                          <Popconfirm key={spectator.id} title="申请上麦中..." okText="同意" cancelText="忽视" visible={true}
                                      onConfirm={async () => await consentSpeaking(spectator.id)}
                                      onCancel={() => $.RoomModel.removeApplicant(spectator.id)}
                                      cancelButtonProps={{ type: 'text' }}
                                      okButtonProps={{ type: 'link' }}
                                      getPopupContainer={ () => document.getElementById(spectator.id)!}
                          > {avatar} </Popconfirm>
                        </AttentionSeeker>)
                    } else {
                      avatar = (
                        <AttentionSeeker effect="flash">
                          <Popover key={spectator.id} content="请求上麦中" visible={true}
                                   getPopupContainer={ () => document.getElementById(spectator.id)!}
                          > {avatar}</Popover>
                        </AttentionSeeker>
                      )
                    }
                  }
                  return (
                    <S.StageUser key={spectator.id} id={spectator.id}>
                      {avatar}
                      <S.StageUserName onClick={async () => await userCard.view(spectator.id)}
                                       primary={spectator.id === user.id}>
                        {spectator.name}
                      </S.StageUserName>
                    </S.StageUser>
                  )
                })}
              </Skeleton>
            </S.StageUserList>
          </S.StageSpectator>
        </S.StageBody>
        <S.StageFooter>
          <Skeleton loading={isLoading} active paragraph={{ rows: 2 }}>
            <S.StageBtn onClick={$.RoomModel.togglePlaying} border={!room.playing}>
              {room.playing
                ? (<><PauseCircleOutlined/>静音</>)
                : (<><PlayCircleOutlined/>播放</>)}
            </S.StageBtn>
            <S.StageBtn onClick={changeUserStatus}
                        border={(user.status === UserStatus.Speaker && !user.speaking) || (user.status === UserStatus.Applicant)}>
              {user.status === UserStatus.Speaker
                ? (<><AudioMutedOutlined/>闭麦</>)
                : (user.status === UserStatus.Applicant
                    ? (<><LoadingOutlined/>申请中</>)
                    : (<><AudioOutlined/>上麦</>))}
            </S.StageBtn>
            <S.StageBtn onClick={inviteFriend}>
              <UserAddOutlined/>
              邀请
            </S.StageBtn>
            <S.StageBtn onClick={async () => await leaveRoom()}>
              <PoweroffOutlined/>离开
            </S.StageBtn>
          </Skeleton>
        </S.StageFooter>
      </S.StageInner>
    </S.Stage>
  )
}

export default Stage
