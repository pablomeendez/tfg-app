import { View, Text, TouchableOpacity } from 'react-native';
import { Image } from 'expo-image';
import { useRouter } from 'expo-router';
import { useTranslation } from 'react-i18next';
import i18n from './../../i18n/i18n';
import MoodItem from './MoodItem';

export const DailyCheckIn = ({ moods }) => {
  const { t } = useTranslation();
  const language = i18n.language;

  const router = useRouter();

    return (
        <View className="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-xl m-4 shadow-lg border border-blue-100">
              <View className="p-6">
              <View className="flex flex-row items-center mb-4">
                <View className="w-3 h-3 bg-blue-500 rounded-full mr-3"></View>
                <Text className="text-2xl font-bold text-blue-900">
                  ✨ {t('daily_check_in')}
                </Text>
              </View>
              
              <Text className="text-xl text-center text-gray-800 font-medium mb-6">
                {t('start_your_day_with_a_mood_check_in')}
              </Text>
              
              <View className="bg-white rounded-xl p-5 mb-4 shadow-sm border border-gray-100">
                <Text className="text-center text-gray-600 text-sm mb-4 font-medium">
                  {t('choose_your_mood')}
                </Text>
                <View className="flex flex-row flex-wrap justify-center items-center">
                  {moods.length > 0 ? (
                    moods.map((mood) => (
                      <View className="m-2" key={mood.id}>
                        <MoodItem
                          mood={mood}
                          style="dailyCheckIn"
                          onPress={() => {
                            router.push({
                              pathname: "screens/DiaryEntryForm",
                              params: { 
                                moodId: mood.id, 
                                moodName: JSON.stringify(mood.name), 
                                moodImage: mood.image 
                              }
                            });
                          }}
                        />
                      </View>
                    ))
                  ) : (
                    <View className="bg-gray-50 rounded-lg p-4 border border-gray-200">
                      <Text className="text-gray-600 text-center">{t('no_moods_available')}</Text>
                    </View>
                  )}
                </View>
              </View>
            </View>
          </View>
    );
  }
