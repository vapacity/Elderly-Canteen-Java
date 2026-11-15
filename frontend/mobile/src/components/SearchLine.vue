<template>
  <div class="top">
    <van-icon name="arrow-left" @click="onClickLeft" />
    <van-search v-model="searchValue" placeholder="请输入搜索关键词" class="fixed-width-search" />
    <van-image :src="portrait" round fit="cover" @click="clickPortrait" class="roundImg">
      <template v-slot:loading>
        <van-loading type="spinner" size="20" />
      </template>
    </van-image>
  </div>
</template>
<script setup>
import { getPorTrait } from '../api/api'
import { ref } from 'vue'
import { watch } from 'vue'
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
const onClickLeft = () => history.back() //顶部返回键逻辑
const searchValue = ref('')
const portrait = ref('')
const emit = defineEmits(['update:modelValue'])
const token = localStorage.getItem('token')
watch(searchValue, (newValue) => {
  emit('update:modelValue', newValue)
})
const loadPortrait = async (token) => {
  const url = await getPorTrait(token)
  console.log('component:getportrait:url:', url)
  portrait.value = url
  console.log('portrait:', portrait)
}
onMounted(async () => {
  await loadPortrait(token)
})

const router = useRouter()
const clickPortrait = () => {
  router.push({ path: '/User' })
}
</script>

<style scoped>
.roundImg {
  height: 10vw;
  width: 10vw;
}
.top img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  margin-right: 10px;
}

.top .van-icon {
  margin-left: 10px;
}

.top {
  height: 5vh;
  width: 100%;
  align-items: center;
  justify-content: space-between;
  display: flex;
  font-size: large;
  font-weight: bold;
  /* position: relative; */
}
.fixed-width-search {
  width: 80vw; /* 或其他固定宽度值 */
}
</style>
