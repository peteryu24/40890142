<template>
  <div>
    <h1>게시물 목록</h1>
    <ul>
      <li v-for="article in articles" :key="article.articleId">
        <router-link :to="{ name: 'ArticleDetail', params: { id: article.articleId } }">
          {{ article.title }} - 조회수: {{ article.viewCount }}
        </router-link>
      </li>
    </ul>

    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">이전</button>
      <button
        v-for="page in totalPages"
        :key="page"
        :class="{ active: page === currentPage }"
        @click="changePage(page)"
      >
        {{ page }}
      </button>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">
        다음
      </button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      articles: [],
      currentPage: 1,
      totalPages: 0,
    };
  },
  methods: {
    fetchArticles(page) {
      axios
        .get(`http://localhost:8080/articles?page=${page - 1}&size=10`)
        .then((response) => {
          this.articles = response.data.content;
          this.totalPages = response.data.totalPages;
        })
        .catch((error) => {
          console.error(error);
        });
    },
    changePage(page) {
      if (page > 0 && page <= this.totalPages) {
        this.currentPage = page;
        this.fetchArticles(page);
      }
    },
  },
  created() {
    this.fetchArticles(this.currentPage);
  },
};
</script>

<style>
.pagination button.active {
  font-weight: bold;
  color: red;
}
</style>
