import axios from "axios";

// 기본 Axios 인스턴스
const api = axios.create({
  baseURL: "http://localhost:8080", // 기본 서버 주소
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // CORS에서 쿠키 허용
});

// auth 관련 API
export const authApi = {
  sessionCheck: () => api.get("/auth/session-check"),
  checkUsername: (data) => api.post("/auth/check-username", data),
  login: (data) => api.post("/auth/login", data),
  register: (data) => api.post("/auth/register", data),
};

// Articles 관련 API
export const articleApi = {
  // 게시글 목록 가져오기 (정렬 및 검색 추가)
  getArticles: (page, size, sortOrder = "desc", searchQuery = "") =>
    api.get("/articles", {
      params: {
        page,
        size,
        sortOrder,
        searchQuery,
      }, // URL 파라미터 추가
    }),

  getArticleById: (id) => api.get(`/articles/${id}`),
  createArticle: (data) => api.post("/articles", data),
  updateArticle: (id, data) => api.put(`/articles/${id}`, data),
  deleteArticle: (id) => api.delete(`/articles/${id}`),
};

// 파일 업로드 관련 API
export const fileApi = {
  uploadFile: async (file) => {
    try {
      const response = await api.post("/files/upload", file, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      return response.data;
    } catch (error) {
      console.error("파일 업로드 실패:", error);
      throw error;
    }
  },
};

export default api;
