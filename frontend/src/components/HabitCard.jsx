import { View, Text } from "react-native";

const HabitCard = ({ habit }) => {

    return (
        <View>
            <View className="bg-white rounded-xl shadow-md p-4">
                <View className="flex-row items-center justify-between mb-2">
                    <Text className="text-lg font-semibold text-gray-800 flex-1">
                        {habit.name}
                    </Text>
                    <View className="w-3 h-3 rounded-full bg-green-500" />
                </View>

                <Text className="text-gray-600 text-sm mb-3">
                    {habit.description}
                </Text>

                <View className="flex-row items-center justify-between">
                    <View className="bg-blue-100 px-3 pt-1 rounded-full">
                        <Text className="text-blue-700 text-xs font-medium">
                            {habit.category?.name}
                        </Text>
                    </View>
                </View>
            </View>
        </View>
    );
}

export default HabitCard;
