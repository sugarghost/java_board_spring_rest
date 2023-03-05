const { defineConfig } = require("@vue/cli-service");

module.exports = defineConfig({
  transpileDependencies: true,
  // Spring Server에 배포하기 위해 설정
  outputDir: "../src/main/resources/static",

  // vue cli에서 작업하면서 Spring과 연결해 변경사항을 즉각적으로 확인하며 개발하기 위해 설정
  // TODO: 차라리 URL 환경변수로 넣고 쓰는방법이 나음 다른 서버로 요청을 돌리기 위한 설정이기 때문에(나중에 프록시 많이 쓰게 됨)
  devServer: {
    proxy: "http://localhost:8081",
  },

  pluginOptions: {
    vuetify: {
      // https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vuetify-loader
    },
  },
});
