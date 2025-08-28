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
    <View className="flex-1">
      <View className="flex-1 p-5 justify-center">
        <Text className="text-2xl font-bold mb-5 text-center">Login</Text>

        <View className="gap-3">
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
            className="bg-blue-600 rounded-md items-center justify-center p-4 mt-3"
            onPress={handleLogin}
          >
            <Text className="text-white text-lg font-semibold">{t('login')}</Text>
          </TouchableOpacity>

          <Pressable className="items-center mt-1" onPress={() => {router.navigate('/register')}}>
            <Text className="underline"> {t('not_registered')} </Text>
          </Pressable>
        </View>
      </View>
    </View>
  );
};
