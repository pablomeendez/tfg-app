import { View, Text } from "react-native";
import i18n from "../app/i18n/i18n";

const HabitCard = ({ habit }) => {
    const language = i18n.language;

    return (
        <View>
            <View className="bg-white rounded-xl shadow-md p-4">
                <View className="flex-row items-center justify-between mb-2">
                    <Text className="text-lg font-semibold text-gray-800 flex-1">
                        {language === 'en' ? habit.name.en : language === 'es' ? habit.name.es : habit.name.gl}
                    </Text>
                    <View className="w-3 h-3 rounded-full bg-green-500" />
                </View>

                <Text className="text-gray-600 text-sm mb-3">
                    {language === 'en' ? habit.description.en : language === 'es' ? habit.description.es : habit.description.gl || 'No description available'}
                </Text>

                <View className="flex-row items-center justify-between">
                    <View className="bg-blue-100 px-3 pt-1 rounded-full">
                        <Text className="text-blue-700 text-xs font-medium">
                            {language === 'en' ? habit.category?.name.en : language === 'es' ? habit.category?.name.es : habit.category?.name.gl}
                        </Text>
                    </View>
                </View>
            </View>
        </View>
    );
}

export default HabitCard;
