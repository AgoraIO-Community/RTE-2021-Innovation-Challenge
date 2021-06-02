module.exports =
/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = "./forum.js");
/******/ })
/************************************************************************/
/******/ ({

/***/ "./forum.js":
/*!******************!*\
  !*** ./forum.js ***!
  \******************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _src_forum__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./src/forum */ "./src/forum/index.js");
/* empty/unused harmony star reexport */

/***/ }),

/***/ "./node_modules/@babel/runtime/helpers/esm/inheritsLoose.js":
/*!******************************************************************!*\
  !*** ./node_modules/@babel/runtime/helpers/esm/inheritsLoose.js ***!
  \******************************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return _inheritsLoose; });
function _inheritsLoose(subClass, superClass) {
  subClass.prototype = Object.create(superClass.prototype);
  subClass.prototype.constructor = subClass;
  subClass.__proto__ = superClass;
}

/***/ }),

/***/ "./src/forum/addGoaskmePointsPage.js":
/*!*******************************************!*\
  !*** ./src/forum/addGoaskmePointsPage.js ***!
  \*******************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var flarum_extend__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! flarum/extend */ "flarum/extend");
/* harmony import */ var flarum_extend__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(flarum_extend__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var flarum_components_UserPage__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! flarum/components/UserPage */ "flarum/components/UserPage");
/* harmony import */ var flarum_components_UserPage__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(flarum_components_UserPage__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var flarum_components_LinkButton__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! flarum/components/LinkButton */ "flarum/components/LinkButton");
/* harmony import */ var flarum_components_LinkButton__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(flarum_components_LinkButton__WEBPACK_IMPORTED_MODULE_2__);
/* harmony import */ var _components_addGoaskmePointsUserPage__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./components/addGoaskmePointsUserPage */ "./src/forum/components/addGoaskmePointsUserPage.js");




/* harmony default export */ __webpack_exports__["default"] = (function () {
  app.routes['user.GoAskMePoints'] = {
    path: '/u/:username/GoAskMePoints',
    component: _components_addGoaskmePointsUserPage__WEBPACK_IMPORTED_MODULE_3__["default"].component()
  };
  Object(flarum_extend__WEBPACK_IMPORTED_MODULE_0__["extend"])(flarum_components_UserPage__WEBPACK_IMPORTED_MODULE_1___default.a.prototype, 'navItems', function (items) {
    var href = app.route('user.GoAskMePoints', {
      username: this.user.username()
    }); // Hide links from guests if they are not already on the page

    if (!app.session.user && m.route() !== href) return;
    if (app.current.user && app.session.user.data.id !== app.current.user.data.id) return;
    items.add('GoAskMePoints', flarum_components_LinkButton__WEBPACK_IMPORTED_MODULE_2___default.a.component({
      href: href,
      children: app.translator.trans('goaskme-points.forum.user.dropdown_label'),
      icon: 'fa fa-magic'
    }), 85);
  });
});

/***/ }),

/***/ "./src/forum/addGoaskmePointsSessionDropdown.js":
/*!******************************************************!*\
  !*** ./src/forum/addGoaskmePointsSessionDropdown.js ***!
  \******************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var flarum_extend__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! flarum/extend */ "flarum/extend");
/* harmony import */ var flarum_extend__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(flarum_extend__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var flarum_components_LinkButton__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! flarum/components/LinkButton */ "flarum/components/LinkButton");
/* harmony import */ var flarum_components_LinkButton__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(flarum_components_LinkButton__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var flarum_components_SessionDropdown__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! flarum/components/SessionDropdown */ "flarum/components/SessionDropdown");
/* harmony import */ var flarum_components_SessionDropdown__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(flarum_components_SessionDropdown__WEBPACK_IMPORTED_MODULE_2__);
/* harmony import */ var flarum_components_IndexPage__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! flarum/components/IndexPage */ "flarum/components/IndexPage");
/* harmony import */ var flarum_components_IndexPage__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(flarum_components_IndexPage__WEBPACK_IMPORTED_MODULE_3__);




/* harmony default export */ __webpack_exports__["default"] = (function () {
  Object(flarum_extend__WEBPACK_IMPORTED_MODULE_0__["extend"])(flarum_components_SessionDropdown__WEBPACK_IMPORTED_MODULE_2___default.a.prototype, 'items', function (items) {
    var user = app.session.user;
    items.add('GoAskMePoints', flarum_components_LinkButton__WEBPACK_IMPORTED_MODULE_1___default.a.component({
      icon: 'fa fa-magic',
      children: app.translator.trans('goaskme-points.forum.user.dropdown_label'),
      href: app.route('user.GoAskMePoints', {
        username: user.username()
      })
    }), 99);
  }); // 缺乏登陆判断

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
});

/***/ }),

/***/ "./src/forum/addPointsMoney.js":
/*!*************************************!*\
  !*** ./src/forum/addPointsMoney.js ***!
  \*************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var flarum_extend__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! flarum/extend */ "flarum/extend");
/* harmony import */ var flarum_extend__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(flarum_extend__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var flarum_components_UserCard__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! flarum/components/UserCard */ "flarum/components/UserCard");
/* harmony import */ var flarum_components_UserCard__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(flarum_components_UserCard__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var flarum_utils_UserControls__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! flarum/utils/UserControls */ "flarum/utils/UserControls");
/* harmony import */ var flarum_utils_UserControls__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(flarum_utils_UserControls__WEBPACK_IMPORTED_MODULE_2__);
/* harmony import */ var flarum_components_Button__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! flarum/components/Button */ "flarum/components/Button");
/* harmony import */ var flarum_components_Button__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(flarum_components_Button__WEBPACK_IMPORTED_MODULE_3__);
/* harmony import */ var flarum_Model__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! flarum/Model */ "flarum/Model");
/* harmony import */ var flarum_Model__WEBPACK_IMPORTED_MODULE_4___default = /*#__PURE__*/__webpack_require__.n(flarum_Model__WEBPACK_IMPORTED_MODULE_4__);
/* harmony import */ var flarum_models_User__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! flarum/models/User */ "flarum/models/User");
/* harmony import */ var flarum_models_User__WEBPACK_IMPORTED_MODULE_5___default = /*#__PURE__*/__webpack_require__.n(flarum_models_User__WEBPACK_IMPORTED_MODULE_5__);



 //import UserMoneyModal from './components/UserMoneyModal';



/* harmony default export */ __webpack_exports__["default"] = (function () {
  //边框 fa fa-star-o 全空
  //边框 fa fa-star-half-o 空半星
  // fa fa-star-half 半星
  // fa fa-star 全星
  Object(flarum_extend__WEBPACK_IMPORTED_MODULE_0__["extend"])(flarum_components_UserCard__WEBPACK_IMPORTED_MODULE_1___default.a.prototype, 'infoItems', function (items) {
    var num = this.props.user.data.attributes['pointsRank'];
    var showFull = ['荣誉爆炸值：'];

    if (parseInt(parseInt(num) / 2)) {
      showFull.push.apply(showFull, Array(parseInt(parseInt(num) / 2)).fill('fa fa-star'));
    }

    if (parseInt(parseInt(num) % 2)) {
      showFull.push.apply(showFull, Array(parseInt(num) % 2).fill('fa fa-star-half'));
    }

    items.add('points-rate', showFull.map(function (item, index, arr) {
      if (index === 0) {
        return item;
      }

      return m('i', {
        "class": item + '  lv' + arr.length
      });
    }));
  }); //管理员编辑 money

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
});

/***/ }),

/***/ "./src/forum/components/RequestModal.js":
/*!**********************************************!*\
  !*** ./src/forum/components/RequestModal.js ***!
  \**********************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return FlagPostModal; });
/* harmony import */ var _babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @babel/runtime/helpers/esm/inheritsLoose */ "./node_modules/@babel/runtime/helpers/esm/inheritsLoose.js");
/* harmony import */ var flarum_components_Alert__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! flarum/components/Alert */ "flarum/components/Alert");
/* harmony import */ var flarum_components_Alert__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(flarum_components_Alert__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var flarum_components_Modal__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! flarum/components/Modal */ "flarum/components/Modal");
/* harmony import */ var flarum_components_Modal__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(flarum_components_Modal__WEBPACK_IMPORTED_MODULE_2__);
/* harmony import */ var flarum_components_Button__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! flarum/components/Button */ "flarum/components/Button");
/* harmony import */ var flarum_components_Button__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(flarum_components_Button__WEBPACK_IMPORTED_MODULE_3__);


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




var FlagPostModal = /*#__PURE__*/function (_Modal) {
  Object(_babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__["default"])(FlagPostModal, _Modal);

  function FlagPostModal() {
    return _Modal.apply(this, arguments) || this;
  }

  var _proto = FlagPostModal.prototype;

  _proto.init = function init() {
    _Modal.prototype.init.call(this);

    this.username = m.prop(app.session.user.username());
    this.success = false; // this.password = m.prop('')
    // 当前积分数量

    this.pointsNumber = parseInt(app.session.user.data.attributes['pointsCount']);
    this.pointsConverLimit = parseInt(this.pointsNumber / 5);
    this.pointsConverNumber = m.prop(0);
    this.pointsCount = m.prop("");
  };

  _proto.className = function className() {
    return 'RequestPointsConvert Modal--small';
  };

  _proto.title = function title() {
    return app.translator.trans('goaskme-points.forum.convert_gam.title');
  };

  _proto.content = function content() {
    if (this.success) {
      return m("div", {
        className: "Modal-body"
      }, m("div", {
        className: "Form Form--centered"
      }, m("p", {
        className: "helpText",
        style: "text-align: center;"
      }, app.translator.trans('goaskme-points.forum.convert_gam.confirmation_message')), m("div", {
        className: "Form-group"
      }, m(flarum_components_Button__WEBPACK_IMPORTED_MODULE_3___default.a, {
        className: "Button Button--primary Button--block",
        onclick: this.hide.bind(this)
      }, app.translator.trans('goaskme-points.forum.request.dismiss')))));
    }

    return m("div", {
      className: "Modal-body"
    }, m("div", {
      className: "Form Form--centered"
    }, m("div", {
      className: "Form-group"
    }, app.translator.trans('goaskme-points.forum.convert_gam.points_show'), this.pointsNumber), m("div", {
      className: "Form-group"
    }, app.translator.trans('goaskme-points.forum.convert_gam.points_ratio')), m("div", {
      className: "Form-group"
    }, app.translator.trans('goaskme-points.forum.convert_gam.points_max_convert'), this.pointsConverLimit), m("div", {
      className: "Form-group"
    }, m("input", {
      type: "number",
      name: "number",
      className: "FormControl",
      placeholder: app.translator.trans('goaskme-points.forum.convert_gam.convert_num'),
      min: "1",
      max: this.pointsConverLimit,
      bidi: this.pointsConverNumber,
      disabled: this.loading,
      style: "text-align: center;"
    })), m("div", {
      className: "Form-group"
    }, m("input", {
      type: "text",
      name: "text",
      minlength: "4",
      className: "FormControl",
      placeholder: app.translator.trans('goaskme-points.forum.convert_gam.address'),
      bidi: this.pointsCount,
      disabled: this.loading
    })), m("div", {
      className: "Form-group"
    }, flarum_components_Button__WEBPACK_IMPORTED_MODULE_3___default.a.component({
      className: 'Button Button--primary Button--block',
      type: 'submit',
      loading: this.loading,
      disabled: this.loading || this.pointsConverLimit === 0,
      children: app.translator.trans('goaskme-points.forum.request.submit')
    }))));
  };

  _proto.onsubmit = function onsubmit(e) {
    var _this = this;

    e.preventDefault();
    this.alert = null; // if (this.username() === app.session.user.username()) {
    //   this.hide()
    //   return
    // }

    this.loading = true;
    app.store.createRecord('goaskme_convert_gam_requests') // .save(
    //  { username: this.username() },
    //  {
    //    meta: { password: this.password() },
    //    errorHandler: this.onerror.bind(this)
    //  }
    //)
    .save({
      pointsConverNumber: this.pointsConverNumber,
      pointsCount: this.pointsCount,
      errorHandler: this.onerror.bind(this)
    }).then(function (request) {
      app.session.user.data.attributes['pointsCount'] = request.data.attributes['pointsCount'];
      app.session.user.goaskme_convert_gam_requests = m.prop(request);
      _this.success = true;
    })["catch"](function () {}).then(this.loaded.bind(this));
  };

  _proto.onerror = function onerror(error) {
    if (error.status === 500) {
      error.alert.props.children = app.translator.trans('goaskme-points.forum.request.invalid_Argument');
    }

    _Modal.prototype.onerror.call(this, error);
  };

  return FlagPostModal;
}(flarum_components_Modal__WEBPACK_IMPORTED_MODULE_2___default.a);



/***/ }),

/***/ "./src/forum/components/RequestsList.js":
/*!**********************************************!*\
  !*** ./src/forum/components/RequestsList.js ***!
  \**********************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return RequestsList; });
/* harmony import */ var _babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @babel/runtime/helpers/esm/inheritsLoose */ "./node_modules/@babel/runtime/helpers/esm/inheritsLoose.js");
/* harmony import */ var flarum_Component__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! flarum/Component */ "flarum/Component");
/* harmony import */ var flarum_Component__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(flarum_Component__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var flarum_components_LoadingIndicator__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! flarum/components/LoadingIndicator */ "flarum/components/LoadingIndicator");
/* harmony import */ var flarum_components_LoadingIndicator__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(flarum_components_LoadingIndicator__WEBPACK_IMPORTED_MODULE_2__);
/* harmony import */ var flarum_helpers_avatar__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! flarum/helpers/avatar */ "flarum/helpers/avatar");
/* harmony import */ var flarum_helpers_avatar__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(flarum_helpers_avatar__WEBPACK_IMPORTED_MODULE_3__);
/* harmony import */ var flarum_helpers_username__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! flarum/helpers/username */ "flarum/helpers/username");
/* harmony import */ var flarum_helpers_username__WEBPACK_IMPORTED_MODULE_4___default = /*#__PURE__*/__webpack_require__.n(flarum_helpers_username__WEBPACK_IMPORTED_MODULE_4__);
/* harmony import */ var flarum_helpers_icon__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! flarum/helpers/icon */ "flarum/helpers/icon");
/* harmony import */ var flarum_helpers_icon__WEBPACK_IMPORTED_MODULE_5___default = /*#__PURE__*/__webpack_require__.n(flarum_helpers_icon__WEBPACK_IMPORTED_MODULE_5__);
/* harmony import */ var flarum_helpers_humanTime__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! flarum/helpers/humanTime */ "flarum/helpers/humanTime");
/* harmony import */ var flarum_helpers_humanTime__WEBPACK_IMPORTED_MODULE_6___default = /*#__PURE__*/__webpack_require__.n(flarum_helpers_humanTime__WEBPACK_IMPORTED_MODULE_6__);


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





 // import ActionModal from './ActionModal'

var RequestsList = /*#__PURE__*/function (_Component) {
  Object(_babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__["default"])(RequestsList, _Component);

  function RequestsList() {
    return _Component.apply(this, arguments) || this;
  }

  var _proto = RequestsList.prototype;

  _proto.init = function init() {
    this.loading = false;
  };

  _proto.view = function view() {
    var requests = app.cache.points_list || []; //return <div className="NotificationList RequestsList"> 123123123 </div>
    // {owner_id: 1, type: "Convering", amount: -30, current: 5}

    return m("div", {
      className: "NotificationList RequestsList"
    }, m("div", {
      className: "NotificationList-header"
    }, m("h4", null, "\u6D41\u6C34\u660E\u7EC6(current:", parseInt(app.session.user.data.attributes['pointsCount']), ")    "), m("h4", null, "\u65F6\u533A\uFF1AUTC/GMT+08:00 ")), m("div", {
      className: "NotificationList-content"
    }, m("table", {
      "class": "NotificationGrid",
      style: "width: 100%;align-items: center;text-align: center;"
    }, m("thead", null, m("tr", null, m("th", {
      "class": "NotificationGrid-groupToggle"
    }, "\u7C7B\u578B"), m("th", {
      "class": "NotificationGrid-groupToggle"
    }, "\u6570\u91CF"), m("th", {
      "class": "NotificationGrid-groupToggle"
    }, "\u6D41\u6C34"), m("th", {
      "class": "NotificationGrid-groupToggle"
    }, "\u521B\u5EFA\u65F6\u95F4"))), m("tbody", null, requests.length ? requests.map(function (request) {
      return m("tr", null, m("td", {
        "class": "NotificationGrid-groupToggle"
      }, " ", app.translator.trans('goaskme-points.forum.convert_gam_list.' + request.type)), m("td", {
        "class": "NotificationGrid-groupToggle"
      }, " ", request.amount), m("td", {
        "class": "NotificationGrid-groupToggle"
      }, " ", request.current), m("td", {
        "class": "NotificationGrid-groupToggle"
      }, " ", request.created_at));
    }) : !this.loading ? m("div", {
      className: "NotificationList-empty"
    }, "It looks as though there are no Log here.") : m("div", {
      className: "NotificationList-empty"
    }, "nomal")))));
  };

  _proto.load = function load() {
    var _this = this;

    // if (app.cache.points_list) {
    //  return
    // }
    this.loading = true;
    m.redraw();
    app.store.createRecord('points').save({
      username: app.session.user.username()
    }).then(function (res) {
      var saveArr = [];

      if (!Array.isArray(res.data.list)) {
        for (var i in res.data.list) {
          saveArr.push(res.data.list[i]);
        }

        app.cache.points_list = saveArr;
      } else {
        app.cache.points_list = res.data.list;
      }
    })["catch"](function (err) {
      console.log(err);
    }).then(function () {
      _this.loading = false;
      m.redraw();
    });
  };

  return RequestsList;
}(flarum_Component__WEBPACK_IMPORTED_MODULE_1___default.a);



/***/ }),

/***/ "./src/forum/components/RequestsPage.js":
/*!**********************************************!*\
  !*** ./src/forum/components/RequestsPage.js ***!
  \**********************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return RequestsPage; });
/* harmony import */ var _babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @babel/runtime/helpers/esm/inheritsLoose */ "./node_modules/@babel/runtime/helpers/esm/inheritsLoose.js");
/* harmony import */ var flarum_components_Page__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! flarum/components/Page */ "flarum/components/Page");
/* harmony import */ var flarum_components_Page__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(flarum_components_Page__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var _RequestsList__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./RequestsList */ "./src/forum/components/RequestsList.js");


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



var RequestsPage = /*#__PURE__*/function (_Page) {
  Object(_babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__["default"])(RequestsPage, _Page);

  function RequestsPage() {
    return _Page.apply(this, arguments) || this;
  }

  var _proto = RequestsPage.prototype;

  _proto.init = function init() {
    _Page.prototype.init.call(this);

    app.history.push('requests');
    this.list = new _RequestsList__WEBPACK_IMPORTED_MODULE_2__["default"]();
    this.list.load();
    this.bodyClass = 'App--requests';
  };

  _proto.view = function view() {
    return m("div", {
      className: "RequestsPage"
    }, this.list.render());
  };

  return RequestsPage;
}(flarum_components_Page__WEBPACK_IMPORTED_MODULE_1___default.a);



/***/ }),

/***/ "./src/forum/components/addGoaskmePointsUserPage.js":
/*!**********************************************************!*\
  !*** ./src/forum/components/addGoaskmePointsUserPage.js ***!
  \**********************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return addGoaskmePointsUserPage; });
/* harmony import */ var _babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @babel/runtime/helpers/esm/inheritsLoose */ "./node_modules/@babel/runtime/helpers/esm/inheritsLoose.js");
/* harmony import */ var flarum_components_UserPage__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! flarum/components/UserPage */ "flarum/components/UserPage");
/* harmony import */ var flarum_components_UserPage__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(flarum_components_UserPage__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var _RequestsList__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./RequestsList */ "./src/forum/components/RequestsList.js");
/* harmony import */ var flarum_components_Button__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! flarum/components/Button */ "flarum/components/Button");
/* harmony import */ var flarum_components_Button__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(flarum_components_Button__WEBPACK_IMPORTED_MODULE_3__);
/* harmony import */ var flarum_components_Dropdown__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! flarum/components/Dropdown */ "flarum/components/Dropdown");
/* harmony import */ var flarum_components_Dropdown__WEBPACK_IMPORTED_MODULE_4___default = /*#__PURE__*/__webpack_require__.n(flarum_components_Dropdown__WEBPACK_IMPORTED_MODULE_4__);
/* harmony import */ var flarum_utils_ItemList__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! flarum/utils/ItemList */ "flarum/utils/ItemList");
/* harmony import */ var flarum_utils_ItemList__WEBPACK_IMPORTED_MODULE_5___default = /*#__PURE__*/__webpack_require__.n(flarum_utils_ItemList__WEBPACK_IMPORTED_MODULE_5__);
/* harmony import */ var flarum_helpers_listItems__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! flarum/helpers/listItems */ "flarum/helpers/listItems");
/* harmony import */ var flarum_helpers_listItems__WEBPACK_IMPORTED_MODULE_6___default = /*#__PURE__*/__webpack_require__.n(flarum_helpers_listItems__WEBPACK_IMPORTED_MODULE_6__);
/* harmony import */ var _RequestModal__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! ./RequestModal */ "./src/forum/components/RequestModal.js");
/* harmony import */ var flarum_components_LogInModal__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! flarum/components/LogInModal */ "flarum/components/LogInModal");
/* harmony import */ var flarum_components_LogInModal__WEBPACK_IMPORTED_MODULE_8___default = /*#__PURE__*/__webpack_require__.n(flarum_components_LogInModal__WEBPACK_IMPORTED_MODULE_8__);







 // import GoaskmePointsConvertComposer from './GoaskmePointsConvertComposer'



var addGoaskmePointsUserPage = /*#__PURE__*/function (_UserPage) {
  Object(_babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__["default"])(addGoaskmePointsUserPage, _UserPage);

  function addGoaskmePointsUserPage() {
    return _UserPage.apply(this, arguments) || this;
  }

  var _proto = addGoaskmePointsUserPage.prototype;

  _proto.init = function init() {
    _UserPage.prototype.init.call(this);

    this.changeSort('latest');
  };

  _proto.show = function show(user) {
    // We can not create the list in init because the user will not be available if it has to be loaded asynchronously
    this.list = new _RequestsList__WEBPACK_IMPORTED_MODULE_2__["default"]();
    this.list.load(); // We call the parent method after creating the list, this way the this.list property
    // is set before content() is called for the first time

    _UserPage.prototype.show.call(this, user);
  };

  _proto.handleChangeSort = function handleChangeSort(sort, e) {
    e.preventDefault();
    this.changeSort(sort);
  };

  _proto.changeSort = function changeSort(sort) {
    this.sort = sort;
    this.loadUser(m.route.param('username'));
  }
  /*
  newDiscussionAction(e) {
    e.preventDefault()
     const deferred = m.deferred()
     if (app.session.user) {
      let recipients = new ItemList()
      recipients.add('users:' + app.session.user.id(), app.session.user)
       if (this.user !== null && app.session.user.id() !== this.user.id()) {
        recipients.add('users:' + this.user.id(), this.user)
      }
       GoaskmePointsConvertComposer.prototype.recipients = recipients
       const component = new GoaskmePointsConvertComposer({
        user: app.session.user,
        recipients: recipients,
        recipientUsers: recipients,
        titlePlaceholder: app.translator.trans('goaskme-points.forum.user.convert_gam_title'),
        submitLabel: app.translator.trans('goaskme-points.forum.user.submit_button')
      })
       app.composer.load(component)
       deferred.resolve(component)
    } else {
      deferred.reject()
       app.modal.show(new LogInModal())
    }
     return deferred.promise
  }
  */
  ;

  _proto.content = function content() {
    /*
    return (
      <div className="Placeholder">
        <p>It looks as though there are no discussions here.</p>
      </div>
    )
    */
    return m("div", {
      className: "DiscussionsUserPage"
    }, m("div", {
      className: "DiscussionsUserPage-toolbar"
    }, m("ul", {
      className: "DiscussionsUserPage-toolbar-action"
    }, flarum_helpers_listItems__WEBPACK_IMPORTED_MODULE_6___default()(this.actionItems().toArray())), m("ul", {
      className: "DiscussionsUserPage-toolbar-view"
    }, flarum_helpers_listItems__WEBPACK_IMPORTED_MODULE_6___default()(this.viewItems().toArray()))), m("div", {
      className: "RequestsPage"
    }, this.list.view()));
  };

  _proto.actionItems = function actionItems() {
    var items = new flarum_utils_ItemList__WEBPACK_IMPORTED_MODULE_5___default.a();
    items.add('start_convert_gam', flarum_components_Button__WEBPACK_IMPORTED_MODULE_3___default.a.component({
      className: 'Button',
      onclick: function onclick() {
        app.modal.show(new _RequestModal__WEBPACK_IMPORTED_MODULE_7__["default"]());
      }
    }, [app.translator.trans('goaskme-points.forum.user.convert_gam')]), 10);
    /*
        if (app.session.user && app.forum.attribute('canStartPrivateDiscussion')) {
            items.add(
                'start_private',
                Button.component({
                    children: app.translator.trans(
                        canStartDiscussion ? 'fof-byobu.forum.nav.start_button' : 'core.forum.index.cannot_start_discussion_button'
                    ),
                    icon: 'fas fa-pen',
                    className: 'Button Button--primary IndexPage-newDiscussion',
                    itemClassName: 'fof-byobu_primaryControl',
                    //  onclick: this.newDiscussionAction.bind(this),
                    disabled: !canStartDiscussion,
                })
            );
        }
    */

    return items;
  };

  _proto.viewItems = function viewItems() {
    var items = new flarum_utils_ItemList__WEBPACK_IMPORTED_MODULE_5___default.a();
    /*
        const sortMap = this.list.sortMap();
         const sortOptions = {};
        for (const i in sortMap) {
            sortOptions[i] = app.translator.trans('core.forum.index_sort.' + i + '_button');
        }
         items.add(
            'sort',
            Dropdown.component({
                buttonClassName: 'Button',
                label: sortOptions[this.sort] || Object.keys(sortMap).map((key) => sortOptions[key])[0],
                children: Object.keys(sortOptions).map((value) => {
                    const label = sortOptions[value];
                    const active = (this.sort || Object.keys(sortMap)[0]) === value;
                     return Button.component({
                        children: label,
                        icon: active ? 'fas fa-check' : true,
                        onclick: this.handleChangeSort.bind(this, value),
                        active: active,
                    });
                }),
            })
        );
    */

    return items;
  };

  return addGoaskmePointsUserPage;
}(flarum_components_UserPage__WEBPACK_IMPORTED_MODULE_1___default.a);



/***/ }),

/***/ "./src/forum/index.js":
/*!****************************!*\
  !*** ./src/forum/index.js ***!
  \****************************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var flarum_models_User__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! flarum/models/User */ "flarum/models/User");
/* harmony import */ var flarum_models_User__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(flarum_models_User__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var flarum_Model__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! flarum/Model */ "flarum/Model");
/* harmony import */ var flarum_Model__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(flarum_Model__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var _addGoaskmePointsPage__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./addGoaskmePointsPage */ "./src/forum/addGoaskmePointsPage.js");
/* harmony import */ var _addGoaskmePointsSessionDropdown__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./addGoaskmePointsSessionDropdown */ "./src/forum/addGoaskmePointsSessionDropdown.js");
/* harmony import */ var _addPointsMoney__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./addPointsMoney */ "./src/forum/addPointsMoney.js");
/* harmony import */ var _components_RequestsPage__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ./components/RequestsPage */ "./src/forum/components/RequestsPage.js");
/* harmony import */ var _models_PointsPost__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ./models/PointsPost */ "./src/forum/models/PointsPost.js");
/* harmony import */ var _models_PointsRequest__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! ./models/PointsRequest */ "./src/forum/models/PointsRequest.js");

 // 钱包主页面

 // 右上角 钱包按钮

 // 个人页面 显示爆炸值

 // 钱包兑换显示页面


 // 钱包列表

 // import Discussion from 'flarum/models/Discussion';

app.initializers.add('goaskme-flarum-points', function () {
  app.store.models['goaskme_convert_gam_requests'] = _models_PointsPost__WEBPACK_IMPORTED_MODULE_6__["default"];
  app.store.models['points'] = _models_PointsRequest__WEBPACK_IMPORTED_MODULE_7__["default"];
  flarum_models_User__WEBPACK_IMPORTED_MODULE_0___default.a.prototype.goaskme_convert_gam_requests = flarum_Model__WEBPACK_IMPORTED_MODULE_1___default.a.hasOne('goaskme_convert_gam_requestss'); // 注册 兑换页面路由 及响应pag

  app.routes.goaskme_convert_gam = {
    path: '/goaskme_convert_gam',
    component: m(_components_RequestsPage__WEBPACK_IMPORTED_MODULE_5__["default"], null)
  };
  Object(_addGoaskmePointsPage__WEBPACK_IMPORTED_MODULE_2__["default"])();
  Object(_addGoaskmePointsSessionDropdown__WEBPACK_IMPORTED_MODULE_3__["default"])();
  Object(_addPointsMoney__WEBPACK_IMPORTED_MODULE_4__["default"])();
});

/***/ }),

/***/ "./src/forum/models/PointsPost.js":
/*!****************************************!*\
  !*** ./src/forum/models/PointsPost.js ***!
  \****************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return PointsPost; });
/* harmony import */ var _babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @babel/runtime/helpers/esm/inheritsLoose */ "./node_modules/@babel/runtime/helpers/esm/inheritsLoose.js");
/* harmony import */ var flarum_Model__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! flarum/Model */ "flarum/Model");
/* harmony import */ var flarum_Model__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(flarum_Model__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var flarum_utils_mixin__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! flarum/utils/mixin */ "flarum/utils/mixin");
/* harmony import */ var flarum_utils_mixin__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(flarum_utils_mixin__WEBPACK_IMPORTED_MODULE_2__);


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



var PointsPost = /*#__PURE__*/function (_mixin) {
  Object(_babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__["default"])(PointsPost, _mixin);

  function PointsPost() {
    return _mixin.apply(this, arguments) || this;
  }

  return PointsPost;
}(flarum_utils_mixin__WEBPACK_IMPORTED_MODULE_2___default()(flarum_Model__WEBPACK_IMPORTED_MODULE_1___default.a, {//user: Model.hasOne('user'),
  //requestedUsername: Model.attribute('requestedUsername'),
  //status: Model.attribute('status'),
  //reason: Model.attribute('reason'),
  //createdAt: Model.attribute('createdAt', Model.transformDate)
}));



/***/ }),

/***/ "./src/forum/models/PointsRequest.js":
/*!*******************************************!*\
  !*** ./src/forum/models/PointsRequest.js ***!
  \*******************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @babel/runtime/helpers/esm/inheritsLoose */ "./node_modules/@babel/runtime/helpers/esm/inheritsLoose.js");
/* harmony import */ var flarum_Model__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! flarum/Model */ "flarum/Model");
/* harmony import */ var flarum_Model__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(flarum_Model__WEBPACK_IMPORTED_MODULE_1__);



var PointsRequest = /*#__PURE__*/function (_Model) {
  Object(_babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__["default"])(PointsRequest, _Model);

  function PointsRequest() {
    return _Model.apply(this, arguments) || this;
  }

  return PointsRequest;
}(flarum_Model__WEBPACK_IMPORTED_MODULE_1___default.a);
/*
console.log()
Object.assign(Points.prototype, {
  nameSingular: Model.attribute('nameSingular'),
  namePlural: Model.attribute('namePlural'),
  color: Model.attribute('color'),
  icon: Model.attribute('icon'),
  isHidden: Model.attribute('isHidden')
})

Group.ADMINISTRATOR_ID = '1'
Group.GUEST_ID = '2'
Group.MEMBER_ID = '3'
*/


/* harmony default export */ __webpack_exports__["default"] = (PointsRequest);

/***/ }),

/***/ "flarum/Component":
/*!**************************************************!*\
  !*** external "flarum.core.compat['Component']" ***!
  \**************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['Component'];

/***/ }),

/***/ "flarum/Model":
/*!**********************************************!*\
  !*** external "flarum.core.compat['Model']" ***!
  \**********************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['Model'];

/***/ }),

/***/ "flarum/components/Alert":
/*!*********************************************************!*\
  !*** external "flarum.core.compat['components/Alert']" ***!
  \*********************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/Alert'];

/***/ }),

/***/ "flarum/components/Button":
/*!**********************************************************!*\
  !*** external "flarum.core.compat['components/Button']" ***!
  \**********************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/Button'];

/***/ }),

/***/ "flarum/components/Dropdown":
/*!************************************************************!*\
  !*** external "flarum.core.compat['components/Dropdown']" ***!
  \************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/Dropdown'];

/***/ }),

/***/ "flarum/components/IndexPage":
/*!*************************************************************!*\
  !*** external "flarum.core.compat['components/IndexPage']" ***!
  \*************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/IndexPage'];

/***/ }),

/***/ "flarum/components/LinkButton":
/*!**************************************************************!*\
  !*** external "flarum.core.compat['components/LinkButton']" ***!
  \**************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/LinkButton'];

/***/ }),

/***/ "flarum/components/LoadingIndicator":
/*!********************************************************************!*\
  !*** external "flarum.core.compat['components/LoadingIndicator']" ***!
  \********************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/LoadingIndicator'];

/***/ }),

/***/ "flarum/components/LogInModal":
/*!**************************************************************!*\
  !*** external "flarum.core.compat['components/LogInModal']" ***!
  \**************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/LogInModal'];

/***/ }),

/***/ "flarum/components/Modal":
/*!*********************************************************!*\
  !*** external "flarum.core.compat['components/Modal']" ***!
  \*********************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/Modal'];

/***/ }),

/***/ "flarum/components/Page":
/*!********************************************************!*\
  !*** external "flarum.core.compat['components/Page']" ***!
  \********************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/Page'];

/***/ }),

/***/ "flarum/components/SessionDropdown":
/*!*******************************************************************!*\
  !*** external "flarum.core.compat['components/SessionDropdown']" ***!
  \*******************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/SessionDropdown'];

/***/ }),

/***/ "flarum/components/UserCard":
/*!************************************************************!*\
  !*** external "flarum.core.compat['components/UserCard']" ***!
  \************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/UserCard'];

/***/ }),

/***/ "flarum/components/UserPage":
/*!************************************************************!*\
  !*** external "flarum.core.compat['components/UserPage']" ***!
  \************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/UserPage'];

/***/ }),

/***/ "flarum/extend":
/*!***********************************************!*\
  !*** external "flarum.core.compat['extend']" ***!
  \***********************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['extend'];

/***/ }),

/***/ "flarum/helpers/avatar":
/*!*******************************************************!*\
  !*** external "flarum.core.compat['helpers/avatar']" ***!
  \*******************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['helpers/avatar'];

/***/ }),

/***/ "flarum/helpers/humanTime":
/*!**********************************************************!*\
  !*** external "flarum.core.compat['helpers/humanTime']" ***!
  \**********************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['helpers/humanTime'];

/***/ }),

/***/ "flarum/helpers/icon":
/*!*****************************************************!*\
  !*** external "flarum.core.compat['helpers/icon']" ***!
  \*****************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['helpers/icon'];

/***/ }),

/***/ "flarum/helpers/listItems":
/*!**********************************************************!*\
  !*** external "flarum.core.compat['helpers/listItems']" ***!
  \**********************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['helpers/listItems'];

/***/ }),

/***/ "flarum/helpers/username":
/*!*********************************************************!*\
  !*** external "flarum.core.compat['helpers/username']" ***!
  \*********************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['helpers/username'];

/***/ }),

/***/ "flarum/models/User":
/*!****************************************************!*\
  !*** external "flarum.core.compat['models/User']" ***!
  \****************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['models/User'];

/***/ }),

/***/ "flarum/utils/ItemList":
/*!*******************************************************!*\
  !*** external "flarum.core.compat['utils/ItemList']" ***!
  \*******************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['utils/ItemList'];

/***/ }),

/***/ "flarum/utils/UserControls":
/*!***********************************************************!*\
  !*** external "flarum.core.compat['utils/UserControls']" ***!
  \***********************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['utils/UserControls'];

/***/ }),

/***/ "flarum/utils/mixin":
/*!****************************************************!*\
  !*** external "flarum.core.compat['utils/mixin']" ***!
  \****************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['utils/mixin'];

/***/ })

/******/ });
//# sourceMappingURL=forum.js.map