<template>
  <div class="user-profile" v-if="userInfo"> <!-- 确保整个块在数据准备好之后才渲染 -->
    <div class="profile-card">
      <div class="profile-section">
        <div class="profile-header">关于我</div>
        <img src="/xian.png" alt="Divider" class="divider-image" />
      </div>
      <img :src="userInfo.portrait || '/avatar.jpg'" alt="User" class="user-image" />
      <h2>{{ userInfo.name }}</h2>
      <p>管理员</p> <!-- 职位固定为管理员 -->
      <div class="profile-section">
        <div class="profile-privilege">权限</div>
        <img src="/xian.png" alt="Divider" class="divider-image" />
        <button class="finance-button">{{ userInfo.position }}</button>
      </div>
    </div>
  </div>
  <!-- 如果数据尚未加载，可以显示一个加载指示器 -->
  <div v-else>
    <p>加载中...</p>
  </div>
</template>

<script>
export default {
  name: 'UserProfile',
  data() {
    return {
      userInfo: null, // 初始值为 null
    };
  },
  methods: {
    fetchUserInfo() {
      const token = localStorage.getItem("token"); // 获取存储的 token
      fetch('/demo-api/Admin/getAdminInfo', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      })
          .then((response) => response.json())
          .then((data) => {
            if (data.success) {
              this.userInfo = {
                portrait: data.response.portrait || '/avatar.jpg', // 如果头像不存在，使用默认头像
                name: data.response.accountName,
                position: data.response.position,
              };
            } else {
              console.error(data.msg);
            }
          })
          .catch((error) => console.error('获取用户数据时出错:', error));
    },
  },
  mounted() {
    this.fetchUserInfo();
  },
};
</script>

<style scoped>
.user-profile {
  position: absolute;
  top: 20px;
  left: 20px;
}

.profile-card {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 250px;
  position: relative;
}

.profile-section {
  width: 100%;
  text-align: left;
  margin-bottom: 10px;
}

.profile-header, .profile-privilege {
  font-size: 14px;
  font-weight: bold;
  color: #D8CBA7; /* 使用与提供图片相同的颜色 */
}

.divider-image {
  width: 100%;
  margin: 5px 0;
}

.user-image {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-top: 10px;
  margin-bottom: 10px;
}

h2 {
  margin: 10px 0 5px;
  font-size: 20px;
  font-weight: bold;
}

p {
  margin: 5px 0;
  font-size: 14px;
  color: #777;
}

.finance-button {
  background-color: #e0f7fa;
  border: none;
  border-radius: 5px;
  padding: 5px 10px;
  color: #00796b;
  cursor: pointer;
  margin-top: 10px;
}
select,input {
  border: 2px solid black;
}
</style>
