<template>
  <div class="modal fade" id="userProfile" tabindex="-1" aria-labelledby="userProfileModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="userProfileModalLabel">User Profile</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <img :src="userProfile.photo ? 'data:image/jpeg;base64,' + userProfile.photo : defaultPhoto" class="user-img" alt="User Photo">
          <p><strong>Username:</strong> {{ userProfile.username }}</p>
          <p><strong>First Name:</strong> {{ userProfile.firstName }}</p>
          <p><strong>Last Name:</strong> {{ userProfile.lastName }}</p>
          <p><strong>Age:</strong> {{ userProfile.age }}</p>
          <p><strong>Country:</strong> {{ userProfile.country }}</p>
          <p><strong>Location:</strong> {{ userProfile.location }}</p>
          <p><strong>Nationality:</strong> {{ userProfile.nationality }}</p>
          <p><strong>Position:</strong> {{ userProfile.position }}</p>
          <p><strong>Foot:</strong> {{ userProfile.foot }}</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { toRaw, reactive, watch } from 'vue';
import defaultPhoto from '@/images/default-photo.jpg';

const props = defineProps(['userProfile']);
let userProfile = Object.assign({}, toRaw(props.userProfile));
let state = reactive({ userProfile: userProfile });

watch(() => props.userProfile, (newProfile, prevProfile) => {
  userProfile = Object.assign({}, toRaw(newProfile));
  state.userProfile = userProfile;
});
</script>

<style scoped>
.user-img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}
</style>
