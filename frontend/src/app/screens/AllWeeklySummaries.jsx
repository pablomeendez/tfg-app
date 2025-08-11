import {
    Text,
    View,
    ScrollView,
    ActivityIndicator,
    TouchableOpacity,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { useEffect, useState, useContext } from "react";
import { useRouter } from "expo-router";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import weeklySummaryService from "../../services/weeklySummaryService";
import WeeklySummaryCard from "../../components/WeeklySummaryCard";
import { AuthContext } from "../../context/AuthContext";
import { PageNavigation } from "../../components/PageNavigation";
import { useTranslation } from "react-i18next";

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
                <View className="flex-1 justify-center items-center bg-gray-50 p-4">
                    <Text className="text-red-600 text-center mb-4">
                        Error: {error}
                    </Text>
                </View>
            ) : loading ? (
                <View className="flex-1 justify-center items-center bg-gray-50">
                    <ActivityIndicator size="large" color="#3b82f6" />
                    <Text className="mt-2 text-gray-600">{t('loading')}</Text>
                </View>
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
                            <Text className="ml-2 text-gray-700">Back</Text>
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
                            <View className="flex-1 items-center justify-center p-8">
                                <MaterialCommunityIcons
                                    name="chart-line"
                                    size={64}
                                    color="#9CA3AF"
                                />
                                <Text className="text-xl text-gray-600 text-center mt-4">
                                    {t('no_weekly_summary')}
                                </Text>
                                <Text className="text-gray-500 text-center mt-2">
                                    {t('summaries_generated_on_sundays')}
                                </Text>
                                <Text className="text-gray-500 text-center mt-1">
                                    {t('keep_using_app_to_see_progress')}
                                </Text>
                            </View>
                        )}
                    </ScrollView>
                </View>
            )}
        </SafeAreaView>
    );
}
