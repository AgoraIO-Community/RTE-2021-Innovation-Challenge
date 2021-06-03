import User from '../schemas/user'
import Feed from '../schemas/feed'
import Topic from '../schemas/topic'
import axios from 'axios'

import express from 'express'
const router = express.Router()

router.get('/', async (req, res, next) => {
  let url = `https://api.tophub.fun/GetAllType`

  axios.get(url).then(async res => {
    let data = res.data.Data
    for (const key in data) {
      console.log(key)
      let findUserResult = await Topic.findOne({
        name: key
      })
      if (findUserResult) {}
      else {
        let topic = new Topic({
          name: key
        })
        topic.save()
      }
      let items = data[key]
      for (let i = 0; i < items.length; i++) {
        let item = items[i]
        let uname = item.name
        let findUserResult = await User.findOne({
          uid: item.id
        })
        if (findUserResult) {
          continue
        } else {
          let user = new User({
            name: uname,
            img: item.img,
            uid: item.id
          })
          user.save()
        }
      }
    }
  }).then(async () => {
    // 读取feed流
    // let url1 = `https://api.tophub.fun/v2/GetAllInfoGzip?id=1048&page=1&type=pc`
    // let url2 = `https://api.tophub.fun/v2/GetAllInfoGzip?id=1048&page=1&type=pc`

    let users = await User.find()
    users.map(async (user) => {
      let url1 = `https://api.tophub.fun/v2/GetAllInfoGzip?id=${user.uid}&page=0&type=pc`
      let response = await axios.get(url1)
      
      let data = response.data.Data && response.data.Data.data || []
      console.log(data)
      for (let i = 0; i < data.length; i++) {
        let feedInfo = data[i]
        if (!feedInfo.imgUrl) continue
        let findFeedResult = await Feed.findOne({
          fid: feedInfo.id
        })
        if (findFeedResult) continue
        let topic = await Topic.findOne({
          name: feedInfo.TypeName
        })
        let feed = new Feed({
          user: user,
          topic: topic,
          title: feedInfo.Title,
          media: feedInfo.imgUrl,
          fid: feedInfo.id
        })
        feed.save()
      }
    })
  }) 
})
export default router
