/* eslint-disable import/no-extraneous-dependencies */
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  test: {
    include: ['**/*.spec.js'],
    environment: 'happy-dom',
    setupFiles: './vitest.setup.js',
  },
});
