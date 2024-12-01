<template>
  <div>
    <h1>게시물 목록</h1>
    <ul>
      <li v-for="article in articles" :key="article.articleId">
        <h3>{{ article.title }}</h3>
        <p>{{ article.content }}</p>
        <p>작성일: {{ article.createdAt }}</p>
        <p>조회수: {{ article.viewCount }}</p>
      </li>
    </ul>
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
  created() {
    fetch("http://localhost:8080/articles")
      .then((res) => res.json())
      .then((data) => (this.articles = data))
      .catch((error) => console.error(error));
  },
};
</script>

<style scoped>
h1 {
  color: #333;
}
</style>
