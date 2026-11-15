<!-- RatingDialog.vue -->
<template>
    <van-dialog v-model:show="show" close-on-click-overlay
    :show-cancel-button="writeOrView === 'write'" show-confirm-button
        confirm-button-text="确认" cancel-button-text="取消" @confirm="onConfirm" @cancel="onCancel"
        confirmButtonColor="#7AAB13" :before-close="handleBeforeClose">
        <template #title>
            <div class="dialogTitle">请对本次服务进行评价 </div>
        </template>
        <div class="flavor">
            <div style="text-align: center;" class="flavorHead">
                <img src="../assets/image/flavor_review.png">
                <span class="falvorText">口味</span>
                <van-rate :readonly="writeOrView === 'view'" v-model="falvorRate" :size="30" color="#ffd21e"
                    void-icon="star" void-color="#eeeeee" />
            </div>

            <textarea :readonly="writeOrView === 'view'" v-model="flavorComment" 
            :placeholder="writeOrView === 'view' ? (flavorComment=='' ? flavorComment : '无评价'):'可在此处输入评价' "
                class="flavor-textarea" maxlength="50">
            </textarea>

            <p class="textPrompt">{{ flavorComment.length }}/50</p>
        </div>

        <div class="deliver" v-if="deliverOrDining === true">
            <div style="text-align: center;" class="deliverHead">
                <img src="../assets/image/deliver_review.png">
                <span class="deliverText">配送</span>
                <van-rate :readonly="writeOrView === 'view'" v-model="deliverRate" :size="30" color="#ffd21e"
                    void-icon="star" void-color="#eeeeee" />
            </div>
            <textarea :readonly="writeOrView === 'view'" v-model="deliverComment" 
            :placeholder="writeOrView === 'view' ? (deliverComment=='' ? deliverComment : '无评价'):'可在此处输入评价' "
                class="deliver-textarea" maxlength="50"></textarea>
            <p class="textPrompt">{{ deliverComment.length }}/50</p>
        </div>
    </van-dialog>
</template>

<script setup>
import { ref, defineEmits } from 'vue';
import { postDiningComment, postDeliverComment,getDeliverComment,getDiningComment } from '../api/api'
import 'vant/es/toast/style'
const props = defineProps({
    deliverOrDining: Boolean,
    orderId: String,
})

const handleBeforeClose = (action) => {
    if (action === 'confirm') {
        // 如果确认按钮被点击，弹窗将不会关闭
        console.log('确认按钮被点击，等待toast关闭');
    } else {
        // 取消或其他操作(点击遮罩层）弹窗关闭
        console.log('关闭弹窗')
        show.value = false;
        emit('exit')
    }
};

const writeOrView = ref('');
const show = ref(false);  // 控制弹窗显示的状态
const deliverRate = ref(3);
const falvorRate = ref(3);
const flavorComment = ref('');
const deliverComment = ref('');
// 定义事件
const emit = defineEmits(['exit']);


import { showLoadingToast, showSuccessToast, showFailToast } from 'vant';

// 确认按钮处理
const onConfirm = async () => {
    if (writeOrView.value == 'write') {
        showLoadingToast({
            message: '加载中...',
            forbidClick: true,
        });
        const status = props.deliverOrDining==true?
        await postDeliverComment(props.orderId, deliverRate.value, deliverComment.value, falvorRate.value,
            flavorComment.value): await postDiningComment(props.orderId, falvorRate.value,
            flavorComment.value);
        switch (status) {
            case true:
                showSuccessToast({
                    message: '评价成功',
                    duration: 1000,
                    onClose: () => {
                        console.log('toast关闭');
                        show.value = false;
                        console.log('弹窗关闭');
                        emit('exit');
                    }
                })
                break;
            case false:
                showFailToast('评价失败，请重试');
                break;
        }
    }
    else{
        console.log('关闭弹窗');
        show.value = false;
    }


};



// 供父组件调用的方法
function writeComment() {
    writeOrView.value = 'write';
    show.value = true;
    console.log('去评价');
}

const fetchComment = async () => {
    try {
        console.log(props.orderId)
        const response = props.deliverOrDining==true?
        await getDeliverComment(props.orderId):await getDiningComment(props.orderId);
        
        deliverRate.value = response.dStars?response.dStars:0;
        falvorRate.value = response.cStars?response.cStars:0;
        flavorComment.value = response.cReviewText?response.cReviewText:'';
        deliverComment.value = response.dReviewText?response.dReviewText:'';
        console.log('获取评价： 配送得分', deliverRate.value, '配送评价',
            deliverComment.value, '口味得分', falvorRate.value, '口味评价', flavorComment.value)
    } catch (error) {
        console.error('Error fetching comment:', error)
    }
}

const viewComment = async () => {
    writeOrView.value = 'view';
    console.log('查看评论');
    await fetchComment();
    show.value = true;
}

// 使用 defineExpose 暴露方法
defineExpose({
    writeComment,
    viewComment
});
</script>

<style scoped>
.textPrompt {
    margin-left: 250px;
    font-size: 12px;
    color: #aaa;
}

.custom-textarea::placeholder {
    color: #aaa;
    /* 占位符颜色 */
}

.dialogTitle {
    font-size: larger;
    font-weight: 700;
    margin-top: 2vh;
    margin-bottom: 2vh;
}

.flavor-textarea {
    margin-left: 25px;
    width: 270px;
    height: 80px;
    background-color: #f7f7f7;
    border: 1px solid #b9b9b9;
    border-radius: 4px;
    padding: 10px;
    font-size: 14px;
    resize: none;

}


.flavorHead {
    display: flex;
    align-items: center;
    margin-left: 12px;
}

.falvorText {
    font-size: large;
    font-weight: 600;
    letter-spacing: 3px;
    margin-right: 10px;
}

.deliver-textarea {
    margin-left: 25px;
    width: 270px;
    height: 80px;
    background-color: #f7f7f7;
    border: 1px solid #b9b9b9;
    border-radius: 4px;
    padding: 10px;
    font-size: 14px;
    resize: none;

}


.deliverHead {
    display: flex;
    align-items: center;
    margin-left: 23px;
    height: 64px;
}

.deliverText {
    font-size: large;
    font-weight: 600;
    letter-spacing: 3px;
    margin-left: 15px;
    margin-right: 10px;
}

.deliver {
    margin-bottom: 20px;
}
</style>