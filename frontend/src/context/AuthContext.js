import React, { createContext, useState, useEffect} from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';
import userService from '../services/userService';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [userToken, setUserToken] = useState(null);
    const [loading, setLoading] = useState(true);

    const checkLogin = async () => {
        try {
            const token = await AsyncStorage.getItem('userToken');
            if (token) {
                setUserToken(token);
                axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            }
        } catch (e) {
            console.log('Error checking login:', e);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        checkLogin();
    }, []);

    const register = async (userName, password, name, lastName, email) => {
        try {
            return await userService.register(userName, password, name, lastName, email);
        } catch (e) {
            console.log('Error in register:', e);
            throw e;
        }
    }

    const login = async (userName, password) => {
        try {
            const result = await userService.login(userName,password);
            const token = result.data?.serviceToken;
    
            await AsyncStorage.setItem('userToken', token);
            setUserToken(token);
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    
            return result;
        } catch (e) {
            throw e;
        }
    };

    const logout = async () => {
        try {
            await userService.logout();
            await AsyncStorage.removeItem('userToken');
            setUserToken(null);
            axios.defaults.headers.common['Authorization'] = '';
        } catch (e) {
            console.log('Error in logout:', e);
            throw e;
        }
    };

    const value = {
        userToken,
        loading,
        login,
        register,
        logout,
        isAuthenticated: !!userToken
    };
    
    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}