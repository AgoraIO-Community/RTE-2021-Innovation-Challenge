import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { isFailedResponse } from 'helpers/http'

type IsAuthed = () => Promise<boolean>

const isAuthed: IsAuthed = async () => {
  const response = await request.get<HTTPResponse>(
    Api.User.IsAuthed
  )
  return !isFailedResponse(response)
}

export default isAuthed
