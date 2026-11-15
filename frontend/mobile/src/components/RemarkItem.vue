<template>
  <div class="review-box">
    <span class="title">备注</span>
    <textarea v-model="remark.remark" placeholder="请输入备注..." rows="5" cols="50"></textarea>
  </div>
</template>
<script setup>
import { useRemarkstore } from '@/store/modules/remark'
const remark = useRemarkstore()
</script>
<style scoped>
.review-box {
  margin: 20px;
  padding: 20px;
  background-color: #f9f9f9;
  border: 2px solid #d0d0d0; /* 增加边框宽度并调整颜色 */
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 保持原有阴影 */
  transition: border-color 0.3s ease-in-out; /* 边框颜色变化的过渡效果 */
}

.title {
  display: block;
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

textarea {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border: 1px solid #d0d0d5;
  border-radius: 8px;
  resize: vertical; /* 允许垂直调整 */
  background-color: #fff;
  color: #333;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
}

textarea::placeholder {
  color: #aaa;
}
</style>
