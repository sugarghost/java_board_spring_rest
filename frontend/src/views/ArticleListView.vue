<template>
  <div class="article-list">
    <div class="search-area">
      <select id="category-select" v-model="searchParams.categoryId">
        <option value="">All Categories</option>
        <option v-for="category in categories" :value="category.categoryId" :key="category.categoryId">{{ category.name }}
        </option>
      </select>

      <input type="date" id="start-date-input" v-model="searchParams.startDate" />

      <input type="date" id="end-date-input" v-model="searchParams.endDate" />

      <input type="text" id="search-word-input" v-model="searchParams.searchWord" />

      <button @click="searchArticles">Search</button>
    </div>

    <table>
      <thead>
        <tr>
          <th>Category</th>
          <th>Title</th>
          <th>Writer</th>
          <th>View Count</th>
          <th>Created Date</th>
          <th>Modify Date</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="article in displayedArticles" :key="article.id">
          <td>{{ article.categoryName }}</td>
          <td>{{ article.title }}</td>
          <td>{{ article.writer }}</td>
          <td>{{ article.viewCount }}</td>
          <td>{{ article.createdDate }}</td>
          <td>{{ article.modifyDate }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import { ref, computed, onMounted, inject } from "vue";

export default {
  name: "ArticleList",

  setup() {
    const axios = inject("axios");

    const articles = ref([]);
    const categories = ref([{
      categoryId: 0,
      name: "Select Category"
    }]);
    const searchParams = ref({
      categoryId: 0,
      startDate: null,
      endDate: null,
      searchWord: null,
      articlePerPage: 5,
      pageNum: 1
    });
    const pageSize = 10;
    const currentPage = ref(1);
    const getCategories = async () => {
      try {
        const response = await axios.get("/v1/categories", {
        });
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
            ...searchParams.value
          }
        });
        console.log(response);
        articles.value = response.data;
      } catch (error) {
        console.error(error);
      }
    };

    onMounted(() => {
      getCategories();
      searchArticles();
    });


    return {
      articles,
      categories,
      searchParams,
      currentPage,
      searchArticles
    };
  }

};
</script>