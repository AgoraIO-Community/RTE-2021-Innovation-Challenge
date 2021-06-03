import mongoose from 'mongoose'

const FollowSchema = new mongoose.Schema({
  user: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true
  },
  follow_user: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true
  },
  createAt: {
    type: Date,
    default: Date.now()
  },
  updateAt: {
    type: Date,
    default: Date.now()
  }
})
const Follow = mongoose.model('Follow', FollowSchema)

export default Follow
