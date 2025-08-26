import apiClient from "./apiClient";

const weeklySummaryService = {
    getWeeklySummaryByUser: async (page, size) => {
        return await apiClient.get(`/weekly-summary?page=${page}&size=${size}`);
    }
};

export default weeklySummaryService;