import React from 'react'
import { SettingTwoTone } from '@ant-design/icons'
import * as S from './styles'
import FixHeader from 'common/FixHeader'
import { Button, Dropdown, Menu, message } from 'antd'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import UserApi from 'services/user'
import RouteMap from 'types/route'
import useRoom from 'hooks/useRoom'
import { UserStatus } from 'models/user'
import { Helmet } from 'react-helmet'
import App from 'constants/app'

const Setting: React.FC = () => {
  const user = useSelector((state: RootState) => state.UserModel)
  const { leaveRoom } = useRoom()
  const $ = useDispatch<Dispatch>()
  const translateToEnglish = () => {
    message.info('正在翻译中...')
  }
  const languageMenu = (
    <Menu>
      <Menu.Item>
        <a target="_blank" rel="noopener noreferrer">
          Chinese
        </a>
      </Menu.Item>
      <Menu.Item>
        <a target="_blank" rel="noopener noreferrer" onClick={translateToEnglish}>
          English
        </a>
      </Menu.Item>
    </Menu>
  )
  const fallback = () => {
    window.open(App.IssuesUrl, '_blank')
  }
  const contact = () => {
    window.open(App.AuthorUrl, '_blank')
  }
  const logout = async () => {
    if (user.status !== UserStatus.Visitor) {
      await leaveRoom(() => {
        (async () => {
          await UserApi.Logout()
          location.pathname = RouteMap.Landing
        })()
      })
      return
    }
    await UserApi.Logout()
    location.pathname = RouteMap.Landing
  }
  return (
    <S.Setting>
      <Helmet>
        <title>设置 / ITalking</title>
      </Helmet>
      <FixHeader title="设置" icon={<SettingTwoTone/>}/>
      <S.Block>
        <S.Title>
          账户设置
        </S.Title>
        <S.Content>
          <S.Item>
            <Button shape="round" onClick={$.ModalModel.openProfileModal}>
              个人资料设置
            </Button>
          </S.Item>
          <S.Item>
            <Button shape="round" onClick={$.ModalModel.openPasswordModal}>
              更改密码
            </Button>
          </S.Item>
        </S.Content>
      </S.Block>
      <S.Block>
        <S.Title>
          应用设置
        </S.Title>
        <S.Content>
          <S.Item>
            <Dropdown overlay={languageMenu} placement="bottomCenter" arrow>
              <Button shape="round">
                更改语言
              </Button>
            </Dropdown>
          </S.Item>
        </S.Content>
      </S.Block>
      <S.Block>
        <S.Title>
          其他
        </S.Title>
        <S.Content>
          <S.Item>
            <Button shape="round" onClick={fallback}>
              反馈问题
            </Button>
            <Button shape="round" onClick={contact}>
              联系作者
            </Button>
          </S.Item>
          <S.Item>
            <Button shape="round" onClick={logout} danger>
              登出账号
            </Button>
          </S.Item>
        </S.Content>
      </S.Block>
    </S.Setting>
  )
}

export default Setting
