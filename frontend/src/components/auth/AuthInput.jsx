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
            className="border-[1px] p-3 rounded-md font-normal"
            placeholder={placeholder}
            value={value}
            onChangeText={onChangeText}
            autoCapitalize={autoCapitalize}
            keyboardType={keyboardType}
            {...props}
        />
    );
};

export default AuthInput;
