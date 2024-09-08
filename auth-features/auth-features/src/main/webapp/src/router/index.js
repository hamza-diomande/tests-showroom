import { createRouter, createWebHashHistory } from 'vue-router';
import DummyView from '@/views/DummyView.vue';
import LoginView from '@/views/LoginView.vue';
import HomeView from '@/views/HomeView.vue';
import RegisterView2FA from '@/views/RegisterView2FA.vue';
import SetUp2FaView from '@/views/SetUp2FaView.vue';
import Login2FaView from '@/views/Login2FaView.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: { requiresAuth: true },
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/dummy',
    name: 'dummy',
    component: DummyView,
    meta: { requiresAuth: true },
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { requiresNotAuth: true },
  },
  {
    path: '/register2Fa',
    name: 'registerFa',
    component: RegisterView2FA,
    meta: { requiresNotAuth: true },
  },
  {
    path: '/setup-2fa', // Adjusted route path
    name: 'QrCode', // Adjusted route name
    component: SetUp2FaView,
    meta: { requiresNotAuth: true },
  },
  {
    path: '/login-2fa', // Adjusted route path
    name: 'login2Fa', // Adjusted route name
    component: Login2FaView,
    meta: { requiresNotAuth: true },
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
