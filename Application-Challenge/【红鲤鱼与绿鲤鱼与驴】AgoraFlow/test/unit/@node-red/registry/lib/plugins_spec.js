
const should = require("should");
const sinon = require("sinon");
const path = require("path");

const NR_TEST_UTILS = require("nr-test-utils");

const plugins = NR_TEST_UTILS.require("@node-red/registry/lib/plugins");
const registry = NR_TEST_UTILS.require("@node-red/registry/lib/registry");
const { events } = NR_TEST_UTILS.require("@node-red/util");

describe("red/nodes/registry/plugins",function() {
    let receivedEvents = [];
    let modules;
    function handleEvent(evnt) {
        receivedEvents.push(evnt);
    }
    beforeEach(function() {
        plugins.init({});
        receivedEvents = [];
        modules = {
            "test-module": {
                plugins: {
                    "test-set": {
                        id: "test-module/test-set",
                        enabled: true,
                        config: "test-module-config",
                        plugins: []
                    },
                    "test-disabled-set": {
                        id: "test-module/test-disabled-set",
                        enabled: false,
                        config: "disabled-plugin-config",
                        plugins: []
                    }
                }
            }
        }
        events.on("registry:plugin-added",handleEvent);
        sinon.stub(registry,"getModule", moduleId => modules[moduleId]);
        sinon.stub(registry,"getModuleList", () => modules)
    });
    afterEach(function() {
        events.removeListener("registry:plugin-added",handleEvent);
        registry.getModule.restore();
        registry.getModuleList.restore();
    })

    describe("registerPlugin", function() {
        it("registers a plugin", function() {
            let pluginDef = {}
            plugins.registerPlugin("test-module/test-set","a-plugin",pluginDef);
            receivedEvents.length.should.eql(1);
            receivedEvents[0].should.eql("a-plugin");
            should.exist(modules['test-module'].plugins['test-set'].plugins[0])
            modules['test-module'].plugins['test-set'].plugins[0].should.equal(pluginDef)
        })
        it("calls a plugins onadd function", function() {
            let pluginDef = { onadd: sinon.stub() }
            plugins.registerPlugin("test-module/test-set","a-plugin",pluginDef);
            pluginDef.onadd.called.should.be.true();
        })
    })

    describe("getPlugin", function() {
        it("returns a registered plugin", function() {
            let pluginDef = {}
            plugins.registerPlugin("test-module/test-set","a-plugin",pluginDef);
            pluginDef.should.equal(plugins.getPlugin("a-plugin"));
        })
    })
    describe("getPluginsByType", function() {
        it("returns a plugins of a given type", function() {
            let pluginDef = {type: "foo"}
            let pluginDef2 = {type: "bar"}
            let pluginDef3 = {type: "foo"}
            plugins.registerPlugin("test-module/test-set","a-plugin",pluginDef);
            plugins.registerPlugin("test-module/test-set","a-plugin2",pluginDef2);
            plugins.registerPlugin("test-module/test-set","a-plugin3",pluginDef3);

            let fooPlugins = plugins.getPluginsByType("foo");
            let barPlugins = plugins.getPluginsByType("bar");
            let noPlugins = plugins.getPluginsByType("none");

            noPlugins.should.be.of.length(0);

            fooPlugins.should.be.of.length(2);
            fooPlugins.should.containEql(pluginDef);
            fooPlugins.should.containEql(pluginDef3);

            barPlugins.should.be.of.length(1);
            barPlugins.should.containEql(pluginDef2);

        })
    })

    describe("getPluginConfigs", function() {
        it("gets all plugin configs", function() {
            let configs = plugins.getPluginConfigs("en-US");
            configs.should.eql(`
<!-- --- [red-plugin:test-module/test-set] --- -->
test-module-config`)
        })
    })


    describe("getPluginList", function() {
        it("returns a plugins of a given type", function() {
            let pluginDef = {type: "foo"}
            let pluginDef2 = {type: "bar"}
            let pluginDef3 = {type: "foo"}
            plugins.registerPlugin("test-module/test-set","a-plugin",pluginDef);
            plugins.registerPlugin("test-module/test-set","a-plugin2",pluginDef2);
            plugins.registerPlugin("test-module/test-set","a-plugin3",pluginDef3);

            let pluginList = plugins.getPluginList();
            JSON.stringify(pluginList).should.eql(JSON.stringify(
                [
                    {
                        "id": "test-module/test-set",
                        "enabled": true,
                        "local": false,
                        "user": false,
                        "plugins": [
                            {
                                "type": "foo",
                                "id": "a-plugin",
                                "module": "test-module"
                            },
                            {
                                "type": "bar",
                                "id": "a-plugin2",
                                "module": "test-module"
                            },
                            {
                                "type": "foo",
                                "id": "a-plugin3",
                                "module": "test-module"
                            }
                        ]
                    },
                    {
                        "id": "test-module/test-disabled-set",
                        "enabled": false,
                        "local": false,
                        "user": false,
                        "plugins": []
                    }
                ]
            ))
        })
    })


});