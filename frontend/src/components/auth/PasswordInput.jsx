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
        <View className="flex-row items-center border-[1px] rounded-md font-normal">
            <TextInput
                className="flex-1 p-3"
                placeholder={placeholder}
                value={value}
                onChangeText={onChangeText}
                secureTextEntry={passwordVisibility}
                autoCapitalize={autoCapitalize}
                {...props}
            />
            <Pressable className="pr-3" onPress={handlePasswordVisibility}>
                <MaterialCommunityIcons name={eyeIcon} size={18} color="#232323" />
            </Pressable>
        </View>
    );
};

export default PasswordInput;
