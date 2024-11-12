<template>
  <header class="navbar navbar-expand-lg navbar-light bg-danger">
    <div class="container-fluid">
      <router-link to="/" class="navbar-brand text-white">
        <img src="@/images/logo.png" alt="Logo" class="d-inline-block align-text-top" width="30" height="30">
        SportPlanner
      </router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul v-if="isAuthenticated" class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link to="/explore" class="nav-link text-white">
              <i class="fas fa-compass"></i> Explore
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/matches" class="nav-link text-white">
              <i class="fas fa-futbol"></i> Games
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/locations" class="nav-link text-white">
              <i class="fas fa-map"></i> Locations
            </router-link>
          </li>
          <li class="nav-item">
            <router-link to="/createMatch" class="nav-link text-white">
              <i class="fas fa-calendar-plus"></i> Create Match
            </router-link>
          </li>
        </ul>
        <div class="d-flex align-items-center ms-auto">
          <router-link v-if="!isAuthenticated" to="/login" class="btn btn-outline-light me-2">Login</router-link>
          <router-link v-if="!isAuthenticated" to="/signup" class="btn btn-outline-light">Signup</router-link>
          <div v-if="isAuthenticated" class="d-flex align-items-center">
            <router-link to="/notifications" class="nav-link text-white">
              <i class="fas fa-bell"></i>
            </router-link>
            <div class="dropdown">
              <a class="nav-link dropdown-toggle text-white" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                <img :src="userPhoto" alt="Profile Photo" class="rounded-circle" width="30" height="30">
                {{ username }}
              </a>
              <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                <li><router-link to="/profile" class="dropdown-item">Profile</router-link></li>
                <li><router-link to="/settings" class="dropdown-item">Settings</router-link></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item" href="#" @click="onLogout">Logout</a></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>


<script>
import axios from "axios";
import useAppStore from '../pinia/appStore';
import { mapState } from 'pinia';

export default {
  computed: {
    ...mapState(useAppStore, ['isAuthenticated', 'accessToken', 'username', 'userPhoto'])
  },
  methods: {
    async onLogout() {
      await axios.post('/user/logout', {}, { withCredentials: true });
      axios.defaults.headers.common['Authorization'] = '';
      useAppStore().setIsAuthenticated(false);
      useAppStore().setAccessToken('');
    }
  }
}
</script>

<style scoped>
.navbar {
  background-color: #14751e !important; /* Green background color */
}

.nav-link {
  color: white !important;
}

.dropdown-menu {
  background-color: white;
}

.dropdown-item {
  color: black !important;
}

.dropdown-item:hover {
  background-color: #14751e !important; /* Green hover color */
  color: white !important;
}
</style>
