<template>
  <div class="simpleAddressCard">
    <div class="title">收货地址</div>
    <div class="address">{{ props.order_address }}</div>
  </div>
</template>

<script setup>
const props = defineProps({
    order_address: String
})
</script>

<style scoped>
.simpleAddressCard {
  background-color: #d4ff79;
  border-radius: 10px;
  padding: 5% 0% 5% 8%;
  margin: 4% 2% 0% 2%;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
}
.title {
  font-size: large;
  font-weight: bold;
  margin-bottom: 4%;
}
.address {
  font-size: small;
  font-weight: bold;
  margin-left: 1vw;
}
</style>
