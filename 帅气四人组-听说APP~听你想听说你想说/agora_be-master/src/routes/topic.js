import Topic from '../schemas/topic'

import express from 'express'
const router = express.Router()

router.get('/', async (req, res, next) => {

  let topics = await Topic.find()
  res.json(topics)
})
export default router