<template>

    <div class="content">
       <div class="spacer"></div> 
      <div class="main-content">
        <div class="title-container">
          <h1>Start planning your sports activities effortlessly today!</h1>
          <h2>From football to running and beyond, get ready to make every sports activity an unforgettable experience.</h2>
          <router-link to="/signup" class="bottom-button">Signup</router-link>
        </div>
        <div class="start-image-container">
          <img class="start-image" src="../images/imagine_start.png" alt="Start Image" />
        </div>
      </div>
       <div class="spacer"></div>
    </div>

</template>

<script>
export default {
methods:{
  logInEvent(){
    this.$router.push({ name: 'logIn' })
  },
  signUpEvent(){
    this.$router.push({ name: 'signUp' })
  }
}
}
</script>

<style scoped src="../css/StartPage.css"></style>
