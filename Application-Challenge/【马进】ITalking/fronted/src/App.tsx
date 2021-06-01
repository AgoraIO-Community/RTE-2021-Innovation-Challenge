import React, { useEffect, useLayoutEffect, useState } from 'react'
import { Route, Switch, useHistory } from 'react-router-dom'
import { authedRoutes, externalRoutes } from 'router'
import GlobalStyles from 'globalStyles'
import { ErrorBoundary } from 'components/ErrorBoundary'
import Fallback from 'components/Fallback'
import { message } from 'antd'
import UserApi from 'services/user'
import Modal from 'components/Modals'
import useGlobalRTM from 'hooks/useGlobalRTM'
import useRoomRTM from 'hooks/useRoomRTM'
import useRTC from 'hooks/useRTC'
import RouteMap from 'types/route'
import useUserStatus from 'hooks/useUserStatus'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'

const App: React.FC = () => {
  const [isAuthed, setIsAuthed] = useState<boolean>()
  const history = useHistory()
  const user = useSelector((state: RootState) => state.UserModel)
  const $ = useDispatch<Dispatch>()

  useLayoutEffect(() => {
    (async () => {
      const _isAuthed = await UserApi.IsAuthed()
      setIsAuthed(_isAuthed)
    })()
  }, [])

  useUserStatus()
  window.globalRTMApi = useGlobalRTM()

  window.roomRTMApi = useRoomRTM()
  window.rtcApi = useRTC()

  useEffect(() => {
    if (!isAuthed || user.id) {
      return
    }
    (async () => {
      const [setUserSuccess] = await Promise.all([
        $.UserModel.setAsync(),
        $.ReservationModel.setAsync()
      ])
      if (!setUserSuccess) {
        message.warn('获取用户信息失败，请重新登录')
        history.push(RouteMap.Entry)
      }
    })()
  }, [isAuthed])

  if (typeof isAuthed === 'undefined') {
    return <></>
  }

  return (
    <ErrorBoundary FallbackComponent={Fallback} >
      <GlobalStyles/>
      <Switch>
        {authedRoutes.map((route) => (
          <Route
            key={route.component.name}
            path={route.path}
            exact={route.exact}
            render={() => {
              if (isAuthed) {
                return <route.component/>
              }
              history.push(RouteMap.Landing)
            }}
          />
        ))}
        {externalRoutes.map((route) => (
          <Route
            key={route.component.name}
            path={route.path}
            exact={route.exact}
            render={() => {
              if (!isAuthed) {
                return <route.component/>
              }
              history.push(RouteMap.Feed)
            }}
          />
        ))}
      </Switch>
      <Modal/>
    </ErrorBoundary>
  )
}

message.config({
  duration: 2,
  maxCount: 2
})

export default App
