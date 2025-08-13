import { useState, useEffect, useContext, useTransition } from "react";
import { View, Text, ActivityIndicator, ScrollView } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { MaterialCommunityIcons } from '@expo/vector-icons';
import trophyService from "../../services/trophyService";
import { useTranslation } from "react-i18next";
import { LoadingComponent } from "../../components/LoadingComponent";

export default function Trophies() {
    const [trophies, setTrophies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { t } = useTranslation();

    useEffect(() => {
        const fetchTrophies = async () => {
            try {
                const trophyResponse = await trophyService.getTrophiesByUser();
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

    const trophiesByStreak = groupTrophiesByStreak();
    const sortedStreaks = Object.keys(trophiesByStreak).sort((a, b) => parseInt(a) - parseInt(b));

    return (
        <SafeAreaView className="flex-1 bg-gray-50">
            {error ? (
                <View className="">
                    <ErrorComponent error={error} />
                    <Text className="text-gray-500 text-center mt-2">{t('unable_to_load_trophies')}</Text>
                </View>) : loading ? (
                    <LoadingComponent />
                ) :
            <View className="flex-1">
                <View className="p-4">
                    <Text className="text-2xl font-bold text-gray-800 mb-6">{t('my_trophies')}</Text>
                </View>
                
                <ScrollView className="flex-1 px-4" showsVerticalScrollIndicator={false}>
                    {console.log(sortedStreaks.length)}
                    {sortedStreaks.length > 0 ? (
                        sortedStreaks.map(streakDays => (
                            <View key={streakDays} className="mb-6">
                                <View className="bg-blue-50 rounded-lg p-3 mb-3">
                                    <Text className="text-blue-800 font-semibold text-lg">
                                        {t('days_streak', { count: streakDays })}
                                    </Text>
                                    <Text className="text-blue-600 text-sm">
                                        {t('trophy', { count: trophiesByStreak[streakDays].length })}
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
                                                    {t('days_streak', { count: userTrophy.trophy.days })} {t('completed')}
                                                </Text>
                                                <Text className="text-gray-500 text-xs mt-1">
                                                    {t('earned_on', { date: new Date(userTrophy.obtainedAt).toLocaleDateString() })}
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
                            <Text className="text-gray-500 text-lg font-medium mt-4">{t('no_trophies_yet')}</Text>
                            <Text className="text-gray-400 text-center mt-2">
                                
                                {t('complete_habits_to_earn_trophy')}
                            </Text>
                        </View>
                    )}
                </ScrollView>
            </View>}
        </SafeAreaView>
    );
}