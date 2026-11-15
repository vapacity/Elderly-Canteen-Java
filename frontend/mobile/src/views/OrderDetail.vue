<template>
  <div class="container">
    <div class="head">
      <Nav nav_text="订单详情" ref="nav" />
    </div>

    <!-- 志愿者信息与订单内容 -->
    <div class="order_content">
      <!-- 配送地址 -->
      <div v-if="orderDetail.deliverOrDining === true">
        <SimpleAddressCard :order_address="orderDetail.cusAddress" />
      </div>

      <!-- 志愿者信息 -->
      <div>
        <VolunteerCard :orderId="orderDetail.orderId" :orderStatus="orderDetail.status"
          :deliverOrDining="orderDetail.deliverOrDining" :deliverStatus="orderDetail.deliverStatus"
          ref="volunteerCard" />
      </div>

      <!-- 订单信息 -->
      <div>
        <OrderCard :order_detail="orderDetail" />
      </div>

      <!-- 备注信息 -->
      <div class="remark">
        <div class="header">
          <span style="font-weight: bold">用户备注</span>
        </div>
        <div class="remarkContent">
          {{ orderDetail.remark }}
        </div>
      </div>
    </div>

    <!-- 按钮与投诉电话 -->
    <div class="footer">
      <div class="info">
        <div class="hotline">
          <span class="title">投诉电话</span>
          <span class="phone">1234567</span>
        </div>
      </div>
      <hr class="hr-solid" />
      <div class="buttonpt">
        <van-button :style="buttonStyle" class="button" @click="buttonEvent" :disabled="canClick === false">
          {{ buttonText }}</van-button>

        <!-- ref="commentDialog"：获取子组件的引用。在 setup 中，可以使用commentDialog.value.showDialog();来访问子组件 -->
        <CommentDialog @exit="handleCommentExit" ref="commentDialog" :deliverOrDining="orderDetail.deliverOrDining"
          :orderId="orderDetail.orderId" />
      </div>
    </div>
  </div>
</template>

<script setup>
// 由于mock的订单信息快速过期，无法通过订单号查询，故暂时无法使用实际的刷新逻辑
// 按钮的逻辑同样无法彻底实现

import { onMounted } from 'vue'
import { postAccpetOrder, getAcceptedOrder, getIdentityInOrder } from '../api/api'

const isAccepted = ref(false);//false表示当前没接单


const isDeliver = ref();
const isOwner = ref();
const isIdentityReady = ref(false);
const getIdentity = async () => {
  if (identity == 'volunteer') {
    const response2 = await getAcceptedOrder();//当前订单
    console.log("获得当前订单", response2.value);
    isAccepted.value = (Object.keys(response2.value).length !== 0);
    console.log("isAccepted", isAccepted.value);
  }
  const response = await getIdentityInOrder(orderDetail.value.orderId);
  switch (response.success) {
    case true:
      isDeliver.value = response.response.isDeliver;
      isOwner.value = response.response.isOwner;
      console.log('查询当前身份成功！', response.msg)
      isIdentityReady.value = true;
      showButton();
      break;
    case false:
      showFailToast({
        message: '查询当前身份失败！',
        onClose: () => {
          console.log('foast消失')
          onRefresh()
        }
      })
  }



}


const identity = localStorage.getItem('identity')
const buttonText = ref('加载中')
const canClick = ref()

// 确定buttonText和canClick
const showButton = () => {
  console.log('显示按钮内容 identity:', identity, ' deliverOrDining:', orderDetail.value.deliverOrDining,
    ' status', orderDetail.value.status, ' deliverStatus:', orderDetail.value.deliverStatus

  )
  console.log(' isOwner:', isOwner.value, ' isDeliver:', isDeliver.value)


  if (isAccepted.value == true && !isOwner.value && !isDeliver.value) {
    buttonText.value = '确认接单'
    canClick.value = false
    showFailToast({
      message: '请完成已接订单',
      onClose: () => {
        console.log('foast消失')
      }
    })
  }
  else if (orderDetail.value.deliverStatus != '待接单' && !isOwner.value && !isDeliver.value) {
    buttonText.value = '确认接单'
    canClick.value = false
    showFailToast({
      message: '该订单已被接收！',
      onClose: () => {
        console.log('foast消失')
      }
    })
  }
  else if (isOwner.value) {
    // 当前用户是下单者--堂食-待确认-可点的确认取餐
    // 当前用户是下单者--外送-待确认且已送达-可点的确认取餐
    if (
      (orderDetail.value.deliverOrDining == false &&
        orderDetail.value.status == '待确认') ||
      (orderDetail.value.deliverOrDining == true &&
        (orderDetail.value.deliverStatus == '已送达' && orderDetail.value.status == '待确认'))
    ) {
      buttonText.value = '确认取餐'
      canClick.value = true
    }
    //当前用户是下单者-外送-待确认且已接单-
    //是配送-可点的确认送达
    //不是-不可点的确认取餐
    else if (
      orderDetail.value.deliverOrDining == true &&
      orderDetail.value.status == '待确认' && orderDetail.value.deliverStatus == '已接单') {
      if (isDeliver.value == true) {
        buttonText.value = '确认送达'
        canClick.value = true
      }
      else {
        buttonText.value = '确认取餐'
        canClick.value = false
      }

    }
    //当前用户是下单者-外送-待确认且待接单
    //是志愿者且未接单（可点的确认接单）
    //是用户（不可点的确认取餐）
    else if (orderDetail.value.deliverOrDining == true && orderDetail.value.status == '待确认' &&
      orderDetail.value.deliverStatus == '待接单') {
      if (identity == 'volunteer' && isAccepted.value == false) {
        buttonText.value = '确认接单'
        canClick.value = true
      }
      else {
        buttonText.value = '确认取餐'
        canClick.value = false
      }
    }
    // 当前用户是下单者-堂食、外送-已确认-可点的去评价
    else if (orderDetail.value.status == '已确认') {
      buttonText.value = '去评价'
      canClick.value = true
    }

    // 当前用户是下单者-堂食、外送-已评价-可点的查看评价
    else if (orderDetail.value.status == '已评价') {
      buttonText.value = '查看评价'
      canClick.value = true
    }

  }
  else {
    // 志愿者-外送-未接单-可点的确认接单
    if (
      orderDetail.value.deliverOrDining == true &&
      orderDetail.value.deliverStatus == '待接单'
    ) {
      buttonText.value = '确认接单'
      canClick.value = true
    }
    // 志愿者-外送-已接单-可点的确认送达
    else if (
      orderDetail.value.deliverOrDining == true &&
      orderDetail.value.deliverStatus == '已接单'
    ) {
      buttonText.value = '确认送达'
      canClick.value = true
    }
    // 志愿者-外送-已送达，待评价/待确认-不可点的查看评价
    else if (
      orderDetail.value.deliverOrDining == true &&
      orderDetail.value.deliverStatus == '已送达' &&
      orderDetail.value.status == '待评价' || orderDetail.value.status == '待确认'
    ) {
      buttonText.value = '查看评价'
      canClick.value = false
    }
    // 志愿者-外送-已评价-可点的查看评价
    else if (
      orderDetail.value.deliverOrDining == true &&
      orderDetail.value.deliverStatus == '已送达' &&
      orderDetail.value.status == '已评价'
    ) {
      buttonText.value = '查看评价'
      canClick.value = true
    }

  }


}
// onMounted(showButton)

// 确定按钮颜色
import { computed } from 'vue'
const buttonStyle = computed(() => {
  if (
    buttonText.value == '确认取餐' ||
    buttonText.value == '确认接单' ||
    buttonText.value == '确认送达'
  ) {
    return {
      backgroundColor: 'orange'
    }
  } else {
    return {
      backgroundColor: '#d4ff79'
    }
  }
})

import { postConfirmOrder, getOrderMsg, postConfirmDelivered } from '../api/api'
// 确认接单
// #region
const accpetOrder = async () => {
  console.log('点击确认接单')
  const status = await postAccpetOrder(orderDetail.value.orderId)
  console.log('status:', status)
  switch (status.success) {
    case true:
      showSuccessToast({
        message: '接单成功！',
        onClose: () => {
          console.log('确认接单foast消失')
          onRefresh()
        }
      })
      break
    case false:
      showFailToast({
        message: '接单失败，请重试',
        onClose: () => {
          console.log('确认接单foast消失')
          onRefresh()
        }
      })
      break
  }
}
// #endregion

// 评价
// #region
const commentDialog = ref(null)

// 处理评价弹窗退出事件
const handleCommentExit = () => {
  console.log('检测到弹窗关闭')
  onRefresh()
}

// #endregion




const route = useRoute()
// const orderId = computed(() => {
//   return route.query.detail ? JSON.parse(route.query.detail) : {}
// })

const orderDetail = ref({
  orderId: '',
  status: '',
  deliverOrDining: '',
  deliverStatus: ''
});

//从父组件获取订单详情
const volunteerCard = ref(null);
onMounted(() => {
  if (route.query.detail) {
    orderDetail.value = JSON.parse(route.query.detail);
    console.log('父子组件传递orderDetail', orderDetail.value);
  }
});

const nav = ref(null);
//实际的刷新逻辑
const onRefresh = async () => {
  console.log("刷新页面");
  try {
    orderDetail.value = await getOrderMsg(orderDetail.value.orderId);
  } catch (error) {
    showFailToast({
      message: `订单信息异常！\n${error.response.status}\n${error.response.data.msg}`,
      onClose: () => {
        console.log('foast消失')
        if (nav.value) {
          nav.value.onClickLeft();
        }
      }
    })
  }
  try{
    await getIdentity();
  }catch(error){
    showFailToast({
      message: `获取用户身份失败！`,
      onClose: () => {
        console.log('foast消失')
        if (nav.value) {
          nav.value.onClickLeft();
        }
      }
    })
  }
  
  try {
    await volunteerCard.value.fetchVolunteerMsg();
  } catch (error) {
    showFailToast({
      message: `配送信息异常！`,
      onClose: () => {
        console.log('foast消失')
        if (nav.value) {
          nav.value.onClickLeft();
        }
      }
    })
  }








};
onMounted(onRefresh);

// 确认取餐
// #region
const confirmOrder = async () => {
  const status = await postConfirmOrder(orderDetail.value.orderId)
  console.log('status:', status)
  switch (status) {
    case 200:
      showSuccessToast({
        message: '确认取餐成功',
        onClose: () => {
          console.log('确认取餐foast消失')
          onRefresh()
        }
      })
      break
    case 400:
      showFailToast({
        message: '确认失败，请重试',
        onClose: () => {
          console.log('确认取餐foast消失')
          onRefresh()
        }
      })
      break
  }
}
// #endregion

import { showLoadingToast, showSuccessToast, showFailToast } from 'vant'
// 确认送达
const confirmDelivered = async () => {
  const res = await postConfirmDelivered(orderDetail.value.orderId)
  console.log('确认送达 success:', res)
  switch (res) {
    case true:
      showSuccessToast({
        message: '确认成功！',
        onClose: () => {
          console.log('确认送达foast消失')
          onRefresh()
        }
      })
      break
    case false:
      showFailToast({
        message: '确认失败，请重试',
        onClose: () => {
          console.log('确认送达foast消失')
          onRefresh()
        }
      })
      break
  }
}

const buttonEvent = () => {
  if (buttonText.value == '确认取餐') {
    confirmOrder()
  } else if (buttonText.value == '去评价') {
    console.log('点击‘去评价’')
    commentDialog.value.writeComment()
  } else if (buttonText.value == '查看评价') {
    commentDialog.value.viewComment()
  } else if (buttonText.value == '确认接单') {
    /*若进入订单详情后，订单已被其他快递员接受，再点确认接单，失败后只要不退出详情页，能看到订单的后续状态*/
    accpetOrder()
  } else if (buttonText.value == '确认送达') {
    confirmDelivered()
  }
  else {
    onRefresh();
  }
}
</script>

<style scoped>
.head {
  position: fixed;
  top: 0;
  z-index: 1000;
  background-color: white;
  width: 100%;
}

.container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.order_content {
  flex: 1;
  /* 占据剩余空间 */
  overflow-y: auto;
  /* 允许垂直滚动 */

  margin-top: 5vh;
}

.remark {
  padding-top: 5%;
  padding-left: 6%;
  font-size: small;
  width: 100vw;
}

.remark .remarkContent {
  width: 88vw;
  word-wrap: break-word;
}

.footer {
  position: fixed;
  bottom: 0;
  z-index: 1000;
  background-color: white;
  width: 100%;
}

.footer .info {
  padding-top: 2%;
  padding-left: 6%;
  font-size: small;
}

.footer .info .header {
  margin-bottom: 2%;
}

.footer .info .mid {
  margin-bottom: 10%;
}

.hr-solid {
  border: 0;
  border-top: 1px solid #d0d0d5;
  margin-bottom: 10px;
  margin-top: 8px;
}

.buttonpt {
  padding-left: 48%;
  margin-bottom: 3%;
}

.button {
  width: 95%;
  height: 6.5vh;
  color: black;
  border: none;
  font-size: 20px;
  letter-spacing: 3px;
  border-radius: 12px;
  cursor: pointer;
}

.hotline .title {
  /*两个类之间的空格必不可省*/
  color: rgb(219, 95, 95);
  padding-right: 19px;
}

.hotline .phone {
  color: rgb(144, 149, 153);
  /* 设置电话号码的颜色为黑色 */
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 1px;
}
</style>
