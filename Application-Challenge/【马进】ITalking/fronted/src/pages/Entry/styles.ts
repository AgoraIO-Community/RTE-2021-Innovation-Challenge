import styled, { keyframes } from 'styled-components'
import { Link } from 'react-router-dom'

const bgColor = '#ecf0f3'
const shadowColor = '#d1d9e6'

const white = '#f9f9f9'

const gray = '#a0a5a8'
const black = '#211246'

const purple = '#2a1c66'

const transition = '1.25s'

export const Container = styled.div`
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  background-color: ${bgColor};
  color: ${gray};
`

export const PCForm = styled.div`
  position: relative;
  width: 100%;
  max-width: 1000px;
  height: 600px;
  padding: 25px;
  background-color: ${bgColor};
  box-shadow: 10px 10px 10px ${shadowColor},
    -10px -10px 10px ${white};
  border-radius: 12px;
`

export const PCContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 0;
  width: 100%;
  max-width: 600px;
  height: 100%;
  padding: 25px;
  background-color: ${bgColor};
  transition: ${transition};
`

export const SignInContainer = styled(PCContainer)<{ isSignIn: boolean }>`
  z-index: 100;
  left: ${props => props.isSignIn ? 'calc(100% - 600px)' : '0'};
  transform-origin: ${props => props.isSignIn ? 'unset' : 'right'};
`

export const LoginContainer = styled(PCContainer)<{ isSignIn: boolean }>`
  z-index: ${props => props.isSignIn ? '0' : '200'};
  left: ${props => props.isSignIn ? 'calc(100% - 600px)' : '0'};
  transform-origin: ${props => props.isSignIn ? 'unset' : 'right'};
`

export const PCSwitch = styled.div<{ isSignIn: boolean }>`
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 0;
  left: ${props => props.isSignIn ? '0' : 'calc(100% - 400px)'};
  transform-origin: ${props => props.isSignIn ? 'unset' : 'left'};
  height: 100%;
  width: 400px;

  padding: 50px;
  z-index: 200;
  transition: ${transition};

  background-color: ${bgColor};
  overflow: hidden;

  box-shadow: 4px 4px 10px ${shadowColor},
    -4px -4px 10px ${white};

  color: ${black};
`

export const PCSwitchContainer = styled.div<{ isSignIn: boolean }>`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  position: absolute;
  width: 400px;
  padding: 50px 55px;
  transition: ${transition};
  visibility: ${props => props.isSignIn ? 'visible' : 'hidden'};
  opacity: ${props => props.isSignIn ? '1' : '0'};
`

export const Button = styled.button`
  width: 180px;
  height: 50px;
  border-radius: 25px;
  margin-top: 40px;
  font-weight: 700;
  font-size: 14px;
  letter-spacing: 1.15px;

  background-color: ${purple};
  color: ${white};
  box-shadow: 8px 8px 16px ${shadowColor},
    -8px -8px 16px ${white};

  border: none;
  outline: none;
  cursor: pointer;

  &:hover {
    box-shadow: 6px 6px 10px ${shadowColor},
      -6px -6px 10px ${white};
    transform: scale(.985);
    transition: .25s;
  }

  &:active,
  &:focus {
    box-shadow: 2px 2px 6px ${shadowColor},
      -2px -2px 6px ${white};
    transform: scale(.97);
    transition: .25s;
  }
`

export const PCSwitchButton = styled(Button)`
  background-color: #cdd5e0 !important;
  color: #ffffff !important;
`

export const PCBottomCircle = styled.div<{ isSignIn: boolean }>`
  position: absolute;
  width: 500px;
  height: 500px;
  border-radius: 50%;
  background-color: ${bgColor};
  box-shadow: inset 8px 8px 12px ${shadowColor},
    inset -8px -8px 12px ${white};
  bottom: -60%;
  left: ${props => props.isSignIn ? '-60%' : 'calc(100% - 400px)'};
  transform-origin: ${props => props.isSignIn ? 'unset' : 'left'};
  transition: ${transition};
`

export const PCTopCircle = styled.div<{ isSignIn: boolean }>`
  position: absolute;
  width: 300px;
  height: 300px;
  border-radius: 50%;
  background-color: ${bgColor};
  box-shadow: inset 8px 8px 12px ${shadowColor},
    inset -8px -8px 12px ${white};
  bottom: -60%;
  top: -30%;
  left: ${props => props.isSignIn ? '60%' : 'calc(100% - 400px)'};
  transform-origin: ${props => props.isSignIn ? 'unset' : 'left'};
  transition: ${transition};
`

export const MobileForm = styled.div`
  width: 100%;
  padding: 25px;
  background-color: ${bgColor};
  box-shadow: 10px 10px 10px ${shadowColor},
    -10px -10px 10px ${white};
  border-radius: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
`

export const MobileContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  padding: 25px;

  background-color: ${bgColor};
  transition: ${transition};
`

export const MobileSwitch = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
`

export const MobileDescription = styled.div`
  font-size: 14px;
  letter-spacing: .25px;
  text-align: center;
  line-height: 1.6
`

export const MobileButton = styled.span`
  cursor: pointer;
  display: inline-block;
  margin: 0 3px;
  font-weight: bold
`

export const Title = styled.h2`
  font-size: 34px;
  font-weight: 700;
  line-height: 3;
  color: ${black};
  @media screen and (max-width: 900px) {
    font-size: 20px;
  }
`

export const Description = styled.p`
  font-size: 14px;
  letter-spacing: .25px;
  text-align: center;
  line-height: 1.6;
`

export const Form = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 100%;
  height: 100%;
`

export const Input = styled.input`
  width: 350px;
  height: 40px;
  margin: 4px 0;

  padding-left: 25px;
  font-size: 13px;
  letter-spacing: .15px;
  border: none;
  outline: none;

  background-color: ${bgColor};
  transition: .25s ease;
  border-radius: 8px;

  box-shadow: inset 2px 2px 4px ${shadowColor},
    inset -2px -2px 4px ${white};

  &:focus {
    box-shadow: inset 4px 4px 4px ${shadowColor},
      inset -4px -4px 4px ${white};
  }
`

const textFade = keyframes`
  0% {
    opacity: 0.8;
  }
  50% {
    opacity: 0.1;
  }
  100% {
    opacity: 0.8;
  }
`

export const ErrorTip = styled.p<{show: boolean}>`
  margin-bottom: 5px;
  margin-left: 5px;
  width: 350px;
  font-size: 12px;
  color: #ff6262;
  text-shadow: 0 0 1px #ffa8a8;
  opacity: 0.8;
  animation: 600ms ease-in-out alternate 2;
  animation-name: ${props => props.show ? textFade : 'unset'};
`

export const FormLink = styled(Link)`
  color: ${black};
  font-size: 15px;
  margin-top: 10px;
  border-bottom: 1px solid ${purple};
  line-height: 2;

  &:hover {
    border-bottom: 1px solid ${gray};
  }
`
