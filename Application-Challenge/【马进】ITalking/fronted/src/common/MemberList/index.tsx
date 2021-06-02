import React from 'react'
import { Room } from 'models/room'
import * as S from './styles'
import App from 'constants/app'
import Avatar from 'common/Avatar'

interface Props {
  room: Room
  size?: number
}

const MemberList: React.FC<Props> = props => {
  const { room, size = App.MaxMemberShowLength } = props
  const primaryMembers = [...room.speakers, ...room.spectators].slice(0, size)
  return (
    <S.MemberList>
      <S.AvatarList>
        {primaryMembers.map((member, index) => (
          <S.Avatar
            key={member.id}
            style={{ zIndex: size - index, marginLeft: index === 0 ? 0 : -5 }}>
            <Avatar name={member.name}/>
          </S.Avatar>))}
      </S.AvatarList>
      <S.Name>
        {primaryMembers.slice(0, size).map(member => member.name).join(', ')}
      </S.Name>
    </S.MemberList>
  )
}

export default MemberList
