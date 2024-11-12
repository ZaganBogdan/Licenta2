<template>
  <div class="container mt-5">
    <h1 class="mb-4 text-center">Create a New Match</h1>
    <form @submit.prevent="addMatch" class="p-4 border rounded bg-light shadow-sm">
      <div class="mb-3">
        <label for="matchTitle" class="form-label">Match Title</label>
        <input type="text" class="form-control" id="matchTitle" v-model="matchTitle" placeholder="Enter match title" required />
      </div>
      <div class="d-flex">
        <div class="mb-3 me-2 flex-grow-1">
          <label for="matchDate" class="form-label"><i class="bi bi-calendar-event"></i> Date</label>
          <input type="date" class="form-control" id="matchDate" v-model="matchDate" required />
        </div>
        <div class="mb-3 me-2 flex-grow-1">
          <label for="matchStartHour" class="form-label"><i class="bi bi-clock"></i> Start Hour</label>
          <input type="time" class="form-control" id="matchStartHour" v-model="matchStartHour" required />
        </div>
        <div class="mb-3 flex-grow-1">
          <label for="matchEndHour" class="form-label"><i class="bi bi-clock"></i> End Hour</label>
          <input type="time" class="form-control" id="matchEndHour" v-model="matchEndHour" required />
        </div>
      </div>
      <div class="mb-3">
        <label for="matchLevel" class="form-label"><i class="bi bi-star-half"></i> Level</label>
        <select class="form-select" v-model="matchLevel" required>
          <option value="Amateur">Amateur</option>
          <option value="Intermediate">Intermediate</option>
          <option value="Professional">Professional</option>
        </select>
      </div>
      <div class="d-flex">
        <div class="mb-3 me-2 flex-grow-1">
          <label for="teams" class="form-label"><i class="bi bi-people"></i> Teams</label>
          <select class="form-select" v-model="teamsNumber" required>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
          </select>
        </div>
        <div class="mb-3 me-2 flex-grow-1">
          <label for="playersPerTeam" class="form-label">  <i class="bi bi-person"></i>Players Per Team</label>
          <select class="form-select" v-model="playersPerTeam" required>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
            <option value="6">6</option>
            <option value="7">7</option>
            <option value="8">8</option>
            <option value="9">9</option>
            <option value="10">10</option>
            <option value="11">11</option>
          </select>
        </div>
      </div>
      <div class="mb-3">
        <label for="matchPrice" class="form-label"><i class="bi bi-cash"></i> Price Per Player</label>
        <div class="input-group">
          <input type="text" class="form-control" v-model="price" placeholder="Enter price per player" required />
          <span class="input-group-text">Lei</span>
        </div>
      </div>
      <div class="mb-3">
        <label for="matchLocation" class="form-label"><i class="bi bi-geo-alt"></i> Location</label>
        <div class="input-group">
          <input type="text" class="form-control" id="matchLocation" v-model="matchLocation" placeholder="city, street, street number" readonly required />
          <button class="btn btn-outline-secondary" type="button" data-bs-toggle="modal" data-bs-target="#locationsModal">Choose Location</button>
        </div>
      </div>
      <div class="mb-3">
        <label for="matchDetails" class="form-label"><i class="bi bi-info-circle"></i> Details</label>
        <textarea class="form-control" id="matchDetails" v-model="matchDetails" rows="3" placeholder="Enter match details" required></textarea>
      </div>
  
      <div class="text-danger">{{ msg }}</div>
      <button type="submit" class="btn btn-primary w-100"><i class="bi bi-save"></i> Add Match</button>
    </form>
    <locations-modal @locationSelected="locationSelected"></locations-modal>
  </div>
</template>

<script>
import { ref } from 'vue'
import axios from '@/config/Axios.js'
import useAppStore from '@/pinia/appStore'
import LocationsModal from '@/components/LocationsModal.vue'

export default {
  components: {
    LocationsModal
  },
  data() {
    return {
      matchTitle: '',
      matchDate: '',
      matchStartHour: '',
      matchEndHour: '',
      matchLevel: '',
      teamsNumber: '',
      playersPerTeam: '',
      price: '',
      matchLocation: '',
      matchDetails: '',
      visibility: true,
      msg: '',
      locationId: '', // New data property to store location ID
    }
  },
  methods: {
    addMatch() {
      const store = useAppStore()
      let data = {
        title: this.matchTitle,
        owner: store.username,
        date: this.matchDate,
        startHour: this.matchStartHour,
        endHour: this.matchEndHour,
        level: this.matchLevel,
        teams: this.teamsNumber,
        playersPerTeam: this.playersPerTeam,
        price: this.price,
        location: this.matchLocation,
        details: this.matchDetails,
        visibility: this.visibility,
        locationId: this.locationId, // Include location ID in the request data
      }
      console.log(data)
      axios.post('http://localhost:8002/home/addMatch', data)
        .then((response) => {
          if (response.data.message) {
            this.msg = response.data.message
            return
          }
          if (response.status === 202) {
            this.msg = response.data
            return
          }
          this.msg = 'Match saved successfully'
        })
        .catch((error) => {
          this.msg = error
        })
    },
    locationSelected(location) {
      this.matchLocation = `${location.name} - ${location.address}`
      this.locationId = location.id // Store the location ID
    }
  }
}
</script>

<style scoped>
.container {
  max-width: 800px;
}

h1 {
  font-size: 2.5rem;
}

.form-label {
  font-weight: bold;
}

.input-group-text {
  background-color: #f8f9fa;
}

.btn-primary {
  background-color: #14751e;
}
</style>
