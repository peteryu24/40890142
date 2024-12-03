<template>
  <div class="entry-page">
    <div v-if="!isAuthenticated" class="auth-forms">
      <div class="form-container">
        <h2>Login</h2>
        <form @submit.prevent="handleLogin">
          <input v-model="loginData.username" type="text" placeholder="Username" required />
          <input v-model="loginData.password" type="password" placeholder="Password" required />
          <button type="submit">Login</button>
        </form>
      </div>

      <div class="form-container">
        <h2>Register</h2>
        <form @submit.prevent="handleRegister">
          <input v-model="registerData.username" type="text" placeholder="Username" required />
          <input v-model="registerData.password" type="password" placeholder="Password" required />
          <button type="submit">Register</button>
        </form>
      </div>

      <div v-if="usernameAvailable === false" class="error-message">
        <p>Username is already taken. Please choose another one.</p>
      </div>
    </div>

    <div v-if="isAuthenticated" class="welcome-message">
      <h2>Welcome, {{ username }}!</h2>
      <button @click="handleLogout">Logout</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      isAuthenticated: false,
      username: "",
      loginData: {
        username: "",
        password: "",
      },
      registerData: {
        username: "",
        password: "",
      },
      usernameAvailable: null,
    };
  },
  methods: {
    // Check username availability
    async checkUsernameAvailability() {
      try {
        const response = await axios.post("http://localhost:8080/auth/check-username", {
          username: this.registerData.username,
        });
        this.usernameAvailable = response.data.data;
      } catch (error) {
        console.error("Error checking username availability:", error);
      }
    },
    // Handle user login and use the response
    async handleLogin() {
      try {
        const response = await axios.post("http://localhost:8080/auth/login", this.loginData, {
          withCredentials: true,
        });
        this.isAuthenticated = true;
        this.username = response.data.data.username; // Access data from the response
      } catch (error) {
        console.error("Error during login:", error);
      }
    },

    // Handle user registration and use the response
    async handleRegister() {
      if (!this.usernameAvailable) {
        alert("Please check if the username is available first.");
        return;
      }
      try {
        const response = await axios.post("http://localhost:8080/auth/register", this.registerData);
        console.log(response.data); // Access any data from the response if needed
        alert("Registration successful! Please login.");
      } catch (error) {
        console.error("Error during registration:", error);
      }
    },
    // Handle user logout
    async handleLogout() {
      try {
        await axios.post(
          "http://localhost:8080/auth/logout",
          {},
          {
            withCredentials: true,
          }
        );
        this.isAuthenticated = false;
        this.username = "";
      } catch (error) {
        console.error("Error during logout:", error);
      }
    },
  },
  watch: {
    // Automatically check if the username is available when typing in the registration form
    "registerData.username": function (newUsername) {
      if (newUsername) {
        this.checkUsernameAvailability();
      } else {
        this.usernameAvailable = null;
      }
    },
  },
};
</script>

<style scoped>
.entry-page {
  padding: 20px;
}

.auth-forms {
  display: flex;
  flex-direction: column;
}

.form-container {
  margin-bottom: 20px;
}

input {
  padding: 8px;
  margin: 5px 0;
  width: 100%;
}

button {
  padding: 8px;
  background-color: #007bff;
  color: white;
  border: none;
  width: 100%;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.error-message {
  color: red;
}

.welcome-message {
  text-align: center;
}
</style>
