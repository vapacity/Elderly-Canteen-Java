<template>
  <div class="announcement-management">
    <h2>公告管理</h2>
    <div class="search-bar">
      <input type="text" placeholder="标题" v-model="searchTitle" />
      <select v-model="searchDate">
        <option value="">日期</option>
        <option value="2024-01-01">2024-01-01</option>
        <option value="2024-01-02">2024-01-02</option>
      </select>
      <button class="am search" @click="search">搜索</button>
      <button class="am add" @click="showAddAnnouncementModal">添加</button>
      <button class="am draft1" @click="showDraftModal">草稿</button>
    </div>
    <table class="table">
      <thead>
      <tr>
        <th style="width: 25%">标题</th>
        <th style="width: 25%">发布人</th>
        <th style="width: 25%">时间</th>
        <th style="width: 25%">操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="announcement in paginatedAnnouncements" :key="announcement.id">
        <td><img src="bell-icon.png" alt="icon">{{ announcement.title }}</td>
        <td>{{ announcement.author }}</td>
        <td>{{ announcement.date }}</td>
        <td style="text-align:center;"><button class="am view" @click="viewAnnouncement(announcement)">查看内容</button></td>
      </tr>
      </tbody>
    </table>
    <!-- 分页 -->
    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1" class="button orange">上一页</button>
      <span>{{ currentPage }} / {{ totalPages }}</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages" class="button orange">下一页</button>
      <input v-model="pageInput" type="number" min="1" :max="totalPages" placeholder="跳转" class="input-field" />
      <button @click="changePage(pageInput)" :disabled="!pageInput || pageInput < 1 || pageInput > totalPages" class="button orange">跳转</button>
    </div>

    <!-- 添加公告的模态框 -->
    <div v-if="showAddModal" class="modal">
      <div class="modal-content">
        <h3>添加公告</h3>
        <input type="text" v-model="newAnnouncement.title" placeholder="标题" />
        <input type="text" v-model="newAnnouncement.author" placeholder="发布人" />
        <input type="date" v-model="newAnnouncement.date" placeholder="日期" />
        <textarea v-model="newAnnouncement.content" placeholder="内容"></textarea>
        <button @click="addAnnouncement">保存</button>
        <button @click="closeAddAnnouncementModal">取消</button>
      </div>
    </div>

    <!-- 草稿模态框 -->
    <div v-if="showDraftModalFlag" class="modal">
      <div class="modal-content">
        <h3>草稿</h3>
        <input type="text" v-model="newAnnouncement.title" placeholder="标题" />
        <input type="text" v-model="newAnnouncement.author" placeholder="发布人" />
        <input type="date" v-model="newAnnouncement.date" placeholder="日期" />
        <textarea v-model="newAnnouncement.content" placeholder="内容"></textarea>
        <button @click="saveDraft">保存草稿</button>
        <button @click="closeDraftModal">取消</button>
      </div>
    </div>

    <!-- 查看公告内容的模态框 -->
    <div v-if="showViewModal" class="modal">
      <div class="modal-content">
        <h3>公告内容</h3>
        <p><strong>标题:</strong> {{ selectedAnnouncement.title }}</p>
        <p><strong>发布人:</strong> {{ selectedAnnouncement.author }}</p>
        <p><strong>日期:</strong> {{ selectedAnnouncement.date }}</p>
        <p><strong>内容:</strong> {{ selectedAnnouncement.content }}</p>
        <button @click="closeViewModal">关闭</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AnnouncementManagement',
  data() {
    return {
      searchTitle: '',
      searchDate: '',
      announcements: [
        { id: 1, title: '公告1', author: '发布人A', date: '2024-01-01', content: '这是公告1的内容。' },
        { id: 2, title: '公告2', author: '发布人B', date: '2024-01-02', content: '这是公告2的内容。' },
        { id: 3, title: '公告1', author: '发布人A', date: '2024-01-01', content: '这是公告1的内容。' },
        { id: 4, title: '公告2', author: '发布人B', date: '2024-01-02', content: '这是公告2的内容。' },
        { id: 5, title: '公告1', author: '发布人A', date: '2024-01-01', content: '这是公告1的内容。' },
        { id: 6, title: '公告2', author: '发布人B', date: '2024-01-02', content: '这是公告2的内容。' },
        { id: 7, title: '公告1', author: '发布人A', date: '2024-01-01', content: '这是公告1的内容。' },
        { id: 8, title: '公告2', author: '发布人B', date: '2024-01-02', content: '这是公告2的内容。' },
        { id: 9, title: '公告1', author: '发布人A', date: '2024-01-01', content: '这是公告1的内容。' },
        { id: 10, title: '公告2', author: '发布人B', date: '2024-01-02', content: '这是公告2的内容。' },
        { id: 11, title: '公告1', author: '发布人A', date: '2024-01-01', content: '这是公告1的内容。' },
        { id: 12, title: '公告2', author: '发布人B', date: '2024-01-02', content: '这是公告2的内容。' },

      ],
      newAnnouncement: {
        title: '',
        author: '',
        date: '',
        content: '',
      },
      selectedAnnouncement: {
        title: '',
        author: '',
        date: '',
        content: '',
      },
      currentPage: 1,
      itemsPerPage: 5,
      showAddModal: false,
      showDraftModalFlag: false,
      showViewModal: false,
      pageInput: '', // 页码输入
    };
  },
  computed: {
    // 计算总页数
    totalPages() {
      return Math.ceil(this.announcements.length / this.itemsPerPage);
    },
    // 获取当前页的公告
    paginatedAnnouncements() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.announcements.slice(start, end);
    }
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
    search() {
      // 搜索逻辑
    },
    showAddAnnouncementModal() {
      this.showAddModal = true;
    },
    closeAddAnnouncementModal() {
      this.showAddModal = false;
      this.newAnnouncement = { title: '', author: '', date: '', content: '' };
    },
    addAnnouncement() {
      if (this.newAnnouncement.title && this.newAnnouncement.author && this.newAnnouncement.date && this.newAnnouncement.content) {
        this.announcements.push({
          id: this.announcements.length + 1,
          ...this.newAnnouncement
        });
        this.closeAddAnnouncementModal();
      } else {
        // alert('请填写所有字段');
      }
    },
    showDraftModal() {
      this.showDraftModalFlag = true;
    },
    closeDraftModal() {
      this.showDraftModalFlag = false;
      this.newAnnouncement = { title: '', author: '', date: '', content: '' };
    },
    saveDraft() {
      console.log('草稿保存成功: ', this.newAnnouncement);
      this.closeDraftModal();
    },
    viewAnnouncement(announcement) {
      this.selectedAnnouncement = announcement;
      this.showViewModal = true;
    },
    closeViewModal() {
      this.showViewModal = false;
    }
  }
};
</script>

<style scoped>
.announcement-management {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #ffffff; /* 添加背景色 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
}

.search-bar {
  background-color: rgba(225, 217, 208, 0.5);
}

.am {
  padding: 10px 15px;
  margin: 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.am.search {
  background-color: #97cfff;
  color: #0275d8;
  font-weight: bold;
}

.am.add {
  background-color: #98de98;
  color: #3aa13a;
  font-weight: bold;
}

.am.draft1 {
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

.am.draft1:hover{
  background-color: #ffca7f;
}

.am.view {
  background-color: #ffca7f;
  color: #cf8d31;
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
.table td img {
  width: 20px;
  margin-right: 10px;
}

.pagination button {
  margin: 0 5px;
  padding: 3px;
  border: none;
  border-radius: 2px;
  cursor: pointer;
  border: 2px solid rgb(103, 136, 246);
  background-color: white;
  color: rgb(103, 136, 246);
  font-size: 10px;
  font-weight: 400;
}

.pagination button:disabled {
  border: 2px solid rgb(223, 223, 223);
  background-color: white;
  color: rgb(223, 223, 223);
}
.pagination span {
  color: rgb(223, 223, 223);
}
.input-field {
  width: 50px;
  padding: 5px;
  font-size: 10px;
}


.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  width: 300px;
}

textarea {
  width: 100%;
  height: 100px;
  margin-top: 10px;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ddd;
  font-size: 14px;
}
select,input {
  border: 2px solid black;
}
</style>