import * as React from 'react'

interface FallbackProps {
  error: Error
}

interface ErrorBoundaryProps {
  onError?: (error: Error, info: { componentStack: string }) => void
  FallbackComponent: React.ComponentType<FallbackProps>
}

interface ErrorBoundaryState {
  error: Error | null
}

class ErrorBoundary extends React.Component<ErrorBoundaryProps,
ErrorBoundaryState> {
  static getDerivedStateFromError (error: Error) {
    return { error }
  }

  state: ErrorBoundaryState = { error: null }

  componentDidCatch (error: Error, info: React.ErrorInfo) {
    const { onError } = this.props
    onError?.(error, info)
  }

  render () {
    const { error } = this.state
    const { children, FallbackComponent } = this.props

    if (error !== null) {
      return <FallbackComponent error={error}/>
    }

    return children
  }
}

function withErrorBoundary<P> (
  Component: React.ComponentType<P>,
  errorBoundaryProps: ErrorBoundaryProps
): React.ComponentType<P> {
  const Wrapped: React.ComponentType<P> = props => {
    return (
			<ErrorBoundary {...errorBoundaryProps}>
				<Component {...props} />
			</ErrorBoundary>
    )
  }

  // Format for display in DevTools
  const name = Component.displayName ?? Component.name ?? 'Unknown'
  Wrapped.displayName = `withErrorBoundary(${name})`

  return Wrapped
}

export { ErrorBoundary, withErrorBoundary }
export type { ErrorBoundaryProps, FallbackProps }
