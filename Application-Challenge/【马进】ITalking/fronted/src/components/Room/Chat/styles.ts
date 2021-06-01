import styled from 'styled-components'
import { Input } from 'antd'

export const Chat = styled.div`
  flex: 0.3;
  margin-left: 40px;
  @media screen and (max-width: 850px) {
    display: none;
  }
`

export const MessagePanel = styled.div`
  height: calc(100vh - 136px);
  background-color: hsl(0deg 0% 93% / 60%);

  font-size: 18px;
  margin-top: 10px;
  border-radius: 10px;
  padding: 30px 30px 20px 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`

export const MessageList = styled.div`
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
  &::-webkit-scrollbar {
    display: none;
  }
`

export const MessageItem = styled.div`
  display: flex;
  margin-bottom: 20px;
`

export const MessageSender = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 10px;
`

export const MessageAvatar = styled.div`
  width: 30px;
  height: 30px;
  box-shadow: 0 0.625em 3.75em 0 #eaeaea;
  cursor: pointer;
`

export const MessageArea = styled.div`
  display: flex;
  flex-direction: column;
`

export const MessageDetail = styled.div`
  display: flex;
  justify-content: space-between;
  padding-right: 5px;
  margin-bottom: 5px;
`

export const MessageName = styled.div`
  font-size: 14px;
  color: #999;
`

export const MessageTime = styled.div`
  font-size: 14px;
  margin-left: 10px;
  color: #ccc
`

export const MessageContent = styled.div`
  padding: 10px 15px;
  width: fit-content;
  max-width: 335px;
  border-radius: 0px 20px 20px 20px;
  line-height: 1.5;
  font-size: 16px;
  white-space: pre-wrap;
  word-wrap: break-word;
  overflow-wrap: break-word;
  overflow: hidden;
  color: #333;
  background: hsla(0, 0%, 100%, 0.4);
  transition: all 0.25s ease 0.2s, transform 0.3s cubic-bezier(0.6, 0.2, 0.1, 1) 0.1s;

  &:hover {
    transform: translateY(-1px);
  }
`

export const MessageBar = styled.div`
  display: flex;
  align-items: center;
  background: #fff;
  padding: 10px;
  border-radius: 10px;
  margin-top: 10px;
  height: 45px;
`

export const MessageInput = styled(Input)`
  border: none;
  background-color: #fff;

  &::placeholder {
    color: rgba(117, 117, 117, 0.72) !important;
  }

  svg {
    fill: rgba(117, 117, 117, 0.72);
    width: 1em;
    height: 1em;
    display: inline-block;
    font-size: 1rem;
    transition: fill 200ms cubic-bezier(0.4, 0, 0.2, 1) 0ms;
    flex-shrink: 0;
    user-select: none;
    margin-right: 5px;
  }
  .ant-input-suffix {
    svg {
      fill: rgb(29, 161, 242);
      cursor: pointer;
    }
  }
`

export const PickerContainer = styled.div`
  position: fixed;
  bottom: 144px;
  z-index: 2;
  overflow: hidden;
  border-radius: 8px;
  
  .emoji-mart {
    border-width: 0px;
    background: #fff;
    border-radius: 8px;
  }
`
