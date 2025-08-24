import { View, Text, ScrollView, TextInput, TouchableOpacity, KeyboardAvoidingView, Platform } from 'react-native';
import { useState, useEffect, useRef, useContext } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useTranslation } from 'react-i18next';
import { MaterialIcons } from '@expo/vector-icons';
import assistantService from '../../services/assistantService';
import { AuthContext } from '../../context/AuthContext';
import { LoadingComponent } from '../../components/LoadingComponent';
import { ErrorComponent } from '../../components/ErrorComponent';

export default function Chat() {
    const [messages, setMessages] = useState([]);
    const [inputText, setInputText] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const { t } = useTranslation();
    const { userId } = useContext(AuthContext);
    const scrollViewRef = useRef();

    useEffect(() => {
        setMessages([
            {
                id: 1,
                text: t('chat_welcome_message') || '¡Hola! Soy tu asistente personal. Puedo ayudarte con tus hábitos, emociones y rutinas. ¿En qué puedo ayudarte hoy?',
                isUser: false,
                timestamp: new Date().toLocaleTimeString()
            }
        ]);
    }, [t]);

    const sendMessage = async () => {
        if (!inputText.trim() || loading) return;

        const userMessage = {
            id: Date.now(),
            text: inputText.trim(),
            isUser: true,
            timestamp: new Date().toLocaleTimeString()
        };

        setMessages(prev => [...prev, userMessage]);
        setInputText('');
        setError(null);

        try {
            setLoading(true);
            const response = await assistantService.sendMessage(inputText.trim());
            
            const assistantMessage = {
                id: Date.now() + 1,
                text: response.data,
                isUser: false,
                timestamp: new Date().toLocaleTimeString()
            };

            setMessages(prev => [...prev, assistantMessage]);
        } catch (err) {
            console.log(err);
            console.error('Error sending message:', err);
            setError(t('chat_error') || 'Error al enviar el mensaje');
            
            const errorMessage = {
                id: Date.now() + 1,
                text: t('chat_error_message') || 'Lo siento, hubo un error al procesar tu mensaje. Por favor, inténtalo de nuevo.',
                isUser: false,
                timestamp: new Date().toLocaleTimeString()
            };

            setMessages(prev => [...prev, errorMessage]);
        } finally {
            setLoading(false);
        }
    };

    const scrollToBottom = () => {
        setTimeout(() => {
            scrollViewRef.current?.scrollToEnd({ animated: true });
        }, 100);
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const renderMessage = (message) => (
        <View
            key={message.id}
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

    return (
        <SafeAreaView className="flex-1 bg-white">
            <KeyboardAvoidingView 
                className="flex-1" 
                behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
            >
                {/* Header */}
                <View className="bg-blue-600 p-4 shadow-sm">
                    <Text className="text-white text-xl font-bold text-center">
                        {t('chat_title') || 'Asistente Personal'}
                    </Text>
                    <Text className="text-blue-100 text-sm text-center mt-1">
                        {t('chat_subtitle') || 'Tu compañero de bienestar'}
                    </Text>
                </View>

                {error && (
                    <View className="p-2">
                        <ErrorComponent error={error} />
                    </View>
                )}

                {/* Messages */}
                <ScrollView
                    ref={scrollViewRef}
                    className="flex-1 px-4 py-2"
                    showsVerticalScrollIndicator={false}
                    onContentSizeChange={scrollToBottom}
                >
                    {messages.map(renderMessage)}
                    
                    {loading && (
                        <View className="items-start mb-4">
                            <View className="bg-gray-200 p-3 rounded-2xl rounded-bl-sm">
                                <View className="flex-row items-center">
                                    <Text className="text-gray-600 mr-2">
                                        {t('chat_thinking') || 'Pensando...'}
                                    </Text>
                                    <View className="flex-row">
                                        <View className="w-2 h-2 bg-gray-400 rounded-full mx-0.5 animate-pulse" />
                                        <View className="w-2 h-2 bg-gray-400 rounded-full mx-0.5 animate-pulse" />
                                        <View className="w-2 h-2 bg-gray-400 rounded-full mx-0.5 animate-pulse" />
                                    </View>
                                </View>
                            </View>
                        </View>
                    )}
                </ScrollView>

                {/* Input */}
                <View className="border-t border-gray-200 p-4">
                    <View className="flex-row items-center bg-gray-100 rounded-full px-4 py-2">
                        <TextInput
                            className="flex-1 text-base py-2"
                            placeholder={t('chat_input_placeholder') || 'Escribe tu mensaje...'}
                            value={inputText}
                            onChangeText={setInputText}
                            multiline
                            maxLength={500}
                            editable={!loading}
                            onSubmitEditing={sendMessage}
                            blurOnSubmit={false}
                        />
                        <TouchableOpacity
                            onPress={sendMessage}
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
                    <Text className="text-xs text-gray-500 text-center mt-2">
                        {t('chat_disclaimer') || 'El asistente utiliza IA y puede cometer errores. No reemplaza el consejo médico profesional.'}
                    </Text>
                </View>
            </KeyboardAvoidingView>
        </SafeAreaView>
    );
}