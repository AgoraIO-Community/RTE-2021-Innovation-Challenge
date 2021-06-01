import React from 'react'
import * as S from './styles'
import { SmileOutlined } from '@ant-design/icons'

interface Props {
  title: string
  icon: JSX.Element
}

const FixHeader: React.FC<Props> = props => {
  const { title, icon = <SmileOutlined/>, children } = props
  return (
    <S.FixHeader>
      <S.FixTitle>
        {title}{icon}
      </S.FixTitle>
      {children}
    </S.FixHeader>
  )
}

export default FixHeader
