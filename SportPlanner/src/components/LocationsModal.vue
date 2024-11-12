<template>
    <div
      class="modal fade"
      id="locationsModal"
      tabindex="-1"
      aria-labelledby="locationsModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="locationsModalLabel"> Choose a Location</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div class="input-group mb-3">
              <input type="text" class="form-control" placeholder="Search location..." v-model="searchQuery" @input="searchLocations">
              <button class="btn btn-outline-secondary" type="button" @click="searchLocations"><i class="bi bi-search"></i> Search</button>
            </div>
            <ul class="list-group">
              <li v-for="location in filteredLocations" :key="location.id" class="list-group-item" @click="selectLocation(location)">
                {{ location.name }} - {{ location.address }}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { ref } from 'vue'
  import axios from '@/config/Axios.js'
  
  export default {
    data() {
      return {
        locations: [],
        searchQuery: '',
      }
    },
    computed: {
      filteredLocations() {
        return this.locations.filter(location => 
          location.name.toLowerCase().includes(this.searchQuery.toLowerCase()) || 
          location.address.toLowerCase().includes(this.searchQuery.toLowerCase())
        )
      }
    },
    methods: {
      fetchLocations() {
        axios.get(`http://localhost:8002/location/getAllLocations`)
          .then(response => {
            this.locations = response.data
          })
          .catch(error => {
            console.error(error)
          })
      },
      searchLocations() {
        this.fetchLocations()
      },
      selectLocation(location) {
        this.$emit('locationSelected', location)
        var modal = bootstrap.Modal.getInstance(document.getElementById('locationsModal'))
        modal.hide()
      }
    },
    mounted() {
      this.fetchLocations()
    }
  }
  </script>
  
  <style scoped>
  .modal-header {
    background-color: #f8f9fa;
  }
  
  .modal-title {
    font-size: 1.25rem;
    font-weight: bold;
  }
  
  .list-group-item {
    cursor: pointer;
  }
  </style>
  