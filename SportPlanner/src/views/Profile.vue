<template>
  <div class="profile-container">
    <h2>User Profile</h2>
    <form @submit.prevent="saveProfile">
      <div class="mb-3">
        <label for="userPhoto" class="form-label">User Photo</label>
        <div>
          <img :src="profile.photo ? 'data:image/jpeg;base64,' + profile.photo : defaultPhoto" class="user-img" alt="User Photo">
        </div>
        <input type="file" @change="uploadPhoto" class="form-control" id="userPhoto">
      </div>
      <div class="mb-3">
        <label for="username" class="form-label">Username</label>
        <input type="text" class="form-control" id="username" v-model="profile.username" disabled />
      </div>
      <div class="row">
        <div class="col-md-6 mb-3">
          <label for="firstName" class="form-label">First Name</label>
          <input type="text" class="form-control" id="firstName" v-model="profile.firstName" required />
        </div>
        <div class="col-md-6 mb-3">
          <label for="lastName" class="form-label">Last Name</label>
          <input type="text" class="form-control" id="lastName" v-model="profile.lastName" required />
        </div>
      </div>
      <div class="mb-3">
        <label for="phone" class="form-label">Phone</label>
        <input type="text" class="form-control" id="phone" v-model="profile.phone" required />
      </div>
      <div class="row">
        <div class="col-md-6 mb-3">
          <label for="birthday" class="form-label">Birthday</label>
          <input type="date" class="form-control" id="birthday" v-model="profile.birthday"/>
        </div>
        <div class="col-md-6 mb-3">
          <label for="age" class="form-label">Age</label>
          <input type="number" class="form-control" id="age" v-model="profile.age" />
        </div>
      </div>
      <div class="row">
        <div class="col-md-6 mb-3">
        <label for="country" class="form-label">Country</label>
        <input type="text" class="form-control" id="country" v-model="profile.country" />
      </div>
      <div class="col-md-6 mb-3">
        <label for="nationality" class="form-label">Nationality</label>
        <input type="text" class="form-control" id="nationality" v-model="profile.nationality" required />
      </div>
      </div>
      <div class="mb-3">
        <label for="location" class="form-label">Location</label>
        <input type="text" class="form-control" id="location" v-model="profile.location" />
      </div>
     
      <div class="row">
        <div class="col-md-6 mb-3">
          <label for="height" class="form-label">Height (cm)</label>
          <input type="number" class="form-control" id="height" v-model="profile.height" />
        </div>
        <div class="col-md-6 mb-3">
          <label for="weight" class="form-label">Weight (kg)</label>
          <input type="number" class="form-control" id="weight" v-model="profile.weight" />
        </div>
      </div>
      <div class="row">
        <div class="col-md-6 mb-3">
        <label for="position" class="form-label">Position</label>
        <select class="form-select" id="position" v-model="profile.position" >
          <option value="GOALKEEPER">Goalkeeper</option>
          <option value="DEFENDER">Defender</option>
          <option value="MIDFIELDER">Midfielder</option>
          <option value="FORWARD">Forward</option>
        </select>
      </div>
      <div class="col-md-6 mb-3">
        <label for="foot" class="form-label">Preferred Foot</label>
        <select class="form-select" id="foot" v-model="profile.foot" >
          <option value="LEFT">Left</option>
          <option value="RIGHT">Right</option>
          <option value="BOTH">Both</option>
        </select>
      </div>
      </div>
      <div class="mb-3">
        <button type="submit" class="btn btn-primary">Save Profile</button>
      </div>
      <div class="alert alert-danger" v-if="error">{{ error }}</div>
      <div class="alert alert-success" v-if="success">{{ success }}</div>
    </form>
  </div>
</template>

<script>
import axios from "@/config/Axios.js";
import useAppStore from '@/pinia/appStore';
import defaultPhoto from '@/images/default-photo.jpg'; 

export default {
  data() {
    return {
      profile: {
        username: '',
        firstName: '',
        lastName: '',
        phone: '',
        birthday: '',
        country: '',
        location: '',
        nationality: '',
        age: '',
        height: '',
        weight: '',
        position: '',
        foot: '',
        photo: ''
      },
      defaultPhoto,
      error: '',
      success: '',
      
    };
  },
  created() {
    this.fetchProfile();
  },
  methods: {

    fetchProfile() {
      const store = useAppStore();
      axios.get('http://localhost:8002/user/getProfile/'+store.username)
        .then(response => {
          this.profile = response.data;
        })
        .catch(error => {
          this.error = 'Failed to fetch profile data';
          console.error(error);
        });
    },

    saveProfile() {

      axios.post('http://localhost:8002/user/setProfile', this.profile)
        .then(response => {
          this.profile = response.data;
          this.success = 'Profile updated successfully';
          this.error = '';
        })
        .catch(error => {
          this.error = 'Failed to update profile';
          this.success = '';
          console.error(error);
        });
    },

    uploadPhoto(event) {
      const file = event.target.files[0];
      if (!file) return;

      const formData = new FormData();
      formData.append('file', file);

      axios.post('http://localhost:8002/user/uploadPhoto', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      .then(response => {
        this.profile.photo = response.data.photo;
        console.log("dupa ce am adaugat :"+ this.profile.photo)

        this.success = 'Photo uploaded successfully';
      })
      .catch(error => {
        this.error = 'Failed to upload photo';
        console.error(error);
      });
    }
  }
};
</script>

<style scoped>
.profile-container {
  max-width: 600px;
  margin: auto;
}
.user-img {
  max-width: 150px;
  border-radius: 50%;
  margin-bottom: 10px;
}
</style>