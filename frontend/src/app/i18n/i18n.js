import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import * as Localization from 'expo-localization';
import translationEs from './locales/es/translation.json';
import translationEn from './locales/en/translation.json';
import translationGl from './locales/gl/translation.json';

const resources = {
  es: { translation: translationEs },
  en: { translation: translationEn },
  gl: { translation: translationGl },
};

i18n
  .use(initReactI18next)
  .init({
    resources,
    lng: Localization.getLocales()[0].languageCode || 'es',
    fallbackLng: 'es',
    supportedLngs: ['es', 'en', 'gl'],
    interpolation: { escapeValue: false },
    initImmediate: false,
    compatibilityJSON: 'v3',
  });

export default i18n;