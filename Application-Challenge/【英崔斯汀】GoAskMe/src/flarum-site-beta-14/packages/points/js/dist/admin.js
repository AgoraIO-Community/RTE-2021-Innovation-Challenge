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
/******/ 	return __webpack_require__(__webpack_require__.s = "./admin.js");
/******/ })
/************************************************************************/
/******/ ({

/***/ "./admin.js":
/*!******************!*\
  !*** ./admin.js ***!
  \******************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _src_admin__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./src/admin */ "./src/admin/index.js");
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

/***/ "./src/admin/addSettingsPage.js":
/*!**************************************!*\
  !*** ./src/admin/addSettingsPage.js ***!
  \**************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var flarum_extend__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! flarum/extend */ "flarum/extend");
/* harmony import */ var flarum_extend__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(flarum_extend__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var flarum_components_AdminNav__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! flarum/components/AdminNav */ "flarum/components/AdminNav");
/* harmony import */ var flarum_components_AdminNav__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(flarum_components_AdminNav__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var flarum_components_AdminLinkButton__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! flarum/components/AdminLinkButton */ "flarum/components/AdminLinkButton");
/* harmony import */ var flarum_components_AdminLinkButton__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(flarum_components_AdminLinkButton__WEBPACK_IMPORTED_MODULE_2__);
/* harmony import */ var _components_SettingsPage__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./components/SettingsPage */ "./src/admin/components/SettingsPage.js");
/* harmony import */ var _models_Pointslist__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./models/Pointslist */ "./src/admin/models/Pointslist.js");





/* harmony default export */ __webpack_exports__["default"] = (function () {
  // Main page
  app.routes.points = {
    path: '/goaskme/points-convert',
    component: _components_SettingsPage__WEBPACK_IMPORTED_MODULE_3__["default"].component()
  };
  console.log(_components_SettingsPage__WEBPACK_IMPORTED_MODULE_3__["default"].component);
  console.log(_models_Pointslist__WEBPACK_IMPORTED_MODULE_4__["default"]); // Quick access settings from extensions tab

  app.extensionSettings['points'] = function () {
    return m.route(app.route('points'));
  };

  app.store.models['points/list'] = _models_Pointslist__WEBPACK_IMPORTED_MODULE_4__["default"];
  Object(flarum_extend__WEBPACK_IMPORTED_MODULE_0__["extend"])(flarum_components_AdminNav__WEBPACK_IMPORTED_MODULE_1___default.a.prototype, 'items', function (items) {
    items.add('points', flarum_components_AdminLinkButton__WEBPACK_IMPORTED_MODULE_2___default.a.component({
      href: app.route('points'),
      icon: 'fas fa-gavel',
      description: 'Points convert check here'
    }, 'Points Bill222'));
  }); // app.routes['points'] = { path: '/goaskme/points-convert', component: SettingsPage.component() };
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
});

/***/ }),

/***/ "./src/admin/components/SettingsPage.js":
/*!**********************************************!*\
  !*** ./src/admin/components/SettingsPage.js ***!
  \**********************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return SettingsPage; });
/* harmony import */ var _babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @babel/runtime/helpers/esm/inheritsLoose */ "./node_modules/@babel/runtime/helpers/esm/inheritsLoose.js");
/* harmony import */ var flarum_components_Button__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! flarum/components/Button */ "flarum/components/Button");
/* harmony import */ var flarum_components_Button__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(flarum_components_Button__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var flarum_components_LoadingIndicator__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! flarum/components/LoadingIndicator */ "flarum/components/LoadingIndicator");
/* harmony import */ var flarum_components_LoadingIndicator__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(flarum_components_LoadingIndicator__WEBPACK_IMPORTED_MODULE_2__);
/* harmony import */ var flarum_components_Placeholder__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! flarum/components/Placeholder */ "flarum/components/Placeholder");
/* harmony import */ var flarum_components_Placeholder__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(flarum_components_Placeholder__WEBPACK_IMPORTED_MODULE_3__);
/* harmony import */ var flarum_components_Page__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! flarum/components/Page */ "flarum/components/Page");
/* harmony import */ var flarum_components_Page__WEBPACK_IMPORTED_MODULE_4___default = /*#__PURE__*/__webpack_require__.n(flarum_components_Page__WEBPACK_IMPORTED_MODULE_4__);
/* harmony import */ var _SettingsPageItem__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ./SettingsPageItem */ "./src/admin/components/SettingsPageItem.js");







var SettingsPage = /*#__PURE__*/function (_Page) {
  Object(_babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__["default"])(SettingsPage, _Page);

  function SettingsPage() {
    return _Page.apply(this, arguments) || this;
  }

  var _proto = SettingsPage.prototype;

  _proto.init = function init() {
    _Page.prototype.init.call(this);

    this.loading = true;
    this.page = 0;
    this.pageSize = 20;
  };

  _proto.config = function config(isInitialized) {
    _Page.prototype.config.apply(this, arguments);

    if (isInitialized) return;
    this.refresh();
  };

  _proto.view = function view() {
    var next, prev;

    if (this.nextResults === true) {
      next = flarum_components_Button__WEBPACK_IMPORTED_MODULE_1___default.a.component({
        className: 'Button Button--PageList-next',
        icon: 'fas fa-angle-right',
        onclick: this.loadNext.bind(this)
      });
    }

    if (this.prevResults === true) {
      prev = flarum_components_Button__WEBPACK_IMPORTED_MODULE_1___default.a.component({
        className: 'Button Button--PageList-prev',
        icon: 'fas fa-angle-left',
        onclick: this.loadPrev.bind(this)
      });
    }

    return m("div", {
      className: "ExtensionsPage"
    }, m("div", {
      className: "ExtensionsPage-header"
    }, m("div", {
      className: "container"
    }, "\u5934\u6807\u9898\u63CF\u8FF0\u90E8\u5206")), m("br", null), m("div", {
      className: "BannedIpsPage-table"
    }, m("div", {
      className: "container"
    }, this.loading ? flarum_components_LoadingIndicator__WEBPACK_IMPORTED_MODULE_2___default.a.component() : app.store.all('points/list')[0].data.list.length ? m("table", {
      style: {
        width: '100%',
        textAlign: 'center'
      },
      className: "PermissionGrid PointsList"
    }, m("thead", null, m("tr", {
      className: "PermissionGrid-section"
    }, m("th", null, "\u7528\u6237\u540D"), m("th", null, "\u7C7B\u578B"), m("th", null, "\u6263\u9664Points"), m("th", null, "\u5269\u4F59Points"), m("th", null, "\u94B1\u5305\u5730\u5740"), m("th", null, "\u63D0\u4EA4\u65F6\u95F4"), m("th", null, "\u64CD\u4F5C"))), m("tbody", null, app.store.all('points/list')[0].data.list.map(function (bannedIP) {
      return _SettingsPageItem__WEBPACK_IMPORTED_MODULE_5__["default"].component({
        bannedIP: bannedIP
      });
    }))) : m("div", null, flarum_components_Placeholder__WEBPACK_IMPORTED_MODULE_3___default.a.component({
      text: app.translator.trans('goaskme-points.admin.empty_text')
    })))), m("div", null, prev, next));
  };

  _proto.refresh = function refresh() {
    if (app.store.all('points/list').length) {
      // 清空数据
      app.store.all('points/list')[0].data.list = [];
    }

    return this.loadResults().then(this.parseResults.bind(this));
  }
  /**
   * Load a new page of Pages results.
   *
   * @param {Integer} page number.
   * @return {Promise}
   */
  ;

  _proto.loadResults = function loadResults() {
    this.loading = true;
    var offset = this.page * this.pageSize;
    return app.store.find('points/list', {
      offset: offset,
      limit: this.pageSize
    });
  }
  /**
   * Load the next page of results.
   *
   * @public
   */
  ;

  _proto.loadNext = function loadNext() {
    if (this.nextResults === true) {
      this.page++;
      this.refresh();
    }
  }
  /**
   * Load the previous page of results.
   *
   * @public
   */
  ;

  _proto.loadPrev = function loadPrev() {
    if (this.prevResults === true) {
      this.page--;
      this.refresh();
    }
  }
  /**
   * Parse results and append them to the page list.
   *
   * @param {Page[]} results
   * @return {Page[]}
   */
  ;

  _proto.parseResults = function parseResults(results) {
    this.loading = false;
    this.nextResults = !!results.data.next;
    this.prevResults = !!results.data.prev;
    m.lazyRedraw();
  };

  return SettingsPage;
}(flarum_components_Page__WEBPACK_IMPORTED_MODULE_4___default.a);



/***/ }),

/***/ "./src/admin/components/SettingsPageItem.js":
/*!**************************************************!*\
  !*** ./src/admin/components/SettingsPageItem.js ***!
  \**************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return SettingsPageItem; });
/* harmony import */ var _babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @babel/runtime/helpers/esm/inheritsLoose */ "./node_modules/@babel/runtime/helpers/esm/inheritsLoose.js");
/* harmony import */ var flarum_Component__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! flarum/Component */ "flarum/Component");
/* harmony import */ var flarum_Component__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(flarum_Component__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var flarum_components_Button__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! flarum/components/Button */ "flarum/components/Button");
/* harmony import */ var flarum_components_Button__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(flarum_components_Button__WEBPACK_IMPORTED_MODULE_2__);
/* harmony import */ var flarum_components_Alert__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! flarum/components/Alert */ "flarum/components/Alert");
/* harmony import */ var flarum_components_Alert__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(flarum_components_Alert__WEBPACK_IMPORTED_MODULE_3__);



 // import UnbanIPModal from '../../common/components/UnbanIPModal';
// import ChangeReasonModal from './ChangeReasonModal';

var SettingsPageItem = /*#__PURE__*/function (_Component) {
  Object(_babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__["default"])(SettingsPageItem, _Component);

  function SettingsPageItem() {
    return _Component.apply(this, arguments) || this;
  }

  var _proto = SettingsPageItem.prototype;

  _proto.init = function init() {
    this.item = this.props.bannedIP;
    var alert;
    this.viewButton = flarum_components_Button__WEBPACK_IMPORTED_MODULE_2___default.a.component({
      className: 'Button Button--link',
      children: '确定',
      onclick: function onclick() {
        // m.route(app.route.post(post));
        app.alerts.dismiss(alert);
      }
    });
  }
  /**
   * owner_id: 1
   * type: "Convering"
   * amount: -2500
   * current: 3129661
   * created_at: "2020-08-12 15:30:43"
   * extra: "sdfshjwlkjehfilgflerbvhjer" */
  ;

  _proto.view = function view() {
    var _this = this;

    return m("tr", {
      className: "PermissionGrid-child"
    }, m("td", null, this.item.owner.username), m("td", null, this.item.type), m("td", null, this.item.amount), m("td", null, this.item.current), m("td", null, this.item.extra), m("td", null, this.item.created_at), m("td", null, m("div", {
      className: "Button--group"
    }, flarum_components_Button__WEBPACK_IMPORTED_MODULE_2___default.a.component({
      className: 'Button Button--warning',
      icon: 'fas fa-check',
      onclick: function onclick() {
        _this.doCheck('converDown');
      }
    }), flarum_components_Button__WEBPACK_IMPORTED_MODULE_2___default.a.component({
      className: 'Button Button--danger',
      icon: 'fas fa-times',
      onclick: function onclick() {
        _this.doCheck('converReject');
      }
    }))));
  };

  _proto.doCheck = function doCheck(type) {
    var _this2 = this;

    m.request({
      method: "GET",
      url: "/api/points/check",
      data: {
        id: this.item.id,
        type: type
      }
    }).then(function (res) {
      _this2.succesAlert(res);

      _this2.item.type = res.type;
      m.redraw();
    });
  };

  _proto.succesAlert = function succesAlert(res) {
    var alert;
    var viewButton = flarum_components_Button__WEBPACK_IMPORTED_MODULE_2___default.a.component({
      className: 'Button Button--link',
      children: '确定',
      onclick: function onclick() {
        // m.route(app.route.post(post));
        app.alerts.dismiss(alert);
      }
    });
    app.alerts.show(alert = new flarum_components_Alert__WEBPACK_IMPORTED_MODULE_3___default.a({
      type: 'success',
      children: 'change iteam type to ' + res.type,
      controls: [viewButton]
    }));
  };

  _proto.errorAlert = function errorAlert(res) {
    var alert;
    var viewButton = flarum_components_Button__WEBPACK_IMPORTED_MODULE_2___default.a.component({
      className: 'Button Button--link',
      children: '确定',
      onclick: function onclick() {
        // m.route(app.route.post(post));
        app.alerts.dismiss(alert);
      }
    });
    app.alerts.show(alert = new flarum_components_Alert__WEBPACK_IMPORTED_MODULE_3___default.a({
      type: 'error',
      children: JSON.stringify(res),
      controls: [viewButton]
    }));
  };

  return SettingsPageItem;
}(flarum_Component__WEBPACK_IMPORTED_MODULE_1___default.a);



/***/ }),

/***/ "./src/admin/index.js":
/*!****************************!*\
  !*** ./src/admin/index.js ***!
  \****************************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _addSettingsPage__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./addSettingsPage */ "./src/admin/addSettingsPage.js");

app.initializers.add('goaskme/points-convert', function () {
  console.log('[goaskme/points-convert Hello, admin888888888888!');
  Object(_addSettingsPage__WEBPACK_IMPORTED_MODULE_0__["default"])();
});

/***/ }),

/***/ "./src/admin/models/Pointslist.js":
/*!****************************************!*\
  !*** ./src/admin/models/Pointslist.js ***!
  \****************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return PointsList; });
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



var PointsList = /*#__PURE__*/function (_mixin) {
  Object(_babel_runtime_helpers_esm_inheritsLoose__WEBPACK_IMPORTED_MODULE_0__["default"])(PointsList, _mixin);

  function PointsList() {
    return _mixin.apply(this, arguments) || this;
  }

  return PointsList;
}(flarum_utils_mixin__WEBPACK_IMPORTED_MODULE_2___default()(flarum_Model__WEBPACK_IMPORTED_MODULE_1___default.a, {}));



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

/***/ "flarum/components/AdminLinkButton":
/*!*******************************************************************!*\
  !*** external "flarum.core.compat['components/AdminLinkButton']" ***!
  \*******************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/AdminLinkButton'];

/***/ }),

/***/ "flarum/components/AdminNav":
/*!************************************************************!*\
  !*** external "flarum.core.compat['components/AdminNav']" ***!
  \************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/AdminNav'];

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

/***/ "flarum/components/LoadingIndicator":
/*!********************************************************************!*\
  !*** external "flarum.core.compat['components/LoadingIndicator']" ***!
  \********************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/LoadingIndicator'];

/***/ }),

/***/ "flarum/components/Page":
/*!********************************************************!*\
  !*** external "flarum.core.compat['components/Page']" ***!
  \********************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/Page'];

/***/ }),

/***/ "flarum/components/Placeholder":
/*!***************************************************************!*\
  !*** external "flarum.core.compat['components/Placeholder']" ***!
  \***************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['components/Placeholder'];

/***/ }),

/***/ "flarum/extend":
/*!***********************************************!*\
  !*** external "flarum.core.compat['extend']" ***!
  \***********************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = flarum.core.compat['extend'];

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
//# sourceMappingURL=admin.js.map