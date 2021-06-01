const Api = {
  Timeout: 1000 * 5,
  BaseUrl: '/v1',
  Entry: {
    SignIn: '/entry/signIn',
    Login: '/entry/login',
    Ping: '/entry/ping',
    Search: '/entry/search'
  },
  User: {
    Info: '/user/info',
    OwnInfo: '/user/ownInfo',
    IsAuthed: '/user/isAuthed',
    Speak: '/user/speak',
    Mute: '/user/mute',
    Token: '/user/token',
    Follow: '/user/follow',
    Upgrade: '/user/upgrade',
    Password: '/user/password',
    Logout: '/user/logout'
  },
  Room: {
    Create: '/room/create',
    Info: '/room/info',
    List: '/room/list',
    Token: '/room/token',
    Leave: '/room/leave',
    Dissolve: '/room/dissolve',
    Speaker: '/room/speaker'
  },
  Notification: {
    Info: '/notification/info'
  },
  Reservation: {
    List: '/reservation/list'
  }
}

export default Api
