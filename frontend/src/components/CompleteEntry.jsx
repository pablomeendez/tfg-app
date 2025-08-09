import { View, Text } from 'react-native';
import { Image } from 'expo-image';
import ImageViewer from './ImageViewer';

export const CompleteEntry = ( {latestEntry} ) => {
    return (
            <View className="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-xl m-4 shadow-lg border border-blue-100">
              <View className="p-6">
                <View className="flex flex-row items-center mb-4">
                  <View className="w-3 h-3 bg-blue-500 rounded-full mr-3"></View>
                  <Text className="text-2xl font-bold text-blue-900">
                    Your Latest Entry
                  </Text>
                </View>
                
                <View className="bg-white rounded-xl p-5 mb-4 shadow-sm border border-gray-100">
                  <Text className="text-gray-900 text-lg leading-6 font-medium mb-4">
                    "{latestEntry.description}"
                  </Text>
                  
                  {latestEntry.mood && (
                    <View className="mb-4 bg-blue-50 rounded-xl p-4 border border-blue-100">
                      <View className="flex flex-row items-center">
                        <View className="w-3 h-3 bg-blue-500 rounded-full mr-3"></View>
                        <Text className="text-blue-900 text-sm font-semibold mr-4">Mood:</Text>
                        <View className="flex flex-row items-center">
                          {latestEntry.mood.image && (
                            <View className="bg-yellow-300 rounded-full mr-2">
                              <Image 
                                source={{ uri: latestEntry.mood.image }} 
                                style={{ width: 24, height: 24 }}
                                contentFit="cover"
                              />
                            </View>
                          )}
                          <Text className="text-gray-800 text-sm font-semibold">
                            {latestEntry.mood.name || `Mood ${latestEntry.mood.id}`}
                          </Text>
                        </View>
                      </View>
                    </View>
                  )}
                  
                  {latestEntry.images && latestEntry.images.length > 0 && (
                    <View className="mb-4">
                      <Text className="text-gray-700 text-sm font-semibold mb-2">📸 Images</Text>
                      <View className="flex flex-row flex-wrap">
                        {latestEntry.images.map((image) => (
                          <View key={image.id} className="mr-2 flex-1" style={{ minWidth: '45%', maxWidth: '48%' }}>
                            <ImageViewer 
                              image={image.imageData}
                            />
                          </View>
                        ))}
                      </View>
                    </View>
                  )}
                  
                  {latestEntry.habits && latestEntry.habits.length > 0 && (
                    <View className="mb-4">
                      <Text className="text-gray-700 text-sm font-semibold mb-2">✅ Completed Habits</Text>
                      <View className="space-y-2">
                        {latestEntry.habits.map((habit, index) => (
                          <View key={index} className="flex flex-row items-center bg-green-50 rounded-lg p-3 border border-green-100">
                            <View className="w-3 h-3 bg-green-500 rounded-full mr-3"></View>
                            <Text className="text-green-800 text-sm font-medium flex-1">
                              {habit.name || habit.description || `Habit ${habit.id}`}
                            </Text>
                            {habit.streak && (
                              <View className="bg-green-200 rounded-full px-2 py-1">
                                <Text className="text-green-800 text-xs font-semibold">
                                  {habit.streak} day streak
                                </Text>
                              </View>
                            )}
                          </View>
                        ))}
                      </View>
                    </View>
                  )}
                  
                  <View className="border-t border-gray-100 pt-4">
                    <View className="flex flex-row items-center justify-between mb-3">
                      <View className="flex flex-row items-center">
                        <View className="w-2 h-2 bg-gray-400 rounded-full mr-2"></View>
                        <Text className="text-gray-600 text-sm font-medium">
                          {new Date(latestEntry.date).toLocaleDateString('en-US', {
                            weekday: 'long',
                            month: 'long',
                            day: 'numeric',
                            year: 'numeric'
                          })}
                        </Text>
                      </View>
                      
                      <Text className="text-gray-500 text-sm">
                        {new Date(latestEntry.date).toLocaleTimeString({
                          hour: '2-digit',
                          minute: '2-digit'
                        })}
                      </Text>
                    </View>
                  </View>
                </View>
              </View>
            </View>
    );
}