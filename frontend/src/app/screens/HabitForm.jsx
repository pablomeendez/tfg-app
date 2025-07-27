import React, { useState, useEffect, useContext } from 'react';
import { View, Text, TouchableOpacity, ActivityIndicator, ScrollView } from 'react-native'; // AGREGAR ActivityIndicator y ScrollView
import { SafeAreaView } from 'react-native-safe-area-context';
import { useRouter } from 'expo-router';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { AuthContext } from '../../context/AuthContext'; // AGREGAR ESTE IMPORT
import habitService from '../../services/habitService';
import HabitCard from '../../components/HabitCard';

const HabitForm = () => {
    const [allHabits, setAllHabits] = useState([]);
    const [myHabits, setMyHabits] = useState([]);
    const router = useRouter();
    const { userId } = useContext(AuthContext);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchHabits = async () => {
            try {
                const allHabitsResponse = await habitService.getAllHabits();
                
                let userIdToUse = userId;
                if (!userIdToUse) {
                    userIdToUse = await AsyncStorage.getItem('userId');
                }

                if (userIdToUse) {
                    const myHabitsResponse = await habitService.getHabitsByUser(userIdToUse);
                    setMyHabits(myHabitsResponse.data || []); 
                }
                
                setAllHabits(allHabitsResponse.data || []);
                setLoading(false);
            } catch (err) {
                console.error('HabitForm: Error fetching habits:', err);
                setError(err.message);
                setLoading(false);
            }
        };

        fetchHabits();
    }, [userId]); 

    const handleAddHabit = async (habitId) => {
        try {
            await habitService.createUserHabit(habitId);
            
            let userIdToUse = userId;
            if (!userIdToUse) {
                userIdToUse = await AsyncStorage.getItem('userId');
            }
            
            if (userIdToUse) {
                const updatedMyHabits = await habitService.getHabitsByUser(userIdToUse);
                setMyHabits(updatedMyHabits.data || []); // AGREGAR || []
            }
        } catch (err) {
            console.error('Error adding habit:', err);
            setError(err.message);
        }
    }

    const handleDeleteHabit = async (userHabitId) => {
        try {
            await habitService.deleteUserHabit(userHabitId);
            
            let userIdToUse = userId;
            if (!userIdToUse) {
                userIdToUse = await AsyncStorage.getItem('userId');
            }
            
            if (userIdToUse) {
                const updatedMyHabits = await habitService.getHabitsByUser(userIdToUse);
                setMyHabits(updatedMyHabits.data);
            }
        } catch (err) {
            console.error('Error deleting habit:', err);
            setError(err.message);
        }
    }

    if (loading) {
        return (
            <SafeAreaView className="flex-1 justify-center items-center">
                <ActivityIndicator size="large" />
                <Text className="mt-2">Cargando hábitos...</Text>
            </SafeAreaView>
        );
    }

    if (error) {
        return (
            <SafeAreaView className="flex-1 justify-center items-center">
                <Text className="text-red-500 text-center mb-4">Error: {error}</Text>
                <TouchableOpacity 
                    onPress={() => {
                        setError(null);
                        setLoading(true);
                    }}
                    className="bg-blue-500 px-4 py-2 rounded"
                >
                    <Text className="text-white">Reintentar</Text>
                </TouchableOpacity>
            </SafeAreaView>
        );
    }

    return (
        <SafeAreaView className="flex-1"> 
            <ScrollView className="flex-1 p-4"> 
                <Text className="text-2xl font-bold mb-4 text-center">Habit Form</Text> 
                {allHabits.map((habit, index) => (
                    <View key={`habit-${habit.id || index}`} className="mb-4"> 
                        <HabitCard habit={habit} />
                        <View className="flex-row justify-center items-center mt-2"> 
                            {myHabits.some(myHabit => myHabit.habit?.id === habit.id) ? 
                                <TouchableOpacity 
                                    className="bg-red-500 w-9/12 justify-center h-10 rounded-lg" 
                                    onPress={() => {
                                        const userHabit = myHabits.find(myHabit => myHabit.habit?.id === habit.id);
                                        if (userHabit) {
                                            handleDeleteHabit(userHabit.id);
                                        }
                                    }}>
                                    <Text className="text-center text-white font-semibold">Remove</Text> 
                                </TouchableOpacity>
                            : (
                                <TouchableOpacity 
                                    className="bg-blue-500 w-9/12 justify-center h-10 rounded-lg" 
                                    onPress={() => handleAddHabit(habit.id)}
                                >
                                    <Text className="text-center text-white font-semibold">Add</Text>
                                </TouchableOpacity>
                            )}
                        </View>
                    </View>
                ))}
                
                {allHabits.length === 0 && (
                    <Text className="text-gray-500 text-center py-4">No hay hábitos disponibles</Text>
                )}
            </ScrollView>
            
            <View className="p-4"> 
                <TouchableOpacity 
                    className="bg-green-600 p-4 rounded-lg" 
                    onPress={() => router.replace('/(tabs)')} 
                >
                    <Text className="text-center text-white text-lg font-semibold">Done</Text> 
                </TouchableOpacity>
            </View>
        </SafeAreaView>
    )
}

export default HabitForm;