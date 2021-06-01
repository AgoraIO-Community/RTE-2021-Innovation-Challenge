import { extend } from 'flarum/extend'
import UserCard from 'flarum/components/UserCard'
import UserControls from 'flarum/utils/UserControls'
import Button from 'flarum/components/Button'
//import UserMoneyModal from './components/UserMoneyModal';
import Model from 'flarum/Model'
import User from 'flarum/models/User'

export default function() {
  //边框 fa fa-star-o 全空
  //边框 fa fa-star-half-o 空半星
  // fa fa-star-half 半星
  // fa fa-star 全星
  extend(UserCard.prototype, 'infoItems', function(items) {
    const num = this.props.user.data.attributes['pointsRank']
    let showFull = ['荣誉爆炸值：']
    if (parseInt(parseInt(num) / 2)) {
      showFull.push.apply(showFull, Array(parseInt(parseInt(num) / 2)).fill('fa fa-star'))
    }
    if (parseInt(parseInt(num) % 2)) {
      showFull.push.apply(showFull, Array(parseInt(num) % 2).fill('fa fa-star-half'))
    }
    items.add(
      'points-rate',
      showFull.map(function(item, index, arr) {
        if (index === 0) {
          return item
        }
        return m('i', { class: item + '  lv' + arr.length })
      })
    )
  })

  //管理员编辑 money
  /*
    extend(UserControls, 'moderationControls', function (items, user) {
        if (user.canEditMoney()) {
            items.add('money', Button.component({
                children: app.translator.trans('antoinefr-money.forum.user_controls.money_button'),
                icon: 'fas fa-money-bill',
                onclick: function () {
                    app.modal.show(new UserMoneyModal({ user }));
                }
            }));
        }
    });
    */
}
