import { Text } from 'react-native';
import { useTranslation } from 'react-i18next';

const MultiLanguageText = ({ 
    textObject, 
    fallback = '', 
    className = '',
    style,
    ...props 
}) => {
    const { i18n } = useTranslation();
    const language = i18n.language;

    const getText = () => {
        if (!textObject) return fallback;
        
        return language === 'en' 
            ? textObject.en 
            : language === 'es' 
                ? textObject.es 
                : textObject.gl || textObject.en || fallback;
    };

    return (
        <Text 
            className={className} 
            style={style}
            {...props}
        >
            {getText()}
        </Text>
    );
};

export default MultiLanguageText;
