import Vue from "vue";
import VueRouter from "vue-router";
import EntryPage from "@/view/EntryPage.vue";
import ArticleListView from "@/view/ArticleListView.vue";
import ArticleWriteView from "@/view/ArticleWriteView.vue"; // 글쓰기 페이지 추가
import ArticleDetailView from "@/view/ArticleDetailView.vue";

Vue.use(VueRouter);

const routes = [
  { path: "/", name: "EntryPage", component: EntryPage },
  { path: "/articles", name: "ArticleListView", component: ArticleListView },
  { path: "/articles/write", name: "ArticleWriteView", component: ArticleWriteView }, // 글쓰기 라우트 추가
  { path: "/articles/:articleId", name: "ArticleDetailView", component: ArticleDetailView },
];

const router = new VueRouter({
  mode: "history", // HTML5 history 모드 활성화
  routes,
});

router.beforeEach((to, from, next) => {
  const authToken = localStorage.getItem("authToken"); // 세션 체크

  // 로그인 페이지로 가는 경로는 예외 처리
  if (to.name !== "EntryPage" && !authToken) {
    next({ name: "EntryPage" }); // 로그인 안 된 상태에서 다른 페이지 접근 시 EntryPage로 리디렉션
  } else {
    next(); // 정상적인 페이지 접근
  }
});

export default router;
