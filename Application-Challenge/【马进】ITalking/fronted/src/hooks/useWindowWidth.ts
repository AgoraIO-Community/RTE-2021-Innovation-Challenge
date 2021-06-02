import { useEffect, useState } from 'react'
import { debounce } from 'helpers/fn'

function useWindowWidth (delay = 500) {
  const [width, setWidth] = useState(window.innerWidth)

  useEffect(() => {
    const handleResize = debounce(() => setWidth(window.innerWidth), delay)
    window.addEventListener('resize', handleResize)
    return () => {
      window.removeEventListener('resize', handleResize)
    }
  }, [])

  return width
}

export default useWindowWidth
