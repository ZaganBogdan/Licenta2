import axios from "axios";
import useAppStore from '@/pinia/appStore'

axios.defaults.baseURL = 'http://localhost:8001';

let refresh = false;

axios.interceptors.response.use(resp => resp, async error => {
    if (error.response.status === 401 && !refresh) {
        refresh = true;
        const response = await axios.post('/user/refresh-token', {}, {withCredentials: true});

        if (response.status === 200) {
          console.log(response.data)
          const store = useAppStore()
            store.setAccessToken(response.data)
            store.setIsAuthenticated(true)
            axios.defaults.headers.common['Authorization'] = `Bearer ${response.data}`;
            error.config.headers['Authorization'] = `Bearer ${response.data}`;
            console.log(error.config.headers)
            return axios(error.config);
        }
    }
    refresh = false;
    return error;
});

export default axios