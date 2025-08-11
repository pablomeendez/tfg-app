import { useContext, useState } from "react";
import { View, Text, TextInput, TouchableOpacity, Alert, Pressable } from "react-native";
import { AuthContext } from "../../context/AuthContext";
import { useRouter } from "expo-router";
import useTogglePasswordVisibility from '../../hooks/useTogglePasswordVisibility';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { SafeAreaView } from "react-native-safe-area-context";
import { t } from "i18next";
import { useTranslation } from "react-i18next";


export default function Register() {

    const [params,  setParams] = useState({username: '', password: '', name: '', lastName: '', email: ''});
    const router = useRouter();
    const { register } = useContext(AuthContext);
    const { passwordVisibility, eyeIcon, handlePasswordVisibility } = useTogglePasswordVisibility();
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
                    <TextInput
                        className="border-[1px] p-3 rounded-md font-normal"
                        placeholder={t('username')}
                        value={params.username}
                        onChangeText={(e) => setParams({...params, username: e})}
                        autoCapitalize="none"
                    />
                    
                    <View className="flex-row items-center border-[1px] rounded-md font-normal">
                        <TextInput
                            className="flex-1 p-3"
                            placeholder={t('password')}
                            value={params.password}
                            onChangeText={(e) => setParams({...params, password: e})}
                            secureTextEntry={passwordVisibility}
                            autoCapitalize="none"
                        />
                        <Pressable className="pr-3" onPress={handlePasswordVisibility}>
                            <MaterialCommunityIcons name={eyeIcon} size={16} color="#232323" />
                        </Pressable>
                    </View>

                    <TextInput
                        className="border-[1px] p-3 rounded-md font-normal"
                        placeholder={t('name')}
                        value={params.name}
                        onChangeText={(e) => setParams({...params, name: e})}
                        autoCapitalize="none"
                    />

                    <TextInput
                        className="border-[1px] p-3 rounded-md font-normal"
                        placeholder={t('last_name')}
                        value={params.lastName}
                        onChangeText={(e) => setParams({...params, lastName: e})}
                        autoCapitalize="none"
                    />

                    <TextInput
                        className="border-[1px] p-3 rounded-md font-normal"
                        placeholder={t('email')}
                        value={params.email}
                        onChangeText={(e) => setParams({...params, email: e})}
                        autoCapitalize="none"
                    />

                    <Pressable className="bg-blue-600 p-4 rounded-md items-center mt-3" onPress={handleRegister}>
                        <Text className="text-[#fff] text-xl font-semibold">{t('register')}</Text>
                    </Pressable>
                </View>
            </SafeAreaView>
    );
};