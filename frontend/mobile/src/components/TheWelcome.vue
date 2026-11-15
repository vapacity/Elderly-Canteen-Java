<template>
  <div class="theHelloContainer">
    <van-image :src="portrait" class="helloImage" round fit="cover">
      <template v-slot:loading>
        <van-loading type="spinner" size="20" />
      </template>
    </van-image>
    <span class="greetingText">你好，{{ accountName }}</span>
  </div>
</template>

<script setup>
const accountName = localStorage.getItem('accountName')
const portrait = localStorage.getItem('portrait')
</script>

<style scoped>
.theHelloContainer {
  width: 80%;
  height: 12%;

  display: flex;
  position: relative;

  justify-content: center; /* 内容水平居中 */
  align-items: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 阴影效果 */
  border-radius: 15px; /* 四角圆滑 */

  top: -25%;
  left: 50%;
  transform: translate(-50%, -50%); /* 使容器在页面中居中 */

  background-color: white;
}

.helloImage {
  position: absolute;
  left: 3vw;
  top: 10%;
  width: 20vw;
  height: 20vw;
  border-radius: 50%; /* 将图片设置为圆形 */
}

.greetingText {
  font-size: 0.6rem;
  margin-left: 20vw;
}
</style>
