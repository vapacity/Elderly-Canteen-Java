<template>
  <div class="tab-content-2">
    <!-- 按照你的需求填充内容 -->
    <div class="log-item" v-for="log in logs" :key="log.id">
      <div class="log-time">{{ log.time }}</div>
      <div class="log-details">
        <div class="log-operation">{{ log.operation }}</div>
        <div class="log-description">{{ log.description }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TabContent2',
  data() {
    return {
      logs: [
        // 示例数据
        { id: 1, time: '时间', operation: '操作', description: '详情' },
        // 添加更多日志
      ]
    };
  }
};
</script>

<style scoped>
.tab-content-2 {
  display: flex;
  flex-direction: column;
}

.log-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;
}

.log-time {
  width: 60px;
  font-weight: bold;
}

.log-details {
  flex-grow: 1;
  margin-left: 10px;
}

.log-operation {
  font-weight: bold;
}

.log-description {
  color: #999;
}
</style>
