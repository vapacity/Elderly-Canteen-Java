<template>
  <div class="user-list">
    <h2>用户列表</h2>
    <div class="search-bar">
      <input type="text" placeholder="昵称" v-model="searchName" />
      <select v-model="searchIdentity">
        <option value="">身份</option>
        <option value="user">普通用户</option>
        <option value="volunteer">志愿者</option>
        <option value="senior">老人</option>
      </select>
      <button @click="search" class="btn blue">搜索</button>
    </div>
    <div class="table-container">
      <table class="table">
        <thead>
        <tr>
          <th>ID</th>
          <th>昵称</th>
          <th>手机号</th>
          <th>身份</th>
          <th>性别</th>
          <th>详细信息</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-if="filteredUsers.length === 0">
          <td colspan="7" class="no-data">没有找到匹配的用户</td>
        </tr>
        <tr
            v-for="(user, index) in translatedUsers"
            :key="index"
            class="table-row"
        >
          <td>{{ user.accountId }}</td>
          <td>{{ user.accountName }}</td>
          <td>{{ user.phoneNum }}</td>
          <td>{{ user.identityDisplay }}</td>
          <td>{{ user.genderDisplay }}</td>
          <td>
            <button class="btn small blue" @click="viewDetail(user)">查看</button>
          </td>
          <td class="action-buttons">
            <button
                class="btn small yellow"
                @click="confirmResetPassword(user)"
            >
              重置密码
            </button>
            <button class="btn small blue" @click="editUser(user)">修改</button>
            <button
                class="btn small red"
                @click="deleteUser(user.accountId)"
            >
              删除
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
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
        <h3>用户信息</h3>
        <p><strong>姓名：</strong> {{ selectedUser.name || "空" }}</p>
        <p><strong>身份证号：</strong> {{ selectedUser.idCard || "空" }}</p>
        <p><strong>生日：</strong> {{ selectedUser.birthDate || "空" }}</p>
        <p><strong>地址：</strong> {{ selectedUser.address || "空" }}</p>
      </div>
    </div>

    <!-- 重置密码确认弹出框 -->
    <div v-if="showResetPasswordConfirm" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeResetPasswordConfirm">&times;</span>
        <h3>确认重置密码</h3>
        <p>确定要重置密码吗？</p>
        <button @click="resetPassword(selectedUser)" class="btn green">确认</button>
        <button @click="closeResetPasswordConfirm" class="btn red">取消</button>
      </div>
    </div>

    <!-- 重置密码弹出框 -->
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

    <!-- 修改用户信息弹出框 -->
    <div v-if="showEditUser" class="modal">
      <div class="modal-content">
        <!-- 将提示信息放在这里 -->
        <transition name="fade">
          <div v-if="showMessage" class="message-popup">{{ showMessage }}</div>
        </transition>

        <span class="close" @click="closeEditUser">&times;</span>
        <h3>修改用户信息</h3>
        <form @submit.prevent="confirmEditUser">
          <div class="form-group">
            <label for="editNickname">昵称</label>
            <input type="text" id="editNickname" v-model="selectedUser.accountName" />
          </div>
          <div class="form-group">
            <label for="editPhone">手机号</label>
            <input type="text" id="editPhone" v-model="selectedUser.phoneNum" />
          </div>
          <div class="form-group">
            <label for="editGender">性别</label>
            <select id="editGender" v-model="selectedUser.gender">
              <option value="male">男</option>
              <option value="female">女</option>
            </select>
          </div>
          <button type="submit" class="btn green">确认</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'UserList',
  data() {
    return {
      searchName: '',
      searchIdentity: '',
      users: [],
      filteredUsers: [],
      currentPage: 1,
      itemsPerPage: 10,
      pageInput: '', // 页码输入

      showDetail: false,
      showResetPasswordConfirm: false,
      showResetPassword: false,
      showEditUser: false,
      selectedUser: {},
      newPassword: '',
      showMessage: "", // 控制提示信息弹窗
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.filteredUsers.length / this.itemsPerPage);
    },
    paginatedUsers() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredUsers.slice(start, end);
    },
    translatedUsers() {
      return this.paginatedUsers.map(user => ({
        ...user,
        genderDisplay: user.gender === 'male' ? '男' : user.gender === 'female' ? '女' : '未知',
        identityDisplay: user.identity === 'user' ? '普通用户' : user.identity === 'volunteer' ? '志愿者' : '老人'
      }));
    }
  },
  mounted() {
    this.fetchUsers();
  },
  methods: {
    // 页码跳转逻辑
    changePage(page) {
      const pageNum = Number(page); // 将输入的页码转换为数字
      if (pageNum >= 1 && pageNum <= this.totalPages) {
        this.currentPage = pageNum; // 设置当前页码
        this.pageInput = ''; // 清空输入框
      } else {
        alert('请输入有效的页码'); // 添加简单的页码验证
      }
    },
    fetchUsers() {
      const token = localStorage.getItem("token"); // 获取存储的 token
      axios
          .get('/demo-api/users/search', {
            headers: {
              'Authorization': `Bearer ${token}` // 添加 token 到请求头
            }
          })
          .then((response) => {
            this.users = response.data.response || [];
            this.filteredUsers = this.users;
          })
          .catch(() => {
            this.showError('获取用户列表失败，无法获取用户数据，请稍后再试');
          });
    },
    search() {
      const token = localStorage.getItem("token"); // 获取存储的 token
      axios
          .get('/demo-api/users/search', {
            headers: {
              'Authorization': `Bearer ${token}` // 添加 token 到请求头
            },
            params: {
              accountName: this.searchName || undefined,
              identity: this.searchIdentity || undefined,
            },
          })
          .then((response) => {
            if (response.data.success) {
              this.filteredUsers = response.data.response.map(user => ({
                ...user,
                gender: user.gender || '未知'
              }));
              this.currentPage = 1;
            } else {
              this.showError('搜索用户失败：' + response.data.msg);
              this.filteredUsers = [];
            }
          })
          .catch(() => {
            this.showError('搜索用户失败，请求服务器出错');
          });
    },
    viewDetail(user) {
      if (!user) return;
      const token = localStorage.getItem("token"); // 获取存储的 token
      axios.get(`/demo-api/users/${user.accountId}`, {
            headers: {
              'Authorization': `Bearer ${token}` // 添加 token 到请求头
            }
          })
          .then(response => {
            if (response.data.success) {
              const userData = response.data.response[0];
              this.selectedUser = {
                name: userData.name || '空',
                idCard: userData.idCard || '空',
                birthDate: userData.birthDate || '空',
                address: userData.address || '空'
              };
              this.showDetail = true;
            } else {
              this.showError('获取用户详细信息失败：' + response.data.msg);
            }
          })
          .catch(() => {
            this.showError('获取用户详细信息失败，请求服务器出错');
          });
    },
    closeDetail() {
      this.showDetail = false;
    },
    confirmResetPassword(user) {
      this.selectedUser = user;
      this.showResetPasswordConfirm = true;
    },
    resetPassword(user) {
      const token = localStorage.getItem("token"); // 获取存储的 token
      axios.post(`/demo-api/users/resetpsd/${user.accountId}`, {
            headers: {
              'Authorization': `Bearer ${token}` // 添加 token 到请求头
            }
          })
          .then(response => {
            this.newPassword = response.data.password || '';
            this.selectedUser = { ...user, password: this.newPassword };
            this.showResetPasswordConfirm = false;
            this.showResetPassword = true;
          })
          .catch(() => {
            this.showError('重置密码失败，无法重置密码，请稍后再试');
          });
    },
    closeResetPasswordConfirm() {
      this.showResetPasswordConfirm = false;
    },
    closeResetPassword() {
      this.showResetPassword = false;
    },
    copyPassword() {
      const copyText = this.newPassword;
      navigator.clipboard.writeText(copyText).then(() => {
        this.showSuccess('密码已复制，密码已成功复制到剪贴板');
      });
    },
    editUser(user) {
      this.selectedUser = { ...user };
      this.showEditUser = true;
    },
    closeEditUser() {
      this.showEditUser = false;
    },
    confirmEditUser() {
      // 验证手机号长度
      if (this.selectedUser.phoneNum.length !== 11) {
        this.showError('手机号验证失败，手机号必须为11位，请重新输入');
        return;
      }

      const updatedUser = {
        ...this.selectedUser,
        gender: this.selectedUser.gender === '男' ? 'male' : 'female',
        identity: ''
      };
      const token = localStorage.getItem("token"); // 获取存储的 token
      axios.put(`/demo-api/users/${this.selectedUser.accountId}`, updatedUser,{
            headers: {
              'Authorization': `Bearer ${token}` // 添加 token 到请求头
            }
          })
          .then(() => {
            const index = this.users.findIndex(u => u.accountId === this.selectedUser.accountId);
            if (index !== -1) {
              this.users.splice(index, 1, this.selectedUser);
              this.filteredUsers = [...this.users];
            }
            this.showEditUser = false;
          })
          .catch(() => {
            this.showError('修改用户信息失败，无法修改用户信息，请稍后再试');
          });
    },
    deleteUser(userId) {
      const token = localStorage.getItem("token"); // 获取存储的 token
      axios.delete(`/demo-api/users/${userId}`, {
            headers: {
              'Authorization': `Bearer ${token}` // 添加 token 到请求头
            }
          })
          .then(() => {
            this.users = this.users.filter(u => u.accountId !== userId);
            this.filteredUsers = this.users;
          })
          .catch(() => {
            this.showError('删除用户失败，无法删除用户，请稍后再试');
          });
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
    showError(message) {
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
    },
  }
};
</script>

<style scoped>
.user-list {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #ffffff; /* 添加背景色 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
}

.table-container {
  width: 100%;
  overflow-x: auto;
  margin: 0;
  padding: 0;
  min-height: 500px;  /* 根据需要调整，确保即使数据少时也有足够的空间 */
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

.no-data {
  text-align: center;
  padding: 20px;
  color: #999;
}

.btn {
  padding: 10px 15px;
  margin: 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn.small {
  padding: 5px;
  width: auto;
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
  background-color: #28a745;
  color: white;
}

.action-buttons {
  display: flex;
  gap: 1px; /* 调整按钮之间的间距 */
  justify-content: flex-start; /* 按钮靠左对齐 */
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  min-height: 50px;  /* 设置一个最小高度 */
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

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
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

.full-width {
  width: 100%;
}
/* 新增提示信息弹窗样式 */
.message-popup {
  position: absolute; /* 改为相对于父容器 */
  top: -30px; /* 调整位置到标题上方 */
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 5px 10px;
  border-radius: 5px;
  z-index: 10;
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
