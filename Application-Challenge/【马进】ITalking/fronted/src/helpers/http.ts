import { AxiosResponse } from 'axios'
import { HTTPResponse } from 'types/http'

type IsFailedResponse = (response: AxiosResponse<HTTPResponse>) => boolean
export const isFailedResponse: IsFailedResponse = response => {
  return response.status >= 300 || (response?.data?.type ?? '').toLocaleLowerCase() !== 'success'
}
