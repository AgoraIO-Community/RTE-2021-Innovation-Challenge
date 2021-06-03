// app.js
import express from 'express'
import path from 'path'
import cookieParser from 'cookie-parser'
import logger from 'morgan'
import indexRouter from './routes/index'
import UserRouter from './routes/user'
import FeedRouter from './routes/feed'
import CrawlerRouter from './routes/crawler'
import TopicRouter from './routes/topic'
import FollowRouter from './routes/follow'

import mongoose from 'mongoose'
mongoose.connect('mongodb://127.0.0.1:27017/tingshuo');

const db = mongoose.connection
db.on('error', console.error.bind(console, 'connection error:'))
db.once('open', () => {
  console.log('we are connected')
})

const app = express()
app.use(logger('dev'))
app.use(express.json())
app.use(express.urlencoded({ extended: false }))
app.use(cookieParser())
app.use(express.static(path.join(__dirname, '../public')))


app.use('/', indexRouter)
app.use('/user', UserRouter)
app.use('/feed', FeedRouter)
app.use('/crawler', CrawlerRouter)
app.use('/topic', TopicRouter)
app.use('/follow', FollowRouter)

export default app
