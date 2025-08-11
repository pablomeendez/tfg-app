import { View, Text, ScrollView, ActivityIndicator, TouchableOpacity } from 'react-native';
import { useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import habitService from '../../services/habitService';
import HabitCard from '../../components/HabitCard';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useTranslation } from 'react-i18next';

export default function Habits() { 
    const [allHabits, setAllHabits] = useState([]);
    const [myHabits, setMyHabits] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { t } = useTranslation();

    useEffect(() => {
        const fetchHabits = async () => {
            try {
                const response = await habitService.getAllHabits();
                const myHabitsResponse = await habitService.getHabitsByUser();
                setAllHabits(response.data);
                setMyHabits(myHabitsResponse.data); 
                setLoading(false);
            } catch (err) {
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

    return (
        <SafeAreaView className="flex-1 bg-white">
            {error ? 
                (<View className="flex-1 justify-center items-center bg-gray-50 p-4">
                    <Text className="text-red-600 text-center mb-4">{t('error')}: {error}</Text>
                </View>) 
            :
            loading ? (
                <View className="flex-1 justify-center items-center bg-gray-50">
                    <ActivityIndicator size="large" color="#3b82f6" />
                <Text className="mt-2 text-gray-600">{t('loading')}</Text>
            </View> ) 
            :
            <ScrollView className="flex-1 p-4">
                <View className="mb-6">
                    <Text className="text-2xl font-bold text-gray-800 mb-4">
                        {t('my_habits')}
                    </Text>
                    {myHabits.map((userHabit, index) => (
                        <View key={`my-habit-${index}`}>
                            <HabitCard habit={userHabit.habit} />
                            <View className="flex-row justify-center items-center mt-2 mb-3">
                                <TouchableOpacity 
                                    className="bg-red-500 w-9/12 justify-center h-10 rounded-lg pb-2" 
                                    onPress={() => {
                                        handleDeleteHabit(userHabit.id)
                                    }}>
                                    <Text className="text-center text-white font-semibold">{t('remove')}</Text> 
                                </TouchableOpacity>
                            </View>
                        </View>
                    ))}
                </View>

                <View>
                    <Text className="text-2xl font-bold text-gray-800 mb-4">
                        {t('all_habits')}
                    </Text>
                    {allHabits.map((habit, index) => (
                        <View className="mb-2" key={`my-habit-${index}`}>
                            <HabitCard habit={habit} />
                            {myHabits.some(myHabit => myHabit.habit.id === habit.id) ? 
                                <View className="flex-row justify-center items-center mt-2 mb-2">
                                    <View 
                                        className="bg-gray-500 w-9/12 justify-center h-10 rounded-lg">
                                        <Text className="text-center text-white font-semibold">{t('already_added')}</Text>
                                    </View>
                                </View> : (
                                <View className="flex-row justify-center items-center mt-2 mb-2">
                                    <TouchableOpacity 
                                        className="bg-blue-500 w-9/12 justify-center h-10 rounded-lg" 
                                        onPress={() => handleAddHabit(habit.id)}
                                        >
                                        <Text className="text-center text-white font-semibold">{t('add')}</Text>
                                    </TouchableOpacity>
                                </View>
                            ) }
                        </View>
                    ))}
                </View>
            </ScrollView>}
        </SafeAreaView>
    );
}