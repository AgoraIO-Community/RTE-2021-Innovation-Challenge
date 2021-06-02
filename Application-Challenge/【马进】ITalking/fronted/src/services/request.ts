import axios from 'axios'
import Api from 'constants/api'

const request = axios.create({
  timeout: Api.Timeout,
  baseURL: Api.BaseUrl,
  withCredentials: true,
  validateStatus (status) {
    return status >= 100 && status < 600
  }
})

export default request
