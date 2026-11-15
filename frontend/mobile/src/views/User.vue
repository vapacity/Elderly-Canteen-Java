<template>
  <van-pull-refresh v-model="loading" success-text="刷新成功" @refresh="onRefresh">
    <PersonalBackground :ava="preview">
      <div class="textContainer">
        <span class="head">个人信息</span>
        <button class="info-button" @click="alterInformation">🖊</button>
        <div class="space"></div>
        <div class="row">
          <span class="label">ID:</span> <span class="value">{{ userData.accountId }}</span>
        </div>
        <div class="row">
          <span class="label">账户名:</span> <span class="value">{{ userData.accountName }}</span>
        </div>
        <div class="row">
          <span class="label">联系方式:</span> <span class="value">{{ userData.phoneNum }}</span>
        </div>
        <div class="row">
          <span class="label">性别:</span> <span class="value">{{ userData.gender }}</span>
        </div>
        <div class="row">
          <span class="label">出生日期:</span> <span class="value">{{ userData.birthDate }}</span>
        </div>
        <div class="row" v-if="userData.identity === 'senior'">
          <span class="label">补贴:</span> <span class="value">{{ userData.subsidy }}</span>
        </div>
        <div class="row">
          <span class="label">账户余额</span> <span class="value">{{ userData.money }}</span
          ><van-icon name="gold-coin-o" class="prePaid" size="7vw" @click="prePaid" />
        </div>
        <div class="row">
          <span class="label">地址:</span> <span class="value">{{ userData.address }}</span>
        </div>
      </div>
      <div class="buttonContainer">
        <button class="buttonType" @click="authen">实名认证</button>
        <button class="buttonType" @click="apply">志愿者申请</button>
      </div>
      <div class="buttonExit" @click="exitEnsure">退出登录</div>
      <van-dialog
        v-model:show="show"
        title="账户充值"
        show-cancel-button
        @confirm="ensurePrepaid"
        width="70vw"
      >
        <div class="input-container">
          <input v-model="prePaidMoney" placeholder="输入充值金额" class="prePaidInput" />
        </div>
      </van-dialog>
      <van-dialog
        v-model:show="showExit"
        title="确认登出"
        show-cancel-button
        @confirm="exit"
        width="70vw"
      >
      </van-dialog>
    </PersonalBackground>
  </van-pull-refresh>
  <BottomTabbar nowView="user" />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import 'vant/es/toast/style'
import { showToast } from 'vant'

import defaultPic from '@/assets/testpic.jpg'

const router = useRouter()
const getSuccess = ref(false)
const preview = ref('')
const show = ref(false)
const showExit = ref(false)
const prePaidMoney = ref('')

const userData = ref({
  accountId: '',
  accountName: '',
  phoneNum: '',
  identity: '',
  portrait: '',
  gender: '',
  birthDate: '',
  address: '',
  name: '',
  money: '',
  IDCard: '',
  subsidy: ''
})
const loading = ref(false)
const onRefresh = () => {
  setTimeout(() => {
    window.location.reload()
    loading.value = false
  }, 1000)
}

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
      getSuccess.value = true
      userData.value = response.data.response
      preview.value = userData.value.portrait ? userData.value.portrait : defaultPic
      if (userData.value.gender === 'male') {
        userData.value.gender = '男'
      } else {
        userData.value.gender = '女'
      }

      localStorage.setItem('portrait', preview.value)
      localStorage.setItem('name', userData.value.name)
      localStorage.setItem('IDCard', userData.value.idCard)
    } else {
      showToast('获取信息失败' + response.data.msg)
    }
  } catch (error) {
    showToast('获取信息失败')
  }
}

const ensurePrepaid = async () => {
  const token = localStorage.getItem('token')

  console.log(prePaidMoney.value)
  if (prePaidMoney.value <= 0 || prePaidMoney.value > 1000) {
    showToast('请输入 0 到 1000 之间的充值金额')
    return
  }
  try {
    const data = {
      money: prePaidMoney.value
    }

    const response = await axios.post('/demo-api/Account/prePaid', data, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    })
    if (response.data.success) {
      showToast('充值成功')
    } else {
      showToast('充值失败')
    }
  } catch (error) {
    console.log(error)
    showToast('充值失败')
  }
}

onMounted(() => {
  fetchData()
})

const alterInformation = () => {
  router.push({ name: 'Alter' })
}

const authen = () => {
  router.push({ name: 'Authen' })
}

const prePaid = () => {
  show.value = true
}

const apply = () => {
  const name = localStorage.getItem('name')
  const identity = localStorage.getItem('identity')
  const idCard = localStorage.getItem('IDCard')
  const year = idCard.substring(6, 10)
  const extractedYear = parseInt(year, 10)
  console.log(extractedYear)
  if (extractedYear <= 1964) {
    showToast('60岁以上老人无法注册志愿者')
    return
  }

  if (identity === 'volunteer') {
    showToast('该账户已是志愿者')
    return
  }
  if (name === 'null') {
    showToast('未实名用户不能进行志愿者申请')
    return
  }
  router.push({ name: 'Apply' })
}

const exit = () => {
  router.push({ name: 'Login' })
}

const exitEnsure = () => {
  showExit.value = true
}
</script>

<style scoped>
.prePaid {
  position: relative;
  left: 20vw;
  top: -1vw;
}

.input-container {
  height: 10vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.prePaidInput {
  width: 50vw;
  height: 5vh;
}

.head {
  font-weight: bold;
  font-size: 120%;
}

.space {
  height: 6%;
}

.row {
  left: 0%;
  top: 0%;
  display: flex;
  flex-direction: row;
  width: 80vw;
  height: 10%;
}

.label {
  font-weight: bold;
  min-width: 30%;
  text-align: left;
  font-size: 100%;
}

.value {
  text-align: left;
  font-size: 100%;
  line-height: 1.3;
}

.info-button {
  border-radius: 50%;
  background-color: #ffa822;
  margin-left: 10%;
  border: none;
  color: white;
  font-weight: bold;
}

.errorContainer,
.textContainer {
  position: relative;
  top: 8%;
  height: 60%;
  font-size: 0.4rem;
  padding-left: 15%; /* 调整左侧距离 */

  z-index: 10;
}

.buttonContainer {
  display: flex;

  height: 12vh;
  width: 100vw;
  justify-content: space-between;
  padding: 0 10vw;
}

.buttonType {
  font-weight: bold;
  color: white;
  position: relative;
  font-size: 0.5rem;
  top: 5vh;

  height: 7vh;
  width: 35vw;

  background-color: #ffa822;
  border-radius: 10px;
  z-index: 10;

  border: none;
}

.buttonExit {
  position: relative;
  font-weight: bold;
  left: 40vw;
  width: 20vw;

  top: 4vh;
  font-size: 2vh;

  color: #007bff;
  text-decoration: underline;
}
</style>
