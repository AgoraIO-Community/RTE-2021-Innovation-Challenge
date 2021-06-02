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

    this.request = app.session.user.goaskme_convert_gam_requests()
  }

  className() {
    return 'ResultsModal Modal'
  }

  title() {
    return app.translator.trans('fof-username-request.forum.results.title')
  }

  content() {
    if (this.request.status() === 'Approved') {
      return (
        <div className="Modal-body">
          <div className="Form Form--centered">
            <h2>{app.translator.trans('fof-username-request.forum.results.approved')}</h2>
            <h3>{app.translator.trans('fof-username-request.forum.results.new_username', { username: app.session.user.username() })}</h3>
            <div className="Form-group">
              <Button className="Button Button--primary Button--block" onclick={this.hide.bind(this)}>
                {app.translator.trans('fof-username-request.forum.request.dismiss_button')}
              </Button>
            </div>
          </div>
        </div>
      )
    } else {
      return (
        <div className="Modal-body">
          <div className="Form Form--centered">
            <h2>{app.translator.trans('fof-username-request.forum.results.rejected')}</h2>
            <h3>{app.translator.trans('fof-username-request.forum.results.reason', { reason: this.request.reason(), i: <i /> })}</h3>
            <p className="helpText">{app.translator.trans('fof-username-request.forum.results.resubmit')}</p>
            <div className="Form-group">
              <Button className="Button Button--primary Button--block" onclick={this.hide.bind(this)}>
                {app.translator.trans('fof-username-request.forum.request.dismiss_button')}
              </Button>
            </div>
          </div>
        </div>
      )
    }
  }

  onhide() {
    app.session.user.goaskme_convert_gam_requests = m.prop()
    this.request.save({ delete: true })
  }
}
