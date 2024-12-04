<template>
  <div class="wrapper">
    <div class="container">
      <!-- 로고 -->
      <img src="@/assets/logo.png" alt="logo" class="logo" />

      <!-- 뒤로가기 버튼 -->
      <button class="back-btn" @click="goBack">뒤로가기</button>

      <!-- 게시글 제목 -->
      <h1 class="article-title">{{ article.title }}</h1>

      <!-- 게시글 내용 -->
      <div class="article-content" v-html="article.content"></div>

      <!-- 게시글 메타 정보 (작성자, 등록일, 조회수) -->
      <div class="article-meta">
        <p><strong>작성자:</strong> {{ article.authorUsername }}</p>
        <p><strong>등록일:</strong> {{ new Date(article.createdAt).toLocaleDateString() }}</p>
        <p><strong>조회수:</strong> {{ article.viewCount }}</p>
      </div>

      <!-- 첨부파일 여부 -->
      <div v-if="article.hasFile" class="file-info">
        <p>
          <strong>첨부파일:</strong>
          <!-- 언더스코어 이후의 부분만 표시 -->
          {{
            article.fileNames && article.fileNames.length > 0
              ? article.fileNames.map((fileName) => fileName.split("_").slice(1).join("_").trim()).join(", ")
              : "파일이 첨부되지 않았습니다."
          }}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import { articleApi } from "@/utils/api";

export default {
  data() {
    return {
      article: null, // Holds the article data
    };
  },
  methods: {
    // Fetch the article details using the articleId from the route params
    async fetchArticle() {
      const articleId = this.$route.params.articleId; // get articleId from route params
      try {
        // Check if the user has already viewed the article
        const hasViewed = localStorage.getItem(`viewed_${articleId}`);

        if (!hasViewed) {
          // Fetch article details if not viewed
          const response = await articleApi.getArticle(articleId);
          if (response.data) {
            this.article = response.data.data;
            this.increaseViewCount(articleId); // Increase view count once article is fetched
            localStorage.setItem(`viewed_${articleId}`, "true"); // Mark as viewed
          }
        } else {
          // Fetch article without increasing view count
          const response = await articleApi.getArticle(articleId);
          if (response.data) {
            this.article = response.data.data;
          }
        }
      } catch (error) {
        console.error("게시글을 가져오는 중 오류 발생:", error);
        alert("게시글을 가져오는 데 실패했습니다. 다시 시도해주세요.");
      }
    },
    // 조회수 증가 API 호출
    async increaseViewCount(articleId) {
      try {
        // Call API to increase view count
        await articleApi.increaseViewCount(articleId);
        this.article.viewCount += 1; // Immediately update the local view count
      } catch (error) {
        console.error("조회수 증가 중 오류 발생:", error);
        alert("조회수 증가에 실패했습니다. 다시 시도해주세요.");
      }
    },

    // 뒤로가기 버튼 클릭 시
    goBack() {
      this.$router.go(-1); // 이전 페이지로 돌아가기
    },
  },
  async mounted() {
    // Fetch article details when the component is mounted
    this.fetchArticle();
  },
};
</script>

<style scoped>
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
  background: #f5f5f5;
  overflow: hidden;
}

.container {
  width: 60%;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
}

h1.article-title {
  font-size: 3em;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
  text-align: center;
}

.article-content {
  font-size: 1.2em;
  line-height: 1.8;
  margin-bottom: 20px;
  color: #444;
}

.article-meta {
  font-size: 0.9em;
  color: #888;
  position: absolute;
  bottom: 10px;
  left: 20px;
}

.article-meta p {
  margin: 5px 0;
}

.file-info {
  font-size: 0.9em;
  color: #888;
  margin-top: 15px;
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #ff4b2b;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}

button:hover {
  background-color: #ff6b3f;
}

.back-btn {
  background-color: #333;
  color: white;
  margin-bottom: 20px;
  position: absolute;
  top: 20px;
  right: 20px; /* 위치를 우측으로 변경 */
  padding: 8px 15px;
  border-radius: 5px;
}

.back-btn:hover {
  background-color: #555;
}
</style>
