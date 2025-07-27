import React, { createContext, useState, useEffect} from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import userService from '../services/userService';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [userToken, setUserToken] = useState(null);
    const [userId, setUserId] = useState(null);
    const [loading, setLoading] = useState(true);

    const verifyUserExists = async (userId, token) => {
        try {
            const response = await userService.loginFromServiceToken(userId, token);
            return response.status === 200;
        } catch (error) {
            console.log('User verification failed:', error);
            return false;
        }
    };

    const clearAuthData = async () => {
        await AsyncStorage.removeItem('userToken');
        await AsyncStorage.removeItem('userId');
        setUserToken(null);
        setUserId(null);
        console.log('Auth data cleared');
    };

    const checkLogin = async () => {
        try {
            const token = await AsyncStorage.getItem('userToken');
            const userId = await AsyncStorage.getItem('userId');
            if (token && userId) {
                const userExists = await verifyUserExists(userId);
                
                if (userExists) {
                    setUserToken(token);
                    setUserId(userId);
                    console.log('User verified and logged in:', userId);
                } else {
                    console.log('User no longer exists, clearing auth data');
                    await clearAuthData();
                }
            } else {
                console.log('No stored auth data found');
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
            throw e;w
        }
    }

    const login = async (userName, password) => {
        try {
            const result = await userService.login(userName, password);
            const token = result.data?.serviceToken;
            const userId = result.data?.user.id;
            
            if (token) {
                await AsyncStorage.setItem('userToken', token);
                await AsyncStorage.setItem('userId', userId.toString());
                setUserToken(token);
                setUserId(userId);
            } else {
                console.log('No token received from login response'); // Debug log
            }
    
            return result;
        } catch (e) {
            console.log('Error in login:', e);
            throw e;
        }
    };

    const logout = async () => {
        try {
            await userService.logout();
            await AsyncStorage.removeItem('userToken');
            await AsyncStorage.removeItem('userId');
            setUserToken(null);
            setUserId(null);
        } catch (e) {
            console.log('Error in logout:', e);
            throw e;
        }
    };

    const value = {
        userToken,
        userId,
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