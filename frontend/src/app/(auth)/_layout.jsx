import { Stack } from 'expo-router';
import { Modal } from 'react-native';

export default function AuthLayout() {
    return (
        <Stack screenOptions={{ headerShown: false }}>
            <Stack.Screen name="index" />
            <Stack.Screen name="register" />
        </Stack>
    );
} 