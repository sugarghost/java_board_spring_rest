import { createApp } from 'vue'
import App from './App.vue'
// import vuetify from './plugins/vuetify'
import { loadFonts } from './plugins/webfontloader'
import router from '@/router/route'
import axios from 'axios' // [axios]

const app = createApp(App);

// [axios / http 통신]
app.config.globalProperties.$axios = axios; 


loadFonts()

app
  // .use(vuetify)
  .use(router)
  .mount('#app')
