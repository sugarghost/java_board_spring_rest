import { ref, Ref } from "vue";

import axios from "../axios/axios";
import type { IArticle, ISearchParams } from "../types/article";
// TODO: class로 뺴고 Api 프로바이더로 빼기
export function getArticlesApi() {
  const articles: Ref<IArticle[]> = ref([]);
  // isLoding으로 충분함 또한 일반적으로 Loading을 따로 두지는 않음
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
