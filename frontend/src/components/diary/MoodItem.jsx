import { View, TouchableOpacity, Text } from 'react-native';
import { Image } from "expo-image";
import MultiLanguageText from './../common/MultiLanguageText';

const MoodItem = ({ 
    mood, 
    onPress, 
    selected = false,
    showName = true,
    size = 45,
    style = "default" 
}) => {
    const containerClass = style === "dailyCheckIn" 
        ? "rounded-full p-3 shadow-sm active:scale-95 border border-yellow-400 w-20 h-20 justify-center items-center"
        : `p-2 rounded-lg ${selected ? 'bg-blue-100 border-2 border-blue-300' : ''}`;

    return (
        <View className={style === "dailyCheckIn" ? "w-20 h-28" : ""}>
            <TouchableOpacity 
                onPress={onPress}
                className={containerClass}
            >
                <View className="flex items-center justify-center">
                    {mood.image ? (
                        <View className="bg-yellow-300 rounded-full">
                            <Image 
                                source={{ uri: mood.image }} 
                                style={{ width: size, height: size }} 
                            />
                        </View>
                    ) : (
                        <MultiLanguageText
                            textObject={mood.name}
                            fallback={`Mood ${mood.id}`}
                            className="text-gray-700 font-semibold text-xs text-center"
                        />
                    )}
                </View>
            </TouchableOpacity>
            
            {mood.name && showName && (
                <View className="h-6 mt-2">
                    <MultiLanguageText
                        textObject={mood.name}
                        className="text-center text-xs text-gray-600 font-medium w-20"
                        numberOfLines={2}
                        ellipsizeMode="tail"
                    />
                </View>
            )}
        </View>
    );
};

export default MoodItem;
