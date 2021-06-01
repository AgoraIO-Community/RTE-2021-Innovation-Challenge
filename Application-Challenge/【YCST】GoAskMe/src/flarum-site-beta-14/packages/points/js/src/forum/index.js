import User from 'flarum/models/User'
import Model from 'flarum/Model'
// 钱包主页面
import addGoaskmePointsPage from './addGoaskmePointsPage'
// 右上角 钱包按钮
import addGoaskmePointsSessionDropdown from './addGoaskmePointsSessionDropdown'
// 个人页面 显示爆炸值
import addPointsMoney from './addPointsMoney'
// 钱包兑换显示页面
import RequestsPage from './components/RequestsPage'
import PointsPost from './models/PointsPost'

// 钱包列表
import PointsRequest from './models/PointsRequest'
// import Discussion from 'flarum/models/Discussion';
app.initializers.add('goaskme-flarum-points', () => {
  app.store.models['goaskme_convert_gam_requests'] = PointsPost
  app.store.models['points'] = PointsRequest
  User.prototype.goaskme_convert_gam_requests = Model.hasOne('goaskme_convert_gam_requestss')

  // 注册 兑换页面路由 及响应pag
  app.routes.goaskme_convert_gam = { path: '/goaskme_convert_gam', component: <RequestsPage /> }

  addGoaskmePointsPage()
  addGoaskmePointsSessionDropdown()
  addPointsMoney()
})
