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
        <v-select v-model="searchParams.categoryId" :items="categories" item-title="name" item-value="categoryId"
          single-line label="카테고리"></v-select>
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
          :page="searchParams.pageNum" @update:options="updateList">

          <template v-slot:[`column.name`]="{ column }">
            {{ column.title.toUpperCase() }}
          </template>

          <template #item="{ item }">
            <tr>
              <td>{{ item.raw.categoryName }}</td>
              <td>
                <!-- TODO: 링크에 color가 안들어감, 방법 찾아야함 -->
                <span @click="$router.push('/view/' + item.raw.articleId)"
                  @keydown="$router.push('/view/' + item.raw.articleId)">
                  {{ item.raw.title }}
                </span>
              </td>
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
  </v-container>
</template>

<script>
import { ref, computed, onBeforeMount, inject } from "vue";
import { useStore } from "vuex";
import { useRouter, useRoute } from "vue-router"
import { formatDate, subStringWithSkipMark } from "../assets/common";

export default {
  name: "ArticleList",

  setup() {
    // TODO: 라이프 사이클에 대한 이해를 기반으로 구성요소 분리가 필요함
    // TODO: V-data-table 제외하고, paganation 디자인 요소를 가져오고 실제 구현 로직은 직접 하기
    // TODO: V-data-table => table + pagnation 디자인도 빼고 그냥 실제 구현만 직접하기
    // TODO: 라이플 사이클, 이벤트 버스 적용하기

    // TODO: 프론트 개발은 내가 어떤 액션을 만들고, 어떻게 라이프 흐름이 따라가고를 이해하고 적용시키는 위주의 훈련을 하기(직접하기!)


    // TODO: Vue란 무엇인가에 대해서 고민하기
    // 공통 요소
    const axios = inject("axios");
    const store = useStore();
    const route = useRoute()

    // route에 넘어온 파라미터 있으면 store에 저장
    // TODO: 보기 않좋고, 현재 검색 시스템이 위에 URL에 파라미터 적용되는 시스템이 아님, 연동 되도록 바꿔야함
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
      { title: "카테고리", value: "categoryName" },
      { title: "제목", value: "title", class: "purple-darken-2" },
      { title: "작성자", value: "writer" },
      { title: "조회수", value: "viewCount" },
      { title: "생성일", value: "createdDate", align: "center" },
      { title: "수정일", value: "modifiedDate", align: "center" },
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
      } catch (error) {
        console.error(error);
      }
    };
    onBeforeMount(() => {
      getCategories();
    });


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
        title: subStringWithSkipMark(articleData.title, 80) + (articleData.isFileExist ? " 📎" : ""),
        writer: articleData.writer,
        viewCount: articleData.viewCount,
        categoryName: articleData.categoryName,
        createdDate: articleData.createdDate,
        modifiedDate: articleData.modifiedDate,
        isFileExist: articleData.isFileExist,
      }));
      totalItems.value = Number(response.headers["x-total-count"]);
      store.commit("updateSearchParams", searchParams.value);
      loading.value = false;
    }
    onBeforeMount(() => {
      getArticleList();
    });

    const updateList = ({ page, itemsPerPage, sortBy }) => {
      searchParams.value.pageNum = page;
      searchParams.value.articlePerPage = itemsPerPage;
      getArticleList();
    }


    // search 관련
    const searchArticles = () => {
      searchParams.value.pageNum = 1;
      getArticleList();
    };

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
