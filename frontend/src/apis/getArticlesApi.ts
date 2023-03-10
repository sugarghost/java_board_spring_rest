import { ref, Ref } from "vue";

import { subStringWithSkipMark } from "@/assets/common";
import axios from "../axios/axios";
import usePagination from "../assets/pagination";
import type { IArticle, ISearchParams } from "../types/article";

export default function getArticlesApi(
  currentPage: Ref<number>,
  articlesPerPage?: Ref<number>,
  pagesPerBlock?: Ref<number>
) {
  const articles: Ref<IArticle[]> = ref([]);

  const articlesAreLoading = ref(false);

  const totalArticleCount = ref(0);

  const { totalPageCount, pageStart, pageEnd } = usePagination<IArticle>({
    rowsPerPage: articlesPerPage,
    pagesPerBlock,
    totalArticleCount,
    currentPage,
  });

  const getArticles = async (searchParams: ISearchParams) => {
    articlesAreLoading.value = true;
    try {
      const response = await axios.get("/articles", {
        params: {
          ...searchParams,
        },
      });
      articles.value = response.data.map((articleData: IArticle) => ({
        articleId: articleData.articleId,
        title:
          subStringWithSkipMark(articleData.title, 80) + (articleData.isFileExist ? " 📎" : ""),
        writer: articleData.writer,
        viewCount: articleData.viewCount,
        categoryName: articleData.categoryName,
        createdDate: articleData.createdDate,
        modifiedDate: articleData.modifiedDate,
        isFileExist: articleData.isFileExist,
      }));
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
    totalPageCount,
    totalArticleCount,
    pageStart,
    pageEnd,
  };
}
