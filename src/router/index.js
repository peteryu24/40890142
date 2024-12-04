import Vue from "vue";
import VueRouter from "vue-router";
import EntryPage from "@/view/EntryPage.vue";
import ArticleListView from "@/view/ArticleListView.vue";

Vue.use(VueRouter);

const routes = [
  { path: "/", name: "EntryPage", component: EntryPage },
  { path: "/articles", name: "ArticleListView", component: ArticleListView },
];

const router = new VueRouter({
  mode: "history", // HTML5 history 모드 활성화
  routes,
});

export default router;
