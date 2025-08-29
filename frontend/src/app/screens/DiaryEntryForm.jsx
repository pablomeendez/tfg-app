import { useContext, useEffect, useState, useCallback, useMemo } from "react";
import { Text, TouchableOpacity, View, ScrollView, Alert } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { Link, useLocalSearchParams, useRouter } from "expo-router";
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useTranslation } from "react-i18next";
import { AuthContext } from "../../context/AuthContext";
import habitService from "../../services/habitService";
import diaryEntryService from "../../services/diaryEntryService";
import { LoadingComponent } from "../../components/common/LoadingComponent";
import { ErrorComponent } from "../../components/common/ErrorComponent";
import FormSection from "../../components/common/FormSection";
import DescriptionInput from "../../components/diary/DescriptionInput";
import ImageSelector from "../../components/diary/ImageSelector";
import MoodDisplay from "../../components/diary/MoodDisplay";
import HabitSelector from "../../components/habits/HabitSelector";
import { MaterialCommunityIcons } from "@expo/vector-icons";

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
    const { t, i18n } = useTranslation();
    const language = i18n.language;

    const mood = {
        id: moodId,
        name: moodName ? JSON.parse(moodName) : null,
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

    const handleSubmit = async () => {
        setLoading(true);
        try {
            const createdEntry = await diaryEntryService.createDiaryEntry(description, selectedImages, mood, selectedHabits);
            if (createdEntry.data.habitEntries.some(entry => entry.userTrophy)) {
                Alert.alert(
                    t('congratulations'),
                    t('you_have_earned_a_new_trophy'),
                    [{ text: t('ok'), style: 'default' }]
                );
            }
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

                    <MoodDisplay mood={mood} language={language} t={t} />

                    <FormSection title={t("description")} icon="text">
                        <DescriptionInput 
                            description={description}
                            setDescription={setDescription}
                            placeholder="What's on your mind today? Share your thoughts and feelings..."
                            t={t}
                        />
                    </FormSection>

                    <FormSection title={`${t("images")} (${selectedImages.length}/3)`} icon="image-multiple">
                        <ImageSelector 
                            images={selectedImages}
                            setImages={setSelectedImages}
                            t={t}
                        />
                    </FormSection>

                    <FormSection title={t("todays_habits")} icon="calendar-check">
                        <HabitSelector 
                            myHabits={myHabits}
                            selectedHabits={selectedHabits}
                            setSelectedHabits={setSelectedHabits}
                            t={t}
                        />
                    </FormSection>

                    <View className="mb-8">
                        <TouchableOpacity 
                            className="bg-green-500 rounded-xl p-4 shadow-lg mt-0 items-center justify-center"
                            onPress={handleSubmit}
                            disabled={loading}
                        >
                            <Text className="text-white text-lg font-semibold">
                                {loading ? t("saving") : t("save_diary_entry")}
                            </Text>
                        </TouchableOpacity>
                    </View>
                </View>
            </ScrollView>}
        </SafeAreaView>
    );
};

export default DiaryEntryForm;