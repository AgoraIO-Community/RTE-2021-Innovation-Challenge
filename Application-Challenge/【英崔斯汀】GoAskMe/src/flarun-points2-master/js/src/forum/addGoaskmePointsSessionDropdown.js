import { extend } from 'flarum/extend';
import LinkButton from 'flarum/components/LinkButton';
import SessionDropdown from 'flarum/components/SessionDropdown';
import IndexPage from 'flarum/components/IndexPage';

export default () => {
    extend(SessionDropdown.prototype, 'items', (items) => {
        const user = app.session.user;
        items.add(
            'GoAskMePoints',
            LinkButton.component({
                icon: 'fa fa-magic',
                children: app.translator.trans('goaskme-points.forum.user.dropdown_label'),
                href: app.route('user.GoAskMePoints', { username: user.username() }),
            }),
            99
        );
    });
    // 缺乏登陆判断
    /*
        extend(IndexPage.prototype, 'navItems', (items) => {
            const user = app.session.user;
            items.add(
                'GoAskMePoints',
                LinkButton.component({
                    icon: 'fa fa-magic',
                    children: app.translator.trans('goaskme-points.forum.user.dropdown_label'),
                    href: app.route('user.GoAskMePoints', { username: user.username() }),
                }),
                75
            );
    
        });
        */
};
