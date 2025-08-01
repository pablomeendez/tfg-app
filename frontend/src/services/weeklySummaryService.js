import apiClient from "./apiClient";

const weeklySummaryService = {
    getWeeklySummaryByUserId: async (userId) => {
        return await apiClient.get(`/weeklySummary/user?userId=${userId}`);
    }
};

export default weeklySummaryService;