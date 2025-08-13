import { View, Text } from "react-native";
import i18n from "../app/i18n/i18n";

const HabitCard = ({ habit }) => {
    const language = i18n.language;

    return (
        <View>
            <View className="bg-white rounded-xl shadow-md p-4">
                <View className="flex-row items-center justify-between mb-2">
                    <Text className="text-lg font-semibold text-gray-800 flex-1">
                        {language === 'en' ? habit.nameEn : language === 'es' ? habit.nameEs : habit.nameGl}
                    </Text>
                    <View className="w-3 h-3 rounded-full bg-green-500" />
                </View>

                <Text className="text-gray-600 text-sm mb-3">
                    {language === 'en' ? habit.descriptionEn : language === 'es' ? habit.descriptionEs : habit.descriptionGl || 'No description available'}
                </Text>

                <View className="flex-row items-center justify-between">
                    <View className="bg-blue-100 px-3 pt-1 rounded-full">
                        <Text className="text-blue-700 text-xs font-medium">
                            {language === 'en' ? habit.category?.nameEn : language === 'es' ? habit.category?.nameEs : habit.category?.nameGl}
                        </Text>
                    </View>
                </View>
            </View>
        </View>
    );
}

export default HabitCard;
