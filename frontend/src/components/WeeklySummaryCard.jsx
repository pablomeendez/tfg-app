import React from 'react';
import { View, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTranslation } from 'react-i18next';

const WeeklySummaryCard = ({ summary }) => {
    const { t } = useTranslation();
  if (!summary) return null;

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString({ 
      day: 'numeric', 
      month: 'long', 
      year: 'numeric' 
    });
  };

  return (
    <View className="bg-white rounded-lg shadow-md m-3 p-4 border border-gray-200">
      <Text className="text-lg font-bold text-gray-800 mb-3">
        📊 {t('weekly_summary')}
      </Text>
      <Text className="text-sm text-gray-600 mb-3">
        {formatDate(summary.date)}
      </Text>
      
      <View className="space-y-2">
        <View className="flex-row items-center justify-between py-1">
          <View className="flex-row items-center">
            <MaterialCommunityIcons name="book-open-variant" size={20} color="#3B82F6" />
            <Text className="ml-2 text-gray-700">{t('diary_entries')}</Text>
          </View>
          <Text className="font-semibold text-blue-600">{summary.totalEntries}</Text>
        </View>

        <View className="flex-row items-center justify-between py-1">
          <View className="flex-row items-center">
            <MaterialCommunityIcons name="check-circle" size={20} color="#10B981" />
            <Text className="ml-2 text-gray-700">{t('completed_habits')}</Text>
          </View>
          <Text className="font-semibold text-green-600">{summary.habitsCompleted}</Text>
        </View>

        <View className="flex-row items-center justify-between py-1">
          <View className="flex-row items-center">
            <MaterialCommunityIcons name="trophy" size={20} color="#F59E0B" />
            <Text className="ml-2 text-gray-700">{t('trophies_earned')}</Text>
          </View>
          <Text className="font-semibold text-yellow-600">{summary.trophiesEarned}</Text>
        </View>

        {summary.biggestStreak && (
          <View className="flex-row items-center justify-between py-1">
            <View className="flex-row items-center">
              <MaterialCommunityIcons name="fire" size={20} color="#EF4444" />
              <Text className="ml-2 text-gray-700">{t('longest_streak')}</Text>
            </View>
            <Text className="font-semibold text-red-600">{summary.biggestStreak.streak} {t('days')}</Text>
          </View>
        )}

        {summary.moodTrend && (
          <View className="flex-row items-center justify-between py-1">
            <View className="flex-row items-center">
              <MaterialCommunityIcons name="emoticon" size={20} color="#8B5CF6" />
              <Text className="ml-2 text-gray-700">{t('predomint_mood')}</Text>
            </View>
            <Text className="font-semibold text-purple-600">{summary.moodTrend.name}</Text>
          </View>
        )}
      </View>
    </View>
  );
};

export default WeeklySummaryCard;
