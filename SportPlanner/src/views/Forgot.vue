<template>
  <div class="email-container">
    <div class="form-group">
      <label for="email">Please insert your email:</label>
      <input type="email" name="email" v-model="email" required />
    </div>
    <div v-if="error" class="error-message">{{ error }}</div>
    <button class="submit-button" @click="forgotRequest">Submit</button>
  </div>
</template>

<script>
import axios from '@/config/Axios.js'
import useAppStore from '@/pinia/appStore'

export default {
  data() {
    return {
      error: ''
    }
  },
  methods: {
    forgotRequest() {
      this.error = ''
      axios
        .post('/user/forgot-password/' + this.email)
        .then((response) => {
          if (response.status === 200) {
            this.$router.push({ name: 'login' })
          }else{
            this.error = response.response.data
          }
        })
        .catch((error) => {
          this.error = error
          console.error(error)
        })
    }
  }
}
</script>

<style scoped src="../css/Forgot.css"></style>
