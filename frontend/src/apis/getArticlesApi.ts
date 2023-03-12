import { ref, Ref } from "vue";

import axios from "../axios/axios";
import type { IArticle, ISearchParams } from "../types/article";

export function getArticlesApi() {
  const articles: Ref<IArticle[]> = ref([]);

  const articlesAreLoading = ref(false);

  const totalArticleCount = ref(0);

  const getArticles = async (searchParams: ISearchParams) => {
    articlesAreLoading.value = true;
    try {
      const response = await axios.get("/articles", {
        params: {
          ...searchParams,
        },
      });
      articles.value = response.data;
      totalArticleCount.value = Number(response.headers["x-total-count"]);
    } catch (err) {
      console.log(err);
    } finally {
      articlesAreLoading.value = false;
    }
  };

  return {
    articles,
    getArticles,
    articlesAreLoading,
    totalArticleCount,
  };
}

export function getArticleApi() {
  const article: Ref<IArticle> = ref({} as IArticle);

  const getArticle = async (articleId: number) => {
    try {
      const response = await axios.get(`/articles/${articleId}`, {});
      article.value = response.data;
    } catch (error) {
      console.error(error);
    }
  };

  return {
    article,
    getArticle,
  };
}
