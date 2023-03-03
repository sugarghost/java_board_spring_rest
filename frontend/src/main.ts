import { createApp } from "vue";
import axios from "axios";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
// import { loadFonts } from './plugins/webfontloader';

// loadFonts();

createApp(App)
.provide("axios", axios)
  .use(router)
  .use(store)
  .use(vuetify)
  .mount("#app");
