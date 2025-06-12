import { Text, View, ScrollView, Button } from 'react-native';
import { Link } from 'expo-router';

export default function Index() {
  return (
    <ScrollView>
      <View className="flex flex-col bg-gray-300 h-28 rounded-md m-3 shadow items-center">
        <Text className="text-xl pt-2">
          ¿Cómo has estado hoy?
        </Text>
        <View className="flex flex-row rounded-xl mt-2">
        <View className="bg-white rounded-xl m-1">
            <Button 
              title="😃"
              onPress={() => {
                
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
  );
} 