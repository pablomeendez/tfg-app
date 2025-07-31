import { Text, View, ScrollView, Button, ActivityIndicator } from 'react-native';
import { useRouter } from 'expo-router';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useEffect, useState } from 'react';
import diaryEntryService from '../../services/diaryEntryService';

export default function Index() {

  const router = useRouter();
  const [moods, setMoods] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMoods = async () => {
      try {
        setLoading(true);
        const response = await diaryEntryService.getAllMoods();
        
        if (response && response.data) {
          setMoods(response.data);
        } else {
          setMoods([]);
        }
      } catch (error) {
        console.error("Error fetching moods:", error);
        setMoods([]); 
      } finally {
        setLoading(false);
      }
    };

    fetchMoods();
  }, []);

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
            <View className="bg-white rounded-xl m-1">
              {moods.length > 0 ? (
                moods.map((mood) => (
                  <Button
                    key={mood.id}
                    title={mood.name || `Mood ${mood.id}`}
                    onPress={() => {
                      console.log('Navigating with moodId:', mood.id);
                      router.push({ 
                        pathname: "/screens/DiaryEntryForm", 
                        params: { moodId: mood.id, moodName: mood.name } 
                      });
                    }}
                  />
                ))
              ) : (
                <Text className="p-2 text-gray-600">No moods available</Text>
              )}
            </View>
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
} 