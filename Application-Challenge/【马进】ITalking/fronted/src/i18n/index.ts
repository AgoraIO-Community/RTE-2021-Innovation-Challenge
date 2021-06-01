import i18n from 'i18next'
import LanguageDetector from 'i18next-browser-languagedetector'

import en from './translations/en.json'
import zh from './translations/zh.json'
import { initReactI18next } from 'react-i18next'

export const resources = {
  en: {
    translation: en
  },
  zh: {
    translation: zh
  }
} as const

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    lng: 'zh',
    fallbackLng: 'en',
    debug: true,
    resources,
    interpolation: {
      escapeValue: false
    }
  })

export default i18n
