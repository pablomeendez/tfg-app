import { MaterialCommunityIcons } from "@expo/vector-icons";
import { useTranslation } from "react-i18next";
import { Text, View } from "react-native";
import MultiLanguageText from "../common/MultiLanguageText";

export const TrophyCard = ({ index, userTrophy }) => {
    const { t } = useTranslation();

    return (
        <View key={index} className="bg-white rounded-xl p-4 mb-3 shadow-sm border border-gray-100">
            <View className="flex-row items-center">
                <View className="bg-yellow-100 rounded-full p-3 mr-4">
                    <MaterialCommunityIcons name="trophy" size={24} color="#F59E0B" />
                </View>
                <View className="flex-1">
                    <MultiLanguageText
                        textObject={userTrophy.trophy.name}
                        className="text-lg font-bold text-gray-800"
                    />
                    <MultiLanguageText
                        textObject={userTrophy.trophy.description}
                        className="text-gray-600 mt-1"
                    />
                    <MultiLanguageText
                        textObject={userTrophy.habit.name}
                        className="text-gray-600 mt-1"
                    />
                    <Text className="text-green-600 text-sm font-medium mt-2">
                        {t('days_streak', { count: userTrophy.trophy.days })} {t('completed')}
                    </Text>
                    <Text className="text-gray-500 text-xs mt-1">
                        {t('earned_on', { date: new Date(userTrophy.obtainedAt).toLocaleDateString() })}
                    </Text>
                    
                </View>
            </View>
        </View>
    );
}