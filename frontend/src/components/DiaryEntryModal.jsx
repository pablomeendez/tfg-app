import { View, Text, ScrollView, TouchableOpacity, Modal } from 'react-native';
import { Image } from 'expo-image';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import ImageViewer from './ImageViewer';
import { useTranslation } from 'react-i18next';

export const DiaryEntryModal = ({ isVisible, onClose, selectedEntry }) => {
    const { t } = useTranslation();

    return (
        <Modal
                animationType="slide"
                transparent={true}
                visible={isVisible}
                onRequestClose={onClose}
                className="bg-opacity-50 border-inherit"
            >
                <View className="flex-1 justify-end border-inherit">
                    <View className="bg-white rounded-t-3xl max-h-4/5">
                        <View className="p-6">
                            <View className="flex-row items-center justify-between mb-4">
                                <Text className="text-2xl font-bold text-gray-800">{t('diary_entry')}</Text>
                                <TouchableOpacity
                                    onPress={onClose}
                                    className="bg-gray-100 rounded-full p-2"
                                >
                                    <MaterialCommunityIcons name="close" size={24} color="#6B7280" />
                                </TouchableOpacity>
                            </View>

                            {selectedEntry ? (
                                <ScrollView showsVerticalScrollIndicator={false} className="max-h-fit">
                                    <View className="bg-blue-50 rounded-xl p-4 mb-4">
                                        <View className="flex-row items-center">
                                            <MaterialCommunityIcons name="calendar" size={20} color="#3B82F6" />
                                            <Text className="text-blue-800 font-medium ml-2">
                                                {new Date(selectedEntry.date).toLocaleDateString({
                                                    weekday: 'long',
                                                    year: 'numeric',
                                                    month: 'long',
                                                    day: 'numeric'
                                                })}
                                            </Text>
                                        </View>
                                    </View>           
                                    <View className="bg-purple-50 rounded-xl p-4 mb-4">
                                        <View className="flex-row items-center">
                                            <MaterialCommunityIcons name="emoticon" size={20} color="#8B5CF6" />
                                            <Text className="text-purple-800 font-semibold ml-2 mr-4">{t('mood')}:</Text>
                                            <View className="flex-row items-center flex-1">
                                                <View className="bg-yellow-300 rounded-full mr-3">
                                                    <Image 
                                                        source={{ uri: selectedEntry.mood.image }} 
                                                        style={{ width: 32, height: 32 }} 
                                                    />
                                                </View>
                                                <Text className="text-purple-800 font-medium">
                                                    {selectedEntry.mood.name || 'Unknown'}
                                                </Text>
                                            </View>
                                        </View>
                                    </View>

                                    <View className="bg-gray-50 rounded-xl p-4 mb-4">
                                        <Text className="text-gray-800 font-semibold mb-2">{t('description')}:</Text>
                                        <Text className="text-gray-700 leading-6">
                                            {selectedEntry.description || 'No content available'}
                                        </Text>
                                    </View>

                                    {selectedEntry.images && selectedEntry.images.length > 0 && (
                                        <View className="bg-green-50 rounded-xl p-4 mb-4">
                                            <View className="flex-row items-center mb-3">
                                                <MaterialCommunityIcons name="image-multiple" size={20} color="#10B981" />
                                                <Text className="text-green-800 font-semibold ml-2">
                                                    {t('images')} ({selectedEntry.images.length})
                                                </Text>
                                            </View>
                                            <ScrollView horizontal showsHorizontalScrollIndicator={false}>
                                                <View className="flex-row">
                                                    {selectedEntry.images.map((image, index) => (
                                                        <View key={index} className="mr-3">
                                                            <ImageViewer image={image.imageData} />
                                                        </View>
                                                    ))}
                                                </View>
                                            </ScrollView>
                                        </View>
                                    )}

                                    <View className="bg-yellow-50 rounded-xl p-4">
                                        <Text className="text-yellow-800 font-semibold mb-2">{t('entry_details')}:</Text>
                                        <Text className="text-yellow-700 text-sm">
                                            {t('created')}: {new Date(selectedEntry.date).toLocaleString()}
                                        </Text>

                                    </View>
                                </ScrollView>
                            ) : (
                                <View className="items-center py-8">
                                    <MaterialCommunityIcons name="book-open-variant" size={64} color="#D1D5DB" />
                                    <Text className="text-gray-500 text-lg mt-4">{t('no_diary_entries')}</Text>
                                    <Text className="text-gray-400 text-center mt-2">
                                        {t('only_habits_completed')}
                                    </Text>
                                </View>
                            )}
                        </View>
                    </View>
                </View>
            </Modal>
    );
};