import { useEffect, useState } from 'react'

const useDebounce = <T>(
  value: T,
  { delay = 100, filter = v => v }: { delay?: number, filter?: (v: T) => T }
) => {
  const [debouncedValue, setDebouncedValue] = useState<T>(value)
  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(filter(value))
    }, delay)
    return () => {
      clearTimeout(handler)
    }
  }, [value])
  return debouncedValue
}

export default useDebounce
