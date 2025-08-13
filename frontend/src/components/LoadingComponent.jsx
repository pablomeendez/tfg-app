import { ActivityIndicator, Text, View } from "react-native";

export const LoadingComponent = () => {
    return (
        <View className="flex-1 justify-center items-center bg-gray-50">
            <ActivityIndicator size="large" color="#3b82f6" />
            <Text className="mt-2 text-gray-600">Loading...</Text>
        </View>
    );
}