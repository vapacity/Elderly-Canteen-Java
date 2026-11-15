<template>
  <div class="top">
    <div class="backIcon">
      <van-icon name="arrow-left" @click="onClickLeft" v-show="nav_text !== '我的订单'"/>
    </div>

    <p>{{ props.nav_text }}</p>
    <van-image :src="portrait" round fit="cover" @click="clickPortrait" class="roundImg">
      <template v-slot:loading>
        <van-loading type="spinner" size="20" />
      </template>
    </van-image>
  </div>
</template>
<script setup>
import { getPorTrait } from '../api/api'
const onClickLeft = () => history.back() //顶部返回键逻辑
const props = defineProps({
  nav_text: String
})

//头像加载（渲染阶段）、点击跳转用户页
//#region
const portrait = ref()
const loadPortrait = async () => {
  const url = await getPorTrait()
  console.log('component:getportrait:url:', url)
  portrait.value = url
  console.log('portrait:', portrait)
}
onMounted(loadPortrait)

const router = useRouter()
const clickPortrait = () => {
  router.push({ path: '/User' })
}
defineExpose({
  onClickLeft,
});
//#endregion
</script>

<style scoped>
.roundImg {
  height: 10vw;
  width: 10vw;
}

.top p {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  margin: 0;
  font-weight: bold;
}

.top img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  margin-right: 10px;
}

.top .backIcon {
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
</style>
