import React from 'react'
import * as S from './styles'

interface Props {
  description: string
}

const EmptyStatus: React.FC<Props> = props => {
  return <S.EmptyBlock description={props.description} />
}

export default EmptyStatus
