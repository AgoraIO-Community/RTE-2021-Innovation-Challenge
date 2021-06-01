import { useEffect, useState } from 'react'

type UseDocumentVisibility = () => VisibilityState

const useDocumentVisibility: UseDocumentVisibility = () => {
  const [documentVisibility, setDocumentVisibility] = useState(document.visibilityState)

  const changeVisibility = () => {
    setDocumentVisibility(document.visibilityState)
  }

  useEffect(() => {
    document.addEventListener('visibilitychange', changeVisibility)
    return () => {
      document.removeEventListener('visibilitychange', changeVisibility)
    }
  }, [])

  return documentVisibility
}

export default useDocumentVisibility
