<template>
  <div class="stats-container">
    <h1>用户统计</h1>
    <div class="stat-section">
      <div class="stat-box">
        <h2>页面访问统计</h2>
        <div class="stat-value">{{ animatedVisitCount }} 次</div>
      </div>
      <div class="stat-box">
        <h2>注册用户总数</h2>
        <div class="stat-value">{{ animatedTotalUsers }} 人</div>
      </div>
      <div class="stat-box">
        <h2>月均新增用户数</h2>
        <div class="stat-value">{{ animatedMonthlyNewUsers }} 人</div>
      </div>
    </div>
    <div class="chart-section">
      <div class="chart-box">
        <h2>每月访问网站的数量折线图</h2>
        <canvas id="lineChart"></canvas>
      </div>
      <div class="chart-box">
        <h2>用户评价词云</h2>
        <svg id="wordCloud"></svg>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { Chart, registerables } from 'chart.js';
import axios from 'axios';
import * as d3 from 'd3';
import cloud from 'd3-cloud';

Chart.register(...registerables);

export default {
  setup() {
    const visitCount = ref(0);
    const totalUsers = ref(0);
    const monthlyNewUsers = ref(0);
    const visitData = ref([]);
    const wordData = ref([]);

    const animatedVisitCount = ref(0);
    const animatedTotalUsers = ref(0);
    const animatedMonthlyNewUsers = ref(0);

    let chartInstance = null;

    const fetchData = async () => {
      try {
        const visitResponse = await axios.get('/demo-api/visit-count');
        const userResponse = await axios.get('/demo-api/user-stats');
        const wordCloudResponse = await axios.get('/demo-api/word-cloud');

        visitCount.value = visitResponse.data.totalVisits;
        totalUsers.value = userResponse.data.totalUsers;
        monthlyNewUsers.value = userResponse.data.monthlyNewUsers;
        visitData.value = userResponse.data.visitData;
        wordData.value = wordCloudResponse.data;

        animateValue(animatedVisitCount, visitCount.value, 700);
        animateValue(animatedTotalUsers, totalUsers.value, 700);
        animateValue(animatedMonthlyNewUsers, monthlyNewUsers.value, 700);
        drawLineChart();
        drawWordCloud();
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
          labels: ['项目1', '项目2', '项目3', '项目4', '项目5'],
          datasets: [{
            label: '每月访问量',
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

    const drawWordCloud = () => {
      d3.select('#wordCloud').selectAll('*').remove();
      const layout = cloud()
        .size([500, 300])
        .words(wordData.value)
        .padding(5)
        .rotate(() => ~~(Math.random() * 2) * 90)
        .font('Impact')
        .fontSize(d => d.size)
        .on('end', draw);

      layout.start();

      function draw(words) {
        d3.select('#wordCloud')
          .attr('width', layout.size()[0])
          .attr('height', layout.size()[1])
          .append('g')
          .attr('transform', `translate(${layout.size()[0] / 2},${layout.size()[1] / 2})`)
          .selectAll('text')
          .data(words)
          .enter().append('text')
          .style('font-size', d => `${d.size}px`)
          .style('font-family', 'Impact')
          .style('fill', (d, i) => d3.schemeCategory10[i % 10])
          .attr('text-anchor', 'middle')
          .attr('transform', d => `translate(${d.x},${d.y})rotate(${d.rotate})`)
          .text(d => d.text)
          .on('mouseover', function (event, d) {
            d3.select(this).transition()
              .duration(200)
              .style('font-size', `${d.size * 1.5}px`)
              .style('fill', 'red');
          })
          .on('mouseout', function (event, d) {
            d3.select(this).transition()
              .duration(200)
              .style('font-size', `${d.size}px`)
              .style('fill', d3.schemeCategory10[d.size % 10]);
          });
      }
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
      animatedMonthlyNewUsers
    };
  }
};
</script>

<style scoped>
/* Styles remain unchanged */
.stats-container {
  max-width: 1200px;
  margin: auto;
  padding: 20px;
  font-family: 'Roboto', sans-serif;
  background-color: #fdfdfd;
  border-radius: 8px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease-in-out;
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
  border-radius: 8px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 280px;
}

.stat-box h2 {
  font-size: 1.2em;
  color: #555;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 2em;
  font-weight: bold;
  color: #007BFF;
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
