import { View, TouchableOpacity, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import HabitCard from './HabitCard';

// Componente que encapsula tu lógica de selección de hábitos
const HabitSelector = ({ myHabits, selectedHabits, setSelectedHabits, t }) => {
    const toggleHabitSelection = (userHabit) => {
        setSelectedHabits(prev => {
            if (prev.some(h => h.id === userHabit.id)) {
                return prev.filter(h => h.id !== userHabit.id);
            } else {
                return [...prev, userHabit];
            }
        });
    };

    if (!myHabits || myHabits.length === 0) {
        return (
            <View className="bg-gray-50 rounded-lg p-6 items-center">
                <MaterialCommunityIcons name="calendar-outline" size={48} color="#9CA3AF" />
                <Text className="text-gray-500 text-center mt-3 text-base">
                    {t("no_habits_today")}
                </Text>
                <Text className="text-gray-400 text-center mt-1 text-sm">
                    {t("add_some_habits")}
                </Text>
            </View>
        );
    }

    return (
        <View className="space-y-3">
            {myHabits.map((userHabit) => {
                const isSelected = selectedHabits.some(h => h.id === userHabit.id);
                return (
                    <TouchableOpacity
                        key={userHabit.id} 
                        onPress={() => toggleHabitSelection(userHabit)}
                        className={`rounded-xl border-2 m-2 ${
                            isSelected
                                ? 'border-blue-500 bg-blue-50'
                                : 'border-gray-200 bg-gray-50'
                        }`}
                        activeOpacity={0.7}
                    >
                        <View className="flex-row items-center justify-between">
                            <View className="flex-1 mr-3">
                                <HabitCard habit={userHabit.habit} />
                            </View>
                            <View className="p-1">
                                <MaterialCommunityIcons 
                                    name={isSelected ? "check-circle" : "circle-outline"} 
                                    size={28} 
                                    color={isSelected ? "#3B82F6" : "#9CA3AF"} 
                                />
                            </View>
                        </View>
                    </TouchableOpacity>
                );
            })}
        </View>
    );
};

export default HabitSelector;
