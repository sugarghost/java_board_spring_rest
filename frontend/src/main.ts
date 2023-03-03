import { createApp } from "vue";
import axios from "axios";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
// import { loadFonts } from './plugins/webfontloader';

// loadFonts();

createApp(App)
  // axios를 전역으로 사용하기 위해 Vue 인스턴스에 등록
  .provide("axios", axios)
  // 주소 매핑을 위해 router 사용
  .use(router)
  // 상태 관리를 위해 store 사용
  .use(store)
  // 디자인을 위해 vuetify 사용
  .use(vuetify)
  .mount("#app");
