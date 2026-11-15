<template>
  <van-pull-refresh v-model="loading" success-text="刷新成功" @refresh="onRefresh">
    <div class="backgroundC">
      <div class="colorPiece"></div>
      <img src="../assets/newSlogan.jpg" alt="背景图" class="slogan" />
      <div class="imageContainer">
        <div class="imageWrapper" @click="resturant">
          <div>
            <img src="../assets/resturant.png" alt="图片1" class="image" />
          </div>
          <p class="imageText">食堂就餐</p>
        </div>
        <div class="imageWrapper" @click="deliver">
          <div>
            <img src="../assets/deliver.png" alt="图片2" class="image" />
          </div>
          <p class="imageText">爱心外卖</p>
        </div>
      </div>
      <TheWelcome />
      <div class="informationContainer">
        <div class="tip">⬆</div>
        <div class="tip">请选择就餐方式</div>
        <div class="titleLine" v-if="identity === 'volunteer' || identity === 'admin'">
          志愿者服务
        </div>
        <div class="buttonContainer" v-if="identity === 'volunteer' || identity === 'admin'">
          <div class="button button1" @click="getVolunteerOrder">志愿接单</div>
          <div class="button button2" @click="getVolunteerInfor">志愿信息</div>
        </div>

        <div class="titleLine line2">食堂信息</div>
        <div class="informationBox">
          名称：老人食堂<br />
          地址：上海市嘉定区曹安公路xxx号y栋<br />
          联系电话：123-4567-8910
        </div>
      </div>
    </div>
  </van-pull-refresh>
  <BottomTabbar nowView="home" />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import 'vant/es/toast/style'
import { showToast } from 'vant'

import defaultPic from '@/assets/testpic.jpg'
import { useUserStore } from '@/store/modules/user'

const ava = ref('')
const loading = ref(false)
const user = useUserStore()
const onRefresh = () => {
  setTimeout(() => {
    window.location.reload()
    loading.value = false
  }, 1000)
}
onMounted(() => {
  fetchData()
})

const fetchData = async () => {
  try {
    // 从 localStorage 中获取保存的 Token
    const token = localStorage.getItem('token')

    // 使用 axios 发起 GET 请求，附带 Authorization 头
    const response = await axios.get('/demo-api/account/getPersonInfo', {
      headers: {
        Authorization: `Bearer ${token}` // 将 Token 添加到 Authorization 头中
      }
    })

    if (response.data.getSuccess === true) {
      console.log(response.data.response) // 调试用
      ava.value = response.data.response.portrait ? response.data.response.portrait : defaultPic
      localStorage.setItem('portrait', ava.value)
    } else {
      showToast('获取信息失败')
    }
  } catch (error) {
    showToast('获取信息失败')
  }
}
const identity = localStorage.getItem('identity')

const router = useRouter()

const getVolunteerOrder = () => {
  router.push({ name: 'AccpetOrder' })
}

const getVolunteerInfor = () => {
  router.push({ name: 'VolunteerInfor' })
}

const resturant = () => {
  user.deliver_or_dining = false
  router.push({ name: 'OrderPage' })
}

const deliver = () => {
  user.deliver_or_dining = true
  router.push({ name: 'OrderPage' })
}
</script>

<style scoped>
.backgroundC {
  width: 100vw;
  height: 100vh; /* 使背景占满整个视口高度 */
  position: relative; /* 使子元素的绝对定位相对于背景容器 */
  background: #ffa822;
}

.colorPiece {
  position: absolute;
  top: 12%;

  width: 100vw;
  height: 30%;
  background-color: rgb(239, 255, 250);
}

.imageContainer {
  display: flex;
  position: relative;
  width: 100%;
  height: 30%;
  top: 7%;
  justify-content: space-evenly; /* 图片之间均匀分布 */
  align-items: center; /* 垂直居中 */
}

.imageWrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 45%; /* 每个图片容器占据容器宽度的45%，留出间隔 */
}

.image {
  width: 90%; /* 图片占据父容器的宽度 */
  height: auto; /* 保持图片比例 */
}

.imageText {
  color: white;
  font-weight: bold;
  text-align: center;
  font-size: 0.5rem;
}

.slogan {
  position: relative;
  top: 0%;
  width: 100%;
  height: auto;
}

.informationContainer {
  position: relative;
  width: 100vw;
  height: 47%;
  top: -10%;
  box-shadow: 0 6px 10px rgba(0, 0, 0, 0.1); /* 阴影效果 */
  border-radius: 20px; /* 四角圆滑 */
  background-color: white;
}

.titleLine {
  position: relative;
  top: 10%;

  height: 0.8rem;
  width: 100vw;

  background-color: wheat;

  font-size: 0.5rem;
  padding-left: 10%;

  color: #ffa822;
  font-weight: bold;

  letter-spacing: 0.15rem;
}

.line2 {
  top: 14%;
}

.buttonContainer {
  display: flex;
  justify-content: space-around; /* 按钮之间的间距 */
  position: relative;

  width: 100%;
  top: 12%; /* 按钮容器的位置 */
}

.button {
  padding: 10px 20px;
  border-radius: 0.5rem;
  text-align: center;
  color: white;
  font-size: 0.6rem;
}

.button1 {
  background-color: rgb(215, 110, 110); /* 按钮1的背景色 */
}

.button2 {
  background-color: rgb(199, 228, 134); /* 按钮2的背景色 */
}

.informationBox {
  position: relative;
  top: 15%;

  font-size: 0.4rem;
  padding-left: 15%;

  color: brown;

  line-height: 0.8rem;
}

.tip {
  position: relative;
  left: 4%;
  top: 3%;
  display: flex;
  align-items: center; /* 垂直居中对齐 */
  justify-content: center; /* 保持内容的水平对齐 */
  height: 4%;
  width: 90%;
  font-size: 0.4rem;
  line-height: 5%; /* 确保行高与容器高度一致 */
}
</style>
