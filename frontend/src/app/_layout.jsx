import { Stack, usePathname, useRouter } from 'expo-router';
import { AuthProvider, AuthContext } from '../context/AuthContext';
import { useContext, useEffect, useRef } from 'react';
import './../../global.css';
import { ActivityIndicator, View } from 'react-native';
import './../i18n/i18n';


function RootLayoutContent() {
    const { isAuthenticated, loading, userToken } = useContext(AuthContext);
    const router = useRouter();
    const pathname = usePathname();
    const hasRedirected = useRef(false);

    useEffect(() => {
        if (!loading && isAuthenticated && !hasRedirected.current) {
            if (pathname === '/' || pathname.startsWith('/(auth)')) {
                hasRedirected.current = true;
                router.replace('/(tabs)');
            }
        }
    }, [isAuthenticated, loading, pathname, router]);

    if (loading) {
        return (
            <View className="flex-1 justify-center items-center">
                <ActivityIndicator size="large" color="#007AFF" />
            </View>
        );
    }

    return ( 
        <Stack screenOptions={{ headerShown: false }}>
            {isAuthenticated ? (
                <Stack.Screen name="(tabs)" />
            ) : (
                <Stack.Screen name="(auth)" />
            )}
        </Stack>
    );
}
export default function RootLayout() {
    return (
        <AuthProvider>
            <RootLayoutContent />
        </AuthProvider>
    );
}
