<script setup>
import deliveryIcon from '@/assets/deliver.png'
import diningIcon from '@/assets/resturant.png'
const props = defineProps({
    order_detail: Object
})


const router = useRouter();
const toDetail=()=>{
    console.log('进入订单详情',props.order_detail);
    router.push({ path: '/OrderDetail', query: { detail:JSON.stringify(props.order_detail)} });
}

const limitedDishes = computed(() => {
    const dishes = props.order_detail.orderDishes;
    return dishes.length > 2 ? dishes.slice(0, 2) : dishes;
});
</script>

<template>
    <div class="order-cell" @click="toDetail">
        <div class="part1">
            <img :src="order_detail.deliverOrDining ?
                deliveryIcon
                : diningIcon" />
            <p class="order-number">{{ order_detail.orderId }}</p>
            <van-icon name="arrow" size="4vw"></van-icon>
        </div>


        <div class="order-date">{{ order_detail.updatedTime }}</div>

        <div class="part3">
            <div class="food-imgs">
                <div v-for="(dish, index) in limitedDishes" :key="index" class="food-imgs">
                    <img :src="dish.picture" :alt="dish.dishName">
                </div>
            </div>
            <div class="price">¥{{ order_detail.money }}</div>
        </div>

    </div>
</template>

<style scoped>
.food-imgs img {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
    margin-right: 1.5vh;
    margin-top: 1vh;
}

.food-imgs {
    height: 7.5vh;
    display: flex;
    flex-direction: row;
}

.price {
    font-size: 2.8vh;
    font-weight: bold;
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
    height: 15.5vh;
    border-radius: 15px;
    /* 圆角半径 */
    background-color: white;
    width: 90vw;
    padding-left: 6vw;
    padding-right: 6vw;
    padding-top: 1.2vh;
    padding-bottom: 1.2vh;
    margin-bottom: 15px;
}
</style>
