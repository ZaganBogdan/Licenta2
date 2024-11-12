import { defineStore } from 'pinia'
import { useLocalStorage } from '@vueuse/core'

const useAppStore = defineStore('appStore', {
  state: () => ({
    accessToken: useLocalStorage('accessToken', ''),
    refreshToken: useLocalStorage('refreshToken', ''),
    hasAuthorization: true,
    isAuthenticated: false,
    username:'',
    photo:''
  }),
  actions: {
    setAccessToken(value) {
      this.accessToken = value
    },
    setRefreshToken(value) {
      this.refreshToken = value
    },
    setAuthorization(value) {
      this.hasAuthorization = value
    },
    setIsAuthenticated(value) {
      this.isAuthenticated = value
    },
    setUsername(value) {
      this.username = value
    },
    setPhoto(value) {
      this.photo = value
    }
  }
})
export default useAppStore
