<template>
  <section>
    <a id="menuNav" class="menuAnchor"></a>
    <h2 class="menuTitle">每周菜单</h2>
    <p v-if="error" class="menuMessage">菜单暂时无法加载，请刷新页面。</p>
    <p v-else-if="!weeks.length" class="menuMessage">菜单加载中…</p>
    <template v-else>
      <div class="weekOptions" role="group" aria-label="选择菜单日期">
        <button
          v-for="(dates, index) in weeks"
          :key="dates[0]"
          type="button"
          :aria-pressed="selectedWeek === index"
          @click="selectedWeek = index"
        >
          {{ index === 0 ? "本周" : "下周" }}
          {{ dates[0].slice(5) }} 至 {{ dates[6].slice(5) }}
        </button>
      </div>
      <div class="menuFrame">
        <article v-for="(day, index) in displayedDays" :key="day.date" class="menuDay">
          <h3>{{ weekdayNames[index] }} · {{ day.date.slice(5) }}</h3>
          <ul v-if="day.dishes.length">
            <li v-for="dish in day.dishes" :key="dish.dishId">
              {{ dish.dishName }}
              <small v-if="showStock">库存 {{ dish.stock }} 份</small>
            </li>
          </ul>
          <p v-else>暂无菜单</p>
        </article>
      </div>
    </template>
  </section>
</template>

<script>
export default {
  props: {
    weeks: { type: Array, required: true },
    menus: { type: Array, required: true },
    error: { type: Boolean, default: false },
    showStock: { type: Boolean, default: false },
  },
  data() {
    return {
      selectedWeek: 0,
      weekdayNames: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"],
    };
  },
  computed: {
    displayedDays() {
      const dates = this.weeks[this.selectedWeek] || [];
      return dates.map((date) => ({
        date,
        dishes: this.menus.filter((dish) => dish.week === date),
      }));
    },
  },
};
</script>

<style scoped>
.menuAnchor {
  display: block;
  height: 50px;
  margin-top: -50px;
}

.menuTitle {
  color: #c75239;
  font-size: 36px;
  margin: 0 5vh 12px;
}

.weekOptions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin: 0 5vh;
}

.weekOptions button {
  border: 1px solid #c75239;
  border-radius: 8px;
  padding: 9px 14px;
  background: #fff;
  color: #9c3d28;
  cursor: pointer;
}
.weekOptions button[aria-pressed="true"] {
  background: #9c3d28;
  color: #fff;
}

.menuFrame {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(125px, 1fr));
  border-radius: 10px;
  margin: 16px 5vh 20px;
  background: #fffcf7;
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
}
.menuDay {
  padding: 12px 5px;
  text-align: center;
}
.menuDay h3 {
  color: #9c3d28;
  font-size: 16px;
  margin: 0 0 6px;
}
.menuDay ul {
  margin: 0;
  padding: 0;
  list-style: none;
}
.menuDay li {
  padding: 4px 0;
}
.menuDay small {
  display: block;
  color: #62584e;
}
.menuMessage {
  margin: 0 5vh 20px;
}
</style>
