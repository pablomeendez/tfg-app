import AsyncStorage from '@react-native-async-storage/async-storage';
import apiClient from './apiClient';

const userService = {
    register: (userName, password, name, lastName, email) => {
        return apiClient.post('/users/signup', {
            userName: userName, 
            password: password, 
            firstName: name, 
            lastName: lastName, 
            email: email
        });
    },
    login: (userName, password) => {
        return apiClient.post('/users/login', {
            userName: userName, 
            password: password
        });
    },
    logout: async () => {
        await AsyncStorage.removeItem('userToken');
    },
    updateProfile: (userId, userData) => {
        return apiClient.put(`/users/${userId}`, userData);
    },
    loginFromServiceToken: async (userId, serviceToken) => {
        return apiClient.post('/users/login-service-token', {
            userId: userId,
            serviceToken: serviceToken
        });
    },
};

export default userService;

