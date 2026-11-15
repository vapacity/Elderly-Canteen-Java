<template>
  <main class="dashboard">
    <header>
      <h1>食堂管理台</h1>
      <div>
        <router-link to="/home">返回主页</router-link>
        <button type="button" @click="signOut">退出登录</button>
      </div>
    </header>
    <WeekMenu :weeks="weeks" :menus="menus" :error="error" show-stock />
  </main>
</template>

<script>
import axios from "axios";
import WeekMenu from "@/components/WeekMenu.vue";

export default {
  components: { WeekMenu },
  data() {
    return { weeks: [], menus: [], error: false };
  },
  async created() {
    try {
      const { data } = await axios.get("/demo-api/admin/weekly-menu", {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      });
      this.weeks = data.weeks;
      this.menus = data.response;
    } catch (error) {
      if ([401, 403].includes(error.response?.status)) {
        this.signOut();
      } else {
        this.error = true;
      }
    }
  },
  methods: {
    signOut() {
      localStorage.removeItem("token");
      localStorage.removeItem("identity");
      this.$store.commit("setLoggedIn", false);
      this.$store.commit("setUser", { name: "", avatar: "", id: "" });
      this.$router.replace("/login");
    },
  },
};
</script>

<style scoped>
.dashboard {
  max-width: 1100px;
  margin: 28px auto;
}
header {
  margin: 0 5vh 24px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}
header button {
  margin-left: 16px;
  padding: 6px 12px;
  cursor: pointer;
}
</style>
