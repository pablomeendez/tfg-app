import { View, Text, TouchableOpacity } from 'react-native';
import { Image } from 'expo-image';
import { useRouter } from 'expo-router';
import { useTranslation } from 'react-i18next';
import i18n from './../../i18n/i18n';
import MoodChooser from './MoodChooser';

export const DailyCheckIn = ({ moods }) => {
  const { t } = useTranslation();

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

              <MoodChooser moods={moods} />
            </View>
          </View>
    );
  }
