/*
 *
 *  This file is part of fof/username-request.
 *
 *  Copyright (c) 2019 FriendsOfFlarum.
 *
 *  For the full copyright and license information, please view the LICENSE.md
 *  file that was distributed with this source code.
 *
 */

import Component from 'flarum/Component'
import LoadingIndicator from 'flarum/components/LoadingIndicator'
import avatar from 'flarum/helpers/avatar'
import username from 'flarum/helpers/username'
import icon from 'flarum/helpers/icon'
import humanTime from 'flarum/helpers/humanTime'
// import ActionModal from './ActionModal'

export default class RequestsList extends Component {
  init() {
    this.loading = false
  }

  view() {
    const requests = app.cache.points_list || []
    //return <div className="NotificationList RequestsList"> 123123123 </div>
    // {owner_id: 1, type: "Convering", amount: -30, current: 5}
    return (
      <div className="NotificationList RequestsList">
        <div className="NotificationList-header">
          <h4 >流水明细(current:{parseInt(app.session.user.data.attributes['pointsCount'])})    </h4>
          
          <h4 >时区：UTC/GMT+08:00 </h4>
        </div>
        <div className="NotificationList-content">
          <table class="NotificationGrid" style="width: 100%;align-items: center;text-align: center;">
            <thead>
              <tr>
                <th class="NotificationGrid-groupToggle">类型</th>
                <th class="NotificationGrid-groupToggle">数量</th>
                <th class="NotificationGrid-groupToggle">流水</th>
                <th class="NotificationGrid-groupToggle">创建时间</th>
              </tr>
            </thead>
            <tbody>
              {requests.length ? (
                requests.map(request => {
                  return (
                    <tr>
                      <td class="NotificationGrid-groupToggle"> {app.translator.trans('goaskme-points.forum.convert_gam_list.'+request.type)}</td>
                      <td class="NotificationGrid-groupToggle"> {request.amount}</td>
                      <td class="NotificationGrid-groupToggle"> {request.current}</td>
                      <td class="NotificationGrid-groupToggle"> {request.created_at}</td>
                    </tr>
                  )
                })
              ) : !this.loading ? (
                <div className="NotificationList-empty">It looks as though there are no Log here.</div>
              ) : (
                <div className="NotificationList-empty">nomal</div>
              )}
            </tbody>
          </table>
        </div>
      </div>
    )
  }

  load() {
    // if (app.cache.points_list) {
    //  return
    // }

    this.loading = true
    m.redraw()

    app.store
      .createRecord('points')
      .save({ username: app.session.user.username() })
      .then(res => {
        const saveArr = []
        if (!Array.isArray(res.data.list)) {
          for (var i in res.data.list) {
            saveArr.push(res.data.list[i])
          }
          app.cache.points_list = saveArr
        } else {
          app.cache.points_list = res.data.list
        }
      })
      .catch(err => {
        console.log(err)
      })
      .then(() => {
        this.loading = false
        m.redraw()
      })
  }
}
