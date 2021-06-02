import React from 'react'
import Menu from 'components/Menu'
import Sidebar from 'components/Sidebar'
import * as S from './styles'
import { Route, Switch } from 'react-router-dom'
import { innerRoutes } from 'router'

const Home: React.FC = () => {
  return (
    <S.Home>
      <Menu/>
      <Switch>
        {innerRoutes.map((route) => (
          <Route
            key={route.component.name}
            path={route.path}
            exact={route.exact}
            component={route.component}
          />
        ))}
      </Switch>
      <Sidebar/>
    </S.Home>
  )
}

export default Home
