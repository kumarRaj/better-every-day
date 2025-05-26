import { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import Animated, { FadeIn, FadeOut, SlideInRight, SlideOutLeft } from 'react-native-reanimated';
import { useHabits } from '@/contexts/HabitContext';
import Colors from '@/constants/Colors';
import Header from '@/components/Header';
import HabitCard from '@/components/HabitCard';
import { Heart, Bookmark } from 'lucide-react-native';
import { usePlatform } from '@/hooks/usePlatform';
import { useHapticFeedback } from '@/hooks/useHapticFeedback';

export default function TodayScreen() {
  const { habits, toggleFavorite } = useHabits();
  const [currentHabit, setCurrentHabit] = useState<Habit | null>(null);
  const [key, setKey] = useState(0);
  const { triggerFeedback } = useHapticFeedback();
  const { isWeb } = usePlatform();

  useEffect(() => {
    if (habits.length > 0) {
      showRandomHabit();
    } else {
      setCurrentHabit(null);
    }
  }, [habits]);

  const showRandomHabit = () => {
    const activeHabits = habits.filter(habit => !habit.isArchived);

    if (activeHabits.length === 0) {
      setCurrentHabit(null);
      return;
    }
    
    let newHabit;
    if (activeHabits.length === 1) {
      newHabit = habits[0];
    } else {
      do {
        const randomIndex = Math.floor(Math.random() * activeHabits.length);
        newHabit = activeHabits[randomIndex];
      } while (newHabit?.id === currentHabit?.id && activeHabits.length > 1);
    }
    
    setKey(prev => prev + 1);
    setCurrentHabit(newHabit);
    triggerFeedback('medium');
  };

  const handleFavorite = () => {
    if (currentHabit) {
      toggleFavorite(currentHabit.id);
      triggerFeedback('light');
    }
  };

  return (
    <SafeAreaView style={styles.container} edges={['top']}>
      <Header title="Better everyday" />
      
      <View style={styles.contentContainer}>
        {currentHabit ? (
          <Animated.View 
            key={key}
            style={styles.cardContainer}
            entering={SlideInRight.springify().damping(15)}
            exiting={SlideOutLeft.springify().damping(15)}
          >
            <HabitCard habit={currentHabit} />
            
            <View style={styles.actionButtons}>
              <TouchableOpacity 
                style={[styles.actionButton, styles.favoriteButton]} 
                onPress={handleFavorite}
              >
                <Heart 
                  size={24} 
                  color={currentHabit.isFavorite ? Colors.accent : Colors.textSecondary} 
                  fill={currentHabit.isFavorite ? Colors.accent : 'transparent'} 
                />
                <Text style={styles.actionButtonText}>
                  {currentHabit.isFavorite ? 'Favorited' : 'Favorite'}
                </Text>
              </TouchableOpacity>
            </View>
          </Animated.View>
        ) : (
          <Animated.View 
            style={styles.emptyState}
            entering={FadeIn.duration(400)}
            exiting={FadeOut.duration(200)}
          >
            <Bookmark size={64} color={Colors.textSecondary} />
            <Text style={styles.emptyStateText}>No habits added yet</Text>
            <Text style={styles.emptyStateSubtext}>
              Add habits in the Library tab to get started
            </Text>
          </Animated.View>
        )}
        
        <TouchableOpacity 
          style={[
            styles.nextButton,
            !habits.length && styles.nextButtonDisabled
          ]} 
          onPress={showRandomHabit}
          disabled={!habits.length}
        >
          <Animated.Text 
            style={[
              styles.nextButtonText,
              !habits.length && styles.nextButtonTextDisabled
            ]}
            entering={FadeIn}
          >
            Show Next Habit
          </Animated.Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.background,
  },
  contentContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 16,
    marginTop: -40,
  },
  cardContainer: {
    width: '100%',
    maxWidth: 400,
    alignSelf: 'center',
  },
  nextButton: {
    backgroundColor: Colors.primary,
    paddingVertical: 16,
    paddingHorizontal: 32,
    borderRadius: 30,
    marginTop: 40,
    shadowColor: Colors.shadow,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.2,
    shadowRadius: 8,
    elevation: 4,
  },
  nextButtonDisabled: {
    backgroundColor: Colors.textTertiary,
    shadowOpacity: 0,
    elevation: 0,
  },
  nextButtonText: {
    color: Colors.white,
    fontSize: 18,
    fontWeight: '600',
    textAlign: 'center',
  },
  nextButtonTextDisabled: {
    color: Colors.white,
    opacity: 0.7,
  },
  actionButtons: {
    flexDirection: 'row',
    justifyContent: 'center',
    marginTop: 24,
  },
  actionButton: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 8,
    paddingHorizontal: 16,
    borderRadius: 20,
    backgroundColor: Colors.cardBackground,
  },
  favoriteButton: {
    marginRight: 12,
  },
  actionButtonText: {
    color: Colors.text,
    fontSize: 16,
    marginLeft: 8,
  },
  emptyState: {
    alignItems: 'center',
    justifyContent: 'center',
    padding: 24,
  },
  emptyStateText: {
    fontSize: 20,
    fontWeight: '600',
    color: Colors.text,
    marginTop: 16,
  },
  emptyStateSubtext: {
    fontSize: 16,
    color: Colors.textSecondary,
    marginTop: 8,
    textAlign: 'center',
  },
});