<template>
  <div class="stats-container">
    <h1>志愿者统计</h1>
    <div class="stat-section">
      <div class="stat-box">
        <h2>志愿者申请统计</h2>
        <div class="stat-value">{{ animatedVisitCount }} 次</div>
      </div>
      <div class="stat-box">
        <h2>志愿者总数</h2>
        <div class="stat-value">{{ animatedTotalUsers }} 人</div>
      </div>
    </div>
    <div class="chart-section">
      <div class="chart-box">
        <h2>每月人均接单次数折线图</h2>
        <canvas id="lineChart"></canvas>
      </div>
      <div class="chart-box">
        <h2>志愿者得分排行榜</h2>
        <table>
          <thead>
            <tr>
              <th>姓名</th>
              <th>接单次数</th>
              <th>平均得分</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="volunteer in volunteers" :key="volunteer.name">
              <td>{{ volunteer.name }}</td>
              <td>{{ volunteer.orders }}</td>
              <td>{{ volunteer.score }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { Chart, registerables } from 'chart.js';
import axios from 'axios';

Chart.register(...registerables);

export default {
  setup() {
    const visitCount = ref(0);
    const totalUsers = ref(0);
    const monthlyNewUsers = ref(0);

    const animatedVisitCount = ref(0);
    const animatedTotalUsers = ref(0);
    const animatedMonthlyNewUsers = ref(0);

    const visitData = ref([]);
    const volunteers = ref([]);

    let chartInstance = null;

    const fetchData = async () => {
      try {
        const visitResponse = await axios.get('/demo-api/vol-request'); // 获取志愿者申请数据
        const userResponse = await axios.get('/demo-api/vol-stats'); // 获取志愿者统计数据
        const volunteersResponse = await axios.get('/demo-api/volunteers'); // 获取志愿者详细数据

        visitCount.value = visitResponse.data.totalVisits;
        totalUsers.value = userResponse.data.totalUsers;
        visitData.value = userResponse.data.monthlyOrders;
        volunteers.value = volunteersResponse.data;

        animateValue(animatedVisitCount, visitCount.value, 700);
        animateValue(animatedTotalUsers, totalUsers.value, 700);
        animateValue(animatedMonthlyNewUsers, monthlyNewUsers.value, 700);
        drawLineChart();
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    const animateValue = (refValue, targetValue, duration) => {
      let start = 0;
      const step = targetValue / (duration / 10);
      const interval = setInterval(() => {
        start += step;
        if (start >= targetValue) {
          refValue.value = targetValue;
          clearInterval(interval);
        } else {
          refValue.value = Math.floor(start);
        }
      }, 10);
    };

    const drawLineChart = () => {
      const canvas = document.getElementById('lineChart');
      const ctx = canvas.getContext('2d');
      if (chartInstance) {
        chartInstance.destroy();
      }
      chartInstance = new Chart(ctx, {
        type: 'line',
        data: {
          labels: ['1月', '2月', '3月', '4月', '5月'], // 示例标签，可根据实际数据调整
          datasets: [{
            label: '每月接单次数',
            data: visitData.value,
            borderColor: 'rgba(75, 192, 192, 1)',
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderWidth: 2,
            pointBackgroundColor: 'rgba(75, 192, 192, 1)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(75, 192, 192, 1)'
          }]
        },
        options: {
          responsive: true,
          animation: {
            duration: 700,
            easing: 'easeInOutQuad'
          },
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                color: '#555'
              },
              grid: {
                color: '#ddd'
              }
            },
            x: {
              ticks: {
                color: '#555'
              },
              grid: {
                color: '#ddd'
              }
            }
          },
          plugins: {
            legend: {
              display: true,
              labels: {
                color: '#333'
              }
            }
          }
        }
      });
    };

    onMounted(() => {
      fetchData();
    });

    onBeforeUnmount(() => {
      if (chartInstance) {
        chartInstance.destroy();
      }
    });

    return {
      visitCount,
      totalUsers,
      monthlyNewUsers,
      animatedVisitCount,
      animatedTotalUsers,
      animatedMonthlyNewUsers,
      volunteers
    };
  }
};
</script>

<style scoped>
.stats-container {
  max-width: 1200px;
  margin: auto;
  padding: 30px;
  font-family: 'Roboto', sans-serif;
  background-color: #fdfdfd;
  border-radius: 12px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease-in-out;
}

.stats-container:hover {
  box-shadow: 0 0 30px rgba(0, 0, 0, 0.2);
}

h1 {
  text-align: center;
  color: #333;
  font-size: 2.5em;
  margin-bottom: 30px;
}

.stat-section {
  display: flex;
  justify-content: space-around;
  margin-bottom: 40px;
}

.stat-box {
  background-color: #f0f4f7;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  flex: 1;
  margin: 0 10px;
  transition: transform 0.3s ease;
}

.stat-box:hover {
  transform: translateY(-5px);
}

.stat-box h2 {
  font-size: 1.5em;
  color: #555;
  margin-bottom: 15px;
}

.stat-value {
  font-size: 2.5em;
  font-weight: bold;
  color: #007BFF;
}

.chart-section {
  display: flex;
  justify-content: space-around;
  margin-bottom: 40px;
}

.chart-box {
  background-color: #ffffff;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  flex: 1;
  margin: 0 10px;
  transition: box-shadow 0.3s ease;
}

.chart-box:hover {
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}

.chart-box h2 {
  font-size: 1.5em;
  color: #555;
  margin-bottom: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  font-size: 1em;
  color: #555;
}

th, td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: center;
}

th {
  background-color: #f9f9f9;
  color: #000000;
}

td {
  background-color: #fdfdfd;
}

td:first-child {
  font-weight: bold;
}

canvas {
  display: block;
  margin: auto;
  width: 100%;
  height: 100%;
}

@media (max-width: 768px) {
  .stat-section, .chart-section {
    flex-direction: column;
    gap: 20px;
  }

  .stat-box, .chart-box {
    width: 100%;
  }
}
</style>
