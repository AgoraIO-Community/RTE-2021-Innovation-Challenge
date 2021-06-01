import { extend } from 'flarum/extend';
import AdminNav from 'flarum/components/AdminNav';
import AdminLinkButton from 'flarum/components/AdminLinkButton';

import SettingsPage from './components/SettingsPage';
import PoinstsList from './models/Pointslist'
export default () => {
  // Main page
  app.routes.points = {
    path: '/goaskme/points-convert',
    component: SettingsPage.component()
  };
  console.log(SettingsPage.component);
  console.log(PoinstsList);
  // Quick access settings from extensions tab
  app.extensionSettings['points'] = () => m.route(app.route('points'));
    app.store.models['points/list'] = PoinstsList;
  extend(AdminNav.prototype, 'items', items => {
    items.add(
      'points',
      AdminLinkButton.component({
        href: app.route('points'),
        icon: 'fas fa-gavel',
        description: 'Points convert check here',
      }, 'Points Bill222')
    );
  });




  // app.routes['points'] = { path: '/goaskme/points-convert', component: SettingsPage.component() };
  //   console.log(SettingsPage.component());
  //   app.extensionSettings['points'] = () => m.route(app.route('points'));
  //   // 列表存储
  //   app.store.models['points/list'] = PoinstsList;
  //   extend(AdminNav.prototype, 'items', (items) => {
  //     items.add(
  //       'goaskme-points-convert',
  //       AdminLinkButton.component({
  //         href: app.route('points'),
  //         icon: 'fas fa-gavel',
  //         description: 'Points convert check here',
  //       }, 'Points Bill')
  //     );
  //   });
};
