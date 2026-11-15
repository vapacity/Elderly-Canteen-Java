<template>
  <transition name="fade">
      <div v-if="showMessage" class="message-popup">{{ showMessage }}</div>
    </transition>
  <div class="weekly-menu">
    <div class="header">
      <h1>每周菜单</h1>
      <div class="date-picker">
        <label for="menu-date">
          <i class="calendar-icon"></i>
        </label>
        <input type="date" v-model="menuDate" id="menu-date" @change="fetchWeeklyMenu" />
      </div>
      <div class="status">
        状态: {{ status }}
      </div>
    </div>

    <div class="menu-grid">
      <div class="day" v-for="day in days" :key="day">
        <div class="weekmenu-title">{{ day }}</div>
        <div class="dish-list">
          <div class="dish-item" v-for="dish in weeklyMenu[day]" :key="dish.id">
            <span>{{ dish.name }}</span>
            <button v-if="status === '可编辑'" class="remove-button" @click="removeDish(day, dish.id)">
              <img src="trash.png" alt="Remove" class="icon" />
            </button>
          </div>
          <button v-if="status === '可编辑'" class="add-button" @click="openAddDishDialog(day)">+</button>
        </div>
      </div>
    </div>

    <div class="add-dish-dialog" v-if="showDialog">
      <div class="dialog-content">
        <h3>添加菜品</h3>
        <button class="close-button" @click="closeAddDishDialog">
          <img src="close.png" alt="Close" class="icon" />
        </button>
        <select v-model="selectedDishId" id="dish-select">
          <option value="">请选择菜品</option>
          <!-- 动态生成的菜品选项 -->
          <option v-for="dish in availableDishes" :key="dish.dishId" :value="dish.dishId">
            {{ dish.dishName }} ({{ dish.category }})
          </option>
        </select>
        <button class="confirm-button" @click="addDish">确认</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

// 配置全局的axios默认值
axios.defaults.baseURL = '/api';
axios.defaults.headers.common['Authorization'] = `Bearer ${localStorage.getItem("token")}`;

export default {
  data() {
    return {
      menuDate: new Date().toISOString().split('T')[0], // 默认为今天
      days: ["星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
      weeklyMenu: {
        "星期一": [],
        "星期二": [],
        "星期三": [],
        "星期四": [],
        "星期五": [],
        "星期六": [],
        "星期日": [],
      },
      showDialog: false,
      currentDay: "",
      availableDishes: [], // 用于存储可选菜品
      selectedDishId: "", // 用户选择的菜品ID
      showMessage: "",
    };
  },
  computed: {
    isCurrentWeek() {
      const selectedDate = new Date(this.menuDate);
      const currentDate = new Date();
      const currentWeekStart = this.getWeekStartDate(currentDate);
      const selectedWeekStart = this.getWeekStartDate(selectedDate);
      return currentWeekStart.getTime() <= selectedWeekStart.getTime();
    },
    status() {
      return this.isCurrentWeek ? "可编辑" : "可查看";
    },
  },
  methods: {
    fetchWeeklyMenu() {
      const token = localStorage.getItem("token"); // 获取存储的 token
      const selectedDate = new Date(this.menuDate);
      const url = `/demo-api/menu?date=${selectedDate.toISOString().split('T')[0]}`;

      axios.get(url, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
          .then(response => {
            if (response.data) {
              const menuData = response.data;
              this.weeklyMenu = {
                "星期一": menuData.mon || [],
                "星期二": menuData.tue || [],
                "星期三": menuData.wed || [],
                "星期四": menuData.thu || [],
                "星期五": menuData.fri || [],
                "星期六": menuData.sat || [],
                "星期日": menuData.sun || []
              };
            } else {
              console.error('获取菜单失败: 数据为空或格式不正确');
              this.clearWeeklyMenu();
            }
          })
          .catch(error => {
            console.error('Error fetching menu:', error);
            if (error.response) {
              if(error.response.data.message){
                this.show(error.response.data.message);
              }
              else if(error.response.data.msg){
                this.show(error.response.data.msg);
              }
            }
            this.clearWeeklyMenu();
          });
    },
    clearWeeklyMenu() {
      this.weeklyMenu = {
        "星期一": [],
        "星期二": [],
        "星期三": [],
        "星期四": [],
        "星期五": [],
        "星期六": [],
        "星期日": [],
      };
    },
    getWeekStartDate(date) {
      const dayOfWeek = date.getDay();
      const startOfWeek = new Date(date);
      startOfWeek.setHours(0, 0, 0, 0);
      const diff = (dayOfWeek === 0 ? 6 : dayOfWeek - 1);
      startOfWeek.setDate(date.getDate() - diff);
      return startOfWeek;
    },
    openAddDishDialog(day) {
      if (this.status === '可编辑') {
        this.currentDay = day;
        this.showDialog = true;
        this.fetchAvailableDishes();
      }
    },
    closeAddDishDialog() {
      this.showDialog = false;
      this.selectedDishId = "";
    },

    fetchAvailableDishes() {
      const token = localStorage.getItem("token"); // 获取存储的 token
      axios.get('/demo-api/dish/search', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
          .then(response => {
            console.log('API 响应完整数据:', response);

            if (response.data.success) {
              // 检查 API 返回的数据结构是否正确
              if (Array.isArray(response.data.dish)) {
                this.availableDishes = response.data.dish; // 使用 API 返回的菜品数据填充 availableDishes
                console.log('加载的可选菜品列表:', this.availableDishes); // 打印菜品列表到控制台
              } else {
                console.error('API 返回的菜品数据格式不正确:', response.data.dish);
              }
            } else {
              console.error('加载可选菜品失败:', response.data.msg);
            }
          })
          .catch(error => {
            console.error('获取可选菜品时出错:', error);
            if (error.response) {
              if(error.response.data.message){
                this.show(error.response.data.message);
              }
              else if(error.response.data.msg){
                this.show(error.response.data.msg);
              }
            }
          });
    },


    calculateDateForDay(baseDate, targetDay) {
      const dayMap = {
        "星期一": 1,
        "星期二": 2,
        "星期三": 3,
        "星期四": 4,
        "星期五": 5,
        "星期六": 6,
        "星期日": 0 // 在JavaScript中，0表示星期日
      };

      const base = new Date(baseDate); // 使用选定的基准日期
      const baseDay = base.getDay(); // 获取基准日期的星期几（0表示星期日，1表示星期一，等等）

      const targetDayIndex = dayMap[targetDay]; // 用户点击的星期几的数字表示


      let diff = targetDayIndex - baseDay;


      const targetDate = new Date(base);
      targetDate.setDate(base.getDate() + diff);

      // 如果是星期日，日期加7天
      if (targetDay === "星期日") {
        targetDate.setDate(targetDate.getDate() + 7);
      }

      return targetDate.toISOString().split('T')[0];
    },




    addDish() {
      if (this.selectedDishId && this.currentDay) {
        const selectedDish = this.availableDishes.find(dish => dish.dishId === this.selectedDishId);

        if (!selectedDish) {
          console.error('在可选菜品中找不到所选菜品。');
          return;
        }

        // 将中文的星期几转换为英文缩写
        const dayMap = {
          "星期一": "Mon",
          "星期二": "Tue",
          "星期三": "Wed",
          "星期四": "Thu",
          "星期五": "Fri",
          "星期六": "Sat",
          "星期日": "Sun"
        };
        const englishDay = dayMap[this.currentDay];

        if (!englishDay) {
          console.error('无法转换星期几为英文缩写:', this.currentDay);
          return;
        }

        // 计算用户选择的日期对应的具体星期几的日期
        const targetDate = this.calculateDateForDay(this.menuDate, this.currentDay);

        console.log('正在添加的菜品:', selectedDish, '选择的日期:', targetDate);
        const token = localStorage.getItem("token"); // 获取存储的 token
        // 发送添加请求
        axios.post('/demo-api/menu/add', {
          date: targetDate, // 使用计算得到的日期
          Day: englishDay, // 字段名需要与后端一致
          DishId: selectedDish.dishId,
          dish: {
            category: selectedDish.category,
            name: selectedDish.dishName,
          },
          success: true,
          message: ""
        },
        {headers:{
          'Authorization': `Bearer ${token}`
        }}
      )
            .then(response => {
              console.log('后端响应:', response.data); // 打印完整的响应数据

              if (response.data.success) {
                console.log('菜品添加成功:', response.data);
                this.show("菜品添加成功");
                // 刷新菜单
                this.fetchWeeklyMenu();

                this.newDish = { category: "", name: "" }; // 重置新菜品输入
                this.closeAddDishDialog();
              } else {
                console.error('添加菜品失败:', response.data.message);
              }
            })
            .catch(error => {
              console.error('添加菜品时出错:', error);
              if (error.response) {
              if(error.response.data.message){
                this.show(error.response.data.message);
              }
              else if(error.response.data.msg){
                this.show(error.response.data.msg);
              }
            }
            });
      } else {
        this.show("请选择一个菜品和日期以添加。");
      }
    },





    convertDayToAbbreviation(day) {
      const dayMap = {
        "星期一": "Mon",
        "星期二": "Tue",
        "星期三": "Wed",
        "星期四": "Thu",
        "星期五": "Fri",
        "星期六": "Sat",
        "星期日": "Sun"
      };
      return dayMap[day];
    },

    show(message) {
      this.showMessage = message;
      setTimeout(() => {
        this.showMessage = "";
      }, 1000); // 错误信息3秒后消失
    },

    removeDish(day, id) {
      if (this.status === '可编辑') {
        // 将中文的星期几转换为英文缩写
        const dayMap = {
          "星期一": "Mon",
          "星期二": "Tue",
          "星期三": "Wed",
          "星期四": "Thu",
          "星期五": "Fri",
          "星期六": "Sat",
          "星期日": "Sun"
        };
        const englishDay = dayMap[day];

        // 计算用户选择的日期对应的具体星期几的日期
        const targetDate = this.calculateDateForDay(this.menuDate, day);

        // 创建请求体
        const requestPayload = {
          date: targetDate, // 使用计算得到的日期
          day: englishDay,
          dishId: id
        };

        // 打印请求体信息以调试
        console.log('准备删除菜品，发送的请求体:', requestPayload);

        // 使用DELETE方法发送请求
        axios.delete('/demo-api/menu/remove', {
          headers: {
            'Authorization': axios.defaults.headers.common['Authorization'],
            'Content-Type': 'application/json'
          },
          data: requestPayload // 将请求体传递给后端
        }).then(response => {
          console.log('删除请求响应:', response.data); // 打印后端的响应数据

          if (response.data.success) {
            console.log('菜品删除成功');
            this.show("菜品删除成功");
            this.fetchWeeklyMenu(); // 刷新菜单
          } else {
            console.error('删除菜品失败:', response.data.message);
          }
        }).catch(error => {
          console.error('Error removing dish:', error);

          // 打印详细的错误信息
          if (error.response) {
              if(error.response.data.message){
                this.show(error.response.data.message);
              }
              else if(error.response.data.msg){
                this.show(error.response.data.msg);
              }
            } else {
            console.error('请求失败的原因:', error.message);
          }
        });
      }
    }

  },
  mounted() {
    this.fetchWeeklyMenu();
  },
};
</script>

<style scoped>
.weekly-menu {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #ffffff; /* 添加背景色 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
  font-family: Arial, Helvetica, sans-serif;
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.date-picker {
  display: flex;
  align-items: center;
}
.calendar-icon {
  margin-right: 10px;
}
.status {
  margin-left: 20px;
}
.menu-grid {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
.weekmenu-title {
  background-color: rgb(255, 229, 193);
  font-size: 20px;
  padding: 10px;
  border-radius: 6px;
  border: 2px solid #f5a249;
  color: #f5a249;
}
.day {
  width: 13%;
  background-color: rgb(248, 246, 245);
  border-left: 2px dashed #ded5ce;
  padding: 10px;
  text-align: center;
  position: relative;
}
.day:first-child{
  border-left: none;
  border-top-left-radius: 10px;
  border-bottom-left-radius: 10px;
}
.day:last-child{
  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px;
}

.dish-list {
  margin-top: 10px;
}
.dish-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  background: rgba(225, 217, 208, 0.5);
  border: none;
  border-radius: 3px;
  padding: 10px;
  width: 90px;
  box-shadow: 0 2px 3px rgba(0, 0, 0, 0.1);
  position: relative;
}
.remove-button {
  background-color: transparent;
  border: none;
  cursor: pointer;
  padding: 0;
  position: absolute;
  right: -20px;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
  justify-content: center;
}
.remove-button .icon {
  width: 20px;
  height: 20px;
  transition: transform 0.3s ease;
}
.remove-button:hover .icon {
  transform: scale(1.2);
}

.add-button {
  background-color: rgb(131, 177, 131);
  color: rgb(27, 125, 22);
  border: none;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  font-size: 20px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
  margin-top: 15px;
  transition: background-color 0.3s ease, transform 0.2s ease;
}
.add-button:hover {
  transform: scale(1.1);
}

.add-dish-dialog {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  border: 1px solid #ccc;
  padding: 20px;
  z-index: 1000;
  width: 200px;
  height: 130px;
}
.dialog-content {
  position: relative;
  display: flex;
  flex-direction: column; 
  justify-content: space-between; 
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
  width: 15px;
  height: 15px;
  transition: transform 0.3s ease;
}
.close-button:hover .icon {
  transform: scale(1.2);
}

.confirm-button {
  background-color: green;
  color: white;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  margin-top: 10px;
  border-radius: 5px;
  font-size: 12px;
  transition: background-color 0.3s ease, transform 0.2s ease;
}
.confirm-button:hover {
  background-color: #27ae60;
  transform: scale(1.05);
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
select {
  border: 2px solid black;
}
</style>