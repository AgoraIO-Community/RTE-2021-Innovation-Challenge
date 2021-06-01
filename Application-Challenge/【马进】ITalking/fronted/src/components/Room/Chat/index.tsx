import React, { useEffect, useRef, useState } from 'react'
import * as S from './styles'
import SearchBar from 'common/SearchBar'
import Avatar from 'common/Avatar'
import { MessageOutlined, SmileTwoTone, SmileOutlined } from '@ant-design/icons'
import { getChatId } from 'helpers/id'
import { getCurrentTimeString } from 'helpers/time'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import { ChatState } from 'models/chat'
import 'emoji-mart/css/emoji-mart.css'
import { Picker } from 'emoji-mart'
import { Slide } from 'react-awesome-reveal'
import { Skeleton } from 'antd'

const Chat: React.FC = () => {
  const user = useSelector((state: RootState) => state.UserModel)
  const global = useSelector((state: RootState) => state.GlobalModel)
  const chat = useSelector((state: RootState) => state.ChatModel)
  const $ = useDispatch<Dispatch>()
  const [showPicker, setShowPicker] = useState(false)
  const inputRef = useRef<any>(null)

  const [content, setContent] = useState('')
  const $messagePanel = useRef<HTMLDivElement>(null)

  useEffect(() => {
    if (!$messagePanel.current) {
      return
    }
    $messagePanel.current.scrollTop = $messagePanel.current.scrollHeight
  }, [chat])

  const changeContent = (e: React.FormEvent<HTMLInputElement>) => {
    const { value } = e.currentTarget
    setContent(value)
    setShowPicker(false)
  }

  const sendMessage = async () => {
    if (!content.trim().length) {
      return
    }
    setShowPicker(false)
    setContent('')
    const chat: ChatState = {
      id: getChatId(),
      name: user.name,
      time: getCurrentTimeString(),
      content
    }
    await $.ChatModel.addSync(chat)
  }

  const toggleShowPicker = () => {
    setShowPicker(!showPicker)
  }

  const addEmoji = (emoji: any) => {
    setContent(content + emoji.native)
    inputRef.current.focus()
    setShowPicker(false)
  }

  return (
    <S.Chat>
      <SearchBar/>
      <S.MessagePanel>
        <S.MessageList ref={$messagePanel}>
          {chat.map(chat => (
            <S.MessageItem key={chat.id}>
              <S.MessageSender>
                <S.MessageAvatar>
                  <Avatar name={chat.name} size={30}/>
                </S.MessageAvatar>
              </S.MessageSender>
              <S.MessageArea>
                <S.MessageDetail>
                  <S.MessageName>
                    {chat.name}
                  </S.MessageName>
                  <S.MessageTime>
                    {chat.time}
                  </S.MessageTime>
                </S.MessageDetail>
                <S.MessageContent>
                  {chat.content}
                </S.MessageContent>
              </S.MessageArea>
            </S.MessageItem>
          ))}

        </S.MessageList>
        <Skeleton loading={!global.roomRTMJoined} active paragraph={{ rows: 1 }}>
          <S.MessageBar>
            <S.MessageInput ref={inputRef} size="large" placeholder="说点什么..." prefix={<MessageOutlined/>}
                            suffix={showPicker
                              ? <SmileTwoTone onClick={toggleShowPicker}/>
                              : <SmileOutlined onClick={toggleShowPicker}/>}
                            bordered={false}
                            value={content} onChange={changeContent} onPressEnter={sendMessage}
            />
            <S.PickerContainer>
              {showPicker && (
                <Slide direction={'up'} delay={200} duration={500} cascade>
                  <Picker
                    showPreview={false}
                    showSkinTones={false}
                    onSelect={addEmoji}
                  />
                </Slide>
              )}
            </S.PickerContainer>
          </S.MessageBar>
        </Skeleton>
      </S.MessagePanel>
    </S.Chat>
  )
}

export default Chat
