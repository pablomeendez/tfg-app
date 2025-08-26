import { View, Text } from "react-native";
import i18n from "../../i18n/i18n";
import MultiLanguageText from './../common/MultiLanguageText';

const HabitCard = ({ habit }) => {
    const language = i18n.language;

    return (
        <View>
            <View className="bg-white rounded-xl shadow-md p-4">
                <View className="flex-row items-center justify-between mb-2">
                    <MultiLanguageText 
                        textObject={habit.name}
                        className="text-lg font-semibold text-gray-800 flex-1"
                    />
                    <View className="w-3 h-3 rounded-full bg-green-500" />
                </View>

                <MultiLanguageText 
                    textObject={habit.description}
                    fallback="No description available"
                    className="text-gray-600 text-sm mb-3"
                />

                <View className="flex-row items-center justify-between">
                    <View className="bg-blue-100 px-3 pt-1 rounded-full">
                        <MultiLanguageText 
                            textObject={habit.category?.name}
                            className="text-blue-700 text-xs font-medium"
                        />
                    </View>
                </View>
            </View>
        </View>
    );
}

export default HabitCard;
