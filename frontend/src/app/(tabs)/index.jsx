import { Text, View, ScrollView, Button } from 'react-native';
import { useRouter } from 'expo-router';
import { SafeAreaView } from 'react-native-safe-area-context';

export default function Index() {

  const router = useRouter();

  return (
    <SafeAreaView className="flex-1 bg-white">
      <ScrollView>
        <View className="flex flex-col bg-gray-300 h-28 rounded-md m-3 shadow items-center">
          <Text className="text-xl pt-2">
            ¿How have you been?
          </Text>
          <View className="flex flex-row rounded-xl mt-2">
          <View className="bg-white rounded-xl m-1">
              <Button 
                title="😃"
                onPress={() => {
                  router.push({pathname:"/screens/DiaryEntryForm", params: { moodId: 1 } });
                }}
              />
            </View>
            <View className="bg-white rounded-xl m-1">
              <Button 
                title="😊"
                onPress={() => {
                  
                }}
              />
            </View>
            <View className="bg-white rounded-xl m-1">
              <Button 
                title="😑"
                onPress={() => {
                  
                }}
              />
            </View>
            <View className="bg-white rounded-xl m-1">
              <Button 
                title="😞"
                onPress={() => {
                  
                }}
              />
            </View>
            <View className="bg-white rounded-xl m-1">
              <Button 
                title="😭"
                onPress={() => {
                  
                }}
              />
            </View>
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
} 