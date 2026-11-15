<template>
  <ReturnButton :targetRoute="{ name: 'Alter' }" />
  <div class="background">
    <div class="headerBox">改绑手机</div>
    <div class="registerBox">
      <div class="row">
        <span class="label">新手机号</span> <input class="inputBox" v-model="newPhoneNum" />
      </div>
      <button class="getIn" @click="Ensure">确认修改</button>
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
const newPhoneNum = ref('')

const Ensure = async () => {
  const token = localStorage.getItem('token')
  try {
    const data = {
      newPhoneNum: newPhoneNum.value
    }

    const response = await axios.post('/demo-api/Account/changePhone', data, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    })
    console.log('Response:', response)
    if (response.data.success) {
      router.push({ name: 'User' })
    } else {
      showToast('修改失败')
    }
  } catch (error) {
    console.error('Error updating account:', error)
    showToast('修改失败')
  }
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
</style>
