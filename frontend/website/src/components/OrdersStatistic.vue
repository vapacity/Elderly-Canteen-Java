<template>
    <div class="stats-container">
      <h1>订单统计</h1>
      <div class="stat-section">
        <div class="stat-box">
          <h2>下单次数统计</h2>
          <div class="stat-value">{{ animatedOrderCount }} 次</div>
        </div>
        <div class="stat-box">
          <h2>总销售额</h2>
          <div class="stat-value">{{ animatedTotalSales }} 元</div>
          <div>月均销售额 {{ averageMonthlySales }} 元</div>
        </div>
        <div class="stat-box">
          <h2>本月销售额</h2>
          <div class="stat-value">{{ animatedMonthlySales }} 元</div>
        </div>
        <div class="stat-box">
          <h2>平均交易金额</h2>
          <div class="stat-value">{{ animatedAverageTransaction }} 元</div>
        </div>
      </div>
      <div class="chart-section">
        <div class="chart-box">
          <h2>每月订单情况折线图</h2>
          <canvas id="lineChart"></canvas>
        </div>
        <div class="chart-box">
          <h2>订单类型</h2>
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
      const orderCount = ref(112);
      const totalSales = ref(3000);
      const monthlySales = ref(1600);
      const averageTransaction = ref(20);
      const averageMonthlySales = ref(1500);
  
      const animatedOrderCount = ref(0);
      const animatedTotalSales = ref(0);
      const animatedMonthlySales = ref(0);
      const animatedAverageTransaction = ref(0);
  
      const lineChartData = {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
        datasets: [
          {
            label: '空单',
            data: [30, 35, 40, 45, 50],
            borderColor: 'rgba(75, 192, 192, 1)',
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
          },
          {
            label: '外卖',
            data: [20, 25, 30, 35, 40],
            borderColor: 'rgba(153, 102, 255, 1)',
            backgroundColor: 'rgba(153, 102, 255, 0.2)',
          },
          {
            label: '堂食',
            data: [50, 55, 60, 65, 70],
            borderColor: 'rgba(255, 159, 64, 1)',
            backgroundColor: 'rgba(255, 159, 64, 0.2)',
          }
        ]
      };
  
      const pieChartData = {
        labels: ['空单', '外卖', '堂食'],
        datasets: [
          {
            data: [120, 80, 100],
            backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
            hoverBackgroundColor: ['#FF6384', '#36A2EB', '#FFCE56']
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
        animateValue(animatedOrderCount, orderCount.value, duration);
        animateValue(animatedTotalSales, totalSales.value, duration);
        animateValue(animatedMonthlySales, monthlySales.value, duration);
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
        orderCount,
        totalSales,
        monthlySales,
        averageTransaction,
        averageMonthlySales,
        animatedOrderCount,
        animatedTotalSales,
        animatedMonthlySales,
        animatedAverageTransaction
      };
    }
  };
  </script>
  
  <style scoped>
  .stats-container {
    max-width: 1200px;
    margin: auto;
    padding: 20px;
    font-family: Arial, sans-serif;
    background-color: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
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
  