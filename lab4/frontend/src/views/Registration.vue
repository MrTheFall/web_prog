<template>
  <div class="container">
    <header class="header">
      <h1>
        <router-link to="/main" class="header-link">{{ isLogin ? 'Вход' : 'Регистрация' }}</router-link>
      </h1>
      <p>Ильин Никита | P3210 | 408698</p>
    </header>
    <form @submit.prevent="handleSubmit" class="form-container">
      <div class="form-group">
        <label for="username">Имя пользователя</label>
        <input type="text" id="username" v-model="username" required />
      </div>
      <div class="form-group">
        <label for="password">Пароль</label>
        <input type="password" id="password" v-model="password" required />
      </div>
      <button type="submit" class="submit-button">{{ isLogin ? 'Войти' : 'Зарегистрироваться' }}</button>
      <button type="button" @click="toggleMode" class="toggle-button">{{ isLogin ? 'Перейти к регистрации' : 'Перейти к входу' }}</button>
    </form>
    <p id="floatingMessage" class="floating-message" v-if="errorMessage">
      {{ errorMessage }}
    </p>
  </div>
</template>

<script>
export default {
  name: 'AuthForm',
  data() {
    return {
      username: '',
      password: '',
      isLogin: true,
      errorMessage: '',
    };
  },
  methods: {
    async handleSubmit() {
      const endpoint = this.isLogin 
        ? '/api/auth/login' 
        : '/api/auth/register';
        
      const payload = {
        username: this.username,
        password: this.password,
      };

      try {
        const response = await this.$axios.post(endpoint, payload);
        
        if (response.status === 200) {
          if (this.isLogin) {
            this.$router.push('/app');
          }
          else {
            this.isLogin = true;
          }
        }
        
        console.log('Success:', response.data);
      } catch (error) {
        console.error('Error:', error.message);
        this.showError(error.response.data.message); 
      }
    },
    showError(message) {
      this.errorMessage = message;
      setTimeout(() => {
        this.errorMessage = '';
      }, 1500);
    },
    toggleMode() {
      this.isLogin = !this.isLogin; // Switch between login and registration
    },
  },
};
</script>

<style scoped>
body {
  background-color: #f0f4f8;
  color: #333;
  margin: 0;
  display: grid;
  justify-content: center;
  align-items: center;
  height: 100vh;
  font-family: "Press Start 2P", system-ui;
}

.container {
  display: grid;
  background-color: #ffffff;
  /*padding: 40px 20px;*/
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 400px;
}

.header {
  text-align: center;
}

.header h1 {
  font-size: 28px;
  margin-bottom: 10px;
  color: #ff00dd;
}

.header p {
  font-size: 14px;
  margin: 0;
}

.form-container {
  display: grid;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 5px;
}

input {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.submit-button, .toggle-button {
  padding: 10px;
  border: none;
  border-radius: 5px;
  background-color: #ff00dd;
  color: white;
  cursor: pointer;
  font-size: 16px;
}

.toggle-button {
  background-color: transparent;
  color: #ff00dd;
  border: 1px solid #ff00dd;
}

.submit-button:hover, .toggle-button:hover {
  opacity: 0.9;
}

/* Десктопный режим */
@media (min-width: 1202px) {
  .container {
    width: 400px;
    padding: 50px;
  }
}

/* Планшетный режим и мобильный режим */
@media (max-width: 1201px) {
  /* На планшетах и мобильных устройствах элементы input будут идти друг под другом */
  .form-container {
    display: flex;
    flex-direction: column;
    gap: 15px; /* Добавляем небольшой зазор между элементами */
  }
}

@media (min-width: 804px) and (max-width: 1201px) {
  .container {
    min-width: 450px;
    width: 350px;
  }

  .header h1 {
    font-size: 24px;
  }
}

@media (max-width: 803px) {
  .container {
    min-width: 250px;
    width: 250px;
  }

  .header h1 {
    font-size: 20px;
  }

  .submit-button, .toggle-button {
    font-size: 14px;
  }
}
</style>
