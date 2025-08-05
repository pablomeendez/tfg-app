import { Text, View, ScrollView, TouchableOpacity, ActivityIndicator } from 'react-native';
import {  useRouter } from 'expo-router';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useEffect, useState, useContext } from 'react';
import diaryEntryService from '../../services/diaryEntryService';
import weeklySummaryService from '../../services/weeklySummaryService';
import { Image } from 'expo-image';
import WeeklySummaryCard from '../../components/WeeklySummaryCard';
import { AuthContext } from '../../context/AuthContext';

export default function Index() {

  const router = useRouter();
  const { userId } = useContext(AuthContext);
  const [moods, setMoods] = useState([]);
  const [weeklySummary, setWeeklySummary] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        
        const moodsResponse = await diaryEntryService.getAllMoods();
        if (moodsResponse && moodsResponse.data) {
          setMoods(moodsResponse.data);
        } else {
          setMoods([]);
        }

        if (userId) {
          console.log("Fetching weekly summary for user:", userId);
          try {
            const summaryResponse = await weeklySummaryService.getWeeklySummaryByUserId(userId);
            if (summaryResponse && summaryResponse.data && summaryResponse.data.length > 0) {
              // Get the most recent summary
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
    };

    fetchData();
  }, [userId]);

  if (loading) {
    return (
      <SafeAreaView className="flex-1 items-center justify-center">
        <ActivityIndicator size="large" color="#0000ff" />
      </SafeAreaView>
    )
  }

  return (
    <SafeAreaView className="flex-1 bg-white">
      <ScrollView>
        <View className="flex flex-col bg-gray-300 h-28 rounded-md m-3 shadow items-center">
          <Text className="text-xl pt-2">
            ¿How have you been?
          </Text>
          <View className="flex flex-row rounded-xl mt-2">
              {moods.length > 0 ? (
                moods.map((mood) => (
                   <View className="bg-yellow-300 rounded-full m-1" key={mood.id}>
                    <TouchableOpacity
                      onPress={() => {
                        router.push({
                          pathname: "/screens/DiaryEntryForm",
                        params: { moodId: mood.id, moodName: mood.name }
                      });
                    }}
                    >
                    {mood.image ? <Image source={{ uri: mood.image }} style={{ width: 40, height: 40  }} /> : mood.name || `Mood ${mood.id}`}
                    </TouchableOpacity>
                  </View>
                ))
              ) : (
                <Text className="p-2 text-gray-600">No moods available</Text>
              )}
            
          </View>
        </View>

        {/* Weekly summary */}
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