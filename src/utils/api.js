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
  checkUsername: (data) => api.post("/auth/check-username", data),
  login: (data) => api.post("/auth/login", data),
  register: (data) => api.post("/auth/register", data),
};

// article 관련 API
export const articleApi = {
  getArticles: () => api.get("/articles"),
  getArticleById: (id) => api.get(`/articles/${id}`),
  createArticle: (data) => api.post("/articles", data),
  updateArticle: (id, data) => api.put(`/articles/${id}`, data),
  deleteArticle: (id) => api.delete(`/articles/${id}`),
};

// 파일 업로드 관련 API
export const fileApi = {
  uploadFile: (file) =>
    api.post("/files/upload", file, {
      headers: { "Content-Type": "multipart/form-data" },
    }),
};

export default api;
