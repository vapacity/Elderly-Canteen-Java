<template>
  <ReturnButton :targetRoute="{ name: 'Login' }" />
  <div class="background">
    <div class="headerBox">新用户注册</div>
    <div class="registerBox">
      <div class="row">
        <span class="label">手机号码</span> <input class="inputBox" v-model="phoneNum" />
      </div>
      <div class="row">
        <span class="label">账户名称</span> <input class="inputBox" v-model="userName" />
      </div>
      <div class="row">
        <span class="label">账户密码</span> <input class="inputBox" v-model="password" />
      </div>
      <div class="row">
        <span class="label">性别</span>
        <select v-model="gender" class="inputBox">
          <option value="">选择性别</option>
          <option value="male">男</option>
          <option value="female">女</option>
        </select>
      </div>
      <div class="row">
        <span class="label">验证码</span>
        <input class="inputBox half" v-model="verificationCode" /><button
          class="verifyBtn"
          @click="getCredit"
        >
          发送
        </button>
      </div>
      <button class="getIn" @click="getIn">注册</button>
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

const phoneNum = ref('')
const userName = ref('')
const password = ref('')
const gender = ref('')
const verificationCode = ref('')

const getIn = async () => {
  try {
    const formData = new FormData()
    formData.append('userName', userName.value)
    formData.append('gender', gender.value)
    formData.append('verificationCode', verificationCode.value)
    formData.append('phoneNum', phoneNum.value)
    formData.append('password', password.value)
    const response = await axios.post('/demo-api/account/register', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (response.data.registerSuccess) {
      if (userName.value !== '') {
        localStorage.setItem('accountName', userName.value)
      }
      const { token, identity, accountName } = response.data.response
      localStorage.setItem('token', token)
      localStorage.setItem('identity', identity)
      localStorage.setItem('accountName', accountName)

      router.push({ name: 'Home' })
    } else {
      showToast('注册失败：' + response.data.msg)
    }
  } catch (error) {
    showToast('注册失败')
  }
}

const getCredit = async () => {
  const phoneNumber = String(phoneNum.value).trim() // 确保是字符串并去除前后空格
  const isValidPhoneNumber = /^\d{11}$/.test(phoneNumber)

  if (!isValidPhoneNumber) {
    alert('手机号无效。必须是11位数字。')
    return
  }
  try {
    const response = await axios.post(
      '/demo-api/Account/sendOTP',
      {
        phoneNum: phoneNum.value
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
    showToast('发送失败')
  }
}
</script>

<style scoped>
.background {
  display: flex;
  position: relative;
  flex-direction: column;
  /* 使子元素按列布局 */
  top: 0;

  height: 100vh;
  width: 100vw;

  background-color: wheat;
}

.registerBox {
  position: relative;
  height: 70%;
  top: 9%;
  width: 80%;
  left: 10%;
  background-color: white;
  border-radius: 20px;

  z-index: 1;
  box-shadow: 0 0px 20px rgba(0, 0, 0, 0.2);
  /* 阴影效果 */
}

.headerBox {
  position: relative;
  height: 5%;
  top: 10%;
  width: 60%;
  left: 20%;
  background-color: white;
  font-weight: bold;
  font-size: 80%;
  text-align: center;
  border-radius: 10px;

  z-index: 3;
  box-shadow: 0 -7px 10px rgba(0, 0, 0, 0.2);
  /* 阴影效果 */
}

.row {
  top: 10%;
  left: 10%;
  position: relative;
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 12%;
}

.label {
  width: 20%;
  font-weight: bold;
  font-size: 60%;
  min-width: 25%;
  text-align: left;
}

.inputBox {
  width: 50%;
  height: 50%;
  border-radius: 1vh;
  font-size: 0.4rem;
  border: 2px solid #000;
}

.half {
  width: 25%;
  height: 50%;
}

.verifyBtn {
  position: relative;
  width: 20%;
  height: 50%;
  left: 5%;
  border-radius: 5px;
  font-size: 60%;
}

.getIn {
  position: relative;
  width: 40%;
  height: 10%;
  left: 30%;
  top: 10%;
  border-radius: 20px;
  font-size: 60%;
  border: none;
  color: white;
  font-weight: bold;
  background-color: #ffa822;
}
</style>
