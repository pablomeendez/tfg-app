import { Text, View } from "react-native";

export const ErrorComponent = ({ error }) => {
    return (
        <View className="flex-1 justify-center items-center bg-gray-50 p-4">
                <Text className="text-red-600 text-center mb-4">Error: {error}</Text>
            </View>
    );
}