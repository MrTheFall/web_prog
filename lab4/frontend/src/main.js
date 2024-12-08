import { createApp } from 'vue';
import App from './App.vue';
import router from "@/router";
import Axios from "axios";

const app = createApp(App);

app.config.globalProperties.$axios = Axios.create({ baseURL: '/' });

app.use(router);

app.mount('#app');