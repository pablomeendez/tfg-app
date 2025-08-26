import apiClient from "./apiClient";

const habitService = {
    getAllHabits: () => {
        return apiClient.get('/habits/all');
    },
    createUserHabit: (habitId) => {
        return apiClient.post('/habits/user-habit', habitId);
    },
    deleteUserHabit: (userHabitId) => {
        return apiClient.delete(`/habits/user-habit/${userHabitId}`);
    },
    getHabitsByUser: () => {
        return apiClient.get(`/habits/user-habit`);
    },
    createHabitEntry: (userHabitId, diaryEntryId) => {
        return apiClient.post('/habits/entry', { userHabitId, diaryEntryId });
    },
    getHabitEntriesByUserAndUserHabit: ( habitId) => {
        return apiClient.get(`/habits/entries?habitId=${habitId}`);
    }
}

export default habitService;