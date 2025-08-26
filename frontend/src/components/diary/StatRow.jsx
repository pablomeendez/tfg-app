import { View, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';

// Componente que replica tus filas de estadísticas
const StatRow = ({ 
    icon, 
    iconColor = "#6B7280", 
    label, 
    value, 
    valueColor = "#374151" 
}) => {
    return (
        <View className="flex-row items-center justify-between py-1">
            <View className="flex-row items-center">
                <MaterialCommunityIcons name={icon} size={20} color={iconColor} />
                <Text className="ml-2 text-gray-700">{label}</Text>
            </View>
            <Text className="font-semibold" style={{ color: valueColor }}>
                {value}
            </Text>
        </View>
    );
};

export default StatRow;
