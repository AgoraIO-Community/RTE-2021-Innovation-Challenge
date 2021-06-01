const workboxVersion = '5.1.4'
importScripts(
	`https://cdn.jsdelivr.net/npm/workbox-sw@${workboxVersion}/build/workbox-sw.min.js`
)
const cacheSuffixVersion = '1619362196650'
const maxEntries = 100

workbox.core.setCacheNameDetails({
  prefix: 'ITalking',
  suffix: cacheSuffixVersion
})

workbox.core.skipWaiting()
workbox.core.clientsClaim()

workbox.routing.registerRoute(
  /^https:\/\/italking.tomotoes.com\/static\/css\/.*/,
  new workbox.strategies.CacheFirst({
    cacheName: 'css-cache',
    plugins: [
      new workbox.expiration.ExpirationPlugin({
        maxEntries,
        maxAgeSeconds: 30 * 24 * 60 * 60,
        purgeOnQuotaError: true
      }),
      new workbox.cacheableResponse.CacheableResponsePlugin({
        statuses: [0, 200]
      })
    ]
  })
)

workbox.routing.registerRoute(
  /^https:\/\/italking.tomotoes.com\/static\/js\/.*/,
  new workbox.strategies.CacheFirst({
    cacheName: 'js-cache',
    plugins: [
      new workbox.expiration.ExpirationPlugin({
        maxEntries,
        maxAgeSeconds: 30 * 24 * 60 * 60,
        purgeOnQuotaError: true
      }),
      new workbox.cacheableResponse.CacheableResponsePlugin({
        statuses: [0, 200]
      })
    ]
  })
)

workbox.routing.registerRoute(
  /.*\.(?:png|jpg|jpeg|svg|gif|cur|ico|icon|webp)/,
  new workbox.strategies.StaleWhileRevalidate({
    cacheName: `image-cache-${cacheSuffixVersion}`,
    plugins: [
      new workbox.expiration.ExpirationPlugin({
        maxEntries: 10,
        purgeOnQuotaError: true
      })
    ]
  })
)

workbox.routing.registerRoute(
  /.*\.(?:woff|woff2|eot|ttf|otf)/,
  new workbox.strategies.CacheFirst({
    cacheName: `font-cache-${cacheSuffixVersion}`,
    plugins: [
      new workbox.expiration.ExpirationPlugin({
        maxEntries,
        maxAgeSeconds: 30 * 24 * 60 * 60,
        purgeOnQuotaError: true
      }),
      new workbox.cacheableResponse.CacheableResponsePlugin({
        statuses: [0, 200]
      })
    ]
  })
)

workbox.routing.registerRoute(
  /^https:\/\/www.googletagmanager.com\/gtag/,
  new workbox.strategies.NetworkOnly()
)

workbox.routing.registerRoute(
  /.*agora.*/,
  new workbox.strategies.NetworkOnly()
)

workbox.routing.registerRoute(
  /^https:\/\/api.tomotoes.com\/.*/,
  new workbox.strategies.NetworkOnly()
)

workbox.routing.setDefaultHandler(
  new workbox.strategies.NetworkOnly()
)
