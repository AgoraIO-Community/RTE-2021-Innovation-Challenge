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

import Alert from 'flarum/components/Alert'
import Modal from 'flarum/components/Modal'
import Button from 'flarum/components/Button'

export default class FlagPostModal extends Modal {
  init() {
    super.init()

    this.username = m.prop(app.session.user.username())

    this.success = false

    // this.password = m.prop('')
    // 当前积分数量
    this.pointsNumber = parseInt(app.session.user.data.attributes['pointsCount'])

    this.pointsConverLimit = parseInt(this.pointsNumber / 5)

    this.pointsConverNumber = m.prop(0)
    this.pointsCount = m.prop("")
  }

  className() {
    return 'RequestPointsConvert Modal--small'
  }

  title() {
    return app.translator.trans('goaskme-points.forum.convert_gam.title')
  }

  content() {
    if (this.success) {
      return (
        <div className="Modal-body">
          <div className="Form Form--centered">
            <p className="helpText" style="text-align: center;">{app.translator.trans('goaskme-points.forum.convert_gam.confirmation_message')}</p>
            <div className="Form-group">
              <Button className="Button Button--primary Button--block" onclick={this.hide.bind(this)}>
                {app.translator.trans('goaskme-points.forum.request.dismiss')}
              </Button>
            </div>
          </div>
        </div>
      )
    }

    return (
      <div className="Modal-body">
        <div className="Form Form--centered">
          <div className="Form-group">
            {app.translator.trans('goaskme-points.forum.convert_gam.points_show')}
            {this.pointsNumber}
          </div>
          <div className="Form-group">{app.translator.trans('goaskme-points.forum.convert_gam.points_ratio')}</div>
          <div className="Form-group">
            {app.translator.trans('goaskme-points.forum.convert_gam.points_max_convert')}
            {this.pointsConverLimit}
          </div>
          <div className="Form-group">
            <input
              type="number"
              name="number"
              className="FormControl"
              placeholder={app.translator.trans('goaskme-points.forum.convert_gam.convert_num')}
              min="1"
              max={this.pointsConverLimit}
              bidi={this.pointsConverNumber}
              disabled={this.loading}
              style="text-align: center;"
            />
          </div>
          <div className="Form-group">
            <input
              type="text"
              name="text"
              minlength="4"
              className="FormControl" 
              placeholder={app.translator.trans('goaskme-points.forum.convert_gam.address')}
              bidi={this.pointsCount}
              disabled={this.loading}
            />
          </div>
          <div className="Form-group">
            {Button.component({
              className: 'Button Button--primary Button--block',
              type: 'submit',
              loading: this.loading,
              disabled: this.loading || this.pointsConverLimit === 0,
              children: app.translator.trans('goaskme-points.forum.request.submit')
            })}
          </div>
        </div>
      </div>
    )
  }

  onsubmit(e) {
    e.preventDefault()

    this.alert = null

    // if (this.username() === app.session.user.username()) {
    //   this.hide()
    //   return
    // }

    this.loading = true

    app.store
      .createRecord('goaskme_convert_gam_requests')
      // .save(
      //  { username: this.username() },
      //  {
      //    meta: { password: this.password() },
      //    errorHandler: this.onerror.bind(this)
      //  }
      //)
      .save({
        pointsConverNumber: this.pointsConverNumber,
        pointsCount: this.pointsCount, errorHandler: this.onerror.bind(this)
      })
      .then(request => {
        app.session.user.data.attributes['pointsCount'] = request.data.attributes['pointsCount']
        app.session.user.goaskme_convert_gam_requests = m.prop(request)
        this.success = true
      })
      .catch(() => { })
      .then(this.loaded.bind(this))
  }

  onerror(error) {
    if (error.status === 500) {
      error.alert.props.children = app.translator.trans('goaskme-points.forum.request.invalid_Argument')
    }

    super.onerror(error)
  }
}
