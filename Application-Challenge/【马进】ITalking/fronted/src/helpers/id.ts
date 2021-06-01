import App from 'constants/app'
import { v4 as uuidv4 } from 'uuid'

export const getChatId = () => {
  return App.ChatIdPrefix + uuidv4()
}
