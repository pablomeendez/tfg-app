import { useContext, useEffect, useState, useCallback, useMemo } from "react";
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
import { Image } from "expo-image";
import { LoadingComponent } from "../../components/LoadingComponent";
import { useTranslation } from "react-i18next";

const DiaryEntryForm = () => {
    const { userId } = useContext(AuthContext);
    const [myHabits, setMyHabits] = useState([]);
    const [description, setDescription] = useState("");
    const [selectedImages, setSelectedImages] = useState([]);
    const [selectedHabits, setSelectedHabits] = useState([]);
    const [loading, setLoading] = useState(false);
    const localParams = useLocalSearchParams();
    const moodId = localParams.moodId;
    const moodName = localParams.moodName;
    const moodImage = localParams.moodImage;
    const [error, setError] = useState(null);
    const { t } = useTranslation();

    const mood = {
        id: moodId,
        name: moodName,
        image: moodImage
    }

    const router = useRouter();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await habitService.getHabitsByUser();
                setMyHabits(response.data || []);
                 
            } catch (error) {
                console.error("Error fetching data:", error);
                setError(error.message);
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

    const toggleHabitSelection = useCallback((userHabit) => {
        setSelectedHabits(prev => {
            if (prev.includes(userHabit)) {
                return prev.filter(uh => uh.id !== userHabit.id);
            } else {
                return [...prev, userHabit];
            }
        });
    }, []);

    const habitItems = useMemo(() => {
        return myHabits.map((userHabit) => {
            const isSelected = selectedHabits.includes(userHabit);
            return (
                <TouchableOpacity
                    key={userHabit.id} 
                    onPress={() => toggleHabitSelection(userHabit)}
                    className={`rounded-xl border-2 m-2 ${
                        isSelected
                            ? 'border-blue-500 bg-blue-50'
                            : 'border-gray-200 bg-gray-50'
                    }`}
                    activeOpacity={0.7}
                >
                    <View className="flex-row items-center justify-between">
                        <View className="flex-1 mr-3">
                            <HabitCard habit={userHabit.habit} />
                        </View>
                        <View className="p-1">
                            <MaterialCommunityIcons 
                                name={isSelected ? "check-circle" : "circle-outline"} 
                                size={28} 
                                color={isSelected ? "#3B82F6" : "#9CA3AF"} 
                            />
                        </View>
                    </View>
                </TouchableOpacity>
            );
        });
    }, [myHabits, selectedHabits, toggleHabitSelection]);

    const handleSubmit = async () => {
        setLoading(true);
        try {
            await diaryEntryService.createDiaryEntry(description, selectedImages, mood, selectedHabits);
            
            router.replace('/(tabs)');
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <SafeAreaView className="flex-1 bg-gray-50">
            { error ? 
                <ErrorComponent error={error} /> 
                : loading ? 
                <LoadingComponent /> :
            <ScrollView className="flex-1" showsVerticalScrollIndicator={false}>
                <View className="px-6 py-4">

                    <View className="flex-row items-center justify-between mb-6">
                        <Link href="/(tabs)" className="p-2">
                            <MaterialCommunityIcons name="arrow-left" size={28} color="#374151" />
                        </Link>
                        <Text className="text-2xl font-bold text-gray-800">{t("new_diary_entry")}</Text>
                        <View className="w-10" />
                    </View>

                    {mood.id && (
                        <View className="bg-white rounded-xl p-5 mb-6 shadow-sm border border-gray-100">
                            <Text className="text-xl font-semibold text-gray-800 mb-4 flex-row items-center">
                                <MaterialCommunityIcons name="emoticon-happy" size={24} color="#6B7280" className="mr-2" />
                                {t("selected_mood")}
                            </Text>
                            
                            <View className="bg-gray-50 rounded-lg p-4 border border-blue-100">
                                <View className="flex flex-row items-center">
                                    <View className="bg-yellow-300 rounded-full mr-4">
                                        <Image 
                                            source={{ uri: mood.image }} 
                                            style={{ width: 40, height: 40 }} 
                                        />
                                    </View>
                                    <View className="flex-1">
                                        <Text className="text-lg font-semibold text-gray-800 mb-1">
                                            {mood.name}
                                        </Text>
                                    </View>
                                </View>
                            </View>
                        </View>
                    )}

                    <View className="bg-white rounded-xl p-5 mb-6 shadow-sm border border-gray-100">
                        <Text className="text-xl font-semibold text-gray-800 mb-4 flex-row items-center">
                            <MaterialCommunityIcons name="text" size={24} color="#6B7280" className="mr-2" />
                            {t("description")}
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
                            {t("images")} ({selectedImages.length}/3)
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
                                {selectedImages.length >= 3 ? t("maximum_images_reached") : t("add_image")}
                            </Text>
                        </TouchableOpacity>
                    </View>

                    <View className="bg-white rounded-xl p-5 mb-6 shadow-sm border border-gray-100">
                        <Text className="text-xl font-semibold text-gray-800 mb-4 flex-row items-center">
                            <MaterialCommunityIcons name="calendar-check" size={24} color="#6B7280" className="mr-2" />
                            {t("todays_habits")}
                        </Text>
                        
                        {myHabits.length > 0 ? (
                            <View className="space-y-3">
                                {habitItems}
                            </View>
                        ) : (
                            <View className="bg-gray-50 rounded-lg p-6 items-center">
                                <MaterialCommunityIcons name="calendar-outline" size={48} color="#9CA3AF" />
                                <Text className="text-gray-500 text-center mt-3 text-base">
                                    {t("no_habits_today")}
                                </Text>
                                <Text className="text-gray-400 text-center mt-1 text-sm">
                                    {t("add_some_habits")}
                                </Text>
                            </View>
                        )}
                    </View>

                    <View className="mb-8">
                        <TouchableOpacity 
                            onPress={handleSubmit}
                            className={`rounded-xl p-4 shadow-lg ${
                                loading 
                                    ? 'bg-gray-400' 
                                    : 'bg-green-500 active:bg-green-600'
                            }`}
                            activeOpacity={0.8}
                            disabled={loading}
                        >
                            <View className="flex-row items-center justify-center">
                                {loading ? (
                                    <MaterialCommunityIcons name="loading" size={24} color="white" style={{ marginRight: 8 }} />
                                ) : (
                                    <MaterialCommunityIcons name="content-save" size={24} color="white" style={{ marginRight: 8 }} />
                                )}
                                <Text className="text-white font-bold text-lg">
                                    {loading ? t("saving") : t("save_diary_entry")}
                                </Text>
                            </View>
                        </TouchableOpacity>
                    </View>
                </View>
            </ScrollView>}
        </SafeAreaView>
    );
};

export default DiaryEntryForm;