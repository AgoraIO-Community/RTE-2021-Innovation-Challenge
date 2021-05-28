# Agora-RTC-vue DEMO

## init

```bash
yarn install
```

## require

at ./src/main.js

```js
import AgoraRTC from "../agora-rtc-vue"
import "../agora-rtc-vue/lib/agora-rtc-vue.css"

Vue.use(AgoraRTC)
```

## usage

at ./src/components/HelloWorls.vue

```vue
   <agora appid="" channel="" token="">
     <agora-audio-sender></agora-audio-sender>
     <agora-audio-receiver></agora-audio-receiver>
     <agora-video-sender></agora-video-sender>
     <agora-video-receiver></agora-video-receiver>
   </agora>
```

insert your appid,token,channel

## run

```bash
yarn serve
```