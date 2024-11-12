<template>
   <div class="signup-container">
    <h3>Sign Up</h3>
      <div class="form-group">  
        <label for="email">Email:</label>
        <input type="email" name="email" v-model="email" required />
      </div>

      <div class="form-group">
        <label for="username">Username:</label>
        <input type="username" name="username" v-model="username" required />
      </div>

      <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" name="password" v-model="password" required />
      </div>

      <div class="form-group">
        <label for="confirmPassword"> Confirm Password:</label>
        <input type="password" name="confirmPassword" v-model="confirmPassword" required />
      </div>
      <div class="form-group">
        <label for="type">Type:</label>
        <select name="type" v-model="type" required>
          <option value="ADMIN">ADMIN</option>
          <option value="USER">USER</option>
        </select>
      </div>
      <div v-if="error" class="error-message">{{ error }}</div>
      <button class="signup-button" @click="signUpRequest">Sign Up</button>
      </div>
    
</template>

<script>

import axios from "@/config/Axios.js"
import { inject } from 'vue';

export default {
    data(){
      return{  
        email: '',
        usename: '',
        password: '',
        confirmPassword: '',
        type: '',
        error: ''
       }
    },

    methods:{
        signUpRequest(){
            this.error = ''
            if(this.checkPasswords()){
            axios.post('/user/signup', {
                email: this.email,
                username: this.username,
                type: this.type,
                password: this.password,
                confirmPassword: this.confirmPassword
                })
            .then((response) => {
          console.log(response.data)
          this.$router.push({ name: 'login' })
        })
        .catch((error) => {
            console.log("am prins eroarea")
            this.error = error.response.data
            console.error(error.data);
        });
        }
        },
    
        checkPasswords: function () {
      if (this.password !== this.confirmPassword) {
        this.error = 'Passwords do not match'
        return false;
      }
      return true;
    }

    }
}
</script>

<style scoped src="../css/SignUp.css">

</style>