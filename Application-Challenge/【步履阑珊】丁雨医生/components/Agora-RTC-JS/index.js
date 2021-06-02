"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
Object.defineProperty(exports, "RtcChannel", {
  enumerable: true,
  get: function () {
    return _RtcChannel.default;
  }
});
exports.default = void 0;

var _RtcEngine = _interopRequireDefault(require("./common/RtcEngine.native"));

var _RtcChannel = _interopRequireDefault(require("./common/RtcChannel.native"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var _default = _RtcEngine.default;
exports.default = _default;
//# sourceMappingURL=index.js.map