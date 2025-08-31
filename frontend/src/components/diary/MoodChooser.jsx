import { View, Text } from "react-native";
import MoodItem from "./MoodItem";
import { useTranslation } from "react-i18next";
import { useRouter } from "expo-router";

const MoodChooser = ({ moods }) => {

    const { t } = useTranslation();
    const router = useRouter();

    return (
        <View className="bg-white rounded-xl p-5 mb-4 shadow-sm border border-gray-100">
                <Text className="text-center text-gray-600 text-sm mb-4 font-medium">
                  {t('choose_your_mood')}
                </Text>
                <View className="flex flex-row flex-wrap justify-center items-center">
                  {moods.length > 0 ? (
                    moods.map((mood) => (
                      <View className="m-2" key={mood.id}>
                        <MoodItem
                          mood={mood}
                          style="dailyCheckIn"
                          onPress={() => {
                            router.push({
                              pathname: "screens/DiaryEntryForm",
                              params: { 
                                moodId: mood.id, 
                                moodName: JSON.stringify(mood.name), 
                                moodImage: mood.image 
                              }
                            });
                          }}
                        />
                      </View>
                    ))
                  ) : (
                    <View className="bg-gray-50 rounded-lg p-4 border border-gray-200">
                      <Text className="text-gray-600 text-center">{t('no_moods_available')}</Text>
                    </View>
                  )}
                </View>
              </View>
    )
}

export default MoodChooser;