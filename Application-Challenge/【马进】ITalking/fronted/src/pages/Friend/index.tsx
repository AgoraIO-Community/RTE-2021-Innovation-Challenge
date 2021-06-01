import React from 'react'
import { EyeTwoTone } from '@ant-design/icons'

import * as S from './styles'
import FixHeader from 'common/FixHeader'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import useUserCard from 'hooks/useUserCard'
import EmptyStatus from 'common/EmptyStatus'
import { message } from 'antd'
import Avatar from 'common/Avatar'
import FollowBtn from 'common/FollowBtn'
import { Helmet } from 'react-helmet'

const Friend: React.FC = () => {
  const user = useSelector((state: RootState) => state.UserModel)
  const global = useSelector((state: RootState) => state.GlobalModel)
  const $ = useDispatch<Dispatch>()
  const userCard = useUserCard()
  const friendList = global.viewFollowing ? user.followings : user.followers

  const toggleFollowStatus = async (params: { uid: string, isFollowing: boolean }) => {
    const toggleSuccess = await $.UserModel.toggleFollow(params)
    if (!toggleSuccess) {
      message.warn('因不可抗拒因素，操作失败')
    }
  }

  return (
    <S.Friend>
      <Helmet>
        <title>好友 / ITalking</title>
      </Helmet>
      <FixHeader title="好友列表" icon={<EyeTwoTone />}/>
      <S.Tabs>
        <S.Tab active={!global.viewFollowing}
               onClick={() => $.GlobalModel.setViewFollowing(false)}>
          关注者
        </S.Tab>
        <S.Tab active={global.viewFollowing}
               onClick={() => $.GlobalModel.setViewFollowing(true)}>
          正在关注
        </S.Tab>
      </S.Tabs>
      {!friendList?.length
        ? (<EmptyStatus description="空无一人..."/>)
        : (
          <S.FriendList>
            {friendList.map(friend => {
              const isFollowing = user.followings.some(f => f.id === friend.id)
              return (
                <S.FriendItem key={friend.id}>
                  <S.FriendAvatar onClick={async () => await userCard.view(friend.id)}>
                    <Avatar name={friend.name} size={48}/>
                  </S.FriendAvatar>
                  <S.FriendDetail>
                    <S.FriendName onClick={async () => await userCard.view(friend.id)}>
                      {friend.name}
                    </S.FriendName>
                    {friend.description && (
                      <S.FriendDescription onClick={async () => await userCard.view(friend.id)}>
                        {friend.description}
                      </S.FriendDescription>
                    )}
                  </S.FriendDetail>
                  <S.FollowBtn>
                    <FollowBtn onClick={async () => await toggleFollowStatus({ uid: friend.id, isFollowing })}
                               isFollowing={isFollowing}/>
                  </S.FollowBtn>
                </S.FriendItem>
              )
            })}
          </S.FriendList>
          )}
    </S.Friend>

  )
}

export default Friend
