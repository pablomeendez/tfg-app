import { View} from 'react-native';
import { Link, Stack } from 'expo-router';
import { useTranslation } from 'react-i18next';

export default function NotFoundScreen() {
    const { t } = useTranslation();

    return (
        <>
            <Stack.Screen options={{ title: t('not_found') }}/>
            <View>
                <Link href="/">
                    {t('go_back_home')}
                </Link>
            </View>
        </>
    )
}