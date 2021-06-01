import login from 'services/entry/login'
import signIn from 'services/entry/signIn'
import ping from './ping'
import search from 'services/entry/search'

const EntryApi = {
  Login: login,
  SignIn: signIn,
  Ping: ping,
  Search: search
}

export default EntryApi
