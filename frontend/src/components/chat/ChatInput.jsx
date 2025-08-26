import { View, TextInput, TouchableOpacity } from 'react-native';
import { MaterialIcons } from '@expo/vector-icons';

// Componente que replica tu input de chat exacto
const ChatInput = ({ 
    inputText, 
    setInputText, 
    onSend, 
    loading, 
    placeholder,
    ...props 
}) => {
    return (
        <View className="flex-row items-center bg-gray-100 rounded-full px-4 py-2">
            <TextInput
                className="flex-1 text-base py-2"
                placeholder={placeholder}
                value={inputText}
                onChangeText={setInputText}
                multiline
                maxLength={500}
                editable={!loading}
                onSubmitEditing={onSend}
                blurOnSubmit={false}
                {...props}
            />
            <TouchableOpacity
                onPress={onSend}
                disabled={!inputText.trim() || loading}
                className={`ml-2 p-2 rounded-full ${
                    inputText.trim() && !loading
                        ? 'bg-blue-500'
                        : 'bg-gray-300'
                }`}
            >
                <MaterialIcons
                    name="send"
                    size={20}
                    color={inputText.trim() && !loading ? 'white' : 'gray'}
                />
            </TouchableOpacity>
        </View>
    );
};

export default ChatInput;
