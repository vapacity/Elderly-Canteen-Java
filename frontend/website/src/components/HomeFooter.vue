<!-- components/Footer.vue -->
<template>
  <div class="footer">
    <div class="contact-info">
      <div class="contact-header">联系我们</div>
      <p>地址: 上海市嘉定区曹安公路4800号</p>
      <p>电话: 123456</p>
    </div>
    <div class="qrcode">
      <div class="qrcode-header">手机程序二维码</div>
      <img src="@/assets/qr-code.png" alt="QR Code" class="qr-code" />
    </div>
    <div class="restaurant-info">
      <div class="restaurant-name">老人食堂</div>
      <img
        src="@/assets/logo.png"
        alt="Restaurant Logo"
        class="restaurant-logo"
      />
    </div>
  </div>
</template>

<style scoped>
.footer {
  display: flex;
  justify-content: space-between;
  background-color: #ffddb1;
  padding: 20px;
  border-top: 1px solid #e7e7e7;
}

.contact-info,
.restaurant-info {
  width: 30%;
  color: #e1810b;
}
.qrcode {
  color: #e1810b;
  margin-right: 50px;
}

.contact-header,
.qrcode-header {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
}

.qr-code {
  width: 150px;
  height: 150px;
  display: block;
  margin: 10px auto;
  height: auto;
}

.restaurant-name {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
  text-align: center;
}

.restaurant-logo {
  width: 150px;
  height: 150px;
  display: block;
  margin: 0 auto;
}
</style>
