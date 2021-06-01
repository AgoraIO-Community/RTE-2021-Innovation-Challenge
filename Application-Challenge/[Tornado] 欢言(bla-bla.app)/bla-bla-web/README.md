# bla-bla-web
## Installation

**Step 1:** Checkout and running the Ember CLI server(this repository):

* `git clone https://github.com/tningjs/bla-bla-web.git`
* Register an Agora account, and get an `APP_ID` to put into `AGORA_ENV` at `environment.js`. [Refer](https://docs.agora.io/en/Voice/product_voice?platform=Web)
* Register a leancloud account, get an `APP_ID`, `APP_KEY` to put into `LEANCLOUD_ENV` at `environment.js`. [refer](https://docs.leancloud.app/leanstorage_guide-js.html)
* `cd bla-bla-web`
* `yarn install`
* `ember serve`

**Step 2:** Checkout and running the API server:

* `git clone https://github.com/tningjs/bla-bla-api`
* Use the same Agora account in step 1 to get the API keys, put them into `env` and rename it to `.env`
* `cd bla-bla-api`
* `yarn install`
* `yarn start`

## Credits

- Development: Tao Ning([@tningjs](https://github.com/tningjs)) and Sean Johnson([@seanjohnson08](https://github.com/seanjohnson08))
- Design: Lora Zhang([@Tingzzz](https://github.com/Tingzzz))
- The project is inspired by [neshouse](https://neshouse.com/). We chose the same backend service, and CSS framework, many thanks to the author([@bestony](https://github.com/bestony))❤️ who kindly open source it. However, the code in this repo is written from scratch by using a different JS framework [Emberjs](https://emberjs.com/) and added a lot more features comparing to the open-source version.
- The voice chat/message uses [Agora Web SDK](https://docs.agora.io/cn/Voice/API%20Reference/web_ng/index.html)
- The data storage uses [LeanStorage SDK](https://docs.leancloud.app/leanstorage_guide-js.html)
- The Javascript framework is [Emberjs](https://emberjs.com/)
- The CSS pixel framework is [NES.css](https://nostalgic-css.github.io/NES.css/) for styling
- The English font is from [Press Start 2P](https://fonts.google.com/specimen/Press+Start+2P)
- The Chinese font is from [Zpix](https://github.com/SolidZORO/zpix-pixel-font)
