<template>
  <div class="container match-view-container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <button class="btn-custom" @click="showParticipants" :class="{ active: showingParticipants }">Participants</button>
      <button class="btn-custom" @click="showDetails" :class="{ active: showingDetails }">Details</button>
    </div>
    <div v-if="showingParticipants">
      <div class="card mb-4">
        <div class="card-body text-center">
          <div class="button-container mb-4" v-if="!isPrevious">
            <div v-if="isOwner && availableSpots > 0">
              <button @click="invitePlayers" class="btn btn-primary btn-lg">
                Invite
              </button>
              <invitePlayers :match-id="match.id"></invitePlayers>
            </div>
            <button v-else-if="!isOwner && !isPending && !isParticipating && !isInvited" @click="joinMatch" class="btn btn-primary btn-lg">
              I want to play
            </button>
            <button v-else-if="!isOwner && isPending" @click="cancelRequest" class="btn btn-danger btn-lg">
              Cancel Request
            </button>
            <button v-else-if="!isOwner && requested && isParticipating" @click="leaveMatch" class="btn btn-danger btn-lg">
              Leave Match
            </button>
            <div v-else-if="!isOwner && isInvited">
              <button @click="acceptInvitation" class="btn btn-success btn-lg">
                Accept
              </button>
              <button @click="declineInvitation" class="btn btn-danger btn-lg">
                Decline
              </button>
            </div>
          </div>
          <div v-if="isJoining" class="joining-indicator">
            <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
            Joining...
          </div>
          <div class="confirmed-header mb-4">
            <h2>Confirmed ({{ match.players.length }}) <span class="badge badge-secondary">{{ availableSpots }} spots available</span></h2>
          </div>
          <div class="players-grid">
            <div class="player-slot" v-for="(player, index) in filledPlayers" :key="player.id" @click="player.isOpenSpot ? null : showUserProfile(player)">
              <img
                :src="player.isOpenSpot ? defaultPhoto : (player.photo ? 'data:image/jpeg;base64,' + player.photo : defaultPhoto)"
                class="user-img"
                :alt="player.isOpenSpot ? 'Open Spot' : 'User Photo'"
              >
              <div class="user-info" :class="{'left-align': index % 2 === 0, 'right-align': index % 2 !== 0}">
                <p v-if="!player.isOpenSpot"><strong>{{ player.username }}</strong></p>
                <p v-if="!player.isOpenSpot">{{ player.position }}</p>
                <p v-if="player.isOpenSpot"><strong>Open Spot</strong></p>
                <p v-if="player.isOpenSpot">N/A</p>
              </div>
            </div>
          </div>
          <div class="pending-header mb-4">
            <h2>Pending ({{ pendingRequests.length }})</h2>
          </div>
          <div class="user-card" v-for="pendingRequest in pendingRequests" :key="pendingRequest.id" @click="showUserProfile(pendingRequest.sendingUser)">
  <img 
    :src="pendingRequest.sendingUser.photo ? 'data:image/jpeg;base64,' + pendingRequest.sendingUser.photo : defaultPhoto"
    class="user-img" 
    alt="User Photo" 
  >
  <div class="user-info align">
    <p><strong>{{ pendingRequest.sendingUser.username }}</strong> </p>
    <p> {{ pendingRequest.sendingUser.position }}</p>
  </div>
  <div class="action-buttons" v-if="isOwner && availableSpots > 0">
    <button @click="acceptRequest(pendingRequest.id)" class="btn btn-success btn-sm">Accept</button>
    <button @click="declineRequest(pendingRequest.id)" class="btn btn-danger btn-sm">Decline</button>
  </div>
</div>
        </div>
      </div>
    </div>
    <div v-if="showingDetails">
      <div class="card mb-4">
        <div class="card-body">
          <h2>Match Details</h2>
          <h3>{{ match.title }}</h3>
          <p><i class="bi bi-calendar-event"></i> <strong>Date:</strong> {{ match.date }}</p>
          <p><i class="bi bi-clock"></i> <strong>Start Time:</strong> {{ match.startHour }}</p>
          <p><i class="bi bi-clock"></i> <strong>Finish Time:</strong> {{ match.endHour }}</p>
          <p><i class="bi bi-building"></i> <strong>Location:</strong> {{ match.location.name }}</p>
          <p><i class="bi bi-geo-alt"></i> <strong>Address:</strong> {{ match.location.address }}</p>
          <p><i class="bi bi-graph-up"></i> <strong>Level:</strong> {{ match.level }}</p>
          <p><i class="bi bi-currency-exchange"></i> <strong>Price:</strong> {{ match.price }} Lei</p>
          <p><i class="bi bi-people"></i> <strong>Teams:</strong> {{ match.teams }}</p>
          <p><i class="bi bi-people-fill"></i> <strong>Players per Team:</strong> {{ match.playersPerTeam }}</p>
          <p><i class="bi bi-card-text"></i> <strong>Details:</strong> {{ match.details }}</p>
        </div>
      </div>
    </div>
    <div v-if="notification" class="notification">
      {{ notification }}
    </div>
  </div>
</template>

<script>
import { Modal } from 'bootstrap';
import axios from "@/config/Axios.js";
import { useRoute } from 'vue-router';
import useAppStore from '@/pinia/appStore';
import moment from 'moment';
import invitePlayers from '@/views/InvitePlayers.vue';
import userProfile from '@/views/UserProfile.vue';
import defaultPhoto from '@/images/default-photo.jpg';

export default {
  components: {
    invitePlayers,
    userProfile,
  },
  data() {
    return {
      match: null,
      pendingRequests: [],
      pendingInvitations: [],
      availableSpots: 0,
      isOwner: false,
      error: '',
      showingParticipants: true,
      showingDetails: false,
      showingChat: false,
      requested: false,
      isPending: false,
      isJoining: false,
      isPrevious: false,
      isParticipating: false,
      isInvited: false,
      selectedUserProfile: null,
      userProfileModal: null,
      defaultPhoto,
      filledPlayers: [],
    };
  },
  created() {

    this.fetchMatchDetails();

  },
  mounted() {
    const inviteModalElement = document.getElementById('invitePlayers');
    if (inviteModalElement) {
      this.inviteModal = new Modal(inviteModalElement);
    }
    const userModalElement = document.getElementById('userProfile');
    if (userModalElement) {
      this.userProfileModal = new Modal(userModalElement);
    }
  },
  methods: {
    fetchMatchDetails() {
      const store = useAppStore();
      const route = useRoute();
      const matchId = route.params.id;

      axios.get(`http://localhost:8002/home/getMatch/${matchId}`)
        .then(response => {
          this.match = response.data;
          this.isPrevious = this.checkIfPrevious(this.match.date);
          this.calculateAvailableSpots();
          this.isOwner = this.match.owner.username === store.username;
          this.checkUserStatus(store.username);
          this.populateFilledPlayers();
          console.log("locatia "+this.match)
        })
        .catch(error => {
          this.error = 'Failed to fetch match details';
          console.error(error);
        });

      axios.get(`http://localhost:8002/request/getPendingRequests/${matchId}`)
        .then(response => {
          this.pendingRequests = response.data;
          this.checkUserStatus(store.username);
        })
        .catch(error => {
          this.error = 'Failed to fetch pending requests';
          console.error(error);
        });

      axios.get(`http://localhost:8002/request/getPendingInvitations/${matchId}`)
        .then(response => {
          this.pendingInvitations = response.data;
          this.checkUserStatus(store.username);
        })
        .catch(error => {
          this.error = 'Failed to fetch pending invitations';
          console.error(error);
        });
        
    },
    populateFilledPlayers() {
      if (this.match) {
        const totalSpots = this.match.teams * this.match.playersPerTeam;
        const filledPlayers = [...this.match.players];
        while (filledPlayers.length < totalSpots) {
          filledPlayers.push({ id: filledPlayers.length, isOpenSpot: true });
        }
        this.filledPlayers = filledPlayers;
      }
    },
    checkIfPrevious(matchDate) {
      const now = moment();
      const matchMoment = moment(matchDate);
      return matchMoment.isBefore(now);
    },
    calculateAvailableSpots() {
      if (this.match) {
        const totalSpots = this.match.teams * this.match.playersPerTeam;
        const takenSpots = this.match.players.length;
        this.availableSpots = totalSpots - takenSpots;
      }
    },
    checkUserStatus(username) {
      this.requested = this.match.players.some(player => player.username === username);
      this.isPending = this.pendingRequests.some(request => request.sendingUser.username === username);
      this.isParticipating = this.match.players.some(player => player.username === username);
      this.isInvited = this.pendingInvitations.some(invitation => invitation.receivingUser.username === username);
    },
    joinMatch() {
      const store = useAppStore();
      this.isJoining = true;
      const requestDto = {
        id: null,
        matchId: this.match.id,
        sendingUser: store.username,
        receivingUser: this.match.owner.username,
        requestType: 'MATCH_REQUEST',
        status: null
      };
      axios.post(`http://localhost:8002/request/sendRequest`, requestDto)
        .then(response => {
          console.log("Joined match successfully");
          this.fetchMatchDetails();
          this.isJoining = false;
          this.requested = true;
        })
        .catch(error => {
          console.error("Error joining match", error);
          this.error = 'Failed to join match';
          this.isJoining = false;
        });
    },
    leaveMatch() {
      const store = useAppStore();
      const leaveMatchDto = {
        matchId: this.match.id,
        leavingUser: store.username,
        ownerUser: this.match.owner.username
      };
      axios.post(`http://localhost:8002/home/leaveMatch`, leaveMatchDto)
        .then(response => {
          console.log("Left match successfully");
          this.fetchMatchDetails();
          this.requested = false;
        })
        .catch(error => {
          console.error("Error cancelling request or leaving match", error);
          this.error = 'Failed to cancel request or leave match';
        });
    },
    acceptRequest(requestId) {
      this.handleRequest(requestId, 'ACCEPTED');
    },
    declineRequest(requestId) {
      this.handleRequest(requestId, 'DECLINED');
    },
    cancelRequest() {
      const store = useAppStore();
      var requestId;
      for (var pendingRequest of this.pendingRequests) {
        if (pendingRequest.sendingUser.username == store.username) {
          requestId = pendingRequest.id;
        }
      }
      this.handleRequest(requestId, 'CANCELED');
    },
    handleRequest(requestId, action) {
      const store = useAppStore();
      const requestDto = {
        id: requestId,
        matchId: this.match.id,
        sendingUser: store.username,
        receivingUser: this.match.owner.username,
        requestType: 'MATCH_REQUEST',
        status: action
      };
      axios.post(`http://localhost:8002/request/responseRequest`, requestDto)
        .then(response => {
          console.log(`${action}ED request successfully`);
          this.fetchMatchDetails();
          this.fetchPendingRequests();
        })
        .catch(error => {
          console.error(`Error ${action}ing request`, error);
          this.error = `Failed to ${action} request`;
        });
    },
    fetchPendingRequests() {
      const route = useRoute();
      const matchId = route.params.id;
      axios.get(`http://localhost:8002/request/getPendingRequests/${matchId}`)
        .then(response => {
          console.log(response.data)
          this.pendingRequests = response.data;
        })
        .catch(error => {
          this.error = 'Failed to fetch pending requests';
          console.error(error);
        });
    },
    invitePlayers() {
      this.inviteModal.show();
    },
    showParticipants() {
      this.showingParticipants = true;
      this.showingDetails = false;
      this.showingChat = false;
    },
    showDetails() {
      this.showingParticipants = false;
      this.showingDetails = true;
      this.showingChat = false;
    },
    showChat() {
      this.showingParticipants = false;
      this.showingDetails = false;
      this.showingChat = true;
    },
    showUserProfile(user) {
      this.selectedUserProfile = user;
      this.userProfileModal.show();
    },
    acceptInvitation() {
      this.handleInvitation('ACCEPTED');
    },
    declineInvitation() {
      this.handleInvitation('DECLINED');
    },
    handleInvitation(action) {
      const store = useAppStore();
      const invitation = this.pendingInvitations.find(invitation => invitation.receivingUser.username === store.username);
      if (invitation) {
        const requestDto = {
          id: invitation.id,
          matchId: this.match.id,
          sendingUser:  this.match.owner.username,
          receivingUser: store.username,
          requestType: 'INVITE_REQUEST',
          status: action
        };
        axios.post(`http://localhost:8002/request/responseRequest`, requestDto)
          .then(response => {
            console.log(`${action}ED invitation successfully`);
            this.fetchMatchDetails();
          })
          .catch(error => {
            console.error(`Error ${action}ing invitation`, error);
            this.error = `Failed to ${action} invitation`;
          });
      }
    },
  }
};
</script>

<style scoped src="../css/MatchView.css"></style>
