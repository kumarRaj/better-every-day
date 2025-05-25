export type Habit = {
  id: string;
  title: string;
  description: string;
  category: string;
  isFavorite: boolean;
  isArchived: boolean;
  createdAt: string;
};

export type HabitsContextType = {
  habits: Habit[];
  categories: string[];
  addHabit: (habit: Habit) => void;
  updateHabit: (habit: Habit) => void;
  deleteHabit: (id: string) => void;
  toggleFavorite: (id: string) => void;
  toggleArchive: (id: string) => void;
  resetToDefaults: () => void;
};