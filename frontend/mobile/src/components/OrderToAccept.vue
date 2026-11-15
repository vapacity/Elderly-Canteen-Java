<script setup>
const props = defineProps({
    order_detail: Object,
    isAccepted:Boolean
})


const router = useRouter();
const toDetail=()=>{
    router.push({ path: '/OrderDetail', query: { detail:JSON.stringify(props.order_detail)} });
}

const emit = defineEmits(['clickAccept']);
const clickAccept=()=>{
    console.log("点击接单按钮");
    emit('clickAccept',props.order_detail);
}
</script>

<template>
    <div v-if="Object.keys(order_detail).length!==0">
        <div class="order-cell" @click="toDetail">
        <div class="part1">
            <img :src="order_detail.deliverOrDining ?
                'https://z4a.net/images/2024/07/22/waimai.png'
                : 'https://z4a.net/images/2024/07/22/tangshi.png'" />
            <p class="order-number">{{ order_detail.orderId }}</p>
            <van-icon name="arrow" size="4vw"></van-icon>
        </div>


        <div class="order-date">{{ order_detail.updatedTime }}</div>

     
        <div class="part3">
            <div class="address">地址：{{ order_detail. cusAddress}}</div>
            <!-- .stop阻止事件继续向上冒泡,使点击触发的是按钮事件而非卡片事件 -->
            <van-button @click.stop="clickAccept" 
            :disabled="isAccepted===true" 
            :style="{ backgroundColor: 'rgb(249, 184, 62)' }" >接单</van-button> 
        </div>
    </div>
    </div>
   

</template>

<style scoped>
.part3 .van-button{
    max-height: 80%;
    object-fit: contain;
    border-radius: 8px;
}
.address {
    font-size:1.8vh;
    width: 80%;

    display: flex;
    align-items: center;
    height: 100%;
}
.part3 {
    height: 50%;
    display: flex;
    justify-content: space-between;
    align-items: end;
}


.order-date {
    font-size: small;
    color: rgb(153, 151, 151);
    margin-top: 5px;
}

.part1 p {
    font-size: medium;
    margin-right: auto;
    font-weight: bold;

}

.part1 img {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
    margin-right: 5%;
    /* 保持图片比例并裁剪 */
}

.part1 {
    height: 20%;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.order-cell {
    display: flex;
    flex-direction: column;
    height: 15.5vh;
    border-radius: 15px;
    /* 圆角半径 */
    background-color: white;
    width: 90vw;
    padding-left: 5vw;
    padding-right: 5vw;
    padding-top: 1.2vh;
    padding-bottom: 1.5vh;
    margin-bottom: 10px;
}
</style>
