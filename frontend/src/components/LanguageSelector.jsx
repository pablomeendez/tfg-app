import { useState, useEffect } from 'react';
import { View, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTranslation } from 'react-i18next';
import DropDownPicker from 'react-native-dropdown-picker';
import AsyncStorage from '@react-native-async-storage/async-storage';

const LanguageSelector = ({ currentLanguage, onLanguageChange }) => {
    const { t, i18n } = useTranslation();
    const [languageOpen, setLanguageOpen] = useState(false);
    const [selectedLanguage, setSelectedLanguage] = useState(currentLanguage || i18n.language);
    const [languageItems] = useState([
        { label: 'Español', value: 'es', icon: () => <Text>🇪🇸</Text> },
        { label: 'English', value: 'en', icon: () => <Text>🇺🇸</Text> },
        { label: 'Galego', value: 'gl', icon: () => <Text>🇪🇸</Text> }
    ]);

    const handleLanguageChange = async (value) => {
        if (value) {
            setSelectedLanguage(value);
            await i18n.changeLanguage(value);
            await AsyncStorage.setItem('userLanguage', value);
            onLanguageChange && onLanguageChange(value);
        }
    };

    return (
        <View className="bg-white rounded-lg p-4 mb-3" style={{ zIndex: 1000 }}>
            <View className="flex-row items-center mb-3">
                <MaterialCommunityIcons name="translate" size={24} color="#F59E0B" />
                <Text className="text-gray-500 text-sm ml-3">{t('language')}</Text>
            </View>
            <DropDownPicker
                open={languageOpen}
                value={selectedLanguage}
                items={languageItems}
                setOpen={setLanguageOpen}
                setValue={setSelectedLanguage}
                onChangeValue={handleLanguageChange}
                placeholder={t('select_language')}
                style={{
                    backgroundColor: '#F9FAFB',
                    borderColor: '#D1D5DB',
                    borderRadius: 8,
                    minHeight: 45,
                }}
                textStyle={{
                    color: '#374151',
                    fontSize: 16,
                    fontWeight: '500',
                }}
                dropDownContainerStyle={{
                    backgroundColor: '#FFFFFF',
                    borderColor: '#D1D5DB',
                    borderRadius: 8,
                    marginTop: 5,
                }}
                selectedItemContainerStyle={{
                    backgroundColor: '#EBF4FF',
                }}
                selectedItemLabelStyle={{
                    color: '#1D4ED8',
                    fontWeight: '600',
                }}
                zIndex={1000}
                zIndexInverse={3000}
                listMode="SCROLLVIEW"
                scrollViewProps={{
                    nestedScrollEnabled: true,
                }}
            />
        </View>
    );
};

export default LanguageSelector;
