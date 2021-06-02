import styled from 'styled-components'
import { Props } from './index'

export const Button = styled.button<Props>`
  background: ${(props) => props.primary ? '#2e186a' : '#fff'};
  color: ${(props) => (props.primary ? '#fff' : '#2E186A')};
  font-size: 1rem;
  font-weight: 700;
  width: 100%;
  border: ${(props) => (props.primary ? '0px' : '1px solid #2E186A')};
  height: 55px;
  outline: none;
  cursor: pointer;
  border-radius: 25px;
  max-width: 180px;

  @media only screen and (max-width: 1024px) {
    width: 160px;
  }

  @media only screen and (max-width: 768px) {
    width: 140px;
  }

  @media only screen and (max-width: 480px) {
    width: 130px;
  }
  
  &:hover {
    transform: scale(.985);
    transition: .25s;
  }

  &:active,
  &:focus {
    transform: scale(.97);
    transition: .25s;
  }
`
