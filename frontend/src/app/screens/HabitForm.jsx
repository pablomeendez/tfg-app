import React, { useState, useEffect, useContext, useTransition } from 'react';
import { View, Text, TouchableOpacity, ActivityIndicator, ScrollView } from 'react-native'; // AGREGAR ActivityIndicator y ScrollView
import { SafeAreaView } from 'react-native-safe-area-context';
import { useRouter } from 'expo-router';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { AuthContext } from '../../context/AuthContext';
import habitService from '../../services/habitService';
import HabitCard from '../../components/HabitCard';
import { ErrorComponent } from '../../components/ErrorComponent';
import { LoadingComponent } from '../../components/LoadingComponent';

const HabitForm = () => {
    const [allHabits, setAllHabits] = useState([]);
    const [myHabits, setMyHabits] = useState([]);
    const router = useRouter();
    const { userId } = useContext(AuthContext);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const { t } = useTransition();

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
                setMyHabits(updatedMyHabits.data || []);
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


    return (
        <SafeAreaView className="flex-1"> 
            {error ? (
                <ErrorComponent error={error} />
            ) : loading ? (
                <LoadingComponent />
            ) : 
            <View>
                <ScrollView className="flex-1 p-4"> 
                    <Text className="text-2xl font-bold mb-4 text-center">{t('habit_form')}</Text> 
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
                                    <Text className="text-center text-white font-semibold">{t('remove')}</Text> 
                                </TouchableOpacity>
                            : (
                                <TouchableOpacity 
                                    className="bg-blue-500 w-9/12 justify-center h-10 rounded-lg" 
                                    onPress={() => handleAddHabit(habit.id)}
                                >
                                    <Text className="text-center text-white font-semibold">{t('add')}</Text>
                                </TouchableOpacity>
                            )}
                        </View>
                    </View>
                ))}
                
                {allHabits.length === 0 && (
                    <Text className="text-gray-500 text-center py-4">{t('no_habits_available')}</Text>
                )}
                </ScrollView>
                
                <View className="p-4"> 
                    <TouchableOpacity 
                        className="bg-green-600 p-4 rounded-lg" 
                        onPress={() => router.replace('/(tabs)')} 
                    >
                        <Text className="text-center text-white text-lg font-semibold">{t('done')}</Text> 
                    </TouchableOpacity>
                </View>
            </View>}
        </SafeAreaView>
    )
}

export default HabitForm;