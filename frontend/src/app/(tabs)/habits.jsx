import { View, Text, ScrollView, ActivityIndicator, TouchableOpacity } from 'react-native';
import { useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import habitService from '../../services/habitService';
import HabitCard from '../../components/habits/HabitCard';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useTranslation } from 'react-i18next';
import { ErrorComponent } from '../../components/common/ErrorComponent';
import { LoadingComponent } from '../../components/common/LoadingComponent';
import useStore from '../../store/store';

export default function Habits() { 
    const allHabits = useStore((state) => state.habits);
    const setAllHabits = useStore((state) => state.setHabits);
    const myHabits = useStore((state) => state.userHabits);
    const setMyHabits = useStore((state) => state.setUserHabits);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { t } = useTranslation();

    useEffect(() => {
        const fetchHabits = async () => {
            try {
                if (allHabits.length === 0) {
                    const response = await habitService.getAllHabits();
                    setAllHabits(response.data);
                }
                const myHabitsResponse = await habitService.getHabitsByUser();
                setMyHabits(myHabitsResponse.data); 
            } catch (err) {
                setError(err.message);
                setLoading(false);
            } finally {
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
                (<ErrorComponent error={error} />) 
            :
            loading ? (
                <LoadingComponent />) 
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
                                    className="bg-red-500 rounded-md items-center justify-center w-9/12 h-10 mt-0"
                                    onPress={() => handleDeleteHabit(userHabit.id)}
                                >
                                    <Text className="text-white text-lg font-semibold">{t('remove')}</Text>
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
                                    <TouchableOpacity 
                                        className="bg-gray-500 rounded-md items-center justify-center w-9/12 h-10 mt-0 p-2 opacity-50"
                                        onPress={() => {}}
                                        disabled={true}
                                    >
                                        <Text className="text-white text-lg font-semibold">{t('already_added')}</Text>
                                    </TouchableOpacity>
                                </View> : (
                                <View className="flex-row justify-center items-center mt-2 mb-2">
                                    <TouchableOpacity 
                                        className="bg-blue-600 rounded-md items-center justify-center w-9/12 h-10 mt-0"
                                        onPress={() => handleAddHabit(habit.id)}
                                    >
                                        <Text className="text-white text-lg font-semibold">{t('add')}</Text>
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