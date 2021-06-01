import React from 'react'
import { FallbackProps } from 'components/ErrorBoundary'
import { Button, Result } from 'antd'
import App from 'constants/app'

const Fallback: React.FC<FallbackProps> = props => {
  const { error } = props
  const feedback = () => {
    window.open(App.IssuesUrl, '_blank')
  }
  return <Result
    status="warning"
    title="There are some problems with your operation."
    subTitle={`error: ${error.message}`}
    extra={
      <Button type="primary" onClick={feedback}>
        Problem Feedback
      </Button>
    }
  />
}

export default Fallback
