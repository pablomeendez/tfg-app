import { View, Text } from 'react-native';

// Componente que replica tus mensajes de chat exactos
const ChatMessage = ({ message }) => {
    return (
        <View
            className={`mb-4 ${message.isUser ? 'items-end' : 'items-start'}`}
        >
            <View
                className={`max-w-[80%] p-3 rounded-2xl ${
                    message.isUser
                        ? 'bg-blue-500 rounded-br-sm'
                        : 'bg-gray-200 rounded-bl-sm'
                }`}
            >
                <Text
                    className={`text-base ${
                        message.isUser ? 'text-white' : 'text-gray-800'
                    }`}
                >
                    {message.text}
                </Text>
                <Text
                    className={`text-xs mt-1 ${
                        message.isUser ? 'text-blue-100' : 'text-gray-500'
                    }`}
                >
                    {message.timestamp}
                </Text>
            </View>
        </View>
    );
};

export default ChatMessage;
