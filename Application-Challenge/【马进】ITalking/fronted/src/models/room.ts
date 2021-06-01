import { createModel } from '@rematch/core'
import { RootModel } from 'models/index'
import { DefaultUser, User } from './user'
import RoomApi from 'services/room'
import { isFailedResponse } from 'helpers/http'
import { filterRoom } from 'helpers/filter'
import { formatHHMMString } from 'helpers/time'

export enum RoomType {
  Public= 'public', Private = 'private'
}

export interface Room {
  name: string
  id: string
  description?: string
  announcement?: string
  type: RoomType
  edges?: {
    creator?: User
    speakers?: User[]
    spectators?: User[]
  }
  creator?: User
  speakers: User[]
  spectators: User[]
  // Data in memory
  applicants: string[]
  duration: number
  time: string
  playing: boolean
  volume: number
}

export const DefaultRoom: Room = {
  id: '',
  name: '',
  type: RoomType.Public,
  edges: {
    creator: DefaultUser,
    speakers: [],
    spectators: []
  },
  speakers: [],
  spectators: [],
  applicants: [],
  duration: 0,
  time: '00:00',
  playing: true,
  volume: 100
}

const state: Room = DefaultRoom

let timer: NodeJS.Timer

const RoomModel = createModel<RootModel>()({
  state,
  reducers: {
    stopSpeaking (state, memberId: string) {
      state.speakers = state.speakers.map(speaker => {
        if (speaker.id === memberId) {
          return ({ ...speaker, speaking: false })
        }
        return speaker
      })
      return state
    },
    resumeSpeaking (state, memberId: string) {
      state.speakers = state.speakers.map(speaker => {
        if (speaker.id === memberId) {
          return ({ ...speaker, speaking: true })
        }
        return speaker
      })
      return state
    },
    removeMember (state, memberId: string) {
      state.applicants = state.applicants.filter(id => id !== memberId)
      state.spectators = state.spectators.filter(member => member.id !== memberId)
      state.speakers = state.speakers.filter(member => member.id !== memberId)
      return state
    },
    addApplicant (state, memberId: string) {
      if (!state.applicants.includes(memberId)) {
        state.applicants.push(memberId)
      }
      return state
    },
    removeApplicant (state, memberId: string) {
      state.applicants = state.applicants.filter(id => id !== memberId)
      return state
    },
    tick (state) {
      state.duration += 1
      state.time = formatHHMMString(state.duration)
      return state
    },
    resetRoom () {
      clearInterval(timer)
      return DefaultRoom
    },
    setRoom (state, data: Room) {
      return { ...state, ...filterRoom(data) }
    },
    togglePlaying (state) {
      if (state.playing) {
        window?.rtcApi?.mute()
      } else {
        window?.rtcApi?.play()
      }
      state.playing = !state.playing
      return state
    }
  },
  effects: dispatch => ({
    async setAsync (roomId: string) {
      const response = await RoomApi.GetInfoAndJoin(roomId)
      if (isFailedResponse(response)) {
        return false
      }
      const { data: roomData } = response.data
      dispatch.RoomModel.setRoom(roomData)
      dispatch.RoomModel.startTick()
      return true
    },
    async updateAsync (payload: { id: string, name: string, description?: string, announcement?: string, type: RoomType }) {
      const { id, ...rest } = payload
      const response = await RoomApi.UpdateInfo(rest)
      if (isFailedResponse(response)) {
        return false
      }
      await dispatch.RoomModel.setAsync(payload.id)
      return true
    },
    startTick () {
      clearInterval(timer)
      timer = setInterval(() => {
        dispatch.RoomModel.tick()
      }, 1e3)
    }
  })

})

export default RoomModel
