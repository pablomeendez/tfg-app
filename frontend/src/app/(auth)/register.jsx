import { useContext, useState } from "react";
import { View, Text, TextInput, TouchableOpacity, Alert, Pressable } from "react-native";
import { AuthContext } from "../../context/AuthContext";
import { useRouter } from "expo-router";
import useTogglePasswordVisibility from '../../hooks/useTogglePasswordVisibility';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { SafeAreaView } from "react-native-safe-area-context";


export default function Register() {

    const [params,  setParams] = useState({username: '', password: '', name: '', lastName: '', email: ''});
    const router = useRouter();
    const { register } = useContext(AuthContext);
    const { passwordVisibility, eyeIcon, handlePasswordVisibility } = useTogglePasswordVisibility();

    const handleRegister = async () => {
        try {
            await register(params.username, params.password, params.name, params.lastName, params.email);
            router.replace('/(auth)');
        } catch (error) {
            Alert(error);
        }
    }
      
    return (
        <SafeAreaView className="flex-1">
            <View className="flex-1 p-5 justify-center gap-2">
                <TouchableOpacity className="absolute left-0 top-0 m-4 z-10" onPress={() => router.back()}>
                    <Text className="text-blue-600 text-lg">Volver</Text>
                </TouchableOpacity>
                <Text className="text-2xl font-bold mb-5 text-center">Register</Text>
                    <TextInput
                        className="border-[1px] p-3 rounded-md font-normal"
                        placeholder="Username"
                        value={params.username}
                        onChangeText={(e) => setParams({...params, username: e})}
                        autoCapitalize="none"
                    />
                    
                    <View className="flex-row items-center border-[1px] rounded-md font-normal">
                        <TextInput
                            className="flex-1 p-3"
                            placeholder="Password"
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
                        placeholder="Name"
                        value={params.name}
                        onChangeText={(e) => setParams({...params, name: e})}
                        autoCapitalize="none"
                    />

                    <TextInput
                        className="border-[1px] p-3 rounded-md font-normal"
                        placeholder="LastName"
                        value={params.lastName}
                        onChangeText={(e) => setParams({...params, lastName: e})}
                        autoCapitalize="none"
                    />

                    <TextInput
                        className="border-[1px] p-3 rounded-md font-normal"
                        placeholder="Email"
                        value={params.email}
                        onChangeText={(e) => setParams({...params, email: e})}
                        autoCapitalize="none"
                    />

                    <Pressable className="bg-blue-600 p-4 rounded-md items-center mt-3" onPress={handleRegister}>
                        <Text className="text-[#fff] text-xl font-semibold">Register</Text>
                    </Pressable>
                </View>
            </SafeAreaView>
    );
};