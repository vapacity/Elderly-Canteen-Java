// src/store/index.js
import { createStore } from "vuex";

function isTokenExpired(token) {
  if (!token) return true;

  const payload = JSON.parse(atob(token.split(".")[1]));
  const currentTime = Date.now() / 1000;
  return payload.exp < currentTime;
}

const store = createStore({
  state: {
    loggedIn: !!localStorage.getItem("token"),
    user: {
      name: "",
      avatar: "",
      id: "",
    },
  },
  mutations: {
    setLoggedIn(state, status) {
      state.loggedIn = status;
    },
    setUser(state, user) {
      state.user = user;
    },
  },
  actions: {
    checkAuth({ commit }) {
      const token = localStorage.getItem("token");
      if (!token || isTokenExpired(token)) {
        commit("setLoggedIn", false);
        localStorage.removeItem("token");
      } else {
        commit("setLoggedIn", true);
      }
    },
    login({ commit }, user) {
      commit("setLoggedIn", true);
      commit("setUser", user);
    },
    logout({ commit }) {
      commit("setLoggedIn", false);
      commit("setUser", { name: "", avatar: "", id: "" });
      localStorage.removeItem("token");
      window.location.href = "/";
    },
  },
  getters: {
    isLoggedIn: (state) => state.loggedIn,
    currentUser: (state) => state.user,
  },
});

export default store;
