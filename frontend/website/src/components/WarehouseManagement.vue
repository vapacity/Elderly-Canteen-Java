<template>
  <div class="warehouse-management">
    <transition name="fade">
      <div v-if="showMessage" class="message-popup">{{ showMessage }}</div>
    </transition>
    <h2>仓库管理</h2>
    <div class="search-bar">
      <input type="text" placeholder="食材名" v-model="searchName" />

      <select v-model="searchGrade">
        <option value="">高耗品等级</option>
        <option value="1">1</option>
        <option value="2">2</option>
        <option value="3">3</option>
        <option value="4">4</option>
        <option value="5">5</option>
      </select>

      <!-- 搜索按钮 -->
      <button @click="searchWarehouse" class="btn blue">搜索</button>
      <button @click="openRestockItem" class="btn green">进货</button>
      <button class="btn yellow" @click="addItem">添加</button>
    </div>
    <table class="table">
      <thead>
      <tr>
        <th>食材ID</th>
        <th>食材名</th>
        <th>数量</th>
        <th>高耗品等级</th>
        <th>保质期</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(item, index) in paginatedItems" :key="index" class="table-row">
        <td>{{ item.ingredientId }}</td>
        <td>{{ item.ingredientName }}</td>
        <td>{{ item.amount }}</td>
        <td>{{ item.grade }}</td>
        <td>{{ item.expiry }}</td>
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

    <!-- 添加食材弹出框 -->
    <div v-if="showAddItem" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeAddItem">&times;</span>
        <h3>添加食材</h3>
        <form @submit.prevent="confirmAddItem">
          <div class="form-group">
            <label for="newName">食材名</label>
            <input type="text" id="newName" v-model="newItem.name" required />
          </div>
          <button type="submit" class="btn green">确认</button>
        </form>
      </div>
    </div>

    <!-- 进货食材弹出框 -->
    <div v-if="showRestockItem" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeRestockItem">&times;</span>
        <h3>进货食材</h3>
        <form @submit.prevent="confirmRestockItem">
          <div class="form-group">
            <label for="restockIngredient">选择食材</label>
            <!-- 使用下拉框选择现有食材 -->
            <select id="restockIngredient" v-model="restockItem.ingredientId">
              <option v-for="ingredient in availableIngredients" :key="ingredient.ingredientId" :value="ingredient.ingredientId">
                {{ ingredient.ingredientName }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="restockAmount">数量</label>
            <input type="number" id="restockAmount" v-model="restockItem.amount" />
          </div>
          <div class="form-group">
            <label for="restockPrice">价格</label>
            <input type="number" id="restockPrice" v-model="restockItem.price" />
          </div>
          <div class="form-group">
            <label for="restockExpiry">保质期</label>
            <input type="date" id="restockExpiry" v-model="restockItem.expiry" />
          </div>
          <button type="submit" class="btn green">确认</button>
        </form>
      </div>
    </div>

    <!-- 修改食材信息弹出框 -->
    <div v-if="showEditItem" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeEditItem">&times;</span>
        <h3>修改食材信息</h3>
        <form @submit.prevent="confirmEditItem">
          <div class="form-group">
            <label for="editName">食材名</label>
            <input type="text" id="editName" v-model="selectedItem.ingredientName" />
          </div>
          <button type="submit" class="btn green">确认</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: 'WarehouseManagement',
  data() {
    return {
      showSearchItem: false,
      showRestockItem: false,
      searchName: '',
      searchID: '',
      searchGrade: '',
      searchExpiry: '',
      restockItem: {
        ingredientId: '',
        amount: 0,
        price: 0,
        expiry: ''
      },
      availableIngredients: [],
      items: [],
      pageInput: '', // 页码输入
      filteredItems: [],
      currentPage: 1,
      itemsPerPage: 8,
      showAddItem: false,
      showEditItem: false,
      selectedItem: {},
      newItem: { name: '', quantity: '', grade: '1', expiry: '2024-09-11' },
      showMessage: "",
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.filteredItems.length / this.itemsPerPage);
    },
    paginatedItems() {
      if (!Array.isArray(this.filteredItems)) {
        return [];
      }
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredItems.slice(start, end);
    }
  },
  mounted() {
    this.fetchWarehouseData(); // 初始化时从后端加载数据
    this.fetchAvailableIngredients(); // 获取可选的食材
  },
  methods: {

    filterItems() {
      this.filteredItems = this.items.filter(item => {
        const matchesName = !this.searchName || item.ingredientName.includes(this.searchName);
        const matchesGrade = !this.searchGrade || item.grade === this.searchGrade;
        return matchesName && matchesGrade;
      });
      this.currentPage = 1; // 重置到第一页
    },

    async fetchWarehouseData() {
      try {
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.get('/demo-api/repo/search', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (response.status === 200 && response.data.success) {
          this.items = Array.isArray(response.data.ingredients) ? response.data.ingredients : [];
          this.filteredItems = [...this.items];
        } else {
          console.error('加载仓库数据失败', response.data.message || response);
        }
      } catch (error) {
        console.error('请求失败', error.response ? error.response.data : error.message);
      }
    },

    async fetchAvailableIngredients() {
      try {
        console.log('正在获取可选的食材列表...'); // 日志：请求开始
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.get('/demo-api/ingredients/search', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        console.log('API响应:', response.data); // 日志：打印API响应数据

        if (response.data && response.data.success && Array.isArray(response.data.ingredients)) {
          this.availableIngredients = response.data.ingredients;
          console.log('成功获取到的食材列表:', this.availableIngredients); // 日志：打印成功获取的食材列表
        } else {
          console.warn('API返回的数据格式不正确或没有成功:', response.data);
          this.availableIngredients = []; // 设置为空数组，防止数据错误时出错
        }
      } catch (error) {
        console.error('获取可选食材列表时出错:', error);
      }
    },

    async restock() {
      try {
        // 确保所有必需的字段都存在
        if (!this.restockItem.ingredientId || !this.restockItem.amount || !this.restockItem.expiry || !this.restockItem.price) {
          console.error('进货信息不完整，请检查所有字段是否已填写');
          return;
        }

        // 克隆 restockItem 对象，确保它是普通对象而不是Vue的代理对象
        const requestPayload = JSON.parse(JSON.stringify(this.restockItem));

        console.log('正在发送进货请求，发送的请求体:', requestPayload);
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.post('/demo-api/repo/restock', requestPayload, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (response.status === 200 && response.data.success) {
          console.log('进货成功，当前食材列表将刷新');
          this.show("进货成功");
          // 进货成功后重新调用 fetchWarehouseData 方法以刷新界面
          await this.fetchWarehouseData();

          // 关闭进货对话框
          this.closeRestockItem();
        } else {
          console.error('进货失败', response.data.message || response);
        }
      } catch (error) {
        console.error('请求失败', error.response ? error.response.data : error.message);
        if (error.response) {
              if(error.response.data.message){
                this.show(error.response.data.message);
              }
              else if(error.response.data.msg){
                this.show(error.response.data.msg);
              }
            }
      }
    },
    show(message) {
      this.showMessage = message;
      setTimeout(() => {
        this.showMessage = "";
      }, 1000); // 错误信息3秒后消失
    },






    async searchWarehouse() {
      try {
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.get('/demo-api/repo/search/', {
          params: {
            name: this.searchName || undefined,
            id: this.searchID || undefined,
            grade: this.searchGrade || undefined,
            expiry: this.searchExpiry || undefined
          },
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (response.status === 200 && response.data.success) {
          this.filteredItems = Array.isArray(response.data.ingredients) ? response.data.ingredients : [];
          this.currentPage = 1;
        } else {
          console.error('搜索失败', response.data.message || response);
        }
      } catch (error) {
        console.error('请求失败', error.response ? error.response.data : error.message);
      }
    },

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

    async confirmAddItem() {
      // 将前端输入的数据映射到API请求体所需的字段名
      const requestPayload = {
        IngredientName: this.newItem.name, // 修改字段名为后端要求的IngredientName
        quantity: this.newItem.quantity,
        grade: this.newItem.grade,
        expiry: this.newItem.expiry
      };

      try {
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.post('/demo-api/ingredients/add', requestPayload, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (response.status === 200 && response.data.success) {
          console.log('食材添加成功');
          this.fetchWarehouseData();  // 刷新仓库数据
          this.closeAddItem();        // 关闭添加食材的弹出框
          this.resetNewItem();        // 重置表单
        } else {
          console.error('添加食材失败', response.data.message || response);
          this.show(response.data.message);
        }
      } catch (error) {
        console.error('请求失败', error.response ? error.response.data : error.message);
      }
    },


    resetNewItem() {
      this.newItem = { name: '', quantity: 0, grade: '1', expiry: '2024-09-11' };
    },

    editItem(item) {
      this.selectedItem = { ...item };
      this.showEditItem = true;
    },

    closeEditItem() {
      this.showEditItem = false;
    },

    async confirmEditItem() {
      try {
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.put('/demo-api/ingredients/update', this.selectedItem, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (response.status === 200 && response.data.success) {
          const index = this.items.findIndex(i => i.id === this.selectedItem.id);
          if (index !== -1) {
            this.items.splice(index, 1, this.selectedItem);
            this.filteredItems = [...this.items];
          }
          this.showEditItem = false;
        } else {
          console.error('修改食材失败', response.data.message || response);
        }
      } catch (error) {
        console.error('请求失败', error.response ? error.response.data : error.message);
      }
    },

    async deleteItem(itemId, expiry) {
      // 日志：检查传入的 itemId 和 expiry 参数
      console.log('正在尝试删除项目，传入的 itemId:', itemId);
      console.log('正在尝试删除项目，传入的 expiry:', expiry);

      // 检查 itemId 和 expiry 是否有效
      if (!itemId) {
        console.error('ID无效');
        return;
      }

      if (!expiry) {
        console.error('expiry无效');
        return;
      }

      try {
        // 构建新的 API URL，插入 itemId 和 expiry
        const url = `/demo-api/repo/delete/${itemId}/${expiry}`;
        console.log('发送的 DELETE 请求 URL:', url);
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.delete(url, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        console.log('收到的响应:', response);

        if (response.status === 200 && response.data.success) {
          console.log('删除成功，响应数据:', response.data);
          this.items = this.items.filter(i => i.ingredientId !== itemId);
          this.filteredItems = this.items;
        } else {
          console.error('删除失败，响应数据:', response.data.message || response);
        }
      } catch (error) {
        console.error('请求失败，错误信息:', error.response ? error.response.data : error.message);
        console.error('请求失败的配置信息:', error.config); // 输出请求的配置信息
      }
    },








    addItem() {
      this.showAddItem = true;
    },



    closeAddItem() {
      this.showAddItem = false;
    },

    openSearchItem() {
      this.showSearchItem = true;
    },

    closeSearchItem() {
      this.showSearchItem = false;
    },

    openRestockItem() {
      this.showRestockItem = true;
    },

    closeRestockItem() {
      this.showRestockItem = false;
    },

    confirmRestockItem() {
      this.restock();
      this.closeRestockItem();
    }
  }
};
</script>

<style scoped>
.warehouse-management {
  width: 100%;
  padding: 20px;
}

.search-bar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  background-color: rgba(225, 217, 208, 0.5);
}

.search-bar input,
.search-bar select {
  margin-right: 10px;
}

.table {
  width: 100%;
  border-collapse: collapse;
  background-color: #fff; /* 白色背景 */
  border-radius: 8px; /* 圆角 */
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

.btn.search {
  background-color: #4CAF50;
  color: white;
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
select,input {
  border: 2px solid black;
}
</style>
