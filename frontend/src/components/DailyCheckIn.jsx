import { View, Text, TouchableOpacity } from 'react-native';
import { Image } from 'expo-image';
import { useRouter } from 'expo-router';

export const DailyCheckIn = ({ moods }) => {

  const router = useRouter();

    return (
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
    );
  }
