import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import ArticleListView from "../views/ArticleListView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "list",
    component: ArticleListView,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
