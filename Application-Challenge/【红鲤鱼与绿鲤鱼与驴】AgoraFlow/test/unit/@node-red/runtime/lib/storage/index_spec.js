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
var should = require("should");
var paff = require('path');

var NR_TEST_UTILS = require("nr-test-utils");

var storage = NR_TEST_UTILS.require("@node-red/runtime/lib/storage/index");

describe("red/storage/index", function() {

    it('rejects the promise when settings suggest loading a bad module', function(done) {

        var wrongModule = {
            settings:{
                storageModule : "thisaintloading"
            }
        };

        storage.init(wrongModule).then( function() {
            var one = 1;
            var zero = 0;
            try {
                zero.should.equal(one, "The initialization promise should never get resolved");
            } catch(err) {
                done(err);
            }
        }).catch(function(e) {
            done(); //successfully rejected promise
        });
    });

    it('non-string storage module', function(done) {
        var initSetsMeToTrue = false;

        var moduleWithBooleanSettingInit = {
            init : function() {
                initSetsMeToTrue = true;
            }
        };

        var setsBooleanModule = {
            settings: {
                storageModule : moduleWithBooleanSettingInit
            }
        };

        storage.init(setsBooleanModule);
        initSetsMeToTrue.should.be.true();
        done();
    });

    it('respects storage interface', function(done) {
        var calledFlagGetFlows = false;
        var calledFlagGetCredentials = false;
        var calledFlagGetAllFlows = false;
        var calledInit = false;
        var calledFlagGetSettings = false;
        var calledFlagGetSessions = false;

        var interfaceCheckerModule = {
                init : function (settings) {
                    settings.should.be.an.Object();
                    calledInit = true;
                },
                getFlows : function() {
                    calledFlagGetFlows = true;
                    return Promise.resolve([]);
                },
                saveFlows : function (flows) {
                    flows.should.be.an.Array();
                    flows.should.have.lengthOf(0);
                    return Promise.resolve("");
                },
                getCredentials : function() {
                    calledFlagGetCredentials = true;
                    return Promise.resolve({});
                },
                saveCredentials : function(credentials) {
                    credentials.should.be.true();
                },
                getSettings : function() {
                    calledFlagGetSettings = true;
                },
                saveSettings : function(settings) {
                    settings.should.be.true();
                },
                getSessions : function() {
                    calledFlagGetSessions = true;
                },
                saveSessions : function(sessions) {
                    sessions.should.be.true();
                },
                getAllFlows : function() {
                    calledFlagGetAllFlows = true;
                },
                getFlow : function(fn) {
                    fn.should.equal("name");
                },
                saveFlow : function(fn, data) {
                    fn.should.equal("name");
                    data.should.be.true();
                },
                getLibraryEntry : function(type, path) {
                    type.should.be.true();
                    path.should.equal("name");
                },
                saveLibraryEntry : function(type, path, meta, body) {
                    type.should.be.true();
                    path.should.equal("name");
                    meta.should.be.true();
                    body.should.be.true();
                }
        };

        var moduleToLoad = {
            settings: {
                storageModule : interfaceCheckerModule
            }
        };

        var promises = [];
        storage.init(moduleToLoad);
        promises.push(storage.getFlows());
        promises.push(storage.saveFlows({flows:[],credentials:{}}));
        storage.getSettings();
        storage.saveSettings(true);
        storage.getSessions();
        storage.saveSessions(true);
        storage.getAllFlows();
        storage.getFlow("name");
        storage.saveFlow("name", true);
        storage.getLibraryEntry(true, "name");
        storage.saveLibraryEntry(true, "name", true, true);

        Promise.all(promises).then(function() {
            try {
                calledInit.should.be.true();
                calledFlagGetFlows.should.be.true();
                calledFlagGetCredentials.should.be.true();
                calledFlagGetAllFlows.should.be.true();
                done();
            } catch(err) {
                done(err);
            }
        });
    });

    describe('respects deprecated flow library functions', function() {

        var savePath;
        var saveContent;
        var saveMeta;
        var saveType;

        var interfaceCheckerModule = {
                init : function (settings) {
                    settings.should.be.an.Object();
                },
                getLibraryEntry : function(type, path) {
                    if (type === "flows") {
                        if (path === "/" || path === "\\") {
                            return Promise.resolve(["a",{fn:"test.json"}]);
                        } else if (path == "/a" || path == "\\a") {
                            return Promise.resolve([{fn:"test2.json"}]);
                        } else if (path == paff.join("","a","test2.json")) {
                            return Promise.resolve("test content");
                        }
                    }
                },
                saveLibraryEntry : function(type, path, meta, body) {
                    saveType = type;
                    savePath = path;
                    saveContent = body;
                    saveMeta = meta;
                    return Promise.resolve();
                }
        };

        var moduleToLoad = {
            settings: {
                storageModule : interfaceCheckerModule
            }
        };
        before(function() {
            storage.init(moduleToLoad);
        });
        it('getAllFlows',function(done) {
            storage.getAllFlows().then(function (res) {
                try {
                    res.should.eql({ d: { a: { f: ['test2'] } }, f: [ 'test' ] });
                    done();
                } catch(err) {
                    done(err);
                }
            });
        });

        it('getFlow',function(done) {
            storage.getFlow(paff.join("a","test2.json")).then(function(res) {
                try {
                    res.should.eql("test content");
                    done();
                } catch(err) {
                    done(err);
                }
            });
        });

        it ('saveFlow', function (done) {
            storage.saveFlow(paff.join("a","test2.json"),"new content").then(function(res) {
                try {
                    savePath.should.eql(paff.join("a","test2.json"));
                    saveContent.should.eql("new content");
                    saveMeta.should.eql({});
                    saveType.should.eql("flows");
                    done();
                } catch(err) {
                    done(err);
                }
            });

        });
    });

    describe('handles missing settings/sessions interface', function() {
        before(function() {
            var interfaceCheckerModule = {
                init : function () {}
            };
            storage.init({settings:{storageModule: interfaceCheckerModule}});
        });

        it('defaults missing getSettings',function(done) {
            storage.getSettings().then(function(settings) {
                should.not.exist(settings);
                done();
            });
        });
        it('defaults missing saveSettings',function(done) {
            storage.saveSettings({}).then(function() {
                done();
            });
        });
        it('defaults missing getSessions',function(done) {
            storage.getSessions().then(function(settings) {
                should.not.exist(settings);
                done();
            });
        });
        it('defaults missing saveSessions',function(done) {
            storage.saveSessions({}).then(function() {
                done();
            });
        });
    });

});
