import React, { useState } from 'react'
import * as S from './styles'
import { Button } from 'antd'
import { UserAddOutlined, UserDeleteOutlined, UserOutlined } from '@ant-design/icons'

interface Props {
  onClick: () => Promise<void>
  isFollowing: boolean
}

const FollowBtn: React.FC<Props> = props => {
  const { onClick, isFollowing } = props
  const [isHoveringBtn, setIsHoveringBtn] = useState(false)

  return (
    <S.FollowBtn onClick={onClick}>
      <Button onMouseEnter={() => setIsHoveringBtn(true)} onMouseLeave={() => setIsHoveringBtn(false)}
              shape="round" type={isFollowing ? 'primary' : 'default' }
              icon={(isFollowing && isHoveringBtn)
                ? <UserDeleteOutlined/>
                : (isFollowing
                    ? <UserOutlined/>
                    : <UserAddOutlined/>)}
              danger={isFollowing && isHoveringBtn}>
        {isFollowing ? (isHoveringBtn ? '取消关注' : '正在关注') : '关注 ta'}
      </Button>
    </S.FollowBtn>
  )
}

export default FollowBtn
