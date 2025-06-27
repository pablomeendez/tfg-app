import { useContext } from 'react';
import { View, Text, TouchableOpacity, Alert } from 'react-native';
import { AuthContext } from '../../context/AuthContext';
import { useRouter } from 'expo-router';

export default function Profile() {

    const { logout } = useContext(AuthContext);
    const router = useRouter();

    const handleLogout = async () => {
        try {
            await logout();
            router.replace('/(auth)');
        } catch (e) {
            Alert.alert('Error', 'Could not logout');
        }
    }

    return (
        <View className="flex flex-row-reverse">
            <View className="">
                <TouchableOpacity className="bg-red-500 rounded-2xl p-1 m-1" onPress={handleLogout}>
                    <Text className="text-xl">Logout</Text>
                </TouchableOpacity>
            </View>
        </View>
    )
}