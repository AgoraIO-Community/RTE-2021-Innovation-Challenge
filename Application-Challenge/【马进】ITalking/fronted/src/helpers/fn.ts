import { ArbitraryFunction } from 'types/basic'

type Debounce = (fn: ArbitraryFunction<void>, ms: number) => ArbitraryFunction<void>
export const debounce: Debounce = (fn, ms = 0) => {
  let timeoutId: NodeJS.Timeout
  return function (...args: any[]) {
    clearTimeout(timeoutId)
    timeoutId = setTimeout(() => fn.apply(undefined, args), ms)
  }
}

export const capitalize = ([first, ...rest]: any, lowerRest = false): string =>
  first.toUpperCase() +
  (lowerRest ? rest.join('').toLowerCase() : rest.join(''))

export const once = (fn: ArbitraryFunction<void>) => {
  let called = false
  return function (...args: any) {
    if (called) return
    called = true
    return fn.apply(undefined, args)
  }
}
