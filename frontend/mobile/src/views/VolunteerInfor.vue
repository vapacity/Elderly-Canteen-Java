<template>
    <PersonalBackground  :ava="portrait">
    <ReturnButton :targetRoute="{ name: 'Home' }" />
    <div class="textContainer">
        <span class="head">志愿者信息</span>
        <div class="space"></div>
        <div class="row"><span class="label">ID:</span> <span class="value">{{ userData.accountId }}</span></div>
        <div class="row"><span class="label">姓名:</span> <span class="value">{{ userData.name }}</span></div>
        <div class="row"><span class="label">创建时间:</span> <span class="value">{{ userData.time }}</span></div>
        <div class="row"><span class="label">接单次数:</span> <span class="value">{{ userData.deliverCount }}</span></div>
        <div class="row"><span class="label">勋章墙:</span></div>
        <div class="medal-wall">
          <img v-for="n in numberOfMedals" :key="n" src="../assets/medal.png" alt="勋章" class="medal" />
        </div>
      </div>    
    </PersonalBackground>
    <BottomTabbar nowView="user" />
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import axios from 'axios';
  import { computed } from 'vue';
  import 'vant/es/toast/style'
  import { showToast } from 'vant'

  const portrait = localStorage.getItem('portrait');

  const userData = ref({
    accountId: '',
    name: '',
    time: '',
    deliverCount: '',
  });
  
  
  const numberOfMedals = computed(() => {
  return Math.floor(userData.value.deliverCount / 5);
  })
  
  
  const fetchData = async () => {
  try {
    // 从 localStorage 中获取保存的 Token
    const token = localStorage.getItem('token');
  
    // 使用 axios 发起 GET 请求，附带 Authorization 头
    const response = await axios.get('/demo-api/Volunteer/getVolInfo', {
      headers: {
        Authorization: `Bearer ${token}`  // 将 Token 添加到 Authorization 头中
      }
    });
  
    if (response.data.success === true) {
      console.log(response.data.response); // 调试用
      userData.value = response.data.response;
    } else {
      showToast('获取信息失败')
    }
  } catch (error) {
    showToast(error);
  }
  };
  
  
  onMounted(() => {
    fetchData();
  });
  </script>
  
  <style scoped>
.medal-wall {
  display: flex;
  flex-wrap: wrap;
}
.medal {
  width: 40px;
  height: 40px;
  margin: 5px;
}

  .head{
  font-weight: bold;
  font-size: 120%;
  }
  
  .space{
  height: 8%
  }
  
  .row {
  left: 0%;
  top: 0%;
  display: flex;
  flex-direction: row;
  width: 80vw;
  height: 10%
  }
  
  .label{
  font-weight: bold;
  min-width: 30%;
  text-align: left;
  font-size: 100%;
  }
  
  .value {
  text-align: left;
  font-size: 100%;
  }
  
  .textContainer {
    position: relative;
    top: 10%;
    height: 60%;
    font-size: 0.4rem;
    padding-left: 15%; /* 调整左侧距离 */
    
    z-index: 10;
  }
  
  .buttonContainer{
  display: flex;
  flex-direction: column;
  color: white;
  font-weight: bold;
  height: 40%;
  width: 100%;
  }
  
  .buttonType{
    position: relative;
    font-size: 60%;
  
    height: 25%;
    width: 50%;
  
    left: 25%;
    background-color: #ffa822;
    border-radius: 10px ;
    z-index: 10;
  
    border: none;
  }
  
  .authen{
  top: 5%;
  }
  
  .apply{
  top: 15%;
  }
  
  </style>
  