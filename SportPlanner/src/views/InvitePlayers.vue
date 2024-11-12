<template>
  <div class="modal fade" id="invitePlayers" tabindex="-1" role="dialog" aria-labelledby="invitePlayersModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="invitePlayersModalLabel">Invite Players</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <div class="search-bar">
            <input type="text" v-model="searchQuery" @input="filterUsers" class="form-control" placeholder="Search users by name or position">
          </div>

          <div class="filter-position">
            <select v-model="selectedPosition" @change="filterUsers" class="form-select">
              <option value="">All Positions</option>
              <option value="GOALKEEPER">Goalkeeper</option>
              <option value="DEFENDER">Defender</option>
              <option value="MIDFIELDER">Midfielder</option>
              <option value="FORWARD">Forward</option>
            </select>
          </div>

          <div class="user-card" v-for="user in filteredUsers" :key="user.id">
            <img src="@/images/default-photo.jpg" class="user-img" alt="Anonymous User">
            <div class="user-info">
              <p><strong>Username:</strong> {{ user.username }}</p>
              <p><strong>Position:</strong> {{ user.position }}</p>
              <p><strong>Foot:</strong> {{ user.foot }}</p>
            </div>
            <div class="action-buttons">
              <button v-if="!isInvited(user)" @click="sendInvitation(user)" class="btn btn-primary btn-sm">Invite</button>
              <button v-else @click="cancelInvitation(user)" class="btn btn-danger btn-sm">Cancel</button>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary" @click="sendInvites">Send Invites</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "@/config/Axios.js";
import useAppStore from '@/pinia/appStore'
import { nextTick } from 'vue';
import { Modal } from 'bootstrap';

export default {
  name: 'InvitePlayers',
  props: {
    matchId: {
      type: Number,
      required: true
    }
  },

    data() {
      return {
        users: [],
        searchQuery: '',
        selectedPosition: '',
        filteredUsers: [],
        invitedUsers: [],
        inviteModal: null
      };
    },
    created() {
        console.log("AM ajuns aici"+this.matchId)
         axios.get(`http://localhost:8002/home/getNonParticipatingUsers/${this.matchId}`)
         .then(response => {
           this.users = response.data;
           this.filteredUsers = this.users;
            console.log(this.users)
         })
         .catch(error => {
           this.msg = error.message;
           console.error('Error on getting non participating users', error);
         });
      
    },
  mounted() {
    nextTick(() => {
      this.inviteModal = new Modal(document.getElementById('invitePlayers'));
    });
  },
    methods: {
      filterUsers() {
        const query = this.searchQuery.toLowerCase();
        const position = this.selectedPosition;
  
        this.filteredUsers = this.users.filter(user => {
          const matchesQuery = user.username.toLowerCase().includes(query);
          const matchesPosition = position ? user.position === position : true;
          return matchesQuery && matchesPosition;
        });
      },
      isInvited(user) {
      return this.invitedUsers.includes(user.id);
      },
      sendInvitation(user) {
      const store = useAppStore();
      const requestDto = {
        id: null,
        matchId: this.matchId,
        sendingUser: store.username,
        receivingUser: user.username,
        requestType: 'INVITE_REQUEST',
        status: null
      };
      console.log(requestDto)
      axios.post(`http://localhost:8002/request/sendRequest`, requestDto)
        .then(response => {
          console.log("Invite request  successfully");
          this.invitedUsers.push(user.id);
        })
        .catch(error => {
          console.error("Error joining match", error);
          this.error = 'Failed to join match';
        })
    },
    cancelInvitation(user) {
        const store = useAppStore();
      const requestDto = {
        id: requestId,
        matchId: this.match.id,
        sendingUser: store.username,
        receivingUser: this.match.owner.username,
        requestType: 'MATCH_REQUEST',
        status: 'CANCELED'
      };
      axios.post(`http://localhost:8002/request/responseRequest`, requestDto)
        .then(response => {
          console.log(`Canceled request successfully`);
          this.invitedUsers = this.invitedUsers.filter(id => id !== user.id);
        })
        .catch(error => {
          console.error(`Error canceling request`, error);
          this.error = `Failed to cancel request`;
        });
    },
    }
  };
  </script>
  
  <style scoped src="../css/InvitePlayers.css"></style>
  