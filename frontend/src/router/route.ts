import { createRouter, createWebHistory } from 'vue-router'
import ArticleListView from '@/views/ArticleListView.vue'
import ErrorView from '@/views/ErrorView.vue'

  

  const router = createRouter({
    history : createWebHistory(),
    routes : [ // path별 component를 추가한다.
        { path : "/", name : "list", component : ArticleListView },
        {
            path : "/:pathMatch(.*)",
            name : "not-found",
            component : ErrorView
        }
    ]
});

export default router;