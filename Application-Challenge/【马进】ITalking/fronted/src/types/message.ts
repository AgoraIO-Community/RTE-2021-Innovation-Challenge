interface Message {
  type: RoomMessageType | GlobalMessageType
  data?: any
}

export enum RoomMessageType {
  RequestSpeaking, ConsentSpeaking, StopSpeaking, ResumeSpeaking, DissolveRoom, UpdateRoom, Chat
}

export enum GlobalMessageType {
  RefreshFeed = 100
}

export default Message
