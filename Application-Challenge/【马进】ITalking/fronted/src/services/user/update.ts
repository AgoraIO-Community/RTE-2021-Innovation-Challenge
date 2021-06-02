import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'

type UpdateUserInfo = (props: {name: string, description: string}) => Promise<AxiosResponse<HTTPResponse>>

const updateUserInfo: UpdateUserInfo = async props => {
  return await request.post<HTTPResponse>(
    Api.User.OwnInfo,
    props
  )
}

export default updateUserInfo
