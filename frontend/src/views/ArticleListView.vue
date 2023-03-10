<template>
  <v-container fluid>
    <h1 class="mb-4">자유게시판 - 목록</h1>
    <v-row class="align-content-center p-2">
      <v-col cols="1" class="align-self-center">등록일</v-col>
      <v-col cols="2">
        <v-text-field type="date" v-model="searchParams.startDate" outlined color="primary" />
      </v-col>
      <v-col cols="1" class="align-self-center text-center">~</v-col>
      <v-col cols="2">
        <v-text-field type="date" v-model="searchParams.endDate" outlined color="primary" />
      </v-col>
      <v-col cols="2">
        <v-select
          v-model="searchParams.categoryId"
          :items="categories"
          item-title="name"
          item-value="categoryId"
          single-line
          label="카테고리"
        ></v-select>
      </v-col>
      <v-col cols="2">
        <v-text-field
          v-model="searchParams.searchWord"
          placeholder="제목, 내용, 작성자 검색"
          outlined
          color="primary"
        />
      </v-col>
      <v-col cols="2">
        <input type="hidden" name="pageNum" value="1" />
        <v-btn color="primary" @click="searchArticles">검색</v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col>총 {{ totalArticleCount }}건</v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-table>
          <thead>
            <tr>
              <th>카테고리</th>
              <th>제목</th>
              <th>작성자</th>
              <th>조회수</th>
              <th>등록일</th>
              <th>수정일</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in articles" :key="String(item.articleId)">
              <td>{{ item.categoryName }}</td>
              <td>
                <span @click="$router.push('/view/' + item.articleId)">
                  {{ item.title }}
                </span>
              </td>
              <td>{{ item.writer }}</td>
              <td>{{ item.viewCount }}</td>
              <td>{{ formatDate(item.createdDate) }}</td>
              <td>{{ formatDate(item.modifiedDate) }}</td>
            </tr>
          </tbody>
        </v-table>
      </v-col>
    </v-row>

    <pagination-component
      class="pagination-component"
      v-model="currentPage"
      :pageStart="pageStart"
      :pageEnd="pageEnd"
      :totalPageCount="totalPageCount"
    />
    <v-row>
      <v-col class="d-flex justify-end">
        <v-btn color="primary" @click="$router.push('/write')">글쓰기</v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import { onMounted, ref, watch, computed, onBeforeMount, inject } from "vue";
import { useStore } from "vuex";
import { useRouter, useRoute } from "vue-router";
import { formatDate, subStringWithSkipMark } from "../assets/common";
import getArticlesApi from "../apis/getArticlesApi";
import categoriesApi from "../apis/categoriesApi";
import PaginationComponent from "../components/PaginationComponent.vue";

const store = useStore();
const route = useRoute();

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

// category 관련요소
const { categories, getCategories } = categoriesApi();

onBeforeMount(() => {
  getCategories();
});

// articleList 관련요소
const searchParams = ref(store.getters.searchParams);
const currentPage = ref(searchParams.value.pageNum);
const articlesPerPage = ref(searchParams.value.articlePerPage);
const {
  articles,
  articlesAreLoading,
  getArticles,
  totalArticleCount,
  pageStart,
  pageEnd,
  totalPageCount,
} = getArticlesApi(currentPage, articlesPerPage);

onBeforeMount(() => {
  getArticles(searchParams.value);
});

// search 관련
const searchArticles = () => {
  searchParams.value.pageNum = 1;
  currentPage.value = 1;
  store.commit("updateSearchParams", searchParams.value);
  getArticles(searchParams.value);
};

watch(
  () => currentPage.value,
  (newVal) => {
    searchParams.value.pageNum = newVal;
    store.commit("updateSearchParams", searchParams.value);
    getArticles(searchParams.value);
  }
);
</script>
