import styled from 'styled-components'

export const Menu = styled.menu`
  flex: 0.2;
  margin-top: 20px;
  padding-left: 20px;
  padding-right: 20px;
  @media screen and (max-width: 850px) {
    flex: 0.1;
  }
  @media screen and (max-width: 570px) {
    display: none;
  }
`

export const Banner = styled.div`
  font-size: 30px;
  margin-left: 20px;
  display: flex;
  cursor: pointer;
`

export const Title = styled.h2`
  text-shadow: 0 0 1px #ccc;
  margin-bottom: 0px;
  font-size: 30px;
`

export const MenuList = styled.div`
  margin: 30px 0px;
  position: relative;
`

export const MenuItem = styled.div<{ active: boolean }>`
  display: flex;
  align-items: center;
  cursor: pointer;
  width: 200px;
  border-radius: 30px;
  transition: .2s;
  user-select: none;

  h4 {
    transition: .2s;
    color: ${(props) => props.active ? '#50b7f5' : '#0a1f44'};
  }

  svg {
    transition: .2s;
    fill: ${(props) => props.active ? '#50b7f5' : '#0a1f44'};
  }

  &:hover {
    background-color: #e8f5fe;

    h4 {
      color: #50b7f5;
    }

    svg {
      fill: #50b7f5;
    }
  }
`

export const MenuIcon = styled.div`
  & > span {
    user-select: none;
    padding: 20px;

    svg {
      width: 1em;
      height: 1em;
      display: inline-block;
      font-size: 1.5rem;
      transition: fill 200ms cubic-bezier(0.4, 0, 0.2, 1) 0ms;
      flex-shrink: 0;
    }
  }
`

export const MenuTitle = styled.h4`
  font-weight: 800;
  font-size: 20px;
  margin-right: 20px;
  margin-top: 0px;
  margin-bottom: 0px;
  transition: color 100ms ease-out;
`

export const CreateRoomButton = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgb(29, 161, 242);
  box-shadow: rgb(0 0 0 / 8%) 0px 8px 28px;
  border-radius: 9999px;
  outline-style: none;
  transition-property: background-color, box-shadow;
  transition-duration: 0.2s;
  min-width: calc(77.28px);
  min-height: 48px;
  cursor: pointer;
  padding-left: 32px;
  padding-right: 32px;
  border: 0 solid black;
  box-sizing: border-box;
  font-weight: 700;
  color: rgb(255, 255, 255);
  font-size: 15px;
  line-height: 20px;
  user-select: none;
  width: 200px;

  &:hover {
    background-color: #1a91da;
    box-shadow: rgb(0 0 0 / 10%) 0px 8px 28px;
  }
`

export const UnreadDot = styled.div`
  position: absolute;
  transform: translate(29px, -15px);
`
