import { useState, useEffect, useContext } from "react";
import { View, Text, ActivityIndicator, ScrollView } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { AuthContext } from "../../context/AuthContext";
import trophyService from "../../services/trophyService";

export default function Trophies() {
    const [trophies, setTrophies] = useState([]);
    const [loading, setLoading] = useState(true);
    const { userId } = useContext(AuthContext);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchTrophies = async () => {
            try {
                const trophyResponse = await trophyService.getTrophiesByUser(userId);
                setTrophies(trophyResponse.data || []);
                setLoading(false);
            } catch (err) {
                console.error('Error fetching trophies:', err);
                setError(err.message);
                setLoading(false);
            }
        };

        fetchTrophies();
    }, []);

    const groupTrophiesByStreak = () => {
        const trophiesByStreak = {};
        
        trophies.forEach(userTrophy => {
            const streakDays = userTrophy.trophy?.days;
            
            if (streakDays && streakDays > 0) {
                if (!trophiesByStreak[streakDays]) {
                    trophiesByStreak[streakDays] = [];
                }
                
                trophiesByStreak[streakDays].push(userTrophy);
            }
        });
        
        return trophiesByStreak;
    };

    if (loading) {
        return (
            <SafeAreaView className="flex-1 bg-gray-50">
                <View className="flex-1 justify-center items-center">
                    <ActivityIndicator size="large" color="#3b82f6" />
                    <Text className="mt-2 text-gray-600">Loading trophies...</Text>
                </View>
            </SafeAreaView>
        );
    }

    if (error) {
        return (
            <SafeAreaView className="flex-1 bg-gray-50">
                <View className="flex-1 justify-center items-center p-4">
                    <MaterialCommunityIcons name="trophy-broken" size={64} color="#EF4444" />
                    <Text className="text-red-500 text-center mt-4 text-lg">{error}</Text>
                    <Text className="text-gray-500 text-center mt-2">Unable to load your trophies</Text>
                </View>
            </SafeAreaView>
        );
    }

    const trophiesByStreak = groupTrophiesByStreak();
    const sortedStreaks = Object.keys(trophiesByStreak).sort((a, b) => parseInt(a) - parseInt(b));

    return (
        <SafeAreaView className="flex-1 bg-gray-50">
            <View className="p-4">
                <Text className="text-2xl font-bold text-gray-800 mb-6">My Trophies</Text>
            </View>
            
            <ScrollView className="flex-1 px-4" showsVerticalScrollIndicator={false}>
                {sortedStreaks.length > 0 ? (
                    sortedStreaks.map(streakDays => (
                        <View key={streakDays} className="mb-6">
                            <View className="bg-blue-50 rounded-lg p-3 mb-3">
                                <Text className="text-blue-800 font-semibold text-lg">
                                    {streakDays} Day Streak
                                </Text>
                                <Text className="text-blue-600 text-sm">
                                    {trophiesByStreak[streakDays].length} trophy(ies) earned
                                </Text>
                            </View>
                            
                            {trophiesByStreak[streakDays].map((userTrophy, index) => (
                                <View key={index} className="bg-white rounded-xl p-4 mb-3 shadow-sm border border-gray-100">
                                    <View className="flex-row items-center">
                                        <View className="bg-yellow-100 rounded-full p-3 mr-4">
                                            <MaterialCommunityIcons name="trophy" size={24} color="#F59E0B" />
                                        </View>
                                        <View className="flex-1">
                                            <Text className="text-lg font-bold text-gray-800">
                                                {userTrophy.trophy?.name || userTrophy.name || 'Trophy'}
                                            </Text>
                                            <Text className="text-gray-600 mt-1">
                                                {userTrophy.trophy?.description || userTrophy.description || 'No description available'}
                                            </Text>
                                            <Text className="text-green-600 text-sm font-medium mt-2">
                                                {userTrophy.trophy.days} day streak completed!
                                            </Text>
                                            <Text className="text-gray-500 text-xs mt-1">
                                                Earned on: {new Date(userTrophy.obtainedAt).toLocaleDateString()}
                                            </Text>
                                        </View>
                                    </View>
                                </View>
                            ))}
                        </View>
                    ))
                ) : (
                    <View className="bg-white rounded-xl p-8 items-center">
                        <MaterialCommunityIcons name="trophy-outline" size={64} color="#D1D5DB" />
                        <Text className="text-gray-500 text-lg font-medium mt-4">No trophies yet</Text>
                        <Text className="text-gray-400 text-center mt-2">
                            Complete your habits consistently to earn your first trophy!
                        </Text>
                    </View>
                )}
            </ScrollView>
        </SafeAreaView>
    );
}