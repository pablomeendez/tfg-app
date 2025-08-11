import { View, Text, TouchableOpacity } from 'react-native';
import { useState } from 'react';
import DropDownPicker from 'react-native-dropdown-picker';
import { useTranslation } from 'react-i18next';

const defaultPickerItems = [
    { label: '5 items per page', value: 5 },
    { label: '10 items per page', value: 10 },
    { label: '20 items per page', value: 20 },
    { label: '30 items per page', value: 30 },
];

export const PageNavigation = ({data, page, setPage, items, setItems}) => {
    const [open, setOpen] = useState(false);
    const [pickerItems, setPickerItems] = useState(defaultPickerItems);
    const { t } = useTranslation();

    return (
        <View className="p-4 border-b border-white rounded-lg bg-white">
            <View className="flex-row items-center">
                {!data.last ? <TouchableOpacity 
                        onPress={() => setPage(page + 1)}
                    className="bg-blue-100 p-2 rounded-lg"
                >
                    <Text className="text-blue-600 font-medium">‹ {t('previous')}</Text>
                </TouchableOpacity> : <View
                    className="bg-gray-100 p-2 rounded-lg"
                >
                    <Text className="text-gray-600 font-medium">‹ {t('previous')}</Text>
                    </View>}
            
                <View className="flex-1 items-center justify-center">
                    <Text className="text-gray-800 font-semibold">
                        {t('page')} {page + 1} {t('of')} {data.totalPages}
                    </Text>
                </View>
            
            {!data.first ? <TouchableOpacity 
                onPress={() => setPage(prev => Math.max(prev - 1, 0))}
                className="bg-blue-100 p-2 rounded-lg"
            >
                <Text className="text-blue-600 font-medium">{t('next')} ›</Text>
            </TouchableOpacity> : <View
                className="bg-gray-100 p-2 rounded-lg"
            >
                <Text className="text-gray-600 font-medium">{t('next')} ›</Text>
                </View>}
        </View>
        <View className="bg-white p-4">
            <Text className="text-gray-700 font-medium mb-2">{t('items_per_page')}</Text>
            <DropDownPicker
                open={open}
                value={items}
                items={pickerItems}
                setOpen={setOpen}
                setValue={setItems}
                setItems={setPickerItems}
                className="bg-gray-50 border border-gray-300 rounded-lg"
                style={{
                    minHeight: 35,
                }}
                textStyle={{
                    color: '#374151',
                    fontSize: 14,
                    fontWeight: '500',
                }}
                dropDownContainerStyle={{
                    backgroundColor: '#FFFFFF',
                    borderColor: '#D1D5DB',
                    borderWidth: 1,
                    borderRadius: 8,
                }}
                selectedItemContainerStyle={{
                    backgroundColor: '#EBF4FF',
                }}
                selectedItemLabelStyle={{
                    color: '#1D4ED8',
                    fontWeight: '600',
                }}
            />
        </View>
    </View>
    );
}