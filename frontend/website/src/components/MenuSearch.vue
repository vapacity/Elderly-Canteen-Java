<!-- components/MenuSearch.vue -->
<template>
  <section>
    <!-- 锚点，用于使导航栏跳转到本区域上方50px的位置，不然顶部导航栏会遮住部分内容 -->
    <a
      id="searchNav"
      style="display: block; height: 50px; margin-top: -50px"
    ></a>
    <div class="chartFrame">
      <div class="chartLeft">
        <h1 style="color: orange; margin-bottom: 20px">
          【轻松搜寻】你的美食指南
        </h1>
        <p>不确定今天吃什么？只需输入关键词，快速找到你想吃的菜品！</p>
        <div class="search">
          <img src="../assets/search.png" alt="Search Icon" />
          <input
            type="text"
            name="menuSearch"
            id="menuSearch"
            placeholder="搜索您感兴趣的菜"
            @input="searchMenuItems"
            v-model="temSearchQuery"
          />
        </div>
        <div class="menucharttext">
          <div>
            <h1 style="color: darkblue; font-size: 25px; margin-bottom: 20px">
              【人气榜单】最受欢迎的美味
            </h1>
            <p>查看最热门的菜品，跟着大家的选择，享受美味！</p>
          </div>
          <div class="rankpoint">
            <img src="../assets/rankpoint.gif" alt="Search Icon" />
          </div>
        </div>
      </div>
      <!--没有进行搜索的情况（初始化）-->
      <div class="menuChart">
        <div v-if="searchResult.length == 0 && searchQuery == ''">
          <div v-for="item in menuItems" :key="item.id" class="menuItem">
            <div class="rank">{{ item.rank }}</div>
            <div class="dishImg"><img :src="item.picture" /></div>
            <div class="dishIofo">
              <div class="dishName" style="font-size: 20px; margin-bottom: 5px">
                {{ item.dishName }}
              </div>
              <div class="dishCat">类别：{{ item.category }}</div>
              <div class="dishSale" style="color: #218380">
                销量：{{ item.sale }}
              </div>
            </div>
            <div class="dishPrice">￥{{ item.price }}</div>
          </div>
        </div>
        <!--成功搜索的情况-->
        <div v-else-if="searchResult.length != 0">
          <div v-for="item in searchResult" :key="item.id" class="menuItem">
            <div class="rank">{{ item.rank }}</div>
            <div class="dishImg"><img :src="item.picture" /></div>
            <div class="dishIofo">
              <div class="dishName" style="font-size: 20px; margin-bottom: 5px">
                {{ item.dishName }}
              </div>
              <div class="dishCat">类别：{{ item.category }}</div>
              <div class="dishSale" style="color: #218380">
                销量：{{ item.sale }}
              </div>
            </div>
            <div class="dishPrice">￥{{ item.price }}</div>
          </div>
        </div>
        <!--搜索失败-->
        <div v-else>
          <div class="menuItem" id="noneItem">暂无信息</div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  props: {
    menuItems: Array,
  },
  data() {
    return {
      temSearchQuery: "",
      searchQuery: "",
      searchResult: [],
    };
  },
  methods: {
    searchMenuItems() {
      if (!Array.isArray(this.menuItems)) {
        console.error("menuItems is not an array:", this.menuItems);
        return;
      }
      this.searchQuery = this.temSearchQuery;
      this.searchResult = this.menuItems.filter((item) =>
        item.dishName?.includes(this.searchQuery)
      );
    },
  },
};
</script>

<style scoped>
h1 {
  margin-left: 15px;
  margin-bottom: 5px;
}
.chartFrame {
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin: 5vh 5vh;
}

.search {
  display: flex;
  justify-content: center;
  margin: 30px 0;
  margin-bottom: 70px;
}
.search img {
  width: 40px;
  height: 40px;
  margin: 20px;
}
#menuSearch {
  width: 300px;
  height: 60px;
  border-radius: 30px;
  padding-left: 15px;
  padding-right: 15px;
  margin-top: 10px;
  border-color: #db962b;
  outline: none;
  box-shadow: 0 0px 15px rgba(180, 109, 27, 0.3);
}
.rankpoint {
  width: 100px;
  height: 100px;
  margin-left: 50px;
}
.rankpoint img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.menucharttext {
  display: flex;
  justify-content: center;
  align-items: center;
}
.chartLeft {
  width: 30%;
}

.menuChart {
  padding-left: 20px;
  padding-right: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 60%;
  height: 500px;
  overflow-y: auto;
}
.menuChart::-webkit-scrollbar {
  width: 5px; /* 滚动条宽度 */
}
.menuChart::-webkit-scrollbar-thumb {
  background-color: #cccccc; /* 滚动条滑块的颜色 */
  border-radius: 2.5px; /* 滚动条滑块的圆角 */
  opacity: 90%;
}
.menuChart::-webkit-scrollbar-track {
  opacity: 100%;
}
.menuChart .menuItem {
  display: flex;
  height: 120px;
  align-items: center;
  border-bottom: 1.5px solid #c5c5c5; /* 在每个列表项底部添加一条横线 */
}
.menuChart .menuItem:last-child {
  border-bottom: none; /* 移除最后一个列表项底部的横线 */
}
.rank {
  background-color: #db962b;
  height: 30px;
  width: 30px;
  margin: 10px;
  text-align: center;
  line-height: 2;
  color: #ffffff;
  border-radius: 50%;
}

.dishImg {
  height: 80px;
  width: 80px;
  margin: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden; /* 防止图片超出容器 */
  border-radius: 50%;
}
.dishImg img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.menuItem .dishPrice {
  color: #db962b;
  font-weight: bold;
  margin-left: auto;
}
#noneItem {
  justify-content: center;
}
</style>
