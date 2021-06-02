import { useEffect, useRef } from 'react'

type UseClickAway = (
  onClickAway: (event: MouseEvent | TouchEvent) => void,
  getTarget: () => any
) => void

const useClickAway: UseClickAway = (onClickAway, getTarget) => {
  const onClickAwayRef = useRef(onClickAway)
  onClickAwayRef.current = onClickAway

  useEffect(() => {
    const handler = (event: any) => {
      const target = getTarget()
      if (!target || target?.contains(event.target)) {
        return
      }
      onClickAwayRef.current(event)
    }

    document.addEventListener('click', handler)

    return () => {
      document.removeEventListener('click', handler)
    }
  }, [getTarget])
}

export default useClickAway
