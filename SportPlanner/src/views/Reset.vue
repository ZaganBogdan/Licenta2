<template>
  <div class="password-container">
    <div class="form-group">
      <label for="password">New Password:</label>
      <input type="password" name="password" v-model="password" required />
    </div>
    <div class="form-group">
      <label for="confirmPassword">Confirm New Password:</label>
      <input type="password" name="confirmPassword" v-model="confirmPassword" required />
    </div>
    <div v-if="error" class="error-message">{{ error }}</div>
    <button class="reset-button" @click="resetPassword">Reset</button>
  </div>
</template>

<script>
import axios from '@/config/Axios.js'
import { useRoute, useRouter } from 'vue-router'

export default {
  data() {
    return {
      error: '',
      password: '',
      confirmPassword: '',
      queryParams: {},
      showQueryParams: false
    }
  },
   setup() {
    const route = useRoute()


      const token = route.params.token
      return {token}
    },
  methods: {
    resetPassword() {
      if (this.checkPasswords()) {

        console.log('aici'+ this.token)
        this.error = ''
        axios
          .post('/user/reset-password/'+this.token, {
            password: this.password,
            confirmPassword: this.confirmPassword
          })
          .then((response) => {
            if (response.status === 200) {
              this.$router.push({ name: 'login' })
            } else {
              this.error = response.response.data
            }
          })
          .catch((error) => {
            this.error = error
            console.error(error)
          })
      }
    },
    checkPasswords: function () {
      if (this.password !== this.confirmPassword) {
        this.error = 'Passwords do not match'
        return false
      }
      return true
    }
  }
}
</script>

<style scoped src="../css/Reset.css"></style>
