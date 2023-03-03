const { defineConfig } = require("@vue/cli-service");

module.exports = defineConfig({
  transpileDependencies: true,
  // Spring Server에 배포하기 위해 설정
  outputDir: "../src/main/resources/static",

  // vue cli에서 작업하면서 Spring과 연결해 변경사항을 즉각적으로 확인하며 개발하기 위해 설정
  devServer: {
    proxy: "http://localhost:8081",
  },

  pluginOptions: {
    vuetify: {
      // https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vuetify-loader
    },
  },
});
