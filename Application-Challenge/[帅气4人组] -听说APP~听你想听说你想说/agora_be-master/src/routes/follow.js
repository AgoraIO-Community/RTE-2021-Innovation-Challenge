import Follow from '../schemas/Follow'
import User from '../schemas/user'

import express from 'express'
const router = express.Router()

router.get('/', async (req, res, next) => {
  let { phone } = req.body
  let user = await User.findOne({phone: phone})
  if (!user) return res.json({code: -1, msg: '用户不存在'})

  let userList = await Follow.find({user: user}).limit(10).populate('user').populate('follow_user')
  res.json(userList)
})

router.post('/', async (req, res, next) => {
  let { phone, follow_uid } = req.body
  let user = await User.findOne({phone: phone})
  let follow_user = await User.findOne({_id: follow_uid})

  if (!user) return res.json({code: -1, msg: '用户不存在'})
  if (!follow_user) return res.json({code: -1, msg: '关注的用户不存在'})
  let follow = new Follow({
    user: user,
    follow_user: follow_user,
  })
  follow.save()
  res.json({msg: 'ok'})
})

router.delete('/', async (req, res, next) => {
  let { phone, follow_uid } = req.body
  let user = await User.findOne({phone: phone})
  let follow_user = await User.findOne({_id: follow_uid})

  if (!user) return res.json({code: -1, msg: '用户不存在'})
  if (!follow_user) return res.json({code: -1, msg: '关注的用户不存在'})

  await Follow.remove({
    user: user,
    follow_user: follow_user,
  })
  res.json({msg: 'ok'})
})

export default router
