<template>
  <div class="wrapper">
    <div class="container" :class="{ 'right-panel-active': isPanelActive }">
      <!-- 회원가입 화면 -->
      <div class="sign-up-container">
        <form @submit.prevent="handleSignUp">
          <h1>Register</h1>
          <input v-model="signUpData.username" type="text" placeholder="ID" required />
          <button type="button" @click="checkUsernameAvailability" class="form_btn">중복 확인</button>
          <input v-model="signUpData.password" type="password" placeholder="Password" required />
          <input v-model="signUpData.passwordCheck" type="password" placeholder="Password Check" required />
          <button class="form_btn" type="submit">회원가입</button>
          <p v-if="usernameAvailabilityMessage">{{ usernameAvailabilityMessage }}</p>
        </form>
      </div>

      <!-- 로그인 화면 -->
      <div class="sign-in-container">
        <form @submit.prevent="handleSignIn">
          <h1>Welcome</h1>
          <input v-model="signInData.username" type="text" placeholder="ID" required />
          <input v-model="signInData.password" type="password" placeholder="Password" required />
          <button class="form_btn" type="submit">로그인</button>
        </form>
      </div>

      <!-- 오버레이 -->
      <div class="overlay-container">
        <div class="overlay-left">
          <button @click="togglePanel" class="overlay_btn">뒤로 가기</button>
        </div>
        <div class="overlay-right">
          <p>계정이 없으신가요?</p>
          <button @click="togglePanel" class="overlay_btn">회원가입</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { authApi } from "@/utils/api";

export default {
  data() {
    return {
      isPanelActive: false,
      signInData: {
        username: "",
        password: "",
      },
      signUpData: {
        username: "",
        password: "",
        passwordCheck: "",
        isUsernameAvailable: false,
      },
      usernameAvailabilityMessage: "",
    };
  },
  methods: {
    async handleSignIn() {
      try {
        const response = await authApi.login(this.signInData);
        console.log("로그인 성공:", response.data);
        alert("로그인 성공!");
      } catch (error) {
        console.error("로그인 실패:", error);
        alert("로그인 실패! 아이디와 비밀번호를 확인하세요.");

        this.signInData.username = "";
        this.signInData.password = "";
      }
    },
    async handleSignUp() {
      // 비밀번호와 비밀번호 확인이 다를 경우
      if (this.signUpData.password !== this.signUpData.passwordCheck) {
        alert("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");

        // 비밀번호 필드 초기화
        this.signUpData.password = "";
        this.signUpData.passwordCheck = "";
        return;
      }

      // 아이디 중복 확인 여부 체크
      if (!this.signUpData.isUsernameAvailable) {
        alert("아이디 중복 확인을 먼저 해주세요.");

        // 비밀번호 필드 초기화
        this.signUpData.password = "";
        this.signUpData.passwordCheck = "";
        this.usernameAvailabilityMessage = "";
        return;
      }

      try {
        const response = await authApi.register(this.signUpData);
        console.log("회원가입 성공:", response.data);
        alert("회원가입 성공!");
        this.togglePanel();
      } catch (error) {
        console.error("회원가입 실패:", error);
        alert("회원가입 실패! 다시 시도하세요.");

        // 비밀번호 필드 초기화
        this.signUpData.password = "";
        this.signUpData.passwordCheck = "";
      }
    },
    async checkUsernameAvailability() {
      if (!this.signUpData.username) {
        this.usernameAvailabilityMessage = "아이디를 입력해주세요.";
        return;
      }

      try {
        const response = await authApi.checkUsername({
          username: this.signUpData.username,
        });
        if (response.data.data) {
          this.signUpData.isUsernameAvailable = true;
          this.usernameAvailabilityMessage = "사용 가능한 아이디입니다.";
        } else {
          this.signUpData.isUsernameAvailable = false;
          this.usernameAvailabilityMessage = "이미 사용 중인 아이디입니다.";
        }
      } catch (error) {
        console.error("아이디 중복 확인 실패:", error);
        this.usernameAvailabilityMessage = "중복 확인 중 문제가 발생했습니다.";
      }
    },
    togglePanel() {
      this.isPanelActive = !this.isPanelActive;

      this.signUpData.username = "";
      this.signUpData.password = "";
      this.signUpData.passwordCheck = "";
      this.signUpData.isUsernameAvailable = false;

      this.usernameAvailabilityMessage = "";

      this.signInData.username = "";
      this.signInData.password = "";
    },
  },
};
</script>

<style scoped>
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css");

* {
  box-sizing: border-box;
}

body {
  font-family: "Montserrat", sans-serif;
  margin: 0;
  padding: 0;
}

.wrapper {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #ebecf0;
  overflow: hidden;
}

.container {
  border-radius: 10px;
  box-shadow: -5px -5px 10px #fff, 5px 5px 10px #babebc;
  position: absolute;
  width: 768px;
  min-height: 480px;
  overflow: hidden;
  transition: all 0.5s;
}

form {
  background: #ebecf0;
  display: flex;
  flex-direction: column;
  padding: 0 50px;
  height: 100%;
  justify-content: center;
  align-items: center;
}

form input {
  background: #eee;
  padding: 16px;
  margin: 8px 0;
  width: 85%;
  border: 0;
  outline: none;
  border-radius: 20px;
  box-shadow: inset 7px 2px 10px #babebc, inset -5px -5px 12px #fff;
}

button {
  border-radius: 20px;
  border: none;
  outline: none;
  font-size: 12px;
  font-weight: bold;
  padding: 15px 45px;
  margin: 14px;
  letter-spacing: 1px;
  text-transform: uppercase;
  cursor: pointer;
  transition: transform 80ms ease-in;
}

.form_btn {
  box-shadow: -5px -5px 10px #fff, 5px 5px 8px #babebc;
}

.form_btn:active {
  box-shadow: inset 1px 1px 2px #babebc, inset -1px -1px 2px #fff;
}

.overlay_btn {
  background-color: #ff4b2b;
  color: #fff;
  box-shadow: -5px -5px 10px #ff6b3f, 5px 5px 8px #bf4b2b;
}

.sign-in-container {
  position: absolute;
  left: 0;
  width: 50%;
  height: 100%;
  transition: all 0.5s;
}

.sign-up-container {
  position: absolute;
  left: 0;
  width: 50%;
  height: 100%;
  opacity: 0;
  transition: all 0.5s;
}

.overlay-left {
  display: flex;
  flex-direction: column;
  padding: 0 50px;
  justify-content: center;
  align-items: center;
  position: absolute;
  right: 0;
  width: 50%;
  height: 100%;
  opacity: 0;
  background-color: #ff4b2b;
  color: #fff;
  transition: all 0.5s;
}

.overlay-right {
  display: flex;
  flex-direction: column;
  padding: 0 50px;
  justify-content: center;
  align-items: center;
  position: absolute;
  right: 0;
  width: 50%;
  height: 100%;
  background-color: #ff4b2b;
  color: #fff;
  transition: all 0.5s;
}

.container.right-panel-active .sign-in-container {
  transform: translateX(100%);
  opacity: 0;
}

.container.right-panel-active .sign-up-container {
  transform: translateX(100%);
  opacity: 1;
  z-index: 2;
}

.container.right-panel-active .overlay-right {
  transform: translateX(-100%);
  opacity: 0;
}

.container.right-panel-active .overlay-left {
  transform: translateX(-100%);
  opacity: 1;
  z-index: 2;
}

span {
  font-size: 12px;
  color: #000;
  letter-spacing: 0.5px;
  margin-bottom: 10px;
}

p {
  font-size: 16px;
  font-weight: bold;
  letter-spacing: 0.5px;
  margin: 20px 0 30px;
}
</style>
