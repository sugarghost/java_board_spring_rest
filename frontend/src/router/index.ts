import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import ArticleListView from "../views/ArticleListView.vue";
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
    path: "/view/:articleId",
    name: "view",
    component: ArticleDetailView,
  },
  {
    path: "/write",
    name: "write",
    component: () => import(/* webpackChunkName: "write" */ "../views/ArticleWriteView.vue"),
  },
  {
    path: "/modify/:articleId",
    name: "modify",
    component: () => import(/* webpackChunkName: "modify" */ "../views/ArticleModifyView.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
