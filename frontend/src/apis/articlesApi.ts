import {ref, Ref} from "@vue/reactivity";
import axios from "axios";

import {usePagination} from "../assets/pagination";
import {subStringWithSkipMark} from "@/assets/common";
import type {IArticle, ISearchParams} from "../types/article";

export function articlesApi(
  currentPage: Ref<number>,
  rowsPerPage?: Ref<number>,
  pagesPerBlock?: Ref<number>
) {
  const articles: Ref<IArticle[]> = ref([]);

  const articlesAreLoading = ref(false);

  const totalCount = ref(0);

  const {numberOfPagesArray} = usePagination<IArticle>({
    rowsPerPage,
    pagesPerBlock,
    totalCount,
    currentPage,
  });

  const getArticles = async (searchParams: ISearchParams) => {
    articlesAreLoading.value = true;
    try {
      const response = await axios.get("/v1/articles", {
        params: {
          ...searchParams,
        },

      });
      articles.value = response.data.map((articleData: IArticle) => ({
        articleId: articleData.articleId,
        title: subStringWithSkipMark(articleData.title, 80) + (articleData.isFileExist ? " 📎" : ""),
        writer: articleData.writer,
        viewCount: articleData.viewCount,
        categoryName: articleData.categoryName,
        createdDate: articleData.createdDate,
        modifiedDate: articleData.modifiedDate,
        isFileExist: articleData.isFileExist,
      }));
      totalCount.value = Number(response.headers["x-total-count"]);
    } catch (err) {
      console.log(err);
    } finally {
      articlesAreLoading.value = false;
    }
  };

  return {
    articles,
    getArticles: getArticles,
    articlesAreLoading: articlesAreLoading,
    numberOfPagesArray,
    totalCount
  };
}
