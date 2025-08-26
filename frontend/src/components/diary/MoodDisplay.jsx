import { View, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Image } from "expo-image";

const MoodDisplay = ({ mood, language, t }) => {
    if (!mood.id) {
        return null;
    }

    return (
        <View className="bg-white rounded-xl p-5 mb-6 shadow-sm border border-gray-100">
            <Text className="text-xl font-semibold text-gray-800 mb-4 flex-row items-center">
                <MaterialCommunityIcons name="emoticon-happy" size={24} color="#6B7280" className="mr-2" />
                {t("selected_mood")}
            </Text>
            
            <View className="bg-gray-50 rounded-lg p-4 border border-blue-100">
                <View className="flex flex-row items-center">
                    <View className="bg-yellow-300 rounded-full mr-4">
                        <Image 
                            source={{ uri: mood.image }} 
                            style={{ width: 40, height: 40 }} 
                        />
                    </View>
                    <View className="flex-1">
                        <Text className="text-lg font-semibold text-gray-800 mb-1">
                            {mood.name && 
                                (language === 'en' ? mood.name.en : language === 'es' ? mood.name.es : mood.name.gl)
                            }
                        </Text>
                    </View>
                </View>
            </View>
        </View>
    );
};

export default MoodDisplay;
