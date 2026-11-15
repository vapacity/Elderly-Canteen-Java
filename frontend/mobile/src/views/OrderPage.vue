<template>
  <div class="floating-cart-bar">
    <div class="cart-icon">
      <van-badge :content="menu.totalNum" v-if="menu.totalNum !== 0">
        <van-icon name="cart-o" color="#ffb94a" size="4vh" @click="onClickIcon" />
      </van-badge>
      <template v-else>
        <van-icon name="cart-o" color="#ffb94a" size="4vh" @click="onClickIcon" />
      </template>
    </div>
    <div class="text" @click="onClickIcon">
      <span>查看购物车</span>
    </div>
  </div>

  <div class="header">
    <SearchLine v-model="searchTerm" class="nav" /><img src="../assets/slogan.jpg" class="slogan" />
  </div>
  <div class="container">
    <div class="button-list">
      <div class="menu">
        <button
          class="menu-button"
          @click="onClickMenuButton('促销')"
          :class="{ focus: buttons[0].focus }"
        >
          <img src="@/assets/image/tag.png" height="25px" width="25px" />
          <br />
          促销
        </button>

        <button
          class="menu-button"
          @click="onClickMenuButton('素类')"
          :class="{ focus: buttons[1].focus }"
        >
          <img src="@/assets/image/vegetable.png" height="25px" width="25px" />
          <br />
          素类
        </button>

        <button
          class="menu-button"
          @click="onClickMenuButton('荤类')"
          :class="{ focus: buttons[2].focus }"
        >
          <img src="@/assets/image/meat.png" height="25px" width="25px" />
          <br />
          荤类
        </button>

        <button
          class="menu-button"
          @click="onClickMenuButton('主食')"
          :class="{ focus: buttons[3].focus }"
        >
          <img src="@/assets/image/meat.png" height="25px" width="25px" />
          <br />
          主食
        </button>

        <button
          class="menu-button"
          @click="onClickMenuButton('汤类')"
          :class="{ focus: buttons[4].focus }"
        >
          <img src="@/assets/image/hot-soup.png" height="25px" width="25px" />
          <br />
          汤类
        </button>

        <button
          class="menu-button"
          @click="onClickMenuButton('饮料')"
          :class="{ focus: buttons[5].focus }"
        >
          <img src="@/assets/image/drink.png" height="25px" width="25px" />
          <br />
          饮料
        </button>
      </div>
    </div>
    <div class="item-list">
      <DishItem v-bind="item" v-for="(item, id) in filteredItems" :key="id" class="dish-item" />
      <div class="blank"></div>
    </div>
    <div></div>
  </div>
  <van-popup v-model:show="showBottom" position="bottom" class="popup-container">
    <div class="popup-header">
      <span class="header-title">购物车</span>
      <span class="clear-cart-button" @click="clearCartItem">清空</span>
    </div>
    <div class="popup-content">
      <div v-if="cartItems.length === 0" class="empty-cart">购物车为空</div>
      <div v-else>
        <div v-for="(item, index) in cartItems" :key="index" class="cart-item">
          <img :src="item.imageUrl" alt="" class="cart-item-img" />
          <div class="cart-item-info">
            <span class="item-name">{{ item.dishName }}</span>
            <div class="item-price-info">
              <span v-if="item.discountPrice !== 0" class="original-price">
                ￥{{ item.dishPrice.toFixed(2) }}
              </span>
              <span v-if="item.discountPrice !== 0" class="discount-price">
                ￥{{ item.discountPrice.toFixed(2) }}
              </span>
              <span v-else class="price"> ￥{{ item.dishPrice.toFixed(2) }} </span>
            </div>
            <div class="item-quantity-control">
              <van-button size="small" type="default" @click="decreaseQuantity(index)"
                >-</van-button
              >
              <span class="item-quantity">x {{ item.quantity }}</span>
              <van-button
                size="small"
                type="primary"
                @click="increaseQuantity(index)"
                class="plus-button"
                >+</van-button
              >
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="popup-footer">
      <span class="total-price">总价：￥{{ totalPrice.toFixed(2) }}</span>
      <van-button class="checkout-button" type="primary" @click="handleCheckout">去结算</van-button>
    </div>
  </van-popup>
</template>

<script setup>
import 'vant/es/toast/style'
import { useMenuStore } from '@/store/modules/menu'
import { ref } from 'vue'
import { computed } from 'vue'
import { showToast } from 'vant'
import router from '@/router'
import {
  createCart,
  getCartItem,
  getMenuToday,
  updateCartItem,
  clearCart,
  deleteCartItem
} from '@/api/api'
import { onMounted } from 'vue'
import { useUserStore } from '@/store/modules/user'
const menu = useMenuStore()
const cartItems = ref([])
const items = ref([])
const totalPrice = computed(() => {
  return cartItems.value.reduce((total, item) => {
    const price = item.discountPrice > 0 ? item.discountPrice : item.dishPrice
    return total + price * item.quantity
  }, 0)
})
const getItems = async () => {
  try {
    items.value = await getMenuToday()
  } catch (error) {
    console.error('获取当日菜品失败：', error)
    showToast('获取当日菜品失败，请稍后再试')
  }
}

const getCartId = async () => {
  try {
    const cartId = await createCart()
    if (!cartId) throw new Error('购物车未准备好')
    localStorage.setItem('cartId', cartId)
    menu.items = await getCartItem(cartId) || []
    return true
  } catch (error) {
    console.error('生成购物车失败：', error)
    showToast('生成购物车失败，请稍后再试')
    return false
  }
}
onMounted(async () => {
  if (await getCartId()) await getItems()
})
const showBottom = ref(false)
const buttons = ref([
  { name: '促销', focus: false, category: '6' },
  { name: '素类', focus: false, category: '1' },
  { name: '荤类', focus: false, category: '2' },
  { name: '主食', focus: false, category: '3' },
  { name: '汤类', focus: false, category: '4' },
  { name: '饮料', focus: false, category: '5' }
])
const onClickIcon = async () => {
  if (menu.totalNum == 0) {
    showToast('购物车为空，请先选购！')
  } else {
    try {
      const cartId = localStorage.getItem('cartId')
      cartItems.value = await getCartItem(cartId)
      showBottom.value = true
    } catch (error) {
      console.error('获取购物车菜品失败:', error)
      showToast('获取购物车菜品失败，请稍后再试。')
    }
  }
}
const onClickMenuButton = (name) => {
  buttons.value.forEach((button) => {
    if (button.name === name) {
      console.log(button.name)
      if (button.focus === true) {
        button.focus = false
      } else button.focus = true
    } else button.focus = false
  })
}

const searchTerm = ref('')
const filteredItems = computed(() => {
  const activeButton = buttons.value.find((button) => button.focus)

  // 筛选按类别
  let filteredByCategory = activeButton
    ? items.value.filter(
        (item) => item.category === activeButton.category || activeButton.name === '促销'
      )
    : items.value

  // 筛选按搜索词
  const searchLower = searchTerm.value.trim().toLowerCase()
  filteredByCategory = filteredByCategory.filter((item) =>
    item.dishName.toLowerCase().includes(searchLower)
  )

  // 如果按钮是促销按钮，筛选出促销菜品
  if (activeButton && activeButton.name === '促销') {
    filteredByCategory = filteredByCategory.filter(
      (item) => item.discountPrice && item.discountPrice !== item.dishPrice
    )
  }

  if (filteredByCategory.length === 0 && items.value.length !== 0) {
    showToast('没有匹配的菜品')
  }

  return filteredByCategory
})
const increaseQuantity = async (index) => {
  const item = cartItems.value[index]
  const cartId = localStorage.getItem('cartId')
  const res = await updateCartItem(cartId, item.dishId, item.quantity + 1)
  if (res.success === true) {
    menu.addItem(item)
    item.quantity++
  } else {
    showToast('菜品已卖完')
  }
}
const decreaseQuantity = async (index) => {
  const item = cartItems.value[index]
  item.quantity--
  if (item.quantity === 0) {
    cartItems.value.splice(index, 1)
    const cartId = localStorage.getItem('cartId')
    menu.minusItem(item)
    await deleteCartItem(cartId, item.dishId)
  } else {
    menu.minusItem(item)
    const cartId = localStorage.getItem('cartId')
    await updateCartItem(cartId, item.dishId, item.quantity)
  }
}
const user = useUserStore()
const handleCheckout = () => {
  if (user.deliver_or_dining) router.push('/ShoppingCart')
  else router.push('/diningCart')
}
const clearCartItem = async () => {
  try {
    const cartId = localStorage.getItem('cartId')
    await clearCart(cartId)
    cartItems.value = [] // 清空本地购物车数据
    menu.clear()
  } catch (error) {
    console.error('清空购物车失败:', error)
    showToast('清空购物车失败，请稍后再试。')
  }
}
</script>
<style scoped>
.search-line-icon {
  display: flex;
  align-items: center;
  justify-content: center;
}

.slogan {
  width: 100%;
  margin: 0;
  padding: 0;
  display: block;
}

.container {
  display: flex;
}
.button-list {
  padding-top: 1vh;
  width: 20%; /* 根据需要调整宽度 */
  background-color: #f0f0f0; /* 更柔和的背景色 */
  z-index: 1;
}

.menu {
  font-size: 20px;
  font-weight: bold; /* 加粗字体 */
  color: #333; /* 深色字体以提高对比度 */
  margin: 10px 10px; /* 上下间距 */
  text-align: center;
}

.menu-button {
  width: 100%;
  border: none;
  background-color: #f0f0f0; /* 白色背景 */
  color: black; /* 更深的橙色 */
  padding: 2px; /* 更大的内边距 */
  font-size: large; /* 更合适的字体大小 */
  text-align: center;
  border-radius: 7px;
  transition:
    background-color 0.3s,
    color 0.3s; /* 添加平滑的过渡效果 */
}
.menu-button.focus {
  background-color: white;
  color: black;
}
.menu-line {
  display: flex;
  align-items: center;
}
.item-list {
  max-height: 70vh;
  width: 82%;
  overflow-y: auto;
  flex-wrap: wrap;
}
.dish-item {
  width: 94%;
  box-sizing: border-box;
}
.cart {
  margin-right: 5%;
  margin-left: 5%;
}

.text span {
  font-size: large;
  margin-top: 1vh;
  margin-left: 18vw;
}
.num {
  font-size: large;
  font-weight: bold;
}
.blank {
  height: 8vh; /* 设置行的高度 */
  width: 100%; /* 宽度占满父容器 */
  background-color: transparent; /* 背景色透明 */
}
.popup-container {
  border-radius: 10px 10px 0 0;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #f8f8f8;
  border-bottom: 1px solid #e0e0e0;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
}

.close-icon {
  font-size: 24px;
  color: #333;
}

.popup-content {
  padding: 10px;
}

.empty-cart {
  text-align: center;
  color: #999;
  padding: 20px;
}

.cart-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #e0e0e0;
}

.cart-item-img {
  width: 60px;
  height: 60px;
  margin-right: 10px;
  border-radius: 8px;
}

.cart-item-info {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-name {
  font-size: 16px;
  font-weight: 500;
}

.item-price-info {
  display: flex;
  flex-direction: column;
}

.original-price {
  text-decoration: line-through;
  color: #999;
  font-size: 14px;
}

.discount-price {
  color: #ff5722;
  font-size: 16px;
  font-weight: bold;
}

.item-quantity {
  font-size: 14px;
  color: #666;
}

.item-quantity-control {
  display: flex;
  align-items: center;
}

.item-quantity-control .van-button {
  margin: 0 5px;
}

.popup-footer {
  padding: 10px;
  background-color: #f8f8f8;
  border-top: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.total-price {
  font-size: 18px;
  font-weight: bold;
}

.checkout-button {
  width: 120px;
  background-color: #ffa500;
  border-color: #ffa500;
}
.plus-button {
  background-color: #ffa500;
  border-color: #ffa500;
}
.footer {
  border-radius: 15px;
  border-color: #ffa500;
}
.clear-cart-button {
  font-size: medium;
  color: red;
  font-weight: bold;
}

.floating-cart-text {
  color: white;
  font-size: 16px;
  margin-right: 10px;
}

.floating-cart-button {
  background-color: white;
  color: #ffb94a;
}
.price {
  font-size: medium;
}
.floating-cart-bar {
  position: fixed;
  bottom: 5%; /* 距离底部20%的位置 */
  left: 0; /* 确保悬浮栏从屏幕左侧开始 */
  width: 100%; /* 宽度适应屏幕 */
  margin: 0; /* 去掉边距 */
  border-radius: 50px; /* 保持圆角效果 */
  display: flex;
  align-items: center;
  justify-content: space-between; /* 添加空间分隔 */
  padding: 0 10px; /* 添加左右内边距 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2); /* 阴影效果 */
  background-color: white; /* 背景色设置为白色 */
  z-index: 1000; /* 确保悬浮栏在最上层 */
}

.cart-icon {
  padding: 10px; /* 内边距调整 */
}

.text {
  font-size: 14px; /* 字体大小适配手机屏幕 */
  font-weight: bold;
  background-color: #ffb94a;
  text-align: center;
  height: auto; /* 高度自适应内容 */
  flex: 1; /* 使其填满剩余空间 */
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50px; /* 保持圆角效果 */
}
.text span {
  margin-right: 17%;
}
</style>
