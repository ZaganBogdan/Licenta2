import { createRouter, createWebHistory } from 'vue-router'
import StartPage from '../views/StartPage.vue'
import Login from '../views/LogIn.vue'
import Signup from '../views/SignUp.vue'
import UserHome from '../views/UserHome.vue'
import Forgot from '../views/Forgot.vue'
import Reset from '../views/Reset.vue'
import Explore from '../views/Explore.vue'
import Locations from '../views/Locations.vue'
import LocationView from '../views/LocationView.vue'
import Profile from '../views/Profile.vue'
import MatchView from '../views/MatchView.vue'
import YourMatches from '../views/YourMatches.vue'
import CreateMatch from '../views/CreateMatch.vue'


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
      {
        path: '/',
        name: 'startPage',
        component: StartPage,
      },
      {
        path: '/signup',
        name: 'signup',
        component: Signup,
      },
      {
        path: '/login',
        name: 'login',
        component: Login,
      },
      {
        path: '/home',
        name: 'userHome',
        component: UserHome,
      },
      {
        path: '/forgot',
        name: 'forgot',
        component: Forgot,
      },
      {
        path: '/reset/:token',
        name: 'reset',
        component: Reset,
      },
      {
        path: '/explore',
        name: 'explore',
        component: Explore,
      },
      {
        path: '/locations',
        name: 'locations',
        component: Locations,
      },
      {
        path: '/locationView/:id',
        name: 'locationView',
        component: LocationView,
      },
      {
        path: '/profile',
        name: 'profile',
        component: Profile,
      },
      {
        path: '/matchView/:id',
        name: 'matchView',
        component: MatchView,
      },
      {
        path: '/matches',
        name: 'yourMatches',
        component: YourMatches,
      },
      {
        path: '/createMatch/',
        name: 'CreateMatch',
        component: CreateMatch,
      }
    ]
})
export default router