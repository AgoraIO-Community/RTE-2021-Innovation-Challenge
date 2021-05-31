<template>
  <div class="hello">
   <agora appid="" channel="" token="">
     <agora-audio-sender></agora-audio-sender>
     <agora-audio-receiver></agora-audio-receiver>
     <agora-video-sender></agora-video-sender>
     <agora-video-receiver></agora-video-receiver>
   </agora>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
