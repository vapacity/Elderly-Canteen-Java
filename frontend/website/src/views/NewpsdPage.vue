<template>
  <div class="container">
    <transition name="fade">
      <div v-if="showMessage" class="message-popup">{{ showMessage }}</div>
    </transition>
    <div class="wrapper">
      <div class="header">忘记密码</div>

      <div class="inputpart" v-if="step === 1">
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
        <button @click="verifyOTP">下一步</button>
      </div>

      <div class="inputpart" v-else-if="step === 2">
        <label for="newPassword">请输入新的密码:</label>
        <input
          type="password"
          v-model="newPassword"
          id="newPassword"
          placeholder="新的密码"
        />

        <label for="confirmPassword">确认密码:</label>
        <input
          type="password"
          v-model="confirmPassword"
          id="confirmPassword"
          placeholder="确认密码"
        />

        <button @click="resetPassword">重置密码</button>
      </div>

      <div v-if="showSuccessMessage" class="success-message">
        密码重置成功,2秒后跳转...
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      step: 1,
      phonenumber: "",
      otp: "",
      newPassword: "",
      confirmPassword: "",
      buttonText: "发送验证码",
      isButtonDisabled: false,
      countdown: 60,
      showSuccessMessage: false,
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
    resetButton() {
      this.buttonText = "发送验证码";
      this.isButtonDisabled = false;
      this.countdown = 60;
    },
    async verifyOTP() {
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
          this.show("验证成功");
          this.step = 2;
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
            this.show("验证失败");
          }
        } else {
          // 一些其他的错误
          this.show("验证失败");
        }
      }
    },
    async resetPassword() {
      if (!this.newPassword || !this.confirmPassword) {
        this.show("请输入新密码和确认密码");
        return;
      }

      if (this.newPassword !== this.confirmPassword) {
        this.show("两次输入的密码不一致");
        return;
      }
      const token = localStorage.getItem("token");
      try {
        const response = await axios.post(
          `/demo-api/Account/changePassword?pswd=${this.newPassword}`,
          {},
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log(response);
        if (response.data.success) {
          this.showSuccessMessage = true;
          setTimeout(() => {
            this.$router.push("/login");
          }, 2000);
        } else {
          this.show(response.data.msg || "密码重置失败，请稍后重试");
        }
      } catch (error) {
        if (error.response) {
          // 请求已发出，但服务器响应的状态码不在 2xx 范围内
          if (error.response.status === 401) {
            this.show("用户身份验证失败");
          } else {
            console.error("密码重置失败", error);
            this.show("密码重置失败，请稍后再试");
          }
        } else {
          console.error("密码重置失败", error);
          this.show("密码重置失败，请稍后再试");
        }
      }
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

.wrapper {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: rgba(191, 136, 65, 0.7);
  width: 500px;
  height: 400px;
  border-radius: 10px;
  padding: 10px;
  box-shadow: 10px 20px 10px rgba(0, 0, 0, 0.5);
}

.header {
  font-size: 38px;
  font-weight: bold;
  text-align: center;
  margin: 20px;
  color: orange;
}

.inputpart {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 350px;
}

.input-item {
  display: block;
  width: 100%;
  margin-bottom: 20px;
  border: none;
  border-bottom: 2px solid #ccc;
  background: none;
  font-size: 16px;
  outline: none;
  color: #fff;
  transition: border-color 0.3s;
}

.otp {
  width: 350px;
  display: flex;
  align-items: center;
  justify-content: space-between;
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

button {
  padding: 10px 20px;
  margin-top: 10px;
  border: none;
  color: white;
  background-color: orange;
  border-radius: 10px;
  transition: background-color 0.3s;
  cursor: pointer;
  max-width: 150px;
}

button:hover {
  background-color: darkorange;
}

input {
  margin: 20px 0;
  display: block;
  border: none;
  border-bottom: 2px solid #ccc;
  padding: 10px 0;
  background: none;
  font-size: 16px;
  outline: none;
  color: #fff;
  transition: border-color 0.3s;
}

input::placeholder {
  color: #ccc;
}

input:focus {
  border-bottom-color: darkorange;
}

label {
  color: #fff;
  font-size: 20px;
}

.success-message {
  color: #c6c4c4;
  font-size: 12px;
  text-align: center;
  margin-top: 20px;
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
