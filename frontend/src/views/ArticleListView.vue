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
          <!--
          <v-select
            v-model="select"
            :hint="`${select.state}, ${select.abbr}`"
            :items="items"
            item-title="state"
            item-value="abbr"
            label="Select"
            persistent-hint
            return-object
            single-line
          ></v-select>
          -->
          <v-select
            v-model="category"
            :items="categories.value"
            item-text="name"
            item-value="categoryId"
            return-object
            single-line
            label="Select a category"
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
        <v-col>총 {{ pagination.totalCount }}건</v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-data-table-server
            :headers="headers"
            :items-length="pagination.totalCount"
            :loading="loading"
            :items-per-page="5"
            item-value="name"
            class="elevation-1"
            @update:options="options = $event"
          >
          </v-data-table-server>
          <v-data-table :headers="headers" :items="articles">
            <template #header>
              <tr>
                <th
                  v-for="(header, i) in headers"
                  :key="i"
                  :class="`text-${header.align || 'start'}`"
                >
                  {{ header.text }}
                </th>
              </tr>
            </template>
            <template #item="{ item }">
              <tr>
                <td>{{ item.categoryName }}</td>
                <td>{{ item.title }}</td>
                <td>{{ item.writer }}</td>
                <td>{{ item.viewCount }}</td>
                <td>{{ formatDate(item.createdDate) }}</td>
                <td>{{ formatDate(item.modifiedDate) }}</td>
              </tr>
            </template>
          </v-data-table>
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

export default {
  name: "ArticleList",

  setup() {
    const axios = inject("axios");
    const store = useStore();

    const select = { state: "Florida", abbr: "FL" };
    const items = [
      { state: "Florida", abbr: "FL" },
      { state: "Georgia", abbr: "GA" },
      { state: "Nebraska", abbr: "NE" },
      { state: "California", abbr: "CA" },
      { state: "New York", abbr: "NY" },
    ];

    const headers = ref([
      { text: "Category", value: "categoryName" },
      { text: "Title", value: "title" },
      { text: "Writer", value: "writer" },
      { text: "Views", value: "viewCount" },
      { text: "Created", value: "createdDate", align: "center" },
      { text: "Modified", value: "modifiedDate", align: "center" },
    ]);

    const articles = ref([]);
    const category = ref({
      categoryId: 0,
      name: "카테고리",
    });
    const categories = ref([
      { categoryId: 1, name: "category1" },
      { categoryId: 2, name: "category2" },
    ]);
    const pagination = ref({
      totalCount: 1,
      pageSize: 10,
      pageBlockSize: 10,
    });

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

    const searchParams = ref(store.getters.searchParams);

    const getCategories = async () => {
      try {
        const response = await axios.get("/v1/categories", {});
        console.log(response);
        categories.value = response.data;
      } catch (error) {
        console.error(error);
      }
    };
    const searchArticles = async () => {
      try {
        const response = await axios.get("/v1/articles", {
          params: {
            ...searchParams.value,
          },
        });
        console.log(response.data);
        articles.value = response.data.map((article) => ({
          articleId: article.articleId,
          categoryName: article.categoryName,
          title: article.title + (article.isFileExist ? "📎" : ""),
          writer: article.writer,
          viewCount: article.viewCount,
          createdDate: article.createdDateValid ? formatDate(article.createdDate) : "-",
          modifiedDate: article.modefiedDateValid ? formatDate(article.modifiedDate) : "-",
        }));
        pagination.value.totalCount = response.headers["x-total-count"];
        console.log(articles.value);
        store.commit("updateSearchParams", searchParams.value);
      } catch (error) {
        console.error(error);
      }
    };

    const totalPages = computed(() =>
      Math.ceil(pagination.value.totalCount / searchParams.value.articlesPerPage)
    );
    const pageNumbers = computed(() => {
      const pages = [];
      let startPage = Math.max(
        1,
        searchParams.value.pageNum - Math.floor(pagination.value.pageBlockSize / 2)
      );
      const endPage = Math.min(
        pagination.value.totalPages,
        startPage + pagination.value.pageBlockSize - 1
      );
      if (endPage - startPage < pagination.value.pageBlockSize - 1) {
        startPage = Math.max(1, endPage - pagination.value.pageBlockSize + 1);
      }
      for (let i = startPage; i <= endPage; i += 1) {
        pages.push(i);
      }
      return pages;
    });

    const movePage = (pageNum) => {
      searchParams.value.pageNum = pageNum;
      searchArticles();
    };

    onBeforeMount(() => {
      getCategories();
      searchArticles();
    });

    return {
      category,
      select,
      items,
      headers,
      articles,
      categories,
      searchParams,
      pagination,
      totalPages,
      pageNumbers,
      searchArticles,
      formatDate,
    };
  },
};
</script>
