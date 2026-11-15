<template>
  <div class="back" @click="returnPersonal">&lt;</div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

const returnPersonal = () => {
  router.push({ name: 'User' })
}
</script>

<style scoped>
.back {
  position: fixed;

  top: 5%;
  left: 5%;

  height: 5vh;
  width: 5vh;

  padding-left: 1vh;

  z-index: 1000;
  font-size: 100%;
  background-color: rgb(254, 245, 221);
}
</style>
