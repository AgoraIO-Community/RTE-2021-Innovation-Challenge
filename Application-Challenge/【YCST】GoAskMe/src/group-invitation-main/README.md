# Group Invitation

[![MIT license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/clarkwinkelmann/flarum-ext-group-invitation/blob/master/LICENSE.md) [![Latest Stable Version](https://img.shields.io/packagist/v/clarkwinkelmann/flarum-ext-group-invitation.svg)](https://packagist.org/packages/clarkwinkelmann/flarum-ext-group-invitation) [![Total Downloads](https://img.shields.io/packagist/dt/clarkwinkelmann/flarum-ext-group-invitation.svg)](https://packagist.org/packages/clarkwinkelmann/flarum-ext-group-invitation) [![Donate](https://img.shields.io/badge/paypal-donate-yellow.svg)](https://www.paypal.me/clarkwinkelmann)

This extension creates sharable links that will let any user join the group that was associated with the invitation.

![Screenshot](https://i.imgur.com/uumjObr.png)

Invitation links can be created in the extension settings.
A link can optionally have a limited number of uses.

The number of uses for an invitation is recorded but is not public.
There is a generic "can no longer be used" error if the invitation has no usage left.

A permission controls who can use links.
Don't forget to set it to **Members** if you want anyone to be able to use links.

If a user sees "not allowed to use invitations" even though you allowed them, check:

- That the user has verified their email
- That the user has accepted the terms of use (if using FoF Terms)
- That the user isn't suspended

This extension provides no way of **removing** groups.
If a user no longer wants to be part of a group, a moderator will have to remove the group manually through Flarum's user edit modal.

The word "group" has been used thorough this extension because it's the official name for the matching Flarum feature.
You can use the Linguist extension to adapt the naming based on your use case: role, badge, etc...

## Installation

    composer require clarkwinkelmann/flarum-ext-group-invitation

## Support

This extension is under **minimal maintenance**.

It was developed for a client and released as open-source for the benefit of the community.
I might publish simple bugfixes or compatibility updates for free.

You can [contact me](https://clarkwinkelmann.com/flarum) to sponsor additional features or updates.

Support is offered on a "best effort" basis through the Flarum community thread.

## Links

- [GitHub](https://github.com/clarkwinkelmann/flarum-ext-group-invitation)
- [Packagist](https://packagist.org/packages/clarkwinkelmann/flarum-ext-group-invitation)
- [Discuss](https://discuss.flarum.org/d/24627)
