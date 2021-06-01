import React, { Suspense } from 'react'
import ReactDOM from 'react-dom'
import 'antd/dist/antd.css'
import 'nprogress/nprogress.css'
import './i18n'
import * as serviceWorker from './serviceWorker'
import App from 'App'
import { BrowserRouter } from 'react-router-dom'
import { Provider } from 'react-redux'
import store from './store'
import Loading from 'common/Loading'
import * as Sentry from '@sentry/react'
import { Integrations } from '@sentry/tracing'
import APP from 'constants/app'

Sentry.init({
  dsn: APP.SentryDSN,
  integrations: [new Integrations.BrowserTracing()],
  tracesSampleRate: 1.0
})

ReactDOM.render(
  <React.StrictMode>
    <Provider store={store}>
      <BrowserRouter>
        <Suspense fallback={<Loading/>}>
          <App/>
        </Suspense>
      </BrowserRouter>
    </Provider>
  </React.StrictMode>,
  document.getElementById('root')
)
// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister()
