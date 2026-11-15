<template>
  <div class="dishcard">
    <div class="card-left">
      <img :src="props.imageUrl" />
    </div>
    <div class="card-right">
      <div class="card-right-top">
        <span class="text">{{ props.dishName }}</span>
      </div>
      <div class="card-right-bottom">
        <div class="card-right-bottom-left">
          <div class="item-price-info">
            <span v-if="props.discountPrice > 0" class="original-price">
              ￥{{ props.dishPrice }}
            </span>
            <span class="discount-price">
              ￥{{ props.discountPrice > 0 ? props.discountPrice : props.dishPrice }}
            </span>
          </div>
        </div>
        <div class="card-right-bottom-right">
          <span class="num">&nbsp;×{{ props.quantity }}&nbsp;</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'
const props = defineProps({
  imageUrl: String,
  dishName: String,
  dishPrice: Number,
  discountPrice: Number,
  quantity: Number
})
</script>

<style scoped>
.dishcard {
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1); /* 保持原有阴影 */
  margin-top: 4%;
  border-radius: 15px;
  padding: 4%;
  display: flex;
  border: 2px solid #e0e0e0; /* 增加边框宽度和颜色 */
}

img {
  height: 10vh;
  width: 25vw;
}
.card-left {
  width: 40%;
}
.card-right {
  width: 60%;
}
.card-right-top {
  height: 50%;
  display: flex;
  align-items: center;
}
.card-right-top .text {
  font-size: 20px;
  font-weight: bold;
}
.card-right-bottom {
  height: 50%;
  display: flex;
  align-items: center;
}
.card-right-bottom-left {
  width: 70%;
}
.card-right-bottom-right {
  width: 30%;
  display: flex;
  justify-items: center;
  align-items: center;
}
.num {
  font-size: small;
}
.card-right-bottom .text {
  font-size: medium;
  font-weight: bold;
}
.card-right-bottom .num {
  font-size: small;
}
.icon-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
}
.item-price-info {
  display: flex;
  align-items: center;
  margin-top: 5px;
}
.original-price {
  font-size: 14px;
  color: #999;
  text-decoration: line-through; /* 显示划线 */
  margin-right: 10px;
}
.discount-price {
  font-size: 16px;
  color: #ff5722;
}
</style>
