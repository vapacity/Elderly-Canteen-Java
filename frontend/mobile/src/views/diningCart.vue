<template>
  <div class="container">
    <Nav class="fixed-nav" nav_text="我的购物车" />
    <div class="content">
      <OrderList />
      <RemarkItem />
    </div>
    <div class="footer">
      <van-button type="primary" class="order-button" @click="jumpToPay"> 立即下单 </van-button>
    </div>
  </div>
  <van-popup
    v-model:show="show"
    closeable
    :close-on-click-overlay="false"
    :style="{ height: '40%', width: '90%', padding: '20px', boxSizing: 'border-box' }"
    round
  >
    <div class="payment-container">
      <h3 class="balance">
        账户余额: <span>{{ balance }} 元</span>
      </h3>
      <van-button type="primary" class="pay-button" @click="handlePay">支付</van-button>
    </div>
  </van-popup>
</template>

<script setup>
import 'vant/es/toast/style'
import { ensureCart, getBanlance, getCartItem } from '@/api/api'
import { useUserStore } from '@/store/modules/user'
import { useRemarkstore } from '@/store/modules/remark'
import { useMenuStore } from '@/store/modules/menu'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { computed } from 'vue'
const show = ref(false)
const user = useUserStore()
const remark = useRemarkstore()
const menu = useMenuStore()
const cartItems = ref([])
const jumpToPay = async () => {
  show.value = true
  balance.value = await getBanlance()
}
const totalPrice = computed(() => {
  return cartItems.value.reduce((total, item) => {
    const price = item.discountPrice > 0 ? item.discountPrice : item.dishPrice
    return total + price * item.quantity
  }, 0)
})
const router = useRouter()
onMounted(async () => {
  const cartId = localStorage.getItem('cartId')
  cartItems.value = await getCartItem(cartId)
})
const handlePay = async () => {
  if (balance.value < totalPrice.value) {
    showToast('余额不足！')
    return
  }
  const cartId = localStorage.getItem('cartId')
  remark.setMark()
  const userRemark = remark.remark
  console.log(userRemark)
  const deliver_or_dining = user.deliver_or_dining
  const result = await ensureCart(cartId, deliver_or_dining, false, '堂食', userRemark)
  if (!result.success) {
    window.alert(result.msg || '下单未完成，请重试')
    return
  }
  localStorage.removeItem('cartId')
  menu.clear()
  remark.clear()
  user.clear()
  router.push({ path: '/MyOrders' })
}
const balance = ref(0)
</script>

<style scoped>
.container {
  padding-top: 30px;
}
.content {
  flex: 1;
}
/* 固定在顶部的导航 */
.fixed-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.order-button {
  background-color: #ff9f00;
  color: #fff;
  border: none;
  width: 35%;
  margin-left: 60%;
  margin-bottom: 2%;
}
.payment-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  justify-content: space-between;
  box-sizing: border-box;
}

.balance {
  margin-top: 80px;
  font-size: 18px;
  margin-bottom: 16px;
  margin-left: 50px;
  font-weight: bold;
}

.pay-button {
  width: 100%;
  padding: 12px;
}
</style>
