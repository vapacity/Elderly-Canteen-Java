<template>
  <div class="orderlist">
    <span class="title"> 我的点餐列表 </span>
    <!-- 显示“购物车为空”消息 -->
    <div v-if="cartItems.length === 0" class="empty-cart-message">购物车为空</div>
    <!-- 否则，显示点餐列表 -->
    <div v-else>
      <!-- 显示点餐列表 -->
      <div v-if="cartItems.length > 1">
        <!-- 仅显示一盘菜 -->
        <DishCard v-bind="cartItems[0]" v-if="!showMore" />

        <!-- 显示剩余菜品 -->
        <div v-show="showMore" class="more-items">
          <DishCard v-for="(item, index) in cartItems" :key="index" v-bind="item" />
        </div>

        <!-- 显示更多按钮 -->
        <div class="show-more" @click="toggleShowMore">
          <span v-if="!showMore">显示更多</span>
          <span v-if="showMore">收起</span>
          <van-icon name="arrow-down" v-if="!showMore" />
          <van-icon name="arrow-up" v-if="showMore" />
        </div>
      </div>
      <!-- 只有一盘菜时显示 -->
      <div v-else>
        <DishCard v-bind="cartItems[0]" />
      </div>
      <hr class="hr-solid" />
      <span class="text"> 合计￥{{ totalPrice.toFixed(2) }} </span>
    </div>
  </div>
</template>

<script setup>
import DishCard from './DishCard.vue'
import { getCartItem } from '@/api/api'
import { onMounted } from 'vue'
import { computed } from 'vue'
import { ref } from 'vue'
const cartItems = ref([])
const totalPrice = computed(() => {
  return cartItems.value.reduce((total, item) => {
    const price = item.discountPrice > 0 ? item.discountPrice : item.dishPrice
    return total + price * item.quantity
  }, 0)
})
onMounted(async () => {
  const cartId = localStorage.getItem('cartId')
  cartItems.value = await getCartItem(cartId)
})
const showMore = ref(false)

const toggleShowMore = () => {
  showMore.value = !showMore.value
}
</script>
<style scoped>
.orderlist {
  border-radius: 15px;
  margin: 4%;
  padding: 20px; /* 增加内边距 */
  background: #ffffff; /* 背景色 */
  border: 2px solid #e0e0e0; /* 添加边框 */
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15); /* 改进阴影效果 */
  transition:
    box-shadow 0.3s ease-in-out,
    border-color 0.3s ease-in-out; /* 添加过渡效果 */
}

.title {
  font-size: 22px;
  font-weight: bold;
  color: #333; /* 更深的颜色 */
  margin-bottom: 10px; /* 增加底部间距 */
}

.hr-solid {
  margin: 20px 0; /* 增加上下间距 */
  border: 0;
  border-top: 1px solid #d0d0d5;
}

.text {
  font-size: 22px;
  font-weight: bold;
  color: #333; /* 更深的颜色 */
}

.empty-cart-message {
  text-align: center;
  font-size: 18px;
  color: #888; /* 更柔和的颜色 */
  margin-top: 20px; /* 增加顶部间距 */
}

.show-more {
  cursor: pointer;
  text-align: center;
  color: #ff9f00; /* 更亮的颜色 */
  font-weight: bold;
  font-size: 16px;
  margin-top: 15px; /* 增加顶部间距 */
  padding: 10px; /* 增加内边距 */
  border-radius: 5px; /* 添加圆角 */
  background: #fff4e6; /* 背景渐变 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 添加阴影 */
  transition:
    background 0.3s ease,
    color 0.3s ease; /* 添加过渡效果 */
}

.show-more:hover {
  background: #ffe0b2; /* 悬停时背景颜色变化 */
  color: #e68a00; /* 悬停时文字颜色变化 */
}

.more-items {
  margin-top: 15px; /* 增加顶部间距 */
}
</style>
