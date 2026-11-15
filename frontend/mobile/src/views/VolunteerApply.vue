<template>
  <ReturnButton :targetRoute="{ name: 'User' }" />
  <PersonalBackground :ava="portrait">
    <div class="header">志愿者申请</div>
    <div class="realname">真实姓名：{{ name }}</div>
    <div class="container">
      <textarea v-model="selfStatement" class="description" placeholder="输入申请内容"></textarea>
    </div>
    <button class="send" @click="sendApplication">提 交</button>
  </PersonalBackground>
  <BottomTabbar nowView="user" />
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import 'vant/es/toast/style'
import { showToast } from 'vant'

const router = useRouter()
const name = localStorage.getItem('name')
const portrait = localStorage.getItem('portrait')
const selfStatement = ref('')

const sendApplication = async () => {
  const token = localStorage.getItem('token')
  if (selfStatement.value.trim().length < 5) {
    showToast('请输入至少 5 个字的申请内容')
    return
  }
  try {
    const data = {
      selfStatement: selfStatement.value
    }

    const response = await axios.post('/demo-api/Volunteer/apply', data, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    })
    console.log('Response:', response)
    if (response.data.success) {
      showToast('发送成功')
      router.push({ name: 'User' })
    } else {
      showToast('发送失败')
    }
  } catch (error) {
    showToast('发送失败')
  }
}
</script>

<style scoped>
.container {
  position: relative;
  top: 20%;
  left: 10%;
  height: 40%;
  width: 80%;
  background-color: wheat;
  border-radius: 20px;
  border: none;
  color: black;
}

.description {
  position: relative;

  height: 90%;
  width: 90%;
  left: 5%;
  top: 5%;
  background-color: wheat;
  border: none;
  font-size: 60%;
}

.header {
  position: relative;
  font-weight: bold;
  font-size: 80%;
  text-align: center;
  top: 10%;
}

.send {
  position: relative;
  font-size: 60%;

  top: 25%;
  left: 30%;

  height: 10%;
  width: 40%;

  border-radius: 10px;

  border: none;
  color: white;
  font-weight: bold;
  background-color: #ffa822;
}

.realname {
  position: relative;

  top: 15%;
  left: 10%;

  height: 5%;
  width: 80%;

  font-size: 80%;
  font-weight: bold;
}
</style>
