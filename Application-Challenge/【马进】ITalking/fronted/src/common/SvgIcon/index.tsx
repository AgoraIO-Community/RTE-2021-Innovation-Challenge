import React from 'react'
import * as S from './styles'

interface Props {
  src: string
  width?: number
  height?: number
  center?: boolean
}

const SvgIcon: React.FC<Props> = props => {
  const { src, width = '100%', height = '100%', center = false } = props
  return <S.Icon center={center} src={`/img/svg/${src}`} alt={src} width={width} height={height}/>
}

export default SvgIcon
