<template>
  <div class="back" @click="handleClick">&lt;</div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { defineProps } from 'vue'

const props = defineProps({
  targetRoute: {
    type: Object,
    required: true
  }
})
const router = useRouter()

const handleClick = () => {
  router.push(props.targetRoute)
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
  background-color: wheat;
}
</style>
