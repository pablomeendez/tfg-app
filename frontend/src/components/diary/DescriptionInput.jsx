import { TextInput } from 'react-native';

const DescriptionInput = ({ description, setDescription, placeholder, t }) => {
    return (
        <TextInput 
            multiline 
            numberOfLines={4}
            className="min-h-24 max-h-32 rounded-lg bg-gray-50 p-4 text-gray-700 border border-gray-200 text-base"
            placeholder={placeholder || "What's on your mind today? Share your thoughts and feelings..."}
            placeholderTextColor="#9CA3AF"
            onChangeText={setDescription}
            value={description}
            textAlignVertical="top"
        />
    );
};

export default DescriptionInput;
