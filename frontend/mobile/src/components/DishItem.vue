<template>
  <div class="item">
    <div class="pic">
      <img :src="props.imageUrl" />
      <div v-if="badgeCount > 0" class="badge">{{ badgeCount }}</div>
    </div>
    <div class="right">
      <div class="text">
        <span class="name">{{ props.dishName }}</span>
        <span v-if="props.discountPrice !== 0" class="original-price">
          ￥{{ props.dishPrice.toFixed(2) }}
        </span>
        <span v-if="props.discountPrice !== 0" class="discount-price">
          ￥{{ props.discountPrice.toFixed(2) }}
        </span>
        <span v-else> ￥{{ props.dishPrice.toFixed(2) }} </span>
      </div>
      <div class="bottom">
        <div class="sale">
          <span>销量：{{ props.sales }}</span>
          <span>库存： {{ props.stock }}</span>
        </div>
        <button class="plus-button" @click="addItem">＋</button>
      </div>
    </div>
  </div>
</template>
<script setup>
import 'vant/es/toast/style'
import { useMenuStore } from '@/store/modules/menu'
import { computed } from 'vue'
import { updateCartItem } from '@/api/api'
import { showToast } from 'vant'
const menu = useMenuStore()
const badgeCount = computed(() => {
  return menu.getItemCount(props.dishName)
})
const props = defineProps({
  imageUrl: String,
  dishId: Number,
  dishName: String,
  dishPrice: Number,
  discountPrice: Number,
  category: String,
  sales: Number,
  stock: Number
})
const addItem = async () => {
  const item = {
    dishName: props.dishName,
    quantity: 1
  }
  const cartId = localStorage.getItem('cartId')
  console.log(menu.getItemCount(item.dishName) + 1)
  const res = await updateCartItem(cartId, props.dishId, menu.getItemCount(item.dishName) + 1)
  if (res.success === false) {
    showToast('菜品已经卖完了!')
  } else {
    menu.addItem(item)
  }
}
</script>
<style scoped>
.item {
  width: 90vw; /* 宽度设置为视口宽度的90% */
  border-radius: 4vw; /* 边框圆角使用视口宽度的2% */
  margin-top: 2vh;
  margin-left: 2vw;
  box-shadow: 0 4px 8px rgba(255, 154, 58, 0.5);
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
  display: flex;
  position: relative;
}

.pic {
  margin: 2vw;
  position: relative;
}

.pic img {
  border-radius: 2vw;
  height: 15vh; /* 图片高度使用视口高度的20% */
  width: 30vw; /* 图片宽度使用视口宽度的26% */
}

.badge {
  position: absolute;
  top: -2vw; /* 上边距使用视口宽度的2% */
  right: -2vw; /* 右边距使用视口宽度的2% */
  background-color: red;
  color: white;
  border-radius: 50%;
  width: 5vw; /* 直径使用视口宽度的4% */
  height: 5vw; /* 直径使用视口宽度的4% */
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5vw; /* 字体大小使用视口宽度的1.5% */
}

.right {
  width: 75vw; /* 右侧区域宽度使用视口宽度的75% */
}

.text {
  width: 100%;
  margin-left: 4vw; /* 左边距使用视口宽度的4% */
}

.name {
  margin-top: 1vh;
  font-size: medium; /* 字体大小使用视口宽度的2% */
  font-weight: bold;
  display: block;
}

.price span {
  font-size: 4vw; /* 字体大小使用视口宽度的2% */
  font-weight: bold;
}

.right .bottom {
  margin-left: 4vw; /* 左边距使用视口宽度的4% */
  display: flex;
  text-align: center;
}

.sale {
  /* 右边距使用视口宽度的5% */
  margin-right: 14vw;
}

.sale span {
  font-size: 2vw; /* 字体大小使用视口宽度的2% */
  display: block;
}

.plus-button {
  margin-top: 1vh;
  background-color: orange;
  border: none;
  color: black;
  border-radius: 50%;
  width: 6vw; /* 宽度使用视口宽度的6% */
  height: 6vw; /* 高度使用视口宽度的6% */
  font-size: 3vw; /* 字体大小使用视口宽度的3% */
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 1vw 2vw rgba(0, 0, 0, 0.2); /* 阴影使用视口宽度的单位 */
  transition:
    background-color 0.3s,
    box-shadow 0.3s;
}

.original-price {
  text-decoration: line-through;
  font-size: 4vw;
  color: gray;
}

.discount-price {
  color: red;
  font-size: 4vw;
  font-weight: bold;
}
</style>
