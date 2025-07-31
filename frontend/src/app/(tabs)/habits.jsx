import { View, Text, ScrollView, ActivityIndicator, TouchableOpacity } from 'react-native';
import { useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import habitService from '../../services/habitService';
import HabitCard from '../../components/HabitCard';
import { SafeAreaView } from 'react-native-safe-area-context';

export default function Habits() { 
    const [allHabits, setAllHabits] = useState([]);
    const [myHabits, setMyHabits] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchHabits = async () => {
            try {
                const response = await habitService.getAllHabits();
                const userId = await AsyncStorage.getItem('userId');
                const myHabitsResponse = await habitService.getHabitsByUser(userId);
                setAllHabits(response.data);
                setMyHabits(myHabitsResponse.data); 
                setLoading(false);
            } catch (err) {
                console.error('Error fetching habits:', err);
                setError(err.message);
                setLoading(false);
            }
        };

        fetchHabits();
    }, []);

    const handleAddHabit = async (habitId) => {
        try {
            await habitService.createUserHabit(habitId);
            const userId = await AsyncStorage.getItem('userId');
            const updatedMyHabits = await habitService.getHabitsByUser(userId);
            
            setMyHabits(updatedMyHabits.data);
        } catch (err) {
            console.error('Error adding habit:', err);
            setError(err.message);
        }
    }

    const handleDeleteHabit = async (userHabitId) => {
        try {
            await habitService.deleteUserHabit(userHabitId);
            const userId = await AsyncStorage.getItem('userId');
            const updatedMyHabits = await habitService.getHabitsByUser(userId);
            
            setMyHabits(updatedMyHabits.data);
        } catch (err) {
            console.error('Error deleting habit:', err);
            setError(err.message);
        }
    }

    if (loading) {
        return (
            <View className="flex-1 justify-center items-center">
                <ActivityIndicator />
            </View>
        );
    }

    if (error) {
        return (
            <View className="flex-1 justify-center items-center">
                <Text>Error: {error}</Text>
            </View>
        );
    }

    return (
        <SafeAreaView className="flex-1 bg-white">
            <ScrollView className="flex-1 p-4">
                <View className="mb-6">
                    <Text className="text-2xl font-bold text-gray-800 mb-4">
                        Mis Hábitos
                    </Text>
                    {myHabits.map((userHabit, index) => (
                        <View key={`my-habit-${index}`}>
                            <HabitCard habit={allHabits.find(h => h.id === userHabit.habit.id)} />
                            <View className="flex-row justify-center items-center mt-2 mb-3">
                                <TouchableOpacity 
                                    className="bg-red-500 w-9/12 justify-center h-10 rounded-lg pb-2" 
                                    onPress={() => {
                                        handleDeleteHabit(userHabit.id)
                                    }}>
                                    <Text className="text-center text-white font-semibold">Remove</Text> 
                                </TouchableOpacity>
                            </View>
                        </View>
                    ))}
                </View>

                <View>
                    <Text className="text-2xl font-bold text-gray-800 mb-4">
                        Todos los Hábitos
                    </Text>
                    {allHabits.map((habit, index) => (
                        <View className="mb-2" key={`my-habit-${index}`}>
                            <HabitCard habit={habit} />
                            {myHabits.some(myHabit => myHabit.habit.id === habit.id) ? null : (
                                <View className="flex-row justify-center items-center mt-2 mb-2">
                                    <TouchableOpacity 
                                        className="bg-blue-500 w-9/12 justify-center h-10 rounded-lg" 
                                        onPress={() => handleAddHabit(habit.id)}
                                        >
                                        <Text className="text-center text-white font-semibold">Add</Text>
                                    </TouchableOpacity>
                                </View>
                            ) }
                        </View>
                    ))}
                </View>
            </ScrollView>
        </SafeAreaView>
    );
}