import apiClient from "./apiClient";

const trophyService = {
    getAllTrophies: async () => {
        return await apiClient.get("/trophies");
    },
    getTrophiesByUser: async () => {
        return await apiClient.get(`/trophies/user-trophy`);
    },
    getTrophiesByUserAndHabit: async (habitId) => {
        return await apiClient.get(`/trophies/user-trophy/habit?habitId=${habitId}`);
    }
};

export default trophyService;
