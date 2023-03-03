<template>
  <v-container fluid>
    <h1>자유게시판 - 목록</h1>
    <v-form>
      <v-row class="border align-content-center p-2">
        <v-col cols="1" class="align-self-center">등록일</v-col>
        <v-col cols="2">
          <v-text-field type="date" v-model="searchParams.startDate" outlined color="primary" />
        </v-col>
        <v-col cols="1" class="align-self-center text-center">~</v-col>
        <v-col cols="2">
          <v-text-field type="date" v-model="searchParams.endDate" outlined color="primary" />
        </v-col>
        <v-col cols="2">
          <v-select v-model="searchParams.categoryId" :items="categories" item-title="name" item-value="categoryId"
            single-line label="Select a category"></v-select>
        </v-col>
        <v-col cols="2">
          <v-text-field v-model="searchParams.searchWord" placeholder="제목, 내용, 작성자 검색" outlined color="primary" />
        </v-col>
        <v-col cols="2">
          <input type="hidden" name="pageNum" value="1" />
          <v-btn color="primary" @click="searchArticles">검색</v-btn>
        </v-col>
      </v-row>
      <v-row>
        <v-col>총 {{ totalItems }}건</v-col>
      </v-row>
      <v-row>
        <v-col>

          <v-data-table-server :headers="headers" :items="articles" item-value="name" class="elevation-1"
            :items-per-page="searchParams.articlePerPage" :items-length="totalItems" :loading="loading"
            @update:options="updateList">

            <template v-slot:[`column.name`]="{ column }">
              {{ column.title.toUpperCase() }}
            </template>

            <template #item="{ item }">
              <tr>
                <td>{{ item.raw.categoryName }}</td>
                <td>{{ item.raw.title }}</td>
                <td>{{ item.raw.writer }}</td>
                <td>{{ item.raw.viewCount }}</td>
                <td>{{ formatDate(item.raw.createdDate) }}</td>
                <td>{{ formatDate(item.raw.modifiedDate) }}</td>
              </tr>
            </template>
          </v-data-table-server>
        </v-col>
      </v-row>
      <v-row>
        <v-col class="d-flex justify-end">
          <v-btn color="primary" @click="$router.push('/write')">글쓰기</v-btn>
        </v-col>
      </v-row>
    </v-form>
  </v-container>
</template>

<script>
import { ref, computed, onBeforeMount, inject } from "vue";
import { useStore } from "vuex";
import { useRouter, useRoute } from "vue-router"

export default {
  name: "ArticleList",

  setup() {
    // 공통 요소
    const axios = inject("axios");
    const store = useStore();
    const route = useRoute()

    // if route have query param, update searchParams and store
    if (route.query) {
      const searchParamsQuery = store.getters.searchParams;

      if (route.query.pageNum) {
        searchParamsQuery.pageNum = Number(route.query.pageNum);
      }
      if (route.query.articlePerPage) {
        searchParamsQuery.articlePerPage = Number(route.query.articlePerPage);
      }
      if (route.query.categoryId) {
        searchParamsQuery.categoryId = Number(route.query.categoryId);
      }
      if (route.query.startDate) {
        searchParamsQuery.startDate = route.query.startDate;
      }
      if (route.query.endDate) {
        searchParamsQuery.endDate = route.query.endDate;
      }
      if (route.query.searchWord) {
        searchParamsQuery.searchWord = route.query.searchWord;
      }
      store.commit("updateSearchParams", searchParamsQuery);
    }

    const headers = [
      { title: "Category", value: "categoryName" },
      { title: "Title", value: "title" },
      { title: "Writer", value: "writer" },
      { title: "Views", value: "viewCount" },
      { title: "Created", value: "createdDate", align: "center" },
      { title: "Modified", value: "modifiedDate", align: "center" },
    ];


    // category 관련요소
    const categories = ref([]);
    const getCategories = async () => {
      try {
        const response = await axios.get("/v1/categories", {});

        categories.value = response.data.map((categoryData) => ({
          categoryId: categoryData.categoryId,
          name: categoryData.name,
        }))

        console.log(categories.value);
      } catch (error) {
        console.error(error);
      }
    };

    // articleList 관련요소
    const articles = ref([]);
    const totalItems = ref(0);
    const searchParams = ref(store.getters.searchParams);
    const loading = ref(false);

    const getArticleList = async () => {
      const response = await axios.get("/v1/articles", {
        params: {
          ...searchParams.value,
        },

      });
      articles.value = response.data.map((articleData) => ({
        articleId: articleData.articleId,
        title: articleData.title,
        writer: articleData.writer,
        viewCount: articleData.viewCount,
        categoryName: articleData.categoryName,
        createdDate: articleData.createdDate,
        modifiedDate: articleData.modifiedDate,
      }));
      totalItems.value = Number(response.headers["x-total-count"]);
      store.commit("updateSearchParams", searchParams.value);
      loading.value = false;
    }

    const updateList = ({ page, itemsPerPage, sortBy }) => {
      searchParams.value.pageNum = page;
      searchParams.value.articlePerPage = itemsPerPage;
      getArticleList();
    }

    // articleList에서 날짜 표시할때 사용

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

    // search 관련
    const searchArticles = () => {
      searchParams.value.pageNum = 1;
      getArticleList();
    };

    onBeforeMount(() => {
      getCategories();
      getArticleList();
    });
    return {
      headers,
      articles,
      totalItems,
      searchParams,
      loading,
      getArticleList,
      formatDate,
      updateList,
      getCategories,
      categories,
      searchArticles,
    };
  },
};
</script>
