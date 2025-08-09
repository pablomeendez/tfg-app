import apiClient from "./apiClient";

const trophyService = {
    getAllTrophies: async () => {
        return await apiClient.get("/trophies");
    },
    getTrophiesByUser: async () => {
        return await apiClient.get(`/trophies/userTrophy`);
    },
    getTrophiesByUserAndHabit: async (habitId) => {
        return await apiClient.get(`/trophies/userTrophy/habit?habitId=${habitId}`);
    }
};

export default trophyService;
