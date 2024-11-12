import SockJS from 'sockjs-client'
import * as StompJs from '@stomp/stompjs'
import { useToast } from 'vue-toast-notification'
import 'vue-toast-notification/dist/theme-sugar.css'



const wsClient = {
  stompClient: null,
  connectWebSocket: function (username) {
    const socket = new SockJS('http://localhost:8081/ws')
    this.stompClient = StompJs.Stomp.over(socket)
    console.log("am ajuns in conect");
    this.stompClient.connect(
      {},
      () => {
        console.log("apelez conect");
        this.onWebSocketConnected(username)
      },
      (error) => {
        console.log("eroare in conect");
        this.onWebSocketError(error)
      }
    )
  },

  onWebSocketConnected: function (username) {
    console.log("am ajuns in on connected");
    this.stompClient.subscribe(`/user/${username}/notifications`, (message) => {
      this.onNotificationReceived(message)
    })
  },

  sendNotification: function (username, message) {
    const notification = {
      receiver: username,
      message: message
    }
    this.stompClient.send('/app/notifications', {}, JSON.stringify(notification))
  },

  onWebSocketError: function (error) {
    console.error('WebSocket connection error:', error)
  },

  onNotificationReceived: function (message) {
    console.log("am primit notificarea");
    const $toast = useToast()
    const payload = message.body
    $toast.info(payload, { duration: 10000 })
    
  }
}
export default wsClient
