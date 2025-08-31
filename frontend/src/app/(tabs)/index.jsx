import { Text, View, ScrollView, TouchableOpacity, ActivityIndicator } from 'react-native';
import { useRouter, useFocusEffect } from 'expo-router';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useEffect, useState, useContext, useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import { AuthContext } from '../../context/AuthContext';
import diaryEntryService from '../../services/diaryEntryService';
import weeklySummaryService from '../../services/weeklySummaryService';
import {DailyCheckIn} from '../../components/diary/DailyCheckIn';
import {CompleteEntry} from '../../components/diary/CompleteEntry';
import WeeklySummaryCard from '../../components/weeklySummary/WeeklySummaryCard';
import { LoadingComponent } from '../../components/common/LoadingComponent';
import { ErrorComponent } from '../../components/common/ErrorComponent';
import EmptyStateCard from '../../components/common/EmptyStateCard';
import useStore from '../../store/store';
import LatestWeeklySummary from '../../components/weeklySummary/LatestWeeklySummary';


export default function Index() {

  const router = useRouter();
  const { userId } = useContext(AuthContext);
  const [moods, setMoods] = useState([]);
  const [weeklySummary, setWeeklySummary] = useState(null);
  const [latestEntry, setLatestEntry] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const { t } = useTranslation();

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
      if (summaryResponse && summaryResponse.data && summaryResponse.data.content && summaryResponse.data.content.length > 0) {
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

  const isLatestEntryFromToday = () => {
    if (!latestEntry) return false;
    const today = new Date();
    const entryDate = new Date(latestEntry.date);
    return today.toDateString() === entryDate.toDateString();
  };

  const shouldShowMoodForm = () => {
    const currentHour = new Date().getHours();
    const isInTimeRange = currentHour >= 22 && currentHour < 24;
    const hasNoEntryToday = !isLatestEntryFromToday();
    return (isInTimeRange && hasNoEntryToday) || !latestEntry;
  };

  return (
    <SafeAreaView className="flex-1 bg-white">
      {error ? (
         <ErrorComponent error={error}/>
      ) : loading ? (
        <LoadingComponent />
      ) :
        <ScrollView>
          {shouldShowMoodForm() ? (
            <DailyCheckIn moods={moods} />
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
                      📖 {t('view_all_entries')}
                    </Text>
                  </TouchableOpacity>
                </View>
              </View>
          ) : (
            <EmptyStateCard
              icon="book-open-outline"
              title={t('no_diary_entries')}
              subtitle={t('start_by_creating_entry')}
            />
          )
        )}

        {weeklySummary ? (
          <LatestWeeklySummary weeklySummary={weeklySummary} />
        ) : (
          <EmptyStateCard
            icon="chart-line"
            title={t('no_weekly_summary')}
            subtitle={t('summaries_generated_on_sundays')}
          />
        )}
      </ScrollView>
      }
    </SafeAreaView>
  );
} 