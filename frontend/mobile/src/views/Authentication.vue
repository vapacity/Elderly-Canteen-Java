<template>
  <ReturnButton :targetRoute="{ name: 'User' }" />
  <PersonalBackground :ava="portrait">
    <div class="header">实名认证</div>
    <div class="inputBox">
      <div class="row">
        <span class="label">真实姓名</span>
        <input class="input" v-model="name" :disabled="isNameDisabled" />
      </div>
      <div class="row">
        <span class="label">身份证号</span>
        <input class="input" v-model="IDCard" :disabled="isIDCardDisabled" />
      </div>
    </div>
    <div class="tips">请注意：仅通过实名认证的老人才可享受爱心外卖服务</div>
    <button class="send" @click="sendApplication">提 交</button>
    <div class="tips second">请注意：一旦完成实名认证，无法修改</div>
  </PersonalBackground>
  <BottomTabbar nowView="user" />
</template>

<script setup>
import 'vant/es/toast/style'
import { showToast } from 'vant'
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const portrait = localStorage.getItem('portrait')

const name = ref('')
const IDCard = ref('')
const isNameDisabled = ref(false)
const isIDCardDisabled = ref(false)

onMounted(() => {
  const storedName = localStorage.getItem('name')
  const storedIDCard = localStorage.getItem('IDCard')

  if (storedName !== 'null') {
    name.value = storedName
    isNameDisabled.value = true
  }

  if (storedIDCard !== 'null') {
    IDCard.value = storedIDCard
    isIDCardDisabled.value = true
  }
})

const sendApplication = async () => {
  const isValidIDCard = /^\d{17}[\dXx]$/.test(IDCard.value)
  if (!isValidIDCard) {
    showToast('身份证号无效。')
    return
  }
  const token = localStorage.getItem('token')
  try {
    const data = {
      name: name.value,
      IDCard: IDCard.value
    }

    const response = await axios.post('/demo-api/Account/nameAuthenticate', data, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    })
    console.log('Response:', response)
    if (response.data.success) {
      showToast('实名成功')
      router.push({ name: 'User' })
    } else {
      showToast('实名失败，' + response.value.msg)
    }
  } catch (error) {
    if (error.response) {
      const statusCode = error.response.status
      if (statusCode === 400) {
        showToast(`实名失败，此账号已实名`)
      }
      if (statusCode === 404) {
        showToast(`实名失败`)
      }
    } else if (error.request) {
      showToast('登录失败，未收到响应')
    } else {
      // 其他错误
      showToast('登录失败，发生错误')
    }
  }
}
</script>

<style scoped>
.header {
  position: relative;
  font-weight: bold;
  font-size: 80%;
  text-align: center;
  top: 10%;
}

.inputBox {
  position: relative;
  height: 40%;
  top: 15%;

  width: 80%;
  left: 10%;
}

.row {
  position: relative;

  top: 10%;
  left: 0%;

  display: flex;
  height: 30%;
  width: 100%;
}

.label {
  font-size: 0.4rem;
  font-weight: bold;
  width: 30%;
  height: 10%;
}

.input {
  width: 70%;
  height: 50%;
  font-size: 60%;

  border-radius: 10px;

  border: none;

  background-color: wheat;
}

.send {
  position: relative;
  font-size: 80%;
  color: white;
  font-weight: bold;

  top: 0%;
  left: 30%;

  height: 10%;
  width: 40%;

  border-radius: 10px;

  background-color: #ffa822;

  border: none;
}

.tips {
  position: relative;
  top: 0%;
  left: 10%;

  height: 10%;
  width: 80%;

  font-weight: bold;

  font-size: 50%;
  color: red;

  text-align: center; /* 文本居中 */
}

.second {
  top: 10%;
}
</style>
