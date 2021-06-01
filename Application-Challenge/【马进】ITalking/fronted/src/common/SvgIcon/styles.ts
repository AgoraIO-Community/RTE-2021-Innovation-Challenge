import styled from 'styled-components'

export const Icon = styled.img<{center: boolean}>`
  svg {
    text-align: ${props => props.center ? 'center' : 'unset'};
  }
`
