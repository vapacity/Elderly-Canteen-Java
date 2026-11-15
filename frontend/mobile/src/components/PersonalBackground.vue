<template>
  <div class="background">
    <van-image :src="props.ava" alt="头像" class="headImage" round  fit="cover">
      <template v-slot:loading>
        <van-loading type="spinner" size="20" />
      </template>
    </van-image>
    <div class="textBox">
      <slot></slot>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
    ava: String
})
</script>

<style scoped>
.background {
  display: flex;
  position: relative;
  flex-direction: column; /* 使子元素按列布局 */
  align-items: center; /* 子元素水平居中 */
  top: 0;

  height: 100vh;
  width: 100vw;

  background-color: wheat;
}

.headImage {
  position: relative;
  top: 15%;

  height: 30vw;
  width: 30vw;

  box-shadow: 10px 10px 20px rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

.textBox {
  flex: 1;

  height: 70%;
  width: 100%;

  background-color: white;
  border-radius: 0.5rem;

  margin-top: 2rem;
}
</style>
