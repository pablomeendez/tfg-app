import apiClient  from "./apiClient";

const diaryEntryService = {
    createDiaryEntry: (description, images, mood, selectedHabits) => {
        return apiClient.post('/diaryEntry', {description: description, images: images, date: new Date(Date.now()), mood: mood, habits: selectedHabits});
    },
    updateDiaryEntry: (diaryEntryId, description, images, moodId) => {
        return apiClient.put(`/diaryEntry/${diaryEntryId}`, {description: description, images: images, moodId: moodId});
    },

    deleteDiaryEntry: (diaryEntryId) => {
        return apiClient.delete(`/diaryEntry/?diaryEntryId=${diaryEntryId}`);
    },

    getDiaryEntries: (page, size) => {
        return apiClient.get(`/diaryEntry/?page=${page}&size=${size}`);
    },
    getAllMoods: () => {
        return apiClient.get('/diaryEntry/moods');
    },
    getLatestDiaryEntry: () => {
        return apiClient.get(`/diaryEntry/latest`);
    }
}

export default diaryEntryService;   