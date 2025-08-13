import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import * as Localization from 'expo-localization';
import AsyncStorage from '@react-native-async-storage/async-storage';
import translationEs from './locales/es/translation.json';
import translationEn from './locales/en/translation.json';
import translationGl from './locales/gl/translation.json';

const resources = {
  es: { translation: translationEs },
  en: { translation: translationEn },
  gl: { translation: translationGl },
};

const initI18n = async () => {
  let savedLanguage = null;
  try {
    savedLanguage = await AsyncStorage.getItem('userLanguage');
  } catch (error) {
    console.log('Error loading saved language:', error);
  }

  const lng = savedLanguage || Localization.getLocales()[0].languageCode || 'es';

  i18n
    .use(initReactI18next)
    .init({
      resources,
      lng: lng,
      fallbackLng: 'es',
      supportedLngs: ['es', 'en', 'gl'],
      interpolation: { escapeValue: false },
      initImmediate: false,
      compatibilityJSON: 'v3',
    });
};

initI18n();

export default i18n;