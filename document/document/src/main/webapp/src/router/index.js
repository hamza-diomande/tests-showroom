import { createRouter, createWebHashHistory } from 'vue-router';
import DummyView from '@/views/DummyView.vue';
import LoginView from '@/views/LoginView.vue';
import UsersView from '@/views/UsersView.vue';
import HomeView from '../views/HomeView.vue';

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
    path: '/users',
    name: 'usersView',
    component: UsersView,
    meta: { requiresAuth: true },

  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
