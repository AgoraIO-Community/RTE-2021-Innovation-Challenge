import Component from 'flarum/Component';
import Button from 'flarum/components/Button';
import Alert from 'flarum/components/Alert'

// import UnbanIPModal from '../../common/components/UnbanIPModal';
// import ChangeReasonModal from './ChangeReasonModal';

export default class SettingsPageItem extends Component {
    init() {
        this.item = this.props.bannedIP;


        let alert;
        this.viewButton = Button.component({
            className: 'Button Button--link',
            children: '确定',
            onclick: () => {
                // m.route(app.route.post(post));
                app.alerts.dismiss(alert);
            },
        });

    }
    /**
     * owner_id: 1
     * type: "Convering"
     * amount: -2500
     * current: 3129661
     * created_at: "2020-08-12 15:30:43"
     * extra: "sdfshjwlkjehfilgflerbvhjer" */
    view() {
        return (
            <tr className="PermissionGrid-child">
                <td>{this.item.owner.username}</td>
                <td>{this.item.type}</td>
                <td>{this.item.amount}</td>
                <td>{this.item.current}</td>
                <td>{this.item.extra}</td>
                <td>{this.item.created_at}</td>
                <td>
                    <div className="Button--group">
                        {Button.component({
                            className: 'Button Button--warning',
                            icon: 'fas fa-check',
                            onclick: () => { this.doCheck('converDown') }
                        })}
                        {Button.component({
                            className: 'Button Button--danger',
                            icon: 'fas fa-times',
                            onclick: () => { this.doCheck('converReject') }
                        })}
                    </div>
                </td>
            </tr>
        );
    }

    doCheck(type) {
        m.request({
            method: "GET",
            url: "/api/points/check",
            data: { id: this.item.id, type },
        }).then(res => {
            this.succesAlert(res)
            this.item.type = res.type
            m.redraw()
        })
    }

    succesAlert(res) {
        let alert;
        const viewButton = Button.component({
            className: 'Button Button--link',
            children: '确定',
            onclick: () => {
                // m.route(app.route.post(post));
                app.alerts.dismiss(alert);
            },
        });

        app.alerts.show(
            (alert = new Alert({
                type: 'success',
                children: 'change iteam type to ' + res.type ,
                controls: [viewButton],
            }))
        );

    }

    errorAlert(res) {
        let alert;
        const viewButton = Button.component({
            className: 'Button Button--link',
            children: '确定',
            onclick: () => {
                // m.route(app.route.post(post));
                app.alerts.dismiss(alert);
            },
        });

        app.alerts.show(
            (alert = new Alert({
                type: 'error',
                children: JSON.stringify(res),
                controls: [viewButton],
            }))
        );

    }
}
