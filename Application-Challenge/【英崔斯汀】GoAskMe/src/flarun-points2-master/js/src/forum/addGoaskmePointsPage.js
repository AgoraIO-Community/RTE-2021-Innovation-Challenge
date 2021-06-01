import { extend } from 'flarum/extend'
import UserPage from 'flarum/components/UserPage'
import LinkButton from 'flarum/components/LinkButton'
import addGoaskmePointsUserPage from './components/addGoaskmePointsUserPage'

export default function() {
  app.routes['user.GoAskMePoints'] = { path: '/u/:username/GoAskMePoints', component: addGoaskmePointsUserPage.component() }

  extend(UserPage.prototype, 'navItems', function(items) {
    const href = app.route('user.GoAskMePoints', { username: this.user.username() })
    // Hide links from guests if they are not already on the page
    if (!app.session.user && m.route() !== href) return
    if (app.current.user && app.session.user.data.id !== app.current.user.data.id) return

    items.add(
      'GoAskMePoints',
      LinkButton.component({
        href,
        children: app.translator.trans('goaskme-points.forum.user.dropdown_label'),
        icon: 'fa fa-magic'
      }),
      85
    )
  })
}
