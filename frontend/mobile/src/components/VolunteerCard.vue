<template>
  <div class="volunteercard">
    <div class="card-left">
      <div class="volunteer" v-if="deliverOrDining === true">
        <div v-if="deliverStatus === '待接单'" class="toAcceptedText">待接单</div>
        <div v-else>
          <div>
            <span class="num">{{ volunteerId }}</span>
          </div>
          <div>
            <span class="name">志愿者: {{ volunteerName }}</span>
          </div>
        </div>
      </div>
      <div v-else>
        <span class="diningText">堂食</span>
      </div>
    </div>

    <div class="card-right">
      <img src="../assets/image/order_state.png" v-if="orderStatus === '待确认'" />
      <img src="../assets/order_state1.png" v-else />
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { getOrderDeliverMsg } from '../api/api'
const props = defineProps({
  orderStatus: String,
  deliverOrDining: Boolean,
  orderId: String,
  deliverStatus: String
})

// 加载志愿者姓名，id
//#region
const volunteerName = ref('')
const volunteerId = ref('')
const fetchVolunteerMsg = async () => {
  if (props.deliverOrDining == true && props.deliverStatus != '待接单') {
    try {
      const response = await getOrderDeliverMsg(props.orderId)
      if(response==null){
        throw new Error('deliverStatus与OrderDeliverMsg矛盾')
      }
      volunteerId.value = response.volunteerId
      volunteerName.value = response.volunteerName
      console.log(
        'success fetching VolunteerMsg。id:',
        volunteerId.value,
        '  name:',
        volunteerName.value
      )
    } catch (error) {
      console.error('Error fetching VolunteerMsg:', error.message?error.message:error.response.data.msg)
      throw new Error('配送员信息异常')
    }
  }
}

//#endregion
defineExpose({
  fetchVolunteerMsg,
});
</script>

<style scoped>
.volunteercard {
  padding: 3% 5% 5% 12%;
  display: flex;
  align-items: center;
  /* 垂直居中 */
}

.card-left {
  width: 60%;
}

.diningText {
  font-size: 50px;
  color: grey;
  font-weight: bold;
  letter-spacing: 12px;
}

.toAcceptedText {
  font-size: 40px;
  color: grey;
  font-weight: bold;
  letter-spacing: 6px;
}

.num {
  font-size: large;
  color: grey;
  font-weight: bold;
  letter-spacing: 2px;
}

.name {
  color: grey;
  font-weight: bold;
  font-size: medium;
  letter-spacing: 3px;
}

.card-right {
  padding-top: 3%;
  width: 40%;
}
</style>
