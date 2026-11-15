<template>
  <div class="app-bar">
    <div class="menu-button">
      &#9776; {{ selectedMenu }}
    </div>
    <div class="spacer"></div>
    <div class="user-icon" @click="$emit('menu-selected', '我')">
      <img :src="avatarPreview" alt="头像" />
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "AppBar",
  props: {
    selectedMenu: {
      type: String,
      default: '公告管理'
    }
  },
  data() {
    return {
      avatarPreview: this.defaultAvatar, 
    };
  },
  created() {
    this.fetchUserData();
  },
  methods: {
    fetchUserData() {
      const token = localStorage.getItem("token"); // 获取存储的 token

      const config = {
        method: "get",
        url: "/demo-api/account/getPersonInfo",
        headers: {
          Authorization: `Bearer ${token}`, // 在请求头中包含 token
        },
      };

      axios(config)
        .then((response) => {
          if (response.data.getSuccess) {
            const user = response.data.response;
            // 更新用户名和头像
            this.avatarPreview = user.portrait
              ? `${user.portrait}`
              : this.defaultAvatar;
          } else {
            alert(response.data.msg); // 显示错误信息
          }
        })
        .catch((error) => {
          console.error("Error fetching user data:", error);
        });
    },
  }
};
</script>

<style scoped>
.app-bar {
  display: flex;
  align-items: center;
  padding: 0 16px;
  padding-left: 230px;
  height: 56px;
  background-color: #ffffff; /* 顶部栏背景颜色 */
  border-bottom: 1px solid #ddd;
  position: fixed;
  top: 0;
  right: 0;
  left: 0;
  z-index: 1001; /* 确保顶部栏在所有内容之上 */
}
.menu-button {
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
}
.spacer {
  flex: 1;
}
.user-icon img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  cursor: pointer;
}
</style>