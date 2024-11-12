<template>
   <div class="container match-view-container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <button class="btn-custom" @click="showUpcomings" :class="{ active: showingUpcomings }">Upcoming</button>
      <button class="btn-custom" @click="showInvitations" :class="{ active: showingInvitations }">Invitation</button>
      <button class="btn-custom" @click="showPrevious" :class="{ active: showingPrevious }">Previous</button>
    </div>
    <div v-if="showingUpcomings">
  <div class="match-container">
    <div class="match-list">
      <h3>Match LIST</h3>
      <div class="list" id="match-list">
        <div v-for="match in matches" :key="match.id" class="match-item" @click="handleMatchClick(match.id)">
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
  </div>
  <div v-if="showingInvitations">
  <div class="match-container">
    <div class="match-list">
      <h3>Match LIST</h3>
      <div class="list" id="match-list">
        <div v-for="match in matches" :key="match.id" class="match-item" @click="handleMatchClick(match.id)">
          <input type="hidden" :value="match.id" />
          <div class="match-header">
            <h2 class="match-title">{{ match.title }}</h2>
            <span class="match-level">{{ match.level }}</span>
          </div>
          <div class="match-details">
            <div class="match-date">
              <label>Date:</label>
              <span>{{ match.date }}</span>
            </div>
            <div class="match-time">
              <label>Time:</label>
              <span>{{ match.startHour }} - {{ match.endHour }}</span>
            </div>
            <div class="match-info">
              <label>Location:</label>
              <span>{{ match.location }}</span>
            </div>
          </div>
          <div class="match-footer">
            <div class="match-price">
              <span>{{ match.price }}</span>
              <span class="currency">Lei</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    </div>
  </div>
  <div v-if="showingPrevious">
  <div class="match-container">
    <div class="match-list">
      <h3>Match LIST</h3>
      <div class="list" id="match-list">
        <div v-for="match in matches" :key="match.id" class="match-item" @click="handleMatchClick(match.id)">
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
      showingUpcomings:true,
      showingInvitations: false,
      showingPrevious: false,
      msg: '',
    };
  },
  mounted() {
    this.fetchUpcomingMatches();
  },
  methods: {
    fetchUpcomingMatches() {
      const store = useAppStore();
      axios.get(`http://localhost:8002/home/getUserUpcomingMatches/${store.username}`)
        .then(response => {
          this.matches = response.data;
          console.log(this.matches);
        })
        .catch(error => {
          this.msg = error.message;
          console.error('Error fetching upcoming matches:', error);
        });
    },
    fetchInvitations() {
      const store = useAppStore();
      axios.get(`http://localhost:8002/home/getUserInvitations/${store.username}`)
        .then(response => {
          this.matches = response.data;
          console.log(this.matches);
        })
        .catch(error => {
          this.msg = error.message;
          console.error('Error fetching invitations:', error);
        });
    },
    fetchPreviousMatches() {
      const store = useAppStore();
      axios.get(`http://localhost:8002/home/getUserPreviousMatches/${store.username}`)
        .then(response => {
          this.matches = response.data;
          console.log(this.matches);
        })
        .catch(error => {
          this.msg = error.message;
          console.error('Error fetching previous matches:', error);
        });
    },
    handleMatchClick(matchId) {
      this.$router.push({
         name: 'matchView',
         params: { id: matchId}
         })
    },
    removeMatch(match) {
      this.matches = this.matches.filter(m => m.id !== match.id);
    },
    showUpcomings() {
      this.showingUpcomings = true;
      this.showingInvitations = false;
      this.showingPrevious = false;
      this.fetchUpcomingMatches();
    },
    showInvitations() {
      this.showingUpcomings = false;
      this.showingInvitations = true;
      this.showingPrevious = false;
      this.fetchInvitations();
    },
    showPrevious() {
      this.showingUpcomings = false;
      this.showingInvitations = false;
      this.showingPrevious = true;
      this.fetchPreviousMatches();
    }
  }
};
</script>

<style scoped src="../css/YourMatches.css"></style>
