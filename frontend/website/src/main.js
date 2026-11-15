// src/main.js
import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store"; // 引入 Vuex Store
// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
const vuetify = createVuetify({
    components,
    directives,
})
const app = createApp(App).use(vuetify);

app.use(router);
app.use(store); // 使用 Vuex Store

app.mount("#app");
