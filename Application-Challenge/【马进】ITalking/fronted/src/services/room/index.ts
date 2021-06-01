import createRoom from 'services/room/create'
import getRoomInfoAndJoin from 'services/room/getInfoAndJoin'
import getRoomList from 'services/room/getList'
import getRoomToken from 'services/room/token'
import leaveRoom from 'services/room/leave'
import dissolveRoom from 'services/room/dissolve'
import addSpeaker from 'services/room/speaker'
import updateRoom from 'services/room/update'

const RoomApi = {
  Create: createRoom,
  GetInfoAndJoin: getRoomInfoAndJoin,
  GetList: getRoomList,
  GetRTCToken: getRoomToken,
  Leave: leaveRoom,
  Dissolve: dissolveRoom,
  AddSpeaker: addSpeaker,
  UpdateInfo: updateRoom
}

export default RoomApi
