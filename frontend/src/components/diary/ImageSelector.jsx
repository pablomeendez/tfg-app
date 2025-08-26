import { View, TouchableOpacity, Text, Alert } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import * as ImagePicker from 'expo-image-picker';
import ImageViewer from './ImageViewer';

const ImageSelector = ({ images, setImages, t }) => {
    const handleImagePicker = async () => {
        const permissionResult = await ImagePicker.requestMediaLibraryPermissionsAsync();
        if (permissionResult.granted === false) {
            Alert.alert("Permission Required", "Permission to access camera roll is required!");
            return;
        }

        const result = await ImagePicker.launchImageLibraryAsync({
            mediaTypes: ['images'],
            base64: true,
            allowsEditing: true,
            quality: 0.7,
        });
        
        if (!result.canceled) {
            if (images.length < 3) {
                setImages([...images, ...result.assets.map(asset => asset.base64)]);
            }
        }
    };

    const handleRemoveImage = (index) => {
        setImages(images.filter((_, i) => i !== index));
    };

    return (
        <>
            <View className="flex-row flex-wrap mb-4">
                {images.map((image, index) => (
                    <View key={index} className="mr-3 mb-3">
                        <ImageViewer image={image} index={index} onRemove={handleRemoveImage} />
                    </View>
                ))}
            </View>
            
            <TouchableOpacity 
                onPress={handleImagePicker} 
                className="bg-blue-500 active:bg-blue-600 rounded-lg p-4 flex-row items-center justify-center"
                disabled={images.length >= 3}
            >
                <MaterialCommunityIcons 
                    name="camera-plus" 
                    size={20} 
                    color="white" 
                    className="mr-2" 
                />
                <Text className="text-white font-semibold text-base">
                    {images.length >= 3 ? t("maximum_images_reached") : t("add_image")}
                </Text>
            </TouchableOpacity>
        </>
    );
};

export default ImageSelector;
