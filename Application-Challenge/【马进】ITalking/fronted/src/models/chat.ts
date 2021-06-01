import { createModel } from '@rematch/core'
import { RootModel } from './index'
import { RoomMessageType } from 'types/message'

export interface ChatState {
  id: string
  name: string
  time: string
  content: string
}

const state: ChatState[] = []

const ChatModel = createModel<RootModel>()({
  state,
  reducers: {
    add (state, payload: ChatState) {
      state.push(payload)
      return state
    },
    clear () {
      return []
    }
  },
  effects: dispatch => ({
    async addSync (payload: ChatState) {
      dispatch.ChatModel.add(payload)
      await window.roomRTMApi?.sendMessage({
        type: RoomMessageType.Chat,
        data: payload
      })
    }
  })
})

export default ChatModel
