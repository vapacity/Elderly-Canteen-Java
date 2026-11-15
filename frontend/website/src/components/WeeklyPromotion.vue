<template>
  <div class="weekly-promotion">
    <transition name="fade">
      <div v-if="showMessage" class="message-popup">{{ showMessage }}</div>
    </transition>
    <div class="header">
      <h1>每周促销</h1>
      <div class="date-picker">
        <label for="promotion-date">
          <i class="calendar-icon"></i>
        </label>
        <input type="date" v-model="promotionDate" id="promotion-date" @change="fetchDishes" />
      </div>
      <button class="status" @click="openBatchManage">批量管理</button>
    </div>

    <!-- 批量管理对话框 -->
    <div v-if="showBatchManageDialog" class="batch-manage-dialog">
      <div class="dialog-content">
        <h3>批量管理</h3>
        <button class="close-button" @click="closeBatchManageDialog">
          <img src="close.png" alt="Close" class="icon" />
        </button>
        <div class="dish-selection">
          <div v-for="dish in dishes" :key="dish.id" class="dish-item">
            <input type="checkbox" v-model="selectedDishes" :value="dish.id" />
            <label>{{ dish.name }}</label>
          </div>
        </div>
        <div class="discount-section">
          <label for="discount">折扣率: </label>
          <input type="number" v-model="discount" min="0" max="100" step="1" />
          <label >% </label>
        </div>
        <button class="confirm-button" @click="applyDiscount">确认</button>
      </div>
    </div>

    <table class="promotion-table">
      <thead>
      <tr>
        <th>菜品</th>
        <th>类别</th>
        <th>原价</th>
        <th>现价</th>
        <th>折扣率</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(dish, index) in dishes" :key="dish.id">
        <td>{{ dish.name }}</td>
        <td>{{ dish.category }}</td>
        <td>
          {{ dish.originalPrice !== undefined ? dish.originalPrice.toFixed(2) : '暂无折扣' }} 元
        </td>
        <td>
          {{ dish.currentPrice !== undefined ? dish.currentPrice.toFixed(2) : '暂无折扣' }} 元
        </td>
        <td>
          <div v-if="editingIndex === index">
            <div>
              <input type="number" v-model="dish.discountRate" min="0" max="100" step="1" class="edit-input" />
              <label>%</label>
            </div>
            <div class="edit-buttons">
              <button @click="confirmEdit(dish.id)" class="confirm-button small">确认</button>
              <button @click="cancelEdit" class="cancel-button small">取消</button>
            </div>
          </div>
          <div v-else>
            {{ dish.discountRate ? dish.discountRate.toFixed(2) + '%' : '暂无折扣' }}
          </div>
        </td>

        <td>
          <button v-if="editingIndex !== index" @click="editPrice(index)" class="edit-button">修改</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>



<script>
import axios from 'axios';

export default {
  data() {
    return {
      promotionDate: this.getCurrentDate(),
      editingIndex: null,
      dishes: [],
      showBatchManageDialog: false,
      selectedDishes: [],
      discount: null,
      pageInput: '',
      menuDate: new Date().toISOString().split('T')[0],
      showMessage: "",
    };
  },
  methods: {
    getCurrentDate() {
      const today = new Date();
      const year = today.getFullYear();
      const month = String(today.getMonth() + 1).padStart(2, '0');
      const day = String(today.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },

    fetchDishes() {
      const selectedDate = new Date(this.promotionDate); // 使用用户选择的日期
      const dayOfWeek = selectedDate.getDay(); // 获取选择日期对应的星期几
      const dayMap = ['sun', 'mon', 'tue', 'wed', 'thu', 'fri', 'sat']; // 对应的API字段名

      const url = `/demo-api/menu?date=${selectedDate.toISOString().split('T')[0]}`; // 使用新 API 请求的 URL

      axios.get(url, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem("token")}`
        }
      })
          .then(response => {
            if (response.data) {
              const menuData = response.data;
              const dishesOfTheDay = menuData[dayMap[dayOfWeek]] || []; // 获取当天的菜品

              // 使用 Map 进行去重，Map 对象的 key 是菜品的 id
              const uniqueDishesMap = new Map();
              dishesOfTheDay.forEach(dish => {
                if (!uniqueDishesMap.has(dish.id)) {
                  uniqueDishesMap.set(dish.id, {
                    ...dish,
                    discountRate: ((dish.discountPrice) / dish.originalPrice) * 100, // 计算折扣率
                    currentPrice: dish.discountPrice // 现价直接使用后端返回的 discountPrice
                  });
                }
              });

              // 转换 Map 为数组
              this.dishes = Array.from(uniqueDishesMap.values());
            } else {
              console.error('获取菜单失败: 数据为空或格式不正确');
              this.show('获取菜单失败: 数据为空或格式不正确');
              this.clearWeeklyMenu();
            }
          })
          .catch(error => {
            console.error('Error fetching dishes:', error);
           if (error.response) {
              if(error.response.data.message){
                this.show(`获取菜品数据失败${error.response.data.message}`);
              }
              else if(error.response.data.msg){
                this.show(`获取菜品失败${error.response.data.msg}`);
              }
            }
            this.clearWeeklyMenu();
          });
    },

    show(message) {
      this.showMessage = message;
      setTimeout(() => {
        this.showMessage = "";
      }, 1000); // 错误信息3秒后消失
    },

    calculateCurrentPrice(index) {
      const dish = this.dishes[index];
      const discount = dish.discountRate / 100; // 确保折扣率为百分比形式
      dish.currentPrice = dish.originalPrice * (discount); // 根据折扣率和原价计算现价
    },

    editPrice(index) {
      this.editingIndex = index;
    },

    confirmEdit(dishId) {
      const dish = this.dishes[this.editingIndex];
      const discount = dish.discountRate / 100; // 将折扣率转换为小数形式
      const token = localStorage.getItem("token"); // 获取存储的 token
      // 发起 API 请求，将折扣率发送给后端
      axios.put('/demo-api/menu/uploadDiscount',
        {
          date: this.promotionDate,
          dishId: dishId,
          discount: discount // 发送折扣率为小数形式
        },
        {
          headers: {
            'Authorization': `Bearer ${token}`, // 替换为实际的 token
          }
        }
      )
          .then(response => {
            if (response.data) {
              const updatedDish = response.data; // 假设 API 返回更新后的菜品数据

              // 根据返回的数据更新前端状态
              this.dishes = this.dishes.map(d =>
                  d.id === dishId
                      ? { ...d, currentPrice: updatedDish.updatedPrice, discountRate: dish.discountRate }
                      : d
              );

              this.editingIndex = null; // 退出编辑模式
              console.log('折扣率:', dish.discountRate, '现价:', updatedDish.updatedPrice);
              this.show(`更新成功`);
            } else {
              console.error('更新失败：返回数据为空或格式不正确');
              this.show('更新失败：返回数据为空或格式不正确');
            }
          })
          .catch(error => {
            console.error('Error updating price:', error);
            if (error.response) {
                if(error.response.data.message){
                  this.show(`更新失败${error.response.data.message}`, 'error');
                }
                else if(error.response.data.msg){
                  this.show(`更新失败${error.response.data.msg}`, 'error');
                }
              }
          });
    },



    cancelEdit() {
      this.editingIndex = null;
      this.fetchDishes();
    },

    openBatchManage() {
      this.showBatchManageDialog = true;
      this.fetchDishes();
    },

    closeBatchManageDialog() {
      this.showBatchManageDialog = false;
    },

    applyDiscount() {
      if (this.selectedDishes.length === 0 || this.discount <= 0 || this.discount > 100) {
        this.show("请正确选择菜品并设置有效的折扣！");
        return;
      }

      // 将折扣率转换为小数形式
      const discountDecimal = this.discount / 100;

      const promises = this.selectedDishes.map(dishId => {
        const dish = this.dishes.find(d => d.id === dishId);
        if (dish) {
          // 更新每个菜品的折扣率和现价
          dish.discountRate = this.discount;
          dish.currentPrice = dish.originalPrice * (1 - discountDecimal);
          const token = localStorage.getItem("token"); // 获取存储的 token
          // 发起 API 请求，逐一发送更新请求
          return axios.put('/demo-api/menu/uploadDiscount', {
            date: this.promotionDate,
            dishId: dishId,
            discount: discountDecimal // 发送折扣率为小数形式
          },
          {
            headers: {
              'Authorization': `Bearer ${token}`, // 替换为实际的 token
            }
          }
        );
        }
        return Promise.resolve();
      });

      // 等待所有请求完成后刷新数据
      Promise.all(promises)
          .then(() => {
            this.closeBatchManageDialog();
            this.fetchDishes();
            this.show("折扣已成功应用！");
          })
          .catch(error => {
            console.error('Error applying discount:', error);
            if (error.response) {
                if(error.response.data.message){
                  this.show(`更新失败${error.response.data.message}`, 'error');
                }
                else if(error.response.data.msg){
                  this.show(`更新失败${error.response.data.msg}`, 'error');
                }
              }
          });
    }





  },
  mounted() {
    this.fetchDishes();
  }
}
</script>



<style scoped>
.weekly-promotion {
  font-family: Arial, sans-serif;
  padding: 20px;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.date-picker {
  display: flex;
  align-items: center;
}
.calendar-icon {
  margin-right: 10px;
  font-size: 18px;
}
.status {
  border: 2px solid rgb(103, 136, 246);
  background-color: white;
  color: rgb(103, 136, 246);
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-weight: bold;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s ease;
}
.status:hover {
  border: 2px solid rgb(103, 136, 246);
  background-color: white;
  color: rgb(103, 136, 246);
}
.promotion-table {
  width: 100%;
  border-collapse: collapse;
  background-color: #fff; /* 白色背景 */
  overflow: hidden; /* 隐藏溢出 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
}
.promotion-table th,
.promotion-table td {
  padding: 15px;
  border: 1px solid rgba(225, 217, 208, 0.5);
  text-align: left;
  font-size: 14px;
}
.promotion-table th {
  background-color: rgba(225, 217, 208, 0.5);
  color: rgb(0, 0, 0);
  text-transform: uppercase;
  font-weight: bold;
}
.promotion-table tbody tr:nth-child(even) {
  background-color: #f9f9f9; /* 斑马条纹效果 */
}

.promotion-table tbody tr:hover {
  background-color: #f1f1f1; /* 鼠标悬停时的背景颜色 */
}
.promotion-table td button {
  border: 2px solid rgb(103, 136, 246);
  background-color: white;
  color: rgb(103, 136, 246);
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
  transition: background-color 0.3s;
  display: block;
  margin: 0 auto;
}

.promotion-table td input.edit-input {
  width: 80px;
  padding: 4px;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 10px;
}
.confirm-button.small,
.cancel-button.small {
  width: 60px;
  padding: 6px;
  font-size: 14px;
  text-align: center;
  display: inline-block;
  border-radius: 4px;
}
.confirm-button {
  background-color: #28a745;
}

.cancel-button {
  background-color: #dc3545;
}

.edit-buttons {
  display: block;
  justify-content: center;
}

/* 批量管理对话框样式 */
.batch-manage-dialog {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  border: 1px solid #ccc;
  padding: 30px; /* 增加内边距 */
  z-index: 1000;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  border-radius: 10px; /* 增加边框圆角 */
  width: 500px; /* 调整对话框宽度 */
  max-width: 90%; /* 设置最大宽度 */
  display: flex;
  flex-direction: column;
  align-items: center; /* 居中对齐内容 */
}

.dialog-content {
  position: relative;
  width: 100%; /* 让内容宽度与父容器对齐 */
}

.close-button {
  background-color: transparent;
  border: none;
  cursor: pointer;
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-button .icon {
  width: 24px; /* 调整关闭图标大小 */
  height: 24px;
  transition: transform 0.3s ease;
}

.close-button:hover .icon {
  transform: scale(1.2);
}

.dish-selection {
  display: flex;
  flex-direction: column;
  margin-top: 20px;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 8px;
  border: 1px solid #d3d3d3;
  width: 100%; /* 让选择框填满对话框 */
  max-height: 200px; /* 限制最大高度，避免超出屏幕 */
  overflow-y: auto; /* 添加滚动条 */
}

.dish-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.dish-item label {
  margin-left: 10px;
  font-size: 16px; /* 更大的字体 */
}

.discount-section {
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.discount-section label {
  margin-right: 10px;
  font-size: 16px; /* 更大的字体 */
}

.discount-section input {
  width: 60px;
  padding: 5px;
  font-size: 16px; /* 更大的字体 */
  margin-left: 10px;
}

.confirm-button {
  margin-top: 20px;
  width: 100%;
  padding: 10px;
  background-color: #28a745;
  color: white;
  font-size: 18px; /* 更大的字体 */
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.confirm-button:hover {
  background-color: #218838;
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
select, input {
  border: 2px solid black;
}
</style>

