import { extend } from 'flarum/extend';
import AdminNav from 'flarum/components/AdminNav';
import AdminLinkButton from 'flarum/components/AdminLinkButton';

import SettingsPage from './components/SettingsPage';
import PoinstsList from './models/Pointslist'
export default () => {
    app.routes['goaskme-points-convert'] = { path: '/goaskme/points-convert', component: SettingsPage.component() };

    app.extensionSettings['goaskme-points-convert'] = () => m.route(app.route('goaskme-points-convert'));
    // 列表存储
    app.store.models['points/list'] = PoinstsList;
    
    extend(AdminNav.prototype, 'items', (items) => {
        items.add(
            'goaskme-points-convert',
            AdminLinkButton.component({
                href: app.route('goaskme-points-convert'),
                icon: 'fas fa-gavel',
                children: 'Points Bill',
                description: 'Points convert check here',
            })
        );
    });
};
