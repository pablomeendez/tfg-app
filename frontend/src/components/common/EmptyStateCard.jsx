import { View, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';

const EmptyStateCard = ({ 
  icon = "information-outline", 
  iconSize = 64,
  iconColor = "#D1D5DB",
  title, 
  subtitle = null, 
  backgroundColor = "bg-gray-100",
  titleColor = "text-gray-600",
  subtitleColor = "text-gray-500",
  padding = "p-4",
  margin = "m-3",
  showIcon = true
}) => {
  return (
    <View className={`${backgroundColor} rounded-lg ${margin} ${padding} items-center`}>
      {showIcon && icon && (
        <MaterialCommunityIcons 
          name={icon} 
          size={iconSize} 
          color={iconColor} 
        />
      )}
      <Text className={`${titleColor} text-center ${showIcon && icon ? 'mt-4' : ''}`}>
        {title}
      </Text>
      {subtitle && (
        <Text className={`${subtitleColor} text-center text-sm mt-1`}>
          {subtitle}
        </Text>
      )}
    </View>
  );
};

export default EmptyStateCard;
