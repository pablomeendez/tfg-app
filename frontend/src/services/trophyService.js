import apiClient from "./apiClient";

const trophyService = {
    getAllTrophies: async () => {
        return await apiClient.get("/trophies");
    },
    getTrophiesByUser: async (userId) => {
        return await apiClient.get(`/trophies/userTrophy?userId=${userId}`);
    },
    getTrophiesByUserAndHabit: async (userId, habitId) => {
        return await apiClient.get(`/trophies/userTrophy/habit?userId=${userId}&habitId=${habitId}`);
    }
};

export default trophyService;
