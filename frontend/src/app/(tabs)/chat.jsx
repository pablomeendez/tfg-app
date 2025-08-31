import { View, Text, ScrollView, KeyboardAvoidingView, Platform } from 'react-native';
import { useState, useEffect, useRef, useContext } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useTranslation } from 'react-i18next';
import assistantService from '../../services/assistantService';
import { AuthContext } from '../../context/AuthContext';
import { LoadingComponent } from '../../components/common/LoadingComponent';
import { ErrorComponent } from '../../components/common/ErrorComponent';
import ChatMessage from '../../components/chat/ChatMessage';
import ChatInput from '../../components/chat/ChatInput';

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

    const handleRetry = () => {
        setError(null);
        if (inputText.trim()) {
            sendMessage();
        }
    };

    const handleDismissError = () => {
        setError(null);
    };

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

            const responseText = response.data.aiMessage.text ? response.data.aiMessage.text : response.data;
            const assistantMessage = {
                id: Date.now() + 1,
                text: responseText,
                isUser: false,
                timestamp: new Date().toLocaleTimeString()
            };

            setMessages(prev => [...prev, assistantMessage]);
        } catch (err) {
            setError(err);
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

    return (
        <SafeAreaView className="flex-1 bg-white">
            <KeyboardAvoidingView 
                className="flex-1" 
                behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
            >
                {error && (
                    <ErrorComponent 
                        error={error} 
                        onRetry={handleRetry}
                        onDismiss={handleDismissError}
                        autoCloseTime={5000}
                    />
                )}
                
                <View className="flex-1">
                    <View className="p-4">
                        <Text className="text-2xl font-bold text-gray-800 mb-2">
                            {t('chat_title') || 'Asistente Personal'}
                        </Text>
                        <Text className="text-gray-600 text-sm">
                            {t('chat_subtitle') || 'Tu compañero de bienestar'}
                        </Text>
                    </View>

                    <ScrollView
                        ref={scrollViewRef}
                        className="flex-1 px-4"
                        showsVerticalScrollIndicator={false}
                        onContentSizeChange={scrollToBottom}
                    >
                        {messages.map(message => (
                            <ChatMessage key={message.id} message={message} />
                        ))}
                        
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

                    <View className="border-t border-gray-200 p-4">
                        <ChatInput
                            inputText={inputText}
                            setInputText={setInputText}
                            onSend={sendMessage}
                            loading={loading}
                            placeholder={t('chat_input_placeholder') || 'Escribe tu mensaje...'}
                        />
                        <Text className="text-xs text-gray-500 text-center mt-2">
                            {t('chat_disclaimer') || 'El asistente utiliza IA y puede cometer errores. No reemplaza el consejo médico profesional.'}
                        </Text>
                    </View>
                </View>
            </KeyboardAvoidingView>
        </SafeAreaView>
    );
}