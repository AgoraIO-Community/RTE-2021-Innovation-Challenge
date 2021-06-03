import mongoose from 'mongoose'

const UserSchema = new mongoose.Schema({
  name: String,
  uid: String,
  phone: String,
  password: String,
  avatar: String,
  createAt: {
    type: Date,
    default: Date.now()
  },
  updateAt: {
    type: Date,
    default: Date.now()
  }
})
const User = mongoose.model('User', UserSchema)

export default User
