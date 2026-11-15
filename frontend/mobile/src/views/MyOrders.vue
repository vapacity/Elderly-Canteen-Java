<template>
  <div class="container">
    <!-- 顶部导航栏 -->
    <div class="head">
      <Nav nav_text="我的订单" />
      <van-tabs v-model:active="active" swipeable title-active-color="rgb(249, 184, 62)" @change="onRefresh">
        <van-tab title="全部" name="all"></van-tab>
        <van-tab title="外送" name="deliver"></van-tab>
        <van-tab title="堂食" name="dining"></van-tab>
      </van-tabs>
    </div>

    <!-- 订单列表 -->
    <div class="orders">

      <div class="scroll">
        <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
          <van-list v-model:loading="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
            <OrderInList v-for="item in filteredList" :key="item.orderId" :order_detail="item">
            </OrderInList>
          </van-list>
        </van-pull-refresh>
      </div>

    </div>

    <!-- 底部导航栏 -->
    <BottomTabbar nowView="orders" />
  </div>
</template>

<script setup>
import { getOrders } from '../api/api'

const active = ref('all') //默认展示所有订单，可更改为堂食或外送
const listReady = ref(false) // 添加一个布尔变量以确保数据准备好
const orderList = ref([])
const fetchOrders = async () => {
  try {
    const response = await getOrders()
    orderList.value = response
    //在筛选前，先对订单时间降序排序，新的在前
    orderList.value.sort((a, b) => {
      // 如果 updatedTime 是时间字符串（例如 '2023-08-15T10:00:00Z'），可以直接比较它们
      return new Date(b.updatedTime) - new Date(a.updatedTime)
    })
    listReady.value = true // 数据准备好
    onLoad()
  } catch (error) {
    console.error('Error fetching orderList:', error)
  } finally {
    refreshing.value = false // 确保在数据获取后重置 refreshing
  }
}
onMounted(fetchOrders) //页面渲染前获取一次订单数据

const list = ref([]) //要传给OrderInList卡片的数据
const loading = ref(false)
const finished = ref(false)
const refreshing = ref(false)
const pageSize = 10 // 每次加载的数据量
let currentIndex = 0 // 当前已加载的索引
const filteredList = computed(() => {
  if (active.value === 'all') {
    return list.value
  } else if (active.value === 'deliver') {
    return list.value.filter((item) => item.deliverOrDining === true) // 外送
  } else if (active.value === 'dining') {
    return list.value.filter((item) => item.deliverOrDining === false) // 堂食
  }
  return list.value
})
const onLoad = () => {
  // 确保数据准备好之后再加载。否则在页面刚打开时，onLoad会先于fetch以空数据加载
  if (!listReady.value) return

  console.log('use', orderList.value)
  loading.value = true

  try {
    // 获取当前索引到 pageSize 数据
    const nextData = orderList.value.slice(currentIndex, currentIndex + pageSize)
    list.value.push(...nextData)
    currentIndex += pageSize

    // 检查是否加载完所有数据
    if (currentIndex >= orderList.value.length) {
      finished.value = true
    }
  } catch (error) {
    console.error('Error fetching orders:', error)
  } finally {
    loading.value = false
  }
}
const onRefresh = () => {
  // 清空列表数据
  list.value = []
  finished.value = false
  refreshing.value = true
  currentIndex = 0 // 重置已加载的索引
  listReady.value = false //不置false同样会使onLoad先加载一遍。
  fetchOrders()
}

const router = useRouter()
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.head {
  position: fixed;
  top: 0;
  z-index: 1000;
  background-color: white;
  width: 100%;
}

.van-tabs {
  height: 5vh;
}

.scroll {
  height: 81.6vh;
  overflow: auto;
  width: 90vw;
}

.orders {
  width: 100%;
  display: flex;
  flex-grow: 1;
  padding: 20px;
  background-color: rgba(244, 244, 244);
  margin-top: calc(5vh + 5vh);
  /*使用 position: fixed 的head被从文档流中移除了，导致 orders 容器向上移动 */
}
</style>
