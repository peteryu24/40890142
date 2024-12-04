<template>
  <div class="wrapper">
    <div class="container">
      <!-- 로고 -->
      <img src="@/assets/logo.png" alt="logo" class="logo" />

      <!-- 제목, 내용, 파일 입력 -->
      <h1 class="create-article-title">새 게시글 작성</h1>

      <form @submit.prevent="submitArticle">
        <!-- 제목 입력 -->
        <div class="input-group">
          <label for="title">제목</label>
          <input type="text" v-model="title" id="title" required />
        </div>

        <!-- 내용 입력 -->
        <div class="input-group">
          <label for="content">내용</label>
          <textarea v-model="content" id="content" rows="5" required></textarea>
        </div>

        <!-- 파일 첨부 -->
        <div class="input-group">
          <label for="files">첨부파일</label>
          <input type="file" @change="handleFileUpload" multiple />
        </div>

        <!-- 제출 버튼 -->
        <button type="submit">게시글 작성</button>
      </form>
    </div>
  </div>
</template>

<script>
import { articleApi } from "@/utils/api";

export default {
  data() {
    return {
      title: "",
      content: "",
      files: [],
    };
  },
  methods: {
    // 파일 업로드 핸들러
    handleFileUpload(event) {
      this.files = Array.from(event.target.files);
    },

    // 게시글 작성 제출 처리
    async submitArticle() {
      const formData = new FormData();
      formData.append("title", this.title);
      formData.append("content", this.content);

      // 파일 첨부가 있다면 FormData에 추가
      if (this.files.length > 0) {
        this.files.forEach((file) => {
          formData.append("files", file);
        });
      }

      try {
        // articleApi.createArticle을 사용해 8080 포트로 요청을 보냄
        const response = await articleApi.createArticle(formData);

        alert(response.data.message); // 성공 메시지
        this.$router.push("/articles"); // 게시글 목록 페이지로 이동
      } catch (error) {
        console.error("게시글 작성 중 오류 발생:", error);
        alert("게시글 작성에 실패했습니다. 다시 시도해주세요.");
      }
    },
  },
};
</script>

<style scoped>
/* 스타일링 */
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
}

h1.create-article-title {
  font-size: 2em;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
  text-align: center;
}

.input-group {
  margin-bottom: 15px;
}

input[type="text"],
textarea {
  width: 100%;
  padding: 10px;
  margin-top: 5px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

textarea {
  resize: vertical;
}

button {
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
</style>
