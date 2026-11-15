<template>
  <div class="VerifyVol">
    <transition name="fade">
      <div v-if="showMessage" class="message-popup">{{ showMessage }}</div>
    </transition>
    <h1>志愿者审核申请</h1>
    <table class="table">
      <thead>
        <tr>
          <th>申请ID</th>
          <th>账户ID</th>
          <th>申请时间</th>
          <th>查看</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="application in applications" :key="application.applicationId">
          <td>{{ application.applicationId }}</td>
          <td>{{ application.accountId }}</td>
          <td>{{ application.applicationDate }}</td>
          <td>
            <button class="btn" @click="viewApplication(application.applicationId)">查看详情</button>
          </td>
          <td>
            <button class="btn" @click="openApproveDialog(application)">审核</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="selectedApplication" class="application-details">
      <h2>申请详情</h2>
      <p>姓名: {{ selectedApplication.name }}</p>
      <p>性别: {{ selectedApplication.gender }}</p>
      <p>出生日期: {{ selectedApplication.birthDate }}</p>
      <p>手机号: {{ selectedApplication.phoneNum }}</p>
      <p>身份证号: {{ selectedApplication.idCard }}</p>
      <p>申请理由: {{ selectedApplication.selfStatement }}</p>
      <button class="btn" @click="closeDetails">关闭</button>
    </div>

    <div v-if="showApproveDialog" class="approve-dialog">
      <div class="dialog-content">
        <h2>审核情况</h2>
        <div class="textarea-container">
          <textarea v-model="approvalReason" placeholder="审核理由"></textarea>
        </div>
        <div class="dialog-actions">
          <button class="approve-button" @click="approveApplication('通过')">通过</button>
          <button class="reject-button" @click="approveApplication('不通过')">不通过</button>
        </div>
        <button class="close-dialog" @click="closeApproveDialog">X</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      applications: [],
      selectedApplication: null,
      showApproveDialog: false,
      approvalReason: '',
      pageInput: '', // 页码输入
      showMessage: "",
    };
  },
  created() {
    console.log('组件已创建，正在获取申请列表...');
    this.fetchApplications();
    console.log('当前申请数据:', this.applications);
  },
  methods: {
    // 页码跳转逻辑
    changePage(page) {
      const pageNum = Number(page); // 将输入的页码转换为数字
      if (pageNum >= 1 && pageNum <= this.totalPages) {
        this.currentPage = pageNum; // 设置当前页码
        this.pageInput = ''; // 清空输入框
      } else {
        // alert('请输入有效的页码'); // 添加简单的页码验证
      }
    },
    async fetchApplications() {
      try {
        console.log('正在从API获取所有申请数据...');
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.get('/demo-api/Volunteer/getAllApply', {
          headers: {
          'Authorization': `Bearer ${token}`,
        }
        });
        console.log('API响应数据:', response);
        this.applications = response.data.response;
        console.log('成功获取到申请数据:', this.applications);
      } catch (error) {
        console.error('获取申请数据时出错:', error);
      }
    },
    async viewApplication(applicationId) {
      try {
        console.log(`正在获取申请ID为 ${applicationId} 的详情...`);
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.get(`/demo-api/Volunteer/applyInfo/${applicationId}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        this.selectedApplication = response.data.response;
        console.log('成功获取到申请详情:', this.selectedApplication);
      } catch (error) {
        console.error('获取申请详情时出错:', error);
      }
    },
    openApproveDialog(application) {
      console.log('打开审核对话框，当前申请:', application);
      this.selectedApplication = application;
      this.showApproveDialog = true;
    },
    closeDetails() {
      console.log('关闭详情视图');
      this.selectedApplication = null;
    },
    closeApproveDialog() {
      console.log('关闭审核对话框');
      this.showApproveDialog = false;
      this.approvalReason = '';
    },
    show(message) {
      this.showMessage = message;
      setTimeout(() => {
        this.showMessage = "";
      }, 1000); // 错误信息3秒后消失
    },
    approveApplication(status) {
      console.log(`审核操作: 申请ID: ${this.selectedApplication.applicationId} 状态: ${status} 理由: ${this.approvalReason}`);
      // 调用 reviewApplication 函数进行审核
      this.reviewApplication(this.selectedApplication.applicationId, status, this.approvalReason);
    },
    async reviewApplication(applicationId, status, reason) {
      if(reason==''){
        this.show("必须输入理由");
        return;
      }
      try {
        console.log(`正在审核申请ID为 ${applicationId} 的数据...`);
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.post(`/demo-api/Volunteer/review/${applicationId}`, {
          reason: reason,
          status: status
        },
        {
          headers: {
          'Authorization':`Bearer ${token}`,
        }});
        console.log('审核请求成功:', response.data);
        this.show(`审核成功: 申请ID: ${applicationId} 状态: ${status}`);
        this.closeApproveDialog();
        this.fetchApplications(); // 刷新申请列表
      } catch (error) {
        console.error('审核请求时出错:', error);
        this.show('审核失败，请重试。');
      }
    }
  }
};
</script>


<style scoped>
.VerifyVol{
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #ffffff; /* 添加背景色 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
}
.table {
  width: 100%;
  border-collapse: collapse;
  background-color: #fff; /* 白色背景 */
  overflow: hidden; /* 隐藏溢出 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
}

.table th,
.table td {
  padding: 15px;
  border: 1px solid rgba(225, 217, 208, 0.5);
  text-align: left;
  font-size: 14px;
}

.table th {
  background-color: rgba(225, 217, 208, 0.5);
  color: rgb(0, 0, 0);
  text-transform: uppercase;
  font-weight: bold;
}

.table tbody tr:nth-child(even) {
  background-color: #f9f9f9; /* 斑马条纹效果 */
}

.table tbody tr:hover {
  background-color: #f1f1f1; /* 鼠标悬停时的背景颜色 */
}
button {
  cursor: pointer;
  padding: 5px 10px;
  margin: 5px;
}

.application-details {
  border: 1px solid #ddd;
  padding: 10px;
  background-color: #f9f9f9;
}

.application-details h2 {
  margin-top: 0;
}

.approve-dialog {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  max-width: 400px;
  width: 100%;
}

.dialog-content {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.dialog-content h2 {
  margin-top: 0;
  font-size: 20px;
  margin-bottom: 15px;
}

.textarea-container {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

textarea {
  width: 100%;
  height: 100px;
  margin-top: 10px;
  margin-bottom: 15px;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
}

.dialog-actions {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.approve-button,
.reject-button {
  padding: 10px 20px;
  border-radius: 5px;
  font-size: 14px;
}

.approve-button {
  background-color: #4CAF50;
  color: white;
  border: none;
}

.reject-button {
  background-color: #f44336;
  color: white;
  border: none;
}

.close-dialog {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
}
select,input {
  border: 2px solid black;
}
.message-popup {
  position: fixed;
  bottom: 50px;
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.6);
  color: rgb(255, 255, 255);
  padding: 10px 20px;
  border-radius: 5px;
  z-index: 999;
  opacity: 1;
  transition: opacity 0.5s ease-in-out;
}
.btn {
  border: 2px solid rgb(49, 81, 195);
  border-radius: 6px;
  color: rgb(49, 81, 195);
}
</style>
