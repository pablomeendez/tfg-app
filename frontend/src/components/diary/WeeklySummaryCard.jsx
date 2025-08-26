import React from 'react';
import { View, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTranslation } from 'react-i18next';
import MultiLanguageText from './../common/MultiLanguageText';
import StatRow from './StatRow';
import i18n from './../../i18n/i18n';

const WeeklySummaryCard = ({ summary }) => {
    const { t } = useTranslation();

    const language = i18n.language;

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

        <StatRow
          icon="trophy"
          iconColor="#F59E0B"
          label={t('trophies_earned')}
          value={summary.trophiesEarned}
          valueColor="#D97706"
        />

        {summary.biggestStreak && (
          <StatRow
            icon="fire"
            iconColor="#EF4444"
            label={t('longest_streak')}
            value={`${summary.biggestStreak} ${t('days')}`}
            valueColor="#DC2626"
          />
        )}

        {summary.moodTrend && (
          <StatRow
            icon="emoticon"
            iconColor="#8B5CF6"
            label={t('predomint_mood')}
            value={
              <MultiLanguageText 
                textObject={summary.moodTrend.name}
                className="font-semibold text-purple-600"
              />
            }
          />
        )}
      </View>
    </View>
  );
};

export default WeeklySummaryCard;
