<template>
  <div class="ManageVol">
    <h1>上任志愿者管理</h1>
    <div class="search-bar">
      <input v-model="searchName" placeholder="姓名" />
      <select v-model="searchCriteria">
        <option value="score">得分</option>
      </select>
      <button @click="search" class="btn blue">搜索</button>

    </div>
    <table class="table">
      <thead>
      <tr>
        <th>账号ID</th>
        <th>姓名</th>
        <th>身份证号</th>
        <th>电话</th>
        <th>接单次数</th>
        <th>得分</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <!-- 确保使用正确的数据源渲染表格 -->
      <tr v-for="volunteer in filteredVolunteers" :key="volunteer.accountId">
        <td>{{ volunteer.accountId }}</td>
        <td>{{ volunteer.name }}</td>
        <td>{{ volunteer.idCard }}</td>
        <td>{{ volunteer.phoneNum }}</td>
        <td>{{ volunteer.deliverCount }}</td>
        <td>{{ volunteer.score }}</td>
        <td>

            <button class="btn red" @click="deleteVolunteer(volunteer.accountId)">删除</button>
        </td>
      </tr>
      </tbody>
    </table>
    <!-- 编辑对话框 -->
  </div>
</template>


<script>
import axios from 'axios';

export default {
  data() {
    return {
      searchName: '',
      searchCriteria: 'score',
      volunteers: [],
      filteredVolunteers: [],
      showEditDialog: false,
      editMode: false,
      editVolunteerData: {
        accountId: null,
        name: '',
        idCard: '',
        phoneNum: '',
        deliverCount: 0,
        pageInput: '', // 页码输入
        score: 0
      }
    };
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
    async fetchVolunteers() {
      try {
        console.log('正在获取所有志愿者信息...');
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.get('/demo-api/Volunteer/getAll', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        console.log('获取的志愿者信息:', response.data);

        // 检查API返回的数据结构是否正确
        if (response.data && response.data.success && Array.isArray(response.data.response)) {
          this.volunteers = response.data.response;
          console.log('成功获取志愿者数据:', this.volunteers);
          this.search(); // 更新筛选后的志愿者列表
        } else {
          this.volunteers = []; // 如果数据格式不正确，设置为空数组
          console.warn('API返回的数据格式不正确:', response.data);
        }
      } catch (error) {
        console.error('获取志愿者信息时出错:', error);
      }
    },




    async deleteVolunteer(accountId) {
      try {
        console.log(`正在删除账号ID为 ${accountId} 的志愿者...`);
        const token = localStorage.getItem("token"); // 获取存储的 token
        await axios.delete(`/demo-api/Volunteer/del/${accountId}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        console.log('志愿者删除成功');

        // 从前端列表中移除被删除的志愿者
        this.volunteers = this.volunteers.filter(v => v.accountId !== accountId);
        this.filteredVolunteers = this.filteredVolunteers.filter(v => v.accountId !== accountId);
        console.log('删除后剩余的志愿者:', this.filteredVolunteers);

      } catch (error) {
        console.error('删除志愿者时出错:', error);
        alert('删除志愿者失败，请重试。');
      }
    },



    search() {
      console.log('筛选前的志愿者数据:', this.volunteers);

      // 对搜索条件进行检查和筛选
      this.filteredVolunteers = this.volunteers.filter(v => {
        // 检查志愿者的姓名是否包含在搜索名称中，如果搜索名称为空，则匹配所有志愿者
        const matchesName = this.searchName === '' || v.name.includes(this.searchName);

        // 检查得分是否符合筛选条件，如果搜索条件是得分并且v.score存在
        const matchesScore = this.searchCriteria !== 'score' || (v.score !== undefined && v.score !== null);

        // 返回符合条件的志愿者
        return matchesName && matchesScore;
      });

      console.log('筛选后的志愿者列表:', this.filteredVolunteers);
    },
  },
  mounted() {
    this.fetchVolunteers();
  }
};
</script>


<style scoped>
.ManageVol{
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #ffffff; /* 添加背景色 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
}
.search-bar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  background-color: rgba(225, 217, 208, 0.5);
}
.buttonSearch{
  padding: 10px 15px;
  margin: 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  color: white;
  background-color: #0275d8; /* 蓝色 */
}
.search-bar input,
.search-bar select {
  margin-right: 10px;
  padding: 5px;
}

.btn.blue {
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  background-color: #97cfff;
  color: #0275d8;
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
.button-group {
  display: flex;
  justify-content:center;
}

.dialog-content {
  position: relative;
}

.dialog-content h2 {
  margin-top: 0;
}

label {
  display: block;
  margin-bottom: 10px;
}

.dialog-actions {
  display: flex;
  justify-content: space-between;
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
</style>
