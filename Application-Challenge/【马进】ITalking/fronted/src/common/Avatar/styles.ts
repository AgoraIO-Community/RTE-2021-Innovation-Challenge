import styled from 'styled-components'

export const Avatar = styled.div<{size: number}>`
  width: ${props => props.size}px;
  height: ${props => props.size}px;
  overflow: hidden;
  transition: opacity .2s;
  &:hover {
    opacity: .8;
  }
`
