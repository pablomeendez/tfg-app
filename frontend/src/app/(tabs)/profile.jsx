import { useContext, useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, Alert, ScrollView, ActivityIndicator } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { AuthContext } from '../../context/AuthContext';
import { useRouter } from 'expo-router';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useTranslation } from 'react-i18next';
import { LoadingComponent } from '../../components/LoadingComponent';
import DropDownPicker from 'react-native-dropdown-picker';

export default function Profile() {
    const { logout } = useContext(AuthContext);
    const router = useRouter();
    const [userInfo, setUserInfo] = useState(null);
    const [loading, setLoading] = useState(true);
    const { t, i18n } = useTranslation();    
    const [languageOpen, setLanguageOpen] = useState(false);
    const [currentLanguage, setCurrentLanguage] = useState(i18n.language);
    const [languageItems, setLanguageItems] = useState([
        { label: 'Español', value: 'es', icon: () => <Text>🇪🇸</Text> },
        { label: 'English', value: 'en', icon: () => <Text>🇺🇸</Text> },
        { label: 'Galego', value: 'gl', icon: () => <Text>🇪🇸</Text> }
    ]);

    useEffect(() => {
        const loadUserData = async () => {
            try {
                const userString = await AsyncStorage.getItem('user');
                if (userString) {
                    const user = JSON.parse(userString);
                    setUserInfo(user);
                }
                setLoading(false);
            } catch (error) {
                console.error('Error loading user data:', error);
                setLoading(false);
            }
        };

        loadUserData();
    }, []);

    const changeLanguage = async (languageCode) => {
        try {
            await i18n.changeLanguage(languageCode);
            await AsyncStorage.setItem('userLanguage', languageCode);
            setCurrentLanguage(languageCode);
        } catch (error) {
            console.error('Error changing language:', error);
            Alert.alert(t('error'), t('could_not_change_language'));
        }
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

                        <View className="bg-white rounded-lg p-4 mb-3 flex-row items-center">
                            <MaterialCommunityIcons name="identifier" size={24} color="#6B7280" />
                            <View className="ml-3 flex-1">
                                <Text className="text-gray-500 text-sm">{t('user_id')}</Text>
                                <Text className="text-gray-800 text-base font-medium">#{userInfo?.id}</Text>
                            </View>
                        </View>

                        <View className="bg-white rounded-lg p-4 mb-3 flex-row items-center">
                            <MaterialCommunityIcons name="account-circle" size={24} color="#3B82F6" />
                            <View className="ml-3 flex-1">
                                <Text className="text-gray-500 text-sm">{t('username')}</Text>
                                <Text className="text-gray-800 text-base font-medium">{userInfo?.userName}</Text>
                            </View>
                        </View>

                        <View className="bg-white rounded-lg p-4 mb-3 flex-row items-center">
                            <MaterialCommunityIcons name="account" size={24} color="#10B981" />
                            <View className="ml-3 flex-1">
                                <Text className="text-gray-500 text-sm">{t('name')}</Text>
                                <Text className="text-gray-800 text-base font-medium">{userInfo?.firstName}</Text>
                            </View>
                        </View>

                        <View className="bg-white rounded-lg p-4 mb-3 flex-row items-center">
                            <MaterialCommunityIcons name="account-outline" size={24} color="#10B981" />
                            <View className="ml-3 flex-1">
                                <Text className="text-gray-500 text-sm">{t('last_name')}</Text>
                                <Text className="text-gray-800 text-base font-medium">{userInfo?.lastName}</Text>
                            </View>
                        </View>

                        <View className="bg-white rounded-lg p-4 mb-3 flex-row items-center">
                            <MaterialCommunityIcons name="email" size={24} color="#8B5CF6" />
                            <View className="ml-3 flex-1">
                                <Text className="text-gray-500 text-sm">{t('email')}</Text>
                                <Text className="text-gray-800 text-base font-medium">{userInfo?.email}</Text>
                            </View>
                        </View>

                        {/* Selector de idioma */}
                        <View className="bg-white rounded-lg p-4 mb-3" style={{ zIndex: 1000 }}>
                            <View className="flex-row items-center mb-3">
                                <MaterialCommunityIcons name="translate" size={24} color="#F59E0B" />
                                <Text className="text-gray-500 text-sm ml-3">{t('language')}</Text>
                            </View>
                            <DropDownPicker
                                open={languageOpen}
                                value={currentLanguage}
                                items={languageItems}
                                setOpen={setLanguageOpen}
                                setValue={setCurrentLanguage}
                                setItems={setLanguageItems}
                                onChangeValue={(value) => {
                                    if (value) {
                                        changeLanguage(value);
                                    }
                                }}
                                placeholder={t('select_language')}
                                style={{
                                    backgroundColor: '#F9FAFB',
                                    borderColor: '#D1D5DB',
                                    borderRadius: 8,
                                    minHeight: 45,
                                }}
                                textStyle={{
                                    color: '#374151',
                                    fontSize: 16,
                                    fontWeight: '500',
                                }}
                                dropDownContainerStyle={{
                                    backgroundColor: '#FFFFFF',
                                    borderColor: '#D1D5DB',
                                    borderRadius: 8,
                                    marginTop: 5,
                                }}
                                selectedItemContainerStyle={{
                                    backgroundColor: '#EBF4FF',
                                }}
                                selectedItemLabelStyle={{
                                    color: '#1D4ED8',
                                    fontWeight: '600',
                                }}
                                zIndex={1000}
                                zIndexInverse={3000}
                                listMode="SCROLLVIEW"
                                scrollViewProps={{
                                    nestedScrollEnabled: true,
                                }}
                            />
                        </View>

                        <TouchableOpacity 
                            className="bg-red-500 rounded-lg p-4 flex-row items-center justify-center"
                            onPress={handleLogout}
                        >
                            <MaterialCommunityIcons name="logout" size={20} color="white" />
                            <Text className="text-white text-lg font-semibold ml-2">{t('log_out')}</Text>
                        </TouchableOpacity>
                    </View>
                </ScrollView>
            </View>
            }
        </SafeAreaView>
    )
}