import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api',
    httpsAgent: false,
    headers: {
        'Content-Type': 'application/json'
    },
});

export default apiClient;