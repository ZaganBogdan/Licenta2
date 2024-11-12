<template>
  <h3>Location LIST</h3>
  <div class="list" id="location-list">
    <div v-for="location in locations" :key="location.id" class="location-item" @click="handleLocationClick(location.id)">
      <input type="hidden" :value="location.id" />
      <div class="location-header">
        <div class="location-details"> <h2 class="location-name">{{ location.name }}</h2>
          <div class="location-address">
            <label>Address:</label>
            <span>{{ location.address }}</span>
          </div>
        </div>
        <img v-if="location.photo" :src="'data:image/jpeg;base64,' + location.photo" alt="Location Image" class="location-image"/>
      </div>
    </div>
  </div>
</template>

<script>

import axios from '@/config/Axios.js';

export default {
  data() {
    return {
      locations: [],
      msg: '',
    };
  },
  mounted(){
    axios.get(`http://localhost:8002/location/getAllLocations`)
        .then(response => {
            this.locations = response.data;
            console.log(this.matches);
       
        })
        .catch(error => {
          this.msg = error.message;
          console.error('Error fetching matches:', error);
        });
  },
  methods:{
    handleLocationClick(locationId) {
      this.$router.push({
         name: 'locationView',
         params: { id: locationId}
         })
    },
  }
}
</script>


<style scoped src="../css/Location.css"></style>