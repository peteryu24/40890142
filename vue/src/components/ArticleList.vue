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
        <tr v-for="(article, index) in articles" :key="article.articleId">
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
  },
  mounted() {
    this.fetchArticles();
  },
};
</script>

<style scoped>
.article-list {
  max-width: 800px;
  margin: 0 auto;
  font-family: Arial, sans-serif;
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
  text-align: center;
}

thead {
  background-color: #f8f9fa;
}

th,
td {
  border: 1px solid #dee2e6;
  padding: 10px;
}

tbody tr:nth-child(even) {
  background-color: #f1f3f5;
}

tbody tr:hover {
  background-color: #e9ecef;
}
</style>
