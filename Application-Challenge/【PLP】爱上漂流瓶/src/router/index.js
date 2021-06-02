import Vue from 'vue'
import VueRouter from 'vue-router'
import routes from './routes'
import Home from '../views/home/index.vue'

Vue.use(VueRouter)

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [{
      path: '/',
      name: 'home',
      component: Home
    }].concat(routes)
})

router.onError((error) => {
  const pattern = /Loading chunk (\w)+ failed/g;
  const isChunkLoadFailed = error.message.match(pattern);
  const targetPath = router.history.pending.fullPath;
  if (isChunkLoadFailed) {
     router.replace(targetPath);
  }
})

export default router
