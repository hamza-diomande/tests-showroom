/* eslint-disable import/no-extraneous-dependencies */
import { createApp } from 'vue';
import PrimeVue from 'primevue/config';

import http from '@/http';
import DummyService from '@/services/DummyService';
import AuthService from '@/services/AuthService';
import authStore from '@/store/modules/auth.store';
import authGuard from '@/router/guards/auth.guard';
import documentStore from '@/store/modules/document.store';
import unauthorizedInterceptor from '@/http/interceptors/unauthorized.interceptor';

import { createBootstrap } from 'bootstrap-vue-next';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css';

import 'primevue/resources/themes/saga-blue/theme.css';
import 'primevue/resources/primevue.min.css';
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';
import UserService from '@/services/UserService';
import DocumentService from './services/DocumentService';
import App from './App.vue';
import store from './store';
import router from './router';

const API_URL = process.env.VUE_APP_API_URL || 'http://localhost:1987';
const dummyService = new DummyService(http, API_URL);
const authService = new AuthService(http, API_URL);
const userService = new UserService(http, API_URL);
const documentService = new DocumentService(http, API_URL);

http.interceptors.response.use(
  (response) => response,
  unauthorizedInterceptor(store),
);
router.beforeEach(authGuard(store));
store.registerModule('auth', authStore(http, router, authService, userService));
store.registerModule('genDoc', documentStore(http, router, documentService));

const app = createApp(App)
  .use(store)
  .use(router)
  .use(PrimeVue)
  .use(createBootstrap({ components: true, directives: true })); // Change this line;
  // .use(IconsPlugin);

app.config.globalProperties.$dummyService = dummyService;
app.config.globalProperties.$authService = authService;
app.config.globalProperties.$userService = userService;
app.config.globalProperties.$documentService = documentService;

app.mount('#app');
