<template>
  <div class="match-container">
    <div class="filter-section">
      <h3>Filter By</h3>
      <div class="filters">
        <div class="filter-item">
          <label for="date-filter">Date:</label>
          <input type="date" id="date-filter" v-model="filters.date" @change="applyFilters" />
        </div>
        <div class="filter-item">
          <label for="time-filter">Time:</label>
          <input type="time" id="time-filter" v-model="filters.time" @change="applyFilters" />
        </div>
        <div class="filter-item">
          <label for="location-filter">Location:</label>
          <input type="text" id="location-filter" v-model="filters.location" @input="applyFilters" placeholder="Enter location" />
        </div>
        <div class="filter-item">
          <label for="level-filter">Level:</label>
          <select id="level-filter" v-model="filters.level" @change="applyFilters">
            <option value="">All</option>
            <option value="Beginner">Beginner</option>
            <option value="Intermediate">Intermediate</option>
            <option value="Advanced">Advanced</option>
          </select>
        </div>
      </div>
    </div>
    <div class="match-list">
      <h3>Match LIST</h3>
      <div class="list" id="match-list">
        <div v-for="match in filteredMatches" :key="match.id" class="match-item" @click="handleMatchClick(match.id)">
          <input type="hidden" :value="match.id" />
          <div class="match-header">
            <h2 class="match-title">{{ match.title }}</h2>
          </div>
          <div class="match-details">
            <div class="match-info-left">
              <div class="match-info">
                <i class="fas fa-calendar-alt"></i>
                <label>Date:</label>
                <span>{{ match.date }}</span>
              </div>
              <div class="match-info">
                <i class="fas fa-clock"></i>
                <label>Time:</label>
                <span>{{ match.startHour }}</span>
              </div>
              <div class="match-info">
                <i class="bi bi-geo-alt"></i>
                <label>Location:</label>
                <span>{{ match.location.name }}</span>
              </div>
            </div>
            <div class="match-info-right">
              <div class="match-info">
                <i class="bi bi-star"></i>
                <label>Level:</label>
                <span>{{ match.level }}</span>
              </div>
              <div class="match-info">
                <i class="bi bi-cash"></i>
                <label>Price:</label>
                <span>{{ match.price }} Lei</span>
              </div>
              <div class="match-info">
                <i class="bi bi-people"></i>
                <label>Players:</label>
                <span>{{ match.players.length }} / {{ match.teams * match.playersPerTeam }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from '@/config/Axios.js';
import useAppStore from '@/pinia/appStore'

export default {
  data() {
    return {
      matches: [],
      filteredMatches: [],
      filters: {
        date: '',
        location: '',
        time: '',
        level: ''
      },
      msg: '',
    };
  },
  mounted() {
    this.fetchMatches();
  },
  methods: {
    fetchMatches() {
      const store = useAppStore();
      axios.get(`http://localhost:8002/home/getAllMatches/${store.username}`)
        .then(response => {
          this.matches = response.data;
          this.filteredMatches = this.matches;
        })
        .catch(error => {
          this.msg = error.message;
          console.error('Error fetching matches:', error);
        });
    },
    handleMatchClick(matchId) {
      this.$router.push({
         name: 'matchView',
         params: { id: matchId }
      });
    },
    applyFilters() {
      this.filteredMatches = this.matches.filter(match => {
        const matchDate = new Date(match.date);
        const filterDate = this.filters.date ? new Date(this.filters.date) : null;
        const filterTime = this.filters.time ? this.filters.time : null;

        return (!this.filters.date || matchDate.toISOString().split('T')[0] === filterDate.toISOString().split('T')[0]) &&
               (!this.filters.location || match.location.name.toLowerCase().includes(this.filters.location.toLowerCase())) &&
               (!this.filters.time || match.startHour <= filterTime && match.endHour >= filterTime) &&
               (!this.filters.level || match.level === this.filters.level);
      });
    }
  }
};
</script>
<style scoped src="../css/Explore.css"></style>
