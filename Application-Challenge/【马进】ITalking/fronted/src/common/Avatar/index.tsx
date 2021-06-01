import React from 'react'
// @ts-expect-error
import multiavatar from '@multiavatar/multiavatar'
import * as S from './styles'

interface Props { name: string, size?: number, onClick?: () => Promise<void> }

const Avatar: React.FC<Props> = props => {
  const { name, size = 20, onClick = () => {} } = props
  const svgCode = multiavatar(name)
  return <S.Avatar size={size} onClick={onClick} dangerouslySetInnerHTML={{ __html: svgCode }}/>
}

export default Avatar
