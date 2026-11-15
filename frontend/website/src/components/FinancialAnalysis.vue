<template>
  <div class="stats-container">
    <h1>财务统计</h1>
    <div class="stat-section">
      <div class="stat-box">
        <h2>总收入</h2>
        <div class="stat-value">{{ animatedTotalRevenue }} 元</div>
      </div>
      <div class="stat-box">
        <h2>总支出</h2>
        <div class="stat-value">{{ animatedTotalExpenses }} 元</div>
      </div>
      <div class="stat-box">
        <h2>净利润</h2>
        <div class="stat-value">{{ animatedNetProfit }} 元</div>
      </div>
      <div class="stat-box">
        <h2>平均每笔交易</h2>
        <div class="stat-value">{{ animatedAverageTransaction }} 元</div>
      </div>
    </div>
    <div class="chart-section">
      <div class="chart-box">
        <h2>月收入支出折线图</h2>
        <canvas id="lineChart"></canvas>
      </div>
      <div class="chart-box">
        <h2>支出类型饼图</h2>
        <canvas id="pieChart"></canvas>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

export default {
  setup() {
    const totalRevenue = ref(50000);
    const totalExpenses = ref(20000);
    const netProfit = ref(30000);
    const averageTransaction = ref(500);

    const animatedTotalRevenue = ref(0);
    const animatedTotalExpenses = ref(0);
    const animatedNetProfit = ref(0);
    const animatedAverageTransaction = ref(0);

    const lineChartData = {
      labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
      datasets: [
        {
          label: '收入',
          data: [8000, 7500, 9000, 8500, 9500],
          borderColor: 'rgba(75, 192, 192, 1)',
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
        },
        {
          label: '支出',
          data: [4000, 5000, 6000, 5500, 6500],
          borderColor: 'rgba(255, 99, 132, 1)',
          backgroundColor: 'rgba(255, 99, 132, 0.2)',
        }
      ]
    };

    const pieChartData = {
      labels: ['工资', '材料', '租金', '其他'],
      datasets: [
        {
          data: [8000, 6000, 5000, 3000],
          backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0'],
          hoverBackgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0']
        }
      ]
    };

    let lineChartInstance = null;
    let pieChartInstance = null;

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
      const ctx = document.getElementById('lineChart').getContext('2d');
      if (lineChartInstance) {
        lineChartInstance.destroy();
      }
      lineChartInstance = new Chart(ctx, {
        type: 'line',
        data: lineChartData,
        options: {
          responsive: true,
          animation: {
            duration: 2000,
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

    const drawPieChart = () => {
      const ctx = document.getElementById('pieChart').getContext('2d');
      if (pieChartInstance) {
        pieChartInstance.destroy();
      }
      pieChartInstance = new Chart(ctx, {
        type: 'pie',
        data: pieChartData,
        options: {
          responsive: true,
          animation: {
            duration: 2000,
            easing: 'easeInOutQuad'
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
      const duration = 2000;
      animateValue(animatedTotalRevenue, totalRevenue.value, duration);
      animateValue(animatedTotalExpenses, totalExpenses.value, duration);
      animateValue(animatedNetProfit, netProfit.value, duration);
      animateValue(animatedAverageTransaction, averageTransaction.value, duration);
      drawLineChart();
      drawPieChart();
    });

    onBeforeUnmount(() => {
      if (lineChartInstance) {
        lineChartInstance.destroy();
      }
      if (pieChartInstance) {
        pieChartInstance.destroy();
      }
    });

    return {
      animatedTotalRevenue,
      animatedTotalExpenses,
      animatedNetProfit,
      animatedAverageTransaction
    };
  }
};
</script>

<style scoped>
.stats-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #ffffff; /* 添加背景色 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
}

.stat-section {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.stat-box {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 200px;
}

.stat-box h2 {
  font-size: 1.2em;
  color: #555;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 2em;
  font-weight: bold;
  color: #333;
}

.chart-section {
  display: flex;
  justify-content: space-around;
}

.chart-box {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 45%;
}

.chart-box h2 {
  font-size: 1.2em;
  color: #555;
  margin-bottom: 20px;
}

canvas {
  display: block;
  margin: auto;
  width: 100%;
  height: 100%;
}

svg {
  display: block;
  margin: auto;
  width: 100%;
  height: 100%;
}
</style>
