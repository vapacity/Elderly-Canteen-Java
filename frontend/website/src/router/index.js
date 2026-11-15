import { createRouter, createWebHistory } from "vue-router";
import HomePage from "../views/HomePage.vue";
import LoginPage from "../views/LoginPage.vue";
import OTPLoginPage from "../views/OTPLoginPage.vue";
import RegisterPage from "../views/RegisterPage.vue";
import NewpsdPage from "../views/NewpsdPage.vue";
import MyProfile from "../views/MyProfile.vue";
import DemoAdminPage from "../views/DemoAdminPage.vue";

const routes = [
  {
    path: "/",
    redirect: "/home",
  },
  {
    path: "/login",
    name: "LoginPage",
    component: LoginPage,
  },
  {
    path: "/OTP",
    name: "OTPLoginPage",
    component: OTPLoginPage,
  },
  {
    path: "/home",
    name: "HomePage",
    component: HomePage,
  },
  {
    path: "/register",
    name: "RegisterPage",
    component: RegisterPage,
  },
  {
    path: "/newpsd",
    name: "NewpsdPage",
    component: NewpsdPage,
    props: (route) => ({ from: route.query.from, phone: route.query.phone }),
  },
  {
    path: "/profile",
    name: "MyProfile",
    component: MyProfile,
    meta: { requiresAuth: true },
  },
  {
    path: "/admin",
    name: "AdminPage",
    component: DemoAdminPage,
    meta: { requiresAuth: true, requiresAdmin: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 导航守卫
router.beforeEach((to, from, next) => {
  const loggedIn = !!localStorage.getItem("token"); // 这里使用localStorage作为示例，可以根据实际情况调整

  if (to.matched.some((record) => record.meta.requiresAuth) && !loggedIn) {
    next("/login"); // 重定向到登录页面
  } else if (to.meta.requiresAdmin && localStorage.getItem("identity") !== "admin") {
    next("/home");
  } else {
    next(); // 继续导航
  }
});

export default router;
