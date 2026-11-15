<script setup>
const props = defineProps({
    nowView: {
        type: String,
        default: 'home'
    }
});
const active = ref(props.nowView);
</script>

<template>
    <div class="bottom">
        <van-tabbar v-model="active">
            <van-tabbar-item name="home" replace to="/Home">
                <span>首页</span>
                <template #icon="home">
                    <van-icon name="home-o" size="30px"></van-icon>
                </template>
            </van-tabbar-item>
            <van-tabbar-item name="orders" replace to="/MyOrders">
                <span>订单</span>
                <template #icon="orders">
                    <van-icon name="orders-o" size="30px"></van-icon>
                </template>
            </van-tabbar-item>
            <van-tabbar-item name="user" replace to="/User">
                <span>我的</span>
                <template #icon="user">
                    <van-icon name="user-circle-o" size="30px"></van-icon>
                </template>
            </van-tabbar-item>
        </van-tabbar>
    </div>
</template>

<style scoped>
.bottom {
    height: 8.4vh;
    width: 100%;
    display: flex;
    position: fixed;
    bottom: 0;
    z-index: 1000;

}

.bottom .van-tabbar-item {
    height: 100%;
    /* 使van-tabbar-item高度适应van-tabbar */
    display: flex;
    align-items: center;
    /* 垂直居中对齐 */
    justify-content: center;
    /* 水平居中对齐 */
    font-size: 15px;
    /* 放大文字 */
}

.bottom .van-tabbar {
    height: 100%;
    /* 使van-tabbar高度适应底部容器 */
    position: relative;
    /* 设置相对定位，覆盖默认的固定定位 */
}
</style>
