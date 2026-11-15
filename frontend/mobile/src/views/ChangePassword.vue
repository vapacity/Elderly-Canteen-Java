<template>
  <ReturnButton :targetRoute="{ name: 'Alter' }" />
  <div class="background">
    <div class="headerBox">修改密码</div>
    <div class="registerBox">
      <div class="row">
        <span class="label">旧密码</span> <input class="inputBox" v-model="oldPassword" />
      </div>
      <div class="row">
        <span class="label">新密码</span> <input class="inputBox" v-model="newPassword" />
      </div>
      <div class="row">
        <span class="label">重新输入</span> <input class="inputBox" v-model="rePassword" />
      </div>
      <button class="getIn" @click="Ensure">确认修改</button>
      <div class="forget" @click="forget">忘记密码？</div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import 'vant/es/toast/style'
import { showToast } from 'vant'

const router = useRouter()
const newPassword = ref('')
const rePassword = ref('')
const oldPassword = ref('')

const Ensure = async () => {
  const token = localStorage.getItem('token')
  if (newPassword.value !== rePassword.value) {
    return
  }
  try {
    const data = {
      oldPassword: oldPassword.value,
      newPassword: newPassword.value
    }

    const response = await axios.post('/demo-api/Account/alterPassword', data, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    })
    console.log('Response:', response)
    if (response.data.success) {
      showToast('修改成功')
      router.push({ name: 'User' })
    } else {
      showToast('修改失败: ' + response.data.message)
    }
  } catch (error) {
    showToast('修改失败，请稍后重试', error)
  }
}

const forget = () => {
  router.push({ name: 'AlterPassword' })
}
</script>

<style scoped>
.background {
  position: relative;
  top: 0;

  height: 100vh;
  width: 100vw;

  background-color: wheat;
}

.registerBox {
  position: relative;
  height: 70%;
  top: 14%;
  width: 80%;
  left: 10%;
  background-color: white;
  border-radius: 20px;

  z-index: 1;
  box-shadow: 0 0px 20px rgba(0, 0, 0, 0.2); /* 阴影效果 */
}

.headerBox {
  position: relative;
  height: 5%;
  top: 15%;
  width: 60%;
  left: 20%;
  background-color: white;

  font-weight: bold;
  font-size: 80%;
  text-align: center;
  border-radius: 10px;

  z-index: 3;
  box-shadow: 0 -7px 10px rgba(0, 0, 0, 0.2); /* 阴影效果 */
}

.row {
  top: 10%;
  left: 10%;
  position: relative;
  display: flex;
  width: 100%;
  height: 12%;
}

.label {
  width: 20%;
  font-weight: bold;
  font-size: 60%;
  min-width: 25%;
}

.inputBox {
  position: relative;
  width: 50%;
  height: 60%;
  border-radius: 10px;
  font-size: 60%;
}

.half {
  width: 25%;
  height: 50%;
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
.forget {
  top: 30%;
  left: 35%;
  height: 5%;
  width: 30%;
  position: relative;
  color: #007bff;
  text-decoration: underline;
  font-size: 60%;
}
</style>
