import { createApp } from 'vue';
import PrimeVue from 'primevue/config';

import http from '@/http';
import DummyService from '@/services/DummyService';
import AuthService from '@/services/AuthService';
import authStore from '@/store/modules/auth.store';
import authGuard from '@/router/guards/auth.guard';
import unauthorizedInterceptor from '@/http/interceptors/unauthorized.interceptor';

import 'primevue/resources/themes/saga-blue/theme.css';
import 'primevue/resources/primevue.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';
import UserService from '@/services/UserService';
import App from './App.vue';
import store from './store';
import router from './router';

const API_URL = process.env.VUE_APP_API_URL || 'http://localhost:1987';
const dummyService = new DummyService(http, API_URL);
const authService = new AuthService(http, API_URL);
const userService = new UserService(http, API_URL);

http.interceptors.response.use(
  (response) => response,
  unauthorizedInterceptor(store),
);
router.beforeEach(authGuard(store));
store.registerModule('auth', authStore(http, router, authService, userService));

const app = createApp(App)
  .use(store)
  .use(router)
  .use(PrimeVue);

app.config.globalProperties.$dummyService = dummyService;
app.config.globalProperties.$authService = authService;
app.config.globalProperties.$userService = userService;

app.mount('#app');
