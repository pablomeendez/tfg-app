import { View, TouchableOpacity, Text } from "react-native";
import WeeklySummaryCard from "./WeeklySummaryCard";
import { useRouter } from "expo-router";
import { useTranslation } from "react-i18next";

const LatestWeeklySummary = ({weeklySummary}) => {

    const router = useRouter();
    const { t } = useTranslation();
    console.log(weeklySummary);

    return (
        <View>
            <WeeklySummaryCard summary={weeklySummary} />
            <TouchableOpacity 
              className="bg-blue-500 rounded-lg m-3 p-3"
              onPress={() => {
                router.push('/screens/AllWeeklySummaries');
              }}
            >
              <Text className="text-white text-center font-semibold">
                {t('view_all_summaries')}
              </Text>
            </TouchableOpacity>
          </View>
    )
}

export default LatestWeeklySummary;