/* eslint-disable import/no-extraneous-dependencies */
// vitest.setup.js
import { config } from '@vue/test-utils';
import { createRouter, createWebHistory } from 'vue-router';

// Définir les routes nécessaires
const routes = [
  { path: '/test-link', name: 'TestLink', component: { template: '<div>Test Page</div>' } },
  { path: '/', name: 'home', component: { template: '<div>Test Page</div>' } },
  { path: '/login', name: 'login', component: { template: '<div>Login Page</div>' } },
  { path: '/feature2', name: 'feature2', component: { template: '<div>Test Page</div>' } },
  { path: '/feature3', name: 'feature3', component: { template: '<div>Test Page</div>' } },
  { path: '/users', name: 'users', component: { template: '<div>Test Page</div>' } },

  // Ajoutez d'autres routes nécessaires ici
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Ajouter le routeur et les stubs globaux
config.global.plugins = [router];
config.global.stubs = {
  // 'router-link': RouterLinkStub,
  template: '<a :href="to" @click="$emit(\'click\', $event)"><slot></slot></a>',
  props: ['to'],
};
