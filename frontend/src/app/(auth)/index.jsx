import { useState, useContext } from 'react';
import {
  View,
  Text,
  Alert,
  Pressable,
  TouchableOpacity
} from 'react-native';
import { useRouter } from 'expo-router';
import { AuthContext } from '../../context/AuthContext';
import userService from '../../services/userService';
import { useTranslation } from 'react-i18next';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { SafeAreaView } from 'react-native-safe-area-context';
import AuthInput from '../../components/auth/AuthInput';
import PasswordInput from '../../components/auth/PasswordInput';

export default function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useContext(AuthContext);
  const router = useRouter();
  const { t } = useTranslation();

  const handleLogin = async () => {
    if (!username.trim() || !password.trim()) {
      Alert.alert(t('error'), t('enter_credentials'));
      return;
    }

    try {
      const response = await login(username, password);
      const user = response.data.user;
      if (user.firstEntry) {
        user.firstEntry = false;
        const updateResponse = await userService.updateProfile(user.id, user);
        router.replace('../screens/HabitForm');
      } else {
        router.replace('/(tabs)');
      }
    } catch (e) {
      Alert.alert(t('error'), t('invalid_credentials'));
    }
  }

  return (
    <SafeAreaView className="flex-1 bg-gray-50">
      <View className="bg-white px-6 py-8">
        <View className="items-center">
          <View className="bg-blue-100 w-16 h-16 rounded-full items-center justify-center mb-4">
            <MaterialCommunityIcons name="account-circle" size={32} color="#3B82F6" />
          </View>
          <Text className="text-2xl font-bold text-gray-800">{t('welcome_back')}</Text>
          <Text className="text-gray-600 mt-1">{t('sign_in_to_continue')}</Text>
        </View>
      </View>

      {/* Form */}
      <View className="flex-1 px-6 py-8">
        <View className="bg-white rounded-xl p-6 border border-gray-100">
          <View className="space-y-4">
            <AuthInput
              placeholder={t('username')}
              value={username}
              onChangeText={setUsername}
            />

            <PasswordInput
              placeholder={t('password')}
              value={password}
              onChangeText={setPassword}
            />

            <TouchableOpacity 
              className="bg-blue-600 rounded-lg items-center justify-center p-4 mt-6"
              onPress={handleLogin}
            >
              <Text className="text-white text-lg font-semibold">{t('login')}</Text>
            </TouchableOpacity>
          </View>
        </View>

        <View className="mt-8 items-center">
          <Pressable 
            className="flex-row items-center" 
            onPress={() => {router.navigate('/register')}}
          >
            <Text className="text-gray-600">{t('dont_have_account')} </Text>
            <Text className="text-blue-600 font-semibold">{t('register_here')}</Text>
          </Pressable>
        </View>
      </View>
    </SafeAreaView>
  );
};
