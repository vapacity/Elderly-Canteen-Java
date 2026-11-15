<template>
  <div class="modal">
    <transition name="fade">
      <div v-if="showMessage" class="message-popup">{{ showMessage }}</div>
    </transition>
    <div class="modal-content">
      <div class="modal-header">
        <div class="header">改绑手机</div>
        <button class="close" @click="$emit('close')">&times;</button>
      </div>
      <div class="modal-body">
        <div class="input-group">
          <p>新手机号：</p>
          <input
            type="text"
            v-model="phonenumber"
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
          <p>验证码：</p>
          <input type="text" v-model="otp" />
        </div>
        <div class="btn" @click="changePhone">确认改绑</div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ChangePhoneModal",
  data() {
    return {
      phonenumber: "",
      otp: "",
      verificationStatus: "", // 'success' or 'failure'
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
    resetButton() {
      this.buttonText = "发送验证码";
      this.isButtonDisabled = false;
      this.countdown = 60;
    },
    async changePhone() {
      if (this.phonenumber.length !== 11) {
        this.show("手机号必须为11位数字");
        return;
      }
      if (!this.otp) {
        this.show("验证码不能为空！");
        return;
      }
      try {
        // 验证验证码逻辑
        await this.verifyOtp();
        if (this.verificationStatus == "success") {
          // 更新手机号逻辑
          await this.updatePhoneNumber();
        }
      } catch (error) {
        this.show("操作失败，请稍后再试");
      }
    },
    async verifyOtp() {
      try {
        const response = await axios.post(
          "/demo-api/Account/verifiationCodeWithoutUserCheck",
          {
            PhoneNum: this.phonenumber,
            Code: this.otp,
          }
        );
        console.log(response);
        if (response.data.success) {
          this.show(response.data.msg);
          this.verificationStatus = "success";
          return true;
        } else {
          this.verificationStatus = "failure";
          this.show(response.data.msg);
          return false;
        }
      } catch (error) {
        this.verificationStatus = "failure";
        this.show("验证失败");
      }
    },
    async updatePhoneNumber() {
      try {
        const token = localStorage.getItem("token");
        if (!token) {
          this.show("用户未登录，请先登录");
          return;
        }
        const headers = {
          Authorization: `Bearer ${token}`,
        };
        const response = await axios.post(
          "/demo-api/Account/changePhone",
          {
            NewPhoneNum: this.phonenumber,
          },
          { headers }
        );
        console.log(response);
        if (response.data.success) {
          this.show("改绑成功");
          this.$emit("close");
          this.$router.push("/profile");
        } else {
          this.show(response.data.msg);
        }
      } catch (error) {
        if (error.response) {
          this.show(`更新手机号失败: ${error.response.data.message}`);
        } else {
          this.show("网络错误或服务器未响应");
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
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 900;
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
  width: 400px;
  position: relative;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header {
  font-size: 30px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 10px;
  line-height: 40px;
  color: #d68c37;
  font-family: "Roboto", sans-serif; /* 使用自定义字体 */
  text-shadow: 2px 2px 4px rgba(251, 251, 103, 0.5); /* 添加文字阴影 */
}

.close {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}

.input-group {
  display: flex;
  align-items: center;
  margin: 10px 0;
}

.input-group p {
  margin: 0;
  width: 80px;
}

.input-group input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.input-group button {
  margin-left: 10px;
  padding: 5px 10px;
  border: none;
  background: orange;
  color: white;
  border-radius: 5px;
  cursor: pointer;
}

.btn {
  text-align: center;
  padding: 10px;
  margin-top: 20px;
  background-color: #de6700;
  color: white;
  border-radius: 5px;
  cursor: pointer;
}

.btn:hover {
  background-color: #ff8e24;
}

.disabledButton {
  background: grey;
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
