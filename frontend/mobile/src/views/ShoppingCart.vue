<template>
  <div class="container">
    <Nav class="fixed-nav" nav_text="我的购物车" />
    <div class="content">
      <AddressCard />
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
      <van-button type="primary" class="pay-button" :loading="paying" @click="handlePay">支付</van-button>
    </div>
  </van-popup>
</template>

<script setup>
import { ensureCart, getBanlance } from '@/api/api'
import { useUserStore } from '@/store/modules/user'
import { useRemarkstore } from '@/store/modules/remark'
import { useMenuStore } from '@/store/modules/menu'
import { ref } from 'vue'
import { showToast } from 'vant'
import { useRouter } from 'vue-router'
const show = ref(false)
const paying = ref(false)
const user = useUserStore()
const remark = useRemarkstore()
const menu = useMenuStore()
const jumpToPay = async () => {
  show.value = true
  balance.value = await getBanlance()
}
const router = useRouter()
const handlePay = async () => {
  const cartId = localStorage.getItem('cartId')
  const newAddr = user.addr
  remark.setMark()
  const userRemark = remark.remark
  console.log(userRemark)
  const setDefault = user.setDefault
  const deliver_or_dining = user.deliver_or_dining
  if (paying.value) return
  paying.value = true
  try {
    const result = await ensureCart(cartId, deliver_or_dining, setDefault, newAddr, userRemark)
    if (!result.success) {
      showToast(result.msg || '支付未完成，请重试')
      return
    }
    showToast('下单成功')
  } catch {
    showToast('未收到提交结果，请先查看我的订单')
    return
  } finally {
    paying.value = false
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
