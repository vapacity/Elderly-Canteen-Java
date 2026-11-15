const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    proxy: {
      '/demo-api': {
        target: 'http://127.0.0.1:18180',
        changeOrigin: true,
      },
    },
  },
});

