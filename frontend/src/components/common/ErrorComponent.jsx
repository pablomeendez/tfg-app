import { Text, View, TouchableOpacity } from "react-native";
import { useEffect } from "react";
import { useTranslation } from "react-i18next";

export const ErrorComponent = ({ error, onRetry, onDismiss, autoCloseTime = 5000 }) => {
    const { t } = useTranslation();
    
    const errorMessage = typeof error === 'string' 
        ? error 
        : error?.message || error?.response?.data || t('error_default_message') || 'Ha ocurrido un error inesperado';

    useEffect(() => {
        if (autoCloseTime && onDismiss) {
            const timer = setTimeout(() => {
                onDismiss();
            }, autoCloseTime);

            return () => clearTimeout(timer);
        }
    }, [autoCloseTime, onDismiss]);
        
    return (
        <View className="absolute top-0 left-0 right-0 bg-red-50 border-b border-red-200 p-6 z-50">
            <Text className="text-red-700 text-center mb-4 font-bold text-lg leading-6">
                ⚠️ {errorMessage}
            </Text>
            
            <View className="flex-row justify-center space-x-4">
                {onRetry && (
                    <TouchableOpacity 
                        onPress={onRetry}
                        className="bg-red-600 px-6 py-3 rounded-xl min-w-[100]"
                    >
                        <Text className="text-white font-bold text-base text-center">
                            {t('error_retry_button') || 'Reintentar'}
                        </Text>
                    </TouchableOpacity>
                )}
                
                {onDismiss && (
                    <TouchableOpacity 
                        onPress={onDismiss}
                        className="bg-gray-600 px-6 py-3 rounded-xl min-w-[100]"
                    >
                        <Text className="text-white font-bold text-base text-center">
                            {t('error_close_button') || 'Cerrar'}
                        </Text>
                    </TouchableOpacity>
                )}
            </View>
            
            {autoCloseTime && (
                <Text className="text-gray-600 text-sm text-center mt-3 font-medium">
                    {t('error_auto_close_message') || 'Se cerrará automáticamente en unos segundos'}
                </Text>
            )}
        </View>
    );
}