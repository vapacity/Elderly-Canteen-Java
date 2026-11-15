<template>
  <div class="container">
    <div class="info-wrapper">
      <div class="card" @mousemove="handleMouseMove" @mouseleave="resetCard">
        <img src="@/assets/poster.png" alt="Card Image" class="card-image" />
      </div>
      <div class="login-wrapper">
        <div class="msg"><a :href="backLink">← 返回登录</a></div>
        <div class="header">用户注册</div>
        <div class="form-wrapper">
          <div class="input-group">
            <p><span class="required">*</span>昵称</p>
            <input
              type="text"
              v-model="username"
              placeholder="昵称(不长于10个字符)"
              maxlength="10"
              class="input-item"
            />
          </div>
          <div class="input-group">
            <p><span class="required">*</span>密码</p>
            <input
              type="password"
              v-model="password"
              placeholder="密码(6-16个字符组成,区分大小写)"
              maxlength="16"
              minlength="6"
              class="input-item"
            />
          </div>
          <div class="input-group">
            <p><span class="required">*</span>确认密码</p>
            <input
              type="password"
              v-model="confirmPassword"
              placeholder="确认密码"
              class="input-item"
            />
          </div>
          <div class="input-group">
            <p><span class="required">*</span>手机号</p>
            <input
              type="text"
              v-model="phonenumber"
              placeholder="常用手机号"
              class="input-item"
              @input="validatePhoneNumber"
            />
            <button
              :class="{
                disabledButton: isButtonDisabled,
                enabledButton: !isButtonDisabled,
              }"
              :disabled="isButtonDisabled"
              @click="sendOTP"
            >
              {{ buttonText }}
            </button>
          </div>
          <div class="input-group">
            <p><span class="required">*</span>验证码</p>
            <input
              type="text"
              v-model="otp"
              placeholder="验证码"
              maxlength="6"
              class="input-item"
            />
          </div>
          <div class="input-group">
            <p><span class="required">*</span>性别</p>
            <select v-model="gender" class="input-item">
              <option value="">选择性别</option>
              <option value="male">男</option>
              <option value="female">女</option>
            </select>
          </div>
          <div class="input-group">
            <p>出生日期</p>
            <input
              type="date"
              v-model="birthdate"
              placeholder="出生日期"
              class="input-item"
            />
          </div>
          <div class="input-group">
            <p>头像</p>
            <input class="avatar" type="file" @change="handleAvatarUpload" />
          </div>
          <div class="avatar-preview">
            <img :src="avatarPreview || defaultAvatar" alt="头像预览" />
          </div>
          <div class="btn" @click="register">注册</div>
        </div>
      </div>
    </div>
    <transition name="fade">
      <div v-if="showMessage" class="message-popup">{{ showMessage }}</div>
    </transition>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      buttonText: "发送验证码",
      isButtonDisabled: false,
      countdown: 60,

      username: "",
      password: "",
      confirmPassword: "",
      phonenumber: "",
      otp: "",
      gender: "",
      birthdate: "",
      avatar: null,
      avatarPreview: null,
      defaultAvatar: require("@/assets/defaultportrait.png"),
      showMessage: "",
    };
  },
  computed: {
    backLink() {
      const from = this.$route.query.from;
      if (from === "login") {
        return "/login";
      } else if (from === "OTP") {
        return "/OTP"; // 假设你有一个名为/otpLogin的路由
      }
      // 默认返回，或者处理其他情况
      return "/login";
    },
  },
  methods: {
    validatePhoneNumber() {
      // 只保留数字
      this.phonenumber = this.phonenumber.replace(/\D/g, "");
      // 限制最大长度为11位
      if (this.phonenumber.length > 11) {
        this.phonenumber = this.phonenumber.slice(0, 11);
      }
    },
    handleAvatarUpload(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onloadend = () => {
          this.avatar = file;
          this.avatarPreview = reader.result;
        };
        reader.readAsDataURL(file);
      }
    },
    register() {
      if (
        !this.username ||
        !this.password ||
        !this.confirmPassword ||
        !this.phonenumber ||
        !this.otp ||
        !this.gender
      ) {
        this.showError("请填写所有必填项");
        return;
      }

      if (this.password !== this.confirmPassword) {
        this.showError("密码和确认密码不一致");
        return;
      }

      // 构建 FormData 对象
      const formData = new FormData();
      formData.append("userName", this.username);
      formData.append("password", this.password);
      formData.append("phone", this.phonenumber);
      formData.append("verificationCode", this.otp);
      formData.append("gender", this.gender);
      formData.append("birthDate", this.birthdate || ""); // 如果生日为空，则传递空字符串

      if (this.avatar) {
        formData.append("avatar", this.avatar); // 如果有上传头像，则添加到 FormData 中
      } else {
        // 将默认头像URL转换为Blob，然后添加到FormData中
        const img = new Image();
        img.src = this.defaultAvatar;
        img.onload = () => {
          const canvas = document.createElement("canvas");
          canvas.width = img.width;
          canvas.height = img.height;
          const ctx = canvas.getContext("2d");
          ctx.drawImage(img, 0, 0);
          canvas.toBlob((blob) => {
            const defaultAvatarFile = new File([blob], "defaultAvatar.png", {
              type: "image/png",
            });
            formData.append("avatar", defaultAvatarFile);
            this.sendRegisterRequest(formData); // 发送注册请求
          });
        };
        return; // 防止代码继续执行，等待头像处理完成
      }

      this.sendRegisterRequest(formData); // 发送注册请求
    },

    sendRegisterRequest(formData) {
      const config = {
        method: "post",
        url: "/demo-api/account/register",
        data: formData,
      };

      axios(config)
        .then((response) => {
          if (response.data.msg === "注册成功") {
            // 将 Token 存储在 localStorage
            localStorage.setItem("token", response.data.response.token);
            localStorage.setItem("identity", response.data.response.identity);
            console.log(response.data.response);
            this.showSuccess("注册成功！");
          } else {
            this.showError(response.data.msg);
          }
        })
        .catch((error) => {
          console.log(error);
          this.showError("注册失败，请稍后再试");
        });
    },

    async sendOTP() {
      if (this.phonenumber.length !== 11) {
        this.showError("手机号必须为11位数字");
        return;
      }

      try {
        const response = await axios.post(
          "/demo-api/Account/sendOTP",
          {
            PhoneNum: this.phonenumber,
          }
        );
        console.log(response);
        if (response.data.success) {
          this.isButtonDisabled = true;
          this.buttonText = `${this.countdown}s后重新发送`;

          const countdownInterval = setInterval(() => {
            if (this.countdown > 1) {
              this.countdown--;
              this.buttonText = `${this.countdown}s后重新发送`;
            } else {
              clearInterval(countdownInterval);
              this.resetButton();
            }
          }, 1000);
        } else {
          alert(response.data.msg);
        }
      } catch (error) {
        this.showError("发送验证码失败");
      }
    },
    resetButton() {
      this.buttonText = "发送验证码";
      this.isButtonDisabled = false;
      this.countdown = 60;
    },
    showError(message) {
      this.showMessage = message;
      setTimeout(() => {
        this.showMessage = "";
      }, 3000); // 错误信息3秒后消失
    },
    showSuccess(message) {
      this.showMessage = message;
      setTimeout(() => {
        this.showMessage = "";
        this.$router.push("/home");
      }, 2000);
    },

    handleMouseMove(event) {
      const card = event.currentTarget;
      const rect = card.getBoundingClientRect();
      const x = event.clientX - rect.left - rect.width / 2;
      const y = event.clientY - rect.top - rect.height / 2;
      const rotateX = (y / rect.height) * 15;
      const rotateY = (x / rect.width) * -15;

      card.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`;
      card.style.boxShadow = `${x * 0.1}px ${
        y * 0.1
      }px 20px rgba(67, 42, 20, 0.2)`;
      card.style.backgroundImage = `radial-gradient(circle at ${
        x + rect.width / 2
      }px ${y + rect.height / 2}px, rgba(255, 255, 255, 0.5), transparent)`;
    },
    resetCard(event) {
      const card = event.currentTarget;
      card.style.transform = `perspective(1000px) rotateX(0deg) rotateY(0deg)`;
      card.style.boxShadow = `none`;
      card.style.backgroundImage = `none`;
    },
  },
};
</script>

<style scoped>
/* 样式保持不变 */
.container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-image: url("https://images.unsplash.com/photo-1571162478581-ad3d0c1057ab?q=80&w=1770&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
  background-size: cover;
  background-position: center;
  overflow: hidden;
}

.container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 半透明黑色覆盖层 */
  transition: none; /* 禁用过渡效果 */
  z-index: 1;
  pointer-events: none; /* 禁用指针事件 */
}

.info-wrapper {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: center;
  align-items: center;
  background-color: white;
  border-radius: 5px;
}

.login-wrapper {
  position: relative;
  z-index: 2; /* 确保内容位于覆盖层之上 */
  width: 400px;
  height: 680px; /* 增加高度以适应更多字段 */
  padding: 10px 50px;
  font-size: 15px;
  overflow-y: scroll;
}

a {
  text-decoration: none;
  color: gray;
}

a:hover {
  text-decoration: none;
  color: #de6700;
}

.header {
  font-size: 25px;
  font-weight: bold;
  text-align: center;
  line-height: 40px;
  color: #de6700;
}

.form-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.input-group {
  display: flex;
  align-items: center;
  width: 100%;
  margin: 10px 0;
}

.input-group p {
  margin: 0;
  width: 80px; /* 设定固定宽度，根据需要调整 */
}

.input-item {
  flex: 1;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
  font-size: 10px;
}

.required {
  color: red;
  margin-right: 5px;
}

button {
  margin-left: 10px;
}

.btn {
  width: 20%;
  padding: 15px;
  margin: 20px 0;
  text-align: center;
  background-color: #de6700;
  width: 100px;
  color: white;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn:hover {
  background-color: #ff8e24;
}

.message-popup {
  position: fixed;
  top: 50px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  z-index: 999;
  opacity: 1;
  transition: opacity 0.5s ease-in-out;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}

.fade-enter, .fade-leave-to /* .fade-leave-active in <2.1.8 */ {
  opacity: 0;
}

.avatar-preview {
  margin-top: 10px;
}

.avatar-preview img {
  width: 100px;
  height: 100px;
  border-radius: 50%; /* 圆形头像 */
  object-fit: cover;
}

.card {
  width: 550px;
  height: 680px;
  padding: 10px;
  background-color: rgba(255, 219, 173, 0.7);
  border: 10px;
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  flex-direction: column;
  border-radius: 5px;
  transition: transform 0.2s ease, box-shadow 0.2s ease,
    background-image 0.2s ease;
}

.card-image {
  width: 100%;
  height: auto;
  border-radius: 10px;
  object-fit: cover;
}

.msg {
  margin-top: 10px;
}

button {
  padding: 8px;
  color: white;
  border: none;
  border-radius: 10px;
  transition: background-color 0.3s;
}

.enabledButton {
  background-color: rgba(135, 130, 130, 0.5);
}
.enabledButton:hover {
  background-color: darkorange;
}
.disabledButton {
  background-color: rgba(135, 130, 130, 0.5);
  cursor: not-allowed;
}
.avatar {
  width: 60%;
  border: none;
}
</style>
