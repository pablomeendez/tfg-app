import { useContext, useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, Alert, ScrollView, ActivityIndicator } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { AuthContext } from '../../context/AuthContext';
import { useRouter } from 'expo-router';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useTranslation } from 'react-i18next';
import { LoadingComponent } from '../../components/common/LoadingComponent';
import ProfileInfoCard from '../../components/profile/ProfileInfoCard';
import LanguageSelector from '../../components/profile/LanguageSelector';

export default function Profile() {
    const { logout, userId, user } = useContext(AuthContext);
    const router = useRouter();
    const [userInfo, setUserInfo] = useState(user);
    const [loading, setLoading] = useState(false);
    const { t, i18n } = useTranslation();    
    const [currentLanguage, setCurrentLanguage] = useState(i18n.language);

    const handleLanguageChange = (newLanguage) => {
        setCurrentLanguage(newLanguage);
    };

    useEffect(() => {
        const loadSavedLanguage = async () => {
            try {
                const savedLanguage = await AsyncStorage.getItem('userLanguage');
                if (savedLanguage) {
                    setCurrentLanguage(savedLanguage);
                    await i18n.changeLanguage(savedLanguage);
                }
            } catch (error) {
                console.error('Error loading saved language:', error);
            }
        };
        loadSavedLanguage();
    }, []);

    const handleLogout = async () => {
        Alert.alert(
            t('log_out'),
            t('are_you_sure_you_want_to_log_out'),
            [
                { text: t('cancel'), style: 'cancel' },
                {
                    text: t('log_out'),
                    style: 'destructive',
                    onPress: async () => {
                        try {
                            await logout();
                            router.replace('/(auth)');
                        } catch (e) {
                            Alert.alert(t('error'), t('could_not_log_out'));
                        }
                    }
                }
            ]
        );
    }

    return (
        <SafeAreaView className="flex-1 bg-white">
            {loading ? (
                <LoadingComponent />
            ) : 
            <View className="flex-1">
                <ScrollView 
                    className="flex-1"
                    nestedScrollEnabled={true}
                    showsVerticalScrollIndicator={false}
                >
                    <View className="bg-blue-500 px-4 py-6">
                        <View className="items-center">
                            <View className="w-20 h-20 bg-white rounded-full items-center justify-center mb-4">
                                <MaterialCommunityIcons name="account" size={40} color="#3B82F6" />
                            </View>
                            <Text className="text-white text-2xl font-bold mb-1">
                                {userInfo?.firstName} {userInfo?.lastName}
                            </Text>
                            <Text className="text-blue-100 text-base">
                                @{userInfo?.userName}
                            </Text>
                        </View>
                    </View>

                    <View className="flex-1 px-6 py-6 bg-gray-50">
                        <Text className="text-xl font-bold text-gray-800 mb-4">{t('personal_information')}</Text>

                        <ProfileInfoCard
                            icon="identifier"
                            iconColor="#6B7280"
                            label={t('user_id')}
                            value={`#${userInfo?.id}`}
                        />

                        <ProfileInfoCard
                            icon="account-circle"
                            iconColor="#3B82F6"
                            label={t('username')}
                            value={userInfo?.userName}
                        />

                        <ProfileInfoCard
                            icon="account"
                            iconColor="#10B981"
                            label={t('name')}
                            value={userInfo?.firstName}
                        />

                        <ProfileInfoCard
                            icon="account-outline"
                            iconColor="#10B981"
                            label={t('last_name')}
                            value={userInfo?.lastName}
                        />

                        <ProfileInfoCard
                            icon="email"
                            iconColor="#8B5CF6"
                            label={t('email')}
                            value={userInfo?.email}
                        />

                        <LanguageSelector
                            currentLanguage={currentLanguage}
                            onLanguageChange={handleLanguageChange}
                        />

                        <TouchableOpacity 
                            className="bg-red-500 rounded-md items-center justify-center p-4 mt-3"
                            onPress={handleLogout}
                        >
                            <Text className="text-white text-lg font-semibold">{t('log_out')}</Text>
                        </TouchableOpacity>
                    </View>
                </ScrollView>
            </View>
            }
        </SafeAreaView>
    )
}