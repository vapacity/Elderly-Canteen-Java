<!-- components/WeeklyPromotion.vue -->
<template>
  <div class="weekDiscount">
    <h1 style="color: rgb(249, 194, 60); margin-left: 5vh; font-size: 36px">
      今日促销📣
    </h1>
    <div class="menuFrame">
      <div v-for="item in dishOnSale" :key="item.id" class="dishOnSaleItem">
        <div class="dishImg"><img :src="item.picture" /></div>
        <div class="dishName">{{ item.dishName }}</div>
        <div class="oriPrice">￥{{ item.price }}</div>
        <div class="disPrice">￥{{ item.disPrice }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    dishOnSale: Array,
  },
};
</script>

<style scoped>
.menuFrame {
  height: 250px;
  padding: 5px;
  display: flex;
  border-radius: 10px;
  margin: 20px 5vh;
  background-color: #fffcf7;
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
  overflow-x: auto;
}

.menuFrame::-webkit-scrollbar {
  height: 5px; /* 滚动条宽度 */
}
.menuFrame::-webkit-scrollbar-thumb {
  background-color: #cccccc; /* 滚动条滑块的颜色 */
  border-radius: 2.5px; /* 滚动条滑块的圆角 */
  opacity: 90%;
}
.menuFrame::-webkit-scrollbar-track {
  opacity: 100%;
}

.dishOnSaleItem {
  flex: 0 0 14.2857%;
  border-right: 0.5mm dashed #cacaca;
  box-sizing: border-box; /* 确保边框和内边距包含在元素的总宽度内 */
  display: flex;
  flex-direction: column; /* 垂直堆叠内容 */
  align-items: center;
  overflow: hidden;
}
.dishOnSaleItem:last-child {
  border-right: none;
}
.dishImg {
  height: 80px;
  width: 80px;
  margin: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden; /* 防止图片超出容器 */
  border-radius: 50%;
}
.dishImg img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.dishName {
  font-size: 14px;
  font-weight: bolder;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.oriPrice {
  color: #838383;
  text-decoration: line-through;
  font-size: smaller;
  margin-top: 15px;
  margin-bottom: 15px;
}
.disPrice {
  color: rgb(255, 106, 0);
  font-weight: bolder;
  font-size: 17px;
  border: none;
}
</style>
