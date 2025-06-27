import AsyncStorage from '@react-native-async-storage/async-storage';
import apiClient from './apiClient';
import axios from 'axios';

const removeServiceToken = () => {
    AsyncStorage.removeItem('userToken')
    axios.defaults.headers.common['Authorization'] = '';
}


const userService = {
    register: (userName, password, name, lastName, email) => {
        return apiClient.post('/users/signUp', {userName: userName, password: password, firstName: name, lastName: lastName, email: email});
    },
    login: (userName, password) => {
        return apiClient.post('/users/login', {userName: userName, password: password});
    },
    logout: () => {
        removeServiceToken();
    }
};

export default userService;

