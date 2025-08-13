import { useTranslation } from 'react-i18next';
import { View, Text, TouchableOpacity } from 'react-native';
import i18n from '../app/i18n/i18n';

const DiaryEntryCard = ({ entry, onPress }) => {
    const language = i18n.language;
    const { t } = useTranslation();

    return (
        <View className="flex-1" key={entry.id}>
            <Text className="text-lg font-semibold text-gray-800 mb-2">
                {new Date(entry.date).toLocaleDateString({
                    weekday: 'long',
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric'
                })}
            </Text>
            <View className="bg-white rounded-lg p-3 mb-3 shadow-sm border border-gray-100">
                <TouchableOpacity 
                    onPress={onPress}
                className="flex-1"
                >   
                <Text className="text-gray-800 font-medium">
                    {entry.description}
                </Text>
                {entry.habitEntries && entry.habitEntries.length > 0 ? (
                    
                    entry.habitEntries.map((habitEntry, index) => (
                        <View key={index} className="flex-1 flex-row gap-4 rounded-lg p-2 mb-1 border-l-4 border-green-500 my-2">
                            <View>
                                <Text className="text-green-800 font-medium">{language === 'en' ? habitEntry.habit.descriptionEn : language === 'es' ? habitEntry.habit.descriptionEs : habitEntry.habit.descriptionGl}</Text>
                                <Text className="text-green-600 text-xs">{language === 'en' ? habitEntry.habit.category.nameEn : language === 'es' ? habitEntry.habit.category.nameEs : habitEntry.habit.category.nameGl}</Text>
                            </View>
                            <View>
                                <Text className="text-green-600">{t('days_streak', { count: habitEntry.streak })}</Text>
                            </View>
                        </View>
                    ))
                ) : (
                    <Text className="text-gray-500 italic">{t('no_habits_completed')}</Text>
                )}
                </TouchableOpacity>
            </View>
        </View>
    )
}

export default DiaryEntryCard;