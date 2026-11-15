<template>
  <div class="modal">
    <transition name="fade">
      <div v-if="showMessage" class="message-popup">{{ showMessage }}</div>
    </transition>
    <div class="modal-content">
      <div class="modal-header">
        <div class="header">修改密码</div>
        <button class="close" @click="$emit('close')">&times;</button>
      </div>
      <div class="modal-body">
        <div class="input-group">
          <p>原密码：</p>
          <input type="password" v-model="oldPassword" />
          <a @click="forgotPassword">忘记密码?</a>
        </div>
        <div class="input-group">
          <p>新密码：</p>
          <input type="password" v-model="newPassword" />
        </div>
        <div class="input-group">
          <p>确认密码：</p>
          <input type="password" v-model="confirmPassword" />
          <div v-if="passwordMismatch" class="error">两次输入不一致</div>
        </div>
        <div class="btn" @click="changePassword">确认修改</div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "ChangePasswordModal",
  data() {
    return {
      oldPassword: "",
      newPassword: "",
      confirmPassword: "",
      passwordMismatch: false,
      showMessage: "",
    };
  },
  methods: {
    forgotPassword() {
      this.$emit("close");
      // 这里可以调用显示忘记密码弹窗的逻辑
      this.$router.push({
        path: "/newpsd",
        query: { from: "change-password", phone: this.phone },
      });
    },
    async changePassword() {
      if (!this.oldPassword || !this.newPassword || !this.confirmPassword) {
        this.showError("请输入原密码、新密码和确认密码");
        return;
      }

      if (this.newPassword !== this.confirmPassword) {
        this.passwordMismatch = true;
        return;
      }

      const token = localStorage.getItem("token");
      try {
        const response = await axios.post(
          `/demo-api/Account/alterPassword`,
          {
            oldPassword: this.oldPassword,
            newPassword: this.newPassword,
          },
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (response.data.success) {
          // this.$emit("close");
          this.showSuccess(response.data.msg);
        } else {
          this.showError(response.data.msg);
        }
      } catch (error) {
        if (error.response) {
          // 请求已发出，但服务器响应的状态码不在 2xx 范围内
          if (error.response.status === 400) {
            this.showError("原密码不正确");
          }
        } else {
          // 一些其他的错误
          console.error("密码修改失败", error);
          this.showError("密码修改失败，请稍后再试");
        }
      }
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
        this.$emit("close");
      }, 2000);
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
  max-width: 300px; /* 限制输入框最大宽度为200px，调整为你需要的值 */
}

.error {
  color: red;
  margin-left: 10px;
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

a {
  margin-left: 7px;
  text-decoration: underline; /* 添加下划线 */
  color: rgb(186, 143, 42); /* 设置颜色为蓝色 */
  cursor: pointer; /* 鼠标悬停时显示为指针 */
  font-size: 15px;
}

a:hover {
  color: rgb(175, 196, 35); /* 可选：鼠标悬停时颜色变为深蓝色 */
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
