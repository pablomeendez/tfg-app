import React, { useState, useEffect, useMemo } from 'react';
import { View, Text, ScrollView, ActivityIndicator, TouchableOpacity } from 'react-native';
import diaryEntryService from '../../services/diaryEntryService';
import { SafeAreaView } from 'react-native-safe-area-context';
import {DiaryEntryModal} from '../../components/DiaryEntryModal';
import DiaryEntryCard from '../../components/DiaryEntryCard';
import {PageNavigation} from '../../components/PageNavigation';
import { useTranslation } from 'react-i18next';
import { ErrorComponent } from '../../components/ErrorComponent';
import { LoadingComponent } from '../../components/LoadingComponent';

export default function Diary() {
    const [diaryEntries, setDiaryEntries] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [modalVisible, setModalVisible] = useState(false);
    const [selectedEntry, setSelectedEntry] = useState(null);
    const [page, setPage] = useState(0);
    const [items, setItems] = useState(5);
    const { t } = useTranslation();

    useEffect(() => {
        fetchData();
    }, [page, items]);

    const fetchData = useMemo ( () => async () => {
        try {
            setLoading(true);
            const diaryResponse = await diaryEntryService.getDiaryEntries(page, items);
            setDiaryEntries(diaryResponse.data);
            
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }, [page, items]);

    return (
        <SafeAreaView className="flex-1 bg-white">
            {error ? (
                <View>
                    <ErrorComponent error={error} />
                    <TouchableOpacity 
                        onPress={fetchData}
                        className="bg-blue-500 px-4 py-2 rounded-lg"
                    >
                        <Text className="text-white font-medium">{t('retry')}</Text>
                    </TouchableOpacity>
                </View>
            ) : loading ? 
                <LoadingComponent />
            :
            <View className="flex-1">
                <PageNavigation
                    data={diaryEntries}
                    page={page}
                    setPage={setPage}
                    items={items}
                    setItems={setItems}
                />

                <ScrollView className="flex-1 px-4 mt-4">
                    {diaryEntries.totalElements > 0 ? (
                        diaryEntries.content.map(entry => (
                            <DiaryEntryCard
                                key={entry.id}
                                entry={entry}
                                onPress={() => {
                                    setSelectedEntry(entry);
                                    setModalVisible(true);
                                }}
                            />
                        ))
                    ) : (
                        <View className="bg-white rounded-lg p-6 mt-4">
                            <Text className="text-center text-gray-400 mt-2">
                                {t('no_entries')}
                            </Text>
                        </View>
                    )
                }
                </ScrollView>
                <DiaryEntryModal
                    isVisible={modalVisible}
                    onClose={() => setModalVisible(false)}
                    selectedEntry={selectedEntry}
                />
            </View>
            }
        </SafeAreaView>
    );
}