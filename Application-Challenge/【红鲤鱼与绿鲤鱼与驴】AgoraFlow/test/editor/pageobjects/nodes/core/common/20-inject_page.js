/**
 * Copyright JS Foundation and other contributors, http://js.foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

var util = require("util");

var nodePage = require("../../node_page");

function injectNode(id) {
    nodePage.call(this, id);
}

util.inherits(injectNode, nodePage);

var payloadTypeList = {
    "msg": 1,
    "flow": 2,
    "global": 3,
    "str": 4,
    "num": 5,
    "bool": 6,
    "json": 7,
    "bin": 8,
    "date": 9,
    "jsonata": 10,
    "env": 11,
};

var repeatTypeList = {
    "none": 1,
    "interval": 2,
    "intervalBetweenTimes": 3,
    "atASpecificTime": 4,
};

injectNode.prototype.setPayload = function(payloadType, payload) {
    // Open a payload type list.
    browser.clickWithWait('//*[@id="node-input-property-container"]/li[1]/div/div/div[3]');
    // Select a payload type.
    var payloadTypeXPath = '//*[contains(@class, "red-ui-typedInput-options")]/a[' + payloadTypeList[payloadType] + ']';
    browser.clickWithWait(payloadTypeXPath);
    if (payload) {
        // Input a value.
        browser.setValue('//*[@id="node-input-property-container"]/li[1]/div/div/div[3]/div[1]/input', payload);
    }
}

injectNode.prototype.setTopic = function(topic) {
    browser.setValue('//*[@id="node-input-property-container"]/li[2]/div/div/div[3]/div[1]/input', topic);
}

injectNode.prototype.setOnce = function(once) {
    var isChecked = browser.isSelected('#node-input-once');
    if (isChecked !== once) {
        browser.clickWithWait('#node-input-once');
    }
}

injectNode.prototype.setRepeat = function(repeatType) {
    var repeatTypeXPath = '//*[@id="inject-time-type-select"]/option[' + repeatTypeList[repeatType] + ']';
    browser.clickWithWait(repeatTypeXPath);
}

injectNode.prototype.setRepeatInterval = function(repeat) {
    browser.setValue('#inject-time-interval-count', repeat);
}

module.exports = injectNode;
