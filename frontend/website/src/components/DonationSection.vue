<!-- components/DonationSection.vue -->
<template>
  <section>
    <!-- 锚点，用于使导航栏跳转到本区域上方50px的位置，不然顶部导航栏会遮住部分内容 -->
    <a
      id="donateNav"
      style="display: block; height: 50px; margin-top: -50px"
    ></a>
    <div class="donation">
      <transition name="fade">
        <div v-if="showMessage" class="message-popup">{{ showMessage }}</div>
      </transition>
      <div class="donation-info">
        <div class="donation-intro">
          随着社会老龄化进程的加快，越来越多的长者面临着生活照料和精神关怀的需求。长者食堂旨在为社区内的老年人提供一个温馨舒适的用餐环境，不仅解决了他们日常饮食的难题，还为他们搭建了一个交流互动的平台。
        </div>
        <div class="donation-text">
          <h1 style="margin-bottom: 20px">您的支持至关重要</h1>
          <p>
            我们诚挚邀请您加入我们的行列，您的每一份贡献都将化作实实在在的帮助。
          </p>
          <h1 style="margin: 20px 0; font-size: large">如何参与</h1>
          <p style="margin-bottom: 10px">
            捐款：您可以直接向我们的长者食堂项目捐款，任何数额都是宝贵的。
          </p>
          <p>
            志愿服务：如果您有时间，欢迎您成为我们的志愿者，参与长者食堂的服务工作。（详情请扫描下方二维码）
          </p>
        </div>
        <button @click="showDonationModal" class="donate-button">捐赠</button>
      </div>

      <div class="donation-section">
        <div
          class="scrolling-text"
          :style="{ animationDuration: scrollDuration + 's' }"
        >
          <span
            class="notice"
            v-for="(donation, index) in donationList"
            :key="index"
          >
            <!-- 格式化显示每条捐赠信息 -->
            {{ donationText(donation) }}
          </span>
        </div>
      </div>
    </div>
  </section>
  <!-- 捐赠弹窗 -->
  <div v-if="isDonationModalVisible" class="modal">
    <div class="modal-content">
      <div class="modal-header">
        <h3>我要捐赠</h3>
        <button class="close" @click="closeDonationModal">&times;</button>
      </div>
      <div class="modal-body">
        <div class="image-column">
          <img
            src="@/assets/donating.png"
            alt="Donating"
            class="donating-image"
          />
        </div>
        <div class="form-column">
          <label><b>请输入您的署名:</b></label>
          <input type="string" v-model="donationName" />
          <label><b>请输入您的捐赠金额:</b></label>
          <input
            type="number"
            v-model="donationAmount"
            @input="validateDonationAmount"
          />
          <div class="payment-methods">
            <button
              class="wechat"
              @click="selectPaymentMethod('wechat')"
              :class="{ selected: paymentMethod === 'wechat' }"
            >
              微信支付
            </button>
            <button
              class="alipay"
              @click="selectPaymentMethod('alipay')"
              :class="{ selected: paymentMethod === 'alipay' }"
            >
              支付宝支付
            </button>
          </div>
          <img
            v-if="paymentMethod === 'wechat'"
            :src="qrCodeImageWechat"
            alt="WeChat QR Code"
            class="qr-code"
          />
          <img
            v-else-if="paymentMethod === 'alipay'"
            :src="qrCodeImageAlipay"
            alt="Alipay QR Code"
            class="qr-code"
          />
          <button @click="donate" class="confirm-button">确认捐赠</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import qrCodeImageWechat from "@/assets/qr-code-wechat.png";
import qrCodeImageAlipay from "@/assets/qr-code-alipay.png";

export default {
  props: {
    donationList: Array,
    scrollDuration: Number,
    loggedIn: {
      type: Boolean,
      required: true,
    },
    user: {
      type: Object,
      required: true,
      default: () => ({ name: "", avatar: "", id: "" }),
    },
  },
  data() {
    return {
      isDonationModalVisible: false,
      donationName: "",
      donationAmount: 0,
      paymentMethod: "",
      wechatButton: false,
      alipayButton: false,
      qrCodeImageWechat, // 这张图旋转+翻转过，为了在调试阶段区别两个二维码
      qrCodeImageAlipay, // 这张图没改
      message: "", // 新增的 message 数据属性,用于捐赠弹窗
      errorMessage: "",
      showMessage: "",
    };
  },
  methods: {
    // 格式化显示每条捐赠信息
    donationText(donation) {
      return `${donation.date}  感谢 ${donation.name} 捐赠了 ${donation.amount}元！`;
    },

    showDonationModal() {
      if (!this.loggedIn) {
        this.show("请先登录！");
        return;
      }
      console.log("donate person arealdy loggedin");
      this.isDonationModalVisible = true;
    },
    closeDonationModal() {
      this.isDonationModalVisible = false;
    },
    donate() {
      // 验证输入金额是否大于0
      if (this.donationAmount <= 0) {
        this.show("请输入大于0的金额");
        return;
      }

      const donationAPI = "/demo-api/Donate/submitDonation";
      const donationData = {
        accountId: this.user.id,
        price: this.donationAmount,
        origin: this.donationName ? this.donationName : "匿名",
      };
      axios
        .post(donationAPI, donationData)
        .then((response) => {
          if (response.data.success) {
            this.show("捐赠成功！感谢您的支持！");
            window.location.reload(); // 重新获取捐赠列表
            this.closeDonationModal(); // 关闭捐赠弹窗
          } else {
            this.show("捐赠失败，请重试。");
            window.location.reload(); // 重新获取捐赠列表
            this.closeDonationModal(); // 关闭捐赠弹窗
          }
        })
        .catch((error) => {
          console.error("捐赠失败", error);
          this.show("捐赠失败，请重试。");
          window.location.reload(); // 重新获取捐赠列表
          this.closeDonationModal(); // 关闭捐赠弹窗
        })
        .finally(() => {
          setTimeout(() => {
            this.closeDonationModal();
          }, 1500);
        });
    },
    selectPaymentMethod(method) {
      if (method == "wechat") {
        this.wechatButton = !this.wechatButton;
        this.alipayButton = false;
        if (this.wechatButton) this.paymentMethod = method;
        else this.paymentMethod = "";
      } else if (method == "alipay") {
        this.alipayButton = !this.alipayButton;
        this.wechatButton = false;
        if (this.alipayButton) this.paymentMethod = method;
        else this.paymentMethod = "";
      }
    },
    validateDonationAmount() {
      const value = this.donationAmount.toString();
      const decimalIndex = value.indexOf(".");

      if (decimalIndex >= 0 && value.length - decimalIndex - 1 > 2) {
        this.donationAmount = parseFloat(value).toFixed(2);
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
.donation {
  background-image: url("@/assets/donate.jpg"); /* 背景图片路径 */
  background-size: 100% auto; /* 背景图片宽度对齐，高度自动调整 */
  background-repeat: no-repeat; /* 防止背景图片重复 */
  background-position: top; /* 背景图片居中对齐 */
  display: flex;
  flex-direction: column; /* 垂直堆叠内容 */
  align-items: center;
}
.donation-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30px;
}

.donation-intro {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30px;
  height: 40vh;
  width: 40%;
  background-color: #ffddb1;
  border-radius: 35%;
  border-bottom-left-radius: 0;
  border-top-right-radius: 0;
  box-shadow: 0 0px 15px rgba(0, 0, 0, 0.3);
  letter-spacing: 1ch;
}
.donation-text {
  margin: 0 20px;
  width: 50%;
}

.donate-button {
  width: 15%;
  margin-top: 10px;
  padding: 20px 20px;
  background-color: rgba(203, 127, 27, 0.798);
  color: #e6e6e6;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 20px;
  transition: background-color 0.3s;
}

.donate-button:hover {
  background-color: rgb(203, 127, 27);
}

.payment-methods button:hover {
  /* 鼠标移到上面 */
  color: #ffffff;
  background-color: rgba(255, 150, 30, 0.5);
}

.wechat.selected {
  /* 鼠标选中 */
  color: #ffffff;
  background-color: rgb(255, 150, 30);
}
.alipay.selected {
  color: #ffffff;
  background-color: rgb(255, 150, 30);
}

.donation-section {
  overflow: hidden;
  white-space: nowrap;
  width: 100%;
}

.scrolling-text {
  display: inline-block;
  white-space: nowrap;
  animation: scroll linear infinite;
  /* animation-duration 动态设置 */
}

.notice {
  background-color: rgba(253, 235, 216, 0.8);
  margin: 15px;
  padding: 15px;
  border-radius: 30px;
  font-size: 20px;
}
@keyframes scroll {
  0% {
    transform: translateX(20%);
  }
  100% {
    transform: translateX(-100%);
  }
}

.scrolling-text span {
  display: inline-block;
}

.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.5s ease;
}

.slide-up-enter,
.slide-up-leave-to {
  transform: translateY(20px);
  opacity: 0;
}

/* 弹窗 */
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
  border-radius: 10px;
  width: 35%;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fcaa26; /* 背景色 */
  padding: 10px;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}

.modal-body {
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 20px;
  overflow: auto; /* 添加滚动条以防止内容溢出 */
  gap: 10px;
}

.close {
  background: none;
  border: none;
  font-size: 2rem;
  cursor: pointer;
}

.image-column {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 90%;
  padding: 0;
  margin-bottom: 20px;
}

.donating-image {
  max-width: 100%;
  height: auto;
}

.form-column {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;
  gap: 10px; /* 设置各组件之间的边距 */
}

.form-column label {
  margin-bottom: 10px;
  font-size: 150%;
  color: #e1810b;
}

.form-column input {
  margin-bottom: 10px;
  padding: 10px;
  width: 100%;
  box-sizing: border-box;
}

.payment-methods,
.confirm-button {
  margin-bottom: 10px;
  width: 100%;
}

.payment-methods {
  display: flex;
  justify-content: space-around;
}

.wechat,
.alipay {
  padding: 10px;
  border: none;
  background-color: #3dc041;
  color: white;
  font-size: 1rem;
  font-weight: bold;
  border-radius: 10px;
  cursor: pointer;
  flex-grow: 1;
  margin: 0 5px;
}

.alipay {
  background-color: #5995ef;
}

.payment-methods button :hover {
  background-color: #d8a773;
  color: #fff;
}

.qr-code {
  width: 150px;
  height: 150px;
  display: block;
  margin: 10px auto;
  height: auto;
}

.confirm-button {
  padding: 5%;
  background-color: #ff5f82;
  color: white;
  font-size: 1rem;
  font-weight: bold;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.confirm-button:hover {
  background-color: #a93c49;
}

.toast {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  background-color: black;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  font-size: 16px;
  opacity: 0.9;
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
