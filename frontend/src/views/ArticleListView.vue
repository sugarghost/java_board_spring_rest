<template>
<div class="container-fluid">
  <h1>자유게시판 - 목록</h1>
  <form method="get" action="list.do">
    <div class="row border align-content-center p-2">
      <div class="col-1 align-self-center">
        등록일
      </div>
      <div class="col-2">
        <input type="date" class="form-control" name="startDate"
        v-model="searchParams.startDate">
      </div>
      <div class="col-1 align-self-center text-center">
        ~
      </div>
      <div class="col-2">
        <input type="date" class="form-control" name="endDate" v-model="searchParams.endDate">
      </div>
      <div class="col-2">
        <select class="form-control" name="categoryId"  v-model="searchParams.categoryId">
          <option value="0">카테고리</option>
        <option v-for="category in categories" :value="category.categoryId" :key="category.categoryId">{{ category.name }}
        </option>
        </select>
      </div>
      <div class="col-2">
        <input type="text" class="form-control" name="searchWord"
        placeholder="제목, 내용, 작성자 검색"
        v-model="searchParams.searchWord">
      </div>
      <div class="col-2">
        <input type="hidden" name="pageNum" value="1">
        <input type="button" class="btn btn-primary" @click="searchArticles" value="검색">
      </div>
    </div>
    <div class="row">
      <div class="col-12">
        총 0건
      </div>
    </div>
    <div class="row">
      <div class="col-12">

        <table class="table">
          <thead>
          <tr>
            <td>카테고리</td>
            <td>제목</td>
            <td>작성자</td>
            <td>조회수</td>
            <td>등록 일시</td>
            <td>수정 일시</td>
          </tr>
          </thead>

          <tbody v-if="articles.length < 1">
          <tr>
            <td colspan="6">검색 결과가 없습니다.</td>
          </tr>
          </tbody>
          <tbody v-else>
          <tr v-for="article in articles" :key="article.id">
            <td>
              {{ article.categoryName }}
            </td>
            <td>
              <a>
                {{ article.title }}
              </a>
              <span>
                                    {{(article.isFileExist) ? "📎" : ""}}
                </span>
            </td>
            <td>{{article.writer}}
            </td>
            <td>{{article.viewCount}}
            </td>
            <td>
              {{(article.createdDateValid) ? formatDate(article.createdDate)
              : '-' }}
            </td>
            <td>
              {{(article.modifiedDateValid) ? formatDate(article.modifiedDate)
              : '-' }}
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
<div class="row">
  <div class="col-12">
    <ul class="pagination d-flex justify-content-center">
      <template v-if="searchParams.pageNum != 1">
        <li class="page-item">
          <button class="page-link" @click="movePage(1)">
            &lt;&lt;
          </button>
        </li>
        <li class="page-item">
          <button class="page-link" @click="movePage(searchParams.pageNum-1)">
            &lt;
          </button>
        </li>
      </template>
      <template v-for="pageNumber in pageNumbers" :key="pageNumber">

        <li class="page-item" :class="{ active: pageNumber === searchParams.pageNum }">
        <button class="page-link" @click="movePage(pageNumber)"></button>
      </li>
      </template>
      <template v-if="searchParams.pageNum < totalPages">
        <li class="page-item">
          <button class="page-link" @click="movePage(searchParams.pageNum+1)">
            &gt;
          </button>
        </li>
        <li class="page-item">
          <button class="page-link" @click="movePage(totalPages)">
            &gt;&gt;
          </button>
        </li>
      </template>
    </ul>
  </div>
</div>
    <div class="row">
      <div class="col-12 d-flex justify-content-end">
        <input type="button" class="btn btn-primary" @click="$router.push('/write')"
        value="글쓰기"/>
      </div>
    </div>
  </form>
</div>
</template>

<script>
import { ref, computed, onBeforeMount, inject } from "vue";
import {useStore} from "vuex";

export default {
  name: "ArticleList",

  setup() {
    const axios = inject("axios");
    const store = useStore();
    
    const articles = ref([]);
    const categories = ref([]);
    const pagination = ref({
      totalCount: 1,
      pageSize: 10,
      pageBlockSize: 10,
    });
    const searchParams = ref(store.getters.searchParams);

    const getCategories = async () => {
      try {
        const response = await axios.get("/v1/categories", {
        });
        console.log(response)
        categories.value = response.data;
      } catch (error) {
        console.error(error);
      }
    };
    const searchArticles = async () => {
      try {
        const response = await axios.get("/v1/articles", {
          params: {
            ...searchParams.value
          }
        });
        console.log(response)
        articles.value = response.data;
        pagination.value.totalCount = response.headers["x-total-count"];
        store.commit("updateSearchParams", searchParams.value)
      } catch (error) {
        console.error(error);
      }
    };

    const formatDate = (date) => {
      if (!date) {
        return "-";
      }
      const formatedDate = new Date(date);

      const options = { 
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
      };
      return formatedDate.toLocaleDateString("ko-KR", options);
    };

    
    const totalPages = computed(() => Math.ceil(pagination.value.totalCount / searchParams.value.articlesPerPage))
    const pageNumbers = computed(() => {
      const pages = []
      let startPage = Math.max(1, searchParams.value.pageNum - Math.floor(pagination.value.pageBlockSize / 2))
      const endPage = Math.min(pagination.value.totalPages, startPage + pagination.value.pageBlockSize - 1)
      if (endPage - startPage < pagination.value.pageBlockSize - 1) {
        startPage = Math.max(1, endPage - pagination.value.pageBlockSize + 1)
      }
      for (let i = startPage; i <= endPage; i+=1) {
        pages.push(i)
      }
      return pages
    })

    const movePage = (pageNum) => {
      searchParams.value.pageNum = pageNum;
      searchArticles();
    };

    onBeforeMount(() => {
      getCategories();
      searchArticles();
    });

    return {
      articles,
      categories,
      searchParams,
      pagination,
      totalPages,
      pageNumbers,
      searchArticles,
      formatDate
    };
  }

};
</script>