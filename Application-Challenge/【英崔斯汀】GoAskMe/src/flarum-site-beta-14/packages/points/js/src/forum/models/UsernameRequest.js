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
import Model from 'flarum/Model'
import mixin from 'flarum/utils/mixin'

export default class UsernameRequest extends mixin(Model, {
  user: Model.hasOne('user'),
  requestedUsername: Model.attribute('requestedUsername'),
  status: Model.attribute('status'),
  reason: Model.attribute('reason'),
  createdAt: Model.attribute('createdAt', Model.transformDate)
}) {}
