import { lazy } from 'react'
import RouteMap from 'types/route'

const Landing = lazy(async () => await import('../pages/Landing'))
const Entry = lazy(async () => await import('../pages/Entry'))
const Room = lazy(async () => await import('../pages/Room'))
const NotFound = lazy(async () => await import('../pages/NotFound'))
const Home = lazy(async () => await import('../pages/Home'))

const Feed = lazy(async () => await import('../pages/Feed'))
const Reservation = lazy(async () => await import('../pages/Reservation'))
const Notification = lazy(async () => await import('../pages/Notification'))
const Friend = lazy(async () => await import('../pages/Friend'))
const Setting = lazy(async () => await import('../pages/Setting'))

export const HomeRouteList = [RouteMap.Feed, RouteMap.Reservation, RouteMap.Notification, RouteMap.Friend, RouteMap.Setting]

export const externalRoutes = [
  {
    path: RouteMap.Landing,
    exact: true,
    component: Landing
  },
  {
    path: RouteMap.Entry,
    exact: true,
    component: Entry
  },
  {
    path: RouteMap.NotFound,
    component: NotFound
  }
]

export const authedRoutes = [
  {
    path: HomeRouteList,
    exact: true,
    component: Home
  },
  {
    path: RouteMap.Room,
    exact: true,
    component: Room
  }
]

export const innerRoutes = [
  {
    path: RouteMap.Feed,
    exact: true,
    component: Feed
  },
  {
    path: RouteMap.Reservation,
    exact: true,
    component: Reservation
  },
  {
    path: RouteMap.Notification,
    exact: true,
    component: Notification
  },
  {
    path: RouteMap.Friend,
    exact: true,
    component: Friend
  },
  {
    path: RouteMap.Setting,
    exact: true,
    component: Setting
  }
]
