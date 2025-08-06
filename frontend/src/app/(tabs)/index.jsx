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

export default function Index() {

  const router = useRouter();
  const { userId } = useContext(AuthContext);
  const [moods, setMoods] = useState([]);
  const [weeklySummary, setWeeklySummary] = useState(null);
  const [latestEntry, setLatestEntry] = useState(null);
  const [loading, setLoading] = useState(true);

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

      if (userId) {
        console.log("Fetching weekly summary for user:", userId);
        try {
          const summaryResponse = await weeklySummaryService.getWeeklySummaryByUserId(userId);
          if (summaryResponse && summaryResponse.data && summaryResponse.data.length > 0) {
            setWeeklySummary(summaryResponse.data[summaryResponse.data.length - 1]);
          } else {
            setWeeklySummary(null);
          }
        } catch (summaryError) {
          console.log("Error fetching weekly summary:", summaryError);  
          setWeeklySummary(null);
        }
      } else {
        console.log("No user logged in");
      }

    } catch (error) {
      console.error("Error fetching data:", error);
      setMoods([]); 
    } finally {
      setLoading(false);
    }
  }, [userId]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  if (loading) {
    return (
      <SafeAreaView className="flex-1 items-center justify-center">
        <ActivityIndicator size="large" color="#0000ff" />
      </SafeAreaView>
    )
  }

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
    const isInTimeRange = currentHour >= 15 && currentHour < 16;
    const hasNoEntryToday = !isLatestEntryFromToday();
    return isInTimeRange && hasNoEntryToday;
  };

  return (
    <SafeAreaView className="flex-1 bg-white">
      <ScrollView>
        {shouldShowMoodForm() ? (
          <View className="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-xl m-4 shadow-lg border border-blue-100">
            <View className="p-6">
              <View className="flex flex-row items-center mb-4">
                <View className="w-3 h-3 bg-blue-500 rounded-full mr-3"></View>
                <Text className="text-2xl font-bold text-blue-900">
                  ✨ Daily Check-in
                </Text>
              </View>
              
              <Text className="text-xl text-center text-gray-800 font-medium mb-6">
                How have you been feeling today?
              </Text>
              
              <View className="bg-white rounded-xl p-5 mb-4 shadow-sm border border-gray-100">
                <Text className="text-center text-gray-600 text-sm mb-4 font-medium">
                  Choose your mood to continue
                </Text>
                <View className="flex flex-row flex-wrap justify-center items-center">
                  {moods.length > 0 ? (
                    moods.map((mood) => (
                      <View className="m-2" key={mood.id}>
                        <TouchableOpacity
                          className="rounded-full p-3 shadow-sm active:scale-95 border border-yellow-400"
                          onPress={() => {
                            router.push({
                              pathname: "/screens/DiaryEntryForm",
                              params: { moodId: mood.id, moodName: mood.name, moodImage: mood.image }
                            });
                          }}
                        >
                          <View className="flex items-center justify-center">
                            {mood.image ? (
                              <View className="bg-yellow-300 rounded-full">
                                <Image 
                                  source={{ uri: mood.image }} 
                                  style={{ width: 45, height: 45 }} 
                                />
                              </View>
                            ) : (
                              <Text className="text-gray-700 font-semibold text-sm">
                                {mood.name || `Mood ${mood.id}`}
                              </Text>
                            )}
                          </View>
                        </TouchableOpacity>
                        {mood.name && (
                          <Text className="text-center text-xs text-gray-600 mt-2 font-medium">
                            {mood.name}
                          </Text>
                        )}
                      </View>
                    ))
                  ) : (
                    <View className="bg-gray-50 rounded-lg p-4 border border-gray-200">
                      <Text className="text-gray-600 text-center">No moods available</Text>
                    </View>
                  )}
                </View>
              </View>
            </View>
          </View>
        ) : (
          latestEntry ? (
            <View className="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-xl m-4 shadow-lg border border-blue-100">
              <View className="p-6">
                <View className="flex flex-row items-center mb-4">
                  <View className="w-3 h-3 bg-blue-500 rounded-full mr-3"></View>
                  <Text className="text-2xl font-bold text-blue-900">
                    Your Latest Entry
                  </Text>
                </View>
                
                <View className="bg-white rounded-xl p-5 mb-4 shadow-sm border border-gray-100">
                  {/* Description */}
                  <Text className="text-gray-900 text-lg leading-6 font-medium mb-4">
                    "{latestEntry.description}"
                  </Text>
                  
                  {/* Mood section */}
                  {latestEntry.moodId && (
                    <View className="mb-4 bg-blue-50 rounded-xl p-4 border border-blue-100">
                      <View className="flex flex-row items-center">
                        <View className="w-3 h-3 bg-blue-500 rounded-full mr-3"></View>
                        <Text className="text-blue-900 text-sm font-semibold mr-4">Mood:</Text>
                        {currentMood ? (
                          <View className="flex flex-row items-center">
                            {currentMood.image && (
                              <View className="bg-yellow-300 rounded-full mr-2">
                                <Image 
                                  source={{ uri: currentMood.image }} 
                                  style={{ width: 24, height: 24 }} 
                                />
                              </View>
                            )}
                            <Text className="text-gray-800 text-sm font-semibold">
                              {currentMood.name}
                            </Text>
                          </View>
                        ) : (
                          <View className="bg-gray-200 rounded-full px-4 py-2">
                            <Text className="text-gray-600 text-sm font-semibold">
                              Mood {latestEntry.moodId}
                            </Text>
                          </View>
                        )}
                      </View>
                    </View>
                  )}
                  
                  {/* Images section */}
                  {latestEntry.images && latestEntry.images.length > 0 && (
                    <View className="mb-4">
                      <Text className="text-gray-700 text-sm font-semibold mb-2">📸 Images</Text>
                      <View className="flex flex-row flex-wrap">
                        {latestEntry.images.map((image) => (
                          <View key={image.id} className="mr-2 flex-1" style={{ minWidth: '45%', maxWidth: '48%' }}>
                            <ImageViewer 
                              image={image.imageData}
                            />
                          </View>
                        ))}
                      </View>
                    </View>
                  )}
                  
                  {/* Habits section */}
                  {latestEntry.habits && latestEntry.habits.length > 0 && (
                    <View className="mb-4">
                      <Text className="text-gray-700 text-sm font-semibold mb-2">✅ Completed Habits</Text>
                      <View className="space-y-2">
                        {latestEntry.habits.map((habit, index) => (
                          <View key={index} className="flex flex-row items-center bg-green-50 rounded-lg p-3 border border-green-100">
                            <View className="w-3 h-3 bg-green-500 rounded-full mr-3"></View>
                            <Text className="text-green-800 text-sm font-medium flex-1">
                              {habit.name || habit.description || `Habit ${habit.id}`}
                            </Text>
                            {habit.streak && (
                              <View className="bg-green-200 rounded-full px-2 py-1">
                                <Text className="text-green-800 text-xs font-semibold">
                                  {habit.streak} day streak
                                </Text>
                              </View>
                            )}
                          </View>
                        ))}
                      </View>
                    </View>
                  )}
                  
                  <View className="border-t border-gray-100 pt-4">
                    <View className="flex flex-row items-center justify-between mb-3">
                      <View className="flex flex-row items-center">
                        <View className="w-2 h-2 bg-gray-400 rounded-full mr-2"></View>
                        <Text className="text-gray-600 text-sm font-medium">
                          {new Date(latestEntry.date).toLocaleDateString('en-US', {
                            weekday: 'long',
                            month: 'long',
                            day: 'numeric',
                            year: 'numeric'
                          })}
                        </Text>
                      </View>
                      
                      <Text className="text-gray-500 text-sm">
                        {new Date(latestEntry.date).toLocaleTimeString('en-US', {
                          hour: '2-digit',
                          minute: '2-digit'
                        })}
                      </Text>
                    </View>
                    
          
                  </View>
                </View>
                
                <TouchableOpacity 
                  className="bg-blue-600 rounded-xl py-4 px-6 shadow-md active:bg-blue-700"
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
    </SafeAreaView>
  );
} 