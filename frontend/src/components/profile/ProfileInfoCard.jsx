import { View, Text, TouchableOpacity } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';

const ProfileInfoCard = ({ icon, iconColor, label, value, onPress = null }) => {
    const CardComponent = onPress ? TouchableOpacity : View;
    
    return (
        <CardComponent 
            className={`bg-white rounded-lg p-4 mb-3 flex-row items-center ${onPress ? 'justify-between' : ''}`}
            onPress={onPress}
        >
            <View className="flex-row items-center flex-1">
                <MaterialCommunityIcons name={icon} size={24} color={iconColor} />
                <View className="ml-3 flex-1">
                    <Text className="text-gray-500 text-sm">{label}</Text>
                    {typeof value === 'string' ? (
                        <Text className="text-gray-800 text-base font-medium">{value}</Text>
                    ) : (
                        value
                    )}
                </View>
            </View>
            {onPress && (
                <MaterialCommunityIcons name="chevron-right" size={20} color="#9CA3AF" />
            )}
        </CardComponent>
    );
};

export default ProfileInfoCard;
