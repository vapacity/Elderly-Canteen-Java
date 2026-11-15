<template>
  <transition name="fade">
      <div v-if="showMessage" class="message-popup">{{ showMessage }}</div>
    </transition>
  <div class="admin-list">
    <h2>管理员列表</h2>
    <div class="search-bar">
      <input type="text" placeholder="昵称" v-model="searchName" />
      <select v-model="searchPosition">
        <option value="">职位</option>
        <option value="总管理">总管理</option>
        <option value="菜品">菜品</option>
        <option value="HR">HR</option>
        <option value="财务">财务</option>
      </select>
      <button @click="search" class="btn blue">搜索</button>
      <button @click="openAddAdmin" class="btn green">添加管理员</button>
    </div>

    <table class="table">
      <thead>
      <tr>
        <th>ID</th>
        <th>姓名</th>
        <th>手机号</th>
        <th>职位</th>
        <th>性别</th>
        <th>详细信息</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="i in itemsPerPage" :key="i">
        <td v-if="translatedAdmins[i - 1]">{{ translatedAdmins[i - 1].accountId || '暂无' }}</td>
        <td v-else-if="translatedAdmins[i - 1] === undefined"></td>

        <td v-if="translatedAdmins[i - 1]">{{ translatedAdmins[i - 1].name || '暂无' }}</td>
        <td v-else-if="translatedAdmins[i - 1] === undefined"></td>

        <td v-if="translatedAdmins[i - 1]">{{ translatedAdmins[i - 1].phoneNum || '暂无' }}</td>
        <td v-else-if="translatedAdmins[i - 1] === undefined"></td>

        <td v-if="translatedAdmins[i - 1]">{{ translatedAdmins[i - 1].position || '暂无' }}</td>
        <td v-else-if="translatedAdmins[i - 1] === undefined"></td>

        <td v-if="translatedAdmins[i - 1]">{{ translatedAdmins[i - 1].genderDisplay || '暂无' }}</td>
        <td v-else-if="translatedAdmins[i - 1] === undefined"></td>

        <td>
          <button v-if="translatedAdmins[i - 1]" class="btn blue" @click="viewDetails(translatedAdmins[i - 1])">查看</button>
        </td>
        <td>
          <button v-if="translatedAdmins[i - 1]" class="btn yellow" @click="confirmResetPassword(translatedAdmins[i - 1])">重置密码</button>
          <button v-if="translatedAdmins[i - 1]" class="btn blue" @click="editAdmin(translatedAdmins[i - 1])">修改</button>
          <button v-if="translatedAdmins[i - 1]" class="btn red" @click="deleteAdmin(translatedAdmins[i - 1].accountId)">删除</button>
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

    <!-- 详细信息弹出框 -->
    <div v-if="showDetail" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeDetail">&times;</span>
        <h3>详细信息</h3>
        <p><strong>姓名：</strong> {{ selectedAdmin.name || '暂无' }}</p>
        <p><strong>身份证：</strong> {{ selectedAdmin.idCard || '暂无' }}</p>
        <p><strong>生日：</strong> {{ selectedAdmin.birthDate || '暂无' }}</p>
        <p><strong>地址：</strong> {{ selectedAdmin.address || '暂无' }}</p>
        <p><strong>邮箱：</strong> {{ selectedAdmin.email || '暂无' }}</p>
      </div>
    </div>

    <!-- 添加管理员弹出框 -->
    <div v-if="showAddAdmin" class="modal">
      <div class="modal-content form-container">
        <span class="close" @click="closeAddAdmin">&times;</span>
        <h3>添加管理员</h3>
        <form @submit.prevent="confirmAddAdmin">
          <div class="form-group">
            <label for="newName">姓名</label>
            <input type="text" id="newName" v-model="newAdmin.name" class="input-field" required />
          </div>
          <div class="form-group">
            <label for="newPhone">手机号</label>
            <input type="text" id="newPhone" v-model="newAdmin.phoneNum" class="input-field" required />
          </div>
          <div class="form-group">
            <label for="newGender">性别</label>
            <select id="newGender" v-model="newAdmin.gender" class="input-field" required>
              <option value="male">男</option>
              <option value="female">女</option>
            </select>
          </div>
          <div class="form-group">
            <label for="newPosition">职位</label>
            <select id="newPosition" v-model="newAdmin.position" class="input-field" required>
              <option value="财务">财务</option>
              <option value="职工">职工</option>
              <option value="仓库">仓库</option>
              <option value="用户">用户</option>
              <option value="总管理">总管理</option>
            </select>
          </div>
          <div class="form-group">
            <label for="newIdCard">身份证</label>
            <input type="text" id="newIdCard" v-model="newAdmin.idCard" class="input-field" required />
          </div>
          <div class="form-group">
            <label for="newBirthDate">生日</label>
            <input type="date" id="newBirthDate" v-model="newAdmin.birthDate" class="input-field" />
          </div>
          <div class="form-group">
            <label for="newAddress">地址</label>
            <input type="text" id="newAddress" v-model="newAdmin.address" class="input-field" />
          </div>
          <div class="form-group">
            <label for="newEmail">邮箱</label>
            <input type="email" id="newEmail" v-model="newAdmin.email" class="input-field" />
          </div>
          <div class="button-group">
            <button type="submit" class="btn green">确认</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 修改管理员信息弹出框 -->
    <div v-if="showEditAdmin" class="modal">
      <div class="modal-content form-container">
        <span class="close" @click="closeEditAdmin">&times;</span>
        <h3>修改管理员信息</h3>
        <form @submit.prevent="confirmEditAdmin">
          <div class="form-group">
            <label for="editPhone">手机号</label>
            <input type="text" id="editPhone" v-model="selectedAdmin.phoneNum" class="input-field" />
          </div>
          <div class="form-group">
            <label for="editGender">性别</label>
            <select id="editGender" v-model="selectedAdmin.gender" class="input-field">
              <option value="男">男</option>
              <option value="女">女</option>
            </select>
          </div>
          <div class="form-group">
            <label for="editPosition">职位</label>
            <select id="editPosition" v-model="selectedAdmin.position" class="input-field">
              <option value="财务">财务</option>
              <option value="职工">职工</option>
              <option value="仓库">仓库</option>
              <option value="用户">用户</option>
              <option value="总管理">总管理</option>
            </select>
          </div>
          <div class="button-group">
            <button type="submit" class="btn green">确认</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 确认重置密码弹出框 -->
    <div v-if="showResetPasswordConfirm" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeResetPasswordConfirm">&times;</span>
        <h3>确认重置密码</h3>
        <p>确定要重置密码吗？</p>
        <button @click="resetPassword(selectedAdmin)" class="btn green">确认</button>
        <button @click="closeResetPasswordConfirm" class="btn red">取消</button>
      </div>
    </div>

    <!-- 重置密码显示弹出框 -->
    <div v-if="showResetPassword" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeResetPassword">&times;</span>
        <h3>重置密码</h3>
        <p>已重置密码！</p>
        <div class="reset-password-container">
          <input type="text" v-model="newPassword" readonly />
          <button @click="copyPassword" class="btn blue">点击复制</button>
        </div>
        <button @click="closeResetPassword" class="btn green">确认</button>
      </div>
    </div>

  </div>
</template>

<script>
import axios from 'axios'; // 引入Axios

export default {
  name: 'AdminList',
  data() {
    return {
      searchName: '',
      searchPosition: '',
      admins: [],
      filteredAdmins: [],
      currentPage: 1,
      itemsPerPage: 10,
      showDetail: false,
      showAddAdmin: false,
      showEditAdmin: false,
      showResetPassword: false,
      showResetPasswordConfirm: false,
      selectedAdmin: {},
      newAdmin: {
        name: '',
        phoneNum: '',
        gender: 'male', // 默认值改为 male 以与后端一致
        position: '财务',
        idCard: '',
        birthDate: '',
        address: '',
        email: ''
      },
      newPassword: '',
      showMessage: "", // 控制提示信息弹窗
      pageInput: '', // 页码输入

    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.filteredAdmins.length / this.itemsPerPage);
    },
    paginatedAdmins() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredAdmins.slice(start, end);
    },
    translatedAdmins() {
      return this.paginatedAdmins.map(admin => {
        return {
          ...admin,
          genderDisplay: admin.gender === 'male' ? '男' : admin.gender === 'female' ? '女' : '暂无'
        };
      });
    }
  },
  mounted() {
    this.fetchAdmins(); // 组件挂载时获取管理员列表
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
    fetchAdmins() {
      const params = {};
      if (this.searchName) params.name = this.searchName;
      if (this.searchPosition) params.position = this.searchPosition;
      const token = localStorage.getItem("token"); // 获取存储的 token
      axios.get('/demo-api/admin/search', {
        headers: {
          'Authorization': `Bearer ${token}` // headers在第二个参数
        },
        params
      })

          .then(response => {
            if (response.data && response.data.success) {
              this.admins = response.data.response || [];
              this.filteredAdmins = this.admins;
              this.currentPage = 1; // 重置到第一页
            } else {
              this.showError('API返回错误: ' + response.data.msg);
            }
          })
          .catch(() => { // 去掉未使用的 error 参数
            this.showError('获取管理员列表时出错');
          });
    },
    search() {
      this.fetchAdmins(); // 根据搜索条件获取管理员
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    },
    goToPage(page) {
      this.currentPage = page;
    },
    viewDetails(admin) {
      const adminId = admin.accountId;
      const token = localStorage.getItem("token"); // 获取存储的 token
      axios.get(`/demo-api/admin/${adminId}`, {
            headers: {
              'Authorization': `Bearer ${token}` // 添加 token 到请求头
            }
          })
          .then(response => {
            if (response.data && response.data.success) {
              this.selectedAdmin = response.data.response[0];
              this.showDetail = true;
            } else {
              this.showError('获取管理员详细信息失败: ' + response.data.msg);
            }
          })
          .catch(() => { // 去掉未使用的 error 参数
            this.showError('获取管理员详细信息时出错，请稍后重试');
          });
    },
    closeDetail() {
      this.showDetail = false;
    },
    openAddAdmin() {
      this.showAddAdmin = true;
    },
    closeAddAdmin() {
      this.showAddAdmin = false;
    },
    confirmAddAdmin() {
      // 验证手机号和身份证号
      if (this.newAdmin.phoneNum.length !== 11) {
        this.showError('手机号必须为11位，请重新输入');
        return;
      }
      if (this.newAdmin.idCard.length !== 18) {
        this.showError('身份证号必须为18位，请重新输入');
        return;
      }
      const token = localStorage.getItem("token"); // 获取存储的 token
      axios.post('/demo-api/admin/add', this.newAdmin, {
        headers: {
          'Authorization': `Bearer ${token}` // headers在第三个参数
        }
      })

          .then(response => {
            if (response.data && response.data.success) {
              this.showSuccess('管理员添加成功');
              this.fetchAdmins();
              this.closeAddAdmin();
              this.resetNewAdmin();
            } else {
              this.showError('添加管理员失败: ' + response.data.msg);
            }
          })
          .catch(() => { // 去掉未使用的 error 参数
            this.showError('添加管理员时出错，请稍后重试');
          });
    },

    resetNewAdmin() {
      this.newAdmin = {
        name: '',
        phoneNum: '',
        gender: 'male',
        position: '财务',
        idCard: '',
        birthDate: '',
        address: '',
        email: ''
      };
    },
    // 确认重置密码
    confirmResetPassword(admin) {
      this.selectedAdmin = admin;
      this.showResetPasswordConfirm = true; // 显示确认重置密码弹出框
    },
    // 重置密码
    resetPassword(admin) {
      const token = localStorage.getItem("token"); // 获取存储的 token
      axios.post(`/demo-api/users/resetpsd/${admin.accountId}`, {
            headers: {
              'Authorization': `Bearer ${token}` // 添加 token 到请求头
            }
          })
          .then(response => {
            if (response.data && response.data.success) {
              this.newPassword = response.data.password || ''; // 获取新的密码
              this.selectedAdmin = { ...admin, password: this.newPassword };
              this.showResetPasswordConfirm = false;
              this.showResetPassword = true; // 显示重置密码结果弹出框
            } else {
              this.showError('重置密码失败: ' + response.data.msg);
            }
          })
          .catch(() => { // 去掉未使用的 error 参数
            this.showError('重置密码失败，请稍后重试');
          });
    },
    // 关闭确认重置密码弹出框
    closeResetPasswordConfirm() {
      this.showResetPasswordConfirm = false;
    },
    // 关闭重置密码结果弹出框
    closeResetPassword() {
      this.showResetPassword = false;
    },
    // 复制新密码
    copyPassword() {
      const copyText = this.newPassword;
      navigator.clipboard.writeText(copyText).then(() => {
        this.showSuccess('密码已复制到剪贴板');
      });
    },
    editAdmin(admin) {
      this.selectedAdmin = { ...admin };
      this.showEditAdmin = true;
    },
    closeEditAdmin() {
      this.showEditAdmin = false;
    },
    confirmEditAdmin() {
      // 验证手机号
      if (this.selectedAdmin.phoneNum.length !== 11) {
        this.showError('手机号必须为11位，请重新输入');
        return;
      }

      const adminId = this.selectedAdmin.accountId;
      const genderValue = this.selectedAdmin.gender === '男' ? 'male' : 'female';
      const updateData = {
        phoneNum: this.selectedAdmin.phoneNum,
        position: this.selectedAdmin.position,
        gender: genderValue
      };
      const token = localStorage.getItem("token"); // 获取存储的 token
      axios.put(`/demo-api/admin/${adminId}`, updateData, {
        headers: {
          'Authorization': `Bearer ${token}` // headers在第三个参数
        }
      })

          .then(response => {
            if (response.data && response.data.success) {
              this.showSuccess('管理员信息更新成功');
              this.fetchAdmins();
              this.closeEditAdmin();
            } else {
              this.showError('更新管理员信息失败: ' + response.data.msg);
            }
          })
          .catch(() => { // 去掉未使用的 error 参数
            this.showError('更新管理员信息时出错，请稍后重试');
          });
    },
    deleteAdmin(adminId) {
      const token = localStorage.getItem("token"); // 获取存储的 token
      axios.delete(`/demo-api/admin/${adminId}`, {
        headers: {
          'Authorization': `Bearer ${token}` // headers在第二个参数
        }
      })

          .then(response => {
            if (response.data && response.data.success) {
              this.showSuccess(response.data.msg || '删除成功');
              this.fetchAdmins(); // 刷新管理员列表
            } else {
              this.showError('删除管理员失败: ' + response.data.msg);
            }
          })
          .catch((error) => { 
            if (error.response) {
              if(error.response.data.msg){
                this.showError(error.response.data.msg);
              }
            }else{
              this.showError('删除管理员时出错，请稍后重试');              
            }

          });
    },
    showError(message) {
      console.log('Error Message:', message); // Debugging line
      this.showMessage = message;
      setTimeout(() => {
        this.showMessage = "";
      }, 3000); // 错误信息3秒后消失
    },
    showSuccess(message) {
      this.showMessage = message;
      setTimeout(() => {
        this.showMessage = "";
      }, 2000); // 成功信息2秒后消失
    }
  }
};
</script>

<style scoped>
/* 样式代码保持不变 */
.admin-list {
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

.btn {
  padding: 10px 15px;
  margin: 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
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

.center-button {
  display: block;
  margin: 20px auto;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination button,
.pagination span {
  margin: 0 5px;
  cursor: pointer;
}

.pagination .active {
  font-weight: bold;
  text-decoration: underline;
}

.modal {
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
  position: relative;
  width: 400px;
}

.form-container {
  width: 300px;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.form-group label {
  width: 80px;
}

.form-group .input-field {
  width: 100%;
  padding: 5px;
  box-sizing: border-box;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.close {
  position: absolute;
  top: 10px;
  right: 10px;
  color: #aaa;
  font-size: 24px;
  font-weight: bold;
  cursor: pointer;
}

.reset-password-container {
  display: flex;
  align-items: center;
}

.reset-password-container input {
  flex: 1;
  margin-right: 10px;
}

.message-popup {
  position: fixed;
  top: 60px; 
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
.fade-enter, .fade-leave-to {
  opacity: 0;
}
select,input {
  border: 2px solid black;
}
</style>
