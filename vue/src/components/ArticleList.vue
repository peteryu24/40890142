<template>
  <div class="article-list">
    <h1>게시물 목록</h1>
    <table>
      <thead>
        <tr>
          <th>번호</th>
          <th>제목</th>
          <th>날짜</th>
          <th>조회수</th>
          <th>첨부파일</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="(article, index) in articles"
          :key="article.articleId"
          @click="goToDetail(article.articleId)"
          style="cursor: pointer"
        >
          <td>{{ index + 1 }}</td>
          <td>{{ article.title }}</td>
          <td>{{ formatDate(article.createdAt) }}</td>
          <td>{{ article.viewCount }}</td>
          <td>{{ article.hasFile ? "있음" : "없음" }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: "ArticleList",
  data() {
    return {
      articles: [],
    };
  },
  methods: {
    fetchArticles() {
      fetch("http://localhost:8080/articles")
        .then((response) => response.json())
        .then((data) => {
          this.articles = data;
        })
        .catch((error) => {
          console.error("Error fetching articles:", error);
        });
    },
    formatDate(dateString) {
      const options = { year: "numeric", month: "2-digit", day: "2-digit" };
      return new Date(dateString).toLocaleDateString("ko-KR", options);
    },
    goToDetail(articleId) {
      this.$router.push(`/articles/${articleId}`);
    },
  },
  mounted() {
    this.fetchArticles();
  },
};
</script>
