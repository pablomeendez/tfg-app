import { useContext, useEffect, useState } from "react";
import { AuthContext } from "../../context/AuthContext";
import habitService from "../../services/habitService";
import { Text, TextInput, TouchableOpacity, View, ScrollView, Alert } from "react-native";
import HabitCard from "../../components/HabitCard";
import { Link, useLocalSearchParams, useGlobalSearchParams } from "expo-router";
import { SafeAreaView } from "react-native-safe-area-context";
import * as ImagePicker from 'expo-image-picker';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import ImageViewer from "../../components/ImageViewer";
import { useRouter } from "expo-router";
import AsyncStorage from '@react-native-async-storage/async-storage';
import diaryEntryService from "../../services/diaryEntryService";

const DiaryEntryForm = () => {
    const { userId } = useContext(AuthContext);
    const [myHabits, setMyHabits] = useState([]);
    const localParams = useLocalSearchParams();
    const globalParams = useGlobalSearchParams();
    const moodId = localParams.moodId || globalParams.moodId;
    const [description, setDescription] = useState("");
    const [selectedImages, setSelectedImages] = useState([]);
    const [selectedHabits, setSelectedHabits] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const router = useRouter();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const userIdToUse = userId || (await AsyncStorage.getItem("userId"));
                if (userIdToUse) {
                    const response = await habitService.getHabitsByUser(userIdToUse);
                    setMyHabits(response.data || []);
                }   
            } catch (error) {
                console.error("Error fetching data:", error);
            }
        };
        fetchData();
    }, []);

    const handleImagePicker = async () => {
        const permissionResult = await ImagePicker.requestMediaLibraryPermissionsAsync();
        if (permissionResult.granted === false) {
            Alert.alert("Permission Required", "Permission to access camera roll is required!");
            return;
        }

        const result = await ImagePicker.launchImageLibraryAsync({
            mediaTypes: ['images'],
            base64: true,
            allowsEditing: true,
            quality: 0.7,
        });
        if (!result.canceled) {
            if (selectedImages.length < 3) {
                setSelectedImages([...selectedImages, ...result.assets.map(asset => asset.base64)]);
            } else {
                Alert.alert("Limit Reached", "You can only select up to 3 images.");
            }
        }
    };

    const handleRemoveImage = (index) => {
        setSelectedImages(selectedImages.filter((_, i) => i !== index));
    };

    const toggleHabitSelection = (userHabitId) => {
        setSelectedHabits(prev => {
            if (prev.includes(userHabitId)) {
                return prev.filter(id => id !== userHabitId);
            } else {
                return [...prev, userHabitId];
            }
        });
    };

    const handleSubmit = async () => {
        try {
            const response = await diaryEntryService.createDiaryEntry(description, selectedImages, moodId);
            selectedHabits.forEach(async (userHabitId) => {
                const habitResponse = await habitService.createHabitEntry(userHabitId, response.data.id);
                if (habitResponse.data.userTrophy != null) {
                    Alert.alert("Trophy Earned", `You earned a trophy for completing the habit: ${habitResponse.data.userTrophy.trophy.name}`);
                }
            });
            Alert.alert("Success", "Diary entry saved successfully!");
            router.replace('/(tabs)');
        } catch (error) {
            console.error('Error submitting diary entry:', error);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <SafeAreaView className="flex-1 bg-gray-50">
            <ScrollView className="flex-1" showsVerticalScrollIndicator={false}>
                <View className="px-6 py-4">

                    <View className="flex-row items-center justify-between mb-6">
                        <Link href="/(tabs)" className="p-2">
                            <MaterialCommunityIcons name="arrow-left" size={28} color="#374151" />
                        </Link>
                        <Text className="text-2xl font-bold text-gray-800">New Diary Entry</Text>
                        <View className="w-10" />
                    </View>

                    {moodId && (
                        <View className="bg-white rounded-xl p-4 mb-6 shadow-sm border border-gray-100">
                            <Text className="text-lg font-semibold text-gray-700 mb-2">Selected Mood</Text>
                            <View className="bg-blue-50 rounded-lg p-3">
                                <Text className="text-blue-800 font-medium">{moodId}</Text>
                            </View>
                        </View>
                    )}

                    <View className="bg-white rounded-xl p-5 mb-6 shadow-sm border border-gray-100">
                        <Text className="text-xl font-semibold text-gray-800 mb-4 flex-row items-center">
                            <MaterialCommunityIcons name="text" size={24} color="#6B7280" className="mr-2" />
                            Description
                        </Text>
                        <TextInput 
                            multiline 
                            numberOfLines={4}
                            className="min-h-24 max-h-32 rounded-lg bg-gray-50 p-4 text-gray-700 border border-gray-200 text-base"
                            placeholder="What's on your mind today? Share your thoughts and feelings..."
                            placeholderTextColor="#9CA3AF"
                            onChangeText={setDescription}
                            value={description}
                            textAlignVertical="top"
                        />
                    </View>

                    <View className="bg-white rounded-xl p-5 mb-6 shadow-sm border border-gray-100">
                        <Text className="text-xl font-semibold text-gray-800 mb-4 flex-row items-center">
                            <MaterialCommunityIcons name="image-multiple" size={24} color="#6B7280" className="mr-2" />
                            Images ({selectedImages.length}/3)
                        </Text>
                        
                        <View className="flex-row flex-wrap mb-4">
                            {selectedImages.map((image, index) => (
                                <View key={index} className="mr-3 mb-3">
                                    <ImageViewer image={image} index={index} onRemove={handleRemoveImage} />
                                </View>
                            ))}
                        </View>
                        
                        <TouchableOpacity 
                            onPress={handleImagePicker} 
                            className="bg-blue-500 active:bg-blue-600 rounded-lg p-4 flex-row items-center justify-center"
                            disabled={selectedImages.length >= 3}
                        >
                            <MaterialCommunityIcons 
                                name="camera-plus" 
                                size={20} 
                                color="white" 
                                className="mr-2" 
                            />
                            <Text className="text-white font-semibold text-base">
                                {selectedImages.length >= 3 ? "Maximum images reached" : "Add Image"}
                            </Text>
                        </TouchableOpacity>
                    </View>

                    <View className="bg-white rounded-xl p-5 mb-6 shadow-sm border border-gray-100">
                        <Text className="text-xl font-semibold text-gray-800 mb-4 flex-row items-center">
                            <MaterialCommunityIcons name="calendar-check" size={24} color="#6B7280" className="mr-2" />
                            Today's Habits
                        </Text>
                        
                        {myHabits.length > 0 ? (
                            <View className="space-y-3">
                                {myHabits.map((userHabit) => (
                                    <TouchableOpacity
                                        key={userHabit.id} 
                                        onPress={() => toggleHabitSelection(userHabit.id)}
                                        className={`rounded-xl border-2 transition-colors m-2 ${
                                            selectedHabits.includes(userHabit.id)
                                                ? 'border-blue-500 bg-blue-50'
                                                : 'border-gray-200 bg-gray-50 active:bg-gray-100'
                                        }`}
                                        activeOpacity={0.7}
                                    >
                                        <View className="flex-row items-center justify-between">
                                            <View className="flex-1 mr-3">
                                                <HabitCard habit={userHabit.habit} />
                                            </View>
                                            <View className="p-1">
                                                <MaterialCommunityIcons 
                                                    name={selectedHabits.includes(userHabit.id) ? "check-circle" : "circle-outline"} 
                                                    size={28} 
                                                    color={selectedHabits.includes(userHabit.id) ? "#3B82F6" : "#9CA3AF"} 
                                                />
                                            </View>
                                        </View>
                                    </TouchableOpacity>
                                ))}
                            </View>
                        ) : (
                            <View className="bg-gray-50 rounded-lg p-6 items-center">
                                <MaterialCommunityIcons name="calendar-outline" size={48} color="#9CA3AF" />
                                <Text className="text-gray-500 text-center mt-3 text-base">
                                    No habits registered yet
                                </Text>
                                <Text className="text-gray-400 text-center mt-1 text-sm">
                                    Add some habits to track your progress
                                </Text>
                            </View>
                        )}
                    </View>

                    <View className="mb-8">
                        <TouchableOpacity 
                            onPress={handleSubmit}
                            className="bg-green-500 active:bg-green-600 rounded-xl p-4 shadow-lg"
                            activeOpacity={0.8}
                            disabled={isLoading}
                        >
                            <View className="flex-row items-center justify-center">
                                {isLoading ? (
                                    <MaterialCommunityIcons name="loading" size={24} color="white" className="mr-2" />
                                ) : (
                                    <MaterialCommunityIcons name="content-save" size={24} color="white" className="mr-2" />
                                )}
                                <Text className="text-white font-bold text-lg">
                                    {isLoading ? "Saving..." : "Save Diary Entry"}
                                </Text>
                            </View>
                        </TouchableOpacity>
                    </View>
                </View>
            </ScrollView>
        </SafeAreaView>
    );
};

export default DiaryEntryForm;