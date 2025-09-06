import { View, TextInput, Pressable } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import useTogglePasswordVisibility from '../../hooks/useTogglePasswordVisibility';

const PasswordInput = ({ 
    placeholder, 
    value, 
    onChangeText, 
    autoCapitalize = "none",
    ...props 
}) => {
    const { passwordVisibility, eyeIcon, handlePasswordVisibility } = useTogglePasswordVisibility();

    return (
        <View className="bg-gray-50 border border-gray-200 rounded-lg flex-row items-center min-h-[60px]">
            <TextInput
                className="flex-1 px-4 py-5 text-gray-800 text-base"
                placeholder={placeholder}
                placeholderTextColor="#9CA3AF"
                value={value}
                onChangeText={onChangeText}
                secureTextEntry={passwordVisibility}
                autoCapitalize={autoCapitalize}
                textAlignVertical="center"
                {...props}
            />
            <Pressable className="px-4 py-5" onPress={handlePasswordVisibility}>
                <MaterialCommunityIcons name={eyeIcon} size={20} color="#6B7280" />
            </Pressable>
        </View>
    );
};

export default PasswordInput;
