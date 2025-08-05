import apiClient from "./apiClient";

const habitService = {
    getAllHabits: () => {
        return apiClient.get('/habits/all');
    },
    createUserHabit: (habitId) => {
        return apiClient.post('/habits/userHabit', habitId);
    },
    deleteUserHabit: (userHabitId) => {
        return apiClient.delete(`/habits/userHabit/${userHabitId}`);
    },
    getHabitsByUser: (userId) => {
        return apiClient.get(`/habits/?userId=${userId}`);
    },
    createHabitEntry: (userHabitId, diaryEntryId) => {
        return apiClient.post('/habits/entry', { userHabitId, diaryEntryId });
    },
    deleteHabitEntry: (habitEntryId) => {
        return apiClient.delete(`/habits/entry/${habitEntryId}`);
    },
    getHabitEntriesByUserAndUserHabit: (userId, habitId) => {
        return apiClient.get(`/habits/entries?habitId=${habitId}`);
    }
}

export default habitService;