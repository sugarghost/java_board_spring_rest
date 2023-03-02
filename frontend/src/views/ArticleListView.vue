<template lang="ko">
<v-container>
  <!-- searchBar -->
  <v-card class="mb-4">
      <v-card-text>
        <v-form>
          <v-row>
            <v-col cols="3">
                <v-text-field type="date" v-model="search.startDate" />
            </v-col>
            <v-col cols="3">
                <v-text-field type="date" v-model="search.endDate" />
            </v-col>
            <v-col cols="2">
              <select v-model="search.categoryId" class="v-select" label="Category">
                <option value="">선택해주세요</option>
                <option v-for="category in categoryList" :key="category.categoryId" :value="category.categoryId">{{ category.name }}</option>
              </select>
              
            </v-col>
            <v-col cols="2">
              <v-text-field
                v-model="search.searchWord"
                label="Keyword"
                outlined
              ></v-text-field>
            </v-col>
            <v-col cols="2">
              <v-btn color="primary" text @click="getArticleList">Search</v-btn>
            </v-col>
          </v-row>
        </v-form>
      </v-card-text>
    </v-card>

    <!-- Article list -->
    <v-card>
      <v-card-text>
        <v-data-table-server
            v-model:items-per-page="itemsPerPage"
            :headers="headers"
            :items-length="totalArticle"
            :items="articleList"
            :loading="loading"
            class="elevation-1"
            item-title="name"
            item-value="name"
            @update:options="getArticleList"
        ></v-data-table-server>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" text @click="navigateToWritePage">Write</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
export default {
  data: () => ({
    itemsPerPage: 5,
    headers: [
      { title: "카테고리", key: "categoryName", align: "end" },
      { title: "제목", key: "title", align: "end" },
      { title: "작성자", key: "writer", align: "end" },
      { title: "조회수", key: "viewCount", align: "end" },
      { title: "등록일시", key: "createdDate", align: "end" },
      { title: "수정일시", key: "modefiedDate", align: "end" },
    ],

    search: {
      categoryId: null,
      searchWord: "",
      startDate: null,
      endDate: null,
    },
    categoryList: [],
    articleList: [
      {
        categoryName: "JAVA",
        title: "!!!!!!",
        writer: "김윤교",
        viewCount: 1,
        createdDate: "2023-02-18T05:15:43.000+00:00",
        modefiedDate: "-",
      },
      {
        categoryName: "JAVA",
        title: "리다이렉트테스트!!!QQ",
        writer: "김윤교",
        viewCount: 19,
        createdDate: "2023-02-18T01:39:37.000+00:00",
        modefiedDate: "-",
      },
      {
        categoryName: "Database",
        title: "새로고치면 글이 복사가 된다고!",
        writer: "김윤교",
        viewCount: 17,
        createdDate: "2023-02-17T06:13:40.000+00:00",
        modefiedDate: "-",
      },
      {
        categoryName: "Database",
        title: "새로고치면 글이 복사가 된다고!",
        writer: "김윤교",
        viewCount: 1,
        createdDate: "2023-02-17T06:13:39.000+00:00",
        modefiedDate: "-",
      },
      {
        categoryName: "Database",
        title: "새로고치면 글이 복사가 된다고!",
        writer: "김윤교",
        viewCount: 1,
        createdDate: "2023-02-17T06:13:36.000+00:00",
        modefiedDate: "-",
      },
    ],
    loading: true,
    totalArticle: 0,
  }),
  beforeMount() {
    this.getCategoryList();
    this.getArticleList({ page: 1, itemsPerPage: 5 });
  },
  methods: {
    async getCategoryList() {
      try {
        const response = await this.$axios.get("/v1/categories");
        this.categoryList = response.data;
      } catch (error) {
        console.error(error);
      }
    },

    async getArticleList({ page, itemsPerPage }) {
      try {
        this.loading = true;

        const response = await this.$axios.get("/v1/articles", {
          params: {
            ...this.search,
            pageNum: page,
            articlePerPage: itemsPerPage,
          },
        });
        console.log(response);
        const data = response.data.map((item) => {
          return {
            categoryName: item.categoryName,
            title: item.title,
            writer: item.writer,
            viewCount: item.viewCount,
            createdDate: item.createdDate,
            modefiedDate: item.modefiedDate != null ? item.modefiedDate : "-",
          };
        });
        this.articleList = data;
        console.log(data);
        this.totalArticle = Number(response.headers["x-total-count"]);
      } catch (error) {
        console.error(error);
      } finally {
        this.loading = false;
      }
    },
    navigateToWritePage() {
      this.$router.push({ name: "article-write" });
    },
  },
};
</script>
