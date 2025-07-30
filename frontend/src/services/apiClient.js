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

// Interceptor para agregar el token automáticamente a todas las peticiones
apiClient.interceptors.request.use(
    async (config) => {
        const token = await AsyncStorage.getItem('userToken');
        console.log('Token from AsyncStorage:', token); // Debug log
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
            console.log('Authorization header set:', config.headers.Authorization); // Debug log
        } else {
            console.log('No token found in AsyncStorage'); // Debug log
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Interceptor para manejar respuestas y errores
apiClient.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error) => {
        console.log('API Error:', error.response?.status, error.response?.data); // Debug log
        if (error.response?.status === 401) {
            console.log('401 Unauthorized - Token might be invalid or expired');
            // Opcional: limpiar el token inválido
            // await AsyncStorage.removeItem('userToken');
        }
        return Promise.reject(error);
    }
);

export default apiClient;