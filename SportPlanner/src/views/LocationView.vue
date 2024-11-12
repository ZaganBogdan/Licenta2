<template>
  <div class="container match-view-container mt-5">
    <div class="card mb-4">
      <div class="card-body">
        <h2>Location Details</h2>
        <div class="row">
          <div class="col-md-6">
            <h3>{{ location.name }}</h3>
            <p><strong>Address:</strong> {{ location.address }}</p>
            <div class="mb-3">
              <label for="map" class="form-label">Map</label>
              <div ref="map" style="width: 100%; height: 400px;"></div>
            </div>
          </div>
          <div class="col-md-6">
            <p><strong>Description:</strong> {{ location.description }}</p>
            <img :src="location.photo ? 'data:image/jpeg;base64,' + location.photo : defaultPhoto" class="location-img" alt="Location Photo">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import axios from '@/config/Axios.js'
import { useRoute } from 'vue-router'
import useAppStore from '@/pinia/appStore'
import { Loader } from '@googlemaps/js-api-loader'
import { Client } from '@googlemaps/google-maps-services-js'

export default {
  data() {
    return {
      location: null,
      map: null,
      mapCenter: { lat: 0, lng: 0 },
      googleMapsClient: null,
      defaultPhoto: 'default-image-url', // Replace with your default image URL
    };
  },
  created() {
    this.fetchLocationDetails();
    this.googleMapsClient = new Client({});
  },
  methods: {
    fetchLocationDetails() {
      const store = useAppStore();
      const route = useRoute();
      const locationId = route.params.id;

      axios.get(`http://localhost:8002/location/getLocation/${locationId}`)
        .then(response => {
          this.location = response.data;
          this.mapCenter = { lat: this.location.latitude, lng: this.location.longitude };
          this.initMap();
        })
        .catch(error => {
          this.error = 'Failed to fetch location details';
          console.error(error);
        });
    },
    initMap() {
      const loader = new Loader({
        apiKey: 'AIzaSyDMHGeb_OXtp8pueKfKwBWqt_9H2hdNJPI',
        version: 'weekly',
      });
      loader.load().then(() => {
        this.map = new google.maps.Map(this.$refs.map, {
          center: this.mapCenter,
          zoom: 10,
        });
        new google.maps.Marker({
          position: this.mapCenter,
          map: this.map,
        });
      });
    },
  }
}
</script>

<style scoped src="../css/LocationView.css"></style>