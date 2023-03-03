import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import ArticleListView from "../views/ArticleListView.vue";
import ArticleWriteView from "../views/ArticleWriteView.vue";
import ArticleDetailView from "../views/ArticleDetailView.vue";
import ArticleModifyView from "../views/ArticleModifyView.vue";

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
  {
    path: "/modify/:articleId",
    name: "modify",
    component: ArticleModifyView,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
