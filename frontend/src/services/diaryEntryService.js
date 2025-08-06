import apiClient  from "./apiClient";

const diaryEntryService = {
    createDiaryEntry: (description, images, moodId) => {
        return apiClient.post('/diaryEntry/create', {description: description, images: images, moodId: moodId});
    },
    updateDiaryEntry: (diaryEntryId, description, images, moodId) => {
        return apiClient.put(`/diaryEntry/${diaryEntryId}`, {description: description, images: images, moodId: moodId});
    },

    deleteDiaryEntry: (diaryEntryId) => {
        return apiClient.delete(`/diaryEntry/?diaryEntryId=${diaryEntryId}`);
    },

    getDiaryEntries: (userId) => {
        return apiClient.get(`/diaryEntry/?userId=${userId}`);
    },
    getAllMoods: () => {
        return apiClient.get('/diaryEntry/moods');
    },
    getLatestDiaryEntry: () => {
        return apiClient.get(`/diaryEntry/latest`);
    }
}

export default diaryEntryService;   