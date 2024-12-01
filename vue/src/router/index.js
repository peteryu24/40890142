import { createRouter, createWebHistory } from "vue-router";
import ArticleList from "../components/ArticleList.vue";
import ArticleDetail from "../components/ArticleDetail.vue";

const routes = [
  {
    path: "/",
    name: "ArticleList",
    component: ArticleList,
  },
  {
    path: "/articles/:id",
    name: "ArticleDetail",
    component: ArticleDetail,
    props: true,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
