import { Text, View, ScrollView, TouchableOpacity, ActivityIndicator } from 'react-native';
import {  useRouter, useFocusEffect } from 'expo-router';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useEffect, useState, useContext, useCallback } from 'react';
import diaryEntryService from '../../services/diaryEntryService';
import weeklySummaryService from '../../services/weeklySummaryService';
import { Image } from 'expo-image';
import WeeklySummaryCard from '../../components/WeeklySummaryCard';
import ImageViewer from '../../components/ImageViewer';
import { AuthContext } from '../../context/AuthContext';
import { CompleteEntry } from '../../components/CompleteEntry';

export default function Index() {

  const router = useRouter();
  const { userId } = useContext(AuthContext);
  const [moods, setMoods] = useState([]);
  const [weeklySummary, setWeeklySummary] = useState(null);
  const [latestEntry, setLatestEntry] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
      
      const moodsResponse = await diaryEntryService.getAllMoods();
      if (moodsResponse && moodsResponse.data) {
        setMoods(moodsResponse.data);
      } else {
        setMoods([]);
      }

        const latestEntryResponse = await diaryEntryService.getLatestDiaryEntry();
        if (latestEntryResponse && latestEntryResponse.data) {
          setLatestEntry(latestEntryResponse.data);
        } else {
          setLatestEntry(null);
        }

        const summaryResponse = await weeklySummaryService.getWeeklySummaryByUser(0, 1);
        console.log(summaryResponse.data);
        if (summaryResponse && summaryResponse.data && summaryResponse.data.content && summaryResponse.data.content.length > 0) {
          console.log((summaryResponse.data.content[0]));
          setWeeklySummary(summaryResponse.data.content[0]);
        } else {
          setWeeklySummary(null);
        }

    } catch (error) {
      setError(error.message);
      setMoods([]);
    } finally {
      setLoading(false);
    }
  }, [userId]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const getCurrentMood = () => {
    if (!latestEntry?.moodId || moods.length === 0) return null;
    return moods.find(m => m.id == latestEntry.moodId);
  };

  const currentMood = getCurrentMood();

  const isLatestEntryFromToday = () => {
    if (!latestEntry) return false;
    const today = new Date();
    const entryDate = new Date(latestEntry.date);
    return today.toDateString() === entryDate.toDateString();
  };

  const shouldShowMoodForm = () => {
    const currentHour = new Date().getHours();
    const isInTimeRange = currentHour >= 3 && currentHour < 4;
    const hasNoEntryToday = !isLatestEntryFromToday();
    return isInTimeRange && hasNoEntryToday;
  };

  return (
    <SafeAreaView className="flex-1 bg-white">
      {error ? (
         <View className="flex-1 justify-center items-center bg-gray-50 p-4">
            <Text className="text-red-600 text-center mb-4">Error: {error}</Text>
          </View>
      ) : loading ? (
        <View className="flex-1 justify-center items-center bg-gray-50">
          <ActivityIndicator size="large" color="#3b82f6" />
          <Text className="mt-2 text-gray-600">Loading...</Text>
        </View>
      ) :
        <ScrollView>
          {shouldShowMoodForm() ? (
            <DaylyCheckIn moods={moods} />
        ) : (
          latestEntry ? (
              <View>
                  <CompleteEntry latestEntry={latestEntry} />
                <View className="flex-row justify-center">
                  <TouchableOpacity 
                    className=" justrify-center items-center bg-blue-600 rounded-xl py-4 px-6 shadow-md active:bg-blue-700 w-4/5"
                    onPress={() => {
                      router.push('/diary');
                    }}
                  >
                    <Text className="text-white text-center font-semibold text-base">
                      📖 View All Entries
                    </Text>
                  </TouchableOpacity>
                </View>
              </View>
          ) : (
            <View className="bg-gray-100 rounded-lg m-3 p-4">
              <Text className="text-gray-600 text-center">No diary entries yet</Text>
              <Text className="text-gray-500 text-center text-sm mt-1">
                Start by creating your first entry
              </Text>
            </View>
          )
        )}

        {weeklySummary ? (
          <View>
            <WeeklySummaryCard summary={weeklySummary} />
            <TouchableOpacity 
              className="bg-blue-500 rounded-lg m-3 p-3"
              onPress={() => {
                router.push('/screens/AllWeeklySummaries');
              }}
            >
              <Text className="text-white text-center font-semibold">
                View all weekly summaries
              </Text>
            </TouchableOpacity>
          </View>
        ) : (
          <View className="bg-gray-100 rounded-lg m-3 p-4">
            <Text className="text-gray-600 text-center">No weekly summary available</Text>
            <Text className="text-gray-500 text-center text-sm mt-1">
              Summaries are generated automatically on Sundays
            </Text>
          </View>
        )}
      </ScrollView>
      }
    </SafeAreaView>
  );
} 