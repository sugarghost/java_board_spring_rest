import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import ArticleListView from "../views/ArticleListView.vue";
import ArticleWriteView from "../views/ArticleWriteView.vue";
import ArticleDetailView from "../views/ArticleDetailView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    redirect(to) {
      return "/list";
    },
  },
  {
    path: "/list",
    name: "list",
    component: ArticleListView,
  },
  {
    path: "/write",
    name: "write",
    component: ArticleWriteView,
  },
  {
    path: "/view/:articleId",
    name: "view",
    component: ArticleDetailView,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
