import './assets/main.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap/dist/js/bootstrap.bundle.js'
import vue3GoogleLogin from 'vue3-google-login'
import ToastPlugin from 'vue-toast-notification'
import 'vue-toast-notification/dist/theme-bootstrap.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'bootstrap-icons/font/bootstrap-icons.css'
import '@fortawesome/fontawesome-free/css/all.min.css';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'



const pinia = createPinia()
const app = createApp(App)

app.use(pinia)
app.use(router)
app.use(ToastPlugin)
app.component('font-awesome-icon', FontAwesomeIcon)
app.use(vue3GoogleLogin, {
  clientId: '407964874603-dt5r3uksoirparcaep85t6vo4nlum73t.apps.googleusercontent.com',
})
app.mount('#app')
