import React from 'react'
import * as S from './styles'

export interface Props {
  children: JSX.Element | JSX.Element[]
}

const Container: React.FC<Props> = props => {
  const { children } = props
  return (
    <S.Container>
      {children}
    </S.Container>
  )
}

export default Container
