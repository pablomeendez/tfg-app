import { TextInput } from 'react-native';

const AuthInput = ({ 
    placeholder, 
    value, 
    onChangeText, 
    autoCapitalize = "none",
    keyboardType = "default",
    ...props 
}) => {
    return (
        <TextInput
            className="bg-gray-50 border border-gray-200 px-4 py-5 rounded-lg text-gray-800 text-base min-h-[60px]"
            placeholder={placeholder}
            placeholderTextColor="#9CA3AF"
            value={value}
            onChangeText={onChangeText}
            autoCapitalize={autoCapitalize}
            keyboardType={keyboardType}
            textAlignVertical="center"
            {...props}
        />
    );
};

export default AuthInput;
