import styled from 'styled-components'
import { Empty } from 'antd'

export const Sidebar = styled.div`
  flex: 0.3;
  margin-left: 40px;
  @media screen and (max-width: 850px) {
    display: none;
  }
`

export const UpcomingRoom = styled.div`
  margin-top: 15px;
  background-color: #f5f8fa;
  border-radius: 20px;
`

export const RoomHeader = styled.div`
  font-size: 16px;
  font-weight: bold;
  padding: 15px 20px;
  border-bottom: #e6ecf0 1px solid;


  svg {
    fill: #333;
    width: 1em;
    height: 1em;
    display: inline-block;
    font-size: 1rem;
    transition: fill 200ms cubic-bezier(0.4, 0, 0.2, 1) 0ms;
    flex-shrink: 0;
    user-select: none;
    margin-left: 5px;
  }
`

export const RoomList = styled.div`
  display: flex;
  flex-direction: column;
`

export const RoomItem = styled.div`
  border-bottom: #e6ecf0 1px solid;
  display: flex;
  flex-direction: column;
  padding: 8px 20px;
  cursor: pointer;
  transition: background-color 200ms cubic-bezier(0.4, 0, 0.2, 1) 0ms;
  position: relative;
  
  &:hover {
    background-color: #e6ecf0;
  }
`

export const RoomTime = styled.div`
  text-transform: uppercase;
  font-size: 14px;
  color: #fd4d4d;
  position: absolute;
  right: 20px;
`

export const RoomName = styled.div`
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  color: #333;
  font-weight: bold;
  margin-bottom: 5px;
`

export const RoomDescription = styled.div`
  color: #5f5e5e;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  margin-bottom: 5px;
`

export const RoomCreator = styled.div`
  display: flex;
`

export const RoomCreatorAvatar = styled.span`
  overflow: hidden;
  display: flex;
  margin-right: 10px;

  img {
    display: inline-block;
    border-radius: 9999px;
    border-color: rgb(21, 26, 33);
    background-color: rgb(21, 26, 33);
  }
`

export const RoomCreatorName = styled.span`
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-size: 14px;
  color: rgb(34 43 56)
`

export const RoomExplore = styled.div`
  font-size: 14px;
  padding: 15px 20px;
  cursor: pointer;
  color: rgb(29, 161, 242);

  svg {
    fill: rgb(29, 161, 242);
    width: 1em;
    height: 1em;
    display: inline-block;
    font-size: 14px;
    transition: fill 200ms cubic-bezier(0.4, 0, 0.2, 1) 0ms;
    flex-shrink: 0;
    user-select: none;
    margin-left: 5px;
  }
`

export const Footer = styled.div`
  margin-top: 20px;
  display: flex;
  cursor: pointer;
  user-select: none;

  a {
    color: rgb(91, 112, 131);
  }
`

export const RoomCard = styled.div`
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 15px 20px;
  border-radius: 8px;
  border: 1px solid rgb(29, 161, 242);
  height: 190px;
  background-color: #f5f8fa;
  transition: all 200ms cubic-bezier(0.4, 0, 0.2, 1) 0ms;
  width: 100%;
  margin-top: 20px;
  justify-content: space-around;
  &:hover {
    box-shadow: 0 0 4px 0px rgb(134 209 255);
  }
`

export const RoomCardHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
`

export const RoomCardCount = styled.div`

  display: flex;
  color: #666;
  align-items: center;
`

export const RoomCardDot = styled.div`
  width: 10px;
  height: 10px;
  border-radius: 50%;
  margin-right: 10px;
  background-color: #fd4d4d;
`
export const RoomCardName = styled.div`
  font-size: 18px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-weight: 700;
  color: #333;
`

export const RoomCardDescription = styled.div`
  margin: 8px 0px;
  font-size: 16px;
  color: #5f5e5e;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  width: 100%;
`

export const RoomCardFooter = styled.div`
  margin-top: 20px;
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: space-between;
`

export const RoomCardBtn = styled.div<{ primary?: boolean }>`
  cursor: pointer;
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 8px;
  background-color: #fff;

  svg {
    fill: ${props => props.primary ? '#fd4d4d' : '#a5a3a3'};
    width: 1em;
    height: 1em;
    display: inline-block;
    font-size: 20px;
    flex-shrink: 0;
    user-select: none;
    transition: all 0.25s ease 0.2s, transform 0.3s cubic-bezier(0.6, 0.2, 0.1, 1) 0.1s;
  }

  &:hover {
    svg {
      transform: scale(1.1);
    }
  }
`

export const EmptyBlock = styled(Empty)`
  margin: 0px;
  padding: 20px 0px;
  color: #999;
  border-bottom: #e6ecf0 1px solid;
`
