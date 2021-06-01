import React from 'react'
import * as S from './styles'

export interface Props {
  primary?: boolean
  children: React.ReactNode
  onClick?: () => void
}

const Button: React.FC<Props> = props => {
  const { primary = true, children, onClick = () => {} } = props
  return (
    <S.Button primary={primary} onClick={onClick}>
      {children}
    </S.Button>
  )
}

export default Button
