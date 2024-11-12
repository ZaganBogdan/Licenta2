<template>
  
</template>

<script setup>

import { ref, onMounted } from 'vue';
import axios from "@/config/Axios.js"
import useAppStore from '@/pinia/appStore'
import explore from '@/views/Explore.vue'


  const errorMessage = ref('')
  const store = useAppStore();
  const component = ref(explore);

  onMounted(async () => {
  try{
     await axios.get('/user/authenticatedUser')
                  .then((response) =>{
                    console.log(response)
                    console.log("sa vedem userul"+response.data.username);
                    const store = useAppStore()
                    store.setUsername(response.data.username)
                  })
    await store.setIsAuthenticated(true)
  } catch (err) {
    await store.setAuthorization(false)
  }
});

</script>

<style scoped>
.left-content .btn {
  color: black; 
}

.left-content{
  margin-right: auto; 
  width: 25%;
}
 .right-content {
  width: 25%;
}

.main-content {
  min-width: 400px;
  height: 100%;
  width: 50%;
  border-left: 2px black solid ;
  border-right: 2px black solid ;

}
.router-link-active {
  background-color: green; 
  color: white;
}
.custom-router-link {
  width: 200px; 
  height: 50px; 
  display: inline-block; 
  text-align: left;
}

</style>