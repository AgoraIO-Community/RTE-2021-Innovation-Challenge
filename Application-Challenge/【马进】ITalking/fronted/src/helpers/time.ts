import { format, formatDistance, formatRelative } from 'date-fns'
import { zhCN } from 'date-fns/locale'
import { ReservationType } from 'models/reservation'
import moment from 'moment'

export const getCurrentTimeString = () => {
  const date = new Date()
  return `${date.getHours().toString().padStart(2, '0')} : ${date.getMinutes().toString().padStart(2, '0')}`
}

export const formatHHMMString = (duration: number) => {
  const date = new Date(0)
  date.setSeconds(duration)
  return date.toISOString().substr(14, 5)
}

export const formatRelativeTime = (time: string) => {
  const date = new Date(time)
  const now = new Date()
  const formattedDate = formatDistance(date, now, { addSuffix: true, locale: zhCN })

  if (formattedDate.includes('秒') || formattedDate.includes('分钟')) {
    return formattedDate
  }

  if (formattedDate.includes('小时') || formattedDate.includes('昨天')) {
    return formatRelative(date, now, { locale: zhCN })
  }

  if (formattedDate.includes('天')) {
    return formattedDate
  }

  return format(date, 'yyyy-MM-dd', { locale: zhCN })
}

const makeTime = (timeStamp: number) =>
  new Date(timeStamp).toISOString().replace(/[-:]|\.\d{3}/g, '')

interface Query { [key: string]: null | boolean | number | string }

const makeUrl = (base: string, query: Query) =>
  Object.keys(query).reduce((accum, key, index) => {
    const value = query[key]

    if (value !== null) {
      return `${accum}${index === 0 ? '?' : '&'}${key}=${encodeURIComponent(
        value
      )}`
    }
    return accum
  }, base)

export const makeGoogleCalendarUrl = (reservation: ReservationType) =>
  makeUrl('https://calendar.google.com/calendar/render', {
    action: 'TEMPLATE',
    dates: `${makeTime(reservation.time * 1000)}/${makeTime(moment(reservation.time * 1000).add(1, 'day').unix() * 1000)}`,
    location: 'https://italking.tomotoes.com/home',
    text: reservation.name,
    ...(reservation.description ? { details: reservation.description } : {})
  })
