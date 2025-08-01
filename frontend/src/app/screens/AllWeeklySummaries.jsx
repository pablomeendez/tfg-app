import { Text, View, ScrollView, ActivityIndicator, TouchableOpacity } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useEffect, useState, useContext } from 'react';
import { useRouter } from 'expo-router';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import weeklySummaryService from '../../services/weeklySummaryService';
import WeeklySummaryCard from '../../components/WeeklySummaryCard';
import { AuthContext } from '../../context/AuthContext';

export default function AllWeeklySummaries() {
  const router = useRouter();
  const { userId } = useContext(AuthContext);
  const [summaries, setSummaries] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSummaries = async () => {
      if (!userId) {
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        const response = await weeklySummaryService.getWeeklySummaryByUserId(userId);
        if (response && response.data) {
          // Ordenar por fecha, más reciente primero
          const sortedSummaries = response.data.sort((a, b) => 
            new Date(b.date) - new Date(a.date)
          );
          setSummaries(sortedSummaries);
        } else {
          setSummaries([]);
        }
      } catch (error) {
        console.error("Error fetching summaries:", error);
        setSummaries([]);
      } finally {
        setLoading(false);
      }
    };

    fetchSummaries();
  }, [userId]);

  if (loading) {
    return (
      <SafeAreaView className="flex-1 items-center justify-center bg-white">
        <ActivityIndicator size="large" color="#3B82F6" />
        <Text className="mt-2 text-gray-600">Loading summaries...</Text>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView className="flex-1 bg-white">
      {/* Header */}
      <View className="flex-row items-center justify-between p-4 border-b border-gray-200">
        <TouchableOpacity 
          onPress={() => router.back()}
          className="flex-row items-center"
        >
          <MaterialCommunityIcons name="arrow-left" size={24} color="#374151" />
          <Text className="ml-2 text-gray-700">Back</Text>
        </TouchableOpacity>
        <Text className="text-xl font-bold text-gray-800">Weekly Summaries</Text>
        <View style={{ width: 70 }} />
      </View>

      <ScrollView className="flex-1">
        {summaries.length > 0 ? (
          <>
            <Text className="text-gray-600 text-center p-4">
              You have {summaries.length} weekly summaries
            </Text>
            {summaries.map((summary, index) => (
              <WeeklySummaryCard key={summary.id || index} summary={summary} />
            ))}
          </>
        ) : (
          <View className="flex-1 items-center justify-center p-8">
            <MaterialCommunityIcons name="chart-line" size={64} color="#9CA3AF" />
            <Text className="text-xl text-gray-600 text-center mt-4">
              No summaries available
            </Text>
            <Text className="text-gray-500 text-center mt-2">
              Summaries are generated automatically every Sunday
            </Text>
            <Text className="text-gray-500 text-center mt-1">
              Keep using the app to see your progress!
            </Text>
          </View>
        )}
      </ScrollView>
    </SafeAreaView>
  );
}
