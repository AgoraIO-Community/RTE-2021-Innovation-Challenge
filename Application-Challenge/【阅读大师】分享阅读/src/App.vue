<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
    import './assets/css/global.css';
    export default {
        beforeCreate: () => {
            // 添加背景色
            document
                .querySelector('body')
                .setAttribute('style', 'background-color:#e9ecef;height:100%;');
        }
    };
</script>

<style lang="less"></style>
