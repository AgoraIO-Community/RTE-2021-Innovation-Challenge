import { init, RematchDispatch, RematchRootState } from '@rematch/core'
import { models, RootModel } from 'models'
import immerPlugin from '@rematch/immer'

const store = init<RootModel>({
  models,
  plugins: [immerPlugin()],
  redux: {
    devtoolOptions: {
      disabled: false
    }
  }
})

export type Store = typeof store
export type Dispatch = RematchDispatch<RootModel>
export type RootState = RematchRootState<RootModel>

export default store
