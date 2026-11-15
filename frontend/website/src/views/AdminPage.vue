<template>
    <div class="app">
      <AppBar :selectedMenu="selectedMenu" @menu-selected="handleMenuSelected"/>
      <div class="main">
        <MySidebar @menu-selected="handleMenuSelected" />
        <div class="content-wrapper">
          <MyContent v-if="selectedMenu === '公告管理'" />
          <UserList v-if="selectedMenu === '普通用户列表'" />
          <AdminList v-if="selectedMenu === '管理员列表'" />
          <DishList v-if="selectedMenu ==='菜品列表'"/>
          <WeeklyMenu v-if="selectedMenu ==='每周菜单'"/>
          <WeeklyPromotion v-if="selectedMenu ==='每周促销'"/>
          <EmployeeManagement v-if="selectedMenu ==='职工管理'"/>
          <VerifyVolunteer v-if="selectedMenu ==='审核志愿者申请'"/>
          <VolunteerManagement v-if="selectedMenu ==='上任志愿者管理'"/>
          <UserStatistic v-if="selectedMenu ==='用户统计'"/>
          <VolunteerStatistic v-if="selectedMenu ==='志愿者统计'"/>
          <OrdersStatistic v-if="selectedMenu ==='订单统计'"/>
          <WarehouseManagement v-if="selectedMenu === '仓库管理'" />
          <FinancialAudit v-if="selectedMenu === '财务审核'" />
          <FinancialSummary v-if="selectedMenu === '财务总表'" />
          <FinancialAnalysis v-if="selectedMenu === '财务统计'" />
          <SystemLog v-if="selectedMenu === '系统日志'" />
          <CanteenInfo v-if="selectedMenu === '食堂信息'" />
          <AnnouncementManagement v-if="selectedMenu === '公告管理'" />
          <div class="overlay" v-if="isUserProfileVisible">
            <UserProfile />
            <SecondComponent />
          </div>
        </div>
      </div>
      <div class="floating-button" @click="handleClick">返回主页</div>
    </div>
  </template>
  
  <script>
  import AppBar from "@/components/AppBar.vue";
  import MySidebar from "@/components/MySidebar.vue";
  import MyContent from "@/components/MyContent.vue";
  import UserProfile from "@/components/UserProfile.vue";
  import SecondComponent from "@/components/SecondComponent.vue";
  import UserList from "@/components/UserList.vue";
  import AdminList from "@/components/AdminList.vue"; // 导入AdminList组件
  import DishList from "@/components/DishList.vue";
  import WeeklyMenu from "@/components/WeeklyMenu.vue";
  import WeeklyPromotion from '@/components/WeeklyPromotion.vue';
  import EmployeeManagement from '@/components/EmployeeManagement.vue';
  import VerifyVolunteer from "@/components/VerifyVolunteer.vue";
  import VolunteerManagement from "@/components/VolunteerManagement.vue";
  import UserStatistic from '@/components/UserStatistic.vue';
  import VolunteerStatistic from '@/components/VolunteerStatistic.vue';
  import OrdersStatistic from '@/components/OrdersStatistic.vue';
  import WarehouseManagement from '@/components/WarehouseManagement.vue';
  import FinancialAudit from '@/components/FinancialAudit.vue';
  import FinancialSummary from '@/components/FinancialSummary.vue'; // 导入FinancialSummary组件
  import AnnouncementManagement from '@/components/AnnouncementManagement.vue';
  import FinancialAnalysis from '@/components/FinancialAnalysis.vue';
  import SystemLog from '@/components/SystemLog.vue';
  import CanteenInfo from '@/components/CanteenInfo.vue'; // 导入AnnouncementManagement组件
  
  export default {
    components: {
      CanteenInfo,
      SystemLog,
      FinancialAnalysis,
      AppBar,
      MySidebar,
      MyContent,
      UserProfile,
      SecondComponent,
      UserList,
      AdminList, // 注册AdminList组件
      DishList,
      WeeklyMenu,
      WeeklyPromotion,
      EmployeeManagement,
      VerifyVolunteer,
      VolunteerManagement,
      UserStatistic,
      VolunteerStatistic,
      OrdersStatistic,
      WarehouseManagement,
      FinancialAudit,
      FinancialSummary, // 注册FinancialSummary组件
      AnnouncementManagement // 注册AnnouncementManagement组件
    },
    data() {
      return {
        selectedMenu: '普通用户列表',
        isUserProfileVisible: false
      };
    },
    methods: {
      handleMenuSelected(menuName) {
        this.selectedMenu = menuName;
        this.isUserProfileVisible = (menuName === '我');
      },
      handleClick() {
        this.$router.push('/home'); 
      }
    }
  };
  </script>
  
  <style>
  /* 设置整个页面的背景颜色 */
  html, body {
    background-color: #f5f5f5; /* 浅灰色 */
    margin: 0;
    padding: 0;
    height: 100%;
  }
  .app {
    display: flex;
    height: 100vh;
    flex-direction: column;
  }
  
  .main {
    display: flex;
    flex: 1;
  }
  
  .content-wrapper {
    flex: 1;
    margin-top: 56px; /* 确保内容区域在AppBar下方 */
    margin-left: 200px; /* 确保内容区域在侧边栏右侧 */
    padding: 20px;
    position: relative; /* 允许绝对定位 */
  }
  
  .overlay {
    display: flex;
    gap: 20px; /* 在两个组件之间增加间距 */
    position: relative;
    width: 100%;
    height: 100%;
  }
  
  .user-profile {
    flex-shrink: 0; /* 保持组件大小不变 */
    position: relative;
    top: 20px;
    left: 20px;
    z-index: 10; /* 确保它位于第二个组件之上 */
  }
  
  .second-component {
    flex-grow: 1; /* 使组件占满右侧白色空间 */
    margin-left: 50px; /* 增加左边距，确保两个组件之间有距离 */
    position: relative;
    top: 20px;
    right: 20px;
    bottom: 20px;
    overflow-y: auto;
    scrollbar-width: none; /* 隐藏滚动条 Firefox */
  }
  
  .second-component::-webkit-scrollbar {
    display: none; /* 隐藏滚动条 Chrome */
  }

  .floating-button {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 100px;
  height: 100px;
  display: flex;       
  align-items: center;
  justify-content: center;
  background-image: linear-gradient(to right, #7fe9cb, #1dd28d); /* 渐变背景 */
  color: white;
  border-radius: 10px;
  cursor: pointer;
  z-index: 1000; /* 确保它在所有元素之上 */
  transition: background-image 0.3s ease, transform 0.3s ease;
}

.floating-button:hover {
  background-image: linear-gradient(to right, #4ab395, #119864); 
  color: rgb(228, 228, 228);
  transform: scale(1.1); /* 可选的放大效果 */
}
  </style>