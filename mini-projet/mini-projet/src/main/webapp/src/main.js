/* eslint-disable import/extensions */
import { createApp } from 'vue';
import PrimeVue from 'primevue/config';

import http from '@/http';
import DummyService from '@/services/DummyService';
import AuthService from '@/services/AuthService';
import authStore from '@/store/modules/auth.store';
// import dummyStore from '@/store/modules/dummy.store';
import authGuard from '@/router/guards/auth.guard';
import homeStore from '@/store/modules/home.store';
import userStore from '@/store/modules/user.store';
import unauthorizedInterceptor from '@/http/interceptors/unauthorized.interceptor';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { createBootstrap } from 'bootstrap-vue-next';
import 'bootstrap';
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css';

import 'primevue/resources/themes/saga-blue/theme.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';
import Dialog from 'primevue/dialog';
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
store.registerModule('home', homeStore());
store.registerModule('user', userStore(http, router, userService));

const app = createApp(App)
  .use(store)
  .use(router)
  .use(PrimeVue)
  .use(createBootstrap({ components: true, directives: true }));

app.config.globalProperties.$dummyService = dummyService;
app.config.globalProperties.$authService = authService;
app.config.globalProperties.$userService = userService;

// eslint-disable-next-line vue/multi-word-component-names
app.component('Dialog', Dialog);

app.mount('#app');
