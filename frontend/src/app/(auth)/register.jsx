import { useContext, useState } from "react";
import { View, Text, TouchableOpacity, Alert } from "react-native";
import { AuthContext } from "../../context/AuthContext";
import { useRouter } from "expo-router";
import { SafeAreaView } from "react-native-safe-area-context";
import { useTranslation } from "react-i18next";
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
        <SafeAreaView className="flex-1">
            <View className="flex-1 p-5 justify-center gap-2">
                <TouchableOpacity className="absolute left-0 top-0 m-4 z-10" onPress={() => router.back()}>
                    <Text className="text-blue-600 text-lg">Back</Text>
                </TouchableOpacity>
                <Text className="text-2xl font-bold mb-5 text-center">Register</Text>
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
                        className="bg-blue-600 rounded-md items-center justify-center p-4 mt-3"
                        onPress={handleRegister}
                    >
                        <Text className="text-white text-lg font-semibold">{t('register')}</Text>
                    </TouchableOpacity>
                </View>
            </SafeAreaView>
    );
};