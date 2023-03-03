import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import ArticleListView from "../views/ArticleListView.vue";
import ArticleListWrite from "../views/ArticleListWrite.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "list",
    component: ArticleListView,
  },
  {
    path: "/write",
    name: "write",
    component: ArticleListWrite,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
