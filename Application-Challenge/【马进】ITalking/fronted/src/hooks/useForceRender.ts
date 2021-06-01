import {useCallback, useState} from 'react'

const useForceRender: () => void = () => {
  const [, dispatch] = useState<{}>(Object.create(null))

  return useCallback(
    (): void => {
      dispatch(Object.create(null))
    },
    [dispatch]
  )
}

export default useForceRender
