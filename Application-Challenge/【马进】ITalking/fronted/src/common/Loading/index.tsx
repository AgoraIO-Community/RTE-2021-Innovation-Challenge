import React, { useEffect } from 'react'
import NProgress from 'nprogress'

const Loading: React.FC = () => {
  useEffect(() => {
    NProgress.start()
    return () => {
      NProgress.done()
    }
  }, [])
  return <></>
}

export default Loading
