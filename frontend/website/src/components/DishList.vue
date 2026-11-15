<template>
  <div class="dish-list">
    <h2>菜品管理</h2>
    <div class="search-bar">
      <label for="dish-name">名称 </label>
      <input v-model="searchName" id="dish-name" placeholder="输入菜品名称" />

      <label for="dish-category">类别 </label>
      <select v-model="searchCategory" id="dish-category">
        <option value="">请选择</option>
        <!-- 动态生成的类别选项 -->
        <option v-for="category in categories" :key="category.cateId" :value="category.cateName">
          {{ category.cateName }}
        </option>
      </select>

      <button @click="searchDishes" class="btn blue">搜索</button>
      <button @click="openAddDishForm" class="btn green">+</button>
    </div>
    <!-- 缓冲条 -->
    <div v-if="isLoading" class="loading-indicator">加载中...</div>
    <div v-if="showForm" class="dish-form-overlay">
      <div class="dish-form-container">
        <div class="dish-form">
          <h3>{{ isEditing ? '编辑菜品' : '添加菜品' }}</h3>

          <!-- 图片预览和上传 -->
          <div class="image-preview">
            <img :src="imagePreviewUrl" alt="预览图片" v-if="imagePreviewUrl" />
            <!-- 仅在编辑模式下显示“修改图片”按钮 -->
            <button v-if="isEditing" @click="triggerFileInput">修改图片</button>
            <input type="file" ref="fileInput" @change="handleFileChange" style="display: none;" />
          </div>

          <label for="dish-form-name">菜品名称</label>
          <input v-model="form.dishName" id="dish-form-name" placeholder="输入菜品名称" />

          <label for="dish-form-price">价格</label>
          <input v-model="form.price" id="dish-form-price" placeholder="输入菜品价格" />

          <label for="dish-form-category">类别</label>
          <select v-model="form.category" id="dish-form-category">
            <option value="">请选择</option>
            <!-- 动态生成的类别选项 -->
            <option v-for="category in categories" :key="category.cateId" :value="category.cateName">
              {{ category.cateName }}
            </option>
          </select>

          <!-- 配方表格 -->
          <div class="recipe-header">
            <label for="recipe-table" class="recipe-label">配方</label>
            <button @click="addIngredient" class="add-ingredient-button">添加原料</button>
          </div>

          <div id="recipe-table" class="recipe-table-container">
            <table class="recipe-table">
              <thead>
              <tr>
                <th>食材</th>
                <th>份量</th>
                <th>操作</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(ingredient, index) in form.formula" :key="index">
                <td>
                  <select v-model="ingredient.ingredientId" @change="updateIngredientName(index)">
                    <option :value="ingredient.ingredientId">{{ ingredient.ingredientName }}</option>
                    <option v-for="recipe in availableRecipes" :key="recipe.ingredientId" :value="recipe.ingredientId">
                      {{ recipe.ingredientName }}
                    </option>
                  </select>
                </td>
                <td>
                  <input v-model="ingredient.amount" placeholder="输入原料数量" />
                </td>
                <td>
                  <button @click="removeIngredient(index)" class="delete-button">删除</button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>

          <div class="button-group">
            <button @click="isEditing ? updateDish() : saveDish()" class="confirm-button">{{ isEditing ? '保存' : '添加' }}</button>
            <button @click="cancelForm" class="cancel-button">取消</button>
          </div>
        </div>
      </div>
    </div>

    <div class="dish-container">
      <div v-for="dish in paginatedDishes" :key="dish.dishId" class="dish-item">
        <img :src="dish.image" alt="Dish Image" />
        <div class="dish-details">
          <h3>{{ dish.dishName }}</h3>
          <p>类别: {{ dish.category }}</p>
          <p>配方:</p>
          <ul>
            <li v-for="(ingredient, index) in dish.formula" :key="index">
              {{ ingredient.ingredientName }} - 数量: {{ ingredient.amount }}
            </li>
          </ul>
        </div>
        <div class="dish-actions">
          <span class="dish-price">价格: {{ dish.price }}</span>
          <div class="Action-buttons">
            <button @click.stop="deleteDish(dish.dishId)">删除</button>
            <button @click.stop="openEditDishForm(dish)">修改</button>
          </div>
        </div>
      </div>
    </div>

    <div class="pagination">
      <button @click="goToPage(currentPage - 1)" :disabled="currentPage === 1">上一页</button>
      <input v-model="pageInput" type="number" min="1" :max="totalPages" />
      <button @click="goToPage(pageInput)" :disabled="!pageInput || pageInput < 1 || pageInput > totalPages">跳转</button>
      <button @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPages">下一页</button>
    </div>
    <div v-if="notification.message" class="notification" :class="notification.type">{{ notification.message }}</div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

axios.defaults.baseURL = '';

export default {
  setup() {
    const isLoading = ref(false);  // 追踪是否正在加载数据
    const searchName = ref('');
    const searchCategory = ref('');
    const dishes = ref([]);
    const filteredDishes = ref([]);
    const availableRecipes = ref([]);
    const currentPage = ref(1);
    const itemsPerPage = ref(10);
    const showForm = ref(false);
    const isEditing = ref(false);
    const categories = ref([]);
    const imagePreviewUrl = ref('');
    const form = ref({
      dishId: null,
      dishName: '',
      category: '',
      formula: [],
      price: '',
      image: ''
    });
    const pageInput = ref(1);
    const fileInput = ref(null);
    const notification = ref({ message: '', type: '' });

    const showNotification = (message, type = 'info') => {
      notification.value = { message, type };
      setTimeout(() => {
        notification.value = { message: '', type: '' };
      }, 3000);
    };

    const fetchCategories = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get('/demo-api/category/search', {
        headers: {
          Authorization: `Bearer ${token}`
        },
        params: {
          // 如果有查询参数，可以放在这里
        }
      });
        
        if (response.data.success) {
          categories.value = response.data.cates;
        } else {
          console.error('API 请求成功，但返回的 success 为 false:', response.data.message);
          showNotification(`${response.data.message}`, 'error');
        }
      } catch (error) {
        console.error('获取类别数据失败:', error.message);
        if (error.response) {
          if(error.response.data.message){
            showNotification(`获取类别数据失败${error.response.data.message}`, 'error');
          }
          else if(error.response.data.msg){
            showNotification(`获取类别数据失败${error.response.data.msg}`, 'error');
          }
        }
      }
    };

    const fetchDishes = async () => {
      isLoading.value = true;  // 开始加载
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get('/demo-api/dish/search', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        if (response.data.success) {
          dishes.value = Array.isArray(response.data.dish) ? response.data.dish : [];
          filteredDishes.value = dishes.value;
          console.log(response.data);
        } else {
          console.error('API 请求成功，但返回的 success 为 false:', response.data.message);
          showNotification(`${response.data.message}`, 'error');
        }
      } catch (error) {
        console.error('获取菜品数据失败:', error.message);
        if (error.response) {
          if(error.response.data.message){
            showNotification(`获取菜品数据失败${error.response.data.message}`, 'error');
          }
          else if(error.response.data.msg){
            showNotification(`获取菜品数据失败${error.response.data.msg}`, 'error');
          }
        } else {
          console.error('请求失败:', error.message);
        }
      }finally {
        isLoading.value = false;  // 结束加载
      }
    };

    const fetchRecipes = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get('/demo-api/ingredients/search', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        if (response && response.data) {
          if (response.data.success) {
            if (Array.isArray(response.data.ingredients) && response.data.ingredients.length > 0) {
              availableRecipes.value = response.data.ingredients;
            } else {
              console.error('API 返回的 ingredients 为空或不是数组:', response.data.ingredients);
              showNotification('API 返回的数据格式不正确，请稍后重试。', 'error');
            }
          } else {
            console.error('API 请求成功，但返回的 success 为 false:', response.data.message);
            showNotification('错误: ' + (response.data.message || '未知错误'), 'error');
          }
        } else {
          console.error('API 响应无效或没有返回数据:', response);
          showNotification('无法获取配方数据，请稍后重试。', 'error');
        }
      } catch (error) {
        console.error('获取配方数据失败:', error);
        showNotification('获取配方数据时发生错误，请检查您的网络连接或稍后重试。', 'error');
      }
    };

    const updateIngredientName = (index) => {
      const selectedRecipe = availableRecipes.value.find(recipe => recipe.ingredientId === form.value.formula[index].ingredientId);
      if (selectedRecipe) {
        form.value.formula[index].ingredientName = selectedRecipe.ingredientName;
      }
    };

    const searchDishes = () => {
      filteredDishes.value = dishes.value.filter(dish => {
        return (searchName.value === '' || dish.dishName.includes(searchName.value)) &&
            (searchCategory.value === '' || dish.category === searchCategory.value);
      });
      currentPage.value = 1;
    };

    const goToPage = (page) => {
      const pageNum = Number(page);
      if (pageNum >= 1 && pageNum <= totalPages.value) {
        currentPage.value = pageNum;
        pageInput.value = pageNum;
      }
    };

    const openAddDishForm = async () => {
      isEditing.value = false;
      resetForm();
      await fetchRecipes();
      showForm.value = true;
    };

    const openEditDishForm = async (dish) => {
      isEditing.value = true;
      showForm.value = true;
      form.value = { ...dish };
      imagePreviewUrl.value = form.value.image || '';

      await fetchRecipes();

      if (Array.isArray(form.value.formula)) {
        form.value.formula.forEach((ingredient, index) => {
          updateIngredientName(index);
        });
      } else {
        console.warn('form.value.formula 不是一个有效的数组:', form.value.formula);
      }
    };

    const uploadImage = async (file, dishId) => {
      if (!file || !(file instanceof File)) {
        showNotification('请选择一个有效的图片文件。', 'error');
        return null;
      }

      const formData = new FormData();
      formData.append('image', file);
      formData.append('DishId', dishId);

      for (let pair of formData.entries()) {
        console.log(`${pair[0]}: ${pair[1]}`);
      }
      const token = localStorage.getItem("token"); // 获取存储的 token
      try {
        const response = await fetch(`/demo-api/Dish/uploadImage?DishId=${dishId}`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`
          },
          body: formData,
        });

        if (!response.ok) {
          throw new Error(`HTTP 错误！状态码: ${response.status}，响应文本: ${response.statusText}`);
        }

        const responseData = await response.json();
        showNotification('图片上传成功！', 'success');
        fetchDishes();

        return responseData.url;
      } catch (error) {
        console.error('上传图片失败:', error.message);
        showNotification('上传图片失败，请检查请求路径和服务器配置，然后重试。', 'error');
        return null;
      }
    };

    const saveDish = async () => {
      try {
        if (!form.value.dishName || !form.value.category || !form.value.price || form.value.formula.length === 0) {
          showNotification('请填写所有必需的字段，包括菜品名称、类别、价格和至少一个配方。', 'error');
          return;
        }

        const selectedCategory = categories.value.find(category => category.cateName === form.value.category);
        if (!selectedCategory) {
          showNotification('所选类别无效，请重新选择。', 'error');
          return;
        }

        const requestData = {
          dishId: form.value.dishId,
          Name: form.value.dishName,
          cateId: selectedCategory.cateId,
          category: form.value.category,
          price: parseFloat(parseFloat(form.value.price).toFixed(2)),
          formula: form.value.formula.map(item => ({
            ingredientId: item.ingredientId,
            ingredientName: item.ingredientName,
            amount: item.amount
          }))
        };
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.post('/demo-api/dish/addDish',
        requestData,  // 这是请求的 body 数据
        {
          headers: {
            Authorization: `Bearer ${token}`  // headers 放在第三个参数
          }
        }
      );


        if (response.data.success) {
          dishes.value.push(response.data.dish);
          filteredDishes.value = dishes.value;
          showNotification('添加成功', 'success');
          cancelForm();
          goToPage(currentPage.value);
        } else {
          console.error('服务器返回错误信息:', response.data.message);
          showNotification('保存菜品失败：' + response.data.message, 'error');
        }
      } catch (error) {
        console.error('保存菜品失败:', error);
        if (error.response) {
          if(error.response.data.message){
            showNotification(`保存菜品失败${error.response.data.message}`, 'error');
          }
          else if(error.response.data.msg){
            showNotification(`保存菜品失败${error.response.data.msg}`, 'error');
          }
        }
      }
    };

    const updateDish = async () => {
      try {
        if (fileInput.value.files[0]) {
          const updatedImageUrl = await uploadImage(fileInput.value.files[0], form.value.dishId);
          if (updatedImageUrl) {
            form.value.image = updatedImageUrl;
          } else {
            return;
          }
        }

        const selectedCategory = categories.value.find(category => category.cateName === form.value.category);
        if (selectedCategory) {
          form.value.cateId = selectedCategory.cateId;
        } else {
          showNotification('所选类别无效，请重新选择。', 'error');
          return;
        }

        const updateData = {
          dishId: form.value.dishId,
          Name: form.value.dishName,
          cateId: form.value.cateId,
          category: form.value.category,
          price: parseFloat(parseFloat(form.value.price).toFixed(2)),
          formula: form.value.formula.map(item => ({
            ingredientId: item.ingredientId,
            ingredientName: item.ingredientName,
            amount: item.amount
          }))
        };
        console.log('更新后的数据：', updateData);
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.put('/demo-api/dish/updateDish',
          updateData,  // 第二个参数为请求主体数据
          {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          }
        );
        console.log('返回的数据：', response.data.dish);
        if (response.data.success) {
          const index = dishes.value.findIndex(d => d.dishId === form.value.dishId);
          if (index !== -1) {
            dishes.value.splice(index, 1, { ...form.value });
            filteredDishes.value = dishes.value;
          }
          showNotification("更新菜品成功", 'success');
          cancelForm();
          goToPage(currentPage.value);
        } else {
          console.error('更新请求成功，但服务器返回失败信息:', response.data.msg);
          showNotification(`${response.data.msg}`, 'error');
        }
      } catch (error) {
        console.error('更新菜品失败:', error);
        if (error.response) {
          if(error.response.data.message){
            showNotification(`更新菜品失败${error.response.data.message}`, 'error');
          }
          else if(error.response.data.msg){
            showNotification(`更新菜品失败${error.response.data.msg}`, 'error');
          }
        }
      }
    };

    const cancelForm = () => {
      showForm.value = false;
      resetForm();
    };

    const resetForm = () => {
      form.value = {
        dishId: null,
        dishName: '',
        category: '',
        formula: [],
        price: '',
        image: ''
      };
      imagePreviewUrl.value = '';
    };

    const addIngredient = () => {
      form.value.formula.push({ IngredientId: '', IngredientName: '', amount: '' });
    };

    const removeIngredient = (index) => {
      form.value.formula.splice(index, 1);
    };

    const deleteDish = async (id) => {
      try {
        console.log('即将删除的菜品ID:', id);
        const token = localStorage.getItem("token"); // 获取存储的 token
        const response = await axios.delete(`/demo-api/dish/delete/${id}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        console.log('删除请求成功，服务器响应:', response.data);
        showNotification(`删除成功`, 'success');
        dishes.value = dishes.value.filter(dish => dish.dishId !== id);
        filteredDishes.value = dishes.value;

        console.log('本地菜品数据更新完成。当前菜品列表:', dishes.value);

        goToPage(currentPage.value);
      } catch (error) {
        console.error('删除菜品失败:', error);

        if (error.response) {
          console.error('响应状态:', error.response.status);
          console.error('响应数据:', error.response.data);
          console.error('响应头部:', error.response.headers);
          if(error.response.data.message){
            showNotification(`删除菜品失败${error.response.data.message}`, 'error');
          }
          else if(error.response.data.msg){
            showNotification(`删除菜品失败${error.response.data.msg}`, 'error');
          }
        } else if (error.request) {
          console.error('请求未收到响应:', error.request);
        } else {
          console.error('请求配置时发生错误:', error.message);
        }
      }
    };

    const handleFileChange = (event) => {
      const file = event.target.files[0];
      if (file) {
        imagePreviewUrl.value = URL.createObjectURL(file);
      }
    };

    const triggerFileInput = () => {
      fileInput.value.click();
    };

    const paginatedDishes = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage.value;
      return filteredDishes.value.slice(start, start + itemsPerPage.value);
    });

    const totalPages = computed(() => {
      return Math.ceil(filteredDishes.value.length / itemsPerPage.value);
    });

    onMounted(() => {
      fetchDishes();
      fetchCategories();
    });

    return {
      searchName,
      searchCategory,
      dishes,
      filteredDishes,
      availableRecipes,
      currentPage,
      itemsPerPage,
      showForm,
      categories,
      isEditing,
      imagePreviewUrl,
      form,
      fetchDishes,
      fetchRecipes,
      searchDishes,
      fetchCategories,
      goToPage,
      openAddDishForm,
      openEditDishForm,
      saveDish,
      updateDish,
      cancelForm,
      resetForm,
      addIngredient,
      removeIngredient,
      updateIngredientName,
      deleteDish,
      handleFileChange,
      triggerFileInput,
      fileInput,
      paginatedDishes,
      totalPages,
      pageInput,
      notification,
      isLoading  // 确保 isLoading 被返回
    };
  }
};
</script>

<style>
/* 保持所有样式不变 */
.dish-list {
  padding: 20px;
}
.loading-indicator {
  text-align: center;
  font-size: 16px;
  color: #333;
  padding: 10px;
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
.search-bar {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  font-family: 'Arial', sans-serif;
  padding: 10px 20px;
  background-color: rgba(225, 217, 208, 0.5);
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.search-bar label {
  font-weight: bold;
  margin-right: 10px;
  color: #333;
}

.search-bar input,
.search-bar select {
  margin-right: 20px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #fff;
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
  font-size: 14px;
  width: 200px;
}

.search-bar button {
  padding: 10px 15px;
  border: none;
  background-color: #007bff;
  color: white;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease, transform 0.3s ease;
  margin-left: 10px;
}


.search-bar button:last-child {
  background-color: #28a745;
  font-size: 20px;
  padding: 5px 10px;
}



.dish-form-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  overflow-y: auto;
}

.dish-form-container {
  width: 100%;
  max-width: 600px;
  padding: 30px;
  background-color: #ffffff;
  border-radius: 15px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
  overflow-y: auto;
  max-height: 80vh;
  margin: 0 auto;
}

.dish-form {
  width: 100%;
}

.dish-form h3 {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: bold;
}

.dish-form label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
  color: #555;
}

.dish-form input,
.dish-form select {
  margin-bottom: 15px;
  padding: 12px;
  width: 100%;
  box-sizing: border-box;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #f9f9f9;
  font-size: 16px;
  transition: border-color 0.3s ease;
}

.dish-form input:focus,
.dish-form select:focus {
  border-color: #007bff;
  background-color: #fff;
}

.recipe-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.recipe-label {
  font-size: 18px;
  font-weight: bold;
  margin: 0;
  display: inline-block;
  vertical-align: middle;
}

.add-ingredient-button {
  padding: 5px 10px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
  font-size: 14px;
  display: inline-block;
  vertical-align: middle;
}

.recipe-table-container {
  max-height: 200px;
  overflow-y: auto;
  margin-bottom: 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
  position: relative;
}

.recipe-table thead th {
  position: sticky;
  top: 0;
  background-color: #f9f9f9;
  z-index: 1;
  padding: 8px;
  border-bottom: 2px solid #ddd;
  text-align: center;
}

.recipe-table {
  width: 100%;
  border-collapse: collapse;
}

.recipe-table th,
.recipe-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
  vertical-align: middle;
}

.recipe-table th:nth-child(1),
.recipe-table td:nth-child(1) {
  width: 50%;
}

.recipe-table th:nth-child(2),
.recipe-table td:nth-child(2) {
  width: 35%;
}

.recipe-table th:nth-child(3),
.recipe-table td:nth-child(3) {
  width: 15%;
}

.recipe-table select,
.recipe-table input {
  width: 100%;
  text-align: center;
  padding: 5px;
  box-sizing: border-box;
  display: block;
  margin: 0 auto;
}

.delete-button {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}


.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  width: 100%;
}

.confirm-button,
.cancel-button {
  width: 30%;
  padding: 12px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.confirm-button {
  background-color: #28a745;
  color: white;
}

.confirm-button:hover {
  background-color: #218838;
}

.cancel-button {
  background-color: #dc3545;
  color: white;
}

.cancel-button:hover {
  background-color: #c82333;
}

.image-preview {
  margin-bottom: 15px;
  text-align: center;
}

.image-preview img {
  max-width: 100%;
  max-height: 200px;
  display: block;
  margin: 0 auto 10px;
}

.dish-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 10px;
  width: 60%;
  margin-left: auto;
  margin-right: auto;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.dish-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  background-color: #f9f9f9;
}

.dish-item img {
  width: 120px;
  height: 120px;
  object-fit: cover;
  margin-right: 20px;
  border-radius: 10px;
  border: 1px solid #ccc;
}

.dish-details {
  flex-grow: 1;
}

.dish-details h3 {
  margin: 0 0 10px;
}

.dish-details p {
  margin: 5px 0;
}

.dish-actions {
  display: flex;
  align-items: center;
  flex-direction: column;
  text-align: center;
}

.dish-price {
  font-weight: bold;
  margin-bottom: 10px;
}

.Action-buttons button {
  margin-top: 5px;
  padding: 5px 10px;

  border: 2px solid rgb(103, 136, 246);
  background-color: white;
  color: rgb(103, 136, 246);
  font-weight: bold;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-right: 10px;
}

.Action-buttons button:last-child {
  margin-right: 0;
}



.pagination {
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.pagination button {
  margin-right: 5px;
  padding: 5px 10px;
  border: 2px solid rgb(103, 136, 246);
  background-color: white;
  color: rgb(103, 136, 246);
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.pagination button:disabled {
  background-color: #ddd;
  border-color: #ddd;
  cursor: not-allowed;
}

.pagination input {
  width: 50px;
  text-align: center;
  margin-right: 5px;
  padding: 5px;
  border: 2px solid rgb(103, 136, 246);
  background-color: white;
  color: rgb(103, 136, 246);
  border-radius: 5px;
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

select ,input {
  border: 2px solid black;
}
</style>
