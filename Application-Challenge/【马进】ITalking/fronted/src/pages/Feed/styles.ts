import styled from 'styled-components'
import { Dropdown, Menu } from 'antd'

export const Feed = styled.div`
  flex: 0.5;
  min-width: fit-content;
  overflow-y: auto;
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
  font-size: 18px;
  &::-webkit-scrollbar {
    display: none;
  }
  @media screen and (max-width: 850px) {
    flex: 0.9;
  }
  @media screen and (max-width: 570px) {
    flex: 1;
  }
`

export const DropdownMenu = styled(Dropdown)`
  color: #0a1f44;
  font-size: 16px;
  svg {
    fill: #0a1f44;
    width: 1em;
    height: 1em;
    display: inline-block;
    font-size: 15px;
    transition: fill 200ms cubic-bezier(0.4, 0, 0.2, 1) 0ms;
    flex-shrink: 0;
    margin-left: 5px;
  }
  
  &:hover {
    color: unset;
  }
`

export const FeedListContainer = styled.div`
  height: calc(100vh - 100px);
`

export const FeedList = styled.div`
  margin-bottom: 60px;
  display: flex;
  flex-direction: column;
`

export const FeedItem = styled.div`
  display: flex;
  flex-direction: column;
  padding: 20px;
  margin: 10px;
  cursor: pointer;
  border-radius: 10px;
  background: hsla(0, 0%, 100%, 0.6);
  box-shadow: 0 0.625em 3.75em 0 #eaeaea;
  transition: all 0.25s ease 0.2s, transform 0.3s cubic-bezier(0.6, 0.2, 0.1, 1) 0.1s;
  user-select: none;
  
  &:not(:first-child) {
    margin-top: 20px;
  }

  &:hover {
    box-shadow: 0 1em 4em 0.5em #d7d7d7;
    transform: translateY(-2px);
  }
`

export const FeedItemHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
`

export const FeedName = styled.div`
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
`

export const FeedMembersCount = styled.div`
  display: flex;  
  color: #666;
  align-items: center;
`

export const FeedDot = styled.div`
  width: 10px;
  height: 10px;
  border-radius: 50%;
  margin-right: 10px;
  background-color: #fd4d4d;
`

export const FeedDescription = styled.div`
  font-size: 15px;
  color: #5f5e5e;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  width: 100%;
  margin-bottom: 8px;
`

export const MenuItem = styled(Menu.Item)<{actived: boolean}>`
  background-color: ${props => props.actived ? '#f5f5f5' : 'unset'};
`
