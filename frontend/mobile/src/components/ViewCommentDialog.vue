<!-- RatingDialog.vue -->
<template>
    <van-dialog v-model:show="show" show-cancel-button show-confirm-button confirm-button-text="确认"
        cancel-button-text="取消" @confirm="onConfirm" 
        @cancel="onCancel" confirmButtonColor="#7AAB13"
        :before-close="handleBeforeClose"
        >
        <template #title>
            <div class="dialogTitle">请对本次服务进行评价 </div>
        </template>
        <div class="flavor">
            <div style="text-align: center;" class="flavorHead">
                <img src="../assets/image/flavor_review.png">
                <span class="falvorText">口味</span>
                <van-rate v-model="falvorRate" :size="30" color="#ffd21e" void-icon="star" void-color="#eeeeee" />
            </div>
            <textarea v-model="flavorComment" placeholder="可在此处输入评价" class="flavor-textarea" maxlength="50"></textarea>
            <p class="textPrompt">{{ flavorComment.length }}/50</p>
        </div>

        <div class="deliver" v-if="deliverOrDining === true">
            <div style="text-align: center;" class="deliverHead">
                <img src="../assets/image/deliver_review.png">
                <span class="deliverText">配送</span>
                <van-rate v-model="deliverRate" :size="30" color="#ffd21e" void-icon="star" void-color="#eeeeee" />
            </div>
            <textarea v-model="deliverComment" placeholder="可在此处输入评价" class="deliver-textarea"
                maxlength="50"></textarea>
            <p class="textPrompt">{{ deliverComment.length }}/50</p>
        </div>
    </van-dialog>
</template>

<script setup>
import { ref, defineEmits } from 'vue';
import { postComment,getComment } from '../api/api'
import 'vant/es/toast/style'
const props = defineProps({
    deliverOrDining: Boolean,
    orderId: String,
})

const handleBeforeClose = (action, done) => {
  if (action === 'confirm') {
    // 如果确认按钮被点击，不调用 done()，弹窗将不会关闭
    console.log('确认按钮被点击，但弹窗不会关闭');
  } else {
    // 取消或其他操作调用 done()，让弹窗关闭
    done();
  }
};

const writeOrView=ref('');
const show = ref(false);  // 控制弹窗显示的状态
const deliverRate = ref(3);
const falvorRate = ref(3);
const flavorComment = ref('');
const deliverComment = ref('');
// 定义事件
const emit = defineEmits(['confirm', 'cancel']);


import { showLoadingToast, showSuccessToast, showFailToast } from 'vant';

// 确认按钮处理
const onConfirm = async () => {
    showLoadingToast({
        message: '加载中...',
        forbidClick: true,
    });
    const status = await postComment(props.orderId, deliverRate.value, falvorRate.value,
        deliverComment.value, flavorComment.value);
    switch (status) {
        case 200:
            showSuccessToast({
                message: '评价成功',
                duration:1000,
                onClose: () => { 
                    console.log('toast关闭');
                    show.value = false;
                    console.log('弹窗关闭');
                    emit('confirm');
                }
            })
            break;
        case 400:
            showFailToast('评价失败，请重试');
            break;
    }

};

// 取消按钮处理
const onCancel = () => {
    emit('cancel');
    show.value = false;
};

// 供父组件调用的方法
function writeComment() {
    writeOrView.value='write';
    show.value = true;
    console.log('去评论');
}

function viewComment() {
    writeOrView.value='view';
    show.value = true;
    console.log('查看评论');
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