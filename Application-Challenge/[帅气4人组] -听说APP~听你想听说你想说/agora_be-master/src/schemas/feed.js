import mongoose from 'mongoose'
import User from './user'
const FeedSchema = new mongoose.Schema({
  user: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true
  }, // refer to User
  topic: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Topic'
  },
  fid: String,
  title: String,
  media: String, // image or video
  createAt: {
    type: Date,
    default: Date.now()
  },
  updateAt: {
    type: Date,
    default: Date.now()
  }
})
const Feed = mongoose.model('Feed', FeedSchema)

export default Feed
