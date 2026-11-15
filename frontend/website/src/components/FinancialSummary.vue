<template>
  <div class="financial-summary">
    <h2>财务总表</h2>
    <div class="search-bar">
      <select v-model="searchSource">
        <option value="">来源</option>
        <option value="捐赠">捐赠</option>
        <option value="进货">进货</option>
        <option value="工资">工资</option>
        <option value="点单">点单</option>
      </select>
      <select v-model="searchType">
        <option value="">收支</option>
        <option value="0">收入</option>
        <option value="1">支出</option>
      </select>
      <input type="date" v-model="searchDate" />
      <button class="btn blue" @click="search">搜索</button>
    </div>
    <div class="summary">
      <div class="summary-item">
        <label>净收入：</label>
        <input type="text" v-model="netIncome" readonly />
      </div>
      <div class="summary-item">
        <label>总收入：</label>
        <input type="text" v-model="totalIncome" readonly />
      </div>
      <div class="summary-item">
        <label>总支出：</label>
        <input type="text" v-model="totalExpense" readonly />
      </div>
    </div>
    <table class="table">
      <thead>
      <tr>
        <th>流水号</th>
        <th>来源</th>
        <th>申请人</th>
        <th>审核人</th>
        <th>金额</th>
        <th>时间</th>
        <th>收支</th>
        <th>审核状态</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="item in paginatedItems" :key="item.financeId" class="table-row">
        <td>{{ item.financeId }}</td>
        <td>{{ item.financeType }}</td>
        <td>{{ item.accountId }}</td>
        <td>{{ item.administratorId }}</td>
        <td>{{ item.price }}</td>
        <td>{{ item.financeDate }}</td>
        <td>{{ item.inOrOut === '0' ? '收入' : '支出' }}</td>
        <td>{{ item.status }}</td>
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
  </div>
</template>

<script>


export default {
  name: 'FinancialSummary',
  data() {
    return {
      searchSource: '',
      searchType: '',
      searchDate: '',
      pageInput: '', // 页码输入

      netIncome: 0,   // 新增净收入字段
      totalIncome: 0, // 新增总收入字段
      totalExpense: 0, // 新增总支出字段
      items: [],
      filteredItems: [],
      currentPage: 1,
      itemsPerPage: 8,
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.filteredItems.length / this.itemsPerPage);
    },
    paginatedItems() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredItems.slice(start, end);
    },
  },
  mounted() {
    this.fetchData(); // 初始化时获取数据
    this.fetchTotal(); // 初始化时获取总收入、总支出和净收入
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
    async fetchData() {
  try {
    const token = localStorage.getItem("token"); // 获取存储的 token
    const formattedDate = this.searchDate ? this.searchDate : '';
    const response = await fetch(`/demo-api/Finance/financial-records?financeType=${this.searchSource}&inOrOut=${this.searchType}&financeDate=${formattedDate}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`, // 添加 Authorization 头
        'Content-Type': 'application/json'  // 根据需要设置 Content-Type
      }
    });
    const data = await response.json();
    this.items = data.response || [];
    this.filteredItems = this.items;
  } catch (error) {
    console.error('数据获取错误:', error);
  }
},

async fetchTotal() {
  try {
    const token = localStorage.getItem("token"); // 获取存储的 token
    const response = await fetch(`/demo-api/Finance/getTotal`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`, // 添加 Authorization 头
        'Content-Type': 'application/json'  // 根据需要设置 Content-Type
      }
    });
    const data = await response.json();
    this.netIncome = data.response.netIn; // 设置净收入
    this.totalIncome = data.response.totalIn; // 设置总收入
    this.totalExpense = data.response.totalOut; // 设置总支出
  } catch (error) {
    console.error('获取总收入、总支出和净收入时发生错误:', error);
  }
},

    search() {
      this.fetchData(); // 通过API获取过滤后的数据
      this.fetchTotal(); // 同时获取最新的总收入、总支出和净收入
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
  },
};
</script>

<style scoped>
.financial-summary {
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

.search-bar select {
  margin-right: 10px;
}

.summary {
  display: flex;
  justify-content: space-around; /* 均匀分布 */
  margin-bottom: 10px;
}

.summary-item {
  display: flex;
  align-items: center;
}

.summary label {
  margin-right: 5px;
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
  padding: 10px 20px;
  margin: 2px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  width: 100px;
  text-align: center;
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
select,input {
  border: 2px solid black;
}
</style>
