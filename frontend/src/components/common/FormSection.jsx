import { View, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';

const FormSection = ({ title, icon, children }) => {
    return (
        <View className="bg-white rounded-xl p-5 mb-6 shadow-sm border border-gray-100">
            <Text className="text-xl font-semibold text-gray-800 mb-4 flex-row items-center">
                {icon && <MaterialCommunityIcons name={icon} size={24} color="#6B7280" className="mr-2" />}
                {title}
            </Text>
            {children}
        </View>
    );
};

export default FormSection;
