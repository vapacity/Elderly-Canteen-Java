<template>
  <div class="navbar">
    <button class="arrow-button" @click="goBack">
      <van-icon name="arrow-left" />
    </button>
    <div class="title">
      <p class="title-text">{{ props.title }}</p>
    </div>
    <div class="oldman-icon">
      <img src="../assets/oldman.jpg" height="40px" width="40px" />
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const goBack = () => {
  router.back()
}
const props = defineProps({
  title: String
})
</script>

<style scoped>
.navbar {
  width: 100%;
  display: flex;
  height: 50px;
}
.arrow-button {
  background: none;
  border: none;
}
.arrow-left {
  width: 10%;
  display: flex;
  align-items: center;
}
.title {
  width: 80%;
}
.title-text {
  text-align: center;
  font-size: medium;
  font-weight: bold;
}
.oldman-icon {
  width: 10%;
  display: flex;
  align-items: center;
}
</style>
