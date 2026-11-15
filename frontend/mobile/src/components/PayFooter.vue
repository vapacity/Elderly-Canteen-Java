<template>
  <div class="pay-footer">
    <hr class="divider" />
    <div class="pay-footer-buttons">
      <van-button
        type="primary"
        class="order-button"
        :class="{ disabled: menu.items.length === 0 }"
        @click="jump"
      >
        立即下单
      </van-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useMenuStore } from '@/store/modules/menu'

const menu = useMenuStore()
const router = useRouter()

const jump = () => {
  if (menu.items.length > 0) {
    router.push({ path: '/OrderDetail' })
  } else {
    console.log('菜单为空，无法跳转') // 或者使用其他方式提示用户
  }
}
</script>

<style scoped>
.pay-footer {
  width: 100%;
  background-color: #f9f9f9;
  border-top: 1px solid #e0e0e0;
  box-shadow: 0 -4px 8px rgba(0, 0, 0, 0.1);
}

.pay-footer-header {
  margin-bottom: 16px;
}

.header-text {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.pay-footer-icons {
  display: flex;
  justify-content: space-between; /* Adjusts spacing between items */
  margin-bottom: 16px;
}

.payment-option {
  display: flex;
  align-items: center;
  margin: 0 12px; /* Adds space between the payment options */
}

.payment-icon {
  height: 24px;
  width: 24px;
  margin-right: 8px;
}

.payment-label {
  font-size: 14px;
  color: #666;
}

.divider {
  border: 0;
  border-bottom: 1px solid #d0d0d5;
  margin-bottom: 16px;
}

.pay-footer-buttons {
  display: flex;
  justify-content: space-between;
}

.clear-button {
  flex: 1;
  background-color: #fff;
  color: #333;
  border: 1px solid #ccc;
  margin-right: 8px;
}

.order-button {
  background-color: #ff9f00;
  color: #fff;
  border: none;
  width: 35%;
  margin-left: 60%;
}

.order-button.disabled {
  background-color: #d0d0d5;
  color: #666;
  cursor: not-allowed;
}
</style>
