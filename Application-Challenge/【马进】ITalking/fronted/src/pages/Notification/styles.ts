import styled from 'styled-components'

export const Notification = styled.div`
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

export const Tabs = styled.div`
  height: 53px;
  display: flex;
  align-items: center;
  border-bottom: 1px solid rgb(235, 238, 240);
`

export const Tab = styled.div<{ active: boolean }>`
  flex: 0.5;
  display: flex;
  justify-content: center;
  align-items: center;
  color: ${props => props.active ? 'rgb(29, 161, 242)' : 'rgb(91, 112, 131)'};
  font-weight: 700;
  font-size: 15px;
  line-height: 20px;
  transition: background-color .2s, color .2s;
  border-bottom-style: solid;
  border-bottom-color: rgb(29, 161, 242);
  border-bottom-width: ${props => props.active ? '2px' : '0px'};
  height: 53px;
  cursor: pointer;

  &:hover {
    background-color: #e8f5fe;
    color: rgb(29, 161, 242);
  }
`

export const NotificationList = styled.div`
  overflow-y: auto;
  margin-bottom: 60px;
  display: flex;
  flex-direction: column;
`

export const NotificationItem = styled.div`
  display: flex;
  padding: 15px 20px;
  margin: 10px;
  cursor: pointer;
  border-radius: 10px;
  background: hsla(0, 0%, 100%, 0.6);
  box-shadow: 0 0.625em 3.75em 0 #eaeaea;
  transition: all 0.25s ease 0.2s, transform 0.3s cubic-bezier(0.6, 0.2, 0.1, 1) 0.1s;
  user-select: none;
  position: relative;
  
  &:not(:first-child) {
    margin-top: 20px;
  }

  &:hover {
    box-shadow: 0 1em 4em 0.5em #d7d7d7;
    transform: translateY(-2px);
  }
`

export const NotificationSender = styled.div`
  display: flex;
  flex-direction: column;
  width: fit-content;
  align-items: center;
  margin: 0px 10px;
`

export const NotificationName = styled.div`
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-size: 16px;
  color: #333;
  margin-top: 8px;
  transition: opacity .2s;
  &:hover {
    opacity: .8;
  }
`

export const NotificationAvatar = styled.div`
  height: 48px;
  width: 48px;
`

export const NotificationContent = styled.div`
  display: flex;
  align-items: center;
  color: rgb(91, 112, 131);
  font-size: 15px;
  margin-left: 10px;
`

export const NotificationTime = styled.div`
  color: #989898;
  font-size: 16px;
  position: absolute;
  right: 20px;
`
