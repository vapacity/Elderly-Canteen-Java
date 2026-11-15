<template>
  <div class="second-component">
    <div class="tabs">
      <div
          v-for="(tab, index) in tabs"
          :key="index"
          :class="['tab', { 'active-tab': activeTab === index }]"
          @click="activeTab = index"
      >
        {{ tab.name }}
      </div>
    </div>
    <div class="tab-content">
      <div v-if="activeTab === 0">
        <TabContent1 />
      </div>
      <div v-if="activeTab === 1">
        <TabContent2 />
      </div>
      <div v-if="activeTab === 2">
        <TabContent3 />
      </div>
    </div>
  </div>
</template>

<script>
import TabContent1 from './TabContent1.vue';
import TabContent2 from './TabContent2.vue';
import TabContent3 from './TabContent3.vue';

export default {
  name: 'SecondComponent',
  components: {
    TabContent1,
    TabContent2,
    TabContent3
  },
  data() {
    return {
      activeTab: 0,
      tabs: [
        { name: '我的公告' },
        { name: '我的日志' },
        { name: '我的信息' }
      ]
    };
  }
};
</script>

<style scoped>
.second-component {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  position: absolute;
  top: 20px;
  right: 20px;
  bottom: 20px;
  left: 300px; /* 确保它不会与第一个组件重叠 */
  overflow-y: auto;
  scrollbar-width: none; /* 隐藏滚动条 Firefox */
}

.second-component::-webkit-scrollbar {
  display: none; /* 隐藏滚动条 Chrome */
}

.tabs {
  display: flex;
  justify-content: flex-start;
  border-bottom: 1px solid #ddd;
  margin-bottom: 20px;
}

.tab {
  padding: 10px 20px;
  cursor: pointer;
  font-weight: bold;
  color: #D8CBA7;
}

.active-tab {
  color: #000;
  border-bottom: 2px solid #F5A623;
}

.tab-content {
  padding: 20px;
}

select,input {
  border: 2px solid black;
}
</style>
