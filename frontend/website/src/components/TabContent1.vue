<template>
  <div class="tab-content-1">
    <div class="item" v-for="item in items" :key="item.id">
      <div class="item-header">
        <img :src="item.image" alt="User" class="item-image" />
        <div class="item-details">
          <div class="item-name">{{ item.name }}</div>
          <div class="item-time">{{ item.time }}</div>
        </div>
        <div class="item-status">{{ item.status }}</div>
      </div>
      <div class="item-content">{{ item.content }}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TabContent1',
  data() {
    return {
      items: [
        // 示例数据
        { id: 1, image: '/avatar.jpg', name: '人名', time: '时间', status: '状态', content: '内容' },
        // 添加更多项目
      ]
    };
  }
};
</script>

<style scoped>
.tab-content-1 {
  display: flex;
  flex-direction: column;
}

.item {
  margin-bottom: 20px;
}

.item-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.item-image {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.item-details {
  flex-grow: 1;
}

.item-name {
  font-weight: bold;
}

.item-time {
  color: #999;
}

.item-status {
  color: #00796b;
}

.item-content {
  margin-bottom: 10px;
}


</style>
