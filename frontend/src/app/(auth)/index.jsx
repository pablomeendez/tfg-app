import { useState, useContext } from 'react';
import {
  View,
  Text,
  TextInput,
  Alert,
  Pressable,
} from 'react-native';
import { useRouter } from 'expo-router';
import { AuthContext } from '../../context/AuthContext';
import useTogglePasswordVisibility from '../../hooks/useTogglePasswordVisibility';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import userService from '../../services/userService';
import { useTranslation } from 'react-i18next';

export default function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useContext(AuthContext);
  const router = useRouter();
  const { passwordVisibility, eyeIcon, handlePasswordVisibility } = useTogglePasswordVisibility();
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
        router.replace('/screens/HabitForm');
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
          <TextInput
            className="border-[1px] p-3 rounded-md font-normal"
            placeholder={t('username')}
            value={username}
            onChangeText={setUsername}
            autoCapitalize="none"
          />

          <View className="flex-row items-center border-[1px] rounded-md font-normal">
            <TextInput
                className="flex-1 p-3"
                placeholder={t('password')}
                value={password}
                onChangeText={setPassword}
                secureTextEntry={passwordVisibility}
                autoCapitalize="none"
            />
            <Pressable className="pr-3" onPress={handlePasswordVisibility}>
                <MaterialCommunityIcons name={eyeIcon} size={18} color="#232323" />
            </Pressable>
          </View>

          <Pressable className="bg-blue-600 p-4 rounded-md items-center mt-3" onPress={handleLogin}>
            <Text className="text-[#fff] text-xl font-semibold">{t('login')}</Text>
          </Pressable>

          <Pressable className="items-center mt-1" onPress={() => {router.navigate('/register')}}>
            <Text className="underline"> {t('not_registered')} </Text>
          </Pressable>
        </View>
      </View>
    </View>
  );
};
