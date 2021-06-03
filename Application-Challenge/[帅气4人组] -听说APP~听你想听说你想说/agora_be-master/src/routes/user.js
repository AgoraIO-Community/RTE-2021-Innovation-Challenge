import User from '../schemas/user'

import express from 'express'
const router = express.Router()

router.post('/register', async (req, res, next) => {
  let name = req.body.name
  if (!name) return res.json({code: -1, msg: '请输入用户昵称'})
  let phone = req.body.phone
  if (!phone) return res.json({code: -1, msg: '请填写手机号'})
  let code = req.body.code
  if (!code || code !== '1234') return res.json({code: -1, msg: '验证码输入错误'})

  let queryedUser = await User.findOne({phone: phone})
  if (queryedUser) return res.json({code: -1, msg: '用户已存在, 请直接登录'})
  let user = new User({
    name: name,
    phone: phone
  })
  user.save()
  res.json({msg: 'ok'})
})

router.post('/login', async (req, res, next) => {
  let { phone, code } = req.body
  if (!code || code !== '1234') return res.json({code: -1, msg: '验证码输入错误'})
  let user = await User.findOne({phone: phone})
  if (!user) return res.json({code: -1, msg: '未找到该用户'})
  res.json(user)
})

export default router
