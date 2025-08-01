import { Text, View, ScrollView, TouchableOpacity, ActivityIndicator } from 'react-native';
import { useRouter } from 'expo-router';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useEffect, useState } from 'react';
import diaryEntryService from '../../services/diaryEntryService';
import { Image } from 'expo-image';

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
      </ScrollView>
    </SafeAreaView>
  );
} 