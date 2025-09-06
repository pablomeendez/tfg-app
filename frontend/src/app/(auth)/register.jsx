import { useContext, useState } from "react";
import { View, Text, TouchableOpacity, Alert, ScrollView } from "react-native";
import { AuthContext } from "../../context/AuthContext";
import { useRouter } from "expo-router";
import { SafeAreaView } from "react-native-safe-area-context";
import { useTranslation } from "react-i18next";
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AuthInput from '../../components/auth/AuthInput';
import PasswordInput from '../../components/auth/PasswordInput';


export default function Register() {

    const [params,  setParams] = useState({username: '', password: '', name: '', lastName: '', email: ''});
    const router = useRouter();
    const { register } = useContext(AuthContext);
    const { t } = useTranslation();

    const handleRegister = async () => {
        if (!params.username.trim() || !params.password.trim() || !params.name.trim() || !params.lastName.trim() || !params.email.trim()) {
            Alert.alert(t('error'), t('enter_credentials'));
            return;
        }

        if (!params.email.includes('@')) {
            Alert.alert(t('error'), t('invalid_email'));
            return;
        }

        try {
            await register(params.username, params.password, params.name, params.lastName, params.email);
            Alert.alert(t('success'), t('account_created'), [
                { text: 'OK', onPress: () => router.replace('/(auth)') }
            ]);
        } catch (error) {
            Alert.alert(t('error'), t('registration_failed'));
        }
    }
      
    return (
        <SafeAreaView className="flex-1 bg-gray-50">
            <View className="bg-white px-6 py-4">
                <View className="flex-row items-center justify-between">
                    <TouchableOpacity 
                        className="bg-gray-100 rounded-full p-2" 
                        onPress={() => router.back()}
                    >
                        <MaterialCommunityIcons name="arrow-left" size={24} color="#6B7280" />
                    </TouchableOpacity>
                    <Text className="text-lg font-semibold text-gray-800">{t('create_account')}</Text>
                    <View className="w-10" />
                </View>
            </View>

            <ScrollView className="flex-1" showsVerticalScrollIndicator={false}>
                <View className="px-6 py-8">
                    <View className="items-center mb-8">
                        <View className="bg-green-100 w-16 h-16 rounded-full items-center justify-center mb-4">
                            <MaterialCommunityIcons name="account-plus" size={32} color="#10B981" />
                        </View>
                        <Text className="text-2xl font-bold text-gray-800">{t('join_us')}</Text>
                        <Text className="text-gray-600 mt-1 text-center">{t('create_your_wellness_account')}</Text>
                    </View>

                    {/* Form */}
                    <View className="bg-white rounded-xl p-6 border border-gray-100">
                        <View className="space-y-4">
                            <AuthInput
                                placeholder={t('username')}
                                value={params.username}
                                onChangeText={(e) => setParams({...params, username: e})}
                            />
                            
                            <PasswordInput
                                placeholder={t('password')}
                                value={params.password}
                                onChangeText={(e) => setParams({...params, password: e})}
                            />

                            <AuthInput
                                placeholder={t('name')}
                                value={params.name}
                                onChangeText={(e) => setParams({...params, name: e})}
                            />

                            <AuthInput
                                placeholder={t('last_name')}
                                value={params.lastName}
                                onChangeText={(e) => setParams({...params, lastName: e})}
                            />

                            <AuthInput
                                placeholder={t('email')}
                                value={params.email}
                                onChangeText={(e) => setParams({...params, email: e})}
                                keyboardType="email-address"
                            />

                            <TouchableOpacity 
                                className="bg-green-600 rounded-lg items-center justify-center p-4 mt-6"
                                onPress={handleRegister}
                            >
                                <Text className="text-white text-lg font-semibold">{t('register')}</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </ScrollView>
        </SafeAreaView>
    );
};