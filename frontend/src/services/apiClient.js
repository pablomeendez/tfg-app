import axios from 'axios';
import AsyncStorage from "@react-native-async-storage/async-storage";
import { Platform } from 'react-native';

const getBaseURL = () => {
    if (Platform.OS === 'android') {
        return 'http://192.168.1.3:8080/api';
    } else {
        return 'http://localhost:8080/api';
    }
}

const apiClient = axios.create({
    baseURL: getBaseURL(),
    httpsAgent: false,
    headers: {
        'Content-Type': 'application/json'
    },
});

apiClient.interceptors.request.use(
    async (config) => {
        const token = await AsyncStorage.getItem('userToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        } else {
            console.log('No token found in AsyncStorage'); 
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

apiClient.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error) => {
        console.log('API Error:', error.response?.status, error.response?.data); 
        if (error.response?.status === 401) {
            console.log('401 Unauthorized - Token might be invalid or expired');
        }
        return Promise.reject(error);
    }
);

export default apiClient;