<template>
  <div id="app">
    <router-view />
    <!-- 라우터를 통해 페이지 동적 렌더링 -->
  </div>
</template>

<script>
export default {
  name: "App",
};
</script>

<style>
/* html, body의 크기를 100%로 설정하여 화면 전체를 채우도록 */
html,
body {
  margin: 0; /* 마진 제거 */
  padding: 0; /* 패딩 제거 */
  height: 100%; /* 화면 높이를 100%로 설정 */
  width: 100%; /* 화면 너비를 100%로 설정 */
  overflow: hidden; /* 스크롤 제거 */
}

/* #app 스타일 수정 */
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background-color: #fff; /* 배경을 흰색으로 설정 */
  margin: 0; /* 마진 제거 */
  height: 100vh; /* 화면 전체 높이로 설정 */
  display: flex; /* Flexbox 사용 */
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
}

/* 로고 이미지 스타일 */
.logo {
  position: absolute; /* 로고를 절대 위치로 배치 */
  top: 20px;
  left: 20px;
  width: 80px;
  height: auto;
}
</style>
