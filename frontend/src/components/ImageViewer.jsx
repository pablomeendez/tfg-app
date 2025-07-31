import React from "react";
import { View, Image, TouchableOpacity } from "react-native";
import { MaterialCommunityIcons } from '@expo/vector-icons';

const ImageViewer = ({ image, onRemove, index }) => {
    return (
        <View className="relative m-1">
            <Image
                source={{ uri: `data:image/jpeg;base64,${image}` }}
                style={{
                    width: 100,
                    height: 100,
                    borderRadius: 8,
                }}
                resizeMode="cover"
            />
            {onRemove && (
                <TouchableOpacity 
                    onPress={() => onRemove(index)}
                    className="absolute -top-2 -right-2 bg-red-500 rounded-full w-6 h-6 items-center justify-center"
                >
                    <MaterialCommunityIcons name="close" size={16} color="white" />
                </TouchableOpacity>
            )}
        </View>
    );
}

export default ImageViewer;