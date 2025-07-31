import { View, Text, ScrollView, ActivityIndicator, TouchableOpacity, FlatList } from 'react-native';
import { useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import habitService from '../../services/habitService';
import diaryEntryService from '../../services/diaryEntryService';
import { SafeAreaView } from 'react-native-safe-area-context';

export default function Diary() {
    const [diaryEntries, setDiaryEntries] = useState([]);
    const [myHabits, setMyHabits] = useState([]);
    const [habitEntries, setHabitEntries] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth());
    const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());

    const months = [
        'January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'
    ];

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            setLoading(true);
            const userId = await AsyncStorage.getItem('userId');
            
            const diaryResponse = await diaryEntryService.getDiaryEntries(userId);
            setDiaryEntries(diaryResponse.data || []);
            
            const habitsResponse = await habitService.getHabitsByUser(userId);
            setMyHabits(habitsResponse.data || []);
            
                const allHabitEntries = [];
            for (const userHabit of habitsResponse.data || []) {
                try {
                    const entriesResponse = await habitService.getHabitEntriesByUserAndUserHabit(userId, userHabit.id);
                    allHabitEntries.push(...(entriesResponse.data || []));
                } catch (err) {
                    console.warn(`Error fetching entries for habit ${userHabit.id}:`, err);
                }
            }
            setHabitEntries(allHabitEntries);
            
        } catch (err) {
            console.error('Error fetching data:', err);
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const filterEntriesByMonth = (entries, dateField = 'date') => {
        return entries.filter(entry => {
            const entryDate = new Date(entry[dateField]);
            return entryDate.getMonth() === selectedMonth && entryDate.getFullYear() === selectedYear;
        });
    };

    const getHabitsCompletedByDay = () => {
        const filteredEntries = filterEntriesByMonth(habitEntries, 'date');
        const habitsByDay = {};
        
        filteredEntries.forEach(entry => {
            const day = new Date(entry.date).getDate();
            if (!habitsByDay[day]) {
                habitsByDay[day] = [];
            }
            
            const userHabit = myHabits.find(uh => uh.id === entry.userHabit.id);
            if (userHabit) {
                habitsByDay[day].push({
                    ...entry,
                    habitName: userHabit.habit?.name || 'Unknown habit',
                    habitCategory: userHabit.habit?.category?.name || 'No category'
                });
            }
        });
        
        return habitsByDay;
    };

    const changeMonth = (direction) => {
        if (direction === 'prev') {
            if (selectedMonth === 0) {
                setSelectedMonth(11);
                setSelectedYear(selectedYear - 1);
            } else {
                setSelectedMonth(selectedMonth - 1);
            }
        } else {
            if (selectedMonth === 11) {
                setSelectedMonth(0);
                setSelectedYear(selectedYear + 1);
            } else {
                setSelectedMonth(selectedMonth + 1);
            }
        }
    };

    const renderDayWithHabits = (day, habits) => (
        <View key={day} className="bg-white rounded-lg p-3 mb-3 shadow-sm">
            <Text className="text-lg font-semibold text-gray-800 mb-2">
                {day} of {months[selectedMonth]}
            </Text>
            {habits.length > 0 ? (
                habits.map((habit, index) => (
                    <View key={index} className="bg-green-50 rounded-lg p-2 mb-1 border-l-4 border-green-500">
                        <Text className="text-green-800 font-medium">{habit.habitName}</Text>
                        <Text className="text-green-600 text-xs">{habit.habitCategory}</Text>
                    </View>
                ))
            ) : (
                <Text className="text-gray-500 italic">No habits completed this day</Text>
            )}
        </View>
    );

    if (loading) {
        return (
            <View className="flex-1 justify-center items-center bg-gray-50">
                <ActivityIndicator size="large" color="#3b82f6" />
                <Text className="mt-2 text-gray-600">Loading diary...</Text>
            </View>
        );
    }

    if (error) {
        return (
            <View className="flex-1 justify-center items-center bg-gray-50 p-4">
                <Text className="text-red-600 text-center mb-4">Error: {error}</Text>
                <TouchableOpacity 
                    onPress={fetchData}
                    className="bg-blue-500 px-4 py-2 rounded-lg"
                >
                    <Text className="text-white font-medium">Retry</Text>
                </TouchableOpacity>
            </View>
        );
    }

    const habitsByDay = getHabitsCompletedByDay();
    const daysWithHabits = Object.keys(habitsByDay).sort((a, b) => parseInt(b) - parseInt(a));

    return (
        <SafeAreaView className="flex-1 bg-white">
            <View className="flex-1">
                <View className="bg-white p-4 border-b border-gray-200">
                    <View className="flex-row items-center justify-between">
                        <TouchableOpacity 
                            onPress={() => changeMonth('prev')}
                        className="bg-blue-100 p-2 rounded-lg"
                    >
                        <Text className="text-blue-600 font-medium">‹ Previous</Text>
                    </TouchableOpacity>
                    
                    <Text className="text-xl font-bold text-gray-800">
                        {months[selectedMonth]} {selectedYear}
                    </Text>
                    
                    <TouchableOpacity 
                        onPress={() => changeMonth('next')}
                        className="bg-blue-100 p-2 rounded-lg"
                    >
                        <Text className="text-blue-600 font-medium">Next ›</Text>
                    </TouchableOpacity>
                </View>
            </View>

            <View className="bg-white mx-4 mt-4 rounded-lg p-4 shadow-sm">
                <Text className="text-lg font-semibold text-gray-800 mb-2">
                    {months[selectedMonth]} Summary
                </Text>
                <View className="flex-row justify-between">
                    <View className="flex-1">
                        <Text className="text-2xl font-bold text-green-600">
                            {daysWithHabits.length}
                        </Text>
                        <Text className="text-gray-600 text-sm">Active days</Text>
                    </View>
                    <View className="flex-1">
                        <Text className="text-2xl font-bold text-blue-600">
                            {Object.values(habitsByDay).reduce((total, habits) => total + habits.length, 0)}
                        </Text>
                        <Text className="text-gray-600 text-sm">Habits completed</Text>
                    </View>
                </View>
            </View>

            <ScrollView className="flex-1 px-4 mt-4">
                {daysWithHabits.length > 0 ? (
                    daysWithHabits.map(day => 
                        renderDayWithHabits(parseInt(day), habitsByDay[day])
                    )
                ) : (
                    <View className="bg-white rounded-lg p-6 mt-4">
                        <Text className="text-center text-gray-500 text-lg">
                            No habits completed in {months[selectedMonth]} {selectedYear}
                        </Text>
                        <Text className="text-center text-gray-400 mt-2">
                            Start completing your habits to see your progress here!
                        </Text>
                    </View>
                )}
            </ScrollView>
        </View>
    </SafeAreaView>
);
}