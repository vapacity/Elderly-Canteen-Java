<template>
  <div class="container">
    <transition name="fade">
      <div v-if="showMessage" class="message-popup">{{ showMessage }}</div>
    </transition>
    <div class="login-register-wrapper">
      <div class="login-section">
        <div class="back"><a href="/home">← 返回</a></div>
        <div class="header">用户登录</div>
        <div class="form-wrapper">
          <input
            type="tel"
            v-model="phonenumber"
            placeholder="请输入手机号"
            class="input-item"
            @input="validatePhoneNumber"
          />
          <div class="otp">
            <input
              type="text"
              v-model="otp"
              placeholder="验证码"
              class="input-item"
              style="width: 60%"
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
          <a href="/login">切换到手机号密码登录>></a>
          <div class="btn" @click="login">登录</div>
        </div>
        <div class="msg">
          没有账号？
          <a href="/register?from=OTP">注册</a>
        </div>
      </div>
      <div class="poster-section"></div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      phonenumber: "",
      otp: "",

      buttonText: "发送验证码",
      isButtonDisabled: false,
      countdown: 60,
      showMessage: "",
    };
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
    async sendOTP() {
      if (this.phonenumber.length !== 11) {
        this.show("手机号必须为11位数字");
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
          this.show(response.data.msg);
        }
      } catch (error) {
        this.show("发送验证码失败");
      }
    },
    async login() {
      if (this.phonenumber.length !== 11) {
        this.show("手机号必须为11位数字");
        return;
      }
      if (!this.otp) {
        this.show("验证码不能为空！");
      }
      console.log(this.phonenumber);
      console.log(this.otp);
      try {
        const response = await axios.post(
          "/demo-api/Account/verifiationCodeLogin",
          {
            PhoneNum: this.phonenumber,
            Code: this.otp,
          }
        );
        console.log(response);
        if (response.data.success) {
          // 将 Token 存储在 localStorage
          localStorage.setItem("token", response.data.response.token);
          localStorage.setItem("identity", response.data.response.identity);
          // 跳转到 /home 页面
          this.$router.push("/home");
        } else {
          this.show(response.data.msg);
        }
      } catch (error) {
        if (error.response) {
          // 请求已发出，但服务器响应的状态码不在 2xx 范围内
          if (error.response.status === 404) {
            this.show("用户不存在");
          } else if (error.response.status === 400) {
            this.show("验证码不正确");
          } else {
            this.show("登录失败");
          }
        } else {
          // 一些其他的错误
          this.show("登录失败");
        }
      }
    },
    show(message) {
      this.showMessage = message;
      setTimeout(() => {
        this.showMessage = "";
      }, 3000);
    },
    resetButton() {
      this.buttonText = "发送验证码";
      this.isButtonDisabled = false;
      this.countdown = 60;
    },
  },
};
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-image: url("https://images.unsplash.com/photo-1571162478581-ad3d0c1057ab?q=80&w=1770&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
  background-size: cover;
  background-position: center;
}

.container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.2); /* 半透明黑色覆盖层 */
  z-index: 1;
}

.login-register-wrapper {
  position: relative;
  z-index: 2;
  display: flex;
  background-color: rgba(191, 136, 65, 0.7);
  width: 700px;
  height: 500px;
  box-shadow: 10px 20px 10px rgba(0, 0, 0, 0.5);
}

.login-section {
  width: 50%;
  padding: 40px;
  background-color: rgba(0, 0, 0, 0);
}

.poster-section {
  width: 80%;
  background-image: url("@/assets/poster.png");
  background-size: cover; /* 使背景图片覆盖整个元素 */
  background-position: center; /* 使背景图片居中 */
  background-repeat: no-repeat; /* 防止背景图片重复 */
}

.back {
  margin-top: -20px;
  margin-bottom: 15px;
  margin-left: -15px;
}

.header {
  font-size: 38px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 40px;
  color: #fff;
}

.form-wrapper {
  display: flex;
  flex-direction: column;
}

.input-item {
  display: block;
  width: 100%;
  margin-bottom: 20px;
  border: none;
  border-bottom: 2px solid #ccc;
  padding: 10px 0;
  background: none;
  font-size: 16px;
  outline: none;
  color: #fff;
  transition: border-color 0.3s;
}

.input-item::placeholder {
  color: #ccc;
}

.input-item:focus {
  border-bottom-color: darkorange;
}

.btn {
  text-align: center;
  padding: 10px;
  width: 100%;
  margin-top: 40px;
  margin-left: -10px;
  color: #fff;
  cursor: pointer;
  background-color: orange;
  border-radius: 10px;
  transition: background-color 0.3s;
}

.btn:hover {
  background-color: darkorange;
}

.msg {
  text-align: center;
  line-height: 88px;
  color: #fff;
}

a {
  text-decoration: none;
  color: #ffe281;
}

a:hover {
  text-decoration: underline; /* 鼠标悬停时显示下划线 */
}
.otp {
  display: flex;
  align-items: center;
}
button {
  margin-left: 10px;
  margin-right: -10px;
  margin-top: -20px;
  padding: 10px;
  color: white;
  border: none;
  border-radius: 15px;
  transition: background-color 0.3s;
}

.enabledButton {
  background-color: rgba(72, 45, 8, 0.5);
}
.enabledButton:hover {
  background-color: darkorange;
}
.disabledButton {
  background-color: rgba(72, 45, 8, 0.5);
  cursor: not-allowed;
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
