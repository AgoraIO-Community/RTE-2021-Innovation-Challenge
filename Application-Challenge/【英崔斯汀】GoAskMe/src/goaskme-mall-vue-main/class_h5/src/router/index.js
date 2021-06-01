import Vue from "vue";
import VueRouter from "vue-router";
import Index from "../views/index.vue";
import Course_detail from "../views/course_detail.vue";
import Release from "../views/release.vue";
import Issue from "../views/issue.vue";
import Search from "../views/search.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Index",
    component: Index,
  },
  {
    path: "/index",
    name: "Index",
    component: Index,
  },
  {
    path: "/course_detail",
    name: "Course_detail",
    component: Course_detail,
  },
  {
    path: "/release",
    name: "Release",
    component: Release,
  },
  {
    path: "/issue",
    name: "Issue",
    component: Issue,
  },
  {
    path: "/search",
    name: "Search",
    component: Search,
  },
  // {
  //   path: '/about',
  //   name: 'About',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  // }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
