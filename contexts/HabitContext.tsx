import { createContext, useState, useContext, useEffect } from 'react';
import { Habit, HabitsContextType } from '@/types/habits';
import AsyncStorage from '@react-native-async-storage/async-storage';
import DefaultHabits, { DefaultCategories } from '@/constants/DefaultHabits';

const HabitsContext = createContext<HabitsContextType>({
  habits: [],
  categories: [],
  addHabit: () => {},
  updateHabit: () => {},
  deleteHabit: () => {},
  toggleFavorite: () => {},
  toggleArchive: () => {},
  resetToDefaults: () => {},
});

export const useHabits = () => useContext(HabitsContext);

const STORAGE_KEY = 'habits_data';

export const HabitProvider = ({ children }: { children: React.ReactNode }) => {
  const [habits, setHabits] = useState<Habit[]>([]);
  const [categories, setCategories] = useState<string[]>(DefaultCategories);
  
  // Load habits from storage on app start
  useEffect(() => {
    const loadHabits = async () => {
      try {
        const storedHabits = await AsyncStorage.getItem(STORAGE_KEY);
        
        if (storedHabits) {
          const parsedHabits = JSON.parse(storedHabits);
          // Ensure all habits have isArchived property
          const habitsWithArchive = parsedHabits.map((habit: Habit) => ({
            ...habit,
            isArchived: habit.isArchived || false
          }));
          setHabits(habitsWithArchive);
        } else {
          // If no stored habits, use defaults
          setHabits(DefaultHabits);
          await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(DefaultHabits));
        }
      } catch (error) {
        console.error('Error loading habits from storage:', error);
        // Fallback to defaults if storage fails
        setHabits(DefaultHabits);
      }
    };
    
    loadHabits();
  }, []);
  
  // Save habits to storage whenever they change
  useEffect(() => {
    const saveHabits = async () => {
      try {
        await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(habits));
      } catch (error) {
        console.error('Error saving habits to storage:', error);
      }
    };
    
    if (habits.length > 0) {
      saveHabits();
    }
  }, [habits]);
  
  const addHabit = (habit: Habit) => {
    setHabits(prevHabits => [...prevHabits, { ...habit, isArchived: false }]);
    
    // Add new category if it doesn't exist
    if (!categories.includes(habit.category)) {
      setCategories(prevCategories => [...prevCategories, habit.category]);
    }
  };
  
  const updateHabit = (updatedHabit: Habit) => {
    setHabits(prevHabits => 
      prevHabits.map(habit => 
        habit.id === updatedHabit.id ? updatedHabit : habit
      )
    );
  };
  
  const deleteHabit = (id: string) => {
    setHabits(prevHabits => prevHabits.filter(habit => habit.id !== id));
  };
  
  const toggleFavorite = (id: string) => {
    setHabits(prevHabits => 
      prevHabits.map(habit => 
        habit.id === id
          ? { ...habit, isFavorite: !habit.isFavorite }
          : habit
      )
    );
  };

  const toggleArchive = (id: string) => {
    setHabits(prevHabits => 
      prevHabits.map(habit => 
        habit.id === id
          ? { ...habit, isArchived: !habit.isArchived }
          : habit
      )
    );
  };
  
  const resetToDefaults = () => {
    setHabits(DefaultHabits);
    setCategories(DefaultCategories);
  };
  
  const value = {
    habits,
    categories,
    addHabit,
    updateHabit,
    deleteHabit,
    toggleFavorite,
    toggleArchive,
    resetToDefaults,
  };
  
  return (
    <HabitsContext.Provider value={value}>
      {children}
    </HabitsContext.Provider>
  );
};