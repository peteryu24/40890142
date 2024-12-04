<template>
  <div class="wrapper">
    <div class="container">
      <!-- 로고 -->
      <img src="@/assets/logo.png" alt="logo" class="logo" />
      <!-- 글쓰기 버튼 -->
      <button class="write-btn" @click="navigateToWrite">글쓰기</button>

      <h1>게시글 목록</h1>
      <div class="controls">
        <!-- 정렬 -->
        <label for="sortOrder">정렬:</label>
        <select v-model="sortOrder" @change="fetchArticles(0)">
          <option value="desc">최신순</option>
          <option value="asc">오래된순</option>
        </select>

        <!-- 검색 -->
        <input v-model="searchQuery" @keyup.enter="fetchArticles(0)" type="text" placeholder="검색어를 입력하세요" />
        <button @click="fetchArticles(0)">검색</button>
      </div>
      <div v-if="articles.length === 0">
        <p>게시글이 없습니다. 새 글을 작성해보세요!</p>
      </div>
      <table v-else>
        <thead>
          <tr>
            <th>제목</th>
            <th>작성자</th>
            <th>조회수</th>
            <th>첨부파일</th>
            <th>등록일</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="article in articles" :key="article.articleId">
            <td>
              <router-link :to="'/articles/' + article.articleId">
                {{ article.title }}
              </router-link>
            </td>
            <td>{{ article.authorUsername }}</td>
            <td>{{ article.viewCount }}</td>
            <td>{{ article.hasFile ? "있음" : "없음" }}</td>
            <td>{{ new Date(article.createdAt).toLocaleDateString() }}</td>
          </tr>
        </tbody>
      </table>
      <div class="pagination" v-if="totalPages > 1">
        <button :disabled="currentPage === 0" @click="fetchArticles(currentPage - 1)">이전</button>
        <span>페이지 {{ currentPage + 1 }} / {{ totalPages }}</span>
        <button :disabled="currentPage === totalPages - 1" @click="fetchArticles(currentPage + 1)">다음</button>
      </div>
    </div>
  </div>
</template>

<script>
import { articleApi } from "@/utils/api";

export default {
  data() {
    return {
      articles: [],
      currentPage: 0,
      totalPages: 0,
      pageSize: 10,
      sortOrder: "desc", // 기본값: 최신순
      searchQuery: "",
    };
  },
  methods: {
    async fetchArticles(page) {
      // 페이지 업데이트
      this.currentPage = page;

      try {
        const response = await articleApi.getArticles(
          this.currentPage, // 현재 페이지
          this.pageSize,
          this.sortOrder,
          this.searchQuery // 검색어 유지
        );
        const result = response.data.data;
        if (result && result.content) {
          this.articles = result.content;
          this.totalPages = result.totalPages;
        } else {
          this.articles = [];
          this.totalPages = 0;
        }
      } catch (error) {
        console.error("게시글을 가져오는 중 오류 발생:", error);
        alert("게시글을 가져오는 데 실패했습니다. 다시 시도해주세요.");
        this.articles = [];
        this.totalPages = 0;
      }
    },
    navigateToWrite() {
      this.$router.push({ name: "ArticleWriteView" });
    },
  },
  async mounted() {
    this.fetchArticles(0);
  },
};
</script>

<style scoped>
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css");

* {
  box-sizing: border-box;
}

body {
  font-family: "Montserrat", sans-serif;
  margin: 0;
  padding: 0;
}

.wrapper {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #ebecf0;
  overflow: hidden;
}

.container {
  border-radius: 10px;
  box-shadow: -5px -5px 10px #fff, 5px 5px 10px #babebc;
  position: absolute;
  width: 768px;
  min-height: 480px;
  background: #fff;
  overflow: hidden;
  padding: 20px;
  text-align: center;
}

.logo {
  position: absolute;
  top: 20px;
  left: 20px;
  width: 80px;
  height: auto;
}

.write-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  padding: 10px 20px;
  background-color: #ff4b2b;
  color: #fff;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.write-btn:hover {
  background-color: #ff6b3f;
}

.controls {
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
  gap: 10px;
  align-items: center;
}

.controls label {
  margin-right: 10px;
}

.controls select,
.controls input,
.controls button {
  padding: 5px;
  font-size: 14px;
  border: none;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

button {
  padding: 10px 20px;
  cursor: pointer;
  border-radius: 10px;
  background-color: #ff4b2b;
  color: white;
  border: none;
  font-weight: bold;
  font-size: 14px;
  box-shadow: -5px -5px 10px #ff6b3f, 5px 5px 8px #bf4b2b;
}

button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th,
td {
  padding: 10px;
  border: 1px solid #ddd;
  text-align: center;
}

th {
  background-color: #f4f4f4;
}
</style>
