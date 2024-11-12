<template>
  <div class="login-container">
    <div class="form-group">
      <label for="email">Email:</label>
      <input type="email" name="email" v-model="email" required />
    </div>

    <div class="form-group">
      <label for="password">Password:</label>
      <input type="password" name="password" v-model="password" required />
    </div>
    <div class-="mb-3">
      <router-link to="/forgot">Forgot password?</router-link>
    </div>
    <div v-if="error" class="error-message">{{ error }}</div>
    <button class="signin-button" @click="loginRequest">Login</button>
    <div class="login-google mt-3">
      <GoogleLogin :callback="googleLogin" prompt/>
    </div>
    
  </div>
</template>

<script>
import axios from '@/config/Axios.js'
import useAppStore from '@/pinia/appStore'
import wsClient from '@/config/websockets.js'
// import vue3GoogleLogin from 'vue3-google-login';
//eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import gAuth from 'vue3-google-login'
export default {
  methods: {
    loginRequest() {
      this.error = ''
      axios
        .post(
          '/user/login',
          {
            email: this.email,
            password: this.password
          },
          { withCredentials: true }
        )
        .then((response) => {

          const store = useAppStore()
          store.setIsAuthenticated(true)
          store.setAccessToken(response.data.accessToken)
          store.setUsername(response.data.username);

          axios.defaults.headers.common['Authorization'] = 'Bearer ' + store.accessToken

          wsClient.connectWebSocket(store.username)
  
          this.$router.push({ name: 'explore' })
      
        })
        .catch((error) => {
          this.error = error.response.data
          console.error(error.data)
        })
    },
   async googleLogin(response){
    console.log("am ajuns in google")
    console.log(response)
    const jwt = response.credential;
        
        const tokenPayload = jwt.split('.')[1];
        const decodedPayload = JSON.parse(atob(tokenPayload));
        const idToken = decodedPayload.sub; 
        
        console.log(idToken);
    try {
        const { status, data } = await axios.post('/user/google-oauth2', {
          token: response.credential
        }, { withCredentials: true });

          const store = useAppStore()
          store.setIsAuthenticated(true)
          store.setAccessToken(response.data.accessToken)
          axios.defaults.headers.common['Authorization'] = 'Bearer ' + store.accessToken

          this.$router.push({ name: 'userHome' })
        }catch(error){
          console.log('am prins eroarea')
          this.error = error.response.data
          console.error(error.data)
        }
    }
  }
}
</script>

<style scoped src="../css/LogIn.css"></style>
