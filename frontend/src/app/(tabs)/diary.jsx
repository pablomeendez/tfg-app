import { View, Text, ScrollView, ActivityIndicator, TouchableOpacity, FlatList, Modal } from 'react-native';
import { useState, useEffect } from 'react';
import { Image } from 'expo-image';
import AsyncStorage from '@react-native-async-storage/async-storage';
import habitService from '../../services/habitService';
import diaryEntryService from '../../services/diaryEntryService';
import { SafeAreaView } from 'react-native-safe-area-context';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import ImageViewer from '../../components/ImageViewer';

export default function Diary() {
    const [diaryEntries, setDiaryEntries] = useState([]);
    const [myHabits, setMyHabits] = useState([]);
    const [habitEntries, setHabitEntries] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth());
    const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());
    const [modalVisible, setModalVisible] = useState(false);
    const [selectedEntry, setSelectedEntry] = useState(null);
    const [moods, setMoods] = useState([]);

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

            const moodsResponse = await diaryEntryService.getAllMoods();
            setMoods(moodsResponse.data || []);
            
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
                    habitCategory: userHabit.habit?.category?.name || 'No category',
                    habitStreak: entry.streak,
                });
            }
        });
        
        return habitsByDay;
    };

    const getDiaryEntriesByDay = () =>  {
        const filteredEntries = filterEntriesByMonth(diaryEntries, 'date');
        const entriesByDay = {};

        filteredEntries.forEach(entry => {
            const day = new Date(entry.date).getDate();
            if (!entriesByDay[day]) {
                entriesByDay[day] = [];
            }
            entriesByDay[day].push(entry);
        });
        return entriesByDay;
    }


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

    const renderDayWithHabits = (day, habits = []) => {
        const handleDayPress = () => {
            const foundEntry = diaryEntries.find(entry => {
                const entryDate = new Date(entry.date);
                return entryDate.getFullYear() === selectedYear && 
                       entryDate.getMonth() === selectedMonth && 
                       entryDate.getDate() === day;
            });
            setSelectedEntry(foundEntry || null);
            setModalVisible(true);
        };

        return (
            <TouchableOpacity 
                key={day} 
                onPress={handleDayPress}
                className="bg-white rounded-lg p-3 mb-3 shadow-sm border border-gray-100"
                activeOpacity={0.7}
            >
                <Text className="text-lg font-semibold text-gray-800 mb-2">
                    {day} of {months[selectedMonth]}
                </Text>
                {habits && habits.length > 0 ? (
                    habits.map((habit, index) => (
                        <View key={index} className="flex-1 flex-row gap-4 rounded-lg p-2 mb-1 border-l-4 border-green-500 my-2">
                            <View>
                                <Text className="text-green-800 font-medium">{habit.habitName}</Text>
                                <Text className="text-green-600 text-xs">{habit.habitCategory}</Text>
                            </View>
                            <View>
                                <Text className="text-green-600">Streak: {habit.habitStreak}</Text>
                            </View>
                        </View>
                    ))
                ) : (
                    <Text className="text-gray-500 italic">No habits completed this day</Text>
                )}
            </TouchableOpacity>
        );
    };

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

    const diaryEntriesByDay = getDiaryEntriesByDay();
    const daysWithEntries = Object.keys(diaryEntriesByDay).sort((a, b) => parseInt(b) - parseInt(a));
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
                {daysWithEntries.length > 0 ? (
                    daysWithEntries.map(day => 
                        renderDayWithHabits(parseInt(day), habitsByDay[day] || [])
                    )
                ) : (
                    <View className="bg-white rounded-lg p-6 mt-4">
                        <Text className="text-center text-gray-500 text-lg">
                            No diary entries in {months[selectedMonth]} {selectedYear}
                        </Text>
                        <Text className="text-center text-gray-400 mt-2">
                            Start writing diary entries to see them here!
                        </Text>
                    </View>
                )}
            </ScrollView>

            <Modal
                animationType="slide"
                transparent={true}
                visible={modalVisible}
                onRequestClose={() => setModalVisible(false)}
                className="bg-opacity-50 border-inherit"
            >
                <View className="flex-1 justify-end border-inherit">
                    <View className="bg-white rounded-t-3xl max-h-4/5">
                        <View className="p-6">
                            <View className="flex-row items-center justify-between mb-4">
                                <Text className="text-2xl font-bold text-gray-800">Diary Entry</Text>
                                <TouchableOpacity 
                                    onPress={() => setModalVisible(false)}
                                    className="bg-gray-100 rounded-full p-2"
                                >
                                    <MaterialCommunityIcons name="close" size={24} color="#6B7280" />
                                </TouchableOpacity>
                            </View>

                            {selectedEntry ? (
                                <ScrollView showsVerticalScrollIndicator={false} className="max-h-96">
                                    <View className="bg-blue-50 rounded-xl p-4 mb-4">
                                        <View className="flex-row items-center">
                                            <MaterialCommunityIcons name="calendar" size={20} color="#3B82F6" />
                                            <Text className="text-blue-800 font-medium ml-2">
                                                {new Date(selectedEntry.date).toLocaleDateString('en-US', {
                                                    weekday: 'long',
                                                    year: 'numeric',
                                                    month: 'long',
                                                    day: 'numeric'
                                                })}
                                            </Text>
                                        </View>
                                    </View>

                                    <View className="bg-purple-50 rounded-xl p-4 mb-4">
                                        <View className="flex-row items-center">
                                            <MaterialCommunityIcons name="emoticon" size={20} color="#8B5CF6" />
                                            <Text className="text-purple-800 font-medium ml-2">
                                                Mood: {moods.find(mood => mood.id === selectedEntry.moodId)?.name || 'Unknown'}
                                            </Text>
                                        </View>
                                    </View>

                                    <View className="bg-gray-50 rounded-xl p-4 mb-4">
                                        <Text className="text-gray-800 font-semibold mb-2">Content:</Text>
                                        <Text className="text-gray-700 leading-6">
                                            {selectedEntry.description || 'No content available'}
                                        </Text>
                                    </View>

                                    {selectedEntry.images && selectedEntry.images.length > 0 && (
                                        <View className="bg-green-50 rounded-xl p-4 mb-4">
                                            <View className="flex-row items-center mb-3">
                                                <MaterialCommunityIcons name="image-multiple" size={20} color="#10B981" />
                                                <Text className="text-green-800 font-semibold ml-2">
                                                    Images ({selectedEntry.images.length})
                                                </Text>
                                            </View>
                                            <ScrollView horizontal showsHorizontalScrollIndicator={false}>
                                                <View className="flex-row">
                                                    {selectedEntry.images.map((image, index) => (
                                                        <View key={index} className="mr-3">
                                                            <ImageViewer image={image.imageData} />
                                                        </View>
                                                    ))}
                                                </View>
                                            </ScrollView>
                                        </View>
                                    )}

                                    <View className="bg-yellow-50 rounded-xl p-4">
                                        <Text className="text-yellow-800 font-semibold mb-2">Entry Details:</Text>
                                        <Text className="text-yellow-700 text-sm">
                                            Created: {new Date(selectedEntry.date).toLocaleString()}
                                        </Text>

                                    </View>
                                </ScrollView>
                            ) : (
                                <View className="items-center py-8">
                                    <MaterialCommunityIcons name="book-open-variant" size={64} color="#D1D5DB" />
                                    <Text className="text-gray-500 text-lg mt-4">No diary entry for this day</Text>
                                    <Text className="text-gray-400 text-center mt-2">
                                        Only habits were completed on this day
                                    </Text>
                                </View>
                            )}
                        </View>
                    </View>
                </View>
            </Modal>
        </View>
    </SafeAreaView>
);
}