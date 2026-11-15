<!-- components/Header.vue -->
<template>
  <transition name="fade">
      <div v-if="showMessage" class="message-popup">{{ showMessage }}</div>
    </transition>
  <div class="header">
    <div class="header-left">
      <span class="canteen-name">老人食堂</span>
    </div>
    <nav>
      <ul>
        <li><a href="#searchNav">搜索</a></li>
        <li><a href="#menuNav">菜单</a></li>
        <li><a href="#reviewsNav">评价</a></li>
        <li><a href="#donateNav">捐赠</a></li>
      </ul>
    </nav>
    <div class="header-right">
      <template v-if="loggedIn">
        <img
          :src="avatarPreview"
          alt="Avatar"
          class="avatar"
          @click="goToProfile"
        />
        <span class="username">{{ username }}，你好！</span>
      </template>
      <template v-else>
        <button @click="goToLogin" class="login-button">登录</button>
      </template>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  props: {
    loggedIn: {
      type: Boolean,
      required: true,
    },
  },
  data() {
    return {
      username: "",
      avatarPreview: this.defaultAvatar, // 默认头像
      defaultAvatar: require("@/assets/defaultportrait.png"), // 默认头像路径
      showMessage: "",
    };
  },
  created() {
    this.fetchUserData();
  },
  methods: {
    sleep(ms) {
      return new Promise(resolve => setTimeout(resolve, ms));
    },
    goToLogin() {
      this.$router.push("/login");
    },
    async goToProfile() {
      const role = localStorage.getItem("identity");
      if(role=='admin'){
        this.show("管理员身份认证成功！正在跳转...");
        await this.sleep(2000);
        this.$router.push("/admin");//管理员页
      }
      else{
        this.$router.push("/profile");
      }
    },
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
            this.username = user.accountName;
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
    show(message) {
      this.showMessage = message;
      setTimeout(() => {
        this.showMessage = "";
      }, 3000);
    },
  },
};
</script>

<style scoped>
/* Header 相关样式 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: rgba(255, 184, 73);
  padding: 10px 20px;
  margin-bottom: -1px;
  position: sticky;
  top: 0;
  z-index: 99;
}

.header-left {
  font-size: 24px;
  font-weight: bold;
}

.header-right {
  display: flex;
  align-items: center;
}

.canteen-name {
  color: #ffffff;
}

nav ul {
  list-style-type: none;
  padding: 0;
}

nav ul li {
  display: inline;
  margin-left: 5vh;
  margin-right: 5vh;
}

nav ul li a {
  color: white;
  text-decoration: none;
}

nav ul li a:hover {
  text-decoration: underline;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
}
.username {
  margin-left: 10px;
  font-size: 14px;
  color: #ffffff;
}

.login-button {
  text-align: center;
  padding: 10px 30px;
  color: #fff;
  cursor: pointer;
  background-color: #42b983;
  border-radius: 10px;
  border: none;
  transition: background-color 0.3s;
}

.login-button:hover {
  background-color: #369d6e;
}

.message-popup {
  position: fixed;
  top: 50px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.6);
  color: rgb(255, 255, 255);
  padding: 10px 20px;
  border-radius: 5px;
  z-index: 999;
  opacity: 1;
  transition: opacity 0.5s ease-in-out;
}
</style>
