<script setup>
import { RouterView } from 'vue-router'
import NavBlack from './components/Nav.vue'
import useAppStore from '@/pinia/appStore'
import axios from '@/config/Axios.js'
import wsClient from '@/config/websockets.js'

import { mapState } from 'pinia'
</script>

<template>
  <NavBlack />
  
  <div class="left-content"></div>
        <div class="main-content">
          <router-view />
        </div>

        <div class="right-content"></div>
</template>

<script>
export default {
  components: { NavBlack },
  data() {},
  computed: {
    ...mapState(useAppStore, ['isAuthenticated', 'accessToken'])
  },
  async mounted() {
    const store = useAppStore()
    try {
      const response = await axios.get('/user/authenticatedUser')
      store.setUsername(response.data.username)
      store.setIsAuthenticated(true)
      if (store.accessToken) {
        axios.defaults.headers.common['Authorization'] = 'Bearer ' + store.accessToken
      }

      console.log(wsClient)
          wsClient.connectWebSocket(store.username)
  

    } catch (err) {
      console.log('error trying to authenticate:' + err)
      store.setAuthorization(false)
    }

    console.log(this.isAuthenticated)
  }
}
</script>

<style scoped src="@/css/App.css"></style>
