<script setup>

import { onMounted } from 'vue';
import {
  getAcceptableOrder, getAcceptedOrder, getFinishedOrder
  , postAccpetOrder, getPorTrait
} from '../api/api';


//标签页-待送订单
//#region
const listReady = ref(false); // 添加一个布尔变量以确保数据准备好
const orderList = ref([]);
const acceptedOrder = ref({});
const isAccepted = ref();//false表示当前没接单
// const fetchOrders = async () => {

//   try {

//     console.log('fetchOrders尝试执行')
//     const response2 = await getAcceptedOrder();//当前订单
//     acceptedOrder.value = response2;
//     console.log("获得当前订单", acceptedOrder.value);
//     isAccepted.value = (Object.keys(acceptedOrder.value).length !== 0);
//     console.log("isAccepted", isAccepted.value);

//     console.log('fetchOrders尝试执行')
//     const response = await getAcceptableOrder();//待送订单
//     console.log(response);
//     orderList.value = response;
//     console.log('获得待送订单：',orderList.value)


//     //订单按新到旧排序
//     orderList.value.sort((a, b) => {
//       // 如果 updatedTime 是时间字符串（例如 '2023-08-15T10:00:00Z'），可以直接比较它们
//       return new Date(b.updatedTime) - new Date(a.updatedTime);
//     });

//     listReady.value = true; // 数据准备好
//     onLoad();
//   } catch (error) {
//     console.error('Error fetching orderList:', error);
//     console.error()
//   } finally {
//     refreshing.value = false; // 确保在数据获取后重置 refreshing
//   }
// };


const fetchOrders = async () => {
  console.log('fetchOrders尝试执行')
  const response2 = await getAcceptedOrder();//当前订单
  acceptedOrder.value = response2.value;
  console.log("获得当前订单", acceptedOrder.value);
  isAccepted.value = (Object.keys(response2.value).length !== 0);
  console.log("isAccepted", isAccepted.value);


  const response = await getAcceptableOrder();//待送订单
  switch (response.success) {
    case true:
      console.log(response);
      orderList.value = response.response;
      console.log('获得待送订单：', orderList.value)
      break;
    case false:
      console.log(response.msg);
  }



  //订单按新到旧排序
  orderList.value.sort((a, b) => {
    // 如果 updatedTime 是时间字符串（例如 '2023-08-15T10:00:00Z'），可以直接比较它们
    return new Date(b.updatedTime) - new Date(a.updatedTime);
  });

  listReady.value = true; // 数据准备好
  onLoad();
  refreshing.value = false; // 确保在数据获取后重置 refreshing
};
onMounted(fetchOrders);

const list = ref([]);//要传给OrderInList的数据
const loading = ref(false);
const finished = ref(false);
const refreshing = ref(false);
const pageSize = 10; // 每次加载的数据量
let currentIndex = 0; // 当前已加载的索引

const onLoad = () => {
  // 确保数据准备好之后再加载。否则在页面刚打开时，onLoad会先于fetch以空数据加载
  console.log('判断数据是否获取成功')
  if (!listReady.value) return;

  console.log("orderList获取成功，use", orderList.value);
  loading.value = true;

  try {
    // 获取当前索引到 pageSize 数据
    const nextData = orderList.value.slice(currentIndex, currentIndex + pageSize);
    list.value.push(...nextData);
    currentIndex += pageSize;


    // 检查是否加载完所有数据
    if (currentIndex >= orderList.value.length) {
      finished.value = true;
    }
  } catch (error) {
    console.error('Error fetching orders:', error);
  } finally {
    loading.value = false;
  }
}
const onRefresh = () => {
  console.log("刷新页面");
  // 清空列表数据
  list.value = [];
  finished.value = false;
  refreshing.value = true;//用来控制下拉刷新
  currentIndex = 0; // 重置已加载的索引
  listReady.value = false;//不置false同样会使onLoad先加载一遍。
  fetchOrders();
};
//#endregion



//标签页-已送订单
// #region
const refreshingFinished = ref(false);
const listReady2 = ref(false); // 添加一个布尔变量以确保数据准备好
const orderList2 = ref([]);
const fetchFinishedOrders = async () => {
  try {
    const response = await getFinishedOrder();
    orderList2.value = response;
    listReady2.value = true; // 数据准备好
    onLoadFinished();
  } catch (error) {
    console.error('Error fetching orderList:', error);
  } finally {
    refreshingFinished.value = false; // 确保在数据获取后重置 refreshing
  }
};

const list2 = ref([]);//要传给OrderInList卡片的数据
const loading2 = ref(false);
const finished2 = ref(false);
const pageSize2 = 10; // 每次加载的数据量
let currentIndex2 = 0; // 当前已加载的索引
const onLoadFinished = () => {
  // 确保数据准备好之后再加载。否则在页面刚打开时，onLoad会先于fetch以空数据加载
  if (!listReady2.value) return;

  console.log("use", orderList2.value);
  loading2.value = true;

  try {
    // 获取当前索引到 pageSize 数据
    const nextData = orderList2.value.slice(currentIndex2, currentIndex2 + pageSize2);
    list2.value.push(...nextData);
    currentIndex2 += pageSize2;

    // 检查是否加载完所有数据
    if (currentIndex >= orderList2.value.length) {
      finished2.value = true;
    }
  } catch (error) {
    console.error('Error fetching orders:', error);
  } finally {
    loading2.value = false;
  }
}
const onRefreshFinished = () => {
  console.log("刷新页面");
  // 清空列表数据
  list2.value = [];
  finished2.value = false;
  refreshingFinished.value = true;
  currentIndex2 = 0; // 重置已加载的索引
  listReady2.value = false;//不置false同样会使onLoad先加载一遍。
  fetchFinishedOrders();
};
//#endregion


//标签页跳转时执行刷新
const active = ref('待送订单');
watch(active, (newActive) => {
  newActive == "待送订单" ? onRefresh() : onRefreshFinished();
});


//接单逻辑（发送请求，弹窗，刷新）
// #region 
import { showLoadingToast, showSuccessToast, showFailToast } from 'vant';
const accpetOrder = async (accpeted_order) => {
  console.log("接单");
  const isSuccess = await postAccpetOrder(accpeted_order.orderId);
  console.log('postAccpetOrder返回:', isSuccess);
  switch (isSuccess.success) {
    case true:
      showSuccessToast({
        message: '接单成功！',
        onClose: () => {
          console.log('确认接单foast消失')
          onRefresh();
        }
      })
      break;
    case false:
      showFailToast({
        message: '接单失败，请重试',
        onClose: () => {
          console.log('确认接单foast消失')
          onRefresh();
        }
      })
      break;
  }

}
// #endregion





</script>


<template>
  <div class="container">
    <div class="head">
      <Nav nav_text="志愿接单" />
      <van-tabs v-model:active="active" title-active-color="rgb(249, 184, 62)">
        <van-tab title="待送订单" name="待送订单"></van-tab>
        <van-tab title="已送订单" name="已送订单"></van-tab>
      </van-tabs>
    </div>



    <div class="pageContent">
      <div class="acceptable" v-if="active === '待送订单'">

        <div class="current-order">
          <div class="current-title">当前订单</div>

          <OrderToAccept :order_detail="acceptedOrder" :isAccepted="isAccepted" />
        </div>


        <div class="orders">
          <div class="order-title">可接订单</div>
          <div class="scroll">
            <van-pull-refresh v-model="refreshing" @refresh="onRefresh" v-if="active === '待送订单'">
              <van-list v-model:loading="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
                <OrderToAccept v-for="item in list" :key="item.orderId" :order_detail="item" :isAccepted="isAccepted"
                  @clickAccept="accpetOrder"></OrderToAccept>

              </van-list>
            </van-pull-refresh>
          </div>

        </div>


      </div>
      <div v-else class="finishedOrders">

        <div class="finishedScroll">
          <van-pull-refresh v-model="refreshingFinished" @refresh="onRefreshFinished">
            <van-list v-model:loading="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
              <OrderInList v-for="item in list2" :key="item.orderId" :order_detail="item"></OrderInList>
            </van-list>
          </van-pull-refresh>
        </div>

      </div>
    </div>

  </div>


</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}


/*顶部导航栏*/
/*#region*/
.van-tab {
  height: 5vh;
}

.head {
  position: fixed;
  top: 0;
  z-index: 1000;
  background-color: white;
  width: 100%;
}

/*#endregion*/

.pageContent {
  margin-top: calc(5vh + 5vh);
  width: 100%;
  display: flex;
  flex-grow: 1;
}

/*标签页-待送订单 */
.acceptable {
  width: 100%;
  flex-grow: 1;
  background-color: rgba(244, 244, 244);
}

/*当前订单部分*/
/*#region*/
.current-order {
  width: 100%;
  height: 25vh;
  background-color: rgb(249, 184, 62);
  display: flex;
  flex-direction: column;

  justify-content: center;
  align-items: center;
}

.current-title {
  color: white;
  height: 5vh;
  align-items: center;
  width: 90vw;

  font-size: large;
  font-weight: bold;
  letter-spacing: 0.2vh;
}


/*#endregion*/

/*可接订单部分 */
/*#region*/
.order-title {
  font-size: large;
  font-weight: bold;
  margin-bottom: 10px;
}


.scroll {
  height: 51vh;
  overflow: auto;
  width: 90vw;
}

.orders {
  width: 100%;
  flex-grow: 1;
  padding: 20px;
}

/*#endregion */





/*标签页-已送订单 */
.finishedScroll {
  height: 85vh;
  overflow: auto;
  width: 90vw;

}

.finishedOrders {
  width: 100%;
  display: flex;
  flex-grow: 1;
  padding: 20px;
  background-color: rgba(244, 244, 244);
}
</style>
