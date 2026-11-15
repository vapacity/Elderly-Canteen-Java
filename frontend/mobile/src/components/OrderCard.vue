<template>
  <div class="orderCard">
    <div class="card-header">
      <div class="text">
        <span class="title">订单信息</span>
      </div>
      <div class="info">
        <van-row :gutter="[0, 10]">
          <van-col span="12">订单号</van-col>
          <van-col span="12" style="text-align: right">{{ props.order_detail.orderId }}</van-col>
          <van-col span="12">就餐方式</van-col>
          <van-col span="12" style="text-align: right">
            {{ props.order_detail.deliverOrDining ? '外送' : '堂食' }}
          </van-col>
          <van-col span="12">补贴消耗</van-col>
          <van-col span="12" style="text-align: right">{{ props.order_detail.subsidy }}</van-col>
        </van-row>
      </div>
    </div>
    <hr class="hr-solid" />

    <div class="card-mid">
      <div class="text">
        <span class="title">菜品价格明细</span>
      </div>
      <div class="info">
        <ul>
          <li v-for="(item, index) in order_detail.orderDishes" :key="index" class="item">
            <van-row>
              <van-col span="12">{{ item.dishName }}</van-col>
              <van-col span="6">x{{ item.quantity }}</van-col>
              <van-col span="6" style="text-align: right">￥{{ item.price }}</van-col>
            </van-row>
          </li>
        </ul>
      </div>
    </div>

    <hr class="hr-solid" />
    <div class="card-footer">
      <div class="left">
        <img src="../assets/price.png" />
        <span class="text"> 总计</span>
      </div>
      <div class="right">
        <span class="text"> ￥{{ order_detail.money - order_detail.subsidy }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  order_detail: Object
})
</script>

<style scoped>
.orderCard {
  margin: 1% 6%;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  border-radius: 15px;
  padding: 3% 6%;
}

.card-header .text {
  margin-bottom: 5%;
}

.card-header .text .title {
  font-weight: bold;
  font-size: 20px;
  letter-spacing: 3px;
}

.card-header .info {
  font-size: small;
  margin-left: 8px;
  margin-left: 8px;
}

.hr-solid {
  border: 0;
  border-top: 1px solid #d0d0d5;
  margin-top: 5px;
  margin-bottom: 10px;
}

.card-mid .text {
  margin-bottom: 2%;
}

.card-mid .text .title {
  font-weight: bold;
  font-size: small;
}

.card-mid .info {
  font-size: small;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-footer .left {
  display: flex;
  align-items: center;
}

.card-footer .left .text {
  font-weight: bold;
  font-size: medium;
  margin-left: 6px;
}

.card-footer .right .text {
  color: red;
  font-size: medium;
}

ul {
  list-style-type: none;
  /* 移除默认的列表样式 */
  padding: 0;
  margin: 0;
}

li {
  padding-bottom: 8px;
  padding-left: 6px;
}
</style>
