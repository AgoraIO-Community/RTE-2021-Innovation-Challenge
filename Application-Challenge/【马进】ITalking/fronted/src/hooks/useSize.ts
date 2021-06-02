import { useState, useLayoutEffect, MutableRefObject } from 'react'
import ResizeObserver from 'resize-observer-polyfill'

interface Size { width?: number, height?: number }

type UseSize = (target: MutableRefObject<any>) => Size

const useSize: UseSize = target => {
  const [state, setState] = useState<Size>(() => {
    return {
      width: ((target.current || {}) as HTMLElement).clientWidth,
      height: ((target.current || {}) as HTMLElement).clientHeight
    }
  })

  useLayoutEffect(() => {
    if (!target.current) {
      return () => {}
    }

    const resizeObserver = new ResizeObserver((entries: any[]) => {
      entries.forEach((entry) => {
        setState({
          width: entry.target.clientWidth,
          height: entry.target.clientHeight
        })
      })
    })

    resizeObserver.observe(target.current as HTMLElement)
    return () => {
      resizeObserver.disconnect()
    }
  }, [target])

  return state
}

export default useSize
