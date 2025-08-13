import { create } from 'zustand';

const useStore = create((set) => ({
    moods: [],
    setMoods: (moods) => set({ moods }),
    
    weeklySummaries: [],
    setWeeklySummaries: (weeklySummaries) => set({ weeklySummaries }),

    habits: [],
    setHabits: (habits) => set({ habits }),

    userHabits: [],
    setUserHabits: (userHabits) => set({ userHabits }),

    trophies: [],
    setTrophies: (trophies) => set({ trophies }),

    diaryEntries: [],
    setDiaryEntries: (diaryEntries) => set({ diaryEntries }),
    
}));

export default useStore;