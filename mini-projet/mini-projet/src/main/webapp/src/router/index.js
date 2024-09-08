import { createRouter, createWebHashHistory } from 'vue-router';
import DummyView from '@/views/DummyView.vue';
import LoginView from '@/views/LoginView.vue';
import ProfileView from '@/views/ProfileView.vue';
import UsersView from '@/views/UsersView.vue';
import HomeView2 from '@/views/HomeView2.vue';
import LoginCaptcheckView from '@/views/LoginWithCaptcheckView.vue';
import UserForm from '@/views/UserForm.vue';

/* const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView2,
    meta: { requiresNotAuth: true },
  },
  {
    path: '/auth',
    name: 'auth',
    component: { template: '<router-view />' }, // Placeholder for nested routes
    meta: { requiresNotAuth: true },
    redirect: { name: 'login' }, // Automatically redirect to login
    children: [
      {
        path: 'login',
        name: 'login',
        component: LoginView,
      },
      {
        path: 'loginCaptcheck',
        name: 'loginCaptcheck',
        component: LoginCaptcheckView,
      },
      {
        path: 'profile',
        name: 'profile',
        component: ProfileView,
        meta: { requiresAuth: true },
      },
    ],
  },
  {
    path: '/users',
    name: 'users',
    component: UsersView,
    meta: { requiresAuth: true },
  },
  {
    path: '/new-user',
    name: 'userForm',
    component: UserForm,
    meta: { requiresAuth: true },
  },
  {
    path: '/dummy',
    name: 'dummy',
    component: DummyView,
    meta: { requiresAuth: true },
  },
  {
    path: '/about',
    name: 'about',
    component: () => import(/* webpackChunkName: "about" */ /* '../views/AboutView.vue'),
    meta: { requiresAuth: true },
  },
]; */

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView2,
    meta: { requiresNotAuth: true },
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
    meta: { requiresNotAuth: true },
  },
  {
    path: '/profile',
    name: 'profile',
    component: ProfileView,
    meta: { requiresAuth: 'true' },

  },
  {
    path: '/users',
    name: 'Users',
    component: UsersView,
    meta: { requiresNotAuth: true },
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { requiresNotAuth: true },
  },
  {
    path: '/loginCaptcheck',
    name: 'loginCaptcheck',
    component: LoginCaptcheckView,
    meta: { requiresNotAuth: true },
  },
  {
    path: '/new-user/:id?',
    name: 'userForm',
    component: UserForm,
    props: (route) => ({ id: route.params.id }),
    meta: { requiresNotAuth: true },
  },
  /* {
    path: '/edit-user/:id',
    name: 'editUserForm',
    component: UserForm,
    meta: { requiresNotAuth: true },
    props: true, // Passe le paramètre id en tant que prop à la vue
  }, */
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
