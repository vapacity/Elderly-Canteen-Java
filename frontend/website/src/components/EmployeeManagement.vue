<template>
  <div id="app">
    <div class="container">
      <h2>职工管理</h2>
      <div class="search-add-container">
        <input v-model="searchName" placeholder="姓名" class="input-field" />
        <select v-model="searchPosition" class="input-field">
          <option value="">职位</option>
          <option v-for="position in positions" :key="position" :value="position">{{ position }}</option>
        </select>
        <button @click="search" class="btn blue">搜索</button>
        <button @click="showAddModal" class="btn green">添加职工</button>
        <button @click="showBatchModal" class="btn yellow">发工资</button>
      </div>
      <table class="table">
        <thead>
        <tr>
          <th>ID</th>
          <th>姓名</th>
          <th>身份证号</th>
          <th>电话</th>
          <th>地址</th>
          <th>职位</th>
          <th>工资</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>

          <tr v-for="employee in paginatedEmployees" :key="employee.employeeId">
            <td>{{ employee.employeeId }}</td>
            <td>{{ employee.employeeName }}</td>
            <td>{{ employee.idCard }}</td>
            <td>{{ employee.phoneNum }}</td>
            <td>{{ employee.address }}</td>
            <td>{{ employee.employeePosition }}</td>
            <td>{{ employee.salary }}</td>
            <td>
              <button @click="showEditModal(employee)" class="btn blue">修改</button>
              <button @click="deleteEmployee(employee.employeeId)" class="btn red">删除</button>
            </td>
          </tr>

        </tbody>
      </table>
      <div class="pagination">
        <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1" class="button orange">上一页</button>
        <span>{{ currentPage }} / {{ totalPages }}</span>
        <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages" class="button orange">下一页</button>
        <input v-model="pageInput" type="number" min="1" :max="totalPages" placeholder="跳转" class="input-field" />
        <button @click="changePage(pageInput)" :disabled="!pageInput || pageInput < 1 || pageInput > totalPages" class="button orange">跳转</button>
      </div>

      <div v-if="isModalVisible" class="modal-overlay" @click.self="closeModal">
        <div class="modal-content">
          <!-- 修改关闭按钮为图像 -->
          <button class="close-button" @click="closeModal">
            <img src="close.png" alt="关闭" class="close-icon" />
          </button>
          <h3>{{ isEditing ? '修改职工' : '添加职工' }}</h3>
          <form @submit.prevent="isEditing ? updateEmployee() : addEmployee()" class="modal-form">
            <div class="form-group">
              <label>姓名</label>
              <input v-model="employeeForm.employeeName" required class="input-field" />
            </div>
            <div class="form-group">
              <label>身份证号</label>
              <input v-model="employeeForm.idCard" required class="input-field" />
            </div>
            <div class="form-group">
              <label>电话</label>
              <input v-model="employeeForm.phoneNum" required class="input-field" />
            </div>
            <div class="form-group">
              <label>地址</label>
              <input v-model="employeeForm.address" required class="input-field" />
            </div>
            <div class="form-group">
              <label>职位</label>
              <select v-model="employeeForm.employeePosition" required class="input-field">
                <option v-for="position in positions" :key="position" :value="position">{{ position }}</option>
              </select>
            </div>
            <div class="form-group">
              <label>工资</label>
              <input v-model="employeeForm.salary" type="number" required class="input-field" />
            </div>
            <button type="submit" class="button primary confirm-button">确认</button>
          </form>
        </div>
      </div>

      <div v-if="isBatchModalVisible" class="modal-overlay" @click.self="closeBatchModal">
        <div class="modal-content">
          <!-- 修改关闭按钮为图像 -->
          <button class="close-button" @click="closeBatchModal">
            <img src="close.png" alt="关闭" class="close-icon" />
          </button>
          <h3>请选职工</h3>
          <form @submit.prevent="batchPaySalary">
            <table class="table">
              <thead>
              <tr>
                <th></th>
                <th>ID</th>
                <th>姓名</th>
                <th>工资</th>
                <th>本月是否已发工资</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="employee in employees" :key="employee.employeeId">
                <td><input v-if="!employee.isPaidThisMonth" type="checkbox" v-model="selectedEmployees" :value="employee.employeeId" /></td>
                <td>{{ employee.employeeId }}</td>
                <td>{{ employee.employeeName }}</td>
                <td>{{ employee.salary }}</td>
                <td>
                  <span v-if="employee.isPaidThisMonth">已发放</span>
                  <span v-else>未发放</span>
                </td>
              </tr>
              </tbody>
            </table>
            <div class="form-group">
              <input type="checkbox" v-model="selectAll" @change="toggleSelectAll" /> 全选
            </div>
            <button type="submit" class="button primary">确认</button>
          </form>
        </div>
      </div>
    </div>
    <div v-if="notification.message" class="notification" :class="notification.type">{{ notification.message }}</div>
  </div>
</template>

<script>
import axios from 'axios';
import { ref, reactive, computed } from 'vue';

axios.defaults.baseURL = ''; //

export default {
  setup() {
    const employees = ref([]);
    const filteredEmployees = ref([]);
    const isModalVisible = ref(false);
    const isBatchModalVisible = ref(false);
    const isEditing = ref(false);
    const employeeForm = reactive({
      employeeName: '',
      idCard: '',
      phoneNum: '',
      address: '',
      employeePosition: '',
      salary: ''
    });
    const positions = ref(['财务','职工','仓库','总管理']);
    const searchName = ref('');
    const searchPosition = ref('');
    const currentPage = ref(1);
    const totalPages = ref(1);
    const itemsPerPage = 10;
    const selectedEmployees = ref([]);
    const selectAll = ref(false);
    const pageInput = ref('');
    const notification = ref({ message: '', type: '' });

    const fetchEmployees = async () => {
      try {
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.get('/demo-api/employee/getInfo', {
          headers: {
            'Authorization': `Bearer ${token}` // 添加 token 到请求头
          }
        });
        if (Array.isArray(response.data) && response.data.length > 0 && response.data[0].response && Array.isArray(response.data[0].response)) {
          employees.value = response.data[0].response.map(emp => ({
            ...emp,
            paid: emp.paid || 0
          }));
          filteredEmployees.value = employees.value;
          totalPages.value = Math.ceil(filteredEmployees.value.length / itemsPerPage);
        } else {
          console.error('API 返回的数据结构不符合预期:', response.data);
          employees.value = [];
          filteredEmployees.value = [];
          totalPages.value = 0;
        }
      } catch (error) {
        console.error('Error fetching employees:', error);
      }
    };

    const search = async () => {
      try {
        let filtered = employees.value;
        if (searchName.value) {
          filtered = filtered.filter(emp => emp.employeeName.includes(searchName.value));
        }
        if (searchPosition.value) {
          filtered = filtered.filter(emp => emp.employeePosition === searchPosition.value);
        }
        filteredEmployees.value = filtered;
        totalPages.value = Math.ceil(filteredEmployees.value.length / itemsPerPage);
        currentPage.value = 1;
      } catch (error) {
        console.error('Error searching employees:', error);
      }
    };

    const paginatedEmployees = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage;
      return filteredEmployees.value.slice(start, start + itemsPerPage);
    });

    const showAddModal = () => {
      isEditing.value = false;
      resetForm();
      isModalVisible.value = true;
    };

    const showEditModal = (employee) => {
      isEditing.value = true;
      Object.assign(employeeForm, employee);
      isModalVisible.value = true;
    };

    const closeModal = () => {
      isModalVisible.value = false;
    };

    const closeBatchModal = () => {
      isBatchModalVisible.value = false;
    };

    const showBatchModal = () => {
      isBatchModalVisible.value = true;
    };

    const addEmployee = async () => {
      if (employeeForm.phoneNum.length !== 11) {
        showNotification(`电话号码必须为11位,请重新输入。`, 'error');
        return;
      }

      if (employeeForm.idCard.length !== 18) {
        showNotification(`身份证号必须为18位,请重新输入。`, 'error');
        return;
      }
      try {
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.post('/demo-api/employee/add', { ...employeeForm, paid: 0 }, {
          headers: {
            'Authorization': `Bearer ${token}` // 添加 token 到请求头
          }
        });

        if (response.status === 200) {
          employees.value.push(response.data);
          closeModal();
          totalPages.value = Math.ceil(employees.value.length / itemsPerPage);
          currentPage.value = totalPages.value;
          fetchEmployees();
        }
      } catch (error) {
        if (error.response && error.response.status === 400) {
          console.error('Employee ID already exists');
        } else {
          console.error('Error adding employee:', error);
        }
      }
    };
    const showNotification = (message, type = 'info') => {
      notification.value = { message, type };
      setTimeout(() => {
        notification.value = { message: '', type: '' };
      }, 3000);
    };
    const updateEmployee = async () => {
      if (employeeForm.phoneNum.length !== 11) {
        showNotification(`电话号码必须为11位,请重新输入。`, 'error');
        return;
      }

      if (employeeForm.idCard.length !== 18) {
        showNotification(`身份证号必须为18位,请重新输入。`, 'error');
        return;
      }

      try {
        const token = localStorage.getItem("token"); // 获取存储的 token
        // 发送 PUT 请求更新员工信息
        await axios.put(`/demo-api/employee/${employeeForm.employeeId}`, { ...employeeForm }, {
          headers: {
            'Authorization': `Bearer ${token}` // 添加 token 到请求头
          }
        });
        const index = employees.value.findIndex(employee => employee.employeeId === employeeForm.employeeId);
        employees.value.splice(index, 1, { ...employeeForm });
        closeModal();
      } catch (error) {
        console.error('Error updating employee:', error);
      }
    };

    const deleteEmployee = async (id) => {
      const token = localStorage.getItem("token"); // 获取存储的 token
      try {
        const response= await axios.delete(`/demo-api/employee/${id}`,{
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        console.log('删除请求成功，服务器响应:', response.data);

        employees.value = employees.value.filter(employee => employee.employeeId !== id);
        totalPages.value = Math.ceil(employees.value.length / itemsPerPage);
        fetchEmployees();
        if (currentPage.value > totalPages.value) {
          currentPage.value = totalPages.value;
        }
      } catch (error) {
        console.error('删除职工失败:', error);
      }
    };

    const batchPaySalary = async () => {
      try {
        const employeeIds = selectedEmployees.value;
        console.log('所有员工的isPaidThisMonth属性:');
        employees.value.forEach(employee => {
          console.log(`员工ID: ${employee.employeeId}, isPaidThisMonth: ${employee.isPaidThisMonth}`);
        });
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.post('/demo-api/employee/payWage', employeeIds, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (response.status === 200) {
          showNotification(`工资发放成功`, 'success');
          fetchEmployees();
          closeBatchModal();
        } else {
          console.error('工资发放失败:', response.data.message);
        }
        fetchEmployees();
      } catch (error) {
        console.error('批量发工资失败:', error.message);
        if (error.response) {
          console.error('响应数据:', error.response.data);
        }
      }
    };

    const resetForm = () => {
      employeeForm.employeeName = '';
      employeeForm.idCard = '';
      employeeForm.phoneNum = '';
      employeeForm.address = '';
      employeeForm.employeePosition = '';
      employeeForm.salary = '';
    };

    const toggleSelectAll = () => {
      if (selectAll.value) {
        selectedEmployees.value = employees.value.map(employee => employee.employeeId);
      } else {
        selectedEmployees.value = [];
      }
    };

    const changePage = (page) => {
      const pageNum = Number(page);
      if (pageNum >= 1 && pageNum <= totalPages.value) {
        currentPage.value = pageNum;
        pageInput.value = '';
      }
    };

    fetchEmployees();

    return {
      employees,
      filteredEmployees,
      isModalVisible,
      isBatchModalVisible,
      isEditing,
      employeeForm,
      positions,
      searchName,
      searchPosition,
      currentPage,
      totalPages,
      selectedEmployees,
      selectAll,
      pageInput,
      paginatedEmployees,
      showAddModal,
      showEditModal,
      closeModal,
      closeBatchModal,
      showBatchModal,
      addEmployee,
      updateEmployee,
      deleteEmployee,
      batchPaySalary,
      search,
      toggleSelectAll,
      changePage,
      notification,
    };
  }
};
</script>



<style scoped>
/* Container */
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #ffffff; /* 添加背景色 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
}

/* Search and Add Container */
.search-add-container {
  display: flex;
  gap: 8px; /* 减少元素之间的间距 */
  margin-bottom: 10px; /* 减少底部间距 */
  justify-content: flex-start; /* 将元素紧凑排列在左侧 */
  align-items: center; /* 垂直居中 */
}

.input-field {
  padding: 6px 10px; /* 减少输入框内边距，使其更紧凑 */
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #fff;
  font-size: 14px;
  transition: border-color 0.3s ease;
  width: 150px; /* 调整输入框宽度，保持紧凑 */
}

.input-field:focus {
  border-color: #007bff;
  outline: none;
}

.btn.blue {
  padding: 10px 15px;
  margin: 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  background-color: #97cfff;
  color: #0275d8;
  font-weight: bold;
}

.btn.yellow {
  padding: 10px 15px;
  margin: 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  background-color: #ffca7f;
  color: #cf8d31;
  font-weight: bold;
}

.btn.red {
  padding: 10px 15px;
  margin: 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  background-color: #f98691;
  color: rgb(247, 10, 10);
  font-weight: bold;
}

.btn.green {
  padding: 10px 15px;
  margin: 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  background-color: #81fa9d;
  color: #006b29;
  font-weight: bold;
}


.search-add-container button:last-child {
  margin-left: 10px; /* 让发工资按钮稍微远离其他按钮 */
}


/* Button Styles */


.close-button {
  position: absolute;
  top: 15px;
  right: 15px;
  background: transparent;
  border: none;
  cursor: pointer;
  width: 30px; /* 设置按钮的宽度 */
  height: 30px; /* 设置按钮的高度 */
  padding: 0; /* 去除内边距 */
  transition: transform 0.3s ease, filter 0.3s ease; /* 添加过渡效果 */
}

/* 设置关闭图标的样式 */
.close-icon {
  width: 100%; /* 图像宽度适应按钮 */
  height: 100%; /* 图像高度适应按钮 */
  object-fit: contain; /* 保持图像的纵横比 */
}
.close-button:hover .close-icon {
  transform: scale(1.1); /* 悬停时放大 */
  filter: brightness(1.2); /* 悬停时增加亮度 */
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
/* Modal Overlay */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5); /* 背景遮罩 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000; /* 确保模态框位于最前 */
}

/* Modal Content */
.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  max-width: 600px; /* 模态框的最大宽度 */
  width: 100%;
  max-height: 80vh; /* 模态框的最大高度 */
  overflow-y: auto; /* 启用垂直滚动条 */
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3); /* 深度阴影 */
  position: relative;
  animation: fadeIn 0.3s ease-in-out; /* 弹出动画 */
  padding-bottom: 60px; /* 为表单底部留出空间 */
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

/* Close Button */
.close-button {
  position: absolute;
  top: 15px;
  right: 15px;
  background: #dc3545;
  color: white;
  border: none;
  font-size: 18px;
  cursor: pointer;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s ease;
}

.close-button:hover {
  background: #c82333;
}

/* Modal Title */
.modal-content h3 {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: bold;
}

/* Table within Modal */
.modal-content table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.modal-content th,
.modal-content td {
  padding: 12px;
  border: 1px solid #ddd;
  text-align: left;
}

.modal-content th {
  background-color: #007bff;
  color: white;
  text-transform: uppercase;
  font-weight: bold;
}

.modal-content tbody tr:nth-child(even) {
  background-color: #f9f9f9; /* 斑马条纹效果 */
}

.modal-content tbody tr:hover {
  background-color: #f1f1f1; /* 鼠标悬停时的背景颜色 */
}


/* Form Group within Modal */
.form-group {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.form-group input[type="checkbox"] {
  margin-right: 10px;
}

.form-group label {
  margin: 0;
  font-weight: bold;
  color: #555;
}

.form-group select {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f9f9f9;
  font-size: 14px;
  transition: border-color 0.3s ease;
}

.form-group input:focus,
.form-group select:focus {
  border-color: #007bff;
  background-color: #fff;
}

/* Right-aligned confirm button */
.confirm-button {
  position: absolute;
  width: 15%;
  bottom: 10px;
  right: 10px;
}

/* Pagination */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.pagination button {
  margin: 0 5px;
}

.notification {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  padding: 10px 20px;
  border-radius: 5px;
  background-color: #333;
  color: #fff;
  opacity: 0.9;
  z-index: 1001;
}

.notification.info {
  background-color: #007bff;
}

.notification.success {
  background-color: #28a745;
}

.notification.error {
  background-color: #dc3545;
}
.message-popup {
  position: absolute;
  top: -40px; /* 相对于模态框标题的上方 */
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.6);
  color: rgb(255, 255, 255);
  padding: 10px 20px;
  border-radius: 5px;
  z-index: 999;
  opacity: 1;
  transition: opacity 0.5s ease-in-out;
  font-size: 12px;
}

/* 新增过渡效果 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
select,input {
  border: 2px solid black;
}
</style>
