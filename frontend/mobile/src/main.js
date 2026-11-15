import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
// 计算设置rem的基准值，即html的font-size
import 'amfe-flexible'
import { Dialog, Field, CellGroup, Toast } from 'vant'
// vant
const app = createApp(App)

// 路由
app.use(router)

// veux
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate, { expire: '1d' })
app.use(pinia)

// 挂载
app.mount('#app')

app.use(Dialog)
app.use(Field)
app.use(CellGroup)
app.use(Toast)
