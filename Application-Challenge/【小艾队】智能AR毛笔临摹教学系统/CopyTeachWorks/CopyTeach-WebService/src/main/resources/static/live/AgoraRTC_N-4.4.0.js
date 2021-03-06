/**
 * AgoraWebSDK_N-v4.4.0-0-g48538343 Copyright AgoraInc.
 */

/*
 Determine if an object is a Buffer

 @author   Feross Aboukhadijeh <https://feross.org>
 @license  MIT
 *****************************************************************************
 Copyright (c) Microsoft Corporation. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 this file except in compliance with the License. You may obtain a copy of the
 License at http://www.apache.org/licenses/LICENSE-2.0

 THIS CODE IS PROVIDED ON AN *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION ANY IMPLIED
 WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE,
 MERCHANTABLITY OR NON-INFRINGEMENT.

 See the Apache Version 2.0 License for specific language governing permissions
 and limitations under the License.
*****************************************************************************/
'use strict';
!function (wb, Fb) {
    "object" == typeof exports && "undefined" != typeof module ? module.exports = Fb() : "function" == typeof define && define.amd ? define(Fb) : (wb = "undefined" != typeof globalThis ? globalThis : wb || self).AgoraRTC = Fb()
}(this, function () {
    function wb(d, f, a) {
        return d(a = {
            path: f, exports: {}, require: function (a, c) {
                throw Error("Dynamic requires are not currently supported by @rollup/plugin-commonjs");
            }
        }, a.exports), a.exports
    }

    function Fb(d, f, a) {
        return (d = d.match(f)) && d.length >= a && oa(d[a], 10)
    }

    function hc(d,
                f, a) {
        if (d.RTCPeerConnection) {
            d = d.RTCPeerConnection.prototype;
            var b = d.addEventListener;
            d.addEventListener = function (c, d) {
                if (c !== f) return b.apply(this, arguments);
                let e = b => {
                    (b = a(b)) && d(b)
                };
                return this._eventMap = this._eventMap || {}, this._eventMap[d] = e, b.apply(this, [c, e])
            };
            var c = d.removeEventListener;
            d.removeEventListener = function (a, b) {
                if (a !== f || !this._eventMap || !this._eventMap[b]) return c.apply(this, arguments);
                let d = this._eventMap[b];
                return delete this._eventMap[b], c.apply(this, [a, d])
            };
            X(d, "on" + f, {
                get() {
                    return this["_on" +
                    f]
                }, set(a) {
                    this["_on" + f] && (this.removeEventListener(f, this["_on" + f]), delete this["_on" + f]);
                    a && this.addEventListener(f, this["_on" + f] = a)
                }, enumerable: !0, configurable: !0
            })
        }
    }

    function Xk(d) {
        return "boolean" != typeof d ? Error("Argument type: " + typeof d + ". Please use a boolean.") : (cg = d, d ? "adapter.js logging disabled" : "adapter.js logging enabled")
    }

    function Yk(d) {
        return "boolean" != typeof d ? Error("Argument type: " + typeof d + ". Please use a boolean.") : (dg = !d, "adapter.js deprecation warnings " + (d ? "disabled" : "enabled"))
    }

    function mb() {
        "object" != typeof window || cg || "undefined" != typeof console && "function" == typeof console.log && console.log.apply(console, arguments)
    }

    function qd(d, f) {
        dg && console.warn(d + " is deprecated, please use " + f + " instead.")
    }

    function Gb(d) {
        let {navigator: f} = d, a = {browser: null, version: null};
        if (void 0 === d || !d.navigator) return a.browser = "Not a browser.", a;
        if (f.mozGetUserMedia) a.browser = "firefox", a.version = Fb(f.userAgent, /Firefox\/(\d+)\./, 1); else if (f.webkitGetUserMedia || !1 === d.isSecureContext && d.webkitRTCPeerConnection &&
            !d.RTCIceGatherer) a.browser = "chrome", a.version = Fb(f.userAgent, /Chrom(e|ium)\/(\d+)\./, 2); else if (f.mediaDevices && f.userAgent.match(/Edge\/(\d+).(\d+)$/)) a.browser = "edge", a.version = Fb(f.userAgent, /Edge\/(\d+).(\d+)$/, 2); else {
            if (!d.RTCPeerConnection || !f.userAgent.match(/AppleWebKit\/(\d+)\./)) return a.browser = "Not a supported browser.", a;
            a.browser = "safari";
            a.version = Fb(f.userAgent, /AppleWebKit\/(\d+)\./, 1);
            a.supportsUnifiedPlan = d.RTCRtpTransceiver && "currentDirection" in d.RTCRtpTransceiver.prototype
        }
        return a
    }

    function eg(d) {
        var f;
        return "[object Object]" === Object.prototype.toString.call(d) ? rd(f = aa(d)).call(f, function (a, b) {
            var c = "[object Object]" === Object.prototype.toString.call(d[b]);
            let e = c ? eg(d[b]) : d[b];
            c = c && !aa(e).length;
            return void 0 === e || c ? a : Ha(a, {[b]: e})
        }, {}) : d
    }

    function fg(d, f, a) {
        let b = a ? "outbound-rtp" : "inbound-rtp", c = new ba;
        if (null === f) return c;
        let e = [];
        return q(d).call(d, a => {
            "track" === a.type && a.trackIdentifier === f.id && e.push(a)
        }), q(e).call(e, a => {
            q(d).call(d, e => {
                e.type === b && e.trackId === a.id && function sa(a,
                                                                  b, c) {
                    var d;
                    b && !c.has(b.id) && (c.set(b.id, b), q(d = aa(b)).call(d, d => {
                        if (gg(d).call(d, "Id")) sa(a, a.get(b[d]), c); else if (gg(d).call(d, "Ids")) {
                            var e;
                            q(e = b[d]).call(e, b => {
                                sa(a, a.get(b), c)
                            })
                        }
                    }))
                }(d, e, c)
            })
        }), c
    }

    function hg(d) {
        let f = d && d.navigator;
        if (f.mediaDevices) {
            var a = Gb(d), b = function (a) {
                var b;
                if ("object" != typeof a || a.mandatory || a.optional) return a;
                const c = {};
                var d;
                (q(b = aa(a)).call(b, b => {
                    if ("require" !== b && "advanced" !== b && "mediaSource" !== b) {
                        var d = "object" == typeof a[b] ? a[b] : {ideal: a[b]};
                        void 0 !== d.exact && "number" ==
                        typeof d.exact && (d.min = d.max = d.exact);
                        var e = function (a, b) {
                            return a ? a + b.charAt(0).toUpperCase() + Aa(b).call(b, 1) : "deviceId" === b ? "sourceId" : b
                        };
                        if (void 0 !== d.ideal) {
                            c.optional = c.optional || [];
                            let a = {};
                            "number" == typeof d.ideal ? (a[e("min", b)] = d.ideal, c.optional.push(a), a = {}, a[e("max", b)] = d.ideal, c.optional.push(a)) : (a[e("", b)] = d.ideal, c.optional.push(a))
                        }
                        var g;
                        void 0 !== d.exact && "number" != typeof d.exact ? (c.mandatory = c.mandatory || {}, c.mandatory[e("", b)] = d.exact) : q(g = ["min", "max"]).call(g, a => {
                            void 0 !== d[a] &&
                            (c.mandatory = c.mandatory || {}, c.mandatory[e(a, b)] = d[a])
                        })
                    }
                }), a.advanced) && (c.optional = l(d = c.optional || []).call(d, a.advanced));
                return c
            }, c = function (c, d) {
                if (61 <= a.version) return d(c);
                if ((c = JSON.parse(A(c))) && "object" == typeof c.audio) {
                    var e = function (a, b, c) {
                        b in a && !(c in a) && (a[c] = a[b], delete a[b])
                    };
                    e((c = JSON.parse(A(c))).audio, "autoGainControl", "googAutoGainControl");
                    e(c.audio, "noiseSuppression", "googNoiseSuppression");
                    c.audio = b(c.audio)
                }
                if (c && "object" == typeof c.video) {
                    let g = c.video.facingMode;
                    g = g &&
                        ("object" == typeof g ? g : {ideal: g});
                    e = 66 > a.version;
                    if (!(!g || "user" !== g.exact && "environment" !== g.exact && "user" !== g.ideal && "environment" !== g.ideal || f.mediaDevices.getSupportedConstraints && f.mediaDevices.getSupportedConstraints().facingMode && !e)) {
                        let a;
                        if (delete c.video.facingMode, "environment" === g.exact || "environment" === g.ideal ? a = ["back", "rear"] : "user" !== g.exact && "user" !== g.ideal || (a = ["front"]), a) return f.mediaDevices.enumerateDevices().then(e => {
                            e = N(e).call(e, a => "videoinput" === a.kind);
                            let h = R(e).call(e,
                                b => ig(a).call(a, a => {
                                    var c;
                                    return ya(c = b.label.toLowerCase()).call(c, a)
                                }));
                            return !h && e.length && ya(a).call(a, "back") && (h = e[e.length - 1]), h && (c.video.deviceId = g.exact ? {exact: h.deviceId} : {ideal: h.deviceId}), c.video = b(c.video), jg("chrome: " + A(c)), d(c)
                        })
                    }
                    c.video = b(c.video)
                }
                return jg("chrome: " + A(c)), d(c)
            }, e = function (b) {
                return 64 <= a.version ? b : {
                    name: {
                        PermissionDeniedError: "NotAllowedError",
                        PermissionDismissedError: "NotAllowedError",
                        InvalidStateError: "NotAllowedError",
                        DevicesNotFoundError: "NotFoundError",
                        ConstraintNotSatisfiedError: "OverconstrainedError",
                        TrackStartError: "NotReadableError",
                        MediaDeviceFailedDueToShutdown: "NotAllowedError",
                        MediaDeviceKillSwitchOn: "NotAllowedError",
                        TabCaptureError: "AbortError",
                        ScreenCaptureError: "AbortError",
                        DeviceCaptureError: "AbortError"
                    }[b.name] || b.name, message: b.message, constraint: b.constraint || b.constraintName, toString() {
                        return this.name + (this.message && ": ") + this.message
                    }
                }
            };
            d = function (a, b, d) {
                c(a, a => {
                    f.webkitGetUserMedia(a, b, a => {
                        d && d(e(a))
                    })
                })
            };
            if (f.getUserMedia = ta(d).call(d, f), f.mediaDevices.getUserMedia) {
                var g;
                let a = ta(g = f.mediaDevices.getUserMedia).call(g, f.mediaDevices);
                f.mediaDevices.getUserMedia = function (b) {
                    return c(b, b => a(b).then(a => {
                        var c;
                        if (b.audio && !a.getAudioTracks().length || b.video && !a.getVideoTracks().length) throw q(c = a.getTracks()).call(c, a => {
                            a.stop()
                        }), new DOMException("", "NotFoundError");
                        return a
                    }, a => v.reject(e(a))))
                }
            }
        }
    }

    function kg(d) {
        d.MediaStream = d.MediaStream || d.webkitMediaStream
    }

    function lg(d) {
        if ("object" != typeof d || !d.RTCPeerConnection || "ontrack" in d.RTCPeerConnection.prototype) hc(d,
            "track", d => (d.transceiver || X(d, "transceiver", {value: {receiver: d.receiver}}), d)); else {
            X(d.RTCPeerConnection.prototype, "ontrack", {
                get() {
                    return this._ontrack
                }, set(a) {
                    this._ontrack && this.removeEventListener("track", this._ontrack);
                    this.addEventListener("track", this._ontrack = a)
                }, enumerable: !0, configurable: !0
            });
            let f = d.RTCPeerConnection.prototype.setRemoteDescription;
            d.RTCPeerConnection.prototype.setRemoteDescription = function () {
                return this._ontrackpoly || (this._ontrackpoly = a => {
                    var b;
                    a.stream.addEventListener("addtrack",
                        b => {
                            let c;
                            var g;
                            d.RTCPeerConnection.prototype.getReceivers ? c = R(g = this.getReceivers()).call(g, a => a.track && a.track.id === b.track.id) : c = {track: b.track};
                            g = new Event("track");
                            g.track = b.track;
                            g.receiver = c;
                            g.transceiver = {receiver: c};
                            g.streams = [a.stream];
                            this.dispatchEvent(g)
                        });
                    q(b = a.stream.getTracks()).call(b, b => {
                        let c;
                        var g;
                        d.RTCPeerConnection.prototype.getReceivers ? c = R(g = this.getReceivers()).call(g, a => a.track && a.track.id === b.id) : c = {track: b};
                        g = new Event("track");
                        g.track = b;
                        g.receiver = c;
                        g.transceiver = {receiver: c};
                        g.streams = [a.stream];
                        this.dispatchEvent(g)
                    })
                }, this.addEventListener("addstream", this._ontrackpoly)), f.apply(this, arguments)
            }
        }
    }

    function mg(d) {
        if ("object" == typeof d && d.RTCPeerConnection && !("getSenders" in d.RTCPeerConnection.prototype) && "createDTMFSender" in d.RTCPeerConnection.prototype) {
            let f = function (a, b) {
                return {
                    track: b, get dtmf() {
                        return void 0 === this._dtmf && ("audio" === b.kind ? this._dtmf = a.createDTMFSender(b) : this._dtmf = null), this._dtmf
                    }, _pc: a
                }
            };
            if (!d.RTCPeerConnection.prototype.getSenders) {
                d.RTCPeerConnection.prototype.getSenders =
                    function () {
                        var a;
                        return this._senders = this._senders || [], Aa(a = this._senders).call(a)
                    };
                let a = d.RTCPeerConnection.prototype.addTrack;
                d.RTCPeerConnection.prototype.addTrack = function (b, c) {
                    let d = a.apply(this, arguments);
                    return d || (d = f(this, b), this._senders.push(d)), d
                };
                let b = d.RTCPeerConnection.prototype.removeTrack;
                d.RTCPeerConnection.prototype.removeTrack = function (a) {
                    var c;
                    b.apply(this, arguments);
                    let d = G(c = this._senders).call(c, a);
                    var e;
                    -1 !== d && Ia(e = this._senders).call(e, d, 1)
                }
            }
            let a = d.RTCPeerConnection.prototype.addStream;
            d.RTCPeerConnection.prototype.addStream = function (b) {
                var c;
                this._senders = this._senders || [];
                a.apply(this, [b]);
                q(c = b.getTracks()).call(c, a => {
                    this._senders.push(f(this, a))
                })
            };
            let b = d.RTCPeerConnection.prototype.removeStream;
            d.RTCPeerConnection.prototype.removeStream = function (a) {
                var c;
                this._senders = this._senders || [];
                b.apply(this, [a]);
                q(c = a.getTracks()).call(c, a => {
                    var b;
                    let c = R(b = this._senders).call(b, b => b.track === a);
                    var d, e;
                    c && Ia(d = this._senders).call(d, G(e = this._senders).call(e, c), 1)
                })
            }
        } else if ("object" ==
            typeof d && d.RTCPeerConnection && "getSenders" in d.RTCPeerConnection.prototype && "createDTMFSender" in d.RTCPeerConnection.prototype && d.RTCRtpSender && !("dtmf" in d.RTCRtpSender.prototype)) {
            let f = d.RTCPeerConnection.prototype.getSenders;
            d.RTCPeerConnection.prototype.getSenders = function () {
                let a = f.apply(this, []);
                return q(a).call(a, a => a._pc = this), a
            };
            X(d.RTCRtpSender.prototype, "dtmf", {
                get() {
                    return void 0 === this._dtmf && ("audio" === this.track.kind ? this._dtmf = this._pc.createDTMFSender(this.track) : this._dtmf = null),
                        this._dtmf
                }
            })
        }
    }

    function ng(d) {
        if (d.RTCPeerConnection) {
            var f = d.RTCPeerConnection.prototype.getStats;
            d.RTCPeerConnection.prototype.getStats = function () {
                let [a, b, c] = arguments;
                if (0 < arguments.length && "function" == typeof a) return f.apply(this, arguments);
                if (0 === f.length && (0 === arguments.length || "function" != typeof a)) return f.apply(this, []);
                let d = function (a) {
                    const b = {};
                    a = a.result();
                    return q(a).call(a, a => {
                        var c;
                        const d = {
                            id: a.id,
                            timestamp: a.timestamp,
                            type: {localcandidate: "local-candidate", remotecandidate: "remote-candidate"}[a.type] ||
                                a.type
                        };
                        q(c = a.names()).call(c, b => {
                            d[b] = a.stat(b)
                        });
                        b[d.id] = d
                    }), b
                }, g = function (a) {
                    var b;
                    return new ba(C(b = aa(a)).call(b, b => [b, a[b]]))
                };
                return 2 <= arguments.length ? f.apply(this, [function (a) {
                    b(g(d(a)))
                }, a]) : (new v((a, b) => {
                    f.apply(this, [function (b) {
                        a(g(d(b)))
                    }, b])
                })).then(b, c)
            }
        }
    }

    function og(d) {
        if ("object" == typeof d && d.RTCPeerConnection && d.RTCRtpSender && d.RTCRtpReceiver) {
            if (!("getStats" in d.RTCRtpSender.prototype)) {
                let a = d.RTCPeerConnection.prototype.getSenders;
                a && (d.RTCPeerConnection.prototype.getSenders =
                    function () {
                        let b = a.apply(this, []);
                        return q(b).call(b, a => a._pc = this), b
                    });
                let b = d.RTCPeerConnection.prototype.addTrack;
                b && (d.RTCPeerConnection.prototype.addTrack = function () {
                    let a = b.apply(this, arguments);
                    return a._pc = this, a
                });
                d.RTCRtpSender.prototype.getStats = function () {
                    let a = this;
                    return this._pc.getStats().then(b => fg(b, a.track, !0))
                }
            }
            if (!("getStats" in d.RTCRtpReceiver.prototype)) {
                let a = d.RTCPeerConnection.prototype.getReceivers;
                a && (d.RTCPeerConnection.prototype.getReceivers = function () {
                    let b = a.apply(this,
                        []);
                    return q(b).call(b, a => a._pc = this), b
                });
                hc(d, "track", a => (a.receiver._pc = a.srcElement, a));
                d.RTCRtpReceiver.prototype.getStats = function () {
                    let a = this;
                    return this._pc.getStats().then(b => fg(b, a.track, !1))
                }
            }
            if ("getStats" in d.RTCRtpSender.prototype && "getStats" in d.RTCRtpReceiver.prototype) {
                var f = d.RTCPeerConnection.prototype.getStats;
                d.RTCPeerConnection.prototype.getStats = function () {
                    if (0 < arguments.length && arguments[0] instanceof d.MediaStreamTrack) {
                        var a, b;
                        let c = arguments[0], d, g, h;
                        return q(a = this.getSenders()).call(a,
                            a => {
                                a.track === c && (d ? h = !0 : d = a)
                            }), q(b = this.getReceivers()).call(b, a => (a.track === c && (g ? h = !0 : g = a), a.track === c)), h || d && g ? v.reject(new DOMException("There are more than one sender or receiver for the track.", "InvalidAccessError")) : d ? d.getStats() : g ? g.getStats() : v.reject(new DOMException("There is no sender or receiver for the track.", "InvalidAccessError"))
                    }
                    return f.apply(this, arguments)
                }
            }
        }
    }

    function pg(d) {
        d.RTCPeerConnection.prototype.getLocalStreams = function () {
            var a;
            return this._shimmedLocalStreams = this._shimmedLocalStreams ||
                {}, C(a = aa(this._shimmedLocalStreams)).call(a, a => this._shimmedLocalStreams[a][0])
        };
        let f = d.RTCPeerConnection.prototype.addTrack;
        d.RTCPeerConnection.prototype.addTrack = function (a, b) {
            var c;
            if (!b) return f.apply(this, arguments);
            this._shimmedLocalStreams = this._shimmedLocalStreams || {};
            let d = f.apply(this, arguments);
            return this._shimmedLocalStreams[b.id] ? -1 === G(c = this._shimmedLocalStreams[b.id]).call(c, d) && this._shimmedLocalStreams[b.id].push(d) : this._shimmedLocalStreams[b.id] = [b, d], d
        };
        let a = d.RTCPeerConnection.prototype.addStream;
        d.RTCPeerConnection.prototype.addStream = function (b) {
            var c, d, e;
            this._shimmedLocalStreams = this._shimmedLocalStreams || {};
            q(c = b.getTracks()).call(c, a => {
                var b;
                if (R(b = this.getSenders()).call(b, b => b.track === a)) throw new DOMException("Track already exists.", "InvalidAccessError");
            });
            let f = this.getSenders();
            a.apply(this, arguments);
            c = N(d = this.getSenders()).call(d, a => -1 === G(f).call(f, a));
            this._shimmedLocalStreams[b.id] = l(e = [b]).call(e, c)
        };
        let b = d.RTCPeerConnection.prototype.removeStream;
        d.RTCPeerConnection.prototype.removeStream =
            function (a) {
                return this._shimmedLocalStreams = this._shimmedLocalStreams || {}, delete this._shimmedLocalStreams[a.id], b.apply(this, arguments)
            };
        let c = d.RTCPeerConnection.prototype.removeTrack;
        d.RTCPeerConnection.prototype.removeTrack = function (a) {
            var b;
            (this._shimmedLocalStreams = this._shimmedLocalStreams || {}, a) && q(b = aa(this._shimmedLocalStreams)).call(b, b => {
                var c;
                let d = G(c = this._shimmedLocalStreams[b]).call(c, a);
                var e;
                -1 !== d && Ia(e = this._shimmedLocalStreams[b]).call(e, d, 1);
                1 === this._shimmedLocalStreams[b].length &&
                delete this._shimmedLocalStreams[b]
            });
            return c.apply(this, arguments)
        }
    }

    function qg(d) {
        function f(a, b) {
            var c;
            let d = b.sdp;
            return q(c = aa(a._reverseStreams || [])).call(c, b => {
                b = a._reverseStreams[b];
                d = d.replace(new RegExp(a._streams[b.id].id, "g"), b.id)
            }), new RTCSessionDescription({type: b.type, sdp: d})
        }

        function a(a, b) {
            var c;
            let d = b.sdp;
            return q(c = aa(a._reverseStreams || [])).call(c, b => {
                b = a._reverseStreams[b];
                d = d.replace(new RegExp(b.id, "g"), a._streams[b.id].id)
            }), new RTCSessionDescription({type: b.type, sdp: d})
        }

        var b;
        if (d.RTCPeerConnection) {
            var c = Gb(d);
            if (d.RTCPeerConnection.prototype.addTrack && 65 <= c.version) return pg(d);
            var e = d.RTCPeerConnection.prototype.getLocalStreams;
            d.RTCPeerConnection.prototype.getLocalStreams = function () {
                let a = e.apply(this);
                return this._reverseStreams = this._reverseStreams || {}, C(a).call(a, a => this._reverseStreams[a.id])
            };
            var g = d.RTCPeerConnection.prototype.addStream;
            d.RTCPeerConnection.prototype.addStream = function (a) {
                var b;
                (this._streams = this._streams || {}, this._reverseStreams = this._reverseStreams ||
                    {}, q(b = a.getTracks()).call(b, a => {
                    var b;
                    if (R(b = this.getSenders()).call(b, b => b.track === a)) throw new DOMException("Track already exists.", "InvalidAccessError");
                }), this._reverseStreams[a.id]) || (b = new d.MediaStream(a.getTracks()), this._streams[a.id] = b, this._reverseStreams[b.id] = a, a = b);
                g.apply(this, [a])
            };
            var h = d.RTCPeerConnection.prototype.removeStream;
            d.RTCPeerConnection.prototype.removeStream = function (a) {
                this._streams = this._streams || {};
                this._reverseStreams = this._reverseStreams || {};
                h.apply(this, [this._streams[a.id] ||
                a]);
                delete this._reverseStreams[this._streams[a.id] ? this._streams[a.id].id : a.id];
                delete this._streams[a.id]
            };
            d.RTCPeerConnection.prototype.addTrack = function (a, b) {
                var c, e, g;
                if ("closed" === this.signalingState) throw new DOMException("The RTCPeerConnection's signalingState is 'closed'.", "InvalidStateError");
                let h = Aa([]).call(arguments, 1);
                if (1 !== h.length || !R(c = h[0].getTracks()).call(c, b => b === a)) throw new DOMException("The adapter.js addTrack polyfill only supports a single  stream which is associated with the specified track.",
                    "NotSupportedError");
                if (R(e = this.getSenders()).call(e, b => b.track === a)) throw new DOMException("Track already exists.", "InvalidAccessError");
                this._streams = this._streams || {};
                this._reverseStreams = this._reverseStreams || {};
                (c = this._streams[b.id]) ? (c.addTrack(a), v.resolve().then(() => {
                    this.dispatchEvent(new Event("negotiationneeded"))
                })) : (c = new d.MediaStream([a]), this._streams[b.id] = c, this._reverseStreams[c.id] = b, this.addStream(c));
                return R(g = this.getSenders()).call(g, b => b.track === a)
            };
            q(b = ["createOffer",
                "createAnswer"]).call(b, function (a) {
                let b = d.RTCPeerConnection.prototype[a];
                d.RTCPeerConnection.prototype[a] = {
                    [a]() {
                        const a = arguments;
                        return arguments.length && "function" == typeof arguments[0] ? b.apply(this, [b => {
                            b = f(this, b);
                            a[0].apply(null, [b])
                        }, b => {
                            a[1] && a[1].apply(null, b)
                        }, arguments[2]]) : b.apply(this, arguments).then(a => f(this, a))
                    }
                }[a]
            });
            var m = d.RTCPeerConnection.prototype.setLocalDescription;
            d.RTCPeerConnection.prototype.setLocalDescription = function () {
                return arguments.length && arguments[0].type ? (arguments[0] =
                    a(this, arguments[0]), m.apply(this, arguments)) : m.apply(this, arguments)
            };
            var r = Y(d.RTCPeerConnection.prototype, "localDescription");
            X(d.RTCPeerConnection.prototype, "localDescription", {
                get() {
                    let a = r.get.apply(this);
                    return "" === a.type ? a : f(this, a)
                }
            });
            d.RTCPeerConnection.prototype.removeTrack = function (a) {
                var b;
                if ("closed" === this.signalingState) throw new DOMException("The RTCPeerConnection's signalingState is 'closed'.", "InvalidStateError");
                if (!a._pc) throw new DOMException("Argument 1 of RTCPeerConnection.removeTrack does not implement interface RTCRtpSender.",
                    "TypeError");
                if (a._pc !== this) throw new DOMException("Sender was not created by this connection.", "InvalidAccessError");
                let c;
                this._streams = this._streams || {};
                q(b = aa(this._streams)).call(b, b => {
                    var d;
                    R(d = this._streams[b].getTracks()).call(d, b => a.track === b) && (c = this._streams[b])
                });
                c && (1 === c.getTracks().length ? this.removeStream(this._reverseStreams[c.id]) : c.removeTrack(a.track), this.dispatchEvent(new Event("negotiationneeded")))
            }
        }
    }

    function me(d) {
        let f = Gb(d);
        if (!d.RTCPeerConnection && d.webkitRTCPeerConnection &&
        (d.RTCPeerConnection = d.webkitRTCPeerConnection), d.RTCPeerConnection) {
            var a;
            53 > f.version && q(a = ["setLocalDescription", "setRemoteDescription", "addIceCandidate"]).call(a, function (a) {
                let b = d.RTCPeerConnection.prototype[a];
                d.RTCPeerConnection.prototype[a] = {
                    [a]() {
                        return arguments[0] = new ("addIceCandidate" === a ? d.RTCIceCandidate : d.RTCSessionDescription)(arguments[0]), b.apply(this, arguments)
                    }
                }[a]
            });
            var b = d.RTCPeerConnection.prototype.addIceCandidate;
            d.RTCPeerConnection.prototype.addIceCandidate = function () {
                return arguments[0] ?
                    78 > f.version && arguments[0] && "" === arguments[0].candidate ? v.resolve() : b.apply(this, arguments) : (arguments[1] && arguments[1].apply(null), v.resolve())
            }
        }
    }

    function rg(d) {
        hc(d, "negotiationneeded", d => {
            if ("stable" === d.target.signalingState) return d
        })
    }

    function sg(d, f, a, b, c) {
        f = H.writeRtpDescription(d.kind, f);
        if (f += H.writeIceParameters(d.iceGatherer.getLocalParameters()), f += H.writeDtlsParameters(d.dtlsTransport.getLocalParameters(), "offer" === a ? "actpass" : c || "active"), f += "a=mid:" + d.mid + "\r\n", d.rtpSender && d.rtpReceiver ?
            f += "a=sendrecv\r\n" : d.rtpSender ? f += "a=sendonly\r\n" : d.rtpReceiver ? f += "a=recvonly\r\n" : f += "a=inactive\r\n", d.rtpSender) a = d.rtpSender._initialTrackId || d.rtpSender.track.id, d.rtpSender._initialTrackId = a, b = "msid:" + (b ? b.id : "-") + " " + a + "\r\n", f = f + ("a=" + b) + ("a=ssrc:" + d.sendEncodingParameters[0].ssrc + " " + b), d.sendEncodingParameters[0].rtx && (f += "a=ssrc:" + d.sendEncodingParameters[0].rtx.ssrc + " " + b, f += "a=ssrc-group:FID " + d.sendEncodingParameters[0].ssrc + " " + d.sendEncodingParameters[0].rtx.ssrc + "\r\n");
        return f +=
            "a=ssrc:" + d.sendEncodingParameters[0].ssrc + " cname:" + H.localCName + "\r\n", d.rtpSender && d.sendEncodingParameters[0].rtx && (f += "a=ssrc:" + d.sendEncodingParameters[0].rtx.ssrc + " cname:" + H.localCName + "\r\n"), f
    }

    function sd(d, f) {
        var a = {codecs: [], headerExtensions: [], fecMechanisms: []}, b = function (a, b) {
            a = parseInt(a, 10);
            for (var c = 0; c < b.length; c++) if (b[c].payloadType === a || b[c].preferredPayloadType === a) return b[c]
        }, c = function (a, c, d, f) {
            a = b(a.parameters.apt, d);
            c = b(c.parameters.apt, f);
            return a && c && a.name.toLowerCase() ===
                c.name.toLowerCase()
        };
        return d.codecs.forEach(function (b) {
            for (var e = 0; e < f.codecs.length; e++) {
                var h = f.codecs[e];
                if (b.name.toLowerCase() === h.name.toLowerCase() && b.clockRate === h.clockRate && ("rtx" !== b.name.toLowerCase() || !b.parameters || !h.parameters.apt || c(b, h, d.codecs, f.codecs))) {
                    (h = JSON.parse(JSON.stringify(h))).numChannels = Math.min(b.numChannels, h.numChannels);
                    a.codecs.push(h);
                    h.rtcpFeedback = h.rtcpFeedback.filter(function (a) {
                        for (var c = 0; c < b.rtcpFeedback.length; c++) if (b.rtcpFeedback[c].type === a.type &&
                            b.rtcpFeedback[c].parameter === a.parameter) return !0;
                        return !1
                    });
                    break
                }
            }
        }), d.headerExtensions.forEach(function (b) {
            for (var c = 0; c < f.headerExtensions.length; c++) {
                var d = f.headerExtensions[c];
                if (b.uri === d.uri) {
                    a.headerExtensions.push(d);
                    break
                }
            }
        }), a
    }

    function tg(d, f, a) {
        return -1 !== {
            offer: {
                setLocalDescription: ["stable", "have-local-offer"],
                setRemoteDescription: ["stable", "have-remote-offer"]
            },
            answer: {
                setLocalDescription: ["have-remote-offer", "have-local-pranswer"],
                setRemoteDescription: ["have-local-offer", "have-remote-pranswer"]
            }
        }[f][d].indexOf(a)
    }

    function ne(d, f) {
        var a = d.getRemoteCandidates().find(function (a) {
            return f.foundation === a.foundation && f.ip === a.ip && f.port === a.port && f.priority === a.priority && f.protocol === a.protocol && f.type === a.type
        });
        return a || d.addRemoteCandidate(f), !a
    }

    function Ja(d, f) {
        f = Error(f);
        return f.name = d, f.code = {
            NotSupportedError: 9,
            InvalidStateError: 11,
            InvalidAccessError: 15,
            TypeError: void 0,
            OperationError: void 0
        }[d], f
    }

    function ug(d) {
        var f;
        d = d && d.navigator;
        let a = ta(f = d.mediaDevices.getUserMedia).call(f, d.mediaDevices);
        d.mediaDevices.getUserMedia =
            function (b) {
                return a(b).catch(a => v.reject(function (a) {
                    return {
                        name: {PermissionDeniedError: "NotAllowedError"}[a.name] || a.name,
                        message: a.message,
                        constraint: a.constraint,
                        toString() {
                            return this.name
                        }
                    }
                }(a)))
            }
    }

    function vg(d) {
        var f;
        "getDisplayMedia" in d.navigator && d.navigator.mediaDevices && (d.navigator.mediaDevices && "getDisplayMedia" in d.navigator.mediaDevices || (d.navigator.mediaDevices.getDisplayMedia = ta(f = d.navigator.getDisplayMedia).call(f, d.navigator)))
    }

    function oe(d) {
        let f = Gb(d);
        if (d.RTCIceGatherer &&
            (d.RTCIceCandidate || (d.RTCIceCandidate = function (a) {
                return a
            }), d.RTCSessionDescription || (d.RTCSessionDescription = function (a) {
                return a
            }), 15025 > f.version)) {
            let a = Y(d.MediaStreamTrack.prototype, "enabled");
            X(d.MediaStreamTrack.prototype, "enabled", {
                set(b) {
                    a.set.call(this, b);
                    let c = new Event("enabled");
                    c.enabled = b;
                    this.dispatchEvent(c)
                }
            })
        }
        !d.RTCRtpSender || "dtmf" in d.RTCRtpSender.prototype || X(d.RTCRtpSender.prototype, "dtmf", {
            get() {
                return void 0 === this._dtmf && ("audio" === this.track.kind ? this._dtmf = new d.RTCDtmfSender(this) :
                    "video" === this.track.kind && (this._dtmf = null)), this._dtmf
            }
        });
        d.RTCDtmfSender && !d.RTCDTMFSender && (d.RTCDTMFSender = d.RTCDtmfSender);
        let a = Zk(d, f.version);
        d.RTCPeerConnection = function (b) {
            return b && b.iceServers && (b.iceServers = function (a, b) {
                let c = !1;
                return a = JSON.parse(A(a)), N(a).call(a, a => {
                    if (a && (a.urls || a.url)) {
                        var b = a.urls || a.url;
                        a.url && !a.urls && qd("RTCIceServer.url", "RTCIceServer.urls");
                        let d = "string" == typeof b;
                        return d && (b = [b]), b = N(b).call(b, a => 0 === G(a).call(a, "stun:") ? !1 : (a = td(a).call(a, "turn") &&
                            !td(a).call(a, "turn:[") && ya(a).call(a, "transport=udp")) && !c ? (c = !0, !0) : a && !c), delete a.url, a.urls = d ? b[0] : b, !!b.length
                    }
                })
            }(b.iceServers, f.version), mb("ICE servers after filtering:", b.iceServers)), new a(b)
        };
        d.RTCPeerConnection.prototype = a.prototype
    }

    function wg(d) {
        !d.RTCRtpSender || "replaceTrack" in d.RTCRtpSender.prototype || (d.RTCRtpSender.prototype.replaceTrack = d.RTCRtpSender.prototype.setTrack)
    }

    function xg(d) {
        let f = Gb(d), a = d && d.navigator;
        d = d && d.MediaStreamTrack;
        if (a.getUserMedia = function (b, d, g) {
            qd("navigator.getUserMedia",
                "navigator.mediaDevices.getUserMedia");
            a.mediaDevices.getUserMedia(b).then(d, g)
        }, !(55 < f.version && "autoGainControl" in a.mediaDevices.getSupportedConstraints())) {
            var b;
            let c = function (a, b, c) {
                b in a && !(c in a) && (a[c] = a[b], delete a[b])
            }, e = ta(b = a.mediaDevices.getUserMedia).call(b, a.mediaDevices);
            if (a.mediaDevices.getUserMedia = function (a) {
                return "object" == typeof a && "object" == typeof a.audio && (a = JSON.parse(A(a)), c(a.audio, "autoGainControl", "mozAutoGainControl"), c(a.audio, "noiseSuppression", "mozNoiseSuppression")),
                    e(a)
            }, d && d.prototype.getSettings) {
                let a = d.prototype.getSettings;
                d.prototype.getSettings = function () {
                    let b = a.apply(this, arguments);
                    return c(b, "mozAutoGainControl", "autoGainControl"), c(b, "mozNoiseSuppression", "noiseSuppression"), b
                }
            }
            if (d && d.prototype.applyConstraints) {
                let a = d.prototype.applyConstraints;
                d.prototype.applyConstraints = function (b) {
                    return "audio" === this.kind && "object" == typeof b && (b = JSON.parse(A(b)), c(b, "autoGainControl", "mozAutoGainControl"), c(b, "noiseSuppression", "mozNoiseSuppression")), a.apply(this,
                        [b])
                }
            }
        }
    }

    function yg(d) {
        "object" == typeof d && d.RTCTrackEvent && "receiver" in d.RTCTrackEvent.prototype && !("transceiver" in d.RTCTrackEvent.prototype) && X(d.RTCTrackEvent.prototype, "transceiver", {
            get() {
                return {receiver: this.receiver}
            }
        })
    }

    function pe(d) {
        let f = Gb(d);
        if ("object" == typeof d && (d.RTCPeerConnection || d.mozRTCPeerConnection)) {
            var a;
            (!d.RTCPeerConnection && d.mozRTCPeerConnection && (d.RTCPeerConnection = d.mozRTCPeerConnection), 53 > f.version) && q(a = ["setLocalDescription", "setRemoteDescription", "addIceCandidate"]).call(a,
                function (a) {
                    let b = d.RTCPeerConnection.prototype[a];
                    d.RTCPeerConnection.prototype[a] = {
                        [a]() {
                            return arguments[0] = new ("addIceCandidate" === a ? d.RTCIceCandidate : d.RTCSessionDescription)(arguments[0]), b.apply(this, arguments)
                        }
                    }[a]
                });
            var b = d.RTCPeerConnection.prototype.addIceCandidate;
            d.RTCPeerConnection.prototype.addIceCandidate = function () {
                return arguments[0] ? 68 > f.version && arguments[0] && "" === arguments[0].candidate ? v.resolve() : b.apply(this, arguments) : (arguments[1] && arguments[1].apply(null), v.resolve())
            };
            var c = {
                inboundrtp: "inbound-rtp",
                outboundrtp: "outbound-rtp",
                candidatepair: "candidate-pair",
                localcandidate: "local-candidate",
                remotecandidate: "remote-candidate"
            }, e = d.RTCPeerConnection.prototype.getStats;
            d.RTCPeerConnection.prototype.getStats = function () {
                let [a, b, d] = arguments;
                return e.apply(this, [a || null]).then(a => {
                    if (53 > f.version && !b) try {
                        q(a).call(a, a => {
                            a.type = c[a.type] || a.type
                        })
                    } catch (w) {
                        if ("TypeError" !== w.name) throw w;
                        q(a).call(a, (b, d) => {
                            a.set(d, Ha({}, b, {type: c[b.type] || b.type}))
                        })
                    }
                    return a
                }).then(b,
                    d)
            }
        }
    }

    function zg(d) {
        if ("object" == typeof d && d.RTCPeerConnection && d.RTCRtpSender && !(d.RTCRtpSender && "getStats" in d.RTCRtpSender.prototype)) {
            var f = d.RTCPeerConnection.prototype.getSenders;
            f && (d.RTCPeerConnection.prototype.getSenders = function () {
                let a = f.apply(this, []);
                return q(a).call(a, a => a._pc = this), a
            });
            var a = d.RTCPeerConnection.prototype.addTrack;
            a && (d.RTCPeerConnection.prototype.addTrack = function () {
                let b = a.apply(this, arguments);
                return b._pc = this, b
            });
            d.RTCRtpSender.prototype.getStats = function () {
                return this.track ?
                    this._pc.getStats(this.track) : v.resolve(new ba)
            }
        }
    }

    function Ag(d) {
        if ("object" == typeof d && d.RTCPeerConnection && d.RTCRtpSender && !(d.RTCRtpSender && "getStats" in d.RTCRtpReceiver.prototype)) {
            var f = d.RTCPeerConnection.prototype.getReceivers;
            f && (d.RTCPeerConnection.prototype.getReceivers = function () {
                let a = f.apply(this, []);
                return q(a).call(a, a => a._pc = this), a
            });
            hc(d, "track", a => (a.receiver._pc = a.srcElement, a));
            d.RTCRtpReceiver.prototype.getStats = function () {
                return this._pc.getStats(this.track)
            }
        }
    }

    function Bg(d) {
        !d.RTCPeerConnection ||
        "removeStream" in d.RTCPeerConnection.prototype || (d.RTCPeerConnection.prototype.removeStream = function (d) {
            var a;
            qd("removeStream", "removeTrack");
            q(a = this.getSenders()).call(a, a => {
                var b;
                a.track && ya(b = d.getTracks()).call(b, a.track) && this.removeTrack(a)
            })
        })
    }

    function Cg(d) {
        d.DataChannel && !d.RTCDataChannel && (d.RTCDataChannel = d.DataChannel)
    }

    function Dg(d) {
        if ("object" == typeof d && d.RTCPeerConnection) {
            if ("getLocalStreams" in d.RTCPeerConnection.prototype || (d.RTCPeerConnection.prototype.getLocalStreams = function () {
                return this._localStreams ||
                (this._localStreams = []), this._localStreams
            }), !("addStream" in d.RTCPeerConnection.prototype)) {
                let f = d.RTCPeerConnection.prototype.addTrack;
                d.RTCPeerConnection.prototype.addStream = function (a) {
                    var b, c, d;
                    this._localStreams || (this._localStreams = []);
                    ya(b = this._localStreams).call(b, a) || this._localStreams.push(a);
                    q(c = a.getAudioTracks()).call(c, b => f.call(this, b, a));
                    q(d = a.getVideoTracks()).call(d, b => f.call(this, b, a))
                };
                d.RTCPeerConnection.prototype.addTrack = function (a, b) {
                    var c;
                    b && (this._localStreams ? ya(c = this._localStreams).call(c,
                        b) || this._localStreams.push(b) : this._localStreams = [b]);
                    return f.call(this, a, b)
                }
            }
            "removeStream" in d.RTCPeerConnection.prototype || (d.RTCPeerConnection.prototype.removeStream = function (d) {
                var a, b, c;
                this._localStreams || (this._localStreams = []);
                let e = G(a = this._localStreams).call(a, d);
                if (-1 !== e) {
                    Ia(b = this._localStreams).call(b, e, 1);
                    var g = d.getTracks();
                    q(c = this.getSenders()).call(c, a => {
                        ya(g).call(g, a.track) && this.removeTrack(a)
                    })
                }
            })
        }
    }

    function Eg(d) {
        if ("object" == typeof d && d.RTCPeerConnection && ("getRemoteStreams" in
        d.RTCPeerConnection.prototype || (d.RTCPeerConnection.prototype.getRemoteStreams = function () {
            return this._remoteStreams ? this._remoteStreams : []
        }), !("onaddstream" in d.RTCPeerConnection.prototype))) {
            X(d.RTCPeerConnection.prototype, "onaddstream", {
                get() {
                    return this._onaddstream
                }, set(a) {
                    this._onaddstream && (this.removeEventListener("addstream", this._onaddstream), this.removeEventListener("track", this._onaddstreampoly));
                    this.addEventListener("addstream", this._onaddstream = a);
                    this.addEventListener("track", this._onaddstreampoly =
                        a => {
                            var b;
                            q(b = a.streams).call(b, a => {
                                var b;
                                (this._remoteStreams || (this._remoteStreams = []), ya(b = this._remoteStreams).call(b, a)) || (this._remoteStreams.push(a), b = new Event("addstream"), b.stream = a, this.dispatchEvent(b))
                            })
                        })
                }
            });
            let f = d.RTCPeerConnection.prototype.setRemoteDescription;
            d.RTCPeerConnection.prototype.setRemoteDescription = function () {
                let a = this;
                return this._onaddstreampoly || this.addEventListener("track", this._onaddstreampoly = function (b) {
                    var c;
                    q(c = b.streams).call(c, b => {
                        var c;
                        (a._remoteStreams ||
                        (a._remoteStreams = []), 0 <= G(c = a._remoteStreams).call(c, b)) || (a._remoteStreams.push(b), c = new Event("addstream"), c.stream = b, a.dispatchEvent(c))
                    })
                }), f.apply(a, arguments)
            }
        }
    }

    function Fg(d) {
        if ("object" == typeof d && d.RTCPeerConnection) {
            d = d.RTCPeerConnection.prototype;
            var f = d.createOffer, a = d.createAnswer, b = d.setLocalDescription, c = d.setRemoteDescription,
                e = d.addIceCandidate;
            d.createOffer = function (a, b) {
                let c = f.apply(this, [2 <= arguments.length ? arguments[2] : arguments[0]]);
                return b ? (c.then(a, b), v.resolve()) : c
            };
            d.createAnswer =
                function (b, c) {
                    let d = a.apply(this, [2 <= arguments.length ? arguments[2] : arguments[0]]);
                    return c ? (d.then(b, c), v.resolve()) : d
                };
            var g = function (a, c, d) {
                a = b.apply(this, [a]);
                return d ? (a.then(c, d), v.resolve()) : a
            };
            d.setLocalDescription = g;
            g = function (a, b, d) {
                a = c.apply(this, [a]);
                return d ? (a.then(b, d), v.resolve()) : a
            };
            d.setRemoteDescription = g;
            g = function (a, b, c) {
                a = e.apply(this, [a]);
                return c ? (a.then(b, c), v.resolve()) : a
            };
            d.addIceCandidate = g
        }
    }

    function Gg(d) {
        let f = d && d.navigator;
        if (f.mediaDevices && f.mediaDevices.getUserMedia) {
            var a;
            d = f.mediaDevices;
            let b = ta(a = d.getUserMedia).call(a, d);
            f.mediaDevices.getUserMedia = a => b(Hg(a))
        }
        var b;
        !f.getUserMedia && f.mediaDevices && f.mediaDevices.getUserMedia && (f.getUserMedia = ta(b = function (a, b, d) {
            f.mediaDevices.getUserMedia(a).then(b, d)
        }).call(b, f))
    }

    function Hg(d) {
        return d && void 0 !== d.video ? Ha({}, d, {video: eg(d.video)}) : d
    }

    function Ig(d) {
        let f = d.RTCPeerConnection;
        d.RTCPeerConnection = function (a, b) {
            if (a && a.iceServers) {
                let b = [];
                for (let c = 0; c < a.iceServers.length; c++) {
                    let d = a.iceServers[c];
                    !d.hasOwnProperty("urls") &&
                    d.hasOwnProperty("url") ? (qd("RTCIceServer.url", "RTCIceServer.urls"), d = JSON.parse(A(d)), d.urls = d.url, delete d.url, b.push(d)) : b.push(a.iceServers[c])
                }
                a.iceServers = b
            }
            return new f(a, b)
        };
        d.RTCPeerConnection.prototype = f.prototype;
        "generateCertificate" in d.RTCPeerConnection && X(d.RTCPeerConnection, "generateCertificate", {get: () => f.generateCertificate})
    }

    function Jg(d) {
        "object" == typeof d && d.RTCPeerConnection && "receiver" in d.RTCTrackEvent.prototype && !d.RTCTransceiver && X(d.RTCTrackEvent.prototype, "transceiver",
            {
                get() {
                    return {receiver: this.receiver}
                }
            })
    }

    function Kg(d) {
        let f = d.RTCPeerConnection.prototype.createOffer;
        d.RTCPeerConnection.prototype.createOffer = function (a) {
            if (a) {
                var b, c;
                void 0 !== a.offerToReceiveAudio && (a.offerToReceiveAudio = !!a.offerToReceiveAudio);
                let d = R(b = this.getTransceivers()).call(b, a => "audio" === a.receiver.track.kind);
                !1 === a.offerToReceiveAudio && d ? "sendrecv" === d.direction ? d.setDirection ? d.setDirection("sendonly") : d.direction = "sendonly" : "recvonly" === d.direction && (d.setDirection ? d.setDirection("inactive") :
                    d.direction = "inactive") : !0 !== a.offerToReceiveAudio || d || this.addTransceiver("audio");
                void 0 !== a.offerToReceiveVideo && (a.offerToReceiveVideo = !!a.offerToReceiveVideo);
                b = R(c = this.getTransceivers()).call(c, a => "video" === a.receiver.track.kind);
                !1 === a.offerToReceiveVideo && b ? "sendrecv" === b.direction ? b.setDirection ? b.setDirection("sendonly") : b.direction = "sendonly" : "recvonly" === b.direction && (b.setDirection ? b.setDirection("inactive") : b.direction = "inactive") : !0 !== a.offerToReceiveVideo || b || this.addTransceiver("video")
            }
            return f.apply(this,
                arguments)
        }
    }

    function ud(d) {
        if (d.RTCIceCandidate && !(d.RTCIceCandidate && "foundation" in d.RTCIceCandidate.prototype)) {
            var f = d.RTCIceCandidate;
            d.RTCIceCandidate = function (a) {
                var b;
                if ("object" == typeof a && a.candidate && 0 === G(b = a.candidate).call(b, "a=") && ((a = JSON.parse(A(a))).candidate = a.candidate.substr(2)), a.candidate && a.candidate.length) {
                    b = new f(a);
                    a = H.parseCandidate(a.candidate);
                    let c = Ha(b, a);
                    return c.toJSON = function () {
                        return {
                            candidate: c.candidate,
                            sdpMid: c.sdpMid,
                            sdpMLineIndex: c.sdpMLineIndex,
                            usernameFragment: c.usernameFragment
                        }
                    },
                        c
                }
                return new f(a)
            };
            d.RTCIceCandidate.prototype = f.prototype;
            hc(d, "icecandidate", a => (a.candidate && X(a, "candidate", {
                value: new d.RTCIceCandidate(a.candidate),
                writable: "false"
            }), a))
        }
    }

    function Ic(d) {
        if (d.RTCPeerConnection) {
            var f = Gb(d);
            "sctp" in d.RTCPeerConnection.prototype || X(d.RTCPeerConnection.prototype, "sctp", {
                get() {
                    return void 0 === this._sctp ? null : this._sctp
                }
            });
            var a = function (a) {
                if (!a || !a.sdp) return !1;
                a = H.splitSections(a.sdp);
                return a.shift(), ig(a).call(a, a => {
                    var b;
                    return (a = H.parseMLine(a)) && "application" ===
                        a.kind && -1 !== G(b = a.protocol).call(b, "SCTP")
                })
            }, b = function (a) {
                a = a.sdp.match(/mozilla...THIS_IS_SDPARTA-(\d+)/);
                if (null === a || 2 > a.length) return -1;
                a = oa(a[1], 10);
                return a != a ? -1 : a
            }, c = function (a) {
                let b = 65536;
                return "firefox" === f.browser && (b = 57 > f.version ? -1 === a ? 16384 : 2147483637 : 60 > f.version ? 57 === f.version ? 65535 : 65536 : 2147483637), b
            }, e = function (a, b) {
                let c = 65536;
                "firefox" === f.browser && 57 === f.version && (c = 65535);
                a = H.matchPrefix(a.sdp, "a=max-message-size:");
                return 0 < a.length ? c = oa(a[0].substr(19), 10) : "firefox" ===
                    f.browser && -1 !== b && (c = 2147483637), c
            }, g = d.RTCPeerConnection.prototype.setRemoteDescription;
            d.RTCPeerConnection.prototype.setRemoteDescription = function () {
                if (this._sctp = null, "chrome" === f.browser && 76 <= f.version) {
                    var {sdpSemantics: d} = this.getConfiguration();
                    "plan-b" === d && X(this, "sctp", {
                        get() {
                            return void 0 === this._sctp ? null : this._sctp
                        }, enumerable: !0, configurable: !0
                    })
                }
                if (a(arguments[0])) {
                    var m = b(arguments[0]);
                    d = c(m);
                    m = e(arguments[0], m);
                    let a;
                    a = 0 === d && 0 === m ? Number.POSITIVE_INFINITY : 0 === d || 0 === m ? Math.max(d,
                        m) : Math.min(d, m);
                    d = {};
                    X(d, "maxMessageSize", {get: () => a});
                    this._sctp = d
                }
                return g.apply(this, arguments)
            }
        }
    }

    function Jc(d) {
        function f(a, c) {
            let b = a.send;
            a.send = function () {
                var d = arguments[0];
                d = d.length || d.size || d.byteLength;
                if ("open" === a.readyState && c.sctp && d > c.sctp.maxMessageSize) throw new TypeError("Message too large (can send a maximum of " + c.sctp.maxMessageSize + " bytes)");
                return b.apply(a, arguments)
            }
        }

        if (d.RTCPeerConnection && "createDataChannel" in d.RTCPeerConnection.prototype) {
            var a = d.RTCPeerConnection.prototype.createDataChannel;
            d.RTCPeerConnection.prototype.createDataChannel = function () {
                let b = a.apply(this, arguments);
                return f(b, this), b
            };
            hc(d, "datachannel", a => (f(a.channel, a.target), a))
        }
    }

    function qe(d) {
        var f;
        if (d.RTCPeerConnection && !("connectionState" in d.RTCPeerConnection.prototype)) {
            var a = d.RTCPeerConnection.prototype;
            X(a, "connectionState", {
                get() {
                    return {
                        completed: "connected",
                        checking: "connecting"
                    }[this.iceConnectionState] || this.iceConnectionState
                }, enumerable: !0, configurable: !0
            });
            X(a, "onconnectionstatechange", {
                get() {
                    return this._onconnectionstatechange ||
                        null
                }, set(a) {
                    this._onconnectionstatechange && (this.removeEventListener("connectionstatechange", this._onconnectionstatechange), delete this._onconnectionstatechange);
                    a && this.addEventListener("connectionstatechange", this._onconnectionstatechange = a)
                }, enumerable: !0, configurable: !0
            });
            q(f = ["setLocalDescription", "setRemoteDescription"]).call(f, b => {
                let c = a[b];
                a[b] = function () {
                    return this._connectionstatechangepoly || (this._connectionstatechangepoly = a => {
                        let b = a.target;
                        if (b._lastConnectionState !== b.connectionState) {
                            b._lastConnectionState =
                                b.connectionState;
                            let c = new Event("connectionstatechange", a);
                            b.dispatchEvent(c)
                        }
                        return a
                    }, this.addEventListener("iceconnectionstatechange", this._connectionstatechangepoly)), c.apply(this, arguments)
                }
            })
        }
    }

    function re(d) {
        if (d.RTCPeerConnection) {
            var f = Gb(d);
            if (!("chrome" === f.browser && 71 <= f.version)) {
                var a = d.RTCPeerConnection.prototype.setRemoteDescription;
                d.RTCPeerConnection.prototype.setRemoteDescription = function (b) {
                    var c, d;
                    b && b.sdp && -1 !== G(c = b.sdp).call(c, "\na=extmap-allow-mixed") && (b.sdp = N(d = b.sdp.split("\n")).call(d,
                        a => "a=extmap-allow-mixed" !== Vb(a).call(a)).join("\n"));
                    return a.apply(this, arguments)
                }
            }
        }
    }

    function ic(d) {
        return "string" == typeof d ? Ha({}, $k[d]) : d
    }

    function se(d) {
        return "string" == typeof d ? Ha({}, al[d]) : d
    }

    function vd(d) {
        return "string" == typeof d ? Ha({}, bl[d]) : d
    }

    function wd(d) {
        return "string" == typeof d ? Ha({}, cl[d]) : d
    }

    function jc(d, f) {
        var a;
        ya(a = aa(u)).call(a, d) && (u[d] = f)
    }

    function kc(d, f, a) {
        return {sampleRate: d, stereo: f, bitrate: a}
    }

    function J(d, f, a, b, c) {
        return {width: d, height: f, frameRate: a, bitrateMin: b, bitrateMax: c}
    }

    function eb(d, f, a, b, c) {
        return {width: {max: d}, height: {max: f}, frameRate: a, bitrateMin: b, bitrateMax: c}
    }

    function te(d, f) {
        return {numSpatialLayers: d, numTemporalLayers: f}
    }

    function Lg(d) {
        return "[object Array]" === lc.call(d)
    }

    function Mg(d) {
        return null !== d && "object" == typeof d
    }

    function Ng(d) {
        return "[object Function]" === lc.call(d)
    }

    function xd(d, f) {
        if (null != d) if ("object" != typeof d && (d = [d]), Lg(d)) for (var a = 0, b = d.length; a < b; a++) f.call(null, d[a], a, d); else for (a in d) Object.prototype.hasOwnProperty.call(d, a) && f.call(null,
            d[a], a, d)
    }

    function Og(d) {
        return encodeURIComponent(d).replace(/%40/gi, "@").replace(/%3A/gi, ":").replace(/%24/g, "$").replace(/%2C/gi, ",").replace(/%20/g, "+").replace(/%5B/gi, "[").replace(/%5D/gi, "]")
    }

    function yd() {
        this.handlers = []
    }

    function Pg(d, f) {
        !K.isUndefined(d) && K.isUndefined(d["Content-Type"]) && (d["Content-Type"] = f)
    }

    function Kc(d) {
        this.defaults = d;
        this.interceptors = {request: new Qg, response: new Qg}
    }

    function ue(d) {
        this.message = d
    }

    function zd(d) {
        if ("function" != typeof d) throw new TypeError("executor must be a function.");
        var f;
        this.promise = new Promise(function (a) {
            f = a
        });
        var a = this;
        d(function (b) {
            a.reason || (a.reason = new Rg(b), f(a.reason))
        })
    }

    function Sg(d) {
        d = new Ad(d);
        var f = Tg(Ad.prototype.request, d);
        return K.extend(f, Ad.prototype, d), K.extend(f, d), f
    }

    function Ug() {
        let d = new Date;
        return d.toTimeString().split(" ")[0] + ":" + d.getMilliseconds()
    }

    function ve(d, f) {
        if ("boolean" != typeof d) throw new p(n.INVALID_PARAMS, "Invalid ".concat(f, ": The value is of the boolean type."));
    }

    function Ka(d, f, a) {
        var b;
        if (!ya(a).call(a, d)) throw new p(n.INVALID_PARAMS,
            l(b = "".concat(f, " can only be set as ")).call(b, A(a)));
    }

    function V(d, f, a = 1, b = 1E4, c = !0) {
        if (d < a || d > b || c && ("number" != typeof d || 0 != d % 1)) {
            var e, g;
            throw new p(n.INVALID_PARAMS, l(e = l(g = "invalid ".concat(f, ": the value range is [")).call(g, a, ", ")).call(e, b, "]. integer only"));
        }
    }

    function La(d, f, a = 1, b = 255, c = !0) {
        if (null == d) throw new p(n.INVALID_PARAMS, "".concat(f || "param", " cannot be empty"));
        var e, g, h;
        if (!Vg(d, a, b, c)) throw new p(n.INVALID_PARAMS, l(e = l(g = l(h = "Invalid ".concat(f || "string param", ": Length of the string: [")).call(h,
            a, ",")).call(g, b, "].")).call(e, c ? " ASCII characters only." : ""));
    }

    function Wg(d, f) {
        if (!mc(d)) throw new p(n.INVALID_PARAMS, "".concat(f, " should be an array"));
    }

    function we(d) {
        if ("string" != typeof d || !/^[a-zA-Z0-9 !#\$%&\(\)\+\-:;<=\.>\?@\[\]\^_\{\}\|~,]{1,64}$/.test(d)) throw k.error("Invalid Channel Name ".concat(d)), new p(n.INVALID_PARAMS, "The length must be within 64 bytes. The supported characters: a-z,A-Z,0-9,space,!, #, $, %, &, (, ), +, -, :, ;, <, =, ., >, ?, @, [, ], ^, _,  {, }, |, ~, ,");
    }

    function xe(d) {
        var f;
        if (!("number" == typeof d && Math.floor(d) === d && 0 <= d && 4294967295 >= d || Vg(d, 1, 255))) throw k.error(l(f = "Invalid UID ".concat(d, " ")).call(f, typeof d)), new p(n.INVALID_PARAMS, "[String uid] Length of the string: [1,255]. ASCII characters only. [Number uid] The value range is [0,10000]");
    }

    function Vg(d, f = 1, a = 255, b = !0) {
        if (f = "string" == typeof d && d.length <= a && d.length >= f) {
            if (!(b = !b)) a:if ("string" != typeof d) b = !1; else {
                for (b = 0; b < d.length; b += 1) if (f = d.charCodeAt(b), 0 > f || 255 < f) {
                    b = !1;
                    break a
                }
                b = !0
            }
            f =
                b
        }
        return f
    }

    function dl(d) {
        return La(d.reportId, "params.reportId", 0, 100, !1), La(d.category, "params.category", 0, 100, !1), La(d.event, "params.event", 0, 100, !1), La(d.label, "params.label", 0, 100, !1), V(d.value, "params.value", -9007199254740991, 9007199254740991, !1), !0
    }

    function Xg(d) {
        return V(d.timeout, "config.timeout", 0, 1E5), V(d.timeoutFactor, "config.timeoutFactor", 0, 100, !1), V(d.maxRetryCount, "config.maxRetryConfig", 0, 1 / 0), V(d.maxRetryTimeout, "config.maxRetryTimeout", 0, 1 / 0), !0
    }

    function ye(d) {
        if (!mc(d) || 1 > d.length) return !1;
        try {
            q(d).call(d, d => {
                if (!d.urls) throw Error();
            })
        } catch (f) {
            return !1
        }
        return !0
    }

    function Yg(d) {
        return La(d.turnServerURL, "turnServerURL"), La(d.username, "username"), La(d.password, "password"), d.udpport && V(d.udpport, "udpport", 1, 99999, !0), d.forceturn && ve(d.forceturn, "forceturn"), d.security && ve(d.security, "security"), d.tcpport && V(d.tcpport, "tcpport", 1, 99999, !0), !0
    }

    function Zg(d) {
        return void 0 !== d.level && Ka(d.level, "level", [1, 2]), !0
    }

    function ze(d, f) {
        La(d.url, "".concat(f, ".url"), 1, 1E3, !1);
        null == d.x || V(d.x, "".concat(f,
            ".x"), 0, 1E4);
        null == d.y || V(d.y, "".concat(f, ".y"), 0, 1E4);
        null == d.width || V(d.width, "".concat(f, ".width"), 0, 1E4);
        null == d.height || V(d.height, "".concat(f, ".height"), 0, 1E4);
        null == d.zOrder || V(d.zOrder, "".concat(f, ".zOrder"), 0, 255);
        null == d.alpha || V(d.alpha, "".concat(f, ".alpha"), 0, 1, !1)
    }

    function $g(d) {
        if (!d.channelName) throw new p(n.INVALID_PARAMS, "invalid channelName in info");
        if (!d.uid || "number" != typeof d.uid) throw new p(n.INVALID_PARAMS, "invalid uid in info, uid must be a number");
        return d.token && La(d.token,
            "info.token", 1, 2047), xe(d.uid), we(d.channelName), !0
    }

    function ah(d) {
        return Ka(d, "mediaSource", ["screen", "window", "application"]), !0
    }

    function ka(d) {
        var f, a, b, c;
        d = d || navigator.userAgent;
        let e = d.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
        if ("Chrome" === e[1]) {
            var g = d.match(/(OPR(?=\/))\/?(\d+)/i);
            null !== g && (e = g)
        }
        "Safari" === e[1] && (g = d.match(/version\/(\d+)/i), null !== g && (e[2] = g[1]));
        -1 !== G(f = d.toLowerCase()).call(f, "qqbrowser") && (f = d.match(/(qqbrowser(?=\/))\/?(\d+)/i), null !==
        f && (e = f));
        -1 !== G(a = d.toLowerCase()).call(a, "micromessenger") && (a = d.match(/(micromessenger(?=\/))\/?(\d+)/i), null !== a && (e = a));
        -1 !== G(b = d.toLowerCase()).call(b, "edge") && (b = d.match(/(edge(?=\/))\/?(\d+)/i), null !== b && (e = b));
        -1 !== G(c = d.toLowerCase()).call(c, "trident") && (c = /\brv[ :]+(\d+)/g.exec(d) || [], null !== c && (e = ["", "IE", c[1]]));
        c = null;
        b = [{s: W.WIN_10, r: /(Windows 10.0|Windows NT 10.0)/}, {
            s: W.WIN_81,
            r: /(Windows 8.1|Windows NT 6.3)/
        }, {s: W.WIN_8, r: /(Windows 8|Windows NT 6.2)/}, {s: W.WIN_7, r: /(Windows 7|Windows NT 6.1)/},
            {s: W.WIN_VISTA, r: /Windows NT 6.0/}, {s: W.WIN_SERVER_2003, r: /Windows NT 5.2/}, {
                s: W.WIN_XP,
                r: /(Windows NT 5.1|Windows XP)/
            }, {s: W.WIN_2000, r: /(Windows NT 5.0|Windows 2000)/}, {s: W.ANDROID, r: /Android/}, {
                s: W.OPEN_BSD,
                r: /OpenBSD/
            }, {s: W.SUN_OS, r: /SunOS/}, {s: W.LINUX, r: /(Linux|X11)/}, {
                s: W.IOS,
                r: /(iPhone|iPad|iPod)/
            }, {s: W.MAC_OS_X, r: /Mac OS X/}, {s: W.MAC_OS, r: /(MacPPC|MacIntel|Mac_PowerPC|Macintosh)/}, {
                s: W.QNX,
                r: /QNX/
            }, {s: W.UNIX, r: /UNIX/}, {s: W.BEOS, r: /BeOS/}, {s: W.OS_2, r: /OS\/2/}, {
                s: W.SEARCH_BOT,
                r: /(nuhk|Googlebot|Yammybot|Openbot|Slurp|MSNBot|Ask Jeeves\/Teoma|ia_archiver)/
            }];
        for (let e in b) if (a = b[e], a.r.test(d)) {
            c = a.s;
            break
        }
        return {name: e[1], version: e[2], os: c}
    }

    function Lc() {
        return ka().name === Z.CHROME
    }

    function bh() {
        return window.navigator.appVersion && null !== window.navigator.appVersion.match(/Chrome\/([\w\W]*?)\./) && 35 >= window.navigator.appVersion.match(/Chrome\/([\w\W]*?)\./)[1]
    }

    function Bd() {
        let d = ka();
        return d.name === Z.EDGE || d.name === Z.SAFARI ? !1 : !!navigator.userAgent.toLocaleLowerCase().match(/chrome\/[\d]./i)
    }

    function ch(d, f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b,
                function (a) {
                    return Y(d, a).enumerable
                }));
            a.push.apply(a, b)
        }
        return a
    }

    function xb(d) {
        for (var f = 1; f < arguments.length; f++) {
            var a, b = null != arguments[f] ? arguments[f] : {};
            if (f % 2) q(a = ch(Object(b), !0)).call(a, function (a) {
                Oa(d, a, b[a])
            }); else if (fa) Pa(d, fa(b)); else {
                var c;
                q(c = ch(Object(b))).call(c, function (a) {
                    X(d, a, Y(b, a))
                })
            }
        }
        return d
    }

    function el(d) {
        if (!d.address || !d.tcp) throw new p(n.UNEXPECTED_RESPONSE, "Invalid address format ".concat(d));
        return d.address.match(/^[\.:\d]+$/) ? "".concat(d.address.replace(/[^\d]/g,
            "-"), ".edge.agora.io") : (k.info("Cannot recognized as IP address ".concat(d.address, ". Used As Host instead")), l(f = "".concat(d.address, ":")).call(f, d.tcp));
        var f
    }

    function fb(d) {
        return "number" == typeof d ? d : d.exact || d.ideal || d.max || d.min || 0
    }

    function dh(d, f) {
        let a = d.videoSend[0];
        if (!a) return null;
        f = f && f.videoSend[0] ? f.videoSend[0].inputFrame : void 0;
        d = {
            id: pa(10, ""),
            timestamp: (new Date(d.timestamp)).toISOString(),
            mediaType: "video",
            type: "ssrc",
            ssrc: a.ssrc.toString()
        };
        return a.inputFrame && (f && a.inputFrame.height ===
        f.height || (d.A_fhi = a.inputFrame.height ? a.inputFrame.height.toString() : "0"), f && a.inputFrame.width === f.width || (d.A_fwi = a.inputFrame.width ? a.inputFrame.width.toString() : "0"), f && a.inputFrame.frameRate === f.frameRate || (d.A_fri = a.inputFrame.frameRate ? a.inputFrame.frameRate.toString() : "0")), d
    }

    function eh(d) {
        return 0 <= d && .17 > d ? 1 : .17 <= d && .36 > d ? 2 : .36 <= d && .59 > d ? 3 : .59 <= d && 1 >= d ? 4 : 1 < d ? 5 : 0
    }

    function fl(d, f) {
        let a = {};
        d.height && d.width && (f = f._videoHeight || f.getMediaStreamTrack().getSettings().height, a.scaleResolutionDownBy =
            f ? f / fb(d.height) : 4);
        return a.maxFramerate = d.framerate ? fb(d.framerate) : void 0, a.maxBitrate = d.bitrate ? 1E3 * d.bitrate : void 0, a
    }

    function fh(d, f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b, function (a) {
                return Y(d, a).enumerable
            }));
            a.push.apply(a, b)
        }
        return a
    }

    function Ae(d) {
        for (var f = 1; f < arguments.length; f++) {
            var a, b = null != arguments[f] ? arguments[f] : {};
            if (f % 2) q(a = fh(Object(b), !0)).call(a, function (a) {
                Oa(d, a, b[a])
            }); else if (fa) Pa(d, fa(b)); else {
                var c;
                q(c = fh(Object(b))).call(c, function (a) {
                    X(d, a, Y(b, a))
                })
            }
        }
        return d
    }

    function Cd(d) {
        return window.TextEncoder ? (new TextEncoder).encode(d).length : d.length
    }

    function yb(d) {
        return new v(f => {
            window.setTimeout(f, d)
        })
    }

    function gl(d) {
        let f = new p(n.TIMEOUT, "timeout");
        return new v((a, b) => {
            window.setTimeout(() => b(f), d)
        })
    }

    function pa(d = 7, f) {
        var a, b;
        let c = Math.random().toString(16).substr(2, d).toLowerCase();
        return c.length === d ? l(a = "".concat(f)).call(a, c) : l(b = "".concat(f)).call(b, c) + pa(d - c.length, "")
    }

    function Mc(d) {
        return new v((f, a) => {
            let b = document.createElement("video");
            b.setAttribute("autoplay",
                "");
            b.setAttribute("muted", "");
            b.muted = !0;
            b.autoplay = !0;
            b.setAttribute("playsinline", "");
            b.setAttribute("style", "position: absolute; top: 0; left: 0; width: 1px; height: 1px");
            document.body.appendChild(b);
            b.addEventListener("playing", () => {
                !b.videoWidth && ka().name === Z.FIREFOX || (document.body.removeChild(b), f([b.videoWidth, b.videoHeight]))
            });
            b.srcObject = new MediaStream([d])
        })
    }

    function Wb(d) {
        return v.all(C(d).call(d, d => d.then(a => {
            throw a;
        }, a => a))).then(d => {
            throw d;
        }, d => d)
    }

    function Ma(d, f, ...a) {
        return 0 ===
        d.getListeners(f).length ? v.reject(new p(n.UNEXPECTED_ERROR, "can not emit promise")) : new v((b, c) => {
            d.emit(f, ...a, b, c)
        })
    }

    function Qa(d, f, ...a) {
        return 0 === d.getListeners(f).length ? v.resolve() : Ma(d, f, ...a)
    }

    function Xb(d, f, ...a) {
        return 0 === d.getListeners(f).length ? null : Nc(d, f, ...a)
    }

    function Nc(d, f, ...a) {
        let b = null, c = null;
        if (d.emit(f, ...a, a => {
            b = a
        }, a => {
            c = a
        }), null !== c) throw c;
        if (null === b) throw new p(n.UNEXPECTED_ERROR, "handler is not sync");
        return b
    }

    function Oc(d, f) {
        f = G(d).call(d, f);
        -1 !== f && Ia(d).call(d,
            f, 1)
    }

    function gh(d) {
        let f = [];
        return q(d).call(d, a => {
            -1 === G(f).call(f, a) && f.push(a)
        }), f
    }

    function ab(d) {
        v.resolve().then(d)
    }

    function Pc(d, f) {
        hh[f] || (hh[f] = !0, d())
    }

    function ih(d) {
        d = window.atob(d);
        let f = new Uint8Array(new ArrayBuffer(d.length));
        for (let a = 0; a < d.length; a += 1) f[a] = d.charCodeAt(a);
        return f
    }

    function Be(d) {
        let f = "";
        for (let a = 0; a < d.length; a += 1) f += String.fromCharCode(d[a]);
        return window.btoa(f)
    }

    async function jh(d, f) {
        let a = (a, c) => a ? "number" != typeof a ? a.max || a.exact || a.ideal || a.min || c : a : c;
        d = {
            audio: !1,
            video: {
                mandatory: {
                    chromeMediaSource: "desktop",
                    chromeMediaSourceId: d,
                    maxHeight: a(f.height, 1080),
                    maxWidth: a(f.width, 1920)
                }
            }
        };
        return f.frameRate && "number" != typeof f.frameRate ? (d.video.mandatory.maxFrameRate = f.frameRate.max, d.video.mandatory.minFrameRate = f.frameRate.min) : "number" == typeof f.frameRate && (d.video.mandatory.maxFrameRate = f.frameRate), await navigator.mediaDevices.getUserMedia(d)
    }

    async function hl(d) {
        let f = await function (a) {
            return new v((b, c) => {
                const d = document.createElement("div");
                d.innerText =
                    "share screen";
                d.setAttribute("style", "text-align: center; height: 25px; line-height: 25px; border-radius: 4px 4px 0 0; background: #D4D2D4; border-bottom:  solid 1px #B9B8B9;");
                const g = document.createElement("div");
                g.setAttribute("style", "width: 100%; height: 500px; padding: 15px 25px ; box-sizing: border-box;");
                const h = document.createElement("div");
                h.innerText = "Agora Web Screensharing wants to share the contents of your screen with webdemo.agorabeckon.com. Choose what you'd like to share.";
                h.setAttribute("style",
                    "height: 12%;");
                const f = document.createElement("div");
                f.setAttribute("style", "width: 100%; height: 80%; background: #FFF; border:  solid 1px #CBCBCB; display: flex; flex-wrap: wrap; justify-content: space-around; overflow-y: scroll; padding: 0 15px; box-sizing: border-box;");
                const r = document.createElement("div");
                r.setAttribute("style", "text-align: right; padding: 16px 0;");
                const w = document.createElement("button");
                w.innerHTML = "cancel";
                w.setAttribute("style", "width: 85px;");
                w.onclick = () => {
                    document.body.removeChild(y);
                    const a = Error("NotAllowedError");
                    a.name = "NotAllowedError";
                    c(a)
                };
                r.appendChild(w);
                g.appendChild(h);
                g.appendChild(f);
                g.appendChild(r);
                const y = document.createElement("div");
                y.setAttribute("style", "position: fixed; z-index: 99999999; top: 50%; left: 50%; width: 620px; height: 525px; background: #ECECEC; border-radius: 4px; -webkit-transform: translate(-50%,-50%); transform: translate(-50%,-50%);");
                y.appendChild(d);
                y.appendChild(g);
                document.body.appendChild(y);
                C(a).call(a, a => {
                    if (a.id) {
                        const c = document.createElement("div");
                        c.setAttribute("style", "width: 30%; height: 160px; padding: 20px 0; text-align: center;box-sizing: content-box;");
                        c.innerHTML = '<div style="height: 120px; display: table-cell; vertical-align: middle;"><img style="width: 100%; background: #333333; box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.2);" src=' + a.thumbnail.toDataURL() + ' /></div><span style="\theight: 40px; line-height: 40px; display: inline-block; width: 70%; word-break: keep-all; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">' +
                            a.name + "</span>";
                        c.onclick = () => {
                            document.body.removeChild(y);
                            b(a.id)
                        };
                        f.appendChild(c)
                    }
                })
            })
        }(await kh(d.mediaSource));
        return await jh(f, d)
    }

    async function kh(d) {
        let f = ["window", "screen"];
        "application" !== d && "window" !== d || (f = ["window"]);
        "screen" === d && (f = ["screen"]);
        let a = lh();
        if (!a) throw new p(n.ELECTRON_IS_NULL);
        d = null;
        try {
            d = a.desktopCapturer.getSources({types: f})
        } catch (b) {
            d = null
        }
        d && d.then || (d = new v((b, c) => {
            a.desktopCapturer.getSources({types: f}, (a, d) => {
                a ? c(a) : b(d)
            })
        }));
        try {
            return await d
        } catch (b) {
            throw new p(n.ELECTRON_DESKTOP_CAPTURER_GET_SOURCES_ERROR,
                b.toString());
        }
    }

    function lh() {
        if (Dd) return Dd;
        try {
            return Dd = window.require("electron"), Dd
        } catch (d) {
            return null
        }
    }

    async function zb(d, f) {
        let a = 0, b = null;
        for (; 2 > a;) try {
            b = await il(d, f, 0 < a);
            break
        } catch (g) {
            var c, e;
            if (g instanceof p) throw k.error(l(e = "[".concat(f, "] ")).call(e, g.toString())), g;
            let b = Ed(g.name || g.code || g, g.message);
            if (b.code === n.MEDIA_OPTION_INVALID) k.debug("[".concat(f, "] detect media option invalid, retry")), a += 1, await yb(500); else throw k.error(l(c = "[".concat(f, "] ")).call(c, b.toString())),
                b;
        }
        if (!b) throw new p(n.UNEXPECTED_ERROR, "can not find stream after getUserMedia");
        return b
    }

    async function il(d, f, a) {
        if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) throw new p(n.NOT_SUPPORTED, "can not find getUserMedia");
        a && (d.video && (delete d.video.width, delete d.video.height), d.screen && (delete d.screen.width, delete d.screen.height));
        var b = ca;
        a = new MediaStream;
        if (d.audioSource && a.addTrack(d.audioSource), d.videoSource && a.addTrack(d.videoSource), !d.audio && !d.video && !d.screen) return k.debug("Using Video Source/ Audio Source"),
            a;
        if (d.screen) if (lh()) d.screen.sourceId ? nc(a, await jh(d.screen.sourceId, d.screen)) : nc(a, await hl(d.screen)); else if (Lc() && d.screen.extensionId && d.screen.mandatory) {
            if (!b.getStreamFromExtension) throw new p(n.NOT_SUPPORTED, "This browser does not support screen sharing");
            k.debug("[".concat(f, '] Screen access on chrome stable, looking for extension"'));
            var c = await (e = d.screen.extensionId, g = f, new v((a, b) => {
                try {
                    chrome.runtime.sendMessage(e, {getStream: !0}, c => {
                        if (!c || !c.streamId) return k.error("[".concat(g,
                            "] No response from Chrome Plugin. Plugin not installed properly"), c), void b(new p(n.CHROME_PLUGIN_NO_RESPONSE, "No response from Chrome Plugin. Plugin not installed properly"));
                        a(c.streamId)
                    })
                } catch (y) {
                    var c;
                    k.error(l(c = "[".concat(g, "] AgoraRTC screensharing plugin is not accessible(")).call(c, e, ")"), y.toString());
                    b(new p(n.CHROME_PLUGIN_NOT_INSTALL))
                }
            }));
            d.screen.mandatory.chromeMediaSourceId = c;
            nc(a, await navigator.mediaDevices.getUserMedia({video: {mandatory: d.screen.mandatory}}))
        } else if (b.getDisplayMedia) d.screen.mediaSource &&
        ah(d.screen.mediaSource), c = {
            width: d.screen.width,
            height: d.screen.height,
            frameRate: d.screen.frameRate,
            displaySurface: "screen" === d.screen.mediaSource ? "monitor" : d.screen.mediaSource
        }, k.debug("[".concat(f, "] getDisplayMedia:"), A({
            video: c,
            audio: !!d.screenAudio
        })), nc(a, await navigator.mediaDevices.getDisplayMedia({video: c, audio: !!d.screenAudio})); else {
            if (ka().name !== Z.FIREFOX) throw k.error("[".concat(f, "] This browser does not support screenSharing")), new p(n.NOT_SUPPORTED, "This browser does not support screen sharing");
            d.screen.mediaSource && ah(d.screen.mediaSource);
            b = {
                video: {
                    mediaSource: d.screen.mediaSource,
                    width: d.screen.width,
                    height: d.screen.height,
                    frameRate: d.screen.frameRate
                }
            };
            k.debug(l(c = "[".concat(f, "] getUserMedia: ")).call(c, A(b)));
            nc(a, await navigator.mediaDevices.getUserMedia(b))
        }
        var e, g;
        if (!d.video && !d.audio) return a;
        d = {video: d.video, audio: d.audio};
        k.debug("[".concat(f, "] GetUserMedia"), A(d));
        f = ka();
        let h;
        c = null;
        f.name !== Z.SAFARI && f.os !== W.IOS || (c = await Ce.lock());
        try {
            h = await navigator.mediaDevices.getUserMedia(d)
        } catch (m) {
            throw c &&
            c(), m;
        }
        return d.audio && (mh = !0), d.video && (nh = !0), nc(a, h), c && c(), a
    }

    function Ed(d, f) {
        switch (d) {
            case "Starting video failed":
            case "OverconstrainedError":
            case "TrackStartError":
                var a;
                return new p(n.MEDIA_OPTION_INVALID, l(a = "".concat(d, ": ")).call(a, f));
            case "NotFoundError":
            case "DevicesNotFoundError":
                var b;
                return new p(n.DEVICE_NOT_FOUND, l(b = "".concat(d, ": ")).call(b, f));
            case "NotSupportedError":
                var c;
                return new p(n.NOT_SUPPORTED, l(c = "".concat(d, ": ")).call(c, f));
            case "NotReadableError":
                var e;
                return new p(n.NOT_READABLE,
                    l(e = "".concat(d, ": ")).call(e, f));
            case "InvalidStateError":
            case "NotAllowedError":
            case "PERMISSION_DENIED":
            case "PermissionDeniedError":
                var g;
                return new p(n.PERMISSION_DENIED, l(g = "".concat(d, ": ")).call(g, f));
            case "ConstraintNotSatisfiedError":
                var h;
                return new p(n.CONSTRAINT_NOT_SATISFIED, l(h = "".concat(d, ": ")).call(h, f));
            default:
                var m;
                return k.error("getUserMedia unexpected error", d), new p(n.UNEXPECTED_ERROR, l(m = "".concat(d, ": ")).call(m, f))
        }
    }

    function nc(d, f) {
        let a = d.getVideoTracks()[0], b = d.getAudioTracks()[0],
            c = f.getVideoTracks()[0];
        (f = f.getAudioTracks()[0]) && (b && d.removeTrack(b), d.addTrack(f));
        c && (a && d.removeTrack(a), d.addTrack(c))
    }

    function oh(d, f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b, function (a) {
                return Y(d, a).enumerable
            }));
            a.push.apply(a, b)
        }
        return a
    }

    function Ab(d, f, a) {
        return new v((b, c) => {
            f.timeout = f.timeout || u.HTTP_CONNECT_TIMEOUT;
            f.responseType = f.responseType || "json";
            f.data && !a ? (f.data = A(f.data), ph += Cd(f.data)) : a && (ph += f.data.size);
            f.headers = f.headers || {};
            f.headers["Content-Type"] = f.headers["Content-Type"] ||
                "application/json";
            f.method = "POST";
            f.url = d;
            Bb.request(f).then(a => {
                "string" == typeof a.data ? De += Cd(a.data) : a.data instanceof ArrayBuffer || a.data instanceof Uint8Array ? De += a.data.byteLength : De += Cd(A(a.data));
                b(a.data)
            }).catch(a => {
                Bb.isCancel(a) ? c(new p(n.OPERATION_ABORTED, "cancel token canceled")) : "ECONNABORTED" === a.code ? c(new p(n.NETWORK_TIMEOUT, a.message)) : a.response ? c(new p(n.NETWORK_RESPONSE_ERROR, a.response.status)) : c(new p(n.NETWORK_ERROR, a.message))
            })
        })
    }

    async function jl(d, f) {
        let a = new Blob([f.data],
            {type: "buffer"});
        return await Ab(d, function (a) {
            for (var b = 1; b < arguments.length; b++) {
                var d, g = null != arguments[b] ? arguments[b] : {};
                if (b % 2) q(d = oh(Object(g), !0)).call(d, function (b) {
                    Oa(a, b, g[b])
                }); else if (fa) Pa(a, fa(g)); else {
                    var h;
                    q(h = oh(Object(g))).call(h, function (b) {
                        X(a, b, Y(g, b))
                    })
                }
            }
            return a
        }({}, f, {data: a, headers: {"Content-Type": "application/octet-stream"}}), !0)
    }

    function qh(d, f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b, function (a) {
                return Y(d, a).enumerable
            }));
            a.push.apply(a, b)
        }
        return a
    }

    function ua(d) {
        for (var f =
            1; f < arguments.length; f++) {
            var a, b = null != arguments[f] ? arguments[f] : {};
            if (f % 2) q(a = qh(Object(b), !0)).call(a, function (a) {
                Oa(d, a, b[a])
            }); else if (fa) Pa(d, fa(b)); else {
                var c;
                q(c = qh(Object(b))).call(c, function (a) {
                    X(d, a, Y(b, a))
                })
            }
        }
        return d
    }

    function rh(d = {report: t}) {
        return function (f, a, b) {
            let c = f[a];
            if ("function" == typeof c) {
                let e = "AgoraRTCClient" === f.constructor.name ? "Client" : f.constructor.name;
                b.value = function (...b) {
                    var g;
                    let f = d.report.reportApiInvoke(this._sessionId || null, {
                        name: l(g = "".concat(e, ".")).call(g,
                            a), options: b, tag: D.TRACER
                    });
                    try {
                        let a = c.apply(this, b);
                        return f.onSuccess(), a
                    } catch (r) {
                        throw f.onError(r), r;
                    }
                }
            }
            return b
        }
    }

    function Fd(d) {
        var f = kl[Math.floor(d / 1E4)];
        if (!f) return {desc: "unkonw error", retry: !1};
        f = f[d % 1E4];
        if (!f) {
            if (Math.floor(d / 1E4) === oc.ACCESS_POINT) {
                d %= 1E4;
                if ("1" === d.toString()[0]) return {desc: d.toString(), retry: !1};
                if ("2" === d.toString()[0]) return {desc: d.toString(), retry: !0}
            }
            return {desc: "unkonw error", retry: !1}
        }
        return f
    }

    function sh(d) {
        return ll[d] || {desc: "UNKNOW_ERROR_".concat(d), action: "failed"}
    }

    function Hb(d, f, a, b) {
        let c = Ha({}, Ra, b), e = c.timeout, g = async () => {
            await yb(e);
            e *= c.timeoutFactor;
            e = Math.min(c.maxRetryTimeout, e)
        }, h = !1;
        b = new v(async (b, e) => {
            f = f || (() => !1);
            a = a || (() => !0);
            for (let m = 0; m < c.maxRetryCount; m += 1) {
                if (h) return e(new p(n.OPERATION_ABORTED));
                try {
                    const a = await d();
                    if (!f(a, m) || m + 1 === c.maxRetryCount) return b(a);
                    await g()
                } catch (y) {
                    if (!a(y, m) || m + 1 === c.maxRetryCount) return e(y);
                    await g()
                }
            }
        });
        return b.cancel = () => h = !0, b
    }

    function ml() {
        th ? (k.info("create audio context"), pc = new th, pc.onstatechange =
            () => {
                Qc.emit("state-change")
            }, function (d) {
            function f(c) {
                "running" === d.state ? (a(!1), q && d.suspend().then(b, b)) : "closed" !== d.state && (q ? a(!1) : (a(!0), c && d.resume().then(b, b)))
            }

            function a(a) {
                if (x !== a) {
                    x = a;
                    for (let b = 0, d = t; b < d.length; b += 1) {
                        let e = d[b];
                        a ? window.addEventListener(e, c, {
                            capture: !0,
                            passive: !0
                        }) : window.removeEventListener(e, c, {capture: !0, passive: !0})
                    }
                }
            }

            function b() {
                f(!1)
            }

            function c() {
                f(!0)
            }

            function e(a) {
                if (!A) if (u.paused) if (q) g(!1); else if (a) {
                    g(!1);
                    A = !0;
                    a = void 0;
                    try {
                        (a = u.play()) ? a.then(h, h) : (u.addEventListener("playing",
                            h), u.addEventListener("abort", h), u.addEventListener("error", h))
                    } catch (Mo) {
                        h()
                    }
                } else g(!0); else g(!1), q && u.pause()
            }

            function g(a) {
                if (v !== a) {
                    v = a;
                    for (let b = 0, c = t; b < c.length; b++) {
                        let d = c[b];
                        a ? window.addEventListener(d, m, {
                            capture: !0,
                            passive: !0
                        }) : window.removeEventListener(d, m, {capture: !0, passive: !0})
                    }
                }
            }

            function h() {
                u.removeEventListener("playing", h);
                u.removeEventListener("abort", h);
                u.removeEventListener("error", h);
                A = !1;
                e(!1)
            }

            function m() {
                e(!0)
            }

            function r() {
                p && Ua ? n || (n = !0, q = !1, u && e(!0), f(!0)) : n && (n = !1)
            }

            function w() {
                B && document[B.hidden] === n && (p = !document[B.hidden], r())
            }

            function y(a) {
                if (!a || a.target === window) {
                    if (document.hasFocus()) {
                        if (Ua) return;
                        Ua = !0
                    } else {
                        if (!Ua) return;
                        Ua = !1
                    }
                    r()
                }
            }

            function k(a, b) {
                let c;
                for (c = b; 1 < a; a--) c += b;
                return c
            }

            let B;
            void 0 !== document.hidden ? B = {
                hidden: "hidden",
                visibilitychange: "visibilitychange"
            } : void 0 !== document.webkitHidden ? B = {
                hidden: "webkitHidden",
                visibilitychange: "webkitvisibilitychange"
            } : void 0 !== document.mozHidden ? B = {hidden: "mozHidden", visibilitychange: "mozvisibilitychange"} :
                void 0 !== document.msHidden && (B = {hidden: "msHidden", visibilitychange: "msvisibilitychange"});
            var l = navigator.userAgent.toLowerCase();
            l = 0 <= G(l).call(l, "iphone") && 0 > G(l).call(l, "like iphone") || 0 <= G(l).call(l, "ipad") && 0 > G(l).call(l, "like ipad") || 0 <= G(l).call(l, "ipod") && 0 > G(l).call(l, "like ipod");
            let n = !0, p = !0, Ua = !0, q = !1,
                t = "click contextmenu auxclick dblclick mousedown mouseup touchend keydown keyup".split(" "), u,
                x = !1, v = !1, A = !1;
            if (l) {
                let a = document.createElement("div");
                a.innerHTML = "<audio x-webkit-airplay='deny'></audio>";
                u = a.children.item(0);
                u.controls = !1;
                u.disableRemotePlayback = !0;
                u.preload = "auto";
                u.src = "data:audio/mpeg;base64,//uQx" + k(23, "A") + "WGluZwAAAA8AAAACAAACcQCA" + k(16, "gICA") + k(66, "/") + "8AAABhTEFNRTMuMTAwA8MAAAAAAAAAABQgJAUHQQAB9AAAAnGMHkkI" + k(320, "A") + "//sQxAADgnABGiAAQBCqgCRMAAgEAH" + k(15, "/") + "7+n/9FTuQsQH//////2NG0jWUGlio5gLQTOtIoeR2WX////X4s9Atb/JRVCbBUpeRUq" + k(18, "/") + "9RUi0f2jn/+xDECgPCjAEQAABN4AAANIAAAAQVTEFNRTMuMTAw" + k(97, "V") + "Q==";
                u.loop = !0;
                u.load();
                e(!0)
            }
            d.onstatechange = function () {
                f(!0)
            };
            f(!1);
            B && document.addEventListener(B.visibilitychange, w, !0);
            l && (window.addEventListener("focus", y, !0), window.addEventListener("blur", y, !0));
            w();
            y()
        }(pc)) : k.error("your browser is not support web audio")
    }

    function Rc() {
        if (!pc && (ml(), !pc)) throw new p(n.NOT_SUPPORTED, "can not create audio context");
        return pc
    }

    function Sc(d) {
        if (!function () {
            if (null !== Fe) return Fe;
            var a = Rc();
            let c = a.createBufferSource(), d = a.createGain();
            a = a.createGain();
            c.connect(d);
            c.connect(a);
            c.disconnect(d);
            a = !1;
            try {
                c.disconnect(d)
            } catch (g) {
                a =
                    !0
            }
            return c.disconnect(), Fe = a, a
        }()) {
            k.debug("polyfill audio node");
            var f = d.connect, a = d.disconnect;
            d.connect = (a, c, e) => {
                var b;
                return d._inputNodes || (d._inputNodes = []), ya(b = d._inputNodes).call(b, a) || (a instanceof AudioNode ? (d._inputNodes.push(a), f.call(d, a, c, e)) : f.call(d, a, c)), d
            };
            d.disconnect = (b, c, e) => {
                a.call(d);
                b ? Oc(d._inputNodes, b) : d._inputNodes = [];
                for (let a of d._inputNodes) f.call(d, a)
            }
        }
    }

    function Ge(d, f) {
        let a = 1 / f, b = Rc(), c = b.createGain();
        c.gain.value = 0;
        c.connect(b.destination);
        let e = !1, g = () => {
            if (e) return void (c =
                null);
            const h = b.createOscillator();
            h.onended = g;
            h.connect(c);
            h.start(0);
            h.stop(b.currentTime + a);
            d(b.currentTime)
        };
        return g(), () => {
            e = !0
        }
    }

    function uh(d, f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b, function (a) {
                return Y(d, a).enumerable
            }));
            a.push.apply(a, b)
        }
        return a
    }

    function He(d) {
        for (var f = 1; f < arguments.length; f++) {
            var a, b = null != arguments[f] ? arguments[f] : {};
            if (f % 2) q(a = uh(Object(b), !0)).call(a, function (a) {
                Oa(d, a, b[a])
            }); else if (fa) Pa(d, fa(b)); else {
                var c;
                q(c = uh(Object(b))).call(c, function (a) {
                    X(d,
                        a, Y(b, a))
                })
            }
        }
        return d
    }

    function vh(d, f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b, function (a) {
                return Y(d, a).enumerable
            }));
            a.push.apply(a, b)
        }
        return a
    }

    function Ie(d) {
        for (var f = 1; f < arguments.length; f++) {
            var a, b = null != arguments[f] ? arguments[f] : {};
            if (f % 2) q(a = vh(Object(b), !0)).call(a, function (a) {
                Oa(d, a, b[a])
            }); else if (fa) Pa(d, fa(b)); else {
                var c;
                q(c = vh(Object(b))).call(c, function (a) {
                    X(d, a, Y(b, a))
                })
            }
        }
        return d
    }

    function wh(d, f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b, function (a) {
                return Y(d, a).enumerable
            }));
            a.push.apply(a, b)
        }
        return a
    }

    function nl(d, f, a, b, c) {
        Je += 1;
        let e = {
            sid: a.sid,
            command: "convergeAllocateEdge",
            uid: "666",
            appId: a.appId,
            ts: Math.floor(x() / 1E3),
            seq: Je,
            requestId: Je,
            version: Va,
            cname: a.cname
        }, g = {service_name: f, json_body: A(e)}, h, m, r = d[0];
        return Hb(async () => {
            h = x();
            var a = await Ab(r, {
                data: g,
                cancelToken: b,
                headers: {"X-Packet-Service-Type": "0", "X-Packet-URI": "61"}
            });
            if (m = x() - h, 0 !== a.code) {
                var c = new p(n.UNEXPECTED_RESPONSE, "live streaming ap error, code" + a.code, {
                    retry: !0,
                    responseTime: m
                });
                throw k.error(c.toString()),
                    c;
            }
            a = JSON.parse(a.json_body);
            if (200 !== a.code) throw a = new p(n.UNEXPECTED_RESPONSE, l(c = "live streaming app center error, code: ".concat(a.code, ", reason: ")).call(c, a.reason), {
                code: a.code,
                responseTime: m
            }), k.error(a.toString()), a;
            if (!a.servers || 0 === a.servers.length) throw c = new p(n.UNEXPECTED_RESPONSE, "live streaming app center empty server", {
                code: a.code,
                responseTime: m
            }), k.error(c.toString()), c;
            c = function (a, b) {
                var c;
                return {
                    addressList: C(c = a.servers).call(c, a => {
                        var c, d;
                        return l(c = l(d = "wss://".concat(a.address.replace(/\./g,
                            "-"), ".edge.agora.io:")).call(d, a.wss, "?serviceName=")).call(c, encodeURIComponent(b))
                    }), workerToken: a.workerToken, vid: a.vid
                }
            }(a, f);
            return u.LIVE_STREAMING_ADDRESS && (c.addressList = u.LIVE_STREAMING_ADDRESS instanceof Array ? u.LIVE_STREAMING_ADDRESS : [u.LIVE_STREAMING_ADDRESS]), function (a) {
                for (var b = 1; b < arguments.length; b++) {
                    var c, d = null != arguments[b] ? arguments[b] : {};
                    if (b % 2) q(c = wh(Object(d), !0)).call(c, function (b) {
                        Oa(a, b, d[b])
                    }); else if (fa) Pa(a, fa(d)); else {
                        var e;
                        q(e = wh(Object(d))).call(e, function (b) {
                            X(a,
                                b, Y(d, b))
                        })
                    }
                }
                return a
            }({}, c, {responseTime: m})
        }, (b, c) => (t.apworkerEvent(a.sid, {
            success: !0,
            sc: 200,
            serviceName: f,
            responseDetail: A(b.addressList),
            firstSuccess: 0 === c,
            responseTime: m,
            serverIp: d[c % d.length]
        }), !1), (b, c) => (t.apworkerEvent(a.sid, {
            success: !1,
            sc: b.data && b.data.code || 200,
            serviceName: f,
            responseTime: m,
            serverIp: d[c % d.length]
        }), !!(b.code !== n.OPERATION_ABORTED && b.code !== n.UNEXPECTED_RESPONSE || b.data && b.data.retry) && (r = d[(c + 1) % d.length], !0)), c)
    }

    function xh({url: d, areaCode: f}, a, b, c) {
        let e = x(), g = {
            opid: 133,
            flag: 4096,
            ts: x(),
            key: a.token,
            cname: a.cname,
            sid: a.sid,
            detail: {6: a.stringUid, 11: f},
            uid: a.uid || 0
        };
        a.multiIP && a.multiIP.gateway_ip && (g.detail[5] = A({
            vocs_ip: [a.multiIP.uni_lbs_ip],
            vos_ip: [a.multiIP.gateway_ip]
        }));
        return Hb(async () => {
            let a = await Ab(d + "".concat(-1 === G(d).call(d, "?") ? "?" : "&", "action=wrtc_gateway"), {
                data: g,
                cancelToken: b,
                headers: {"X-Packet-Service-Type": 0, "X-Packet-URI": 69}
            });
            if (a.addresses && 0 === a.addresses.length && 0 === a.code) throw new p(n.VOID_GATEWAY_ADDRESS, "", {retry: !0});
            if (u.GATEWAY_ADDRESS &&
                0 < u.GATEWAY_ADDRESS.length) {
                var c;
                console.log(u.GATEWAY_ADDRESS);
                let b = C(c = u.GATEWAY_ADDRESS).call(c, (b, c) => ({
                    ip: b.ip,
                    port: b.port,
                    ticket: a.addresses[0] && a.addresses[0].ticket
                }));
                a.addresses = b
            }
            return function (a, b) {
                var c;
                let d = [".agora.io", ".sd-rtn.com"], e = d[1] && -1 !== G(b).call(b, d[1]) ? 1 : 0;
                return a.addresses = a.addresses || [], {
                    gatewayAddrs: C(c = a.addresses).call(c, a => {
                        var b, c, g;
                        return a.ip.match(/^[\.:\d]+$/) ? l(b = l(c = "".concat(a.ip.replace(/[^\d]/g, "-"), ".edge")).call(c, d[e++ % d.length], ":")).call(b, a.port) :
                            (k.info("Cannot recognized as IP address ".concat(a.ip, ". Used As Host instead")), l(g = "".concat(a.ip, ":")).call(g, a.port))
                    }),
                    uid: a.uid,
                    cid: a.cid,
                    vid: a.detail && a.detail[8],
                    uni_lbs_ip: a.detail && a.detail[1],
                    res: a
                }
            }(a, d)
        }, b => {
            if (0 === b.res.code) return t.joinChooseServer(a.sid, {
                lts: e,
                succ: !0,
                csAddr: d,
                serverList: b.gatewayAddrs,
                ec: null,
                cid: b.res.cid.toString(),
                uid: b.res.uid.toString()
            }), !1;
            b = Fd(b.res.code);
            throw new p(n.CAN_NOT_GET_GATEWAY_SERVER, b.desc, {retry: b.retry});
        }, b => {
            return b.code !== n.OPERATION_ABORTED &&
                (b.code === n.CAN_NOT_GET_GATEWAY_SERVER || b.code === n.VOID_GATEWAY_ADDRESS ? (t.joinChooseServer(a.sid, {
                    lts: e,
                    succ: !1,
                    csAddr: d,
                    serverList: null,
                    ec: b.message
                }), k.warning(l(c = l(g = l(h = "[".concat(a.clientId, "] Choose server ")).call(h, d, " failed, message: ")).call(g, b.message, ", retry: ")).call(c, b.data.retry)), b.data.retry) : (t.joinChooseServer(a.sid, {
                    lts: e,
                    succ: !1,
                    csAddr: d,
                    serverList: null,
                    ec: b.code
                }), k.warning("[".concat(a.clientId, "] Choose server network error, retry"), b), !0));
            var c, g, h
        }, c)
    }

    async function yh(d,
                      f, a) {
        return {
            gatewayInfo: await async function (a, c, d) {
                var b, e;
                const f = C(b = Aa(e = u.WEBCS_DOMAIN).call(e, 0, u.AJAX_REQUEST_CONCURRENT)).call(b, b => {
                    var c;
                    return {
                        url: a.proxyServer ? l(c = "https://".concat(a.proxyServer, "/ap/?url=")).call(c, b + "/api/v1") : "https://".concat(b, "/api/v1"),
                        areaCode: Ke()
                    }
                });
                let r = null;
                b = C(f).call(f, b => (k.debug("[".concat(a.clientId, "] Connect to choose_server:"), b.url), xh(b, a, c, d)));
                e = () => new v(async (b, e) => {
                    var g, h;
                    if (await yb(1E3), null === r) {
                        var f = C(g = Aa(h = u.WEBCS_DOMAIN_BACKUP_LIST).call(h,
                            0, u.AJAX_REQUEST_CONCURRENT)).call(g, b => {
                                var c;
                                return {
                                    url: a.proxyServer ? l(c = "https://".concat(a.proxyServer, "/ap/?url=")).call(c, b + "/api/v1") : "https://".concat(b, "/api/v1"),
                                    areaCode: Ke()
                                }
                            }),
                            m = C(f).call(f, b => (k.debug("[".concat(a.clientId, "] Connect to backup choose_server:"), b.url), xh(b, a, c, d)));
                        Wb(m).then(a => {
                            q(m).call(m, a => a.cancel());
                            b(a)
                        }).catch(a => e(a[0]))
                    }
                });
                try {
                    var w;
                    r = await Wb(l(w = [e()]).call(w, b))
                } catch (y) {
                    throw y[0];
                }
                return q(b).call(b, a => a.cancel()), r
            }(d, f, a)
        }
    }

    async function ol(d, f, a) {
        var b,
            c, e;
        if ("disabled" !== d.cloudProxyServer) {
            var g = await async function (a, b, c) {
                var d, e, g = x();
                const h = C(d = Aa(e = u.PROXY_CS).call(e, 0, u.AJAX_REQUEST_CONCURRENT)).call(d, b => {
                    var c;
                    return a.proxyServer ? l(c = "https://".concat(a.proxyServer, "/ap/?url=")).call(c, b + "/api/v1") : "https://".concat(b, "/api/v1")
                });
                if ("proxy3" === a.cloudProxyServer || "proxy4" === a.cloudProxyServer || "proxy5" === a.cloudProxyServer) {
                    var f, m;
                    g = C(f = Aa(m = u.PROXY_CS).call(m, 0, u.AJAX_REQUEST_CONCURRENT)).call(f, b => {
                        var c;
                        return {
                            url: a.proxyServer ?
                                l(c = "https://".concat(a.proxyServer, "/ap/?url=")).call(c, b + "/api/v1") : "https://".concat(b, "/api/v1"),
                            areaCode: Ke()
                        }
                    });
                    g = C(g).call(g, d => function ({url: a, areaCode: b}, c, d, e) {
                        const g = x(), h = {
                            opid: 133,
                            flag: 1048576,
                            ts: +new Date,
                            key: c.token,
                            cname: c.cname,
                            sid: c.sid,
                            detail: {6: c.stringUid, 11: b},
                            uid: c.uid || 0
                        };
                        return Hb(async () => await Ab(a, {
                            data: h,
                            cancelToken: d,
                            headers: {"X-Packet-Service-Type": 0, "X-Packet-URI": 69}
                        }), b => {
                            var d;
                            if (0 === b.code) return t.joinWebProxyAP(c.sid, {
                                lts: g,
                                sucess: 1,
                                apServerAddr: a,
                                turnServerAddrList: C(d =
                                    b.addresses).call(d, a => a.ip).join(","),
                                errorCode: null,
                                eventType: c.cloudProxyServer
                            }), !1;
                            b = Fd(b.code);
                            throw new p(n.CAN_NOT_GET_GATEWAY_SERVER, b.desc, {retry: b.retry});
                        }, b => {
                            return b.code !== n.OPERATION_ABORTED && (b.code === n.CAN_NOT_GET_GATEWAY_SERVER || b.code === n.VOID_GATEWAY_ADDRESS ? (t.joinWebProxyAP(h.sid, {
                                lts: g,
                                sucess: 0,
                                apServerAddr: a,
                                turnServerAddrList: null,
                                errorCode: b.code,
                                eventType: c.cloudProxyServer
                            }), k.warning(l(d = l(e = l(f = "[".concat(c.clientId, "] proxy ap server ")).call(f, a, " failed, message: ")).call(e,
                                b.message, ", retry: ")).call(d, b.data.retry)), b.data.retry) : (t.joinWebProxyAP(h.sid, {
                                lts: g,
                                sucess: 0,
                                apServerAddr: a,
                                turnServerAddrList: null,
                                errorCode: b.code,
                                eventType: c.cloudProxyServer
                            }), !0));
                            var d, e, f
                        }, e)
                    }(d, a, b, c));
                    var r = null;
                    try {
                        r = await Wb(g)
                    } catch (Ua) {
                        throw k.error("[".concat(a.clientId, "] can not get proxy server after trying several times")), new p(n.CAN_NOT_GET_PROXY_SERVER);
                    }
                    q(g).call(g, a => a.cancel());
                    g = r.addresses;
                    if (!g || 0 === g.length) throw k.error("[".concat(a.clientId, "] can not get proxy server, empty proxy server list")),
                        new p(n.CAN_NOT_GET_PROXY_SERVER, "empty proxy server list");
                    return {
                        addresses: C(g).call(g, a => a.ip),
                        serverResponse: {
                            tcpport: g[0].port || 443,
                            udpport: g[0].port || Ga.udpport,
                            username: Ga.username,
                            password: Ga.password
                        }
                    }
                }
                m = C(h).call(h, d => function (a, b, c, d) {
                    const e = x(), g = {
                        command: "convergeAllocateEdge",
                        sid: b.sid,
                        appId: b.appId,
                        token: b.token,
                        uid: b.uid,
                        cname: b.cname,
                        ts: Math.floor(x() / 1E3),
                        version: Va,
                        seq: 0,
                        requestId: 1
                    };
                    return Hb(async () => ({
                        res: await Ab(a, {
                            data: {service_name: "webrtc_proxy", json_body: A(g)}, cancelToken: c,
                            headers: {"X-Packet-Service-Type": 0, "X-Packet-URI": 61}
                        }), url: a
                    }), a => {
                        if (!a.res.json_body) throw k.debug("[".concat(b.clientId, "] Get proxy server failed: no json_body")), new p(n.UNEXPECTED_RESPONSE, A(a.res));
                        const c = JSON.parse(a.res.json_body);
                        var d, e;
                        if (200 !== c.code) throw k.debug(l(d = l(e = "[".concat(b.clientId, "] Get proxy server failed: response code [")).call(e, c.code, "], reason [")).call(d, c.reason, "]")), new p(n.UNEXPECTED_RESPONSE, A(a.res));
                        return k.debug("[".concat(b.clientId, "] App return server length"),
                            c.servers.length), !1
                    }, b => b.code !== n.OPERATION_ABORTED && (t.requestProxyAppCenter(g.sid, {
                        lts: e,
                        succ: !1,
                        APAddr: a,
                        workerManagerList: null,
                        ec: b.code,
                        response: b.message
                    }), !0), d)
                }(d, a, b, c));
                f = null;
                try {
                    f = await Wb(m)
                } catch (Ua) {
                    throw k.error("[".concat(a.clientId, "] can not get proxy server after trying several times")), new p(n.CAN_NOT_GET_PROXY_SERVER);
                }
                q(m).call(m, a => a.cancel());
                m = JSON.parse(f.res.json_body);
                m = C(r = m.servers).call(r, el);
                if ("443only" === a.cloudProxyServer) return {
                    addresses: m, serverResponse: {
                        tcpport: 443,
                        udpport: Ga.udpport, username: Ga.username, password: Ga.password
                    }
                };
                t.requestProxyAppCenter(a.sid, {
                    lts: g,
                    succ: !0,
                    APAddr: f.url,
                    workerManagerList: A(m),
                    ec: null,
                    response: A(f.res)
                });
                g = x();
                r = C(m).call(m, d => function (a, b, c, d) {
                    const e = x();
                    let g = a;
                    td(a).call(a, "http") || (g = "https://".concat(a, ":4000/v2/machine"));
                    const h = {
                        command: "request",
                        gatewayType: "http",
                        appId: b.appId,
                        cname: b.cname,
                        uid: (b.uid || "").toString(),
                        sdkVersion: "2.3.1",
                        sid: b.sid,
                        seq: 1,
                        ts: x(),
                        requestId: 3,
                        clientRequest: {
                            appId: b.appId, cname: b.cname, uid: (b.uid ||
                                "").toString(), sid: b.sid
                        }
                    };
                    return Hb(async () => ({res: await Ab(g, {data: h, cancelToken: c}), url: a}), a => {
                        if (!a.res.serverResponse) throw new p(n.UNEXPECTED_RESPONSE, "requeet worker manager server failed: serverResponse is undefined");
                        return !1
                    }, b => b.code !== n.OPERATION_ABORTED && (t.requestProxyWorkerManager(h.sid, {
                        lts: e,
                        succ: !1,
                        workerManagerAddr: a,
                        ec: b.code,
                        response: b.message
                    }), !0), d)
                }(d, a, b, c));
                f = null;
                try {
                    f = await Wb(r)
                } catch (Ua) {
                    throw k.error("[".concat(a.clientId, "] can not get worker manager after trying several times")),
                        new p(n.CAN_NOT_GET_PROXY_SERVER);
                }
                return q(r).call(r, a => a.cancel()), t.requestProxyWorkerManager(a.sid, {
                    lts: g,
                    succ: !0,
                    workerManagerAddr: f.url,
                    ec: null,
                    response: A(f.res)
                }), {addresses: [f.url], serverResponse: f.res.serverResponse}
            }(d, f, a);
            "443only" === d.cloudProxyServer ? d.proxyServer = u.PROXY_SERVER_TYPE2 : "proxy3" !== d.cloudProxyServer && "proxy4" !== d.cloudProxyServer && "proxy5" !== d.cloudProxyServer || (d.proxyServer = u.PROXY_SERVER_TYPE3);
            t.setProxyServer(d.proxyServer);
            k.setProxyServer(d.proxyServer);
            "normal" ===
            d.cloudProxyServer && (d.proxyServer = g.addresses[0], t.setProxyServer(d.proxyServer), k.setProxyServer(d.proxyServer));
            d.turnServer = {
                mode: "manual", servers: C(b = g.addresses).call(b, a => ({
                    turnServerURL: a,
                    tcpport: "proxy3" === d.cloudProxyServer ? void 0 : g.serverResponse.tcpport ? g.serverResponse.tcpport : Ga.tcpport,
                    udpport: "proxy4" === d.cloudProxyServer ? void 0 : g.serverResponse.udpport ? g.serverResponse.udpport : Ga.udpport,
                    username: g.serverResponse.username || Ga.username,
                    password: g.serverResponse.password || Ga.password,
                    forceturn: "proxy4" !== d.cloudProxyServer,
                    security: "proxy5" === d.cloudProxyServer
                }))
            };
            k.debug(l(c = l(e = "[".concat(d.clientId, "] set proxy server: ")).call(e, d.proxyServer, ", mode: ")).call(c, d.cloudProxyServer))
        }
    }

    async function zh(d, f, a, b) {
        var c;
        let e = Aa(c = u.ACCOUNT_REGISTER).call(c, 0, u.AJAX_REQUEST_CONCURRENT);
        c = [];
        c = f.proxyServer ? C(e).call(e, a => {
            var b;
            return l(b = "https://".concat(f.proxyServer, "/ap/?url=")).call(b, a + "/api/v1")
        }) : C(e).call(e, a => "https://".concat(a, "/api/v1"));
        return (await async function (a,
                                      b, c, d, e) {
            let g = x(), h = {sid: c.sid, opid: 10, appid: c.appId, string_uid: b}, f = a[0];
            c = await Hb(() => Ab(f + "".concat(-1 === G(f).call(f, "?") ? "?" : "&", "action=stringuid"), {
                data: h,
                cancelToken: d,
                headers: {"X-Packet-Service-Type": 0, "X-Packet-URI": 72}
            }), (c, d) => {
                if (0 === c.code) {
                    var e;
                    if (0 >= c.uid || c.uid >= Math.pow(2, 32)) throw k.error(l(e = "Invalid Uint Uid ".concat(b, " => ")).call(e, c.uid), c), t.reqUserAccount(h.sid, {
                        lts: g,
                        success: !1,
                        serverAddr: f,
                        stringUid: h.string_uid,
                        uid: c.uid,
                        errorCode: n.INVALID_UINT_UID_FROM_STRING_UID,
                        extend: h
                    }), new p(n.INVALID_UINT_UID_FROM_STRING_UID);
                    return t.reqUserAccount(h.sid, {
                        lts: g,
                        success: !0,
                        serverAddr: f,
                        stringUid: h.string_uid,
                        uid: c.uid,
                        errorCode: null,
                        extend: h
                    }), !1
                }
                e = Fd(c.code);
                return e.retry && (f = a[(d + 1) % a.length]), t.reqUserAccount(h.sid, {
                    lts: g,
                    success: !1,
                    serverAddr: f,
                    stringUid: h.string_uid,
                    uid: c.uid,
                    errorCode: e.desc,
                    extend: h
                }), e.retry
            }, (b, c) => b.code !== n.OPERATION_ABORTED && (t.reqUserAccount(h.sid, {
                lts: g,
                success: !1,
                serverAddr: f,
                stringUid: h.string_uid,
                uid: null,
                errorCode: b.code,
                extend: h
            }),
                f = a[(c + 1) % a.length], !0), e);
            if (0 !== c.code) throw c = Fd(c.code), new p(n.UNEXPECTED_RESPONSE, c.desc);
            return c
        }(c, d, f, a, b)).uid
    }

    async function pl(d, f, a) {
        var b, c, e = C(b = Aa(c = u.CDS_AP).call(c, 0, u.AJAX_REQUEST_CONCURRENT)).call(b, a => {
            var b;
            return d.proxyServer ? l(b = "https://".concat(d.proxyServer, "/ap/?url=")).call(b, a + "/api/v1") : "https://".concat(a, "/api/v1?action=config")
        });
        b = C(e).call(e, b => function (a, b, c, d) {
            const e = ka(), g = {
                flag: 64, cipher_method: 0, features: {
                    device: e.name, system: e.os, vendor: b.appId, version: Va,
                    cname: b.cname, sid: b.sid, session_id: b.sid, detail: "", proxyServer: b.proxyServer
                }
            };
            return Hb(() => Ab(a, {
                data: g,
                timeout: 1E3,
                cancelToken: c,
                headers: {"X-Packet-Service-Type": 0, "X-Packet-URI": 54}
            }), void 0, a => a.code !== n.OPERATION_ABORTED, d)
        }(b, d, f, a));
        e = c = null;
        let g = {};
        try {
            c = await Wb(b)
        } catch (h) {
            if (h.code === n.OPERATION_ABORTED) throw h;
            e = h
        }
        q(b).call(b, a => a.cancel());
        if (t.reportApiInvoke(d.sid, {
            name: E.REQUEST_CONFIG_DISTRIBUTE,
            options: {error: e, res: c}
        }).onSuccess(), c && c.test_tags) try {
            g = function (a) {
                if (!a.test_tags) return {};
                let b = a.test_tags;
                a = aa(b);
                let c = {};
                return q(a).call(a, a => {
                    var d;
                    let e = Vb(d = Aa(a).call(a, 4)).call(d);
                    a = JSON.parse(b[a])[1];
                    c[e] = a
                }), c
            }(c)
        } catch (h) {
        }
        return g
    }

    async function Ah(d, f, a, b) {
        var c, e;
        let g = C(c = Aa(e = u.UAP_AP).call(e, 0, u.AJAX_REQUEST_CONCURRENT)).call(c, a => {
            var b;
            return f.proxyServer ? l(b = "https://".concat(f.proxyServer, "/ap/?url=")).call(b, a + "/api/v1?action=uap") : "https://".concat(a, "/api/v1?action=uap")
        });
        return await nl(g, d, f, a, b)
    }

    async function ql(d, f, a) {
        var b, c;
        let e = C(b = Aa(c = u.UAP_AP).call(c,
            0, u.AJAX_REQUEST_CONCURRENT)).call(b, a => {
            var b;
            return d.proxyServer ? l(b = "https://".concat(d.proxyServer, "/ap/?url=")).call(b, a + "/api/v1?action=uap") : "https://".concat(a, "/api/v1?action=uap")
        });
        b = C(e).call(e, b => function (a, b, c, d) {
            b = {
                command: "convergeAllocateEdge",
                sid: b.sid,
                appId: b.appId,
                token: b.token,
                ts: x(),
                version: Va,
                cname: b.cname,
                uid: b.uid.toString(),
                requestId: Le,
                seq: Le
            };
            Le += 1;
            const e = {service_name: "tele_channel", json_body: A(b)};
            return Hb(async () => {
                var b = await Ab(a, {
                    data: e, cancelToken: c, headers: {
                        "X-Packet-Service-Type": 0,
                        "X-Packet-URI": 61
                    }
                });
                if (0 !== b.code) {
                    var d = new p(n.UNEXPECTED_RESPONSE, "cross channel ap error, code" + b.code, {retry: !0});
                    throw k.error(d.toString()), d;
                }
                b = JSON.parse(b.json_body);
                if (200 !== b.code) {
                    var g = new p(n.UNEXPECTED_RESPONSE, l(d = "cross channel app center error, code: ".concat(b.code, ", reason: ")).call(d, b.reason));
                    throw k.error(g.toString()), g;
                }
                if (!b.servers || 0 === b.servers.length) throw d = new p(n.UNEXPECTED_RESPONSE, "cross channel app center empty server"), k.error(d.toString()), d;
                return {
                    vid: b.vid,
                    workerToken: b.workerToken, addressList: C(g = b.servers).call(g, a => {
                        var b;
                        return l(b = "wss://".concat(a.address.replace(/\./g, "-"), ".edge.agora.io:")).call(b, a.wss)
                    })
                }
            }, void 0, a => !!(a.code !== n.OPERATION_ABORTED && a.code !== n.UNEXPECTED_RESPONSE || a.data && a.data.retry), d)
        }(b, d, f, a));
        try {
            let a = await Wb(b);
            return q(b).call(b, a => a.cancel()), a
        } catch (g) {
            throw g[0];
        }
    }

    function Bh(d, f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b, function (a) {
                return Y(d, a).enumerable
            }));
            a.push.apply(a, b)
        }
        return a
    }

    function gb(d) {
        if (Array.isArray(d)) return d.map(function (a) {
            return a
        });
        if (!Ch(d)) return d;
        var f = {}, a;
        for (a in d) Ch(d[a]) || Array.isArray(d[a]) ? f[a] = gb(d[a]) : f[a] = d[a];
        return f
    }

    function Ch(d) {
        return !("object" != typeof d || Array.isArray(d) || !d)
    }

    function Me(d, f) {
        function a() {
            this.constructor = d
        }

        Dh(d, f);
        d.prototype = null === f ? Object.create(f) : (a.prototype = f.prototype, new a)
    }

    function Ne(d, f, a, b) {
        return new (a || (a = Promise))(function (c, e) {
            function g(a) {
                try {
                    m(b.next(a))
                } catch (w) {
                    e(w)
                }
            }

            function h(a) {
                try {
                    m(b.throw(a))
                } catch (w) {
                    e(w)
                }
            }

            function m(b) {
                b.done ? c(b.value) : (new a(function (a) {
                    a(b.value)
                })).then(g,
                    h)
            }

            m((b = b.apply(d, f || [])).next())
        })
    }

    function Oe(d, f) {
        function a(a) {
            return function (g) {
                return function (a) {
                    if (b) throw new TypeError("Generator is already executing.");
                    for (; h;) try {
                        if (b = 1, c && (e = 2 & a[0] ? c.return : a[0] ? c.throw || ((e = c.return) && e.call(c), 0) : c.next) && !(e = e.call(c, a[1])).done) return e;
                        switch (c = 0, e && (a = [2 & a[0], e.value]), a[0]) {
                            case 0:
                            case 1:
                                e = a;
                                break;
                            case 4:
                                return h.label++, {value: a[1], done: !1};
                            case 5:
                                h.label++;
                                c = a[1];
                                a = [0];
                                continue;
                            case 7:
                                a = h.ops.pop();
                                h.trys.pop();
                                continue;
                            default:
                                if (!(e = h.trys,
                                (e = 0 < e.length && e[e.length - 1]) || 6 !== a[0] && 2 !== a[0])) {
                                    h = 0;
                                    continue
                                }
                                if (3 === a[0] && (!e || a[1] > e[0] && a[1] < e[3])) h.label = a[1]; else if (6 === a[0] && h.label < e[1]) h.label = e[1], e = a; else if (e && h.label < e[2]) h.label = e[2], h.ops.push(a); else {
                                    e[2] && h.ops.pop();
                                    h.trys.pop();
                                    continue
                                }
                        }
                        a = f.call(d, h)
                    } catch (y) {
                        a = [6, y], c = 0
                    } finally {
                        b = e = 0
                    }
                    if (5 & a[0]) throw a[1];
                    return {value: a[0] ? a[1] : void 0, done: !0}
                }([a, g])
            }
        }

        var b, c, e, g, h = {
            label: 0, sent: function () {
                if (1 & e[0]) throw e[1];
                return e[1]
            }, trys: [], ops: []
        };
        return g = {
            next: a(0), throw: a(1),
            return: a(2)
        }, "function" == typeof Symbol && (g[Symbol.iterator] = function () {
            return this
        }), g
    }

    function rl(d, f, a) {
        a = d.createShader(a);
        if (!a) return (new p(n.WEBGL_INTERNAL_ERROR, "can not create shader")).throw();
        d.shaderSource(a, f);
        d.compileShader(a);
        return d.getShaderParameter(a, d.COMPILE_STATUS) ? a : (f = d.getShaderInfoLog(a), d.deleteShader(a), (new p(n.WEBGL_INTERNAL_ERROR, "error compiling shader:" + f)).throw())
    }

    function sl(d, f, a, b) {
        let c = [];
        for (let a = 0; a < f.length; ++a) c.push(rl(d, f[a], 0 === a ? d.VERTEX_SHADER : d.FRAGMENT_SHADER));
        return function (a, b, c, d) {
            let e = a.createProgram();
            if (!e) throw new p(n.WEBGL_INTERNAL_ERROR, "can not create webgl program");
            if (q(b).call(b, b => {
                a.attachShader(e, b)
            }), c && q(c).call(c, (b, c) => {
                a.bindAttribLocation(e, d ? d[c] : c, b)
            }), a.linkProgram(e), !a.getProgramParameter(e, a.LINK_STATUS)) throw b = a.getProgramInfoLog(e), a.deleteProgram(e), new p(n.WEBGL_INTERNAL_ERROR, "error in program linking:" + b);
            return e
        }(d, c, a, b)
    }

    function Eh(d) {
        var f = new Uint8Array([99, 114, 121, 112, 116, 105, 105]), a = f.length;
        let b = d.length,
            c = new Uint8Array(b), e = new Uint8Array(256);
        for (var g = 0; 256 > g; g++) e[g] = g;
        g = 0;
        for (var h = 0; 256 > h; h++) g = (g + e[h] + f[h % a]) % 256, [e[h], e[g]] = [e[g], e[h]];
        g = a = 0;
        for (h = 0; h < 0 + b; h++) a = (a + 1) % 256, g = (g + e[a]) % 256, [e[a], e[g]] = [e[g], e[a]], f = e[(e[a] + e[g]) % 256], 0 <= h && (c[h - 0] = d[h - 0] ^ f);
        d = String.fromCharCode.apply(null, Ib(c));
        return Function("var winSize = 5; return `" + d + "`")()
    }

    function Pe(d) {
        let f = {};
        if (d.facingMode && (f.facingMode = d.facingMode), d.cameraId && (f.deviceId = {exact: d.cameraId}), !d.encoderConfig) return f;
        d = ic(d.encoderConfig);
        return f.width = d.width, f.height = d.height, !bh() && d.frameRate && (f.frameRate = d.frameRate), ka().name === Z.EDGE && "object" == typeof f.frameRate && (f.frameRate.max = 60), ka().name === Z.FIREFOX && (f.frameRate = {
            ideal: 30,
            max: 30
        }), f
    }

    function Fh(d) {
        let f = {};
        if (bh() || (void 0 !== d.AGC && (f.autoGainControl = d.AGC, Lc() && (f.googAutoGainControl = d.AGC, f.googAutoGainControl2 = d.AGC)), void 0 !== d.AEC && (f.echoCancellation = d.AEC), void 0 !== d.ANS && (f.noiseSuppression = d.ANS, Lc() && (f.googNoiseSuppression = d.ANS))), d.encoderConfig) {
            let a =
                wd(d.encoderConfig);
            f.channelCount = a.stereo ? 2 : 1;
            f.sampleRate = a.sampleRate;
            f.sampleSize = a.sampleSize
        }
        return d.microphoneId && (f.deviceId = {exact: d.microphoneId}), Lc() && 2 === f.channelCount && (f.googAutoGainControl = !1, f.googAutoGainControl2 = !1, f.echoCancellation = !1, f.googNoiseSuppression = !1), f
    }

    function Gh(d, f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b, function (a) {
                return Y(d, a).enumerable
            }));
            a.push.apply(a, b)
        }
        return a
    }

    function Gd(d) {
        for (var f = 1; f < arguments.length; f++) {
            var a, b = null != arguments[f] ? arguments[f] :
                {};
            if (f % 2) q(a = Gh(Object(b), !0)).call(a, function (a) {
                Oa(d, a, b[a])
            }); else if (fa) Pa(d, fa(b)); else {
                var c;
                q(c = Gh(Object(b))).call(c, function (a) {
                    X(d, a, Y(b, a))
                })
            }
        }
        return d
    }

    function Qe(d, f) {
        var a = d.match(/a=rtpmap:(\d+) opus/);
        if (!a || !a[0] || !a[1]) return d;
        var b = a[1];
        a = d.match("a=fmtp:".concat(b, ".*\r\n"));
        if (!a || !a[0]) return d;
        b = "a=fmtp:".concat(b, " minptime=10;useinbandfec=1;");
        var c;
        (f.bitrate && (b += "maxaveragebitrate=".concat(Math.floor(1E3 * f.bitrate), ";")), f.sampleRate) && (b += l(c = "maxplaybackrate=".concat(f.sampleRate,
            ";sprop-maxcapturerate=")).call(c, f.sampleRate, ";"));
        return f.stereo && (b += "stereo=1;sprop-stereo-1;"), b += "\r\n", d.replace(a[0], b)
    }

    function Hh(d) {
        return d.replace("minptime=10", "minptime=10;stereo=1; sprop-stereo=1")
    }

    function Ih(d, f, a) {
        var b = !1;
        switch (f) {
            case "h264":
            case "vp8":
                return d;
            case "vp9":
            case "av1":
                b = !0
        }
        if (b) {
            var c;
            let sa = ta(c = RegExp.prototype.test).call(c, /^([a-z])=(.*)/), z = d.split("m="), Qb = null, Ua = null,
                u = null, Ee = [], x = [];
            var e;
            c = [];
            b = f = null;
            var g;
            let v = [], A = [];
            for (var h = 0; h < z.length; ++h) if (Qb =
                z[h].match(/a=msid-semantic:/), Qb) {
                Qb = z[h];
                break
            }
            for (h = 0; h < z.length; ++h) if (Ua = z[h].match(/audio /), Ua) {
                Ua = "m=" + z[h];
                break
            }
            for (h = 0; h < z.length; ++h) if (u = z[h].match(/video /), u) {
                u = "m=" + z[h];
                break
            }
            Qb && (Ee = N(e = Qb.split(/(\r\n|\r|\n)/)).call(e, sa));
            if (0 < Ee.length && (c = l(c).call(c, Ee)), Ua) for (x = N(g = Ua.split(/(\r\n|\r|\n)/)).call(g, sa), e = 0; e < x.length; ++e) if (null === f && (f = x[e].match(/cname:/), null !== f)) {
                f = "cname:" + x[e].split("cname:")[1];
                break
            }
            if (0 < x.length && (c = l(c).call(c, x)), !u) return d;
            var m;
            e = N(m = u.split(/(\r\n|\r|\n)/)).call(m,
                sa);
            for (g = 0; g < e.length; ++g) if (null === b && (b = e[g].match(/a=msid:/), null !== b && (b = "msid:" + e[g].split(":")[1])), e[g].match(/a=ssrc-group:FID/)) {
                m = e[g].split(" ");
                v.push(Number(m[1]));
                m[2] && A.push(Number(m[2]));
                e.length = g;
                break
            }
            for (m = 1; m < a.numSpatialLayers; ++m) v.push(v[0] + m), 0 < A.length && A.push(A[0] + m);
            m = "a=ssrc-group:SIM ";
            for (g = 0; g < v.length; ++g) m = l(m).call(m, String(v[g])), g < v.length - 1 && (m = l(m).call(m, " "));
            e.push(m);
            for (m = 0; m < a.numSpatialLayers; ++m) {
                var r, w;
                g = l(r = l(w = l("a=ssrc-group:FID ").call("a=ssrc-group:FID ",
                    String(v[m]))).call(w, " ")).call(r, String(A[m]));
                e.push(g)
            }
            for (r = 0; r < a.numSpatialLayers; ++r) {
                var y, k, B, n;
                if (null === f || null === b) return d;
                w = l(y = l(k = "a=ssrc:".concat(String(v[r]))).call(k, " ")).call(y, f);
                m = l(B = l(n = "a=ssrc:".concat(String(v[r]))).call(n, " ")).call(B, b);
                e.push(w);
                e.push(m)
            }
            for (y = 0; y < a.numSpatialLayers; ++y) {
                var p, ja, q, t;
                if (null === f || null === b) return d;
                k = l(p = l(ja = "a=ssrc:".concat(String(A[y]))).call(ja, " ")).call(p, f);
                B = l(q = l(t = "a=ssrc:".concat(String(A[y]))).call(t, " ")).call(q, b);
                e.push(k);
                e.push(B)
            }
            c = l(c).call(c, e);
            d = c.join("\r\n") + "\r\n"
        }
        return d
    }

    function tl(d, f) {
        let a = document.createElement("video"), b = document.createElement("canvas");
        a.setAttribute("style", "display:none");
        b.setAttribute("style", "display:none");
        a.setAttribute("muted", "");
        a.muted = !0;
        a.setAttribute("autoplay", "");
        a.autoplay = !0;
        a.setAttribute("playsinline", "");
        b.width = fb(f.width);
        b.height = fb(f.height);
        f = fb(f.framerate || 15);
        document.body.append(a);
        document.body.append(b);
        let c = d._mediaStreamTrack;
        a.srcObject = new MediaStream([c]);
        a.play();
        let e = b.getContext("2d");
        if (!e) throw new p(n.UNEXPECTED_ERROR, "can not get canvas context");
        let g = b.captureStream(ca.supportRequestFrame ? 0 : f).getVideoTracks()[0], h = Ge(() => {
            if (a.paused && a.play(), 2 < a.videoHeight && 2 < a.videoWidth) {
                const c = a.videoHeight / a.videoWidth * b.width;
                var h, f, m;
                2 <= Math.abs(c - b.height) && (k.debug("adjust low stream resolution", l(h = l(f = l(m = "".concat(b.width, "x")).call(m, b.height, " -> ")).call(f, b.width, "x")).call(h, c)), b.height = c)
            }
            e.drawImage(a, 0, 0, b.width, b.height);
            g.requestFrame &&
            g.requestFrame();
            c !== d._mediaStreamTrack && (c = d._mediaStreamTrack, a.srcObject = new MediaStream([c]))
        }, f), m = g.stop;
        return g.stop = () => {
            m.call(g);
            h();
            a.remove();
            b.width = 0;
            b.remove();
            a = b = null;
            k.debug("clean low stream renderer")
        }, g
    }

    function Jh(d, f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b, function (a) {
                return Y(d, a).enumerable
            }));
            a.push.apply(a, b)
        }
        return a
    }

    function Kh(d) {
        for (var f = 1; f < arguments.length; f++) {
            var a, b = null != arguments[f] ? arguments[f] : {};
            if (f % 2) q(a = Jh(Object(b), !0)).call(a, function (a) {
                Oa(d,
                    a, b[a])
            }); else if (fa) Pa(d, fa(b)); else {
                var c;
                q(c = Jh(Object(b))).call(c, function (a) {
                    X(d, a, Y(b, a))
                })
            }
        }
        return d
    }

    function Lh(d, f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b, function (a) {
                return Y(d, a).enumerable
            }));
            a.push.apply(a, b)
        }
        return a
    }

    function Re(d) {
        for (var f = 1; f < arguments.length; f++) {
            var a, b = null != arguments[f] ? arguments[f] : {};
            if (f % 2) q(a = Lh(Object(b), !0)).call(a, function (a) {
                Oa(d, a, b[a])
            }); else if (fa) Pa(d, fa(b)); else {
                var c;
                q(c = Lh(Object(b))).call(c, function (a) {
                    X(d, a, Y(b, a))
                })
            }
        }
        return d
    }

    function Mh(d,
                f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b, function (a) {
                return Y(d, a).enumerable
            }));
            a.push.apply(a, b)
        }
        return a
    }

    function Se(d) {
        for (var f = 1; f < arguments.length; f++) {
            var a, b = null != arguments[f] ? arguments[f] : {};
            if (f % 2) q(a = Mh(Object(b), !0)).call(a, function (a) {
                Oa(d, a, b[a])
            }); else if (fa) Pa(d, fa(b)); else {
                var c;
                q(c = Mh(Object(b))).call(c, function (a) {
                    X(d, a, Y(b, a))
                })
            }
        }
        return d
    }

    function Nh(d, f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b, function (a) {
                return Y(d, a).enumerable
            }));
            a.push.apply(a, b)
        }
        return a
    }

    function Rb(d) {
        for (var f = 1; f < arguments.length; f++) {
            var a, b = null != arguments[f] ? arguments[f] : {};
            if (f % 2) q(a = Nh(Object(b), !0)).call(a, function (a) {
                Oa(d, a, b[a])
            }); else if (fa) Pa(d, fa(b)); else {
                var c;
                q(c = Nh(Object(b))).call(c, function (a) {
                    X(d, a, Y(b, a))
                })
            }
        }
        return d
    }

    function Oh(d) {
        if (!(d instanceof Ph)) return (new p(n.INVALID_PARAMS, "Config should be instance of [ChannelMediaRelayConfiguration]")).throw();
        let f = d.getSrcChannelMediaInfo();
        d = d.getDestChannelMediaInfo();
        if (!f) return (new p(n.INVALID_PARAMS, "srcChannelMediaInfo should not be empty")).throw();
        if (0 === d.size) return (new p(n.INVALID_PARAMS, "destChannelMediaInfo should not be empty")).throw()
    }

    function Qh(d, f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b, function (a) {
                return Y(d, a).enumerable
            }));
            a.push.apply(a, b)
        }
        return a
    }

    function qc(d) {
        for (var f = 1; f < arguments.length; f++) {
            var a, b = null != arguments[f] ? arguments[f] : {};
            if (f % 2) q(a = Qh(Object(b), !0)).call(a, function (a) {
                Oa(d, a, b[a])
            }); else if (fa) Pa(d, fa(b)); else {
                var c;
                q(c = Qh(Object(b))).call(c, function (a) {
                    X(d, a, Y(b, a))
                })
            }
        }
        return d
    }

    async function ul(d,
                      f) {
        var a = null;
        if ("string" == typeof d) {
            let b = Rh.get(d);
            if (b) return k.debug("use cached audio resource: ", d), b;
            try {
                a = (await Hb(() => Bb.get(d, {responseType: "arraybuffer"}), void 0, void 0, {maxRetryCount: 3})).data
            } catch (c) {
                throw new p(n.FETCH_AUDIO_FILE_FAILED, c.toString());
            }
        } else a = await new v((a, c) => {
            const b = new FileReader;
            b.onload = b => {
                b.target ? a(b.target.result) : c(new p(n.READ_LOCAL_AUDIO_FILE_ERROR))
            };
            b.onerror = () => {
                c(new p(n.READ_LOCAL_AUDIO_FILE_ERROR))
            };
            b.readAsArrayBuffer(d)
        });
        a = await function (a) {
            const b =
                Rc();
            return new v((c, d) => {
                b.decodeAudioData(a, a => {
                    c(a)
                }, a => {
                    d(new p(n.DECODE_AUDIO_FILE_FAILED, a.toString()))
                })
            })
        }(a);
        return "string" == typeof d && f && Rh.set(d, a), a
    }

    function Sh(d, f) {
        var a = aa(d);
        if (ea) {
            var b = ea(d);
            f && (b = N(b).call(b, function (a) {
                return Y(d, a).enumerable
            }));
            a.push.apply(a, b)
        }
        return a
    }

    function Te(d) {
        for (var f = 1; f < arguments.length; f++) {
            var a, b = null != arguments[f] ? arguments[f] : {};
            if (f % 2) q(a = Sh(Object(b), !0)).call(a, function (a) {
                Oa(d, a, b[a])
            }); else if (fa) Pa(d, fa(b)); else {
                var c;
                q(c = Sh(Object(b))).call(c,
                    function (a) {
                        X(d, a, Y(b, a))
                    })
            }
        }
        return d
    }

    function Ue(d, f, a, b) {
        if (a.optimizationMode) if (b && b.width && b.height) {
            let c = function (a, b) {
                var c, d, e, g, f;
                if ("motion" === a) return k.debug(l(g = l(f = "adjust bitrate for motion, (".concat(b.bitrateMax, ", ")).call(f, b.bitrateMin, "}) -> (")).call(g, b.bitrateMax, ", undefined)")), {max: b.bitrateMax};
                if (!b.width || !b.height) return {max: b.bitrateMax, min: b.bitrateMin};
                a = fb(b.width) * fb(b.height);
                g = Math.max(.25, .1 + .03 * fb(b.frameRate || 20));
                if (19200 > a) return {};
                if (76800 > a) f = Tc[0]; else if (307200 >
                    a) f = Tc[1]; else if (921600 > a) f = Tc[2]; else if (2073600 > a) f = Tc[3]; else {
                    if (!(8294400 > a)) return {min: b.bitrateMin, max: b.bitrateMax};
                    f = Tc[4]
                }
                a = [Math.round((f[0][0] + f[0][1] * a) * g), Math.round((f[1][0] + f[1][1] * a) * g), Math.round((f[2][0] + f[2][1] * a) * g)];
                a = {min: Math.max(a[2], b.bitrateMin || 0), max: Math.max(a[2], b.bitrateMax || a[0])};
                return k.debug(l(c = l(d = l(e = "adjust bitrate for detail, (".concat(b.bitrateMax, ", ")).call(e, b.bitrateMin, "}) -> (")).call(d, a.max, ", ")).call(c, a.min, ")")), a
            }(a.optimizationMode, b);
            a.encoderConfig =
                Te({}, b, {bitrateMin: c.min, bitrateMax: c.max});
            ("motion" === a.optimizationMode || "detail" === a.optimizationMode && b.frameRate && 10 > fb(b.frameRate)) && (f.contentHint = a.optimizationMode, f.contentHint === a.optimizationMode ? k.debug("[".concat(d, "] set content hint to"), a.optimizationMode) : k.debug("[".concat(d, "] set content hint failed")))
        } else k.warning("[".concat(d, "] can not apply optimization mode bitrate config, no encoderConfig"))
    }

    var Th = "undefined" != typeof globalThis ? globalThis : "undefined" != typeof window ?
        window : "undefined" != typeof global ? global : "undefined" != typeof self ? self : {}, Hd = function (d) {
            return d && d.Math == Math && d
        },
        M = Hd("object" == typeof globalThis && globalThis) || Hd("object" == typeof window && window) || Hd("object" == typeof self && self) || Hd("object" == typeof Th && Th) || Function("return this")(),
        qa = function (d) {
            try {
                return !!d()
            } catch (f) {
                return !0
            }
        }, la = !qa(function () {
            return 7 != Object.defineProperty({}, "a", {
                get: function () {
                    return 7
                }
            }).a
        }), Uh = {}.propertyIsEnumerable, Vh = Object.getOwnPropertyDescriptor, Id = Vh && !Uh.call({1: 2},
        1) ? function (d) {
            d = Vh(this, d);
            return !!d && d.enumerable
        } : Uh, Yb = function (d, f) {
            return {enumerable: !(1 & d), configurable: !(2 & d), writable: !(4 & d), value: f}
        }, vl = {}.toString, Cb = function (d) {
            return vl.call(d).slice(8, -1)
        }, wl = "".split, Jd = qa(function () {
            return !Object("z").propertyIsEnumerable(0)
        }) ? function (d) {
            return "String" == Cb(d) ? wl.call(d, "") : Object(d)
        } : Object, Zb = function (d) {
            if (null == d) throw TypeError("Can't call method on " + d);
            return d
        }, hb = function (d) {
            return Jd(Zb(d))
        }, Ba = function (d) {
            return "object" == typeof d ? null !==
                d : "function" == typeof d
        }, rc = function (d, f) {
            if (!Ba(d)) return d;
            var a, b;
            if (f && "function" == typeof (a = d.toString) && !Ba(b = a.call(d)) || "function" == typeof (a = d.valueOf) && !Ba(b = a.call(d)) || !f && "function" == typeof (a = d.toString) && !Ba(b = a.call(d))) return b;
            throw TypeError("Can't convert object to primitive value");
        }, xl = {}.hasOwnProperty, T = function (d, f) {
            return xl.call(d, f)
        }, sc = M.document, Kd = Ba(sc) && Ba(sc.createElement), Wh = !la && !qa(function () {
            return 7 != Object.defineProperty(Kd ? sc.createElement("div") : {}, "a", {
                get: function () {
                    return 7
                }
            }).a
        }),
        Xh = Object.getOwnPropertyDescriptor, tc = la ? Xh : function (d, f) {
            if (d = hb(d), f = rc(f, !0), Wh) try {
                return Xh(d, f)
            } catch (a) {
            }
            if (T(d, f)) return Yb(!Id.call(d, f), d[f])
        }, yl = /#|\.prototype\./, uc = function (d, f) {
            d = zl[Al(d)];
            return d == Bl || d != Cl && ("function" == typeof f ? qa(f) : !!f)
        }, Al = uc.normalize = function (d) {
            return String(d).replace(yl, ".").toLowerCase()
        }, zl = uc.data = {}, Cl = uc.NATIVE = "N", Bl = uc.POLYFILL = "P", ha = {}, nb = function (d) {
            if ("function" != typeof d) throw TypeError(String(d) + " is not a function");
            return d
        }, $b = function (d,
                          f, a) {
            if (nb(d), void 0 === f) return d;
            switch (a) {
                case 0:
                    return function () {
                        return d.call(f)
                    };
                case 1:
                    return function (a) {
                        return d.call(f, a)
                    };
                case 2:
                    return function (a, c) {
                        return d.call(f, a, c)
                    };
                case 3:
                    return function (a, c, e) {
                        return d.call(f, a, c, e)
                    }
            }
            return function () {
                return d.apply(f, arguments)
            }
        }, Wa = function (d) {
            if (!Ba(d)) throw TypeError(String(d) + " is not an object");
            return d
        }, Yh = Object.defineProperty, ib = {
            f: la ? Yh : function (d, f, a) {
                if (Wa(d), f = rc(f, !0), Wa(a), Wh) try {
                    return Yh(d, f, a)
                } catch (b) {
                }
                if ("get" in a || "set" in
                    a) throw TypeError("Accessors not supported");
                return "value" in a && (d[f] = a.value), d
            }
        }, ob = la ? function (d, f, a) {
            return ib.f(d, f, Yb(1, a))
        } : function (d, f, a) {
            return d[f] = a, d
        }, Dl = tc, El = function (d) {
            var f = function (a, b, c) {
                if (this instanceof d) {
                    switch (arguments.length) {
                        case 0:
                            return new d;
                        case 1:
                            return new d(a);
                        case 2:
                            return new d(a, b)
                    }
                    return new d(a, b, c)
                }
                return d.apply(this, arguments)
            };
            return f.prototype = d.prototype, f
        }, O = function (d, f) {
            var a, b, c, e, g, h = d.target, m = d.global, r = d.stat, w = d.proto,
                y = m ? M : r ? M[h] : (M[h] || {}).prototype,
                k = m ? ha : ha[h] || (ha[h] = {}), B = k.prototype;
            for (b in f) {
                var l = !uc(m ? b : h + (r ? "." : "#") + b, d.forced) && y && T(y, b);
                var n = k[b];
                l && (c = d.noTargetGet ? (g = Dl(y, b)) && g.value : y[b]);
                var p = l && c ? c : f[b];
                l && typeof n == typeof p || (e = d.bind && l ? $b(p, M) : d.wrap && l ? El(p) : w && "function" == typeof p ? $b(Function.call, p) : p, (d.sham || p && p.sham || n && n.sham) && ob(e, "sham", !0), k[b] = e, w && (T(ha, a = h + "Prototype") || ob(ha, a, {}), ha[a][b] = p, d.real && B && !B[b] && ob(B, b, p)))
            }
        }, Zh = function (d) {
            return "function" == typeof d ? d : void 0
        }, Jb = function (d, f) {
            return 2 > arguments.length ?
                Zh(ha[d]) || Zh(M[d]) : ha[d] && ha[d][f] || M[d] && M[d][f]
        }, Ld = Jb("JSON", "stringify"), Fl = /[\uD800-\uDFFF]/g, $h = /^[\uD800-\uDBFF]$/, ai = /^[\uDC00-\uDFFF]$/,
        Gl = function (d, f, a) {
            var b = a.charAt(f - 1);
            f = a.charAt(f + 1);
            return $h.test(d) && !ai.test(f) || ai.test(d) && !$h.test(b) ? "\\u" + d.charCodeAt(0).toString(16) : d
        }, Hl = qa(function () {
            return '"\\udf06\\ud834"' !== Ld("\udf06\ud834") || '"\\udead"' !== Ld("\udead")
        });
    Ld && O({target: "JSON", stat: !0, forced: Hl}, {
        stringify: function (d, f, a) {
            var b = Ld.apply(null, arguments);
            return "string" ==
            typeof b ? b.replace(Fl, Gl) : b
        }
    });
    ha.JSON || (ha.JSON = {stringify: JSON.stringify});
    var A = function (d, f, a) {
            return ha.JSON.stringify.apply(null, arguments)
        }, vc = {}, Il = 0, Jl = Math.random(), Md = function (d) {
            return "Symbol(" + String(void 0 === d ? "" : d) + ")_" + (++Il + Jl).toString(36)
        }, Kl = !qa(function () {
            return Object.isExtensible(Object.preventExtensions({}))
        }), bi = wb(function (d) {
            var f = ib.f, a = Md("meta"), b = 0, c = Object.isExtensible || function () {
                return !0
            }, e = function (c) {
                f(c, a, {value: {objectID: "O" + ++b, weakData: {}}})
            }, g = d.exports = {
                REQUIRED: !1,
                fastKey: function (b, d) {
                    if (!Ba(b)) return "symbol" == typeof b ? b : ("string" == typeof b ? "S" : "P") + b;
                    if (!T(b, a)) {
                        if (!c(b)) return "F";
                        if (!d) return "E";
                        e(b)
                    }
                    return b[a].objectID
                }, getWeakData: function (b, d) {
                    if (!T(b, a)) {
                        if (!c(b)) return !0;
                        if (!d) return !1;
                        e(b)
                    }
                    return b[a].weakData
                }, onFreeze: function (b) {
                    return Kl && g.REQUIRED && c(b) && !T(b, a) && e(b), b
                }
            };
            vc[a] = !0
        }), ci = M["__core-js_shared__"] || function (d, f) {
            try {
                ob(M, d, f)
            } catch (a) {
                M[d] = f
            }
            return f
        }("__core-js_shared__", {}), Kb = wb(function (d) {
            (d.exports = function (d, a) {
                return ci[d] ||
                    (ci[d] = void 0 !== a ? a : {})
            })("versions", []).push({
                version: "3.4.3",
                mode: "pure",
                copyright: "\u00a9 2019 Denis Pushkarev (zloirock.ru)"
            })
        }), Db = !!Object.getOwnPropertySymbols && !qa(function () {
            return !String(Symbol())
        }), di = Db && !Symbol.sham && "symbol" == typeof Symbol(), Nd = Kb("wks"), Ve = M.Symbol, Ll = di ? Ve : Md,
        va = function (d) {
            return T(Nd, d) || (Db && T(Ve, d) ? Nd[d] = Ve[d] : Nd[d] = Ll("Symbol." + d)), Nd[d]
        }, Lb = {}, Ml = va("iterator"), Nl = Array.prototype, ei = function (d) {
            return void 0 !== d && (Lb.Array === d || Nl[Ml] === d)
        }, Ol = Math.ceil, Pl = Math.floor,
        Od = function (d) {
            return isNaN(d = +d) ? 0 : (0 < d ? Pl : Ol)(d)
        }, Ql = Math.min, pb = function (d) {
            return 0 < d ? Ql(Od(d), 9007199254740991) : 0
        }, fi = {};
    fi[va("toStringTag")] = "z";
    var We = "[object z]" === String(fi), Rl = va("toStringTag"), Sl = "Arguments" == Cb(function () {
        return arguments
    }()), Pd = We ? Cb : function (d) {
        var f;
        if (void 0 === d) var a = "Undefined"; else {
            if (null === d) var b = "Null"; else {
                a:{
                    var c = d = Object(d);
                    try {
                        b = c[Rl];
                        break a
                    } catch (e) {
                    }
                    b = void 0
                }
                b = "string" == typeof (a = b) ? a : Sl ? Cb(d) : "Object" == (f = Cb(d)) && "function" == typeof d.callee ? "Arguments" :
                    f
            }
            a = b
        }
        return a
    }, Tl = va("iterator"), gi = function (d) {
        if (null != d) return d[Tl] || d["@@iterator"] || Lb[Pd(d)]
    }, hi = function (d, f, a, b) {
        try {
            return b ? f(Wa(a)[0], a[1]) : f(a)
        } catch (c) {
            throw f = d.return, void 0 !== f && Wa(f.call(d)), c;
        }
    }, Uc = wb(function (d) {
        var f = function (a, b) {
            this.stopped = a;
            this.result = b
        };
        (d.exports = function (a, b, c, d, g) {
            var e, m;
            b = $b(b, c, d ? 2 : 1);
            if (!g) {
                if ("function" != typeof (g = gi(a))) throw TypeError("Target is not iterable");
                if (ei(g)) {
                    g = 0;
                    for (c = pb(a.length); c > g; g++) if ((e = d ? b(Wa(m = a[g])[0], m[1]) : b(a[g])) && e instanceof
                        f) return e;
                    return new f(!1)
                }
                a = g.call(a)
            }
            for (g = a.next; !(m = g.call(a)).done;) if ("object" == typeof (e = hi(a, b, m.value, d)) && e && e instanceof f) return e;
            return new f(!1)
        }).stop = function (a) {
            return new f(!0, a)
        }
    }), Xe = function (d, f, a) {
        if (!(d instanceof f)) throw TypeError("Incorrect " + (a ? a + " " : "") + "invocation");
        return d
    }, Ul = We ? {}.toString : function () {
        return "[object " + Pd(this) + "]"
    }, Vl = ib.f, ii = va("toStringTag"), Vc = function (d, f, a, b) {
        d && (d = a ? d : d.prototype, T(d, ii) || Vl(d, ii, {
            configurable: !0,
            value: f
        }), b && !We && ob(d, "toString",
            Ul))
    }, qb = function (d) {
        return Object(Zb(d))
    }, ac = Array.isArray || function (d) {
        return "Array" == Cb(d)
    }, Wl = va("species"), Ye = function (d, f) {
        var a;
        return ac(d) && ("function" != typeof (a = d.constructor) || a !== Array && !ac(a.prototype) ? Ba(a) && null === (a = a[Wl]) && (a = void 0) : a = void 0), new (void 0 === a ? Array : a)(0 === f ? 0 : f)
    }, Xl = [].push, bc = function (d) {
        var f = 1 == d, a = 2 == d, b = 3 == d, c = 4 == d, e = 6 == d, g = 5 == d || e;
        return function (h, m, r, w) {
            var k, l, B = qb(h), n = Jd(B);
            m = $b(m, r, 3);
            r = pb(n.length);
            var p = 0;
            w = w || Ye;
            for (h = f ? w(h, r) : a ? w(h, 0) : void 0; r > p; p++) if ((g ||
                p in n) && (l = m(k = n[p], p, B), d)) if (f) h[p] = l; else if (l) switch (d) {
                case 3:
                    return !0;
                case 5:
                    return k;
                case 6:
                    return p;
                case 2:
                    Xl.call(h, k)
            } else if (c) return !1;
            return e ? -1 : b || c ? c : h
        }
    }, wc = bc(0), Yl = bc(1), Zl = bc(2), $l = bc(3);
    bc(4);
    var am = bc(5), bm = bc(6), cm = Kb("native-function-to-string", Function.toString), ji = M.WeakMap,
        dm = "function" == typeof ji && /native code/.test(cm.call(ji)), ki = Kb("keys"), Qd = function (d) {
            return ki[d] || (ki[d] = Md(d))
        }, em = M.WeakMap;
    if (dm) {
        var xc = new em, fm = xc.get, gm = xc.has, hm = xc.set;
        var Ze = function (d, f) {
            return hm.call(xc,
                d, f), f
        };
        var Rd = function (d) {
            return fm.call(xc, d) || {}
        };
        var $e = function (d) {
            return gm.call(xc, d)
        }
    } else {
        var Wc = Qd("state");
        vc[Wc] = !0;
        Ze = function (d, f) {
            return ob(d, Wc, f), f
        };
        Rd = function (d) {
            return T(d, Wc) ? d[Wc] : {}
        };
        $e = function (d) {
            return T(d, Wc)
        }
    }
    var bb = {
            set: Ze, get: Rd, has: $e, enforce: function (d) {
                return $e(d) ? Rd(d) : Ze(d, {})
            }, getterFor: function (d) {
                return function (f) {
                    var a;
                    if (!Ba(f) || (a = Rd(f)).type !== d) throw TypeError("Incompatible receiver, " + d + " required");
                    return a
                }
            }
        }, im = ib.f, jm = bb.set, km = bb.getterFor, lm = Math.max,
        mm = Math.min, Sd = function (d, f) {
            d = Od(d);
            return 0 > d ? lm(d + f, 0) : mm(d, f)
        }, li = function (d) {
            return function (f, a, b) {
                var c;
                f = hb(f);
                var e = pb(f.length);
                b = Sd(b, e);
                if (d && a != a) for (; e > b;) {
                    if ((c = f[b++]) != c) return !0
                } else for (; e > b; b++) if ((d || b in f) && f[b] === a) return d || b || 0;
                return !d && -1
            }
        }, nm = li(!0), mi = li(!1), ni = function (d, f) {
            var a;
            d = hb(d);
            var b = 0, c = [];
            for (a in d) !T(vc, a) && T(d, a) && c.push(a);
            for (; f.length > b;) T(d, a = f[b++]) && (~mi(c, a) || c.push(a));
            return c
        }, Td = "constructor hasOwnProperty isPrototypeOf propertyIsEnumerable toLocaleString toString valueOf".split(" "),
        Sb = Object.keys || function (d) {
            return ni(d, Td)
        }, oi = la ? Object.defineProperties : function (d, f) {
            Wa(d);
            for (var a, b = Sb(f), c = b.length, e = 0; c > e;) ib.f(d, a = b[e++], f[a]);
            return d
        }, af = Jb("document", "documentElement"), pi = Qd("IE_PROTO"), bf = function () {
        }, Ud = function () {
            var d = Kd ? sc.createElement("iframe") : {};
            var f = Td.length;
            d.style.display = "none";
            af.appendChild(d);
            d.src = "javascript:";
            (d = d.contentWindow.document).open();
            d.write("<script>document.F=Object\x3c/script>");
            d.close();
            for (Ud = d.F; f--;) delete Ud.prototype[Td[f]];
            return Ud()
        }, cc = Object.create || function (d, f) {
            var a;
            return null !== d ? (bf.prototype = Wa(d), a = new bf, bf.prototype = null, a[pi] = d) : a = Ud(), void 0 === f ? a : oi(a, f)
        };
    vc[pi] = !0;
    var Vd, qi, ri, cf = function (d, f, a, b) {
        b && b.enumerable ? d[f] = a : ob(d, f, a)
    }, df = function (d, f, a) {
        for (var b in f) a && a.unsafe && d[b] ? d[b] = f[b] : cf(d, b, f[b], a);
        return d
    }, om = !qa(function () {
        function d() {
        }

        return d.prototype.constructor = null, Object.getPrototypeOf(new d) !== d.prototype
    }), si = Qd("IE_PROTO"), pm = Object.prototype, ef = om ? Object.getPrototypeOf : function (d) {
        return d =
            qb(d), T(d, si) ? d[si] : "function" == typeof d.constructor && d instanceof d.constructor ? d.constructor.prototype : d instanceof Object ? pm : null
    }, ti = (va("iterator"), !1);
    [].keys && ("next" in (ri = [].keys()) ? (qi = ef(ef(ri))) !== Object.prototype && (Vd = qi) : ti = !0);
    null == Vd && (Vd = {});
    var ui = Vd, Wd = ti, qm = function () {
            return this
        }, rm = (Object.setPrototypeOf || "__proto__" in {} && function () {
            var d = {};
            try {
                Object.getOwnPropertyDescriptor(Object.prototype, "__proto__").set.call(d, [])
            } catch (f) {
            }
        }(), ui), ff = va("iterator"), sm = function () {
            return this
        },
        gf = function (d, f, a, b, c, e, g) {
            !function (a, b, c) {
                b += " Iterator";
                a.prototype = cc(ui, {next: Yb(1, c)});
                Vc(a, b, !1, !0);
                Lb[b] = qm
            }(a, f, b);
            var h, m, r;
            b = function (b) {
                if (b === c && n) return n;
                if (!Wd && b in l) return l[b];
                switch (b) {
                    case "keys":
                    case "values":
                    case "entries":
                        return function () {
                            return new a(this, b)
                        }
                }
                return function () {
                    return new a(this)
                }
            };
            var w = f + " Iterator", k = !1, l = d.prototype, B = l[ff] || l["@@iterator"] || c && l[c],
                n = !Wd && B || b(c), p = "Array" == f && l.entries || B;
            if (p && (h = ef(p.call(new d)), rm !== Object.prototype && h.next && (Vc(h,
                w, !0, !0), Lb[w] = sm)), "values" == c && B && "values" !== B.name && (k = !0, n = function () {
                return B.call(this)
            }), g && l[ff] !== n && ob(l, ff, n), Lb[f] = n, c) if (m = {
                values: b("values"),
                keys: e ? n : b("keys"),
                entries: b("entries")
            }, g) for (r in m) !Wd && !k && r in l || cf(l, r, m[r]); else O({
                target: f,
                proto: !0,
                forced: Wd || k
            }, m);
            return m
        }, vi = va("species"), wi = function (d) {
            d = Jb(d);
            var f = ib.f;
            la && d && !d[vi] && f(d, vi, {
                configurable: !0, get: function () {
                    return this
                }
            })
        }, tm = ib.f, xi = bi.fastKey, yi = bb.set, hf = bb.getterFor, zi = (function (d, f, a) {
            var b = -1 !== d.indexOf("Map"),
                c = -1 !== d.indexOf("Weak"), e = b ? "set" : "add", g = M[d], h = g && g.prototype, m = {};
            if (la && "function" == typeof g && (c || h.forEach && !qa(function () {
                (new g).entries().next()
            }))) {
                var r = f(function (a, c) {
                    jm(Xe(a, r, d), {type: d, collection: new g});
                    null != c && Uc(c, a[e], a, b)
                });
                var w = km(d);
                wc("add clear delete forEach get has set keys values entries".split(" "), function (a) {
                    var b = "add" == a || "set" == a;
                    !(a in h) || c && "clear" == a || ob(r.prototype, a, function (d, e) {
                        var g = w(this).collection;
                        if (!b && c && !Ba(d)) return "get" == a && void 0;
                        d = g[a](0 ===
                        d ? 0 : d, e);
                        return b ? this : d
                    })
                });
                c || im(r.prototype, "size", {
                    configurable: !0, get: function () {
                        return w(this).collection.size
                    }
                })
            } else r = a.getConstructor(f, d, b, e), bi.REQUIRED = !0;
            Vc(r, d, !1, !0);
            m[d] = r;
            O({global: !0, forced: !0}, m);
            c || a.setStrong(r, d, b)
        }("Map", function (d) {
            return function () {
                return d(this, arguments.length ? arguments[0] : void 0)
            }
        }, {
            getConstructor: function (d, f, a, b) {
                var c = d(function (d, e) {
                        Xe(d, c, f);
                        yi(d, {type: f, index: cc(null), first: void 0, last: void 0, size: 0});
                        la || (d.size = 0);
                        null != e && Uc(e, d[b], d, a)
                    }), e = hf(f),
                    g = function (a, b, c) {
                        var d, g, f = e(a), m = h(a, b);
                        return m ? m.value = c : (f.last = m = {
                            index: g = xi(b, !0),
                            key: b,
                            value: c,
                            previous: d = f.last,
                            next: void 0,
                            removed: !1
                        }, f.first || (f.first = m), d && (d.next = m), la ? f.size++ : a.size++, "F" !== g && (f.index[g] = m)), a
                    }, h = function (a, b) {
                        a = e(a);
                        var c = xi(b);
                        if ("F" !== c) return a.index[c];
                        for (a = a.first; a; a = a.next) if (a.key == b) return a
                    };
                return df(c.prototype, {
                    clear: function () {
                        for (var a = e(this), b = a.index, c = a.first; c;) c.removed = !0, c.previous && (c.previous = c.previous.next = void 0), delete b[c.index], c =
                            c.next;
                        a.first = a.last = void 0;
                        la ? a.size = 0 : this.size = 0
                    }, delete: function (a) {
                        var b = e(this);
                        if (a = h(this, a)) {
                            var c = a.next, d = a.previous;
                            delete b.index[a.index];
                            a.removed = !0;
                            d && (d.next = c);
                            c && (c.previous = d);
                            b.first == a && (b.first = c);
                            b.last == a && (b.last = d);
                            la ? b.size-- : this.size--
                        }
                        return !!a
                    }, forEach: function (a) {
                        for (var b, c = e(this), d = $b(a, 1 < arguments.length ? arguments[1] : void 0, 3); b = b ? b.next : c.first;) for (d(b.value, b.key, this); b && b.removed;) b = b.previous
                    }, has: function (a) {
                        return !!h(this, a)
                    }
                }), df(c.prototype, a ? {
                    get: function (a) {
                        return (a =
                            h(this, a)) && a.value
                    }, set: function (a, b) {
                        return g(this, 0 === a ? 0 : a, b)
                    }
                } : {
                    add: function (a) {
                        return g(this, a = 0 === a ? 0 : a, a)
                    }
                }), la && tm(c.prototype, "size", {
                    get: function () {
                        return e(this).size
                    }
                }), c
            }, setStrong: function (d, f, a) {
                var b = f + " Iterator", c = hf(f), e = hf(b);
                gf(d, f, function (a, d) {
                    yi(this, {type: b, target: a, state: c(a), kind: d, last: void 0})
                }, function () {
                    for (var a = e(this), b = a.kind, c = a.last; c && c.removed;) c = c.previous;
                    return a.target && (a.last = c = c ? c.next : a.state.first) ? "keys" == b ? {
                        value: c.key,
                        done: !1
                    } : "values" == b ? {
                        value: c.value,
                        done: !1
                    } : {value: [c.key, c.value], done: !1} : (a.target = void 0, {value: void 0, done: !0})
                }, a ? "entries" : "values", !a, !0);
                wi(f)
            }
        }), function (d) {
            return function (f, a) {
                var b, c;
                f = String(Zb(f));
                a = Od(a);
                var e = f.length;
                return 0 > a || a >= e ? d ? "" : void 0 : 55296 > (b = f.charCodeAt(a)) || 56319 < b || a + 1 === e || 56320 > (c = f.charCodeAt(a + 1)) || 57343 < c ? d ? f.charAt(a) : b : d ? f.slice(a, a + 2) : c - 56320 + (b - 55296 << 10) + 65536
            }
        }), um = {codeAt: zi(!1), charAt: zi(!0)}.charAt, vm = bb.set, wm = bb.getterFor("String Iterator");
    gf(String, "String", function (d) {
        vm(this, {
            type: "String Iterator",
            string: String(d), index: 0
        })
    }, function () {
        var d, f = wm(this), a = f.string, b = f.index;
        return b >= a.length ? {value: void 0, done: !0} : (d = um(a, b), f.index += d.length, {value: d, done: !1})
    });
    var xm = bb.set, ym = bb.getterFor("Array Iterator");
    gf(Array, "Array", function (d, f) {
        xm(this, {type: "Array Iterator", target: hb(d), index: 0, kind: f})
    }, function () {
        var d = ym(this), f = d.target, a = d.kind, b = d.index++;
        return !f || b >= f.length ? (d.target = void 0, {value: void 0, done: !0}) : "keys" == a ? {
            value: b,
            done: !1
        } : "values" == a ? {value: f[b], done: !1} : {
            value: [b,
                f[b]], done: !1
        }
    }, "values");
    Lb.Arguments = Lb.Array;
    var Ai = va("toStringTag"), Xd;
    for (Xd in {
        CSSRuleList: 0,
        CSSStyleDeclaration: 0,
        CSSValueList: 0,
        ClientRectList: 0,
        DOMRectList: 0,
        DOMStringList: 0,
        DOMTokenList: 1,
        DataTransferItemList: 0,
        FileList: 0,
        HTMLAllCollection: 0,
        HTMLCollection: 0,
        HTMLFormElement: 0,
        HTMLSelectElement: 0,
        MediaList: 0,
        MimeTypeArray: 0,
        NamedNodeMap: 0,
        NodeList: 1,
        PaintRequestList: 0,
        Plugin: 0,
        PluginArray: 0,
        SVGLengthList: 0,
        SVGNumberList: 0,
        SVGPathSegList: 0,
        SVGPointList: 0,
        SVGStringList: 0,
        SVGTransformList: 0,
        SourceBufferList: 0,
        StyleSheetList: 0,
        TextTrackCueList: 0,
        TextTrackList: 0,
        TouchList: 0
    }) {
        var Bi = M[Xd], jf = Bi && Bi.prototype;
        jf && !jf[Ai] && ob(jf, Ai, Xd);
        Lb[Xd] = Lb.Array
    }
    var ba = ha.Map, zm = va("match"), kf = function (d) {
        var f;
        if (Ba(d) && (void 0 !== (f = d[zm]) ? f : "RegExp" == Cb(d))) throw TypeError("The method doesn't accept regular expressions");
        return d
    }, Am = va("match"), lf = function (d) {
        var f = /./;
        try {
            "/./"[d](f)
        } catch (a) {
            try {
                return f[Am] = !1, "/./"[d](f)
            } catch (b) {
            }
        }
        return !1
    }, Ci = "".endsWith, Bm = Math.min, Cm = lf("endsWith");
    O({
        target: "String",
        proto: !0, forced: !Cm
    }, {
        endsWith: function (d) {
            var f = String(Zb(this));
            kf(d);
            var a = 1 < arguments.length ? arguments[1] : void 0, b = pb(f.length);
            a = void 0 === a ? b : Bm(pb(a), b);
            b = String(d);
            return Ci ? Ci.call(f, b, a) : f.slice(a - b.length, a) === b
        }
    });
    var za = function (d) {
        return ha[d + "Prototype"]
    }, Dm = za("String").endsWith, Di = String.prototype, gg = function (d) {
        var f = d.endsWith;
        return "string" == typeof d || d === Di || d instanceof String && f === Di.endsWith ? Dm : f
    }, Xc = function (d, f) {
        var a = [][d];
        return !a || !qa(function () {
            a.call(null, f || function () {
                throw 1;
            }, 1)
        })
    }, Ei = Xc("forEach") ? function (d) {
        return wc(this, d, 1 < arguments.length ? arguments[1] : void 0)
    } : [].forEach;
    O({target: "Array", proto: !0, forced: [].forEach != Ei}, {forEach: Ei});
    var Em = za("Array").forEach, Fi = Array.prototype, Fm = {DOMTokenList: !0, NodeList: !0}, q = function (d) {
        var f = d.forEach;
        return d === Fi || d instanceof Array && f === Fi.forEach || Fm.hasOwnProperty(Pd(d)) ? Em : f
    }, Yc = {f: Object.getOwnPropertySymbols}, Yd = Object.assign, Gi = !Yd || qa(function () {
        var d = {}, f = {}, a = Symbol();
        return d[a] = 7, "abcdefghijklmnopqrst".split("").forEach(function (a) {
            f[a] =
                a
        }), 7 != Yd({}, d)[a] || "abcdefghijklmnopqrst" != Sb(Yd({}, f)).join("")
    }) ? function (d, f) {
        for (var a = qb(d), b = arguments.length, c = 1, e = Yc.f, g = Id; b > c;) for (var h, m = Jd(arguments[c++]), r = e ? Sb(m).concat(e(m)) : Sb(m), w = r.length, k = 0; w > k;) h = r[k++], la && !g.call(m, h) || (a[h] = m[h]);
        return a
    } : Yd;
    O({target: "Object", stat: !0, forced: Object.assign !== Gi}, {assign: Gi});
    var Ha = ha.Object.assign, Gm = qa(function () {
        Sb(1)
    });
    O({target: "Object", stat: !0, forced: Gm}, {
        keys: function (d) {
            return Sb(qb(d))
        }
    });
    var aa = ha.Object.keys, Hi = function (d) {
        return function (f,
                         a, b, c) {
            nb(a);
            f = qb(f);
            var e = Jd(f), g = pb(f.length), h = d ? g - 1 : 0, m = d ? -1 : 1;
            if (2 > b) for (; ;) {
                if (h in e) {
                    c = e[h];
                    h += m;
                    break
                }
                if (h += m, d ? 0 > h : g <= h) throw TypeError("Reduce of empty array with no initial value");
            }
            for (; d ? 0 <= h : g > h; h += m) h in e && (c = a(c, e[h], h, f));
            return c
        }
    }, Hm = {left: Hi(!1), right: Hi(!0)}.left;
    O({target: "Array", proto: !0, forced: Xc("reduce")}, {
        reduce: function (d) {
            return Hm(this, d, arguments.length, 1 < arguments.length ? arguments[1] : void 0)
        }
    });
    var Im = za("Array").reduce, Ii = Array.prototype, rd = function (d) {
        var f = d.reduce;
        return d === Ii || d instanceof Array && f === Ii.reduce ? Im : f
    };
    O({target: "Object", stat: !0, forced: !la, sham: !la}, {defineProperty: ib.f});
    var Ji = wb(function (d) {
            var f = ha.Object;
            d = d.exports = function (a, b, c) {
                return f.defineProperty(a, b, c)
            };
            f.defineProperty.sham && (d.sham = !0)
        }), X = Ji,
        Jm = /^[\t\n\x0B\f\r \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029\ufeff][\t\n\x0B\f\r \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029\ufeff]*/,
        Km = /[\t\n\x0B\f\r \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029\ufeff][\t\n\x0B\f\r \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029\ufeff]*$/,
        mf = function (d) {
            return function (f) {
                f = String(Zb(f));
                return 1 & d && (f = f.replace(Jm, "")), 2 & d && (f = f.replace(Km, "")), f
            }
        };
    mf(1);
    mf(2);
    var Ki = mf(3), Zd = M.parseInt, Lm = /^[+-]?0[Xx]/,
        Li = 8 !== Zd("\t\n\x0B\f\r \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029\ufeff08") ||
        22 !== Zd("\t\n\x0B\f\r \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029\ufeff0x16") ? function (d, f) {
            d = Ki(String(d));
            return Zd(d, f >>> 0 || (Lm.test(d) ? 16 : 10))
        } : Zd;
    O({global: !0, forced: parseInt != Li}, {parseInt: Li});
    var oa = ha.parseInt;
    let cg = !0, dg = !0;
    var Mi = tc, Mm = qa(function () {
        Mi(1)
    });
    O({target: "Object", stat: !0, forced: !la || Mm, sham: !la}, {
        getOwnPropertyDescriptor: function (d, f) {
            return Mi(hb(d), f)
        }
    });
    var yc, $d, Y = wb(function (d) {
        var f = ha.Object;
        d = d.exports =
            function (a, b) {
                return f.getOwnPropertyDescriptor(a, b)
            };
        f.getOwnPropertyDescriptor.sham && (d.sham = !0)
    }), dc = function (d, f, a) {
        f = rc(f);
        f in d ? ib.f(d, f, Yb(0, a)) : d[f] = a
    }, Zc = Jb("navigator", "userAgent") || "", Ni = M.process, Oi = Ni && Ni.versions, Pi = Oi && Oi.v8;
    Pi ? $d = (yc = Pi.split("."))[0] + yc[1] : Zc && (!(yc = Zc.match(/Edge\/(\d+)/)) || 74 <= yc[1]) && (yc = Zc.match(/Chrome\/(\d+)/)) && ($d = yc[1]);
    var ae = $d && +$d, Nm = va("species"), $c = function (d) {
        return 51 <= ae || !qa(function () {
            var f = [];
            return (f.constructor = {})[Nm] = function () {
                return {foo: 1}
            },
            1 !== f[d](Boolean).foo
        })
    }, Qi = va("isConcatSpreadable"), Om = 51 <= ae || !qa(function () {
        var d = [];
        return d[Qi] = !1, d.concat()[0] !== d
    }), Pm = $c("concat");
    O({target: "Array", proto: !0, forced: !Om || !Pm}, {
        concat: function (d) {
            var f, a, b = qb(this), c = Ye(b, 0), e = 0;
            var g = -1;
            for (f = arguments.length; g < f; g++) {
                var h = a = -1 === g ? b : arguments[g];
                if (Ba(h)) {
                    var m = h[Qi];
                    h = void 0 !== m ? !!m : ac(h)
                } else h = !1;
                if (h) {
                    if (9007199254740991 < e + (m = pb(a.length))) throw TypeError("Maximum allowed index exceeded");
                    for (h = 0; h < m; h++, e++) h in a && dc(c, e, a[h])
                } else {
                    if (9007199254740991 <=
                        e) throw TypeError("Maximum allowed index exceeded");
                    dc(c, e++, a)
                }
            }
            return c.length = e, c
        }
    });
    var Qm = za("Array").concat, Ri = Array.prototype, l = function (d) {
        var f = d.concat;
        return d === Ri || d instanceof Array && f === Ri.concat ? Qm : f
    };
    O({target: "Array", proto: !0, forced: !$c("filter")}, {
        filter: function (d) {
            return Zl(this, d, 1 < arguments.length ? arguments[1] : void 0)
        }
    });
    var Rm = za("Array").filter, Si = Array.prototype, N = function (d) {
            var f = d.filter;
            return d === Si || d instanceof Array && f === Si.filter ? Rm : f
        }, Sm = M.Promise, Ti = va("iterator"),
        Ui = !1;
    try {
        var Tm = 0, Vi = {
            next: function () {
                return {done: !!Tm++}
            }, return: function () {
                Ui = !0
            }
        };
        Vi[Ti] = function () {
            return this
        };
        Array.from(Vi, function () {
            throw 2;
        })
    } catch (d) {
    }
    var zc, Wi, nf, Xi = function (d, f) {
            if (!f && !Ui) return !1;
            var a = !1;
            try {
                f = {}, f[Ti] = function () {
                    return {
                        next: function () {
                            return {done: a = !0}
                        }
                    }
                }, d(f)
            } catch (b) {
            }
            return a
        }, Um = va("species"), Yi = function (d, f) {
            var a;
            d = Wa(d).constructor;
            return void 0 === d || null == (a = Wa(d)[Um]) ? f : nb(a)
        }, Zi = /(iphone|ipod|ipad).*applewebkit/i.test(Zc), $i = M.location, of = M.setImmediate,
        aj = M.clearImmediate, bj = M.process, cj = M.MessageChannel, pf = M.Dispatch, qf = 0, ad = {},
        rf = function (d) {
            if (ad.hasOwnProperty(d)) {
                var f = ad[d];
                delete ad[d];
                f()
            }
        }, sf = function (d) {
            return function () {
                rf(d)
            }
        }, dj = function (d) {
            rf(d.data)
        }, ej = function (d) {
            M.postMessage(d + "", $i.protocol + "//" + $i.host)
        };
    of && aj || (of = function (d) {
        for (var f = [], a = 1; arguments.length > a;) f.push(arguments[a++]);
        return ad[++qf] = function () {
            ("function" == typeof d ? d : Function(d)).apply(void 0, f)
        }, zc(qf), qf
    }, aj = function (d) {
        delete ad[d]
    }, "process" == Cb(bj) ?
        zc = function (d) {
            bj.nextTick(sf(d))
        } : pf && pf.now ? zc = function (d) {
            pf.now(sf(d))
        } : cj && !Zi ? (nf = (Wi = new cj).port2, Wi.port1.onmessage = dj, zc = $b(nf.postMessage, nf, 1)) : !M.addEventListener || "function" != typeof postMessage || M.importScripts || qa(ej) ? zc = "onreadystatechange" in (Kd ? sc.createElement("script") : {}) ? function (d) {
            af.appendChild(Kd ? sc.createElement("script") : {}).onreadystatechange = function () {
                af.removeChild(this);
                rf(d)
            }
        } : function (d) {
            setTimeout(sf(d), 0)
        } : (zc = ej, M.addEventListener("message", dj, !1)));
    var bd, ec,
        cd, Ac, tf, uf, vf, fj, wf = of, Vm = tc, gj = M.MutationObserver || M.WebKitMutationObserver, xf = M.process,
        yf = M.Promise, hj = "process" == Cb(xf), ij = Vm(M, "queueMicrotask"), jj = ij && ij.value;
    jj || (bd = function () {
        var d;
        for (hj && (d = xf.domain) && d.exit(); ec;) {
            var f = ec.fn;
            ec = ec.next;
            try {
                f()
            } catch (a) {
                throw ec ? Ac() : cd = void 0, a;
            }
        }
        cd = void 0;
        d && d.enter()
    }, hj ? Ac = function () {
        xf.nextTick(bd)
    } : gj && !Zi ? (tf = !0, uf = document.createTextNode(""), (new gj(bd)).observe(uf, {characterData: !0}), Ac = function () {
        uf.data = tf = !tf
    }) : yf && yf.resolve ? (vf = yf.resolve(void 0),
        fj = vf.then, Ac = function () {
        fj.call(vf, bd)
    }) : Ac = function () {
        wf.call(M, bd)
    });
    var zf, kj, lj = jj || function (d) {
            d = {fn: d, next: void 0};
            cd && (cd.next = d);
            ec || (ec = d, Ac());
            cd = d
        }, Wm = function (d) {
            var f, a;
            this.promise = new d(function (b, c) {
                if (void 0 !== f || void 0 !== a) throw TypeError("Bad Promise constructor");
                f = b;
                a = c
            });
            this.resolve = nb(f);
            this.reject = nb(a)
        }, be = {
            f: function (d) {
                return new Wm(d)
            }
        }, Af = function (d, f) {
            if (Wa(d), Ba(f) && f.constructor === d) return f;
            d = be.f(d);
            return (0, d.resolve)(f), d.promise
        }, ce = function (d) {
            try {
                return {
                    error: !1,
                    value: d()
                }
            } catch (f) {
                return {error: !0, value: f}
            }
        }, Xm = va("species"), mj = bb.get, Ym = bb.set, Zm = bb.getterFor("Promise"), Xa = Sm, nj = M.TypeError,
        Bf = M.document, de = M.process, $m = Kb("inspectSource"), Bc = (Jb("fetch"), be.f), an = Bc,
        dd = "process" == Cb(de), bn = !!(Bf && Bf.createEvent && M.dispatchEvent), ee = uc("Promise", function () {
            if ($m(Xa) === String(Xa) && (66 === ae || !dd && "function" != typeof PromiseRejectionEvent) || !Xa.prototype.finally) return !0;
            if (51 <= ae && /native code/.test(Xa)) return !1;
            var d = Xa.resolve(1), f = function (a) {
                a(function () {
                    },
                    function () {
                    })
            };
            return (d.constructor = {})[Xm] = f, !(d.then(function () {
            }) instanceof f)
        }), cn = ee || !Xi(function (d) {
            Xa.all(d).catch(function () {
            })
        }), oj = function (d) {
            var f;
            return !(!Ba(d) || "function" != typeof (f = d.then)) && f
        }, Cf = function (d, f, a) {
            if (!f.notified) {
                f.notified = !0;
                var b = f.reactions;
                lj(function () {
                    for (var c = f.value, e = 1 == f.state, g = 0; b.length > g;) {
                        var h, m, r, w = b[g++], k = e ? w.ok : w.fail, l = w.resolve, B = w.reject, n = w.domain;
                        try {
                            k ? (e || (2 === f.rejection && dn(d, f), f.rejection = 1), !0 === k ? h = c : (n && n.enter(), h = k(c), n && (n.exit(),
                                r = !0)), h === w.promise ? B(nj("Promise-chain cycle")) : (m = oj(h)) ? m.call(h, l, B) : l(h)) : B(c)
                        } catch (z) {
                            n && !r && n.exit(), B(z)
                        }
                    }
                    f.reactions = [];
                    f.notified = !1;
                    a && !f.rejection && en(d, f)
                })
            }
        }, pj = function (d, f, a) {
            var b, c;
            bn ? ((b = Bf.createEvent("Event")).promise = f, b.reason = a, b.initEvent(d, !1, !0), M.dispatchEvent(b)) : b = {
                promise: f,
                reason: a
            };
            (c = M["on" + d]) ? c(b) : "unhandledrejection" === d && function (a, b) {
                var c = M.console;
                c && c.error && (1 === arguments.length ? c.error(a) : c.error(a, b))
            }("Unhandled promise rejection", a)
        }, en = function (d,
                          f) {
            wf.call(M, function () {
                var a, b = f.value;
                if (1 !== f.rejection && !f.parent && (a = ce(function () {
                    dd ? de.emit("unhandledRejection", b, d) : pj("unhandledrejection", d, b)
                }), f.rejection = dd || 1 !== f.rejection && !f.parent ? 2 : 1, a.error)) throw a.value;
            })
        }, dn = function (d, f) {
            wf.call(M, function () {
                dd ? de.emit("rejectionHandled", d) : pj("rejectionhandled", d, f.value)
            })
        }, Cc = function (d, f, a, b) {
            return function (c) {
                d(f, a, c, b)
            }
        }, Dc = function (d, f, a, b) {
            f.done || (f.done = !0, b && (f = b), f.value = a, f.state = 2, Cf(d, f, !0))
        }, Df = function (d, f, a, b) {
            if (!f.done) {
                f.done =
                    !0;
                b && (f = b);
                try {
                    if (d === a) throw nj("Promise can't be resolved itself");
                    var c = oj(a);
                    c ? lj(function () {
                        var b = {done: !1};
                        try {
                            c.call(a, Cc(Df, d, b, f), Cc(Dc, d, b, f))
                        } catch (g) {
                            Dc(d, b, g, f)
                        }
                    }) : (f.value = a, f.state = 1, Cf(d, f, !1))
                } catch (e) {
                    Dc(d, {done: !1}, e, f)
                }
            }
        };
    ee && (Xa = function (d) {
        Xe(this, Xa, "Promise");
        nb(d);
        zf.call(this);
        var f = mj(this);
        try {
            d(Cc(Df, this, f), Cc(Dc, this, f))
        } catch (a) {
            Dc(this, f, a)
        }
    }, (zf = function (d) {
        Ym(this, {
            type: "Promise",
            done: !1,
            notified: !1,
            parent: !1,
            reactions: [],
            rejection: !1,
            state: 0,
            value: void 0
        })
    }).prototype =
        df(Xa.prototype, {
            then: function (d, f) {
                var a = Zm(this), b = Bc(Yi(this, Xa));
                return b.ok = "function" != typeof d || d, b.fail = "function" == typeof f && f, b.domain = dd ? de.domain : void 0, a.parent = !0, a.reactions.push(b), 0 != a.state && Cf(this, a, !1), b.promise
            }, catch: function (d) {
                return this.then(void 0, d)
            }
        }), kj = function () {
        var d = new zf, f = mj(d);
        this.promise = d;
        this.resolve = Cc(Df, d, f);
        this.reject = Cc(Dc, d, f)
    }, be.f = Bc = function (d) {
        return d === Xa || d === qj ? new kj(d) : an(d)
    });
    O({global: !0, wrap: !0, forced: ee}, {Promise: Xa});
    Vc(Xa, "Promise",
        !1, !0);
    wi("Promise");
    var qj = Jb("Promise");
    O({target: "Promise", stat: !0, forced: ee}, {
        reject: function (d) {
            var f = Bc(this);
            return f.reject.call(void 0, d), f.promise
        }
    });
    O({target: "Promise", stat: !0, forced: !0}, {
        resolve: function (d) {
            return Af(this === qj ? Xa : this, d)
        }
    });
    O({target: "Promise", stat: !0, forced: cn}, {
        all: function (d) {
            var f = this, a = Bc(f), b = a.resolve, c = a.reject, e = ce(function () {
                var a = nb(f.resolve), e = [], m = 0, r = 1;
                Uc(d, function (d) {
                    var g = m++, h = !1;
                    e.push(void 0);
                    r++;
                    a.call(f, d).then(function (a) {
                        h || (h = !0, e[g] = a, --r ||
                        b(e))
                    }, c)
                });
                --r || b(e)
            });
            return e.error && c(e.value), a.promise
        }, race: function (d) {
            var f = this, a = Bc(f), b = a.reject, c = ce(function () {
                var c = nb(f.resolve);
                Uc(d, function (d) {
                    c.call(f, d).then(a.resolve, b)
                })
            });
            return c.error && b(c.value), a.promise
        }
    });
    O({target: "Promise", stat: !0}, {
        allSettled: function (d) {
            var f = this, a = be.f(f), b = a.resolve, c = a.reject, e = ce(function () {
                var a = nb(f.resolve), c = [], e = 0, r = 1;
                Uc(d, function (d) {
                    var g = e++, h = !1;
                    c.push(void 0);
                    r++;
                    a.call(f, d).then(function (a) {
                        h || (h = !0, c[g] = {status: "fulfilled", value: a},
                        --r || b(c))
                    }, function (a) {
                        h || (h = !0, c[g] = {status: "rejected", reason: a}, --r || b(c))
                    })
                });
                --r || b(c)
            });
            return e.error && c(e.value), a.promise
        }
    });
    O({target: "Promise", proto: !0, real: !0}, {
        finally: function (d) {
            var f = Yi(this, Jb("Promise")), a = "function" == typeof d;
            return this.then(a ? function (a) {
                return Af(f, d()).then(function () {
                    return a
                })
            } : d, a ? function (a) {
                return Af(f, d()).then(function () {
                    throw a;
                })
            } : d)
        }
    });
    var v = ha.Promise;
    O({target: "Array", proto: !0, forced: !$c("map")}, {
        map: function (d) {
            return Yl(this, d, 1 < arguments.length ?
                arguments[1] : void 0)
        }
    });
    var fn = za("Array").map, rj = Array.prototype, C = function (d) {
        var f = d.map;
        return d === rj || d instanceof Array && f === rj.map ? fn : f
    }, gn = Math.max, hn = Math.min;
    O({target: "Array", proto: !0, forced: !$c("splice")}, {
        splice: function (d, f) {
            var a, b, c, e, g = qb(this), h = pb(g.length), m = Sd(d, h);
            var r = arguments.length;
            if (0 === r ? a = b = 0 : 1 === r ? (a = 0, b = h - m) : (a = r - 2, b = hn(gn(Od(f), 0), h - m)), 9007199254740991 < h + a - b) throw TypeError("Maximum allowed length exceeded");
            r = Ye(g, b);
            for (c = 0; c < b; c++) (e = m + c) in g && dc(r, c, g[e]);
            if (r.length =
                b, a < b) {
                for (c = m; c < h - b; c++) {
                    var w = c + a;
                    (e = c + b) in g ? g[w] = g[e] : delete g[w]
                }
                for (c = h; c > h - b + a; c--) delete g[c - 1]
            } else if (a > b) for (c = h - b; c > m; c--) w = c + a - 1, (e = c + b - 1) in g ? g[w] = g[e] : delete g[w];
            for (c = 0; c < a; c++) g[c + m] = arguments[c + 2];
            return g.length = h - b + a, r
        }
    });
    var jn = za("Array").splice, sj = Array.prototype, Ia = function (d) {
        var f = d.splice;
        return d === sj || d instanceof Array && f === sj.splice ? jn : f
    }, tj = [].indexOf, uj = !!tj && 0 > 1 / [1].indexOf(1, -0), kn = Xc("indexOf");
    O({target: "Array", proto: !0, forced: uj || kn}, {
        indexOf: function (d) {
            return uj ?
                tj.apply(this, arguments) || 0 : mi(this, d, 1 < arguments.length ? arguments[1] : void 0)
        }
    });
    var ln = za("Array").indexOf, vj = Array.prototype, G = function (d) {
        var f = d.indexOf;
        return d === vj || d instanceof Array && f === vj.indexOf ? ln : f
    }, mn = va("species"), nn = [].slice, on = Math.max;
    O({target: "Array", proto: !0, forced: !$c("slice")}, {
        slice: function (d, f) {
            var a, b = hb(this);
            var c = pb(b.length);
            d = Sd(d, c);
            f = Sd(void 0 === f ? c : f, c);
            if (ac(b) && ("function" != typeof (a = b.constructor) || a !== Array && !ac(a.prototype) ? Ba(a) && null === (a = a[mn]) && (a = void 0) :
                a = void 0, a === Array || void 0 === a)) return nn.call(b, d, f);
            a = new (void 0 === a ? Array : a)(on(f - d, 0));
            for (c = 0; d < f; d++, c++) d in b && dc(a, c, b[d]);
            return a.length = c, a
        }
    });
    var pn = za("Array").slice, wj = Array.prototype, Aa = function (d) {
        var f = d.slice;
        return d === wj || d instanceof Array && f === wj.slice ? pn : f
    }, xj = !0;
    "find" in [] && Array(1).find(function () {
        xj = !1
    });
    O({target: "Array", proto: !0, forced: xj}, {
        find: function (d) {
            return am(this, d, 1 < arguments.length ? arguments[1] : void 0)
        }
    });
    var qn = za("Array").find, yj = Array.prototype, R = function (d) {
        var f =
            d.find;
        return d === yj || d instanceof Array && f === yj.find ? qn : f
    }, zj = [].slice, Ef = {};
    O({target: "Function", proto: !0}, {
        bind: Function.bind || function (d) {
            var f = nb(this), a = zj.call(arguments, 1), b = function () {
                var c = a.concat(zj.call(arguments));
                if (this instanceof b) {
                    var e = c.length;
                    if (!(e in Ef)) {
                        for (var g = [], h = 0; h < e; h++) g[h] = "a[" + h + "]";
                        Ef[e] = Function("C,a", "return new C(" + g.join(",") + ")")
                    }
                    c = Ef[e](f, c)
                } else c = f.apply(d, c);
                return c
            };
            return Ba(f.prototype) && (b.prototype = f.prototype), b
        }
    });
    var rn = za("Function").bind, Aj =
        Function.prototype, ta = function (d) {
        var f = d.bind;
        return d === Aj || d instanceof Function && f === Aj.bind ? rn : f
    };
    O({target: "Array", proto: !0}, {
        includes: function (d) {
            return nm(this, d, 1 < arguments.length ? arguments[1] : void 0)
        }
    });
    var sn = za("Array").includes;
    O({target: "String", proto: !0, forced: !lf("includes")}, {
        includes: function (d) {
            return !!~String(Zb(this)).indexOf(kf(d), 1 < arguments.length ? arguments[1] : void 0)
        }
    });
    var tn = za("String").includes, Bj = Array.prototype, Cj = String.prototype, ya = function (d) {
        var f = d.includes;
        return d ===
        Bj || d instanceof Array && f === Bj.includes ? sn : "string" == typeof d || d === Cj || d instanceof String && f === Cj.includes ? tn : f
    };
    O({target: "Array", proto: !0, forced: Xc("some")}, {
        some: function (d) {
            return $l(this, d, 1 < arguments.length ? arguments[1] : void 0)
        }
    });
    var un = za("Array").some, Dj = Array.prototype, ig = function (d) {
        var f = d.some;
        return d === Dj || d instanceof Array && f === Dj.some ? un : f
    };
    let jg = mb;
    var Ej = Object.freeze({
        __proto__: null,
        shimMediaStream: kg,
        shimOnTrack: lg,
        shimGetSendersWithDtmf: mg,
        shimGetStats: ng,
        shimSenderReceiverGetStats: og,
        shimAddTrackRemoveTrackWithNative: pg,
        shimAddTrackRemoveTrack: qg,
        shimPeerConnection: me,
        fixNegotiationNeeded: rg,
        shimGetUserMedia: hg,
        shimGetDisplayMedia: function (d, f) {
            d.navigator.mediaDevices && "getDisplayMedia" in d.navigator.mediaDevices || d.navigator.mediaDevices && ("function" == typeof f ? d.navigator.mediaDevices.getDisplayMedia = function (a) {
                return f(a).then(b => {
                    let c = a.video && a.video.width, e = a.video && a.video.height;
                    return a.video = {
                        mandatory: {
                            chromeMediaSource: "desktop", chromeMediaSourceId: b, maxFrameRate: a.video &&
                                a.video.frameRate || 3
                        }
                    }, c && (a.video.mandatory.maxWidth = c), e && (a.video.mandatory.maxHeight = e), d.navigator.mediaDevices.getUserMedia(a)
                })
            } : console.error("shimGetDisplayMedia: getSourceId argument is not a function"))
        }
    }), Fj = "".startsWith, vn = Math.min, wn = lf("startsWith");
    O({target: "String", proto: !0, forced: !wn}, {
        startsWith: function (d) {
            var f = String(Zb(this));
            kf(d);
            var a = pb(vn(1 < arguments.length ? arguments[1] : void 0, f.length)), b = String(d);
            return Fj ? Fj.call(f, b, a) : f.slice(a, a + b.length) === b
        }
    });
    var xn = za("String").startsWith,
        Gj = String.prototype, td = function (d) {
            var f = d.startsWith;
            return "string" == typeof d || d === Gj || d instanceof String && f === Gj.startsWith ? xn : f
        };
    O({
        target: "String", proto: !0, forced: qa(function () {
            return "trim" !== "\t\n\v\f\r \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\u2028\u2029\ufeff".trim.name
        })
    }, {
        trim: function () {
            return Ki(this)
        }
    });
    var yn = za("String").trim, Hj = String.prototype, Vb = function (d) {
        var f = d.trim;
        return "string" == typeof d || d === Hj || d instanceof String &&
        f === Hj.trim ? yn : f
    }, H = wb(function (d) {
        var f = {
            generateIdentifier: function () {
                return Math.random().toString(36).substr(2, 10)
            }
        };
        f.localCName = f.generateIdentifier();
        f.splitLines = function (a) {
            var b;
            return C(b = Vb(a).call(a).split("\n")).call(b, function (a) {
                return Vb(a).call(a)
            })
        };
        f.splitSections = function (a) {
            a = a.split("\nm=");
            return C(a).call(a, function (a, c) {
                var b;
                return Vb(b = 0 < c ? "m=" + a : a).call(b) + "\r\n"
            })
        };
        f.getDescription = function (a) {
            return (a = f.splitSections(a)) && a[0]
        };
        f.getMediaSections = function (a) {
            a = f.splitSections(a);
            return a.shift(), a
        };
        f.matchPrefix = function (a, b) {
            var c;
            return N(c = f.splitLines(a)).call(c, function (a) {
                return 0 === G(a).call(a, b)
            })
        };
        f.parseCandidate = function (a) {
            var b;
            a = {
                foundation: (b = 0 === G(a).call(a, "a=candidate:") ? a.substring(12).split(" ") : a.substring(10).split(" "))[0],
                component: oa(b[1], 10),
                protocol: b[2].toLowerCase(),
                priority: oa(b[3], 10),
                ip: b[4],
                address: b[4],
                port: oa(b[5], 10),
                type: b[7]
            };
            for (var c = 8; c < b.length; c += 2) switch (b[c]) {
                case "raddr":
                    a.relatedAddress = b[c + 1];
                    break;
                case "rport":
                    a.relatedPort =
                        oa(b[c + 1], 10);
                    break;
                case "tcptype":
                    a.tcpType = b[c + 1];
                    break;
                case "ufrag":
                    a.ufrag = b[c + 1];
                    a.usernameFragment = b[c + 1];
                    break;
                default:
                    a[b[c]] = b[c + 1]
            }
            return a
        };
        f.writeCandidate = function (a) {
            var b = [];
            b.push(a.foundation);
            b.push(a.component);
            b.push(a.protocol.toUpperCase());
            b.push(a.priority);
            b.push(a.address || a.ip);
            b.push(a.port);
            var c = a.type;
            return b.push("typ"), b.push(c), "host" !== c && a.relatedAddress && a.relatedPort && (b.push("raddr"), b.push(a.relatedAddress), b.push("rport"), b.push(a.relatedPort)), a.tcpType &&
            "tcp" === a.protocol.toLowerCase() && (b.push("tcptype"), b.push(a.tcpType)), (a.usernameFragment || a.ufrag) && (b.push("ufrag"), b.push(a.usernameFragment || a.ufrag)), "candidate:" + b.join(" ")
        };
        f.parseIceOptions = function (a) {
            return a.substr(14).split(" ")
        };
        f.parseRtpMap = function (a) {
            a = a.substr(9).split(" ");
            var b = {payloadType: oa(a.shift(), 10)};
            return a = a[0].split("/"), b.name = a[0], b.clockRate = oa(a[1], 10), b.channels = 3 === a.length ? oa(a[2], 10) : 1, b.numChannels = b.channels, b
        };
        f.writeRtpMap = function (a) {
            var b = a.payloadType;
            void 0 !== a.preferredPayloadType && (b = a.preferredPayloadType);
            var c = a.channels || a.numChannels || 1;
            return "a=rtpmap:" + b + " " + a.name + "/" + a.clockRate + (1 !== c ? "/" + c : "") + "\r\n"
        };
        f.parseExtmap = function (a) {
            var b;
            a = a.substr(9).split(" ");
            return {
                id: oa(a[0], 10),
                direction: 0 < G(b = a[0]).call(b, "/") ? a[0].split("/")[1] : "sendrecv",
                uri: a[1]
            }
        };
        f.writeExtmap = function (a) {
            return "a=extmap:" + (a.id || a.preferredId) + (a.direction && "sendrecv" !== a.direction ? "/" + a.direction : "") + " " + a.uri + "\r\n"
        };
        f.parseFmtp = function (a) {
            for (var b = {},
                     c = a.substr(G(a).call(a, " ") + 1).split(";"), d = 0; d < c.length; d++) {
                var g, h;
                a = Vb(g = c[d]).call(g).split("=");
                b[Vb(h = a[0]).call(h)] = a[1]
            }
            return b
        };
        f.writeFmtp = function (a) {
            var b = "", c = a.payloadType;
            if (void 0 !== a.preferredPayloadType && (c = a.preferredPayloadType), a.parameters && aa(a.parameters).length) {
                var d, g = [];
                q(d = aa(a.parameters)).call(d, function (b) {
                    a.parameters[b] ? g.push(b + "=" + a.parameters[b]) : g.push(b)
                });
                b += "a=fmtp:" + c + " " + g.join(";") + "\r\n"
            }
            return b
        };
        f.parseRtcpFb = function (a) {
            a = a.substr(G(a).call(a, " ") +
                1).split(" ");
            return {type: a.shift(), parameter: a.join(" ")}
        };
        f.writeRtcpFb = function (a) {
            var b, c = "", d = a.payloadType;
            (void 0 !== a.preferredPayloadType && (d = a.preferredPayloadType), a.rtcpFeedback && a.rtcpFeedback.length) && q(b = a.rtcpFeedback).call(b, function (a) {
                c += "a=rtcp-fb:" + d + " " + a.type + (a.parameter && a.parameter.length ? " " + a.parameter : "") + "\r\n"
            });
            return c
        };
        f.parseSsrcMedia = function (a) {
            var b = G(a).call(a, " "), c = {ssrc: oa(a.substr(7, b - 7), 10)}, d = G(a).call(a, ":", b);
            return -1 < d ? (c.attribute = a.substr(b + 1, d - b - 1),
                c.value = a.substr(d + 1)) : c.attribute = a.substr(b + 1), c
        };
        f.parseSsrcGroup = function (a) {
            a = a.substr(13).split(" ");
            return {
                semantics: a.shift(), ssrcs: C(a).call(a, function (a) {
                    return oa(a, 10)
                })
            }
        };
        f.getMid = function (a) {
            if (a = f.matchPrefix(a, "a=mid:")[0]) return a.substr(6)
        };
        f.parseFingerprint = function (a) {
            a = a.substr(14).split(" ");
            return {algorithm: a[0].toLowerCase(), value: a[1]}
        };
        f.getDtlsParameters = function (a, b) {
            a = f.matchPrefix(a + b, "a=fingerprint:");
            return {role: "auto", fingerprints: C(a).call(a, f.parseFingerprint)}
        };
        f.writeDtlsParameters = function (a, b) {
            var c, d = "a=setup:" + b + "\r\n";
            return q(c = a.fingerprints).call(c, function (a) {
                d += "a=fingerprint:" + a.algorithm + " " + a.value + "\r\n"
            }), d
        };
        f.getIceParameters = function (a, b) {
            a = f.splitLines(a);
            return a = l(a).call(a, f.splitLines(b)), {
                usernameFragment: N(a).call(a, function (a) {
                    return 0 === G(a).call(a, "a=ice-ufrag:")
                })[0].substr(12), password: N(a).call(a, function (a) {
                    return 0 === G(a).call(a, "a=ice-pwd:")
                })[0].substr(10)
            }
        };
        f.writeIceParameters = function (a) {
            return "a=ice-ufrag:" + a.usernameFragment +
                "\r\na=ice-pwd:" + a.password + "\r\n"
        };
        f.parseRtpParameters = function (a) {
            for (var b, c = {
                codecs: [],
                headerExtensions: [],
                fecMechanisms: [],
                rtcp: []
            }, d = f.splitLines(a)[0].split(" "), g = 3; g < d.length; g++) {
                var h = d[g], m = f.matchPrefix(a, "a=rtpmap:" + h + " ")[0];
                if (m) {
                    var r;
                    m = f.parseRtpMap(m);
                    var w = f.matchPrefix(a, "a=fmtp:" + h + " ");
                    switch (m.parameters = w.length ? f.parseFmtp(w[0]) : {}, m.rtcpFeedback = C(r = f.matchPrefix(a, "a=rtcp-fb:" + h + " ")).call(r, f.parseRtcpFb), c.codecs.push(m), m.name.toUpperCase()) {
                        case "RED":
                        case "ULPFEC":
                            c.fecMechanisms.push(m.name.toUpperCase())
                    }
                }
            }
            return q(b =
                f.matchPrefix(a, "a=extmap:")).call(b, function (a) {
                c.headerExtensions.push(f.parseExtmap(a))
            }), c
        };
        f.writeRtpDescription = function (a, b) {
            var c, d, g, h = "";
            h += "m=" + a + " ";
            h += 0 < b.codecs.length ? "9" : "0";
            h += " UDP/TLS/RTP/SAVPF ";
            h += C(c = b.codecs).call(c, function (a) {
                return void 0 !== a.preferredPayloadType ? a.preferredPayloadType : a.payloadType
            }).join(" ") + "\r\n";
            h += "c=IN IP4 0.0.0.0\r\n";
            h += "a=rtcp:9 IN IP4 0.0.0.0\r\n";
            q(d = b.codecs).call(d, function (a) {
                h += f.writeRtpMap(a);
                h += f.writeFmtp(a);
                h += f.writeRtcpFb(a)
            });
            var m,
                r = 0;
            (q(g = b.codecs).call(g, function (a) {
                a.maxptime > r && (r = a.maxptime)
            }), 0 < r && (h += "a=maxptime:" + r + "\r\n"), h += "a=rtcp-mux\r\n", b.headerExtensions) && q(m = b.headerExtensions).call(m, function (a) {
                h += f.writeExtmap(a)
            });
            return h
        };
        f.parseRtpEncodingParameters = function (a) {
            var b, c, d, g, h, m, r, k = [], y = f.parseRtpParameters(a),
                l = -1 !== G(b = y.fecMechanisms).call(b, "RED"), n = -1 !== G(c = y.fecMechanisms).call(c, "ULPFEC");
            b = N(d = C(g = f.matchPrefix(a, "a=ssrc:")).call(g, function (a) {
                return f.parseSsrcMedia(a)
            })).call(d, function (a) {
                return "cname" ===
                    a.attribute
            });
            var p = 0 < b.length && b[0].ssrc;
            d = C(h = f.matchPrefix(a, "a=ssrc-group:FID")).call(h, function (a) {
                a = a.substr(17).split(" ");
                return C(a).call(a, function (a) {
                    return oa(a, 10)
                })
            });
            0 < d.length && 1 < d[0].length && d[0][0] === p && (r = d[0][1]);
            q(m = y.codecs).call(m, function (a) {
                "RTX" === a.name.toUpperCase() && a.parameters.apt && (a = {
                    ssrc: p,
                    codecPayloadType: oa(a.parameters.apt, 10)
                }, p && r && (a.rtx = {ssrc: r}), k.push(a), l && ((a = JSON.parse(A(a))).fec = {
                    ssrc: p,
                    mechanism: n ? "red+ulpfec" : "red"
                }, k.push(a)))
            });
            0 === k.length && p && k.push({ssrc: p});
            var z, ja, t = f.matchPrefix(a, "b=");
            t.length && (t = 0 === G(z = t[0]).call(z, "b=TIAS:") ? oa(t[0].substr(7), 10) : 0 === G(ja = t[0]).call(ja, "b=AS:") ? 950 * oa(t[0].substr(5), 10) - 16E3 : void 0, q(k).call(k, function (a) {
                a.maxBitrate = t
            }));
            return k
        };
        f.parseRtcpParameters = function (a) {
            var b, c, d = {}, g = N(b = C(c = f.matchPrefix(a, "a=ssrc:")).call(c, function (a) {
                return f.parseSsrcMedia(a)
            })).call(b, function (a) {
                return "cname" === a.attribute
            })[0];
            g && (d.cname = g.value, d.ssrc = g.ssrc);
            b = f.matchPrefix(a, "a=rtcp-rsize");
            d.reducedSize = 0 < b.length;
            d.compound = 0 === b.length;
            a = f.matchPrefix(a, "a=rtcp-mux");
            return d.mux = 0 < a.length, d
        };
        f.parseMsid = function (a) {
            var b, c, d, g = f.matchPrefix(a, "a=msid:");
            if (1 === g.length) return {stream: (d = g[0].substr(7).split(" "))[0], track: d[1]};
            a = N(b = C(c = f.matchPrefix(a, "a=ssrc:")).call(c, function (a) {
                return f.parseSsrcMedia(a)
            })).call(b, function (a) {
                return "msid" === a.attribute
            });
            return 0 < a.length ? {stream: (d = a[0].value.split(" "))[0], track: d[1]} : void 0
        };
        f.generateSessionId = function () {
            return Math.random().toString().substr(2,
                21)
        };
        f.writeSessionBoilerplate = function (a, b, c) {
            b = void 0 !== b ? b : 2;
            return "v=0\r\no=" + (c || "thisisadapterortc") + " " + (a || f.generateSessionId()) + " " + b + " IN IP4 127.0.0.1\r\ns=-\r\nt=0 0\r\n"
        };
        f.writeMediaSection = function (a, b, c, d) {
            b = f.writeRtpDescription(a.kind, b);
            if (b += f.writeIceParameters(a.iceGatherer.getLocalParameters()), b += f.writeDtlsParameters(a.dtlsTransport.getLocalParameters(), "offer" === c ? "actpass" : "active"), b += "a=mid:" + a.mid + "\r\n", a.direction ? b += "a=" + a.direction + "\r\n" : a.rtpSender && a.rtpReceiver ?
                b += "a=sendrecv\r\n" : a.rtpSender ? b += "a=sendonly\r\n" : a.rtpReceiver ? b += "a=recvonly\r\n" : b += "a=inactive\r\n", a.rtpSender) c = "msid:" + d.id + " " + a.rtpSender.track.id + "\r\n", b = b + ("a=" + c) + ("a=ssrc:" + a.sendEncodingParameters[0].ssrc + " " + c), a.sendEncodingParameters[0].rtx && (b += "a=ssrc:" + a.sendEncodingParameters[0].rtx.ssrc + " " + c, b += "a=ssrc-group:FID " + a.sendEncodingParameters[0].ssrc + " " + a.sendEncodingParameters[0].rtx.ssrc + "\r\n");
            return b += "a=ssrc:" + a.sendEncodingParameters[0].ssrc + " cname:" + f.localCName + "\r\n",
            a.rtpSender && a.sendEncodingParameters[0].rtx && (b += "a=ssrc:" + a.sendEncodingParameters[0].rtx.ssrc + " cname:" + f.localCName + "\r\n"), b
        };
        f.getDirection = function (a, b) {
            a = f.splitLines(a);
            for (var c = 0; c < a.length; c++) switch (a[c]) {
                case "a=sendrecv":
                case "a=sendonly":
                case "a=recvonly":
                case "a=inactive":
                    return a[c].substr(2)
            }
            return b ? f.getDirection(b) : "sendrecv"
        };
        f.getKind = function (a) {
            return f.splitLines(a)[0].split(" ")[0].substr(2)
        };
        f.isRejected = function (a) {
            return "0" === a.split(" ", 2)[1]
        };
        f.parseMLine = function (a) {
            a =
                f.splitLines(a)[0].substr(2).split(" ");
            return {kind: a[0], port: oa(a[1], 10), protocol: a[2], fmt: Aa(a).call(a, 3).join(" ")}
        };
        f.parseOLine = function (a) {
            a = f.matchPrefix(a, "o=")[0].substr(2).split(" ");
            return {
                username: a[0],
                sessionId: a[1],
                sessionVersion: oa(a[2], 10),
                netType: a[3],
                addressType: a[4],
                address: a[5]
            }
        };
        f.isValidSDP = function (a) {
            if ("string" != typeof a || 0 === a.length) return !1;
            a = f.splitLines(a);
            for (var b = 0; b < a.length; b++) if (2 > a[b].length || "=" !== a[b].charAt(1)) return !1;
            return !0
        };
        d.exports = f
    }), Zk = function (d,
                       f) {
        function a(a, b) {
            b.addTrack(a);
            b.dispatchEvent(new d.MediaStreamTrackEvent("addtrack", {track: a}))
        }

        function b(a, b, c, e) {
            var g = new Event("track");
            g.track = b;
            g.receiver = c;
            g.transceiver = {receiver: c};
            g.streams = e;
            d.setTimeout(function () {
                a._dispatchEvent("track", g)
            })
        }

        var c = function (a) {
            var b = this, c = document.createDocumentFragment();
            if (["addEventListener", "removeEventListener", "dispatchEvent"].forEach(function (a) {
                b[a] = c[a].bind(c)
            }), this.canTrickleIceCandidates = null, this.needNegotiation = !1, this.localStreams =
                [], this.remoteStreams = [], this._localDescription = null, this._remoteDescription = null, this.signalingState = "stable", this.iceConnectionState = "new", this.connectionState = "new", this.iceGatheringState = "new", a = JSON.parse(JSON.stringify(a || {})), this.usingBundle = "max-bundle" === a.bundlePolicy, "negotiate" === a.rtcpMuxPolicy) throw Ja("NotSupportedError", "rtcpMuxPolicy 'negotiate' is not supported");
            switch (a.rtcpMuxPolicy || (a.rtcpMuxPolicy = "require"), a.iceTransportPolicy) {
                case "all":
                case "relay":
                    break;
                default:
                    a.iceTransportPolicy =
                        "all"
            }
            switch (a.bundlePolicy) {
                case "balanced":
                case "max-compat":
                case "max-bundle":
                    break;
                default:
                    a.bundlePolicy = "balanced"
            }
            if (a.iceServers = function (a, b) {
                var c = !1;
                return (a = JSON.parse(JSON.stringify(a))).filter(function (a) {
                    if (a && (a.urls || a.url)) {
                        var d = a.urls || a.url;
                        a.url && !a.urls && console.warn("RTCIceServer.url is deprecated! Use urls instead.");
                        var e = "string" == typeof d;
                        return e && (d = [d]), d = d.filter(function (a) {
                            return 0 !== a.indexOf("turn:") || -1 === a.indexOf("transport=udp") || -1 !== a.indexOf("turn:[") || c ?
                                0 === a.indexOf("stun:") && 14393 <= b && -1 === a.indexOf("?transport=udp") : (c = !0, !0)
                        }), delete a.url, a.urls = e ? d[0] : d, !!d.length
                    }
                })
            }(a.iceServers || [], f), this._iceGatherers = [], a.iceCandidatePoolSize) for (var e = a.iceCandidatePoolSize; 0 < e; e--) this._iceGatherers.push(new d.RTCIceGatherer({
                iceServers: a.iceServers,
                gatherPolicy: a.iceTransportPolicy
            })); else a.iceCandidatePoolSize = 0;
            this._config = a;
            this.transceivers = [];
            this._sdpSessionId = H.generateSessionId();
            this._sdpSessionVersion = 0;
            this._dtlsRole = void 0;
            this._isClosed =
                !1
        };
        Object.defineProperty(c.prototype, "localDescription", {
            configurable: !0, get: function () {
                return this._localDescription
            }
        });
        Object.defineProperty(c.prototype, "remoteDescription", {
            configurable: !0, get: function () {
                return this._remoteDescription
            }
        });
        c.prototype.onicecandidate = null;
        c.prototype.onaddstream = null;
        c.prototype.ontrack = null;
        c.prototype.onremovestream = null;
        c.prototype.onsignalingstatechange = null;
        c.prototype.oniceconnectionstatechange = null;
        c.prototype.onconnectionstatechange = null;
        c.prototype.onicegatheringstatechange =
            null;
        c.prototype.onnegotiationneeded = null;
        c.prototype.ondatachannel = null;
        c.prototype._dispatchEvent = function (a, b) {
            this._isClosed || (this.dispatchEvent(b), "function" == typeof this["on" + a] && this["on" + a](b))
        };
        c.prototype._emitGatheringStateChange = function () {
            var a = new Event("icegatheringstatechange");
            this._dispatchEvent("icegatheringstatechange", a)
        };
        c.prototype.getConfiguration = function () {
            return this._config
        };
        c.prototype.getLocalStreams = function () {
            return this.localStreams
        };
        c.prototype.getRemoteStreams =
            function () {
                return this.remoteStreams
            };
        c.prototype._createTransceiver = function (a, b) {
            var c = 0 < this.transceivers.length;
            a = {
                track: null,
                iceGatherer: null,
                iceTransport: null,
                dtlsTransport: null,
                localCapabilities: null,
                remoteCapabilities: null,
                rtpSender: null,
                rtpReceiver: null,
                kind: a,
                mid: null,
                sendEncodingParameters: null,
                recvEncodingParameters: null,
                stream: null,
                associatedRemoteMediaStreams: [],
                wantReceive: !0
            };
            this.usingBundle && c ? (a.iceTransport = this.transceivers[0].iceTransport, a.dtlsTransport = this.transceivers[0].dtlsTransport) :
                (c = this._createIceAndDtlsTransports(), a.iceTransport = c.iceTransport, a.dtlsTransport = c.dtlsTransport);
            return b || this.transceivers.push(a), a
        };
        c.prototype.addTrack = function (a, b) {
            if (this._isClosed) throw Ja("InvalidStateError", "Attempted to call addTrack on a closed peerconnection.");
            var c;
            if (this.transceivers.find(function (b) {
                return b.track === a
            })) throw Ja("InvalidAccessError", "Track already exists.");
            for (var e = 0; e < this.transceivers.length; e++) this.transceivers[e].track || this.transceivers[e].kind !== a.kind ||
            (c = this.transceivers[e]);
            return c || (c = this._createTransceiver(a.kind)), this._maybeFireNegotiationNeeded(), -1 === this.localStreams.indexOf(b) && this.localStreams.push(b), c.track = a, c.stream = b, c.rtpSender = new d.RTCRtpSender(a, c.dtlsTransport), c.rtpSender
        };
        c.prototype.addStream = function (a) {
            var b = this;
            if (15025 <= f) a.getTracks().forEach(function (c) {
                b.addTrack(c, a)
            }); else {
                var c = a.clone();
                a.getTracks().forEach(function (a, b) {
                    var d = c.getTracks()[b];
                    a.addEventListener("enabled", function (a) {
                        d.enabled = a.enabled
                    })
                });
                c.getTracks().forEach(function (a) {
                    b.addTrack(a, c)
                })
            }
        };
        c.prototype.removeTrack = function (a) {
            if (this._isClosed) throw Ja("InvalidStateError", "Attempted to call removeTrack on a closed peerconnection.");
            if (!(a instanceof d.RTCRtpSender)) throw new TypeError("Argument 1 of RTCPeerConnection.removeTrack does not implement interface RTCRtpSender.");
            var b = this.transceivers.find(function (b) {
                return b.rtpSender === a
            });
            if (!b) throw Ja("InvalidAccessError", "Sender was not created by this connection.");
            var c = b.stream;
            b.rtpSender.stop();
            b.rtpSender = null;
            b.track = null;
            b.stream = null;
            -1 === this.transceivers.map(function (a) {
                return a.stream
            }).indexOf(c) && -1 < this.localStreams.indexOf(c) && this.localStreams.splice(this.localStreams.indexOf(c), 1);
            this._maybeFireNegotiationNeeded()
        };
        c.prototype.removeStream = function (a) {
            var b = this;
            a.getTracks().forEach(function (a) {
                var c = b.getSenders().find(function (b) {
                    return b.track === a
                });
                c && b.removeTrack(c)
            })
        };
        c.prototype.getSenders = function () {
            return this.transceivers.filter(function (a) {
                return !!a.rtpSender
            }).map(function (a) {
                return a.rtpSender
            })
        };
        c.prototype.getReceivers = function () {
            return this.transceivers.filter(function (a) {
                return !!a.rtpReceiver
            }).map(function (a) {
                return a.rtpReceiver
            })
        };
        c.prototype._createIceGatherer = function (a, b) {
            var c = this;
            if (b && 0 < a) return this.transceivers[0].iceGatherer;
            if (this._iceGatherers.length) return this._iceGatherers.shift();
            var e = new d.RTCIceGatherer({
                iceServers: this._config.iceServers,
                gatherPolicy: this._config.iceTransportPolicy
            });
            return Object.defineProperty(e, "state", {
                value: "new",
                writable: !0
            }), this.transceivers[a].bufferedCandidateEvents =
                [], this.transceivers[a].bufferCandidates = function (b) {
                var d = !b.candidate || 0 === Object.keys(b.candidate).length;
                e.state = d ? "completed" : "gathering";
                null !== c.transceivers[a].bufferedCandidateEvents && c.transceivers[a].bufferedCandidateEvents.push(b)
            }, e.addEventListener("localcandidate", this.transceivers[a].bufferCandidates), e
        };
        c.prototype._gather = function (a, b) {
            var c = this, e = this.transceivers[b].iceGatherer;
            if (!e.onlocalcandidate) {
                var g = this.transceivers[b].bufferedCandidateEvents;
                this.transceivers[b].bufferedCandidateEvents =
                    null;
                e.removeEventListener("localcandidate", this.transceivers[b].bufferCandidates);
                e.onlocalcandidate = function (d) {
                    if (!(c.usingBundle && 0 < b)) {
                        var g = new Event("icecandidate");
                        g.candidate = {sdpMid: a, sdpMLineIndex: b};
                        var f = d.candidate;
                        (d = !f || 0 === Object.keys(f).length) ? "new" !== e.state && "gathering" !== e.state || (e.state = "completed") : ("new" === e.state && (e.state = "gathering"), f.component = 1, f.ufrag = e.getLocalParameters().usernameFragment, f = H.writeCandidate(f), g.candidate = Object.assign(g.candidate, H.parseCandidate(f)),
                            g.candidate.candidate = f, g.candidate.toJSON = function () {
                            return {
                                candidate: g.candidate.candidate,
                                sdpMid: g.candidate.sdpMid,
                                sdpMLineIndex: g.candidate.sdpMLineIndex,
                                usernameFragment: g.candidate.usernameFragment
                            }
                        });
                        f = H.getMediaSections(c._localDescription.sdp);
                        f[g.candidate.sdpMLineIndex] += d ? "a=end-of-candidates\r\n" : "a=" + g.candidate.candidate + "\r\n";
                        c._localDescription.sdp = H.getDescription(c._localDescription.sdp) + f.join("");
                        f = c.transceivers.every(function (a) {
                            return a.iceGatherer && "completed" === a.iceGatherer.state
                        });
                        "gathering" !== c.iceGatheringState && (c.iceGatheringState = "gathering", c._emitGatheringStateChange());
                        d || c._dispatchEvent("icecandidate", g);
                        f && (c._dispatchEvent("icecandidate", new Event("icecandidate")), c.iceGatheringState = "complete", c._emitGatheringStateChange())
                    }
                };
                d.setTimeout(function () {
                    g.forEach(function (a) {
                        e.onlocalcandidate(a)
                    })
                }, 0)
            }
        };
        c.prototype._createIceAndDtlsTransports = function () {
            var a = this, b = new d.RTCIceTransport(null);
            b.onicestatechange = function () {
                a._updateIceConnectionState();
                a._updateConnectionState()
            };
            var c = new d.RTCDtlsTransport(b);
            return c.ondtlsstatechange = function () {
                a._updateConnectionState()
            }, c.onerror = function () {
                Object.defineProperty(c, "state", {value: "failed", writable: !0});
                a._updateConnectionState()
            }, {iceTransport: b, dtlsTransport: c}
        };
        c.prototype._disposeIceAndDtlsTransports = function (a) {
            var b = this.transceivers[a].iceGatherer;
            b && (delete b.onlocalcandidate, delete this.transceivers[a].iceGatherer);
            (b = this.transceivers[a].iceTransport) && (delete b.onicestatechange, delete this.transceivers[a].iceTransport);
            (b = this.transceivers[a].dtlsTransport) && (delete b.ondtlsstatechange, delete b.onerror, delete this.transceivers[a].dtlsTransport)
        };
        c.prototype._transceive = function (a, b, c) {
            var d = sd(a.localCapabilities, a.remoteCapabilities);
            b && a.rtpSender && (d.encodings = a.sendEncodingParameters, d.rtcp = {
                cname: H.localCName,
                compound: a.rtcpParameters.compound
            }, a.recvEncodingParameters.length && (d.rtcp.ssrc = a.recvEncodingParameters[0].ssrc), a.rtpSender.send(d));
            c && a.rtpReceiver && 0 < d.codecs.length && ("video" === a.kind && a.recvEncodingParameters &&
            15019 > f && a.recvEncodingParameters.forEach(function (a) {
                delete a.rtx
            }), a.recvEncodingParameters.length ? d.encodings = a.recvEncodingParameters : d.encodings = [{}], d.rtcp = {compound: a.rtcpParameters.compound}, a.rtcpParameters.cname && (d.rtcp.cname = a.rtcpParameters.cname), a.sendEncodingParameters.length && (d.rtcp.ssrc = a.sendEncodingParameters[0].ssrc), a.rtpReceiver.receive(d))
        };
        c.prototype.setLocalDescription = function (a) {
            var b = this;
            if (-1 === ["offer", "answer"].indexOf(a.type)) return Promise.reject(Ja("TypeError",
                'Unsupported type "' + a.type + '"'));
            if (!tg("setLocalDescription", a.type, b.signalingState) || b._isClosed) return Promise.reject(Ja("InvalidStateError", "Can not set local " + a.type + " in state " + b.signalingState));
            if ("offer" === a.type) {
                var c = H.splitSections(a.sdp);
                var d = c.shift();
                c.forEach(function (a, c) {
                    a = H.parseRtpParameters(a);
                    b.transceivers[c].localCapabilities = a
                });
                b.transceivers.forEach(function (a, c) {
                    b._gather(a.mid, c)
                })
            } else if ("answer" === a.type) {
                c = H.splitSections(b._remoteDescription.sdp);
                d = c.shift();
                var e = 0 < H.matchPrefix(d, "a=ice-lite").length;
                c.forEach(function (a, c) {
                    var g = b.transceivers[c], f = g.iceGatherer, h = g.iceTransport, m = g.dtlsTransport,
                        r = g.localCapabilities, k = g.remoteCapabilities;
                    if (!(H.isRejected(a) && 0 === H.matchPrefix(a, "a=bundle-only").length || g.rejected)) {
                        var w = H.getIceParameters(a, d);
                        a = H.getDtlsParameters(a, d);
                        e && (a.role = "server");
                        b.usingBundle && 0 !== c || (b._gather(g.mid, c), "new" === h.state && h.start(f, w, e ? "controlling" : "controlled"), "new" === m.state && m.start(a));
                        c = sd(r, k);
                        b._transceive(g,
                            0 < c.codecs.length, !1)
                    }
                })
            }
            return b._localDescription = {
                type: a.type,
                sdp: a.sdp
            }, "offer" === a.type ? b._updateSignalingState("have-local-offer") : b._updateSignalingState("stable"), Promise.resolve()
        };
        c.prototype.setRemoteDescription = function (c) {
            var e = this;
            if (-1 === ["offer", "answer"].indexOf(c.type)) return Promise.reject(Ja("TypeError", 'Unsupported type "' + c.type + '"'));
            if (!tg("setRemoteDescription", c.type, e.signalingState) || e._isClosed) return Promise.reject(Ja("InvalidStateError", "Can not set remote " + c.type +
                " in state " + e.signalingState));
            var g = {};
            e.remoteStreams.forEach(function (a) {
                g[a.id] = a
            });
            var r = [], k = H.splitSections(c.sdp), y = k.shift(), l = 0 < H.matchPrefix(y, "a=ice-lite").length,
                n = 0 < H.matchPrefix(y, "a=group:BUNDLE ").length;
            e.usingBundle = n;
            var p = H.matchPrefix(y, "a=ice-options:")[0];
            return e.canTrickleIceCandidates = !!p && 0 <= p.substr(14).split(" ").indexOf("trickle"), k.forEach(function (b, h) {
                var m = H.splitLines(b), k = H.getKind(b),
                    w = H.isRejected(b) && 0 === H.matchPrefix(b, "a=bundle-only").length,
                    p = m[0].substr(2).split(" ")[2];
                m = H.getDirection(b, y);
                var B = H.parseMsid(b), sa = H.getMid(b) || H.generateIdentifier();
                if (w || "application" === k && ("DTLS/SCTP" === p || "UDP/DTLS/SCTP" === p)) e.transceivers[h] = {
                    mid: sa,
                    kind: k,
                    protocol: p,
                    rejected: !0
                }; else {
                    var z, q;
                    !w && e.transceivers[h] && e.transceivers[h].rejected && (e.transceivers[h] = e._createTransceiver(k, !0));
                    var t, Qb, u = H.parseRtpParameters(b);
                    w || (t = H.getIceParameters(b, y), (Qb = H.getDtlsParameters(b, y)).role = "client");
                    p = H.parseRtpEncodingParameters(b);
                    var ja = H.parseRtcpParameters(b), x = 0 < H.matchPrefix(b,
                        "a=end-of-candidates", y).length;
                    b = H.matchPrefix(b, "a=candidate:").map(function (a) {
                        return H.parseCandidate(a)
                    }).filter(function (a) {
                        return 1 === a.component
                    });
                    if (("offer" === c.type || "answer" === c.type) && !w && n && 0 < h && e.transceivers[h] && (e._disposeIceAndDtlsTransports(h), e.transceivers[h].iceGatherer = e.transceivers[0].iceGatherer, e.transceivers[h].iceTransport = e.transceivers[0].iceTransport, e.transceivers[h].dtlsTransport = e.transceivers[0].dtlsTransport, e.transceivers[h].rtpSender && e.transceivers[h].rtpSender.setTransport(e.transceivers[0].dtlsTransport),
                    e.transceivers[h].rtpReceiver && e.transceivers[h].rtpReceiver.setTransport(e.transceivers[0].dtlsTransport)), "offer" !== c.type || w) {
                        if ("answer" === c.type && !w) {
                            k = (z = e.transceivers[h]).iceGatherer;
                            var v = z.iceTransport;
                            var A = z.dtlsTransport;
                            var C = z.rtpReceiver;
                            w = z.sendEncodingParameters;
                            sa = z.localCapabilities;
                            e.transceivers[h].recvEncodingParameters = p;
                            e.transceivers[h].remoteCapabilities = u;
                            e.transceivers[h].rtcpParameters = ja;
                            b.length && "new" === v.state && (!l && !x || n && 0 !== h ? b.forEach(function (a) {
                                ne(z.iceTransport,
                                    a)
                            }) : v.setRemoteCandidates(b));
                            n && 0 !== h || ("new" === v.state && v.start(k, t, "controlling"), "new" === A.state && A.start(Qb));
                            !sd(z.localCapabilities, z.remoteCapabilities).codecs.filter(function (a) {
                                return "rtx" === a.name.toLowerCase()
                            }).length && z.sendEncodingParameters[0].rtx && delete z.sendEncodingParameters[0].rtx;
                            e._transceive(z, "sendrecv" === m || "recvonly" === m, "sendrecv" === m || "sendonly" === m);
                            !C || "sendrecv" !== m && "sendonly" !== m ? delete z.rtpReceiver : (q = C.track, B ? (g[B.stream] || (g[B.stream] = new d.MediaStream), a(q,
                                g[B.stream]), r.push([q, C, g[B.stream]])) : (g.default || (g.default = new d.MediaStream), a(q, g.default), r.push([q, C, g.default])))
                        }
                    } else {
                        (z = e.transceivers[h] || e._createTransceiver(k)).mid = sa;
                        z.iceGatherer || (z.iceGatherer = e._createIceGatherer(h, n));
                        b.length && "new" === z.iceTransport.state && (!x || n && 0 !== h ? b.forEach(function (a) {
                            ne(z.iceTransport, a)
                        }) : z.iceTransport.setRemoteCandidates(b));
                        sa = d.RTCRtpReceiver.getCapabilities(k);
                        15019 > f && (sa.codecs = sa.codecs.filter(function (a) {
                            return "rtx" !== a.name
                        }));
                        w = z.sendEncodingParameters ||
                            [{ssrc: 1001 * (2 * h + 2)}];
                        t = !1;
                        if ("sendrecv" === m || "sendonly" === m) {
                            if (t = !z.rtpReceiver, C = z.rtpReceiver || new d.RTCRtpReceiver(z.dtlsTransport, k), t) q = C.track, B && "-" === B.stream || (B ? (g[B.stream] || (g[B.stream] = new d.MediaStream, Object.defineProperty(g[B.stream], "id", {
                                get: function () {
                                    return B.stream
                                }
                            })), Object.defineProperty(q, "id", {
                                get: function () {
                                    return B.track
                                }
                            }), v = g[B.stream]) : (g.default || (g.default = new d.MediaStream), v = g.default)), v && (a(q, v), z.associatedRemoteMediaStreams.push(v)), r.push([q, C, v])
                        } else z.rtpReceiver &&
                        z.rtpReceiver.track && (z.associatedRemoteMediaStreams.forEach(function (a) {
                            var b = a.getTracks().find(function (a) {
                                return a.id === z.rtpReceiver.track.id
                            });
                            b && function (a, b) {
                                b.removeTrack(a);
                                b.dispatchEvent(new d.MediaStreamTrackEvent("removetrack", {track: a}))
                            }(b, a)
                        }), z.associatedRemoteMediaStreams = []);
                        z.localCapabilities = sa;
                        z.remoteCapabilities = u;
                        z.rtpReceiver = C;
                        z.rtcpParameters = ja;
                        z.sendEncodingParameters = w;
                        z.recvEncodingParameters = p;
                        e._transceive(e.transceivers[h], !1, t)
                    }
                }
            }), void 0 === e._dtlsRole && (e._dtlsRole =
                "offer" === c.type ? "active" : "passive"), e._remoteDescription = {
                type: c.type,
                sdp: c.sdp
            }, "offer" === c.type ? e._updateSignalingState("have-remote-offer") : e._updateSignalingState("stable"), Object.keys(g).forEach(function (a) {
                var c = g[a];
                if (c.getTracks().length) {
                    if (-1 === e.remoteStreams.indexOf(c)) {
                        e.remoteStreams.push(c);
                        var f = new Event("addstream");
                        f.stream = c;
                        d.setTimeout(function () {
                            e._dispatchEvent("addstream", f)
                        })
                    }
                    r.forEach(function (a) {
                        var d = a[0], g = a[1];
                        c.id === a[2].id && b(e, d, g, [c])
                    })
                }
            }), r.forEach(function (a) {
                a[2] ||
                b(e, a[0], a[1], [])
            }), d.setTimeout(function () {
                e && e.transceivers && e.transceivers.forEach(function (a) {
                    a.iceTransport && "new" === a.iceTransport.state && 0 < a.iceTransport.getRemoteCandidates().length && (console.warn("Timeout for addRemoteCandidate. Consider sending an end-of-candidates notification"), a.iceTransport.addRemoteCandidate({}))
                })
            }, 4E3), Promise.resolve()
        };
        c.prototype.close = function () {
            this.transceivers.forEach(function (a) {
                a.iceTransport && a.iceTransport.stop();
                a.dtlsTransport && a.dtlsTransport.stop();
                a.rtpSender && a.rtpSender.stop();
                a.rtpReceiver && a.rtpReceiver.stop()
            });
            this._isClosed = !0;
            this._updateSignalingState("closed")
        };
        c.prototype._updateSignalingState = function (a) {
            this.signalingState = a;
            a = new Event("signalingstatechange");
            this._dispatchEvent("signalingstatechange", a)
        };
        c.prototype._maybeFireNegotiationNeeded = function () {
            var a = this;
            "stable" === this.signalingState && !0 !== this.needNegotiation && (this.needNegotiation = !0, d.setTimeout(function () {
                if (a.needNegotiation) {
                    a.needNegotiation = !1;
                    var b = new Event("negotiationneeded");
                    a._dispatchEvent("negotiationneeded", b)
                }
            }, 0))
        };
        c.prototype._updateIceConnectionState = function () {
            var a, b = {new: 0, closed: 0, checking: 0, connected: 0, completed: 0, disconnected: 0, failed: 0};
            if (this.transceivers.forEach(function (a) {
                a.iceTransport && !a.rejected && b[a.iceTransport.state]++
            }), a = "new", 0 < b.failed ? a = "failed" : 0 < b.checking ? a = "checking" : 0 < b.disconnected ? a = "disconnected" : 0 < b.new ? a = "new" : 0 < b.connected ? a = "connected" : 0 < b.completed && (a = "completed"), a !== this.iceConnectionState) this.iceConnectionState = a, a = new Event("iceconnectionstatechange"),
                this._dispatchEvent("iceconnectionstatechange", a)
        };
        c.prototype._updateConnectionState = function () {
            var a, b = {new: 0, closed: 0, connecting: 0, connected: 0, completed: 0, disconnected: 0, failed: 0};
            if (this.transceivers.forEach(function (a) {
                a.iceTransport && a.dtlsTransport && !a.rejected && (b[a.iceTransport.state]++, b[a.dtlsTransport.state]++)
            }), b.connected += b.completed, a = "new", 0 < b.failed ? a = "failed" : 0 < b.connecting ? a = "connecting" : 0 < b.disconnected ? a = "disconnected" : 0 < b.new ? a = "new" : 0 < b.connected && (a = "connected"), a !== this.connectionState) this.connectionState =
                a, a = new Event("connectionstatechange"), this._dispatchEvent("connectionstatechange", a)
        };
        c.prototype.createOffer = function (a) {
            var b = this;
            if (b._isClosed) return Promise.reject(Ja("InvalidStateError", "Can not call createOffer after close"));
            var c = b.transceivers.filter(function (a) {
                return "audio" === a.kind
            }).length, e = b.transceivers.filter(function (a) {
                return "video" === a.kind
            }).length;
            if (a) {
                if (a.mandatory || a.optional) throw new TypeError("Legacy mandatory/optional constraints not supported.");
                void 0 !== a.offerToReceiveAudio &&
                (c = !0 === a.offerToReceiveAudio ? 1 : !1 === a.offerToReceiveAudio ? 0 : a.offerToReceiveAudio);
                void 0 !== a.offerToReceiveVideo && (e = !0 === a.offerToReceiveVideo ? 1 : !1 === a.offerToReceiveVideo ? 0 : a.offerToReceiveVideo)
            }
            for (b.transceivers.forEach(function (a) {
                "audio" === a.kind ? 0 > --c && (a.wantReceive = !1) : "video" === a.kind && 0 > --e && (a.wantReceive = !1)
            }); 0 < c || 0 < e;) 0 < c && (b._createTransceiver("audio"), c--), 0 < e && (b._createTransceiver("video"), e--);
            var g = H.writeSessionBoilerplate(b._sdpSessionId, b._sdpSessionVersion++);
            b.transceivers.forEach(function (a,
                                             c) {
                var e = a.track, g = a.kind, h = a.mid || H.generateIdentifier();
                a.mid = h;
                a.iceGatherer || (a.iceGatherer = b._createIceGatherer(c, b.usingBundle));
                h = d.RTCRtpSender.getCapabilities(g);
                15019 > f && (h.codecs = h.codecs.filter(function (a) {
                    return "rtx" !== a.name
                }));
                h.codecs.forEach(function (b) {
                    "H264" === b.name && void 0 === b.parameters["level-asymmetry-allowed"] && (b.parameters["level-asymmetry-allowed"] = "1");
                    a.remoteCapabilities && a.remoteCapabilities.codecs && a.remoteCapabilities.codecs.forEach(function (a) {
                        b.name.toLowerCase() ===
                        a.name.toLowerCase() && b.clockRate === a.clockRate && (b.preferredPayloadType = a.payloadType)
                    })
                });
                h.headerExtensions.forEach(function (b) {
                    (a.remoteCapabilities && a.remoteCapabilities.headerExtensions || []).forEach(function (a) {
                        b.uri === a.uri && (b.id = a.id)
                    })
                });
                c = a.sendEncodingParameters || [{ssrc: 1001 * (2 * c + 1)}];
                e && 15019 <= f && "video" === g && !c[0].rtx && (c[0].rtx = {ssrc: c[0].ssrc + 1});
                a.wantReceive && (a.rtpReceiver = new d.RTCRtpReceiver(a.dtlsTransport, g));
                a.localCapabilities = h;
                a.sendEncodingParameters = c
            });
            "max-compat" !==
            b._config.bundlePolicy && (g += "a=group:BUNDLE " + b.transceivers.map(function (a) {
                return a.mid
            }).join(" ") + "\r\n");
            g += "a=ice-options:trickle\r\n";
            b.transceivers.forEach(function (a, c) {
                g += sg(a, a.localCapabilities, "offer", a.stream, b._dtlsRole);
                g += "a=rtcp-rsize\r\n";
                !a.iceGatherer || "new" === b.iceGatheringState || 0 !== c && b.usingBundle || (a.iceGatherer.getLocalCandidates().forEach(function (a) {
                    a.component = 1;
                    g += "a=" + H.writeCandidate(a) + "\r\n"
                }), "completed" === a.iceGatherer.state && (g += "a=end-of-candidates\r\n"))
            });
            a = new d.RTCSessionDescription({type: "offer", sdp: g});
            return Promise.resolve(a)
        };
        c.prototype.createAnswer = function () {
            var a = this;
            if (a._isClosed) return Promise.reject(Ja("InvalidStateError", "Can not call createAnswer after close"));
            if ("have-remote-offer" !== a.signalingState && "have-local-pranswer" !== a.signalingState) return Promise.reject(Ja("InvalidStateError", "Can not call createAnswer in signalingState " + a.signalingState));
            var b = H.writeSessionBoilerplate(a._sdpSessionId, a._sdpSessionVersion++);
            a.usingBundle &&
            (b += "a=group:BUNDLE " + a.transceivers.map(function (a) {
                return a.mid
            }).join(" ") + "\r\n");
            b += "a=ice-options:trickle\r\n";
            var c = H.getMediaSections(a._remoteDescription.sdp).length;
            a.transceivers.forEach(function (d, e) {
                if (!(e + 1 > c)) {
                    if (d.rejected) return "application" === d.kind ? "DTLS/SCTP" === d.protocol ? b += "m=application 0 DTLS/SCTP 5000\r\n" : b += "m=application 0 " + d.protocol + " webrtc-datachannel\r\n" : "audio" === d.kind ? b += "m=audio 0 UDP/TLS/RTP/SAVPF 0\r\na=rtpmap:0 PCMU/8000\r\n" : "video" === d.kind && (b += "m=video 0 UDP/TLS/RTP/SAVPF 120\r\na=rtpmap:120 VP8/90000\r\n"),
                        void (b += "c=IN IP4 0.0.0.0\r\na=inactive\r\na=mid:" + d.mid + "\r\n");
                    var g;
                    d.stream && ("audio" === d.kind ? g = d.stream.getAudioTracks()[0] : "video" === d.kind && (g = d.stream.getVideoTracks()[0]), g && 15019 <= f && "video" === d.kind && !d.sendEncodingParameters[0].rtx && (d.sendEncodingParameters[0].rtx = {ssrc: d.sendEncodingParameters[0].ssrc + 1}));
                    e = sd(d.localCapabilities, d.remoteCapabilities);
                    !e.codecs.filter(function (a) {
                        return "rtx" === a.name.toLowerCase()
                    }).length && d.sendEncodingParameters[0].rtx && delete d.sendEncodingParameters[0].rtx;
                    b += sg(d, e, "answer", d.stream, a._dtlsRole);
                    d.rtcpParameters && d.rtcpParameters.reducedSize && (b += "a=rtcp-rsize\r\n")
                }
            });
            var e = new d.RTCSessionDescription({type: "answer", sdp: b});
            return Promise.resolve(e)
        };
        c.prototype.addIceCandidate = function (a) {
            var b, c = this;
            return a && void 0 === a.sdpMLineIndex && !a.sdpMid ? Promise.reject(new TypeError("sdpMLineIndex or sdpMid required")) : new Promise(function (d, e) {
                if (!c._remoteDescription) return e(Ja("InvalidStateError", "Can not add ICE candidate without a remote description"));
                if (a && "" !== a.candidate) {
                    var g = a.sdpMLineIndex;
                    if (a.sdpMid) for (var f = 0; f < c.transceivers.length; f++) if (c.transceivers[f].mid === a.sdpMid) {
                        g = f;
                        break
                    }
                    var h = c.transceivers[g];
                    if (!h) return e(Ja("OperationError", "Can not add ICE candidate"));
                    if (h.rejected) return d();
                    f = 0 < Object.keys(a.candidate).length ? H.parseCandidate(a.candidate) : {};
                    if ("tcp" === f.protocol && (0 === f.port || 9 === f.port) || f.component && 1 !== f.component) return d();
                    if ((0 === g || 0 < g && h.iceTransport !== c.transceivers[0].iceTransport) && !ne(h.iceTransport,
                        f)) return e(Ja("OperationError", "Can not add ICE candidate"));
                    e = a.candidate.trim();
                    0 === e.indexOf("a=") && (e = e.substr(2));
                    (b = H.getMediaSections(c._remoteDescription.sdp))[g] += "a=" + (f.type ? e : "end-of-candidates") + "\r\n";
                    c._remoteDescription.sdp = H.getDescription(c._remoteDescription.sdp) + b.join("")
                } else for (g = 0; g < c.transceivers.length && (c.transceivers[g].rejected || (c.transceivers[g].iceTransport.addRemoteCandidate({}), (b = H.getMediaSections(c._remoteDescription.sdp))[g] += "a=end-of-candidates\r\n", c._remoteDescription.sdp =
                    H.getDescription(c._remoteDescription.sdp) + b.join(""), !c.usingBundle)); g++) ;
                d()
            })
        };
        c.prototype.getStats = function (a) {
            if (a && a instanceof d.MediaStreamTrack) {
                var b = null;
                if (this.transceivers.forEach(function (c) {
                    c.rtpSender && c.rtpSender.track === a ? b = c.rtpSender : c.rtpReceiver && c.rtpReceiver.track === a && (b = c.rtpReceiver)
                }), !b) throw Ja("InvalidAccessError", "Invalid selector.");
                return b.getStats()
            }
            var c = [];
            return this.transceivers.forEach(function (a) {
                ["rtpSender", "rtpReceiver", "iceGatherer", "iceTransport", "dtlsTransport"].forEach(function (b) {
                    a[b] &&
                    c.push(a[b].getStats())
                })
            }), Promise.all(c).then(function (a) {
                var b = new Map;
                return a.forEach(function (a) {
                    a.forEach(function (a) {
                        b.set(a.id, a)
                    })
                }), b
            })
        };
        ["RTCRtpSender", "RTCRtpReceiver", "RTCIceGatherer", "RTCIceTransport", "RTCDtlsTransport"].forEach(function (a) {
            if ((a = d[a]) && a.prototype && a.prototype.getStats) {
                var b = a.prototype.getStats;
                a.prototype.getStats = function () {
                    return b.apply(this).then(function (a) {
                        var b = new Map;
                        return Object.keys(a).forEach(function (c) {
                            var d;
                            a[c].type = {
                                inboundrtp: "inbound-rtp",
                                outboundrtp: "outbound-rtp",
                                candidatepair: "candidate-pair",
                                localcandidate: "local-candidate",
                                remotecandidate: "remote-candidate"
                            }[(d = a[c]).type] || d.type;
                            b.set(c, a[c])
                        }), b
                    })
                }
            }
        });
        var e = ["createOffer", "createAnswer"];
        return e.forEach(function (a) {
            var b = c.prototype[a];
            c.prototype[a] = function () {
                var a = arguments;
                return "function" == typeof a[0] || "function" == typeof a[1] ? b.apply(this, [arguments[2]]).then(function (b) {
                    "function" == typeof a[0] && a[0].apply(null, [b])
                }, function (b) {
                    "function" == typeof a[1] && a[1].apply(null, [b])
                }) : b.apply(this, arguments)
            }
        }),
            (e = ["setLocalDescription", "setRemoteDescription", "addIceCandidate"]).forEach(function (a) {
                var b = c.prototype[a];
                c.prototype[a] = function () {
                    var a = arguments;
                    return "function" == typeof a[1] || "function" == typeof a[2] ? b.apply(this, arguments).then(function () {
                        "function" == typeof a[1] && a[1].apply(null)
                    }, function (b) {
                        "function" == typeof a[2] && a[2].apply(null, [b])
                    }) : b.apply(this, arguments)
                }
            }), ["getStats"].forEach(function (a) {
            var b = c.prototype[a];
            c.prototype[a] = function () {
                var a = arguments;
                return "function" == typeof a[1] ?
                    b.apply(this, arguments).then(function () {
                        "function" == typeof a[1] && a[1].apply(null)
                    }) : b.apply(this, arguments)
            }
        }), c
    }, Ij = Object.freeze({
        __proto__: null,
        shimPeerConnection: oe,
        shimReplaceTrack: wg,
        shimGetUserMedia: ug,
        shimGetDisplayMedia: vg
    }), Jj = Object.freeze({
        __proto__: null,
        shimOnTrack: yg,
        shimPeerConnection: pe,
        shimSenderGetStats: zg,
        shimReceiverGetStats: Ag,
        shimRemoveStream: Bg,
        shimRTCDataChannel: Cg,
        shimGetUserMedia: xg,
        shimGetDisplayMedia: function (d, f) {
            d.navigator.mediaDevices && "getDisplayMedia" in d.navigator.mediaDevices ||
            d.navigator.mediaDevices && (d.navigator.mediaDevices.getDisplayMedia = function (a) {
                return a && a.video ? (!0 === a.video ? a.video = {mediaSource: f} : a.video.mediaSource = f, d.navigator.mediaDevices.getUserMedia(a)) : (a = new DOMException("getDisplayMedia without video constraints is undefined"), a.name = "NotFoundError", a.code = 8, v.reject(a))
            })
        }
    }), Kj = Object.freeze({
        __proto__: null,
        shimLocalStreamsAPI: Dg,
        shimRemoteStreamsAPI: Eg,
        shimCallbacksAPI: Fg,
        shimGetUserMedia: Gg,
        shimConstraints: Hg,
        shimRTCIceServerUrls: Ig,
        shimTrackEventTransceiver: Jg,
        shimCreateOfferLegacy: Kg
    }), zn = Object.freeze({
        __proto__: null,
        shimRTCIceCandidate: ud,
        shimMaxMessageSize: Ic,
        shimSendThrowTypeError: Jc,
        shimConnectionState: qe,
        removeAllowExtmapMixed: re
    });
    !function ({window: d} = {}, f = {shimChrome: !0, shimFirefox: !0, shimEdge: !0, shimSafari: !0}) {
        let a = Gb(d), b = {browserDetails: a, commonShim: zn, extractVersion: Fb, disableLog: Xk, disableWarnings: Yk};
        switch (a.browser) {
            case "chrome":
                if (!Ej || !me || !f.shimChrome) return mb("Chrome shim is not included in this adapter release."), b;
                mb("adapter.js shimming chrome.");
                b.browserShim = Ej;
                hg(d);
                kg(d);
                me(d);
                lg(d);
                qg(d);
                mg(d);
                ng(d);
                og(d);
                rg(d);
                ud(d);
                qe(d);
                Ic(d);
                Jc(d);
                re(d);
                break;
            case "firefox":
                if (!Jj || !pe || !f.shimFirefox) return mb("Firefox shim is not included in this adapter release."), b;
                mb("adapter.js shimming firefox.");
                b.browserShim = Jj;
                xg(d);
                pe(d);
                yg(d);
                Bg(d);
                zg(d);
                Ag(d);
                Cg(d);
                ud(d);
                qe(d);
                Ic(d);
                Jc(d);
                break;
            case "edge":
                if (!Ij || !oe || !f.shimEdge) return mb("MS edge shim is not included in this adapter release."), b;
                mb("adapter.js shimming edge.");
                b.browserShim = Ij;
                ug(d);
                vg(d);
                oe(d);
                wg(d);
                Ic(d);
                Jc(d);
                break;
            case "safari":
                if (!Kj || !f.shimSafari) return mb("Safari shim is not included in this adapter release."), b;
                mb("adapter.js shimming safari.");
                b.browserShim = Kj;
                Ig(d);
                Kg(d);
                Fg(d);
                Dg(d);
                Eg(d);
                Jg(d);
                Gg(d);
                ud(d);
                Ic(d);
                Jc(d);
                re(d);
                break;
            default:
                mb("Unsupported browser!")
        }
    }({window});
    var W, Z;
    !function (d) {
        d.WIN_10 = "Windows 10";
        d.WIN_81 = "Windows 8.1";
        d.WIN_8 = "Windows 8";
        d.WIN_7 = "Windows 7";
        d.WIN_VISTA = "Windows Vista";
        d.WIN_SERVER_2003 = "Windows Server 2003";
        d.WIN_XP = "Windows XP";
        d.WIN_2000 = "Windows 2000";
        d.ANDROID = "Android";
        d.OPEN_BSD = "Open BSD";
        d.SUN_OS = "Sun OS";
        d.LINUX = "Linux";
        d.IOS = "iOS";
        d.MAC_OS_X = "Mac OS X";
        d.MAC_OS = "Mac OS";
        d.QNX = "QNX";
        d.UNIX = "UNIX";
        d.BEOS = "BeOS";
        d.OS_2 = "OS/2";
        d.SEARCH_BOT = "Search Bot"
    }(W || (W = {}));
    (function (d) {
        d.CHROME = "Chrome";
        d.SAFARI = "Safari";
        d.EDGE = "Edge";
        d.FIREFOX = "Firefox";
        d.OPERA = "OPR";
        d.QQ = "QQBrowser";
        d.WECHAT = "MicroMessenger"
    })(Z || (Z = {}));
    let Va = function (d) {
        if (d.match(/[0-9]+\.[0-9]+\.[0-9]+$/)) return d;
        var f = d.match(/([0-9]+\.[0-9]+\.[0-9]+)\-alpha\.([0-9]+)/);
        if (f && f[1] && f[2]) {
            var a, b = f[2];
            return l(a = "".concat(f[1], ".")).call(a, b)
        }
        return (f = d.match(/([0-9]+\.[0-9]+\.[0-9]+)\-special\.([0-9]+)/)) && f[1] && f[2] ? (a = f[2], l(b = "".concat(f[1], ".")).call(b, 100 * (Number(a) + 1))) : "4.0.0.999"
    }("4.4.0");
    try {
        var Lj = !0 === JSON.parse("true")
    } catch (d) {
        Lj = !0
    }
    let Ff = Lj,
        Ga = {username: "test", password: "111111", turnServerURL: "", tcpport: 3433, udpport: 3478, forceturn: !1},
        $k = {
            "90p": J(160, 90),
            "90p_1": J(160, 90),
            "120p": J(160, 120, 15, 30, 65),
            "120p_1": J(160, 120, 15, 30, 65),
            "120p_3": J(120, 120, 15,
                30, 50),
            "120p_4": J(212, 120),
            "180p": J(320, 180, 15, 30, 140),
            "180p_1": J(320, 180, 15, 30, 140),
            "180p_3": J(180, 180, 15, 30, 100),
            "180p_4": J(240, 180, 15, 30, 120),
            "240p": J(320, 240, 15, 40, 200),
            "240p_1": J(320, 240, 15, 40, 200),
            "240p_3": J(240, 240, 15, 40, 140),
            "240p_4": J(424, 240, 15, 40, 220),
            "360p": J(640, 360, 15, 80, 400),
            "360p_1": J(640, 360, 15, 80, 400),
            "360p_3": J(360, 360, 15, 80, 260),
            "360p_4": J(640, 360, 30, 80, 600),
            "360p_6": J(360, 360, 30, 80, 400),
            "360p_7": J(480, 360, 15, 80, 320),
            "360p_8": J(480, 360, 30, 80, 490),
            "360p_9": J(640, 360, 15, 80, 800),
            "360p_10": J(640, 360, 24, 80, 800),
            "360p_11": J(640, 360, 24, 80, 1E3),
            "480p": J(640, 480, 15, 100, 500),
            "480p_1": J(640, 480, 15, 100, 500),
            "480p_2": J(640, 480, 30, 100, 1E3),
            "480p_3": J(480, 480, 15, 100, 400),
            "480p_4": J(640, 480, 30, 100, 750),
            "480p_6": J(480, 480, 30, 100, 600),
            "480p_8": J(848, 480, 15, 100, 610),
            "480p_9": J(848, 480, 30, 100, 930),
            "480p_10": J(640, 480, 10, 100, 400),
            "720p": J(1280, 720, 15, 120, 1130),
            "720p_1": J(1280, 720, 15, 120, 1130),
            "720p_2": J(1280, 720, 30, 120, 2E3),
            "720p_3": J(1280, 720, 30, 120, 1710),
            "720p_5": J(960, 720, 15, 120, 910),
            "720p_6": J(960,
                720, 30, 120, 1380),
            "1080p": J(1920, 1080, 15, 120, 2080),
            "1080p_1": J(1920, 1080, 15, 120, 2080),
            "1080p_2": J(1920, 1080, 30, 120, 3E3),
            "1080p_3": J(1920, 1080, 30, 120, 3150),
            "1080p_5": J(1920, 1080, 60, 120, 4780),
            "1440p": J(2560, 1440, 30, 120, 4850),
            "1440p_1": J(2560, 1440, 30, 120, 4850),
            "1440p_2": J(2560, 1440, 60, 120, 7350),
            "4k": J(3840, 2160, 30, 120, 8910),
            "4k_1": J(3840, 2160, 30, 120, 8910),
            "4k_3": J(3840, 2160, 60, 120, 13500)
        }, al = {
            "480p": eb(640, 480, 5),
            "480p_1": eb(640, 480, 5),
            "480p_2": eb(640, 480, 30),
            "480p_3": eb(640, 480, 15),
            "720p": eb(1280, 720,
                5),
            "720p_1": eb(1280, 720, 5),
            "720p_2": eb(1280, 720, 30),
            "720p_3": eb(1280, 720, 15),
            "1080p": eb(1920, 1080, 5),
            "1080p_1": eb(1920, 1080, 5),
            "1080p_2": eb(1920, 1080, 30),
            "1080p_3": eb(1920, 1080, 15)
        }, bl = {"1SL1TL": te(1, 1), "3SL3TL": te(3, 3), "2SL3TL": te(2, 3)}, cl = {
            speech_low_quality: kc(16E3, !1),
            speech_standard: kc(32E3, !1, 18),
            music_standard: kc(48E3, !1),
            standard_stereo: kc(48E3, !0, 56),
            high_quality: kc(48E3, !1, 128),
            high_quality_stereo: kc(48E3, !0, 192)
        }, u = {
            PROCESS_ID: "",
            ENCRYPT_AES: !0,
            AREAS: ["CHINA", "GLOBAL"],
            WEBCS_DOMAIN: ["webrtc2-ap-web-1.agora.io",
                "webrtc2-2.ap.sd-rtn.com"],
            WEBCS_DOMAIN_BACKUP_LIST: ["webrtc2-ap-web-3.agora.io", "webrtc2-4.ap.sd-rtn.com"],
            PROXY_CS: ["ap-proxy-1.agora.io", "ap-proxy-2.agora.io"],
            CDS_AP: ["cds-ap-web-1.agora.io", "cds-web-2.ap.sd-rtn.com", "cds-ap-web-3.agora.io", "cds-web-4.ap.sd-rtn.com"],
            ACCOUNT_REGISTER: ["sua-ap-web-1.agora.io", "sua-web-2.ap.sd-rtn.com", "sua-ap-web-3.agora.io", "sua-web-4.ap.sd-rtn.com"],
            UAP_AP: ["uap-ap-web-1.agora.io", "uap-web-2.ap.sd-rtn.com", "uap-ap-web-3.agora.io", "uap-web-4.ap.sd-rtn.com"],
            LOG_UPLOAD_SERVER: "logservice.agora.io",
            EVENT_REPORT_DOMAIN: "statscollector-1.agora.io",
            EVENT_REPORT_BACKUP_DOMAIN: "web-2.statscollector.sd-rtn.com",
            GATEWAY_ADDRESS: [],
            GATEWAY_WSS_ADDRESS: "",
            LIVE_STREAMING_ADDRESS: "",
            ACCOUNT_REGISTER_RETRY_TIMEOUT: 1,
            ACCOUNT_REGISTER_RETRY_RATIO: 2,
            ACCOUNT_REGISTER_RETRY_TIMEOUT_MAX: 6E4,
            ACCOUNT_REGISTER_RETRY_COUNT_MAX: 1E5,
            AUDIO_CONTEXT: null,
            WEBCS_BACKUP_CONNECT_TIMEOUT: 6E3,
            HTTP_CONNECT_TIMEOUT: 5E3,
            PLAYER_STATE_DEFER: 2E3,
            SIGNAL_REQUEST_TIMEOUT: 1E4,
            SIGNAL_REQUEST_WATCH_INTERVAL: 1E3,
            REPORT_STATS: !0,
            UPLOAD_LOG: !1,
            NOT_REPORT_EVENT: [],
            FILEPATH_LENMAX: 255,
            SUBSCRIBE_TCC: !0,
            PING_PONG_TIME_OUT: 10,
            DUALSTREAM_OPERATION_CHECK: !0,
            WEBSOCKET_TIMEOUT_MIN: 1E4,
            EVENT_REPORT_SEND_INTERVAL: 3E3,
            CONFIG_DISTRIBUTE_INTERVAL: 3E5,
            MEDIA_ELEMENT_EXISTS_DEPTH: 3,
            CANDIDATE_TIMEOUT: 5E3,
            SHIM_CANDIDATE: !1,
            LEAVE_MSG_TIMEOUT: 2E3,
            SHOW_REPORT_INVOKER_LOG: !1,
            STATS_FILTER: {transportId: !0, googTrackId: !0},
            JOIN_EXTEND: "",
            PUB_EXTEND: "",
            SUB_EXTEND: "",
            FORCE_TURN: !1,
            TURN_ENABLE_TCP: !0,
            TURN_ENABLE_UDP: !0,
            MAX_UPLOAD_CACHE: 50,
            UPLOAD_CACHE_INTERVAL: 2E3,
            AJAX_REQUEST_CONCURRENT: 3,
            REPORT_APP_SCENARIO: void 0,
            CHROME_FORCE_PLAN_B: !1,
            AUDIO_SOURCE_VOLUME_UPDATE_INTERVAL: 400,
            AUDIO_SOURCE_AVG_VOLUME_DURATION: 3E3,
            AUDIO_VOLUME_INDICATION_INTERVAL: 2E3,
            NORMAL_EVENT_QUEUE_CAPACITY: 100,
            CUSTOM_REPORT: !0,
            CUSTOM_REPORT_LIMIT: 20,
            PROXY_SERVER_TYPE2: "webnginx-proxy.agora.io",
            PROXY_SERVER_TYPE3: "webrtc-cloud-proxy.sd-rtn.com",
            CUSTOM_PUB_ANSWER_MODIFIER: null,
            CUSTOM_SUB_ANSWER_MODIFIER: null,
            CUSTOM_PUB_OFFER_MODIFIER: null,
            CUSTOM_SUB_OFFER_MODIFIER: null,
            DSCP_TYPE: "high"
        };
    Ff || (u.WEBCS_DOMAIN = ["ap-web-1-oversea.agora.io", "ap-web-1-north-america.agora.io"], u.WEBCS_DOMAIN_BACKUP_LIST = ["ap-web-2-oversea.agora.io", "ap-web-2-north-america.agora.io"], u.PROXY_CS = ["proxy-ap-web-oversea.agora.io", "proxy-ap-web-america.agora.io"], u.CDS_AP = ["cds-ap-web-oversea.agora.io", "cds-ap-web-america.agora.io", "cds-ap-web-america2.agora.io"], u.ACCOUNT_REGISTER = ["sua-ap-web-oversea.agora.io", "sua-ap-web-america.agora.io", "sua-ap-web-america2.agora.io"], u.UAP_AP = ["uap-ap-web-oversea.agora.io",
        "uap-ap-web-america.agora.io", "uap-ap-web-america2.agora.io"], u.LOG_UPLOAD_SERVER = "logservice-oversea.agora.io", u.EVENT_REPORT_DOMAIN = "statscollector-1-oversea.agora.io", u.EVENT_REPORT_BACKUP_DOMAIN = "statscollector-2-oversea.agora.io", u.PROXY_SERVER_TYPE3 = "webrtc-cloud-proxy.agora.io", u.AREAS = ["NORTH_AMERICA", "OVERSEA"]);
    let An = [[0, 1, 2, 3, 4, 5, 5], [0, 2, 2, 3, 4, 5, 5], [0, 3, 3, 3, 4, 5, 5], [0, 4, 4, 4, 4, 5, 5], [0, 5, 5, 5, 5, 5, 5]],
        Mj = [];
    var Gf = [], Nj = Gf.sort, Bn = qa(function () {
            Gf.sort(void 0)
        }), Cn = qa(function () {
            Gf.sort(null)
        }),
        Dn = Xc("sort");
    O({target: "Array", proto: !0, forced: Bn || !Cn || Dn}, {
        sort: function (d) {
            return void 0 === d ? Nj.call(qb(this)) : Nj.call(qb(this), nb(d))
        }
    });
    var En = za("Array").sort, Oj = Array.prototype, ed = function (d) {
        var f = d.sort;
        return d === Oj || d instanceof Array && f === Oj.sort ? En : f
    };
    O({target: "Array", stat: !0}, {isArray: ac});
    var n, mc = ha.Array.isArray;
    !function (d) {
        d.UNEXPECTED_ERROR = "UNEXPECTED_ERROR";
        d.UNEXPECTED_RESPONSE = "UNEXPECTED_RESPONSE";
        d.TIMEOUT = "TIMEOUT";
        d.INVALID_PARAMS = "INVALID_PARAMS";
        d.NOT_READABLE = "NOT_READABLE";
        d.NOT_SUPPORTED = "NOT_SUPPORTED";
        d.INVALID_OPERATION = "INVALID_OPERATION";
        d.OPERATION_ABORTED = "OPERATION_ABORTED";
        d.WEB_SECURITY_RESTRICT = "WEB_SECURITY_RESTRICT";
        d.NETWORK_ERROR = "NETWORK_ERROR";
        d.NETWORK_TIMEOUT = "NETWORK_TIMEOUT";
        d.NETWORK_RESPONSE_ERROR = "NETWORK_RESPONSE_ERROR";
        d.API_INVOKE_TIMEOUT = "API_INVOKE_TIMEOUT";
        d.ENUMERATE_DEVICES_FAILED = "ENUMERATE_DEVICES_FAILED";
        d.DEVICE_NOT_FOUND = "DEVICE_NOT_FOUND";
        d.ELECTRON_IS_NULL = "ELECTRON_IS_NULL";
        d.ELECTRON_DESKTOP_CAPTURER_GET_SOURCES_ERROR = "ELECTRON_DESKTOP_CAPTURER_GET_SOURCES_ERROR";
        d.CHROME_PLUGIN_NO_RESPONSE = "CHROME_PLUGIN_NO_RESPONSE";
        d.CHROME_PLUGIN_NOT_INSTALL = "CHROME_PLUGIN_NOT_INSTALL";
        d.MEDIA_OPTION_INVALID = "MEDIA_OPTION_INVALID";
        d.PERMISSION_DENIED = "PERMISSION_DENIED";
        d.CONSTRAINT_NOT_SATISFIED = "CONSTRAINT_NOT_SATISFIED";
        d.TRACK_IS_DISABLED = "TRACK_IS_DISABLED";
        d.SHARE_AUDIO_NOT_ALLOWED = "SHARE_AUDIO_NOT_ALLOWED";
        d.LOW_STREAM_ENCODING_ERROR = "LOW_STREAM_ENCODING_ERROR";
        d.SET_ENCODING_PARAMETER_ERROR = "SET_ENCODING_PARAMETER_ERROR";
        d.INVALID_UINT_UID_FROM_STRING_UID = "INVALID_UINT_UID_FROM_STRING_UID";
        d.CAN_NOT_GET_PROXY_SERVER = "CAN_NOT_GET_PROXY_SERVER";
        d.CAN_NOT_GET_GATEWAY_SERVER = "CAN_NOT_GET_GATEWAY_SERVER";
        d.VOID_GATEWAY_ADDRESS = "VOID_GATEWAY_ADDRESS";
        d.UID_CONFLICT = "UID_CONFLICT";
        d.INVALID_LOCAL_TRACK = "INVALID_LOCAL_TRACK";
        d.INVALID_TRACK = "INVALID_TRACK";
        d.SENDER_NOT_FOUND = "SENDER_NOT_FOUND";
        d.CREATE_OFFER_FAILED = "CREATE_OFFER_FAILED";
        d.SET_ANSWER_FAILED = "SET_ANSWER_FAILED";
        d.ICE_FAILED = "ICE_FAILED";
        d.PC_CLOSED = "PC_CLOSED";
        d.SENDER_REPLACE_FAILED = "SENDER_REPLACE_FAILED";
        d.GATEWAY_P2P_LOST =
            "GATEWAY_P2P_LOST";
        d.NO_ICE_CANDIDATE = "NO_ICE_CANDIDATE";
        d.CAN_NOT_PUBLISH_MULTIPLE_VIDEO_TRACKS = "CAN_NOT_PUBLISH_MULTIPLE_VIDEO_TRACKS";
        d.EXIST_DISABLED_VIDEO_TRACK = "EXIST_DISABLED_VIDEO_TRACK";
        d.INVALID_REMOTE_USER = "INVALID_REMOTE_USER";
        d.REMOTE_USER_IS_NOT_PUBLISHED = "REMOTE_USER_IS_NOT_PUBLISHED";
        d.CUSTOM_REPORT_SEND_FAILED = "CUSTOM_REPORT_SEND_FAILED";
        d.CUSTOM_REPORT_FREQUENCY_TOO_HIGH = "CUSTOM_REPORT_FREQUENCY_TOO_HIGH";
        d.FETCH_AUDIO_FILE_FAILED = "FETCH_AUDIO_FILE_FAILED";
        d.READ_LOCAL_AUDIO_FILE_ERROR =
            "READ_LOCAL_AUDIO_FILE_ERROR";
        d.DECODE_AUDIO_FILE_FAILED = "DECODE_AUDIO_FILE_FAILED";
        d.WS_ABORT = "WS_ABORT";
        d.WS_DISCONNECT = "WS_DISCONNECT";
        d.WS_ERR = "WS_ERR";
        d.LIVE_STREAMING_TASK_CONFLICT = "LIVE_STREAMING_TASK_CONFLICT";
        d.LIVE_STREAMING_INVALID_ARGUMENT = "LIVE_STREAMING_INVALID_ARGUMENT";
        d.LIVE_STREAMING_INTERNAL_SERVER_ERROR = "LIVE_STREAMING_INTERNAL_SERVER_ERROR";
        d.LIVE_STREAMING_PUBLISH_STREAM_NOT_AUTHORIZED = "LIVE_STREAMING_PUBLISH_STREAM_NOT_AUTHORIZED";
        d.LIVE_STREAMING_TRANSCODING_NOT_SUPPORTED =
            "LIVE_STREAMING_TRANSCODING_NOT_SUPPORTED";
        d.LIVE_STREAMING_CDN_ERROR = "LIVE_STREAMING_CDN_ERROR";
        d.LIVE_STREAMING_INVALID_RAW_STREAM = "LIVE_STREAMING_INVALID_RAW_STREAM";
        d.LIVE_STREAMING_WARN_STREAM_NUM_REACH_LIMIT = "LIVE_STREAMING_WARN_STREAM_NUM_REACH_LIMIT";
        d.LIVE_STREAMING_WARN_FAILED_LOAD_IMAGE = "LIVE_STREAMING_WARN_FAILED_LOAD_IMAGE";
        d.LIVE_STREAMING_WARN_FREQUENT_REQUEST = "LIVE_STREAMING_WARN_FREQUENT_REQUEST";
        d.WEBGL_INTERNAL_ERROR = "WEBGL_INTERNAL_ERROR";
        d.BEAUTY_PROCESSOR_INTERNAL_ERROR = "BEAUTY_PROCESSOR_INTERNAL_ERROR";
        d.CROSS_CHANNEL_WAIT_STATUS_ERROR = "CROSS_CHANNEL_WAIT_STATUS_ERROR";
        d.CROSS_CHANNEL_FAILED_JOIN_SRC = "CROSS_CHANNEL_FAILED_JOIN_SEC";
        d.CROSS_CHANNEL_FAILED_JOIN_DEST = "CROSS_CHANNEL_FAILED_JOIN_DEST";
        d.CROSS_CHANNEL_FAILED_PACKET_SENT_TO_DEST = "CROSS_CHANNEL_FAILED_PACKET_SENT_TO_DEST";
        d.CROSS_CHANNEL_SERVER_ERROR_RESPONSE = "CROSS_CHANNEL_SERVER_ERROR_RESPONSE";
        d.METADATA_OUT_OF_RANGE = "METADATA_OUT_OF_RANGE";
        d.LOCAL_AEC_ERROR = "LOCAL_AEC_ERROR"
    }(n || (n = {}));
    var Tg = function (d, f) {
        return function () {
            for (var a =
                Array(arguments.length), b = 0; b < a.length; b++) a[b] = arguments[b];
            return d.apply(f, a)
        }
    }, lc = Object.prototype.toString, K = {
        isArray: Lg, isArrayBuffer: function (d) {
            return "[object ArrayBuffer]" === lc.call(d)
        }, isBuffer: function (d) {
            return null != d && null != d.constructor && "function" == typeof d.constructor.isBuffer && d.constructor.isBuffer(d)
        }, isFormData: function (d) {
            return "undefined" != typeof FormData && d instanceof FormData
        }, isArrayBufferView: function (d) {
            return "undefined" != typeof ArrayBuffer && ArrayBuffer.isView ? ArrayBuffer.isView(d) :
                d && d.buffer && d.buffer instanceof ArrayBuffer
        }, isString: function (d) {
            return "string" == typeof d
        }, isNumber: function (d) {
            return "number" == typeof d
        }, isObject: Mg, isUndefined: function (d) {
            return void 0 === d
        }, isDate: function (d) {
            return "[object Date]" === lc.call(d)
        }, isFile: function (d) {
            return "[object File]" === lc.call(d)
        }, isBlob: function (d) {
            return "[object Blob]" === lc.call(d)
        }, isFunction: Ng, isStream: function (d) {
            return Mg(d) && Ng(d.pipe)
        }, isURLSearchParams: function (d) {
            return "undefined" != typeof URLSearchParams && d instanceof
                URLSearchParams
        }, isStandardBrowserEnv: function () {
            return ("undefined" == typeof navigator || "ReactNative" !== navigator.product && "NativeScript" !== navigator.product && "NS" !== navigator.product) && "undefined" != typeof window && "undefined" != typeof document
        }, forEach: xd, merge: function f() {
            function a(a, c) {
                "object" == typeof b[c] && "object" == typeof a ? b[c] = f(b[c], a) : b[c] = a
            }

            for (var b = {}, c = 0, e = arguments.length; c < e; c++) xd(arguments[c], a);
            return b
        }, deepMerge: function a() {
            function b(b, e) {
                "object" == typeof c[e] && "object" == typeof b ?
                    c[e] = a(c[e], b) : c[e] = "object" == typeof b ? a({}, b) : b
            }

            for (var c = {}, e = 0, g = arguments.length; e < g; e++) xd(arguments[e], b);
            return c
        }, extend: function (a, b, c) {
            return xd(b, function (b, g) {
                a[g] = c && "function" == typeof b ? Tg(b, c) : b
            }), a
        }, trim: function (a) {
            return a.replace(/^\s*/, "").replace(/\s*$/, "")
        }
    }, Pj = function (a, b, c) {
        if (!b) return a;
        if (c) b = c(b); else if (K.isURLSearchParams(b)) b = b.toString(); else {
            var e = [];
            K.forEach(b, function (a, b) {
                null != a && (K.isArray(a) ? b += "[]" : a = [a], K.forEach(a, function (a) {
                    K.isDate(a) ? a = a.toISOString() :
                        K.isObject(a) && (a = JSON.stringify(a));
                    e.push(Og(b) + "=" + Og(a))
                }))
            });
            b = e.join("&")
        }
        b && (c = a.indexOf("#"), -1 !== c && (a = a.slice(0, c)), a += (-1 === a.indexOf("?") ? "?" : "&") + b);
        return a
    };
    yd.prototype.use = function (a, b) {
        return this.handlers.push({fulfilled: a, rejected: b}), this.handlers.length - 1
    };
    yd.prototype.eject = function (a) {
        this.handlers[a] && (this.handlers[a] = null)
    };
    yd.prototype.forEach = function (a) {
        K.forEach(this.handlers, function (b) {
            null !== b && a(b)
        })
    };
    var Qg = yd, Hf = function (a, b, c) {
            return K.forEach(c, function (c) {
                a =
                    c(a, b)
            }), a
        }, Qj = function (a) {
            return !(!a || !a.__CANCEL__)
        }, Rj = function (a, b) {
            K.forEach(a, function (c, e) {
                e !== b && e.toUpperCase() === b.toUpperCase() && (a[b] = c, delete a[e])
            })
        }, fe = function (a, b, c, e, g) {
            return function (a, b, c, e, g) {
                return a.config = b, c && (a.code = c), a.request = e, a.response = g, a.isAxiosError = !0, a.toJSON = function () {
                    return {
                        message: this.message,
                        name: this.name,
                        description: this.description,
                        number: this.number,
                        fileName: this.fileName,
                        lineNumber: this.lineNumber,
                        columnNumber: this.columnNumber,
                        stack: this.stack,
                        config: this.config,
                        code: this.code
                    }
                }, a
            }(Error(a), b, c, e, g)
        },
        Fn = "age authorization content-length content-type etag expires from host if-modified-since if-unmodified-since last-modified location max-forwards proxy-authorization referer retry-after user-agent".split(" "),
        Gn = K.isStandardBrowserEnv() ? function () {
            function a(a) {
                return c && (e.setAttribute("href", a), a = e.href), e.setAttribute("href", a), {
                    href: e.href,
                    protocol: e.protocol ? e.protocol.replace(/:$/, "") : "",
                    host: e.host,
                    search: e.search ? e.search.replace(/^\?/, "") : "",
                    hash: e.hash ?
                        e.hash.replace(/^#/, "") : "",
                    hostname: e.hostname,
                    port: e.port,
                    pathname: "/" === e.pathname.charAt(0) ? e.pathname : "/" + e.pathname
                }
            }

            var b, c = /(msie|trident)/i.test(navigator.userAgent), e = document.createElement("a");
            return b = a(window.location.href), function (c) {
                c = K.isString(c) ? a(c) : c;
                return c.protocol === b.protocol && c.host === b.host
            }
        }() : function () {
            return !0
        }, Hn = K.isStandardBrowserEnv() ? {
            write: function (a, b, c, e, g, h) {
                var m = [];
                m.push(a + "=" + encodeURIComponent(b));
                K.isNumber(c) && m.push("expires=" + (new Date(c)).toGMTString());
                K.isString(e) && m.push("path=" + e);
                K.isString(g) && m.push("domain=" + g);
                !0 === h && m.push("secure");
                document.cookie = m.join("; ")
            }, read: function (a) {
                return (a = document.cookie.match(new RegExp("(^|;\\s*)(" + a + ")=([^;]*)"))) ? decodeURIComponent(a[3]) : null
            }, remove: function (a) {
                this.write(a, "", Date.now() - 864E5)
            }
        } : {
            write: function () {
            }, read: function () {
                return null
            }, remove: function () {
            }
        }, In = function (a) {
            return new Promise(function (b, c) {
                var e = a.data, g = a.headers;
                K.isFormData(e) && delete g["Content-Type"];
                var h = new XMLHttpRequest;
                a.auth && (g.Authorization = "Basic " + btoa((a.auth.username || "") + ":" + (a.auth.password || "")));
                if (h.open(a.method.toUpperCase(), Pj(a.url, a.params, a.paramsSerializer), !0), h.timeout = a.timeout, h.onreadystatechange = function () {
                    if (h && 4 === h.readyState && (0 !== h.status || h.responseURL && 0 === h.responseURL.indexOf("file:"))) {
                        var e, g, m, k, l,
                            n = "getAllResponseHeaders" in h ? (e = h.getAllResponseHeaders(), l = {}, e ? (K.forEach(e.split("\n"), function (a) {
                                (k = a.indexOf(":"), g = K.trim(a.substr(0, k)).toLowerCase(), m = K.trim(a.substr(k + 1)),
                                    !g) || l[g] && 0 <= Fn.indexOf(g) || (l[g] = "set-cookie" === g ? (l[g] ? l[g] : []).concat([m]) : l[g] ? l[g] + ", " + m : m)
                            }), l) : l) : null;
                        !function (a, b, c) {
                            var e = c.config.validateStatus;
                            !e || e(c.status) ? a(c) : b(fe("Request failed with status code " + c.status, c.config, null, c.request, c))
                        }(b, c, {
                            data: a.responseType && "text" !== a.responseType ? h.response : h.responseText,
                            status: h.status,
                            statusText: h.statusText,
                            headers: n,
                            config: a,
                            request: h
                        });
                        h = null
                    }
                }, h.onabort = function () {
                    h && (c(fe("Request aborted", a, "ECONNABORTED", h)), h = null)
                }, h.onerror =
                    function () {
                        c(fe("Network Error", a, null, h));
                        h = null
                    }, h.ontimeout = function () {
                    c(fe("timeout of " + a.timeout + "ms exceeded", a, "ECONNABORTED", h));
                    h = null
                }, K.isStandardBrowserEnv()) {
                    var m = (a.withCredentials || Gn(a.url)) && a.xsrfCookieName ? Hn.read(a.xsrfCookieName) : void 0;
                    m && (g[a.xsrfHeaderName] = m)
                }
                if ("setRequestHeader" in h && K.forEach(g, function (a, b) {
                    void 0 === e && "content-type" === b.toLowerCase() ? delete g[b] : h.setRequestHeader(b, a)
                }), a.withCredentials && (h.withCredentials = !0), a.responseType) try {
                    h.responseType = a.responseType
                } catch (r) {
                    if ("json" !==
                        a.responseType) throw r;
                }
                "function" == typeof a.onDownloadProgress && h.addEventListener("progress", a.onDownloadProgress);
                "function" == typeof a.onUploadProgress && h.upload && h.upload.addEventListener("progress", a.onUploadProgress);
                a.cancelToken && a.cancelToken.promise.then(function (a) {
                    h && (h.abort(), c(a), h = null)
                });
                void 0 === e && (e = null);
                h.send(e)
            })
        }, Jn = {"Content-Type": "application/x-www-form-urlencoded"}, ge = {
            adapter: function () {
                var a;
                return ("undefined" != typeof process && "[object process]" === Object.prototype.toString.call(process) ||
                    "undefined" != typeof XMLHttpRequest) && (a = In), a
            }(),
            transformRequest: [function (a, b) {
                return Rj(b, "Accept"), Rj(b, "Content-Type"), K.isFormData(a) || K.isArrayBuffer(a) || K.isBuffer(a) || K.isStream(a) || K.isFile(a) || K.isBlob(a) ? a : K.isArrayBufferView(a) ? a.buffer : K.isURLSearchParams(a) ? (Pg(b, "application/x-www-form-urlencoded;charset=utf-8"), a.toString()) : K.isObject(a) ? (Pg(b, "application/json;charset=utf-8"), JSON.stringify(a)) : a
            }],
            transformResponse: [function (a) {
                if ("string" == typeof a) try {
                    a = JSON.parse(a)
                } catch (b) {
                }
                return a
            }],
            timeout: 0,
            xsrfCookieName: "XSRF-TOKEN",
            xsrfHeaderName: "X-XSRF-TOKEN",
            maxContentLength: -1,
            validateStatus: function (a) {
                return 200 <= a && 300 > a
            },
            headers: {common: {Accept: "application/json, text/plain, */*"}}
        };
    K.forEach(["delete", "get", "head"], function (a) {
        ge.headers[a] = {}
    });
    K.forEach(["post", "put", "patch"], function (a) {
        ge.headers[a] = K.merge(Jn)
    });
    var Kn = function (a) {
        var b, c, e;
        a.cancelToken && a.cancelToken.throwIfRequested();
        return a.baseURL && (e = a.url, !/^([a-z][a-z\d\+\-\.]*:)?\/\//i.test(e)) && (a.url = (b = a.baseURL,
            (c = a.url) ? b.replace(/\/+$/, "") + "/" + c.replace(/^\/+/, "") : b)), a.headers = a.headers || {}, a.data = Hf(a.data, a.headers, a.transformRequest), a.headers = K.merge(a.headers.common || {}, a.headers[a.method] || {}, a.headers || {}), K.forEach("delete get head post put patch common".split(" "), function (b) {
            delete a.headers[b]
        }), (a.adapter || ge.adapter)(a).then(function (b) {
            a.cancelToken && a.cancelToken.throwIfRequested();
            return b.data = Hf(b.data, b.headers, a.transformResponse), b
        }, function (b) {
            Qj(b) || (a.cancelToken && a.cancelToken.throwIfRequested(),
            b && b.response && (b.response.data = Hf(b.response.data, b.response.headers, a.transformResponse)));
            return Promise.reject(b)
        })
    }, If = function (a, b) {
        b = b || {};
        var c = {};
        return K.forEach(["url", "method", "params", "data"], function (a) {
            void 0 !== b[a] && (c[a] = b[a])
        }), K.forEach(["headers", "auth", "proxy"], function (e) {
            K.isObject(b[e]) ? c[e] = K.deepMerge(a[e], b[e]) : void 0 !== b[e] ? c[e] = b[e] : K.isObject(a[e]) ? c[e] = K.deepMerge(a[e]) : void 0 !== a[e] && (c[e] = a[e])
        }), K.forEach("baseURL transformRequest transformResponse paramsSerializer timeout withCredentials adapter responseType xsrfCookieName xsrfHeaderName onUploadProgress onDownloadProgress maxContentLength validateStatus maxRedirects httpAgent httpsAgent cancelToken socketPath".split(" "),
            function (e) {
                void 0 !== b[e] ? c[e] = b[e] : void 0 !== a[e] && (c[e] = a[e])
            }), c
    };
    Kc.prototype.request = function (a, b) {
        "string" == typeof a ? (a = b || {}).url = a : a = a || {};
        (a = If(this.defaults, a)).method = a.method ? a.method.toLowerCase() : "get";
        var c = [Kn, void 0];
        a = Promise.resolve(a);
        this.interceptors.request.forEach(function (a) {
            c.unshift(a.fulfilled, a.rejected)
        });
        for (this.interceptors.response.forEach(function (a) {
            c.push(a.fulfilled, a.rejected)
        }); c.length;) a = a.then(c.shift(), c.shift());
        return a
    };
    Kc.prototype.getUri = function (a) {
        return a =
            If(this.defaults, a), Pj(a.url, a.params, a.paramsSerializer).replace(/^\?/, "")
    };
    K.forEach(["delete", "get", "head", "options"], function (a) {
        Kc.prototype[a] = function (b, c) {
            return this.request(K.merge(c || {}, {method: a, url: b}))
        }
    });
    K.forEach(["post", "put", "patch"], function (a) {
        Kc.prototype[a] = function (b, c, e) {
            return this.request(K.merge(e || {}, {method: a, url: b, data: c}))
        }
    });
    var Ad = Kc;
    ue.prototype.toString = function () {
        return "Cancel" + (this.message ? ": " + this.message : "")
    };
    ue.prototype.__CANCEL__ = !0;
    var Rg = ue;
    zd.prototype.throwIfRequested =
        function () {
            if (this.reason) throw this.reason;
        };
    zd.source = function () {
        var a;
        return {
            token: new zd(function (b) {
                a = b
            }), cancel: a
        }
    };
    var Eb = Sg(ge);
    Eb.Axios = Ad;
    Eb.create = function (a) {
        return Sg(If(Eb.defaults, a))
    };
    Eb.Cancel = Rg;
    Eb.CancelToken = zd;
    Eb.isCancel = Qj;
    Eb.all = function (a) {
        return Promise.all(a)
    };
    Eb.spread = function (a) {
        return function (b) {
            return a.apply(null, b)
        }
    };
    var Bb = Eb.default = Eb;
    let rb = {DEBUG: 0, INFO: 1, WARNING: 2, ERROR: 3, NONE: 4}, Sj = a => {
        for (const b in rb) if (rb[b] === a) return b;
        return "DEFAULT"
    }, k = new class {
        constructor() {
            this.logLevel =
                rb.DEBUG;
            this.uploadLogWaitingList = [];
            this.uploadLogUploadingList = [];
            this.currentLogID = this.uploadErrorCount = 0
        }

        debug(...a) {
            var b;
            a = l(b = [rb.DEBUG]).call(b, a);
            this.log.apply(this, a)
        }

        info(...a) {
            var b;
            a = l(b = [rb.INFO]).call(b, a);
            this.log.apply(this, a)
        }

        warning(...a) {
            var b;
            a = l(b = [rb.WARNING]).call(b, a);
            this.log.apply(this, a)
        }

        error(...a) {
            var b;
            a = l(b = [rb.ERROR]).call(b, a);
            this.log.apply(this, a)
        }

        setLogLevel(a) {
            this.logLevel = a = Math.min(Math.max(0, a), 4)
        }

        enableLogUpload() {
            jc("UPLOAD_LOG", !0)
        }

        disableLogUpload() {
            jc("UPLOAD_LOG",
                !1);
            this.uploadLogUploadingList = [];
            this.uploadLogWaitingList = []
        }

        setProxyServer(a) {
            this.proxyServerURL = a
        }

        log(...a) {
            var b, c, e, g;
            const h = Math.max(0, Math.min(4, a[0]));
            if (!(a[0] = Ug() + " Agora-SDK [".concat(Sj(h), "]:"), this.appendLogToWaitingList(h, a), h < this.logLevel)) {
                var m = Ug() + " %cAgora-SDK [".concat(Sj(h), "]:");
                switch (h) {
                    case rb.DEBUG:
                        a = l(b = [m, "color: #64B5F6;"]).call(b, Aa(a).call(a, 1));
                        console.log.apply(console, a);
                        break;
                    case rb.INFO:
                        a = l(c = [m, "color: #1E88E5; font-weight: bold;"]).call(c, Aa(a).call(a,
                            1));
                        console.log.apply(console, a);
                        break;
                    case rb.WARNING:
                        a = l(e = [m, "color: #FB8C00; font-weight: bold;"]).call(e, Aa(a).call(a, 1));
                        console.warn.apply(console, a);
                        break;
                    case rb.ERROR:
                        a = l(g = [m, "color: #B00020; font-weight: bold;"]).call(g, Aa(a).call(a, 1)), console.error.apply(console, a)
                }
            }
        }

        appendLogToWaitingList(a, ...b) {
            if (u.UPLOAD_LOG) {
                var c = "";
                q(b).call(b, a => {
                    "object" == typeof a && (a = A(a));
                    c += "".concat(a, " ")
                });
                this.uploadLogWaitingList.push({payload_str: c, log_level: a, log_item_id: this.currentLogID++});
                0 ===
                this.uploadLogUploadingList.length && this.uploadLogInterval()
            }
        }

        async uploadLogs() {
            var a, b = {sdk_version: Va, process_id: u.PROCESS_ID, payload: A(this.uploadLogUploadingList)};
            b = await Bb.post(this.url || (this.proxyServerURL ? l(a = "https://".concat(this.proxyServerURL, "/ls/?h=")).call(a, u.LOG_UPLOAD_SERVER, "&p=443&d=upload/v1") : "https://".concat(u.LOG_UPLOAD_SERVER, "/upload/v1")), b, {responseType: "text"});
            if ("OK" !== b.data) throw Error("unexpected upload log response: " + b.data);
            this.uploadLogUploadingList = []
        }

        uploadLogInterval() {
            if (0 !==
                this.uploadLogUploadingList.length || 0 !== this.uploadLogWaitingList.length) {
                var a;
                0 === this.uploadLogUploadingList.length && (this.uploadLogUploadingList = Ia(a = this.uploadLogWaitingList).call(a, 0, 10));
                this.uploadLogs().then(() => {
                    this.uploadErrorCount = 0;
                    0 < this.uploadLogWaitingList.length && window.setTimeout(() => this.uploadLogInterval(), 3E3)
                }).catch(a => {
                    this.uploadErrorCount += 1;
                    2 > this.uploadErrorCount ? window.setTimeout(() => this.uploadLogInterval(), 200) : window.setTimeout(() => this.uploadLogInterval(), 1E3)
                })
            }
        }
    };

    class p {
        constructor(a, b = "", c) {
            var e;
            this.name = "AgoraRTCException";
            this.code = a;
            this.message = l(e = "AgoraRTCError ".concat(this.code, ": ")).call(e, b);
            this.data = c
        }

        toString() {
            var a;
            return this.data ? l(a = "".concat(this.message, " data: ")).call(a, A(this.data)) : this.message
        }

        print() {
            return k.error(this.toString()), this
        }

        throw() {
            throw this.print(), this;
        }
    }

    var Tj, Ca;
    !function (a) {
        a.FREE = "free";
        a.UPLOADING = "uploading"
    }(Tj || (Tj = {}));
    (function (a) {
        a.NONE = "none";
        a.INIT = "init";
        a.CANPLAY = "canplay";
        a.PLAYING = "playing";
        a.PAUSED =
            "paused";
        a.SUSPEND = "suspend";
        a.STALLED = "stalled";
        a.WAITING = "waiting";
        a.ERROR = "error";
        a.DESTROYED = "destroyed";
        a.ABORT = "abort";
        a.ENDED = "ended";
        a.EMPTIED = "emptied"
    })(Ca || (Ca = {}));
    O({target: "Number", stat: !0}, {MAX_SAFE_INTEGER: 9007199254740991});
    O({target: "Number", stat: !0}, {MIN_SAFE_INTEGER: -9007199254740991});
    let Ln = {
        sid: "",
        lts: 0,
        success: null,
        cname: null,
        uid: null,
        peer: null,
        cid: null,
        elapse: null,
        extend: null,
        vid: 0
    };
    var Da, ma, Uj, E, D, fc, Mb, oc, fd, Ya, Ea, F, P, Sa, ra, Q, da, sb, U, I, jb;
    !function (a) {
        a.PUBLISH = "publish";
        a.SUBSCRIBE = "subscribe";
        a.SESSION_INIT = "session_init";
        a.JOIN_CHOOSE_SERVER = "join_choose_server";
        a.REQ_USER_ACCOUNT = "req_user_account";
        a.JOIN_GATEWAY = "join_gateway";
        a.STREAM_SWITCH = "stream_switch";
        a.REQUEST_PROXY_WORKER_MANAGER = "request_proxy_worker_manager";
        a.REQUEST_PROXY_APPCENTER = "request_proxy_appcenter";
        a.FIRST_VIDEO_RECEIVED = "first_video_received";
        a.FIRST_AUDIO_RECEIVED = "first_audio_received";
        a.FIRST_VIDEO_DECODE = "first_video_decode";
        a.FIRST_AUDIO_DECODE = "first_audio_decode";
        a.ON_ADD_AUDIO_STREAM =
            "on_add_audio_stream";
        a.ON_ADD_VIDEO_STREAM = "on_add_video_stream";
        a.ON_UPDATE_STREAM = "on_update_stream";
        a.ON_REMOVE_STREAM = "on_remove_stream";
        a.USER_ANALYTICS = "req_user_analytics"
    }(Da || (Da = {}));
    (function (a) {
        a.SESSION = "io.agora.pb.Wrtc.Session";
        a.JOIN_CHOOSE_SERVER = "io.agora.pb.Wrtc.JoinChooseServer";
        a.REQ_USER_ACCOUNT = "io.agora.pb.Wrtc.ReqUserAccount";
        a.JOIN_GATEWAT = "io.agora.pb.Wrtc.JoinGateway";
        a.PUBLISH = "io.agora.pb.Wrtc.Publish";
        a.SUBSCRIBE = "io.agora.pb.Wrtc.Subscribe";
        a.STREAM_SWITCH = "io.agora.pb.Wrtc.StreamSwitch";
        a.AUDIO_SENDING_STOPPED = "io.agora.pb.Wrtc.AudioSendingStopped";
        a.VIDEO_SENDING_STOPPED = "io.agora.pb.Wrtc.VideoSendingStopped";
        a.REQUEST_PROXY_APPCENTER = "io.agora.pb.Wrtc.RequestProxyAppCenter";
        a.REQUEST_PROXY_WORKER_MANAGER = "io.agora.pb.Wrtc.RequestProxyWorkerManager";
        a.API_INVOKE = "io.agora.pb.Wrtc.ApiInvoke";
        a.FIRST_VIDEO_RECEIVED = "io.agora.pb.Wrtc.FirstVideoReceived";
        a.FIRST_AUDIO_RECEIVED = "io.agora.pb.Wrtc.FirstAudioReceived";
        a.FIRST_VIDEO_DECODE = "io.agora.pb.Wrtc.FirstVideoDecode";
        a.FIRST_AUDIO_DECODE =
            "io.agora.pb.Wrtc.FirstAudioDecode";
        a.ON_ADD_AUDIO_STREAM = "io.agora.pb.Wrtc.OnAddAudioStream";
        a.ON_ADD_VIDEO_STREAM = "io.agora.pb.Wrtc.OnAddVideoStream";
        a.ON_UPDATE_STREAM = "io.agora.pb.Wrtc.OnUpdateStream";
        a.ON_REMOVE_STREAM = "io.agora.pb.Wrtc.OnRemoveStream";
        a.JOIN_CHANNEL_TIMEOUT = "io.agora.pb.Wrtc.JoinChannelTimeout";
        a.PEER_PUBLISH_STATUS = "io.agora.pb.Wrtc.PeerPublishStatus";
        a.WORKER_EVENT = "io.agora.pb.Wrtc.WorkerEvent";
        a.AP_WORKER_EVENT = "io.agora.pb.Wrtc.APWorkerEvent";
        a.JOIN_WEB_PROXY_AP = "io.agora.pb.Wrtc.JoinWebProxyAP";
        a.WEBSOCKET_QUIT = "io.agora.pb.Wrtc.WebSocketQuit";
        a.USER_ANALYTICS = "io.agora.pb.Wrtc.UserAnalytics"
    })(ma || (ma = {}));
    (function (a) {
        a[a.WORKER_EVENT = 156] = "WORKER_EVENT";
        a[a.AP_WORKER_EVENT = 160] = "AP_WORKER_EVENT"
    })(Uj || (Uj = {}));
    (function (a) {
        a.CREATE_CLIENT = "createClient";
        a.CHECK_SYSTEM_REQUIREMENTS = "checkSystemRequirements";
        a.CHECK_VIDEO_TRACK_IS_ACTIVE = "checkVideoTrackIsActive";
        a.CHECK_AUDIO_TRACK_IS_ACTIVE = "checkAudioTrackIsActive";
        a.CREATE_MIC_AUDIO_TRACK = "createMicrophoneAudioTrack";
        a.CREATE_CUSTOM_AUDIO_TRACK =
            "createCustomAudioTrack";
        a.CREATE_BUFFER_AUDIO_TRACK = "createBufferSourceAudioTrack";
        a.CREATE_CAM_VIDEO_TRACK = "createCameraVideoTrack";
        a.CREATE_CUSTOM_VIDEO_TRACK = "createCustomVideoTrack";
        a.CREATE_MIC_AND_CAM_TRACKS = "createMicrophoneAndCameraTracks";
        a.CREATE_SCREEN_VIDEO_TRACK = "createScreenVideoTrack";
        a.SET_ENCRYPTION_CONFIG = "Client.setEncryptionConfig";
        a.START_PROXY_SERVER = "Client.startProxyServer";
        a.STOP_PROXY_SERVER = "Client.stopProxyServer";
        a.SET_PROXY_SERVER = "Client.setProxyServer";
        a.SET_TURN_SERVER =
            "Client.setTurnServer";
        a.SET_CLIENT_ROLE = "Client.setClientRole";
        a.SET_LOW_STREAM_PARAMETER = "Client.setLowStreamParameter";
        a.ENABLE_DUAL_STREAM = "Client.enableDualStream";
        a.DISABLE_DUAL_STREAM = "Client.disableDualStream";
        a.JOIN = "Client.join";
        a.LEAVE = "Client.leave";
        a.PUBLISH = "Client.publish";
        a.UNPUBLISH = "Client.unpublish";
        a.SUBSCRIBE = "Client.subscribe";
        a.UNSUBSCRIBE = "Client.unsubscribe";
        a.RENEW_TOKEN = "Client.renewToken";
        a.SET_REMOTE_VIDEO_STREAM_TYPE = "Client.setRemoteVideoStreamType";
        a.SET_STREAM_FALLBACK_OPTION =
            "Client.setStreamFallbackOption";
        a.ENABLE_AUDIO_VOLUME_INDICATOR = "Client.enableAudioVolumeIndicator";
        a.SEND_CUSTOM_REPORT_MESSAGE = "Client.sendCustomReportMessage";
        a.ON_LIVE_STREAM_WARNING = "Client.onLiveStreamWarning";
        a.ON_LIVE_STREAM_ERROR = "Client.onLiveStreamingError";
        a.START_LIVE_STREAMING = "Client.startLiveStreaming";
        a.SET_LIVE_TRANSCODING = "Client.setLiveTranscoding";
        a.STOP_LIVE_STREAMING = "Client.stopLiveStreaming";
        a.ADD_INJECT_STREAM_URL = "Client.addInjectStreamUrl";
        a.REMOVE_INJECT_STREAM_URL = "Client.removeInjectStreamUrl";
        a.START_CHANNEL_MEDIA_RELAY = "Client.startChannelMediaRelay";
        a.UPDATE_CHANNEL_MEDIA_RELAY = "Client.updateChannelMediaRelay";
        a.STOP_CHANNEL_MEDIA_RELAY = "Client.stopChannelMediaRelay";
        a.REQUEST_CONFIG_DISTRIBUTE = "_config-distribute-request";
        a.SET_CONFIG_DISTRIBUTE = "_configDistribute";
        a.LOCAL_TRACK_SET_MUTED = "LocalTrack.setMute";
        a.LOCAL_AUDIO_TRACK_PLAY = "LocalAudioTrack.play";
        a.LOCAL_AUDIO_TRACK_PLAY_IN_ELEMENT = "LocalAudioTrack.playInElement";
        a.LOCAL_AUDIO_TRACK_STOP = "LocalAudioTrack.stop";
        a.LOCAL_AUDIO_TRACK_SET_VOLUME =
            "LocalAudioTrack.setVolume";
        a.MIC_AUDIO_TRACK_SET_DEVICE = "MicrophoneAudioTrack.setDevice";
        a.BUFFER_AUDIO_TRACK_START = "BufferSourceAudioTrack.startProcessAudioBuffer";
        a.BUFFER_AUDIO_TRACK_STOP = "BufferSourceAudioTrack.stopProcessAudioBuffer";
        a.BUFFER_AUDIO_TRACK_PAUSE = "BufferSourceAudioTrack.pauseProcessAudioBuffer";
        a.BUFFER_AUDIO_TRACK_RESUME = "BufferSourceAudioTrack.resumeProcessAudioBuffer";
        a.BUFFER_AUDIO_TRACK_SEEK = "BufferSourceAudioTrack.seekAudioBuffer";
        a.LOCAL_VIDEO_TRACK_PLAY = "LocalVideoTrack.play";
        a.LOCAL_VIDEO_TRACK_STOP = "LocalVideoTrack.stop";
        a.LOCAL_VIDEO_TRACK_BEAUTY = "LocalVideoTrack.setBeautyEffect";
        a.CAM_VIDEO_TRACK_SET_DEVICE = "CameraVideoTrack.setDevice";
        a.CAM_VIDEO_TRACK_SET_ENCODER_CONFIG = "CameraVideoTrack.setEncoderConfiguration";
        a.REMOTE_VIDEO_TRACK_PLAY = "RemoteVideoTrack.play";
        a.REMOTE_VIDEO_TRACK_STOP = "RemoteVideoTrack.stop";
        a.REMOTE_AUDIO_TRACK_PLAY = "RemoteAudioTrack.play";
        a.REMOTE_AUDIO_TRACK_STOP = "RemoteAudioTrack.stop";
        a.REMOTE_AUDIO_SET_VOLUME = "RemoteAudioTrack.setVolume";
        a.REMOTE_AUDIO_SET_OUTPUT_DEVICE = "RemoteAudioTrack.setOutputDevice";
        a.STREAM_TYPE_CHANGE = "streamTypeChange"
    })(E || (E = {}));
    (D || (D = {})).TRACER = "tracer";
    (function (a) {
        a.IDLE = "IDLE";
        a.INITING = "INITING";
        a.INITEND = "INITEND"
    })(fc || (fc = {}));
    (function (a) {
        a.STATE_CHANGE = "state_change";
        a.RECORDING_DEVICE_CHANGED = "recordingDeviceChanged";
        a.PLAYOUT_DEVICE_CHANGED = "playoutDeviceChanged";
        a.CAMERA_DEVICE_CHANGED = "cameraDeviceChanged"
    })(Mb || (Mb = {}));
    (function (a) {
        a[a.ACCESS_POINT = 101] = "ACCESS_POINT";
        a[a.UNILBS = 201] =
            "UNILBS";
        a[a.STRING_UID_ALLOCATOR = 901] = "STRING_UID_ALLOCATOR"
    })(oc || (oc = {}));
    (function (a) {
        a[a.IIIEGAL_APPID = 1] = "IIIEGAL_APPID";
        a[a.IIIEGAL_UID = 2] = "IIIEGAL_UID";
        a[a.INTERNAL_ERROR = 3] = "INTERNAL_ERROR"
    })(fd || (fd = {}));
    (function (a) {
        a[a.INVALID_VENDOR_KEY = 5] = "INVALID_VENDOR_KEY";
        a[a.INVALID_CHANNEL_NAME = 7] = "INVALID_CHANNEL_NAME";
        a[a.INTERNAL_ERROR = 8] = "INTERNAL_ERROR";
        a[a.NO_AUTHORIZED = 9] = "NO_AUTHORIZED";
        a[a.DYNAMIC_KEY_TIMEOUT = 10] = "DYNAMIC_KEY_TIMEOUT";
        a[a.NO_ACTIVE_STATUS = 11] = "NO_ACTIVE_STATUS";
        a[a.DYNAMIC_KEY_EXPIRED =
            13] = "DYNAMIC_KEY_EXPIRED";
        a[a.STATIC_USE_DYNAMIC_KEY = 14] = "STATIC_USE_DYNAMIC_KEY";
        a[a.DYNAMIC_USE_STATIC_KEY = 15] = "DYNAMIC_USE_STATIC_KEY";
        a[a.USER_OVERLOAD = 16] = "USER_OVERLOAD";
        a[a.FORBIDDEN_REGION = 18] = "FORBIDDEN_REGION";
        a[a.CANNOT_MEET_AREA_DEMAND = 19] = "CANNOT_MEET_AREA_DEMAND"
    })(Ya || (Ya = {}));
    (function (a) {
        a[a.NO_FLAG_SET = 100] = "NO_FLAG_SET";
        a[a.FLAG_SET_BUT_EMPTY = 101] = "FLAG_SET_BUT_EMPTY";
        a[a.INVALID_FALG_SET = 102] = "INVALID_FALG_SET";
        a[a.NO_SERVICE_AVAILABLE = 200] = "NO_SERVICE_AVAILABLE";
        a[a.NO_SERVICE_AVAILABLE_P2P =
            201] = "NO_SERVICE_AVAILABLE_P2P";
        a[a.NO_SERVICE_AVAILABLE_VOICE = 202] = "NO_SERVICE_AVAILABLE_VOICE";
        a[a.NO_SERVICE_AVAILABLE_WEBRTC = 203] = "NO_SERVICE_AVAILABLE_WEBRTC";
        a[a.NO_SERVICE_AVAILABLE_CDS = 204] = "NO_SERVICE_AVAILABLE_CDS";
        a[a.NO_SERVICE_AVAILABLE_CDN = 205] = "NO_SERVICE_AVAILABLE_CDN";
        a[a.NO_SERVICE_AVAILABLE_TDS = 206] = "NO_SERVICE_AVAILABLE_TDS";
        a[a.NO_SERVICE_AVAILABLE_REPORT = 207] = "NO_SERVICE_AVAILABLE_REPORT";
        a[a.NO_SERVICE_AVAILABLE_APP_CENTER = 208] = "NO_SERVICE_AVAILABLE_APP_CENTER";
        a[a.NO_SERVICE_AVAILABLE_ENV0 =
            209] = "NO_SERVICE_AVAILABLE_ENV0";
        a[a.NO_SERVICE_AVAILABLE_VOET = 210] = "NO_SERVICE_AVAILABLE_VOET";
        a[a.NO_SERVICE_AVAILABLE_STRING_UID = 211] = "NO_SERVICE_AVAILABLE_STRING_UID";
        a[a.NO_SERVICE_AVAILABLE_WEBRTC_UNILBS = 212] = "NO_SERVICE_AVAILABLE_WEBRTC_UNILBS";
        a[a.NO_SERVICE_AVAILABLE_UNILBS_FLV = 213] = "NO_SERVICE_AVAILABLE_UNILBS_FLV"
    })(Ea || (Ea = {}));
    (function (a) {
        a[a.K_TIMESTAMP_EXPIRED = 2] = "K_TIMESTAMP_EXPIRED";
        a[a.K_CHANNEL_PERMISSION_INVALID = 3] = "K_CHANNEL_PERMISSION_INVALID";
        a[a.K_CERTIFICATE_INVALID = 4] = "K_CERTIFICATE_INVALID";
        a[a.K_CHANNEL_NAME_EMPTY = 5] = "K_CHANNEL_NAME_EMPTY";
        a[a.K_CHANNEL_NOT_FOUND = 6] = "K_CHANNEL_NOT_FOUND";
        a[a.K_TICKET_INVALID = 7] = "K_TICKET_INVALID";
        a[a.K_CHANNEL_CONFLICTED = 8] = "K_CHANNEL_CONFLICTED";
        a[a.K_SERVICE_NOT_READY = 9] = "K_SERVICE_NOT_READY";
        a[a.K_SERVICE_TOO_HEAVY = 10] = "K_SERVICE_TOO_HEAVY";
        a[a.K_UID_BANNED = 14] = "K_UID_BANNED";
        a[a.K_IP_BANNED = 15] = "K_IP_BANNED";
        a[a.K_CHANNEL_BANNED = 16] = "K_CHANNEL_BANNED";
        a[a.WARN_NO_AVAILABLE_CHANNEL = 103] = "WARN_NO_AVAILABLE_CHANNEL";
        a[a.WARN_LOOKUP_CHANNEL_TIMEOUT =
            104] = "WARN_LOOKUP_CHANNEL_TIMEOUT";
        a[a.WARN_LOOKUP_CHANNEL_REJECTED = 105] = "WARN_LOOKUP_CHANNEL_REJECTED";
        a[a.WARN_OPEN_CHANNEL_TIMEOUT = 106] = "WARN_OPEN_CHANNEL_TIMEOUT";
        a[a.WARN_OPEN_CHANNEL_REJECTED = 107] = "WARN_OPEN_CHANNEL_REJECTED";
        a[a.WARN_REQUEST_DEFERRED = 108] = "WARN_REQUEST_DEFERRED";
        a[a.ERR_DYNAMIC_KEY_TIMEOUT = 109] = "ERR_DYNAMIC_KEY_TIMEOUT";
        a[a.ERR_NO_AUTHORIZED = 110] = "ERR_NO_AUTHORIZED";
        a[a.ERR_VOM_SERVICE_UNAVAILABLE = 111] = "ERR_VOM_SERVICE_UNAVAILABLE";
        a[a.ERR_NO_CHANNEL_AVAILABLE_CODE = 112] = "ERR_NO_CHANNEL_AVAILABLE_CODE";
        a[a.ERR_MASTER_VOCS_UNAVAILABLE = 114] = "ERR_MASTER_VOCS_UNAVAILABLE";
        a[a.ERR_INTERNAL_ERROR = 115] = "ERR_INTERNAL_ERROR";
        a[a.ERR_NO_ACTIVE_STATUS = 116] = "ERR_NO_ACTIVE_STATUS";
        a[a.ERR_INVALID_UID = 117] = "ERR_INVALID_UID";
        a[a.ERR_DYNAMIC_KEY_EXPIRED = 118] = "ERR_DYNAMIC_KEY_EXPIRED";
        a[a.ERR_STATIC_USE_DYANMIC_KE = 119] = "ERR_STATIC_USE_DYANMIC_KE";
        a[a.ERR_DYNAMIC_USE_STATIC_KE = 120] = "ERR_DYNAMIC_USE_STATIC_KE";
        a[a.ERR_NO_VOCS_AVAILABLE = 2E3] = "ERR_NO_VOCS_AVAILABLE";
        a[a.ERR_NO_VOS_AVAILABLE = 2001] = "ERR_NO_VOS_AVAILABLE";
        a[a.ERR_JOIN_CHANNEL_TIMEOUT = 2002] = "ERR_JOIN_CHANNEL_TIMEOUT";
        a[a.ERR_REPEAT_JOIN_CHANNEL = 2003] = "ERR_REPEAT_JOIN_CHANNEL";
        a[a.ERR_JOIN_BY_MULTI_IP = 2004] = "ERR_JOIN_BY_MULTI_IP";
        a[a.ERR_NOT_JOINED = 2011] = "ERR_NOT_JOINED";
        a[a.ERR_REPEAT_JOIN_REQUEST = 2012] = "ERR_REPEAT_JOIN_REQUEST";
        a[a.ERR_INVALID_VENDOR_KEY = 2013] = "ERR_INVALID_VENDOR_KEY";
        a[a.ERR_INVALID_CHANNEL_NAME = 2014] = "ERR_INVALID_CHANNEL_NAME";
        a[a.ERR_INVALID_STRINGUID = 2015] = "ERR_INVALID_STRINGUID";
        a[a.ERR_TOO_MANY_USERS = 2016] = "ERR_TOO_MANY_USERS";
        a[a.ERR_SET_CLIENT_ROLE_TIMEOUT = 2017] = "ERR_SET_CLIENT_ROLE_TIMEOUT";
        a[a.ERR_SET_CLIENT_ROLE_NO_PERMISSION = 2018] = "ERR_SET_CLIENT_ROLE_NO_PERMISSION";
        a[a.ERR_SET_CLIENT_ROLE_ALREADY_IN_USE = 2019] = "ERR_SET_CLIENT_ROLE_ALREADY_IN_USE";
        a[a.ERR_PUBLISH_REQUEST_INVALID = 2020] = "ERR_PUBLISH_REQUEST_INVALID";
        a[a.ERR_SUBSCRIBE_REQUEST_INVALID = 2021] = "ERR_SUBSCRIBE_REQUEST_INVALID";
        a[a.ERR_NOT_SUPPORTED_MESSAGE = 2022] = "ERR_NOT_SUPPORTED_MESSAGE";
        a[a.ERR_ILLEAGAL_PLUGIN = 2023] = "ERR_ILLEAGAL_PLUGIN";
        a[a.ERR_REJOIN_TOKEN_INVALID =
            2024] = "ERR_REJOIN_TOKEN_INVALID";
        a[a.ERR_REJOIN_USER_NOT_JOINED = 2025] = "ERR_REJOIN_USER_NOT_JOINED";
        a[a.ERR_INVALID_OPTIONAL_INFO = 2027] = "ERR_INVALID_OPTIONAL_INFO";
        a[a.ILLEGAL_AES_PASSWORD = 2028] = "ILLEGAL_AES_PASSWORD";
        a[a.ILLEGAL_CLIENT_ROLE_LEVEL = 2029] = "ILLEGAL_CLIENT_ROLE_LEVEL";
        a[a.ERR_TEST_RECOVER = 9E3] = "ERR_TEST_RECOVER";
        a[a.ERR_TEST_TRYNEXT = 9001] = "ERR_TEST_TRYNEXT";
        a[a.ERR_TEST_RETRY = 9002] = "ERR_TEST_RETRY"
    })(F || (F = {}));
    (function (a) {
        a.CONNECTION_STATE_CHANGE = "connection-state-change";
        a.MEDIA_RECONNECT_START =
            "media-reconnect-start";
        a.MEDIA_RECONNECT_END = "media-reconnect-end";
        a.IS_USING_CLOUD_PROXY = "is-using-cloud-proxy";
        a.USER_JOINED = "user-joined";
        a.USER_LEAVED = "user-left";
        a.USER_PUBLISHED = "user-published";
        a.USER_UNPUBLISHED = "user-unpublished";
        a.USER_INFO_UPDATED = "user-info-updated";
        a.CLIENT_BANNED = "client-banned";
        a.CHANNEL_MEDIA_RELAY_STATE = "channel-media-relay-state";
        a.CHANNEL_MEDIA_RELAY_EVENT = "channel-media-relay-event";
        a.VOLUME_INDICATOR = "volume-indicator";
        a.CRYPT_ERROR = "crypt-error";
        a.ON_TOKEN_PRIVILEGE_WILL_EXPIRE =
            "token-privilege-will-expire";
        a.ON_TOKEN_PRIVILEGE_DID_EXPIRE = "token-privilege-did-expire";
        a.NETWORK_QUALITY = "network-quality";
        a.STREAM_TYPE_CHANGED = "stream-type-changed";
        a.STREAM_FALLBACK = "stream-fallback";
        a.RECEIVE_METADATA = "receive-metadata";
        a.STREAM_MESSAGE = "stream-message";
        a.LIVE_STREAMING_ERROR = "live-streaming-error";
        a.LIVE_STREAMING_WARNING = "live-streaming-warning";
        a.INJECT_STREAM_STATUS = "stream-inject-status";
        a.EXCEPTION = "exception";
        a.ERROR = "error"
    })(P || (P = {}));
    (function (a) {
        a.NETWORK_ERROR =
            "NETWORK_ERROR";
        a.SERVER_ERROR = "SERVER_ERROR";
        a.MULTI_IP = "MULTI_IP";
        a.TIMEOUT = "TIMEOUT";
        a.OFFLINE = "OFFLINE";
        a.LEAVE = "LEAVE"
    })(Sa || (Sa = {}));
    (function (a) {
        a.CONNECTING = "connecting";
        a.CONNECTED = "connected";
        a.RECONNECTING = "reconnecting";
        a.CLOSED = "closed"
    })(ra || (ra = {}));
    (function (a) {
        a.WS_CONNECTED = "ws_connected";
        a.WS_RECONNECTING = "ws_reconnecting";
        a.WS_CLOSED = "ws_closed";
        a.ON_BINARY_DATA = "on_binary_data";
        a.REQUEST_RECOVER = "request_recover";
        a.REQUEST_JOIN_INFO = "request_join_info";
        a.REQUEST_REJOIN_INFO = "req_rejoin_info";
        a.IS_P2P_DISCONNECTED = "is_p2p_dis";
        a.DISCONNECT_P2P = "dis_p2p";
        a.NEED_RENEW_SESSION = "need-sid";
        a.REPORT_JOIN_GATEWAY = "report_join_gateway";
        a.REQUEST_TIMEOUT = "request_timeout";
        a.REQUEST_SUCCESS = "request_success"
    })(Q || (Q = {}));
    (function (a) {
        a.PING = "ping";
        a.PING_BACK = "ping_back";
        a.JOIN = "join_v2";
        a.REJOIN = "rejoin";
        a.LEAVE = "leave";
        a.SET_CLIENT_ROLE = "set_client_role";
        a.PUBLISH = "publish";
        a.UNPUBLISH = "unpublish";
        a.SUBSCRIBE = "subscribe";
        a.UNSUBSCRIBE = "unsubscribe";
        a.SUBSCRIBE_CHANGE = "subscribe_change";
        a.TRAFFIC_STATS =
            "traffic_stats";
        a.RENEW_TOKEN = "renew_token";
        a.SWITCH_VIDEO_STREAM = "switch_video_stream";
        a.SET_FALLBACK_OPTION = "set_fallback_option";
        a.GATEWAY_INFO = "gateway_info";
        a.CONTROL = "control";
        a.SEND_METADATA = "send_metadata";
        a.DATA_STREAM = "data_stream";
        a.PICK_SVC_LAYER = "pick_svc_layer"
    })(da || (da = {}));
    (function (a) {
        a.PUBLISH_STATS = "publish_stats";
        a.PUBLISH_RELATED_STATS = "publish_related_stats";
        a.SUBSCRIBE_STATS = "subscribe_stats";
        a.SUBSCRIBE_RELATED_STATS = "subscribe_related_stats"
    })(sb || (sb = {}));
    (function (a) {
        a.ON_USER_ONLINE =
            "on_user_online";
        a.ON_USER_OFFLINE = "on_user_offline";
        a.ON_STREAM_FALLBACK_UPDATE = "on_stream_fallback_update";
        a.ON_PUBLISH_STREAM = "on_publish_stream";
        a.ON_UPLINK_STATS = "on_uplink_stats";
        a.ON_P2P_LOST = "on_p2p_lost";
        a.ON_REMOVE_STREAM = "on_remove_stream";
        a.ON_ADD_AUDIO_STREAM = "on_add_audio_stream";
        a.ON_ADD_VIDEO_STREAM = "on_add_video_stream";
        a.ON_TOKEN_PRIVILEGE_WILL_EXPIRE = "on_token_privilege_will_expire";
        a.ON_TOKEN_PRIVILEGE_DID_EXPIRE = "on_token_privilege_did_expire";
        a.ON_USER_BANNED = "on_user_banned";
        a.ON_NOTIFICATION = "on_notification";
        a.ON_CRYPT_ERROR = "on_crypt_error";
        a.MUTE_AUDIO = "mute_audio";
        a.MUTE_VIDEO = "mute_video";
        a.UNMUTE_AUDIO = "unmute_audio";
        a.UNMUTE_VIDEO = "unmute_video";
        a.RECEIVE_METADATA = "receive_metadata";
        a.ON_DATA_STREAM = "on_data_stream";
        a.ENABLE_LOCAL_VIDEO = "enable_local_video";
        a.DISABLE_LOCAL_VIDEO = "disable_local_video";
        a.ENABLE_LOCAL_AUDIO = "enable_local_audio";
        a.DISABLE_LOCAL_AUDIO = "disable_local_audio"
    })(U || (U = {}));
    (function (a) {
        a.CONNECTION_STATE_CHANGE = "CONNECTION_STATE_CHANGE";
        a.NEED_ANSWER = "NEED_ANSWER";
        a.NEED_RENEGOTIATE = "NEED_RENEGOTIATE";
        a.P2P_LOST = "P2P_LOST";
        a.GATEWAY_P2P_LOST = "GATEWAY_P2P_LOST";
        a.NEED_UNPUB = "NEED_UNPUB";
        a.NEED_UNSUB = "NEED_UNSUB";
        a.NEED_UPLOAD = "NEED_UPLOAD";
        a.START_RECONNECT = "START_RECONNECT";
        a.END_RECONNECT = "END_RECONNECT";
        a.NEED_SIGNAL_RTT = "NEED_SIGNAL_RTT"
    })(I || (I = {}));
    (function (a) {
        a.AUDIO_SOURCE_STATE_CHANGE = "audio_source_state_change";
        a.RECEIVE_TRACK_BUFFER = "receive_track_buffer";
        a.ON_AUDIO_BUFFER = "on_audio_buffer"
    })(jb || (jb = {}));
    let he = {
        sendVolumeLevel: 0,
        sendBitrate: 0, sendBytes: 0, sendPackets: 0, sendPacketsLost: 0
    }, ie = {
        sendBytes: 0,
        sendBitrate: 0,
        sendPackets: 0,
        sendPacketsLost: 0,
        sendResolutionHeight: 0,
        sendResolutionWidth: 0,
        captureResolutionHeight: 0,
        captureResolutionWidth: 0,
        targetSendBitrate: 0,
        totalDuration: 0,
        totalFreezeTime: 0
    }, Jf = {
        transportDelay: 0,
        end2EndDelay: 0,
        receiveBitrate: 0,
        receiveLevel: 0,
        receiveBytes: 0,
        receiveDelay: 0,
        receivePackets: 0,
        receivePacketsLost: 0,
        totalDuration: 0,
        totalFreezeTime: 0,
        freezeRate: 0,
        packetLossRate: 0,
        publishDuration: -1
    }, Vj = {
        uplinkNetworkQuality: 0,
        downlinkNetworkQuality: 0
    }, Kf = {
        transportDelay: 0,
        end2EndDelay: 0,
        receiveBitrate: 0,
        receiveBytes: 0,
        receiveDelay: 0,
        receivePackets: 0,
        receivePacketsLost: 0,
        receiveResolutionHeight: 0,
        receiveResolutionWidth: 0,
        totalDuration: 0,
        totalFreezeTime: 0,
        freezeRate: 0,
        packetLossRate: 0,
        publishDuration: -1
    };
    var S, na;
    !function (a) {
        a.CONNECTED = "websocket:connected";
        a.RECONNECTING = "websocket:reconnecting";
        a.WILL_RECONNECT = "websocket:will_reconnect";
        a.CLOSED = "websocket:closed";
        a.FAILED = "websocket:failed";
        a.ON_MESSAGE = "websocket:on_message";
        a.REQUEST_NEW_URLS = "websocket:request_new_urls"
    }(S || (S = {}));
    (function (a) {
        a.TRANSCODE = "mix_streaming";
        a.RAW = "raw_streaming";
        a.INJECT = "inject_streaming"
    })(na || (na = {}));
    let Mn = {alpha: 1, height: 640, width: 360, x: 0, y: 0, zOrder: 0, audioChannel: 0},
        Lf = {x: 0, y: 0, width: 160, height: 160, zOrder: 255, alpha: 1}, Nn = {
            audioBitrate: 48,
            audioChannels: 1,
            audioSampleRate: 48E3,
            backgroundColor: 0,
            height: 360,
            lowLatency: !1,
            videoBitrate: 400,
            videoCodecProfile: 100,
            videoCodecType: 1,
            videoFrameRate: 15,
            videoGop: 30,
            width: 640,
            images: [],
            userConfigs: [],
            userConfigExtraInfo: ""
        }, On = {
            audioBitrate: 48,
            audioChannels: 2,
            audioVolume: 100,
            audioSampleRate: 48E3,
            height: 0,
            width: 0,
            videoBitrate: 400,
            videoFramerate: 15,
            videoGop: 30
        };
    var kb, Ec, ia, Wj, Fa, wa, L, tb, gd, hd;
    !function (a) {
        a.WARNING = "@live_uap-warning";
        a.ERROR = "@line_uap-error";
        a.PUBLISH_STREAM_STATUS = "@live_uap-publish-status";
        a.INJECT_STREAM_STATUS = "@live_uap-inject-status";
        a.WORKER_STATUS = "@live_uap-worker-status";
        a.REQUEST_NEW_ADDRESS = "@live_uap-request-address"
    }(kb || (kb = {}));
    (Ec || (Ec = {})).REQUEST_WORKER_MANAGER_LIST =
        "@live_req_worker_manager";
    (function (a) {
        a[a.LIVE_STREAM_RESPONSE_SUCCEED = 200] = "LIVE_STREAM_RESPONSE_SUCCEED";
        a[a.LIVE_STREAM_RESPONSE_ALREADY_EXISTS_STREAM = 454] = "LIVE_STREAM_RESPONSE_ALREADY_EXISTS_STREAM";
        a[a.LIVE_STREAM_RESPONSE_TRANSCODING_PARAMETER_ERROR = 450] = "LIVE_STREAM_RESPONSE_TRANSCODING_PARAMETER_ERROR";
        a[a.LIVE_STREAM_RESPONSE_BAD_STREAM = 451] = "LIVE_STREAM_RESPONSE_BAD_STREAM";
        a[a.LIVE_STREAM_RESPONSE_WM_PARAMETER_ERROR = 400] = "LIVE_STREAM_RESPONSE_WM_PARAMETER_ERROR";
        a[a.LIVE_STREAM_RESPONSE_WM_WORKER_NOT_EXIST =
            404] = "LIVE_STREAM_RESPONSE_WM_WORKER_NOT_EXIST";
        a[a.LIVE_STREAM_RESPONSE_NOT_AUTHORIZED = 456] = "LIVE_STREAM_RESPONSE_NOT_AUTHORIZED";
        a[a.LIVE_STREAM_RESPONSE_FAILED_LOAD_IMAGE = 457] = "LIVE_STREAM_RESPONSE_FAILED_LOAD_IMAGE";
        a[a.LIVE_STREAM_RESPONSE_REQUEST_TOO_OFTEN = 429] = "LIVE_STREAM_RESPONSE_REQUEST_TOO_OFTEN";
        a[a.LIVE_STREAM_RESPONSE_NOT_FOUND_PUBLISH = 452] = "LIVE_STREAM_RESPONSE_NOT_FOUND_PUBLISH";
        a[a.LIVE_STREAM_RESPONSE_NOT_SUPPORTED = 453] = "LIVE_STREAM_RESPONSE_NOT_SUPPORTED";
        a[a.LIVE_STREAM_RESPONSE_MAX_STREAM_NUM =
            455] = "LIVE_STREAM_RESPONSE_MAX_STREAM_NUM";
        a[a.LIVE_STREAM_RESPONSE_INTERNAL_SERVER_ERROR = 500] = "LIVE_STREAM_RESPONSE_INTERNAL_SERVER_ERROR";
        a[a.LIVE_STREAM_RESPONSE_WORKER_LOST = 501] = "LIVE_STREAM_RESPONSE_WORKER_LOST";
        a[a.LIVE_STREAM_RESPONSE_RESOURCE_LIMIT = 502] = "LIVE_STREAM_RESPONSE_RESOURCE_LIMIT";
        a[a.LIVE_STREAM_RESPONSE_WORKER_QUIT = 503] = "LIVE_STREAM_RESPONSE_WORKER_QUIT";
        a[a.ERROR_FAIL_SEND_MESSAGE = 504] = "ERROR_FAIL_SEND_MESSAGE";
        a[a.PUBLISH_STREAM_STATUS_ERROR_RTMP_HANDSHAKE = 30] = "PUBLISH_STREAM_STATUS_ERROR_RTMP_HANDSHAKE";
        a[a.PUBLISH_STREAM_STATUS_ERROR_RTMP_CONNECT = 31] = "PUBLISH_STREAM_STATUS_ERROR_RTMP_CONNECT";
        a[a.PUBLISH_STREAM_STATUS_ERROR_RTMP_PUBLISH = 32] = "PUBLISH_STREAM_STATUS_ERROR_RTMP_PUBLISH";
        a[a.PUBLISH_STREAM_STATUS_ERROR_PUBLISH_BROKEN = 33] = "PUBLISH_STREAM_STATUS_ERROR_PUBLISH_BROKEN"
    })(ia || (ia = {}));
    (function (a) {
        a.CONNECT_FAILED = "connect failed";
        a.CONNECT_TIMEOUT = "connect timeout";
        a.WS_DISCONNECTED = "websocket disconnected";
        a.REQUEST_TIMEOUT = "request timeout";
        a.REQUEST_FAILED = "request failed";
        a.WAIT_STATUS_TIMEOUT =
            "wait status timeout";
        a.WAIT_STATUS_ERROR = "wait status error";
        a.BAD_STATE = "bad state";
        a.WS_ABORT = "ws abort";
        a.AP_REQUEST_TIMEOUT = "AP request timeout";
        a.AP_JSON_PARSE_ERROR = "AP json parse error";
        a.AP_REQUEST_ERROR = "AP request error";
        a.AP_REQUEST_ABORT = "AP request abort"
    })(Wj || (Wj = {}));
    (function (a) {
        a[a.SetSdkProfile = 0] = "SetSdkProfile";
        a[a.SetSourceChannel = 1] = "SetSourceChannel";
        a[a.SetSourceUserId = 2] = "SetSourceUserId";
        a[a.SetDestChannel = 3] = "SetDestChannel";
        a[a.StartPacketTransfer = 4] = "StartPacketTransfer";
        a[a.StopPacketTransfer = 5] = "StopPacketTransfer";
        a[a.UpdateDestChannel = 6] = "UpdateDestChannel";
        a[a.Reconnect = 7] = "Reconnect";
        a[a.SetVideoProfile = 8] = "SetVideoProfile"
    })(Fa || (Fa = {}));
    (function (a) {
        a.DISCONNECT = "disconnect";
        a.CONNECTION_STATE_CHANGE = "connection-state-change";
        a.NETWORK_QUALITY = "network-quality";
        a.STREAM_TYPE_CHANGE = "stream-type-change";
        a.IS_P2P_DISCONNECTED = "is-p2p-dis";
        a.DISCONNECT_P2P = "dis-p2p";
        a.REQUEST_NEW_GATEWAY_LIST = "req-gate-url";
        a.NEED_RENEW_SESSION = "need-sid"
    })(wa || (wa = {}));
    (function (a) {
        a.NEED_RENEGOTIATE =
            "@need_renegotiate";
        a.NEED_REPLACE_TRACK = "@need_replace_track";
        a.NEED_CLOSE = "@need_close";
        a.NEED_ADD_TRACK = "@need_add_track";
        a.NEED_REMOVE_TRACK = "@need_remove_track";
        a.NEED_SESSION_ID = "@need_sid";
        a.SET_OPTIMIZATION_MODE = "@set_optimization_mode";
        a.GET_STATS = "@get_stats";
        a.GET_LOW_VIDEO_TRACK = "@get_low_video_track";
        a.NEED_RESET_REMOTE_SDP = "@need_reset_remote_sdp"
    })(L || (L = {}));
    (function (a) {
        a.SCREEN_TRACK = "screen_track";
        a.LOW_STREAM = "low_stream"
    })(tb || (tb = {}));
    (function (a) {
        a.SOURCE_STATE_CHANGE = "source-state-change";
        a.TRACK_ENDED = "track-ended";
        a.BEAUTY_EFFECT_OVERLOAD = "beauty-effect-overload"
    })(gd || (gd = {}));
    (hd || (hd = {})).FIRST_FRAME_DECODED = "first-frame-decoded";
    let Xj = "AFRICA ASIA CHINA EUROPE GLOBAL INDIA JAPAN NORTH_AMERICA OCEANIA OVERSEA SOUTH_AMERICA".split(" ");
    var xa;
    !function (a) {
        a.CHINA = "CN";
        a.ASIA = "AS";
        a.NORTH_AMERICA = "NA";
        a.EUROPE = "EU";
        a.JAPAN = "JP";
        a.INDIA = "IN";
        a.OCEANIA = "OC";
        a.SOUTH_AMERICA = "SA";
        a.AFRICA = "AF";
        a.OVERSEA = "OVERSEA";
        a.GLOBAL = "GLOBAL"
    }(xa || (xa = {}));
    let Yj = {
        CHINA: {}, ASIA: {
            CODE: xa.ASIA,
            WEBCS_DOMAIN: ["ap-web-1-asia.agora.io"],
            WEBCS_DOMAIN_BACKUP_LIST: ["ap-web-2-asia.agora.io"],
            PROXY_CS: ["proxy-ap-web-asia.agora.io"],
            CDS_AP: ["cds-ap-web-asia.agora.io", "cds-ap-web-asia2.agora.io"],
            ACCOUNT_REGISTER: ["sua-ap-web-asia.agora.io", "sua-ap-web-asia2.agora.io"],
            UAP_AP: ["uap-ap-web-asia.agora.io", "uap-ap-web-asia2.agora.io"],
            EVENT_REPORT_DOMAIN: ["statscollector-1-asia.agora.io"],
            EVENT_REPORT_BACKUP_DOMAIN: ["statscollector-2-asia.agora.io"],
            LOG_UPLOAD_SERVER: ["logservice-asia.agora.io"],
            PROXY_SERVER_TYPE3: ["southeast-asia.webrtc-cloud-proxy.sd-rtn.com"]
        },
        NORTH_AMERICA: {
            CODE: xa.NORTH_AMERICA,
            WEBCS_DOMAIN: ["ap-web-1-north-america.agora.io"],
            WEBCS_DOMAIN_BACKUP_LIST: ["ap-web-2-north-america.agora.io"],
            PROXY_CS: ["proxy-ap-web-america.agora.io"],
            CDS_AP: ["cds-ap-web-america.agora.io", "cds-ap-web-america2.agora.io"],
            ACCOUNT_REGISTER: ["sua-ap-web-america.agora.io", "sua-ap-web-america2.agora.io"],
            UAP_AP: ["uap-ap-web-america.agora.io", "uap-ap-web-america2.agora.io"],
            EVENT_REPORT_DOMAIN: ["statscollector-1-north-america.agora.io"],
            EVENT_REPORT_BACKUP_DOMAIN: ["statscollector-2-north-america.agora.io"],
            LOG_UPLOAD_SERVER: ["logservice-north-america.agora.io"],
            PROXY_SERVER_TYPE3: ["east-usa.webrtc-cloud-proxy.sd-rtn.com"]
        }, EUROPE: {
            CODE: xa.EUROPE,
            WEBCS_DOMAIN: ["ap-web-1-europe.agora.io"],
            WEBCS_DOMAIN_BACKUP_LIST: ["ap-web-2-europe.agora.io"],
            PROXY_CS: ["proxy-ap-web-europe.agora.io"],
            CDS_AP: ["cds-ap-web-europe.agora.io", "cds-ap-web-europe2.agora.io"],
            ACCOUNT_REGISTER: ["sua-ap-web-europe.agora.io", "sua-ap-web-europe.agora.io"],
            UAP_AP: ["uap-ap-web-europe.agora.io", "uap-ap-web-europe2.agora.io"],
            EVENT_REPORT_DOMAIN: ["statscollector-1-europe.agora.io"],
            EVENT_REPORT_BACKUP_DOMAIN: ["statscollector-2-europe.agora.io"],
            LOG_UPLOAD_SERVER: ["logservice-europe.agora.io"],
            PROXY_SERVER_TYPE3: ["europe.webrtc-cloud-proxy.sd-rtn.com"]
        }, JAPAN: {
            CODE: xa.JAPAN,
            WEBCS_DOMAIN: ["ap-web-1-japan.agora.io"],
            WEBCS_DOMAIN_BACKUP_LIST: ["ap-web-2-japan.agora.io"],
            PROXY_CS: ["proxy-ap-web-japan.agora.io"],
            CDS_AP: ["cds-ap-web-japan.agora.io", "cds-ap-web-japan2.agora.io"],
            ACCOUNT_REGISTER: ["sua-ap-web-japan.agora.io", "sua-ap-web-japan2.agora.io"],
            UAP_AP: ["uap-ap-web-japan.agora.io",
                "\tuap-ap-web-japan2.agora.io"],
            EVENT_REPORT_DOMAIN: ["statscollector-1-japan.agora.io"],
            EVENT_REPORT_BACKUP_DOMAIN: ["statscollector-2-japan.agora.io"],
            LOG_UPLOAD_SERVER: ["logservice-japan.agora.io"],
            PROXY_SERVER_TYPE3: ["japan.webrtc-cloud-proxy.sd-rtn.com"]
        }, INDIA: {
            CODE: xa.INDIA,
            WEBCS_DOMAIN: ["ap-web-1-india.agora.io"],
            WEBCS_DOMAIN_BACKUP_LIST: ["ap-web-2-india.agora.io"],
            PROXY_CS: ["proxy-ap-web-india.agora.io"],
            CDS_AP: ["cds-ap-web-india.agora.io", "cds-ap-web-india2.agora.io"],
            ACCOUNT_REGISTER: ["sua-ap-web-india.agora.io",
                "sua-ap-web-india2.agora.io"],
            UAP_AP: ["uap-ap-web-india.agora.io", "uap-ap-web-india2.agora.io"],
            EVENT_REPORT_DOMAIN: ["statscollector-1-india.agora.io"],
            EVENT_REPORT_BACKUP_DOMAIN: ["statscollector-2-india.agora.io"],
            LOG_UPLOAD_SERVER: ["logservice-india.agora.io"],
            PROXY_SERVER_TYPE3: ["india.webrtc-cloud-proxy.sd-rtn.com"]
        }, OVERSEA: {
            CODE: xa.OVERSEA,
            WEBCS_DOMAIN: ["ap-web-1-oversea.agora.io"],
            WEBCS_DOMAIN_BACKUP_LIST: ["ap-web-2-oversea.agora.io"],
            PROXY_CS: ["proxy-ap-web-oversea.agora.io"],
            CDS_AP: ["cds-ap-web-oversea.agora.io"],
            ACCOUNT_REGISTER: ["sua-ap-web-oversea.agora.io"],
            UAP_AP: ["uap-ap-web-oversea.agora.io"],
            EVENT_REPORT_DOMAIN: ["statscollector-1-oversea.agora.io"],
            EVENT_REPORT_BACKUP_DOMAIN: ["statscollector-2-oversea.agora.io"],
            LOG_UPLOAD_SERVER: ["logservice-oversea.agora.io"],
            PROXY_SERVER_TYPE3: ["webrtc-cloud-proxy.agora.io"]
        }, GLOBAL: {
            CODE: xa.GLOBAL,
            WEBCS_DOMAIN: ["webrtc2-ap-web-1.agora.io"],
            WEBCS_DOMAIN_BACKUP_LIST: ["webrtc2-ap-web-3.agora.io"],
            PROXY_CS: ["ap-proxy-1.agora.io", "ap-proxy-2.agora.io"],
            CDS_AP: ["cds-ap-web-1.agora.io",
                "cds-ap-web-3.agora.io"],
            ACCOUNT_REGISTER: ["sua-ap-web-1.agora.io", "sua-ap-web-3.agora.io"],
            UAP_AP: ["uap-ap-web-1.agora.io", "uap-ap-web-3.agora.io"],
            EVENT_REPORT_DOMAIN: ["statscollector-1.agora.io"],
            EVENT_REPORT_BACKUP_DOMAIN: ["statscollector-2.agora.io"],
            LOG_UPLOAD_SERVER: ["logservice.agora.io"],
            PROXY_SERVER_TYPE3: ["webrtc-cloud-proxy.sd-rtn.com"]
        }, OCEANIA: {
            CODE: xa.OCEANIA,
            WEBCS_DOMAIN: ["ap-web-1-oceania.agora.io"],
            WEBCS_DOMAIN_BACKUP_LIST: ["ap-web-2-oceania.agora.io"],
            PROXY_CS: ["proxy-ap-web-oceania.agora.io"],
            CDS_AP: ["cds-ap-web-oceania.agora.io", "cds-ap-web-oceania2.agora.io"],
            ACCOUNT_REGISTER: ["sua-ap-web-oceania.agora.io", "sua-ap-web-oceania2.agora.io"],
            UAP_AP: ["uap-ap-web-oceania.agora.io", "uap-ap-web-oceania2.agora.io"],
            EVENT_REPORT_DOMAIN: ["statscollector-1-oceania.agora.io"],
            EVENT_REPORT_BACKUP_DOMAIN: ["statscollector-2-oceania.agora.io"],
            LOG_UPLOAD_SERVER: ["logservice-oceania.agora.io"],
            PROXY_SERVER_TYPE3: ["oceania.webrtc-cloud-proxy.sd-rtn.com"]
        }, SOUTH_AMERICA: {
            CODE: xa.SOUTH_AMERICA,
            WEBCS_DOMAIN: ["ap-web-1-south-america.agora.io"],
            WEBCS_DOMAIN_BACKUP_LIST: ["ap-web-2-south-america.agora.io"],
            PROXY_CS: ["proxy-ap-web-south-america.agora.io"],
            CDS_AP: ["cds-ap-web-south-america.agora.io", "cds-ap-web-south-america2.agora.io"],
            ACCOUNT_REGISTER: ["sua-ap-web-south-america.agora.io", "sua-ap-web-south-america2.agora.io"],
            UAP_AP: ["uap-ap-web-south-america.agora.io", "uap-ap-web-south-america2.agora.io"],
            EVENT_REPORT_DOMAIN: ["statscollector-1-south-america.agora.io"],
            EVENT_REPORT_BACKUP_DOMAIN: ["statscollector-2-south-america.agora.io"],
            LOG_UPLOAD_SERVER: ["logservice-south-america.agora.io"],
            PROXY_SERVER_TYPE3: ["south-america.webrtc-cloud-proxy.sd-rtn.com"]
        }, AFRICA: {
            CODE: xa.AFRICA,
            WEBCS_DOMAIN: ["ap-web-1-africa.agora.io"],
            WEBCS_DOMAIN_BACKUP_LIST: ["ap-web-2-africa.agora.io"],
            PROXY_CS: ["proxy-ap-web-africa.agora.io"],
            CDS_AP: ["cds-ap-web-africa.agora.io", "cds-ap-web-africa2.agora.io"],
            ACCOUNT_REGISTER: ["sua-ap-web-africa.agora.io", "sua-ap-web-africa2.agora.io"],
            UAP_AP: ["uap-ap-web-africa.agora.io", "uap-ap-web-africa2.agora.io"],
            EVENT_REPORT_DOMAIN: ["statscollector-1-africa.agora.io"],
            EVENT_REPORT_BACKUP_DOMAIN: ["statscollector-2-africa.agora.io"],
            LOG_UPLOAD_SERVER: ["logservice-south-africa.agora.io"],
            PROXY_SERVER_TYPE3: ["africa.webrtc-cloud-proxy.sd-rtn.com"]
        }
    };
    var id;
    Ff && (Yj.CHINA = {
        CODE: xa.CHINA,
        WEBCS_DOMAIN: ["webrtc2-2.ap.sd-rtn.com"],
        WEBCS_DOMAIN_BACKUP_LIST: ["webrtc2-4.ap.sd-rtn.com"],
        PROXY_CS: ["proxy-web.ap.sd-rtn.com"],
        CDS_AP: ["cds-web-2.ap.sd-rtn.com", "cds-web-4.ap.sd-rtn.com"],
        ACCOUNT_REGISTER: ["sua-web-2.ap.sd-rtn.com", "sua-web-4.ap.sd-rtn.com"],
        UAP_AP: ["uap-web-2.ap.sd-rtn.com",
            "uap-web-4.ap.sd-rtn.com"],
        EVENT_REPORT_DOMAIN: ["web-3.statscollector.sd-rtn.com"],
        EVENT_REPORT_BACKUP_DOMAIN: ["web-4.statscollector.sd-rtn.com"],
        LOG_UPLOAD_SERVER: ["logservice-china.agora.io"],
        PROXY_SERVER_TYPE3: ["east-cn.webrtc-cloud-proxy.sd-rtn.com"]
    });
    (id || (id = {})).UPDATE_BITRATE_LIMIT = "update_bitrate_limit";
    let ca = {
        getDisplayMedia: !1,
        getStreamFromExtension: !1,
        supportUnifiedPlan: !1,
        supportMinBitrate: !1,
        supportSetRtpSenderParameters: !1,
        supportDualStream: !0,
        webAudioMediaStreamDest: !1,
        supportReplaceTrack: !1,
        supportWebGL: !1,
        webAudioWithAEC: !1,
        supportRequestFrame: !1,
        supportShareAudio: !1,
        supportDualStreamEncoding: !1
    };
    O({target: "Object", stat: !0, forced: !la, sham: !la}, {defineProperties: oi});
    var Pa = wb(function (a) {
        var b = ha.Object;
        a = a.exports = function (a, e) {
            return b.defineProperties(a, e)
        };
        b.defineProperties.sham && (a.sham = !0)
    }), Pn = Td.concat("length", "prototype"), Mf = {
        f: Object.getOwnPropertyNames || function (a) {
            return ni(a, Pn)
        }
    }, Qn = Jb("Reflect", "ownKeys") || function (a) {
        var b = Mf.f(Wa(a)), c = Yc.f;
        return c ? b.concat(c(a)) :
            b
    };
    O({target: "Object", stat: !0, sham: !la}, {
        getOwnPropertyDescriptors: function (a) {
            var b, c;
            a = hb(a);
            for (var e = tc, g = Qn(a), h = {}, m = 0; g.length > m;) void 0 !== (c = e(a, b = g[m++])) && dc(h, b, c);
            return h
        }
    });
    var fa = ha.Object.getOwnPropertyDescriptors, Zj = Mf.f, Rn = {}.toString,
        ak = "object" == typeof window && window && Object.getOwnPropertyNames ? Object.getOwnPropertyNames(window) : [],
        bk = function (a) {
            if (ak && "[object Window]" == Rn.call(a)) try {
                var b = Zj(a)
            } catch (c) {
                b = ak.slice()
            } else b = Zj(hb(a));
            return b
        }, ck = {f: va}, Sn = ib.f, Za = Qd("hidden"),
        dk = va("toPrimitive"), Tn = bb.set, ek = bb.getterFor("Symbol"), ub = Object.prototype, cb = M.Symbol,
        jd = Jb("JSON", "stringify"), fk = tc, Tb = ib.f, gk = bk, Un = Id, Nb = Kb("symbols"), kd = Kb("op-symbols"),
        Nf = Kb("string-to-symbol-registry"), Of = Kb("symbol-to-string-registry"), Vn = Kb("wks"), Pf = M.QObject,
        Qf = !Pf || !Pf.prototype || !Pf.prototype.findChild, Rf = la && qa(function () {
            return 7 != cc(Tb({}, "a", {
                get: function () {
                    return Tb(this, "a", {value: 7}).a
                }
            })).a
        }) ? function (a, b, c) {
            var e = fk(ub, b);
            e && delete ub[b];
            Tb(a, b, c);
            e && a !== ub && Tb(ub, b, e)
        } :
        Tb, hk = function (a, b) {
            var c = Nb[a] = cc(cb.prototype);
            return Tn(c, {type: "Symbol", tag: a, description: b}), la || (c.description = b), c
        }, Sf = Db && "symbol" == typeof cb.iterator ? function (a) {
            return "symbol" == typeof a
        } : function (a) {
            return Object(a) instanceof cb
        }, je = function (a, b, c) {
            a === ub && je(kd, b, c);
            Wa(a);
            b = rc(b, !0);
            return Wa(c), T(Nb, b) ? (c.enumerable ? (T(a, Za) && a[Za][b] && (a[Za][b] = !1), c = cc(c, {enumerable: Yb(0, !1)})) : (T(a, Za) || Tb(a, Za, Yb(1, {})), a[Za][b] = !0), Rf(a, b, c)) : Tb(a, b, c)
        }, jk = function (a, b) {
            Wa(a);
            var c = hb(b);
            b = Sb(c).concat(Tf(c));
            return wc(b, function (b) {
                la && !ik.call(c, b) || je(a, b, c[b])
            }), a
        }, ik = function (a) {
            a = rc(a, !0);
            var b = Un.call(this, a);
            return !(this === ub && T(Nb, a) && !T(kd, a)) && (!(b || !T(this, a) || !T(Nb, a) || T(this, Za) && this[Za][a]) || b)
        }, kk = function (a, b) {
            a = hb(a);
            b = rc(b, !0);
            if (a !== ub || !T(Nb, b) || T(kd, b)) {
                var c = fk(a, b);
                return !c || !T(Nb, b) || T(a, Za) && a[Za][b] || (c.enumerable = !0), c
            }
        }, lk = function (a) {
            a = gk(hb(a));
            var b = [];
            return wc(a, function (a) {
                T(Nb, a) || T(vc, a) || b.push(a)
            }), b
        }, Tf = function (a) {
            var b = a === ub;
            a = gk(b ? kd : hb(a));
            var c = [];
            return wc(a,
                function (a) {
                    !T(Nb, a) || b && !T(ub, a) || c.push(Nb[a])
                }), c
        };
    if (Db || (cf((cb = function () {
        if (this instanceof cb) throw TypeError("Symbol is not a constructor");
        var a = arguments.length && void 0 !== arguments[0] ? String(arguments[0]) : void 0, b = Md(a),
            c = function (a) {
                this === ub && c.call(kd, a);
                T(this, Za) && T(this[Za], b) && (this[Za][b] = !1);
                Rf(this, b, Yb(1, a))
            };
        return la && Qf && Rf(ub, b, {configurable: !0, set: c}), hk(b, a)
    }).prototype, "toString", function () {
        return ek(this).tag
    }), Id = ik, ib.f = je, tc = kk, Mf.f = bk = lk, Yc.f = Tf, la && Tb(cb.prototype,
        "description", {
            configurable: !0, get: function () {
                return ek(this).description
            }
        })), di || (ck.f = function (a) {
        return hk(va(a), a)
    }), O({global: !0, wrap: !0, forced: !Db, sham: !Db}, {Symbol: cb}), wc(Sb(Vn), function (a) {
        var b = ha.Symbol || (ha.Symbol = {});
        T(b, a) || Sn(b, a, {value: ck.f(a)});
        !0
    }), O({target: "Symbol", stat: !0, forced: !Db}, {
        for: function (a) {
            a = String(a);
            if (T(Nf, a)) return Nf[a];
            var b = cb(a);
            return Nf[a] = b, Of[b] = a, b
        }, keyFor: function (a) {
            if (!Sf(a)) throw TypeError(a + " is not a symbol");
            if (T(Of, a)) return Of[a]
        }, useSetter: function () {
            Qf =
                !0
        }, useSimple: function () {
            Qf = !1
        }
    }), O({target: "Object", stat: !0, forced: !Db, sham: !la}, {
        create: function (a, b) {
            return void 0 === b ? cc(a) : jk(cc(a), b)
        }, defineProperty: je, defineProperties: jk, getOwnPropertyDescriptor: kk
    }), O({target: "Object", stat: !0, forced: !Db}, {
        getOwnPropertyNames: lk,
        getOwnPropertySymbols: Tf
    }), O({
        target: "Object", stat: !0, forced: qa(function () {
            Yc.f(1)
        })
    }, {
        getOwnPropertySymbols: function (a) {
            return Yc.f(qb(a))
        }
    }), jd) {
        var Wn = !Db || qa(function () {
            var a = cb();
            return "[null]" != jd([a]) || "{}" != jd({a}) || "{}" !=
                jd(Object(a))
        });
        O({target: "JSON", stat: !0, forced: Wn}, {
            stringify: function (a, b, c) {
                for (var e, g = [a], h = 1; arguments.length > h;) g.push(arguments[h++]);
                if (e = b, (Ba(b) || void 0 !== a) && !Sf(a)) return ac(b) || (b = function (a, b) {
                    if ("function" == typeof e && (b = e.call(this, a, b)), !Sf(b)) return b
                }), g[1] = b, jd.apply(null, g)
            }
        })
    }
    cb.prototype[dk] || ob(cb.prototype, dk, cb.prototype.valueOf);
    Vc(cb, "Symbol");
    vc[Za] = !0;
    var ea = ha.Object.getOwnPropertySymbols, Oa = function (a, b, c) {
        return b in a ? Ji(a, b, {
            value: c, enumerable: !0, configurable: !0,
            writable: !0
        }) : a[b] = c, a
    }, Xn = za("Array").values, mk = Array.prototype, Yn = {DOMTokenList: !0, NodeList: !0}, gc = function (a) {
        var b = a.values;
        return a === mk || a instanceof Array && b === mk.values || Yn.hasOwnProperty(Pd(a)) ? Xn : b
    }, Zn = !Xi(function (a) {
        Array.from(a)
    });
    O({target: "Array", stat: !0, forced: Zn}, {
        from: function (a) {
            var b = qb(a);
            var c = "function" == typeof this ? this : Array;
            var e = arguments.length;
            var g = 1 < e ? arguments[1] : void 0, h = void 0 !== g, m = 0;
            var k = gi(b);
            if (h && (g = $b(g, 2 < e ? arguments[2] : void 0, 2)), null == k || c == Array && ei(k)) for (c =
                                                                                                               new c(e = pb(b.length)); e > m; m++) dc(c, m, h ? g(b[m], m) : b[m]); else for (e = (b = k.call(b)).next, c = new c; !(k = e.call(b)).done; m++) dc(c, m, h ? hi(b, g, [k.value, m], !0) : k.value);
            return c.length = m, c
        }
    });
    var Ib = ha.Array.from;
    O({target: "Date", stat: !0}, {
        now: function () {
            return (new Date).getTime()
        }
    });
    var x = ha.Date.now;

    class Ta {
        constructor() {
            this._events = {};
            this.addListener = this.on
        }

        getListeners(a) {
            var b;
            return this._events[a] ? C(b = this._events[a]).call(b, a => a.listener) : []
        }

        on(a, b) {
            this._events[a] || (this._events[a] = []);
            a = this._events[a];
            -1 === this._indexOfListener(a, b) && a.push({listener: b, once: !1})
        }

        once(a, b) {
            this._events[a] || (this._events[a] = []);
            a = this._events[a];
            -1 === this._indexOfListener(a, b) && a.push({listener: b, once: !0})
        }

        off(a, b) {
            if (this._events[a]) {
                var c = this._events[a];
                b = this._indexOfListener(c, b);
                -1 !== b && Ia(c).call(c, b, 1);
                0 === this._events[a].length && delete this._events[a]
            }
        }

        removeAllListeners(a) {
            a ? delete this._events[a] : this._events = {}
        }

        emit(a, ...b) {
            var c;
            this._events[a] || (this._events[a] = []);
            let e = C(c = this._events[a]).call(c,
                a => a);
            for (c = 0; c < e.length; c += 1) {
                let g = e[c];
                g.once && this.off(a, g.listener);
                g.listener.apply(this, b || [])
            }
        }

        _indexOfListener(a, b) {
            let c = a.length;
            for (; c--;) if (a[c].listener === b) return c;
            return -1
        }
    }

    class $n extends Ta {
        constructor() {
            super(...arguments);
            this.resultStorage = new ba
        }

        setLocalAudioStats(a, b, c) {
            this.record("AUDIO_INPUT_LEVEL_TOO_LOW", a, this.checkAudioInputLevel(c));
            this.record("SEND_AUDIO_BITRATE_TOO_LOW", a, this.checkSendAudioBitrate(c))
        }

        setLocalVideoStats(a, b, c) {
            this.record("SEND_VIDEO_BITRATE_TOO_LOW",
                a, this.checkSendVideoBitrate(c));
            this.record("FRAMERATE_INPUT_TOO_LOW", a, this.checkFramerateInput(c, b));
            this.record("FRAMERATE_SENT_TOO_LOW", a, this.checkFramerateSent(c))
        }

        setRemoteAudioStats(a, b) {
            a = a.getUserId();
            this.record("AUDIO_OUTPUT_LEVEL_TOO_LOW", a, this.checkAudioOutputLevel(b))
        }

        setRemoteVideoStats(a, b) {
            a = a.getUserId();
            this.record("RECV_VIDEO_DECODE_FAILED", a, this.checkVideoDecode(b))
        }

        record(a, b, c) {
            this.resultStorage.has(a) || this.resultStorage.set(a, {result: [], isPrevNormal: !0});
            let e = this.resultStorage.get(a);
            if (e && (e.result.push(c), 5 <= e.result.length)) {
                var g;
                c = ya(g = e.result).call(g, !0);
                e.isPrevNormal && !c && this.emit("exception", nk[a], a, b);
                !e.isPrevNormal && c && this.emit("exception", nk[a] + 2E3, a + "_RECOVER", b);
                e.isPrevNormal = c;
                e.result = []
            }
        }

        checkAudioOutputLevel(a) {
            return !(0 < a.receiveBitrate && 0 === a.receiveLevel)
        }

        checkAudioInputLevel(a) {
            return 0 !== a.sendVolumeLevel
        }

        checkFramerateInput(a, b) {
            let c = null;
            b._encoderConfig && b._encoderConfig.frameRate && (c = fb(b._encoderConfig.frameRate));
            a = a.captureFrameRate;
            return !c ||
                !a || !(10 < c && 5 > a || 10 > c && 5 <= c && 1 >= a)
        }

        checkFramerateSent(a) {
            return !(a.captureFrameRate && a.sendFrameRate && 5 < a.captureFrameRate && 1 >= a.sendFrameRate)
        }

        checkSendVideoBitrate(a) {
            return 0 !== a.sendBitrate
        }

        checkSendAudioBitrate(a) {
            return 0 !== a.sendBitrate
        }

        checkVideoDecode(a) {
            return 0 === a.receiveBitrate || 0 !== a.decodeFrameRate
        }
    }

    let nk = {
        FRAMERATE_INPUT_TOO_LOW: 1001,
        FRAMERATE_SENT_TOO_LOW: 1002,
        SEND_VIDEO_BITRATE_TOO_LOW: 1003,
        RECV_VIDEO_DECODE_FAILED: 1005,
        AUDIO_INPUT_LEVEL_TOO_LOW: 2001,
        AUDIO_OUTPUT_LEVEL_TOO_LOW: 2002,
        SEND_AUDIO_BITRATE_TOO_LOW: 2003
    };

    class ld {
        constructor(a) {
            this.localConnectionsMap = new ba;
            this.remoteConnectionsMap = new ba;
            this.trafficStatsPeerList = [];
            this.updateStats = () => {
                var a, c;
                q(a = this.remoteConnectionsMap).call(a, a => {
                    var b;
                    let c = a.audioStats;
                    var e = a.videoStats, k = a.pcStats;
                    let w = xb({}, Jf), l = xb({}, Kf), n = xb({}, Vj), p = a.connection.pc.getStats(),
                        q = p.audioRecv[0], z = p.videoRecv[0];
                    k = k ? k.videoRecv[0] : void 0;
                    let t = !0 === a.connection.pc._statsFilter.videoIsReady,
                        u = this.trafficStats && R(b = this.trafficStats.peer_delay).call(b,
                            b => b.peer_uid === a.connection.getUserId());
                    q && ("opus" !== q.codec && "aac" !== q.codec || (w.codecType = q.codec), q.outputLevel ? w.receiveLevel = Math.round(32767 * q.outputLevel) : a.connection.user.audioTrack && (w.receiveLevel = Math.round(32767 * a.connection.user.audioTrack.getVolumeLevel())), w.receiveBytes = q.bytes, w.receivePackets = q.packets, w.receivePacketsLost = q.packetsLost, w.packetLossRate = w.receivePacketsLost / (w.receivePackets + w.receivePacketsLost), w.receiveBitrate = c ? 8 * Math.max(0, w.receiveBytes - c.receiveBytes) :
                        0, w.totalDuration = c ? c.totalDuration + 1 : 1, w.totalFreezeTime = c ? c.totalFreezeTime : 0, w.freezeRate = w.totalFreezeTime / w.totalDuration, w.receiveDelay = q.jitterBufferMs, b = a.connection.user.audioTrack, 10 < w.totalDuration && ld.isRemoteAudioFreeze(b) && (w.totalFreezeTime += 1));
                    z && ("H264" !== z.codec && "VP8" !== z.codec && "VP9" !== z.codec && "AV1" !== z.codec || (l.codecType = z.codec), l.receiveBytes = z.bytes, l.receiveBitrate = e ? 8 * Math.max(0, l.receiveBytes - e.receiveBytes) : 0, l.decodeFrameRate = z.decodeFrameRate, l.renderFrameRate = z.decodeFrameRate,
                    z.outputFrame && (l.renderFrameRate = z.outputFrame.frameRate), z.receivedFrame ? (l.receiveFrameRate = z.receivedFrame.frameRate, l.receiveResolutionHeight = z.receivedFrame.height, l.receiveResolutionWidth = z.receivedFrame.width) : a.connection.user.videoTrack && (l.receiveResolutionHeight = a.connection.user.videoTrack._videoHeight || 0, l.receiveResolutionWidth = a.connection.user.videoTrack._videoWidth || 0), void 0 !== z.framesRateFirefox && (l.receiveFrameRate = Math.round(z.framesRateFirefox)), l.receivePackets = z.packets, l.receivePacketsLost =
                        z.packetsLost, l.packetLossRate = l.receivePacketsLost / (l.receivePackets + l.receivePacketsLost), l.totalDuration = e ? e.totalDuration + 1 : 1, l.totalFreezeTime = e ? e.totalFreezeTime : 0, l.receiveDelay = z.jitterBufferMs || 0, e = a.connection.user.videoTrack, a.connection.subscribeOptions.video && t && ld.isRemoteVideoFreeze(e, z, k) && (l.totalFreezeTime += 1), l.freezeRate = l.totalFreezeTime / l.totalDuration);
                    u && (w.end2EndDelay = u.B_ad, l.end2EndDelay = u.B_vd, w.transportDelay = u.B_ed, l.transportDelay = u.B_ed, n.uplinkNetworkQuality = u.B_punq ?
                        u.B_punq : 0, n.downlinkNetworkQuality = u.B_pdnq ? u.B_punq : 0);
                    a.audioStats = w;
                    a.videoStats = l;
                    a.pcStats = p;
                    a.networkStats = n;
                    a.connection.user.audioTrack && this.exceptionMonitor.setRemoteAudioStats(a.connection.user.audioTrack, w);
                    a.connection.user.videoTrack && this.exceptionMonitor.setRemoteVideoStats(a.connection.user.videoTrack, l)
                });
                q(c = this.localConnectionsMap).call(c, a => {
                    let b = a.audioStats, c = a.videoStats, e = xb({}, he), k = xb({}, ie);
                    var l = a.connection.pc.getStats();
                    let n = l.audioSend[0];
                    l = l.videoSend[0];
                    let p =
                        a.connection.getUserId();
                    n && ("opus" !== n.codec && "aac" !== n.codec || (e.codecType = n.codec), n.inputLevel ? e.sendVolumeLevel = Math.round(32767 * n.inputLevel) : a.connection.audioTrack && (e.sendVolumeLevel = Math.round(32767 * a.connection.audioTrack.getVolumeLevel())), e.sendBytes = n.bytes, e.sendPackets = n.packets, e.sendPacketsLost = n.packetsLost, e.sendBitrate = b ? 8 * Math.max(0, e.sendBytes - b.sendBytes) : 0);
                    l && ("H264" !== l.codec && "VP8" !== l.codec && "VP9" !== l.codec && "AV1" !== l.codec || (k.codecType = l.codec), k.sendBytes = l.bytes, k.sendBitrate =
                        c ? 8 * Math.max(0, k.sendBytes - c.sendBytes) : 0, l.inputFrame ? (k.captureFrameRate = l.inputFrame.frameRate, k.captureResolutionHeight = l.inputFrame.height, k.captureResolutionWidth = l.inputFrame.width) : a.connection.videoTrack && (k.captureResolutionWidth = a.connection.videoTrack._videoWidth || 0, k.captureResolutionHeight = a.connection.videoTrack._videoHeight || 0), l.sentFrame ? (k.sendFrameRate = l.sentFrame.frameRate, k.sendResolutionHeight = l.sentFrame.height, k.sendResolutionWidth = l.sentFrame.width) : a.connection.videoTrack &&
                        (k.sendResolutionWidth = a.connection.videoTrack._videoWidth || 0, k.sendResolutionHeight = a.connection.videoTrack._videoHeight || 0), l.avgEncodeMs && (k.encodeDelay = l.avgEncodeMs), a.connection.videoTrack && a.connection.videoTrack._encoderConfig && a.connection.videoTrack._encoderConfig.bitrateMax && (k.targetSendBitrate = 1E3 * a.connection.videoTrack._encoderConfig.bitrateMax), k.sendPackets = l.packets, k.sendPacketsLost = l.packetsLost, k.totalDuration = c ? c.totalDuration + 1 : 1, k.totalFreezeTime = c ? c.totalFreezeTime : 0, this.isLocalVideoFreeze(l) &&
                    (k.totalFreezeTime += 1));
                    a.audioStats = e;
                    a.videoStats = k;
                    a.audioStats && a.connection.audioTrack && this.exceptionMonitor.setLocalAudioStats(p, a.connection.audioTrack, a.audioStats);
                    a.videoStats && a.connection.videoTrack && this.exceptionMonitor.setLocalVideoStats(p, a.connection.videoTrack, a.videoStats)
                })
            };
            this.clientId = a;
            this.updateStatsInterval = window.setInterval(this.updateStats, 1E3);
            this.exceptionMonitor = new $n;
            this.exceptionMonitor.on("exception", (a, c, e) => {
                this.onStatsException && this.onStatsException(a,
                    c, e)
            })
        }

        static isRemoteVideoFreeze(a, b, c) {
            if (!a) return !1;
            a = !c || b.framesDecodeCount > c.framesDecodeCount;
            return !!c && b.framesDecodeFreezeTime > c.framesDecodeFreezeTime || !a
        }

        static isRemoteAudioFreeze(a) {
            return !!a && a._isFreeze()
        }

        reset() {
            this.localConnectionsMap = new ba;
            this.remoteConnectionsMap = new ba;
            this.trafficStats = void 0;
            this.trafficStatsPeerList = [];
            this.uplinkStats = void 0
        }

        getLocalAudioTrackStats(a) {
            return (a = this.localConnectionsMap.get(a)) && a.audioStats ? a.audioStats : xb({}, he)
        }

        getLocalVideoTrackStats(a) {
            return (a = this.localConnectionsMap.get(a)) && a.videoStats ? a.videoStats : xb({}, ie)
        }

        getRemoteAudioTrackStats(a) {
            var b;
            let c = this.remoteConnectionsMap.get(a);
            if (!c || !c.audioStats) return xb({}, Jf);
            if (!this.trafficStats) return c.audioStats;
            a = R(b = this.trafficStats.peer_delay).call(b, a => a.peer_uid === c.connection.user.uid);
            return a && (c.audioStats.publishDuration = a.B_ppad + (x() - this.trafficStats.timestamp)), c.audioStats
        }

        getRemoteNetworkQualityStats(a) {
            return (a = this.remoteConnectionsMap.get(a)) && a.networkStats ? a.networkStats : xb({}, Vj)
        }

        getRemoteVideoTrackStats(a) {
            var b;
            let c = this.remoteConnectionsMap.get(a);
            if (!c || !c.videoStats) return xb({}, Kf);
            if (!this.trafficStats) return c.videoStats;
            a = R(b = this.trafficStats.peer_delay).call(b, a => a.peer_uid ===
                c.connection.user.uid);
            return a && (c.videoStats.publishDuration = a.B_ppvd + (x() - this.trafficStats.timestamp)), c.videoStats
        }

        getRTCStats() {
            var a, b;
            let c = 0, e = 0, g = 0, h = 0;
            q(a = this.localConnectionsMap).call(a, a => {
                a.audioStats && (c += a.audioStats.sendBytes, e += a.audioStats.sendBitrate);
                a.videoStats && (c += a.videoStats.sendBytes, e += a.videoStats.sendBitrate)
            });
            q(b = this.remoteConnectionsMap).call(b, a => {
                a.audioStats && (g += a.audioStats.receiveBytes, h += a.audioStats.receiveBitrate);
                a.videoStats && (g += a.videoStats.receiveBytes,
                    h += a.videoStats.receiveBitrate)
            });
            a = 1;
            return this.trafficStats && (a += this.trafficStats.peer_delay.length), {
                Duration: 0,
                UserCount: a,
                SendBitrate: e,
                SendBytes: c,
                RecvBytes: g,
                RecvBitrate: h,
                OutgoingAvailableBandwidth: this.uplinkStats ? this.uplinkStats.B_uab / 1E3 : 0,
                RTT: this.trafficStats ? 2 * this.trafficStats.B_acd : 0
            }
        }

        removeConnection(a) {
            this.localConnectionsMap.delete(a);
            this.remoteConnectionsMap.delete(a)
        }

        addLocalConnection(a) {
            let b = a.connectionId;
            this.localConnectionsMap.has(b) || this.localConnectionsMap.set(b,
                {connection: a})
        }

        addRemoteConnection(a) {
            let b = a.connectionId;
            this.remoteConnectionsMap.has(b) || this.remoteConnectionsMap.set(b, {connection: a})
        }

        updateTrafficStats(a) {
            var b;
            let c = N(b = a.peer_delay).call(b, a => {
                var b;
                return -1 === G(b = this.trafficStatsPeerList).call(b, a.peer_uid)
            });
            q(c).call(c, a => {
                var b, c;
                let e = R(b = Ib(gc(c = this.remoteConnectionsMap).call(c))).call(b, b => b.connection._userId === a.peer_uid);
                void 0 !== a.B_ppad && void 0 !== a.B_ppvd && (this.onUploadPublishDuration && this.onUploadPublishDuration(a.peer_uid,
                    a.B_ppad, a.B_ppvd, e ? x() - e.connection.startTime : 0), this.trafficStatsPeerList.push(a.peer_uid))
            });
            this.trafficStats = a
        }

        updateUplinkStats(a) {
            var b;
            this.uplinkStats && this.uplinkStats.B_fir !== a.B_fir && k.debug(l(b = "[".concat(this.clientId, "]: Period fir changes to ")).call(b, a.B_fir));
            this.uplinkStats = a
        }

        isLocalVideoFreeze(a) {
            return !(!a.inputFrame ||
                !a.sentFrame) && 5 < a.inputFrame.frameRate && 3 > a.sentFrame.frameRate
        }
    }

    var ok;
    let Uf = () => {
        }, hh = {},
        Tc = [[[100, .00520833333333333], [66.6666666666666, .00434027777777778], [66.6666666666667, .00173611111111111]], [[233.333333333333, .00347222222222222], [266.666666666667], [.00173611111111111], [183.333333333333, 2.17013888888889E-4]], [[700, .001953125], [200, .001953125], [175, 2.44140625E-4]], [[899.999999999998, .00173611111111111], [1200, 8.68055555555556E-4], [160, 2.60416666666667E-4]], [[2666.66666666667, 8.84130658436214E-4],
            [1166.66666666667, 8.84130658436214E-4], [600, 4.82253E-5]]], pk = new class {
            constructor() {
                this.fnMap = new ba
            }

            throttleByKey(a, b, c, e, ...g) {
                if (this.fnMap.has(b)) {
                    var h = this.fnMap.get(b);
                    h.threshold !== c ? (h.fn(...h.args), clearTimeout(h.timer), h = window.setTimeout(() => {
                        const a = this.fnMap.get(b);
                        a && a.fn(...a.args);
                        this.fnMap.delete(b)
                    }, c), this.fnMap.set(b, {
                        fn: a,
                        threshold: c,
                        timer: h,
                        args: g,
                        skipFn: e
                    })) : (h.skipFn && h.skipFn(...h.args), this.fnMap.set(b, Ae({}, h, {fn: a, args: g, skipFn: e})))
                } else h = window.setTimeout(() => {
                    const a =
                        this.fnMap.get(b);
                    a && a.fn(...a.args);
                    this.fnMap.delete(b)
                }, c), this.fnMap.set(b, {fn: a, threshold: c, timer: h, args: g, skipFn: e})
            }
        }, qk = ta(ok = pk.throttleByKey).call(ok, pk), Dd = null, ao = 1;

    class Ob {
        constructor(a) {
            var b;
            this.lockingPromise = v.resolve();
            this.locks = 0;
            this.name = "";
            this.lockId = ao++;
            a && (this.name = a);
            k.debug(l(b = "[lock-".concat(this.name, "-")).call(b, this.lockId, "] is created."))
        }

        get isLocked() {
            return 0 < this.locks
        }

        lock() {
            var a, b;
            let c;
            this.locks += 1;
            k.debug(l(a = l(b = "[lock-".concat(this.name, "-")).call(b,
                this.lockId, "] is locked, current queue ")).call(a, this.locks, "."));
            let e = new v(a => {
                c = () => {
                    var b, c;
                    --this.locks;
                    k.debug(l(b = l(c = "[lock-".concat(this.name, "-")).call(c, this.lockId, "] is not locked, current queue ")).call(b, this.locks, "."));
                    a()
                }
            });
            a = this.lockingPromise.then(() => c);
            return this.lockingPromise = this.lockingPromise.then(() => e), a
        }
    }

    let Ce = new Ob("safari"), mh = !1, nh = !1, db = new class extends Ta {
        constructor() {
            super();
            this._state = fc.IDLE;
            this.lastAccessCameraPermission = this.lastAccessMicrophonePermission =
                this.isAccessCameraPermission = this.isAccessMicrophonePermission = !1;
            this.deviceInfoMap = new ba;
            this.init().then(() => {
                var a, b;
                navigator.mediaDevices && navigator.mediaDevices.addEventListener && navigator.mediaDevices.addEventListener("devicechange", ta(b = this.updateDevicesInfo).call(b, this));
                window.setInterval(ta(a = this.updateDevicesInfo).call(a, this), 2500)
            }).catch(a => k.error(a.toString()))
        }

        get state() {
            return this._state
        }

        set state(a) {
            a !== this._state && (this.emit(Mb.STATE_CHANGE, a), this._state = a)
        }

        async enumerateDevices(a,
                               b, c = !1) {
            if (!navigator.mediaDevices || !navigator.mediaDevices.enumerateDevices) return (new p(n.NOT_SUPPORTED, "enumerateDevices() not supported.")).throw();
            var e = await navigator.mediaDevices.enumerateDevices();
            e = this.checkMediaDeviceInfoIsOk(e);
            let g = !this.isAccessMicrophonePermission && a, h = !this.isAccessCameraPermission && b;
            e.audio && (g = !1);
            e.video && (h = !1);
            let m = e = null, r = null;
            if (!c && (g || h)) {
                Ce.isLocked && (k.debug("[device manager] wait GUM lock"), (await Ce.lock())(), k.debug("[device manager] GUM unlock"));
                if (mh && (g = !1, this.isAccessMicrophonePermission = !0), nh && (h = !1, this.isAccessCameraPermission = !0), k.debug("[device manager] check media device permissions", a, b, g, h), g && h) {
                    try {
                        r = await navigator.mediaDevices.getUserMedia({audio: !0, video: !0})
                    } catch (ja) {
                        c = Ed(ja.name || ja.code || ja, ja.message);
                        if (c.code === n.PERMISSION_DENIED) throw c;
                        k.warning("getUserMedia failed in getDevices", c)
                    }
                    this.isAccessMicrophonePermission = this.isAccessCameraPermission = !0
                } else if (g) {
                    try {
                        e = await navigator.mediaDevices.getUserMedia({audio: a})
                    } catch (ja) {
                        c =
                            Ed(ja.name || ja.code || ja, ja.message);
                        if (c.code === n.PERMISSION_DENIED) throw c;
                        k.warning("getUserMedia failed in getDevices", c)
                    }
                    this.isAccessMicrophonePermission = !0
                } else if (h) {
                    try {
                        m = await navigator.mediaDevices.getUserMedia({video: b})
                    } catch (ja) {
                        c = Ed(ja.name || ja.code || ja, ja.message);
                        if (c.code === n.PERMISSION_DENIED) throw c;
                        k.warning("getUserMedia failed in getDevices", c)
                    }
                    this.isAccessCameraPermission = !0
                }
                k.debug("[device manager] mic permission", a, "cam permission", b)
            }
            try {
                var l, y, sa;
                const a = await navigator.mediaDevices.enumerateDevices();
                return e && q(l = e.getTracks()).call(l, a => a.stop()), m && q(y = m.getTracks()).call(y, a => a.stop()), r && q(sa = r.getTracks()).call(sa, a => a.stop()), e = null, m = null, r = null, a
            } catch (ja) {
                var B, t, z;
                e && q(B = e.getTracks()).call(B, a => a.stop());
                m && q(t = m.getTracks()).call(t, a => a.stop());
                r && q(z = r.getTracks()).call(z, a => a.stop());
                r = m = e = null;
                return (new p(n.ENUMERATE_DEVICES_FAILED, ja.toString())).throw()
            }
        }

        async getRecordingDevices(a = !1) {
            a = await this.enumerateDevices(!0, !1, a);
            return N(a).call(a, a => "audioinput" === a.kind)
        }

        async getCamerasDevices(a =
                                    !1) {
            a = await this.enumerateDevices(!1, !0, a);
            return N(a).call(a, a => "videoinput" === a.kind)
        }

        async getSpeakers(a = !1) {
            a = await this.enumerateDevices(!0, !1, a);
            return N(a).call(a, a => "audiooutput" === a.kind)
        }

        searchDeviceNameById(a) {
            return (a = this.deviceInfoMap.get(a)) ? a.device.label || a.device.deviceId : null
        }

        searchDeviceIdByName(a) {
            var b;
            let c = null;
            return q(b = this.deviceInfoMap).call(b, b => {
                b.device.label === a && (c = b.device.deviceId)
            }), c
        }

        async getDeviceById(a) {
            var b = await this.enumerateDevices(!0, !0, !0);
            b = R(b).call(b,
                b => b.deviceId === a);
            if (!b) throw new p(n.DEVICE_NOT_FOUND, "deviceId: ".concat(a));
            return b
        }

        async init() {
            this.state = fc.INITING;
            try {
                await this.updateDevicesInfo(), this.state = fc.INITEND
            } catch (a) {
                throw(k.warning("Device Detection functionality cannot start properly.", a.toString()), this.state = fc.IDLE, "boolean" == typeof isSecureContext ? isSecureContext : "https:" === location.protocol || "file:" === location.protocol || "localhost" === location.hostname || "127.0.0.1" === location.hostname || "::1" === location.hostname) || (new p(n.WEB_SECURITY_RESTRICT,
                    "Your context is limited by web security, please try using https protocol or localhost.")).throw(), a;
            }
        }

        async updateDevicesInfo() {
            var a;
            const b = await this.enumerateDevices(!0, !0, !0), c = x(), e = [], g = this.checkMediaDeviceInfoIsOk(b);
            if (q(b).call(b, a => {
                if (a.deviceId) {
                    var b = this.deviceInfoMap.get(a.deviceId);
                    if ("ACTIVE" !== (b ? b.state : "INACTIVE")) {
                        const b = {initAt: c, updateAt: c, device: a, state: "ACTIVE"};
                        this.deviceInfoMap.set(a.deviceId, b);
                        e.push(b)
                    }
                    b && (b.updateAt = c)
                }
            }), q(a = this.deviceInfoMap).call(a, (a, b) => {
                "ACTIVE" ===
                a.state && a.updateAt !== c && (a.state = "INACTIVE", e.push(a))
            }), this.state !== fc.INITEND) return g.audio && (this.lastAccessMicrophonePermission = !0, this.isAccessMicrophonePermission = !0), void (g.video && (this.lastAccessCameraPermission = !0, this.isAccessCameraPermission = !0));
            q(e).call(e, a => {
                switch (a.device.kind) {
                    case "audioinput":
                        this.lastAccessMicrophonePermission && this.isAccessMicrophonePermission && this.emit(Mb.RECORDING_DEVICE_CHANGED, a);
                        break;
                    case "videoinput":
                        this.lastAccessCameraPermission && this.isAccessCameraPermission &&
                        this.emit(Mb.CAMERA_DEVICE_CHANGED, a);
                        break;
                    case "audiooutput":
                        this.lastAccessMicrophonePermission && this.isAccessMicrophonePermission && this.emit(Mb.PLAYOUT_DEVICE_CHANGED, a)
                }
            });
            g.audio && (this.lastAccessMicrophonePermission = !0, this.isAccessMicrophonePermission = !0);
            g.video && (this.lastAccessCameraPermission = !0, this.isAccessCameraPermission = !0)
        }

        checkMediaDeviceInfoIsOk(a) {
            const b = N(a).call(a, a => "audioinput" === a.kind);
            a = N(a).call(a, a => "videoinput" === a.kind);
            const c = {audio: !1, video: !1};
            for (const a of b) if (a.label &&
                a.deviceId) {
                c.audio = !0;
                break
            }
            for (const b of a) if (b.label && b.deviceId) {
                c.video = !0;
                break
            }
            return c
        }
    };
    var bo = [].slice, co = /MSIE .\./.test(Zc), rk = function (a) {
        return function (b, c) {
            var e = 2 < arguments.length, g = e ? bo.call(arguments, 2) : void 0;
            return a(e ? function () {
                ("function" == typeof b ? b : Function(b)).apply(this, g)
            } : b, c)
        }
    };
    O({global: !0, bind: !0, forced: co}, {setTimeout: rk(M.setTimeout), setInterval: rk(M.setInterval)});
    var Fc = ha.setTimeout;
    let ph = 0, De = 0, t = new class {
        constructor() {
            var a, b;
            this.baseInfoMap = new ba;
            this.clientList =
                Mj;
            this.keyEventUploadPendingItems = [];
            this.normalEventUploadPendingItems = [];
            this.apiInvokeUploadPendingItems = [];
            this.apiInvokeCount = 0;
            this.ltsList = [];
            this.lastSendNormalEventTime = x();
            this.customReportCount = 0;
            this.eventUploadTimer = window.setInterval(ta(a = this.doSend).call(a, this), u.EVENT_REPORT_SEND_INTERVAL);
            this.setSessionIdTimer = window.setInterval(ta(b = this.appendSessionId).call(b, this), u.EVENT_REPORT_SEND_INTERVAL)
        }

        reportApiInvoke(a, b, c) {
            b.timeout = b.timeout || 6E4;
            b.reportResult = void 0 === b.reportResult ||
                b.reportResult;
            const e = x(), g = this.apiInvokeCount += 1,
                h = () => ({tag: b.tag, invokeId: g, sid: a, name: b.name, apiInvokeTime: e, options: b.options}),
                m = !!u.SHOW_REPORT_INVOKER_LOG;
            m && k.info("".concat(b.name, " start"), b.options);
            let r = !1;
            yb(b.timeout).then(() => {
                r || (this.sendApiInvoke(ua({}, h(), {
                    error: n.API_INVOKE_TIMEOUT,
                    success: !1
                })), k.debug("".concat(b.name, " timeout")))
            });
            const l = new p(n.UNEXPECTED_ERROR, "".concat(b.name, ": this api invoke is end"));
            return {
                onSuccess: a => {
                    const e = () => {
                        if (r) throw l;
                        return r = !0, this.sendApiInvoke(ua({},
                            h(), {success: !0}, b.reportResult && {result: a})), m && k.info("".concat(b.name, " onSuccess")), a
                    };
                    return c ? qk(e, b.name + "Success", c, () => r = !0) : e()
                }, onError: a => {
                    const e = () => {
                        if (r) throw a;
                        r = !0;
                        this.sendApiInvoke(ua({}, h(), {success: !1, error: a.toString()}));
                        m && k.info("".concat(b.name, " onFailure"), a.toString())
                    };
                    return c ? qk(e, b.name + "Error", c, () => r = !0) : e()
                }
            }
        }

        sessionInit(a, b) {
            if (!this.baseInfoMap.has(a)) {
                var c = x();
                a = this.createBaseInfo(a, c);
                a.cname = b.cname;
                var e = Ha({}, {
                    willUploadConsoleLog: u.UPLOAD_LOG, maxTouchPoints: navigator.maxTouchPoints,
                    areaVersion: Ff ? "global" : "oversea", areas: u.AREAS && u.AREAS.join(",")
                }, b.extend), g = x();
                b = ua({}, a, {
                    eventType: Da.SESSION_INIT,
                    appid: b.appid,
                    browser: navigator.userAgent,
                    build: "v4.4.0-0-g48538343(4/2/2021, 5:44:00 PM)",
                    lts: g,
                    elapse: g - c,
                    extend: A(e),
                    mode: b.mode,
                    process: u.PROCESS_ID,
                    success: !0,
                    version: Va
                });
                this.send({type: ma.SESSION, data: b}, !0)
            }
        }

        joinChooseServer(a, b) {
            if (a = this.baseInfoMap.get(a)) {
                var c = a.info, e = x();
                b = ua({}, c, {
                    eventType: Da.JOIN_CHOOSE_SERVER,
                    lts: e,
                    eventElapse: e - b.lts,
                    chooseServerAddr: b.csAddr,
                    errorCode: b.ec,
                    elapse: e - a.startTime,
                    success: b.succ,
                    chooseServerAddrList: A(b.serverList),
                    uid: b.uid ? oa(b.uid) : null,
                    cid: b.cid ? oa(b.cid) : null
                });
                this.send({type: ma.JOIN_CHOOSE_SERVER, data: b}, !0)
            }
        }

        reqUserAccount(a, b) {
            if (a = this.baseInfoMap.get(a)) {
                var c = a.info, e = x();
                b = ua({}, c, {
                    eventType: Da.REQ_USER_ACCOUNT,
                    lts: e,
                    success: b.success,
                    serverAddress: b.serverAddr,
                    stringUid: b.stringUid,
                    uid: b.uid,
                    errorCode: b.errorCode,
                    elapse: e - a.startTime,
                    eventElapse: e - b.lts,
                    extend: A(b.extend)
                });
                this.send({
                    type: ma.REQ_USER_ACCOUNT,
                    data: b
                }, !0)
            }
        }

        joinGateway(a, b) {
            if (a = this.baseInfoMap.get(a)) {
                var c = a.info;
                b.vid && (c.vid = b.vid);
                c.uid = b.uid;
                c.cid = b.cid;
                var e = x();
                c = ua({}, c, {
                    eventType: Da.JOIN_GATEWAY,
                    lts: e,
                    gatewayAddr: b.addr,
                    success: b.succ,
                    errorCode: b.ec,
                    elapse: e - a.startTime,
                    eventElapse: e - b.lts
                });
                b.succ && (a.lastJoinSuccessTime = e);
                this.send({type: ma.JOIN_GATEWAT, data: c}, !0)
            }
        }

        joinChannelTimeout(a, b) {
            if (a = this.baseInfoMap.get(a)) {
                var c = x();
                b = ua({}, a.info, {lts: c, timeout: b, elapse: c - a.startTime});
                this.send({
                    type: ma.JOIN_CHANNEL_TIMEOUT,
                    data: b
                }, !0)
            }
        }

        publish(a, b) {
            if (a = this.baseInfoMap.get(a)) {
                var c = a.info, e = x();
                b = ua({}, c, {
                    eventType: Da.PUBLISH,
                    lts: e,
                    eventElapse: e - b.lts,
                    elapse: e - a.startTime,
                    success: b.succ,
                    errorCode: b.ec,
                    videoName: b.videoName,
                    audioName: b.audioName,
                    screenName: b.screenName,
                    screenshare: b.screenshare,
                    audio: b.audio,
                    video: b.video,
                    p2pid: b.p2pid,
                    publishRequestid: b.publishRequestid
                });
                this.send({type: ma.PUBLISH, data: b}, !0)
            }
        }

        subscribe(a, b) {
            if (a = this.baseInfoMap.get(a)) {
                var c = a.info, e = x();
                a = ua({}, c, {
                    eventType: Da.SUBSCRIBE,
                    lts: e,
                    eventElapse: e - b.lts,
                    elapse: e - a.startTime,
                    success: b.succ,
                    errorCode: b.ec,
                    video: b.video,
                    audio: b.audio,
                    subscribeRequestid: b.subscribeRequestid,
                    p2pid: b.p2pid
                });
                "string" == typeof b.peerid ? a.peerSuid = b.peerid : a.peer = b.peerid;
                this.send({type: ma.SUBSCRIBE, data: a}, !0)
            }
        }

        firstRemoteFrame(a, b, c, e) {
            if (a = this.baseInfoMap.get(a)) {
                var g = a.info, h = x();
                b = ua({}, g, {}, e, {elapse: h - a.startTime, eventType: b, lts: h});
                this.send({type: c, data: b}, !0)
            }
        }

        onGatewayStream(a, b, c, e) {
            if (a = this.baseInfoMap.get(a)) b = ua({}, a.info, {}, e, {
                eventType: b,
                lts: x()
            }), this.send({type: c, data: b}, !0)
        }

        streamSwitch(a, b) {
            if (a = this.baseInfoMap.get(a)) {
                var c = a.info, e = x();
                b = ua({}, c, {
                    eventType: Da.STREAM_SWITCH,
                    lts: e,
                    isDual: b.isdual,
                    elapse: e - a.startTime,
                    success: b.succ
                });
                this.send({type: ma.STREAM_SWITCH, data: b}, !0)
            }
        }

        requestProxyAppCenter(a, b) {
            if (a = this.baseInfoMap.get(a)) {
                var c = a.info, e = x();
                b = ua({}, c, {
                    eventType: Da.REQUEST_PROXY_APPCENTER,
                    lts: e,
                    eventElapse: e - b.lts,
                    elapse: e - a.startTime,
                    APAddr: b.APAddr,
                    workerManagerList: b.workerManagerList,
                    response: b.response,
                    errorCode: b.ec,
                    success: b.succ
                });
                this.send({type: ma.REQUEST_PROXY_APPCENTER, data: b}, !0)
            }
        }

        requestProxyWorkerManager(a, b) {
            if (a = this.baseInfoMap.get(a)) {
                var c = a.info, e = x();
                b = ua({}, c, {
                    eventType: Da.REQUEST_PROXY_WORKER_MANAGER,
                    lts: e,
                    eventElapse: e - b.lts,
                    elapse: e - a.startTime,
                    workerManagerAddr: b.workerManagerAddr,
                    response: b.response,
                    errorCode: b.ec,
                    success: b.succ
                });
                this.send({type: ma.REQUEST_PROXY_WORKER_MANAGER, data: b}, !0)
            }
        }

        setProxyServer(a) {
            (this.proxyServer = a) ? k.debug("reportProxyServerurl: ".concat(a)) : k.debug("disable reportProxyServerurl: ".concat(a))
        }

        peerPublishStatus(a,
                          b) {
            if (a = this.baseInfoMap.get(a)) {
                var c = a.info, e = x();
                b = ua({}, c, {
                    subscribeElapse: b.subscribeElapse,
                    peer: b.peer,
                    peerPublishDuration: Math.max(b.audioPublishDuration, b.videoPublishDuration),
                    audiotag: 0 < b.audioPublishDuration ? 1 : -1,
                    videotag: 0 < b.videoPublishDuration ? 1 : -1,
                    lts: e,
                    elapse: e - a.startTime,
                    joinChannelSuccessElapse: e - (a.lastJoinSuccessTime || e)
                });
                this.send({type: ma.PEER_PUBLISH_STATUS, data: b}, !0)
            }
        }

        workerEvent(a, b) {
            if (a = this.baseInfoMap.get(a)) {
                var c = a.info, e = x();
                b = function (a, b, c) {
                    const e = a[b];
                    if (!e ||
                        "string" != typeof e) return [a];
                    a[b] = "";
                    const g = Cd(A(a));
                    let h = 0;
                    const m = [];
                    let k = 0;
                    for (let r = 0; r < e.length; r++) k += 127 >= e.charCodeAt(r) ? 1 : 3, k <= c - g || (m[m.length] = Ae({}, a, {[b]: e.substring(h, r)}), h = r, k = 127 >= e.charCodeAt(r) ? 1 : 3);
                    return h !== e.length - 1 && (m[m.length] = Ae({}, a, {[b]: e.substring(h)})), m
                }(ua({}, c, {}, b, {elapse: e - a.startTime, lts: e, productType: "WebRTC"}), "payload", 1300);
                q(b).call(b, a => this.send({type: ma.WORKER_EVENT, data: a}, !0))
            }
        }

        apworkerEvent(a, b) {
            if (a = this.baseInfoMap.get(a)) {
                var c = a.info, e = x();
                b =
                    ua({}, c, {}, b, {elapse: e - a.startTime, lts: e});
                this.send({type: ma.AP_WORKER_EVENT, data: b}, !0)
            }
        }

        joinWebProxyAP(a, b) {
            if (a = this.baseInfoMap.get(a)) {
                var c = a.info, e = x();
                b = ua({}, c, {}, b, {elapse: e - a.startTime, lts: e});
                this.send({type: ma.JOIN_WEB_PROXY_AP, data: b}, !0)
            }
        }

        WebSocketQuit(a, b) {
            if (a = this.baseInfoMap.get(a)) {
                var c = a.info, e = x();
                b = ua({}, c, {}, b, {elapse: e - a.startTime, lts: e});
                this.send({type: ma.WEBSOCKET_QUIT, data: b}, !0)
            }
        }

        async sendCustomReportMessage(a, b) {
            if (this.customReportCount += b.length, this.customReportCount >
            u.CUSTOM_REPORT_LIMIT) throw new p(n.CUSTOM_REPORT_FREQUENCY_TOO_HIGH);
            this.customReportCounterTimer || (this.customReportCounterTimer = window.setInterval(() => {
                this.customReportCount = 0
            }, 5E3));
            b = C(b).call(b, b => ({type: ma.USER_ANALYTICS, data: ua({sid: a}, b)}));
            b = {msgType: "EventMessages", sentTs: Math.round(x() / 1E3), payloads: C(b).call(b, a => A(a))};
            try {
                await this.postDataToStatsCollector(b)
            } catch (c) {
                throw k.error("send custom report message failed", c.toString()), new p(n.CUSTOM_REPORT_SEND_FAILED, c.message);
            }
        }

        sendApiInvoke(a) {
            var b =
                u.NOT_REPORT_EVENT;
            if (a.tag && ya(b) && ya(b).call(b, a.tag)) return !1;
            if (null === a.sid) return this.apiInvokeUploadPendingItems.push(a), !1;
            b = this.baseInfoMap.get(a.sid);
            if (!b) return this.apiInvokeUploadPendingItems.push(a), !1;
            const {cname: c, uid: e, cid: g} = b.info;
            a.lts = a.lts || x();
            a = {
                invokeId: a.invokeId,
                sid: a.sid,
                cname: c,
                cid: g,
                uid: e,
                lts: a.lts,
                success: a.success,
                elapse: a.lts - b.startTime,
                execElapse: a.lts - a.apiInvokeTime,
                apiName: a.name,
                options: a.options ? A(a.options) : void 0,
                execStates: a.states ? A(a.states) : void 0,
                execResult: a.result ? A(a.result) : void 0,
                errorCode: a.error ? A(a.error) : void 0
            };
            return this.send({type: ma.API_INVOKE, data: a}, !1), !0
        }

        appendSessionId() {
            var a;
            q(a = this.clientList).call(a, a => {
                if (a._sessionId) {
                    const b = this.apiInvokeUploadPendingItems.length;
                    for (let c = 0; c < b; c++) {
                        const b = this.apiInvokeUploadPendingItems.shift();
                        b && (b.sid = a._sessionId, this.sendApiInvoke(Ha({}, b)))
                    }
                }
            })
        }

        send(a, b) {
            if (b) return this.keyEventUploadPendingItems.push(a), void this.sendItems(this.keyEventUploadPendingItems, !0);
            var c;
            (this.normalEventUploadPendingItems.push(a),
            this.normalEventUploadPendingItems.length > u.NORMAL_EVENT_QUEUE_CAPACITY) && Ia(c = this.normalEventUploadPendingItems).call(c, 0, 1);
            10 <= this.normalEventUploadPendingItems.length && this.sendItems(this.normalEventUploadPendingItems, !1)
        }

        doSend() {
            0 < this.keyEventUploadPendingItems.length && this.sendItems(this.keyEventUploadPendingItems, !0);
            0 < this.normalEventUploadPendingItems.length && 5E3 <= x() - this.lastSendNormalEventTime && this.sendItems(this.normalEventUploadPendingItems, !1)
        }

        sendItems(a, b) {
            const c = [];
            for (var e =
                []; a.length;) {
                const b = a.shift();
                20 > c.length ? c.push(b) : e.push(b)
            }
            a.push(...e);
            for (const a of [...c]) {
                var g, h;
                -1 !== G(g = this.ltsList).call(g, a.data.lts) ? (a.data.lts = this.ltsList[this.ltsList.length - 1] + 1, this.ltsList.push(a.data.lts)) : (this.ltsList.push(a.data.lts), ed(h = this.ltsList).call(h, (a, b) => a - b))
            }
            b || (this.lastSendNormalEventTime = x());
            e = {
                msgType: "EventMessages",
                sentTs: Math.round(x() / 1E3),
                payloads: C(c).call(c, a => A(a)),
                vid: (a => (a = a && a.data.sid && this.baseInfoMap.get(a.data.sid)) && a.info.vid && +a.info.vid ||
                    0)(c[0])
            };
            return c.length && this.postDataToStatsCollector(e).catch((a => c => {
                var e, g, h;
                b ? this.keyEventUploadPendingItems = l(e = this.keyEventUploadPendingItems).call(e, a) : (this.normalEventUploadPendingItems = l(g = this.normalEventUploadPendingItems).call(g, a), this.normalEventUploadPendingItems.length > u.NORMAL_EVENT_QUEUE_CAPACITY && (Ia(h = this.normalEventUploadPendingItems).call(h, 0, this.normalEventUploadPendingItems.length - u.NORMAL_EVENT_QUEUE_CAPACITY), k.warning("report: drop normal events")))
            })(c)), a
        }

        async postDataToStatsCollector(a,
                                       b = !1) {
            var c, e, g;
            const h = b ? "/events/proto-raws" : "/events/messages";
            let m = this.url || (this.proxyServer ? l(c = l(e = "https://".concat(this.proxyServer, "/rs/?h=")).call(e, u.EVENT_REPORT_DOMAIN, "&p=6443&d=")).call(c, h) : l(g = "https://".concat(u.EVENT_REPORT_DOMAIN, ":6443")).call(g, h));
            for (c = 0; 2 > c; c += 1) {
                var k, n, p;
                1 === c && (m = this.backupUrl || (this.proxyServer ? l(k = l(n = "https://".concat(this.proxyServer, "/rs/?h=")).call(n, u.EVENT_REPORT_BACKUP_DOMAIN, "&p=6443&d=")).call(k, h) : l(p = "https://".concat(u.EVENT_REPORT_BACKUP_DOMAIN,
                    ":6443")).call(p, h)));
                try {
                    b ? await jl(m, {timeout: 1E4, data: a}) : await Ab(m, {timeout: 1E4, data: a})
                } catch (sa) {
                    if (1 === c) throw sa;
                    continue
                }
                break
            }
        }

        createBaseInfo(a, b) {
            const c = Ha({}, Ln);
            return c.sid = a, this.baseInfoMap.set(a, {info: c, startTime: b}), c
        }
    }, kl = {
        [oc.ACCESS_POINT]: {
            [Ea.NO_FLAG_SET]: {desc: "flag is zero", retry: !1},
            [Ea.FLAG_SET_BUT_EMPTY]: {desc: "flag is empty", retry: !1},
            [Ea.INVALID_FALG_SET]: {desc: "invalid flag", retry: !1},
            [Ea.NO_SERVICE_AVAILABLE]: {desc: "no service available", retry: !0},
            [Ea.NO_SERVICE_AVAILABLE_P2P]: {
                desc: "no unilbs p2p service available",
                retry: !0
            },
            [Ea.NO_SERVICE_AVAILABLE_VOET]: {desc: "no unilbs voice service available", retry: !0},
            [Ea.NO_SERVICE_AVAILABLE_WEBRTC]: {desc: "no unilbs webrtc service available", retry: !0},
            [Ea.NO_SERVICE_AVAILABLE_CDS]: {desc: "no cds service available", retry: !0},
            [Ea.NO_SERVICE_AVAILABLE_CDN]: {desc: "no cdn dispatcher service available", retry: !0},
            [Ea.NO_SERVICE_AVAILABLE_TDS]: {desc: "no tds service available", retry: !0},
            [Ea.NO_SERVICE_AVAILABLE_REPORT]: {desc: "no unilbs report service available", retry: !0},
            [Ea.NO_SERVICE_AVAILABLE_APP_CENTER]: {
                desc: "no app center service available",
                retry: !0
            },
            [Ea.NO_SERVICE_AVAILABLE_ENV0]: {desc: "no unilbs sig env0 service available", retry: !0},
            [Ea.NO_SERVICE_AVAILABLE_VOET]: {desc: "no unilbs voet service available", retry: !0},
            [Ea.NO_SERVICE_AVAILABLE_STRING_UID]: {desc: "no string uid service available", retry: !0},
            [Ea.NO_SERVICE_AVAILABLE_WEBRTC_UNILBS]: {desc: "no webrtc unilbs service available", retry: !0}
        },
        [oc.UNILBS]: {
            [Ya.INVALID_VENDOR_KEY]: {desc: "invalid vendor key, can not find appid", retry: !1},
            [Ya.INVALID_CHANNEL_NAME]: {
                desc: "invalid channel name",
                retry: !1
            },
            [Ya.INTERNAL_ERROR]: {desc: "unilbs internal error", retry: !1},
            [Ya.NO_AUTHORIZED]: {desc: "invalid token, authorized failed", retry: !1},
            [Ya.DYNAMIC_KEY_TIMEOUT]: {desc: "dynamic key or token timeout", retry: !1},
            [Ya.NO_ACTIVE_STATUS]: {desc: "no active status", retry: !1},
            [Ya.DYNAMIC_KEY_EXPIRED]: {desc: "dynamic key expired", retry: !1},
            [Ya.STATIC_USE_DYNAMIC_KEY]: {desc: "static use dynamic key", retry: !1},
            [Ya.DYNAMIC_USE_STATIC_KEY]: {desc: "dynamic use static key", retry: !1},
            [Ya.USER_OVERLOAD]: {
                desc: "amount of users over load",
                retry: !1
            },
            [Ya.FORBIDDEN_REGION]: {desc: "the request is forbidden in this area", retry: !1},
            [Ya.CANNOT_MEET_AREA_DEMAND]: {desc: "unable to allocate services in this area", retry: !1}
        },
        [oc.STRING_UID_ALLOCATOR]: {
            [fd.IIIEGAL_APPID]: {desc: "invalid appid", retry: !1},
            [fd.IIIEGAL_UID]: {desc: "invalid string uid", retry: !1},
            [fd.INTERNAL_ERROR]: {desc: "string uid allocator internal error", retry: !0}
        }
    }, ll = {
        [F.K_TIMESTAMP_EXPIRED]: {desc: "K_TIMESTAMP_EXPIRED", action: "failed"},
        [F.K_CHANNEL_PERMISSION_INVALID]: {
            desc: "K_CHANNEL_PERMISSION_INVALID",
            action: "failed"
        },
        [F.K_CERTIFICATE_INVALID]: {desc: "K_CERTIFICATE_INVALID", action: "failed"},
        [F.K_CHANNEL_NAME_EMPTY]: {desc: "K_CHANNEL_NAME_EMPTY", action: "failed"},
        [F.K_CHANNEL_NOT_FOUND]: {desc: "K_CHANNEL_NOT_FOUND", action: "failed"},
        [F.K_TICKET_INVALID]: {desc: "K_TICKET_INVALID", action: "failed"},
        [F.K_CHANNEL_CONFLICTED]: {desc: "K_CHANNEL_CONFLICTED", action: "failed"},
        [F.K_SERVICE_NOT_READY]: {desc: "K_SERVICE_NOT_READY", action: "tryNext"},
        [F.K_SERVICE_TOO_HEAVY]: {desc: "K_SERVICE_TOO_HEAVY", action: "tryNext"},
        [F.K_UID_BANNED]: {desc: "K_UID_BANNED", action: "failed"},
        [F.K_IP_BANNED]: {desc: "K_IP_BANNED", action: "failed"},
        [F.ERR_INVALID_VENDOR_KEY]: {desc: "ERR_INVALID_VENDOR_KEY", action: "failed"},
        [F.ERR_INVALID_CHANNEL_NAME]: {desc: "ERR_INVALID_CHANNEL_NAME", action: "failed"},
        [F.WARN_NO_AVAILABLE_CHANNEL]: {desc: "WARN_NO_AVAILABLE_CHANNEL", action: "failed"},
        [F.WARN_LOOKUP_CHANNEL_TIMEOUT]: {desc: "WARN_LOOKUP_CHANNEL_TIMEOUT", action: "tryNext"},
        [F.WARN_LOOKUP_CHANNEL_REJECTED]: {desc: "WARN_LOOKUP_CHANNEL_REJECTED", action: "failed"},
        [F.WARN_OPEN_CHANNEL_TIMEOUT]: {desc: "WARN_OPEN_CHANNEL_TIMEOUT", action: "tryNext"},
        [F.WARN_OPEN_CHANNEL_REJECTED]: {desc: "WARN_OPEN_CHANNEL_REJECTED", action: "failed"},
        [F.WARN_REQUEST_DEFERRED]: {desc: "WARN_REQUEST_DEFERRED", action: "failed"},
        [F.ERR_DYNAMIC_KEY_TIMEOUT]: {desc: "ERR_DYNAMIC_KEY_TIMEOUT", action: "failed"},
        [F.ERR_NO_AUTHORIZED]: {desc: "ERR_NO_AUTHORIZED", action: "failed"},
        [F.ERR_VOM_SERVICE_UNAVAILABLE]: {desc: "ERR_VOM_SERVICE_UNAVAILABLE", action: "tryNext"},
        [F.ERR_NO_CHANNEL_AVAILABLE_CODE]: {
            desc: "ERR_NO_CHANNEL_AVAILABLE_CODE",
            action: "failed"
        },
        [F.ERR_MASTER_VOCS_UNAVAILABLE]: {desc: "ERR_MASTER_VOCS_UNAVAILABLE", action: "tryNext"},
        [F.ERR_INTERNAL_ERROR]: {desc: "ERR_INTERNAL_ERROR", action: "tryNext"},
        [F.ERR_NO_ACTIVE_STATUS]: {desc: "ERR_NO_ACTIVE_STATUS", action: "failed"},
        [F.ERR_INVALID_UID]: {desc: "ERR_INVALID_UID", action: "failed"},
        [F.ERR_DYNAMIC_KEY_EXPIRED]: {desc: "ERR_DYNAMIC_KEY_EXPIRED", action: "failed"},
        [F.ERR_STATIC_USE_DYANMIC_KE]: {desc: "ERR_STATIC_USE_DYANMIC_KE", action: "failed"},
        [F.ERR_DYNAMIC_USE_STATIC_KE]: {
            desc: "ERR_DYNAMIC_USE_STATIC_KE",
            action: "failed"
        },
        [F.ERR_NO_VOCS_AVAILABLE]: {desc: "ERR_NO_VOCS_AVAILABLE", action: "tryNext"},
        [F.ERR_NO_VOS_AVAILABLE]: {desc: "ERR_NO_VOS_AVAILABLE", action: "tryNext"},
        [F.ERR_JOIN_CHANNEL_TIMEOUT]: {desc: "ERR_JOIN_CHANNEL_TIMEOUT", action: "tryNext"},
        [F.ERR_JOIN_BY_MULTI_IP]: {desc: "ERR_JOIN_BY_MULTI_IP", action: "recover"},
        [F.ERR_NOT_JOINED]: {desc: "ERR_NOT_JOINED", action: "failed"},
        [F.ERR_REPEAT_JOIN_REQUEST]: {desc: "ERR_REPEAT_JOIN_REQUEST", action: "quit"},
        [F.ERR_REPEAT_JOIN_CHANNEL]: {
            desc: "ERR_REPEAT_JOIN_CHANNEL",
            action: "quit"
        },
        [F.ERR_INVALID_VENDOR_KEY]: {desc: "ERR_INVALID_VENDOR_KEY", action: "failed"},
        [F.ERR_INVALID_CHANNEL_NAME]: {desc: "ERR_INVALID_CHANNEL_NAME", action: "failed"},
        [F.ERR_INVALID_STRINGUID]: {desc: "ERR_INVALID_STRINGUID", action: "failed"},
        [F.ERR_TOO_MANY_USERS]: {desc: "ERR_TOO_MANY_USERS", action: "tryNext"},
        [F.ERR_SET_CLIENT_ROLE_TIMEOUT]: {desc: "ERR_SET_CLIENT_ROLE_TIMEOUT", action: "failed"},
        [F.ERR_SET_CLIENT_ROLE_NO_PERMISSION]: {desc: "ERR_SET_CLIENT_ROLE_TIMEOUT", action: "failed"},
        [F.ERR_SET_CLIENT_ROLE_ALREADY_IN_USE]: {
            desc: "ERR_SET_CLIENT_ROLE_ALREADY_IN_USE",
            action: "success"
        },
        [F.ERR_PUBLISH_REQUEST_INVALID]: {desc: "ERR_PUBLISH_REQUEST_INVALID", action: "failed"},
        [F.ERR_SUBSCRIBE_REQUEST_INVALID]: {desc: "ERR_SUBSCRIBE_REQUEST_INVALID", action: "failed"},
        [F.ERR_NOT_SUPPORTED_MESSAGE]: {desc: "ERR_NOT_SUPPORTED_MESSAGE", action: "failed"},
        [F.ERR_ILLEAGAL_PLUGIN]: {desc: "ERR_ILLEAGAL_PLUGIN", action: "failed"},
        [F.ILLEGAL_CLIENT_ROLE_LEVEL]: {desc: "ILLEGAL_CLIENT_ROLE_LEVEL", action: "failed"},
        [F.ERR_REJOIN_TOKEN_INVALID]: {desc: "ERR_REJOIN_TOKEN_INVALID", action: "failed"},
        [F.ERR_REJOIN_USER_NOT_JOINED]: {desc: "ERR_REJOIN_NOT_JOINED", action: "failed"},
        [F.ERR_INVALID_OPTIONAL_INFO]: {desc: "ERR_INVALID_OPTIONAL_INFO", action: "quit"},
        [F.ERR_TEST_RECOVER]: {desc: "ERR_TEST_RECOVER", action: "recover"},
        [F.ERR_TEST_TRYNEXT]: {desc: "ERR_TEST_TRYNEXT", action: "recover"},
        [F.ERR_TEST_RETRY]: {desc: "ERR_TEST_RETRY", action: "recover"},
        [F.ILLEGAL_AES_PASSWORD]: {desc: "ERR_TEST_RETRY", action: "failed"}
    }, Ra = {timeout: 500, timeoutFactor: 1.5, maxRetryCount: 1 / 0, maxRetryTimeout: 1E4};

    class Vf extends Ta {
        constructor(a,
                    b) {
            super();
            this.currentURLIndex = this.connectionID = 0;
            this.reconnectMode = "tryNext";
            this._state = "closed";
            this.reconnectCount = 0;
            this.name = a;
            this.retryConfig = b
        }

        get url() {
            return this.websocket ? this.websocket.url : null
        }

        get state() {
            return this._state
        }

        set state(a) {
            a !== this._state && (this._state = a, "reconnecting" === this._state ? this.emit(S.RECONNECTING, this.reconnectReason) : "connected" === this._state ? this.emit(S.CONNECTED) : "closed" === this._state ? this.emit(S.CLOSED) : "failed" === this._state && this.emit(S.FAILED))
        }

        init(a) {
            return new v((b,
                          c) => {
                this.urls = a;
                let e = this.urls[this.currentURLIndex];
                this.state = "connecting";
                this.createWebSocketConnection(e).then(b).catch(c);
                this.once(S.CLOSED, () => c(new p(n.WS_DISCONNECT)));
                this.once(S.CONNECTED, () => b())
            })
        }

        close(a, b) {
            if (this.currentURLIndex = 0, this.reconnectCount = 0, this.websocket) {
                this.websocket.onclose = null;
                this.websocket.onopen = null;
                this.websocket.onmessage = null;
                let a = this.websocket;
                b ? Fc(() => a.close(), 500) : a.close();
                this.websocket = void 0
            }
            this.state = a ? "failed" : "closed"
        }

        reconnect(a, b) {
            if (!this.websocket) return void k.warning("[".concat(this.name,
                "] can not reconnect, no websocket"));
            void 0 !== a && (this.reconnectMode = a);
            k.debug("[".concat(this.name, "] reconnect is triggered initiative"));
            a = this.websocket.onclose;
            this.websocket.onclose = null;
            this.websocket.close();
            a && ta(a).call(a, this.websocket)({code: 9999, reason: b})
        }

        sendMessage(a) {
            if (!this.websocket || this.websocket.readyState !== WebSocket.OPEN) throw new p(n.WS_ABORT, "websocket is not ready");
            a = A(a);
            try {
                this.websocket.send(a)
            } catch (b) {
                throw new p(n.WS_ERR, "send websocket message error" + b.toString());
            }
        }

        async createWebSocketConnection(a) {
            let b = this.connectionID += 1;
            return new v((c, e) => {
                var g, h;
                this.websocket && (this.websocket.onclose = null, this.websocket.close());
                u.GATEWAY_WSS_ADDRESS && td(g = this.name).call(g, "gateway") && (a = u.GATEWAY_WSS_ADDRESS);
                k.debug(l(h = "[".concat(this.name, "] start connect, url: ")).call(h, a));
                try {
                    this.websocket = new WebSocket(a), this.websocket.binaryType = "arraybuffer"
                } catch (r) {
                    var m;
                    g = new p(n.WS_ERR, "init websocket failed! Error: ".concat(r.toString()));
                    return k.error(l(m = "[".concat(this.name,
                        "]")).call(m, g)), void e(g)
                }
                yb(5E3).then(() => {
                    b === this.connectionID && this.websocket && this.websocket.readyState !== WebSocket.OPEN && this.websocket && this.websocket.close()
                });
                this.websocket.onopen = () => {
                    k.debug("[".concat(this.name, "] websocket opened:"), a);
                    this.reconnectMode = "retry";
                    this.state = "connected";
                    this.reconnectCount = 0;
                    c()
                };
                this.websocket.onclose = async a => {
                    var b, g, h, m;
                    if (k.debug(l(b = l(g = l(h = l(m = "[".concat(this.name, "] websocket close ")).call(m, this.websocket && this.websocket.url, ", code: ")).call(h,
                        a.code, ", reason: ")).call(g, a.reason, ", current mode: ")).call(b, this.reconnectMode)), this.reconnectCount < this.retryConfig.maxRetryCount) {
                        "connected" === this.state && (this.reconnectReason = a.reason, this.state = "reconnecting");
                        b = Xb(this, S.WILL_RECONNECT, this.reconnectMode) || this.reconnectMode;
                        b = await this.reconnectWithAction(b);
                        if ("closed" === this.state) return void k.debug("[".concat(this.connectionID, "] ws is closed, no need to reconnect"));
                        if (!b) return e(new p(n.WS_DISCONNECT, "websocket reconnect failed: ".concat(a.code))),
                            void this.close(!0);
                        c()
                    } else e(new p(n.WS_DISCONNECT, "websocket close: ".concat(a.code))), this.close()
                };
                this.websocket.onmessage = a => {
                    this.emit(S.ON_MESSAGE, a)
                }
            })
        }

        async reconnectWithAction(a, b) {
            var c, e;
            if (!b && this.reconnectCount >= this.retryConfig.maxRetryCount || !this.urls || "closed" === this.state) return !1;
            this.onlineReconnectListener || !navigator || void 0 === navigator.onLine || navigator.onLine || (this.onlineReconnectListener = new v(a => {
                let b = () => {
                    this.onlineReconnectListener = void 0;
                    window.removeEventListener("online",
                        b);
                    a()
                };
                window.addEventListener("online", b)
            }));
            b = function (a, b) {
                return Math.min(b.maxRetryTimeout, b.timeout * Math.pow(b.timeoutFactor, a))
            }(this.reconnectCount, this.retryConfig);
            if (k.debug(l(c = l(e = "[".concat(this.name, "] wait ")).call(e, b, "ms to reconnect websocket, mode: ")).call(c, a)), await v.race([yb(b), this.onlineReconnectListener || new v(() => {
            })]), "closed" === this.state) return !1;
            this.reconnectCount += 1;
            try {
                if ("retry" === a) await this.createWebSocketConnection(this.urls[this.currentURLIndex]); else if ("tryNext" ===
                    a) {
                    var g, h;
                    if (this.currentURLIndex += 1, this.currentURLIndex >= this.urls.length) return await this.reconnectWithAction("recover");
                    k.debug(l(g = l(h = "[".concat(this.name, "] websocket url length: ")).call(h, this.urls.length, " current index: ")).call(g, this.currentURLIndex));
                    await this.createWebSocketConnection(this.urls[this.currentURLIndex])
                } else "recover" === a && (k.debug("[".concat(this.name, "] request new urls")), this.urls = await Ma(this, S.REQUEST_NEW_URLS), this.currentURLIndex = 0, await this.createWebSocketConnection(this.urls[this.currentURLIndex]));
                return !0
            } catch (m) {
                return k.error("[".concat(this.name, "] reconnect failed"), m.toString()), await this.reconnectWithAction(a)
            }
        }
    }

    class eo {
        constructor(a) {
            this.input = [];
            this.size = a
        }

        add(a) {
            var b;
            (this.input.push(a), this.input.length > this.size) && Ia(b = this.input).call(b, 0, 1)
        }

        mean() {
            var a;
            return 0 === this.input.length ? 0 : rd(a = this.input).call(a, (a, c) => a + c) / this.input.length
        }
    }

    class fo extends Ta {
        constructor(a) {
            super();
            this._connectionState = ra.CLOSED;
            this.openConnectionTime = x();
            this.lastMsgTime = x();
            this.uploadCache =
                [];
            this.rttRolling = new eo(5);
            this.pingpongTimeoutCount = 0;
            this.onWebsocketMessage = a => {
                if (a.data instanceof ArrayBuffer) return void this.emit(Q.ON_BINARY_DATA, a.data);
                a = JSON.parse(a.data);
                if (this.lastMsgTime = x(), Object.prototype.hasOwnProperty.call(a, "_id")) {
                    let b = "res-@".concat(a._id);
                    this.emit(b, a._result, a._message)
                } else if (Object.prototype.hasOwnProperty.call(a, "_type") && (this.emit(a._type, a._message), a._type === U.ON_NOTIFICATION && this.handleNotification(a._message), a._type === U.ON_USER_BANNED)) switch (a._message.error_code) {
                    case 14:
                        this.close("UID_BANNED");
                        break;
                    case 15:
                        this.close("IP_BANNED");
                        break;
                    case 16:
                        this.close("CHANNEL_BANNED")
                }
            };
            this.clientId = a.clientId;
            this.spec = a;
            this.websocket = new Vf("gateway-".concat(this.clientId), this.spec.retryConfig);
            this.handleWebsocketEvents();
            window.addEventListener("offline", () => {
                this.connectionState === ra.CONNECTED && this.reconnect("retry", Sa.OFFLINE)
            })
        }

        get connectionState() {
            return this._connectionState
        }

        set connectionState(a) {
            a !== this._connectionState && (this._connectionState = a, a === ra.CONNECTED ? this.emit(Q.WS_CONNECTED) :
                a === ra.RECONNECTING ? this.emit(Q.WS_RECONNECTING, this._websocketReconnectReason) : a === ra.CLOSED && this.emit(Q.WS_CLOSED, this._disconnectedReason))
        }

        get currentURLIndex() {
            return this.websocket.currentURLIndex
        }

        get url() {
            return this.websocket ? this.websocket.url : null
        }

        get rtt() {
            return this.rttRolling.mean()
        }

        async request(a, b, c) {
            var e, g, h, m, r;
            let w = pa(6, "");
            var y = {_id: w, _type: a, _message: b};
            let q = this.websocket.connectionID;
            var B = () => new v((a, b) => {
                if (this.connectionState === ra.CONNECTED) return a();
                const c = () => {
                    this.off(Q.WS_CLOSED, e);
                    a()
                }, e = () => {
                    this.off(Q.WS_CONNECTED, c);
                    b(new p(n.WS_ABORT))
                };
                this.once(Q.WS_CONNECTED, c);
                this.once(Q.WS_CLOSED, e)
            });
            this.connectionState !== ra.CONNECTING && this.connectionState !== ra.RECONNECTING || a === da.JOIN || a === da.REJOIN || await B();
            var t = new v((c, e) => {
                let g = !1;
                const h = (e, h) => {
                    g = !0;
                    c({isSuccess: "success" === e, message: h || {}});
                    this.off(Q.WS_CLOSED, m);
                    this.off(Q.WS_RECONNECTING, m);
                    this.emit(Q.REQUEST_SUCCESS, a, b)
                };
                this.once("res-@".concat(w), h);
                const m = () => {
                    e(new p(n.WS_ABORT,
                        "type: ".concat(a)));
                    this.off(Q.WS_CLOSED, m);
                    this.off(Q.WS_RECONNECTING, m);
                    this.off("res-@".concat(w), h)
                };
                this.once(Q.WS_CLOSED, m);
                this.once(Q.WS_RECONNECTING, m);
                yb(u.SIGNAL_REQUEST_TIMEOUT).then(() => {
                    this.websocket.connectionID !== q || g || (k.warning("ws request timeout, type: ".concat(a)), this.emit(Q.REQUEST_TIMEOUT, a, b))
                })
            });
            this.websocket.sendMessage(y);
            y = null;
            try {
                y = await t
            } catch (z) {
                if (this.connectionState === ra.CLOSED || a === da.LEAVE) throw new p(n.WS_ABORT);
                return !this.spec.forceWaitGatewayResponse ||
                c ? z.throw() : a === da.JOIN || a === da.REJOIN ? null : (await B(), await this.request(a, b))
            }
            if (y.isSuccess) return y.message;
            c = Number(y.message.error_code || y.message.code);
            B = sh(c);
            t = new p(n.UNEXPECTED_RESPONSE, l(e = "".concat(B.desc, ": ")).call(e, y.message.error_str), {
                code: c,
                data: y.message
            });
            return "success" === B.action ? y.message : (k.warning(l(g = l(h = l(m = l(r = "[".concat(this.websocket.connectionID, "] unexpected response from type ")).call(r, a, ", error_code: ")).call(m, c, ", message: ")).call(h, B.desc, ", action: ")).call(g,
                B.action)), "failed" === B.action ? t.throw() : "quit" === B.action ? (this.initError = t, this.close(), t.throw()) : (c === F.ERR_JOIN_BY_MULTI_IP ? (this.multiIpOption = y.message.option, k.warning("[".concat(this.clientId, "] detect multi ip, recover")), this.reconnect("recover", Sa.MULTI_IP)) : this.reconnect(B.action, Sa.SERVER_ERROR), a === da.JOIN || a === da.REJOIN ? null : await this.request(a, b)))
        }

        waitMessage(a, b) {
            return new v(c => {
                let e = g => {
                    b && !b(g) || (this.off(a, e), c(g))
                };
                this.on(a, e)
            })
        }

        upload(a, b) {
            a = {_type: a, _message: b};
            try {
                this.websocket.sendMessage(a)
            } catch (e) {
                k.info("[".concat(this.clientId,
                    "] upload failed, cache message"), e);
                b = u.MAX_UPLOAD_CACHE || 50;
                var c;
                (this.uploadCache.push(a), this.uploadCache.length > b) && Ia(c = this.uploadCache).call(c, 0, 1);
                0 < this.uploadCache.length && !this.uploadCacheInterval && (this.uploadCacheInterval = window.setInterval(() => {
                    var a;
                    if (this.connectionState === ra.CONNECTED) {
                        var b = Ia(a = this.uploadCache).call(a, 0, 1)[0];
                        0 === this.uploadCache.length && (window.clearInterval(this.uploadCacheInterval), this.uploadCacheInterval = void 0);
                        this.upload(b._type, b._message)
                    }
                }, u.UPLOAD_CACHE_INTERVAL ||
                    2E3))
            }
        }

        send(a, b) {
            this.websocket.sendMessage({_type: a, _message: b})
        }

        init(a) {
            return this.initError = void 0, this.multiIpOption = void 0, this.joinResponse = void 0, this.reconnectToken = void 0, new v((b, c) => {
                this.once(Q.WS_CONNECTED, () => b(this.joinResponse));
                this.once(Q.WS_CLOSED, () => c(this.initError || new p(n.WS_ABORT)));
                this.connectionState = ra.CONNECTING;
                this.websocket.init(a).catch(c)
            })
        }

        close(a) {
            this.pingpongTimer && (this.pingpongTimeoutCount = 0, window.clearInterval(this.pingpongTimer), this.pingpongTimer = void 0);
            this.joinResponse = this.reconnectToken = void 0;
            this._disconnectedReason = a || "LEAVE";
            this.connectionState = ra.CLOSED;
            this.websocket.close()
        }

        async join() {
            var a;
            if (!this.joinResponse) {
                var b = Nc(this, Q.REQUEST_JOIN_INFO);
                b = await this.request(da.JOIN, b);
                if (!b) return this.emit(Q.REPORT_JOIN_GATEWAY, n.TIMEOUT, this.url || ""), !1;
                this.joinResponse = b;
                this.reconnectToken = this.joinResponse.rejoin_token
            }
            return this.connectionState = ra.CONNECTED, this.pingpongTimer && window.clearInterval(this.pingpongTimer), this.pingpongTimer =
                window.setInterval(ta(a = this.handlePingPong).call(a, this), 3E3), !0
        }

        async rejoin() {
            var a, b;
            if (!this.reconnectToken) throw new p(n.UNEXPECTED_ERROR, "can not rejoin, no rejoin token");
            var c = Nc(this, Q.REQUEST_REJOIN_INFO);
            c.token = this.reconnectToken;
            c = await this.request(da.REJOIN, c);
            return !!c && (this.connectionState = ra.CONNECTED, this.pingpongTimer && window.clearInterval(this.pingpongTimer), this.pingpongTimer = window.setInterval(ta(a = this.handlePingPong).call(a, this), 3E3), c.peers && q(b = c.peers).call(b, a => {
                this.emit(U.ON_USER_ONLINE,
                    {uid: a.uid});
                a.audio_mute ? this.emit(U.MUTE_AUDIO, {uid: a.uid}) : this.emit(U.UNMUTE_AUDIO, {uid: a.uid});
                a.video_mute ? this.emit(U.MUTE_VIDEO, {uid: a.uid}) : this.emit(U.UNMUTE_VIDEO, {uid: a.uid});
                a.audio_enable_local ? this.emit(U.ENABLE_LOCAL_AUDIO, {uid: a.uid}) : this.emit(U.DISABLE_LOCAL_AUDIO, {uid: a.uid});
                a.video_enable_local ? this.emit(U.ENABLE_LOCAL_VIDEO, {uid: a.uid}) : this.emit(U.DISABLE_LOCAL_VIDEO, {uid: a.uid});
                a.audio || a.video || this.emit(U.ON_REMOVE_STREAM, {uid: a.uid, uint_id: a.uint_id});
                a.audio && this.emit(U.ON_ADD_AUDIO_STREAM,
                    {uid: a.uid, uint_id: a.uint_id, audio: !0});
                a.video && this.emit(U.ON_ADD_VIDEO_STREAM, {uid: a.uid, uint_id: a.uint_id, video: !0})
            }), !0)
        }

        reconnect(a, b) {
            this.pingpongTimer && (this.pingpongTimeoutCount = 0, window.clearInterval(this.pingpongTimer), this.pingpongTimer = void 0);
            this.websocket.reconnect(a, b)
        }

        handleNotification(a) {
            k.debug("[".concat(this.clientId, "] receive notification: "), a);
            a = sh(a.code);
            if ("success" !== a.action) {
                if ("failed" !== a.action) return "quit" === a.action ? ("ERR_REPEAT_JOIN_CHANNEL" === a.desc && this.close("UID_BANNED"),
                    void this.close()) : void this.reconnect(a.action, Sa.SERVER_ERROR);
                k.error("[".concat(this.clientId, "] ignore error: "), a.desc)
            }
        }

        handlePingPong() {
            if (this.websocket && "connected" === this.websocket.state) {
                0 < this.pingpongTimeoutCount && this.rttRolling.add(3E3);
                this.pingpongTimeoutCount += 1;
                var a = u.PING_PONG_TIME_OUT, b = x();
                this.pingpongTimeoutCount >= a && (k.warning("PINGPONG Timeout. Last Socket Message: ".concat(b - this.lastMsgTime, "ms")), b - this.lastMsgTime > u.WEBSOCKET_TIMEOUT_MIN) ? this.reconnect("retry", Sa.TIMEOUT) :
                    this.request(da.PING, void 0, !0).then(() => {
                        this.pingpongTimeoutCount = 0;
                        let a = x() - b;
                        this.rttRolling.add(a);
                        u.REPORT_STATS && this.send(da.PING_BACK, {pingpongElapse: a})
                    }).catch(a => {
                    })
            }
        }

        handleWebsocketEvents() {
            this.websocket.on(S.ON_MESSAGE, this.onWebsocketMessage);
            this.websocket.on(S.CLOSED, () => {
                this.connectionState = ra.CLOSED
            });
            this.websocket.on(S.FAILED, () => {
                this._disconnectedReason = "NETWORK_ERROR";
                this.connectionState = ra.CLOSED
            });
            this.websocket.on(S.RECONNECTING, a => {
                this._websocketReconnectReason = a;
                this.joinResponse = void 0;
                this.connectionState === ra.CONNECTED ? this.connectionState = ra.RECONNECTING : this.connectionState = ra.CONNECTING
            });
            this.websocket.on(S.WILL_RECONNECT, (a, b) => {
                if (Nc(this, Q.IS_P2P_DISCONNECTED) && "retry" === a) return this.reconnectToken = void 0, this.emit(Q.NEED_RENEW_SESSION), this.emit(Q.DISCONNECT_P2P), b("tryNext");
                "retry" !== a && (this.reconnectToken = void 0, this.emit(Q.NEED_RENEW_SESSION), this.emit(Q.DISCONNECT_P2P));
                b(a)
            });
            this.websocket.on(S.CONNECTED, () => {
                this.openConnectionTime = x();
                this.reconnectToken ? this.rejoin().catch(a => {
                    var b;
                    k.warning(l(b = "[".concat(this.clientId, "] rejoin failed ")).call(b, a));
                    this.reconnect("tryNext", Sa.SERVER_ERROR)
                }) : this.join().catch(a => {
                    if (this.emit(Q.REPORT_JOIN_GATEWAY, a.code, this.url || ""), a instanceof p && a.code === n.UNEXPECTED_RESPONSE && a.data.code === F.ERR_NO_AUTHORIZED) return k.warning("[".concat(this.clientId, "] reconnect no authorized, recover")), void this.reconnect("recover", Sa.SERVER_ERROR);
                    k.error("[".concat(this.clientId, "] join gateway request failed"),
                        a.toString());
                    this.spec.forceWaitGatewayResponse ? this.reconnect("tryNext", Sa.SERVER_ERROR) : (this.initError = a, this.close())
                })
            });
            this.websocket.on(S.REQUEST_NEW_URLS, (a, b) => {
                Ma(this, Q.REQUEST_RECOVER, this.multiIpOption).then(a).catch(b)
            })
        }
    }

    class sk extends Ta {
        constructor(a, b) {
            super();
            this._hints = [];
            this._ID = b || pa(8, "track-");
            this._mediaStreamTrack = this._originMediaStreamTrack = a
        }

        toString() {
            return this._ID
        }

        getTrackId() {
            return this._ID
        }

        getMediaStreamTrack() {
            return this._mediaStreamTrack
        }
    }

    class ke extends sk {
        constructor(a,
                    b) {
            super(a, b);
            this._enabled = !0;
            this._isClosed = !1;
            this._trackProcessors = [];
            this._handleTrackEnded = () => {
                k.debug("[".concat(this.getTrackId, "] track ended"));
                this.emit(gd.TRACK_ENDED)
            };
            this._enabledMutex = new Ob("".concat(b));
            a.addEventListener("ended", this._handleTrackEnded)
        }

        getTrackLabel() {
            return this._originMediaStreamTrack.label
        }

        close() {
            var a;
            this._isClosed || (this.stop(), q(a = this._trackProcessors).call(a, a => a.destroy()), this._trackProcessors = [], this._originMediaStreamTrack.stop(), this._mediaStreamTrack !==
            this._originMediaStreamTrack && (this._mediaStreamTrack.stop(), this._mediaStreamTrack = null), this._originMediaStreamTrack = null, this._enabledMutex = null, k.debug("[".concat(this.getTrackId(), "] close")), this.emit(L.NEED_CLOSE), this._isClosed = !0)
        }

        async _registerTrackProcessor(a) {
            var b;
            if (-1 === G(b = this._trackProcessors).call(b, a)) {
                var c = this._trackProcessors[this._trackProcessors.length - 1];
                this._trackProcessors.push(a);
                a.onOutputChange = async () => {
                    this._mediaStreamTrack = a.output || this._originMediaStreamTrack;
                    this._updatePlayerSource();
                    await Qa(this, L.NEED_REPLACE_TRACK, this._mediaStreamTrack)
                };
                c ? (c.onOutputChange = async () => {
                    c.output && await a.setInput(c.output)
                }, await a.setInput(c.output || c.input || this._originMediaStreamTrack)) : await a.setInput(this._originMediaStreamTrack)
            }
        }

        _getOutputFromProcessors() {
            if (0 === this._trackProcessors.length) return this._originMediaStreamTrack;
            let a = this._trackProcessors[this._trackProcessors.length - 1];
            return a.output || a.input || this._originMediaStreamTrack
        }

        async _updateOriginMediaStreamTrack(a,
                                            b) {
            a !== this._originMediaStreamTrack && ((this._originMediaStreamTrack.removeEventListener("ended", this._handleTrackEnded), b && this._originMediaStreamTrack.stop(), a.addEventListener("ended", this._handleTrackEnded), this._originMediaStreamTrack = a, 0 < this._trackProcessors.length) ? (await this._trackProcessors[0].setInput(a), this._mediaStreamTrack = this._getOutputFromProcessors()) : this._mediaStreamTrack = this._originMediaStreamTrack, this._updatePlayerSource(), await Qa(this, L.NEED_REPLACE_TRACK, this._mediaStreamTrack))
        }

        _getDefaultPlayerConfig() {
            return {}
        }
    }

    let th = window.AudioContext || window.webkitAudioContext, pc = null, Qc = new Ta, Fe = null;

    class tk extends Ta {
        constructor() {
            super();
            this.isPlayed = !1;
            this.audioOutputLevel = this.audioLevelBase = 0;
            this.audioOutputLevelCache = null;
            this.audioOutputLevelCacheMaxLength = u.AUDIO_SOURCE_AVG_VOLUME_DURATION / u.AUDIO_SOURCE_VOLUME_UPDATE_INTERVAL || 15;
            this.isDestroyed = !1;
            this._noAudioInputCount = 0;
            this.context = Rc();
            this.playNode = this.context.destination;
            this.outputNode = this.context.createGain();
            Sc(this.outputNode);
            this.analyserNode =
                this.context.createAnalyser()
        }

        get isNoAudioInput() {
            return 3 <= this.noAudioInputCount
        }

        get noAudioInputCount() {
            return this._noAudioInputCount
        }

        set noAudioInputCount(a) {
            3 > this._noAudioInputCount && 3 <= a ? this.onNoAudioInput && this.onNoAudioInput() : 3 <= this._noAudioInputCount && 0 == this._noAudioInputCount % 10 && this.onNoAudioInput && this.onNoAudioInput();
            this._noAudioInputCount = a
        }

        startGetAudioBuffer(a) {
            this.audioBufferNode || (this.audioBufferNode = this.context.createScriptProcessor(a), this.outputNode.connect(this.audioBufferNode),
                this.audioBufferNode.connect(this.context.destination), this.audioBufferNode.onaudioprocess = a => {
                this.emit(jb.ON_AUDIO_BUFFER, function (a) {
                    for (let b = 0; b < a.outputBuffer.numberOfChannels; b += 1) {
                        let c = a.outputBuffer.getChannelData(b);
                        for (let a = 0; a < c.length; a += 1) c[a] = 0
                    }
                    return a.inputBuffer
                }(a))
            })
        }

        stopGetAudioBuffer() {
            this.audioBufferNode && (this.audioBufferNode.onaudioprocess = null, this.outputNode.disconnect(this.audioBufferNode), this.audioBufferNode = void 0)
        }

        createOutputTrack() {
            if (!ca.webAudioMediaStreamDest) throw new p(n.NOT_SUPPORTED,
                "your browser is not support audio processor");
            return this.destNode && this.outputTrack || (this.destNode = this.context.createMediaStreamDestination(), this.outputNode.connect(this.destNode), this.outputTrack = this.destNode.stream.getAudioTracks()[0]), this.outputTrack
        }

        play(a) {
            "running" !== this.context.state && ab(() => {
                Qc.emit("autoplay-failed")
            });
            this.isPlayed = !0;
            this.playNode = a || this.context.destination;
            this.outputNode.connect(this.playNode)
        }

        stop() {
            if (this.isPlayed) try {
                this.outputNode.disconnect(this.playNode)
            } catch (a) {
            }
            this.isPlayed =
                !1
        }

        getAudioLevel() {
            return this.audioOutputLevel
        }

        getAccurateVolumeLevel() {
            let a = new Uint8Array(this.analyserNode.frequencyBinCount);
            this.analyserNode.getByteFrequencyData(a);
            let b = 0;
            for (let c = 0; c < a.length; c++) b += a[c];
            return b / a.length
        }

        getAudioAvgLevel() {
            var a;
            null === this.audioOutputLevelCache && (this.audioOutputLevelCache = [this.audioOutputLevel]);
            return rd(a = this.audioOutputLevelCache).call(a, (a, c) => a + c) / this.audioOutputLevelCache.length
        }

        getAudioVolume() {
            return this.outputNode.gain.value
        }

        setVolume(a) {
            this.outputNode.gain.setValueAtTime(a,
                this.context.currentTime)
        }

        setMute(a) {
            a ? (this.disconnect(), this.audioLevelBase = 0, this.audioOutputLevel = 0) : this.connect()
        }

        destroy() {
            this.disconnect();
            this.stop();
            this.isDestroyed = !0;
            this.onNoAudioInput = void 0
        }

        disconnect() {
            this.sourceNode && this.sourceNode.disconnect();
            this.outputNode && this.outputNode.disconnect();
            window.clearInterval(this.updateAudioOutputLevelInterval)
        }

        connect() {
            var a;
            this.sourceNode && this.sourceNode.connect(this.outputNode);
            this.outputNode.connect(this.analyserNode);
            this.updateAudioOutputLevelInterval =
                window.setInterval(ta(a = this.updateAudioOutputLevel).call(a, this), u.AUDIO_SOURCE_VOLUME_UPDATE_INTERVAL || 400)
        }

        updateAudioOutputLevel() {
            if (this.context && "running" !== this.context.state && this.context.resume(), this.analyserNode) {
                if (this.analyserNode.getFloatTimeDomainData) {
                    var a = new Float32Array(this.analyserNode.frequencyBinCount);
                    this.analyserNode.getFloatTimeDomainData(a)
                } else {
                    var b;
                    a = new Uint8Array(this.analyserNode.frequencyBinCount);
                    this.analyserNode.getByteTimeDomainData(a);
                    let c = !0;
                    a = new Float32Array(C(b =
                        Ib(a)).call(b, a => (128 !== a && (c = !1), .0078125 * (a - 128))));
                    c ? this.noAudioInputCount += 1 : this.noAudioInputCount = 0
                }
                for (b = 0; b < a.length; b += 1) Math.abs(a[b]) > this.audioLevelBase && (this.audioLevelBase = Math.abs(a[b]), 1 < this.audioLevelBase && (this.audioLevelBase = 1));
                this.audioOutputLevel = this.audioLevelBase;
                this.audioLevelBase /= 4;
                null !== this.audioOutputLevelCache && (this.audioOutputLevelCache.push(this.audioOutputLevel), this.audioOutputLevelCache.length > this.audioOutputLevelCacheMaxLength && this.audioOutputLevelCache.shift())
            }
        }
    }

    class uk extends tk {
        constructor(a, b) {
            if (super(), this.isCurrentTrackCloned = !1, this.isRemoteTrack = !1, this.rebuildWebAudio = () => {
                if (!this.isNoAudioInput || this.isDestroyed) return document.body.removeEventListener("click", this.rebuildWebAudio, !0), void k.debug("rebuild web audio success, current volume", this.getAudioLevel());
                this.context.resume().then(() => k.info("resume success"));
                k.debug("rebuild web audio because of ios 12 bugs");
                this.disconnect();
                var a = this.track;
                this.track = this.track.clone();
                this.isCurrentTrackCloned ?
                    a.stop() : this.isCurrentTrackCloned = !0;
                a = new MediaStream([this.track]);
                this.sourceNode = this.context.createMediaStreamSource(a);
                Sc(this.sourceNode);
                this.analyserNode = this.context.createAnalyser();
                let b = this.outputNode.gain.value;
                this.outputNode = this.context.createGain();
                this.outputNode.gain.setValueAtTime(b, this.context.currentTime);
                Sc(this.outputNode);
                this.connect();
                this.audioElement.srcObject = a;
                this.isPlayed && this.play(this.playNode)
            }, "audio" !== a.kind) throw new p(n.UNEXPECTED_ERROR);
            this.track = a;
            a = new MediaStream([this.track]);
            this.isRemoteTrack = !!b;
            this.sourceNode = this.context.createMediaStreamSource(a);
            Sc(this.sourceNode);
            this.connect();
            this.audioElement = document.createElement("audio");
            this.audioElement.srcObject = a;
            a = ka();
            b && a.os === W.IOS && (Qc.on("state-change", this.rebuildWebAudio), this.onNoAudioInput = () => {
                document.body.addEventListener("click", this.rebuildWebAudio, !0);
                this.rebuildWebAudio();
                document.body.click()
            })
        }

        get isFreeze() {
            return !1
        }

        updateTrack(a) {
            this.sourceNode.disconnect();
            this.track =
                a;
            this.isCurrentTrackCloned = !1;
            a = new MediaStream([a]);
            this.sourceNode = this.context.createMediaStreamSource(a);
            Sc(this.sourceNode);
            this.sourceNode.connect(this.outputNode);
            this.audioElement.srcObject = a
        }

        destroy() {
            this.audioElement.remove();
            Qc.off("state-change", this.rebuildWebAudio);
            super.destroy()
        }
    }

    let lb = new class {
        constructor() {
            this.elementMap = new ba;
            this.elementsNeedToResume = [];
            this.sinkIdMap = new ba;
            this.autoResumeAudioElement()
        }

        async setSinkID(a, b) {
            const c = this.elementMap.get(a);
            if (this.sinkIdMap.set(a,
                b), c) try {
                await c.setSinkId(b)
            } catch (e) {
                throw new p(n.PERMISSION_DENIED, "can not set sink id: " + e.toString());
            }
        }

        play(a, b) {
            if (!this.elementMap.has(b)) {
                var c = document.createElement("audio");
                c.autoplay = !0;
                c.srcObject = new MediaStream([a]);
                this.elementMap.set(b, c);
                (a = this.sinkIdMap.get(b)) && c.setSinkId(a).catch(a => {
                    k.warning("[".concat(b, "] set sink id failed"), a.toString())
                });
                (a = c.play()) && a.then && a.catch(a => {
                    k.warning("audio element play warning", a.toString());
                    this.elementMap.has(b) && "NotAllowedError" ===
                    a.name && (k.warning("detected audio element autoplay failed"), this.elementsNeedToResume.push(c), ab(() => {
                        this.onAutoplayFailed && this.onAutoplayFailed()
                    }))
                })
            }
        }

        updateTrack(a, b) {
            (a = this.elementMap.get(a)) && (a.srcObject = new MediaStream([b]))
        }

        isPlaying(a) {
            return this.elementMap.has(a)
        }

        setVolume(a, b) {
            (a = this.elementMap.get(a)) && (b = Math.max(0, Math.min(100, b)), a.volume = b / 100)
        }

        stop(a) {
            var b, c;
            const e = this.elementMap.get(a);
            if (this.sinkIdMap.delete(a), e) {
                var g = G(b = this.elementsNeedToResume).call(b, e);
                Ia(c = this.elementsNeedToResume).call(c,
                    g, 1);
                e.srcObject = null;
                e.remove();
                this.elementMap.delete(a)
            }
        }

        autoResumeAudioElement() {
            const a = () => {
                var a;
                q(a = this.elementsNeedToResume).call(a, a => {
                    a.play().then(a => {
                        k.debug("Auto resume audio element success")
                    }).catch(a => {
                        k.warning("Auto resume audio element failed!", a)
                    })
                });
                this.elementsNeedToResume = []
            };
            (new v(a => {
                document.body ? a() : window.addEventListener("load", () => a())
            })).then(() => {
                document.body.addEventListener("touchstart", a, !0);
                document.body.addEventListener("mousedown", a, !0)
            })
        }
    };

    class $a extends ke {
        constructor(a,
                    b, c) {
            super(a, c);
            this.trackMediaType = "audio";
            this._enabled = !0;
            this._useAudioElement = !1;
            this._encoderConfig = b;
            this._source = new uk(a);
            ca.webAudioWithAEC || (this._useAudioElement = !0)
        }

        get isPlaying() {
            return this._useAudioElement ? lb.isPlaying(this.getTrackId()) : this._source.isPlayed
        }

        setVolume(a) {
            V(a, "volume", 0, 1E3);
            let b = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.LOCAL_AUDIO_TRACK_SET_VOLUME,
                options: [this.getTrackId(), a]
            }, 300);
            this._source.setVolume(a / 100);
            try {
                let a = this._source.createOutputTrack();
                this._mediaStreamTrack !==
                a && (this._mediaStreamTrack = a, Qa(this, L.NEED_REPLACE_TRACK, this._mediaStreamTrack).then(() => {
                    k.debug("[".concat(this.getTrackId(), "] replace web audio track success"))
                }).catch(a => {
                    k.warning("[".concat(this.getTrackId(), "] replace web audio track failed"), a)
                }))
            } catch (c) {
            }
            b.onSuccess()
        }

        getVolumeLevel() {
            return this._source.getAudioLevel()
        }

        async setPlaybackDevice(a) {
            let b = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.REMOTE_AUDIO_SET_OUTPUT_DEVICE,
                options: [this.getTrackId(), a]
            });
            if (!this._useAudioElement) throw new p(n.NOT_SUPPORTED,
                "your browser does not support setting the audio output device");
            try {
                await lb.setSinkID(this.getTrackId(), a)
            } catch (c) {
                throw b.onError(c), c;
            }
            b.onSuccess()
        }

        async setEnabled(a) {
            var b;
            if (a !== this._enabled) {
                k.info("[".concat(this.getTrackId(), "] start setEnabled"), a);
                var c = await this._enabledMutex.lock();
                if (!a) {
                    this._originMediaStreamTrack.enabled = !1;
                    try {
                        await Qa(this, L.NEED_REMOVE_TRACK, this)
                    } catch (e) {
                        throw k.error("[".concat(this.getTrackId(), "] setEnabled to false error"), e.toString()), c(), e;
                    }
                    return this._enabled =
                        !1, c()
                }
                this._originMediaStreamTrack.enabled = !0;
                try {
                    await Qa(this, L.NEED_ADD_TRACK, this)
                } catch (e) {
                    throw k.error("[".concat(this.getTrackId(), "] setEnabled to true error"), e.toString()), c(), e;
                }
                k.info(l(b = "[".concat(this.getTrackId(), "] setEnabled to ")).call(b, a, " success"));
                this._enabled = !0;
                c()
            }
        }

        getStats() {
            Pc(() => {
                k.warning("[deprecated] LocalAudioTrack.getStats will be removed in the future, use AgoraRTCClient.getLocalAudioStats instead")
            }, "localAudioTrackGetStatsWarning");
            return Xb(this, L.GET_STATS) ||
                He({}, he)
        }

        setAudioFrameCallback(a, b = 4096) {
            if (!a) return this._source.removeAllListeners(jb.ON_AUDIO_BUFFER), void this._source.stopGetAudioBuffer();
            this._source.startGetAudioBuffer(b);
            this._source.removeAllListeners(jb.ON_AUDIO_BUFFER);
            this._source.on(jb.ON_AUDIO_BUFFER, b => a(b))
        }

        play() {
            let a = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.LOCAL_AUDIO_TRACK_PLAY,
                options: [this.getTrackId()]
            });
            k.debug("[".concat(this.getTrackId(), "] start audio playback"));
            this._useAudioElement ? (k.debug("[".concat(this.getTrackId(),
                "] start audio playback in element")), lb.play(this._mediaStreamTrack, this.getTrackId())) : this._source.play();
            a.onSuccess()
        }

        stop() {
            let a = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.LOCAL_AUDIO_TRACK_STOP,
                options: [this.getTrackId()]
            });
            k.debug("[".concat(this.getTrackId(), "] stop audio playback"));
            this._useAudioElement ? lb.stop(this.getTrackId()) : this._source.stop();
            a.onSuccess()
        }

        close() {
            super.close();
            this._source.destroy()
        }

        _updatePlayerSource() {
            k.debug("[track-".concat(this.getTrackId(), "] update player source track"));
            this._source.updateTrack(this._mediaStreamTrack);
            this._useAudioElement && lb.updateTrack(this.getTrackId(), this._mediaStreamTrack)
        }

        async _updateOriginMediaStreamTrack(a, b) {
            this._originMediaStreamTrack !== a && (this._originMediaStreamTrack.removeEventListener("ended", this._handleTrackEnded), a.addEventListener("ended", this._handleTrackEnded), b && this._originMediaStreamTrack.stop(), this._originMediaStreamTrack = a, this._source.updateTrack(this._originMediaStreamTrack), this._mediaStreamTrack !== this._source.outputTrack &&
            (this._mediaStreamTrack = this._originMediaStreamTrack, this._updatePlayerSource(), await Qa(this, L.NEED_REPLACE_TRACK, this._mediaStreamTrack)))
        }
    }

    class Wf extends $a {
        constructor(a, b, c, e) {
            super(a, b.encoderConfig ? wd(b.encoderConfig) : {}, e);
            this._deviceName = "default";
            this._enabled = !0;
            this._config = b;
            this._constraints = c;
            this._deviceName = a.label
        }

        async setDevice(a) {
            var b, c;
            let e = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.MIC_AUDIO_TRACK_SET_DEVICE,
                options: [this.getTrackId(), a]
            });
            if (k.info(l(b = "[".concat(this.getTrackId,
                "] start set device to ")).call(b, a)), this._enabled) try {
                let c = await db.getDeviceById(a);
                b = {};
                b.audio = He({}, this._constraints);
                b.audio.deviceId = {exact: a};
                this._originMediaStreamTrack.stop();
                let e = null;
                try {
                    e = await zb(b, this.getTrackId())
                } catch (m) {
                    throw k.error("[track-".concat(this.getTrackId(), "] setDevice failed"), m.toString()), e = await zb({video: this._constraints}, this.getTrackId()), await this._updateOriginMediaStreamTrack(e.getAudioTracks()[0], !1), m;
                }
                await this._updateOriginMediaStreamTrack(e.getAudioTracks()[0],
                    !1);
                this._deviceName = c.label;
                this._config.microphoneId = a;
                this._constraints.deviceId = {exact: a}
            } catch (g) {
                throw e.onError(g), k.error("[track-".concat(this.getTrackId(), "] setDevice error"), g.toString()), g;
            } else try {
                this._deviceName = (await db.getDeviceById(a)).label, this._config.microphoneId = a, this._constraints.deviceId = {exact: a}
            } catch (g) {
                throw e.onError(g), k.error("[track-".concat(this.getTrackId(), "] setDevice error"), g.toString()), g;
            }
            e.onSuccess();
            k.info(l(c = "[".concat(this.getTrackId, "] set device to ")).call(c,
                a, " success"))
        }

        async setEnabled(a, b) {
            if (b) return k.debug("[".concat(this.getTrackId, "] setEnabled false (do not close microphone)")), await super.setEnabled(a);
            if (a !== this._enabled) {
                k.info("[".concat(this.getTrackId(), "] start setEnabled"), a);
                b = await this._enabledMutex.lock();
                if (!a) {
                    this._originMediaStreamTrack.onended = null;
                    this._originMediaStreamTrack.stop();
                    this._enabled = !1;
                    try {
                        await Qa(this, L.NEED_REMOVE_TRACK, this)
                    } catch (e) {
                        throw k.error("[".concat(this.getTrackId(), "] setEnabled false failed"),
                            e.toString()), b(), e;
                    }
                    return void b()
                }
                a = He({}, this._constraints);
                var c = db.searchDeviceIdByName(this._deviceName);
                c && !a.deviceId && (a.deviceId = c);
                try {
                    let a = await zb({audio: this._constraints}, this.getTrackId());
                    await this._updateOriginMediaStreamTrack(a.getAudioTracks()[0], !1);
                    await Qa(this, L.NEED_ADD_TRACK, this)
                } catch (e) {
                    throw b(), k.error("[".concat(this.getTrackId(), "] setEnabled true failed"), e.toString()), e;
                }
                this._enabled = !0;
                k.info("[".concat(this.getTrackId(), "] setEnabled success"));
                b()
            }
        }
    }

    class go extends $a {
        constructor(a,
                    b, c, e) {
            super(b.createOutputTrack(), c, e);
            this.source = a;
            this._bufferSource = b;
            this._bufferSource.on(jb.AUDIO_SOURCE_STATE_CHANGE, a => {
                this.emit(gd.SOURCE_STATE_CHANGE, a)
            });
            try {
                this._mediaStreamTrack = this._source.createOutputTrack()
            } catch (g) {
            }
        }

        get currentState() {
            return this._bufferSource.currentState
        }

        get duration() {
            return this._bufferSource.duration
        }

        getCurrentTime() {
            return this._bufferSource.currentTime
        }

        startProcessAudioBuffer(a) {
            let b = t.reportApiInvoke(null, {
                tag: D.TRACER, name: E.BUFFER_AUDIO_TRACK_START,
                options: [this.getTrackId(), a, this.duration]
            });
            a && this._bufferSource.updateOptions(a);
            this._bufferSource.startProcessAudioBuffer();
            b.onSuccess()
        }

        pauseProcessAudioBuffer() {
            let a = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.BUFFER_AUDIO_TRACK_PAUSE,
                options: [this.getTrackId()]
            });
            this._bufferSource.pauseProcessAudioBuffer();
            a.onSuccess()
        }

        seekAudioBuffer(a) {
            let b = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.BUFFER_AUDIO_TRACK_SEEK,
                options: [this.getTrackId()]
            });
            this._bufferSource.seekAudioBuffer(a);
            b.onSuccess()
        }

        resumeProcessAudioBuffer() {
            let a =
                t.reportApiInvoke(null, {
                    tag: D.TRACER,
                    name: E.BUFFER_AUDIO_TRACK_RESUME,
                    options: [this.getTrackId()]
                });
            this._bufferSource.resumeProcessAudioBuffer();
            a.onSuccess()
        }

        stopProcessAudioBuffer() {
            let a = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.BUFFER_AUDIO_TRACK_STOP,
                options: [this.getTrackId()]
            });
            this._bufferSource.stopProcessAudioBuffer();
            a.onSuccess()
        }
    }

    class Gc extends $a {
        constructor() {
            let a = Rc().createMediaStreamDestination();
            super(a.stream.getAudioTracks()[0]);
            try {
                this._mediaStreamTrack = this._source.createOutputTrack()
            } catch (b) {
            }
            this.destNode =
                a;
            this.trackList = []
        }

        hasAudioTrack(a) {
            var b;
            return -1 !== G(b = this.trackList).call(b, a)
        }

        addAudioTrack(a) {
            var b;
            -1 === G(b = this.trackList).call(b, a) ? (k.debug("add ".concat(a.getTrackId(), " to mixing track")), a._source.outputNode.connect(this.destNode), this.trackList.push(a), this.updateEncoderConfig()) : k.warning("track is already added")
        }

        removeAudioTrack(a) {
            var b;
            if (-1 !== G(b = this.trackList).call(b, a)) {
                k.debug("remove ".concat(a.getTrackId(), " from mixing track"));
                try {
                    a._source.outputNode.disconnect(this.destNode)
                } catch (c) {
                }
                Oc(this.trackList,
                    a);
                this.updateEncoderConfig()
            }
        }

        updateEncoderConfig() {
            var a;
            let b = {};
            q(a = this.trackList).call(a, a => {
                a._encoderConfig && ((a._encoderConfig.bitrate || 0) > (b.bitrate || 0) && (b.bitrate = a._encoderConfig.bitrate), (a._encoderConfig.sampleRate || 0) > (b.sampleRate || 0) && (b.sampleRate = a._encoderConfig.sampleRate), (a._encoderConfig.sampleSize || 0) > (b.sampleSize || 0) && (b.sampleSize = a._encoderConfig.sampleSize), a._encoderConfig.stereo && (b.stereo = !0))
            });
            this._encoderConfig = b
        }
    }

    let ho = (a, b) => {
        var c = a.length;
        a = (e = a, (new TextEncoder).encode(e));
        var e;
        e = function (a, b) {
            if (0 === b) return a;
            const c = new a.constructor(a.length + b);
            b = new a.constructor(b);
            return c.set(a, 0), c.set(b, a.length), c
        }(a, (4 - a.length % 4) % 4);
        c = function (a, b, c) {
            c = new c(a.length + b.length);
            return c.set(a, 0), c.set(b, a.length), c
        }([c], new Uint32Array(e.buffer), Uint32Array);
        for (e = 0; e < c.length; e++) c[e] ^= b, c[e] = ~c[e];
        return Be(new Uint8Array(c.buffer))
    }, Xf = new ba;

    class io extends Ta {
        constructor(a) {
            super();
            this.inChannelInfo = {joinAt: null, duration: 0};
            this._state = "DISCONNECTED";
            this.needToSendUnpubUnsub =
                new ba;
            this.hasChangeBGPAddress = this.isSignalRecover = !1;
            this.joinGatewayStartTime = 0;
            this._signalTimeout = !1;
            this.clientId = a.clientId;
            this.spec = a;
            this.signal = new fo(Ie({}, a, {retryConfig: a.websocketRetryConfig}));
            this._statsCollector = a.statsCollector;
            this.role = a.role || "audience";
            this._clientRoleOptions = a.clientRoleOptions;
            this.handleSignalEvents()
        }

        get state() {
            return this._state
        }

        set state(a) {
            if (a !== this._state) {
                var b = this._state;
                this._state = a;
                "DISCONNECTED" === a && this._disconnectedReason ? this.emit(wa.CONNECTION_STATE_CHANGE,
                    a, b, this._disconnectedReason) : this.emit(wa.CONNECTION_STATE_CHANGE, a, b)
            }
        }

        async join(a, b) {
            var c, e;
            "disabled" !== a.cloudProxyServer && (this.hasChangeBGPAddress = !0);
            let g = x();
            var h = Xf.get(a.cname);
            if (h || (h = new ba, Xf.set(a.cname, h)), h.has(a.uid)) throw h = new p(n.UID_CONFLICT), t.joinGateway(a.sid, {
                lts: g,
                succ: !1,
                ec: h.code,
                addr: null,
                uid: a.uid,
                cid: a.cid
            }), h;
            h.set(a.uid, !0);
            this.joinInfo = a;
            this.key = b;
            b = a.proxyServer ? C(c = a.gatewayAddrs).call(c, b => {
                var c, e;
                b = b.split(":");
                return l(c = l(e = "wss://".concat(a.proxyServer,
                    "/ws/?h=")).call(e, b[0], "&p=")).call(c, b[1])
            }) : C(e = a.gatewayAddrs).call(e, a => "wss://".concat(a));
            c = 0;
            this.joinGatewayStartTime = g;
            try {
                c = (await this.signal.init(b)).uid
            } catch (m) {
                throw k.error("[".concat(this.clientId, "] User join failed"), m.toString()), t.joinGateway(a.sid, {
                    lts: g,
                    succ: !1,
                    ec: m.code === n.UNEXPECTED_ERROR ? m.message : m.code,
                    addr: this.signal.url,
                    uid: a.uid,
                    cid: a.cid
                }), h.delete(a.uid), this.signal.close(), m;
            }
            return this.state = "CONNECTED", this.inChannelInfo.joinAt = x(), k.debug("[".concat(this.clientId,
                "] Connected to gateway server")), this.trafficStatsInterval = window.setInterval(() => {
                this.updateTrafficStats().catch(a => {
                    k.warning("[".concat(this.clientId, "] get traffic stats error"), a.toString())
                })
            }, 3E3), this.networkQualityInterval = window.setInterval(() => {
                navigator && void 0 !== navigator.onLine && !navigator.onLine ? this.emit(wa.NETWORK_QUALITY, {
                    downlinkNetworkQuality: 6,
                    uplinkNetworkQuality: 6
                }) : this._signalTimeout ? this.emit(wa.NETWORK_QUALITY, {
                    downlinkNetworkQuality: 5,
                    uplinkNetworkQuality: 5
                }) : "CONNECTED" ===
                this.state && this._statsCollector.trafficStats ? this.emit(wa.NETWORK_QUALITY, {
                    uplinkNetworkQuality: eh(this._statsCollector.trafficStats.B_unq),
                    downlinkNetworkQuality: eh(this._statsCollector.trafficStats.B_dnq)
                }) : this.emit(wa.NETWORK_QUALITY, {uplinkNetworkQuality: 0, downlinkNetworkQuality: 0})
            }, 2E3), c
        }

        async leave(a = !1) {
            if ("DISCONNECTED" !== this.state) {
                this.state = "DISCONNECTING";
                try {
                    if (!a && this.signal.connectionState === ra.CONNECTED) {
                        var b = this.signal.request(da.LEAVE, void 0, !0);
                        await (3E3 === 1 / 0 ? b : v.race([b,
                            gl(3E3)]))
                    }
                } catch (c) {
                    k.warning("[".concat(this.clientId, "] leave request failed, ignore"), c)
                }
                this.signal.close();
                this.reset();
                this.state = "DISCONNECTED"
            }
        }

        async publish(a, b) {
            if (!this.joinInfo) throw new p(n.UNEXPECTED_ERROR, "publish no joinInfo");
            let c = a.getUserId(), e = a.videoTrack ? function (a) {
                var b;
                a = a._encoderConfig;
                if (!a) return {};
                const c = {
                    resolution: a.width && a.height ? l(b = "".concat(fb(a.width), "x")).call(b, fb(a.height)) : void 0,
                    maxVideoBW: a.bitrateMax,
                    minVideoBW: a.bitrateMin
                };
                return "number" == typeof a.frameRate ?
                    (c.maxFrameRate = a.frameRate, c.minFrameRate = a.frameRate) : a.frameRate && (c.maxFrameRate = a.frameRate.max || a.frameRate.ideal || a.frameRate.exact || a.frameRate.min, c.minFrameRate = a.frameRate.min || a.frameRate.ideal || a.frameRate.exact || a.frameRate.max), c
            }(a.videoTrack) : {};
            if (a.on(I.NEED_ANSWER, (g, h, m) => {
                var l;
                let p = {
                    state: "offer",
                    stream_type: b,
                    p2p_id: a.pc.ID,
                    sdp: A(g),
                    audio: !!a.audioTrack,
                    video: !!a.videoTrack,
                    screen: a.videoTrack && -1 !== G(l = a.videoTrack._hints).call(l, tb.SCREEN_TRACK),
                    attributes: e,
                    dtx: a.audioTrack instanceof
                        Wf && a.audioTrack._config.DTX,
                    hq: !1,
                    lq: !1,
                    stereo: !1,
                    speech: !1,
                    mode: this.spec.mode,
                    codec: this.spec.codec,
                    extend: u.PUB_EXTEND
                };
                this.signal.request(da.PUBLISH, p, !0).then(a => {
                    c && this.needToSendUnpubUnsub.set(c, !0);
                    h(JSON.parse(a.sdp))
                }).catch(b => {
                    if (g.retry && b.data && b.data.code === F.ERR_PUBLISH_REQUEST_INVALID) return k.warning("[".concat(this.clientId, "] receiver publish error code, retry"), b.toString()), Ma(a, I.NEED_UNPUB).then(() => {
                        g.retry = !1;
                        Ma(a, I.NEED_ANSWER, g).then(h).catch(m)
                    });
                    b.code !== n.WS_ABORT &&
                    m(b)
                })
            }), a.on(I.NEED_RENEGOTIATE, (c, e, m) => {
                this.signal.request(da.PUBLISH, {
                    state: "negotiation",
                    stream_type: b,
                    p2p_id: a.pc.ID,
                    sdp: c
                }, !0).then(a => {
                    e(JSON.parse(a.sdp))
                }).catch(a => {
                    a.code !== n.WS_ABORT && m(a)
                })
            }), a.on(I.NEED_UNPUB, e => c && !this.needToSendUnpubUnsub.has(c) ? e(!1) : "RECONNECTING" === this.state ? e(!0) : void this.signal.request(da.UNPUBLISH, {
                stream_id: a.getUserId(),
                stream_type: b
            }, !0).then(() => e(!1)).catch(a => {
                k.warning("unpublish warning: ", a);
                e(!0)
            })), a.on(I.NEED_UPLOAD, (a, c) => {
                this.signal.upload(a,
                    {stream_type: b, stats: c})
            }), a.on(I.NEED_SIGNAL_RTT, a => {
                a(this.signal.rtt)
            }), "RECONNECTING" !== this.state) {
                if ("CONNECTED" !== this.state) return (new p(n.INVALID_OPERATION, "can not publish when connection state is ".concat(this.state))).throw();
                await a.startP2PConnection()
            } else a.readyToReconnectPC()
        }

        async subscribe(a) {
            if (!this.joinInfo) throw new p(n.UNEXPECTED_ERROR, "subscribe no joinInfo");
            let b = a.getUserId();
            if (a.on(I.NEED_ANSWER, (c, e, g) => {
                var h = a.subscribeOptions;
                h = {
                    stream_id: a.getUserId(),
                    audio: !!h.audio,
                    video: !!h.video,
                    mode: this.spec.mode,
                    codec: this.spec.codec,
                    p2p_id: a.pc.ID,
                    sdp: A(c),
                    tcc: !!u.SUBSCRIBE_TCC,
                    extend: u.SUB_EXTEND
                };
                this.signal.request(da.SUBSCRIBE, h, !0).then(a => {
                    this.needToSendUnpubUnsub.set(b, !0);
                    e(JSON.parse(a.sdp))
                }).catch(b => {
                    if (c.retry && b.data && b.data.code === F.ERR_SUBSCRIBE_REQUEST_INVALID) return k.warning("[".concat(this.clientId, "] receiver subscribe error code, retry"), b.toString()), Ma(a, I.NEED_UNSUB).then(() => {
                        c.retry = !1;
                        Ma(a, I.NEED_ANSWER, c).then(e).catch(g)
                    });
                    b.code !== n.WS_ABORT &&
                    g(b)
                })
            }), a.on(I.NEED_UNSUB, c => this.needToSendUnpubUnsub.has(b) ? "RECONNECTING" === this.state ? c(!0) : void this.signal.request(da.UNSUBSCRIBE, {stream_id: a.getUserId()}, !0).then(() => c(!1)).catch(a => {
                k.warning("unsubscribe warning", a);
                c(!0)
            }) : c(!1)), a.on(I.NEED_UPLOAD, (b, e) => {
                this.signal.upload(b, {stream_id: a.getUserId(), stats: e})
            }), a.on(I.NEED_SIGNAL_RTT, a => {
                a(this.signal.rtt)
            }), "RECONNECTING" !== this.state) {
                if ("CONNECTED" !== this.state) return (new p(n.INVALID_OPERATION, "can not subscribe when connection state is ".concat(this.state))).throw();
                await a.startP2PConnection()
            } else a.readyToReconnectPC()
        }

        async subscribeChange(a, b) {
            var c, e;
            if (!this.joinInfo) throw new p(n.UNEXPECTED_ERROR, "subscribe no joinInfo");
            if (await a.setSubscribeOptions(b), "RECONNECTING" !== this.state) {
                if ("CONNECTED" !== this.state) return (new p(n.INVALID_OPERATION, "can not subscribe change when connection state is ".concat(this.state))).throw();
                k.debug(l(c = l(e = "[".concat(this.clientId, "] send subscribe change, audio: ")).call(e, b.audio, ", video: ")).call(c, b.video));
                await this.signal.request(da.SUBSCRIBE_CHANGE,
                    {stream_id: a.getUserId(), audio: !!b.audio, video: !!b.video}, !0)
            }
        }

        async unsubscribe(a) {
            await a.closeP2PConnection()
        }

        getGatewayInfo() {
            return this.signal.request(da.GATEWAY_INFO)
        }

        renewToken(a) {
            return this.signal.request(da.RENEW_TOKEN, {token: a})
        }

        async setClientRole(a, b) {
            if (b && (this._clientRoleOptions = Ha({}, b)), "CONNECTED" !== this.state) return void (this.role = a);
            await this.signal.request(da.SET_CLIENT_ROLE, {
                role: a,
                level: "audience" === a ? this._clientRoleOptions && this._clientRoleOptions.level ? this._clientRoleOptions.level :
                    2 : 0
            });
            this.role = a
        }

        async setRemoteVideoStreamType(a, b) {
            await this.signal.request(da.SWITCH_VIDEO_STREAM, {stream_id: a, stream_type: b})
        }

        async setStreamFallbackOption(a, b) {
            await this.signal.request(da.SET_FALLBACK_OPTION, {stream_id: a, fallback_type: b})
        }

        async pickSVCLayer(a, b) {
            await this.signal.request(da.PICK_SVC_LAYER, {
                stream_id: a,
                spatial_layer: b.spatialLayer,
                temporal_layer: b.temporalLayer
            })
        }

        getInChannelInfo() {
            return this.inChannelInfo.joinAt && (this.inChannelInfo.duration = x() - this.inChannelInfo.joinAt),
                Ie({}, this.inChannelInfo)
        }

        async getGatewayVersion() {
            return (await this.signal.request(da.GATEWAY_INFO)).version
        }

        reset() {
            if (this.inChannelInfo.joinAt && (this.inChannelInfo.duration = x() - this.inChannelInfo.joinAt, this.inChannelInfo.joinAt = null), this.trafficStatsInterval && (window.clearInterval(this.trafficStatsInterval), this.trafficStatsInterval = void 0), this.joinInfo) {
                let a = Xf.get(this.joinInfo.cname);
                a && a.delete(this.joinInfo.uid)
            }
            this.needToSendUnpubUnsub = new ba;
            this.key = this.joinInfo = void 0;
            this.networkQualityInterval &&
            (window.clearInterval(this.networkQualityInterval), this.networkQualityInterval = void 0)
        }

        updateTurnConfigFromSignal() {
            if (this.joinInfo) {
                var a = (a = (("disabled" === this.joinInfo.cloudProxyServer ? this.signal.url : this.joinInfo.gatewayAddrs[this.signal.currentURLIndex]) || "").match(/(wss:\/\/)?([^:]+):(\d+)/)) ? {
                    username: Ga.username,
                    password: Ga.password,
                    turnServerURL: a[2],
                    tcpport: oa(a[3]) + 30,
                    udpport: oa(a[3]) + 30,
                    forceturn: !1
                } : null;
                var b, c;
                (this.joinInfo.turnServer.serversFromGateway = [], a && "off" !== this.joinInfo.turnServer.mode &&
                "disabled" === this.joinInfo.cloudProxyServer) && (ye(this.joinInfo.turnServer.serversFromGateway) ? this.joinInfo.turnServer.serversFromGateway.push({
                    credential: Ga.password,
                    credentialType: "password",
                    urls: l(b = "turn:".concat(a.turnServerURL, ":")).call(b, a.udpport, "?transport=udp"),
                    username: Ga.username
                }, {
                    credential: Ga.password,
                    credentialType: "password",
                    urls: l(c = "turn:".concat(a.turnServerURL, ":")).call(c, a.tcpport, "?transport=tcp"),
                    username: Ga.username
                }) : this.joinInfo.turnServer.serversFromGateway.push(Ie({},
                    Ga, {turnServerURL: a.turnServerURL, tcpport: a.tcpport, udpport: a.udpport})))
            }
        }

        async updateTrafficStats() {
            var a;
            if ("CONNECTED" === this.state) {
                var b = await this.signal.request(da.TRAFFIC_STATS, void 0, !0);
                b.timestamp = x();
                q(a = b.peer_delay).call(a, a => {
                    var b;
                    let c = this._statsCollector.trafficStats && R(b = this._statsCollector.trafficStats.peer_delay).call(b, b => b.peer_uid === a.peer_uid);
                    c && c.B_st !== a.B_st && ab(() => {
                        this.emit(wa.STREAM_TYPE_CHANGE, a.peer_uid, a.B_st)
                    })
                });
                this._statsCollector.updateTrafficStats(b)
            }
        }

        getJoinMessage() {
            if (!this.joinInfo ||
                !this.key) throw new p(n.UNEXPECTED_ERROR, "can not generate join message, no join info");
            let a = Ha({}, this.joinInfo.apResponse);
            var b = u.REPORT_APP_SCENARIO;
            if ("string" != typeof b) try {
                b = A(b)
            } catch (c) {
                b = void 0
            }
            b && 128 < b.length && (b = void 0);
            b = {
                session_id: this.joinInfo.sid,
                app_id: this.joinInfo.appId,
                channel_key: this.key,
                channel_name: this.joinInfo.cname,
                sdk_version: Va,
                browser: navigator.userAgent,
                process_id: u.PROCESS_ID,
                mode: this.spec.mode,
                codec: this.spec.codec,
                role: this.role,
                has_changed_gateway: this.hasChangeBGPAddress,
                ap_response: a,
                extends: u.JOIN_EXTEND,
                details: {6: this.joinInfo.stringUid},
                features: {rejoin: !0},
                optionalInfo: this.joinInfo.optionalInfo,
                appScenario: b
            };
            return this.joinInfo.stringUid && (b.string_uid = this.joinInfo.stringUid), this.joinInfo.aesmode && this.joinInfo.aespassword && (b.aes_mode = this.joinInfo.aesmode, u.ENCRYPT_AES ? (b.aes_secret = ho(this.joinInfo.aespassword, a.uid), b.aes_encrypt = !0) : b.aes_secret = this.joinInfo.aespassword), a.addresses[this.signal.websocket.currentURLIndex] && (b.ap_response.ticket = a.addresses[this.signal.websocket.currentURLIndex].ticket,
                delete a.addresses), b
        }

        getRejoinMessage() {
            if (!this.joinInfo) throw new p(n.UNEXPECTED_ERROR, "can not generate rejoin message, no join info");
            return {
                session_id: this.joinInfo.sid,
                channel_name: this.joinInfo.cname,
                cid: this.joinInfo.cid,
                uid: this.joinInfo.uid,
                vid: Number(this.joinInfo.vid)
            }
        }

        handleSignalEvents() {
            this.signal.on(Q.WS_RECONNECTING, a => {
                this.joinInfo && t.WebSocketQuit(this.joinInfo.sid, {
                    lts: x(),
                    succ: -1,
                    cname: this.joinInfo.cname,
                    uid: this.joinInfo.uid,
                    cid: this.joinInfo.cid,
                    errorCode: a || Sa.NETWORK_ERROR
                });
                this.joinInfo && (this.state = "RECONNECTING", t.sessionInit(this.joinInfo.sid, {
                    lts: (new Date).getTime(),
                    extend: this.isSignalRecover ? {recover: !0} : {rejoin: !0},
                    cname: this.joinInfo.cname,
                    appid: this.joinInfo.appId,
                    mode: this.spec.mode
                }), this.isSignalRecover = !1, this.joinGatewayStartTime = x())
            });
            this.signal.on(Q.WS_CLOSED, a => {
                let b;
                switch (a) {
                    case "LEAVE":
                        b = Sa.LEAVE;
                        break;
                    case "UID_BANNED":
                    case "IP_BANNED":
                    case "CHANNEL_BANNED":
                    case "SERVER_ERROR":
                        b = Sa.SERVER_ERROR;
                        break;
                    default:
                        b = Sa.NETWORK_ERROR
                }
                k.debug("[signal] websocket closed, reason: ".concat(b ||
                    "undefined -> " + Sa.NETWORK_ERROR));
                this.joinInfo && t.WebSocketQuit(this.joinInfo.sid, {
                    lts: x(),
                    succ: "LEAVE" === a ? 1 : -1,
                    cname: this.joinInfo.cname,
                    uid: this.joinInfo.uid,
                    cid: this.joinInfo.cid,
                    errorCode: b
                });
                this.reset();
                this._disconnectedReason = a;
                this.state = "DISCONNECTED"
            });
            this.signal.on(Q.WS_CONNECTED, () => {
                if (this.updateTurnConfigFromSignal(), this.state = "CONNECTED", this.joinInfo) {
                    var a, b;
                    "audience" === this.role && this._clientRoleOptions && this._clientRoleOptions.level && (k.debug(l(a = l(b = "[".concat(this.clientId,
                        "] patch to send set client role, role: ")).call(b, this.role, ", type: ")).call(a, this._clientRoleOptions.level)), this.setClientRole(this.role, this._clientRoleOptions));
                    t.joinGateway(this.joinInfo.sid, {
                        lts: this.joinGatewayStartTime,
                        succ: !0,
                        ec: null,
                        vid: this.joinInfo.vid,
                        addr: this.signal.url,
                        uid: this.joinInfo.uid,
                        cid: this.joinInfo.cid
                    })
                }
            });
            this.signal.on(U.ON_UPLINK_STATS, a => {
                this._statsCollector.updateUplinkStats(a)
            });
            this.signal.on(Q.REQUEST_RECOVER, (a, b, c) => {
                if (!this.joinInfo) return c(new p(n.UNEXPECTED_ERROR,
                    "gateway: can not recover, no join info"));
                a && (this.joinInfo.multiIP = a, this.hasChangeBGPAddress = !0);
                this.isSignalRecover = !0;
                Ma(this, wa.REQUEST_NEW_GATEWAY_LIST).then(b).catch(c)
            });
            this.signal.on(Q.REQUEST_JOIN_INFO, a => {
                a(this.getJoinMessage())
            });
            this.signal.on(Q.REQUEST_REJOIN_INFO, a => {
                a(this.getRejoinMessage())
            });
            this.signal.on(Q.REPORT_JOIN_GATEWAY, (a, b) => {
                this.joinInfo && t.joinGateway(this.joinInfo.sid, {
                    lts: this.joinGatewayStartTime,
                    succ: !1,
                    ec: a,
                    addr: b,
                    uid: this.joinInfo.uid,
                    cid: this.joinInfo.cid
                })
            });
            this.signal.on(Q.IS_P2P_DISCONNECTED, a => {
                a(Nc(this, wa.IS_P2P_DISCONNECTED))
            });
            this.signal.on(Q.DISCONNECT_P2P, () => {
                this.needToSendUnpubUnsub = new ba;
                this.emit(wa.DISCONNECT_P2P)
            });
            this.signal.on(Q.NEED_RENEW_SESSION, () => {
                this.emit(wa.NEED_RENEW_SESSION)
            });
            this.signal.on(Q.REQUEST_SUCCESS, () => {
                this._signalTimeout = !1
            });
            this.signal.on(Q.REQUEST_TIMEOUT, () => {
                this._signalTimeout = !0
            })
        }
    }

    let Je = 1, Le = 1, Ke = () => {
        const a = u.AREAS;
        0 === a.length && a.push("GLOBAL");
        return rd(a).call(a, (a, c, e) => {
            var b, h, m, k, n;
            c = "OVERSEA" ===
            c ? l(b = l(h = l(m = l(k = l(n = "".concat(xa.ASIA, ",")).call(n, xa.EUROPE, ",")).call(k, xa.AFRICA, ",")).call(m, xa.NORTH_AMERICA, ",")).call(h, xa.SOUTH_AMERICA, ",")).call(b, xa.OCEANIA) : xa[c];
            var p;
            return c ? 0 === e ? c : l(p = "".concat(a, ",")).call(p, c) : a
        }, "")
    }, jo = new class extends Ta {
        constructor() {
            super();
            this.retryConfig = {timeout: 3E3, timeoutFactor: 1.5, maxRetryCount: 1, maxRetryTimeout: 1E4};
            this.mutex = new Ob("config-distribute")
        }

        startGetConfigDistribute(a, b) {
            this.joinInfo = a;
            this.cancelToken = b;
            this.interval && this.stopGetConfigDistribute();
            this.updateConfigDistribute();
            this.interval = window.setInterval(() => {
                this.updateConfigDistribute()
            }, u.CONFIG_DISTRIBUTE_INTERVAL)
        }

        stopGetConfigDistribute() {
            this.interval && clearInterval(this.interval);
            this.cancelToken = this.joinInfo = this.interval = void 0
        }

        async awaitConfigDistributeComplete() {
            this.mutex.isLocked && (await this.mutex.lock())()
        }

        async updateConfigDistribute() {
            if (!this.joinInfo || !this.cancelToken || !this.retryConfig) return void k.debug("[config-distribute] get config distribute interrupted have no joininfo");
            let a;
            const b = await this.mutex.lock();
            try {
                a = await pl(this.joinInfo, this.cancelToken, this.retryConfig), k.debug("[config-distribute] get config distribute", A(a)), a.limit_bitrate && this.handleBitrateLimit(a.limit_bitrate), this.configs = a
            } catch (c) {
                const a = new p(n.NETWORK_RESPONSE_ERROR, c);
                k.warning("[config-distribute] ".concat(a.toString()))
            } finally {
                b()
            }
        }

        getBitrateLimit() {
            return this.configs ? this.configs.limit_bitrate : void 0
        }

        handleBitrateLimit(a) {
            a && a.uplink && a.id && void 0 !== a.uplink.max_bitrate && void 0 !==
            a.uplink.min_bitrate && (this.configs && this.configs.limit_bitrate ? this.configs && this.configs.limit_bitrate && this.configs.limit_bitrate.id !== a.id && this.emit(id.UPDATE_BITRATE_LIMIT, a) : this.emit(id.UPDATE_BITRATE_LIMIT, a))
        }

        getLowStreamConfigDistribute() {
            return this.configs && this.configs.limit_bitrate && function (a) {
                for (var b = 1; b < arguments.length; b++) {
                    var c, e = null != arguments[b] ? arguments[b] : {};
                    if (b % 2) q(c = Bh(Object(e), !0)).call(c, function (b) {
                        Oa(a, b, e[b])
                    }); else if (fa) Pa(a, fa(e)); else {
                        var g;
                        q(g = Bh(Object(e))).call(g,
                            function (b) {
                                X(a, b, Y(e, b))
                            })
                    }
                }
                return a
            }({}, this.configs.limit_bitrate.low_stream_uplink)
        }
    };
    var Yf = function () {
        function a(a) {
            this.input = [];
            this.size = a
        }

        return a.prototype.add = function (a) {
            this.input.push(a);
            this.input.length > this.size && this.input.splice(0, 1)
        }, a.prototype.diffMean = function () {
            return 0 === this.input.length ? 0 : (this.input[this.input.length - 1] - this.input[0]) / this.input.length
        }, a
    }(), Dh = function (a, b) {
        return (Dh = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (a, b) {
                a.__proto__ = b
            } ||
            function (a, b) {
                for (var c in b) b.hasOwnProperty(c) && (a[c] = b[c])
            })(a, b)
    }, Zf = function () {
        return (Zf = Object.assign || function (a) {
            for (var b, c = 1, e = arguments.length; c < e; c++) for (var g in b = arguments[c]) Object.prototype.hasOwnProperty.call(b, g) && (a[g] = b[g]);
            return a
        }).apply(this, arguments)
    }, Ub, md = {
        timestamp: 0,
        bitrate: {actualEncoded: 0, transmit: 0},
        sendPacketLossRate: 0,
        recvPacketLossRate: 0,
        videoRecv: [],
        videoSend: [],
        audioRecv: [],
        audioSend: []
    }, vk = {
        firsCount: 0, nacksCount: 0, plisCount: 0, framesDecodeCount: 0, framesDecodeInterval: 0,
        framesDecodeFreezeTime: 0, decodeFrameRate: 0, bytes: 0, packetsLost: 0, packetLostRate: 0, packets: 0, ssrc: 0
    }, wk = {
        firsCount: 0,
        nacksCount: 0,
        plisCount: 0,
        frameCount: 0,
        bytes: 0,
        packets: 0,
        packetsLost: 0,
        packetLostRate: 0,
        ssrc: 0,
        rttMs: 0
    }, xk = {bytes: 0, packets: 0, packetsLost: 0, packetLostRate: 0, ssrc: 0, rttMs: 0}, yk = {
        jitterBufferMs: 0,
        jitterMs: 0,
        bytes: 0,
        packetsLost: 0,
        packetLostRate: 0,
        packets: 0,
        ssrc: 0,
        receivedFrames: 0,
        droppedFrames: 0
    }, $f = function () {
        function a(a, c) {
            var b = this;
            this.videoIsReady = !1;
            this.stats = gb(md);
            this.isFirstAudioDecoded =
                this.isFirstAudioReceived = this.isFirstVideoDecoded = this.isFirstVideoReceived = !1;
            this.lossRateWindowStats = [];
            this.pc = a;
            this.options = c;
            this.intervalTimer = window.setInterval(function () {
                return Ne(b, void 0, void 0, function () {
                    return Oe(this, function (a) {
                        return this.updateStats(), [2]
                    })
                })
            }, this.options.updateInterval)
        }

        return a.prototype.getStats = function () {
            return this.stats
        }, a.prototype.setVideoIsReady = function (a) {
            this.videoIsReady = a
        }, a.prototype.setIsFirstAudioDecoded = function (a) {
            this.isFirstAudioDecoded =
                a
        }, a.prototype.destroy = function () {
            window.clearInterval(this.intervalTimer)
        }, a.prototype.calcLossRate = function (a) {
            var b = this;
            this.lossRateWindowStats.push(a);
            this.lossRateWindowStats.length > this.options.lossRateInterval && this.lossRateWindowStats.splice(0, 1);
            for (var e = this.lossRateWindowStats.length, g = 0, h = 0, m = 0, k = 0, l = function (c) {
                a[c].forEach(function (a, l) {
                    if (b.lossRateWindowStats[e - 1][c][l] && b.lossRateWindowStats[0][c][l]) {
                        var r = b.lossRateWindowStats[e - 1][c][l].packets - b.lossRateWindowStats[0][c][l].packets;
                        l = b.lossRateWindowStats[e - 1][c][l].packetsLost - b.lossRateWindowStats[0][c][l].packetsLost;
                        "videoSend" === c || "audioSend" === c ? (g += r, m += l) : (h += r, k += l);
                        Number.isNaN(r) || Number.isNaN(r) ? a.packetLostRate = 0 : a.packetLostRate = 0 >= r || 0 >= l ? 0 : l / (r + l)
                    }
                })
            }, n = 0, p = ["videoSend", "audioSend", "videoRecv", "audioRecv"]; n < p.length; n++) l(p[n]);
            a.sendPacketLossRate = 0 >= g || 0 >= m ? 0 : m / (g + m);
            a.recvPacketLossRate = 0 >= h || 0 >= k ? 0 : k / (h + k)
        }, a
    }(), ko = function (a) {
        function b() {
            var b = null !== a && a.apply(this, arguments) || this;
            return b._stats = md,
                b.lastDecodeVideoReceiverStats = new Map, b
        }

        return Me(b, a), b.prototype.updateStats = function () {
            return Ne(this, void 0, void 0, function () {
                var a, b, g, h;
                return Oe(this, function (c) {
                    switch (c.label) {
                        case 0:
                            return [4, this._getStats()];
                        case 1:
                            return a = c.sent(), b = this.statsResponsesToObjects(a), this._stats = gb(md), g = b.filter(function (a) {
                                return "ssrc" === a.type
                            }), this.processSSRCStats(g), (h = b.find(function (a) {
                                return "VideoBwe" === a.type
                            })) && this.processBandwidthStats(h), this._stats.timestamp = Date.now(), this.calcLossRate(this._stats),
                                this.stats = this._stats, [2]
                    }
                })
            })
        }, b.prototype.processBandwidthStats = function (a) {
            this._stats.bitrate = {
                actualEncoded: Number(a.googActualEncBitrate),
                targetEncoded: Number(a.googTargetEncBitrate),
                retransmit: Number(a.googRetransmitBitrate),
                transmit: Number(a.googTransmitBitrate)
            };
            this._stats.sendBandwidth = Number(a.googAvailableSendBandwidth)
        }, b.prototype.processSSRCStats = function (a) {
            var b = this;
            a.forEach(function (a) {
                var c = a.id.includes("send");
                switch (a.mediaType + "_" + (c ? "send" : "recv")) {
                    case "video_send":
                        c =
                            gb(wk);
                        c.codec = a.googCodecName;
                        c.adaptionChangeReason = "none";
                        a.googCpuLimitedResolution && (c.adaptionChangeReason = "cpu");
                        a.googBandwidthLimitedResolution && (c.adaptionChangeReason = "bandwidth");
                        c.avgEncodeMs = Number(a.googAvgEncodeMs);
                        c.inputFrame = {
                            width: Number(a.googFrameWidthInput) || Number(a.googFrameWidthSent),
                            height: Number(a.googFrameHeightInput) || Number(a.googFrameHeightSent),
                            frameRate: Number(a.googFrameRateInput)
                        };
                        c.sentFrame = {
                            width: Number(a.googFrameWidthSent), height: Number(a.googFrameHeightSent),
                            frameRate: Number(a.googFrameRateInput)
                        };
                        c.firsCount = Number(a.googFirReceived);
                        c.nacksCount = Number(a.googNacksReceived);
                        c.plisCount = Number(a.googPlisReceived);
                        c.frameCount = Number(a.framesEncoded);
                        c.bytes = Number(a.bytesSent);
                        c.packets = Number(a.packetsSent);
                        c.packetsLost = Number(a.packetsLost);
                        c.ssrc = Number(a.ssrc);
                        c.rttMs = Number(a.googRtt || 0);
                        b._stats.videoSend.push(c);
                        b._stats.rtt = c.rttMs;
                        break;
                    case "video_recv":
                        c = gb(vk);
                        var e = b.lastDecodeVideoReceiverStats.get(Number(a.ssrc));
                        if (c.codec = a.googCodecName,
                            c.targetDelayMs = Number(a.googTargetDelayMs), c.renderDelayMs = Number(a.googRenderDelayMs), c.currentDelayMs = Number(a.googCurrentDelayMs), c.minPlayoutDelayMs = Number(a.googMinPlayoutDelayMs), c.decodeMs = Number(a.googDecodeMs), c.maxDecodeMs = Number(a.googMaxDecodeMs), c.receivedFrame = {
                            width: Number(a.googFrameWidthReceived),
                            height: Number(a.googFrameHeightReceived),
                            frameRate: Number(a.googFrameRateReceived)
                        }, c.decodedFrame = {
                            width: Number(a.googFrameWidthReceived),
                            height: Number(a.googFrameHeightReceived),
                            frameRate: Number(a.googFrameRateDecoded)
                        },
                            c.outputFrame = {
                                width: Number(a.googFrameWidthReceived),
                                height: Number(a.googFrameHeightReceived),
                                frameRate: Number(a.googFrameRateOutput)
                            }, c.jitterBufferMs = Number(a.googJitterBufferMs), c.firsCount = Number(a.googFirsSent), c.nacksCount = Number(a.googNacksSent), c.plisCount = Number(a.googPlisSent), c.framesDecodeCount = Number(a.framesDecoded), c.bytes = Number(a.bytesReceived), c.packets = Number(a.packetsReceived), c.packetsLost = Number(a.packetsLost), c.ssrc = Number(a.ssrc), 0 < c.packets && !b.isFirstVideoReceived && (b.onFirstVideoReceived &&
                        b.onFirstVideoReceived(), b.isFirstVideoReceived = !0), 0 < c.framesDecodeCount && !b.isFirstVideoDecoded && (b.onFirstVideoDecoded && b.onFirstVideoDecoded(c.decodedFrame.width, c.decodedFrame.height), b.isFirstVideoDecoded = !0), e) {
                            a = e.stats;
                            var g = Date.now() - e.lts;
                            c.framesDecodeFreezeTime = a.framesDecodeFreezeTime;
                            c.framesDecodeInterval = a.framesDecodeInterval;
                            c.framesDecodeCount > a.framesDecodeCount && b.isFirstVideoDecoded ? (e.lts = Date.now(), c.framesDecodeInterval = g, 500 <= c.framesDecodeInterval && (b.videoIsReady ? c.framesDecodeFreezeTime +=
                                c.framesDecodeInterval : b.setVideoIsReady(!0))) : c.framesDecodeCount < e.stats.framesDecodeCount && (c.framesDecodeInterval = 0)
                        }
                        b.lastDecodeVideoReceiverStats.set(c.ssrc, {stats: Zf({}, c), lts: Date.now()});
                        b._stats.videoRecv.push(c);
                        break;
                    case "audio_recv":
                        c = gb(yk);
                        c.codec = a.googCodecName;
                        c.outputLevel = Math.abs(Number(a.audioOutputLevel)) / 32767;
                        c.decodingCNG = Number(a.googDecodingCNG);
                        c.decodingCTN = Number(a.googDecodingCTN);
                        c.decodingCTSG = Number(a.googDecodingCTSG);
                        c.decodingNormal = Number(a.googDecodingNormal);
                        c.decodingPLC = Number(a.googDecodingPLC);
                        c.decodingPLCCNG = Number(a.googDecodingPLCCNG);
                        c.expandRate = Number(a.googExpandRate);
                        c.accelerateRate = Number(a.googAccelerateRate);
                        c.preemptiveExpandRate = Number(a.googPreemptiveExpandRate);
                        c.secondaryDecodedRate = Number(a.googSecondaryDecodedRate);
                        c.speechExpandRate = Number(a.googSpeechExpandRate);
                        c.preferredJitterBufferMs = Number(a.googPreferredJitterBufferMs);
                        c.jitterBufferMs = Number(a.googJitterBufferMs);
                        c.jitterMs = Number(a.googJitterReceived);
                        c.bytes = Number(a.bytesReceived);
                        c.packets = Number(a.packetsReceived);
                        c.packetsLost = Number(a.packetsLost);
                        c.ssrc = Number(a.ssrc);
                        c.receivedFrames = Number(a.googDecodingCTN) || Number(a.packetsReceived);
                        c.droppedFrames = Number(a.googDecodingPLC) + Number(a.googDecodingPLCCNG) || Number(a.packetsLost);
                        0 < c.receivedFrames && !b.isFirstAudioReceived && (b.onFirstAudioReceived && b.onFirstAudioReceived(), b.isFirstAudioReceived = !0);
                        0 < c.decodingNormal && !b.isFirstAudioDecoded && (b.onFirstAudioDecoded && b.onFirstAudioDecoded(), b.isFirstAudioDecoded = !0);
                        b._stats.audioRecv.push(c);
                        break;
                    case "audio_send":
                        c = gb(xk), c.codec = a.googCodecName, c.inputLevel = Math.abs(Number(a.audioInputLevel)) / 32767, c.aecReturnLoss = Number(a.googEchoCancellationReturnLoss || 0), c.aecReturnLossEnhancement = Number(a.googEchoCancellationReturnLossEnhancement || 0), c.residualEchoLikelihood = Number(a.googResidualEchoLikelihood || 0), c.residualEchoLikelihoodRecentMax = Number(a.googResidualEchoLikelihoodRecentMax || 0), c.bytes = Number(a.bytesSent), c.packets = Number(a.packetsSent), c.packetsLost = Number(a.packetsLost), c.ssrc =
                            Number(a.ssrc), c.rttMs = Number(a.googRtt || 0), b._stats.rtt = c.rttMs, b._stats.audioSend.push(c)
                }
            })
        }, b.prototype._getStats = function () {
            var a = this;
            return new Promise(function (b, c) {
                a.pc.getStats(b, c)
            })
        }, b.prototype.statsResponsesToObjects = function (a) {
            var b = [];
            return a.result().forEach(function (a) {
                var c = {id: a.id, timestamp: a.timestamp.valueOf().toString(), type: a.type};
                a.names().forEach(function (b) {
                    c[b] = a.stat(b)
                });
                b.push(c)
            }), b
        }, b
    }($f);
    !function (a) {
        a.CERTIFICATE = "certificate";
        a.CODEC = "codec";
        a.CANDIDATE_PAIR =
            "candidate-pair";
        a.LOCAL_CANDIDATE = "local-candidate";
        a.REMOTE_CANDIDATE = "remote-candidate";
        a.INBOUND = "inbound-rtp";
        a.TRACK = "track";
        a.OUTBOUND = "outbound-rtp";
        a.PC = "peer-connection";
        a.REMOTE_INBOUND = "remote-inbound-rtp";
        a.REMOTE_OUTBOUND = "remote-outbound-rtp";
        a.TRANSPORT = "transport";
        a.CSRC = "csrc";
        a.DATA_CHANNEL = "data-channel";
        a.STREAM = "stream";
        a.SENDER = "sender";
        a.RECEIVER = "receiver"
    }(Ub || (Ub = {}));
    var zk = function (a) {
        function b() {
            var b = null !== a && a.apply(this, arguments) || this;
            return b._stats = md, b.lastDecodeVideoReceiverStats =
                new Map, b.lastVideoFramesRecv = new Map, b.lastVideoFramesSent = new Map, b.lastVideoFramesDecode = new Map, b.lastVideoJBDelay = new Map, b.lastAudioJBDelay = new Map, b.mediaBytesSent = new Map, b.mediaBytesRetransmit = new Map, b.mediaBytesTargetEncode = new Map, b.lastEncoderMs = new Map, b
        }

        return Me(b, a), b.prototype.updateStats = function () {
            return Ne(this, void 0, void 0, function () {
                var a, b = this;
                return Oe(this, function (c) {
                    switch (c.label) {
                        case 0:
                            return a = this, [4, this.pc.getStats()];
                        case 1:
                            return a.report = c.sent(), this._stats =
                                gb(md), this.report.forEach(function (a) {
                                switch (a.type) {
                                    case Ub.OUTBOUND:
                                        "audio" === a.mediaType ? b.processAudioOutboundStats(a) : "video" === a.mediaType && b.processVideoOutboundStats(a);
                                        break;
                                    case Ub.INBOUND:
                                        "audio" === a.mediaType ? b.processAudioInboundStats(a) : "video" === a.mediaType && b.processVideoInboundStats(a);
                                        break;
                                    case Ub.TRANSPORT:
                                        (a = b.report.get(a.selectedCandidatePairId)) && b.processCandidatePairStats(a);
                                        break;
                                    case Ub.CANDIDATE_PAIR:
                                        a.selected && b.processCandidatePairStats(a)
                                }
                            }), this.updateSendBitrate(),
                                this._stats.timestamp = Date.now(), this.calcLossRate(this._stats), this.stats = this._stats, [2]
                    }
                })
            })
        }, b.prototype.processCandidatePairStats = function (a) {
            this._stats.sendBandwidth = a.availableOutgoingBitrate || 0;
            a.currentRoundTripTime && (this._stats.rtt = 1E3 * a.currentRoundTripTime);
            this._stats.videoSend.forEach(function (b) {
                !b.rttMs && a.currentRoundTripTime && (b.rttMs = 1E3 * a.currentRoundTripTime)
            });
            this._stats.audioSend.forEach(function (b) {
                !b.rttMs && a.currentRoundTripTime && (b.rttMs = 1E3 * a.currentRoundTripTime)
            })
        },
            b.prototype.processAudioInboundStats = function (a) {
                var b = this._stats.audioRecv.find(function (b) {
                    return b.ssrc === a.ssrc
                });
                b || (b = gb(yk), this._stats.audioRecv.push(b));
                b.ssrc = a.ssrc;
                b.packets = a.packetsReceived;
                b.packetsLost = a.packetsLost;
                b.bytes = a.bytesReceived;
                b.jitterMs = 1E3 * a.jitter;
                a.trackId && this.processAudioTrackReceiverStats(a.trackId, b);
                a.codecId && (b.codec = this.getCodecFromCodecStats(a.codecId));
                b.receivedFrames || (b.receivedFrames = a.packetsReceived);
                b.droppedFrames || (b.droppedFrames = a.packetsLost);
                0 < b.receivedFrames && !this.isFirstAudioReceived && (this.onFirstAudioReceived && this.onFirstAudioReceived(), this.isFirstAudioReceived = !0);
                b.outputLevel && 0 < b.outputLevel && !this.isFirstAudioDecoded && (this.onFirstAudioDecoded && this.onFirstAudioDecoded(), this.isFirstAudioDecoded = !0)
            }, b.prototype.processVideoInboundStats = function (a) {
            var b = this._stats.videoRecv.find(function (b) {
                return b.ssrc === a.ssrc
            });
            b || (b = gb(vk), this._stats.videoRecv.push(b));
            b.ssrc = a.ssrc;
            b.packets = a.packetsReceived;
            b.packetsLost = a.packetsLost;
            b.bytes = a.bytesReceived;
            b.firsCount = a.firCount;
            b.nacksCount = a.nackCount;
            b.plisCount = a.pliCount;
            b.framesDecodeCount = a.framesDecoded;
            var c = this.lastDecodeVideoReceiverStats.get(b.ssrc), h = this.lastVideoFramesDecode.get(b.ssrc),
                m = Date.now();
            if (0 < b.framesDecodeCount && !this.isFirstVideoDecoded) {
                var k = b.decodedFrame ? b.decodedFrame.width : 0, l = b.decodedFrame ? b.decodedFrame.height : 0;
                this.onFirstVideoDecoded && this.onFirstVideoDecoded(k, l);
                this.isFirstVideoDecoded = !0
            }
            c && (k = c.stats, l = m - c.lts, b.framesDecodeFreezeTime =
                k.framesDecodeFreezeTime, b.framesDecodeInterval = k.framesDecodeInterval, b.framesDecodeCount > k.framesDecodeCount && this.isFirstVideoDecoded ? (c.lts = Date.now(), b.framesDecodeInterval = l, 500 <= b.framesDecodeInterval && (this.videoIsReady ? b.framesDecodeFreezeTime += b.framesDecodeInterval : this.setVideoIsReady(!0))) : b.framesDecodeCount < k.framesDecodeCount && (b.framesDecodeInterval = 0));
            h && 800 <= m - h.lts ? (b.decodeFrameRate = Math.round((b.framesDecodeCount - h.count) / ((m - h.lts) / 1E3)), this.lastVideoFramesDecode.set(b.ssrc,
                {
                    count: b.framesDecodeCount,
                    lts: m,
                    rate: b.decodeFrameRate
                })) : h ? b.decodeFrameRate = h.rate : this.lastVideoFramesDecode.set(b.ssrc, {
                count: b.framesDecodeCount,
                lts: m,
                rate: 0
            });
            a.totalDecodeTime && (b.decodeMs = 1E3 * a.totalDecodeTime);
            a.trackId && this.processVideoTrackReceiverStats(a.trackId, b);
            a.codecId && (b.codec = this.getCodecFromCodecStats(a.codecId));
            a.framerateMean && (b.framesRateFirefox = a.framerateMean);
            0 < b.packets && !this.isFirstVideoReceived && (this.onFirstVideoReceived && this.onFirstVideoReceived(), this.isFirstVideoReceived =
                !0);
            this.lastDecodeVideoReceiverStats.set(b.ssrc, {stats: Zf({}, b), lts: c ? c.lts : Date.now()})
        }, b.prototype.processVideoOutboundStats = function (a) {
            var b = this._stats.videoSend.find(function (b) {
                return b.ssrc === a.ssrc
            });
            b || (b = gb(wk), this._stats.videoSend.push(b));
            var c = this.mediaBytesSent.get(a.ssrc);
            c ? c.add(a.bytesSent) : ((h = new Yf(10)).add(a.bytesSent), this.mediaBytesSent.set(a.ssrc, h));
            void 0 !== a.retransmittedBytesSent && ((c = this.mediaBytesRetransmit.get(a.ssrc)) ? c.add(a.retransmittedBytesSent) : ((h = new Yf(10)).add(a.retransmittedBytesSent),
                this.mediaBytesRetransmit.set(a.ssrc, h)));
            if (a.totalEncodedBytesTarget) {
                var h;
                (c = this.mediaBytesTargetEncode.get(a.ssrc)) ? c.add(a.totalEncodedBytesTarget) : ((h = new Yf(10)).add(a.totalEncodedBytesTarget), this.mediaBytesTargetEncode.set(a.ssrc, h))
            }
            if (b.ssrc = a.ssrc, b.bytes = a.bytesSent, b.packets = a.packetsSent, b.firsCount = a.firCount, b.nacksCount = a.nackCount, b.plisCount = a.pliCount, b.frameCount = a.framesEncoded, b.adaptionChangeReason = a.qualityLimitationReason, a.totalEncodeTime && a.framesEncoded) c = this.lastEncoderMs.get(a.ssrc),
                b.avgEncodeMs = !c || c.lastFrameCount > a.framesEncoded ? 1E3 * a.totalEncodeTime / a.framesEncoded : 1E3 * (a.totalEncodeTime - c.lastEncoderTime) / (a.framesEncoded - c.lastFrameCount), this.lastEncoderMs.set(a.ssrc, {
                lastFrameCount: a.framesEncoded,
                lastEncoderTime: a.totalEncodeTime,
                lts: Date.now()
            });
            (a.codecId && (b.codec = this.getCodecFromCodecStats(a.codecId)), a.mediaSourceId && this.processVideoMediaSource(a.mediaSourceId, b), a.trackId && this.processVideoTrackSenderStats(a.trackId, b), a.remoteId) ? this.processRemoteInboundStats(a.remoteId,
                b) : (c = this.findRemoteStatsId(a.ssrc, Ub.REMOTE_INBOUND)) && this.processRemoteInboundStats(c, b)
        }, b.prototype.processAudioOutboundStats = function (a) {
            var b = this._stats.audioSend.find(function (b) {
                return b.ssrc === a.ssrc
            });
            if (b || (b = gb(xk), this._stats.audioSend.push(b)), b.ssrc = a.ssrc, b.packets = a.packetsSent, b.bytes = a.bytesSent, a.mediaSourceId && this.processAudioMediaSource(a.mediaSourceId, b), a.codecId && (b.codec = this.getCodecFromCodecStats(a.codecId)), a.trackId && this.processAudioTrackSenderStats(a.trackId, b),
                a.remoteId) this.processRemoteInboundStats(a.remoteId, b); else {
                var c = this.findRemoteStatsId(a.ssrc, Ub.REMOTE_INBOUND);
                c && this.processRemoteInboundStats(c, b)
            }
        }, b.prototype.findRemoteStatsId = function (a, b) {
            var c = Array.from(this.report.values()).find(function (c) {
                return c.type === b && c.ssrc === a
            });
            return c ? c.id : null
        }, b.prototype.processVideoMediaSource = function (a, b) {
            (a = this.report.get(a)) && a.width && a.height && a.framesPerSecond && (b.inputFrame = {
                width: a.width,
                height: a.height,
                frameRate: a.framesPerSecond
            })
        }, b.prototype.processAudioMediaSource =
            function (a, b) {
                (a = this.report.get(a)) && (b.inputLevel = a.audioLevel)
            }, b.prototype.processVideoTrackSenderStats = function (a, b) {
            if (a = this.report.get(a)) {
                var c = 0, e = Date.now(), m = this.lastVideoFramesSent.get(b.ssrc);
                m && 800 <= e - m.lts ? (c = Math.round((a.framesSent - m.count) / ((e - m.lts) / 1E3)), this.lastVideoFramesSent.set(b.ssrc, {
                    count: a.framesSent,
                    lts: e,
                    rate: c
                })) : m ? c = m.rate : this.lastVideoFramesSent.set(b.ssrc, {count: a.framesSent, lts: e, rate: 0});
                b.sentFrame = {width: a.frameWidth, height: a.frameHeight, frameRate: c}
            }
        }, b.prototype.processVideoTrackReceiverStats =
            function (a, b) {
                if (a = this.report.get(a)) {
                    var c = this.lastVideoFramesRecv.get(b.ssrc), e = Date.now();
                    b.framesReceivedCount = a.framesReceived;
                    var m = 0;
                    if (c && 800 <= e - c.lts ? (m = Math.round((a.framesReceived - c.count) / ((e - c.lts) / 1E3)), this.lastVideoFramesRecv.set(b.ssrc, {
                        count: a.framesReceived,
                        lts: e,
                        rate: m
                    })) : c ? m = c.rate : this.lastVideoFramesRecv.set(b.ssrc, {
                        count: a.framesReceived,
                        lts: e,
                        rate: 0
                    }), b.receivedFrame = {
                        width: a.frameWidth || 0,
                        height: a.frameHeight || 0,
                        frameRate: m || 0
                    }, b.decodedFrame = {
                        width: a.frameWidth || 0, height: a.frameHeight ||
                            0, frameRate: b.decodeFrameRate || 0
                    }, b.outputFrame = {
                        width: a.frameWidth || 0,
                        height: a.frameHeight || 0,
                        frameRate: b.decodeFrameRate || 0
                    }, a.jitterBufferDelay && a.jitterBufferEmittedCount) c = this.lastVideoJBDelay.get(b.ssrc), this.lastVideoJBDelay.set(b.ssrc, {
                        jitterBufferDelay: a.jitterBufferDelay,
                        jitterBufferEmittedCount: a.jitterBufferEmittedCount
                    }), c || (c = {
                        jitterBufferDelay: 0,
                        jitterBufferEmittedCount: 0
                    }), a = 1E3 * (a.jitterBufferDelay - c.jitterBufferDelay) / (a.jitterBufferEmittedCount - c.jitterBufferEmittedCount), b.jitterBufferMs =
                        a, b.currentDelayMs = Math.round(a)
                }
            }, b.prototype.processAudioTrackSenderStats = function (a, b) {
            (a = this.report.get(a)) && (b.aecReturnLoss = a.echoReturnLoss || 0, b.aecReturnLossEnhancement = a.echoReturnLossEnhancement || 0)
        }, b.prototype.processAudioTrackReceiverStats = function (a, b) {
            if (a = this.report.get(a)) {
                if (a.removedSamplesForAcceleration && a.totalSamplesReceived && (b.accelerateRate = a.removedSamplesForAcceleration / a.totalSamplesReceived), a.jitterBufferDelay && a.jitterBufferEmittedCount) {
                    var c = this.lastAudioJBDelay.get(b.ssrc);
                    this.lastAudioJBDelay.set(b.ssrc, {
                        jitterBufferDelay: a.jitterBufferDelay,
                        jitterBufferEmittedCount: a.jitterBufferEmittedCount
                    });
                    c || (c = {jitterBufferDelay: 0, jitterBufferEmittedCount: 0});
                    b.jitterBufferMs = Math.round(1E3 * (a.jitterBufferDelay - c.jitterBufferDelay) / (a.jitterBufferEmittedCount - c.jitterBufferEmittedCount))
                }
                b.outputLevel = a.audioLevel;
                c = 1920;
                a.totalSamplesDuration && a.totalSamplesReceived && (c = a.totalSamplesReceived / a.totalSamplesDuration / 50, b.receivedFrames = Math.round(a.totalSamplesReceived / c));
                a.concealedSamples && (b.droppedFrames = Math.round(a.concealedSamples / c))
            }
        }, b.prototype.processRemoteInboundStats = function (a, b) {
            (a = this.report.get(a)) && (b.packetsLost = a.packetsLost, a.roundTripTime && (b.rttMs = 1E3 * a.roundTripTime))
        }, b.prototype.getCodecFromCodecStats = function (a) {
            a = this.report.get(a);
            return a ? (a = a.mimeType.match(/\/(.*)$/)) && a[1] ? a[1] : "" : ""
        }, b.prototype.updateSendBitrate = function () {
            var a = 0, b = null, g = null;
            this.mediaBytesSent.forEach(function (b) {
                a += b.diffMean()
            });
            this.mediaBytesRetransmit.forEach(function (a) {
                b =
                    null === b ? a.diffMean() : b + a.diffMean()
            });
            this.mediaBytesTargetEncode.forEach(function (a) {
                g = null === g ? a.diffMean() : g + a.diffMean()
            });
            this._stats.bitrate = {
                actualEncoded: 8 * (null !== b ? a - b : a) / (this.options.updateInterval / 1E3),
                transmit: 8 * a / (this.options.updateInterval / 1E3)
            };
            null !== b && (this._stats.bitrate.retransmit = 8 * b / (this.options.updateInterval / 1E3));
            null !== g && (this._stats.bitrate.targetEncoded = 8 * g / (this.options.updateInterval / 1E3))
        }, b
    }($f), lo = function (a) {
        function b() {
            return null !== a && a.apply(this, arguments) ||
                this
        }

        return Me(b, a), b.prototype.updateStats = function () {
            return Promise.resolve()
        }, b
    }($f);

    class Ak {
        constructor(a) {
            this.localCandidateCount = 0;
            this.allCandidateReceived = !1;
            this.videoTrack = this.audioTrack = null;
            this.mediaStream = new MediaStream;
            this.ID = Bk;
            Bk += 1;
            this.spec = a;
            this.createPeerConnection();
            a = this.pc;
            var b = void 0, c = void 0;
            void 0 === b && (b = 250);
            void 0 === c && (c = 8);
            var e,
                g = (e = navigator.userAgent.toLocaleLowerCase().match(/chrome\/[\d]./i)) && e[0] ? Number(e[0].split("/")[1]) : null;
            this.statsFilter = g ? 76 >
            g ? new ko(a, {updateInterval: b, lossRateInterval: c}) : new zk(a, {
                updateInterval: b,
                lossRateInterval: c
            }) : window.RTCStatsReport && a.getStats() instanceof Promise ? new zk(a, {
                updateInterval: b,
                lossRateInterval: c
            }) : new lo(a, {updateInterval: b, lossRateInterval: c})
        }

        get _statsFilter() {
            return this.statsFilter
        }

        getStats() {
            return this.statsFilter.getStats()
        }

        async createOfferSDP() {
            try {
                let a = await this.pc.createOffer(this.offerOptions);
                if (!a.sdp) throw Error("offer sdp is empty");
                return a.sdp
            } catch (a) {
                throw k.error("create offer error:",
                    a.toString()), new p(n.CREATE_OFFER_FAILED, a.toString());
            }
        }

        async setOfferSDP(a) {
            try {
                await this.pc.setLocalDescription({type: "offer", sdp: a})
            } catch (b) {
                throw k.error("set local offer error", b.toString()), new p(n.CREATE_OFFER_FAILED, b.toString());
            }
        }

        async setAnswerSDP(a) {
            try {
                await this.pc.setRemoteDescription({type: "answer", sdp: a})
            } catch (b) {
                if ("InvalidStateError" !== b.name || "stable" !== this.pc.signalingState) throw k.error("set remote answer error", b.toString()), new p(n.SET_ANSWER_FAILED, b.toString());
                k.debug("[pc-".concat(this.ID,
                    "] ignore invalidstate error"))
            }
        }

        close() {
            this.onConnectionStateChange = this.onICEConnectionStateChange = void 0;
            try {
                this.pc.close()
            } catch (a) {
            }
            this.statsFilter.destroy()
        }

        createPeerConnection() {
            let a = {iceServers: [{urls: "stun:webcs.agora.io:3478"}]}, b = a => {
                const b = [];
                return q(a).call(a, a => {
                    if (a.security) {
                        var c;
                        a.tcpport && b.push({
                            username: a.username,
                            credential: a.password,
                            credentialType: "password",
                            urls: l(c = "turns:".concat((h = a.turnServerURL, h.match(/^[\.:\d]+$/) ? "".concat(h.replace(/[^\d]/g, "-"), ".edge.agora.io") :
                                (k.info("Cannot recognized as IP address ".concat(h, ". Used As Host instead")), h)), ":")).call(c, a.tcpport, "?transport=tcp")
                        })
                    } else {
                        var e, g;
                        a.udpport && b.push({
                            username: a.username,
                            credential: a.password,
                            credentialType: "password",
                            urls: l(e = "turn:".concat(a.turnServerURL, ":")).call(e, a.udpport, "?transport=udp")
                        });
                        a.tcpport && b.push({
                            username: a.username,
                            credential: a.password,
                            credentialType: "password",
                            urls: l(g = "turn:".concat(a.turnServerURL, ":")).call(g, a.tcpport, "?transport=tcp")
                        })
                    }
                    var h
                }), b
            };
            var c;
            this.spec.iceServers ?
                a.iceServers = this.spec.iceServers : this.spec.turnServer && "off" !== this.spec.turnServer.mode && (ye(this.spec.turnServer.servers) ? a.iceServers = this.spec.turnServer.servers : (a.iceServers && a.iceServers.push(...b(this.spec.turnServer.servers)), a.iceServers && this.spec.turnServer.serversFromGateway && a.iceServers.push(...b(this.spec.turnServer.serversFromGateway)), q(c = this.spec.turnServer.servers).call(c, b => {
                b.forceturn && (a.iceTransportPolicy = "relay")
            })));
            u.CHROME_FORCE_PLAN_B && Bd() && (a.sdpSemantics = "plan-b",
                ca.supportUnifiedPlan = !1);
            this.pc = new RTCPeerConnection(a, {optional: [{googDscp: !0}]});
            this.pc.oniceconnectionstatechange = () => {
                this.onICEConnectionStateChange && this.onICEConnectionStateChange(this.pc.iceConnectionState)
            };
            this.pc.onconnectionstatechange = () => {
                this.onConnectionStateChange && this.onConnectionStateChange(this.pc.connectionState)
            };
            this.pc.onsignalingstatechange = () => {
                "closed" === this.pc.connectionState && this.onConnectionStateChange && this.onConnectionStateChange(this.pc.connectionState)
            };
            this.pc.onicecandidateerror =
                a => {
                    var b, c, e;
                    a && k.debug(l(b = l(c = l(e = "[pc-".concat(this.ID, "] ice candidate error ")).call(e, a.url, ", code: ")).call(c, a.errorCode, ", ")).call(b, a.errorText))
                };
            this.pc.onicecandidate = a => {
                if (!a.candidate) return this.pc.onicecandidate = null, this.allCandidateReceived = !0, void k.debug("[pc-".concat(this.ID, "] local candidate count"), this.localCandidateCount);
                this.localCandidateCount += 1
            };
            Fc(() => {
                this.allCandidateReceived || (this.allCandidateReceived = !0, k.debug("[pc-".concat(this.ID, "] onicecandidate timeout, local candidate count"),
                    this.localCandidateCount))
            }, u.CANDIDATE_TIMEOUT)
        }
    }

    class Ck extends Ak {
        constructor(a) {
            super(a)
        }

        async setOfferSDP(a) {
            let b = u.CUSTOM_PUB_OFFER_MODIFIER;
            return b && (a = b(a)), await super.setOfferSDP(a)
        }

        async setAnswerSDP(a) {
            let b = u.CUSTOM_PUB_ANSWER_MODIFIER;
            return b && (a = b(a)), await super.setAnswerSDP(a)
        }

        getAnswerSDP() {
            return this.pc.remoteDescription
        }

        getOfferSDP() {
            return this.pc.localDescription
        }

        async addStream(a) {
            a = a.getTracks();
            for (let b of a) await this.addTrack(b)
        }

        async replaceTrack(a) {
            if (!ca.supportReplaceTrack) {
                var b =
                    "audio" === a.kind ? this.audioTrack : this.videoTrack;
                if (!b) throw new p(n.UNEXPECTED_ERROR, "can not find replaced track");
                return this.removeTrack(b), await this.addTrack(a), !0
            }
            let c = this.getSender(a.kind), e = R(b = this.mediaStream.getTracks()).call(b, b => b.kind === a.kind);
            e && this.mediaStream.removeTrack(e);
            this.mediaStream.addTrack(a);
            try {
                await c.replaceTrack(a), "audio" === a.kind ? this.audioTrack = a : this.videoTrack = a
            } catch (g) {
                throw new p(n.SENDER_REPLACE_FAILED, g.toString());
            }
            return !1
        }

        removeTrack(a) {
            let b = this.getSender(a.kind);
            this.mediaStream.removeTrack(a);
            try {
                this.pc.removeTrack(b)
            } catch (c) {
                k.warning("[pc-".concat(this.ID, "] remove track error, ignore"), c)
            }
            "audio" === a.kind ? (this.audioTrack = null, this.audioSender = void 0, this.audioTransceiver && (this.audioTransceiver.direction = "inactive"), this.audioTransceiver = void 0) : (this.videoTrack = null, this.videoSender = void 0, this.videoTransceiver && (this.videoTransceiver.direction = "inactive"), this.videoTransceiver = void 0)
        }

        onOfferSettled() {
            Bd() && (this.audioSender && u.DSCP_TYPE && this.setAudioRtpEncodingParameters({networkPriority: u.DSCP_TYPE}).catch(a => {
                k.debug("set audio sender`s network priority failed")
            }), this.videoSender && u.DSCP_TYPE && this.setVideoRtpEncodingParameters({networkPriority: u.DSCP_TYPE}).catch(a => {
                k.debug("set video sender`s network priority failed")
            }))
        }

        async addTrack(a) {
            let b = ca;
            if ("audio" === a.kind && this.audioTrack || "video" === a.kind && this.videoTrack) throw new p(n.UNEXPECTED_ERROR, "Can't add multiple stream");
            let c, e;
            this.mediaStream.addTrack(a);
            b.supportUnifiedPlan ? (c = await async function (a, b, c) {
                var e;
                let g = R(e = a.getTransceivers()).call(e,
                    a => "inactive" === a.direction && a.receiver.track.kind === b.kind);
                return g ? (g.direction = "sendrecv", await g.sender.replaceTrack(b), g) : a.addTransceiver(b, {
                    direction: "sendrecv",
                    streams: [c]
                })
            }(this.pc, a, this.mediaStream), e = c.sender) : e = this.pc.addTrack(a, this.mediaStream);
            "audio" === a.kind ? (this.audioTrack = a, this.audioSender = e, this.audioTransceiver = c) : (this.videoTrack = a, this.videoSender = e, this.videoTransceiver = c)
        }

        async setRtpSenderParameters(a, b) {
            if (a = this.videoSender || (this.videoTransceiver ? this.videoTransceiver.sender :
                void 0)) {
                var c = a.getParameters();
                c.degradationPreference = b;
                try {
                    await a.setParameters(c)
                } catch (e) {
                    k.debug("[".concat(this.ID, "] ignore RtpSender.setParameters"), e.toString())
                }
            }
        }

        async setVideoRtpEncodingParameters(a) {
            let b = this.videoSender || (this.videoTransceiver ? this.videoTransceiver.sender : void 0);
            if (!b) throw new p(n.LOW_STREAM_ENCODING_ERROR, "Low stream has no video sender.");
            let c = b.getParameters();
            if (!c.encodings || !c.encodings[0]) throw new p(n.LOW_STREAM_ENCODING_ERROR, "Low stream RtpEncodingParameters is empty.");
            a.scaleResolutionDownBy && (c.encodings[0].scaleResolutionDownBy = a.scaleResolutionDownBy);
            a.maxBitrate && (c.encodings[0].maxBitrate = a.maxBitrate);
            a.maxFramerate && (c.encodings[0].maxFramerate = a.maxFramerate);
            let e = ["very-low", "low", "medium", "high"];
            return a.networkPriority && ya(e).call(e, a.networkPriority) && (k.debug("set video sender network quality:", a.networkPriority), c.encodings[0].networkPriority = a.networkPriority), await b.setParameters(c), b.getParameters()
        }

        async setAudioRtpEncodingParameters(a) {
            let b =
                this.audioSender || (this.audioTransceiver ? this.audioTransceiver.sender : void 0);
            if (!b) throw new p(n.SET_ENCODING_PARAMETER_ERROR, "pc has no audio sender.");
            let c = b.getParameters();
            if (!c.encodings || !c.encodings[0]) throw new p(n.SET_ENCODING_PARAMETER_ERROR, "pc RtpEncodingParameters is empty.");
            let e = ["very-low", "low", "medium", "high"];
            return a.networkPriority && ya(e).call(e, a.networkPriority) && (k.debug("set audio sender network quality:", a.networkPriority), c.encodings[0].networkPriority = a.networkPriority),
                await b.setParameters(c), b.getParameters()
        }

        getSender(a) {
            var b = null;
            if (ca.supportUnifiedPlan) {
                var c;
                b = (b = R(c = this.pc.getTransceivers()).call(c, b => b.sender.track && b.sender.track.kind === a)) ? b.sender : null
            } else {
                var e;
                b = R(e = this.pc.getSenders()).call(e, b => b.track && b.track.kind === a) || null
            }
            if (!b) throw new p(n.SENDER_NOT_FOUND);
            return b
        }
    }

    class Dk extends Ak {
        constructor(a) {
            super(a);
            this.statsFilter.onFirstAudioDecoded = () => this.onFirstAudioDecoded && this.onFirstAudioDecoded();
            this.statsFilter.onFirstVideoDecoded =
                (a, c) => this.onFirstVideoDecoded && this.onFirstVideoDecoded(a, c);
            this.statsFilter.onFirstAudioReceived = () => this.onFirstAudioReceived && this.onFirstAudioReceived();
            this.statsFilter.onFirstVideoReceived = () => this.onFirstVideoReceived && this.onFirstVideoReceived();
            ca.supportUnifiedPlan ? (this.audioTransceiver = this.pc.addTransceiver("audio", {direction: "recvonly"}), this.videoTransceiver = this.pc.addTransceiver("video", {direction: "recvonly"})) : this.offerOptions = {
                offerToReceiveAudio: !0,
                offerToReceiveVideo: !0
            };
            this.pc.ontrack = a => {
                "audio" === a.track.kind ? this.audioTrack = a.track : this.videoTrack = a.track;
                this.onTrack && this.onTrack(a.track, a.streams[0])
            }
        }

        async setOfferSDP(a) {
            let b = u.CUSTOM_SUB_OFFER_MODIFIER;
            return b && (a = b(a)), await super.setOfferSDP(a)
        }

        async setAnswerSDP(a) {
            let b = u.CUSTOM_SUB_ANSWER_MODIFIER;
            return b && (a = b(a)), await super.setAnswerSDP(a)
        }
    }

    let Bk = 1, Ek = 1;

    class Fk extends Ta {
        constructor(a, b) {
            super();
            this.startTime = x();
            this.createTime = x();
            this.readyToReconnect = !1;
            this._connectionState = "disconnected";
            this.currentReconnectCount = 0;
            this.ID = Ek;
            Ek += 1;
            this.joinInfo = a;
            this._userId = b;
            this.createPC()
        }

        get connectionState() {
            return this._connectionState
        }

        set connectionState(a) {
            a !== this._connectionState && (this.emit(I.CONNECTION_STATE_CHANGE, a, this._connectionState), this._connectionState = a)
        }

        get connectionId() {
            var a, b;
            return l(a = l(b = "".concat(this.joinInfo.clientId, "-")).call(b, this.type ? this.type : "sub(".concat(this._userId, ")"), "-")).call(a, this.ID)
        }

        getUserId() {
            return this._userId
        }

        startUploadStats() {
            this.statsUploadInterval =
                window.setInterval(() => {
                    let a = this.pc.getStats();
                    this.uploadStats(a, this.lastUploadPCStats);
                    this.lastUploadPCStats = a
                }, 3E3);
            this.statsUploadSlowInterval = window.setInterval(() => {
                let a = this.pc.getStats();
                this.uploadSlowStats(a)
            }, 6E4);
            this.relatedStatsUploadInterval = window.setInterval(() => {
                let a = this.pc.getStats();
                this.uploadRelatedStats(a, this.lastRelatedPcStats);
                this.lastRelatedPcStats = a
            }, 1E3)
        }

        stopUploadStats() {
            this.statsUploadInterval && window.clearInterval(this.statsUploadInterval);
            this.relatedStatsUploadInterval &&
            window.clearInterval(this.relatedStatsUploadInterval);
            this.relatedStatsUploadInterval = this.statsUploadInterval = void 0
        }

        createWaitConnectionConnectedPromise() {
            return new v((a, b) => {
                "disconnected" === this.connectionState ? b() : "connected" === this.connectionState ? a() : this.once(I.CONNECTION_STATE_CHANGE, c => {
                    "connected" === c ? a() : b()
                })
            })
        }

        async reconnectPC(a) {
            if (this.readyToReconnect = !1, a && this.onPCDisconnected(a), Infinity < this.currentReconnectCount) throw k.debug("[".concat(this.connectionId, "] cannot reconnect pc")),
            a || new p(n.UNEXPECTED_ERROR);
            this.stopUploadStats();
            k.debug("[".concat(this.connectionId, "] start reconnect pc"));
            this.connectionState = "connecting";
            this.currentReconnectCount += 1;
            if (await this.closePC()) return k.debug("[".concat(this.connectionId, "] abort reconnect pc, wait ws")), void this.readyToReconnectPC();
            this.createPC();
            await this.startP2PConnection();
            this.currentReconnectCount = 0
        }

        readyToReconnectPC() {
            this.stopUploadStats();
            this.readyToReconnect = !0;
            this.pc.onICEConnectionStateChange = void 0;
            this.connectionState =
                "connecting"
        }

        updateICEPromise() {
            this.removeAllListeners(I.GATEWAY_P2P_LOST);
            this.icePromise = new v((a, b) => {
                this.pc.onICEConnectionStateChange = c => {
                    var e, g;
                    k.info(l(e = l(g = "[".concat(this.connectionId, "] ice-state: ")).call(g, this.type, " p2p ")).call(e, c));
                    "connected" === c && a();
                    "failed" !== c && "closed" !== c || this.reconnectPC(new p(n.ICE_FAILED)).catch(a => {
                        this.emit(I.P2P_LOST);
                        b(a)
                    })
                };
                this.pc.onConnectionStateChange = a => {
                    var c, g;
                    k.info(l(c = l(g = "[".concat(this.connectionId, "] connection-state: ")).call(g, this.type,
                        " p2p ")).call(c, a));
                    "failed" !== a && "closed" !== a || this.reconnectPC(new p(n.PC_CLOSED)).catch(a => {
                        this.emit(I.P2P_LOST);
                        b(a)
                    })
                };
                this.removeAllListeners(I.GATEWAY_P2P_LOST);
                this.once(I.GATEWAY_P2P_LOST, a => {
                    var c;
                    if (this.pc.ID.toString() === a.toString()) {
                        if (k.info(l(c = "[".concat(this.connectionId, "] ")).call(c, this.type, " p2p gateway lost")), this.pc.allCandidateReceived && 0 === this.pc.localCandidateCount) return this.disconnectedReason = new p(n.NO_ICE_CANDIDATE, "can not get candidate in this pc"), void this.closeP2PConnection(!0);
                        this.reconnectPC(new p(n.GATEWAY_P2P_LOST)).catch(a => {
                            this.emit(I.P2P_LOST);
                            b(a)
                        })
                    }
                })
            })
        }
    }

    class Gk {
        constructor(a) {
            this.freezeTimeCounterList = [];
            this.lastTimeUpdatedTime = this.playbackTime = this.freezeTime = this.timeUpdatedCount = 0;
            this._videoElementStatus = Ca.NONE;
            this.isGettingVideoDimensions = !1;
            this.handleVideoEvents = a => {
                switch (a.type) {
                    case "play":
                    case "playing":
                        this.startGetVideoDimensions();
                        this.videoElementStatus = Ca.PLAYING;
                        break;
                    case "loadeddata":
                        this.onFirstVideoFrameDecoded && this.onFirstVideoFrameDecoded();
                        break;
                    case "canplay":
                        this.videoElementStatus = Ca.CANPLAY;
                        break;
                    case "stalled":
                        this.videoElementStatus = Ca.STALLED;
                        break;
                    case "suspend":
                        this.videoElementStatus = Ca.SUSPEND;
                        break;
                    case "pause":
                        this.videoElementStatus = Ca.PAUSED;
                        this.videoElement && (k.debug("[track-".concat(this.trackId, "] video element paused, auto resume")), this.videoElement.play());
                        break;
                    case "waiting":
                        this.videoElementStatus = Ca.WAITING;
                        break;
                    case "abort":
                        this.videoElementStatus = Ca.ABORT;
                        break;
                    case "ended":
                        this.videoElementStatus = Ca.ENDED;
                        break;
                    case "emptied":
                        this.videoElementStatus = Ca.EMPTIED;
                        break;
                    case "timeupdate": {
                        a = x();
                        if (this.timeUpdatedCount += 1, 10 > this.timeUpdatedCount) return void (this.lastTimeUpdatedTime = a);
                        let b = a - this.lastTimeUpdatedTime;
                        this.lastTimeUpdatedTime = a;
                        500 < b && (this.freezeTime += b);
                        for (this.playbackTime += b; 6E3 <= this.playbackTime;) this.playbackTime -= 6E3, this.freezeTimeCounterList.push(Math.min(6E3, this.freezeTime)), this.freezeTime = Math.max(0, this.freezeTime - 6E3)
                    }
                }
            };
            this.startGetVideoDimensions = () => {
                let a = () => {
                    if (this.isGettingVideoDimensions =
                        !0, this.videoElement && 4 < this.videoElement.videoWidth * this.videoElement.videoHeight) return k.debug("[".concat(this.trackId, "] current video dimensions:"), this.videoElement.videoWidth, this.videoElement.videoHeight), void (this.isGettingVideoDimensions = !1);
                    Fc(a, 500)
                };
                !this.isGettingVideoDimensions && a()
            };
            this.slot = a.element;
            this.trackId = a.trackId;
            this.updateConfig(a)
        }

        get videoElementStatus() {
            return this._videoElementStatus
        }

        set videoElementStatus(a) {
            var b, c;
            a !== this._videoElementStatus && (k.debug(l(b = l(c =
                "[".concat(this.trackId, "] video-element-status change ")).call(c, this._videoElementStatus, " => ")).call(b, a)), this._videoElementStatus = a)
        }

        updateConfig(a) {
            this.config = a;
            this.trackId = a.trackId;
            a = a.element;
            a !== this.slot && (this.destroy(), this.slot = a);
            this.createElements()
        }

        updateVideoTrack(a) {
            this.videoTrack !== a && (this.videoTrack = a, this.createElements())
        }

        play() {
            if (this.videoElement) {
                let a = this.videoElement.play();
                a && a.catch && a.catch(a => {
                    k.warning("[".concat(this.trackId, "] play warning: "), a)
                })
            }
        }

        getCurrentFrame() {
            if (!this.videoElement) return new ImageData(2,
                2);
            let a = document.createElement("canvas");
            a.width = this.videoElement.videoWidth;
            a.height = this.videoElement.videoHeight;
            var b = a.getContext("2d");
            if (!b) return k.error("create canvas context failed!"), new ImageData(2, 2);
            b.drawImage(this.videoElement, 0, 0, a.width, a.height);
            b = b.getImageData(0, 0, a.width, a.height);
            return a.remove(), b
        }

        destroy() {
            if (this.videoElement && (this.videoElement.srcObject = null, this.videoElement = void 0), this.container) {
                try {
                    this.slot.removeChild(this.container)
                } catch (a) {
                }
                this.container =
                    void 0
            }
            this.freezeTimeCounterList = []
        }

        createElements() {
            this.container || (this.container = document.createElement("div"));
            this.container.id = "agora-video-player-".concat(this.trackId);
            this.container.style.width = "100%";
            this.container.style.height = "100%";
            this.container.style.position = "relative";
            this.container.style.overflow = "hidden";
            this.videoTrack ? (this.container.style.backgroundColor = "black", this.createVideoElement(), this.container.appendChild(this.videoElement)) : this.removeVideoElement();
            this.slot.appendChild(this.container)
        }

        createVideoElement() {
            (this.videoElement ||
            (this.videoElementStatus = Ca.INIT, this.videoElement = document.createElement("video"), this.videoElement.onerror = () => this.videoElementStatus = Ca.ERROR, this.container && this.container.appendChild(this.videoElement), q(le).call(le, a => {
                this.videoElement && this.videoElement.addEventListener(a, this.handleVideoEvents)
            }), this.videoElementCheckInterval = window.setInterval(() => {
                !document.getElementById("video_".concat(this.trackId)) && this.videoElement && (this.videoElementStatus = Ca.DESTROYED)
            }, 1E3)), this.videoElement.id =
                "video_".concat(this.trackId), this.videoElement.className = "agora_video_player", this.videoElement.style.width = "100%", this.videoElement.style.height = "100%", this.videoElement.style.position = "absolute", this.videoElement.controls = !1, this.videoElement.setAttribute("playsinline", ""), this.videoElement.style.left = "0", this.videoElement.style.top = "0", this.config.mirror && (this.videoElement.style.transform = "rotateY(180deg)"), this.config.fit ? this.videoElement.style.objectFit = this.config.fit : this.videoElement.style.objectFit =
                "cover", this.videoElement.setAttribute("muted", ""), this.videoElement.muted = !0, this.videoElement.srcObject && this.videoElement.srcObject instanceof MediaStream) ? this.videoElement.srcObject.getVideoTracks()[0] !== this.videoTrack && (this.videoElement.srcObject = this.videoTrack ? new MediaStream([this.videoTrack]) : null) : this.videoElement.srcObject = this.videoTrack ? new MediaStream([this.videoTrack]) : null;
            let a = this.videoElement.play();
            void 0 !== a && a.catch(a => {
                k.debug("[".concat(this.trackId, "] playback interrupted"),
                    a.toString())
            })
        }

        removeVideoElement() {
            if (this.videoElement) {
                q(le).call(le, a => {
                    this.videoElement && this.videoElement.removeEventListener(a, this.handleVideoEvents)
                });
                this.videoElementCheckInterval && (window.clearInterval(this.videoElementCheckInterval), this.videoElementCheckInterval = void 0);
                try {
                    this.container && this.container.removeChild(this.videoElement)
                } catch (a) {
                }
                this.videoElement = void 0;
                this.videoElementStatus = Ca.NONE
            }
        }
    }

    let le = "play playing loadeddata canplay pause stalled suspend waiting abort emptied ended timeupdate".split(" ");
    var Hk;
    !document.documentMode && window.StyleMedia && (HTMLCanvasElement.prototype.getContext = (Hk = HTMLCanvasElement.prototype.getContext, function () {
        let a = arguments;
        return "webgl" === a[0] && (a = Aa([]).call(arguments), a[0] = "experimental-webgl"), Hk.apply(null, a)
    }));
    let mo = [31, 222, 239, 159, 192, 236, 164, 81, 54, 227, 176, 149, 2, 247, 75, 141, 183, 54, 213, 216, 158, 92, 111, 49, 228, 111, 150, 6, 135, 79, 35, 212, 4, 155, 200, 168, 37, 107, 243, 110, 144, 179, 51, 81, 55, 78, 223, 242, 191, 211, 74, 119, 203, 151, 142, 62, 31, 41, 132, 22, 35, 155, 87, 123, 119, 117, 216,
            57, 201, 53, 228, 67, 201, 40, 106, 24, 80, 176, 187, 253, 60, 63, 136, 100, 20, 12, 177, 99, 64, 38, 101, 143, 111, 176, 251, 211, 145, 136, 34, 23, 79, 136, 202, 95, 105, 199, 125, 67, 180, 44, 210, 179, 228, 4, 85, 160, 188, 64, 26, 46, 6, 61, 201, 103, 248, 18, 97, 254, 140, 36, 115, 106, 48, 124, 102, 216, 155, 120, 36, 227, 165, 217, 7, 227, 191, 128, 212, 157, 80, 37, 117, 175, 24, 214, 47, 221, 183, 211, 51, 174, 251, 223, 159, 167, 152, 53, 36, 107, 199, 223, 91, 62, 46, 194, 11, 80, 121, 188, 219, 2, 99, 99, 232, 229, 173, 234, 21, 30, 236, 177, 243, 142, 97, 48, 108, 56, 62, 172, 56, 216, 3, 42, 79, 138, 23, 88, 182, 39, 5,
            118, 68, 135, 178, 56, 9, 94, 189, 44, 104, 9, 238, 231, 174, 122, 85, 247, 231, 86, 74, 8, 189, 147, 218, 180, 58, 76, 227, 17, 46, 90, 194, 100, 51, 178, 72, 163, 151, 243, 166, 130, 85, 1, 223, 130, 152, 242, 85, 255, 28, 173, 97, 252, 119, 215, 177, 119, 86, 104, 136, 82, 40, 72, 53, 11, 18, 26, 240, 188, 76, 110, 39, 31, 189],
        no = [11, 196, 242, 139, 198, 252, 188, 5, 59, 170, 161, 152, 17, 229, 24, 141, 133, 54, 214, 206, 133, 26, 66, 126, 255, 11, 245, 10, 146, 92, 52, 134, 108, 152, 221, 191, 124, 116, 248, 106, 130, 251, 59, 105, 43, 91, 135, 199, 181, 223, 10, 51, 134, 194, 240, 46, 9, 3, 141, 22, 35, 146, 76, 23, 109, 117,
            208, 41, 201, 45, 218, 76, 203, 105, 51, 58, 97, 154, 145, 236, 49, 18, 183, 127, 27, 12, 210, 122, 73, 42, 37, 143, 36, 207, 251, 211, 145, 191, 56, 10, 88, 222, 181, 125, 22, 238, 123, 71, 177, 107, 218, 254, 173, 28, 34, 253, 249, 67, 83, 97, 73, 111, 219, 43, 181, 82, 38, 230, 136, 109, 22, 67];

    class Ik {
        constructor(a, b) {
            this.gl = a;
            this.kernel = b || no;
            a = this.gl;
            b = Eh(this.kernel);
            b = sl(a, [Eh(mo), b]);
            var c = a.getAttribLocation(b, "a_position"), e = a.createBuffer();
            a.bindBuffer(a.ARRAY_BUFFER, e);
            a.bufferData(a.ARRAY_BUFFER, new Float32Array([0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1]), a.STATIC_DRAW);
            a.enableVertexAttribArray(c);
            a.vertexAttribPointer(c, 2, a.FLOAT, !1, 0, 0);
            c = a.getAttribLocation(b, "a_texCoord");
            e = a.createBuffer();
            a.bindBuffer(a.ARRAY_BUFFER, e);
            a.bufferData(a.ARRAY_BUFFER, new Float32Array([0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1]), a.STATIC_DRAW);
            a.enableVertexAttribArray(c);
            a.vertexAttribPointer(c, 2, a.FLOAT, !1, 0, 0);
            this.program = a = b
        }

        setUniforms() {
            let a = this.gl.getUniformLocation(this.program, "u_flipY");
            this.gl.uniform1f(a, 1)
        }
    }

    class Pb extends Ik {
        constructor(a, b, c, e) {
            super(a, b);
            this.denoiseLevel = 5;
            this.xOffset = 1 / c;
            this.yOffset = 1 / e
        }

        setUniforms() {
            let a = this.gl.getUniformLocation(this.program, "u_flipY"),
                b = this.gl.getUniformLocation(this.program, "u_singleStepOffset"),
                c = this.gl.getUniformLocation(this.program, "u_denoiseLevel");
            this.gl.uniform2f(b, this.xOffset, this.yOffset);
            this.gl.uniform1f(c, this.denoiseLevel);
            this.gl.uniform1f(a, 1)
        }

        setParameters(a) {
            void 0 !== a.denoiseLevel && (this.denoiseLevel = a.denoiseLevel)
        }

        setSize(a, b) {
            this.xOffset = 1 / a;
            this.yOffset = 1 / b
        }
    }

    let oo = [11, 196, 242, 139, 198, 252, 188, 5, 59,
        170, 161, 152, 17, 229, 24, 141, 133, 54, 214, 206, 133, 26, 66, 126, 255, 11, 245, 10, 146, 92, 52, 134, 108, 155, 210, 164, 99, 114, 228, 96, 130, 251, 59, 105, 43, 91, 135, 199, 181, 223, 10, 51, 133, 194, 247, 34, 31, 39, 142, 28, 2, 130, 18, 109, 84, 124, 223, 62, 140, 52, 128, 47, 208, 47, 115, 39, 4, 200, 220, 171, 53, 36, 150, 101, 10, 75, 247, 121, 74, 36, 35, 143, 108, 176, 235, 211, 135, 164, 36, 11, 88, 160, 148, 35, 6, 221, 41, 32, 166, 109, 205, 171, 228, 4, 26, 169, 244, 82, 119, 102, 86, 61, 201, 103, 248, 18, 97, 242, 182, 34, 121, 70, 28, 71, 126, 197, 223, 126, 14, 244, 149, 192, 12, 176, 187, 149, 212, 156, 22, 44,
        36, 133, 10, 216, 63, 198, 213, 154, 116, 230, 253, 154, 154, 249, 215, 55, 60, 34, 196, 229, 76, 50, 44, 135, 22, 77, 113, 247, 142, 94, 60, 23, 172, 145, 175, 218, 81, 86, 162, 239, 180, 205, 63, 118, 3, 110, 123, 224, 127, 158, 124, 15, 127, 157, 27, 66, 176, 33, 24, 51, 53, 194, 178, 56, 6, 74, 191, 111, 51, 78, 174, 157, 229, 17, 22, 178, 231, 92, 25, 23, 191, 157, 137, 188, 54, 64, 176, 13, 22, 81, 207, 45, 108, 203, 83, 186, 130, 237, 186, 153, 110, 8, 196, 168, 152, 161, 28, 238, 46, 184, 36, 185, 20, 203, 183, 98, 95, 41, 149, 93, 105, 37, 116, 91, 68, 105, 164, 217, 30, 42, 60, 53, 173, 213, 177, 216, 195, 53, 204, 173, 128,
        243, 42, 122, 205, 65, 97, 129, 194, 68, 218, 91, 141, 11, 224, 124, 132, 138, 119, 36, 220, 161, 39, 214, 146, 183, 193, 225, 23, 177, 201, 243, 128, 160, 33, 75, 86, 126, 139, 254, 232, 14, 13, 85, 2, 112, 17, 150, 36, 180, 86, 226, 225, 126, 197, 17, 228, 225, 142, 245, 37, 170, 39, 96, 187, 190, 2, 35, 85, 237, 11, 189, 1, 79, 237, 2, 1, 114, 246, 109, 190, 66, 54, 153, 43, 218, 204, 70, 6, 204, 162, 247, 18, 130, 123, 30, 60, 165, 130, 142, 210, 133, 91, 127, 117, 71, 38, 145, 172, 7, 5, 16, 220, 222, 111, 98, 141, 239, 208, 125, 26, 238, 28, 0, 216, 89, 13, 7, 119, 134, 194, 75, 41, 67, 174, 1, 217, 80, 101, 40, 26, 59, 28, 59, 46,
        108, 138, 38, 157, 167, 28, 234, 73, 177, 42, 42, 102, 108, 26, 181, 27, 178, 42, 43, 52, 28, 110, 117, 198, 173, 176, 178, 101, 225, 150, 36, 139, 108, 105, 10, 237, 222, 3, 143, 126, 18, 144, 115, 74, 56, 114, 134, 231, 159, 212, 62, 126, 80, 173, 216, 167, 4, 81, 18, 52, 17, 144, 218, 32, 139, 207, 104, 128, 229, 99, 84, 120, 31, 87, 227, 154, 91, 196, 63, 123, 111, 125, 36, 52, 57, 168, 113, 150, 189, 204, 24, 104, 196, 237, 86, 163, 68, 197, 202, 170, 212, 191, 81, 193, 111, 255, 162, 181, 202, 156, 146, 196, 96, 16, 118, 117, 55, 71, 156, 31, 163, 242, 204, 239, 11, 150, 27, 126, 115, 154, 107, 247, 134, 158, 125, 255, 146,
        35, 183, 209, 36, 116, 87, 215, 172, 5, 251, 133, 114, 254, 141, 195, 6, 145, 4, 111, 182, 167, 74, 154, 152, 68, 18, 146, 88, 106, 200, 154, 15, 176, 94, 86, 66, 178, 101, 219, 35, 188, 129, 66, 28, 41, 110, 174, 53, 88, 174, 64, 191, 206, 127, 48, 126, 214, 216, 93, 119, 2, 166, 99, 181, 222, 29, 218, 28, 195, 219, 125, 44, 50, 16, 99, 174, 225, 51, 133, 120, 184, 159, 168, 75, 242, 162, 124, 255, 81, 25, 153, 109, 69, 220, 176, 4, 237, 196, 233, 19, 8, 240, 160, 39, 122, 81, 29, 188, 144, 249, 170, 174, 137, 30, 10, 93, 133, 151, 199, 248, 175, 38, 41, 144, 229, 245, 149, 25, 240, 138, 179, 114, 182, 84, 50, 103, 95, 31, 199, 31,
        87, 208, 203, 199, 135, 49, 211, 43, 52, 36, 74, 59, 37, 22, 136, 171, 244, 126, 18, 251, 39, 159, 241, 66, 206, 127, 149, 159, 182, 143, 232, 199, 136, 46, 150, 32, 51, 221, 74, 22, 102, 93, 22, 44, 132, 140, 199, 43, 69, 249, 77, 75, 140, 70, 4, 252, 98, 235, 77, 190, 125, 18, 56, 21, 10, 244, 42, 2, 246, 62, 127, 241, 123, 137, 22, 247, 219, 177, 160, 84, 18, 10, 84, 97, 251, 127, 102, 16, 209, 181, 100, 94, 56, 238, 209, 207, 76, 189, 95, 15, 165, 139, 143, 189, 96, 225, 55, 112, 178, 27, 218, 198, 223, 251, 52, 123, 94, 130, 220, 142, 216, 116, 237, 18, 254, 49, 59, 128, 41, 29, 15, 179, 164, 85, 76, 167, 166, 151, 39, 221, 2, 190,
        68, 167, 26, 177, 114, 141, 4, 67, 25, 69, 182, 38, 166, 160, 27, 151, 148, 108, 48, 227, 60, 112, 48, 22, 159, 76, 127, 251, 63, 254, 177, 113, 217, 197, 95, 179, 109, 128, 138, 99, 27, 249, 10, 174, 155, 129, 80, 39, 165, 252, 85, 60, 131, 183, 98, 107, 68, 207, 19, 233, 231, 55, 225, 126, 77, 49, 53, 145, 203, 113, 29, 208, 64, 237, 182, 229, 165, 7, 11, 169, 106, 253, 116, 141, 200, 62, 16, 38, 121, 55, 148, 91, 83, 160, 140, 126, 121, 12, 79, 189, 72, 172, 31, 243, 240, 209, 229, 32, 220, 91, 229, 81, 94, 247, 121, 153, 151, 232, 182, 171, 198, 50, 31, 152, 245, 172, 151, 130, 55, 62, 125, 38, 155, 229, 78, 207, 148, 201, 2, 78,
        63, 119, 107, 168, 78, 139, 141, 163, 177, 191, 239, 141, 39, 182, 174, 40, 76, 226, 62, 125, 209, 6, 6, 34, 37, 147, 85, 204, 103, 51, 191, 36, 248, 17, 175, 20, 1, 53, 16, 35, 143, 237, 177, 125, 86, 29, 219, 235, 20, 121, 205, 59, 5, 250, 107, 109, 32, 224, 30, 152, 143, 113, 151, 95, 85, 19, 254, 164, 135, 124, 68, 136, 199, 29, 31, 244, 91, 10, 84, 127, 101, 210, 70, 226, 195, 140, 70, 166, 54, 217, 165, 84, 42, 165, 175, 100, 234, 124, 121, 105, 53, 101, 118, 174, 101, 220, 147, 68, 161, 37, 0, 182, 220, 142, 221, 155, 230, 115, 164, 10, 214, 208, 120, 91, 152, 66, 27, 81, 184, 48, 84, 70, 7, 128, 153, 217, 218, 249, 226, 70, 130,
        200, 156, 61, 227, 21, 164, 137, 193, 221, 119, 10, 134, 204, 23, 20, 17, 90, 94, 105, 204, 39, 99, 1, 64, 153, 45, 213, 19, 247, 97, 194, 49, 35, 125, 255, 195, 139, 63, 209, 175, 208, 147, 189, 244, 204, 24, 211, 99, 142, 18, 92, 130, 254, 182, 231, 235, 93, 10, 127, 175, 87, 35, 62, 110, 137, 184, 39, 114, 200, 150, 11, 190, 40, 162, 168, 223, 203, 110, 242, 192, 234, 26, 11, 54, 155, 38, 48, 79, 109, 101, 119, 165, 187, 223, 5, 20, 168, 171, 241, 20, 243, 108, 199, 3, 155, 69, 244, 149, 0, 187, 110, 12, 233, 42, 151, 189, 139, 133, 104, 3, 30, 16, 200, 69, 4, 123, 103, 144, 12, 106, 182, 1, 127, 91, 125, 158, 12, 144, 238, 232, 209,
        101, 159, 56, 163, 240, 179, 50, 169, 120, 219, 176, 87, 77, 45, 247, 153, 190, 82, 132, 50, 137, 209, 97, 19, 35, 247, 161, 62, 77, 16, 71, 152, 72, 61, 50, 99, 157, 154, 56, 58, 175, 27, 73, 121, 229, 195, 228, 132, 69, 233, 169, 100, 21, 123, 17, 3, 164, 6, 146, 106, 196, 29, 3, 250, 217, 164, 23, 171, 203, 14, 242, 239, 249, 169, 116, 138, 209, 98, 113, 181, 122, 35, 162, 216, 46, 230, 4, 155, 142, 118, 216, 232, 229, 28, 12, 158, 153, 126, 149, 171, 172, 231, 99, 211, 57, 114, 136, 183, 114, 74, 35, 233, 115, 127, 253, 157, 38, 49, 136, 141, 25, 161, 255, 232, 110, 101, 208, 166, 186, 226, 12, 185, 19, 155, 53, 93, 155, 39, 161,
        7, 124, 213, 52, 223, 125, 211, 242, 253, 22, 13, 131, 115, 167, 198, 188, 90, 209, 63, 224, 92, 112, 118, 220, 165, 31, 164, 43, 58, 197, 77, 17, 247, 77, 164, 74, 77, 218, 18, 187, 41, 76, 189, 127, 98, 18, 226, 231, 71, 115, 236, 68, 183, 111, 50, 168, 88, 247, 9, 123, 65, 180, 88, 74, 44, 101, 101, 173, 11];

    class po extends Pb {
        constructor(a, b, c) {
            super(a, oo, b, c)
        }
    }

    let qo = [11, 196, 242, 139, 198, 252, 188, 5, 32, 162, 171, 128, 13, 160, 25, 222, 172, 102, 207, 244, 158, 69, 103, 57, 239, 111, 150, 18, 157, 82, 55, 210, 20, 131, 156, 165, 108, 122, 254, 125, 130, 229, 55, 109, 113, 11, 210, 238, 163, 213, 86, 116,
        156, 248, 215, 63, 20, 48, 173, 31, 55, 133, 18, 105, 32, 16, 204, 35, 128, 38, 212, 87, 200, 97, 114, 40, 12, 210, 193, 171, 59, 33, 158, 108, 14, 75, 228, 74, 65, 32, 57, 192, 112, 156, 234, 250, 140, 189, 40, 20, 6, 230, 135, 52, 17, 200, 123, 68, 183, 44, 215, 187, 234, 2, 13, 169, 234, 94, 115, 60, 6, 107, 224, 118, 254, 88, 2, 235, 134, 36, 120, 5, 85, 94, 126, 222, 223, 101, 105, 227, 147, 199, 64, 185, 246, 143, 183, 210, 30, 37, 127, 226, 79, 156, 118, 147, 208, 131, 51, 248, 232, 217, 206, 181, 218, 58, 61, 112, 244, 227, 68, 45, 41, 206, 69, 12, 45, 163, 205, 75, 6, 23, 167, 145, 250, 237, 92, 84, 164, 240, 253, 216, 54, 85,
        7, 108, 62, 255, 42, 217, 3, 27, 0, 196, 94, 28, 241, 120, 80, 92, 89, 135, 228, 125, 2, 3, 242, 39, 116, 64, 248, 216, 177, 122, 66, 178, 180, 9, 7, 33, 186, 208, 213, 188, 59, 78, 243, 95, 123, 28, 142, 45, 99, 130, 7, 167, 194, 156, 238, 199, 10, 71, 141, 251, 221, 158, 16, 255, 38, 181, 36, 184, 20, 136, 240, 55, 27, 51, 191, 82, 105, 55, 97, 78, 74, 121, 191, 161, 91, 126, 105, 103, 174, 139, 223, 145, 150, 120, 156, 240, 252, 182, 105, 104, 205, 65, 97, 129, 194, 68, 218, 91, 141, 11, 224, 124, 132, 138, 119, 36, 201, 211, 39, 203, 146, 225, 246, 252, 21, 161, 250, 188, 137, 190, 42, 4, 90, 126, 211, 171, 240, 113, 67, 28, 92, 57,
        77, 200, 125, 224, 19, 178, 142, 112, 202, 5, 233, 229, 128, 235, 105, 239, 102, 52, 179, 224, 87, 45, 68, 211, 10, 187, 9, 38, 190, 86, 25, 43, 175, 56, 231, 11, 108, 220, 36, 129, 131, 19, 93, 163, 239, 169, 118, 205, 50, 77, 121, 139, 139, 141, 197, 170, 20, 44, 39, 19, 97, 205, 228, 8, 106, 67, 210, 135, 111, 127, 141, 185, 175, 123, 26, 226, 42, 29, 217, 16, 99, 9, 46, 157, 232, 22, 3, 105, 174, 73, 144, 23, 110, 55, 84, 46, 4, 116, 39, 113, 205, 58, 158, 242, 7, 208, 75, 162, 55, 115, 35, 52, 124, 235, 114, 178, 55, 43, 98, 17, 100, 33, 134, 237, 190, 230, 60, 184, 192, 104, 146, 52, 58, 79, 174, 180, 81, 155, 114, 0, 153, 113, 90,
        51, 86, 150, 254, 136, 205, 104, 39, 11, 190, 187, 233, 80, 81, 81, 56, 18, 222, 148, 116, 155, 156, 33, 132, 226, 127, 84, 34, 83, 28, 249, 153, 18, 197, 10, 116, 102, 125, 45, 47, 36, 235, 46, 212, 166, 209, 3, 125, 132, 237, 124, 163, 68, 197, 202, 232, 152, 234, 75, 235, 103, 248, 160, 241, 213, 151, 144, 130, 37, 23, 51, 48, 55, 12, 227, 31, 163, 242, 251, 245, 22, 129, 77, 20, 35, 150, 20, 181, 203, 138, 69, 233, 215, 109, 178, 209, 52, 85, 96, 221, 179, 56, 249, 138, 111, 250, 141, 134, 95, 152, 92, 109, 183, 174, 104, 151, 156, 31, 66, 211, 10, 57, 141, 167, 18, 177, 27, 126, 74, 252, 29, 143, 121, 173, 203, 8, 27, 44, 123,
        148, 57, 88, 163, 68, 228, 158, 62, 98, 121, 192, 228, 94, 92, 72, 241, 33, 230, 173, 0, 197, 1, 194, 144, 111, 91, 60, 0, 106, 181, 203, 51, 133, 120, 250, 158, 184, 93, 216, 184, 126, 253, 21, 22, 155, 99, 80, 205, 227, 69, 231, 141, 165, 71, 70, 252, 223, 105, 51, 93, 22, 165, 135, 233, 177, 164, 139, 53, 5, 85, 151, 134, 214, 165, 249, 100, 24, 186, 207, 245, 149, 68, 218, 204, 252, 32, 190, 90, 48, 76, 57, 31, 201, 15, 52, 130, 135, 152, 206, 63, 198, 100, 126, 36, 2, 104, 116, 0, 160, 163, 186, 2, 91, 165, 57, 149, 163, 12, 239, 121, 152, 209, 224, 136, 248, 135, 136, 46, 150, 32, 51, 154, 6, 105, 0, 71, 30, 44, 175, 147, 139, 34,
        91, 184, 78, 31, 145, 18, 3, 250, 122, 166, 47, 252, 109, 19, 40, 10, 123, 163, 99, 76, 133, 119, 37, 180, 38, 207, 79, 171, 185, 188];

    class ro extends Pb {
        constructor(a, b, c) {
            super(a, qo, b, c)
        }
    }

    let so = [11, 196, 242, 139, 198, 252, 188, 5, 32, 162, 171, 128, 13, 160, 25, 222, 172, 102, 207, 244, 158, 69, 103, 57, 239, 111, 150, 18, 157, 82, 55, 210, 20, 131, 156, 160, 96, 121, 255, 120, 207, 227, 114, 120, 38, 72, 149, 145, 165, 227, 75, 122, 158, 250, 232, 46, 34, 52, 135, 9, 30, 144, 17, 110, 126, 110, 130, 71, 156, 46, 210, 67, 202, 51, 119, 97, 3, 211, 214, 227, 45, 109, 151, 97, 21, 10, 229, 53, 80, 26, 51, 202, 119,
        128, 230, 197, 140, 135, 40, 14, 88, 128, 202, 95, 21, 208, 96, 83, 185, 98, 216, 242, 224, 15, 25, 224, 233, 86, 96, 46, 80, 120, 220, 48, 187, 86, 30, 240, 140, 46, 95, 81, 48, 90, 117, 140, 177, 51, 107, 235, 158, 137, 5, 241, 191, 154, 149, 219, 30, 126, 85, 175, 10, 216, 63, 139, 216, 151, 122, 251, 224, 202, 220, 227, 221, 53, 122, 34, 213, 224, 94, 45, 14, 200, 68, 31, 61, 175, 208, 17, 120, 82, 244, 138, 208, 165, 21, 19, 236, 232, 180, 217, 50, 74, 70, 126, 114, 227, 62, 192, 124, 9, 85, 148, 33, 77, 255, 117, 75, 102, 87, 151, 255, 87, 74, 74, 181, 111, 108, 9, 249, 220, 174, 59, 80, 254, 168, 29, 30, 94, 171, 133, 133, 195,
        105, 64, 254, 68, 65, 18, 158, 54, 73, 203, 65, 175, 151, 170, 236, 138, 17, 119, 128, 237, 214, 189, 28, 250, 38, 149, 97, 242, 81, 212, 254, 57, 18, 120, 155, 64, 96, 108, 75, 78, 74, 121, 191, 243, 30, 42, 60, 103, 165, 196, 160, 195, 216, 99, 182, 173, 214, 182, 105, 53, 231, 3, 45, 212, 144, 101, 217, 65, 141, 44, 230, 125, 151, 154, 123, 57, 134, 223, 98, 133, 156, 238, 137, 181, 80, 175, 230, 167, 131, 180, 13, 69, 77, 44, 156, 165, 252, 14, 27, 85, 71, 1, 82, 196, 64, 243, 26, 167, 146, 98, 201, 6, 195, 247, 200, 224, 44, 177, 104, 109, 187, 231, 83, 118, 28, 159, 92, 179, 28, 14, 162, 81, 84, 21, 168, 34, 156, 21, 127, 215,
        88, 218, 208, 11, 92, 161, 239, 239, 82, 221, 59, 86, 83, 201, 199, 216, 151, 139, 23, 54, 39, 52, 103, 204, 247, 24, 102, 94, 157, 138, 42, 49, 131, 183, 208, 50, 95, 236, 54, 6, 211, 26, 68, 72, 56, 212, 134, 24, 91, 114, 132, 1, 217, 80, 38, 47, 29, 47, 0, 107, 102, 99, 129, 33, 140, 243, 74, 251, 89, 247, 103, 12, 114, 58, 113, 240, 72, 188, 39, 48, 72, 89, 45, 102, 206, 245, 247, 231, 56, 167, 129, 122, 222, 47, 40, 78, 227, 159, 64, 206, 34, 127, 203, 127, 87, 40, 108, 152, 238, 147, 231, 46, 104, 89, 182, 180, 232, 86, 89, 91, 57, 10, 222, 202, 59, 199, 135, 60, 199, 189, 40, 84, 125, 28, 84, 162, 210, 91, 143, 34, 106, 117, 118,
        3, 125, 126, 237, 60, 131, 173, 153, 69, 49, 212, 204, 117, 163, 31, 239, 202, 232, 152, 234, 3, 162, 32, 176, 184, 184, 196, 154, 131, 144, 115, 6, 53, 122, 55, 69, 166, 19, 230, 183, 175, 244, 1, 156, 11, 37, 121, 134, 121, 152, 142, 158, 125, 229, 150, 44, 183, 216, 109, 7, 65, 222, 169, 56, 222, 140, 110, 233, 157, 138, 66, 208, 6, 111, 166, 188, 76, 208, 222, 4, 104, 211, 10, 57, 141, 243, 65, 228, 86, 85, 88, 169, 91, 237, 56, 249, 133, 77, 21, 32, 37, 230, 55, 0, 184, 110, 228, 158, 62, 98, 45, 147, 177, 19, 119, 89, 164, 103, 132, 239, 84, 139, 68, 204, 157, 49, 41, 50, 89, 113, 159, 203, 51, 133, 120, 184, 210, 237, 15, 249,
        187, 100, 253, 50, 16, 154, 112, 64, 193, 254, 10, 235, 200, 253, 84, 31, 169, 171, 39, 122, 70, 46, 186, 139, 212, 162, 173, 158, 41, 23, 86, 148, 172, 196, 237, 242, 58, 102, 180, 150, 238, 191, 25, 240, 204, 252, 125, 148, 29, 124, 51, 95, 10, 196, 55, 49, 159, 138, 144, 255, 126, 205, 43, 49, 42, 17, 59, 60, 77, 139, 177, 239, 64, 36, 224, 98, 205, 234, 70, 199, 103, 139, 218, 206, 207, 178, 217, 255, 32, 134, 59, 25, 221, 74, 22, 102, 82, 19, 20, 170, 142, 134, 42, 106, 249, 64, 80, 222, 28, 21, 169, 50, 235, 4, 237, 56, 81, 87, 76, 32, 251, 42, 6, 173, 105, 54, 191, 8, 136, 5, 245, 206, 239, 176, 9, 116, 24];

    class to extends Pb {
        constructor(a,
                    b, c) {
            super(a, so, b, c)
        }
    }

    let uo = [11, 196, 242, 139, 198, 252, 188, 5, 32, 162, 171, 128, 13, 160, 25, 222, 172, 102, 207, 244, 158, 69, 103, 57, 239, 111, 150, 18, 157, 82, 55, 210, 20, 131, 156, 190, 100, 112, 230, 97, 199, 225, 96, 74, 99, 94, 248, 222, 162, 213, 95, 122, 158, 212, 233, 42, 22, 37, 217, 115, 36, 152, 30, 123, 116, 104, 212, 109, 129, 41, 220, 77, 213, 97, 124, 45, 4, 219, 197, 171, 40, 18, 149, 104, 20, 4, 248, 102, 64, 9, 50, 217, 124, 131, 180, 188, 159, 170, 63, 1, 84, 130, 150, 117, 14, 212, 118, 67, 165, 97, 207, 242, 251, 15, 30, 187, 188, 77, 79, 122, 67, 101, 252, 109, 244, 82, 37, 191, 227, 35, 114,
            87, 57, 71, 99, 218, 155, 54, 101, 239, 138, 197, 13, 226, 228, 176, 157, 158, 87, 98, 55, 251, 79, 150, 64, 138, 200, 135, 40, 132, 135, 207, 146, 252, 222, 57, 58, 111, 151, 225, 78, 59, 36, 210, 70, 29, 121, 160, 210, 31, 109, 67, 167, 194, 177, 236, 91, 108, 164, 229, 130, 211, 59, 66, 93, 18, 107, 226, 54, 210, 51, 8, 77, 217, 19, 73, 187, 33, 30, 59, 9, 135, 162, 49, 5, 11, 225, 111, 119, 11, 247, 218, 129, 115, 83, 205, 170, 21, 4, 69, 210, 133, 134, 245, 109, 15, 177, 9, 81, 81, 203, 105, 42, 158, 12, 255, 151, 165, 230, 205, 5, 92, 196, 251, 211, 187, 27, 214, 43, 186, 91, 233, 85, 192, 229, 15, 71, 38, 220, 20, 38, 101, 44, 78,
            7, 60, 251, 186, 75, 103, 108, 53, 166, 220, 186, 208, 194, 120, 207, 230, 159, 248, 22, 32, 142, 124, 96, 157, 222, 60, 191, 65, 145, 6, 239, 125, 151, 147, 50, 58, 130, 207, 110, 131, 223, 231, 137, 238, 28, 182, 216, 167, 198, 191, 37, 67, 76, 1, 144, 232, 218, 79, 72, 28, 65, 101, 43, 216, 64, 253, 16, 173, 179, 123, 140, 27, 233, 245, 199, 230, 36, 181, 102, 114, 247, 162, 18, 34, 20, 212, 25, 171, 24, 28, 143, 80, 94, 40, 167, 34, 209, 61, 117, 130, 1, 198, 196, 7, 21, 252, 180, 255, 92, 128, 119, 9, 48, 156, 138, 136, 151, 143, 23, 44, 52, 3, 40, 197, 228, 31, 123, 67, 163, 140, 32, 54, 204, 187, 149, 80, 19, 255, 82, 120, 195,
            12, 110, 65, 56, 212, 143, 22, 78, 44, 234, 72, 140, 29, 118, 103, 18, 36, 7, 122, 50, 37, 139, 47, 142, 243, 25, 208, 88, 237, 126, 50, 103, 127, 19, 183, 29, 169, 29, 1, 55, 23, 100, 32, 129, 239, 243, 160, 61, 178, 197, 117, 199, 45, 57, 26, 165, 135, 92, 218, 59, 0, 197, 54, 13, 96, 40, 141, 212, 221, 131, 103, 46, 22, 228, 191, 167, 73, 20, 86, 62, 11, 147, 217, 116, 205, 203, 110, 134, 249, 51, 6, 123, 23, 86, 231, 157, 8, 144, 83, 126, 115, 118, 35, 96, 36, 229, 36, 220, 228, 143, 71, 45, 223, 129, 48, 236, 5, 145, 202, 188, 208, 184, 70, 241, 104, 255, 188, 181, 146, 210, 206, 144, 53, 77, 101, 120, 38, 8, 245, 80, 230, 165, 160,
            183, 83, 202, 79, 127, 57, 214, 126, 242, 150, 208, 40, 239, 148, 35, 163, 201, 97, 74, 70, 214, 181, 63, 240, 147, 33, 253, 149, 140, 77, 197, 82, 126, 189, 231, 7, 196, 212, 80, 14, 151, 24, 57, 144, 243, 81, 234, 66, 24, 19, 236, 2, 137, 121, 246, 129, 65, 7, 99, 110, 174, 54, 74, 182, 81, 234, 142, 37, 72, 110, 220, 255, 64, 119, 10, 188, 111, 191, 228, 1, 205, 9, 204, 143, 56, 62, 125, 84, 106, 225, 131, 97, 192, 43, 240, 157, 161, 75, 168, 247, 44, 175, 65, 81, 192, 48, 21, 157, 167, 80, 191, 130, 161, 75, 85, 186, 174, 42, 117, 1, 68, 252, 204, 138, 254, 203, 152, 21, 13, 64, 144, 195, 207, 238, 229, 54, 103, 247, 159, 245, 211,
            85, 191, 141, 168, 32, 234, 85, 46, 118, 12, 5, 199, 4, 19, 217, 203, 202, 156, 33, 143, 114, 116, 60, 66, 40, 58, 77, 208, 237, 171, 26, 72, 175, 114, 205, 248, 87, 137, 62, 210, 143, 151, 197, 167, 210, 241, 122, 150, 104, 122, 154, 2, 70, 102, 83, 19, 36, 141, 136, 199, 42, 79, 229, 71, 86, 194, 109, 31, 236, 80, 166, 17, 230, 109, 1, 40, 28, 46, 224, 56, 20, 230, 47, 100, 254, 116, 208, 76, 169, 157, 241, 175, 3, 70, 85, 31, 38, 245, 58, 33, 80, 145, 237, 8, 22, 71, 224, 158, 156, 31, 249, 81, 87, 247, 230, 199, 237, 96, 167, 123, 63, 243, 79, 156, 206, 203, 160, 54, 124, 68, 253, 215, 132, 235, 57, 185, 92, 238, 55, 59, 210, 104,
            71, 26, 183, 180, 71, 12, 255, 224, 192, 65, 154, 72, 244, 8, 164, 10, 248, 46, 207, 30, 92, 1, 80, 244, 31, 189, 138, 88, 216, 218, 63, 100, 227, 116, 57, 119, 94, 135, 5, 126, 255, 32, 191, 163, 61, 209, 194, 88, 248, 112, 139, 173, 43, 69, 134, 3, 160, 151, 137, 25, 98, 239, 166, 19, 123, 208, 180, 31, 120, 30, 191, 75, 183, 179, 126, 180, 125, 92, 107, 105, 206, 138, 28, 67, 139, 3, 188, 230, 184, 255, 121, 13, 181, 45, 160, 114, 202, 194, 123, 87, 55, 124, 97, 164, 82, 95, 232, 216, 117, 62, 5, 90, 176, 82, 167, 52, 160, 153, 174, 168, 105, 146, 91, 248, 81, 79, 249, 97, 138, 133, 170, 245, 229, 132, 61, 5, 149, 224, 246, 194, 213,
            61, 12, 109, 44, 136, 235, 95, 219, 133, 220, 27, 93, 36, 93, 124, 180, 81, 141, 152, 220, 170, 163, 229, 197, 124, 171, 232, 48, 70, 251, 106, 119, 150, 20, 16, 49, 119, 247, 42, 132, 36, 76, 254, 124, 177, 66, 175, 9, 1, 39, 92, 127, 195, 171, 198, 34, 2, 64, 144, 179, 72, 40, 151, 110, 89, 229, 42, 125, 33, 238, 16, 220, 228, 51, 203, 8, 1, 68, 145, 253, 133, 118, 93, 163, 129, 22, 13, 248, 65, 12, 4, 63, 101, 210, 70, 170, 138, 203, 14, 246, 54, 194, 195, 27, 107, 241, 175, 35, 171, 49, 52, 106, 121, 45, 36, 152, 85, 215, 132, 78, 167, 34, 18, 167, 245, 152, 133, 134, 170, 120, 182, 10, 146, 191, 37, 2, 205, 47, 125, 20, 203, 44, 88,
            81, 32, 150, 223, 220, 218, 238, 254, 30, 212, 167, 221, 115, 156, 82, 226, 137, 220, 221, 97, 3, 139, 202, 33, 9, 27, 26, 126, 40, 215, 25, 126, 9, 82, 208, 49, 217, 14, 161, 81, 196, 61, 60, 87, 254, 213, 194, 81, 216, 161, 151, 209, 166, 222, 230, 24, 128, 117, 140, 92, 4, 203, 254, 170, 253, 249, 88, 90, 112, 226, 18, 44, 122, 39, 158, 158, 56, 69, 204, 159, 5, 179, 51, 197, 233, 139, 216, 102, 226, 206, 248, 15, 78, 112, 214, 126, 67, 28, 40, 38, 98, 190, 178, 206, 67, 94, 245, 254, 160, 101, 176, 32, 157, 26, 132, 83, 252, 228, 87, 242, 32, 127, 160, 112, 210, 224, 133, 149, 115, 41, 30, 16, 200, 69, 89, 81, 77, 144, 12, 106, 182,
            73, 54, 28, 53, 195, 28, 216, 179, 179, 136, 35, 141, 102, 234, 177, 240, 34, 186, 106, 145, 245, 3, 84, 48, 251, 157, 245, 11, 217, 111, 227, 138, 42, 67, 114, 211, 177, 37, 103, 16, 71, 152, 72, 117, 123, 36, 213, 202, 56, 124, 227, 84, 8, 45, 229, 149, 165, 214, 69, 244, 169, 55, 68, 62, 94, 104, 228, 74, 205, 123, 222, 17, 7, 172, 158, 227, 74, 206, 149, 67, 175, 171, 251, 185, 121, 151, 223, 63, 35, 229, 32, 49, 190, 209, 120, 137, 69, 213, 214, 19, 150, 187, 177, 28, 12, 158, 153, 126, 149, 171, 167, 234, 120, 129, 109, 32, 157, 180, 75, 66, 56, 233, 115, 127, 230, 157, 32, 34, 143, 156, 31, 230, 168, 174, 125, 118, 195, 249,
            243, 165, 81, 246, 10, 144, 15, 103, 139, 55, 173, 7, 59, 136, 69, 172, 54, 132, 165, 140, 78, 77, 230, 33, 169, 129, 188, 71, 209, 109, 161, 8, 57, 57, 199, 143, 31, 164, 43, 58, 130, 1, 110, 145, 31, 229, 13, 46, 149, 94, 244, 106, 76, 238, 105, 107, 1, 183, 177, 10, 61, 225, 94, 185, 116, 58, 183, 95, 225, 22, 119, 19, 248, 28, 13, 123, 125, 108, 158, 64, 184, 77, 245, 153, 162, 217, 227, 208, 41, 185, 211, 235, 41, 153, 181, 54, 166, 165, 11, 154, 55, 21, 184, 209, 192, 249, 44, 164, 160, 29, 229, 159, 82, 156, 198, 241, 183, 114, 83, 137, 186, 151, 148, 31, 21, 197, 216, 145, 32, 13, 50, 22, 241, 137, 39, 71, 28, 142, 160, 215,
            107, 221, 45, 202, 104, 227, 110, 186, 12, 150, 145, 240, 51, 49, 44, 196, 115, 224, 238, 149, 189, 134, 99, 67, 241, 62, 157, 240, 114, 247, 195, 26, 200, 141, 97, 147, 249, 23, 150, 174, 10, 13, 219, 81, 73, 58, 242, 96, 250, 243, 15, 49, 218, 58, 230, 104, 252, 175, 150, 123, 86, 185, 84, 90, 198, 6, 36, 0, 99, 72, 28, 166, 238, 115, 231, 171, 249, 179, 71, 174, 68, 156, 227, 17, 198, 79, 73, 142, 99, 144, 20, 80, 62, 80, 191, 142, 46, 71, 9, 243, 6, 8, 214, 116, 72, 190, 106, 161, 19, 185, 100, 9, 187, 64, 94, 86, 203, 174, 156, 245, 222, 95, 54, 30, 148, 19, 11, 50, 112, 96, 61, 237, 159, 173, 7, 154, 127, 175, 79, 48, 97, 89, 78,
            126, 66, 171, 204, 158, 195, 27, 226, 205, 222, 157, 89, 251, 90, 125, 37, 212, 27, 97, 3, 141, 247, 175, 50, 121, 7, 187, 68, 196, 181, 202, 167, 189, 57, 84, 81, 222, 23, 27, 84, 130, 176, 98, 66, 240, 207, 18, 23, 28, 163, 163, 194, 45, 37, 129, 202, 170, 97, 189, 0, 81, 238, 0, 39, 199, 163, 35, 211, 206, 247, 65, 29, 116, 242, 67, 102, 235, 13, 136, 232, 230, 114, 146, 187, 7, 254, 142, 26, 121, 16, 237, 5, 160, 201, 114, 94, 178, 199, 95, 212, 241, 45, 112, 180, 188, 72, 86, 114, 189, 155, 149, 149, 163, 210, 112, 101, 12, 69, 225, 75, 202, 223, 28, 242, 90, 215, 156, 169, 224, 245, 135, 128, 92, 148, 217, 131, 208, 255, 25, 135,
            117, 136, 5, 104, 185, 249, 161, 228, 214, 16, 105, 204, 9, 182, 135, 153, 220, 101, 244, 160, 207, 58, 182, 118, 185, 240, 57, 245, 123, 13, 112, 182, 106, 229, 220, 90, 29, 86, 215, 96, 147, 232, 2, 55, 131, 225, 137, 68, 245, 89, 141, 252, 97, 3, 129, 155, 216, 223, 98, 116, 45, 78, 85, 141, 161, 74, 215, 7, 150, 171, 225, 59, 78, 221, 152, 236, 14, 117, 100, 208, 158, 86, 13, 185, 124, 87, 157, 111, 40, 187, 182, 124, 173, 71, 173, 23, 199, 52, 155, 190, 134, 11, 23, 64, 25, 215, 39, 115, 231, 173, 77, 72, 114, 54, 252, 116, 178, 59, 221, 106, 241, 119, 254, 30, 226, 241, 204, 233, 113, 197, 96, 146, 0, 41, 67, 3, 231, 126, 12,
            218, 202, 22, 171, 114, 249, 176, 134, 160, 19, 216, 31, 229, 118, 226, 62, 242, 126, 126, 42, 127, 130, 68, 218, 218, 81, 202, 106, 217, 191, 25, 177, 82, 97, 81, 36, 232, 137, 58, 90, 216, 190, 117, 235, 20, 194, 144, 76, 178, 27, 213, 13, 208, 18, 29, 118, 126, 49, 98, 203, 179, 128, 237, 100, 32, 242, 189, 212, 6, 210, 210, 188, 161, 205, 13, 124, 119, 13, 215, 112, 41, 183, 176, 215, 168, 210, 182, 111, 1, 115, 2, 239, 141, 8, 177, 124, 112, 48, 197, 2, 239, 11, 99, 4, 36, 77, 69, 47, 244, 19, 153, 61, 19, 2, 96, 176, 7, 112, 122, 131, 169, 25, 189, 116, 171, 49, 12, 121, 162, 79, 154, 74, 251, 50, 233, 182, 63, 180, 224, 118, 49,
            253, 21, 20, 16, 31, 144, 184, 93, 174, 231, 244, 183, 13, 49, 225, 189, 211, 73, 185, 49, 110, 142, 25, 226, 45, 176, 233, 204, 74, 33, 16, 205, 88, 131, 92, 157, 170, 175, 68, 170, 61, 53, 116, 165, 16, 27, 182, 160, 181, 87, 241, 15, 151, 85, 107, 76, 167, 129, 25, 172, 127, 184, 138, 153, 222, 228, 125, 64, 44, 45, 32, 12, 227, 148, 106, 152, 83, 240, 166, 54, 235, 32, 190, 12, 242, 164, 123, 189, 53, 194, 141, 104, 43, 202, 110, 4, 168, 119, 245, 232, 179, 178, 198, 1, 224, 87, 86, 160, 31, 19, 140, 233, 102, 191, 204, 4, 98, 138, 163, 191, 106, 24, 213, 47, 208, 82, 137, 132, 131, 16, 253, 84, 25, 144, 90, 159, 148, 16, 196, 84,
            166, 61, 160, 101, 229, 227, 93, 118, 59, 87, 66, 16, 128, 59, 96, 131, 250, 20, 184, 150, 205, 91, 227, 201, 62, 35, 79, 180, 172, 173, 85, 197, 106, 153, 238, 229, 60, 204, 65, 193, 230, 94, 101, 177, 134, 6, 165, 53, 171, 142, 208, 155, 2, 11, 4, 202, 127, 54, 17, 142, 117, 227, 121, 128, 204, 192, 147, 147, 92, 189, 5, 224, 148, 72, 18, 83, 101, 126, 124, 228, 153, 242, 123, 229, 247, 92, 221, 6, 73, 227, 250, 87, 167, 194, 129, 187, 73, 38, 185, 109, 217, 240, 193, 88, 50, 178, 180, 151, 54, 197, 187, 137, 190, 166, 233, 1, 103, 204, 88, 31, 127, 185, 29, 65, 1, 29, 254, 223, 14, 83, 167, 215, 114, 248, 30, 173, 89, 173, 187, 69,
            5, 105, 117, 15, 106, 94, 173, 63, 227, 25, 230, 190, 136, 168, 177, 175, 107, 91, 126, 254, 34, 188, 25, 118, 48, 12, 226, 130, 153, 162, 57, 47, 181, 212, 79, 160, 97, 64, 157, 246, 90, 53, 43, 149, 76, 102, 15, 195, 107, 58, 242, 84, 172, 29, 81, 198, 113, 81, 251, 138, 182, 154, 111, 30, 171, 129, 56, 17, 45, 214, 153, 112, 117, 203, 174, 40, 38, 234, 236, 32, 4, 112, 225, 26, 187, 195, 246, 252, 9, 218, 69, 160, 223, 178, 54, 148, 81, 8, 134, 151, 75, 248, 63, 224, 240, 48, 75, 250, 221, 85, 46, 100, 50, 3, 70, 64, 102, 111, 160, 155, 233, 59, 147, 184, 57, 61, 6, 126, 79, 176, 16, 185, 94, 166, 33, 135, 78, 42, 75, 140, 208, 140,
            44, 153, 187, 64, 103, 119, 160, 236, 16, 239, 74, 218, 219, 212, 207, 110, 53, 30, 76, 248, 40, 111, 98, 44, 20, 113, 204, 233, 109, 135, 96, 107, 39, 163, 203, 125, 45, 157, 152, 71, 239, 175, 174, 159, 147, 80, 111, 93, 38, 253, 228, 154, 225, 181, 101, 12, 241, 127, 65, 49, 189, 5, 85, 151, 237, 213, 143, 14, 104, 138, 54, 52, 27, 4, 132, 67, 35, 156, 86, 157, 73, 16, 229, 222, 245, 110, 79, 165, 179, 56, 179, 53, 218, 229, 100, 58, 87, 149, 48, 231, 64, 63, 115, 67, 3, 172, 6, 186, 115, 154, 60, 53, 214, 152, 149, 89, 234, 37, 143, 82, 255, 64, 28, 183, 93, 112, 39, 70, 185, 57, 0, 199, 9, 61, 175, 219, 41, 76, 37, 176, 82, 125, 65,
            53, 160, 214, 105, 62, 153, 244, 222, 96, 205, 6, 178, 85, 41, 240, 113, 0, 96, 149, 38, 3, 195, 18, 152, 41, 246, 3, 103, 29, 110, 134, 30, 101, 75, 46, 103, 199, 184, 20, 230, 8, 55, 120, 4, 229, 168, 35, 43, 7, 28, 161, 143, 87, 27, 87, 79, 255, 186, 44, 195, 158, 155, 181, 119, 81, 172, 217, 107, 95, 98, 55, 243, 186, 66, 105, 48, 224, 123, 232, 84, 156, 20, 10, 156, 208, 204, 52, 34, 228, 136, 97, 242, 200, 246, 211, 67, 202, 40, 241, 91, 92, 253, 9, 54, 72, 131, 221, 106, 178, 32, 44, 182, 4, 225, 193, 37, 20, 249, 249, 231, 10, 206, 18, 71, 254, 221, 187, 172, 88, 204, 6, 127, 138, 102, 7, 208, 75, 147, 219, 199, 177, 79, 36, 170,
            101, 207, 177, 109, 95, 143, 217, 41, 199, 80, 183, 201, 2, 254, 12, 55, 23, 198, 14, 255, 69, 245, 138, 155, 129, 227, 167, 168, 130, 156, 135, 14, 96, 93, 48, 99, 143, 107, 126, 92, 117, 143, 112, 108, 193, 228, 84, 13, 41, 186, 27, 172, 92, 201, 149, 116, 19, 112, 197, 116, 209, 128, 102, 1, 55, 152, 177, 28, 37, 34, 50, 83, 41, 199, 74, 178, 59, 111, 67, 118, 35, 252, 36, 33, 87, 28, 170, 17, 215, 47, 90, 154, 124, 137, 15, 14, 211, 59, 75, 59, 30, 77, 0, 49, 37, 225, 191, 87, 101, 127, 214, 227, 160, 99, 174, 234, 82, 148, 235, 16, 241, 219, 147, 170, 127, 221, 250, 116, 39, 218, 156, 72, 227, 172, 55, 0, 79, 188, 76, 51, 222, 232,
            24, 36, 62, 94, 154, 3, 61, 230, 146, 114, 253, 0, 128, 58, 253, 90, 72, 211, 242, 38, 39, 133, 153, 161, 119, 105, 195, 152, 225, 208, 105, 140, 80, 217, 186, 196, 157, 21, 116, 230, 116, 139, 25, 159, 143, 118, 128, 77, 201, 238, 247, 228, 15, 168, 4, 133, 148, 21, 148, 12, 44, 241, 7, 115, 17, 129, 176, 202, 46, 130, 122, 129, 235, 141, 223, 85, 21, 199, 65, 181, 169, 52, 174, 161, 153, 62, 25, 164, 115, 213, 89, 138, 199, 103, 79, 200, 165, 135, 249, 244, 27, 209, 178, 240, 129, 211, 61, 9, 111, 157, 147, 119, 36, 119, 255, 110, 130, 84, 49, 210, 225, 247, 100, 26, 121, 127, 163, 160, 26, 79, 99, 24, 77, 65, 32, 178, 109, 36, 27,
            253, 173, 110, 183, 11, 14, 211, 57, 130, 254, 124, 104, 165, 219, 31, 70, 97, 14, 194, 39, 61, 26, 141, 125, 228, 126, 194, 184, 101, 160, 204, 106, 128, 144, 106, 103, 171, 18, 246, 129, 220, 85, 172, 151, 123, 5, 73, 155, 192, 175, 91, 157, 239, 61, 237, 116, 170, 65, 233, 56, 19, 49, 114, 168, 190, 3, 214, 53, 250, 90, 213, 244, 88, 101, 30, 229, 248, 124, 15, 71, 141, 27, 172, 235, 21, 129, 211, 72, 61, 172, 112, 170, 128, 135, 96, 196, 221, 255, 27, 176, 105, 188, 183, 121, 33, 37, 149, 53, 131, 226, 233, 29, 167, 234, 218, 109, 53, 185, 152, 36, 248, 53, 61, 235, 78, 21, 201, 214, 210, 163, 12, 251, 187, 45, 188, 137, 126,
            127, 237, 92, 234, 91, 240, 225, 38, 194, 57, 213, 251, 237, 171, 30, 99, 52, 14, 49, 84, 101, 252, 237, 7, 166, 122, 114, 32, 107, 32, 207, 239, 136, 168, 178, 12, 11, 241, 233, 230, 146, 132, 18, 83, 233, 41, 172, 17, 6, 161, 42, 113, 87, 40, 255, 185, 1, 146, 128, 5, 240, 126, 131, 71, 42, 54, 124, 205, 2, 122, 71, 30, 222, 229, 40, 134, 142, 102, 97, 239, 151, 177, 1, 230, 231, 49, 123, 219, 28, 129, 91, 152, 112, 13, 154, 81, 197, 226, 255, 112, 158, 178, 177, 55, 181, 108, 138, 185, 245, 29, 186, 21, 73, 188, 209, 154, 200, 89, 116, 235, 198, 144, 36, 87, 248, 22, 7, 200, 122, 7, 148, 44, 42, 87, 140, 238, 204, 95, 231, 252, 0,
            136, 0, 22, 39, 70, 123, 125, 165, 113, 227, 172, 146, 163, 128, 158, 36, 52, 91, 19, 36, 245, 27, 150, 138, 141, 11, 67, 239, 224, 65, 24, 116, 101, 7, 39, 46, 142, 172, 164, 243, 148, 0, 33, 226, 59, 47, 203, 137, 156, 241, 66, 250, 157, 30, 204, 101, 143, 134, 98, 238, 155, 226, 25, 184, 136, 219, 89, 100, 193, 11, 143, 71, 139, 243, 230, 151, 0, 249, 1, 78, 26, 32, 93, 104, 157, 67, 97, 164, 248, 86, 124, 146, 93, 74, 222, 228, 167, 55, 53, 100, 135, 216, 109, 13, 64, 37, 106, 177, 200, 200, 182, 92, 251, 69, 31, 243, 89, 80, 198, 14, 132, 203, 72, 103, 28, 104, 217, 24, 97, 223, 113, 11, 29, 178, 191, 210, 46, 162, 255, 68, 99, 8,
            237, 213, 162, 152, 193, 183, 121, 203, 19, 108, 182, 29, 86, 26, 192, 103, 220, 103, 205, 154, 179, 197, 9, 22, 73, 127, 175, 146, 38, 119, 210, 0, 24, 180, 21, 245, 215, 204, 91, 186, 119, 138, 183, 239, 15, 155, 231, 248, 133, 39, 24, 101, 144, 236, 10, 230, 54, 174, 227, 73, 21, 110, 10, 160, 241, 232, 131, 14, 212, 127, 232, 59, 122, 65, 146, 54, 163, 9, 189, 190, 121, 88, 170, 62, 194, 14, 204, 152, 245, 38, 131, 37, 91, 81, 72, 114, 29, 115, 239, 182, 56, 44, 156, 159, 177, 180, 82, 160, 93, 97, 86, 183, 236, 50, 95, 85, 39, 71, 181, 225, 152, 143, 63, 123, 117, 34, 44, 109, 160, 166, 229, 240, 91, 138, 102, 54, 180, 173, 44,
            50, 80, 42, 124, 7, 50, 124, 211, 239, 21, 94, 197, 185, 239, 213, 107, 142, 64, 95, 124, 125, 17, 180, 97, 189, 101, 52, 48, 19, 112, 12, 70, 9, 212, 177, 54, 118, 66, 84, 147, 236, 248, 26, 124, 95, 103, 135, 254, 124, 49, 112, 186, 99, 120, 90, 8, 194, 191, 88, 57, 242, 65, 61, 10, 104, 246, 197, 252, 19, 159, 58, 194, 75, 173, 242, 103, 8, 115, 84, 69, 238, 149, 26, 15, 159, 182, 141, 132, 119, 70, 29, 53, 20, 143, 46, 163, 204, 6, 236, 59, 45, 185, 172, 89, 119, 83, 38, 144, 36, 222, 96, 151, 26, 99, 195, 163, 170, 133, 92, 159, 214, 53, 150, 116, 90, 176, 69, 145, 130, 15, 172, 140, 217, 215, 101, 163, 115, 161, 65, 101, 8, 7, 183,
            113, 213, 134, 58, 175, 130, 251, 143, 173, 248, 168, 135, 60, 159, 30, 194, 68, 208, 119, 120, 2, 40, 178, 227, 247, 161, 77, 47, 136, 46, 244, 163, 72, 65, 158, 25, 225, 195, 61, 132, 182, 204, 177, 186, 200, 81, 2, 65, 105, 212, 72, 94, 203, 232, 217, 182, 123, 251, 228, 160, 1, 161, 204, 123, 20, 37, 1, 77, 208, 179, 45, 149, 181, 122, 102, 190, 123, 213, 164, 231, 41, 216, 130, 234, 248, 208, 251, 252, 220, 84, 209, 67, 47, 61, 220, 5, 142, 162, 26, 236, 121, 142, 248, 132, 255, 65, 122, 203, 196, 102, 191, 187, 2, 195, 127, 255, 193, 92, 49, 91, 186, 154, 39, 156, 29, 211, 172, 49, 104, 245, 114, 153, 223, 211, 199, 249, 35,
            130, 160, 128, 0, 152, 176, 183, 20, 236, 113, 193, 108, 26, 255, 11, 237, 102, 133, 245, 94, 115, 114, 10, 89, 229, 214, 221, 99, 149, 30, 99, 37, 246, 10, 26, 26, 39, 92, 123, 170, 73, 211, 127, 227, 54, 30, 86, 133, 159, 112, 225, 91, 148, 100, 174, 149, 75, 143, 14, 140, 20, 44, 64, 212, 5, 243, 8, 116, 63, 30, 97, 42, 123, 20, 73, 212, 85, 207, 83, 122, 27, 251, 233, 84, 10, 17, 236, 232, 83, 200, 127, 119, 143, 163, 204, 220, 167, 59, 231, 20, 106, 186, 222, 191, 8, 40, 234, 21, 25, 180, 13, 116, 250, 152, 224, 174, 75, 3, 205, 38, 173, 215, 236, 151, 185, 121, 254, 244, 154, 239, 17, 53, 106, 164, 61, 49, 116, 216, 118, 94, 150,
            35, 181, 26, 238, 66, 49, 211, 221, 132, 146, 166, 115, 39, 136, 36, 205, 230, 179, 31, 197, 51, 148, 165, 109, 38, 70, 37, 148, 52, 44, 209, 250, 98, 58, 246, 225, 103, 198, 101, 26, 25, 196, 207, 8, 166, 21, 88, 252, 175, 253, 10, 88, 107, 157, 19, 225, 61, 12, 246, 221, 37, 239, 186, 167, 137, 142, 135, 222, 128, 174, 62, 95, 216, 38, 141, 157, 45, 232, 97, 217, 173, 203, 234, 116, 129, 69, 206, 189, 94, 221, 12, 54, 139, 186, 247, 184, 16, 200, 121, 244, 104, 8, 7, 35, 111, 47, 188, 10, 140, 92, 73, 143, 206, 203, 72, 122, 184, 20, 102, 197, 130, 64, 150, 63, 96, 239, 8, 132, 111, 217, 84, 91, 198, 32, 43, 100, 138, 241, 15, 160,
            42, 190, 253, 193, 184, 164, 124, 29, 210, 96, 67, 224, 221, 182, 29, 218, 129, 149, 29, 128, 174, 98, 88, 88, 125, 56, 40, 255, 120, 5, 0, 87, 174, 42, 150, 90, 112, 201, 183, 169, 19, 57, 195, 191, 12, 58, 244, 235, 132, 25, 145, 72, 146, 214, 8, 125, 100, 135, 12, 5, 102, 97, 248, 174, 24, 159, 90, 33, 43, 187, 6, 61, 212, 241, 225, 190, 219, 252, 197, 123, 129, 164, 108, 123, 55, 230, 4, 153, 166, 105, 234, 15, 85, 216, 23, 56, 32, 3, 41, 110, 68, 146, 172, 133, 202, 98, 41, 7, 47, 152, 35, 255, 168, 106, 241, 226, 222, 77, 244, 52, 185, 65, 252, 227, 32, 66, 38, 11, 172, 60, 28, 28, 103, 84, 1, 1, 205, 182, 190, 28, 189, 102, 253,
            43, 1, 191, 148, 116, 10, 227, 18, 81, 93, 80, 239, 157, 232, 215, 180, 163, 165, 161, 109, 177, 71, 150, 244, 144, 208, 160, 110, 22, 174, 60, 206, 43, 103, 121, 55, 103, 114, 115, 173, 238, 13, 10, 227, 251, 41, 176, 216, 158, 229, 216, 55, 234, 128, 128, 20, 167, 106, 181, 86, 163, 130, 215, 110, 149, 191, 10, 227, 215, 8, 214, 154, 178, 181, 15, 19, 0, 247, 250, 97, 74, 43, 157, 55, 94, 174, 41, 41, 9, 199, 97, 20, 91, 32, 18, 10, 43, 98, 240, 247, 203, 20, 250, 117, 160, 44, 229, 202, 187, 64, 54, 124, 15, 184, 169, 129, 27, 160, 240, 26, 61, 255, 60, 166, 60, 144, 209, 84, 55, 187, 186, 168, 13, 124, 125, 29, 17, 100, 249, 227,
            62, 205, 78, 179, 163, 168, 139, 168, 21, 38, 83, 239, 151, 74, 43, 66, 2, 92, 72, 71, 94, 216, 134, 238, 20, 45, 158, 213, 164, 73, 57, 80, 47, 198, 184, 130, 223, 227, 71, 132, 133, 235, 177, 85, 174, 142, 124, 172, 200, 54, 229, 40, 126, 60, 76, 92, 216, 153, 56, 241, 174, 66, 141, 90, 226, 3, 30, 68, 234, 71, 187, 163, 112, 146, 255, 22, 143, 170, 204, 3, 127, 179, 81, 139, 160, 37, 77, 246, 128, 220, 196, 158, 153, 73, 177, 65, 199, 119, 29, 197, 144, 130, 248, 206, 155, 253, 108, 213, 124, 7, 223, 221, 162, 146, 134, 242, 65, 99, 162, 107, 120, 247, 214, 207, 96, 150, 169, 131, 208, 218, 221, 28, 24, 112, 208, 23, 1, 130, 142,
            232, 56, 104, 45, 33, 158, 95, 255, 123, 31, 74, 76, 120, 178, 155, 213, 6, 195, 164, 8, 8, 69, 241, 197, 127, 83, 169, 21, 167, 19, 94, 143, 252, 33, 159, 248, 241, 170, 153, 147, 1, 149, 199, 201, 131, 170, 79, 236, 212, 209, 143, 107, 98, 24, 123, 56, 33, 193, 85, 247, 64, 225, 135, 210, 78, 145, 57, 16, 145, 71, 170, 20, 133, 87, 235, 4, 166, 239, 100, 82, 235, 81, 50, 223, 9, 193, 52, 49, 86, 129, 190, 196, 82, 165, 107, 63, 115, 161, 98, 33, 20, 193, 29, 42, 151, 205, 252, 124, 72, 245, 48, 181, 67, 7, 13, 21, 127, 59, 226, 188, 144, 129, 112, 244, 192, 121, 213, 80, 42, 196, 1, 13, 107, 108, 78, 0, 40, 121, 225, 148, 237, 234, 209,
            216, 238, 9, 147, 226, 254, 96, 89, 212, 72, 193, 106, 75, 135, 74, 227, 67, 255, 92, 191, 81, 188, 124, 226, 149, 152, 142, 15, 159, 195, 238, 114, 55, 255, 166, 157, 230, 59, 148, 170, 166, 151, 65, 213, 104, 253, 253, 112, 150, 82, 147, 137, 27, 214, 100, 247, 65, 81, 92, 47, 86, 217, 7, 45, 120, 81, 130, 31, 236, 243, 76, 78, 3, 45, 105, 172, 220, 71, 48, 220, 94, 196, 249, 163, 193, 133, 50, 236, 205, 20, 55, 2, 63, 14, 127, 69, 113, 212, 204, 12, 58, 79, 89, 86, 29, 61, 199, 201, 64, 149, 6, 144, 182, 150, 129, 31, 18, 167, 120, 248, 82, 107, 25, 143, 128, 27, 161, 28, 25, 153, 183, 217, 238, 78, 186, 106, 92, 27, 202, 219, 165,
            96, 0, 216, 234, 169, 73, 101, 39, 182, 113, 217, 240, 170, 116, 172, 221, 250, 233, 48, 49, 242, 83, 227, 92, 181, 184, 72, 230, 180, 21, 15, 108, 135, 25, 38, 153, 25, 124, 227, 26, 149, 73, 236, 39, 211, 244, 149, 58, 183, 132, 26, 223, 219, 174, 144, 117, 233, 219, 165, 205, 157, 159, 222, 184, 52, 47, 241, 201, 123, 65, 24, 44, 55, 215, 177, 168, 250, 179, 115, 190, 227, 123, 158, 163, 179, 224, 69, 196, 66, 207, 254, 243, 101, 221, 193, 140, 250, 4, 28, 222, 52, 96, 138, 160, 33, 218, 64, 118, 214, 234, 201, 152, 148, 91, 178, 111, 107, 144, 142, 6, 182, 102, 72, 188, 34, 213, 181, 26, 223, 58, 255, 103, 81, 17, 47, 169, 11,
            245, 224, 123, 148, 215, 237, 186, 107, 75, 152, 90, 202, 166, 22, 149, 197, 5, 246, 238, 78, 76, 229, 106, 199, 94, 127, 195, 0, 45, 82, 6, 159, 103, 96, 138, 231, 71, 46, 107, 59, 216, 39, 43, 12, 221, 27, 214, 56, 155, 145, 66, 187, 169, 250, 235, 78, 211, 179, 239, 183, 198, 163, 93, 5, 196, 24, 174, 143, 225, 106, 139, 89, 98, 13, 127, 207, 184, 194, 30, 1, 165, 198, 169, 8, 197, 118, 86, 163, 221, 138, 23, 209, 61, 116, 79, 99, 233, 43, 130, 60, 244, 85, 229, 243, 172, 123, 148, 200, 120, 192, 127, 211, 52, 11, 159, 41, 95, 212, 230, 188, 169, 156, 137, 29, 212, 12, 148, 168, 148, 133, 243, 44, 241, 139, 127, 24, 246, 220, 227,
            125, 209, 97, 60, 52, 162, 192, 146, 49, 161, 92, 138, 112, 189, 128, 59, 126, 125, 46, 207, 60, 79, 231, 174, 152, 209, 68, 223, 205, 2, 38, 14, 91, 116, 159, 255, 28, 27, 178, 248, 164, 104, 158, 79, 69, 214, 234, 157, 12, 75, 163, 83, 253, 245, 202, 61, 213, 176, 6, 197, 230, 29, 208, 166, 253, 194, 254, 235, 29, 141, 241, 70, 249, 15, 62, 0, 148, 163, 135, 52, 122, 40, 96, 87, 31, 179, 152, 51, 216, 133, 184, 122, 198, 203, 60, 115, 218, 191, 193, 16, 178, 25, 148, 252, 112, 104, 103, 252, 36, 92, 221, 28, 179, 43, 199, 198, 151, 128, 100, 252, 217, 161, 249, 34, 201, 172, 118, 52, 180, 252, 104, 7, 223, 44, 116, 102, 212, 21,
            40, 224, 184, 55, 163, 210, 21, 207, 161, 239, 51, 54, 155, 41, 133, 18, 67, 48, 3, 165, 130, 251, 4, 79, 214, 57, 72, 130, 157, 212, 144],
        vo = [0, 1, 3, 4, 6, 7, 9, 10, 12, 13, 15, 16, 18, 19, 21, 22, 24, 26, 29, 31, 34, 36, 39, 41, 44, 46, 49, 51, 54, 56, 59, 61, 64, 65, 66, 67, 68, 69, 70, 72, 73, 74, 75, 76, 77, 79, 80, 81, 82, 83, 84, 85, 87, 88, 89, 90, 91, 92, 94, 95, 96, 97, 98, 99, 101, 102, 103, 104, 105, 106, 107, 109, 110, 111, 112, 113, 114, 116, 117, 118, 119, 120, 121, 123, 124, 125, 126, 127, 128, 129, 131, 132, 133, 134, 135, 136, 138, 139, 140, 141, 142, 143, 145, 146, 147, 148, 149, 150, 151, 153, 154, 155, 156, 157, 158,
            160, 161, 162, 163, 164, 165, 166, 168, 169, 170, 171, 172, 173, 175, 176, 177, 178, 179, 180, 182, 183, 184, 185, 186, 187, 188, 190, 191, 192, 193, 194, 195, 197, 198, 199, 200, 201, 202, 204, 205, 206, 207, 208, 209, 210, 212, 213, 214, 215, 216, 217, 219, 220, 221, 222, 223, 224, 226, 226, 226, 227, 227, 227, 228, 228, 228, 229, 229, 229, 230, 230, 231, 231, 231, 232, 232, 232, 233, 233, 233, 234, 234, 235, 235, 235, 236, 236, 236, 237, 237, 237, 238, 238, 239, 239, 239, 240, 240, 240, 241, 241, 241, 242, 242, 243, 243, 243, 244, 244, 244, 245, 245, 245, 246, 246, 246, 247, 247, 247, 248, 248, 248, 249, 249, 249, 250,
            250, 250, 251, 251, 251, 252, 252, 252, 253, 253, 253, 254, 254, 254, 255],
        Jk = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 32, 33, 35, 36, 37, 39, 40, 42, 43, 44, 46, 47, 49, 50, 51, 53, 54, 56, 57, 58, 59, 61, 62, 63, 64, 66, 67, 68, 69, 71, 72, 73, 74, 76, 77, 78, 79, 81, 82, 83, 84, 86, 87, 88, 90, 91, 92, 93, 95, 96, 97, 98, 100, 101, 102, 103, 105, 106, 107, 108, 110, 111, 112, 113, 115, 116, 117, 118, 120, 121, 122, 124, 125, 126, 127, 129, 130, 131, 132, 134, 135, 136, 137, 139, 140, 141, 142, 144, 145, 146, 147, 149, 150, 151, 152, 154, 155, 156, 158, 159, 160, 161,
            163, 164, 165, 166, 168, 169, 170, 171, 173, 174, 175, 176, 178, 179, 180, 181, 183, 184, 185, 186, 188, 189, 190, 192, 193, 194, 195, 197, 198, 199, 200, 202, 203, 204, 205, 207, 208, 209, 210, 212, 213, 214, 215, 217, 218, 219, 220, 222, 223, 224, 226, 226, 226, 227, 227, 228, 228, 229, 229, 230, 230, 231, 231, 232, 232, 233, 233, 234, 234, 234, 235, 235, 236, 236, 237, 237, 238, 238, 239, 239, 240, 240, 241, 241, 242, 242, 243, 243, 243, 244, 244, 244, 245, 245, 245, 246, 246, 246, 247, 247, 247, 248, 248, 248, 249, 249, 249, 250, 250, 250, 251, 251, 251, 252, 252, 252, 253, 253, 253, 254, 254, 254, 255],
        wo = [1, 2,
            3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 35, 36, 37, 38, 39, 40, 41, 43, 44, 45, 46, 47, 48, 50, 51, 52, 53, 54, 55, 56, 58, 59, 60, 61, 62, 63, 65, 66, 67, 68, 69, 70, 72, 73, 74, 76, 77, 78, 80, 81, 83, 84, 85, 87, 88, 89, 91, 92, 94, 95, 96, 98, 99, 100, 102, 103, 105, 106, 107, 109, 110, 111, 113, 114, 116, 117, 118, 120, 121, 122, 124, 125, 127, 128, 129, 131, 132, 133, 135, 136, 138, 139, 140, 142, 143, 144, 146, 147, 149, 150, 151, 153, 154, 155, 157, 158, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181,
            182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 192, 193, 193, 194, 195, 195, 196, 197, 197, 198, 199, 199, 200, 201, 201, 202, 203, 203, 204, 205, 205, 206, 207, 207, 208, 209, 209, 210, 211, 211, 212, 213, 213, 214, 215, 215, 216, 217, 217, 218, 219, 219, 220, 221, 221, 222, 223, 223, 224, 225, 225, 226, 227, 227, 228, 229, 229, 230, 231, 231, 232, 233, 233, 234, 235, 235, 236, 237, 237, 238, 239, 239, 240, 241, 241, 242, 243, 243, 244, 245, 245, 246, 247, 247, 248, 249, 249, 250, 251, 251, 252, 253, 253, 254, 255];

    class xo extends Pb {
        constructor(a, b, c, e) {
            super(a, uo, c, e);
            this.lightLevel = .1;
            this.rednessLevel = .5;
            this.mskin_he_max = 175 / 180 * 3.141593;
            this.mskin_he_min = 115 / 180 * 3.141593;
            this.mskin_hc_max = 173 / 180 * 3.141593;
            this.mskin_hc_min = 116 / 180 * 3.141593;
            this.mskin_hc_axis = 2.04203545;
            this.mfacts_rotate_ge = this.mfacts_rotate_le = this.mfacts_rotate_c = 0;
            this.tab_addr = null;
            this.lutTextures = [];
            this.inputTexture = b;
            this.init()
        }

        setUniforms() {
            var a = this.gl.getUniformLocation(this.program, "u_flipY"),
                b = this.gl.getUniformLocation(this.program, "u_denoiseLevel");
            this.gl.uniform1f(b, this.denoiseLevel);
            this.gl.uniform1f(a,
                1);
            a = this.gl.getUniformLocation(this.program, "light");
            this.gl.uniform1f(a, this.lightLevel);
            a = this.gl.getUniformLocation(this.program, "redness");
            this.gl.uniform1f(a, this.rednessLevel);
            a = this.gl.getUniformLocation(this.program, "skin_he_max");
            b = this.gl.getUniformLocation(this.program, "skin_he_min");
            var c = this.gl.getUniformLocation(this.program, "skin_hc_max"),
                e = this.gl.getUniformLocation(this.program, "skin_hc_min");
            let g = this.gl.getUniformLocation(this.program, "skin_hc_axis"),
                h = this.gl.getUniformLocation(this.program,
                    "facts_rotate_c"), k = this.gl.getUniformLocation(this.program, "facts_rotate_le"),
                l = this.gl.getUniformLocation(this.program, "facts_rotate_ge");
            this.gl.uniform1f(a, this.mskin_he_max);
            this.gl.uniform1f(b, this.mskin_he_min);
            this.gl.uniform1f(c, this.mskin_hc_max);
            this.gl.uniform1f(e, this.mskin_hc_min);
            this.gl.uniform1f(g, this.mskin_hc_axis);
            this.gl.uniform1f(h, this.mfacts_rotate_c);
            this.gl.uniform1f(k, this.mfacts_rotate_le);
            this.gl.uniform1f(l, this.mfacts_rotate_ge);
            a = this.gl.getUniformLocation(this.program,
                "u_originImage");
            this.gl.activeTexture(this.gl.TEXTURE2);
            this.gl.bindTexture(this.gl.TEXTURE_2D, this.inputTexture);
            this.gl.uniform1i(a, 2);
            a = ["lighten_lut"];
            b = [this.gl.TEXTURE3];
            for (c = 0; c < a.length; c++) e = this.gl.getUniformLocation(this.program, a[c]), this.gl.activeTexture(b[c]), this.gl.bindTexture(this.gl.TEXTURE_2D, this.lutTextures[c]), this.gl.uniform1i(e, c + 3)
        }

        setParameters(a) {
            void 0 !== a.denoiseLevel && (this.denoiseLevel = a.denoiseLevel);
            void 0 !== a.lightLevel && (this.lightLevel = a.lightLevel);
            void 0 !==
            a.rednessLevel && (this.rednessLevel = a.rednessLevel, this.updateRedness(this.rednessLevel));
            a.lighteningContrastLevel && this.updateLut(a.lighteningContrastLevel)
        }

        init() {
            this.tab_addr = new Uint8Array(Jk);
            let a = [this.tab_addr], b = [256], c = [1];
            for (let e = 0; e < a.length; e++) {
                let g = this.gl.createTexture();
                if (!g) throw new p(n.WEBGL_INTERNAL_ERROR, "create lut texture failed");
                this.gl.bindTexture(this.gl.TEXTURE_2D, g);
                this.gl.texImage2D(this.gl.TEXTURE_2D, 0, this.gl.LUMINANCE, b[e], c[e], 0, this.gl.LUMINANCE, this.gl.UNSIGNED_BYTE,
                    a[e]);
                this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_WRAP_S, this.gl.CLAMP_TO_EDGE);
                this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_WRAP_T, this.gl.CLAMP_TO_EDGE);
                this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MIN_FILTER, this.gl.LINEAR);
                this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MAG_FILTER, this.gl.LINEAR);
                this.lutTextures.push(g)
            }
        }

        updateRedness(a) {
            var b = a;
            1 < a && (a = 1);
            0 > a && (a = 0);
            1 < b && (b = 1);
            0 > b && (b = 0);
            this.mfacts_rotate_c = .8 * a;
            .8 > b && (b = 0);
            this.mskin_he_max = 175 /
                180 * 3.141593;
            this.mskin_hc_max = 173 / 180 * 3.141593;
            this.mskin_he_min = (115 - 4 * b) / 180 * 3.141593;
            this.mskin_hc_min = (116 - 4 * b) / 180 * 3.141593;
            this.mskin_hc_axis = (117 - 4 * b) / 180 * 3.141593;
            this.mskin_hc_axis < this.mskin_hc_min && (this.mskin_hc_axis = this.mskin_hc_min);
            1.5707965 > this.mskin_hc_min && (this.mskin_hc_min = 1.5707965);
            1.5707965 > this.mskin_hc_axis && (this.mskin_hc_axis = 1.5707965);
            1.5707965 > this.mskin_he_min && (this.mskin_he_min = 1.5707965);
            3.141593 < this.mskin_hc_max && (this.mskin_hc_max = 3.141593);
            3.141593 < this.mskin_hc_axis &&
            (this.mskin_hc_axis = 3.141593);
            3.141593 < this.mskin_he_max && (this.mskin_he_max = 3.141593);
            a = this.mskin_he_max - this.mskin_hc_max;
            b = this.mskin_hc_max - this.mskin_hc_axis;
            this.mfacts_rotate_ge = .01 < a ? this.mfacts_rotate_c * b / a : this.mfacts_rotate_c;
            a = this.mskin_hc_min - this.mskin_he_min;
            b = this.mskin_hc_axis - this.mskin_hc_min;
            this.mfacts_rotate_le = .01 < a ? this.mfacts_rotate_c * b / a : this.mfacts_rotate_c
        }

        updateLut(a) {
            var b = null;
            if (0 === a && (b = Jk), 1 === a && (b = wo), 2 === a && (b = vo), !b) throw new p(n.WEBGL_INTERNAL_ERROR, "invalid ylut_table value:" +
                a);
            this.tab_addr = new Uint8Array(b);
            a = [this.tab_addr];
            b = [256];
            let c = [1];
            for (let e = 0; e < a.length; e++) this.gl.bindTexture(this.gl.TEXTURE_2D, this.lutTextures[e]), this.gl.texImage2D(this.gl.TEXTURE_2D, 0, this.gl.LUMINANCE, b[e], c[e], 0, this.gl.LUMINANCE, this.gl.UNSIGNED_BYTE, a[e])
        }
    }

    class yo {
        constructor() {
            this.canvas = this.gl = null;
            this.programs = [];
            this.inputTexture = this.commonProgram = null;
            this.outputTextures = [];
            this.fbos = [];
            this.originalFrameHeight = this.originalFrameWidth = 0;
            this.enableBeauty = !1;
            this.denoiseLevel =
                5;
            this.lightLevel = .35;
            this.rednessLevel = .5;
            this.lighteningContrastLevel = 1
        }

        setEnableBeauty(a) {
            this.enableBeauty = !!a
        }

        init(a, b, c) {
            if (!ca.supportWebGL) throw new p(n.NOT_SUPPORTED, "your browser is not support webGL");
            if (this.gl = c.getContext("webgl"), !this.gl) throw new p(n.WEBGL_INTERNAL_ERROR, "can not get webgl context");
            if (this.initGL(a, b), !this.inputTexture) throw new p(n.WEBGL_INTERNAL_ERROR, "can not find input texture");
            this.canvas = c;
            this.programs.push(new Ik(this.gl));
            this.programs.push(new po(this.gl,
                a, b));
            this.programs.push(new ro(this.gl, a, b));
            this.programs.push(new to(this.gl, a, b));
            this.programs.push(new xo(this.gl, this.inputTexture, a, b));
            this.commonProgram = this.programs[0].program;
            this.setDenoiseLevel(this.denoiseLevel);
            this.setLightLevel(this.lightLevel);
            this.setRednessLevel(this.rednessLevel);
            this.setContrastLevel(this.lighteningContrastLevel)
        }

        render(a) {
            if (!this.gl || !this.commonProgram || !this.canvas) return void k.warning("video effect manager is not init!");
            var b = 0;
            if (this.originalFrameHeight ===
                a.videoWidth && this.originalFrameWidth === a.videoHeight) b = 2; else if (this.originalFrameHeight !== a.videoHeight || this.originalFrameWidth !== a.videoWidth) {
                var c, e, g;
                if (k.debug(l(c = l(e = l(g = "beauty effect: resolution changed ".concat(this.originalFrameWidth, "x")).call(g, this.originalFrameHeight, " -> ")).call(e, a.videoWidth, "x")).call(c, a.videoHeight)), 0 === a.videoHeight || 0 === a.videoWidth) return void k.debug("beauty effect: skip 0 resolution frame");
                this.canvas.width = a.videoWidth;
                this.canvas.height = a.videoHeight;
                a.setAttribute("width", a.videoWidth.toString());
                a.setAttribute("height", a.videoHeight.toString());
                this.release();
                this.init(a.videoWidth, a.videoHeight, this.canvas)
            }
            this.gl.viewport(0, 0, a.videoWidth, a.videoHeight);
            this.gl.bindTexture(this.gl.TEXTURE_2D, this.inputTexture);
            this.gl.texImage2D(this.gl.TEXTURE_2D, 0, this.gl.RGBA, this.gl.RGBA, this.gl.UNSIGNED_BYTE, a);
            a = this.enableBeauty ? this.programs.length - 1 : 0;
            for (c = 0; c <= a; c++) e = this.programs[c].program, this.gl.useProgram(e), e = this.gl.getUniformLocation(e,
                "u_image"), this.programs[c].setUniforms(), this.gl.bindFramebuffer(this.gl.FRAMEBUFFER, this.fbos[b + c % 2]), this.gl.clearColor(0, 0, 0, 1), this.gl.clear(this.gl.COLOR_BUFFER_BIT), this.gl.drawArrays(this.gl.TRIANGLES, 0, 6), this.gl.activeTexture(this.gl.TEXTURE0), this.gl.bindTexture(this.gl.TEXTURE_2D, this.outputTextures[b + c % 2]), this.gl.uniform1i(e, 0);
            this.gl.useProgram(this.commonProgram);
            b = this.gl.getUniformLocation(this.commonProgram, "u_flipY");
            this.gl.uniform1f(b, -1);
            this.gl.bindFramebuffer(this.gl.FRAMEBUFFER,
                null);
            this.gl.clearColor(0, 0, 0, 1);
            this.gl.clear(this.gl.COLOR_BUFFER_BIT);
            this.gl.drawArrays(this.gl.TRIANGLES, 0, 6)
        }

        setDenoiseLevel(a) {
            var b;
            q(b = this.programs).call(b, b => {
                b instanceof Pb && b.setParameters({denoiseLevel: a})
            });
            this.denoiseLevel = a
        }

        setLightLevel(a) {
            var b;
            q(b = this.programs).call(b, b => {
                b instanceof Pb && b.setParameters({lightLevel: a})
            });
            this.lightLevel = a
        }

        setRednessLevel(a) {
            var b;
            q(b = this.programs).call(b, b => {
                b instanceof Pb && b.setParameters({rednessLevel: a})
            });
            this.rednessLevel = a
        }

        setContrastLevel(a) {
            var b;
            q(b = this.programs).call(b, b => {
                b instanceof Pb && b.setParameters({lighteningContrastLevel: a})
            });
            this.lighteningContrastLevel = a
        }

        setSize(a, b) {
            var c;
            q(c = this.programs).call(c, c => {
                c instanceof Pb && c.setSize(a, b)
            })
        }

        release() {
            this.inputTexture = this.commonProgram = this.gl = null;
            this.programs = [];
            this.outputTextures = [];
            this.fbos = []
        }

        initGL(a, b) {
            if (!this.gl) throw new p(n.WEBGL_INTERNAL_ERROR, "can not find webgl context");
            this.inputTexture = this.gl.createTexture();
            this.gl.bindTexture(this.gl.TEXTURE_2D, this.inputTexture);
            this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_WRAP_S, this.gl.CLAMP_TO_EDGE);
            this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_WRAP_T, this.gl.CLAMP_TO_EDGE);
            this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MIN_FILTER, this.gl.LINEAR);
            this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MAG_FILTER, this.gl.LINEAR);
            for (let c = 0; 4 > c; c++) {
                let e = this.gl.createTexture();
                if (!e) throw new p(n.WEBGL_INTERNAL_ERROR, "create texture failed");
                this.gl.bindTexture(this.gl.TEXTURE_2D, e);
                this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_WRAP_S, this.gl.CLAMP_TO_EDGE);
                this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_WRAP_T, this.gl.CLAMP_TO_EDGE);
                this.gl.texParameteri(this.gl.TEXTURE_2D, this.gl.TEXTURE_MIN_FILTER, this.gl.LINEAR);
                2 > c ? this.gl.texImage2D(this.gl.TEXTURE_2D, 0, this.gl.RGBA, a, b, 0, this.gl.RGBA, this.gl.UNSIGNED_BYTE, null) : this.gl.texImage2D(this.gl.TEXTURE_2D, 0, this.gl.RGBA, b, a, 0, this.gl.RGBA, this.gl.UNSIGNED_BYTE, null);
                let g = this.gl.createFramebuffer();
                if (!g) throw new p(n.WEBGL_INTERNAL_ERROR,
                    "create frame buffer failed");
                this.gl.bindFramebuffer(this.gl.FRAMEBUFFER, g);
                this.gl.framebufferTexture2D(this.gl.FRAMEBUFFER, this.gl.COLOR_ATTACHMENT0, this.gl.TEXTURE_2D, e, 0);
                this.outputTextures.push(e);
                this.fbos.push(g)
            }
            this.gl.viewport(0, 0, a, b);
            this.originalFrameWidth = a;
            this.originalFrameHeight = b
        }
    }

    class zo {
        constructor() {
            this.recordedFrameCount = this.targetFrameRate = 0;
            this.recordingTime = 2
        }

        async startRecordBeautyEffectOutput(a, b = 4) {
            if (this.recordID) throw new p(n.UNEXPECTED_ERROR, "another beauty effect recording is in progress");
            let c = pa(6, "");
            return this.recordID = c, this.targetFrameRate = a, this.recordedFrameCount = 0, this.recordingTime = b, await yb(1E3 * this.recordingTime), this.recordID !== c ? (this.recordID = void 0, !0) : (this.recordID = void 0, this.recordedFrameCount < this.targetFrameRate * this.recordingTime / 2 ? (k.warning("detect beauty effect overload, current framerate", this.recordedFrameCount / 2), !1) : (k.debug("beauty effect current framerate", this.recordedFrameCount / 2), !0))
        }

        stopRecordBeautyEffectOutput() {
            this.recordedFrameCount = this.targetFrameRate =
                0;
            this.recordID = void 0
        }

        addFrame() {
            this.recordID && (this.recordedFrameCount += 1)
        }
    }

    class Ao extends class {
        get output() {
            return this._output
        }

        async setInput(a) {
            if (a !== this.input) {
                if (a.kind !== this.kind) throw new p(n.UNEXPECTED_ERROR);
                this.input && this.removeInput();
                this.input = a;
                await this._setInput(a)
            }
        }

        removeInput() {
            this.input = void 0;
            this._removeInput()
        }

        async updateOutput(a) {
            this.output !== a && (this._output = a, this.onOutputChange && await this.onOutputChange())
        }

        replaceOriginMediaStream(a, b) {
            var c, e;
            let g = R(c = a.getTracks()).call(c,
                a => a.kind === this.kind);
            g && a.removeTrack(g);
            b = R(e = b.getTracks()).call(e, a => a.kind === this.kind);
            void 0 === this.output && b && a.addTrack(b);
            this.output && (k.debug("replace ".concat(this.output.kind, " track to origin media stream")), a.addTrack(this.output))
        }
    } {
        constructor() {
            super();
            this.kind = "video";
            this.fps = 15;
            this.overloadDetector = new zo;
            this.enabled = !1;
            this.stopChromeBackgroundLoop = null;
            this.lastRenderTime = 0;
            this.fps = 30;
            this.manager = new yo
        }

        async setBeautyEffectOptions(a, b) {
            void 0 !== b.smoothnessLevel && V(b.smoothnessLevel,
                "options.smoothnessLevel", 0, 1, !1);
            void 0 !== b.lighteningLevel && V(b.lighteningLevel, "options.lighteningLevel", 0, 1, !1);
            void 0 !== b.rednessLevel && V(b.rednessLevel, "options.rednessLevel", 0, 1, !1);
            void 0 !== b.lighteningContrastLevel && Ka(b.lighteningContrastLevel, "options.lighteningContrastLevel", [0, 1, 2]);
            void 0 !== b.smoothnessLevel && this.manager.setDenoiseLevel(Math.max(.1, 10 * b.smoothnessLevel));
            void 0 !== b.lighteningLevel && this.manager.setLightLevel(Math.max(.1, b.lighteningLevel / 2));
            void 0 !== b.rednessLevel &&
            this.manager.setRednessLevel(Math.max(.01, b.rednessLevel));
            void 0 !== b.lighteningContrastLevel && this.manager.setContrastLevel(b.lighteningContrastLevel);
            this.enabled !== a && (this.manager.setEnableBeauty(a), this.enabled = a, a ? this.input && await this.startEffect() : await this.stopEffect())
        }

        destroy() {
            this.onOutputChange = void 0;
            this.stopEffect();
            this.enabled = !1
        }

        async startEffect() {
            let a = ka();
            if (!this.input) return void k.warning("video track is null, fail to start video effect!");
            if (this.output) return void k.warning("video effect is already enabled");
            let b = await this.renderWithWebGL();
            await this.updateOutput(b);
            k.info("start video effect, output:", this.output);
            this.overloadDetector.startRecordBeautyEffectOutput(this.fps).then(a => {
                a || this.onOverload && this.onOverload()
            });
            let c = () => {
                requestAnimationFrame(c);
                const a = x(), b = 1E3 / this.fps, h = this.lastRenderTime ? a - this.lastRenderTime : b;
                h < b || (this.lastRenderTime = a - (h - b), this.video && this.video.paused && this.video.play(), this.enabled && this.video && (this.manager.render(this.video), this.output && this.output.requestFrame &&
                this.output.requestFrame(), this.overloadDetector.addFrame()))
            };
            requestAnimationFrame(c);
            a.name === Z.CHROME && document.addEventListener("visibilitychange", () => {
                document.hidden ? this.stopChromeBackgroundLoop = Ge(() => {
                    this.enabled && this.video && this.manager.render(this.video);
                    this.output && this.output.requestFrame && this.output.requestFrame();
                    this.overloadDetector.addFrame()
                }, this.fps) : this.stopChromeBackgroundLoop && (this.stopChromeBackgroundLoop(), this.stopChromeBackgroundLoop = null)
            }, !1)
        }

        async stopEffect() {
            k.info("stop video effect");
            this.overloadDetector.stopRecordBeautyEffectOutput();
            this.manager.release();
            this.canvas && this.canvas.remove();
            this.video && this.video.remove();
            this.video = this.canvas = void 0;
            await this.updateOutput(void 0)
        }

        async _setInput(a) {
            this.enabled && !this.video && await this.startEffect()
        }

        _removeInput() {
            this.stopEffect()
        }

        async renderWithWebGL() {
            var a;
            if (!this.input) throw new p(n.BEAUTY_PROCESSOR_INTERNAL_ERROR, "can not renderWithWebGL, no input");
            this.canvas && (this.canvas.remove(), this.canvas = void 0);
            this.video &&
            (this.video.remove(), this.video = void 0);
            this.canvas = document.createElement("canvas");
            this.video = document.createElement("video");
            this.video.setAttribute("autoplay", "");
            this.video.setAttribute("muted", "");
            this.video.muted = !0;
            this.video.setAttribute("playsinline", "");
            this.video.setAttribute("style", "display:none");
            this.video.srcObject = new MediaStream([this.input]);
            let b = new v(a => {
                const b = () => {
                    this.video && this.video.removeEventListener("playing", b);
                    a(void 0)
                };
                this.video && this.video.addEventListener("playing",
                    b)
            }), c = this.input.getSettings(), e = c.width, g = c.height;
            if (c.frameRate && this.fps !== c.frameRate && (this.fps = c.frameRate, k.debug("beauty video processor: set fps to", this.fps)), k.debug(l(a = "beauty video processor: width ".concat(e, " height ")).call(a, g)), !e || !g) throw new p(n.BEAUTY_PROCESSOR_INTERNAL_ERROR, "can not get track resolution");
            this.canvas.width = e;
            this.canvas.height = g;
            this.video.setAttribute("width", e.toString());
            this.video.setAttribute("height", g.toString());
            this.manager.init(e, g, this.canvas);
            this.video.play();
            await b;
            return this.canvas.captureStream(ca.supportRequestFrame ? 0 : this.fps).getVideoTracks()[0]
        }
    }

    class Na extends ke {
        constructor(a, b, c, e, g) {
            super(a, g);
            this.trackMediaType = "video";
            this._scalabiltyMode = {numSpatialLayers: 1, numTemporalLayers: 1};
            this._enabled = !0;
            Mc(a).then(([a, b]) => {
                this._videoHeight = b;
                this._videoWidth = a
            }).catch(Uf);
            this._encoderConfig = b;
            this._scalabiltyMode = c;
            this._optimizationMode = e
        }

        get isPlaying() {
            return !(!this._player || this._player.videoElementStatus !== Ca.PLAYING)
        }

        play(a,
             b = {}) {
            let c = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.LOCAL_VIDEO_TRACK_PLAY,
                options: [this.getTrackId(), "string" == typeof a ? a : "HTMLElement", b]
            });
            if (!(a instanceof HTMLElement)) {
                let b = document.getElementById(a.toString());
                var e;
                b ? a = b : (k.warning(l(e = "[track-".concat(this.getTrackId(), '] can not find "#')).call(e, a, '" element, use document.body')), a = document.body)
            }
            k.debug("[track-".concat(this.getTrackId(), "] start video playback"), A(b));
            a = Gd({}, this._getDefaultPlayerConfig(), {}, b, {
                trackId: this.getTrackId(),
                element: a
            });
            this._player ? this._player.updateConfig(a) : (this._player = new Gk(a), this._player.updateVideoTrack(this._mediaStreamTrack));
            this._player.play();
            c.onSuccess()
        }

        stop() {
            let a = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.LOCAL_VIDEO_TRACK_STOP,
                options: [this.getTrackId()]
            });
            if (!this._player) return a.onSuccess();
            this._player.destroy();
            this._player = void 0;
            k.debug("[track-".concat(this.getTrackId(), "] stop video playback"));
            a.onSuccess()
        }

        async setEnabled(a) {
            if (a !== this._enabled) {
                k.info("[".concat(this.getTrackId(),
                    "] start setEnabled"), a);
                var b = await this._enabledMutex.lock();
                if (!a) {
                    this._originMediaStreamTrack.enabled = !1;
                    try {
                        await Qa(this, L.NEED_REMOVE_TRACK, this)
                    } catch (c) {
                        throw k.error("[".concat(this.getTrackId(), "] setEnabled to false error"), c.toString()), b(), c;
                    }
                    return this._enabled = !1, k.info("[".concat(this.getTrackId(), "] setEnabled to false success")), b()
                }
                this._originMediaStreamTrack.enabled = !0;
                try {
                    await Qa(this, L.NEED_ADD_TRACK, this)
                } catch (c) {
                    throw k.error("[".concat(this.getTrackId(), "] setEnabled to true error"),
                        c.toString()), b(), c;
                }
                k.info("[".concat(this.getTrackId(), "] setEnabled to true success"));
                this._enabled = !0;
                b()
            }
        }

        getStats() {
            Pc(() => {
                k.warning("[deprecated] LocalVideoTrack.getStats will be removed in the future, use AgoraRTCClient.getLocalVideoStats instead")
            }, "localVideoTrackGetStatsWarning");
            return Xb(this, L.GET_STATS) || Gd({}, ie)
        }

        async setBeautyEffect(a, b = {}) {
            let c = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.LOCAL_VIDEO_TRACK_BEAUTY,
                options: [this.getTrackId(), a, b]
            });
            if (a || this._videoBeautyProcessor) {
                if (ka().os ===
                    W.IOS || ka().os === W.ANDROID) throw a = new p(n.INVALID_OPERATION, "can not enable beauty effect on mobile device"), c.onError(a), a;
                if (!this._enabled && a) throw a = new p(n.TRACK_IS_DISABLED, "can not enable beauty effect when track is disabled"), c.onError(a), a;
                k.info("[".concat(this.getTrackId(), "] start setBeautyEffect"), a, A(b));
                try {
                    this._videoBeautyProcessor ? await this._videoBeautyProcessor.setBeautyEffectOptions(a, b) : (this._videoBeautyProcessor = new Ao, this._videoBeautyProcessor.onOverload = () => {
                        ab(() => this.emit(gd.BEAUTY_EFFECT_OVERLOAD))
                    },
                        await this._videoBeautyProcessor.setBeautyEffectOptions(a, b), await this._registerTrackProcessor(this._videoBeautyProcessor))
                } catch (e) {
                    throw k.error("[".concat(this.getTrackId(), "] setBeautyEffect error"), e.toString()), c.onError(e), e;
                }
                k.info("[".concat(this.getTrackId(), "] setBeautyEffect success"));
                c.onSuccess()
            }
        }

        getCurrentFrameData() {
            return this._player ? this._player.getCurrentFrame() : new ImageData(2, 2)
        }

        clone(a, b, c, e) {
            let g = this._mediaStreamTrack.clone();
            return new Na(g, a, b, c, e)
        }

        async setBitrateLimit(a) {
            var b;
            if (k.debug(l(b = "[".concat(this.getTrackId(), "] set bitrate limit, ")).call(b, A(a))), a) {
                this._forceBitrateLimit = a;
                this._encoderConfig && (this._encoderConfig.bitrateMax ? this._encoderConfig.bitrateMax = this._encoderConfig.bitrateMax < a.max_bitrate ? this._encoderConfig.bitrateMax : a.max_bitrate : this._encoderConfig.bitrateMax = a.max_bitrate, this._encoderConfig.bitrateMin, this._encoderConfig.bitrateMin = a.min_bitrate);
                try {
                    await Qa(this, L.NEED_RESET_REMOTE_SDP)
                } catch (c) {
                    return c.throw()
                }
            }
        }

        async setOptimizationMode(a) {
            var b;
            if ("motion" === a || "detail" === a || "balanced" === a) {
                try {
                    await Qa(this, L.SET_OPTIMIZATION_MODE, a)
                } catch (c) {
                    throw k.error("[".concat(this.getTrackId(), "] set optimization mode failed"), c.toString()), c;
                }
                this._optimizationMode = a;
                k.info(l(b = "[".concat(this.getTrackId(), "] set optimization mode success (")).call(b, a, ")"))
            } else k.error(n.INVALID_PARAMS, "optimization mode must be motion, detail or balanced")
        }

        setScalabiltyMode(a) {
            var b;
            if (1 === a.numSpatialLayers && 1 !== a.numTemporalLayers) return k.error(n.INVALID_PARAMS,
                "scalability mode currently not supported, no SVC."), void (this._scalabiltyMode = {
                numSpatialLayers: 1,
                numTemporalLayers: 1
            });
            this._scalabiltyMode = a;
            k.info(l(b = "[".concat(this.getTrackId(), "] set scalability mode success (")).call(b, a, ")"))
        }

        _updatePlayerSource() {
            this._player && this._player.updateVideoTrack(this._mediaStreamTrack)
        }

        _getDefaultPlayerConfig() {
            return {fit: "contain"}
        }
    }

    class Kk extends Na {
        constructor(a, b, c, e, g, h) {
            super(a, b.encoderConfig ? ic(b.encoderConfig) : {}, e, g, h);
            this._enabled = !0;
            this._deviceName =
                "default";
            this._config = b;
            this._constraints = c;
            this._deviceName = a.label;
            this._config.encoderConfig && (this._encoderConfig = ic(this._config.encoderConfig))
        }

        async setDevice(a) {
            var b;
            let c = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.CAM_VIDEO_TRACK_SET_DEVICE,
                options: [this.getTrackId(), a]
            });
            if (k.info(l(b = "[track-".concat(this.getTrackId(), "] set device to ")).call(b, a)), this._enabled) try {
                let c = await db.getDeviceById(a);
                b = {};
                b.video = Gd({}, this._constraints);
                b.video.deviceId = {exact: a};
                b.video.facingMode = void 0;
                this._originMediaStreamTrack.stop();
                let g = null;
                try {
                    g = await zb(b, this.getTrackId())
                } catch (h) {
                    throw k.error("[".concat(this.getTrackId(), "] setDevice failed"), h.toString()), g = await zb({video: this._constraints}, this.getTrackId()), await this._updateOriginMediaStreamTrack(g.getVideoTracks()[0], !1), h;
                }
                await this._updateOriginMediaStreamTrack(g.getVideoTracks()[0], !1);
                Mc(this._originMediaStreamTrack).then(([a, b]) => {
                    this._videoHeight = b;
                    this._videoWidth = a
                });
                this._deviceName = c.label;
                this._config.cameraId = a;
                this._constraints.deviceId = {exact: a}
            } catch (e) {
                throw c.onError(e), k.error("[".concat(this.getTrackId(), "] setDevice error"), e.toString()), e;
            } else try {
                this._deviceName = (await db.getDeviceById(a)).label, this._config.cameraId = a, this._constraints.deviceId = {exact: a}
            } catch (e) {
                throw c.onError(e), k.error("[track-".concat(this.getTrackId(), "] setDevice error"), e.toString()), e;
            }
            k.info("[".concat(this.getTrackId(), "] setDevice success"));
            c.onSuccess()
        }

        async setEnabled(a) {
            if (a !== this._enabled) {
                k.info("[".concat(this.getTrackId(),
                    "] start setEnabled"), a);
                var b = await this._enabledMutex.lock();
                if (!a) {
                    this._originMediaStreamTrack.onended = null;
                    this._originMediaStreamTrack.stop();
                    this._enabled = !1;
                    try {
                        await Qa(this, L.NEED_REMOVE_TRACK, this)
                    } catch (e) {
                        throw k.error("[".concat(this.getTrackId(), "] setEnabled to false error"), e.toString()), b(), e;
                    }
                    return k.info("[".concat(this.getTrackId(), "] setEnabled to false success")), b()
                }
                a = Gd({}, this._constraints);
                var c = db.searchDeviceIdByName(this._deviceName);
                c && !a.deviceId && (a.deviceId = {exact: c});
                try {
                    let a = await zb({video: this._constraints}, this.getTrackId());
                    await this._updateOriginMediaStreamTrack(a.getVideoTracks()[0], !1);
                    await Qa(this, L.NEED_ADD_TRACK, this)
                } catch (e) {
                    throw k.error("[".concat(this.getTrackId(), "] setEnabled true error"), e.toString()), b(), e;
                }
                Mc(this._originMediaStreamTrack).then(([a, b]) => {
                    this._videoHeight = b;
                    this._videoWidth = a
                });
                k.info("[".concat(this.getTrackId(), "] setEnabled to true success"));
                this._enabled = !0;
                b()
            }
        }

        async setEncoderConfiguration(a, b) {
            b = t.reportApiInvoke(null,
                {tag: D.TRACER, name: E.CAM_VIDEO_TRACK_SET_ENCODER_CONFIG, options: [this.getTrackId(), a]});
            if (!this._enabled) throw a = new p(n.TRACK_IS_DISABLED, "can not set encoder configuration when track is disabled"), b.onError(a), a;
            a = ic(a);
            this._forceBitrateLimit && (a.bitrateMax = this._forceBitrateLimit.max_bitrate ? this._forceBitrateLimit.max_bitrate : a.bitrateMax, a.bitrateMin = this._forceBitrateLimit.min_bitrate ? this._forceBitrateLimit.min_bitrate : a.bitrateMin);
            let c = (e = this._config, JSON.parse(A(e)));
            var e;
            c.encoderConfig =
                a;
            e = Pe(c);
            k.debug("[".concat(this.getTrackId(), "] setEncoderConfiguration applyConstraints"), A(a), A(e));
            try {
                await this._originMediaStreamTrack.applyConstraints(e), Mc(this._originMediaStreamTrack).then(([a, b]) => {
                    this._videoHeight = b;
                    this._videoWidth = a
                })
            } catch (g) {
                throw a = new p(n.UNEXPECTED_ERROR, g.toString()), k.error("[track-".concat(this.getTrackId(), "] applyConstraints error"), a.toString()), b.onError(a), a;
            }
            this._config = c;
            this._constraints = e;
            this._encoderConfig = a;
            try {
                await Qa(this, L.NEED_RENEGOTIATE)
            } catch (g) {
                return b.onError(g),
                    g.throw()
            }
            b.onSuccess()
        }

        _getDefaultPlayerConfig() {
            return {mirror: !0, fit: "cover"}
        }
    }

    var Lk = !0;
    "findIndex" in [] && Array(1).findIndex(function () {
        Lk = !1
    });
    O({target: "Array", proto: !0, forced: Lk}, {
        findIndex: function (a) {
            return bm(this, a, 1 < arguments.length ? arguments[1] : void 0)
        }
    });
    var Bo = za("Array").findIndex, Mk = Array.prototype, Nk = function (a) {
        var b = a.findIndex;
        return a === Mk || a instanceof Array && b === Mk.findIndex ? Bo : b
    };

    class Ok extends Fk {
        constructor(a, b, c, e) {
            super(b, b.stringUid || b.uid);
            this.type = "pub";
            this._waitingSuccessResponse =
                this.detecting = !1;
            this.renegotiateWithGateway = async () => (k.debug("[pc-".concat(this.pc.ID, "] renegotiate start")), new v(async (a, b) => {
                this.connectionState = "connecting";
                let c = e => {
                    "connected" === e && (this.off(I.CONNECTION_STATE_CHANGE, c), a());
                    "disconnected" === e && (this.off(I.CONNECTION_STATE_CHANGE, c), b(new p(n.OPERATION_ABORTED, "renegotiate abort")))
                };
                this.on(I.CONNECTION_STATE_CHANGE, c);
                var e = await this.pc.createOfferSDP();
                this.audioTrack && this.audioTrack._encoderConfig && (e = Qe(e, this.audioTrack._encoderConfig));
                this.videoTrack && this.videoTrack._scalabiltyMode && ("vp9" !== this.codec ? (this.videoTrack._scalabiltyMode.numSpatialLayers = 1, this.videoTrack._scalabiltyMode.numTemporalLayers = 1) : e = Ih(e, this.codec, this.videoTrack._scalabiltyMode), k.debug("renegoation spatial layers: ", this.videoTrack._scalabiltyMode.numSpatialLayers));
                await this.pc.setOfferSDP(e);
                this.pc.onOfferSettled();
                let g = await Ma(this, I.NEED_RENEGOTIATE, e);
                e = function (a, b) {
                    var c, e, g;
                    const h = ta(c = RegExp.prototype.test).call(c, /^([a-z])=(.*)/);
                    a = N(e =
                        a.split(/(\r\n|\r|\n)/)).call(e, h);
                    b = N(g = b.split(/(\r\n|\r|\n)/)).call(g, h);
                    let k = null;
                    const m = new ba;
                    return q(a).call(a, a => {
                        const b = a.match(/m=(audio|video)/);
                        if (b && b[1]) return void (k = b[1]);
                        k && (a = a.match(/=(sendrecv|recvonly|sendonly|inactive)/)) && a[1] && m.set(k, a[1])
                    }), k = null, C(b).call(b, a => {
                        var b = a.match(/m=(audio|video)/);
                        if (b && b[1]) return k = b[1], a;
                        if (!k) return a;
                        if ((b = a.match(/=(sendrecv|recvonly|sendonly|inactive)/)) && b[1]) {
                            const c = m.get(k);
                            if (c && c !== b[1]) return a.replace(b[1], c)
                        }
                        return a
                    }).join("\r\n") +
                    "\r\n"
                }(e, this.updateAnswerSDP(g.sdp));
                await this.pc.setAnswerSDP(e);
                k.debug("[pc-".concat(this.pc.ID, "] renegotiate success"));
                this.connectionState = "connected"
            }));
            this.handleStreamRenegotiate = (a, b) => {
                "connected" === this.connectionState ? this.renegotiateWithGateway().then(a).catch(b) : a()
            };
            this.handleReplaceTrack = (a, b, c) => {
                if (this.audioTrack instanceof Gc && "audio" === a.kind) return v.resolve();
                this.pc.replaceTrack(a).then(a => a ? this.renegotiateWithGateway() : v.resolve()).then(b).catch(c)
            };
            this.handleCloseAudioTrack =
                a => {
                };
            this.handleCloseVideoTrack = () => {
                this.lowStreamConnection && this.lowStreamConnection.videoTrack && this.lowStreamConnection.videoTrack.close()
            };
            this.handleGetSessionID = a => {
                a(this.joinInfo.sid)
            };
            this.handleGetLocalVideoStats = a => {
                a(this.statsCollector.getLocalVideoTrackStats(this.connectionId))
            };
            this.handleGetLocalAudioStats = a => {
                a(this.statsCollector.getLocalAudioTrackStats(this.connectionId))
            };
            this.handleSetOptimizationMode = (a, b, c) => {
                this.pc.setRtpSenderParameters({}, "detail" === a ? "maintain-resolution" :
                    "motion" === a ? "maintain-framerate" : a).then(b).catch(c)
            };
            this.isLowStreamConnection = !!e;
            this.codec = c;
            this.statsCollector = a;
            this.statsCollector.addLocalConnection(this)
        }

        getAllTracks() {
            let a = [];
            return this.videoTrack && a.push(this.videoTrack), this.audioTrack && this.audioTrack instanceof Gc ? a = l(a).call(a, this.audioTrack.trackList) : this.audioTrack && a.push(this.audioTrack), a
        }

        async addTracks(a) {
            let b = ca;
            if ("connecting" === this.connectionState) try {
                return await this.createWaitConnectionConnectedPromise(), await this.addTracks(a)
            } catch (h) {
                throw new p(n.OPERATION_ABORTED,
                    "publish abort");
            }
            var c = !1;
            let e = this.getAllTracks();
            a = gh(a = N(a).call(a, a => -1 === G(e).call(e, a)));
            for (let e = 0; e < a.length; e += 1) {
                var g = a[e];
                if (!(g instanceof ke)) return (new p(n.INVALID_LOCAL_TRACK)).throw();
                if (g instanceof Na && this.disabledVideoTrack) {
                    if (this.disabledVideoTrack !== g) return (new p(n.EXIST_DISABLED_VIDEO_TRACK)).throw();
                    this.disabledVideoTrack = void 0
                }
                if (g instanceof Na && this.videoTrack) return (new p(n.CAN_NOT_PUBLISH_MULTIPLE_VIDEO_TRACKS)).throw();
                if (g instanceof $a && this.audioTrack) if (this.audioTrack instanceof
                    Gc) this.audioTrack.addAudioTrack(g); else {
                    if (!b.webAudioMediaStreamDest) throw new p(n.NOT_SUPPORTED, "your browser is not support audio mixing");
                    c = new Gc;
                    c.addAudioTrack(this.audioTrack);
                    c.addAudioTrack(g);
                    c = await this.addTrackWithPC(c)
                } else g instanceof Na && this.isLowStreamConnection ? (c = Kh({}, {
                    width: 160,
                    height: 120,
                    framerate: 15,
                    bitrate: 50
                }, {}, this.lowStreamParameter), b.supportDualStreamEncoding ? (k.debug("[".concat(this.connectionId, "] creating low stream using rtp encoding.")), this.lowStreamEncoding =
                    fl(c, g), g = g.clone({
                    bitrateMax: c.bitrate,
                    bitrateMin: c.bitrate
                })) : (k.debug("[".concat(this.connectionId, "] creating low stream using canvas.")), g = tl(g, c), g = new Na(g, {
                    bitrateMax: c.bitrate,
                    bitrateMin: c.bitrate
                })), g._hints.push(tb.LOW_STREAM), c = await this.addTrackWithPC(g), this.bindTrackEvents(g)) : (this.detecting = !0, Fc(() => {
                    this.detecting = !1
                }, 8E3), c = await this.addTrackWithPC(g))
            }
            c && await this.renegotiateWithGateway();
            q(a).call(a, a => this.bindTrackEvents(a))
        }

        async removeTracks(a, b) {
            let c = this.getAllTracks();
            a = gh(a = N(a).call(a, a => -1 !== G(c).call(c, a) || a === this.disabledVideoTrack));
            let e = [];
            for (let c = 0; c < a.length; c += 1) {
                let g = a[c];
                if (this.unbindTrackEvents(g), this.audioTrack instanceof Gc && g instanceof $a) this.audioTrack.removeAudioTrack(g), 0 === this.audioTrack.trackList.length && (e.push(this.audioTrack), this.audioTrack = void 0); else if (g instanceof $a) e.push(g), this.audioTrack = void 0; else if (g instanceof Na) {
                    if (b) {
                        if (this.disabledVideoTrack === g) return void (this.disabledVideoTrack = void 0)
                    } else this.disabledVideoTrack =
                        this.videoTrack;
                    e.push(g);
                    this.isLowStreamConnection && g.close();
                    this.videoTrack = void 0
                }
            }
            if (this.videoTrack || this.audioTrack) {
                if (0 !== e.length) {
                    if ("connecting" === this.connectionState) try {
                        await this.createWaitConnectionConnectedPromise()
                    } catch (h) {
                        return
                    }
                    for (let a of e) {
                        var g;
                        k.debug(l(g = "[".concat(this.connectionId, "] remove ")).call(g, a.trackMediaType, " from pc"));
                        await this.pc.removeTrack(a._mediaStreamTrack)
                    }
                    await this.renegotiateWithGateway()
                }
            } else await this.closeP2PConnection()
        }

        startP2PConnection() {
            return new v(async (a,
                                b) => {
                if (!this.audioTrack && !this.videoTrack) return b(new p(n.UNEXPECTED_ERROR, "no track to publish"));
                let c = e => {
                    if ("connected" === e && (this.off(I.CONNECTION_STATE_CHANGE, c), a()), "disconnected" === e) {
                        if (this.off(I.CONNECTION_STATE_CHANGE, c), this.disconnectedReason) return b(this.disconnectedReason);
                        b(new p(n.OPERATION_ABORTED, "publish abort"))
                    }
                };
                this.on(I.CONNECTION_STATE_CHANGE, c);
                this.disconnectedReason = void 0;
                this.connectionState = "connecting";
                this._waitingSuccessResponse = !0;
                this.startTime = x();
                try {
                    !this.pc.videoTrack &&
                    this.videoTrack && await this.pc.addTrack(this.videoTrack._mediaStreamTrack);
                    !this.pc.audioTrack && this.audioTrack && await this.pc.addTrack(this.audioTrack._mediaStreamTrack);
                    var e = await this.pc.createOfferSDP();
                    this.audioTrack && this.audioTrack._encoderConfig && (e = Qe(e, this.audioTrack._encoderConfig));
                    this.videoTrack && this.videoTrack._scalabiltyMode && ("vp9" !== this.codec && "av1" !== this.codec ? (this.videoTrack._scalabiltyMode.numSpatialLayers = 1, this.videoTrack._scalabiltyMode.numTemporalLayers = 1) : e = Ih(e, this.codec,
                        this.videoTrack._scalabiltyMode), k.debug("spatial layers: ", this.videoTrack._scalabiltyMode.numSpatialLayers));
                    await this.pc.setOfferSDP(e);
                    this.pc.onOfferSettled();
                    k.debug("[".concat(this.connectionId, "] create and set offer success"));
                    e = {messageType: "OFFER", sdp: e, offererSessionId: 104, retry: !0};
                    ca.supportDualStreamEncoding && this.isLowStreamConnection && this.lowStreamEncoding && this.videoTrack && await this.setLowStreamEncoding(this.lowStreamEncoding, this.videoTrack);
                    let a = await Ma(this, I.NEED_ANSWER,
                        e), b = this.updateAnswerSDP(a.sdp);
                    await this.pc.setAnswerSDP(b);
                    k.debug("[".concat(this.connectionId, "] set answer success"));
                    await this.icePromise;
                    this.connectionState = "connected";
                    this.startUploadStats()
                } catch (g) {
                    this.off(I.CONNECTION_STATE_CHANGE, c), this.connectionState = "disconnected", this.reportPublishEvent(!1, g.code), k.error("[".concat(this.connectionId, "] connection error"), g.toString()), b(g)
                }
            })
        }

        reportPublishEvent(a, b, c) {
            var e;
            this._waitingSuccessResponse = !1;
            t.publish(this.joinInfo.sid, {
                lts: this.startTime,
                succ: a,
                ec: b,
                audioName: this.audioTrack && this.audioTrack.getTrackLabel(),
                videoName: this.videoTrack && this.videoTrack.getTrackLabel(),
                screenshare: !(!this.videoTrack || -1 === G(e = this.videoTrack._hints).call(e, tb.SCREEN_TRACK)),
                audio: !!this.audioTrack,
                video: !!this.videoTrack,
                p2pid: this.pc.ID,
                publishRequestid: this.ID,
                extend: c
            })
        }

        async closeP2PConnection(a) {
            let b = this.getAllTracks();
            var c;
            (q(b).call(b, a => {
                this.unbindTrackEvents(a)
            }), this.isLowStreamConnection && this.videoTrack && this.videoTrack.close(), this.videoTrack =
                void 0, this.audioTrack instanceof Gc) && q(c = this.audioTrack.trackList).call(c, a => {
                this.audioTrack.removeAudioTrack(a)
            });
            this.audioTrack = void 0;
            this.stopUploadStats();
            this.statsCollector.removeConnection(this.connectionId);
            await this.closePC(a);
            this.connectionState = "disconnected";
            this.removeAllListeners()
        }

        getNetworkQuality() {
            var a, b = this.pc.getStats();
            if (!b.videoSend[0] && !b.audioSend[0]) return 1;
            var c = Xb(this, I.NEED_SIGNAL_RTT), e = b.videoSend[0] ? b.videoSend[0].rttMs : void 0;
            let g = b.audioSend[0] ? b.audioSend[0].rttMs :
                void 0;
            e = e && g ? (e + g) / 2 : e || g;
            c = 70 * b.sendPacketLossRate / 50 + .3 * ((e && c ? (e + c) / 2 : e || c) || 0) / 1500;
            c = .17 > c ? 1 : .36 > c ? 2 : .59 > c ? 3 : .1 > c ? 4 : 5;
            return this.videoTrack && this.videoTrack._encoderConfig && -1 === G(a = this.videoTrack._hints).call(a, tb.SCREEN_TRACK) && (a = this.videoTrack._encoderConfig.bitrateMax, b = b.bitrate.actualEncoded, a && b) ? (b = (1E3 * a - b) / (1E3 * a), An[.15 > b ? 0 : .3 > b ? 1 : .45 > b ? 2 : .6 > b ? 3 : 4][c]) : c
        }

        handleUpdateBitrateLimit(a) {
            this.videoTrack && this.videoTrack.setBitrateLimit(a)
        }

        uploadStats(a, b) {
            let c = this.audioTrack ? function (a,
                                                b) {
                const c = a.audioSend[0];
                if (!c) return null;
                a = {
                    id: pa(10, ""),
                    timestamp: (new Date(a.timestamp)).toISOString(),
                    mediaType: "audio",
                    type: "ssrc",
                    ssrc: c.ssrc.toString()
                };
                return a.A_astd = b._originMediaStreamTrack.enabled && b._mediaStreamTrack.enabled ? "0" : "1", c.inputLevel ? a.A_ail = Math.round(100 * c.inputLevel).toString() : a.A_ail = Math.round(100 * b._source.getAudioAvgLevel()).toString(), a.A_apil = Math.round(100 * b._source.getAudioAvgLevel()).toString(), a
            }(a, this.audioTrack) : void 0, e = this.videoTrack ? function (a, b) {
                const c =
                    a.videoSend[0];
                if (!c) return null;
                a = {
                    id: pa(10, ""),
                    timestamp: (new Date(a.timestamp)).toISOString(),
                    mediaType: "video",
                    type: "ssrc",
                    ssrc: c.ssrc.toString()
                };
                switch (a.A_vstd = b._originMediaStreamTrack && !b._originMediaStreamTrack.enabled || !b._mediaStreamTrack.enabled ? "1" : "0", c.sentFrame && (a.A_fhs = c.sentFrame.height.toString(), a.A_frs = c.sentFrame.frameRate.toString(), a.A_fws = c.sentFrame.width.toString()), c.adaptionChangeReason) {
                    case "none":
                        a.A_ac = "0";
                        break;
                    case "cpu":
                        a.A_ac = "1";
                        break;
                    case "bandwidth":
                        a.A_ac =
                            "2";
                        break;
                    case "other":
                        a.A_ac = "3"
                }
                return a.A_nr = c.nacksCount.toString(), c.avgEncodeMs && (a.A_aem = c.avgEncodeMs.toFixed(0).toString()), a
            }(a, this.videoTrack) : void 0, g = dh(a, b), h = function (a) {
                const b = {id: "bweforvideo", timestamp: (new Date(a.timestamp)).toISOString(), type: "VideoBwe"};
                return a.bitrate.retransmit && (b.A_rb = a.bitrate.retransmit.toString()), a.bitrate.targetEncoded && (b.A_teb = a.bitrate.targetEncoded.toString()), b.A_aeb = a.bitrate.actualEncoded.toString(), b.A_tb = a.bitrate.transmit.toString(), void 0 !==
                a.sendBandwidth && (b.A_asb = a.sendBandwidth.toString()), b
            }(a);
            c && ab(() => this.emit(I.NEED_UPLOAD, sb.PUBLISH_STATS, c));
            e && ab(() => this.emit(I.NEED_UPLOAD, sb.PUBLISH_STATS, Kh({}, e, {}, g)));
            h && ab(() => this.emit(I.NEED_UPLOAD, sb.PUBLISH_STATS, h))
        }

        uploadSlowStats(a) {
            let b = dh(a);
            b && ab(() => this.emit(I.NEED_UPLOAD, sb.PUBLISH_STATS, b))
        }

        uploadRelatedStats(a) {
            let b = function (a) {
                return (a = a.videoSend[0]) ? {
                    mediaType: "video",
                    isVideoMute: !1,
                    frameRateInput: a.inputFrame && a.inputFrame.frameRate.toString(),
                    frameRateSent: a.sentFrame &&
                        a.sentFrame.frameRate.toString(),
                    googRtt: a.rttMs.toString()
                } : null
            }(a);
            b && ab(() => {
                this.emit(I.NEED_UPLOAD, sb.PUBLISH_RELATED_STATS, b)
            })
        }

        bindTrackEvents(a) {
            var b;
            a.addListener(L.NEED_RESET_REMOTE_SDP, ta(b = this.handleResetRemoteSdp).call(b, this));
            this.isLowStreamConnection || (a instanceof $a ? (a.addListener(L.GET_STATS, this.handleGetLocalAudioStats), a.addListener(L.NEED_CLOSE, this.handleCloseAudioTrack)) : a instanceof Na && (a.addListener(L.GET_STATS, this.handleGetLocalVideoStats), a.addListener(L.NEED_CLOSE,
                this.handleCloseVideoTrack), a.addListener(L.SET_OPTIMIZATION_MODE, this.handleSetOptimizationMode)), a.addListener(L.NEED_RENEGOTIATE, this.handleStreamRenegotiate), a.addListener(L.NEED_REPLACE_TRACK, this.handleReplaceTrack), a.addListener(L.NEED_SESSION_ID, this.handleGetSessionID))
        }

        unbindTrackEvents(a) {
            this.isLowStreamConnection || (a instanceof $a ? (a.off(L.GET_STATS, this.handleGetLocalAudioStats), a.off(L.NEED_CLOSE, this.handleCloseAudioTrack)) : a instanceof Na && (a.off(L.GET_STATS, this.handleGetLocalVideoStats),
                a.off(L.NEED_CLOSE, this.handleCloseVideoTrack)), a.off(L.NEED_RENEGOTIATE, this.handleStreamRenegotiate), a.off(L.NEED_REPLACE_TRACK, this.handleReplaceTrack), a.off(L.NEED_SESSION_ID, this.handleGetSessionID))
        }

        async addTrackWithPC(a) {
            if ("connecting" === this.connectionState) return (new p(n.INVALID_OPERATION, "last publish operation has not finished")).throw();
            var b = this.videoTrack;
            let c = !1;
            this.audioTrack && a instanceof $a ? (this.audioTrack = a, k.debug("[".concat(this.connectionId, "] replace pc audio track")),
                c = await this.pc.replaceTrack(a._mediaStreamTrack)) : this.videoTrack && a instanceof Na ? (this.videoTrack = a, k.debug("[".concat(this.connectionId, "] replace pc video track")), c = await this.pc.replaceTrack(a._mediaStreamTrack)) : a instanceof $a ? (this.audioTrack = a, k.debug("[".concat(this.connectionId, "] add audio track to pc")), await this.pc.addTrack(a._mediaStreamTrack), c = !0) : a instanceof Na && (this.videoTrack = a, k.debug("[".concat(this.connectionId, "] add video track to pc")), await this.pc.addTrack(a._mediaStreamTrack),
                c = !0);
            a = ca;
            this.videoTrack !== b && this.videoTrack && a.supportSetRtpSenderParameters && (b = {}, a = "balanced", this.videoTrack._encoderConfig && (b.maxBitrate = this.videoTrack._encoderConfig.bitrateMax ? 1E3 * this.videoTrack._encoderConfig.bitrateMax : void 0), "motion" === this.videoTrack._optimizationMode ? a = "maintain-framerate" : "detail" === this.videoTrack._optimizationMode && (a = "maintain-resolution"), k.debug("[".concat(this.connectionId, "] set pc rtp sender"), b, a), await this.pc.setRtpSenderParameters(b, a));
            return "disconnected" !==
                this.connectionState && c
        }

        handleResetRemoteSdp() {
            return new v((a, b) => {
                var c;
                k.info("[pc-".concat(this.pc.ID, "] start reset remote sdp"));
                let e = this.pc.getOfferSDP();
                var g = this.pc.getAnswerSDP();
                if (!g || !e) return a();
                g = g.sdp;
                let h;
                this.videoTrack && this.videoTrack._encoderConfig && -1 === G(c = this.videoTrack._hints).call(c, tb.SCREEN_TRACK) && (h = function (a, b) {
                    var c, e;
                    let g = b.bitrateMin;
                    b = b.bitrateMax;
                    let h = ta(c = RegExp.prototype.test).call(c, /^([a-z])=(.*)/);
                    a = N(e = a.split(/(\r\n|\r|\n)/)).call(e, h);
                    if (b) {
                        let c =
                            "AS";
                        ka().name === Z.FIREFOX && (b = 1E3 * (b >>> 0), c = "TIAS");
                        e = Nk(a).call(a, a => ya(a).call(a, c));
                        var k;
                        0 < e && (a[e] = l(k = "b=".concat(c, ":")).call(k, b))
                    }
                    g && (k = Nk(a).call(a, a => ya(a).call(a, "x-google-min-bitrate")), 0 < k && (a[k] = a[k].replace(/x-google-min-bitrate=(.*)/, "x-google-min-bitrate=".concat(g))));
                    return a.join("\r\n") + "\r\n"
                }(g, this.videoTrack._encoderConfig));
                g !== h ? this.pc.setOfferSDP(e.sdp).then(() => {
                    if (h) return this.pc.setAnswerSDP(h)
                }).then(a).catch(a => {
                    var c;
                    k.error(l(c = "[pc-".concat(this.pc.ID, "] reset remote sdp error, ")).call(c,
                        a));
                    b(a)
                }) : k.debug("[pc-".concat(this.pc.ID, "] remote sdp have no not changed"))
            })
        }

        updateAnswerSDP(a) {
            var b, c;
            return a = a.replace(/a=x-google-flag:conference\r\n/g, ""), this.videoTrack && G(b = this.videoTrack._hints).call(b, tb.SCREEN_TRACK), this.videoTrack && this.videoTrack._encoderConfig && -1 === G(c = this.videoTrack._hints).call(c, tb.SCREEN_TRACK) && (a = function (a, b, c) {
                var e = ca;
                let g = c.bitrateMin;
                c = c.bitrateMax;
                let h = a.match(/m=video.*\r\n/) || a.match(/m=video.*\n/);
                if (h && 0 < h.length && e.supportMinBitrate && g) {
                    e =
                        null;
                    var k, n;
                    "h264" === b ? e = a.match(/a=rtpmap:(\d+) H264\/90000\r\n/) || a.match(/a=rtpmap:(\d+) H264\/90000\n/) : "vp8" === b ? e = a.match(/a=rtpmap:(\d+) VP8\/90000\r\n/) || a.match(/a=rtpmap:(\d+) VP8\/90000\n/) : "vp9" === b ? e = a.match(/a=rtpmap:(\d+) VP9\/90000\r\n/) || a.match(/a=rtpmap:(\d+) VP9\/90000\n/) : "av1" === b && (e = a.match(/a=rtpmap:(\d+) AV1X\/90000\r\n/) || a.match(/a=rtpmap:(\d+) AV1X\/90000\n/));
                    e && e[1] && (a = a.replace(h[0], l(k = l(n = "".concat(h[0], "a=fmtp:")).call(n, e[1], " x-google-min-bitrate=")).call(k,
                        g, "\r\n")))
                }
                if (h && 0 < h.length && c) {
                    var p, q;
                    b = "AS";
                    ka().name === Z.FIREFOX && (c = 1E3 * (c >>> 0), b = "TIAS");
                    a = a.replace(h[0], l(p = l(q = "".concat(h[0], "b=")).call(q, b, ":")).call(p, c, "\r\n"))
                }
                return a
            }(a, this.codec, this.videoTrack._encoderConfig)), this.audioTrack && this.audioTrack._encoderConfig && (a = Qe(a, this.audioTrack._encoderConfig)), a = function (a) {
                let b = ka();
                return b.name !== Z.SAFARI && b.os !== W.IOS ? a : a.replace(/a=.*video-orientation\r\n/g, "")
            }(a)
        }

        createPC() {
            this.pc = new Ck({turnServer: this.joinInfo.turnServer});
            this.updateICEPromise()
        }

        async closePC(a) {
            return this.pc.onICEConnectionStateChange =
                void 0, this.pc.close(), !a && await Ma(this, I.NEED_UNPUB)
        }

        onPCDisconnected(a) {
            this.reportPublishEvent(!1, a.code)
        }

        async setLowStreamEncoding(a, b) {
            try {
                let c = await this.pc.setVideoRtpEncodingParameters(a), e = b.getMediaStreamTrack();
                if (a.scaleResolutionDownBy && c.encodings[0].scaleResolutionDownBy !== a.scaleResolutionDownBy) {
                    let c = b._videoHeight || e.getSettings().height, h = b._videoWidth || e.getSettings().width;
                    c && h && await e.applyConstraints({
                        height: c / a.scaleResolutionDownBy,
                        width: h / a.scaleResolutionDownBy
                    })
                }
                a.maxFramerate &&
                c.encodings[0].maxFramerate !== a.maxFramerate && await e.applyConstraints({frameRate: a.maxFramerate})
            } catch (c) {
                if (c instanceof p) throw c;
                throw new p(n.LOW_STREAM_ENCODING_ERROR, c.message);
            }
        }
    }

    class Pk extends sk {
        constructor(a, b, c) {
            super(a);
            this._isDestroyed = !1;
            this._userId = b;
            this._uintId = c
        }

        getUserId() {
            return this._userId
        }

        _updateOriginMediaStreamTrack(a) {
            this._mediaStreamTrack = this._originMediaStreamTrack = a;
            this._updatePlayerSource()
        }

        _destroy() {
            this._isDestroyed = !0;
            k.info("[track-".concat(this.getTrackId(),
                "] is destroyed"));
            this.stop()
        }
    }

    class nd extends Pk {
        constructor(a, b, c) {
            super(a, b, c);
            this.trackMediaType = "video";
            Mc(a).then(([a, b]) => {
                this._videoHeight = b;
                this._videoWidth = a
            }).catch(Uf)
        }

        get isPlaying() {
            return !(!this._player || this._player.videoElementStatus !== Ca.PLAYING)
        }

        getStats() {
            Pc(() => {
                k.warning("[deprecated] RemoteVideoTrack.getStats will be removed in the future, use AgoraRTCClient.getRemoteVideoStats instead")
            }, "remoteVideoTrackGetStatsWarning");
            return Xb(this, L.GET_STATS) || Re({}, Kf)
        }

        play(a, b =
            {}) {
            let c = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.REMOTE_VIDEO_TRACK_PLAY,
                options: [this.getTrackId(), "string" == typeof a ? a : "HTMLElement", b]
            });
            if ("string" == typeof a) {
                let b = document.getElementById(a);
                var e;
                b ? a = b : (k.warning(l(e = "[track-".concat(this.getTrackId(), '] can not find "#')).call(e, a, '" element, use document.body')), a = document.body)
            }
            k.debug("[track-".concat(this.getTrackId(), "] start video playback"), A(b));
            a = Re({fit: "cover"}, b, {trackId: this.getTrackId(), element: a});
            this._player ? this._player.updateConfig(a) :
                (this._player = new Gk(a), this._player.updateVideoTrack(this._mediaStreamTrack), this._player.onFirstVideoFrameDecoded = () => {
                    this.emit(hd.FIRST_FRAME_DECODED)
                });
            this._player.play();
            c.onSuccess()
        }

        stop() {
            let a = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.REMOTE_VIDEO_TRACK_STOP,
                options: [this.getTrackId()]
            });
            if (!this._player) return a.onSuccess();
            this._player.destroy();
            this._player = void 0;
            k.debug("[track-".concat(this.getTrackId(), "] stop video playback"));
            a.onSuccess()
        }

        getCurrentFrameData() {
            return this._player ?
                this._player.getCurrentFrame() : new ImageData(2, 2)
        }

        _updatePlayerSource() {
            k.debug("[track-".concat(this.getTrackId(), "] update player source track"));
            this._player && this._player.updateVideoTrack(this._mediaStreamTrack)
        }
    }

    class od extends Pk {
        constructor(a, b, c) {
            super(a, b, c);
            this.trackMediaType = "audio";
            this._useAudioElement = !1;
            this._source = new uk(a, !0);
            this._source.once(jb.RECEIVE_TRACK_BUFFER, () => {
                this.emit(hd.FIRST_FRAME_DECODED)
            });
            ca.webAudioWithAEC || (this._useAudioElement = !0)
        }

        get isPlaying() {
            return this._useAudioElement ?
                lb.isPlaying(this.getTrackId()) : this._source.isPlayed
        }

        setAudioFrameCallback(a, b = 4096) {
            if (!a) return this._source.removeAllListeners(jb.ON_AUDIO_BUFFER), void this._source.stopGetAudioBuffer();
            this._source.startGetAudioBuffer(b);
            this._source.removeAllListeners(jb.ON_AUDIO_BUFFER);
            this._source.on(jb.ON_AUDIO_BUFFER, b => a(b))
        }

        setVolume(a) {
            let b = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.REMOTE_AUDIO_SET_VOLUME,
                options: [this.getTrackId(), a]
            }, 300);
            this._useAudioElement ? lb.setVolume(this.getTrackId(), a) :
                this._source.setVolume(a / 100);
            b.onSuccess()
        }

        async setPlaybackDevice(a) {
            let b = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.REMOTE_AUDIO_SET_OUTPUT_DEVICE,
                options: [this.getTrackId(), a]
            });
            if (!this._useAudioElement) throw new p(n.NOT_SUPPORTED, "your browser does not support setting the audio output device");
            try {
                await lb.setSinkID(this.getTrackId(), a)
            } catch (c) {
                throw b.onError(c), c;
            }
            b.onSuccess()
        }

        getVolumeLevel() {
            return this._source.getAudioLevel()
        }

        getStats() {
            Pc(() => {
                    k.warning("[deprecated] RemoteAudioTrack.getStats will be removed in the future, use AgoraRTCClient.getRemoteAudioStats instead")
                },
                "remoteAudioTrackGetStatsWarning");
            return Xb(this, L.GET_STATS) || Re({}, Jf)
        }

        play() {
            let a = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.REMOTE_AUDIO_TRACK_PLAY,
                options: [this.getTrackId()]
            });
            k.debug("[".concat(this.getTrackId(), "] start audio playback"));
            this._useAudioElement ? (k.debug("[track-".concat(this.getTrackId(), "] use audio element to play")), lb.play(this._mediaStreamTrack, this.getTrackId())) : this._source.play();
            a.onSuccess()
        }

        stop() {
            let a = t.reportApiInvoke(null, {
                tag: D.TRACER, name: E.REMOTE_AUDIO_TRACK_STOP,
                options: [this.getTrackId()]
            });
            k.debug("[".concat(this.getTrackId(), "] stop audio playback"));
            this._useAudioElement ? lb.stop(this.getTrackId()) : this._source.stop();
            a.onSuccess()
        }

        _destroy() {
            super._destroy();
            this._source.destroy()
        }

        _isFreeze() {
            return this._source.isFreeze
        }

        _updatePlayerSource() {
            k.debug("[track-".concat(this.getTrackId(), "] update player source track"));
            this._source.updateTrack(this._mediaStreamTrack);
            this._useAudioElement && lb.updateTrack(this.getTrackId(), this._mediaStreamTrack)
        }
    }

    class Co extends Fk {
        constructor(a,
                    b, c, e) {
            super(c, a.uid);
            this.type = "sub";
            this.unusedTracks = [];
            this.onTrack = a => {
                var b, c;
                if ("audio" === a.kind && !this.subscribeOptions.audio || "video" === a.kind && !this.subscribeOptions.video) return this.unusedTracks.push(a), void k.debug(l(c = "[".concat(this.connectionId, "] unused ontrack event, kind: ")).call(c, a.kind));
                k.debug(l(b = "[".concat(this.connectionId, "] emit pc ontrack after subscribe ")).call(b, a.kind), a);
                b = "audio" === a.kind ? this.user._audioTrack : this.user._videoTrack;
                var e, g;
                b ? b._updateOriginMediaStreamTrack(a) :
                    "audio" === a.kind ? (this.pc._statsFilter.setIsFirstAudioDecoded(!1), this.user._audioTrack = new od(a, this.getUserId(), this.user._uintid), k.info(l(e = "[".concat(this.connectionId, "] create remote audio track: ")).call(e, this.user._audioTrack.getTrackId())), this.bindTrackEvents(this.user._audioTrack)) : (this.user._videoTrack = new nd(a, this.getUserId(), this.user._uintid), k.info(l(g = "[".concat(this.connectionId, "] create remote video track: ")).call(g, this.user._videoTrack.getTrackId())), this.bindTrackEvents(this.user._videoTrack))
            };
            this.handleGetRemoteAudioStats = a => {
                a(this.statsCollector.getRemoteAudioTrackStats(this.connectionId))
            };
            this.handleGetRemoteVideoStats = a => {
                a(this.statsCollector.getRemoteVideoTrackStats(this.connectionId))
            };
            this.handleGetSessionID = a => {
                a(this.joinInfo.sid)
            };
            this.user = a;
            this.statsCollector = b;
            this.statsCollector.addRemoteConnection(this);
            this.subscribeOptions = e
        }

        async startP2PConnection() {
            return new v(async (a, b) => {
                let c = e => {
                    if ("connected" === e && (t.subscribe(this.joinInfo.sid, {
                        lts: this.startTime,
                        succ: !0,
                        video: this.subscribeOptions.video,
                        audio: this.subscribeOptions.audio,
                        peerid: this.user.uid,
                        ec: null,
                        subscribeRequestid: this.ID,
                        p2pid: this.pc.ID
                    }), this.off(I.CONNECTION_STATE_CHANGE, c), a()), "disconnected" === e) {
                        if (this.off(I.CONNECTION_STATE_CHANGE, c), this.disconnectedReason) return b(this.disconnectedReason);
                        b(new p(n.OPERATION_ABORTED, "subscribe abort"))
                    }
                };
                if (this.on(I.CONNECTION_STATE_CHANGE, c), this.disconnectedReason = void 0, this.connectionState = "connecting", this.startTime = x(), !this.subscribeOptions) return void b(new p(n.UNEXPECTED_ERROR,
                    "no subscribe options"));
                let e = new MediaStream, g = new v(a => {
                    this.pc.onTrack = (b, c) => {
                        var g, h;
                        if ("audio" === b.kind && !this.subscribeOptions.audio || "video" === b.kind && !this.subscribeOptions.video) return this.unusedTracks.push(b), void k.debug(l(h = "[".concat(this.connectionId, "] unused ontrack event ")).call(h, b.kind));
                        e.addTrack(b);
                        h = {audio: 0 < e.getAudioTracks().length, video: 0 < e.getVideoTracks().length};
                        k.debug(l(g = "[".concat(this.connectionId, "] subscribe ontrack: ")).call(g, b.kind), c, b);
                        a:{
                            b = this.subscribeOptions;
                            var m, n;
                            c = ed(m = aa(h)).call(m);
                            m = ed(n = aa(b)).call(n);
                            for (n = 0; n < c.length; n += 1) {
                                if (c[n] !== m[n]) {
                                    h = !1;
                                    break a
                                }
                                if (h[c[n]] !== b[c[n]]) {
                                    h = !1;
                                    break a
                                }
                            }
                            h = !0
                        }
                        h && (this.pc.onTrack = this.onTrack, k.debug("[".concat(this.connectionId, "] get all subscribed tracks")), a(e))
                    }
                });
                try {
                    let a = function (a) {
                        return ka().name !== Z.FIREFOX ? a : a.replace("/recvonly http://www.webrtc.org/experiments/rtp-hdrext/playout-delay", " http://www.webrtc.org/experiments/rtp-hdrext/playout-delay")
                    }(Hh(await this.pc.createOfferSDP()));
                    await this.pc.setOfferSDP(a);
                    k.debug("[".concat(this.connectionId, "] create and set offer success"));
                    let b = await Ma(this, I.NEED_ANSWER, {
                        messageType: "OFFER",
                        sdp: a,
                        offererSessionId: 104,
                        retry: !0
                    });
                    await this.pc.setAnswerSDP(Hh(b.sdp));
                    k.debug("[".concat(this.connectionId, "] set answer success"));
                    let c = await v.all([g, this.icePromise]), e = c[0].getAudioTracks()[0],
                        n = c[0].getVideoTracks()[0];
                    var h, m;
                    e && (this.user._audioTrack ? this.user._audioTrack._updateOriginMediaStreamTrack(e) : (this.user._audioTrack = new od(e, this.getUserId(), this.user._uintid),
                        k.info(l(h = "[".concat(this.connectionId, "] create remote audio track: ")).call(h, this.user._audioTrack.getTrackId())), this.bindTrackEvents(this.user._audioTrack)));
                    n && (this.user._videoTrack ? this.user._videoTrack._updateOriginMediaStreamTrack(n) : (this.user._videoTrack = new nd(n, this.getUserId(), this.user._uintid), k.info(l(m = "[".concat(this.connectionId, "] create remote video track: ")).call(m, this.user._videoTrack.getTrackId())), this.bindTrackEvents(this.user._videoTrack)));
                    this.connectionState = "connected";
                    this.startUploadStats()
                } catch (r) {
                    this.off(I.CONNECTION_STATE_CHANGE, c), this.connectionState = "disconnected", t.subscribe(this.joinInfo.sid, {
                        lts: this.startTime,
                        succ: !1,
                        video: this.subscribeOptions.video,
                        audio: this.subscribeOptions.audio,
                        peerid: this.user.uid,
                        ec: r.code,
                        subscribeRequestid: this.ID,
                        p2pid: this.pc.ID
                    }), b(r)
                }
            })
        }

        async closeP2PConnection(a) {
            "disconnected" !== this.connectionState && (this.stopUploadStats(), this.statsCollector.removeConnection(this.connectionId), this.connectionState = "disconnected",
                await this.setSubscribeOptions({
                    audio: !1,
                    video: !1
                }), await this.closePC(a), this.removeAllListeners())
        }

        getNetworkQuality() {
            var a = this.pc.getStats();
            if (!a.audioRecv[0] && !a.videoRecv[0]) return 1;
            var b = Xb(this, I.NEED_SIGNAL_RTT), c = a.rtt;
            b = (c && b ? (c + b) / 2 : c || b) || 0;
            c = a.audioRecv[0] ? a.audioRecv[0].jitterMs : void 0;
            a = a.recvPacketLossRate;
            let e = 70 * a / 50 + .3 * b / 1500;
            c && (e = 60 * a / 50 + .2 * b / 1500 + .2 * c / 400);
            return .1 > e ? 1 : .17 > e ? 2 : .36 > e ? 3 : .59 > e ? 4 : 5
        }

        uploadStats(a) {
            let b = this.user.audioTrack ? function (a, b) {
                const c = a.audioRecv[0];
                if (!c) return null;
                a = {
                    id: pa(10, ""),
                    timestamp: (new Date(a.timestamp)).toISOString(),
                    mediaType: "audio",
                    type: "ssrc",
                    ssrc: c.ssrc.toString()
                };
                return a.bytesReceived = c.bytes.toString(), a.packetsLost = c.packetsLost.toString(), a.packetsReceived = c.packets.toString(), c.outputLevel ? a.A_aol = Math.round(100 * c.outputLevel).toString() : a.A_aol = Math.round(100 * b._source.getAudioAvgLevel()).toString(), a.A_apol = Math.round(100 * b._source.getAudioAvgLevel()).toString(), b && (a.A_artd = b._originMediaStreamTrack.enabled && b._mediaStreamTrack.enabled ?
                    "0" : "1"), a.A_jr = c.jitterMs.toString(), a.A_jbm = Math.floor(c.jitterBufferMs).toString(), a.A_cdm = Math.floor(c.jitterBufferMs).toString(), a
            }(a, this.user.audioTrack) : void 0, c = this.user.videoTrack ? function (a, b) {
                const c = a.videoRecv[0];
                if (!c) return null;
                a = {
                    id: pa(10, ""),
                    timestamp: (new Date(a.timestamp)).toISOString(),
                    mediaType: "video",
                    type: "ssrc",
                    ssrc: c.ssrc.toString()
                };
                var e;
                return a.bytesReceived = c.bytes.toString(), a.packetsLost = c.packetsLost.toString(), a.packetsReceived = c.packets.toString(), c.framesRateFirefox &&
                (a.A_frr = c.framesRateFirefox.toString()), c.receivedFrame && (a.A_frr = c.receivedFrame.frameRate.toString()), a.A_frd = c.decodeFrameRate.toString(), c.outputFrame && (a.A_fro = c.outputFrame.frameRate.toString()), void 0 !== c.jitterBufferMs && (a.A_jbm = Math.floor(c.jitterBufferMs).toString()), void 0 !== c.currentDelayMs && (a.A_cdm = Math.floor(c.currentDelayMs).toString()), a.A_fs = c.firsCount.toString(), a.A_ns = c.nacksCount.toString(), a.A_ps = c.plisCount.toString(), b && (a.A_vrtd = b._originMediaStreamTrack.enabled && b._mediaStreamTrack.enabled ?
                    "0" : "1"), b._player && 0 < b._player.freezeTimeCounterList.length && (a.A_vrft = Ia(e = b._player.freezeTimeCounterList).call(e, 0, 1)[0].toString()), a
            }(a, this.user.videoTrack) : void 0;
            b && ab(() => this.emit(I.NEED_UPLOAD, sb.SUBSCRIBE_STATS, b));
            c && ab(() => this.emit(I.NEED_UPLOAD, sb.SUBSCRIBE_STATS, c))
        }

        uploadSlowStats(a) {
        }

        uploadRelatedStats(a, b) {
            let c = !0 === this.pc._statsFilter.videoIsReady, e = function (a, b, c) {
                a = a.audioRecv[0];
                if (!a) return null;
                c = ld.isRemoteAudioFreeze(c);
                return {
                    mediaType: "audio",
                    isAudioMute: !1,
                    peerId: b,
                    googJitterReceived: a.jitterMs.toString(),
                    isFreeze: c,
                    bytesReceived: a.bytes.toString(),
                    packetsReceived: a.packets.toString(),
                    packetsLost: a.packetsLost.toString(),
                    frameReceived: a.receivedFrames.toString(),
                    frameDropped: a.droppedFrames.toString()
                }
            }(a, this.getUserId(), this.user.audioTrack), g = function (a, b, c, e, g) {
                b = b.videoRecv[0];
                if (!b) return null;
                a = ld.isRemoteVideoFreeze(g, b, e ? e.videoRecv[0] : void 0) && a;
                c = {
                    mediaType: "video",
                    isVideoMute: !1,
                    peerId: c,
                    frameRateReceived: b.receivedFrame && b.receivedFrame.frameRate.toString(),
                    frameRateDecoded: b.decodedFrame && b.decodedFrame.frameRate.toString(),
                    isFreeze: a,
                    bytesReceived: b.bytes.toString(),
                    packetsReceived: b.packets.toString(),
                    packetsLost: b.packetsLost.toString()
                };
                return b.framesRateFirefox && (c.frameRateDecoded = b.framesRateFirefox.toString(), c.frameRateReceived = b.framesRateFirefox.toString()), c
            }(c, a, this.getUserId(), b, this.user.videoTrack);
            e && ab(() => {
                this.emit(I.NEED_UPLOAD, sb.SUBSCRIBE_RELATED_STATS, e)
            });
            g && ab(() => {
                this.emit(I.NEED_UPLOAD, sb.SUBSCRIBE_RELATED_STATS, g)
            })
        }

        emitOnTrackFromUnusedTracks() {
            if (this.subscribeOptions) {
                var a =
                    this.subscribeOptions.video;
                if (this.subscribeOptions.audio) {
                    var b;
                    let a = R(b = this.unusedTracks).call(b, a => "audio" === a.kind && "live" === a.readyState);
                    Oc(this.unusedTracks, a);
                    a && this.onTrack(a)
                }
                if (a) {
                    var c;
                    a = R(c = this.unusedTracks).call(c, a => "video" === a.kind && "live" === a.readyState);
                    Oc(this.unusedTracks, a);
                    a && this.onTrack(a)
                }
            }
        }

        async setSubscribeOptions(a) {
            var b, c, e, g;
            if (a.audio !== this.subscribeOptions.audio || a.video !== this.subscribeOptions.video) {
                if ("connecting" === this.connectionState) try {
                    await this.createWaitConnectionConnectedPromise()
                } catch (h) {
                    throw new p(n.OPERATION_ABORTED,
                        "can not update subscribe options, operation abort");
                }
                a.audio === this.subscribeOptions.audio && a.video === this.subscribeOptions.video || (k.debug(l(b = l(c = l(e = l(g = "[".concat(this.connectionId, "] update subscribe options [a: ")).call(g, this.subscribeOptions.audio, ", v: ")).call(e, this.subscribeOptions.video, "] -> [a: ")).call(c, a.audio, ", v: ")).call(b, a.video, "]")), this.subscribeOptions = a, !a.audio && this.user._audioTrack && (this.unusedTracks.push(this.user._audioTrack._originMediaStreamTrack), this.user._audioTrack._destroy(),
                    this.unbindTrackEvents(this.user._audioTrack), this.user._audioTrack = void 0), !a.video && this.user._videoTrack && (this.unusedTracks.push(this.user._videoTrack._originMediaStreamTrack), this.user._videoTrack._destroy(), this.unbindTrackEvents(this.user._videoTrack), this.user._videoTrack = void 0), this.emitOnTrackFromUnusedTracks())
            }
        }

        createPC() {
            this.pc = new Dk({turnServer: this.joinInfo.turnServer});
            this.pc.onFirstAudioDecoded = () => {
                this.user.audioTrack && this.user.audioTrack.emit(hd.FIRST_FRAME_DECODED);
                t.firstRemoteFrame(this.joinInfo.sid,
                    Da.FIRST_AUDIO_DECODE, ma.FIRST_AUDIO_DECODE, {
                        peer: this.user._uintid,
                        subscribeElapse: x() - this.startTime,
                        subscribeRequestid: this.ID,
                        p2pid: this.pc.ID
                    })
            };
            this.pc.onFirstAudioReceived = () => {
                t.firstRemoteFrame(this.joinInfo.sid, Da.FIRST_AUDIO_RECEIVED, ma.FIRST_AUDIO_RECEIVED, {
                    peer: this.user._uintid,
                    subscribeElapse: x() - this.startTime,
                    subscribeRequestid: this.ID,
                    p2pid: this.pc.ID
                })
            };
            this.pc.onFirstVideoDecoded = (a, b) => {
                t.firstRemoteFrame(this.joinInfo.sid, Da.FIRST_VIDEO_DECODE, ma.FIRST_VIDEO_DECODE, {
                    peer: this.user._uintid,
                    videowidth: a,
                    videoheight: b,
                    subscribeElapse: x() - this.startTime,
                    subscribeRequestid: this.ID,
                    p2pid: this.pc.ID
                })
            };
            this.pc.onFirstVideoReceived = () => {
                t.firstRemoteFrame(this.joinInfo.sid, Da.FIRST_VIDEO_RECEIVED, ma.FIRST_VIDEO_RECEIVED, {
                    peer: this.user._uintid,
                    subscribeElapse: x() - this.startTime,
                    subscribeRequestid: this.ID,
                    p2pid: this.pc.ID
                })
            };
            this.updateICEPromise()
        }

        async closePC(a) {
            return (this.pc.audioTrack && this.pc.audioTrack.stop(), this.pc.videoTrack && this.pc.videoTrack.stop(), this.pc.onTrack = void 0, this.pc.onICEConnectionStateChange =
                void 0, this.pc.close(), a) ? !1 : await Ma(this, I.NEED_UNSUB)
        }

        onPCDisconnected(a) {
            t.subscribe(this.joinInfo.sid, {
                lts: this.startTime,
                succ: !1,
                video: this.subscribeOptions.video,
                audio: this.subscribeOptions.audio,
                peerid: this.user.uid,
                ec: a.code,
                subscribeRequestid: this.ID,
                p2pid: this.pc.ID
            })
        }

        bindTrackEvents(a) {
            a instanceof od ? a.addListener(L.GET_STATS, this.handleGetRemoteAudioStats) : a instanceof nd && a.addListener(L.GET_STATS, this.handleGetRemoteVideoStats)
        }

        unbindTrackEvents(a) {
            a instanceof od ? a.off(L.GET_STATS,
                this.handleGetRemoteAudioStats) : a instanceof nd && a.off(L.GET_STATS, this.handleGetRemoteVideoStats)
        }
    }

    class Do extends Ta {
        constructor(a, b, c, e) {
            super();
            this.reconnectMode = "retry";
            this.commandReqId = this.reqId = 0;
            this.handleWebSocketOpen = () => {
                this.reconnectMode = "retry";
                this.startPingPong()
            };
            this.handleWebSocketMessage = a => {
                if (a.data) {
                    a = JSON.parse(a.data);
                    var b;
                    a.requestId ? this.emit(l(b = "@".concat(a.requestId, "-")).call(b, a.sid), a) : this.serviceMode === na.INJECT ? this.emit(kb.INJECT_STREAM_STATUS, a) : (t.workerEvent(this.spec.sid,
                        {
                            actionType: "status",
                            serverCode: a.code,
                            workerType: this.serviceMode === na.TRANSCODE ? 1 : 2
                        }), this.emit(kb.PUBLISH_STREAM_STATUS, a))
                }
            };
            this.spec = b;
            this.token = a;
            this.serviceMode = e;
            this.websocket = new Vf("live-streaming", c);
            this.websocket.on(S.CONNECTED, this.handleWebSocketOpen);
            this.websocket.on(S.ON_MESSAGE, this.handleWebSocketMessage);
            this.websocket.on(S.REQUEST_NEW_URLS, (a, b) => {
                Ma(this, kb.REQUEST_NEW_ADDRESS).then(a).catch(b)
            });
            this.websocket.on(S.RECONNECTING, () => {
                this.websocket.reconnectMode = this.reconnectMode
            })
        }

        init(a) {
            return this.websocket.init(a)
        }

        async request(a,
                      b, c, e) {
            this.reqId += 1;
            "request" === a && (this.commandReqId += 1);
            let g = this.commandReqId, h = this.reqId;
            if (!h || !this.websocket) throw new p(n.UNEXPECTED_ERROR);
            var k = Se({
                command: a,
                sdkVersion: "4.4.0" === Va ? "0.0.1" : Va,
                seq: h,
                requestId: h,
                allocate: c,
                cname: this.spec.cname,
                appId: this.spec.appId,
                sid: this.spec.sid,
                uid: this.spec.uid.toString(),
                ts: Math.floor(x() / 1E3)
            }, b);
            if ("closed" === this.websocket.state) throw new p(n.WS_DISCONNECT);
            let r = () => new v((a, b) => {
                this.websocket.once(S.CLOSED, () => b(new p(n.WS_ABORT)));
                this.websocket.once(S.CONNECTED,
                    a)
            });
            "connected" !== this.websocket.state && await r();
            k.clientRequest && (k.clientRequest.workerToken = this.token);
            let w = new v((a, b) => {
                var c;
                const e = () => {
                    b(new p(n.WS_ABORT))
                };
                this.websocket.once(S.RECONNECTING, e);
                this.websocket.once(S.CLOSED, e);
                this.once(l(c = "@".concat(h, "-")).call(c, this.spec.sid), b => {
                    a(b)
                })
            });
            e && t.workerEvent(this.spec.sid, Se({}, e, {
                requestId: g,
                actionType: "request",
                payload: A(b.clientRequest),
                serverCode: 0,
                code: 0
            }));
            let q = x();
            this.websocket.sendMessage(k);
            k = null;
            try {
                k = await w
            } catch (sa) {
                if ("closed" ===
                    this.websocket.state) throw sa;
                return await r(), await this.request(a, b, c)
            }
            return e && t.workerEvent(this.spec.sid, Se({}, e, {
                requestId: g,
                actionType: "response",
                payload: A(k.serverResponse),
                serverCode: k.code,
                success: 200 === k.code,
                responseTime: x() - q
            })), 200 !== k.code && this.handleResponseError(k), k
        }

        tryNextAddress() {
            this.reconnectMode = "tryNext";
            this.websocket.reconnect("tryNext")
        }

        close() {
            let a = "4.4.0" === Va ? "0.0.1" : Va;
            this.reqId += 1;
            "connected" === this.websocket.state ? (this.websocket.sendMessage({
                command: "request",
                appId: this.spec.appId,
                cname: this.spec.cname,
                uid: this.spec.uid.toString(),
                sdkVersion: a,
                sid: this.spec.sid,
                seq: this.reqId,
                ts: Math.floor(x() / 1E3),
                requestId: this.reqId,
                clientRequest: {command: "DestroyWorker"}
            }), this.websocket.close(!1, !0)) : this.websocket.close(!1);
            this.pingpongTimer && (window.clearInterval(this.pingpongTimer), this.pingpongTimer = void 0)
        }

        handleResponseError(a) {
            switch (a.code) {
                case ia.LIVE_STREAM_RESPONSE_ALREADY_EXISTS_STREAM:
                    return void k.warning("live stream response already exists stream");
                case ia.LIVE_STREAM_RESPONSE_TRANSCODING_PARAMETER_ERROR:
                case ia.LIVE_STREAM_RESPONSE_BAD_STREAM:
                case ia.LIVE_STREAM_RESPONSE_WM_PARAMETER_ERROR:
                    return (new p(n.LIVE_STREAMING_INVALID_ARGUMENT, "", {code: a.code})).throw();
                case ia.LIVE_STREAM_RESPONSE_WM_WORKER_NOT_EXIST:
                    if ("UnpublishStream" === a.serverResponse.command || "UninjectStream" === a.serverResponse.command) break;
                    throw new p(n.LIVE_STREAMING_INTERNAL_SERVER_ERROR, "live stream response wm worker not exist", {retry: !0});
                case ia.LIVE_STREAM_RESPONSE_NOT_AUTHORIZED:
                    return (new p(n.LIVE_STREAMING_PUBLISH_STREAM_NOT_AUTHORIZED,
                        "", {code: a.code})).throw();
                case ia.LIVE_STREAM_RESPONSE_FAILED_LOAD_IMAGE:
                    var b = new p(n.LIVE_STREAMING_WARN_FAILED_LOAD_IMAGE);
                    return this.emit(kb.WARNING, b, a.serverResponse.url);
                case ia.LIVE_STREAM_RESPONSE_REQUEST_TOO_OFTEN:
                    return b = new p(n.LIVE_STREAMING_WARN_FREQUENT_REQUEST), this.emit(kb.WARNING, b, a.serverResponse.url);
                case ia.LIVE_STREAM_RESPONSE_NOT_FOUND_PUBLISH:
                    throw new p(n.LIVE_STREAMING_INTERNAL_SERVER_ERROR, "live stream response wm worker not exist", {retry: !0});
                case ia.LIVE_STREAM_RESPONSE_NOT_SUPPORTED:
                    return (new p(n.LIVE_STREAMING_TRANSCODING_NOT_SUPPORTED,
                        "", {code: a.code})).throw();
                case ia.LIVE_STREAM_RESPONSE_MAX_STREAM_NUM:
                    return b = new p(n.LIVE_STREAMING_WARN_STREAM_NUM_REACH_LIMIT), this.emit(kb.WARNING, b, a.serverResponse.url);
                case ia.LIVE_STREAM_RESPONSE_INTERNAL_SERVER_ERROR:
                    return (new p(n.LIVE_STREAMING_INTERNAL_SERVER_ERROR, "", {code: a.code})).throw();
                case ia.LIVE_STREAM_RESPONSE_RESOURCE_LIMIT:
                    throw new p(n.LIVE_STREAMING_INTERNAL_SERVER_ERROR, "live stream resource limit", {
                        retry: !0,
                        changeAddress: !0
                    });
                case ia.LIVE_STREAM_RESPONSE_WORKER_LOST:
                case ia.LIVE_STREAM_RESPONSE_WORKER_QUIT:
                    if ("UnpublishStream" ===
                        a.serverResponse.command || "UninjectStream" === a.serverResponse.command) break;
                    throw new p(n.LIVE_STREAMING_INTERNAL_SERVER_ERROR, "error fail send message", {
                        retry: !0,
                        changeAddress: !0
                    });
                case ia.ERROR_FAIL_SEND_MESSAGE:
                    if ("UnpublishStream" === a.serverResponse.command || "UninjectStream" === a.serverResponse.command) break;
                    if ("UpdateTranscoding" === a.serverResponse.command || "ControlStream" === a.serverResponse.command) return (new p(n.LIVE_STREAMING_INTERNAL_SERVER_ERROR, "error fail send message", {code: a.code})).throw();
                    throw new p(n.LIVE_STREAMING_INTERNAL_SERVER_ERROR, "error fail send message", {
                        retry: !0,
                        changeAddress: !0
                    });
                case ia.PUBLISH_STREAM_STATUS_ERROR_PUBLISH_BROKEN:
                case ia.PUBLISH_STREAM_STATUS_ERROR_RTMP_CONNECT:
                case ia.PUBLISH_STREAM_STATUS_ERROR_RTMP_HANDSHAKE:
                case ia.PUBLISH_STREAM_STATUS_ERROR_RTMP_PUBLISH:
                    return (new p(n.LIVE_STREAMING_CDN_ERROR, "", {code: a.code})).throw()
            }
        }

        startPingPong() {
            this.pingpongTimer && window.clearInterval(this.pingpongTimer);
            this.pingpongTimer = window.setInterval(() => {
                "connected" ===
                this.websocket.state && this.request("ping", {}).catch(Uf)
            }, 6E3)
        }
    }

    class Eo extends Ta {
        constructor(a, b = Ra, c = Ra) {
            super();
            this.retryTimeout = 1E4;
            this.streamingTasks = new ba;
            this.isStartingStreamingTask = !1;
            this.taskMutex = new Ob("live-streaming");
            this.cancelToken = Bb.CancelToken.source();
            this.injectConfig = Rb({}, On);
            this.injectLoopTimes = 0;
            this.lastTaskId = 1;
            this.statusError = new ba;
            this.spec = a;
            this.httpRetryConfig = c;
            this.wsRetryConfig = b
        }

        async setTranscodingConfig(a) {
            var b;
            let c = Rb({}, Nn, {}, a);
            var e, g;
            66 !== c.videoCodecProfile &&
            77 !== c.videoCodecProfile && 100 !== c.videoCodecProfile && (k.debug(l(e = "[".concat(this.spec.clientId, "] set transcoding config, fix video codec profile: ")).call(e, c.videoCodecProfile, " -> 100")), c.videoCodecProfile = 100);
            (c.transcodingUsers || (c.transcodingUsers = c.userConfigs), c.transcodingUsers) && (c.transcodingUsers = C(g = c.transcodingUsers).call(g, a => Rb({}, Mn, {}, a, {zOrder: a.zOrder ? a.zOrder + 1 : 1})));
            !function (a) {
                var b, c;
                null == a.width || V(a.width, "config.width", 0, 1E4);
                null == a.height || V(a.height, "config.height",
                    0, 1E4);
                null == a.videoBitrate || V(a.videoBitrate, "config.videoBitrate", 1, 1E6);
                null == a.videoFrameRate || V(a.videoFrameRate, "config.videoFrameRate");
                null == a.lowLatency || ve(a.lowLatency, "config.lowLatency");
                null == a.audioSampleRate || Ka(a.audioSampleRate, "config.audioSampleRate", [32E3, 44100, 48E3]);
                null == a.audioBitrate || V(a.audioBitrate, "config.audioBitrate", 1, 128);
                null == a.audioChannels || Ka(a.audioChannels, "config.audioChannels", [1, 2, 3, 4, 5]);
                null == a.videoGop || V(a.videoGop, "config.videoGop");
                null == a.videoCodecProfile ||
                Ka(a.videoCodecProfile, "config.videoCodecProfile", [66, 77, 100]);
                null == a.userCount || V(a.userCount, "config.userCount", 0, 17);
                null == a.backgroundColor || V(a.backgroundColor, "config.backgroundColor", 0, 16777215);
                null == a.userConfigExtraInfo || La(a.userConfigExtraInfo, "config.userConfigExtraInfo", 0, 4096, !1);
                a.transcodingUsers && null != a.transcodingUsers && (Wg(a.transcodingUsers, "config.transcodingUsers"), q(b = a.transcodingUsers).call(b, (a, b) => {
                    xe(a.uid);
                    null == a.x || V(a.x, "transcodingUser[".concat(b, "].x"), 0, 1E4);
                    null == a.y || V(a.y, "transcodingUser[".concat(b, "].y"), 0, 1E4);
                    null == a.width || V(a.width, "transcodingUser[".concat(b, "].width"), 0, 1E4);
                    null == a.height || V(a.height, "transcodingUser[".concat(b, "].height"), 0, 1E4);
                    null == a.zOrder || V(a.zOrder - 1, "transcodingUser[".concat(b, "].zOrder"), 0, 100);
                    null == a.alpha || V(a.alpha, "transcodingUser[".concat(b, "].alpha"), 0, 1, !1)
                }));
                null == a.watermark || ze(a.watermark, "watermark");
                null == a.backgroundImage || ze(a.backgroundImage, "backgroundImage");
                a.images && null != a.images && (Wg(a.images,
                    "config.images"), q(c = a.images).call(c, (a, b) => {
                    ze(a, "images[".concat(b, "]"))
                }))
            }(c);
            a = [];
            var h, m;
            c.images && a.push(...C(h = c.images).call(h, a => Rb({}, Lf, {}, a, {zOrder: 255})));
            (c.backgroundImage && (a.push(Rb({}, Lf, {}, c.backgroundImage, {zOrder: 0})), delete c.backgroundImage), c.watermark && (a.push(Rb({}, Lf, {}, c.watermark, {zOrder: 255})), delete c.watermark), c.images = a, c.transcodingUsers) && (c.userConfigs = C(m = c.transcodingUsers).call(m, a => Rb({}, a)), c.userCount = c.transcodingUsers.length, delete c.transcodingUsers);
            h = C(b = c.userConfigs || []).call(b, a => "number" == typeof a.uid ? v.resolve(a.uid) : zh(a.uid, this.spec, this.cancelToken.token, this.httpRetryConfig));
            b = await v.all(h);
            if (q(b).call(b, (a, b) => {
                c.userConfigs && c.userConfigs[b] && (c.userConfigs[b].uid = a)
            }), this.transcodingConfig = c, this.connection) try {
                var n, p, y;
                let a = await this.connection.request("request", {
                    clientRequest: {
                        command: "UpdateTranscoding",
                        transcodingConfig: this.transcodingConfig
                    }
                }, !1, {
                    command: "UpdateTranscoding", workerType: 1, requestByUser: !0, tid: C(n = Ib(gc(p =
                        this.streamingTasks).call(p))).call(n, a => a.taskId).join("#")
                });
                k.debug(l(y = "[".concat(this.spec.clientId, "] update live transcoding config success, code: ")).call(y, a.code, ", config:"), A(this.transcodingConfig))
            } catch (B) {
                var t;
                if (!B.data || !B.data.retry) throw B;
                B.data.changeAddress && this.connection.tryNextAddress();
                q(t = this.streamingTasks).call(t, a => {
                    k.warning("[".concat(this.spec.clientId, "] live streaming receive error"), B.toString(), "try to republish", a.url);
                    this.startLiveStreamingTask(a.url, a.mode,
                        B).then(() => {
                        var b;
                        k.debug(l(b = "[".concat(this.spec.clientId, "] live streaming republish ")).call(b, a.url, " success"))
                    }).catch(b => {
                        k.error("[".concat(this.spec.clientId, "] live streaming republish failed"), a.url, b.toString());
                        this.onLiveStreamError && this.onLiveStreamError(a.url, b)
                    })
                })
            }
        }

        setInjectStreamConfig(a, b) {
            this.injectConfig = Ha({}, this.injectConfig, a);
            this.injectLoopTimes = b
        }

        async startLiveStreamingTask(a, b, c) {
            var e, g, h, m;
            if (R(e = Ib(gc(g = this.streamingTasks).call(g))).call(e, a => a.mode === na.INJECT) &&
                b === na.INJECT) return (new p(n.LIVE_STREAMING_TASK_CONFLICT, "inject stream over limit")).throw();
            if (!this.transcodingConfig && b === na.TRANSCODE) throw new p(n.INVALID_OPERATION, "[LiveStreaming] no transcoding config found, can not start transcoding streaming task");
            e = {
                command: "PublishStream",
                ts: x(),
                url: a,
                uid: this.spec.uid.toString(),
                autoDestroyTime: 100,
                acceptImageTimeout: !0
            };
            k.debug(l(h = l(m = "[".concat(this.spec.clientId, "] start live streaming ")).call(m, a, ", mode: ")).call(h, b));
            h = await this.taskMutex.lock();
            if (!this.connection && c) return void h();
            if (this.streamingTasks.get(a) && !c) return h(), (new p(n.LIVE_STREAMING_TASK_CONFLICT)).throw();
            try {
                this.connection || (this.connection = await this.connect(b))
            } catch (w) {
                throw h(), w;
            }
            switch (b) {
                case na.TRANSCODE:
                    e.transcodingConfig = Rb({}, this.transcodingConfig);
                    break;
                case na.INJECT:
                    e = {
                        cname: this.spec.cname,
                        command: "InjectStream",
                        sid: this.spec.sid,
                        transcodingConfig: this.injectConfig,
                        ts: x(),
                        url: a,
                        loopTimes: this.injectLoopTimes
                    }
            }
            this.uapResponse && this.uapResponse.vid &&
            (e.vid = this.uapResponse.vid);
            this.isStartingStreamingTask = !0;
            m = this.lastTaskId++;
            try {
                var r;
                let g = new v((b, e) => {
                    yb(this.retryTimeout).then(() => {
                        if (c) return e(c);
                        const b = this.statusError.get(a);
                        return b ? (this.statusError.delete(a), e(b)) : void 0
                    })
                }), n = await v.race([this.connection.request("request", {clientRequest: e}, !0, {
                    url: a,
                    command: "PublishStream",
                    workerType: b === na.TRANSCODE ? 1 : 2,
                    requestByUser: !c,
                    tid: m.toString()
                }), g]);
                this.isStartingStreamingTask = !1;
                k.debug(l(r = "[".concat(this.spec.clientId, "] live streaming started, code: ")).call(r,
                    n.code));
                this.streamingTasks.set(a, {clientRequest: e, mode: b, url: a, taskId: m});
                h()
            } catch (w) {
                if (h(), this.isStartingStreamingTask = !1, !w.data || !w.data.retry || c) throw w;
                return w.data.changeAddress ? (this.connection.tryNextAddress(), await this.startLiveStreamingTask(a, b, w)) : await this.startLiveStreamingTask(a, b, w)
            }
        }

        stopLiveStreamingTask(a) {
            return new v((b, c) => {
                let e = this.streamingTasks.get(a);
                if (!e || !this.connection) return (new p(n.UNEXPECTED_ERROR, "can not find streaming task to stop")).throw();
                let g = e.mode;
                e.abortTask = () => {
                    k.debug("[".concat(this.spec.clientId, "] stop live streaming success(worker exception)"));
                    this.streamingTasks.delete(a);
                    b()
                };
                this.connection.request("request", {
                    clientRequest: {
                        command: g === na.INJECT ? "UninjectStream" : "UnpublishStream",
                        url: e.url
                    }
                }, !1, {
                    url: a,
                    command: "UnPublishStream",
                    workerType: g === na.TRANSCODE ? 1 : 2,
                    requestByUser: !0,
                    tid: (this.lastTaskId++).toString()
                }).then(c => {
                    var e;
                    k.debug(l(e = "[".concat(this.spec.clientId, "] stop live streaming success, code: ")).call(e, c.code));
                    this.streamingTasks.delete(a);
                    0 === this.streamingTasks.size && g !== na.INJECT && (this.connection && this.connection.close(), this.connection = void 0);
                    b();
                    g === na.INJECT && this.onInjectStatusChange && this.onInjectStatusChange(5, this.spec.uid, a)
                }).catch(c)
            })
        }

        async controlInjectStream(a, b, c, e) {
            let g = this.streamingTasks.get(a);
            if (!g || !this.connection || g.mode !== na.INJECT) throw new p(n.INVALID_OPERATION, "can not find inject stream task to control");
            return (await this.connection.request("request", {
                clientRequest: {
                    command: "ControlStream", url: a, control: b,
                    audioVolume: c, position: e
                }
            })).serverResponse
        }

        resetAllTask() {
            var a;
            let b = Ib(gc(a = this.streamingTasks).call(a));
            this.terminate();
            for (let a of b) this.startLiveStreamingTask(a.url, a.mode).catch(b => {
                this.onLiveStreamError && this.onLiveStreamError(a.url, b)
            })
        }

        terminate() {
            this.cancelToken && this.cancelToken.cancel();
            this.streamingTasks = new ba;
            this.isStartingStreamingTask = !1;
            this.statusError = new ba;
            this.cancelToken = Bb.CancelToken.source();
            this.uapResponse = void 0;
            this.connection && this.connection.close();
            this.connection =
                void 0
        }

        async connect(a) {
            if (this.connection) throw new p(n.UNEXPECTED_ERROR, "live streaming connection has already connected");
            let b = await Ma(this, Ec.REQUEST_WORKER_MANAGER_LIST, a);
            return this.uapResponse = b, this.connection = new Do(b.workerToken, this.spec, this.wsRetryConfig, a), this.connection.on(kb.WARNING, (a, b) => this.onLiveStreamWarning && this.onLiveStreamWarning(b, a)), this.connection.on(kb.PUBLISH_STREAM_STATUS, a => this.handlePublishStreamServer(a)), this.connection.on(kb.INJECT_STREAM_STATUS, a => this.handleInjectStreamServerStatus(a)),
                this.connection.on(kb.REQUEST_NEW_ADDRESS, (b, e) => {
                    if (!this.connection) return e(new p(n.UNEXPECTED_ERROR, "can not get new live streaming address list"));
                    Ma(this, Ec.REQUEST_WORKER_MANAGER_LIST, a).then(a => {
                        this.uapResponse = a;
                        b(a.addressList)
                    }).catch(e)
                }), await this.connection.init(b.addressList), this.connection
        }

        handlePublishStreamServer(a) {
            var b = a.serverStatus && a.serverStatus.url || "empty_url";
            let c = this.streamingTasks.get(b), e = a.reason;
            switch (a.code) {
                case ia.PUBLISH_STREAM_STATUS_ERROR_PUBLISH_BROKEN:
                case ia.PUBLISH_STREAM_STATUS_ERROR_RTMP_CONNECT:
                case ia.PUBLISH_STREAM_STATUS_ERROR_RTMP_HANDSHAKE:
                case ia.PUBLISH_STREAM_STATUS_ERROR_RTMP_PUBLISH:
                    a =
                        new p(n.LIVE_STREAMING_CDN_ERROR, "", {code: a.code});
                    if (c) return k.error(a.toString()), this.onLiveStreamError && this.onLiveStreamError(b, a);
                    if (!this.isStartingStreamingTask) break;
                    this.statusError.set(b, a);
                case ia.LIVE_STREAM_RESPONSE_FAILED_LOAD_IMAGE:
                    return a = new p(n.LIVE_STREAMING_WARN_FAILED_LOAD_IMAGE, e), this.onLiveStreamWarning && this.onLiveStreamWarning(b, a);
                case ia.LIVE_STREAM_RESPONSE_WORKER_LOST:
                case ia.LIVE_STREAM_RESPONSE_WORKER_QUIT:
                    var g;
                    if (this.connection) {
                        this.connection.tryNextAddress();
                        b = Ib(gc(g = this.streamingTasks).call(g));
                        for (let c of b) c.abortTask ? c.abortTask() : (k.warning("[".concat(this.spec.clientId, "] publish stream status code"), a.code, "try to republish", c.url), this.startLiveStreamingTask(c.url, c.mode, new p(n.LIVE_STREAMING_INTERNAL_SERVER_ERROR, "", {code: a.code})).then(() => {
                            k.debug("[".concat(this.spec.clientId, "] republish live stream success"), c.url)
                        }).catch(a => {
                            k.error(a.toString());
                            this.onLiveStreamError && this.onLiveStreamError(c.url, a)
                        }))
                    }
            }
        }

        handleInjectStreamServerStatus(a) {
            let b =
                Number(a.uid), c = a.serverStatus && a.serverStatus.url;
            switch (a.code) {
                case 200:
                    return void (this.onInjectStatusChange && this.onInjectStatusChange(0, b, c));
                case 451:
                    return this.onInjectStatusChange && this.onInjectStatusChange(1, b, c), void this.streamingTasks.delete(c);
                case 453:
                    return this.onInjectStatusChange && this.onInjectStatusChange(2, b, c), void this.streamingTasks.delete(c);
                case 470:
                    return this.onInjectStatusChange && this.onInjectStatusChange(10, b, c), void this.streamingTasks.delete(c);
                case 499:
                    return this.onInjectStatusChange &&
                    this.onInjectStatusChange(3, b, c), void this.streamingTasks.delete(c);
                default:
                    return void k.debug("inject stream server status", a)
            }
        }

        hasUrl(a) {
            return this.streamingTasks.has(a)
        }
    }

    class Ph {
        constructor() {
            this.destChannelMediaInfos = new ba
        }

        setSrcChannelInfo(a) {
            $g(a);
            this.srcChannelMediaInfo = a
        }

        addDestChannelInfo(a) {
            $g(a);
            this.destChannelMediaInfos.set(a.channelName, a)
        }

        removeDestChannelInfo(a) {
            we(a);
            this.destChannelMediaInfos.delete(a)
        }

        getSrcChannelMediaInfo() {
            return this.srcChannelMediaInfo
        }

        getDestChannelMediaInfo() {
            return this.destChannelMediaInfos
        }
    }

    class Fo extends Ta {
        constructor(a, b, c) {
            super();
            this.requestId = 1;
            this.onOpen = () => {
                this.emit("open");
                this.startHeartBeatCheck()
            };
            this.onClose = a => {
                this.emit("close");
                this.dispose()
            };
            this.onMessage = a => {
                a = JSON.parse(a.data);
                if (!a || "serverResponse" !== a.command || !a.requestId) return a && "serverStatus" === a.command && a.serverStatus && a.serverStatus.command ? (this.emit("status", a.serverStatus), void this.emit(a.serverStatus.command, a.serverStatus)) : void 0;
                this.emit("req_".concat(a.requestId), a)
            };
            this.joinInfo = a;
            this.clientId = b;
            this.ws = new Vf("cross-channel-".concat(this.clientId), c);
            this.ws.on(S.RECONNECTING, () => {
                this.ws.reconnectMode = "retry";
                this.emit("reconnecting")
            });
            this.ws.on(S.CONNECTED, this.onOpen);
            this.ws.on(S.ON_MESSAGE, this.onMessage);
            this.ws.on(S.CLOSED, this.onClose)
        }

        isConnect() {
            return "connected" === this.ws.state
        }

        sendMessage(a) {
            let b = this.requestId++;
            return a.requestId = b, a.seq = b, this.ws.sendMessage(a), b
        }

        waitStatus(a) {
            return new v((b, c) => {
                let e = window.setTimeout(() => {
                        c(new p(n.TIMEOUT, "wait status timeout, status: ".concat(a)))
                    },
                    5E3);
                this.once(a, g => {
                    window.clearTimeout(e);
                    g.state && 0 !== g.state ? c(new p(n.CROSS_CHANNEL_WAIT_STATUS_ERROR, "wait status error, status: ".concat(a))) : b(void 0)
                });
                this.once("dispose", () => {
                    window.clearTimeout(e);
                    c(new p(n.WS_ABORT))
                })
            })
        }

        async request(a) {
            if ("closed" === this.ws.state) throw new p(n.WS_DISCONNECT);
            let b = () => new v((a, b) => {
                this.ws.once(S.CLOSED, () => b(new p(n.WS_ABORT)));
                this.ws.once(S.CONNECTED, a)
            });
            "connected" !== this.ws.state && await b();
            let c = this.sendMessage(a);
            a = await new v((a, b) => {
                const e =
                    () => {
                        b(new p(n.WS_ABORT))
                    };
                this.ws.once(S.RECONNECTING, e);
                this.ws.once(S.CLOSED, e);
                this.once("req_".concat(c), a);
                yb(3E3).then(() => {
                    this.removeAllListeners("req_".concat(c));
                    this.ws.off(S.RECONNECTING, e);
                    this.ws.off(S.CLOSED, e);
                    b(new p(n.TIMEOUT, "cross channel ws request timeout"))
                })
            });
            if (!a || 200 !== a.code) throw new p(n.CROSS_CHANNEL_SERVER_ERROR_RESPONSE, "response: ".concat(A(a)));
            return a
        }

        async connect(a) {
            this.ws.removeAllListeners(S.REQUEST_NEW_URLS);
            this.ws.on(S.REQUEST_NEW_URLS, b => {
                b(a)
            });
            await this.ws.init(a)
        }

        dispose() {
            this.clearHeartBeatCheck();
            this.emit("dispose");
            this.removeAllListeners();
            this.ws.close()
        }

        sendPing(a) {
            let b = this.requestId++;
            return a.requestId = b, this.ws.sendMessage(a), b
        }

        startHeartBeatCheck() {
            this.heartBeatTimer = window.setInterval(() => {
                this.sendPing({
                    command: "ping",
                    appId: this.joinInfo.appId,
                    cname: this.joinInfo.cname,
                    uid: this.joinInfo.uid.toString(),
                    sid: this.joinInfo.sid,
                    ts: +new Date,
                    requestId: 0
                })
            }, 3E3)
        }

        clearHeartBeatCheck() {
            window.clearInterval(this.heartBeatTimer);
            this.heartBeatTimer = void 0
        }
    }

    class Go extends Ta {
        constructor(a,
                    b, c, e) {
            super();
            this.cancelToken = Bb.CancelToken.source();
            this.requestId = 0;
            this._state = "RELAY_STATE_IDLE";
            this.errorCode = "RELAY_OK";
            this.onStatus = a => {
                var b;
                k.debug(l(b = "[".concat(this.clientId, "] ChannelMediaStatus: ")).call(b, A(a)));
                a && a.command && ("onAudioPacketReceived" === a.command && this.emit("event", "PACKET_RECEIVED_AUDIO_FROM_SRC"), "onVideoPacketReceived" === a.command && this.emit("event", "PACKET_RECEIVED_VIDEO_FROM_SRC"), "onSrcTokenPrivilegeDidExpire" === a.command && (this.errorCode = "SRC_TOKEN_EXPIRED",
                    this.state = "RELAY_STATE_FAILURE"), "onDestTokenPrivilegeDidExpire" === a.command && (this.errorCode = "DEST_TOKEN_EXPIRED", this.state = "RELAY_STATE_FAILURE"))
            };
            this.onReconnect = async () => {
                k.debug("[".concat(this.clientId, "] ChannelMediaSocket disconnect, reconnecting"));
                this.emit("event", "NETWORK_DISCONNECTED");
                this.state = "RELAY_STATE_IDLE";
                this.prevChannelMediaConfig && this.sendStartRelayMessage(this.prevChannelMediaConfig).catch(a => {
                    "RELAY_STATE_IDLE" !== this.state && (k.error("auto restart channel media relay failed",
                        a.toString()), this.errorCode = "SERVER_CONNECTION_LOST", this.state = "RELAY_STATE_FAILURE")
                })
            };
            this.joinInfo = a;
            this.clientId = b;
            this.signal = new Fo(this.joinInfo, this.clientId, c);
            this.httpRetryConfig = e
        }

        get state() {
            return this._state
        }

        set state(a) {
            a !== this._state && ("RELAY_STATE_FAILURE" !== a && (this.errorCode = "RELAY_OK"), this.emit("state", a, this.errorCode), this._state = a)
        }

        async startChannelMediaRelay(a) {
            if ("RELAY_STATE_IDLE" !== this.state) throw new p(n.INVALID_OPERATION);
            this.state = "RELAY_STATE_CONNECTING";
            await this.connect();
            k.debug("[".concat(this.clientId, "] startChannelMediaRelay: connect success"));
            try {
                await this.sendStartRelayMessage(a)
            } catch (b) {
                if (b.data && b.data.serverResponse && "SetSourceChannel" === b.data.serverResponse.command) throw new p(n.CROSS_CHANNEL_FAILED_JOIN_SRC);
                if (b.data && b.data.serverResponse && "SetDestChannelStatus" === b.serverResponse.command) throw new p(n.CROSS_CHANNEL_FAILED_JOIN_DEST);
                if (b.data && b.data.serverResponse && "StartPacketTransfer" === b.serverResponse.command) throw new p(n.CROSS_CHANNEL_FAILED_PACKET_SENT_TO_DEST);
                throw b;
            }
            this.prevChannelMediaConfig = a
        }

        async updateChannelMediaRelay(a) {
            if ("RELAY_STATE_RUNNING" !== this.state) throw new p(n.INVALID_OPERATION);
            await this.sendUpdateMessage(a);
            this.prevChannelMediaConfig = a
        }

        async stopChannelMediaRelay() {
            await this.sendStopRelayMessage();
            k.debug("[".concat(this.clientId, "] stopChannelMediaRelay: send stop message success"));
            this.state = "RELAY_STATE_IDLE";
            this.dispose()
        }

        dispose() {
            k.debug("[".concat(this.clientId, "] disposeChannelMediaRelay"));
            this.cancelToken.cancel();
            this.cancelToken = Bb.CancelToken.source();
            this.state = "RELAY_STATE_IDLE";
            this.emit("dispose");
            this.signal.dispose();
            this.prevChannelMediaConfig = void 0
        }

        async connect() {
            let a = await ql(this.joinInfo, this.cancelToken.token, this.httpRetryConfig);
            this.workerToken = a.workerToken;
            await this.signal.connect(a.addressList);
            this.emit("event", "NETWORK_CONNECTED");
            this.signal.on("status", this.onStatus);
            this.signal.on("reconnecting", this.onReconnect)
        }

        async sendStartRelayMessage(a) {
            var b = this.genMessage(Fa.StopPacketTransfer);
            await this.signal.request(b);
            await this.signal.waitStatus("Normal Quit");
            k.debug("[".concat(this.clientId, "] startChannelMediaRelay: StopPacketTransfer success"));
            b = this.genMessage(Fa.SetSdkProfile, a);
            await this.signal.request(b);
            k.debug("[".concat(this.clientId, "] startChannelMediaRelay: SetSdkProfile success"));
            b = this.genMessage(Fa.SetSourceChannel, a);
            await this.signal.request(b);
            await this.signal.waitStatus("SetSourceChannelStatus");
            this.emit("event", "PACKET_JOINED_SRC_CHANNEL");
            k.debug("[".concat(this.clientId,
                "] startChannelMediaRelay: SetSourceChannel success"));
            b = this.genMessage(Fa.SetSourceUserId, a);
            await this.signal.request(b);
            k.debug("[".concat(this.clientId, "] startChannelMediaRelay: SetSourceUserId success"));
            b = this.genMessage(Fa.SetDestChannel, a);
            await this.signal.request(b);
            await this.signal.waitStatus("SetDestChannelStatus");
            this.emit("event", "PACKET_JOINED_DEST_CHANNEL");
            k.debug("[".concat(this.clientId, "] startChannelMediaRelay: SetDestChannel success"));
            a = this.genMessage(Fa.StartPacketTransfer,
                a);
            await this.signal.request(a);
            this.emit("event", "PACKET_SENT_TO_DEST_CHANNEL");
            this.state = "RELAY_STATE_RUNNING";
            k.debug("[".concat(this.clientId, "] startChannelMediaRelay: StartPacketTransfer success"))
        }

        async sendUpdateMessage(a) {
            a = this.genMessage(Fa.UpdateDestChannel, a);
            await this.signal.request(a);
            this.emit("event", "PACKET_UPDATE_DEST_CHANNEL");
            k.debug("[".concat(this.clientId, "] sendUpdateMessage: UpdateDestChannel success"))
        }

        async sendStopRelayMessage() {
            let a = this.genMessage(Fa.StopPacketTransfer);
            await this.signal.request(a);
            k.debug("[".concat(this.clientId, "] sendStopRelayMessage: StopPacketTransfer success"))
        }

        genMessage(a, b) {
            let c = [], e = [], g = [];
            this.requestId += 1;
            let h = {
                appId: this.joinInfo.appId,
                cname: this.joinInfo.cname,
                uid: this.joinInfo.uid.toString(),
                sdkVersion: Va,
                sid: this.joinInfo.sid,
                ts: x(),
                requestId: this.requestId,
                seq: this.requestId,
                allocate: !0,
                clientRequest: {}
            };
            "4.4.0" === h.sdkVersion && (h.sdkVersion = "0.0.1");
            let k = null, l = null;
            switch (a) {
                case Fa.SetSdkProfile:
                    return h.clientRequest = {
                        command: "SetSdkProfile",
                        type: "multi_channel"
                    }, h;
                case Fa.SetSourceChannel:
                    if (l = b && b.getSrcChannelMediaInfo(), !l) throw new p(n.UNEXPECTED_ERROR, "can not find source config");
                    return h.clientRequest = {
                        command: "SetSourceChannel",
                        uid: "0",
                        channelName: l.channelName,
                        token: l.token || this.joinInfo.appId
                    }, h;
                case Fa.SetSourceUserId:
                    if (l = b && b.getSrcChannelMediaInfo(), !l) throw new p(n.UNEXPECTED_ERROR, "can not find source config");
                    return h.clientRequest = {command: "SetSourceUserId", uid: l.uid + ""}, h;
                case Fa.SetDestChannel:
                    if (k = b && b.getDestChannelMediaInfo(),
                        !k) throw new p(n.UNEXPECTED_ERROR, "can not find dest config");
                    return q(k).call(k, a => {
                        c.push(a.channelName);
                        e.push(a.uid + "");
                        g.push(a.token || this.joinInfo.appId)
                    }), h.clientRequest = {command: "SetDestChannel", channelName: c, uid: e, token: g}, h;
                case Fa.StartPacketTransfer:
                    return h.clientRequest = {command: "StartPacketTransfer"}, h;
                case Fa.Reconnect:
                    return h.clientRequest = {command: "Reconnect"}, h;
                case Fa.StopPacketTransfer:
                    return h.clientRequest = {command: "StopPacketTransfer"}, h;
                case Fa.UpdateDestChannel:
                    if (k = b &&
                        b.getDestChannelMediaInfo(), !k) throw new p(n.UNEXPECTED_ERROR, "can not find dest config");
                    return q(k).call(k, a => {
                        c.push(a.channelName);
                        e.push(a.uid + "");
                        g.push(a.token || this.joinInfo.appId)
                    }), h.clientRequest = {command: "UpdateDestChannel", channelName: c, uid: e, token: g}, h
            }
            return h
        }
    }

    class Ho {
        constructor(a, b) {
            this._trust_stream_added_state_ = this._trust_video_mute_state_ = this._trust_audio_mute_state_ = this._trust_video_enabled_state_ = this._trust_audio_enabled_state_ = this._trust_in_room_ = !0;
            this._video_muted_ =
                this._audio_muted_ = !1;
            this._video_enabled_ = this._audio_enabled_ = !0;
            this._video_added_ = this._audio_added_ = !1;
            this.uid = a;
            this._uintid = b
        }

        get hasVideo() {
            return this._video_enabled_ && !this._video_muted_ && this._video_added_
        }

        get hasAudio() {
            return this._audio_enabled_ && !this._audio_muted_ && this._audio_added_
        }

        get audioTrack() {
            if (this.hasAudio) return this._audioTrack
        }

        get videoTrack() {
            if (this.hasVideo) return this._videoTrack
        }
    }

    var Qk = function (a, b, c, e) {
        var g, h = arguments.length, k = 3 > h ? b : null === e ? e = Y(b, c) : e;
        if ("object" ==
            typeof Reflect && "function" == typeof Reflect.decorate) k = Reflect.decorate(a, b, c, e); else for (var l = a.length - 1; 0 <= l; l--) (g = a[l]) && (k = (3 > h ? g(k) : 3 < h ? g(b, c, k) : g(b, c)) || k);
        return 3 < h && k && X(b, c, k), k
    }, Hc = function (a, b) {
        if ("object" == typeof Reflect && "function" == typeof Reflect.metadata) return Reflect.metadata(a, b)
    };

    class ag extends Ta {
        constructor(a) {
            var b, c, e, g;
            let h;
            if (super(), this._users = [], this._sessionId = null, this._bindEnabledTracks = [], this._leaveMutex = new Ob("client-leave"), this._publishMutex = new Ob("client-publish"),
                this._subscribeMutex = new ba, this._remoteStream = new ba, this._encryptionMode = "none", this._encryptionSecret = null, this._turnServer = {
                servers: [],
                mode: "auto"
            }, this._cloudProxyServerMode = "disabled", this._isDualStreamEnabled = !1, this._streamFallbackTypeCacheMap = new ba, this._remoteStreamTypeCacheMap = new ba, this._axiosCancelSource = Bb.CancelToken.source(), this._networkQualitySensitivity = "normal", this._handleLocalTrackEnable = (a, b, c) => {
                this.publish(a, !1).then(b).catch(c)
            }, this._handleLocalTrackDisable = (a, b, c) => {
                this.unpublish(a, !1).then(b).catch(c)
            }, this._handleUserOnline = a => {
                var b;
                this.isStringUID && "string" != typeof a.uid && k.error("[".concat(this._clientId, "] StringUID is Mixed with UintUID"));
                let c = R(b = this._users).call(b, b => b.uid === a.uid);
                c ? c._trust_in_room_ = !0 : (b = new Ho(a.uid, a.uint_id || a.uid), this._users.push(b), k.debug("[".concat(this._clientId, "] user online"), a.uid), this.emit(P.USER_JOINED, b))
            }, this._handleUserOffline = a => {
                var b;
                let c = R(b = this._users).call(b, b => b.uid === a.uid);
                c && (this._handleRemoveStream(a),
                    Oc(this._users, c), this._remoteStreamTypeCacheMap.delete(c.uid), this._streamFallbackTypeCacheMap.delete(c.uid), k.debug("[".concat(this._clientId, "] user offline"), a.uid, "reason:", a.reason), this.emit(P.USER_LEAVED, c, a.reason))
            }, this._handleAddAudioOrVideoStream = (a, b, c) => {
                var e, g, h;
                let m = R(e = this._users).call(e, a => a.uid === b);
                if (!m) return void k.error("[".concat(this._clientId, "] can not find target user!(on_add_stream)"));
                k.debug(l(g = l(h = "[".concat(this._clientId, "] stream added with uid ")).call(h,
                    b, ", type ")).call(g, a));
                e = "audio" === a ? m.hasAudio : m.hasVideo;
                var n, p;
                (m._uintid || (m._uintid = c || b), m._trust_stream_added_state_ = !0, "audio" === a ? m._audio_added_ = !0 : m._video_added_ = !0, ("audio" === a ? m.hasAudio : m.hasVideo) && !e) && (k.info(l(n = l(p = "[".concat(this._clientId, "] remote user ")).call(p, m.uid, " published ")).call(n, a)), this.emit(P.USER_PUBLISHED, m, a));
                "video" === a ? t.onGatewayStream(this._sessionId, Da.ON_ADD_VIDEO_STREAM, ma.ON_ADD_VIDEO_STREAM, {peer: c || b}) : t.onGatewayStream(this._sessionId, Da.ON_ADD_AUDIO_STREAM,
                    ma.ON_ADD_AUDIO_STREAM, {peer: c || b});
                (a = this._remoteStream.get(b)) && a.readyToReconnect && "connecting" === a.connectionState && a.reconnectPC().catch(a => {
                    k.error("[".concat(this._clientId, "] resubscribe error"), a.toString())
                })
            }, this._handleRemoveStream = a => {
                var b, c;
                let e = R(b = this._users).call(b, b => b.uid === a.uid);
                if (!e) return void k.warning("[".concat(this._clientId, "] can not find target user!(on_remove_stream)"));
                k.debug(l(c = "[".concat(this._clientId, "] stream removed with uid ")).call(c, a.uid));
                b = () => {
                };
                e.hasAudio && e.hasVideo ? b = () => {
                    var a, b;
                    k.info(l(a = "[".concat(this._clientId, "] remote user ")).call(a, e.uid, " unpublished audio track"));
                    this.emit(P.USER_UNPUBLISHED, e, "audio");
                    k.info(l(b = "[".concat(this._clientId, "] remote user ")).call(b, e.uid, " unpublished video track"));
                    this.emit(P.USER_UNPUBLISHED, e, "video")
                } : e.hasVideo ? b = () => {
                    var a;
                    k.info(l(a = "[".concat(this._clientId, "] remote user ")).call(a, e.uid, " unpublished video track"));
                    this.emit(P.USER_UNPUBLISHED, e, "video")
                } : e.hasAudio && (b = () => {
                    var a;
                    k.info(l(a = "[".concat(this._clientId, "] remote user ")).call(a, e.uid, " unpublished audio track"));
                    this.emit(P.USER_UNPUBLISHED, e, "audio")
                });
                e._trust_stream_added_state_ = !0;
                e._audio_added_ = !1;
                e._video_added_ = !1;
                (c = this._remoteStream.get(e.uid)) && (c.closeP2PConnection(), this._remoteStream.delete(e.uid));
                t.onGatewayStream(this._sessionId, Da.ON_REMOVE_STREAM, ma.ON_REMOVE_STREAM, {peer: a.uint_id || a.uid});
                b()
            }, this._handleSetStreamLocalEnable = (a, b, c) => {
                var e, g, h, m, n, p;
                let r = R(e = this._users).call(e, a => a.uid ===
                    b);
                if (!r) return void k.error("[".concat(this._clientId, "] can not find target user!(disable_local)"));
                k.debug(l(g = l(h = l(m = "[".concat(this._clientId, "] local ")).call(m, a, " ")).call(h, c ? "enabled" : "disabled", " with uid ")).call(g, b));
                e = "audio" === a ? r.hasAudio : r.hasVideo;
                if ("audio" === a) {
                    r._trust_audio_enabled_state_ = !0;
                    var q = r._audio_enabled_;
                    if (r._audio_enabled_ = c, r._audio_enabled_ === q) return;
                    var w, t;
                    c = r._audio_enabled_ ? "enable-local-audio" : "disable-local-audio";
                    k.debug(l(w = l(t = "[".concat(this._clientId,
                        "] user-info-updated, uid: ")).call(t, b, ", msg: ")).call(w, c));
                    this.emit(P.USER_INFO_UPDATED, b, c)
                } else {
                    r._trust_video_enabled_state_ = !0;
                    w = r._video_enabled_;
                    if (r._video_enabled_ = c, r._video_enabled_ === w) return;
                    var y;
                    c = r._video_enabled_ ? "enable-local-video" : "disable-local-video";
                    k.debug(l(q = l(y = "[".concat(this._clientId, "] user-info-update, uid: ")).call(y, b, ", msg: ")).call(q, c));
                    this.emit(P.USER_INFO_UPDATED, b, c)
                }
                c = "audio" === a ? r.hasAudio : r.hasVideo;
                if (e !== c) {
                    var u, v;
                    if (!e && c) return k.info(l(u = l(v =
                        "[".concat(this._clientId, "] remote user ")).call(v, b, " published ")).call(u, a)), void this.emit(P.USER_PUBLISHED, r, a);
                    if (u = this._remoteStream.get(b)) v = qc({}, u.subscribeOptions), v.audio = !!r.hasAudio && v.audio, v.video = !!r.hasVideo && v.video, v.audio || v.video ? u.setSubscribeOptions(v) : (u.closeP2PConnection().catch(a => {
                        k.warning("close sub pc error", a)
                    }), this._remoteStream.delete(r.uid));
                    k.info(l(n = l(p = "[".concat(this._clientId, "] remote user ")).call(p, r.uid, " unpublished ")).call(n, a));
                    this.emit(P.USER_UNPUBLISHED,
                        r, a)
                }
            }, this._handleMuteStream = (a, b, c) => {
                var e, g, h;
                k.debug("[".concat(this._clientId, "] receive mute message"), a, b, c);
                let m = R(e = this._users).call(e, b => b.uid === a);
                var n;
                if (!m) return void k.warning(l(n = "[".concat(this._clientId, "] can not find remote user, ignore mute event, uid: ")).call(n, a));
                e = "audio" === b ? m.hasAudio : m.hasVideo;
                if ("audio" === b) {
                    m._trust_audio_mute_state_ = !0;
                    var p = m._audio_muted_;
                    if (m._audio_muted_ = c, m._audio_muted_ === p) return;
                    var r, q;
                    c = m._audio_muted_ ? "mute-audio" : "unmute-audio";
                    k.debug(l(r =
                        l(q = "[".concat(this._clientId, "] user-info-update, uid: ")).call(q, a, ", msg: ")).call(r, c));
                    this.emit(P.USER_INFO_UPDATED, a, c)
                } else {
                    m._trust_video_mute_state_ = !0;
                    r = m._video_muted_;
                    if (m._video_muted_ = c, m._video_muted_ === r) return;
                    var w;
                    c = m._video_muted_ ? "mute-video" : "unmute-video";
                    k.debug(l(p = l(w = "[".concat(this._clientId, "] user-info-update, uid: ")).call(w, a, ", msg: ")).call(p, c));
                    this.emit(P.USER_INFO_UPDATED, a, c)
                }
                c = "audio" === b ? m.hasAudio : m.hasVideo;
                if (e !== c) {
                    var t, y;
                    if (!e && c) return k.info(l(t = l(y =
                        "[".concat(this._clientId, "] remote user ")).call(y, a, " published ")).call(t, b)), void this.emit(P.USER_PUBLISHED, m, b);
                    if (t = this._remoteStream.get(a)) y = qc({}, t.subscribeOptions), y.audio = !!m.hasAudio && y.audio, y.video = !!m.hasVideo && y.video, "video" === b && t.pc._statsFilter.setVideoIsReady(!1), y.audio || y.video ? t.setSubscribeOptions(y) : (t.closeP2PConnection().catch(a => {
                        k.warning("close sub pc error", a)
                    }), this._remoteStream.delete(m.uid));
                    k.info(l(g = l(h = "[".concat(this._clientId, "] remote user ")).call(h,
                        a, " unpublished ")).call(g, b));
                    this.emit(P.USER_UNPUBLISHED, m, b)
                }
            }, this._handleP2PLost = a => {
                k.debug("[".concat(this._clientId, "] receive p2p lost"), a);
                let b = null;
                if (this._highStream && this._highStream.pc.ID === a.p2pid) b = this._highStream; else if (this._lowStream && this._lowStream.pc.ID === a.p2pid) b = this._lowStream; else {
                    var c;
                    q(c = this._remoteStream).call(c, c => {
                        c.pc.ID === a.p2pid && (b = c)
                    })
                }
                b ? b.emit(I.GATEWAY_P2P_LOST, a.p2pid) : k.warning("P2PLost stream not found", a)
            }, this._handleTokenWillExpire = () => {
                k.debug("[".concat(this._clientId,
                    "] received message onTokenPrivilegeWillExpire"));
                this.emit(P.ON_TOKEN_PRIVILEGE_WILL_EXPIRE)
            }, this._handleBeforeUnload = a => {
                void 0 !== a.returnValue && "" !== a.returnValue || (this.leave(), k.info("[".concat(this._clientId, "] auto leave onbeforeunload")))
            }, this._handleUpdateNetworkQuality = () => {
                var a;
                if ("normal" !== this._networkQualitySensitivity) {
                    if (navigator && void 0 !== navigator.onLine && !navigator.onLine) return void this.emit(P.NETWORK_QUALITY, {
                        downlinkNetworkQuality: 6,
                        uplinkNetworkQuality: 6
                    });
                    var b = {
                        downlinkNetworkQuality: 0,
                        uplinkNetworkQuality: 0
                    };
                    this._highStream && !this._highStream.detecting && (b.uplinkNetworkQuality = this._highStream.getNetworkQuality());
                    var c = 0;
                    q(a = this._remoteStream).call(a, a => c += a.getNetworkQuality());
                    0 < this._remoteStream.size && (b.downlinkNetworkQuality = Math.round(c / this._remoteStream.size));
                    this.emit(P.NETWORK_QUALITY, b)
                }
            }, this._codec = a.codec, this._mode = a.mode, a.proxyServer && (this._proxyServer = a.proxyServer, t.setProxyServer(this._proxyServer), k.setProxyServer(this._proxyServer)), a.turnServer && (this._turnServer =
                qc({}, this._turnServer, {mode: "manual"}, a.turnServer)), this._clientId = pa(5, "client-"), k.info(l(b = l(c = l(e = l(g = "[".concat(this._clientId, "] Initializing AgoraRTC client v")).call(g, Va, " build: ")).call(e, "v4.4.0-0-g48538343(4/2/2021, 5:44:00 PM)", ", mode: ")).call(c, this._mode, ", codec: ")).call(b, this._codec)), a.clientRoleOptions) try {
                Zg(a.clientRoleOptions), h = Ha({}, a.clientRoleOptions)
            } catch (r) {
                var m;
                k.warning(l(m = "[".concat(this._clientId, "] ")).call(m, r.toString()))
            }
            this._statsCollector = new ld(this._clientId);
            this._statsCollector.onStatsException = (a, b, c) => {
                var e, g, h;
                k.debug(l(e = l(g = l(h = "[".concat(this._clientId, "] receive exception msg, code: ")).call(h, a, ", msg: ")).call(g, b, ", uid: ")).call(e, c));
                this.emit(P.EXCEPTION, {code: a, msg: b, uid: c})
            };
            this._statsCollector.onUploadPublishDuration = (a, b, c, e) => {
                var g;
                let h = R(g = this._users).call(g, b => b.uid === a);
                h && t.peerPublishStatus(this._sessionId, {
                    subscribeElapse: e,
                    audioPublishDuration: b,
                    videoPublishDuration: c,
                    peer: h._uintid
                })
            };
            this._gateway = new io({
                clientId: this._clientId,
                mode: this._mode,
                codec: this._codec,
                websocketRetryConfig: a.websocketRetryConfig || Ra,
                httpRetryConfig: a.httpRetryConfig || Ra,
                forceWaitGatewayResponse: void 0 === a.forceWaitGatewayResponse || a.forceWaitGatewayResponse,
                statsCollector: this._statsCollector,
                role: a.role,
                clientRoleOptions: h
            });
            this._config = a;
            this._configDistribute = jo;
            this._handleGatewayEvents();
            Mj.push(this)
        }

        get connectionState() {
            return this._gateway.state
        }

        get remoteUsers() {
            return this._users
        }

        get localTracks() {
            return this._highStream ? this._highStream.getAllTracks() :
                []
        }

        get uid() {
            return this._uid
        }

        get channelName() {
            return this._channelName
        }

        get isStringUID() {
            return !!this._joinInfo && !!this._joinInfo.stringUid
        }

        async join(a, b, c, e, g) {
            var h;
            let m = t.reportApiInvoke(this._sessionId, {name: E.JOIN, options: [a, b, c, e], tag: D.TRACER});
            try {
                if (!c && null !== c) throw new p(n.INVALID_PARAMS, "Invalid token: ".concat(c, ". If you don not use token, set it to null"));
                c && La(c, "token", 1, 2047);
                we(b);
                e && xe(e);
                g && La(g, "optionalInfo", 1, 2047)
            } catch (y) {
                throw m.onError(y), y;
            }
            if (k.info(l(h = "[".concat(this._clientId,
                "] start join channel ")).call(h, b)), this._leaveMutex.isLocked) k.debug("[".concat(this._clientId, "] join: waiting leave operation")), (await this._leaveMutex.lock())(), k.debug("[".concat(this._clientId, "] join: continue"));
            if ("DISCONNECTED" !== this.connectionState) throw a = new p(n.INVALID_OPERATION, "[".concat(this._clientId, "] Client already in connecting/connected state")), m.onError(a), a;
            this._sessionId || (this._sessionId = pa(32, "").toUpperCase());
            this._gateway.state = "CONNECTING";
            g = {
                clientId: this._clientId,
                appId: a,
                sid: this._sessionId,
                cname: b,
                uid: "string" != typeof e ? e : null,
                turnServer: this._turnServer,
                proxyServer: this._proxyServer,
                token: c || a,
                cloudProxyServer: this._cloudProxyServerMode,
                optionalInfo: g
            };
            "string" == typeof e && (g.stringUid = e, this._uintUid ? (g.uid = this._uintUid, this._uintUid = void 0) : g.uid = 0);
            "none" !== this._encryptionMode && this._encryptionSecret && (g.aesmode = this._encryptionMode, g.aespassword = this._encryptionSecret);
            this._startSession(this._sessionId, {channel: b, appId: a});
            Fc(() => {
                "CONNECTING" === this.connectionState &&
                t.joinChannelTimeout(this._sessionId, 5)
            }, 5E3);
            try {
                var r;
                if (await ol(g, this._axiosCancelSource.token, this._config.httpRetryConfig || Ra), g.stringUid && !g.uid) {
                    var q;
                    let a = await zh(g.stringUid, g, this._axiosCancelSource.token, this._config.httpRetryConfig || Ra);
                    k.debug(l(q = "getUserAccount Success ".concat(g.stringUid, " => ")).call(q, a));
                    g.uid = a
                }
                this._configDistribute.startGetConfigDistribute(g, this._axiosCancelSource.token);
                this._configDistribute.on(id.UPDATE_BITRATE_LIMIT, a => {
                    this._highStream && this._highStream.handleUpdateBitrateLimit(a.uplink);
                    a.low_stream_uplink && this._lowStream && this._lowStream.handleUpdateBitrateLimit({
                        max_bitrate: a.low_stream_uplink.bitrate,
                        min_bitrate: a.low_stream_uplink.bitrate || 0
                    })
                });
                let e = await yh(g, this._axiosCancelSource.token, this._config.httpRetryConfig || Ra);
                this._key = c || a;
                this._joinInfo = qc({}, g, {
                    cid: e.gatewayInfo.cid,
                    uid: g.uid ? g.uid : e.gatewayInfo.uid,
                    vid: e.gatewayInfo.vid,
                    apResponse: e.gatewayInfo.res,
                    uni_lbs_ip: e.gatewayInfo.uni_lbs_ip,
                    gatewayAddrs: e.gatewayInfo.gatewayAddrs
                });
                let h = await this._gateway.join(this._joinInfo,
                    this._key);
                return m.onSuccess(h), this._appId = a, this._channelName = g.cname, this._uid = h, this._networkQualityInterval && window.clearInterval(this._networkQualityInterval), this._networkQualityInterval = window.setInterval(this._handleUpdateNetworkQuality, 2E3), window.addEventListener("beforeunload", this._handleBeforeUnload), k.info(l(r = "[".concat(this._clientId, "] Joining channel success: ")).call(r, b)), h
            } catch (y) {
                throw k.error("[".concat(this._clientId, "] Joining channel failed, rollback"), y), y.code !== n.OPERATION_ABORTED &&
                (this._gateway.state = "DISCONNECTED", this._reset()), m.onError(y), y;
            }
        }

        async leave() {
            let a = t.reportApiInvoke(this._sessionId, {name: E.LEAVE, options: [], tag: D.TRACER});
            k.info("[".concat(this._clientId, "] Leaving channel"));
            window.removeEventListener("beforeunload", this._handleBeforeUnload);
            this._reset();
            let b = await this._leaveMutex.lock();
            if ("DISCONNECTED" === this.connectionState) return k.info("[".concat(this._clientId, "] Leaving channel repeated, success")), b(), a.onSuccess();
            await this._gateway.leave("CONNECTED" !==
                this.connectionState);
            k.info("[".concat(this._clientId, "] Leaving channel success"));
            b();
            a.onSuccess()
        }

        async publish(a, b = !0) {
            var c, e;
            mc(a) || (a = [a]);
            let g = t.reportApiInvoke(this._sessionId, {
                name: E.PUBLISH,
                options: C(a).call(a, a => a ? Object(a).toString() : "null"),
                tag: D.TRACER
            });
            if (0 === a.length) return a = new p(n.INVALID_PARAMS, "track list is empty"), g.onError(a), a.throw();
            if ("live" === this._mode && "audience" === this._gateway.role) return a = new p(n.INVALID_OPERATION, "audience can not publish stream"), g.onError(a),
                a.throw();
            for (let c of a) {
                if (!(c instanceof ke)) return a = new p(n.INVALID_PARAMS, "pamameter is not local track"), g.onError(a), a.throw();
                if (!c._enabled && b) return a = new p(n.TRACK_IS_DISABLED, "can not publish a disabled track: ".concat(c.getTrackId())), g.onError(a), a.throw()
            }
            k.info(l(c = "[".concat(this._clientId, "] Publishing tracks, id ")).call(c, C(a).call(a, a => "".concat(a.getTrackId(), " "))));
            await this._configDistribute.awaitConfigDistributeComplete();
            b && q(a).call(a, a => {
                var b;
                let c = this._configDistribute.getBitrateLimit();
                a instanceof Na && c && a.setBitrateLimit(c.uplink);
                -1 === G(b = this._bindEnabledTracks).call(b, a) && (a.addListener(L.NEED_ADD_TRACK, this._handleLocalTrackEnable), a.addListener(L.NEED_REMOVE_TRACK, this._handleLocalTrackDisable), this._bindEnabledTracks.push(a))
            });
            c = await this._publishMutex.lock();
            try {
                let b = await this._publishHighStream(a), e = (b.audioTrack, b.videoTrack);
                this._isDualStreamEnabled && e && !this._lowStream && await this._publishLowStream(e);
                c();
                g.onSuccess()
            } catch (h) {
                throw c(), b && q(a).call(a, a => {
                    var b,
                        c;
                    let e = G(b = this._bindEnabledTracks).call(b, a);
                    -1 !== e && (a.off(L.NEED_ADD_TRACK, this._handleLocalTrackEnable), a.off(L.NEED_REMOVE_TRACK, this._handleLocalTrackDisable), Ia(c = this._bindEnabledTracks).call(c, e, 1))
                }), g.onError(h), k.error("[".concat(this._clientId, "] publish error"), h.toString()), h;
            }
            k.info(l(e = "[".concat(this._clientId, "] Publish success, id ")).call(e, C(a).call(a, a => "".concat(a.getTrackId(), " "))))
        }

        async unpublish(a, b = !0) {
            var c, e, g;
            if (!this._highStream) return void k.warning("[".concat(this._clientId,
                "] Could not find tracks to unpublish"));
            var h = this._highStream.getAllTracks();
            a ? mc(a) || (a = [a]) : a = this._highStream.getAllTracks();
            h = function (a, b) {
                if (a.length !== b.length) return !1;
                for (let c = 0; c < a.length; c += 1) {
                    const e = a[c];
                    if (N(a).call(a, a => a === e).length !== N(b).call(b, a => a === e).length) return !1
                }
                return !0
            }(h, a);
            let m = t.reportApiInvoke(this._sessionId, {
                name: E.UNPUBLISH,
                options: C(a).call(a, a => a.getTrackId()),
                tag: D.TRACER
            });
            k.info(l(c = l(e = "[".concat(this._clientId, "] Unpublish tracks, tracks ")).call(e, C(a).call(a,
                a => "".concat(a.getTrackId(), " ")), ", isClosePC: ")).call(c, h));
            c = h ? void 0 : await this._publishMutex.lock();
            if (!this._highStream) return k.warning("[".concat(this._clientId, "] Could not find tracks to unpublish")), void (c && c());
            try {
                this._lowStream && 0 < N(a).call(a, a => "video" === a.trackMediaType).length && (await this._lowStream.closeP2PConnection(), this._lowStream = void 0), h ? await this._highStream.closeP2PConnection() : await this._highStream.removeTracks(a, b), c && c()
            } catch (r) {
                if (r.code !== n.OPERATION_ABORTED) throw m.onError(r),
                    k.error("[".concat(this._clientId, "] unpublish error"), r.toString()), c && c(), r;
                k.debug("[".concat(this._clientId, "] ignore unpub operation abort"));
                c && c()
            }
            this._highStream && "disconnected" === this._highStream.connectionState && (this._highStream = void 0, this._lowStream = void 0);
            b && q(a).call(a, a => {
                var b, c;
                let e = G(b = this._bindEnabledTracks).call(b, a);
                -1 !== e && (a.off(L.NEED_ADD_TRACK, this._handleLocalTrackEnable), a.off(L.NEED_REMOVE_TRACK, this._handleLocalTrackDisable), Ia(c = this._bindEnabledTracks).call(c, e,
                    1))
            });
            k.info(l(g = "[".concat(this._clientId, "] Unpublish success,tracks ")).call(g, C(a).call(a, a => "".concat(a.getTrackId()))));
            m.onSuccess()
        }

        async subscribe(a, b) {
            var c, e, g, h;
            Ka(b, "mediaType", ["audio", "video"]);
            let m = t.reportApiInvoke(this._sessionId, {name: E.SUBSCRIBE, options: [a.uid, b], tag: D.TRACER});
            if (!this._joinInfo) throw b = new p(n.INVALID_OPERATION, "Can't subscribe stream, not joined"), m.onError(b), b;
            if ("CONNECTED" !== this.connectionState && "RECONNECTING" !== this.connectionState) throw b = new p(n.INVALID_OPERATION,
                "Can't subscribe stream in ".concat(this.connectionState, " state")), m.onError(b), b;
            if (!R(c = this._users).call(c, b => b === a)) {
                var r;
                b = new p(n.INVALID_REMOTE_USER, "user is not in the channel");
                throw k.error(l(r = "[".concat(this._clientId, "] can not subscribe ")).call(r, a.uid, ", this user is not in the channel")), m.onError(b), b;
            }
            if (!a.hasAudio && !a.hasVideo) {
                var q;
                b = new p(n.INVALID_REMOTE_USER, "user is not published");
                throw k.error(l(q = "[".concat(this._clientId, "] can not subscribe ")).call(q, a.uid, ", user is not published")),
                    m.onError(b), b;
            }
            r = {audio: "audio" === b, video: "video" === b};
            if (!a.hasAudio && r.audio || !a.hasVideo && r.video) {
                var u, v;
                var B = new p(n.REMOTE_USER_IS_NOT_PUBLISHED);
                throw k.error(l(u = l(v = "[".concat(this._clientId, "] can not subscribe ")).call(v, a.uid, " with mediaType ")).call(u, b, ", remote track is not published")), m.onError(B), B;
            }
            (u = this._subscribeMutex.get(a.uid)) || (u = new Ob("sub-".concat(a.uid)), this._subscribeMutex.set(a.uid, u));
            k.info(l(e = l(g = "[".concat(this._clientId, "] subscribe user ")).call(g, a.uid,
                ", mediaType: ")).call(e, b));
            e = await u.lock();
            g = this._remoteStream.get(a.uid);
            try {
                if (g) r.audio = r.audio || g.subscribeOptions.audio, r.video = r.video || g.subscribeOptions.video, await this._gateway.subscribeChange(g, r); else {
                    g = new Co(a, this._statsCollector, this._joinInfo, r);
                    this._remoteStream.set(a.uid, g);
                    try {
                        await this._gateway.subscribe(g)
                    } catch (z) {
                        throw this._remoteStream.delete(a.uid), z;
                    }
                    g.on(I.CONNECTION_STATE_CHANGE, (b, c) => {
                        "connecting" === b ? this.emit(P.MEDIA_RECONNECT_START, a.uid) : "connected" === b &&
                            this.emit(P.MEDIA_RECONNECT_END, a.uid)
                    })
                }
                e()
            } catch (z) {
                var x;
                throw m.onError(z), e(), k.error(l(x = "[".concat(this._clientId, "] subscribe user ")).call(x, a.uid, " error"), z), z;
            }
            k.info(l(B = l(h = "[".concat(this._clientId, "] subscribe success user ")).call(h, a.uid, ", mediaType: ")).call(B, b));
            this._defaultStreamFallbackType && this.setStreamFallbackOption(a.uid, this._defaultStreamFallbackType).catch(a => {
                k.warning("[".concat(this._clientId, "] auto set fallback failed"), a)
            });
            b = "audio" === b ? a.audioTrack : a.videoTrack;
            return b ? (m.onSuccess(b.getTrackId()), b) : (b = new p(n.UNEXPECTED_ERROR, "can not find remote track in user object"), m.onError(b), b.throw())
        }

        async unsubscribe(a, b) {
            var c, e, g, h, m;
            b && Ka(b, "mediaType", ["audio", "video"]);
            let r = t.reportApiInvoke(this._sessionId, {name: E.UNSUBSCRIBE, options: [a.uid, b], tag: D.TRACER});
            if (!R(c = this._users).call(c, b => b === a)) {
                var q;
                b = new p(n.INVALID_REMOTE_USER, "user is not in the channel");
                throw k.error(l(q = "[".concat(this._clientId, "] can not subscribe ")).call(q, a.uid, ", user is not in the channel")),
                    r.onError(b), b;
            }
            k.info(l(e = l(g = "[".concat(this._clientId, "] unsubscribe uid: ")).call(g, a.uid, ", mediaType: ")).call(e, b));
            (q = this._subscribeMutex.get(a.uid)) || (q = new Ob("sub-".concat(a.uid)), this._subscribeMutex.set(a.uid, q));
            q = await q.lock();
            c = this._remoteStream.get(a.uid);
            var u;
            if (!c) return k.warning(l(u = "[".concat(this._clientId, "]: you have not subscribe the remote user ")).call(u, a.uid)), r.onSuccess(), void q();
            u = qc({}, c.subscribeOptions);
            "audio" === b ? u.audio = !1 : "video" === b ? (u.video = !1, c.pc._statsFilter.setVideoIsReady(!1)) :
                (u.audio = !1, u.video = !1);
            try {
                u.audio || u.video ? await this._gateway.subscribeChange(c, u) : (await c.closeP2PConnection(), this._remoteStream.delete(a.uid)), q()
            } catch (B) {
                var v;
                if (B.code !== n.OPERATION_ABORTED) throw r.onError(B), q(), k.error(l(v = "[".concat(this._clientId, "] unsubscribe user ")).call(v, a.uid, " error"), B.toString()), B;
                q();
                k.debug("[".concat(this._clientId, "] ignore unsub operation abort"))
            }
            k.info(l(h = l(m = "[".concat(this._clientId, "] unsubscribe success uid: ")).call(m, a.uid, ", mediaType: ")).call(h,
                b));
            r.onSuccess()
        }

        setLowStreamParameter(a) {
            if (!a) throw new p(n.INVALID_PARAMS);
            null == a.width || V(a.width, "streamParameter.width");
            null == a.height || V(a.height, "streamParameter.height");
            null == a.framerate || V(a.framerate, "streamParameter.framerate");
            null == a.bitrate || V(a.bitrate, "streamParameter.bitrate");
            !0;
            let b = t.reportApiInvoke(this._sessionId, {name: E.SET_LOW_STREAM_PARAMETER, options: [a], tag: D.TRACER});
            (!a.width && a.height || a.width && !a.height) && k.warning("[".concat(this._clientId, "] The width and height parameters take effect only when both are set"));
            k.info("[".concat(this._clientId, "] set low stream parameter to"), A(a));
            let c = this._configDistribute.getLowStreamConfigDistribute();
            c && c.bitrate && a.bitrate && c.bitrate < a.bitrate && (a.bitrate = c.bitrate);
            this._lowStreamParameter = a;
            b.onSuccess()
        }

        async enableDualStream() {
            let a = t.reportApiInvoke(this._sessionId, {name: E.ENABLE_DUAL_STREAM, options: [], tag: D.TRACER});
            if (!ca.supportDualStream) {
                t.streamSwitch(this._sessionId, {lts: x(), isdual: !0, succ: !1});
                var b = new p(n.NOT_SUPPORTED, "Your browser is not support dual stream");
                throw a.onError(b), b;
            }
            if (this._isDualStreamEnabled) throw b = new p(n.INVALID_OPERATION, "Dual stream is already enabled"), a.onError(b), b;
            if (this._highStream && "connected" === this._highStream.connectionState && this._highStream.videoTrack) try {
                await this._publishLowStream(this._highStream.videoTrack)
            } catch (c) {
                throw t.streamSwitch(this._sessionId, {lts: x(), isdual: !0, succ: !1}), a.onError(c), c;
            }
            this._isDualStreamEnabled = !0;
            t.streamSwitch(this._sessionId, {lts: x(), isdual: !0, succ: !0});
            k.info("[".concat(this._clientId,
                "] enable dual stream"));
            a.onSuccess()
        }

        async disableDualStream() {
            let a = t.reportApiInvoke(this._sessionId, {name: E.DISABLE_DUAL_STREAM, options: [], tag: D.TRACER});
            if (this._lowStream) try {
                await this._lowStream.closeP2PConnection()
            } catch (b) {
                throw t.streamSwitch(this._sessionId, {lts: x(), isdual: !1, succ: !1}), a.onError(b), b;
            }
            this._lowStream = void 0;
            this._isDualStreamEnabled = !1;
            this._highStream && (this._highStream.lowStreamConnection = void 0);
            t.streamSwitch(this._sessionId, {lts: x(), isdual: !1, succ: !0});
            k.info("[".concat(this._clientId,
                "] disable dual stream"));
            a.onSuccess()
        }

        async setClientRole(a, b) {
            Ka(a, "role", ["audience", "host"]);
            !0;
            b && Zg(b);
            let c = t.reportApiInvoke(this._sessionId, {name: E.SET_CLIENT_ROLE, options: [a, b], tag: D.TRACER});
            if ("rtc" === this._mode) return k.warning("[".concat(this._clientId, "]rtc mode can not use setClientRole")), a = new p(n.INVALID_OPERATION, "rtc mode can not use setClientRole"), c.onError(a), a.throw();
            if (b && b.level && "host" === a) return a = new p(n.INVALID_OPERATION, "host mode can not set audience latency level"),
                c.onError(a), a.throw();
            try {
                var e, g;
                if ("audience" === a && this._highStream) {
                    let a = new p(n.INVALID_OPERATION, "can not set client role to audience when publishing stream");
                    return c.onError(a), a.throw()
                }
                await this._gateway.setClientRole(a, b);
                k.info(l(e = l(g = "[".concat(this._clientId, "] set client role to ")).call(g, a, ", level: ")).call(e, b && b.level));
                c.onSuccess()
            } catch (h) {
                throw c.onError(h), h;
            }
        }

        setProxyServer(a) {
            La(a, "proxyServer");
            let b = t.reportApiInvoke(this._sessionId, {
                name: E.SET_PROXY_SERVER, options: [a],
                tag: D.TRACER
            });
            if ("DISCONNECTED" !== this.connectionState) throw new p(n.INVALID_OPERATION, "Set proxy server before join channel");
            if ("disabled" !== this._cloudProxyServerMode) throw new p(n.INVALID_OPERATION, "You have already set the proxy");
            this._proxyServer = a;
            t.setProxyServer(this._proxyServer);
            k.setProxyServer(this._proxyServer);
            b.onSuccess()
        }

        setTurnServer(a) {
            if (mc(a) || (a = [a]), "DISCONNECTED" !== this.connectionState) throw new p(n.INVALID_OPERATION, "Set turn server before join channel");
            if ("disabled" !==
                this._cloudProxyServerMode) throw new p(n.INVALID_OPERATION, "You have already set the proxy");
            var b;
            if (ye(a)) return this._turnServer = {
                servers: a,
                mode: "original-manual"
            }, void k.info(l(b = "[".concat(this._clientId, "] Set original turnserver success: ")).call(b, C(a).call(a, a => a.urls).join(","), "."));
            q(a).call(a, a => Yg(a));
            this._turnServer = {servers: a, mode: "manual"};
            k.info("[".concat(this._clientId, "] Set turnserver success."))
        }

        startProxyServer(a) {
            let b = t.reportApiInvoke(this._sessionId, {
                name: E.START_PROXY_SERVER,
                options: [], tag: D.TRACER
            });
            if ("DISCONNECTED" !== this.connectionState) throw a = new p(n.INVALID_OPERATION, "Start proxy server before join channel"), b.onError(a), a;
            if (this._proxyServer || "manual" === this._turnServer.mode) throw a = new p(n.INVALID_OPERATION, "You have already set the proxy"), b.onError(a), a;
            let c = [1, 2, 3, 4, 5];
            switch (void 0 === a && (a = 1), a) {
                case 1:
                    this._cloudProxyServerMode = "normal";
                    break;
                case 2:
                    this._cloudProxyServerMode = "443only";
                    break;
                case 3:
                    this._cloudProxyServerMode = "proxy3";
                    break;
                case 4:
                    this._cloudProxyServerMode =
                        "proxy4";
                    break;
                case 5:
                    this._cloudProxyServerMode = "proxy5";
                    break;
                default:
                    throw a = new p(n.INVALID_PARAMS, "proxy server mode must be ".concat(c.join("|"))), b.onError(a), a;
            }
            k.info("[".concat(this._clientId, "] set cloud proxy server mode to"), this._cloudProxyServerMode);
            b.onSuccess()
        }

        stopProxyServer() {
            let a = t.reportApiInvoke(this._sessionId, {name: E.STOP_PROXY_SERVER, options: [], tag: D.TRACER});
            if ("DISCONNECTED" !== this.connectionState) throw new p(n.INVALID_OPERATION, "Stop proxy server after leave channel");
            t.setProxyServer();
            k.setProxyServer();
            this._cloudProxyServerMode = "disabled";
            k.info("[".concat(this._clientId, "] set cloud proxy server mode to"), this._cloudProxyServerMode);
            this._proxyServer = void 0;
            this._turnServer = {mode: "auto", servers: []};
            a.onSuccess()
        }

        async setRemoteVideoStreamType(a, b) {
            var c, e;
            Ka(b, "streamType", [0, 1]);
            let g = t.reportApiInvoke(this._sessionId, {
                name: E.SET_REMOTE_VIDEO_STREAM_TYPE,
                options: [a, b],
                tag: D.TRACER
            });
            try {
                await this._gateway.setRemoteVideoStreamType(a, b)
            } catch (h) {
                throw g.onError(h),
                    k.error("[".concat(this._clientId, "] set remote video stream type error"), h.toString()), h;
            }
            k.info(l(c = l(e = "[".concat(this._clientId, "] set remote ")).call(e, a, " video stream type to ")).call(c, b));
            this._remoteStreamTypeCacheMap.set(a, b);
            g.onSuccess()
        }

        async setStreamFallbackOption(a, b) {
            var c, e;
            Ka(b, "fallbackType", [0, 1, 2]);
            let g = t.reportApiInvoke(this._sessionId, {
                name: E.SET_STREAM_FALLBACK_OPTION,
                options: ["too long to show", b],
                tag: D.TRACER
            });
            try {
                await this._gateway.setStreamFallbackOption(a, b)
            } catch (h) {
                throw g.onError(h),
                    k.error("[".concat(this._clientId, "] set stream fallback option"), h.toString()), h;
            }
            k.info(l(c = l(e = "[".concat(this._clientId, "] set remote ")).call(e, a, " stream fallback type to ")).call(c, b));
            this._streamFallbackTypeCacheMap.set(a, b);
            g.onSuccess()
        }

        setEncryptionConfig(a, b) {
            Ka(a, "encryptionMode", "aes-128-xts aes-256-xts aes-128-ecb sm4-128-ecb aes-128-gcm aes-256-gcm none".split(" "));
            !0;
            La(b, "secret");
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*,.<>?/:;'"|{}\[\]])(?=.{8,})/.test(b) || k.warning("The secret is not strong:\n      The secret must contain at least 1 lowercase alphabetical character,\n      The secret must contain at least 1 uppercase alphabetical character,\n      The secret must contain at least 1 numeric character,\n      The secret must contain at least one special character,\n      The secret must be eight characters or longer.\n      ");
            let c = t.reportApiInvoke(this._sessionId, {name: E.SET_ENCRYPTION_CONFIG, options: [a], tag: D.TRACER});
            this._encryptionMode = a;
            this._encryptionSecret = b;
            c.onSuccess()
        }

        async renewToken(a) {
            La(a, "token", 1, 2047);
            let b = t.reportApiInvoke(this._sessionId, {name: E.RENEW_TOKEN, options: [a], tag: D.TRACER});
            if (!this._key) return a = new p(n.INVALID_OPERATION, "renewToken should not be called before user join"), b.onError(a), a.throw();
            this._key = a;
            try {
                await this._gateway.renewToken(a)
            } catch (c) {
                throw b.onError(c), k.error("[".concat(this._clientId,
                    "] renewToken failed"), c.toString()), c;
            }
            k.debug("[".concat(this._clientId, "] renewToken success"));
            b.onSuccess()
        }

        enableAudioVolumeIndicator() {
            let a = t.reportApiInvoke(this._sessionId, {
                name: E.ENABLE_AUDIO_VOLUME_INDICATOR,
                options: [],
                tag: D.TRACER
            });
            if (this._audioVolumeIndicationInterval) return k.warning("you have already enabled audio volume indicator!"), a.onSuccess();
            this._audioVolumeIndicationInterval = window.setInterval(() => {
                var a, c, e;
                let g = ed(a = C(c = Ib(gc(e = this._remoteStream).call(e))).call(c, a =>
                    ({
                        level: a.user.audioTrack ? 100 * a.user.audioTrack._source.getAudioAvgLevel() : 0,
                        uid: a.getUserId()
                    }))).call(a, (a, b) => a.level - b.level);
                this._highStream && this._highStream.audioTrack && (g.push({
                    level: 100 * this._highStream.audioTrack._source.getAudioAvgLevel(),
                    uid: this._highStream._userId
                }), g = ed(g).call(g, (a, b) => a.level - b.level));
                this.emit(P.VOLUME_INDICATOR, g)
            }, u.AUDIO_VOLUME_INDICATION_INTERVAL || 2E3);
            a.onSuccess()
        }

        getRTCStats() {
            let a = this._statsCollector.getRTCStats(), b = this._gateway.getInChannelInfo();
            return a.Duration =
                Math.round(b.duration / 1E3), a
        }

        startLiveStreaming(a, b) {
            let c = t.reportApiInvoke(this._sessionId, {name: E.START_LIVE_STREAMING, options: [a, b], tag: D.TRACER});
            if (!b) {
                if ("h264" !== this._codec) return a = new p(n.LIVE_STREAMING_INVALID_RAW_STREAM, "raw streaming is only support h264"), c.onError(a), v.reject(a);
                if (!this._highStream) return a = new p(n.LIVE_STREAMING_INVALID_RAW_STREAM, "can not find stream to raw streaming"), c.onError(a), v.reject(a)
            }
            if (this._liveRawStreamingClient && this._liveRawStreamingClient.hasUrl(a) ||
                this._liveTranscodeStreamingClient && this._liveTranscodeStreamingClient.hasUrl(a)) return a = new p(n.LIVE_STREAMING_TASK_CONFLICT), c.onError(a), v.reject(a);
            b = b ? na.TRANSCODE : na.RAW;
            return this._createLiveStreamingClient(b).startLiveStreamingTask(a, b).then(() => c.onSuccess()).catch(a => {
                throw c.onError(a), a;
            })
        }

        setLiveTranscoding(a) {
            let b = t.reportApiInvoke(this._sessionId, {name: E.SET_LIVE_TRANSCODING, options: [a], tag: D.TRACER});
            return this._createLiveStreamingClient(na.TRANSCODE).setTranscodingConfig(a).then(() =>
                b.onSuccess()).catch(a => {
                throw b.onError(a), a;
            })
        }

        stopLiveStreaming(a) {
            var b;
            let c = t.reportApiInvoke(this._sessionId, {name: E.STOP_LIVE_STREAMING, options: [a], tag: D.TRACER}),
                e = N(b = [this._liveRawStreamingClient, this._liveTranscodeStreamingClient]).call(b, b => b && b.hasUrl(a));
            return e.length ? v.all(C(e).call(e, b => b && b.stopLiveStreamingTask(a))).then(() => c.onSuccess()).catch(a => {
                throw c.onError(a), a;
            }) : (b = new p(n.INVALID_PARAMS, "can not find live streaming url to stop"), c.onError(b), v.reject(b))
        }

        async addInjectStreamUrl(a,
                                 b) {
            let c = t.reportApiInvoke(this._sessionId, {name: E.ADD_INJECT_STREAM_URL, options: [a, b], tag: D.TRACER});
            try {
                if (!this._joinInfo) throw new p(n.INVALID_OPERATION, "can not addInjectStreamUrl, no joininfo");
                let c = this._createLiveStreamingClient(na.INJECT);
                c.setInjectStreamConfig(b, 0);
                await c.startLiveStreamingTask(a, na.INJECT)
            } catch (e) {
                throw c.onError(e), e;
            }
            c.onSuccess()
        }

        async removeInjectStreamUrl() {
            let a = t.reportApiInvoke(this._sessionId, {name: E.REMOVE_INJECT_STREAM_URL, options: [], tag: D.TRACER});
            try {
                var b,
                    c;
                let a = this._createLiveStreamingClient(na.INJECT),
                    g = R(b = Ib(gc(c = a.streamingTasks).call(c))).call(b, a => a.mode === na.INJECT);
                if (!this._joinInfo || !g) throw new p(n.INVALID_OPERATION, "can remove addInjectStreamUrl, no joininfo or inject task");
                await a.stopLiveStreamingTask(g.url)
            } catch (e) {
                throw a.onError(e), e;
            }
            a.onSuccess()
        }

        async startChannelMediaRelay(a) {
            let b = t.reportApiInvoke(this._sessionId, {
                name: E.START_CHANNEL_MEDIA_RELAY,
                options: [a],
                tag: D.TRACER
            });
            try {
                Oh(a), await this._createChannelMediaRelayClient().startChannelMediaRelay(a)
            } catch (c) {
                return b.onError(c),
                    c.throw()
            }
            b.onSuccess()
        }

        async updateChannelMediaRelay(a) {
            let b = t.reportApiInvoke(this._sessionId, {
                name: E.UPDATE_CHANNEL_MEDIA_RELAY,
                options: [a],
                tag: D.TRACER
            });
            try {
                Oh(a), await this._createChannelMediaRelayClient().updateChannelMediaRelay(a)
            } catch (c) {
                return b.onError(c), c.throw()
            }
            b.onSuccess()
        }

        async stopChannelMediaRelay() {
            let a = t.reportApiInvoke(this._sessionId, {name: E.STOP_CHANNEL_MEDIA_RELAY, options: [], tag: D.TRACER});
            try {
                await this._createChannelMediaRelayClient().stopChannelMediaRelay()
            } catch (b) {
                return a.onError(b),
                    b.throw()
            }
            a.onSuccess()
        }

        sendStreamMessage(a) {
            if (!this._joinInfo) throw new p(n.INVALID_OPERATION, "can not send data stream, not joined");
            "string" == typeof a && (a = (new TextEncoder).encode(a));
            if (1024 < (new Blob([a])).size) throw new p(n.INVALID_PARAMS, "stream message out of range.");
            return this._gateway.signal.request(da.DATA_STREAM, {payload: Be(a)})
        }

        sendMetadata(a) {
            if (!this._joinInfo) throw new p(n.INVALID_OPERATION, "can not send metadata, not joined");
            if (1024 < (new Blob([a])).size) throw new p(n.METADATA_OUT_OF_RANGE);
            return this._gateway.signal.request(da.SEND_METADATA, {session_id: this._joinInfo.sid, metadata: Be(a)})
        }

        async sendCustomReportMessage(a) {
            mc(a) || (a = [a]);
            q(a).call(a, dl);
            let b = t.reportApiInvoke(this._sessionId, {
                name: E.SEND_CUSTOM_REPORT_MESSAGE,
                options: [],
                tag: D.TRACER
            });
            if (!this._joinInfo) return a = new p(n.INVALID_OPERATION, "can not send custom report, not joined"), b.onError(a), a.throw();
            await t.sendCustomReportMessage(this._joinInfo.sid, a)
        }

        getLocalAudioStats() {
            return this._highStream ? this._statsCollector.getLocalAudioTrackStats(this._highStream.connectionId) :
                he
        }

        getRemoteAudioStats() {
            var a;
            let b = {};
            return q(a = this._remoteStream).call(a, (a, e) => {
                b[e] = this._statsCollector.getRemoteAudioTrackStats(a.connectionId)
            }), b
        }

        getLocalVideoStats() {
            return this._highStream ? this._statsCollector.getLocalVideoTrackStats(this._highStream.connectionId) : ie
        }

        getRemoteVideoStats() {
            var a;
            let b = {};
            return q(a = this._remoteStream).call(a, (a, e) => {
                b[e] = this._statsCollector.getRemoteVideoTrackStats(a.connectionId)
            }), b
        }

        getRemoteNetworkQuality() {
            var a;
            let b = {};
            return q(a = this._remoteStream).call(a,
                (a, e) => {
                    b[e] = this._statsCollector.getRemoteNetworkQualityStats(a.connectionId)
                }), b
        }

        async pickSVCLayer(a, b) {
            Ka(b.spatialLayer, "spatialLayer", [1, 2, 3]);
            Ka(b.temporalLayer, "temporalLayer", [1, 2, 3]);
            try {
                await this._gateway.pickSVCLayer(a, b)
            } catch (c) {
                throw k.error("[".concat(this._clientId, "] pick SVC layer failed"), c.toString()), c;
            }
        }

        _reset() {
            var a, b, c;
            k.debug("[".concat(this._clientId, "] reset client"));
            this._axiosCancelSource.cancel();
            this._axiosCancelSource = Bb.CancelToken.source();
            this._streamFallbackTypeCacheMap =
                new ba;
            this._remoteStreamTypeCacheMap = new ba;
            this._configDistribute.stopGetConfigDistribute();
            this._defaultStreamFallbackType = this._proxyServer = this._joinInfo = void 0;
            this._sessionId = null;
            this._statsCollector.reset();
            this._channelName = this._uid = this._appId = this._key = void 0;
            q(a = this._users).call(a, a => {
                a.audioTrack && (a.audioTrack.stop(), a.audioTrack._isDestroyed = !0);
                a.videoTrack && (a.videoTrack.stop(), a.videoTrack._isDestroyed = !0)
            });
            this._users = [];
            this._audioVolumeIndicationInterval && (window.clearInterval(this._audioVolumeIndicationInterval),
                this._audioVolumeIndicationInterval = void 0);
            this._highStream && (this._highStream.closeP2PConnection(!0), this._highStream = void 0);
            q(b = this._bindEnabledTracks).call(b, a => {
                a.off(L.NEED_ADD_TRACK, this._handleLocalTrackEnable);
                a.off(L.NEED_REMOVE_TRACK, this._handleLocalTrackDisable)
            });
            this._bindEnabledTracks = [];
            this._lowStream && (this._lowStream.closeP2PConnection(!0), this._lowStream = void 0);
            q(c = this._remoteStream).call(c, a => {
                a.closeP2PConnection(!0)
            });
            this._remoteStream = new ba;
            this._publishMutex = new Ob("client-publish");
            this._subscribeMutex = new ba;
            this._networkQualityInterval && (window.clearInterval(this._networkQualityInterval), this._networkQualityInterval = void 0);
            this._injectStreamingClient && (this._injectStreamingClient.terminate(), this._injectStreamingClient.removeAllListeners(), this._injectStreamingClient = void 0);
            this._liveRawStreamingClient && (this._liveRawStreamingClient.terminate(), this._liveRawStreamingClient.removeAllListeners(), this._liveRawStreamingClient = void 0);
            this._liveTranscodeStreamingClient && (this._liveTranscodeStreamingClient.terminate(),
                this._liveTranscodeStreamingClient.removeAllListeners(), this._liveTranscodeStreamingClient = void 0);
            this._channelMediaRelayClient && (this._channelMediaRelayClient.dispose(), this._channelMediaRelayClient = void 0)
        }

        _startSession(a, b) {
            var c, e, g;
            let h = a || pa(32, "").toUpperCase();
            a ? k.debug(l(c = "[".concat(this._clientId, "] new Session ")).call(c, h)) : k.debug(l(e = l(g = "[".concat(this._clientId, "] renewSession ")).call(g, this._sessionId, " => ")).call(e, h));
            this._sessionId = h;
            b ? t.sessionInit(this._sessionId, {
                lts: (new Date).getTime(),
                cname: b.channel, appid: b.appId, mode: this._mode
            }) : this._joinInfo ? t.sessionInit(this._sessionId, {
                lts: (new Date).getTime(),
                cname: this._joinInfo.cname,
                appid: this._joinInfo.appId,
                mode: this._mode
            }) : this._gateway.joinInfo && t.sessionInit(this._sessionId, {
                lts: (new Date).getTime(),
                cname: this._gateway.joinInfo.cname,
                appid: this._gateway.joinInfo.appId,
                mode: this._mode
            });
            this._joinInfo && (this._joinInfo.sid = h);
            this._gateway.joinInfo && (this._gateway.joinInfo.sid = h)
        }

        async _publishHighStream(a) {
            if (!this._joinInfo) throw new p(n.INVALID_OPERATION,
                "Can't publish stream, haven't joined yet!");
            if ("CONNECTED" !== this.connectionState && "RECONNECTING" !== this.connectionState) throw new p(n.INVALID_OPERATION, "can not publish stream in ".concat(this.connectionState, " state"));
            if ("auto" === this._turnServer.mode && u.FORCE_TURN && !u.TURN_ENABLE_TCP && !u.TURN_ENABLE_UDP) throw new p(n.UNEXPECTED_ERROR, "force TURN With No TURN Configuration");
            if (k.debug("[".concat(this._clientId, "] publish high stream")), this._highStream) return await this._highStream.addTracks(a),
                this._highStream;
            this._highStream = new Ok(this._statsCollector, this._joinInfo, this._codec);
            await this._highStream.addTracks(a);
            try {
                await this._gateway.publish(this._highStream, "high")
            } catch (b) {
                throw this._highStream = void 0, b;
            }
            return this._highStream.on(I.CONNECTION_STATE_CHANGE, (a, c) => {
                this._highStream && ("connected" === a ? this.emit(P.MEDIA_RECONNECT_END, this._highStream.getUserId()) : "connecting" === a && this.emit(P.MEDIA_RECONNECT_START, this._highStream.getUserId()))
            }), this._highStream
        }

        async _publishLowStream(a) {
            if (!this._joinInfo) throw new p(n.INVALID_OPERATION,
                "Can't publish stream, haven't joined yet!");
            if ("CONNECTED" !== this.connectionState && "RECONNECTING" !== this.connectionState) throw new p(n.INVALID_OPERATION, "can not publish stream in ".concat(this.connectionState, " state"));
            if (!this._highStream || "connected" !== this._highStream.connectionState) throw new p(n.UNEXPECTED_ERROR, "Could not find high stream");
            if (this._lowStream) return (new p(n.UNEXPECTED_ERROR, "[".concat(this._clientId, "] Can't publish low stream when stream already publish"))).throw();
            k.debug("[".concat(this._clientId,
                "] publish low stream"));
            this._lowStream = new Ok(this._statsCollector, this._joinInfo, this._codec, !0);
            let b = this._configDistribute.getLowStreamConfigDistribute();
            b && b.bitrate && (this._lowStreamParameter || (this._lowStreamParameter = {
                width: 160,
                height: 120,
                framerate: 15,
                bitrate: 50
            }), this._lowStreamParameter && this._lowStreamParameter.bitrate && b.bitrate < this._lowStreamParameter.bitrate && (this._lowStreamParameter.bitrate = b.bitrate));
            this._lowStream.lowStreamParameter = this._lowStreamParameter;
            await this._lowStream.addTracks([a]);
            try {
                await this._gateway.publish(this._lowStream, "low")
            } catch (c) {
                throw this._lowStream = void 0, c;
            }
            this._highStream.lowStreamConnection = this._lowStream
        }

        _createLiveStreamingClient(a) {
            if (!this._joinInfo || !this._appId) return (new p(n.INVALID_OPERATION, "can not create live streaming client, please join channel first")).throw();
            let b = () => new Eo(this._joinInfo, this._config.websocketRetryConfig || Ra, this._config.httpRetryConfig || Ra),
                c = a => {
                    a.onLiveStreamError = (a, b) => {
                        t.reportApiInvoke(this._sessionId, {
                            name: E.ON_LIVE_STREAM_ERROR,
                            options: [a, b], tag: D.TRACER
                        }).onSuccess();
                        this.emit(P.LIVE_STREAMING_ERROR, a, b)
                    };
                    a.onLiveStreamWarning = (a, b) => {
                        t.reportApiInvoke(this._sessionId, {
                            name: E.ON_LIVE_STREAM_WARNING,
                            options: [a, b],
                            tag: D.TRACER
                        }).onSuccess();
                        this.emit(P.LIVE_STREAMING_WARNING, a, b)
                    };
                    a.on(Ec.REQUEST_WORKER_MANAGER_LIST, (a, b, c) => {
                        if (!this._joinInfo) return c(new p(n.INVALID_OPERATION, "can not find join info to get worker manager"));
                        Ah(a, this._joinInfo, this._axiosCancelSource.token, Ra).then(b).catch(c)
                    })
                };
            switch (a) {
                case na.RAW:
                    return this._liveRawStreamingClient ||
                    (this._liveRawStreamingClient = b(), c(this._liveRawStreamingClient)), this._liveRawStreamingClient;
                case na.TRANSCODE:
                    return this._liveTranscodeStreamingClient || (this._liveTranscodeStreamingClient = b(), c(this._liveTranscodeStreamingClient)), this._liveTranscodeStreamingClient;
                case na.INJECT:
                    return this._injectStreamingClient || (this._injectStreamingClient = b(), this._injectStreamingClient.on(Ec.REQUEST_WORKER_MANAGER_LIST, (a, b, c) => {
                        if (!this._joinInfo) return c(new p(n.INVALID_OPERATION, "can not find join info to get worker manager"));
                        Ah(a, this._joinInfo, this._axiosCancelSource.token, Ra).then(b).catch(c)
                    }), this._injectStreamingClient.onInjectStatusChange = (a, b, c) => {
                        this.emit(P.INJECT_STREAM_STATUS, a, b, c)
                    }), this._injectStreamingClient
            }
        }

        _createChannelMediaRelayClient() {
            return this._joinInfo ? (this._channelMediaRelayClient || (this._channelMediaRelayClient = new Go(this._joinInfo, this._clientId, this._config.websocketRetryConfig || Ra, this._config.httpRetryConfig || Ra), this._channelMediaRelayClient.on("state", a => {
                "RELAY_STATE_FAILURE" === a &&
                this._channelMediaRelayClient && this._channelMediaRelayClient.dispose();
                this.emit(P.CHANNEL_MEDIA_RELAY_STATE, a)
            }), this._channelMediaRelayClient.on("event", a => {
                this.emit(P.CHANNEL_MEDIA_RELAY_EVENT, a)
            })), this._channelMediaRelayClient) : (new p(n.INVALID_OPERATION, "can not create channel media relay client, please join channel first")).throw()
        }

        _handleGatewayEvents() {
            this._gateway.on(wa.DISCONNECT_P2P, () => {
                var a;
                k.debug("[".concat(this._clientId, "] start full reconnect"));
                this._highStream && "disconnected" !==
                this._highStream.connectionState && (k.debug("[".concat(this._clientId, "] ready to reconnect high stream")), this._highStream.readyToReconnectPC());
                this._lowStream && "disconnected" !== this._lowStream.connectionState && (k.debug("[".concat(this._clientId, "] ready to reconnect low stream")), this._lowStream.readyToReconnectPC());
                q(a = this._remoteStream).call(a, (a, c) => {
                    var b;
                    k.debug(l(b = "[".concat(this._clientId, "] ready to reconnect remote stream ")).call(b, c));
                    a.readyToReconnectPC()
                })
            });
            this._gateway.on(wa.CONNECTION_STATE_CHANGE,
                (a, b, c) => {
                    var e, g;
                    let h = () => {
                        this.emit(P.CONNECTION_STATE_CHANGE, a, b, c)
                    };
                    if (k.info(l(e = l(g = "[".concat(this._clientId, "] connection state change: ")).call(g, b, " -> ")).call(e, a)), "DISCONNECTED" === a) return this._reset(), void h();
                    var m, n;
                    if ("RECONNECTING" === a) this._highStream && "connecting" === this._highStream.connectionState && (k.debug("[".concat(this._clientId, "] ready to reconnect high stream")), this._highStream.readyToReconnectPC()), this._lowStream && "connecting" === this._lowStream.connectionState && (k.debug("[".concat(this._clientId,
                        "] ready to reconnect low stream")), this._lowStream.readyToReconnectPC()), q(m = this._remoteStream).call(m, (a, b) => {
                        var c;
                        "connecting" === a.connectionState && (k.debug(l(c = "[".concat(this._clientId, "] ready to reconnect remote stream ")).call(c, b)), a.readyToReconnectPC())
                    }), q(n = this._users).call(n, a => {
                        a._trust_in_room_ = !1;
                        a._trust_audio_enabled_state_ = !1;
                        a._trust_video_enabled_state_ = !1;
                        a._trust_audio_mute_state_ = !1;
                        a._trust_video_mute_state_ = !1;
                        a._trust_stream_added_state_ = !1
                    }), this._userOfflineTimeout &&
                    window.clearTimeout(this._userOfflineTimeout), this._streamRemovedTimeout && window.clearTimeout(this._streamRemovedTimeout), this._streamRemovedTimeout = this._userOfflineTimeout = void 0; else if ("CONNECTED" === a) {
                        var p, t;
                        q(p = this._streamFallbackTypeCacheMap).call(p, (a, b) => {
                            this._gateway.setStreamFallbackOption(b, a).catch(a => k.warning("[".concat(this._clientId, "] auto set stream fallback option failed"), a))
                        });
                        q(t = this._remoteStreamTypeCacheMap).call(t, (a, b) => {
                            this._gateway.setRemoteVideoStreamType(b, a).catch(a =>
                                k.warning("[".concat(this._clientId, "] auto set remote stream type failed"), a))
                        });
                        this._highStream && "connecting" === this._highStream.connectionState ? this._highStream.reconnectPC().then(() => {
                            this._lowStream && "connecting" === this._lowStream.connectionState && this._lowStream.reconnectPC().catch(a => {
                                k.error("[".concat(this._clientId, "] republish low stream error"), a.toString());
                                this.emit(P.ERROR, {reason: a})
                            })
                        }).catch(a => {
                            k.error("[".concat(this._clientId, "] republish high stream error"), a.toString());
                            this.emit(P.ERROR,
                                {reason: a})
                        }) : this._lowStream && "connecting" === this._lowStream.connectionState && this._lowStream.reconnectPC().catch(a => {
                            k.error("[".concat(this._clientId, "] republish low stream error"), a.toString());
                            this.emit(P.ERROR, {reason: a})
                        });
                        this._userOfflineTimeout = window.setTimeout(() => {
                            var a;
                            if ("CONNECTED" === this.connectionState) {
                                this._userOfflineTimeout = void 0;
                                var b = N(a = this._users).call(a, a => !a._trust_in_room_);
                                q(b).call(b, a => {
                                    var b;
                                    k.debug(l(b = "[".concat(this._clientId, "] user offline timeout, emit user offline ")).call(b,
                                        a.uid));
                                    this._handleUserOffline({uid: a.uid})
                                })
                            }
                        }, 3E3);
                        this._streamRemovedTimeout = window.setTimeout(() => {
                            var a;
                            "CONNECTED" === this.connectionState && (this._streamRemovedTimeout = void 0, q(a = this._users).call(a, a => {
                                var b, c, e, g, h;
                                a._trust_audio_mute_state_ || (k.debug(l(b = "[".concat(this._clientId, "] auto dispatch audio unmute event ")).call(b, a.uid)), this._handleMuteStream(a.uid, "audio", !1));
                                a._trust_video_mute_state_ || (k.debug(l(c = "[".concat(this._clientId, "] auto dispatch video unmute event ")).call(c,
                                    a.uid)), this._handleMuteStream(a.uid, "video", !1));
                                a._trust_audio_enabled_state_ || (k.debug(l(e = "[".concat(this._clientId, "] auto dispatch enable local audio ")).call(e, a.uid)), this._handleSetStreamLocalEnable("audio", a.uid, !0));
                                !a._trust_video_enabled_state_ && a._video_enabled_ && (k.debug(l(g = "[".concat(this._clientId, "] auto dispatch enable local video ")).call(g, a.uid)), this._handleSetStreamLocalEnable("video", a.uid, !0));
                                a._trust_stream_added_state_ || (k.debug(l(h = "[".concat(this._clientId, "] auto dispatch stream remove ")).call(h,
                                    a.uid)), this._handleRemoveStream({uid: a.uid, uint_id: a._uintid}))
                            }))
                        }, 1E3)
                    }
                    h()
                });
            this._gateway.on(wa.REQUEST_NEW_GATEWAY_LIST, (a, b) => {
                if (!this._joinInfo) return b(new p(n.UNEXPECTED_ERROR, "can not recover, no join info"));
                yh(this._joinInfo, this._axiosCancelSource.token, this._config.httpRetryConfig || Ra).then(b => {
                    var c;
                    this._joinInfo && (this._joinInfo.apResponse = b.gatewayInfo.res);
                    a(C(c = b.gatewayInfo.gatewayAddrs).call(c, a => {
                        if (this._joinInfo && this._joinInfo.proxyServer) {
                            var b, c;
                            a = a.split(":");
                            return l(b =
                                l(c = "wss://".concat(this._joinInfo.proxyServer, "/ws/?h=")).call(c, a[0], "&p=")).call(b, a[1])
                        }
                        return "wss://".concat(a)
                    }))
                }).catch(b)
            });
            this._gateway.on(wa.NETWORK_QUALITY, a => {
                "normal" === this._networkQualitySensitivity && this.emit(P.NETWORK_QUALITY, a)
            });
            this._gateway.on(wa.STREAM_TYPE_CHANGE, (a, b) => {
                this.emit(P.STREAM_TYPE_CHANGED, a, b);
                t.reportApiInvoke(this._sessionId, {
                    name: E.STREAM_TYPE_CHANGE,
                    options: [a, b],
                    tag: D.TRACER
                }).onSuccess(A({uid: a, streamType: b}))
            });
            this._gateway.on(wa.IS_P2P_DISCONNECTED,
                a => {
                    var b, c, e;
                    let g = [];
                    return this._highStream && g.push(this._highStream), q(b = this._remoteStream).call(b, a => g.push(a)), 0 === g.length || 0 === N(g).call(g, a => "connected" === a.connectionState).length ? a(!0) : (k.debug(l(c = "[".concat(this._clientId, "] ")).call(c, C(e = N(g).call(g, a => "connected" === a.connectionState)).call(e, a => a.connectionId), " is connected")), void a(!1))
                });
            this._gateway.on(wa.NEED_RENEW_SESSION, () => {
                this._startSession()
            });
            this._gateway.signal.on(U.ON_USER_ONLINE, this._handleUserOnline);
            this._gateway.signal.on(U.ON_USER_OFFLINE,
                this._handleUserOffline);
            this._gateway.signal.on(U.ON_ADD_AUDIO_STREAM, a => this._handleAddAudioOrVideoStream("audio", a.uid, a.uint_id));
            this._gateway.signal.on(U.ON_ADD_VIDEO_STREAM, a => this._handleAddAudioOrVideoStream("video", a.uid, a.uint_id));
            this._gateway.signal.on(U.ON_REMOVE_STREAM, this._handleRemoveStream);
            this._gateway.signal.on(U.ON_P2P_LOST, this._handleP2PLost);
            this._gateway.signal.on(U.MUTE_AUDIO, a => this._handleMuteStream(a.uid, "audio", !0));
            this._gateway.signal.on(U.UNMUTE_AUDIO, a => this._handleMuteStream(a.uid,
                "audio", !1));
            this._gateway.signal.on(U.MUTE_VIDEO, a => this._handleMuteStream(a.uid, "video", !0));
            this._gateway.signal.on(U.UNMUTE_VIDEO, a => this._handleMuteStream(a.uid, "video", !1));
            this._gateway.signal.on(U.RECEIVE_METADATA, a => {
                let b = ih(a.metadata);
                this.emit(P.RECEIVE_METADATA, a.uid, b)
            });
            this._gateway.signal.on(U.ON_DATA_STREAM, a => {
                a.seq && delete a.seq;
                a.payload = ih(a.payload);
                this.emit(P.STREAM_MESSAGE, a.uid, a.payload);
                this.onStreamMessage && this.onStreamMessage(a)
            });
            this._gateway.signal.on(U.ON_CRYPT_ERROR,
                () => {
                    Pc(() => {
                        k.warning("[".concat(this._clientId, "] on crypt error"));
                        this.emit(P.CRYPT_ERROR)
                    }, this._sessionId)
                });
            this._gateway.signal.on(U.ON_TOKEN_PRIVILEGE_WILL_EXPIRE, this._handleTokenWillExpire);
            this._gateway.signal.on(U.ON_TOKEN_PRIVILEGE_DID_EXPIRE, () => {
                k.warning("[".concat(this._clientId, "] received message onTokenPrivilegeDidExpire, please get new token and join again"));
                this._reset();
                this._gateway.leave(!0);
                this.emit(P.ON_TOKEN_PRIVILEGE_DID_EXPIRE)
            });
            this._gateway.signal.on(U.ON_STREAM_FALLBACK_UPDATE,
                a => {
                    var b, c;
                    k.debug(l(b = l(c = "[".concat(this._clientId, "] stream fallback peerId: ")).call(c, a.stream_id, ", attr: ")).call(b, a.stream_type));
                    this.emit(P.STREAM_FALLBACK, a.stream_id, 1 === a.stream_type ? "fallback" : "recover")
                });
            this._gateway.signal.on(U.ON_PUBLISH_STREAM, a => {
                var b;
                this.uid === this._uid && (this._highStream && this._highStream._waitingSuccessResponse && "connected" === this._highStream.connectionState ? (this._highStream.reportPublishEvent(!0, null, A({proxy: a.proxy})), k.info(l(b = "[".concat(this._clientId,
                    "] on publish stream, ")).call(b, A(a))), void 0 !== a.proxy && this.emit(P.IS_USING_CLOUD_PROXY, !!a.proxy)) : this._lowStream ? this._lowStream.reportPublishEvent(!0, null, A({proxy: a.proxy})) : k.warning("get on_publish_stream message but cannot handle"))
            });
            this._gateway.signal.on(U.ENABLE_LOCAL_VIDEO, a => {
                this._handleSetStreamLocalEnable("video", a.uid, !0)
            });
            this._gateway.signal.on(U.DISABLE_LOCAL_VIDEO, a => {
                this._handleSetStreamLocalEnable("video", a.uid, !1)
            });
            this._gateway.signal.on(Q.REQUEST_TIMEOUT, (a, b) => {
                if (this._joinInfo) switch (a) {
                    case da.PUBLISH:
                        var c;
                        if (!b) break;
                        a = "high" === b.stream_type ? this._highStream : this._lowStream;
                        if (!a) break;
                        "offer" === b.state && t.publish(this._joinInfo.sid, {
                            lts: a.startTime,
                            succ: !1,
                            ec: n.TIMEOUT,
                            audio: b.audio,
                            video: b.video,
                            p2pid: b.p2p_id,
                            publishRequestid: a.ID,
                            screenshare: !(!a.videoTrack || -1 === G(c = a.videoTrack._hints).call(c, tb.SCREEN_TRACK)),
                            audioName: a.audioTrack && a.audioTrack.getTrackLabel(),
                            videoName: a.videoTrack && a.videoTrack.getTrackLabel()
                        });
                        break;
                    case da.SUBSCRIBE:
                        (c = this._remoteStream.get(b.stream_id)) && b && t.subscribe(this._joinInfo.sid,
                            {
                                lts: c.startTime,
                                succ: !1,
                                ec: n.TIMEOUT,
                                audio: !!b.audio,
                                video: !!b.video,
                                peerid: b.stream_id,
                                subscribeRequestid: c.ID,
                                p2pid: c.pc.ID
                            })
                }
            })
        }
    }

    Qk([rh(), Hc("design:type", Function), Hc("design:paramtypes", [Object]), Hc("design:returntype", void 0)], ag.prototype, "setTurnServer", null);
    Qk([function (a = {report: t}) {
        return function (b, c, e) {
            let g = b[c];
            if ("function" == typeof g) {
                let h = "AgoraRTCClient" === b.constructor.name ? "Client" : b.constructor.name;
                e.value = async function (...b) {
                    var e;
                    let k = a.report.reportApiInvoke(this._sessionId ||
                        null, {name: l(e = "".concat(h, ".")).call(e, c), options: b, tag: D.TRACER});
                    try {
                        let a = await g.apply(this, b);
                        return k.onSuccess(), a
                    } catch (y) {
                        throw k.onError(y), y;
                    }
                }
            }
            return e
        }
    }({report: t}), Hc("design:type", Function), Hc("design:paramtypes", [Object, Object]), Hc("design:returntype", v)], ag.prototype, "pickSVCLayer", null);

    class Io extends tk {
        constructor(a, b = {}) {
            super();
            this.currentLoopCount = this.pausePlayTime = this.startPlayOffset = this.startPlayTime = 0;
            this._currentState = "stopped";
            this.audioBuffer = a;
            this.options = b;
            this.startPlayOffset = this.options.startPlayTime || 0
        }

        get currentState() {
            return this._currentState
        }

        set currentState(a) {
            a !== this._currentState && (this._currentState = a, this.emit(jb.AUDIO_SOURCE_STATE_CHANGE, this._currentState))
        }

        get duration() {
            return this.audioBuffer.duration
        }

        get currentTime() {
            return "stopped" === this.currentState ? 0 : "paused" === this.currentState ? this.pausePlayTime : (this.context.currentTime - this.startPlayTime + this.startPlayOffset) % this.audioBuffer.duration
        }

        createWebAudioDiagram() {
            return this.context.createGain()
        }

        updateOptions(a) {
            "stopped" ===
            this.currentState ? (this.options = a, this.startPlayOffset = this.options.startPlayTime || 0) : k.warning("can not set audio source options")
        }

        startProcessAudioBuffer() {
            this.sourceNode && this.stopProcessAudioBuffer();
            this.sourceNode = this.createSourceNode();
            this.startSourceNode();
            this.currentState = "playing"
        }

        pauseProcessAudioBuffer() {
            this.sourceNode && "playing" === this.currentState && (this.pausePlayTime = this.currentTime, this.sourceNode.onended = null, this.sourceNode.stop(), this.sourceNode.buffer = null, this.sourceNode =
                this.createSourceNode(), this.currentState = "paused")
        }

        seekAudioBuffer(a) {
            this.sourceNode && (this.sourceNode.onended = null, "playing" === this.currentState && this.sourceNode.stop(), this.sourceNode = this.createSourceNode(), "playing" === this.currentState ? (this.startPlayOffset = a, this.startSourceNode()) : "paused" === this.currentState && (this.pausePlayTime = a))
        }

        resumeProcessAudioBuffer() {
            "paused" === this.currentState && this.sourceNode && (this.startPlayOffset = this.pausePlayTime, this.pausePlayTime = 0, this.startSourceNode(),
                this.currentState = "playing")
        }

        stopProcessAudioBuffer() {
            if (this.sourceNode) {
                this.sourceNode.onended = null;
                try {
                    this.sourceNode.stop()
                } catch (a) {
                }
                this.reset()
            }
        }

        startSourceNode() {
            var a;
            this.sourceNode && this.sourceNode.buffer && (this.sourceNode.start(0, this.startPlayOffset), this.startPlayTime = this.context.currentTime, this.sourceNode.onended = ta(a = this.handleSourceNodeEnded).call(a, this))
        }

        createSourceNode() {
            let a = this.context.createBufferSource();
            return a.buffer = this.audioBuffer, a.loop = !!this.options.loop, a.connect(this.outputNode),
                a
        }

        handleSourceNodeEnded() {
            if (this.currentLoopCount += 1, this.options.cycle && this.options.cycle > this.currentLoopCount) return this.startPlayOffset = 0, this.sourceNode = void 0, void this.startProcessAudioBuffer();
            this.reset()
        }

        reset() {
            this.startPlayOffset = this.options.startPlayTime || 0;
            this.currentState = "stopped";
            this.sourceNode && (this.sourceNode.disconnect(), this.sourceNode = void 0);
            this.currentLoopCount = 0
        }
    }

    let Rh = new ba;
    var Jo = ha.setInterval;
    let Rk = ka().name;

    class pd {
        constructor(a, b) {
            this.id = 0;
            pd.count += 1;
            this.id = pd.count;
            this.element = a;
            this.context = b
        }

        initPeers() {
            this.peerPair = [new RTCPeerConnection, new RTCPeerConnection];
            this.peerPair[1].ontrack = a => {
                let b = document.createElement("audio");
                b.srcObject = new MediaStream([a.track]);
                b.play();
                this.audioPlayerElement = b
            }
        }

        async switchSdp() {
            if (this.peerPair) {
                var a = async (a, b) => {
                    b = "offer" === b ? await a.createOffer() : await a.createAnswer();
                    return await a.setLocalDescription(b), "complete" === a.iceGatheringState ? a.localDescription : new v(b => {
                        a.onicegatheringstatechange =
                            () => {
                                "complete" === a.iceGatheringState && b(a.localDescription)
                            }
                    })
                }, b = async (a, b) => await a.setRemoteDescription(b);
                try {
                    let c = await a(this.peerPair[0], "offer");
                    await b(this.peerPair[1], c);
                    let e = await a(this.peerPair[1], "answer");
                    await b(this.peerPair[0], e)
                } catch (c) {
                    throw(new p(n.LOCAL_AEC_ERROR, c.toString())).print();
                }
            }
        }

        async getTracksFromMediaElement(a) {
            if (this.audioTrack) return this.audioTrack;
            let b;
            try {
                a instanceof HTMLVideoElement && (a.captureStream ? a.captureStream() : a.mozCaptureStream()), b = this.context.createMediaStreamDestination(),
                    this.context.createMediaElementSource(a).connect(b)
            } catch (c) {
                throw(new p(n.LOCAL_AEC_ERROR, c.toString())).print();
            }
            if (!b) throw(new p(n.LOCAL_AEC_ERROR, "no dest node when local aec")).print();
            a = b.stream.getAudioTracks()[0];
            return this.audioTrack = a, a
        }

        getElement() {
            return this.element
        }

        async startEchoCancellation() {
            this.context.resume();
            this.peerPair && this.close();
            this.initPeers();
            let a = await this.getTracksFromMediaElement(this.element);
            this.peerPair && this.peerPair[0].addTrack(a);
            await this.switchSdp()
        }

        close() {
            var a;
            k.debug("close echo cancellation unit, id is", this.id);
            this.audioPlayerElement && this.audioPlayerElement.pause();
            this.peerPair && q(a = this.peerPair).call(a, a => {
                a.close()
            });
            this.audioPlayerElement = this.peerPair = void 0
        }
    }

    pd.count = 0;
    var bg = function (a, b) {
        if ("object" == typeof Reflect && "function" == typeof Reflect.metadata) return Reflect.metadata(a, b)
    };
    let Ko = window.AudioContext || window.webkitAudioContext;

    class Sk {
        constructor() {
            this.units = [];
            this._doesEnvironmentNeedAEC() && (this.context = new Ko)
        }

        startProcessingLocalAEC(a) {
            var b;
            if (!this.context || !this._doesEnvironmentNeedAEC()) return k.debug("the system does not need to process local aec"), -1;
            let c = R(b = this.units).call(b, b => b && b.getElement() === a);
            return c || (c = new pd(a, this.context), this.units.push(c)), c.startEchoCancellation(), k.debug("start processing local audio echo cancellation, id is", c.id), c.id
        }

        _doesEnvironmentNeedAEC() {
            return ka().name !== Z.SAFARI
        }
    }

    (function (a, b, c, e) {
        var g, h = arguments.length, k = 3 > h ? b : null === e ? e = Y(b, c) : e;
        if ("object" == typeof Reflect && "function" == typeof Reflect.decorate) k =
            Reflect.decorate(a, b, c, e); else for (var l = a.length - 1; 0 <= l; l--) (g = a[l]) && (k = (3 > h ? g(k) : 3 < h ? g(b, c, k) : g(b, c)) || k);
        return 3 < h && k && X(b, c, k), k
    })([rh({report: t}), bg("design:type", Function), bg("design:paramtypes", [HTMLAudioElement]), bg("design:returntype", Number)], Sk.prototype, "startProcessingLocalAEC", null);
    let Lo = new Sk;
    var Tk, Uk, Vk, Wk;
    jc("PROCESS_ID", l(Tk = l(Uk = l(Vk = l(Wk = "process-".concat(pa(8, ""), "-")).call(Wk, pa(4, ""), "-")).call(Vk, pa(4, ""), "-")).call(Uk, pa(4, ""), "-")).call(Tk, pa(12, "")));
    (function () {
        let a =
            ka();
        var b = navigator.mediaDevices && navigator.mediaDevices.getDisplayMedia ? !0 : !1;
        ca.getDisplayMedia = b;
        ca.getStreamFromExtension = a.name === Z.CHROME && 34 < Number(a.version);
        ca.supportUnifiedPlan = function () {
            if (!(window.RTCRtpTransceiver && "currentDirection" in RTCRtpTransceiver.prototype)) return !1;
            let a = new RTCPeerConnection, b = !1;
            try {
                a.addTransceiver("audio"), b = !0
            } catch (g) {
            }
            return a.close(), b
        }();
        ca.supportMinBitrate = a.name === Z.CHROME || a.name === Z.EDGE;
        ca.supportSetRtpSenderParameters = function () {
            let a = ka();
            return window.RTCRtpSender && window.RTCRtpSender.prototype.setParameters && window.RTCRtpSender.prototype.getParameters ? !!Bd() || a.name === Z.SAFARI || a.name === Z.FIREFOX && 64 <= Number(a.version) : !1
        }();
        a.name !== Z.SAFARI && ka().name !== Z.WECHAT || (ca.supportDualStream = !1);
        ca.webAudioMediaStreamDest = function () {
            let a = ka();
            return a.name === Z.SAFARI && 12 > Number(a.version) ? !1 : !0
        }();
        ca.supportReplaceTrack = window.RTCRtpSender ? "function" == typeof RTCRtpSender.prototype.replaceTrack ? !0 : !1 : !1;
        ca.supportWebGL = "undefined" != typeof WebGLRenderingContext;
        ca.supportRequestFrame = !!window.CanvasCaptureMediaStreamTrack;
        Bd() || (ca.webAudioWithAEC = !0);
        ca.supportShareAudio = function () {
            let a = ka();
            return (a.os === W.WIN_10 || a.os === W.WIN_81 || a.os === W.WIN_7 || a.os === W.LINUX || a.os === W.MAC_OS || a.os === W.MAC_OS_X) && a.name === Z.CHROME && 74 <= Number(a.version) ? !0 : !1
        }();
        ca.supportDualStreamEncoding = function () {
            let a = ka();
            return a.name === Z.CHROME && 87 === Number(a.version)
        }();
        k.info("browser compatibility", A(ca), A(a))
    })();
    let vb = {
        VERSION: Va, BUILD: "v4.4.0-0-g48538343(4/2/2021, 5:44:00 PM)",
        setParameter: jc, getParameter: function (a) {
            return u[a]
        }, getSupportedCodec: async function (a) {
            let b = null;
            a ? (b = new Ck({}), b.addStream(a)) : b = new Dk({});
            {
                a = await b.createOfferSDP();
                const c = {video: [], audio: []};
                a = (a.match(/ VP8/i) && c.video.push("VP8"), a.match(/ VP9/i) && c.video.push("VP9"), a.match(/ AV1X/i) && c.video.push("AV1"), a.match(/ H264/i) && c.video.push("H264"), a.match(/ opus/i) && c.audio.push("OPUS"), c)
            }
            return b.close(), a
        }, checkSystemRequirements: function () {
            const a = t.reportApiInvoke(null, {
                name: E.CHECK_SYSTEM_REQUIREMENTS,
                options: [], tag: D.TRACER
            });
            var b = !1;
            try {
                var c = navigator.mediaDevices && navigator.mediaDevices.getUserMedia, e = window.WebSocket;
                b = !!(window.RTCPeerConnection && c && e)
            } catch (g) {
                return k.error("check system requirement failed: ", g), !1
            }
            c = !1;
            e = ka();
            e.name === Z.CHROME && 58 <= Number(e.version) && e.os !== W.IOS && (c = !0);
            e.name === Z.FIREFOX && 56 <= Number(e.version) && (c = !0);
            e.name === Z.OPERA && 45 <= Number(e.version) && (c = !0);
            e.name === Z.SAFARI && 11 <= Number(e.version) && (c = !0);
            ka().name !== Z.WECHAT && ka().name !== Z.QQ || e.os === W.IOS ||
            (c = !0);
            k.debug("checkSystemRequirements, api:", b, "browser", c);
            b = b && c;
            return a.onSuccess(b), b
        }, getDevices: function (a) {
            return db.enumerateDevices(!0, !0, a)
        }, getMicrophones: function (a) {
            return db.getRecordingDevices(a)
        }, getCameras: function (a) {
            return db.getCamerasDevices(a)
        }, getElectronScreenSources: kh, getPlaybackDevices: function (a) {
            return db.getSpeakers(a)
        }, createClient: function (a = {codec: "vp8", mode: "rtc"}) {
            const b = t.reportApiInvoke(null, {name: E.CREATE_CLIENT, options: [a], tag: D.TRACER});
            try {
                Ka(a.codec, "config.codec",
                    ["vp8", "vp9", "av1", "h264"]), Ka(a.mode, "config.mode", ["rtc", "live"]), void 0 !== a.proxyServer && La(a.proxyServer, "config.proxyServer", 1, 1E4), void 0 !== a.turnServer && Yg(a.turnServer), void 0 !== a.httpRetryConfig && Xg(a.httpRetryConfig), void 0 !== a.websocketRetryConfig && Xg(a.websocketRetryConfig), !0
            } catch (c) {
                throw b.onError(c), c;
            }
            return b.onSuccess(), new ag(qc({forceWaitGatewayResponse: !0}, a, {role: "rtc" === a.mode ? "host" : a.role}))
        }, createCameraVideoTrack: async function (a = {encoderConfig: "480p_1"}) {
            const b = t.reportApiInvoke(null,
                {tag: D.TRACER, name: E.CREATE_CAM_VIDEO_TRACK, options: [Te({}, a)]}), c = Pe(a);
            var e = pa(8, "track-");
            let g = null;
            k.info("start create camera video track with config", A(a), "trackId", e);
            try {
                g = (await zb({video: c}, e)).getVideoTracks()[0] || null
            } catch (h) {
                throw b.onError(h), h;
            }
            if (!g) return e = new p(n.UNEXPECTED_ERROR, "can not find track in media stream"), b.onError(e), e.throw();
            a.optimizationMode && Ue(e, g, a, a.encoderConfig && ic(a.encoderConfig));
            a = new Kk(g, a, c, a.scalabiltyMode ? vd(a.scalabiltyMode) : {
                numSpatialLayers: 1,
                numTemporalLayers: 1
            }, a.optimizationMode, e);
            return b.onSuccess(a.getTrackId()), k.info("create camera video success, trackId:", e), a
        }, createCustomVideoTrack: function (a) {
            const b = t.reportApiInvoke(null, {tag: D.TRACER, name: E.CREATE_CUSTOM_VIDEO_TRACK, options: [a]}),
                c = new Na(a.mediaStreamTrack, {
                    bitrateMax: a.bitrateMax,
                    bitrateMin: a.bitrateMin
                }, a.scalabiltyMode ? vd(a.scalabiltyMode) : {
                    numSpatialLayers: 1,
                    numTemporalLayers: 1
                }, a.optimizationMode);
            return b.onSuccess(c.getTrackId()), k.info("create custom video track success with config",
                a, "trackId", c.getTrackId()), c
        }, createScreenVideoTrack: async function (a = {}, b = "disable") {
            const c = t.reportApiInvoke(null, {
                tag: D.TRACER,
                name: E.CREATE_SCREEN_VIDEO_TRACK,
                options: [Te({}, a), b]
            });
            a.encoderConfig ? "string" == typeof a.encoderConfig || a.encoderConfig.width && a.encoderConfig.height || (a.encoderConfig.width = {max: 1920}, a.encoderConfig.height = {max: 1080}) : a.encoderConfig = "1080p_2";
            var e = {};
            a.screenSourceType && (e.mediaSource = a.screenSourceType);
            a.extensionId && Lc() && (e.extensionId = a.extensionId);
            a.electronScreenSourceId &&
            (e.sourceId = a.electronScreenSourceId);
            var g = a.encoderConfig ? se(a.encoderConfig) : null;
            g = (e.mandatory = {
                chromeMediaSource: "desktop",
                maxWidth: g ? g.width : void 0,
                maxHeight: g ? g.height : void 0
            }, g && g.frameRate && ("number" == typeof g.frameRate ? (e.mandatory.maxFrameRate = g.frameRate, e.mandatory.minFrameRate = g.frameRate) : (e.mandatory.maxFrameRate = g.frameRate.max || g.frameRate.ideal || g.frameRate.exact || void 0, e.mandatory.minFrameRate = g.frameRate.min || g.frameRate.ideal || g.frameRate.exact || void 0), e.frameRate = g.frameRate),
            g && g.width && (e.width = g.width), g && g.height && (e.height = g.height), e);
            const h = pa(8, "track-");
            let l = null;
            e = null;
            const q = ca;
            if (!q.supportShareAudio && "enable" === b) return a = new p(n.NOT_SUPPORTED, "your browser or platform is not support share-screen with audio"), c.onError(a), a.throw();
            k.info("start create screen video track with config", a, "withAudio", b, "trackId", h);
            try {
                const a = await zb({screen: g, screenAudio: "auto" === b ? q.supportShareAudio : "enable" === b}, h);
                l = a.getVideoTracks()[0] || null;
                e = a.getAudioTracks()[0] ||
                    null
            } catch (w) {
                throw c.onError(w), w;
            }
            if (!l) return a = new p(n.UNEXPECTED_ERROR, "can not find track in media stream"), c.onError(a), a.throw();
            if (!e && "enable" === b) return l && l.stop(), a = new p(n.SHARE_AUDIO_NOT_ALLOWED), c.onError(a), a.throw();
            a.optimizationMode || (a.optimizationMode = "detail");
            a.optimizationMode && (Ue(h, l, a, a.encoderConfig && se(a.encoderConfig)), a.encoderConfig && "string" != typeof a.encoderConfig && (a.encoderConfig.bitrateMin = a.encoderConfig.bitrateMax));
            a = new Na(l, a.encoderConfig ? se(a.encoderConfig) :
                {}, a.scalabiltyMode ? vd(a.scalabiltyMode) : {
                numSpatialLayers: 1,
                numTemporalLayers: 1
            }, a.optimizationMode, h);
            if (a._hints.push(tb.SCREEN_TRACK), !e) return c.onSuccess(a.getTrackId()), k.info("create screen video track success", "video:", a.getTrackId()), a;
            b = new $a(e);
            return c.onSuccess([a.getTrackId(), b.getTrackId()]), k.info("create screen video track success", "video:", a.getTrackId(), "audio:", b.getTrackId()), [a, b]
        }, createMicrophoneAndCameraTracks: async function (a = {}, b = {encoderConfig: "480p_1"}) {
            var c, e, g;
            const h =
                    t.reportApiInvoke(null, {tag: D.TRACER, name: E.CREATE_MIC_AND_CAM_TRACKS, options: [a, b]}), m = Pe(b),
                q = Fh(a), u = pa(8, "track-"), v = pa(8, "track-");
            let x = null, B = null;
            k.info(l(c = l(e = l(g = "start create camera video track(".concat(v, ") and microphone audio track(")).call(g, u, ") with config, audio: ")).call(e, A(a), ", video: ")).call(c, A(b)));
            try {
                var C;
                const a = await zb({audio: q, video: m}, l(C = "".concat(u, "-")).call(C, v));
                x = a.getAudioTracks()[0];
                B = a.getVideoTracks()[0]
            } catch (ja) {
                throw h.onError(ja), ja;
            }
            if (!x || !B) {
                var z =
                    new p(n.UNEXPECTED_ERROR, "can not find tracks in media stream");
                return h.onError(z), z.throw()
            }
            b.optimizationMode && Ue(v, B, b, b.encoderConfig && ic(b.encoderConfig));
            a = new Wf(x, a, q, u);
            b = new Kk(B, b, m, b.scalabiltyMode ? vd(b.scalabiltyMode) : {
                numSpatialLayers: 1,
                numTemporalLayers: 1
            }, b.optimizationMode, v);
            return h.onSuccess([a.getTrackId(), b.getTrackId()]), k.info(l(z = "create camera video track(".concat(v, ") and microphone audio track(")).call(z, u, ") success")), [a, b]
        }, createMicrophoneAudioTrack: async function (a =
                                                           {}) {
            const b = t.reportApiInvoke(null, {tag: D.TRACER, name: E.CREATE_MIC_AUDIO_TRACK, options: [a]}), c = Fh(a);
            var e = pa(8, "track-");
            let g = null;
            k.info("start create microphone audio track with config", A(a), "trackId", e);
            try {
                g = (await zb({audio: c}, e)).getAudioTracks()[0] || null
            } catch (h) {
                throw b.onError(h), h;
            }
            if (!g) return e = new p(n.UNEXPECTED_ERROR, "can not find track in media stream"), b.onError(e), e.throw();
            a = new Wf(g, a, c, e);
            return b.onSuccess(a.getTrackId()), k.info("create microphone audio track success, trackId:",
                e), a
        }, createCustomAudioTrack: function (a) {
            const b = t.reportApiInvoke(null, {tag: D.TRACER, name: E.CREATE_CUSTOM_AUDIO_TRACK, options: [a]}),
                c = new $a(a.mediaStreamTrack, a.encoderConfig ? wd(a.encoderConfig) : {});
            return k.info("create custom audio track success with config", a, "trackId", c.getTrackId()), b.onSuccess(c.getTrackId()), c
        }, createBufferSourceAudioTrack: async function (a) {
            const b = t.reportApiInvoke(null, {tag: D.TRACER, name: E.CREATE_BUFFER_AUDIO_TRACK, options: [a]}),
                c = pa(8, "track-");
            k.info("start create buffer source audio track with config",
                A(a), "trackId", c);
            const e = a.source;
            if (!(a.source instanceof AudioBuffer)) try {
                a.source = await ul(a.source, a.cacheOnlineFile)
            } catch (h) {
                return b.onError(h), h.throw()
            }
            const g = new Io(a.source);
            a = new go(e, g, a.encoderConfig ? wd(a.encoderConfig) : {}, c);
            return k.info("create buffer source audio track success, trackId:", c), b.onSuccess(a.getTrackId()), a
        }, setLogLevel: function (a) {
            k.setLogLevel(a)
        }, enableLogUpload: function () {
            k.enableLogUpload()
        }, disableLogUpload: function () {
            k.disableLogUpload()
        }, createChannelMediaRelayConfiguration: function () {
            return new Ph
        },
        checkAudioTrackIsActive: async function (a, b = 5E3) {
            const c = t.reportApiInvoke(null, {tag: D.TRACER, name: E.CHECK_AUDIO_TRACK_IS_ACTIVE, options: [b]});
            if (!(a instanceof $a || a instanceof od)) {
                var e = new p(n.INVALID_TRACK, "the parameter is not a audio track");
                return c.onError(e), e.throw()
            }
            b && 1E3 > b && (b = 1E3);
            const g = a instanceof $a ? a.getTrackLabel() : "remote_track";
            let h = e = a.getVolumeLevel(), m = e;
            const q = x();
            return new v(e => {
                const n = Jo(() => {
                    var p = a.getVolumeLevel();
                    h = p > h ? p : h;
                    m = p < m ? p : m;
                    p = 1E-4 < h - m;
                    var r = x() - q;
                    if (p ||
                        r > b) {
                        var t;
                        clearInterval(n);
                        r = {duration: r, deviceLabel: g, maxVolumeLevel: h, result: p};
                        k.info(l(t = "[track-".concat(a.getTrackId(), "] check audio track active completed. ")).call(t, A(r)));
                        c.onSuccess(r);
                        e(p)
                    }
                }, 200)
            })
        }, checkVideoTrackIsActive: async function (a, b = 5E3) {
            var c;
            const e = t.reportApiInvoke(null, {tag: D.TRACER, name: E.CHECK_VIDEO_TRACK_IS_ACTIVE, options: [b]});
            if (!(a instanceof Na || a instanceof nd)) return a = new p(n.INVALID_TRACK, "the parameter is not a video track"), e.onError(a), a.throw();
            b && 1E3 > b &&
            (b = 1E3);
            var g = a instanceof Na ? a.getTrackLabel() : "remote_track", h = a.getMediaStreamTrack();
            const m = document.createElement("video");
            m.style.width = "1px";
            m.style.height = "1px";
            m.setAttribute("muted", "");
            m.muted = !0;
            m.setAttribute("playsinline", "");
            m.controls = !1;
            Rk === Z.SAFARI && (m.style.opacity = "0.01", m.style.position = "fixed", m.style.left = "0", m.style.top = "0", document.body.appendChild(m));
            m.srcObject = new MediaStream([h]);
            m.play();
            const q = document.createElement("canvas");
            q.width = 160;
            q.height = 120;
            let u = h = 0;
            try {
                const a =
                    x();
                h = await function (a, b, c, e) {
                    let g, h = 0, l = null;
                    return new v((m, q) => {
                        Fc(() => {
                            g && (g(), m(h))
                        }, b);
                        g = Ge(() => {
                            a:{
                                h > e && g && (g(), m(h));
                                var b = c.getContext("2d");
                                if (b) {
                                    b.drawImage(a, 0, 0, 160, 120);
                                    b = b.getImageData(0, 0, c.width, c.height);
                                    var r = Math.floor(b.data.length / 3);
                                    if (l) for (let a = 0; a < r; a += 3) if (b.data[a] !== l[a]) {
                                        h += 1;
                                        l = b.data;
                                        break a
                                    }
                                    l = b.data
                                } else b = new p(n.UNEXPECTED_ERROR, "can not get canvas 2d context."), k.error(b.toString()), q(b)
                            }
                        }, 30)
                    })
                }(m, b, q, 4);
                u = x() - a
            } catch (y) {
                throw e.onError(y), y;
            }
            Rk === Z.SAFARI && (m.pause(),
                m.remove());
            m.srcObject = null;
            b = 4 < h;
            g = {duration: u, changedPicNum: h, deviceLabel: g, result: b};
            return k.info(l(c = "[track-".concat(a.getTrackId(), "] check video track active completed. ")).call(c, A(g))), e.onSuccess(g), b
        }, setArea: function (a) {
            var b;
            "string" == typeof a && (a = [a]);
            q(a).call(a, a => {
                if (!ya(Xj).call(Xj, a)) throw new p(n.INVALID_PARAMS, "invalid area code");
            });
            jc("AREAS", a);
            const c = (a => {
                const b = {
                    CODE: "",
                    WEBCS_DOMAIN: [],
                    WEBCS_DOMAIN_BACKUP_LIST: [],
                    PROXY_CS: [],
                    CDS_AP: [],
                    ACCOUNT_REGISTER: [],
                    UAP_AP: [],
                    EVENT_REPORT_DOMAIN: [],
                    EVENT_REPORT_BACKUP_DOMAIN: [],
                    LOG_UPLOAD_SERVER: [],
                    PROXY_SERVER_TYPE3: []
                };
                return C(a).call(a, a => {
                    const c = Yj[a];
                    (a = aa(c)) && C(a).call(a, a => {
                        var e;
                        "CODE" !== a && (b[a] = l(e = b[a]).call(e, c[a]))
                    })
                }), b
            })(a);
            C(b = aa(c)).call(b, a => {
                jc(a, "LOG_UPLOAD_SERVER" === a || "EVENT_REPORT_DOMAIN" === a || "EVENT_REPORT_BACKUP_DOMAIN" === a || "PROXY_SERVER_TYPE3" === a ? c[a][0] : c[a])
            });
            k.debug("set area success:", a.join(","))
        }, startProcessingLocalAEC: function (a) {
            Lo.startProcessingLocalAEC(a)
        }
    };
    return db.on(Mb.CAMERA_DEVICE_CHANGED, a => {
        k.info("camera device changed", A(a));
        vb.onCameraChanged && vb.onCameraChanged(a)
    }), db.on(Mb.RECORDING_DEVICE_CHANGED, a => {
        k.info("microphone device changed", A(a));
        vb.onMicrophoneChanged && vb.onMicrophoneChanged(a)
    }), db.on(Mb.PLAYOUT_DEVICE_CHANGED, a => {
        k.debug("playout device changed", A(a));
        vb.onPlaybackDeviceChanged && vb.onPlaybackDeviceChanged(a)
    }), lb.onAutoplayFailed = () => {
        k.info("detect audio element autoplay failed");
        vb.onAudioAutoplayFailed && vb.onAudioAutoplayFailed()
    }, Qc.on("autoplay-failed", () => {
        k.info("detect webaudio autoplay failed");
        vb.onAudioAutoplayFailed && vb.onAudioAutoplayFailed()
    }), vb
})
//# sourceMappingURL=AgoraRTC_N-production.js.map
