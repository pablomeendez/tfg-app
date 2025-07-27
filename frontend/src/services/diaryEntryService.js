import apiClient  from "./apiClient";

const diaryEntryService = {
    createDiaryEntry: () => {
        return apiClient.post('/diaryEntry/create');
    },
    updateDiaryEntry: (diaryEntryId) => {
        return apiClient.put(`/diaryEntry/${diaryEntryId}`);
    },

    deleteDiaryEntry: (diaryEntryId) => {
        return apiClient.delete(`/diaryEntry/?diaryEntryId=${diaryEntryId}`);
    },

    getDiaryEntries: (userId) => {
        return apiClient.get(`/diaryEntries/?userId=${userId}`);
    },
}

export default diaryEntryService;   