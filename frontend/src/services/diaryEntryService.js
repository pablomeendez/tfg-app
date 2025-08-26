import apiClient  from "./apiClient";

const diaryEntryService = {
    createDiaryEntry: (description, images, mood, selectedHabits) => {
        return apiClient.post('/diary-entry', {description: description, images: images, mood: mood, habits: selectedHabits});
    },

    getDiaryEntries: (page, size) => {
        return apiClient.get(`/diary-entry?page=${page}&size=${size}`);
    },
    getAllMoods: () => {
        return apiClient.get('/diary-entry/moods');
    },
    getLatestDiaryEntry: () => {
        return apiClient.get(`/diary-entry/latest`);
    }
}

export default diaryEntryService;   