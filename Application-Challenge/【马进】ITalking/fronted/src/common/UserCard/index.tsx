import React from 'react'
import { User } from 'models/user'
import * as S from './styles'
import Avatar from 'common/Avatar'
import { Skeleton } from 'antd'

interface Props {
  user: User
  onInfoClick?: () => void
  onFollowingClick?: () => void
  onFollowedClick?: () => void
}

const UserCard: React.FC<Props> = props => {
  const {
    user, onInfoClick = () => {
    }, onFollowingClick = () => {
    }, onFollowedClick = () => {
    }
  } = props

  return (
    <S.UserCard>
      <Skeleton loading={user.id === ''} active avatar paragraph={{ rows: 4 }}>
        <S.UserAvatar onClick={onInfoClick}>
          <Avatar name={user.name} size={80}/>
        </S.UserAvatar>
        <S.UserName onClick={onInfoClick}>{user.name}</S.UserName>
        <S.UserFollowDetail>
          <S.UserFollowBlock onClick={onFollowedClick}>
            <S.UserFollowLabel>{user.followers.length}</S.UserFollowLabel> 关注者
          </S.UserFollowBlock>
          <S.UserFollowBlock onClick={onFollowingClick}>
            <S.UserFollowLabel>{user.followings.length}</S.UserFollowLabel> 关注的人
          </S.UserFollowBlock>
        </S.UserFollowDetail>
        <S.UserDescription onClick={onInfoClick}>{user.description}</S.UserDescription>
      </Skeleton>
    </S.UserCard>
  )
}

export default UserCard
