<template>
  <div>
    <h1>{{ article.title }}</h1>
    <p>작성자: {{ article.authorUsername }}</p>
    <p>내용: {{ article.content }}</p>
    <p>조회수: {{ article.viewCount }}</p>
    <p>첨부 파일 여부: {{ article.hasFile ? "있음" : "없음" }}</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: ["id"],
  data() {
    return {
      article: {},
    };
  },
  async created() {
    try {
      const response = await axios.get(`http://localhost:8080/articles/${this.id}`);
      this.article = response.data;
    } catch (error) {
      console.error("게시물 정보를 가져오는 중 오류 발생:", error);
    }
  },
};
</script>
