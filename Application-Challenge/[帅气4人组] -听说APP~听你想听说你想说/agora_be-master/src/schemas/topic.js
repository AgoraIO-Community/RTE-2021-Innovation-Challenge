import mongoose from 'mongoose'

const TopicSchema = new mongoose.Schema({
  name: String,
  createAt: {
    type: Date,
    default: Date.now()
  },
  updateAt: {
    type: Date,
    default: Date.now()
  }
})
const Topic = mongoose.model('Topic', TopicSchema)

export default Topic
