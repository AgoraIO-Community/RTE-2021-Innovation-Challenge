///<reference path="./agora-rtm-sdk.d.ts" />
import AgoraRTM from 'agora-rtm-sdk'
import EventEmitter from 'events'
import Vue from 'vue';
var myName;
var myToken;
var myRandomName;
var globalChannelName = "GLOBAL CHANNEL"
var joinedChannelCount = 1
var bus = new Vue()
let myURL="http://127.0.0.1:8081"

var appID = ""


//RTM客户端类
class RTMClient extends EventEmitter {
  constructor() {
    super()
    this.channels = {}
    this._logined = false
  }

  init(appId) {
    this.client = AgoraRTM.createInstance(appId)
    this.subscribeClientEvents()
  }

  // subscribe client events
  subscribeClientEvents() {
    const clientEvents = [
      'ConnectionStateChanged',
    ]
    clientEvents.forEach((eventName) => {
      this.client.on(eventName, (...args) => {
        console.log('emit ', eventName, ...args)
        this.emit(eventName, ...args)
      })
    })
  }
  // subscribe channel events
  subscribeChannelEvents(channelName) {
    const channelEvents = [
      'ChannelMessage'
    ]
    channelEvents.forEach((eventName) => {
      this.channels[channelName].channel.on(eventName, (...args) => {
        console.log('emit ', eventName, args)
        this.emit(eventName, {
          channelName,
          args: args
        })
      })
    })
  }

  async login(accountName, token) {
    this.accountName = accountName
    return this.client.login({
      uid: this.accountName,
      token
    })
  }

  async logout() {
    return this.client.logout()
  }

  async joinChannel(name) {
    console.log('joinChannel', name)
    const channel = this.client.createChannel(name)
    this.channels[name] = {
      channel,
      joined: false,
    }
    this.subscribeChannelEvents(name)
    return channel.join()
  }

  async leaveChannel(name) {
    console.log('leaveChannel', name)
    if (!this.channels[name] ||
      (this.channels[name] &&
        !this.channels[name].joined)) return
    return this.channels[name].channel.leave()
  }

  async sendChannelMessage(text, channelName) {
    if (!this.channels[channelName] || !this.channels[channelName].joined) return
    return this.channels[channelName].channel.sendMessage({
      text
    })
  }

}
const rtm = new RTMClient();
rtm.on('ConnectionStateChanged', (newState, reason) => {
  console.log('reason:', reason, ', new state:', newState)
})

rtm.on('ChannelMessage', async ({
  channelName,
  args
}) => {
  const [message, memberId] = args
  console.log('channel ', channelName, ', messsage: ', message.text, ', memberId: ', memberId)
  bus.$emit("addNewMessage", [channelName, message.text, memberId])
})
rtm.init(appID)
window.rtm = rtm


Vue.component("pop-pus-set-attr", {
  //弹出弹窗,输入坐标和目标
  props: ["coord", "goal"],
  template: `
    <div class="translucent" v-show="isShow">
    <div class="pop">
    <div class="pop-ups-times"><i class="fas fa-times" @click="deleteSetAttrPopUps()"></i></div>
    <label for="inp" class="inp">
    <input type="text" placeholder=" " ref="myInputCoord">
    <span class="label">{{coord}}</span>
    <span class="focus-bg"></span>
    </label><br><label for="inp" class="inp">
    <input type="text" placeholder=" " ref="myInputGoal">
    <span class="label">{{goal}}</span>
    <span class="focus-bg"></span>
    </label><br>
    <div class="ph-float"><a @click="onClick()" class='ph-button ph-btn-green'>确定</a></div> 
    </div>
    </div>`,
  data: function () {
    return {
      isShow: false,
      title: "",
    }
  },
  methods: {
    onClick: async function () {
      bus.$emit("addChannelAttr", [this.title, this.$refs.myInputCoord.value, this.$refs.myInputGoal.value])
      this.isShow = false;
    },
    deleteSetAttrPopUps: function () {
      this.isShow = false;
    }
  },

})

Vue.component("pop-ups", {
  //name类型在网页刚打开时弹出,获得用户名
  // channel 类型,确认后加入channel
  props: ["title", "type"],
  template: `
    <div class="translucent" v-if="isShow">
    <div class="pop">
    <div class="pop-ups-times"><i class="fas fa-times" @click="deletePopUps()"></i></div>
    <label for="inp" class="inp">
    <input type="text" placeholder=" " ref="myInput">
    <span class="label">{{title}}</span>
    <span class="focus-bg"></span>
    </label><br>
    <div class="ph-float"><a @click="onClick()" class='ph-button ph-btn-green'>确定</a></div> 
    </div>
    </div>`,
  data: function () {
    return {
      isShow: true
    }
  },
  methods: {
    onClick: async function () {
      if (this.type == "name") {
        if (this.$refs.myInput.value == "") {
          alert("名字不能为空");
          return;
        }
        let randomNumber = Math.floor(Math.random() * 10000);
        myName = this.$refs.myInput.value;
        let userName = this.$refs.myInput.value + "-" + randomNumber;
        myRandomName = userName;
        try {
          myToken = await this.getToken(userName);
          //login
          try {
            rtm.login(myRandomName, myToken).then(() => {
              rtm._logined = true
              console.log('Login: ' + myRandomName, ' token: ', myToken)
              //登录成功,自动加入公共聊天频道
              rtm.joinChannel(globalChannelName).then(() => {
                rtm.channels[globalChannelName].joined = true;
                console.log("join global channel success")
                this.isShow = false;
              }).catch((err) => {
                alert('Join global channel failed, please open console see more details.')
                console.error(err)
              })
            }).catch((err) => {
              console.log(err)
            })
          } catch (err) {
            alert('Login failed, please open console see more details')
            console.error(err)
            return;
          }
        } catch (e) {
          alert("get token error !")
          console.log(e);
          return;
        }
        document.getElementById("welcome").innerText = "welcome " + myName;
        this.isShow = false;
      } else if (this.type == "channel") {
        //添加频道相关逻辑
        if (joinedChannelCount > 19) {
          alert("最多加入20个频道!")
          this.isShow = false;
          return;
        }
        if (this.$refs.myInput.value == "") {
          alert("频道名不能为空");
          return;
        }
        //处理与频道相关逻辑

        if (!rtm._logined) {
          alert('Please Login First')
          return
        }
        if (rtm.channels[this.$refs.myInput.value] ||
          (rtm.channels[this.$refs.myInput.value] && rtm.channels[this.$refs.myInput.value].joined)) {
          alert('You already joined')
          return
        }
        rtm.joinChannel(this.$refs.myInput.value).then(() => {
          console.log("join channel success")
          bus.$emit("newChannel", this.$refs.myInput.value);
          rtm.channels[this.$refs.myInput.value].joined = true;
          this.isShow = false;
        }).catch((err) => {
          console.log('Join channel failed, please open console see more details.')
          console.error(err)
        })


      }
    },
    deletePopUps: function () {
      if (this.type == "name") {
        alert("请输入名字");
      } else if (this.type == "channel") {
        this.isShow = false;
      }
    },
    getToken: async function (userName) {
      const response = await fetch(myURL+"/token?username=" + userName, {
        "method": "GET"
      })
      const json = await response.json();
      if (json.status == 200) {
        return json.token;
      } else {
        throw "get token error!";
      }
    }
  }
})

Vue.component('chat-tag', {
  props: ['title'],
  template: `<div class="chatName" @click="setActiveChannel()">
  <p>{{title}}</p>
  <div>
  <i class="fas fa-plus" @click="addAttrForChannel()"></i>
  <i class="fas fa-times" @click.stop="deleteChannel()"></i>
  </div>
  </div>`,
  methods: {
    deleteChannel: function () {
      if (!rtm._logined) {
        alert('Please Login First')
        return
      }
      rtm.leaveChannel(this.title).then(() => {
        console.log("leave channel success")
        if (rtm.channels[this.title]) {
          rtm.channels[this.title].joined = false
          rtm.channels[this.title] = null
          bus.$emit("deleteChannel", this.title);
        }
      }).catch((err) => {
        console.log('Leave channel failed, please open console see more details.')
        console.error(err)
      })
    },
    setActiveChannel: function () {
      bus.$emit("setActiveChannel", this.title);
    },

    addAttrForChannel: function () {
      bus.$emit("showAddrInput", this.title);
    }
  },
  data: function () {
    return {

    }
  }
})

var header = new Vue({
  el: "#header",
  data: {
    isPaused: true
  },
  methods: {
    //控制bgm
    onClickPause: function () {
      this.isPaused = !this.isPaused;
      if (this.isPaused) {
        this.$refs.audio.pause();
      } else {
        this.$refs.audio.play();
      }
    }
  }
})

var main = new Vue({
  el: "#popUps",
  data: {},
  methods: {}

})

var chat = new Vue({
  el: "#chat",
  data: {
    myChats: [],
    channelMessages: {},
    activeChannel: "Please select a channel",
    channelMemberCount: 0,
    channelCoord: "",
    channelGoal: ""
  },
  methods: {
    updateMessages: function () {
      if (!this.channelMessages[this.activeChannel]) {
        this.channelMessages[this.activeChannel] = []
      }
      let msgs = document.getElementById("messages-detail")
      msgs.innerHTML = ''
      for (let i = 0; i < this.channelMessages[this.activeChannel].length; i++) {
        msgs.innerHTML += `
        <div class ="msg">
        <div class="f10 msgFrom">${this.channelMessages[this.activeChannel][i][2]}:</div>
        <div class="msgText f18">${this.channelMessages[this.activeChannel][i][1]}</div>
        </div>`
        msgs.scrollTop = msgs.scrollHeight;
      }
    },
    addChannel: function () {
      this.$children[0].isShow = true;
    },
    setActiveChannelG: function () {
      console.log("setActiveChannelG")
      this.activeChannel = globalChannelName;
      rtm.client.getChannelMemberCount([globalChannelName]).then((number) => {
        for (let key in number) {
          this.channelMemberCount = number[key]
        }
      }).catch((err) => {
        alert('get global channel member count error')
        console.log(err)
      })
      this.channelCoord = "Whole canvas"
      this.channelGoal = "Paint whatever you want"
      let chats = document.getElementsByClassName("chatName");
      for (let i = 0; i < chats.length; i++) {
        if (chats[i].children[0].innerText == this.activeChannel) {
          chats[i].style.background = "linear-gradient(134deg, #9795f0, #fbc8d4)"
        } else {
          chats[i].style.background = "linear-gradient(135deg, #96fbc4, #f9f586)"
        }
      }
      //显示消息
      this.updateMessages()
    },
    sendChannelMessage: function () {
      if (!rtm._logined) {
        alert('Please Login First')
        return
      }

      if (!rtm.channels[this.activeChannel] ||
        (rtm.channels[this.activeChannel] && !rtm.channels[this.activeChannel].joined)
      ) {
        alert('Please Join first')
        return
      }
      if(this.$refs.myMessage.value=="")return
      rtm.sendChannelMessage(this.$refs.myMessage.value, this.activeChannel).then(() => {
        if (!this.channelMessages[this.activeChannel]) {
          this.channelMessages[this.activeChannel] = []
        }
        this.channelMessages[this.activeChannel].push([this.activeChannel, this.$refs.myMessage.value, myName])
        console.log("send message to channel success :" + this.$refs.myMessage.value)
        this.$refs.myMessage.value = "";
        this.updateMessages()
      }).catch((err) => {
        alert('Send message to channel ' + this.activeChannel + ' failed, please open console see more details.')
        console.error(err)
      })
    },

  },
  mounted() {
    this.$children[0].isShow = false;
    bus.$on("newChannel", (channel) => {
      this.myChats.push(channel)
      joinedChannelCount += 1;
    })
    bus.$on("deleteChannel", (channel) => {
      for (let i = 0; i < this.myChats.length; i++) {
        if (this.myChats[i] == channel) {
          this.myChats.splice(i, 1);
        }
      }
      joinedChannelCount -= 1;
      this.activeChannel = globalChannelName;
      this.setActiveChannelG();
    })
    bus.$on("showAddrInput", (title) => {
      this.$children[1].isShow = true;
      this.$children[1].title = title;

    })
    bus.$on("addChannelAttr", (data) => {
      console.log("setting channel attr" + " data[0]");
      rtm.client.setChannelAttributes(data[0], {
        coord: data[1],
        goal: data[2]
      }).then(() => {
        console.log('addChannelAttr success')
        bus.$emit("setActiveChannel", data[0])
      }).catch((err) => {
        console.log('addChannelAttr fail')
        console.error(err)
      })


    })
    bus.$on("setActiveChannel", (channel) => {
      console.log("setActiveChannel")
      this.activeChannel = channel;
      rtm.client.getChannelMemberCount([channel]).then((number) => {
        for (let key in number) {
          this.channelMemberCount = number[key]
        }
      }).catch((err) => {
        console.log('get channel member count error')
        console.log(err)
      })
      rtm.client.getChannelAttributes(this.activeChannel).then((attr) => {
        console.log(attr);
        this.channelCoord = ""
        this.channelGoal = ""
        for (let key in attr) {
          if (key == "coord") {
            this.channelCoord = attr[key].value;
          }
          if (key == "goal") {
            this.channelGoal = attr[key].value;
          }
        }
      }).catch((err) => {
        console.log('get channel attr error')
        console.log(err)
      })

      let chats = document.getElementsByClassName("chatName");
      for (let i = 0; i < chats.length; i++) {
        if (chats[i].children[0].innerText == this.activeChannel) {
          chats[i].style.background = "linear-gradient(134deg, #9795f0, #fbc8d4)"
        } else {
          chats[i].style.background = "linear-gradient(135deg, #96fbc4, #f9f586)"
        }
      }
      //显示消息
      if (!this.channelMessages[this.activeChannel]) this.channelMessages[this.activeChannel]=[]
      this.updateMessages()

    })
    bus.$on("addNewMessage", (data) => {
      if (!this.channelMessages[data[0]]) {
        this.channelMessages[data[0]] = []
      } else {
        this.channelMessages[data[0]].push(data)
      }
      this.updateMessages()
    })
  }

})