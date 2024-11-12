<template>
  
</template>

<script>
export default {

}
</script>

<style>

</style>
<!-- <template>
    <div class="chat-container">
      <div class="chat-window">
        <div class="messages">
          <div v-for="message in messages" :key="message.id" class="message">
            <strong>{{ message.sender }}</strong>: {{ message.content }}
          </div>
        </div>
        <div class="chat-input">
          <input v-model="message" @keyup.enter="sendMessage" placeholder="Type a message..." />
          <button @click="sendMessage">Send</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import SockJS from 'sockjs-client';
  import { Stomp } from '@stomp/stompjs';
  
  export default {
    data() {
      return {
        socket: null,
        stompClient: null,
        messages: [],
        message: '',
        username: '',
        matchId: this.$route.params.id
      };
    },
    created() {
      this.connect();
    },
    methods: {
      connect() {
        this.socket = new SockJS('http://localhost:8080/ws');
        this.stompClient = Stomp.over(this.socket);
  
        this.stompClient.connect({}, frame => {
          this.stompClient.subscribe(`/topic/match.${this.matchId}`, tick => {
            const message = JSON.parse(tick.body);
            this.messages.push(message);
          });
  
          this.stompClient.send('/app/chat.addUser',
            {},
            JSON.stringify({ sender: this.username, type: 'JOIN' })
          );
        });
      },
      sendMessage() {
        if (this.message.trim()) {
          const chatMessage = {
            sender: this.username,
            content: this.message,
            type: 'CHAT'
          };
  
          this.stompClient.send('/app/chat.sendMessage', {}, JSON.stringify(chatMessage));
          this.message = '';
        }
      }
    }
  };
  </script>

  
<style scoped src="../css/Chat.css"></style> -->
