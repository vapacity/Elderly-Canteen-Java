<template>
  <div class="background">
    <div class="heading">老人食堂</div>
    <div
      class="header"
      :class="{ password: true, selected: isPassword, unselected: !isPassword }"
      @click="selectPassword"
    >
      密码登录
    </div>
    <div
      class="header"
      :class="{ credit: true, selected: !isPassword, unselected: isPassword }"
      @click="selectCredit"
    >
      验证码登录
    </div>
    <div class="whiteBox"></div>
    <div class="loginBox" v-if="isPassword">
      <input class="input-item" v-model="phoneNum" placeholder="输入手机号" />
      <input class="input-item" v-model="password" type="password" placeholder="输入密码" />
      <button class="loginBtn" @click="loginWithPassword">登 录</button>
      <p class="forgetPassword" @click="forgetPassword">忘记密码？</p>
      <div class="icon-container">
        <div class="left-box">
          <van-icon name="add-o" class="symbol" size="10vw" color="#ffa822" @click="register" />
          <span class="text">注册</span>
        </div>
        <div class="right-box">
          <van-icon name="close" class="symbol" size="10vw" color="#ffa822" />
          <span class="text">退出</span>
        </div>
      </div>
    </div>
    <div class="loginBox" v-else>
      <input class="input-item" v-model="phoneNum" placeholder="输入手机号" />
      <input class="input-item half" v-model="verifyCode" placeholder="输入验证码" />
      <button class="getBtn" @click="getCredit">获取验证码</button>
      <button class="loginBtn" @click="loginWithCredit">登 录</button>
      <p class="forgetPassword" @click="forgetPassword">忘记密码？</p>
      <div class="icon-container">
        <div class="left-box">
          <van-icon name="add-o" class="symbol" size="10vw" color="#ffa822" @click="register" />
          <span class="text">注册</span>
        </div>
        <div class="right-box">
          <van-icon name="close" class="symbol" size="10vw" color="#ffa822" />
          <span class="text">退出</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import 'vant/es/toast/style'
import { showToast } from 'vant'

const router = useRouter()
const isPassword = ref(true)

const phoneNum = ref('')
const password = ref('')
const verifyCode = ref('')

const selectPassword = () => {
  isPassword.value = true
}
const selectCredit = () => {
  isPassword.value = false
}

const quitApp = () => {
  navigator.app.exitApp() // 退出应用
}

if (!sessionStorage.getItem('hasRefreshed')) {
  // 如果没有刷新过页面，则刷新页面
  sessionStorage.setItem('hasRefreshed', 'true')
  window.location.reload()
}

const loginWithPassword = async () => {
  const isValidPhoneNumber = /^\d{11}$/.test(phoneNum.value)
  if (!isValidPhoneNumber) {
    showToast('手机号无效，必须是11位数字。')
    return
  }
  try {
    const response = await axios.post('/demo-api/account/login', {
      phoneNum: phoneNum.value,
      password: password.value
    })
    if (response.data.loginSuccess) {
      const { token, identity, accountName } = response.data.response
      localStorage.setItem('token', token)
      localStorage.setItem('identity', identity)
      localStorage.setItem('accountName', accountName)

      router.push({ name: 'Home' })
    } else {
      showToast('登录失败，' + response.data.msg)
    }
  } catch (error) {
    if (error.response) {
      const statusCode = error.response.status
      if (statusCode === 400) {
        showToast(`登录失败，密码错误`)
      }
      if (statusCode === 404) {
        showToast(`登录失败，用户名未找到`)
      }
    } else if (error.request) {
      showToast('登录失败，未收到响应')
    } else {
      showToast('登录失败，发生错误')
    }
  }
}

const loginWithCredit = async () => {
  const isValidPhoneNumber = /^\d{11}$/.test(phoneNum.value)
  if (!isValidPhoneNumber) {
    showToast('手机号无效，必须是11位数字。')
    return
  }
  try {
    const response = await axios.post('/demo-api/Account/verifiationCodeLogin', {
      phoneNum: phoneNum.value,
      verifyCode: verifyCode.value
    })
    if (response.data.loginSuccess) {
      const { token, identity, accountName } = response.data.response
      localStorage.setItem('token', token)
      localStorage.setItem('identity', identity)
      localStorage.setItem('accountName', accountName)

      router.push({ name: 'Home' })
    } else {
      showToast(`登录失败`)
    }
  } catch (error) {
    if (error.response) {
      const statusCode = error.response.status
      if (statusCode === 400) {
        showToast(`登录失败，密码错误`)
      }
      if (statusCode === 404) {
        showToast(`登录失败，用户名未找到`)
      }
    } else if (error.request) {
      showToast('登录失败，未收到响应')
    } else {
      // 其他错误
      showToast('登录失败，发生错误', error)
    }
  }
}

const getCredit = async () => {
  const isValidPhoneNumber = /^\d{11}$/.test(phoneNum.value)
  if (!isValidPhoneNumber) {
    showToast('手机号无效，必须是11位数字。')
    return
  }
  try {
    const response = await axios.post(
      '/demo-api/Account/sendOTP',
      {
        phone: phoneNum.value
      },
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    )

    if (response.value.success) {
      showToast('发送成功')
    } else {
      showToast('发送失败')
    }
  } catch (error) {
    showToast('发送成功')
  }
}

const forgetPassword = () => {
  router.push({ name: 'AlterPassword' })
}

const register = () => {
  router.push({ name: 'Register' })
}
</script>

<style scoped>
.background {
  height: 220vw;
  width: 100vw;

  position: relative;

  background-image: url('@/assets/loginBack.jpg'); /* 使用本地图片作为背景 */

  background-size: cover; /* 确保背景图片覆盖整个容器 */

  overflow: hidden;
  background-color: white;
}

.background::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(183, 169, 169, 0.5); /* 调整背景透明度 */
  z-index: 0;
  pointer-events: none;
}

.whiteBox {
  position: absolute;
  top: 24.5%;

  height: 11vw;
  width: 80vw;
  left: 10vw;

  text-align: center;
  font-weight: bold;
  font-size: 60%;
  background-color: white;
  border-radius: 10px;
  z-index: 10;
}

.icon-container {
  position: relative;
  display: flex;
  justify-content: space-between; /* 左右分布 */
  top: 4vw;

  height: 40%;
}

.left-box,
.right-box {
  display: flex;
  justify-content: center;
  width: 50%;
}

.text {
  position: relative;
  height: 20vw;
  font-size: 4vw;
  color: #ffa822;
  top: 30vw;
  left: -5vw;
}

.symbol {
  top: 20vw;
  left: 4vw;
  z-index: 10;
  height: 18vw;
}

.heading {
  position: relative;

  z-index: 10;

  top: 10%;

  text-align: center; /* 水平居中 */
  font-weight: bold; /* 字体加粗 */

  color: #ffa822;
}

.loginBox {
  position: absolute;

  top: 25%;
  left: 10%;

  height: 100vw;
  width: 80vw;

  background-color: white;

  border-radius: 20px;
  box-shadow: 0 3px 20px rgba(0, 0, 0, 0.5); /* 阴影效果 */

  text-align: center;

  z-index: 1;
}

.header {
  position: absolute;
  top: 20.5%;

  height: 10vw;
  width: 30vw;

  text-align: center;
  font-weight: bold;
  font-size: 55%;
  background-color: white;
  box-shadow: 0 0px 20px rgba(0, 0, 0, 0.5); /* 阴影效果 */
  border-radius: 1vh;
}

.password {
  left: 15%;
}

.credit {
  left: 55%;
}

.selected {
  z-index: 4;
}

.unselected {
  z-index: 0;
}

.input-item {
  position: relative;
  top: 10%;

  width: 80%;
  height: 13%;
  z-index: 6;

  margin-top: 5%;
  border-radius: 10px;
  font-size: 60%;

  text-align: center;
}

.half {
  width: 40%;
}

.loginBtn {
  position: relative;
  top: 15%;
  height: 13%;
  width: 60%;
  border-radius: 10px;
  background-color: rgb(232, 255, 195);
  font-size: 70%;
  border: none;
  color: white;
  font-weight: bold;
  background-color: #ffa822;
  letter-spacing: 0.05rem;
  z-index:1000;
}

.getBtn {
  position: relative;
  top: 10%;
  height: 13%;
  width: 30%;
  border-radius: 10px;
  margin-left: 10%;
  background-color: white;
  font-size: 50%;
}

.register {
  position: relative;
  top: 25%;
  left: 0%;
  width: 40%;
  height: 15%;
  border-radius: 20px;
  font-size: 60%;
  border: none;
  color: white;
  font-weight: bold;
  background-color: #ffa822;
  letter-spacing: 0.05rem;
}

.forgetPassword {
  position: relative;
  font-weight: bold;
  top: 20%;
  font-size: 60%;
  z-index: 10;
}
</style>
