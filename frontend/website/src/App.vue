<template>
  <div id="app">
    <nav v-if="showNavBar">
      <router-link to="/">Login</router-link>
      <router-link to="/OTP">OTPLogin</router-link>
      <router-link to="/home">Home</router-link>
      <router-link to="/register">Register</router-link>
      <router-link to="/newpsd">Newpsd</router-link>
    </nav>
    <router-view />
  </div>
</template>

<script>
export default {
  name: "App",
  props: {
    showNavBar: {
      type: Boolean,
      default: false,
    },
  },
};
</script>

<style>
html {
  /* 滚动方式设置为平滑滚动 */
  scroll-behavior: smooth;
}
nav {
  display: flex;
  gap: 10px;
}
</style>
