<template>
  <div class="system-log">
    <h1>系统日志</h1>
    <v-container>
      <v-row>
        <v-col
          v-for="log in logs"
          :key="log.Id"
          cols="12"
          class="d-flex justify-start"
        >
          <v-card
            class="mx-auto"
            :style="{ backgroundColor: getCardBackgroundColor(log.logLevel) }"
            :subtitle="formatDate(log.createdAt)"
            width="600"
            hover
            @click="selectLog(log)"
          >
            <template v-slot:title>
              <span :style="{ color: getCardTitleColor(log.logLevel) }" class="font-weight-black">{{ getLogTypeLabel(log.logLevel) }}</span>
            </template>

            <v-card-text :style="{ color: getCardTextColor(log.logLevel) }">
              {{ log.message }}
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import axios from 'axios'; // 引入 axios

export default {
  name: 'SystemLog',
  data() {
    return {
      logs: [], // Array to hold logs
      selectedLog: null // Currently selected log
    };
  },
  methods: {
    // Fetch logs from your API
    async fetchLogs() {
      try {
        // Replace with your actual backend API endpoint
        const response = await axios.get('/demo-api/Log');
        this.logs = response.data; // 通过 API 返回的数据填充 logs 数组
      } catch (error) {
        console.error("Error fetching logs:", error);
      }
    },
    selectLog(log) {
      this.selectedLog = log;
    },
    formatDate(date) {
      const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
      return new Date(date).toLocaleDateString(undefined, options);
    },
    getCardBackgroundColor(logLevel) {
      // Return colors with transparency for different log levels
      switch (logLevel.toLowerCase()) {
        case 'warning':
          return 'rgba(255, 165, 0, 0.2)'; // Orange with transparency
        case 'dangerous':
          return 'rgba(255, 0, 0, 0.2)'; // Red with transparency
        case 'safe':
        default:
          return 'rgba(0, 128, 0, 0.2)'; // Green with transparency
      }
    },
    getCardTextColor() {
      // Make all text inside the card white
      return 'black';
    },
    getCardTitleColor(logLevel) {
      // Return the same color as the card's main color for the title
      switch (logLevel.toLowerCase()) {
        case 'warning':
          return 'rgba(247, 136, 0, 1)'; // Orange
        case 'dangerous':
          return 'rgba(199, 1, 0, 1)'; // Red
        case 'safe':
        default:
          return 'rgba(0, 128, 0, 1)'; // Green
      }
    },
    getLogTypeLabel(logLevel) {
      switch (logLevel.toLowerCase()) {
        case 'warning':
          return 'Warning';
        case 'dangerous':
          return 'Dangerous';
        case 'safe':
        default:
          return 'Safe';
      }
    }
  },
  mounted() {
    this.fetchLogs(); // Fetch logs when the component is mounted
  }
};
</script>

<style scoped>
.system-log {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

h1 {
  text-align: center;
  color: #333;
}

.log-list {
  margin-bottom: 20px;
}

.log-details {
  border: 1px solid #ddd;
  padding: 10px;
  border-radius: 8px;
  background-color: #fff;
}

pre {
  background-color: #f0f0f0;
  padding: 10px;
  border-radius: 4px;
  white-space: pre-wrap; /* Allows text wrapping */
}
</style>
