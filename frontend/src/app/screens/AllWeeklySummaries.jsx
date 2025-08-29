import {
    Text,
    View,
    ScrollView,
    TouchableOpacity,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { useEffect, useState, useContext } from "react";
import { useRouter } from "expo-router";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import weeklySummaryService from "../../services/weeklySummaryService";
import WeeklySummaryCard from "../../components/diary/WeeklySummaryCard";
import { AuthContext } from "../../context/AuthContext";
import { PageNavigation } from "../../components/common/PageNavigation";
import { ErrorComponent } from "../../components/common/ErrorComponent";
import { useTranslation } from "react-i18next";
import { LoadingComponent } from "../../components/common/LoadingComponent";
import EmptyStateCard from "../../components/common/EmptyStateCard";

export default function AllWeeklySummaries() {
    const router = useRouter();
    const { userId } = useContext(AuthContext);
    const [summaries, setSummaries] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [page, setPage] = useState(0);
    const [items, setItems] = useState(5);
    const { t } = useTranslation();

    useEffect(() => {
        const fetchSummaries = async () => {
            if (!userId) {
                setLoading(false);
                return;
            }

            try {
                setLoading(true);
                const response =
                    await weeklySummaryService.getWeeklySummaryByUser(page, items);
                    setSummaries(response.data || []);
            } catch (error) {
                setError(error.message);
                setSummaries([]);
            } finally {
                setLoading(false);
            }
        };

        fetchSummaries();
    }, [page, items]);

    return (
        <SafeAreaView className="flex-1 bg-white">
            {error ? (
                <ErrorComponent error={error} />
            ) : loading ? (
                <LoadingComponent />
            ) : (
                <View className="flex-1">
                    <View className="flex-row items-center justify-between p-4 border-b border-gray-200">
                        <TouchableOpacity
                            onPress={() => router.back()}
                            className="flex-row items-center"
                        >
                            <MaterialCommunityIcons
                                name="arrow-left"
                                size={24}
                                color="#374151"
                            />
                            <Text className="ml-2 text-gray-700">{t('back')}</Text>
                        </TouchableOpacity>
                        <Text className="text-xl font-bold text-gray-800">
                            {t('weekly_summaries')}
                        </Text>
                        <View style={{ width: 70 }} />
                    </View>
                    <PageNavigation
                        data={summaries}
                        page={page}
                        setPage={setPage}
                        items={items}
                        setItems={setItems}
                    />

                    <ScrollView className="flex-1">
                        {summaries.content.length > 0 ? (
                            summaries.content.map((summary, index) => (
                                <WeeklySummaryCard
                                    key={summary.id || index}
                                    summary={summary}
                                />
                            ))
                        ) : (
                            <EmptyStateCard
                                icon="chart-line"
                                iconSize={64}
                                iconColor="#9CA3AF"
                                title={t('no_weekly_summary')}
                                subtitle={`${t('summaries_generated_on_sundays')}\n${t('keep_using_app_to_see_progress')}`}
                                backgroundColor="bg-white"
                                padding="p-8"
                                titleColor="text-xl text-gray-600"
                            />
                        )}
                    </ScrollView>
                </View>
            )}
        </SafeAreaView>
    );
}
