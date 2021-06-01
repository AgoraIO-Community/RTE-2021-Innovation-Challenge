import { useDispatch } from 'react-redux'
import { Dispatch } from 'store'
import { message } from 'antd'

const useUserCard = () => {
  const $ = useDispatch<Dispatch>()
  const view = async (uid: string) => {
    const setSuccess = await $.GlobalModel.setViewedUserAsync(uid)
    if (!setSuccess) {
      message.warn('获取用户信息失败')
      return
    }
    $.ModalModel.openUserInfoModal()
  }
  return { view }
}

export default useUserCard
