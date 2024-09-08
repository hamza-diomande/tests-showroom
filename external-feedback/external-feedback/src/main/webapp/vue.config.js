const { defineConfig } = require('@vue/cli-service');
const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
      before(app) {
        app.use(
          '/api',
          createProxyMiddleware({
            target: 'http://localhost:1987',
            changeOrigin: true,
            pathRewrite: { '^/api': '' },
            onProxyReq: (proxyReq) => {
              // Ajouter des en-têtes CSP ici, si nécessaire
              proxyReq.setHeader('Content-Security-Policy', "default-src 'self'; upgrade-insecure-requests");
            },
          })
        );
      },
    },
});
