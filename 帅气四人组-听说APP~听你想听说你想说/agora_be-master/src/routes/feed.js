import Feed from '../schemas/feed'

import express from 'express'
const router = express.Router()

router.get('/', async (req, res, next) => {
  let page = req.params.page || 0
  let limit = 10
  let feeds = await Feed.find().skip(page * limit).limit(limit).populate('user').populate('topic')
  res.json(feeds)
})
export default router
