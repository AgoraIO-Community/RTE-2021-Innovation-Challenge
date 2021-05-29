    // init: init,
    // register: register,
    // registerSubflow: registerSubflow,
    // checkFlowDependencies: checkFlowDependencies,
    // require: requireModule
    //

const should = require("should");
const sinon = require("sinon");
const fs = require("fs-extra");
const path = require("path");
const os = require("os");

const NR_TEST_UTILS = require("nr-test-utils");
const externalModules = NR_TEST_UTILS.require("@node-red/registry/lib/externalModules");
const exec = NR_TEST_UTILS.require("@node-red/util/lib/exec");

let homeDir;

async function createUserDir() {
    if (!homeDir) {
        homeDir = path.join(os.tmpdir(),"nr-test-"+Math.floor(Math.random()*100000));
    }
    await fs.ensureDir(homeDir);
}

async function setupExternalModulesPackage(dependencies) {
    await fs.ensureDir(path.join(homeDir,"externalModules"))
    await fs.writeFile(path.join(homeDir,"externalModules","package.json"),`{
"name": "Node-RED-External-Modules",
"description": "These modules are automatically installed by Node-RED to use in Function nodes.",
"version": "1.0.0",
"private": true,
"dependencies": ${JSON.stringify(dependencies)}
}`)
}

describe("externalModules api", function() {
    beforeEach(async function() {
        await createUserDir()
    })
    afterEach(async function() {
        await fs.remove(homeDir);
    })
    describe("checkFlowDependencies", function() {
        beforeEach(function() {
            sinon.stub(exec,"run", async function(cmd, args, options) {
                let error;
                if (args[1] === "moduleNotFound") {
                    error = new Error();
                    error.stderr = "E404";
                } else if (args[1] === "moduleVersionNotFound") {
                    error = new Error();
                    error.stderr = "ETARGET";
                } else if (args[1] === "moduleFail") {
                    error = new Error();
                    error.stderr = "Some unexpected install error";
                }
                if (error) {
                    throw error;
                }
            })
        })
        afterEach(function() {
            exec.run.restore();
        })
        it("does nothing when no types are registered",async function() {
            externalModules.init({userDir: homeDir});
            await externalModules.checkFlowDependencies([
                {type: "function", libs:[{module: "foo"}]}
            ])
            exec.run.called.should.be.false();
        });

        it("skips install for modules already installed", async function() {
            externalModules.init({userDir: homeDir});
            externalModules.register("function", "libs");
            await setupExternalModulesPackage({"foo": "1.2.3", "bar":"2.3.4"});
            await externalModules.checkFlowDependencies([
                {type: "function", libs:[{module: "foo"}]}
            ])
            exec.run.called.should.be.false();
        })

        it("skips install for built-in modules", async function() {
            externalModules.init({userDir: homeDir});
            externalModules.register("function", "libs");
            await externalModules.checkFlowDependencies([
                {type: "function", libs:[{module: "fs"}]}
            ])
            exec.run.called.should.be.false();
        })

        it("installs missing modules", async function() {
            externalModules.init({userDir: homeDir});
            externalModules.register("function", "libs");
            fs.existsSync(path.join(homeDir,"externalModules")).should.be.false();
            await externalModules.checkFlowDependencies([
                {type: "function", libs:[{module: "foo"}]}
            ])
            exec.run.called.should.be.true();
            fs.existsSync(path.join(homeDir,"externalModules")).should.be.true();
        })

        it("installs missing modules from inside subflow module", async function() {
            externalModules.init({userDir: homeDir});
            externalModules.register("function", "libs");
            externalModules.registerSubflow("sf", {"flow":[{type: "function", libs:[{module: "foo"}]}]});
            await externalModules.checkFlowDependencies([
                {type: "sf"}
            ])
            exec.run.called.should.be.true();
        })

        it("reports install fail - 404", async function() {
            externalModules.init({userDir: homeDir});
            externalModules.register("function", "libs");
            try {
                await externalModules.checkFlowDependencies([
                    {type: "function", libs:[{module: "moduleNotFound"}]}
                ])
                throw new Error("checkFlowDependencies did not reject after install fail")
            } catch(err) {
                exec.run.called.should.be.true();
                Array.isArray(err).should.be.true();
                err.should.have.length(1);
                err[0].should.have.property("module");
                err[0].module.should.have.property("module","moduleNotFound");
                err[0].should.have.property("error");
                err[0].error.should.have.property("code",404);

            }
        })
        it("reports install fail - target", async function() {
            externalModules.init({userDir: homeDir});
            externalModules.register("function", "libs");
            try {
                await externalModules.checkFlowDependencies([
                    {type: "function", libs:[{module: "moduleVersionNotFound"}]}
                ])
                throw new Error("checkFlowDependencies did not reject after install fail")
            } catch(err) {
                exec.run.called.should.be.true();
                Array.isArray(err).should.be.true();
                err.should.have.length(1);
                err[0].should.have.property("module");
                err[0].module.should.have.property("module","moduleVersionNotFound");
                err[0].should.have.property("error");
                err[0].error.should.have.property("code",404);
            }
        })

        it("reports install fail - unexpected", async function() {
            externalModules.init({userDir: homeDir});
            externalModules.register("function", "libs");
            try {
                await externalModules.checkFlowDependencies([
                    {type: "function", libs:[{module: "moduleFail"}]}
                ])
                throw new Error("checkFlowDependencies did not reject after install fail")
            } catch(err) {
                exec.run.called.should.be.true();
                Array.isArray(err).should.be.true();
                err.should.have.length(1);
                err[0].should.have.property("module");
                err[0].module.should.have.property("module","moduleFail");
                err[0].should.have.property("error");
                err[0].error.should.have.property("code","unexpected_error");
            }
        })
        it("reports install fail - multiple", async function() {
            externalModules.init({userDir: homeDir});
            externalModules.register("function", "libs");
            try {
                await externalModules.checkFlowDependencies([
                    {type: "function", libs:[{module: "moduleNotFound"},{module: "moduleFail"}]}
                ])
                throw new Error("checkFlowDependencies did not reject after install fail")
            } catch(err) {
                exec.run.called.should.be.true();
                Array.isArray(err).should.be.true();
                err.should.have.length(2);
                // Sort the array so we know the order to test for
                err.sort(function(A,B) {
                    return A.module.module.localeCompare(B.module.module);
                })
                err[1].should.have.property("module");
                err[1].module.should.have.property("module","moduleNotFound");
                err[1].should.have.property("error");
                err[1].error.should.have.property("code",404);
                err[0].should.have.property("module");
                err[0].module.should.have.property("module","moduleFail");
                err[0].should.have.property("error");
                err[0].error.should.have.property("code","unexpected_error");

            }
        })
        it("reports install fail - install disabled", async function() {
            externalModules.init({userDir: homeDir, externalModules: {
                modules: {
                    allowInstall: false
                }
            }});
            externalModules.register("function", "libs");
            try {
                await externalModules.checkFlowDependencies([
                    {type: "function", libs:[{module: "foo"}]}
                ])
                throw new Error("checkFlowDependencies did not reject after install fail")
            } catch(err) {
                // Should not try to install
                exec.run.called.should.be.false();
                Array.isArray(err).should.be.true();
                err.should.have.length(1);
                err[0].should.have.property("module");
                err[0].module.should.have.property("module","foo");
                err[0].should.have.property("error");
                err[0].error.should.have.property("code","install_not_allowed");
            }
        })

        it("reports install fail - module disallowed", async function() {
            externalModules.init({userDir: homeDir, externalModules: {
                modules: {
                    denyList: ['foo']
                }
            }});
            externalModules.register("function", "libs");
            try {
                await externalModules.checkFlowDependencies([
                    // foo disallowed
                    // bar allowed
                    {type: "function", libs:[{module: "foo"},{module: "bar"}]}
                ])
                throw new Error("checkFlowDependencies did not reject after install fail")
            } catch(err) {
                exec.run.calledOnce.should.be.true();
                Array.isArray(err).should.be.true();
                err.should.have.length(1);
                err[0].should.have.property("module");
                err[0].module.should.have.property("module","foo");
                err[0].should.have.property("error");
                err[0].error.should.have.property("code","install_not_allowed");
            }
        })

        it("reports install fail - built-in module disallowed", async function() {
            externalModules.init({userDir: homeDir, externalModules: {
                modules: {
                    denyList: ['fs']
                }
            }});
            externalModules.register("function", "libs");
            try {
                await externalModules.checkFlowDependencies([
                    // foo disallowed
                    // bar allowed
                    {type: "function", libs:[{module: "fs"},{module: "bar"}]}
                ])
                throw new Error("checkFlowDependencies did not reject after install fail")
            } catch(err) {
                exec.run.calledOnce.should.be.true();
                Array.isArray(err).should.be.true();
                err.should.have.length(1);
                err[0].should.have.property("module");
                err[0].module.should.have.property("module","fs");
                err[0].should.have.property("error");
                err[0].error.should.have.property("code","module_not_allowed");
            }
        })
    })
    describe("require", async function() {
        it("requires built-in modules", async function() {
            externalModules.init({userDir: homeDir});
            const result = externalModules.require("fs")
            result.should.eql(require("fs"));
        })
        it("rejects unknown modules", async function() {
            externalModules.init({userDir: homeDir});
            try {
                externalModules.require("foo")
                throw new Error("require did not reject after fail")
            } catch(err) {
                err.should.have.property("code","module_not_allowed");
            }
        })

        it("rejects disallowed modules", async function() {
            externalModules.init({userDir: homeDir, externalModules: {
                modules: {
                    denyList: ['fs']
                }
            }});
            try {
                externalModules.require("fs")
                throw new Error("require did not reject after fail")
            } catch(err) {
                err.should.have.property("code","module_not_allowed");
            }
        })
    })
});